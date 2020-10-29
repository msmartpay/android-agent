package agent.msmartpay.in.moneyTransferDMT;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
 * Created by Smartkinda on 6/19/2017.
 */

public class ChangeStatusOTPActivity extends BaseActivity {

    private Button btnSubmit, btnResendOTP;
    private EditText editOTP;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private SharedPreferences sharedPreferences;
    private String agentID, txnKey, SessionID, mobileNumber, beneCode, TransactionRefNo;
    private String url_confirm_add_beneficiary = HttpURL.CONFIRM_ADD_BENEFICIARY;
    private String url_re_send_otp = HttpURL.RE_SEND_OTP;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_change_status_otp_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Confirmation");

        context = ChangeStatusOTPActivity.this;
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.requestFocusFromTouch();
                return false;
            }
        });

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        if (getIntent().hasExtra("beneCode")){
            beneCode = getIntent().getStringExtra("beneCode");
        }
        TransactionRefNo = getIntent().getStringExtra("TransactionRefNo");
      //  beneCode = getIntent().getStringExtra("beneCode");
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        editOTP =  findViewById(R.id.edit_otp);
        btnSubmit =  findViewById(R.id.btn_submit);
        btnResendOTP =  findViewById(R.id.btn_resend_otp);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editOTP.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "Please enter OTP first !!!", Toast.LENGTH_SHORT).show();
                }else{
                    confirmAddBeneficiaryRequest();
                }
            }
        });

        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Re_sendOTPRequest();
                }
        });
    }

    //Json request for add Beneficiary Confirm
    private void confirmAddBeneficiaryRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", mobileNumber.toString())
                    .put("BeneCode", beneCode.toString())
                    .put("TransactionRefNo", TransactionRefNo.toString())
                    .put("otp", editOTP.getText().toString().trim());
            L.m2("url-called-C_AddBene", url_confirm_add_beneficiary);
            Log.e("Request--confirmAddBene",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_confirm_add_beneficiary, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----ConfirmAddBeneficiary>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url data-C_AddBene", object.toString());
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    pd.dismiss();
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    //==================For Re-send OTP======================
    //Json request For Re-send OTP
    private void Re_sendOTPRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
          //  L.m2("check_beneCode--2>", beneCode.toString());
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", mobileNumber.toString())
                    .put("beneCode", beneCode.toString());
            Log.e("Request--Re_send",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_re_send_otp, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----Re_send>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called--Re_send", url_re_send_otp);
                                    L.m2("url data--Re_send", object.toString());
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    pd.dismiss();
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
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
