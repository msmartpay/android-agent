package in.msmartpayagent.dmrPaySprint;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.msmartpayagent.R;
import in.msmartpayagent.dmrPaySprint.dashboard.PSMoneyTransferActivity;
import in.msmartpayagent.network.AppMethods;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainResponse2;
import in.msmartpayagent.network.model.dmr.RefundLiveStatusRequest;
import in.msmartpayagent.network.model.dmr.RefundLiveStatusResponse;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.network.model.dmr.SenderFindRequest;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import retrofit2.Call;
import retrofit2.Callback;

public class PSRefundLiveStatusActivity extends BaseActivity {

    private TextView refund_sender_id, refund_trans_number, refund_trans_status, refund_amount, refund_trans_desc, refund_tran_type, refund_bank_ref_id, refund_timestamp, refund_message;
    private Button btn_refund_done;
    private String agentID, txnKey, TranNo, mobileNumber, OtpString, fromIntent;

    private Context context;
    private ProgressDialogFragment pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_dmr_refund_live_status_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Live Status");

        context = PSRefundLiveStatusActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        mobileNumber = Util.LoadPrefData(getApplicationContext(), Keys.SENDER_MOBILE);

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
        btn_refund_done = (Button) findViewById(R.id.btn_refund_done);

        try {
            refundLiveStatusRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_refund_done.setOnClickListener(view -> refundBalanceDialog("", 1));
    }

    //==========ForLiveStatus==========================
    private void refundLiveStatusRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Refund Live Status...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        RefundLiveStatusRequest request = new RefundLiveStatusRequest();
        request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
        request.setSenderId(mobileNumber);
        request.setTransactionRefNo(TranNo);
        RetrofitClient.getClient(context).transStatus(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+ AppMethods.TransStatus,request).enqueue(new Callback<RefundLiveStatusResponse>() {
            @Override
            public void onResponse(Call<RefundLiveStatusResponse> call, retrofit2.Response<RefundLiveStatusResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    RefundLiveStatusResponse res = response.body();
                    if (res.getStatus().equals("0")) {
                        refund_sender_id.setText(res.getSenderId());
                        refund_trans_number.setText(res.getTransactionRefNo());
                        refund_trans_status.setText(res.getTran_Status());
                        refund_amount.setText(res.getAmount());
                        refund_trans_desc.setText(res.getTran_Desc());
                        refund_tran_type.setText(res.getTran_Type());
                        refund_bank_ref_id.setText(res.getBank_Ref_Id());
                        refund_timestamp.setText(res.getTimestamp());
                        refund_message.setText(res.getMessage());
                    }else {
                        L.toastS(getApplicationContext(), res.getMessage());
                    }
                } else {
                    L.toastS(getApplicationContext(), "No Response");
                }
            }

            @Override
            public void onFailure(Call<RefundLiveStatusResponse> call, Throwable t) {
                pd.dismiss();
                L.toastS(getApplicationContext(), "Error : " + t.getMessage());
            }
        });

    }

    //==============Dialog=============================
    public void refundBalanceDialog(final String message, final int i) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr_dialog_sender_mobile_dmt);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit = (Button) d.findViewById(R.id.btn_push_submit);
        final Button btnClosed = (Button) d.findViewById(R.id.close_push_button);
        final EditText editMobileNo = (EditText) d.findViewById(R.id.edit_push_balance);
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

        btnSubmit.setOnClickListener(v -> {
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
                    PSRefundLiveStatusActivity.this.finish();
                } else if (fromIntent.equalsIgnoreCase("fromSenderHistoryFragment")) {
                    d.dismiss();
                    findSenderRequest();
                }else if (fromIntent.equalsIgnoreCase("RefundFragment")) {
                    d.dismiss();
                   Intent intent = new Intent(getApplicationContext(), PSMoneyTransferActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
                }
            }
        });

        btnClosed.setOnClickListener(v -> {
            // TODO Auto-generated method stub

            d.cancel();
        });

        d.show();
    }

    //==========ForRefundTransaction===================
    private void refundTransactionRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Refund Transaction...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        RefundLiveStatusRequest request = new RefundLiveStatusRequest();
        request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
        request.setSenderId(mobileNumber);
        request.setTransactionRefNo(TranNo);
        RetrofitClient.getClient(context).refundTransaction(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+ AppMethods.RefundTransaction,request).enqueue(new Callback<MainResponse2>() {
            @Override
            public void onResponse(Call<MainResponse2> call, retrofit2.Response<MainResponse2> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    MainResponse2 res = response.body();
                    if (res.getStatus().equals("0")) {
                        refundBalanceDialog(res.getMessage(), 2);
                    }else {
                        L.toastS(getApplicationContext(), res.getMessage());
                    }
                } else {
                    L.toastS(getApplicationContext(), "No Response");
                }
            }

            @Override
            public void onFailure(Call<MainResponse2> call, Throwable t) {
                pd.dismiss();
                L.toastS(getApplicationContext(), "Error : " + t.getMessage());
            }
        });
    }

    //==========ForRefundTransaction===================
    private void confirmRefundRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Refund Transaction...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        RefundLiveStatusRequest request = new RefundLiveStatusRequest();
        request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
        request.setSenderId(mobileNumber);
        request.setTransactionRefNo(TranNo);
        request.setOTP(OtpString);
        RetrofitClient.getClient(context).refundTransactionConfirm(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+ AppMethods.RefundDMRConfirm,request).enqueue(new Callback<MainResponse2>() {
            @Override
            public void onResponse(Call<MainResponse2> call, retrofit2.Response<MainResponse2> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    MainResponse2 res = response.body();
                    if (res.getStatus().equals("0")) {
                        refundBalanceDialog(res.getMessage(), 3);
                    }else {
                        L.toastS(getApplicationContext(), res.getMessage());
                    }
                } else {
                    L.toastS(getApplicationContext(), "No Response");
                }
            }

            @Override
            public void onFailure(Call<MainResponse2> call, Throwable t) {
                pd.dismiss();
                L.toastS(getApplicationContext(), "Error : " + t.getMessage());
            }
        });
    }

    private void findSenderRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Latest Details of Sender...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        SenderFindRequest request = new SenderFindRequest();
        request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
        request.setSenderId(mobileNumber);
        RetrofitClient.getClient(context).findSenderDetails(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+ AppMethods.FindSender,request).enqueue(new Callback<SenderDetailsResponse>() {
            @Override
            public void onResponse(Call<SenderDetailsResponse> call, retrofit2.Response<SenderDetailsResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    SenderDetailsResponse res = response.body();
                    if (res.getStatus().equals("0")) {
                        Util.SavePrefData(context, Keys.SENDER_MOBILE, mobileNumber);
                        Util.SavePrefData(context, Keys.SENDER, Util.getGson().toJson(res));
                        Intent intent = new Intent(context, PSMoneyTransferActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity(intent);
                        finish();
                    }
                } else {
                    L.toastS(getApplicationContext(), "No Response");
                }
            }

            @Override
            public void onFailure(Call<SenderDetailsResponse> call, Throwable t) {
                pd.dismiss();
                L.toastS(getApplicationContext(), "Error : " + t.getMessage());
            }
        });
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
