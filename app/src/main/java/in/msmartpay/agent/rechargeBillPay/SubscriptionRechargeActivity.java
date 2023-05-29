package in.msmartpay.agent.rechargeBillPay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.databinding.BillDataCardActivityBinding;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse;
import in.msmartpay.agent.network.model.OperatorsRequest;
import in.msmartpay.agent.network.model.OperatorsResponse;
import in.msmartpay.agent.network.model.RechargeRequest;
import in.msmartpay.agent.network.model.post.OperatorData;
import in.msmartpay.agent.network.model.post.OperatorResponse;
import in.msmartpay.agent.network.model.wallet.OperatorModel;
import in.msmartpay.agent.rechargeBillPay.operator.OperatorSearchActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.RandomNumber;
import in.msmartpay.agent.utility.TextDrawable;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

public class SubscriptionRechargeActivity extends BaseActivity {
    private BillDataCardActivityBinding binding;
    private ProgressDialogFragment pd;
    private Context context;
    private String agentID, txn_key = "", amt = "", connectionNo = "";
    private ArrayList<OperatorModel> operatorList = new ArrayList<>();
    private OperatorModel operatorModel = null;
    private OperatorData operatorData = null;
    private boolean isAd1 = false, isAd2 = false, isAd3 = false;
    private String ad1 = "", ad2 = "", ad3 = "";

    private final ActivityResultLauncher<Intent> activityOperatorLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.hasExtra(Keys.ARRAY_LIST))
                            operatorList = (ArrayList<OperatorModel>) Util.getListFromJson(data.getStringExtra(Keys.ARRAY_LIST), OperatorModel.class);
                        operatorModel = Util.getGson().fromJson(data.getStringExtra(Keys.OBJECT), OperatorModel.class);
                        Objects.requireNonNull(binding.tilOperator.getEditText()).setText(operatorModel.getDisplayName());
                        operatorDetailsRequest();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BillDataCardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Data Card");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = SubscriptionRechargeActivity.this;

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);


        String code1 = getString(R.string.rupee_symbol);
        Objects.requireNonNull(binding.tidAmount.getEditText()).setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
        Objects.requireNonNull(binding.tidAmount.getEditText()).setCompoundDrawablePadding(code1.length() * 10);

        //For operatorList
        operatorsCodeRequest();

        binding.etOperator.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OperatorSearchActivity.class);
            intent.putExtra(Keys.ARRAY_LIST, Util.getJsonFromModel(operatorList));
            activityOperatorLauncher.launch(intent);
        });

        binding.btnProceed.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                connectionNo = Objects.requireNonNull(binding.tidConsumerNo.getEditText()).getText().toString().trim();
                amt = Objects.requireNonNull(binding.tidAmount.getEditText()).getText().toString().trim();
                if (isAd1)
                    ad1 = Objects.requireNonNull(binding.tidAd1.getEditText()).getText().toString().trim();
                if (isAd2)
                    ad1 = Objects.requireNonNull(binding.tidAd1.getEditText()).getText().toString().trim();
                if (isAd3)
                    ad1 = Objects.requireNonNull(binding.tidAd1.getEditText()).getText().toString().trim();

                if (operatorModel == null) {
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
                }else if ("".equals(amt) || Double.parseDouble(amt) < 10) {
                    L.toastS(context,  "Enter Valid Amount");
                } else {
                    proceedConfirmationDialog();
                }
            } else {
                L.toastS(context,"No Internet Connection !!!");
            }
        });
    }

    //===========operatorCodeRequest==============
    private void operatorsCodeRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Operators...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            OperatorsRequest request = new OperatorsRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService("datacard");

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
            request.setOperatorId(operatorModel.getOpCode());

            RetrofitClient.getClient(getApplicationContext())
                    .getOperatorDetails(request).enqueue(new Callback<OperatorResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<OperatorResponse> call, @NotNull retrofit2.Response<OperatorResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                OperatorResponse res = response.body();
                                if ("0".equals(res.getStatus()) && res.getData()!=null) {
                                        operatorData = res.getData();
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


    private void proceedConfirmationDialog() {
        final Dialog d = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView confirm_mobile, confirm_operator, confirm_amount, tv_cancel, tv_recharge, tv_mobile_dth;
        confirm_mobile = (TextView) d.findViewById(R.id.confirm_mobile);
        confirm_operator = (TextView) d.findViewById(R.id.confirm_operator);
        confirm_amount = (TextView) d.findViewById(R.id.confirm_amount);
        tv_cancel = (TextView) d.findViewById(R.id.tv_cancel);
        tv_recharge = (TextView) d.findViewById(R.id.tv_recharge);
        tv_mobile_dth = (TextView) d.findViewById(R.id.tv_rename);

        tv_mobile_dth.setText("Customer ID");
        confirm_mobile.setText(connectionNo);
        confirm_operator.setText(operatorModel.getOperatorName());
        confirm_amount.setText(amt);

        tv_recharge.setOnClickListener(view -> {
            try {
                DataCardRequest();
                d.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tv_cancel.setOnClickListener(v -> d.cancel());

        d.show();
    }

    //===========DataCardRequest==============

    private void DataCardRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Recharging...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            RechargeRequest request = new RechargeRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService("datacard");
            request.setOperator(operatorModel.getCode());
            request.setMobile_no(connectionNo);
            request.setOpCode(operatorModel.getOpCode());
            request.setRequest_id(RandomNumber.getTranId_14());
            request.setLatitude(Util.LoadPrefData(context, getString(R.string.latitude)));
            request.setLongitude(Util.LoadPrefData(context, getString(R.string.longitude)));
            request.setIp(Util.getIpAddress(context));
            request.setAmount(amt);

            RetrofitClient.getClient(getApplicationContext())
                    .recharge(request).enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse> call, @NotNull retrofit2.Response<MainResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0") || res.getResponseCode().equals("1") || res.getResponseCode().equals("2")) {
                            Intent in = new Intent(context, SuccessDetailActivity.class);
                            in.putExtra("responce", res.getResponseMessage());
                            in.putExtra("mobileno", connectionNo);
                            in.putExtra("requesttype", "datacard");
                            in.putExtra("operator", operatorModel.getDisplayName());
                            in.putExtra("amount", amt);
                            startActivity(in);
                            finish();
                        } else {
                            L.toastS(context, "Unable To Process Your Request. Please try later");
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    //====================================

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
