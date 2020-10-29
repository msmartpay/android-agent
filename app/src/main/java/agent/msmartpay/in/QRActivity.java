package agent.msmartpay.in;


import android.os.Bundle;

import agent.msmartpay.in.utility.BaseActivity;


/**
 * Created by Smartkinda on 6/14/2017.
 */

public class QRActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pay with QR Code");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
