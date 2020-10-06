package msmartpay.in;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

public class ResetPasswordActivity extends BaseActivity {

    private Context context;
    private LinearLayout linear_submit_pass;
    private EditText old_pass, new_pass, confirm_pass;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog pd;
    private Dialog dialog_status;
    private String agentID, txn_key = "";
    private String reset_url = HttpURL.CHANGE_PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pass_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = ResetPasswordActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agent_id", null);
        agentID = agentID.substring(10, agentID.length());
        txn_key = sharedPreferences.getString("txn-key", null);

        linear_submit_pass = (LinearLayout) findViewById(R.id.linear_submit_pass);
        old_pass =  findViewById(R.id.old_pass);
        new_pass =  findViewById(R.id.new_pass);
        confirm_pass =  findViewById(R.id.confirm_pass);

        linear_submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }

    private void resetPasswordRequest() {

        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(true);
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("oldpass", old_pass.getText().toString().trim())
                    .put("newpass", new_pass.getText().toString().trim())
                    .put("txn_key", txn_key);

            L.m2("url-reset", reset_url);
            L.m2("Request--reset",jsonObjectReq.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, reset_url,jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            try {
                                pd.dismiss();
                                L.m2("data-reset", data.toString());
                                if (data.getString("response-code") != null && data.getString("response-code").equals("0")) {
                                    changePasswordSuccesssDialog(data.getString("response-message").toString(), Integer.parseInt(data.getString("response-code")));
                                }else{
                                    changePasswordSuccesssDialog(data.getString("response-message").toString(), Integer.parseInt(data.getString("response-code")));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, " "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePasswordSuccesssDialog(String msg, int i) {
        dialog_status = new Dialog(context);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);

        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        Button trans_status =  dialog_status.findViewById(R.id.trans_status_button);

        text.setText(msg);

        if(i == 0) {
            statusImage.setImageResource(R.drawable.trnsuccess);
            trans_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    dialog_status.dismiss();
                }
            });
        }else{
                statusImage.setImageResource(R.drawable.failed);
                trans_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_status.dismiss();
                    }
                });
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