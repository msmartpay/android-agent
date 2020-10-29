package in.msmartpay.agent.utility;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Harendra on 4/29/2017.
 */

public class BaseActivity extends AppCompatActivity {

    boolean conn = false;
    Builder alertDialog;
    ProgressDialog progressDialog;
    public AlertDialog dialog = null;
    public static Gson gson;


    public Gson getGson(){
        if(gson==null)
            gson = new Gson();
        return gson;
    }




    public  void m2(String tag, String str){
        String temp="";
        final int CHUNK_SIZE =500;
        int offset = 0;
        while (str.length()>=(offset+CHUNK_SIZE)){
            String strFinal="";
            strFinal = str.substring(offset, offset+CHUNK_SIZE);
            temp = temp+strFinal+"\n";
            offset+=CHUNK_SIZE;
        }
        temp = temp+str.substring(offset);

        L.m2(tag, temp);
    }
    public void loadImage(Context ctx, String str , ImageView imgview, final ProgressBar progressBar){
        Glide.with(ctx)
                .load(str)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if(progressBar.getVisibility()== View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                    }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if(progressBar.getVisibility()== View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(imgview);
    }
    public static void loadImageStatic(Context ctx, String str , ImageView imgview, final ProgressBar progressBar){
        Glide.with(ctx)
                .load(str)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                     if(progressBar.getVisibility()== View.VISIBLE) {
                         progressBar.setVisibility(View.GONE);
                     }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if(progressBar.getVisibility()== View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(imgview);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void  getSocketTimeOut(JsonObjectRequest objectRequest){
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                HttpURL.MY_SOCKET_TIMEOUT_MS, /*DefaultRetryPolicy.DEFAULT_MAX_RETRIES*/0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    //Check Internet Connection
    public boolean isConnectionAvailable() {
        if (isOnline() == false) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isOnline() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(BaseActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        if (haveConnectedWifi == true || haveConnectedMobile == true) {
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

    public String LoadPref(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
        String data = sharedPreferences.getString(key, "");
        return data;
    }

    public void SavePref(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }
    public static void createSourceFile(Context ctx, String data) {
        FileWriter writer = null;
       // File cDir;
        File tempFile;

        try {
            System.out.println("Source File creation start");
            //** Getting reference to btn_save of the layout activity_main *//*
            //cDir =ctx.getExternalCacheDir();

            //** Getting a reference to temporary file, if created earlier *//*
            tempFile = new File(/*cDir.getPath()*/getDiskCacheDir(ctx) + "/" + "msmart.txt");
            writer = new FileWriter(tempFile);
            writer.write(data);
            //** Closing the writer object *//*
            writer.close();

            System.out.println("Source File created");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public static String readSourceFile(Context ctx) {
        Log.v("Read file called", "");
        String strLine = "";
        StringBuilder text = new StringBuilder();
        //File cDir;
        File tempFile;
        try {
            //** Getting reference to btn_save of the layout activity_main *//*
            //cDir = ctx.getExternalCacheDir();

            //** Getting a reference to temporary file, if created earlier *//*
            tempFile = new File(/*cDir.getPath()*/ getDiskCacheDir(ctx)+ "/" + "msmart.txt");
            if (!tempFile.exists()) {
                return null;
            } else {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);

                //** Reading the contents of the file , line by line *//*
                while ((strLine = bReader.readLine()) != null) {
                    text.append(strLine + "\n");
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return text.toString();
    }
    public void showKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
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

    /*Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter hotel location !!!", Snackbar.LENGTH_LONG);
    View sbView = snackbar.getView();
    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(26f);

                    snackbar.show();
*/
}
