package in.msmartpay.agent.utility;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

/**
 * Created by Harendra on 4/29/2017.
 */

public class BaseActivity extends AppCompatActivity {

    boolean conn = false;
    Builder alertDialog;
    ProgressDialog progressDialog;
    public AlertDialog dialog = null;
    public static Gson gson;


    public Gson getGson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }


    public void m2(String tag, String str) {
        String temp = "";
        final int CHUNK_SIZE = 500;
        int offset = 0;
        while (str.length() >= (offset + CHUNK_SIZE)) {
            String strFinal = "";
            strFinal = str.substring(offset, offset + CHUNK_SIZE);
            temp = temp + strFinal + "\n";
            offset += CHUNK_SIZE;
        }
        temp = temp + str.substring(offset);

        L.m2(tag, temp);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void getSocketTimeOut(JsonObjectRequest objectRequest) {
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                HttpURL.MY_SOCKET_TIMEOUT_MS, /*DefaultRetryPolicy.DEFAULT_MAX_RETRIES*/0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Check Internet Connection
    public boolean isConnectionAvailable() {
        if (!isOnline()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isOnline() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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

    // ProgressDialog progressDialog; I have declared earlier.
    public void showPDialog(String msg) {
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_animation));
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissPDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void viewAnimationAddList(Context context, View view, int position, int lastPosition) {
        // Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_bottom);
        // Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.listview_slide_left : R.anim.listview_slide_left);
        // view.startAnimation(animation);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AppBarLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
