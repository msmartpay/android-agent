package in.msmartpay.agent.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.print.PrintHelper;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import in.msmartpay.agent.R;

/**
 * Created by Harendra on 4/29/2017.
 */

public class Util {
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    public static Gson gson1;


    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static String getDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        return dateFormatter.format(newDate.getTime());
    }

    public static void showCalender(EditText text) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog chddDatePickerDialog = new DatePickerDialog(text.getContext(), (view1, year, monthOfYear, dayOfMonth) ->
                text.setText(Util.getDate(year, monthOfYear, dayOfMonth)),
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        chddDatePickerDialog.show();
    }


    @SuppressLint("SetTextI18n")
    public static String getAmountWithSymbol(Context ctx, String balance) {
        return ctx.getResources().getString(R.string.rupee_symbol) + " " + getAmount(balance);
    }

    @SuppressLint("SetTextI18n")
    public static String getAmount(String balance) {
        if (balance == null || balance.isEmpty()) {
            return "" + 0.00;
        } else {
            return "" + (int) Math.round(Double.parseDouble(balance) * 100) / (double) 100;
        }
    }

    public static Gson getGson() {
        if (gson1 == null)
            gson1 = new Gson();
        return gson1;
    }
    public static <T> List<T> getListFromJson(String jsonArray, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return getGson().fromJson(jsonArray, typeOfT);
    }

    public static String getStringFromModel(Object obj){
        return getGson().toJson(obj);
    }

    public static String getJsonFromModel(Object obj) {
        return getGson().toJson(obj);
    }


    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private static SharedPreferences getMyPref(Context context) {
        if (pref == null) {
            pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        }
        return pref;
    }

    public static void SavePrefData(Context context, String key, String value) {
        if (editor == null)
            editor = getMyPref(context).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void SavePrefData(Context context, String key, Boolean value) {
        if (editor == null)
            editor = getMyPref(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static Boolean LoadPrefDataBoolean(Context context, String key) {
        return getMyPref(context).getBoolean(key, false);
    }
    public static String LoadPrefData(Context context, String key) {
        return getMyPref(context).getString(key, "");
    }

    public static void clearMyPref(Context context) {
        if (editor == null)
            editor = getMyPref(context).edit();
        editor.clear().apply();
    }

    public static void editable(TextInputLayout til) {
        til.setFocusable(true);
        til.setFocusableInTouchMode(true);
        Objects.requireNonNull(til.getEditText()).setFocusable(true);
        til.getEditText().setFocusableInTouchMode(true);
    }
    public static void nonEditable(TextInputLayout til) {
        til.setFocusable(false);
        til.setFocusableInTouchMode(false);
        Objects.requireNonNull(til.getEditText()).setFocusable(false);
        til.getEditText().setFocusableInTouchMode(false);
    }

    public static void showView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    public static void hideView(View v) {
        v.setVisibility(View.GONE);
    }

    public static void invisibleView(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    public static String getIpAddress(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
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

        if (haveConnectedWifi) {
            return getWifiIPAddress(context);
        }
        if (haveConnectedMobile)
            return getMobileIPAddress();

        return null;
    }

    public static String getWifiIPAddress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiMgr != null;
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }

    public static String getMobileIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void showKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyBoard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void openSettingsDialog(Activity context, int requestCode) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Location Permission")
                .setMessage("This app needs permission to use this feature. You can grant them in app settings.")
                .setCancelable(false)
                .setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivityForResult(intent, requestCode);
                    dialog.dismiss();
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static boolean checkRegexValidation(String regex,String value){
        return Pattern.matches(regex,value);
    }

    public static String generateMd5Hash(byte[] msg) {
        String str;

        byte [] hash = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = md.digest(msg);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder strBuilder = new StringBuilder();
        for(byte b:hash)
        {
            strBuilder.append(String.format("%02x", b));
        }
        str = strBuilder.toString();
        System.out.println("The MD5 hash: "+str);
        return str;
    }

    /**
     * Get bitmap of a view
     *
     * @param view source view
     * @return generated bitmap object
     */
    public static Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


    public static void screenshot(LinearLayoutCompat view, String filename) {

        try {
            // Initialising the directory of storage
            String dirpath =view.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "";
            File file = new File(dirpath);
            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }

            // File name
            String path = dirpath + "/" + filename + ".png";
            File imageurl = new File(path);
            FileOutputStream outputStream = new FileOutputStream(imageurl);
            Bitmap bitmap = getBitmapFromView(view,view.getHeight(),view.getWidth());
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();

            doPhotoPrint(bitmap,view.getContext());

        } catch (Exception io) {
            io.printStackTrace();
        }
    }
    public static void doPhotoPrint(Bitmap bitmap,Context context){
        PrintHelper printHelper=new PrintHelper(context);
        printHelper.setScaleMode( PrintHelper.SCALE_MODE_FIT);
        printHelper.printBitmap("Print Receipt",bitmap);
    }
    public static String SHA1(@NonNull String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        }catch (Exception e){
            return "error";
        }
    }
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                }
                else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };
    public static String[] storagePermissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

}
