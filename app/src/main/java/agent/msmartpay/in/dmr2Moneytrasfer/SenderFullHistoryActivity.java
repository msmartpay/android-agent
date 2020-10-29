package agent.msmartpay.in.dmr2Moneytrasfer;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;

/**
 * Created by Smartkinda on 6/19/2017.
 */

public class SenderFullHistoryActivity extends BaseActivity {

    private TextView tviewDateTime,  tviewSerDeliveryID, tviewRequestedAmount, tviewBeneficiaryName, tviewBankName, tviewAccountNo, tviewIFSCCode, tviewUTR, tviewTransferedType, tviewStatus;
    private Button btnOK;
    private String intentDateTime, intentSerDeliveryID, listDataHistory;
    private ArrayList<SenderHistoryModel> arrayList = new ArrayList<SenderHistoryModel>();
    private String TranNo, Status, DateTime, BeneAccount, Amount, BeneName, BeneBankName, BeneBankIfsc, TransactionType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr2_sender_full_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Full Details");

        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setSmoothScrollingEnabled(true);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.requestFocusFromTouch();
                return false;
            }
        });

        TranNo = getIntent().getStringExtra("TranNo");
        Status = getIntent().getStringExtra("Status");
        DateTime = getIntent().getStringExtra("DateTime");
        BeneAccount = getIntent().getStringExtra("BeneAccount");
        Amount = getIntent().getStringExtra("Amount");
        BeneName = getIntent().getStringExtra("BeneName");
        BeneBankName = getIntent().getStringExtra("BeneBankName");
        BeneBankIfsc = getIntent().getStringExtra("BeneBankIfsc");
        TransactionType = getIntent().getStringExtra("TransactionType");

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

        tviewDateTime.setText(DateTime);
        tviewSerDeliveryID.setText(TranNo);
        tviewBeneficiaryName.setText(BeneName);
        tviewBankName.setText(BeneBankName);
        tviewAccountNo.setText(BeneAccount);
        tviewIFSCCode.setText(BeneBankIfsc);
        tviewRequestedAmount.setText(Amount);
        tviewTransferedType.setText(TransactionType);
        tviewStatus.setText(Status);

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
