package in.msmartpay.agent.aeps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aepssdkssz.FingpayAePSHomeActivity;
import com.aepssdkssz.SSZAePSHomeActivity;
import com.aepssdkssz.util.Constants;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.aeps.onboard.UserNumberDialog;
import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.aeps.AepsAccessKeyRequest;
import in.msmartpay.agent.network.model.aeps.AepsAccessKeyResponse;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import in.co.eko.ekopay.EkoPayActivity;
import retrofit2.Call;
import retrofit2.Callback;

public class AEPSSDKActivity extends BaseActivity {
    
    private Button btn_aeps;
    private ProgressDialogFragment pd;
    String agentID = "", txnKey = "",transactionType="";

    int AEPS_REQUEST_CODE = 10923;
    String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadharpay);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AePS & AadharPay");

        Intent intent = getIntent();
        if (intent != null) {
            transactionType=intent.getStringExtra(Constants.TRANSACTION_TYPE);
        }

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        btn_aeps =  findViewById(R.id.btn_aeps);

        if(Keys.FINGPAY.equalsIgnoreCase(transactionType) || Keys.FINGPAY_MS.equalsIgnoreCase(transactionType)
        || Keys.AADHAAR_PAY.equalsIgnoreCase(transactionType)){
            getFingpayAccessKey();
        }else {
            getAccessKey();
        }
        btn_aeps.setOnClickListener(v -> getAccessKey());

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

                            String aepsStatus=res.getData().getAeps_status();
                            String aadhaarPayStatus=res.getData().getAadharpay_status();

                            if(("1".equalsIgnoreCase(aepsStatus) && (Constants.CASH_WITHDRAWAL.equalsIgnoreCase(transactionType) || Constants.MINI_STATEMENT.equalsIgnoreCase(transactionType) || Constants.BALANCE_ENQUIRY.equalsIgnoreCase(transactionType)) )
                                    || ("1".equalsIgnoreCase(aadhaarPayStatus) && Constants.AADHAAR_PAY.equalsIgnoreCase(transactionType))) {

                                Intent intent = new Intent(getApplicationContext(), SSZAePSHomeActivity.class);
                                intent.putExtra("merchant_id", agentID);
                                intent.putExtra("token", txnKey);//Authorized key
                                intent.putExtra("environment", AppMethods.environment);//
                                intent.putExtra("partner_logo_url", res.getData().getLogo());
                                intent.putExtra("partner_name", res.getData().getPartner_name());
                                intent.putExtra("partner_powered_by", res.getData().getPartner_name());
                                intent.putExtra("partner_contact_us", res.getData().getInitiator_id());
                                intent.putExtra("source_ip", Util.getIpAddress(getApplicationContext()));
                                intent.putExtra("transaction_type", transactionType);
                                intent.putExtra("aadharpay_status", aadhaarPayStatus);
                                intent.putExtra("aeps_status", aepsStatus);

                                String latLong = Util.LoadPrefData(getApplicationContext(), Keys.LATITUDE) + "," + Util.LoadPrefData(getApplicationContext(), Keys.LATITUDE);

                                intent.putExtra("latlong", "28.6871302,77.4812148");
                                startActivityForResult(intent, AEPS_REQUEST_CODE);

                            }else{
                                Toast.makeText(getApplicationContext(), "Activate AePS service", Toast.LENGTH_SHORT).show();
                                UserNumberDialog.showDialog(getSupportFragmentManager(), Util.LoadPrefData(getApplicationContext(), Keys.AGENT_MOB));
                            }

                        }else {
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

    private void getFingpayAccessKey() {

        Intent intent = new Intent(getApplicationContext(), FingpayAePSHomeActivity.class);
        intent.putExtra("merchant_id", agentID);
        intent.putExtra("token", txnKey);//Authorized key
        intent.putExtra("environment", AppMethods.environment);//
        intent.putExtra("source_ip", Util.getIpAddress(getApplicationContext()));
        intent.putExtra("transaction_type", transactionType);
        intent.putExtra("deviceId", Util.getDeviceId(getApplicationContext()));

        intent.putExtra(Keys.LATITUDE, Util.LoadPrefData(getApplicationContext(), Keys.LATITUDE));
        intent.putExtra(Keys.LONGITUDE, Util.LoadPrefData(getApplicationContext(), Keys.LONGITUDE));
        startActivityForResult(intent, AEPS_REQUEST_CODE);

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
        if (requestCode == AEPS_REQUEST_CODE) {
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    L.toastS(this, data.getStringExtra("Message"));
                    data.getStringExtra("StatusCode");    //to get status code
                    data.getStringExtra("Message");     //to get response message
                    //L.m2("check--1", data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message"));
                    Intent intent=new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    L.toastS(this, data.getStringExtra("Message"));
                    data.getStringExtra("StatusCode");    //to get status code
                    data.getStringExtra("Message");     //to get response message
                    //L.m2("check--2", data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message"));
                    Intent intent=new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
