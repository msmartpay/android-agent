package in.msmartpayagent.dmrPaySprint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import in.msmartpayagent.R;
import in.msmartpayagent.dmrPaySprint.dashboard.MoneyTransferActivity;
import in.msmartpayagent.network.AppMethods;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.network.model.dmr.SenderFindRequest;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Smartkinda on 7/5/2017.
 */

public class ImpsNeftTxnRecieptActivity extends BaseActivity {

    private TextView tv_bank_rrn, tv_sender_id, tv_sender_name,  tv_bene_mobile, tviewTransferedAmount, tviewBeneficiaryName
            , tviewBankName, tviewAccountNo, tviewIFSCCode, tv_date_time,tv_message;

    private String SenderId, SenderName, BeneMobile, accountNumber, bankName, beneName, Ifsc, BankRRN, amount, TxnDate, TxnTime,message;
    private Button btnDone;
    private String  mobileNumber;
    private ProgressDialogFragment pd;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_imps_neft_txn_reciept);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction Receipt");

        context = ImpsNeftTxnRecieptActivity.this;
        mobileNumber = Util.LoadPrefData(getApplicationContext(), Keys.SENDER_MOBILE);

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
        message=getIntent().getStringExtra("message");

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
        btnDone = (Button) findViewById(R.id.btn_done);
        tv_message=(TextView) findViewById(R.id.tv_message);

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
        tv_message.setText(message);

        btnDone.setOnClickListener(v -> findSenderRequest());
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
                        Intent intent = new Intent(context, MoneyTransferActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                       finish();

                    }
                } else {
                    L.toastS(context, "No Response");
                }
            }

            @Override
            public void onFailure(Call<SenderDetailsResponse> call, Throwable t) {
                pd.dismiss();
                L.toastS(context, "Error : " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}