package in.msmartpay.agent.location.test;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.location.ForegroundOnlyLocationService;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.Util;

public class LocationTestActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int GPS_REQUEST = 10101;
    private static final int REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 128;
    private static final String TAG = LocationTestActivity.class.getSimpleName();
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


    private Button foreground_only_location_button;
    private TextView output_text_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = Util.getMyPref(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        foregroundOnlyBroadcastReceiver = new ForegroundOnlyBroadcastReceiver();
        foreground_only_location_button = findViewById(R.id.foreground_only_location_button);
        output_text_view = findViewById(R.id.output_text_view);
        foreground_only_location_button.setOnClickListener(v -> {
            boolean enabled = sharedPreferences.getBoolean(Keys.KEY_FOREGROUND_ENABLED, false);
            if (enabled) {
                foregroundOnlyLocationService.unsubscribeToLocationUpdates();
            } else {
                // TODO: Step 1.0, Review Permissions: Checks and requests if needed.
                if (foregroundPermissionApproved()) {
                    foregroundOnlyLocationService.subscribeToLocationUpdates();
                } else {
                    requestForegroundPermissions();
                }
            }
        });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Write Function To enable gps
            Util.onGPS(this, GPS_REQUEST);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(this, ForegroundOnlyLocationService.class);
        serviceIntent.putExtra("activity", "Home");
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE);
        updateButtonState(Util.LoadPrefBoolean(getApplicationContext(), Keys.KEY_FOREGROUND_ENABLED));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                foregroundOnlyBroadcastReceiver, new IntentFilter(ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        );
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

    // TODO: Step 1.0, Review Permissions: Method checks if permissions approved.
    private Boolean foregroundPermissionApproved() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    // TODO: Step 1.0, Review Permissions: Method requests permissions.
    private void requestForegroundPermissions() {
        boolean provideRationale = foregroundPermissionApproved();

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(findViewById(R.id.activity_main), "permission_rationale", Snackbar.LENGTH_LONG)
                    .setAction("ok", v -> ActivityCompat.requestPermissions(
                            LocationTestActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)).show();
        } else {
            Log.d(TAG, "Request foreground only permission");
            ActivityCompat.requestPermissions(
                    LocationTestActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            );
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        if (foregroundOnlyLocationService != null)
                            foregroundOnlyLocationService.subscribeToLocationUpdates();
                } else {
                    updateButtonState(false);
                    Snackbar.make(findViewById(R.id.activity_main), "permission_denied_explanation", Snackbar.LENGTH_LONG)
                            .setAction("settings", v ->
                                    Util.openSettingsDialog(LocationTestActivity.this, 101)).show();

                }
                break;
        }
    }

    private void updateButtonState(boolean trackingLocation) {
        if (trackingLocation) {
            foreground_only_location_button.setText("stop_location_updates_button_text");
        } else {
            foreground_only_location_button.setText("start_location_updates_button_text");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Keys.KEY_FOREGROUND_ENABLED)) {
            updateButtonState(sharedPreferences.getBoolean(Keys.KEY_FOREGROUND_ENABLED, false));
        }
    }

   /* private void updateLocationState(Boolean trackingLocation) {
        if (trackingLocation) {
        } else {
            Boolean enabled = Util.LoadPrefBoolean(getApplicationContext(), Keys.KEY_FOREGROUND_ENABLED);
            if (enabled) {
                if (foregroundOnlyLocationService != null)
                    foregroundOnlyLocationService.unsubscribeToLocationUpdates();
            } else {
                checkPermissions();
            }
        }
    }*/

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

                            //Check gps is enable or not
                            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                //Write Function To enable gps
                                //if (count == 0)
                                Util.onGPS(LocationTestActivity.this, GPS_REQUEST);
                                /* else if((count.div(2))==0)
                                     this.enableGPS()
                                 count++*/
                            }
                            if (foregroundOnlyLocationService != null)
                                foregroundOnlyLocationService.subscribeToLocationUpdates();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Log.e(TAG, "isAnyPermissionPermanentlyDenied");
                            Util.openSettingsDialog(LocationTestActivity.this, 101);
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
    private class ForegroundOnlyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(ForegroundOnlyLocationService.EXTRA_LOCATION);
            if (location != null) {
                Log.e(TAG, "Foreground location:" + Util.getGson().toJson(location));
                logResultsToScreen("Foreground location:" + location.toString());
            }
        }
    }

    private void logResultsToScreen(String output) {
        String outputWithPreviousLogs = output + "\n";
        output_text_view.append(outputWithPreviousLogs);
    }
}

