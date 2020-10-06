package msmartpay.in.myWallet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import msmartpay.in.MainActivity;
import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

public class TicketActivity extends BaseActivity {
    private Dialog dialog_status;
    private String url;
    private TextView operatorID, MobileNo, TransId;
    private Button ticket;
    private JSONObject data = null;
    private EditText remarkEdit;
    private String tranStatus, tranId, agentId, remark;
    private Context context;
    private ProgressDialog pd;
    private String ticket_url= HttpURL.TICKET_URL;
    private String txn_key = "";
    private String mobile_num, operator, agentID;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Place Ticket");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TicketActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentId = sharedPreferences.getString("agent_id", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txn_key = sharedPreferences.getString("txn-key", null);
        txn_key = txn_key.replaceAll("~", "/");
        mobile_num = getIntent().getStringExtra("Mobno");
        operator = getIntent().getStringExtra("operator");
        tranId = getIntent().getStringExtra("trnsid");
        tranStatus = getIntent().getStringExtra("status");

        operatorID = (TextView) findViewById(R.id.operator_ticketactivity);
        MobileNo = (TextView) findViewById(R.id.mobileno_ticketactivity);
        TransId = (TextView) findViewById(R.id.transaction_ticketactivity);
        remarkEdit =  findViewById(R.id.remark);
        ticket =  findViewById(R.id.ticketsubmit);

        operatorID.setText(operator);
        MobileNo.setText(mobile_num);
        TransId.setText(tranId);

        ticket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                remark = remarkEdit.getText().toString();
              remark=  remark.replaceAll(" ","_");
                if (remark != null && remark.length() > 0) {
                    pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pd.setCancelable(true);
                    pd.show();
                    try{
                        JSONObject jsonObjectReq=new JSONObject()
                                .put("agent_id", agentID)
                                .put("txn_key", txn_key)
                                .put("transactionId", tranId)
                                .put("ticketmessage", remark);

                        L.m2("url-ticket", ticket_url);
                        L.m2("Request--ticket",jsonObjectReq.toString());
                        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, ticket_url, jsonObjectReq, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject data) {

                                try {
                                    L.m2("data--ticket",data.toString());
                                    pd.dismiss();
                                    dialog_status = new Dialog(context);
                                    dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog_status.setContentView(R.layout.sk_maindialog);
                                    dialog_status.setCancelable(true);
                                    ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
                                    if (data.get("Status") != null && data.get("Status").equals("0")) {
                                        pd.dismiss();
                                        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
                                        text.setText((String) data.get("message"));
                                        statusImage.setImageResource(R.drawable.trnsuccess);
                                        final Button trans_status =  dialog_status.findViewById(R.id.trans_status_button);
                                        trans_status.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog_status.dismiss();
                                                Intent intent = new Intent();
                                                intent.setClass(context, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    } else if (data.get("Status") != null && data.get("Status").equals("1")) {
                                        pd.dismiss();
                                        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
                                        text.setText((String) data.get("message"));
                                        final Button trans_status =  dialog_status.findViewById(R.id.trans_status_button);
                                        statusImage.setImageResource(R.drawable.failed);
                                        trans_status.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog_status.dismiss();
                                            }
                                        });
                                    } else if (data.get("Status") != null && data.get("Status").equals("2")) {
                                        Toast.makeText(context, data.get("message") + "", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dialog_status.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                           @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json");
                                return headers;
                            }
                        };
                        getSocketTimeOut(objectRequest);
                        Mysingleton.getInstance(context).addToRequsetque(objectRequest);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Please Enter Remark.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
