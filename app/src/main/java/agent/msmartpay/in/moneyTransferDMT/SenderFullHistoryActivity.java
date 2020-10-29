package agent.msmartpay.in.moneyTransferDMT;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;

/**
 * Created by Smartkinda on 6/19/2017.
 */

public class SenderFullHistoryActivity extends BaseActivity {

    private TextView tviewDateTime,  tviewSerDeliveryID, tviewRequestedAmount, tviewBeneficiaryName, tviewBankName, tviewAccountNo, tviewIFSCCode, tviewUTR, tviewTransferedType, tviewStatus;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_sender_full_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Receipt");

        tviewDateTime = (TextView) findViewById(R.id.tv_date_time);
        tviewSerDeliveryID = (TextView) findViewById(R.id.tv_ser_deliv_tid);
        tviewBeneficiaryName = (TextView) findViewById(R.id.tv_bene_name);
        tviewBankName = (TextView) findViewById(R.id.tv_bank_name);
        tviewAccountNo = (TextView) findViewById(R.id.tv_acc_number);
        tviewIFSCCode = (TextView) findViewById(R.id.tv_ifsc_code);
        tviewRequestedAmount = (TextView) findViewById(R.id.tv_request_amount);
        tviewStatus = (TextView) findViewById(R.id.tv_status);
        tviewTransferedType = (TextView) findViewById(R.id.tv_txn_type);
        btnOK =  findViewById(R.id.btn_ok);

        tviewDateTime.setText(getIntent().getStringExtra("DateTime"));
        tviewSerDeliveryID.setText(getIntent().getStringExtra("TranNo"));
        tviewBeneficiaryName.setText(getIntent().getStringExtra("BeneName"));
        tviewBankName.setText(getIntent().getStringExtra("BeneBankName"));
        tviewAccountNo.setText(getIntent().getStringExtra("BeneAccount"));
        tviewIFSCCode.setText(getIntent().getStringExtra("BeneBankIfsc"));
        tviewRequestedAmount.setText(getIntent().getStringExtra("Amount"));
        tviewTransferedType.setText(getIntent().getStringExtra("TransactionType"));
        tviewStatus.setText(getIntent().getStringExtra("Status"));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
