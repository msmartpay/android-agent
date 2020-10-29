package in.msmartpay.agent.moneyTransferDMT;

import android.os.Bundle;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;

/**
 * Created by Smartkinda on 7/18/2017.
 */

public class UpdateBeneAccountDetailView extends BaseActivity {

    private String BeneName, BeneAccountNumber, BeneBankName, BeneStatus, BeneVarify, IFSCcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_update_bene_account_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Update Status");


        BeneName = getIntent().getStringExtra("BeneName");
        BeneAccountNumber = getIntent().getStringExtra("BeneAccountNumber");
        BeneBankName = getIntent().getStringExtra("BeneBankName");
        BeneStatus = getIntent().getStringExtra("BeneStatus");
        BeneVarify = getIntent().getStringExtra("BeneVarify");
        IFSCcode = getIntent().getStringExtra("IFSCcode");
    }
    @Override
    public boolean onSupportNavigateUp() {
       finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
