package agent.msmartpay.in.moneyTransferDMT;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

/**
 * Created by Smartkinda on 7/5/2017.
 */

public class RefundConfirmActivity extends BaseActivity {


    private ProgressDialog pd;
    private String agentID, txnKey, SessionID, ResisteredMobileNo, OrgTransactionRefNo, TransferType, Amount, BeneCode;
    private SharedPreferences sharedPreferences;
    private Button btnSubmit, btnResendOTP;
    private EditText editOTP;
    private TextView tvTitle;
    private JSONObject jsonObject;
    private String url_confirm_refund_transaction = HttpURL.CONFIRM_REFUND_TRANSACTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_change_status_otp_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Refund Confirmation");

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        ResisteredMobileNo = sharedPreferences.getString("ResisteredMobileNo", null);

        OrgTransactionRefNo = getIntent().getStringExtra("OrgTransactionRefNo");
        TransferType = getIntent().getStringExtra("TransferType");
        Amount = getIntent().getStringExtra("Amount");
        BeneCode = getIntent().getStringExtra("BeneCode");


        editOTP =  findViewById(R.id.edit_otp);
        tvTitle = (TextView) findViewById(R.id.tview_title);
        btnSubmit =  findViewById(R.id.btn_submit);
        btnResendOTP =  findViewById(R.id.btn_resend_otp);
        btnResendOTP.setVisibility(View.GONE);
        tvTitle.setText("Please Enter OTP below ");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editOTP.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "Please enter OTP first !!!", Toast.LENGTH_SHORT).show();
                }else{
                    hideKeyBoard(editOTP);
                    confirmRefundTransactionRequest();
                }
            }
        });

    }

    //================For Confirm Refund==========================
    private void confirmRefundTransactionRequest() {
        pd = ProgressDialog.show(RefundConfirmActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {

            L.m2("url-called--RefundCon", url_confirm_refund_transaction);
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", ResisteredMobileNo.toString())
                    .put("OrgTransactionRefNo", OrgTransactionRefNo.toString())
                    .put("TransferType", TransferType.toString())
                    .put("Amount", Amount.toString())
                    .put("BeneCode", BeneCode.toString())
                    .put("otp", editOTP.getText().toString().trim());
            Log.e("Request--RefundCon",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_confirm_refund_transaction, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("url data--RefundCon", object.toString());
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----RefundCon>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    successTransactionDialog(object.getString("message").toString());
                                }
                                else {
                                    Toast.makeText(RefundConfirmActivity.this, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    editOTP.setText("");
                                    tvTitle.setText(object.getString("message").toString());
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(RefundConfirmActivity.this, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(RefundConfirmActivity.this).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }

    //Confirmation Dialog
    public void successTransactionDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(RefundConfirmActivity.this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit =  d.findViewById(R.id.btn_push_submit);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);

        final TextView tvConfirmation = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        tvConfirmation.setText(msg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(jsonObject.getString("Status").equalsIgnoreCase("0")){
                        Intent intent = new Intent(RefundConfirmActivity.this, MoneyTransferActivity1.class);
                        startActivity(intent);
                        RefundConfirmActivity.this.finish();
                        d.dismiss();
                    }else{
                        d.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                d.cancel();
            }
        });
        btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                d.cancel();
            }
        });
        d.show();
    }

    public void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}