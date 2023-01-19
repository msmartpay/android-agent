package in.msmartpay.agent.aeps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.Nullable;

import java.util.Objects;


import in.msmartpay.agent.R;

import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;

import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;


public class SSZPLAEPSActivity extends BaseActivity {
    
    private Button btn_aeps;
    private ProgressDialogFragment pd;
    String agentID = "", txnKey = "";

    int AEPS_REQUEST_CODE = 10923;
    String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AePS");


        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        btn_aeps =  findViewById(R.id.btn_aeps);
        btn_aeps.setOnClickListener(v ->getAccessKey());

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AEPS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) { //user taps CLOSE button after transaction -- case 1
                String response = data.getStringExtra("result");
                //--------- response is  transaction data
            } else if (resultCode == Activity.RESULT_CANCELED) { // user presses back button
                if (data == null) {
                    // ------ If user pressed back without transaction -- case 2
                } else {
                    String response = data.getStringExtra("result");
                    if (response != null && !response.equalsIgnoreCase("")) {
                        //------ If there is some error in partner parameters, response is that error
                        //------ when user performs the transaction, response is transaction data -- case 1
                    } else {

                    }
                }
            }
        }
    }

    private void getAccessKey() {


        /*if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Access Key...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            AepsAccessKeyRequest request = new AepsAccessKeyRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txnKey);

            RetrofitClient.getClient(getApplicationContext())
                    .generateSSPLAccessKey(request).enqueue(new Callback<AepsAccessKeyResponse>() {
                @Override
                public void onResponse(@NotNull Call<AepsAccessKeyResponse> call, @NotNull retrofit2.Response<AepsAccessKeyResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        AepsAccessKeyResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), AepsHomeActivity.class);
                            intent.putExtra("secondary_theme_code", "#fff");
                            intent.putExtra("primary_theme_code", "#000");
                            intent.putExtra("partner_code", res.getData().getPartner_code());
                            intent.putExtra("partner_name", res.getData().getPartner_name());
                            intent.putExtra("user_code", res.getData().getUser_code());
                            intent.putExtra("environment", "live");
                            intent.putExtra("initiator_id", res.getData().getInitiator_id());
                            intent.putExtra("secret_key_timestamp", res.getData().getSecret_key_timestamp());
                            intent.putExtra("secret_key", res.getData().getSecret_key());
                            intent.putExtra("developer_key", res.getData().getDeveloper_key());
                            intent.putExtra("receipt", true);
                            intent.putExtra("merchant_id", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_FULL));
                            intent.putExtra("merchant_name", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_NAME));
                            *//** Login Customer Details **//*
                            intent.putExtra("email", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_EMAIL));
                            intent.putExtra("mobile", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_MOB));
                            intent.putExtra("company", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_COMPANY));
                            intent.putExtra("name", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_NAME));
                            intent.putExtra("pan", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_PAN));
                            intent.putExtra("pincode", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_PIN));
                            intent.putExtra("address", Util.LoadPrefData(getApplicationContext(),Keys.AGENT_ADDRESS));

                            L.m2("SDK call ",Util.getJsonFromModel(intent));

                            startActivityForResult(intent, AEPS_REQUEST_CODE);

                        } else {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AepsAccessKeyResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }*/
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


    @Override
    public boolean onSupportNavigateUp() {

        return super.onSupportNavigateUp();
    }
}
