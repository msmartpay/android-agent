package in.msmartpayagent.dmrPaySprint;

import android.os.Bundle;
import android.widget.TextView;

import in.msmartpayagent.R;
import in.msmartpayagent.utility.BaseActivity;

import java.util.Objects;


public class PSTransactionReceiptActivity extends BaseActivity {
    private TextView tviewDeliveryTID, tviewTransferedType, tviewTransferedAmount, tviewBeneficiaryName, tviewBankName, tviewAccountNo, tviewIFSCCode, tviewTransactionStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_dmr_transaction_reciept);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Money Transfer2 Receipt");

        tviewDeliveryTID = (TextView) findViewById(R.id.tv_delivery_tid);
        tviewTransferedType = (TextView) findViewById(R.id.tv_transfer_type);
        tviewTransferedAmount = (TextView) findViewById(R.id.tv_transfer_amount);
        tviewBeneficiaryName = (TextView) findViewById(R.id.tv_bene_name);
        tviewBankName = (TextView) findViewById(R.id.tv_bank_name);
        tviewAccountNo = (TextView) findViewById(R.id.tv_account_no);
        tviewIFSCCode = (TextView) findViewById(R.id.tv_ifsc_code);
        tviewTransactionStatus = (TextView) findViewById(R.id.tv_transaction_status);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
