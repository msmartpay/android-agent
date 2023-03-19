package in.msmartpayagent.user;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.msmartpayagent.LoginActivity;
import in.msmartpayagent.R;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainResponse;
import in.msmartpayagent.network.model.user.ChangePasswordRequest;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ResetPasswordActivity extends BaseActivity {

    private Context context;
    private LinearLayout linear_submit_pass;
    private EditText old_pass, new_pass, confirm_pass;
    private ProgressDialog pd;
    private Dialog dialog_status;
    private String agentID, txn_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pass_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = ResetPasswordActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        linear_submit_pass = (LinearLayout) findViewById(R.id.linear_submit_pass);
        old_pass = (EditText) findViewById(R.id.old_pass);
        new_pass = (EditText) findViewById(R.id.new_pass);
        confirm_pass = (EditText) findViewById(R.id.confirm_pass);

        linear_submit_pass.setOnClickListener(view -> {
            if(TextUtils.isEmpty(old_pass.getText().toString().trim()) || old_pass.getText().toString().trim().length() < 5){
                old_pass.requestFocus();
                Toast.makeText(context, "Enter correct old password with minimum 6 digits !!!", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(new_pass.getText().toString().trim()) || new_pass.getText().toString().trim().length() < 5){
                new_pass.requestFocus();
                Toast.makeText(context, "Enter correct new password with minimum 6 digits !!!", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(confirm_pass.getText().toString().trim()) || confirm_pass.getText().toString().trim().length() < 5){
                confirm_pass.requestFocus();
                Toast.makeText(context, "Enter correct confirm password with minimum 6 digits !!!", Toast.LENGTH_SHORT).show();
            }else if(new_pass.getText().toString().trim().equals(old_pass.getText().toString().trim())){
                new_pass.requestFocus();
                Toast.makeText(context, "New and old password must not be same !!!", Toast.LENGTH_SHORT).show();
            }else if(!new_pass.getText().toString().trim().equals(confirm_pass.getText().toString().trim())){
                confirm_pass.requestFocus();
                Toast.makeText(context, "New Password did not match with confirm password !!!", Toast.LENGTH_SHORT).show();
            }else {
                  resetPasswordRequest();
            }
        });
    }

    private void resetPasswordRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setNewpass(new_pass.getText().toString().trim());
            request.setOldpass(old_pass.getText().toString().trim());

            RetrofitClient.getClient(getApplicationContext())
                    .changePass(request).enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse> call, @NotNull retrofit2.Response<MainResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse res = response.body();
                        if (res.getResponseCode() != null) {
                            changePasswordSuccesssDialog(res.getResponseMessage(), Integer.parseInt(res.getResponseCode()));
                            //L.toastS(getApplicationContext(), res.getResponseMessage());

                        } else {
                            if (res.getResponseMessage() != null)
                                L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    }else {

                        L.toastS(getApplicationContext(), "No Server Response");
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

    private void changePasswordSuccesssDialog(String msg, int i) {
        dialog_status = new Dialog(context);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);

        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);

        text.setText(msg);

        if(i == 0) {
            statusImage.setImageResource(R.drawable.trnsuccess);
            trans_status.setOnClickListener(view -> {
                finish();
                dialog_status.dismiss();
                Util.clearMyPref(getApplicationContext());
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            });
        }else{
                statusImage.setImageResource(R.drawable.failed);
                trans_status.setOnClickListener(view -> dialog_status.dismiss());
        }
        dialog_status.show();
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