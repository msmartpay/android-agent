package in.msmartpay.agent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.user.MobileValidationRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.L;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Harendra on 7/17/2017.
 */

public class OTPRegisterActivity extends BaseActivity {
    private TextInputEditText et_res_otp;
    private Button btn_register;
    private TextView btn_otp_resend;
    private Context ctx;
    private ProgressDialog pd;
    private Dialog dialog_status;
    private boolean flag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.otp_register);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      /*  setTitle("Verify OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        ctx = OTPRegisterActivity.this;
        et_res_otp = findViewById(R.id.et_res_otp);
        btn_register = (Button) findViewById(R.id.btn_res_otp);
        btn_otp_resend = (TextView) findViewById(R.id.btn_otp_resend);
        btn_register.setOnClickListener(v -> {
            if (isConnectionAvailable()) {
                if (!TextUtils.isEmpty(et_res_otp.getText().toString()) && et_res_otp.getText().toString().length() == 6) {
                    sendOTP();
                } else {
                    Toast.makeText(ctx, "Please,enter 6 digits otp!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ctx, "Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
            }
        });
        btn_otp_resend.setOnClickListener(v -> {
            if (isConnectionAvailable()) {
                resendOTP();
            } else {
                Toast.makeText(ctx, "Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOTP() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = new ProgressDialog(getApplicationContext());
            pd = ProgressDialog.show(getApplicationContext(), "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            MobileValidationRequest request = new MobileValidationRequest();
            request.setMobile(getIntent().getStringExtra("mob"));
            request.setOtp(et_res_otp.getText().toString());
            request.setVersion(AppMethods.VERSION);

            RetrofitClient.getClient(getApplicationContext())
                    .otpValidation(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            flag = true;
                            showDiallog(res.getMessage());
                        } else if (res.getStatus() != null && res.getStatus().equals("2")) {
                            flag = false;
                            showDiallog(res.getMessage());
                        } else if (res.getStatus() != null && res.getStatus().equals("1")) {
                            flag = false;
                            L.toastS(getApplicationContext(), res.getMessage());
                        }
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

    private void resendOTP() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = new ProgressDialog(getApplicationContext());
            pd = ProgressDialog.show(getApplicationContext(), "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            MobileValidationRequest request = new MobileValidationRequest();
            request.setMobile(getIntent().getStringExtra("mob"));
            request.setVersion(AppMethods.VERSION);

            RetrofitClient.getClient(getApplicationContext())
                    .mobileValidation(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            L.toastS(getApplicationContext(), res.getMessage());
                        } else if (res.getStatus() != null && res.getStatus().equals("2")) {
                            L.toastS(getApplicationContext(), res.getMessage());
                        } else if (res.getStatus() != null && res.getStatus().equals("1")) {
                            L.toastS(getApplicationContext(), res.getMessage());
                        }
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

    private void showDiallog(String msg) {
        dialog_status = new Dialog(ctx);
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
                if (flag == false) {
                    dialog_status.dismiss();
                } else {
                    dialog_status.dismiss();
                    Intent intent = new Intent();
                    intent.setClass(ctx, RegisterComplete.class);
                    intent.putExtra("mob", getIntent().getStringExtra("mob"));
                    startActivity(intent);
                    finish();
                }

            }
        });

        dialog_status.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
