package in.msmartpay.agent.location;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.Util;

public class LocationResultActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int GPS_REQUEST = 10101;
    private static final String TAG = LocationResultActivity.class.getSimpleName();
    private LocationManager locationManager = null;
    private SharedPreferences sharedPreferences;

    private Boolean foregroundOnlyLocationServiceBound = false;
    // Provides location updates for while-in-use feature.
    private ForegroundOnlyLocationService foregroundOnlyLocationService = null;
    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private ForegroundOnlyBroadcastReceiver foregroundOnlyBroadcastReceiver;
    // Monitors connection to the while-in-use service.
    private final ServiceConnection foregroundOnlyServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ForegroundOnlyLocationService.LocalBinder binder = (ForegroundOnlyLocationService.LocalBinder) service;
            foregroundOnlyLocationService = binder.getService();
            foregroundOnlyLocationServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            foregroundOnlyLocationService = null;
            foregroundOnlyLocationServiceBound = false;
        }
    };

    private boolean isGetLocation = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_result_activity);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = Util.getMyPref(getApplicationContext());
        foregroundOnlyBroadcastReceiver = new ForegroundOnlyBroadcastReceiver();
        //Check gps is enable or not
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Util.onGPS(LocationResultActivity.this, GPS_REQUEST);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(this, ForegroundOnlyLocationService.class);
        serviceIntent.putExtra("activity", "Home");
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE);
        updateLocationState(sharedPreferences.getBoolean(Keys.KEY_FOREGROUND_ENABLED, false));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                foregroundOnlyBroadcastReceiver, new IntentFilter(ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        );
        checkPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(foregroundOnlyBroadcastReceiver);
    }

    @Override
    protected void onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection);
            foregroundOnlyLocationServiceBound = false;
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean isTrackingEnable = sharedPreferences.getBoolean(Keys.KEY_FOREGROUND_ENABLED, false);
        if (key.equals(Keys.KEY_FOREGROUND_ENABLED)) {
            updateLocationState(isTrackingEnable);
        }
        if (key.equalsIgnoreCase(Keys.LATITUDE)) {
            isGetLocation = true;
            if (isTrackingEnable) {
                if (foregroundOnlyLocationService != null)
                    foregroundOnlyLocationService.unsubscribeToLocationUpdates();
            }
            setResult(RESULT_OK);
            finish();
        }
    }

    private void updateLocationState(Boolean trackingLocation) {
        if (trackingLocation) {

        } else {
            if (foregroundOnlyLocationService != null && !isGetLocation)
                foregroundOnlyLocationService.subscribeToLocationUpdates();
        }
    }

    private void checkPermissions() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        /*list.add(Manifest.permission.ACCESS_COARSE_LOCATION)*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            //list.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
        Dexter.withContext(getApplicationContext())
                .withPermissions(list)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Log.e(TAG, "areAllPermissionsGranted");
                            boolean isTrackingEnable = sharedPreferences.getBoolean(Keys.KEY_FOREGROUND_ENABLED, false);
                            if (foregroundOnlyLocationService != null && !isTrackingEnable)
                                foregroundOnlyLocationService.subscribeToLocationUpdates();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Log.e(TAG, "isAnyPermissionPermanentlyDenied");
                            Util.openSettingsDialog(LocationResultActivity.this, 101);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError ->
                Log.e(TAG, dexterError.name()))
                .onSameThread()
                .check();
    }

    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     */
    private static class ForegroundOnlyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(ForegroundOnlyLocationService.EXTRA_LOCATION);
            if (location != null) {
                Log.e(TAG, "Foreground location:" + Util.getGson().toJson(location));
            }
        }
    }

    @Override
    public void onBackPressed() {
        boolean isTrackingEnable = sharedPreferences.getBoolean(Keys.KEY_FOREGROUND_ENABLED, false);
        if (foregroundOnlyLocationService != null && isTrackingEnable && !isGetLocation) {
            isGetLocation = true;
            foregroundOnlyLocationService.unsubscribeToLocationUpdates();
        }
        super.onBackPressed();
    }
}
