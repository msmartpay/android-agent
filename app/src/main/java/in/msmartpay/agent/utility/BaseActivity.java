package in.msmartpay.agent.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import in.msmartpay.agent.LoginActivity;
import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.user.LoginRequest;
import in.msmartpay.agent.network.model.user.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Harendra on 4/29/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public AlertDialog dialog = null;
    public static Gson gson;
    private String ba_email, ba_password;

    public void gotoHomeScreen(){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

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

        Log.d(tag, temp);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        boolean conn =false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public static void createSourceFile(Context ctx, String data, String fileName) {
        String FILE_NAME = fileName;
        FileWriter writer = null;
        File cDir;
        File tempFile;

        try {
            System.out.println("Source File creation start");
            //** Getting reference to btn_save of the layout activity_main *//*
            cDir =ctx.getCacheDir();

            //** Getting a reference to temporary file, if created earlier *//*
            tempFile = new File(cDir.getPath() + "/" + FILE_NAME);
            writer = new FileWriter(tempFile);
            writer.write(data);
            //** Closing the writer object *//*
            writer.close();

            System.out.println("Source File created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readSourceFile(Context ctx, String fileName) {
        Log.v("Read file called", "");
        String FILE_NAME = fileName;
        String strLine = "";
        StringBuilder text = new StringBuilder();
        File cDir;
        File tempFile;
        try {
            //** Getting reference to btn_save of the layout activity_main *//*
            cDir = ctx.getCacheDir();

            //** Getting a reference to temporary file, if created earlier *//*
            tempFile = new File(cDir.getPath() + "/" + FILE_NAME);
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
    public void startLoginActivity(){
        finishAffinity();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }
    public void loginRequest(String Email,String Password,String action,String otp) {
        ba_email=Email;
        ba_password=Password;
        if(NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            final ProgressDialog pd = ProgressDialog.show(BaseActivity.this, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            LoginRequest request = new LoginRequest();
            request.setDeviceId(Util.getDeviceId(getApplicationContext()));
            request.setLatitude(Util.LoadPrefData(getApplicationContext(), getString(R.string.latitude)));
            request.setLongitude(Util.LoadPrefData(getApplicationContext(), getString(R.string.longitude)));
            request.setIp(Util.getIpAddress(getApplicationContext()));
            request.setMobileNo(ba_email.replaceAll("\\r|\\n", ""));
            request.setPassword(ba_password.replaceAll("\\r|\\n", ""));
            request.setVersion(AppMethods.VERSION);
            request.setAction(action);
            request.setOtp(otp);

            RetrofitClient.getClient(getApplicationContext())
                    .login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NotNull Call<LoginResponse> call, @NotNull retrofit2.Response<LoginResponse> response) {
                    pd.dismiss();
                    try {
                        if (response.isSuccessful() && response.body()!=null){
                            LoginResponse res =response.body();
                            if (res.getResponseCode().equalsIgnoreCase("0")){
                                Util.SavePrefData(getApplicationContext(),getString(R.string.user_credential),"&agent_Id=" + res.getAgentId() + "&agent_initial=" +res.getAgentInitial() + "&agent_dst_id=" + res.getAgentDistId() + "&txn_key=" +res.getTxn_key());
                                Util.SavePrefData(getApplicationContext(),"agent_id_u",res.getAgentId() + "");
                                Util.SavePrefData(getApplicationContext(),"agent_id","&agent_id=" + res.getAgentId() );
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_FULL, (String)res.getAgentInitial() + res.getAgentId() );
                                Util.SavePrefData(getApplicationContext(),Keys.BALANCE,res.getBalance() + "");
                                Util.SavePrefData(getApplicationContext(), Keys.USER_CODE,  res.getEkoUserCode());
                                Util.SavePrefData(getApplicationContext(),Keys.TXN_KEY,res.getTxn_key() + "");
                                Util.SavePrefData(getApplicationContext(),Keys.TPIN_STATUS,res.getTpinStatus());
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_ID,res.getAgentId()  + "");
                                Util.SavePrefData(getApplicationContext(),Keys.KYC_STATUS,res.getKycStatus());
                                Util.SavePrefData(getApplicationContext(),"statement_download","true");
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_PAN, "" + res.getPan_number());
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_PIN, "" + res.getPin());
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_ADDRESS, "" + res.getAddress());
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_COMPANY, "" + res.getAgency_name());
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_NAME, "" + res.getAgentName());
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_EMAIL, "" + res.getEmailId());
                                Util.SavePrefData(getApplicationContext(),Keys.AGENT_MOB, "" + res.getMobileNumber());
                                Util.SavePrefData(getApplicationContext(),Keys.WALLET_STATUS, "" + res.getWalletStatus());
                                Util.SavePrefData(getApplicationContext(),Keys.AEPS_STATUS, "" + res.getAepsStatus());
                                Util.SavePrefData(getApplicationContext(),Keys.AADHARPAY_STATUS, "" + res.getAadharpayStatus());
                                Util.SavePrefData(getApplicationContext(),Keys.SUPPORT_1, "" + res.getSupport1());
                                Util.SavePrefData(getApplicationContext(),Keys.SUPPORT_2, "" + res.getSupport2());
                                Util.SavePrefData(getApplicationContext(),Keys.TICKER_MESSAGE, "" + res.getApp_text());
                                Util.SavePrefData(getApplicationContext(),"DMR", "" + res.getDMR());
                                Util.SavePrefData(getApplicationContext(),"DMRUrl", "" + res.getDMRUrl());
                                Util.SavePrefData(getApplicationContext(),Keys.SUPPORT_EMAIL, "" + res.getSupportEmail());
                                Util.SavePrefData(getApplicationContext(),Keys.DMR_VENDOR, "" + res.getDmrVendor());
                                Util.SavePrefData(getApplicationContext(),Keys.TIME,""+res.getTime());
                                Util.SavePrefData(getApplicationContext(),Keys.ABOUT_US_URL,""+res.getAbout());

                                Util.SavePrefData(getApplicationContext(), Keys.ADDRESS, "" + res.getAddress());
                                Util.SavePrefData(getApplicationContext(), Keys.STATE, "" + res.getState());
                                Util.SavePrefData(getApplicationContext(), Keys.DISTRICT, "" + res.getDistrict());
                                Util.SavePrefData(getApplicationContext(), Keys.PINCODE, "" + res.getPincode());

                                Util.SavePrefData(getApplicationContext(),Keys.TERMS_AND_CONDITIONS_URL,""+res.getTermsAndConditions());
                                String appText = "";
                                if (res.getApp_text() != null) {
                                    appText = res.getApp_text().trim();
                                } else {
                                    appText = "";
                                }
                                /*
                                if (!appText.equalsIgnoreCase("") && appText.length() > 0) {

                                    Log.v("appText", appText);

                                    final Dialog dialog_status = new Dialog(BaseActivity.this);
                                    dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog_status.setContentView(R.layout.alert);
                                    dialog_status.setCancelable(true);
                                    ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
                                    statusImage.setImageResource(R.drawable.about);
                                    statusImage.setColorFilter(ContextCompat.getColor(statusImage.getContext(), R.color.colorPrimary));
                                    TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
                                    text.setText(appText);

                                    final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
                                    trans_status.setOnClickListener(v -> {
                                        dialog_status.dismiss();
                                        Intent intent = new Intent();
                                        intent.setClass(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                                    dialog_status.show();
                                } else { */
                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                               // }
                            }else if (res.getResponseCode() != null && res.getResponseCode().equals("3")) {//Verify Login OTP

                                verifyLoginWithOtpDialog(res.getResponseMessage());
                                L.toastS(getApplicationContext(), res.getResponseMessage());
                            }else if (res.getResponseCode() != null && res.getResponseCode().equals("4")) {//Verify Login OTP

                                verifyLoginWithOtpDialog(res.getResponseMessage());
                                L.toastS(getApplicationContext(), res.getResponseMessage());
                            }else if (res.getResponseCode() != null && res.getResponseCode().equals("1")) {

                                L.toastL(getApplicationContext(), res.getResponseMessage());
                            } else if (res.getResponseCode()!= null && res.getResponseCode().equals("2")) {

                                L.toastL(getApplicationContext(), res.getResponseMessage());
                            } else if (res.getResponseCode()!= null && res.getResponseCode().equals("5")) {

                                L.toastL(getApplicationContext(), res.getResponseMessage());
                                registerDialogBox(res.getResponseMessage());
                            } else {

                                L.toastL(getApplicationContext(), "Wrong UserName or Password! ");
                            }

                        }
                    }catch (Exception e){
                        L.toastL(getApplicationContext(), e.getLocalizedMessage());
                        L.m2("System ERROR",e.getLocalizedMessage());
                        startLoginActivity();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                    pd.dismiss();
                    L.toastS(getApplicationContext(), t.getLocalizedMessage());
                    startLoginActivity();
                }
            });
        }else {
            startLoginActivity();
        }
    }

    private void verifyLoginWithOtpDialog(String message) {
        final Dialog dialog_status = new Dialog(BaseActivity.this);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.verify_login_otp_dialog);
        dialog_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputEditText edit_verify_otp =  dialog_status.findViewById(R.id.edit_verify_otp);
        TextView login_message =  dialog_status.findViewById(R.id.login_message);
        Button btn_login_verify =  dialog_status.findViewById(R.id.btn_login_verify);
        Button close_login_verify =  dialog_status.findViewById(R.id.close_login_verify);

        login_message.setText(message);

        btn_login_verify.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edit_verify_otp.getText().toString().trim())) {
                edit_verify_otp.requestFocus();
                login_message.setText("Please enter otp received on your registered mobile !!!");
                L.toastS(getApplicationContext(), "Please enter otp received on your registered mobile !!!");
            } else if (edit_verify_otp.getText().toString().trim().length() < 6) {
                edit_verify_otp.requestFocus();
                login_message.setText("Please enter valid 6 digit otp!!!");
                L.toastS(getApplicationContext(), "Please enter valid 6 digit otp!!!");
            } else {
                loginRequest(ba_email,ba_password,"Verify",edit_verify_otp.getText().toString().trim());
                dialog_status.dismiss();
            }
        });

        close_login_verify.setOnClickListener(view -> {
            dialog_status.cancel();
            hideKeyBoard(edit_verify_otp);
            startLoginActivity();
        });

        dialog_status.show();
    }

    private void registerDialogBox(String message) {
        final Dialog dialog_status = new Dialog(BaseActivity.this);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.login_register_confirm_dialog);
        dialog_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView login_register_message =  dialog_status.findViewById(R.id.login_register_message);
        Button btn_reg_yes =  dialog_status.findViewById(R.id.btn_reg_yes);
        Button btn_reg_no =  dialog_status.findViewById(R.id.btn_reg_no);
        Button close_reg_dialog =  dialog_status.findViewById(R.id.close_reg_dialog);

        login_register_message.setText(message);

        btn_reg_yes.setOnClickListener(view -> {
            dialog_status.cancel();
            finishAffinity();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.putExtra("action","openRegisterDialog");
            startActivity(i);
        });

        close_reg_dialog.setOnClickListener(view -> {
            dialog_status.cancel();

        });
        btn_reg_no.setOnClickListener(view -> {
            dialog_status.cancel();

        });

        dialog_status.show();
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }*/
}
