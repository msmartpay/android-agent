package in.msmartpay.agent.moneyTransferDMT;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;


/**
 * Created by Smartkinda on 7/5/2017.
 */

public class ImpsNeftTxnRecieptActivity extends BaseActivity {

    private TextView tviewSenderMobileNo, tviewDeliveryTID,  tviewTransferedType, tviewTransferedAmount, tviewBeneficiaryName, tviewBankName, tviewAccountNo, tviewIFSCCode, tviewTransactionStatus;
    private SharedPreferences sharedPreferences;
    private String mobileNumber, accountNumber, bankName, beneName, Ifsc, transactionRefNo, amount, remittanceType, transactionStatus;
    private Button btnDone;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_imps_neft_txn_reciept);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction Reciept");

        context = ImpsNeftTxnRecieptActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);
        accountNumber = getIntent().getStringExtra("accountNumber");
        bankName = getIntent().getStringExtra("bankName");
        beneName = getIntent().getStringExtra("beneName");
        Ifsc = getIntent().getStringExtra("Ifsc");
        transactionRefNo = getIntent().getStringExtra("transactionRefNo");
        amount = getIntent().getStringExtra("amount");
        remittanceType = getIntent().getStringExtra("remittanceType");
        transactionStatus = getIntent().getStringExtra("transactionStatus");

        tviewSenderMobileNo = (TextView) findViewById(R.id.tv_sender_mobile_no);
        tviewDeliveryTID = (TextView) findViewById(R.id.tv_delivery_tid);
        tviewTransferedType = (TextView) findViewById(R.id.tv_transfer_type);
        tviewTransferedAmount = (TextView) findViewById(R.id.tv_transfer_amount);
        tviewBeneficiaryName = (TextView) findViewById(R.id.tv_bene_name);
        tviewBankName = (TextView) findViewById(R.id.tv_bank_name);
        tviewAccountNo = (TextView) findViewById(R.id.tv_acc_number);
        tviewIFSCCode = (TextView) findViewById(R.id.tv_ifsc_code);
        tviewTransactionStatus = (TextView) findViewById(R.id.tv_transaction_status);
        btnDone =  findViewById(R.id.btn_done);

        tviewSenderMobileNo.setText(mobileNumber);
        tviewDeliveryTID.setText(transactionRefNo);
        tviewTransferedType.setText(remittanceType);
        tviewTransferedAmount.setText(amount);
        tviewBeneficiaryName.setText(beneName);
        tviewBankName.setText(bankName);
        tviewAccountNo.setText(accountNumber);
        tviewIFSCCode.setText(Ifsc);
        tviewTransactionStatus.setText(transactionStatus);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, MoneyTransferActivity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                finish();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}