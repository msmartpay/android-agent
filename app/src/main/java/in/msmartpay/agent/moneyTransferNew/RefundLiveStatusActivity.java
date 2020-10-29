package in.msmartpay.agent.moneyTransferNew;

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

public class RefundLiveStatusActivity extends BaseActivity {

    private TextView refund_sender_id, refund_trans_number, refund_trans_status, refund_amount, refund_trans_desc, refund_tran_type, refund_bank_ref_id, refund_timestamp, refund_message;
    private Button btn_refund_done;
    private String agentID, txnKey, TranNo, mobileNumber, OtpString, fromIntent;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private ProgressDialog pd;
    private String url_refund_trans_status = HttpURL.REFUND_TRANS_STATUS;
    private String url_refund_transaction = HttpURL.REFUND_TRANSACTION;
    private String url_confirm_refund_transaction = HttpURL.CONFIRM_REFUND_TRANSACTION;
    private String url_find_sender = HttpURL.FIND_SENDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dmr_refund_live_status_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Live Status");

        context = RefundLiveStatusActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);
        TranNo = getIntent().getStringExtra("TranNo");
        fromIntent = getIntent().getStringExtra("fromIntent");

        refund_sender_id = (TextView) findViewById(R.id.refund_sender_id);
        refund_trans_number = (TextView) findViewById(R.id.refund_trans_number);
        refund_trans_status = (TextView) findViewById(R.id.refund_trans_status);
        refund_amount = (TextView) findViewById(R.id.refund_amount);
        refund_trans_desc = (TextView) findViewById(R.id.refund_trans_desc);
        refund_tran_type = (TextView) findViewById(R.id.refund_tran_type);
        refund_bank_ref_id = (TextView) findViewById(R.id.refund_bank_ref_id);
        refund_timestamp = (TextView) findViewById(R.id.refund_timestamp);
        refund_message = (TextView) findViewById(R.id.refund_message);
        btn_refund_done =  findViewById(R.id.btn_refund_done);

        try {
            refundLiveStatusRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_refund_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refundBalanceDialog("", 1);
            }
        });
    }

    //==========ForLiveStatus==========================
    private void refundLiveStatusRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber)
                    .put("TransactionRefNo", TranNo);

            L.m2("url--tranStatus", url_refund_trans_status);
            L.m2("Request--tranStatus", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_refund_trans_status, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("tranStatus-->" + object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("resp--tranStatus", object.toString());

                                    refund_sender_id.setText(object.getString("SenderId"));
                                    refund_trans_number.setText(object.getString("TransactionRefNo"));
                                    refund_trans_status.setText(object.getString("Tran_Status"));
                                    refund_amount.setText(object.getString("Amount"));
                                    refund_trans_desc.setText(object.getString("Tran_Desc"));
                                    refund_tran_type.setText(object.getString("Tran_Type"));
                                    refund_bank_ref_id.setText(object.getString("Bank_Ref_Id"));
                                    refund_timestamp.setText(object.getString("timestamp"));
                                    refund_message.setText(object.getString("message"));
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
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

    //==============Dialog=============================
    public void refundBalanceDialog(final String message, final int i) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.new_dmr_dialog_sender_mobile_dmt);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit =  d.findViewById(R.id.btn_push_submit);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);
        final EditText editMobileNo =  d.findViewById(R.id.edit_push_balance);
        final TextView textView_message = (TextView) d.findViewById(R.id.tview_otp_info);
        final TextView title = (TextView) d.findViewById(R.id.title);

        if (i == 1) {
            title.setText("Inquiry Success");
            textView_message.setText("Are you sure, you want to refund!");
            textView_message.setVisibility(View.VISIBLE);
            editMobileNo.setVisibility(View.GONE);
        }
        if (i == 2) {
            title.setText("OTP Confirmation");
            textView_message.setVisibility(View.VISIBLE);
            textView_message.setText(message);
            editMobileNo.setVisibility(View.VISIBLE);
            editMobileNo.setHint("Enter Otp");
        }

        if (i == 3) {
            title.setText("Success");
            textView_message.setText(message);
            textView_message.setVisibility(View.VISIBLE);
            editMobileNo.setVisibility(View.GONE);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 1) {
                    d.dismiss();
                    refundTransactionRequest();
                }
                if (i == 2) {
                    OtpString = editMobileNo.getText().toString().trim();
                    if (TextUtils.isEmpty(OtpString)) {
                        Toast.makeText(context, "Please Enter Otp", Toast.LENGTH_SHORT).show();
                    } else {
                        d.dismiss();
                        confirmRefundRequest();
                    }
                }
                if (i == 3) {
                    if (fromIntent.equalsIgnoreCase("fromSenderHistoryActivity")) {
                        d.dismiss();
                        RefundLiveStatusActivity.this.finish();
                    } else if (fromIntent.equalsIgnoreCase("fromSenderHistoryFragment")) {
                        d.dismiss();
                        findSenderRequest();
                    }else if (fromIntent.equalsIgnoreCase("RefundFragment")) {
                        d.dismiss();
                        RefundLiveStatusActivity.this.finish();
                    }
                }
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

    //==========ForRefundTransaction===================
    private void refundTransactionRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber)
                    .put("TransactionRefNo", TranNo);

            L.m2("url--transaction", url_refund_transaction);
            L.m2("Request--transaction", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_refund_transaction, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("transaction-->" + object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("resp--transaction", object.toString());
                                    //Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    refundBalanceDialog(object.getString("message"), 2);
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
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

    //==========ForRefundTransaction===================
    private void confirmRefundRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber)
                    .put("TransactionRefNo", TranNo)
                    .put("OTP", OtpString);

            L.m2("url--transaction", url_confirm_refund_transaction);
            L.m2("Request--transaction", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_confirm_refund_transaction, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("transaction-->" + object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("resp--transaction", object.toString());
                                    //Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    refundBalanceDialog(object.getString("message"), 3);
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
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

    //==========ForFindSender==========================
    private void findSenderRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txnKey)
                    .put("AgentID", agentID)
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
                                    JSONObject senderDetailsObject = object.getJSONObject("SenderDetails");
                                    editor.putString("SenderId", senderDetailsObject.getString("SenderId"));
                                    editor.putString("SenderName", senderDetailsObject.getString("Name"));
                                    editor.putString("ResisteredMobileNo", mobileNumber);
                                    editor.commit();
                                    Intent intent = new Intent(context, MoneyTransferActivity.class);
                                    intent.putExtra("SenderLimit_Detail_BeniList", object.toString());
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
                    Toast.makeText(context, "Server Error : ", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
