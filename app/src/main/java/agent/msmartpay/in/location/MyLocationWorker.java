package agent.msmartpay.in.location;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.Keys;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;
import agent.msmartpay.in.utility.Util;

public class MyLocationWorker extends Worker {

    private static final String DEFAULT_START_TIME = "08:00";
    private static final String DEFAULT_END_TIME = "19:00";

    private static final String TAG = "MyLocationWorker";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /**
     * The current location.
     */
    private Location mLocation;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    private Context mContext;
    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    public MyLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Normally, you want to save a new location to a database. We are simplifying
                // things a bit and just saving it as a local variable, as we only need it again
                // if a Notification is created (when the user navigates away from app).
                if (locationResult.getLastLocation() != null) {
                    mLocation = locationResult.getLastLocation();
                    L.m2(TAG, "Location : " + mLocation);
                    saveLocationToServer(mLocation);


                }
            }
        };

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        try {
            mFusedLocationClient
                    .getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();
                            L.m2(TAG, "Location : " + mLocation);
                            saveLocationToServer(mLocation);

							/*// Create the NotificationChannel, but only on API 26+ because
							// the NotificationChannel class is new and not in the support library
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
								CharSequence name = mContext.getString(R.string.app_name);
								String description = mContext.getString(R.string.app_name);
								int importance = NotificationManager.IMPORTANCE_DEFAULT;
								NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.app_name), name, importance);
								channel.setDescription(description);
								// RegisterFragment the channel with the system; you can't change the importance
								// or other notification behaviors after this
								NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
								notificationManager.createNotificationChannel(channel);
							}

							NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.app_name))
									.setSmallIcon(android.R.drawable.ic_menu_mylocation)
									.setContentTitle("New Location Update")
									.setContentText("You are at " + getCompleteAddressString(mLocation.getLatitude(), mLocation.getLongitude()))
									.setPriority(NotificationCompat.PRIORITY_DEFAULT)
									.setStyle(new NotificationCompat.BigTextStyle().bigText("You are at " + getCompleteAddressString(mLocation.getLatitude(), mLocation.getLongitude())));

							NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

							// notificationId is a unique int for each notification that you must define
							notificationManager.notify(1001, builder.build());
*/
                            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                        } else {
                            Log.w(TAG, "Failed to get location.");
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
        } catch (SecurityException unlikely) {
            //Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
			/*} else {
				L.m2(TAG, "Time up to get location. Your time is : " + DEFAULT_START_TIME + " to " + DEFAULT_END_TIME);
			}*/

        return Result.success();
    }

    private void saveLocationToServer(Location mLocation) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonData = new JSONObject();

            jsonObject.put("agent_id", Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID));
            jsonObject.put("txn_key", Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY));
            jsonData.put("latitude", mLocation.getLatitude() + "");
            jsonData.put("longitude", mLocation.getLongitude() + "");
            jsonData.put("location", Util.getCompleteAddressString(getApplicationContext(), mLocation.getLatitude(), mLocation.getLongitude()));
            jsonObject.put("data", jsonData);

            L.m2("Req. save Location", jsonObject.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, HttpURL.LOCATION,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject data) {
                    L.m2("Res. save Location", data.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            BaseActivity.getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(objectRequest);
        } catch (Exception e) {
            L.m2("data failuer", e.toString());
            e.printStackTrace();
        }

    }

    public static void startLocationWorker(Context context) {
        // START Worker
        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(MyLocationWorker.class, 15, TimeUnit.SECONDS)
                .addTag(TAG)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("Location", ExistingPeriodicWorkPolicy.REPLACE, periodicWork);

    }

    public static void cancelLocationWorker(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG);

    }

    public static List<WorkInfo> getWorkInfoList(Context context) throws ExecutionException, InterruptedException {
        return WorkManager.getInstance(context).getWorkInfosByTag(TAG).get();
    }
}