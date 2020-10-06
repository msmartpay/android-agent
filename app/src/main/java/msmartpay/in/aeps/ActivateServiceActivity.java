package msmartpay.in.aeps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import msmartpay.in.MainActivity;
import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

public class ActivateServiceActivity extends BaseActivity {

    public static JSONObject jsonObjectStatic = new JSONObject();
    private String requestOTP = HttpURL.AS_RequestOTP;
    private String verifyOTP = HttpURL.AS_VerifyMobile;
    private String onboard = HttpURL.AS_UserOnboard;
    private String mobile,otp;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private ProgressDialog pd;
    private String agentID, txnKey;
    private EditText mobile_edit,otp_edit,f_name_edit,m_name_edit,l_name_edit;
    private TextView message;
    private Button request_otp_btn,verify_btn,onboard_btn,back_btn;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_service);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Activate Service");

        context = ActivateServiceActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);

        f_name_edit=findViewById(R.id.f_name);
        m_name_edit=findViewById(R.id.m_name);
        l_name_edit=findViewById(R.id.l_name);

        mobile_edit=findViewById(R.id.mobile);
        otp_edit=findViewById(R.id.otp);
        message=(TextView)findViewById(R.id.message);

        mobile_edit.setText(sharedPreferences.getString("agentMobile",null));

        request_otp_btn=findViewById(R.id.requestOtp);
        verify_btn=findViewById(R.id.verify);
        onboard_btn=findViewById(R.id.onboard);
        back_btn=findViewById(R.id.back);

        request_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    requestOtp();
            }
        });
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOTP();
            }
        });
        onboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(f_name_edit==null || "".equalsIgnoreCase(f_name_edit.getText().toString())){
                    Toast.makeText(context, "Please Enter First Name!", Toast.LENGTH_SHORT).show();
                }else{
                    onboard();
                }

            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    //=============Request Otp=================================
    private void requestOtp() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txnKey)
                    .put("AgentID", agentID)
                    .put("Mobile", mobile_edit.getText().toString().trim());

            L.m2("Req-findSender", jsonObjectReq.toString());
            L.m2("Url-Request", requestOTP);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, requestOTP, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObjectStatic = object;

                            try {
                                L.m2("url-called", requestOTP);
                                L.m2("resp-requestOTP", object.toString());
                                if (object.getString("Status").equalsIgnoreCase("0")) {

                                    message.setText(object.getString("message"));
                                    message.setVisibility(View.VISIBLE);
                                    request_otp_btn.setVisibility(View.GONE);
                                    verify_btn.setVisibility(View.VISIBLE);
                                    otp_edit.setVisibility(View.VISIBLE);

                                }else if (object.getString("Status").equalsIgnoreCase("2")) {

                                    message.setText("User Mobile already verifieed. Submit on boarding details...");
                                    message.setVisibility(View.VISIBLE);
                                    otp_edit.setVisibility(View.GONE);
                                    verify_btn.setVisibility(View.GONE);

                                    f_name_edit.setVisibility(View.VISIBLE);
                                    m_name_edit.setVisibility(View.VISIBLE);
                                    l_name_edit.setVisibility(View.VISIBLE);
                                    onboard_btn.setVisibility(View.VISIBLE);

                                }  else {

                                    message.setText(object.getString("message"));
                                    message.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            if(pd.isShowing())
                pd.dismiss();

            e.printStackTrace();
        }
    }
    //=============Verify OTP=================================
    private void verifyOTP() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txnKey)
                    .put("AgentID", agentID)
                    .put("Mobile", mobile_edit.getText().toString().trim())
                    .put("OTP", otp_edit.getText().toString().trim());

            L.m2("Req-findSender", jsonObjectReq.toString());
            L.m2("Url-findSender", verifyOTP);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, verifyOTP, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObjectStatic = object;
                            System.out.println("resp--verifyOTP" + object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    L.m2("url-called", verifyOTP);
                                    L.m2("resp-verifyOTP", object.toString());
                                    message.setText(object.getString("message"));
                                    message.setVisibility(View.VISIBLE);
                                    otp_edit.setVisibility(View.GONE);
                                    verify_btn.setVisibility(View.GONE);

                                    f_name_edit.setText(sharedPreferences.getString("agentMobile",null));
                                    l_name_edit.setText(sharedPreferences.getString("agentMobile",null));

                                    f_name_edit.setVisibility(View.VISIBLE);
                                    m_name_edit.setVisibility(View.VISIBLE);
                                    l_name_edit.setVisibility(View.VISIBLE);
                                    onboard_btn.setVisibility(View.VISIBLE);

                                } else {

                                    message.setText(object.getString("message"));
                                    message.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            if(pd.isShowing())
                pd.dismiss();
            e.printStackTrace();
        }
    }

    //=============Verify OTP=================================
    private void onboard() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txnKey)
                    .put("AgentID", agentID)
                    .put("Mobile", mobile_edit.getText().toString().trim())
                    .put("firstname", f_name_edit.getText().toString().trim())
                    .put("m_name", m_name_edit.getText().toString().trim())
                    .put("lastname", l_name_edit.getText().toString().trim());

            L.m2("Req-onboard", jsonObjectReq.toString());
            L.m2("Url-onboard", onboard);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, onboard, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObjectStatic = object;
                            System.out.println("resp-onboard" + object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    L.m2("url-called ", onboard);
                                    L.m2("resp-onboard", object.toString());
                                    message.setText(object.getString("message"));
                                    message.setVisibility(View.VISIBLE);


                                    f_name_edit.setText("");
                                    m_name_edit.setText("");
                                    l_name_edit.setText("");
                                    f_name_edit.setVisibility(View.GONE);
                                    m_name_edit.setVisibility(View.GONE);
                                    l_name_edit.setVisibility(View.GONE);

                                    onboard_btn.setVisibility(View.GONE);
                                    back_btn.setVisibility(View.VISIBLE);

                                } else {

                                    message.setText(object.getString("message"));
                                    message.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            if(pd.isShowing())
                pd.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
