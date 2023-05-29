package in.msmartpay.agent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.user.ForgotPasswordRequest;
import in.msmartpay.agent.network.model.user.MobileValidationRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    private Dialog verification_dialog;
    private Button btn_login,btn_send_otp,btn_verify_otp;
    private EditText email, password;
    private CheckBox checkBox;
    private TextView tv, sign_up,signup_requestotp_msg;
    private String Email, Password,signupMobile;
    private ProgressDialog pd;
    private TextInputEditText edit_verify_mobile = null;
    private TextInputLayout til_signup_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().hide();
        setTitle("Login");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = LoginActivity.this;

        email =  findViewById(R.id.et_email);
        password =  findViewById(R.id.et_password);
        checkBox =  findViewById(R.id.cb_remember);
        sign_up =  findViewById(R.id.tv_sign_up);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv = (TextView) findViewById(R.id.tv_forgot);


        btn_login.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        tv.setOnClickListener(this);
        new MyLoginCredential().execute();

        if(getIntent().hasExtra("action")){
            Intent i=getIntent();
            String action=i.getStringExtra("action");
            if("openRegisterDialog".equalsIgnoreCase(action)){
                signUpVerificationDialog();
            }
        }
    }

    class MyLoginCredential extends AsyncTask<Void,Void,Void> {
        private String loginDetails;
        @Override
        protected Void doInBackground(Void... voids) {
            loginDetails = BaseActivity.readSourceFile(context, Keys.MY_FILE);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (loginDetails != null && !loginDetails.equalsIgnoreCase("")) {
                String[] cred = loginDetails.split("&");
                if (cred.length >= 2) {
                    String user = cred[0];
                    String pass = cred[1];

                    email.setText(user);
                    password.setText(pass.replaceAll("\\r|\\n", ""));
                    Email = user;
                    Password = pass.replaceAll("\\r|\\n", "");
                    checkBox.setChecked(true);
                    /*if("".equalsIgnoreCase(Util.LoadPrefData(getApplicationContext(),Keys.TXN_KEY))){
                    }else {
                        loginRequest(user,pass);
                    }*/
                } else {
                    BaseActivity.createSourceFile(context, "", Keys.MY_FILE);
                }
            }
        }
    }
    //*******************onClick*************************
    @Override
    public void onClick(View view) {
        if (isConnectionAvailable()) {
            switch (view.getId()){
                case R.id.btn_login:
                    Email = email.getText().toString().trim();
                    Password = password.getText().toString().trim();
                    if (TextUtils.isEmpty(Email) && Email.length() < 10) {
                        Toast.makeText(context, "Please enter correct userid !!!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(Password) && Password.length() < 6) {
                        Toast.makeText(context, "Please enter correct password and password > 6 !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkBox.isChecked()) {
                            Log.v("create remember file ", "");
                            createSourceFile(context, Email + "&" + Password, Keys.MY_FILE);
                        }
                        loginRequest(Email, Password,"login","");
                    }
                    break;
                case R.id.tv_sign_up:
                    signUpVerificationDialog();
                    break;
                case R.id.tv_forgot:
                    forgetPasswordDialog();
                    break;
            }
        } else {
            Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
        }
    }

    //============Forget Password===============================

    private void forgetPasswordDialog() {
        final Dialog dialog_status = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.signup_mobile_dialog);
        dialog_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edit_verify_mobile =  dialog_status.findViewById(R.id.edit_verify_mobile);
        TextView title =  dialog_status.findViewById(R.id.title);
        Button btn_submit =  dialog_status.findViewById(R.id.btn_verify);
        Button close_mobile =  dialog_status.findViewById(R.id.close_mobile);

        edit_verify_mobile.setText("");
        title.setText("Forget Password");
        btn_submit.setText("Submit");

        btn_submit.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edit_verify_mobile.getText().toString().trim())) {
                edit_verify_mobile.requestFocus();
                L.toastS(context, "Please enter mobile number first !!!");
            } else if (edit_verify_mobile.getText().toString().trim().length() < 9) {
                edit_verify_mobile.requestFocus();
              L.toastS(context, "Mobile number should be 10 digits !!!");
            } else {
                forgetPasswordRequest();
                //forgetPasswordDialog("",1);
                dialog_status.dismiss();
            }
        });

        close_mobile.setOnClickListener(view -> {
            dialog_status.cancel();
            hideKeyBoard(edit_verify_mobile);
        });

        dialog_status.show();
    }

    private void forgetPasswordRequest() {
        if (NetworkConnection.isConnectionAvailable2(context)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            ForgotPasswordRequest request = new ForgotPasswordRequest();
            request.setMob( edit_verify_mobile.getText().toString());

            RetrofitClient.getClient(getApplicationContext())
                    .forgetPass(request).enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse> call, @NotNull retrofit2.Response<MainResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse res = response.body();
                        if (res.getResponseCode() != null) {
                            forgetPasswordDialog(res.getResponseMessage(), Integer.parseInt(res.getResponseCode()));
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        } else {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void forgetPasswordDialog(String msg, int i) {
        final Dialog dialog_status = new Dialog(context);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);

        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);

        text.setText(msg);

        if (i == 0) {
            statusImage.setImageResource(R.drawable.trnsuccess);
            trans_status.setOnClickListener(view -> dialog_status.dismiss());
        } else {
            statusImage.setImageResource(R.drawable.failed);
            trans_status.setOnClickListener(view -> dialog_status.dismiss());
        }
        dialog_status.show();
    }
    //==========================================================
    //===============Sign Up Mobile Verification====================
    private void signUpVerificationDialog() {
        verification_dialog = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        verification_dialog.setCancelable(false);
        verification_dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        verification_dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        verification_dialog.setContentView(R.layout.signup_mobile_dialog);
        verification_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        signup_requestotp_msg = verification_dialog.findViewById(R.id.signup_requestotp_msg);
        edit_verify_mobile =  verification_dialog.findViewById(R.id.edit_verify_mobile);
        til_signup_otp = verification_dialog.findViewById(R.id.til_signup_otp);
        btn_send_otp = (Button) verification_dialog.findViewById(R.id.btn_verify);
        btn_verify_otp = (Button) verification_dialog.findViewById(R.id.btn_verify_otp);
        Button close_mobile = (Button) verification_dialog.findViewById(R.id.close_mobile);
        edit_verify_mobile.setText("");
        Util.hideView(signup_requestotp_msg);

        btn_send_otp.setOnClickListener(view -> {
            if (isConnectionAvailable()) {

                if (TextUtils.isEmpty(edit_verify_mobile.getText().toString())) {
                    edit_verify_mobile.requestFocus();
                    L.toastS(context, "Please enter mobile number first !!!");
                } else if (edit_verify_mobile.getText().toString().length() < 9) {
                    edit_verify_mobile.requestFocus();
                   L.toastS(context, "Mobile number should be 10 digits !!!");
                } else {
                    signUpVerifyRequest();
                }
            }
        });

        btn_verify_otp.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                if (TextUtils.isEmpty(til_signup_otp.getEditText().getText().toString())) {
                    til_signup_otp.getEditText().requestFocus();
                    L.toastS(context, "Please enter valid otp !!!");
                } else {
                    verifyMobileOTP();
                }
            }
        });

        close_mobile.setOnClickListener(view -> {
            verification_dialog.dismiss();
            hideKeyBoard(edit_verify_mobile);
        });

        verification_dialog.show();
    }

    private void signUpVerifyRequest() {
        Util.hideView(signup_requestotp_msg);
        if (NetworkConnection.isConnectionAvailable2(context)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            MobileValidationRequest request = new MobileValidationRequest();
            request.setDeviceId(Util.getDeviceId(getApplicationContext()));
            request.setLatitude(Util.LoadPrefData(getApplicationContext(), getString(R.string.latitude)));
            request.setLongitude(Util.LoadPrefData(getApplicationContext(), getString(R.string.longitude)));
            request.setIp(Util.getIpAddress(getApplicationContext()));
            request.setMobile(edit_verify_mobile.getText().toString());
            request.setVersion(AppMethods.VERSION);

            RetrofitClient.getClient(getApplicationContext())
                    .mobileValidation(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {

                            signup_requestotp_msg.setText(res.getResponseMessage());
                            Util.showView(signup_requestotp_msg);
                            Util.showView(til_signup_otp);
                            Util.showView(btn_verify_otp);
                            Util.hideView(btn_send_otp);

                            edit_verify_mobile.setEnabled(false);
                            til_signup_otp.getEditText().requestFocus();

                            L.toastS(getApplicationContext(),res.getResponseMessage());
                        } else if (res.getResponseCode() != null && res.getResponseCode().equals("2")) {
                            L.toastS(getApplicationContext(),res.getResponseMessage());
                            signup_requestotp_msg.setText(res.getResponseMessage());
                            Util.showView(signup_requestotp_msg);
                        } else if (res.getResponseCode() != null && res.getResponseCode().equals("1")) {
                            L.toastS(getApplicationContext(),res.getResponseMessage());
                            signup_requestotp_msg.setText(res.getResponseMessage());
                            Util.showView(signup_requestotp_msg);
                        }
                    }else{
                        signup_requestotp_msg.setText("Technical failure");
                        Util.showView(signup_requestotp_msg);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void verifyMobileOTP() {
        if (NetworkConnection.isConnectionAvailable2(context)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            MobileValidationRequest request = new MobileValidationRequest();
            request.setMobile(edit_verify_mobile.getText().toString());
            request.setOtp(til_signup_otp.getEditText().getText().toString());
            request.setVersion(AppMethods.VERSION);

            RetrofitClient.getClient(getApplicationContext())
                    .otpValidation(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            verification_dialog.dismiss();
                            VerifyOTPStatusDialog(res.getResponseMessage());
                        } else if (res.getResponseCode() != null && res.getResponseCode().equals("2")) {
                            L.toastS(getApplicationContext(),res.getResponseMessage());
                            signup_requestotp_msg.setText(res.getResponseMessage());
                            Util.showView(signup_requestotp_msg);
                        } else if (res.getResponseCode() != null && res.getResponseCode().equals("1")) {
                            L.toastS(getApplicationContext(),res.getResponseMessage());
                            signup_requestotp_msg.setText(res.getResponseMessage());
                            Util.showView(signup_requestotp_msg);
                        }
                    }else{
                        signup_requestotp_msg.setText("Technical failure");
                        Util.showView(signup_requestotp_msg);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void VerifyOTPStatusDialog(String msg) {
        Dialog dialog_status = new Dialog(context);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        statusImage.setImageResource(R.drawable.about);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        text.setText(msg);

        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog_status.dismiss();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RegisterComplete.class);
                intent.putExtra("mob", edit_verify_mobile.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        dialog_status.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


}