package in.msmartpay.agent.ekobbps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import in.msmartpay.agent.R;
import in.msmartpay.agent.databinding.EkoBbpsPaymentActivityBinding;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.ekobbps.BillPayMainRequest;
import in.msmartpay.agent.network.model.ekobbps.FetchBillRequest;
import in.msmartpay.agent.network.model.ekobbps.FetchBillResponse;
import in.msmartpay.agent.network.model.ekobbps.FetchDetails;
import in.msmartpay.agent.network.model.ekobbps.Operator;
import in.msmartpay.agent.network.model.ekobbps.OperatorCategory;
import in.msmartpay.agent.network.model.ekobbps.OperatorCategoryResponse;
import in.msmartpay.agent.network.model.ekobbps.OperatorLocation;
import in.msmartpay.agent.network.model.ekobbps.OperatorLocationResponse;
import in.msmartpay.agent.network.model.ekobbps.OperatorParameter;
import in.msmartpay.agent.network.model.ekobbps.OperatorParametersResponse;
import in.msmartpay.agent.network.model.ekobbps.OperatorResponse;
import in.msmartpay.agent.network.model.ekobbps.PayBillRequest;
import in.msmartpay.agent.network.model.ekobbps.PayBillResponse;
import in.msmartpay.agent.rechargeBillPay.BillPayActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class BillPaymentActivity extends BaseActivity {

    private Context context;
    private String agentID, txn_key,tpinStatus="",tpin="";
    private ProgressDialogFragment pd;
    private EkoBbpsPaymentActivityBinding binding;
    private List<OperatorCategory> operatorCategorylist = new ArrayList<>();
    private List<OperatorLocation> operatorLocationlist = new ArrayList<>();
    private List<Operator> operatorList = new ArrayList<>();
    private List<OperatorParameter> operatorParametersList = new ArrayList<>();
    private OperatorCategory operatorCategory = null;
    private OperatorLocation operatorLocation = null;
    private Operator operator = null;
    private EkoEditBoxAdapter ekoEditBoxAdapter;
    private String senderName, senderMobile, channel = "0";
    private boolean isOnline = true;
    private HashMap<String, Object> data;
    private FetchDetails fetchDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EkoBbpsPaymentActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("BBPS BillPayment");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        context = BillPaymentActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        tpinStatus = Util.LoadPrefData(getApplicationContext(), Keys.TPIN_STATUS);

        setUpDynamicBoxList();
        categoryRequest();
        locationRequest();

        binding.mactSelectCategory.setOnClickListener(v -> {
            if (!operatorCategorylist.isEmpty()) {
                showOperatorCategoryDialog();
            } else {
                categoryRequest();
            }
        });

        binding.mactSelectLocation.setOnClickListener(v -> {
            if (operatorCategory == null) {
                L.toastS(context, "Please select first category");
            } else {
                if (!operatorLocationlist.isEmpty()) {
                    showOperatorLocationDialog();
                } else {
                    locationRequest();
                }
            }
        });

        binding.mactvSelectOperator.setOnClickListener(v -> {
            if (!operatorList.isEmpty()) {
                showOperatorDialog();
            } else {
                L.toastS(getApplicationContext(), "No operator available");
            }

        });

        binding.llViewBill.setOnClickListener(v -> {

            int selectedId = binding.rgOfflineOnline.getCheckedRadioButtonId();
            RadioButton radioChannelButton = findViewById(selectedId);
            if (selectedId != -1) {
                String radioButtonValue = radioChannelButton.getText().toString();
                if (radioButtonValue.equalsIgnoreCase("Offline")) {
                    channel = "1";
                } else if (radioButtonValue.equalsIgnoreCase("Online")) {
                    channel = "0";
                }
                if (validateFetchBill())
                    fetchBillRequest();
            }else{
                L.toastS(getApplicationContext(), "Please select mode of payment");
            }


        });

        binding.llProceed.setOnClickListener(v -> {
            if (StringUtils.isBlank(binding.tilBillAmount.getEditText().getText().toString()))
                L.toastS(getApplicationContext(), "Invalid amount");
            else if(validateFetchBill()) {
                data.put("amount",binding.tilBillAmount.getEditText().getText().toString());
                data.put("postalcode",fetchDetails.getPostalcode());
                data.put("geocode",fetchDetails.getGeocode());
                data.put("billfetchresponse",fetchDetails.getBillfetchresponse());
                data.put("bbpstrxnrefid",fetchDetails.getBbpstrxnrefid());
                data.put("bbpstrxnrefid",fetchDetails.getBbpstrxnrefid());
                if("Y".equalsIgnoreCase(tpinStatus)){
                    transactionPinDialog();
                }else{
                    payBillRequest();
                }

            }else{
                L.toastS(getApplicationContext(), "Invalid required param");
            }

        });
    }

    private void setUpDynamicBoxList() {
        ekoEditBoxAdapter = new EkoEditBoxAdapter(operatorParametersList,
                (EkoEditBoxAdapter.MyListener) (poss, text) -> {
                    if (!operatorParametersList.isEmpty()) {
                        operatorParametersList.get(poss).setInputValue(text);
                    }
                });
        binding.rvDynamic.setAdapter(ekoEditBoxAdapter);
    }

    private boolean validateFetchBill() {
        boolean isCallFetch = true;
        senderName = Objects.requireNonNull(binding.tilSenderName.getEditText()).getText().toString().trim();
        senderMobile = Objects.requireNonNull(binding.tilSenderMob.getEditText()).getText().toString().trim();
        ArrayList<HashMap<String,String>> fieldsDataArr = new ArrayList<HashMap<String,String>>();
        data = new HashMap<>();
        if (!operatorParametersList.isEmpty()) {
            for (int i = 0; i < operatorParametersList.size(); i++) {
                if (operatorParametersList.get(i).getInputValue().isEmpty()) {
                    isCallFetch = false;
                    L.toastS(getApplicationContext(), operatorParametersList.get(i).getErrorMessage());
                    break;
                } else {
                    HashMap<String,String> fieldsData = new HashMap<String,String>();
                    fieldsData.put("paramname", operatorParametersList.get(i).getParamName());
                    fieldsData.put("paramval", operatorParametersList.get(i).getInputValue());

                    fieldsDataArr.add(fieldsData);

                    if("utility_acc_no".equalsIgnoreCase(operatorParametersList.get(i).getParamName())){
                        data.put("utility_acc_no",operatorParametersList.get(i).getInputValue());
                    }
                }
            }
            data.put("postalcode",Util.LoadPrefData(getApplicationContext(),Keys.PINCODE));
        }
        if (isCallFetch && TextUtils.isEmpty(senderName)) {
            isCallFetch = false;
            L.toastS(getApplicationContext(), "Enter Sender Name");
        } else if (isCallFetch && senderMobile.length() < 10) {
            isCallFetch = false;
            L.toastS(getApplicationContext(), "Enter Correct Sender Mobile Number");
        }
        if (isCallFetch){
            data.put("source_ip", Util.getIpAddress(context));
            data.put("confirmation_mobile_no", senderMobile);
            data.put("sender_name", senderName);
            data.put("operator_id", operator.getOperatorId() + "");
            data.put("latlong", Util.LoadPrefData(context, Keys.LATITUDE) + "," + Util.LoadPrefData(context, Keys.LONGITUDE));
            data.put("hc_channel", channel);
            data.put("operatorParams", fieldsDataArr);
        }
        return isCallFetch;
    }

    private boolean validatePayBill() {
        boolean isCallPay = true;
        data.put("amount", fetchDetails.getAmount());
        return isCallPay;
    }

    private void showOperatorCategoryDialog() {
        hideAndResetByCategoryViews();
        BillPayCategoryDialogFrag categoryDialogFrag = new BillPayCategoryDialogFrag();
        categoryDialogFrag.setListener(model -> {
            operatorCategory = model;
            Objects.requireNonNull(binding.tilSelectCategory.getEditText()).setText(model.getOperatorCategoryName());
            operatorsRequest();
        });
        categoryDialogFrag.setList(operatorCategorylist);
        categoryDialogFrag.show(getSupportFragmentManager(), "Category");
    }

    private void showOperatorLocationDialog() {
        BillPayLocationDialogFrag locationDialogFrag = new BillPayLocationDialogFrag();
        locationDialogFrag.setListener(model -> {
            hideAndResetByLocationViews();
            Objects.requireNonNull(binding.tilSelectLocation.getEditText()).setText(model.getOperatorLocationName());
            operatorLocation = model;
            operatorsRequest();
        });
        locationDialogFrag.setList(operatorLocationlist);
        locationDialogFrag.show(getSupportFragmentManager(), "Location");
    }

    private void showOperatorDialog() {
        BillPayOperatorDialogFrag billPayCategoryDialogFrag = new BillPayOperatorDialogFrag();
        billPayCategoryDialogFrag.setListener(model -> {
            Objects.requireNonNull(binding.tilSelectOperator.getEditText()).setText(model.getName());
            operator = model;
            operatorParametersRequest();
        });
        billPayCategoryDialogFrag.setList(operatorList);
        billPayCategoryDialogFrag.show(getSupportFragmentManager(), "Operator");
    }

    private void categoryRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            Util.showView(binding.llLoader);
            BillPayMainRequest request = new BillPayMainRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            operatorCategorylist.clear();
            hideAndResetByCategoryViews();
            RetrofitClient.getClient(getApplicationContext())
                    .operatorsCategoryRequest(request).enqueue(new Callback<OperatorCategoryResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<OperatorCategoryResponse> call, @NotNull retrofit2.Response<OperatorCategoryResponse> response) {
                            Util.hideView(binding.llLoader);
                            if (response.isSuccessful() && response.body() != null && "0".equalsIgnoreCase(response.body().getStatus())) {
                                OperatorCategoryResponse res = response.body();
                                if (res.getData() != null && !res.getData().isEmpty()) {
                                    operatorCategorylist.addAll(res.getData());
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OperatorCategoryResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                            Util.hideView(binding.llLoader);
                        }
                    });
        }
    }

    private void locationRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            Util.showView(binding.llLoader);
            BillPayMainRequest request = new BillPayMainRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            operatorLocationlist.clear();
            hideAndResetByLocationViews();
            RetrofitClient.getClient(getApplicationContext())
                    .operatorsLocationRequest(request).enqueue(new Callback<OperatorLocationResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<OperatorLocationResponse> call, @NotNull retrofit2.Response<OperatorLocationResponse> response) {
                            Util.hideView(binding.llLoader);
                            if (response.isSuccessful() && response.body() != null && "0".equalsIgnoreCase(response.body().getStatus())) {
                                OperatorLocationResponse res = response.body();
                                if (res.getData() != null && !res.getData().isEmpty()) {
                                    operatorLocationlist.addAll(res.getData());
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OperatorLocationResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                            Util.hideView(binding.llLoader);
                        }
                    });
        }
    }

    private void operatorsRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            Util.showView(binding.llLoader);
            BillPayMainRequest request = new BillPayMainRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setCategory_id(operatorCategory.getOperatorCategoryId() + "");
            request.setLocation(operatorLocation != null ? operatorLocation.getOperatorLocationId() : "0");
            operatorList.clear();
            hideAndResetViews();
            RetrofitClient.getClient(getApplicationContext())
                    .operatorsRequest(request).enqueue(new Callback<OperatorResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<OperatorResponse> call, @NotNull retrofit2.Response<OperatorResponse> response) {
                            Util.hideView(binding.llLoader);
                            if (response.isSuccessful() && response.body() != null && "0".equalsIgnoreCase(response.body().getStatus())) {
                                OperatorResponse res = response.body();
                                if (res.getData() != null && !res.getData().isEmpty()) {
                                    operatorList.addAll(res.getData());
                                    Util.showView(binding.tilSelectOperator);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OperatorResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                            Util.hideView(binding.llLoader);
                        }
                    });
        }
    }

    private void operatorParametersRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            Util.showView(binding.llLoader);
            BillPayMainRequest request = new BillPayMainRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            if (operator != null && operator.getOperatorId() != -1)
                request.setOperator_id(operator.getOperatorId() + "");
            operatorParametersList.clear();
            hideAndResetViews();
            RetrofitClient.getClient(getApplicationContext())
                    .operatorsParametersRequest(request).enqueue(new Callback<OperatorParametersResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<OperatorParametersResponse> call, @NotNull retrofit2.Response<OperatorParametersResponse> response) {
                            Util.hideView(binding.llLoader);
                            if (response.isSuccessful() && response.body() != null && "0".equalsIgnoreCase(response.body().getStatus())) {
                                OperatorParametersResponse res = response.body();
                                if (res.getData() != null && res.getData() != null && !res.getData().getData().isEmpty()) {
                                    operatorParametersList.addAll(res.getData().getData());
                                    viewAndResetViews();
                                }
                            }
                            ekoEditBoxAdapter.notifyDataSetChanged();
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onFailure(@NotNull Call<OperatorParametersResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failure " + t.getLocalizedMessage());
                            Util.hideView(binding.llLoader);
                            ekoEditBoxAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void fetchBillRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching bill details...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            FetchBillRequest request = new FetchBillRequest();
            request.setAgentId(agentID);
            request.setTxnKey(txn_key);
            request.setData(data);
            RetrofitClient.getClient(getApplicationContext())
                    .fetchBillRequest(request).enqueue(new Callback<FetchBillResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<FetchBillResponse> call, @NotNull retrofit2.Response<FetchBillResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 0) {
                                FetchBillResponse res = response.body();
                                if (res.getData() != null) {
                                    fetchDetails = res.getData();
                                    Util.showView(binding.tilCustomerName);
                                    Util.showView(binding.tilBillDate);
                                    Util.showView(binding.tilBillAmount);
                                    Objects.requireNonNull(binding.tilCustomerName.getEditText()).setText(fetchDetails.getUtilitycustomername());
                                    if(StringUtils.isNotBlank(fetchDetails.getBillDueDate()))
                                        Objects.requireNonNull(binding.tilBillDate.getEditText()).setText(fetchDetails.getBillDueDate());
                                    else
                                        Objects.requireNonNull(binding.tilBillDate.getEditText()).setText("NA");
                                    Objects.requireNonNull(binding.tilBillAmount.getEditText()).setText(fetchDetails.getAmount());
                                    Util.hideView(binding.llViewBill);
                                    Util.showView(binding.llProceed);

                                    binding.tilBillAmount.requestFocus();
                                }else{
                                    binding.billpayErrMsg.setText(response.body().getMessage());
                                }
                            }else{
                                if(response.body().getInvalidParams()!=null
                                && StringUtils.isNotBlank(response.body().getInvalidParams().getReason()))
                                    binding.billpayErrMsg.setText(response.body().getInvalidParams().getReason());
                                else
                                    binding.billpayErrMsg.setText(response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<FetchBillResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                            pd.dismiss();
                        }
                    });
        }
    }

    private void payBillRequest() {
        binding.billpayErrMsg.setText("");
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Paying...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            PayBillRequest request = new PayBillRequest();
            request.setAgentId(agentID);
            request.setTxnKey(txn_key);
            request.setOpname(operator.getName());
            request.setTransactionPin(tpin);
            request.setData(data);
            RetrofitClient.getClient(getApplicationContext())
                    .payBillRequest(request).enqueue(new Callback<PayBillResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<PayBillResponse> call, @NotNull retrofit2.Response<PayBillResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 0) {
                                PayBillResponse res = response.body();
                                if (0==res.getStatus()) {
                                    if (res.getData() != null) {
                                        Intent intent = new Intent(getApplicationContext(), BillPaymentReceiptActivity.class);
                                        intent.putExtra(Keys.OBJECT, Util.getJsonFromModel(res.getData()));
                                        intent.putExtra(Keys.OBJECT2,Util.getJsonFromModel(data));
                                        intent.putExtra(Keys.MESSAGE,res.getMessage());
                                        intent.putExtra(Keys.Service_OP,operator.getName());
                                        intent.putExtra(Keys.DUE_DATE,binding.tilBillDate.getEditText().getText().toString());
                                        intent.putExtra(Keys.CUSTOMER_NAME,binding.tilCustomerName.getEditText().getText().toString());
                                        startActivity(intent);
                                    }else {
                                        binding.billpayErrMsg.setText(response.body().getMessage());
                                        L.toastS(getApplicationContext(),res.getMessage());
                                    }
                                }else {
                                    binding.billpayErrMsg.setText(response.body().getMessage());
                                    L.toastS(getApplicationContext(),res.getMessage());
                                }
                            }else {
                                binding.billpayErrMsg.setText(response.body().getMessage());
                                L.toastS(getApplicationContext(),"No Response");
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<PayBillResponse> call, @NotNull Throwable t) {
                            binding.billpayErrMsg.setText("Technical Failure");
                            L.toastS(getApplicationContext(), "Technical Failure" + t.getLocalizedMessage());
                            pd.dismiss();
                        }
                    });
        }
    }

    private void transactionPinDialog() {
        final Dialog dialog_status = new Dialog(BillPaymentActivity.this);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.transaction_pin_dialog);
        dialog_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputLayout til_enter_tpin =  dialog_status.findViewById(R.id.til_enter_tpin);

        Button btn_confirm_tpin =  dialog_status.findViewById(R.id.btn_confirm_tpin);
        Button close_confirm_tpin =  dialog_status.findViewById(R.id.close_confirm_tpin);

        btn_confirm_tpin.setOnClickListener(view -> {
            if(TextUtils.isEmpty(til_enter_tpin.getEditText().getText().toString().trim())){
                Toast.makeText(context, "Enter valid 4-digit Transaction pin!!", Toast.LENGTH_SHORT).show();
                til_enter_tpin.getEditText().requestFocus();
            }else{
                tpin=til_enter_tpin.getEditText().getText().toString().trim();
                dialog_status.dismiss();
                payBillRequest();
            }

        });

        close_confirm_tpin.setOnClickListener(view -> {
            dialog_status.cancel();
            hideKeyBoard(til_enter_tpin.getEditText());
        });

        dialog_status.show();
    }
    private void hideAndResetByCategoryViews() {
        operatorLocation = null;
        Objects.requireNonNull(binding.tilSelectLocation.getEditText()).setText("");
        hideAndResetByLocationViews();
    }

    private void hideAndResetByLocationViews() {
        operator = null;
        Objects.requireNonNull(binding.tilSelectOperator.getEditText()).setText("");
        Util.hideView(binding.tilSelectOperator);
        hideAndResetViews();
    }

    private void hideAndResetViews() {
        Util.hideView(binding.rvDynamic);
        Util.hideView(binding.tilSenderMob);
        Util.hideView(binding.tilSenderName);
        Util.hideView(binding.rgOfflineOnline);
        Util.hideView(binding.rbOffline);
        Util.hideView(binding.rbOnline);
        Util.hideView(binding.tilCustomerName);
        Util.hideView(binding.tilBillAmount);
        Util.hideView(binding.tilBillDate);
        Util.hideView(binding.llViewBill);
        Util.hideView(binding.llProceed);
        Objects.requireNonNull(binding.tilCustomerName.getEditText()).setText("");
        Objects.requireNonNull(binding.tilBillAmount.getEditText()).setText("");
        Objects.requireNonNull(binding.tilBillDate.getEditText()).setText("");

    }

    private void viewAndResetViews() {
        Util.showView(binding.rvDynamic);
        Util.showView(binding.tilSenderMob);
        Util.showView(binding.tilSenderName);
        Util.showView(binding.rgOfflineOnline);
        Util.showView(binding.llViewBill);
        if (operator!=null && operator.getHighCommissionChannel()!=-1){
            binding.rbOnline.setChecked(true);
            if (operator.getHighCommissionChannel()==1) {
                Util.showView(binding.rbOnline);
                Util.showView(binding.rbOffline);
            }else {
                Util.showView(binding.rbOnline);
                Util.hideView(binding.rbOffline);
            }
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
