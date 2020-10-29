package agent.msmartpay.in.moneyTransferNew;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
    private RadioGroup radioPayGroup;
    private RadioButton radioPayButton;
    private String radioButtonValue;
    private String BeneName, BeneAccountNumber, BeneBankName, IMPS, NEFT, IFSCcode, RecipientId, Channel, RecipientIdType, BeneMobile;
    private TextView tvBeneName, tvBeneAccountNumber, tvIFSCcode;
    private SharedPreferences sharedPreferences;
    private String agentID, txnKey, mobileNumber;
    private String transactionAmount, Remark;
    private String SenderId, SenderName;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dmr_imps_neft_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Payment");

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

        context = ImpsNeftActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        SenderId = sharedPreferences.getString("SenderId", null);
        SenderName = sharedPreferences.getString("SenderName", null);
        BeneName = getIntent().getStringExtra("BeneName");
        BeneAccountNumber = getIntent().getStringExtra("BeneAccountNumber");
        BeneBankName = getIntent().getStringExtra("BeneBankName");
        IFSCcode = getIntent().getStringExtra("IFSCcode");
        RecipientId = getIntent().getStringExtra("RecipientId");
        IMPS = getIntent().getStringExtra("IMPS");
        NEFT = getIntent().getStringExtra("NEFT");
        Channel = getIntent().getStringExtra("Channel");
        RecipientIdType = getIntent().getStringExtra("RecipientIdType");
        BeneMobile = getIntent().getStringExtra("BeneMobile");

        tvBeneName = (TextView) findViewById(R.id.tv_payee_name);
        tvBeneAccountNumber = (TextView) findViewById(R.id.tv_payee_acc_no);
        tvIFSCcode = (TextView) findViewById(R.id.tv_payee_ifsc);
        editAmount =  findViewById(R.id.edit_amount);
        editRemark =  findViewById(R.id.edit_remark);
        btnProceed =  findViewById(R.id.txtsubmit);
        radioPayGroup = (RadioGroup) findViewById(R.id.radioGroup);
        editAmount.requestFocus();

        tvBeneName.setText(BeneName);
        tvBeneAccountNumber.setText(BeneAccountNumber);
        tvIFSCcode.setText(IFSCcode);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioPayGroup.getCheckedRadioButtonId();
                radioPayButton = (RadioButton) findViewById(selectedId);
                if (radioPayGroup.getCheckedRadioButtonId() != -1) {
                    radioButtonValue = radioPayButton.getText().toString();
                    if (radioButtonValue.equalsIgnoreCase("NEFT")) {
                        radioButtonValue = "1";
                    } else if (radioButtonValue.equalsIgnoreCase("IMPS")) {
                        radioButtonValue = "2";
                    }
                }

                if (TextUtils.isEmpty(editAmount.getText().toString().trim())) {
                    Toast.makeText(context, "Please enter correct amount!", Toast.LENGTH_SHORT).show();
                    editAmount.requestFocus();
                } else if(Integer.parseInt(editAmount.getText().toString()) < 100){
                    Toast.makeText(context, "Please enter amount equal to or more than \u20B9 100!", Toast.LENGTH_SHORT).show();
                    editAmount.requestFocus();
                }else if (radioPayGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(context, "Please Select Pay Option", Toast.LENGTH_SHORT).show();
                } else {
                    transactionAmount = editAmount.getText().toString().trim();
                    Remark = editRemark.getText().toString().trim();
                    PaymentConfirmDialog();
                }
            }
        });
    }

    //================For Delete Bene===============
    public void PaymentConfirmDialog() {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK =  d.findViewById(R.id.btn_resister_ok);
        final Button btnNO =  d.findViewById(R.id.btn_resister_no);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final TextView title = (TextView) d.findViewById(R.id.title);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);

        title.setText("Payment Confirmation");
        tvMessage.setText("Are you sure, you want to pay \u20B9 " + transactionAmount + " to " + BeneName);

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImpsNeftTransactionRequest();
                d.dismiss();
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

    //==========For Imps Neft Transaction=================
    private void ImpsNeftTransactionRequest() {
        pd = ProgressDialog.show(ImpsNeftActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", SenderId)
                    .put("SenderName", SenderName)
                    .put("BeneMobile", BeneMobile)
                    .put("Ifsc", IFSCcode)
                    .put("BeneAccount", BeneAccountNumber)
                    .put("BeneName", BeneName)
                    .put("RecipientId", RecipientId)
                    .put("TxnType", radioButtonValue)
                    .put("BankName", BeneBankName)
                    .put("TxnAmount", editAmount.getText().toString())
                    .put("REQUEST_ID", String.valueOf((long) Math.floor(Math.random() * 90000000000000L) + 10000000000000L))
                    .put("Remark", Remark == null ? "" : Remark);
            Log.e("Request--transaction", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_imps_neft_transaction, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject = object;
                            System.out.println("Object--transaction>" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called--transaction", url_imps_neft_transaction);
                                    L.m2("url data--transaction", object.toString());

                                    Intent intent = new Intent(ImpsNeftActivity.this, ImpsNeftTxnRecieptActivity.class);
                                    intent.putExtra("SenderId", object.getString("SenderId"));
                                    intent.putExtra("SenderName", object.getString("SenderName"));
                                    intent.putExtra("BeneMobile", object.getString("BeneMobile"));
                                    intent.putExtra("Ifsc", object.getString("ifsc"));
                                    intent.putExtra("accountNumber", object.getString("BeneAccount"));
                                    intent.putExtra("beneName", object.getString("BeneName"));
                                    intent.putExtra("bankName", object.getString("BankName"));
                                    intent.putExtra("amount", object.getString("TxnAmount"));
                                    intent.putExtra("BankRRN", object.getString("BankRRN"));
                                    intent.putExtra("TxnDate", object.getString("TxnDate"));
                                    intent.putExtra("TxnTime", object.getString("TxnTime"));
                                    startActivity(intent);
                                    ImpsNeftActivity.this.finish();
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(ImpsNeftActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ImpsNeftActivity.this, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
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
