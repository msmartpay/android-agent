package in.msmartpay.agent.rechargeBillPay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import in.msmartpay.agent.R;
import in.msmartpay.agent.databinding.BillElectricityActivityBinding;
import in.msmartpay.agent.databinding.BillLandlineActivityBinding;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.OperatorsRequest;
import in.msmartpay.agent.network.model.OperatorsResponse;
import in.msmartpay.agent.network.model.post.OperatorData;
import in.msmartpay.agent.network.model.post.OperatorResponse;
import in.msmartpay.agent.network.model.wallet.OperatorModel;
import in.msmartpay.agent.rechargeBillPay.operator.OperatorSearchActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class LandlinePayActivity extends BaseActivity {
    private BillLandlineActivityBinding binding;
    private ProgressDialogFragment pd;
    private Context context;
    private String agentID, txn_key = "";
    private ArrayList<OperatorModel> operatorList = new ArrayList<>();
    private OperatorModel opreatorModel = null;
    private String connectionNo, amt;
    private OperatorData operatorData = null;
    private boolean isAd1= false, isAd2= false, isAd3= false;
    private String  ad1= "", ad2= "", ad3= "";

    private final ActivityResultLauncher<Intent> activityOperatorLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.hasExtra(Keys.ARRAY_LIST))
                            operatorList = (ArrayList<OperatorModel>) Util.getListFromJson(data.getStringExtra(Keys.ARRAY_LIST), OperatorModel.class);
                        opreatorModel = Util.getGson().fromJson(data.getStringExtra(Keys.OBJECT), OperatorModel.class);
                        Objects.requireNonNull(binding.tilOperator.getEditText()).setText(opreatorModel.getDisplayName());
                        operatorDetailsRequest();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BillLandlineActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Landline");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = LandlinePayActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        Util.hideView(binding.tidAd1);
        Util.hideView(binding.tidAd2);
        Util.hideView(binding.tidAd3);
        Util.showView(binding.btnProceed);

        //For operatorList
        operatorsCodeRequest();

        binding.etOperator.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OperatorSearchActivity.class);
            intent.putExtra(Keys.ARRAY_LIST, Util.getJsonFromModel(operatorList));
           activityOperatorLauncher.launch(intent);
        });


        binding.btnProceed.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                connectionNo = Objects.requireNonNull(binding.tidConsumerNo.getEditText()).getText().toString();
                amt = Objects.requireNonNull(binding.tidAmount.getEditText()).getText().toString();
                if (isAd1)
                    ad1 = Objects.requireNonNull(binding.tidAd1.getEditText()).getText().toString();
                if (isAd2)
                    ad1 = Objects.requireNonNull(binding.tidAd1.getEditText()).getText().toString();
                if (isAd3)
                    ad1 = Objects.requireNonNull(binding.tidAd1.getEditText()).getText().toString();
                if (opreatorModel == null) {
                    L.toastS(context, "Select Operator");
                } else if ("".equals(connectionNo)) {
                    L.toastS(context, "Enter Valid " + Objects.requireNonNull(binding.tidConsumerNo.getHint()));
                } else if (isAd1 && ad1.isEmpty()) {
                    if (!"0".equals(operatorData.getAd1Regex()) && Util.checkRegexValidation(operatorData.getAd1Regex(),ad1)){
                        L.toastS(context, "Enter Valid " + Objects.requireNonNull(binding.tidAd1.getHint()));
                    }else {
                        L.toastS(context, "Enter " + Objects.requireNonNull(binding.tidAd1.getHint()));
                    }
                } else if (isAd2 && ad2.isEmpty()) {
                    if (!"0".equals(operatorData.getAd2Regex()) && Util.checkRegexValidation(operatorData.getAd2Regex(),ad2)){
                        L.toastS(context, "Enter Valid " + Objects.requireNonNull(binding.tidAd2.getHint()));
                    }else {
                        L.toastS(context, "Enter " + Objects.requireNonNull(binding.tidAd2.getHint()));
                    }
                } else if (isAd3 && ad3.isEmpty()) {
                    if (!"0".equals(operatorData.getAd3Regex()) && Util.checkRegexValidation(operatorData.getAd3Regex(),ad3)){
                        L.toastS(context, "Enter Valid " + Objects.requireNonNull(binding.tidAd3.getHint()));
                    }else {
                        L.toastS(context, "Enter " + Objects.requireNonNull(binding.tidAd3.getHint()));
                    }
                }else if("0".equalsIgnoreCase(operatorData.getViewbill()) && amt.isEmpty()){
                    L.toastS(context,  "Enter Valid Amount");
                } else {
                    //proceedConfirmationDialog();

                    Intent intent = new Intent(context, BillPayActivity.class);
                    intent.putExtra("CN", connectionNo);
                    intent.putExtra("CN_hint", "Landline Number");
                    intent.putExtra("Amt", amt);
                    intent.putExtra("AD1", ad1);
                    intent.putExtra("AD2", ad2);
                    intent.putExtra("AD3", ad3);
                    intent.putExtra(getString(R.string.pay_operator_data),Util.getJsonFromModel(operatorData));
                    intent.putExtra(getString(R.string.pay_operator_model), getGson().toJson(opreatorModel));
                    startActivity(intent);
                }
            } else {
                Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
            }
        });
        //add1.setVisibility(View.GONE);
    }

    //===========operatorCodeRequest==============
    private void operatorsCodeRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {

            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Operators...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            OperatorsRequest request = new OperatorsRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService("LANDLINE");

            RetrofitClient.getClient(getApplicationContext())
                    .operators(request).enqueue(new Callback<OperatorsResponse>() {
                @Override
                public void onResponse(@NotNull Call<OperatorsResponse> call, @NotNull retrofit2.Response<OperatorsResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        OperatorsResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            operatorList = (ArrayList<OperatorModel>) res.getData();
                        } else {
                            L.toastS(context, res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OperatorsResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }
    //===========operatorDetailsRequest==============
    private void operatorDetailsRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {

            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Operator Details...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            OperatorsRequest request = new OperatorsRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setOperatorId(opreatorModel.getOpCode());

            RetrofitClient.getClient(getApplicationContext())
                    .getOperatorDetails(request).enqueue(new Callback<OperatorResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<OperatorResponse> call, @NotNull retrofit2.Response<OperatorResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                OperatorResponse res = response.body();
                                if ("0".equals(res.getStatus()) && res.getData()!=null) {
                                        operatorData = res.getData();

                                    if("1".equalsIgnoreCase(operatorData.getViewbill())){

                                        binding.tidAmount.setVisibility(View.GONE);
                                    }
                                    isAd1  = false;
                                    isAd2  = false;
                                    isAd3  = false;
                                    Util.hideView(binding.tidAd1);
                                    Util.hideView(binding.tidAd2);
                                    Util.hideView(binding.tidAd3);
                                    if (!"0".equals(operatorData.getAd1DName())) {
                                            isAd1 = true;
                                            Util.showView(binding.tidAd1);
                                            Objects.requireNonNull(binding.tidAd1.getEditText()).setText("");
                                            binding.tidAd1.setHint(operatorData.getAd1DName());
                                        }
                                        if (!"0".equals(operatorData.getAd2DName())) {
                                            isAd2 = true;
                                            Util.showView(binding.tidAd2);
                                            Objects.requireNonNull(binding.tidAd2.getEditText()).setText("");
                                            binding.tidAd2.setHint(operatorData.getAd2DName());

                                        }
                                        if (!"0".equals(operatorData.getAd3DName())) {
                                            isAd3 = true;
                                            Util.showView(binding.tidAd3);
                                            Objects.requireNonNull(binding.tidAd3.getEditText()).setText("");
                                            binding.tidAd3.setHint(operatorData.getAd3DName());
                                        }
                                } else {
                                    L.toastS(context, res.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OperatorResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                            pd.dismiss();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
