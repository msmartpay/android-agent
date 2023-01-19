package in.msmartpay.agent.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.L;

public class NetworkConnection {

    //Check Internet Connection
    public static boolean isConnectionAvailable(Context context) {
        if (!isOnline(context)) {
            return false;
        } else {
            return true;
        }
    }
    //Check Internet Connection
    public static boolean isConnectionAvailable2(Context context) {
        if (!isOnline(context)) {
            L.toastS(context,context.getResources().getString(R.string.no_internet));
            return false;
        } else {
            return true;
        }
    }

    public static boolean isOnline(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        boolean conn =false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        if (haveConnectedWifi || haveConnectedMobile) {
            Log.e("Log-Wifi", String.valueOf(haveConnectedWifi));
            Log.e("Log-Mobile", String.valueOf(haveConnectedMobile));
            conn = true;
        } else {
            Log.e("Log-Wifi", String.valueOf(haveConnectedWifi));
            Log.e("Log-Mobile", String.valueOf(haveConnectedMobile));
            conn = false;
        }

        return conn;
    }

}
