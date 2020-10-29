package in.msmartpay.agent.moneyTransferNew;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ImpsNeftTxnRecieptActivity extends BaseActivity {

    private TextView tv_bank_rrn, tv_sender_id, tv_sender_name,  tv_bene_mobile, tviewTransferedAmount, tviewBeneficiaryName
            , tviewBankName, tviewAccountNo, tviewIFSCCode, tv_date_time;
    private SharedPreferences sharedPreferences;
    private String SenderId, SenderName, BeneMobile, accountNumber, bankName, beneName, Ifsc, BankRRN, amount, TxnDate, TxnTime;
    private Button btnDone;
    private String url_find_sender = HttpURL.FIND_SENDER;
    private String agentID, txnKey, mobileNumber;
    private ProgressDialog pd;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dmr_imps_neft_txn_reciept);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction Receipt");

        context = ImpsNeftTxnRecieptActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        SenderId = getIntent().getStringExtra("SenderId");
        SenderName = getIntent().getStringExtra("SenderName");
        BeneMobile = getIntent().getStringExtra("BeneMobile");
        accountNumber = getIntent().getStringExtra("accountNumber");
        bankName = getIntent().getStringExtra("bankName");
        beneName = getIntent().getStringExtra("beneName");
        Ifsc = getIntent().getStringExtra("Ifsc");
        BankRRN = getIntent().getStringExtra("BankRRN");
        amount = getIntent().getStringExtra("amount");
        TxnDate = getIntent().getStringExtra("TxnDate");
        TxnTime = getIntent().getStringExtra("TxnTime");

        tv_bank_rrn = (TextView) findViewById(R.id.tv_bank_rrn);
        tv_sender_id = (TextView) findViewById(R.id.tv_sender_id);
        tv_sender_name = (TextView) findViewById(R.id.tv_sender_name);
        tv_bene_mobile = (TextView) findViewById(R.id.tv_bene_mobile);
        tviewIFSCCode = (TextView) findViewById(R.id.tv_ifsc_code);
        tviewTransferedAmount = (TextView) findViewById(R.id.tv_transfer_amount);
        tviewBeneficiaryName = (TextView) findViewById(R.id.tv_bene_name);
        tviewBankName = (TextView) findViewById(R.id.tv_bank_name);
        tviewAccountNo = (TextView) findViewById(R.id.tv_acc_number);
        tv_date_time = (TextView) findViewById(R.id.tv_date_time);
        btnDone =  findViewById(R.id.btn_done);

        tv_bank_rrn.setText(BankRRN);
        tv_sender_id.setText(SenderId);
        tv_sender_name.setText(SenderName);
        tv_bene_mobile.setText(BeneMobile);
        tviewIFSCCode.setText(Ifsc);
        tviewTransferedAmount.setText("\u20B9 "+amount);
        tviewBeneficiaryName.setText(beneName);
        tviewBankName.setText(bankName);
        tviewAccountNo.setText(accountNumber);
        tviewIFSCCode.setText(Ifsc);
        tv_date_time.setText(TxnDate+" "+TxnTime);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findSenderRequest();
            }
        });
    }


    /*******Find Sender on Success********/
    private void findSenderRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber);

            Log.e("Request", jsonObjectReq.toString());
            L.m2("url-called", url_find_sender);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("url data", object.toString());
                            MainActivity.jsonObjectStatic = object;
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    Intent intent = new Intent(context, MoneyTransferActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

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