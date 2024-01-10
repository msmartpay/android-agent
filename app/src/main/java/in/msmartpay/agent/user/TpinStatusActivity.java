package in.msmartpay.agent.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.setting.UpdateTPINRequest;
import in.msmartpay.agent.network.model.setting.UpdateTPINStatusRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class TpinStatusActivity extends BaseActivity {

    private Context context;
    private Switch tpin_status_switch;
    private TextInputLayout til_tpin,til_update_tpin_otp;
    private Button btn_update_tpin_status;
    private TextView tv_resend_status_tpin_otp,update_tpin_message;
    private ProgressDialog pd;
    private String agentID, txn_key = "",tpinStatus="",loginTpinStatus="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpin_status_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Enable/Disable TPIN");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TpinStatusActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        loginTpinStatus = Util.LoadPrefData(getApplicationContext(), Keys.TPIN_STATUS);

        tpin_status_switch = findViewById(R.id.tpin_status_switch);
        btn_update_tpin_status =  findViewById(R.id.btn_update_tpin_status);
        update_tpin_message = findViewById(R.id.update_tpin_message);
        til_update_tpin_otp = findViewById(R.id.til_update_tpin_otp);
        btn_update_tpin_status = findViewById(R.id.btn_update_tpin_status);
        tv_resend_status_tpin_otp = findViewById(R.id.tv_resend_status_tpin_otp);

        if("Y".equalsIgnoreCase(loginTpinStatus)){
            tpin_status_switch.setChecked(true);
            tpin_status_switch.setText("Enable");
            tpinStatus="Y";
        }else if("N".equalsIgnoreCase(loginTpinStatus)){
            tpin_status_switch.setChecked(false);
            tpin_status_switch.setText("Disable");
            tpinStatus="N";
        }else{
            tpin_status_switch.setChecked(false);
            tpin_status_switch.setText("Disable");
            tpinStatus="N";
        }

        tpin_status_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tpin_status_switch.isChecked()) {
                    tpinStatus = tpin_status_switch.getTextOn().toString();
                    tpin_status_switch.setText("Enable");
                }
                else {
                    tpinStatus = tpin_status_switch.getTextOff().toString();
                    tpin_status_switch.setText("Disable");
                }

            }
        });

        btn_update_tpin_status.setOnClickListener(view -> {
            if(TextUtils.isEmpty(til_update_tpin_otp.getEditText().getText().toString().trim()) ){
                til_update_tpin_otp.getEditText().requestFocus();
                Toast.makeText(context, "Enter valid 6-digit OTP", Toast.LENGTH_SHORT).show();
            }else {
                updateTPINStatus();
            }
        });
        tv_resend_status_tpin_otp.setOnClickListener(view -> {
            update_tpin_message.setText("");
            Util.hideView(update_tpin_message);
            requestTpinStatusOtp();

        });
    }

    private void updateTPINStatus() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            UpdateTPINStatusRequest request = new UpdateTPINStatusRequest();
            request.setAgentId(agentID);
            request.setTxnKey(txn_key);
            request.setTpinStatus(tpinStatus);
            request.setOtp(til_update_tpin_otp.getEditText().getText().toString().trim());

            RetrofitClient.getClient(getApplicationContext())
                    .updateTPINStatus(request).enqueue(new Callback<MainResponse2>() {
                        @Override
                        public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                MainResponse2 res = response.body();
                                if("0".equalsIgnoreCase(res.getStatus())){
                                    Util.SavePrefData(getApplicationContext(),Keys.TPIN_STATUS,tpinStatus);
                                }
                                update_tpin_message.setText(res.getMessage());
                                Util.showView(update_tpin_message);
                            }else {
                                update_tpin_message.setText("No Server Response");
                                Util.showView(update_tpin_message);
                                L.toastS(getApplicationContext(), "No Server Response");
                            }
                            til_update_tpin_otp.getEditText().setText("");
                        }

                        @Override
                        public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                            update_tpin_message.setText(t.getLocalizedMessage());
                            Util.showView(update_tpin_message);
                            pd.dismiss();
                        }
                    });
        }
    }

    private void requestTpinStatusOtp() {
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

                                update_tpin_message.setText(res.getMessage());
                                Util.showView(update_tpin_message);
                            }else {
                                update_tpin_message.setText("No Server Response");
                                Util.showView(update_tpin_message);
                                L.toastS(getApplicationContext(), "No Server Response");
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                            update_tpin_message.setText(t.getLocalizedMessage());
                            Util.showView(update_tpin_message);
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