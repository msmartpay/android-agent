package in.msmartpayagent.aeps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import in.co.eko.ekopay.EkoPayActivity;
import in.msmartpayagent.R;
import in.msmartpayagent.aeps.onboard.UserNumberDialog;
import in.msmartpayagent.network.AppMethods;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.aeps.AepsAccessKeyRequest;
import in.msmartpayagent.network.model.aeps.AepsAccessKeyResponse;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

public class EkoAEPSGatewayActivity extends BaseActivity {
    
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
        btn_aeps.setOnClickListener(v -> getAccessKey());

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AEPS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) { //user taps CLOSE button after transaction -- case 1
                String response = data.getStringExtra("result");
                L.m2("aeps",response);
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
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Access Key...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            AepsAccessKeyRequest request = new AepsAccessKeyRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txnKey);

            RetrofitClient.getClient(getApplicationContext())
                    .getAepsAccessKey(request).enqueue(new Callback<AepsAccessKeyResponse>() {
                @Override
                public void onResponse(@NotNull Call<AepsAccessKeyResponse> call, @NotNull retrofit2.Response<AepsAccessKeyResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        AepsAccessKeyResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("environment", AppMethods.environment);
                            // Optional
                            // Default value "production"  If environment not passed then, default environment will be "production"
                            // Expected values "uat" for beta testing or"production" for production environment
                            bundle.putString("product", "aeps");
                            // mandatory
                            bundle.putString("secret_key_timestamp", res.getData().getSecret_key_timestamp());
                            // mandatory
                            bundle.putString("secret_key", res.getData().getSecret_key());
                            // mandatory
                            bundle.putString("developer_key", res.getData().getDeveloper_key());
                            // mandatory
                            bundle.putString("initiator_id", res.getData().getInitiator_id());
                            // mandatory
                            bundle.putString("callback_url", res.getData().getCallback_url());
                            // mandatory
                            bundle.putString("user_code", res.getData().getUser_code());
                            // mandatory
                            bundle.putString("initiator_logo_url", res.getData().getLogo());
                            // mandatory
                            bundle.putString("partner_name", res.getData().getPartner_name());
                            // mandatory
                            bundle.putString("language", language);
                            // Optional

                            // bundle.putString("callback_url_custom_params", callback_url_custom_params);
                            // Optional

                            // bundle.putString("callback_url_custom_headers",  callback_url_custom_headers);
                            // Optional

                            Intent intent = new Intent(EkoAEPSGatewayActivity.this, EkoPayActivity.class);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, AEPS_REQUEST_CODE);

                        }else if(res.getResponseCode() != null && res.getResponseCode().equals("4")) {
                            Toast.makeText(getApplicationContext(), "Activate AePS service", Toast.LENGTH_SHORT).show();
                            UserNumberDialog.showDialog(getSupportFragmentManager(), Util.LoadPrefData(getApplicationContext(), Keys.AGENT_MOB));
                        }else if(res.getResponseCode() != null && res.getResponseCode().equals("2")) {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }else if(res.getResponseCode() != null && res.getResponseCode().equals("3")) {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }else{
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
        }
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
