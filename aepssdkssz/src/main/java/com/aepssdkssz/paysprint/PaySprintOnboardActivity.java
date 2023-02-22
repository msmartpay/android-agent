package com.aepssdkssz.paysprint;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aepssdkssz.R;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;

import java.util.Objects;

public class PaySprintOnboardActivity extends AppCompatActivity {
    
    private Button btn_aeps;
    private String agentID = "", txnKey = "",transactionType="";
    private int PS_REQUEST_CODE=999;
    private String partnerKey = "UFMwMDQ4NWI0ODU5NDM1ZTgzZTU2NzU2ZTA1YjVlOTBlYWE2YjMz";
    private String partnerId = "PS00485";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_onboard_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agent Onboard");

        Intent intent = getIntent();
        if (intent != null) {
            transactionType=intent.getStringExtra(Constants.TRANSACTION_TYPE);
        }

        agentID = Utility.getData(getApplicationContext(),Constants.MERCHANT_ID);

        btn_aeps =  findViewById(R.id.btn_aeps);

        btn_aeps.setOnClickListener(v -> openPSOnboardSDK());

    }

    private void openPSOnboardSDK() {
        /*
        Intent intent = new Intent(getApplicationContext(), HostActivity.class);
        intent.putExtra("pId", partnerId);//partner Id provided in credential
        intent.putExtra("pApiKey", partnerKey);//JWT API Key provided in credential
        intent.putExtra("mCode", Utility.getData(getApplicationContext(),Constants.MERCHANT_ID));//Merchant Code
        intent.putExtra("mobile", Utility.getData(getApplicationContext(),Constants.MERCHANT_MOBILE));// merchant mobile number
        intent.putExtra("lat", Utility.getData(getApplicationContext(),Constants.LATITUDE));
        intent.putExtra("lng", Utility.getData(getApplicationContext(),Constants.LONGITUDE));
        intent.putExtra("firm", Utility.getData(getApplicationContext(),Constants.AGENCY_NAME));
        intent.putExtra("email", Utility.getData(getApplicationContext(),Constants.MERCHANT_EMAIL));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, PS_REQUEST_CODE);*/

        Utility.toast(getApplicationContext(),"Service is not active. Please activate service from web first.");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onSupportNavigateUp() {

        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PS_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                boolean status = data.getBooleanExtra("status", false);
                int response = data.getIntExtra("response", 0);
                String message = data.getStringExtra("message");
                Utility.toast(getApplicationContext(),message);
            } else {

                Utility.toast(getApplicationContext(),"Response not received with result code : "+resultCode);
            }
        }
    }
}
