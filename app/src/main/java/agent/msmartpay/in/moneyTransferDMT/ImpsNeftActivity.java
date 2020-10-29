package agent.msmartpay.in.moneyTransferDMT;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import agent.msmartpay.in.MainActivity;
import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

/**
 * Created by Smartkinda on 6/19/2017.
 */

public class ImpsNeftActivity extends BaseActivity {

    private Button btnProceed;
    private EditText editAmount, editRemark;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_imps_neft_transaction = HttpURL.IMPS_NEFT_TRANSACTION;
    private String url_find_sender = HttpURL.FIND_SENDER;
    private RadioGroup radioPayGroup;
    private RadioButton radioPayButton;
    private String radioButtonValue;
    private String BeneName, BeneAccountNumber, BeneBankName, BeneStatus, BeneVarify, IFSCcode, BeneCode;
    private TextView tvBeneName, tvBeneAccountNumber, tvIFSCcode;
    private SharedPreferences sharedPreferences;
    private String agentID, txnKey, SessionID, mobileNumber;
    private String transactionAmount, Remark;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_imps_neft_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Payment");

        context = ImpsNeftActivity.this;
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

        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        BeneName = getIntent().getStringExtra("BeneName");
        BeneAccountNumber = getIntent().getStringExtra("BeneAccountNumber");
        BeneBankName = getIntent().getStringExtra("BeneBankName");
        BeneStatus = getIntent().getStringExtra("BeneStatus");
        BeneVarify = getIntent().getStringExtra("BeneVarify");
        IFSCcode = getIntent().getStringExtra("IFSCcode");
        BeneCode = getIntent().getStringExtra("beneCodeData");

        L.m2("check_beneCodeData--->", BeneCode.toString());

        tvBeneName = (TextView) findViewById(R.id.tv_payee_name);
        tvBeneAccountNumber = (TextView) findViewById(R.id.tv_payee_acc_no);
        tvIFSCcode = (TextView) findViewById(R.id.tv_payee_ifsc);
        editAmount =  findViewById(R.id.edit_amount);
        editRemark =  findViewById(R.id.edit_remark);
        btnProceed =  findViewById(R.id.txtsubmit);
        radioPayGroup=(RadioGroup)findViewById(R.id.radioGroup);
        editAmount.requestFocus();

        tvBeneName.setText(BeneName);
        tvBeneAccountNumber.setText(BeneAccountNumber);
        tvIFSCcode.setText(IFSCcode);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId=radioPayGroup.getCheckedRadioButtonId();
                radioPayButton=(RadioButton)findViewById(selectedId);
                if(radioPayGroup.getCheckedRadioButtonId() != -1){
                    radioButtonValue = radioPayButton.getText().toString();
                }

                if (TextUtils.isEmpty(editAmount.getText().toString().trim())) {
                    Toast.makeText(context, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                    editAmount.requestFocus();
                }
                else if(radioPayGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(context, "Please Select Pay Option", Toast.LENGTH_SHORT).show();
                } else {
                    transactionAmount = editAmount.getText().toString().trim();
                    Remark = editRemark.getText().toString().trim();
                    ImpsNeftTransactionRequest();
                }
            }
        });
    }

    //==========For Imps Neft Transaction=================
    private void ImpsNeftTransactionRequest() {
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
                    .put("BeneCode", BeneCode.toString())
                    .put("Amount", transactionAmount.toString())
                    .put("TransactionType", radioButtonValue.toString())
                    .put("Remark", Remark == null ? "" : Remark);
            Log.e("Request--transaction",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_imps_neft_transaction, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----transaction>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called--transaction", url_imps_neft_transaction);
                                    L.m2("url data--transaction", object.toString());

                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();

                                    JSONObject jsonDetails = object.getJSONObject("bankDetails");
                                    Intent intent = new Intent(context, ImpsNeftTxnRecieptActivity.class);
                                    intent.putExtra("accountNumber", jsonDetails.getString("accountNumber").toString());
                                    intent.putExtra("bankName", jsonDetails.getString("bankName").toString());
                                    intent.putExtra("beneName", jsonDetails.getString("beneName").toString());
                                    intent.putExtra("Ifsc", jsonDetails.getString("Ifsc").toString());
                                    intent.putExtra("transactionRefNo", object.getString("transactionRefNo").toString());
                                   //intent.putExtra("UTR", object.getString("UTR").toString());
                                    intent.putExtra("amount", object.getString("amount").toString());
                                    intent.putExtra("remittanceType", object.getString("remittanceType").toString());
                                    intent.putExtra("transactionStatus", object.getString("message").toString());
                                    startActivity(intent);
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
                                    Intent intent = new Intent(context, MoneyTransferActivity1.class);
                                    startActivity(intent);
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
