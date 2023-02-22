package com.aepssdkssz.util;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.aepssdkssz.R;

import org.apache.commons.ssl.OpenSSL;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Utility {


    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    public static <T> List<T> getListFromJson(String jsonArray, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return getGson().fromJson(jsonArray, typeOfT);
    }

    public static String getStringFromModel(Object obj) {
        return getGson().toJson(obj);
    }

    public static void setTextViewBG_TextColor(TextView textView, boolean isSelected) {
        if (isSelected) {
            textView.setBackgroundResource(R.drawable.ssz_aeps_round_border_strock_pr);
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.sszAePS_colorWhite));
        } else {
            textView.setBackgroundResource(R.drawable.ssz_aeps_round_border_strock_hint);
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.sszAePS_blackcolor));
        }

    }

    public static void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawablesRelative()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public static ProgressDialog getProgressDialog(Context ctx) {
        ProgressDialog dialog = new ProgressDialog(ctx, R.style.AppCompatAlertDialogStyle);
        try {
            dialog.setCancelable(false);
            dialog.requestWindowFeature(1);
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }

    public static void setCaptureFingerED(ImageView imageView, boolean isEnable) {
        if (isEnable) {
            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.sszAePSColorPrimary), PorterDuff.Mode.SRC_IN);
        } else {
            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.sszAePS_blackcolor), PorterDuff.Mode.SRC_IN);
        }
    }

    public static boolean checkConnection(Context context) {
        boolean flag = false;
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.isConnected())
                flag = true;
            else {
                Utility.toast(context.getApplicationContext(), "No internet available");
            }
        } else {
            Utility.toast(context.getApplicationContext(), "No internet available");
        }
        return flag;
    }

    public static boolean checkConnection2(Context context) {
        boolean flag = false;
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.isConnected())
                flag = true;
            else {
                //   showToast(context.getApplicationContext(), "No internet available", true);
            }
        } else {
            // showToast(context.getApplicationContext(), "No internet available", true);
        }
        return flag;
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void loge(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void loge(String tag, String msg, Throwable t) {
        Log.e(tag, msg, t);
    }

    public static SharedPreferences getPref(Context ctx) {
        if (sharedPreferences == null)
            sharedPreferences = ctx.getSharedPreferences("aeps", MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void saveData(Context ctx, String key, String value) {
        if (editor == null)
            editor = getPref(ctx).edit();
        editor.putString(key, value).apply();
    }

    public static String getData(Context ctx, String key) {
        return getPref(ctx).getString(key, "");
    }

    public static void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public static void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void invisibleView(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public static String encodeBase64(final String clearText) {
        final byte[] encoded = Base64.encode(clearText.getBytes(), 0);
        return new String(encoded);
    }

    public static String decodeBase64(final String encodedText) {
        final byte[] decoded = Base64.decode(encodedText, 0);
        return new String(decoded);
    }

    public static void showMessageDialogue(final Context ctx, final String messageTxt, final String argTitle) {
        new AlertDialog.Builder(ctx)
                .setCancelable(false)
                .setTitle((CharSequence) argTitle)
                .setMessage((CharSequence) messageTxt)
                .setPositiveButton((CharSequence) "OK", (dialog, which) -> dialog.cancel()).show();
    }

    public static void sendReportEmail(final Context context, final String emailSubject, final String emailBody) {

    }

    public static String getCurrentTimeStamp() {
        try {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            final String date = df.format(Calendar.getInstance().getTime());
            loge("Connection", "Formated Current Date: " + date);
            if (date != null) {
                return date.trim();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String encryptHeader(final String json, final String key) {
        return encrypt(json, key);
    }

    public static String encryptBody(final String json, final String key) {
        return encrypt(json, key);
    }

    private static String encrypt(final String plainText, final String key) {
        String encryptedText = "";
        try {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(plainText.getBytes());
            encryptedText = getStringFromInputStream(OpenSSL.encrypt("AES256", key.getBytes(), (InputStream) inputStream));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encryptedText;
    }

    private static String decrypt(final String encryptedString, final String key) {
        String decryptedText = "";
        try {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(encryptedString.getBytes());
            decryptedText = getStringFromInputStream(OpenSSL.decrypt("AES256", key.getBytes(), (InputStream) inputStream));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return decryptedText;
    }

    private static String getStringFromInputStream(final InputStream inputStream) {
        final BufferedReader reader = null;
        new StringBuilder();
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String outputText = "";
        try {
            for (int i = bufferedInputStream.read(); i != -1; i = bufferedInputStream.read()) {
                outputStream.write((byte) i);
            }
            outputText = outputStream.toString("UTF-8");
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioEx2) {
                ioEx2.printStackTrace();
            }
        }
        return outputText;
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

    public static String saveBitmap(Bitmap bitmap, String baseName) {
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File pictureDir = new File(sdcard, "SSZ AePS");
            pictureDir.mkdirs();
            File f = null;
            for (int i = 1; i < 200; ++i) {
                String name = baseName + i + ".JPEG";
                f = new File(pictureDir, name);
                if (!f.exists()) {
                    break;
                }
            }
            if (!f.exists()) {
                String filePath = f.getAbsolutePath();
                FileOutputStream fos = new FileOutputStream(filePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return filePath;
            }
        } catch (Exception e) {
        } finally {
           /* if (fos != null) {
                fos.close();
            }*/
        }
        return null;
    }

    public static void setErrorFalseOfTextInputLayout(TextInputLayout layout) {
        Objects.requireNonNull(layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean searchPackageName(Context context,String packageName){
        final PackageManager pm = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setPackage(packageName);
        //intent.setAction();
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(intent, 0);
        //L.m2("searchPackageName",resolveInfoList.isEmpty()+"");
        return !resolveInfoList.isEmpty();
    }
    public static void openPlayStoreApp(String packageName,
                                        ActivityResultLauncher<Intent> openPlayStoreLauncher) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarketIntent = new Intent(Intent.ACTION_VIEW, uri);
        goToMarketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        try {
            openPlayStoreLauncher.launch(goToMarketIntent);
            // context.startActivity(goToMarketIntent, null);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)
            );
            openPlayStoreLauncher.launch(intent);
            //  context.startActivity(intent, null);
        }
    }


}
