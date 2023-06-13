package in.msmartpay.agent.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.setting.UpdateTPINRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class SetupTpinActivity extends BaseActivity {

    private Context context;
    private TextInputLayout til_tpin,til_tpin_otp;
    private Button btn_save_tpin;
    private TextView tv_resend_tpin_otp,tpin_message;
    private ProgressDialog pd;
    private String agentID, txn_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpin_setup_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("TPIN Setup");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = SetupTpinActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        til_tpin = findViewById(R.id.til_tpin);
        til_tpin_otp = findViewById(R.id.til_tpin_otp);
        btn_save_tpin = findViewById(R.id.btn_save_tpin);
        tv_resend_tpin_otp = findViewById(R.id.tv_resend_tpin_otp);
        tpin_message = findViewById(R.id.tpin_message);

        btn_save_tpin.setOnClickListener(view -> {
            if(TextUtils.isEmpty(til_tpin.getEditText().getText().toString().trim())){
                til_tpin.getEditText().requestFocus();
                Toast.makeText(context, "Enter valid 4-digit TPIN", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(til_tpin_otp.getEditText().getText().toString().trim()) ){
                til_tpin_otp.getEditText().requestFocus();
                Toast.makeText(context, "Enter valid 6-digit One Time Password (OTP)", Toast.LENGTH_SHORT).show();
            }else {
                saveTpin();
            }
        });

        tv_resend_tpin_otp.setOnClickListener(view -> {
            tpin_message.setText("");
            Util.hideView(tpin_message);
            requestTpinOtp();

        });
    }

    private void saveTpin() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            UpdateTPINRequest request = new UpdateTPINRequest();
            request.setAgentId(agentID);
            request.setTxnKey(txn_key);
            request.setTpin(til_tpin.getEditText().getText().toString().trim());
            request.setOtp(til_tpin_otp.getEditText().getText().toString().trim());

            RetrofitClient.getClient(getApplicationContext())
                    .updateTPIN(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        tpin_message.setText(res.getMessage());
                        Util.showView(tpin_message);
                    }else {
                        tpin_message.setText("No Server Response");
                        Util.showView(tpin_message);
                        L.toastS(getApplicationContext(), "No Server Response");
                    }
                    til_tpin.getEditText().setText("");
                    til_tpin_otp.getEditText().setText("");
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void requestTpinOtp() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            UpdateTPINRequest request = new UpdateTPINRequest();
            request.setAgentId(agentID);
            request.setTxnKey(txn_key);


            RetrofitClient.getClient(getApplicationContext())
                    .tpinOtpResend(request).enqueue(new Callback<MainResponse2>() {
                        @Override
                        public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                MainResponse2 res = response.body();
                                tpin_message.setText(res.getMessage());
                                Util.showView(tpin_message);
                            }else {
                                tpin_message.setText("No Server Response");
                                Util.showView(tpin_message);
                                L.toastS(getApplicationContext(), "No Server Response");
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}