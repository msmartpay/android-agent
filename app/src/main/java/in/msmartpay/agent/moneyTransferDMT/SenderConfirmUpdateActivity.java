package in.msmartpay.agent.moneyTransferDMT;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

/**
 * Created by Smartkinda on 7/5/2017.
 */

public class SenderConfirmUpdateActivity extends BaseActivity {

    private TextView tviewSenderMobileNo, tviewFirstName, tviewLastName, tviewDateOfBirth, tviewPinCode, tviewAddress;
    private EditText editTextOTP;
    private String mobileNumber, FirstName, LastName, Dob, Pincode, Address;
    private Button btnConfirmSubmit;
    private ProgressDialog pd;
    private String url_confirm_sender_details_update = HttpURL.SENDER_CONFIRM_DETAILS;
    private String agentID, txnKey, SessionID;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String url_find_sender = HttpURL.FIND_SENDER;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_sender_confirm_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Confirm Details Update");

        context = SenderConfirmUpdateActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        FirstName = getIntent().getStringExtra("FirstName");
        LastName = getIntent().getStringExtra("LastName");
        Dob = getIntent().getStringExtra("Dob");
        Pincode = getIntent().getStringExtra("Pincode");
        Address = getIntent().getStringExtra("Address");

        tviewSenderMobileNo = (TextView) findViewById(R.id.tv_sender_mobile_no);
        tviewFirstName = (TextView) findViewById(R.id.tv_first_name);
        tviewLastName = (TextView) findViewById(R.id.tv_last_name);
        tviewDateOfBirth = (TextView) findViewById(R.id.tv_dob);
        tviewPinCode = (TextView) findViewById(R.id.tv_pin_code);
        tviewAddress = (TextView) findViewById(R.id.tv_address);
        editTextOTP =  findViewById(R.id.edit_otp);
        btnConfirmSubmit =  findViewById(R.id.btn_confirm_submit);

        tviewSenderMobileNo.setText(mobileNumber);
        tviewFirstName.setText(FirstName);
        tviewLastName.setText(LastName);
        tviewDateOfBirth.setText(Dob);
        tviewPinCode.setText(Pincode);
        tviewAddress.setText(Address);

        btnConfirmSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editTextOTP.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "Please enter OTP first !!!", Toast.LENGTH_SHORT).show();
                }else{
                    confirmSenderDetailsUpdateRequest();
                }

            }
        });
    }

    //==============Confirm Update Sender with OTP==============
    private void confirmSenderDetailsUpdateRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            L.m2("url-called-ConSenderReq", url_confirm_sender_details_update);
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", mobileNumber.toString())
                    .put("FirstName", FirstName.toString())
                    .put("LastName", LastName.toString())
                    .put("Dob", Dob.toString())
                    .put("Pincode", Pincode.toString())
                    .put("Address", Address.toString())
                    .put("otp", editTextOTP.getText().toString().trim());
            Log.e("Request--ConSenderReq",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_confirm_sender_details_update, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("url data-ConSenderRes", object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    findSenderRequest();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    //find Current update
    private void findSenderRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txnKey)
                    .put("SessionId", SessionID)
                    .put("SenderId", mobileNumber);
            Log.e("Request",jsonObjectReq.toString());
            L.m2("url-called", url_find_sender);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("url data", object.toString());
                            try {

                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    MainActivity.jsonObjectStatic=object;
                                    editor.putString("ResisteredMobileNo", mobileNumber.toString());
                                    editor.commit();
                                    finish();
                                }
                                else {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    // resisterSenderDialog(object.getString("message").toString());
                                }
                            } catch (JSONException e) {
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
        onBackPressed();
        return true;
    }
}