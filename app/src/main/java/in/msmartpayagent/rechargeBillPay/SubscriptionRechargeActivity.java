package in.msmartpayagent.rechargeBillPay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import in.msmartpayagent.R;
import in.msmartpayagent.network.AppMethods;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainResponse;
import in.msmartpayagent.network.model.OperatorsRequest;
import in.msmartpayagent.network.model.OperatorsResponse;
import in.msmartpayagent.network.model.PlanRequest;
import in.msmartpayagent.network.model.PlanResponse;
import in.msmartpayagent.network.model.RechargeRequest;
import in.msmartpayagent.network.model.wallet.OperatorModel;
import in.msmartpayagent.rechargeBillPay.operator.OperatorSearchActivity;
import in.msmartpayagent.rechargeBillPay.plans.PlansActivity;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.RandomNumber;
import in.msmartpayagent.utility.Util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class SubscriptionRechargeActivity extends BaseActivity {
    private static final int REQUEST_PLAN = 0212;
    private static final int REQUEST_OPRATPOR = 02120;
    private LinearLayout linear_proceed_dth;
    private EditText edit_account_no_dth, edit_amount_dth ;
    private MaterialAutoCompleteTextView edit_operator;
    private TextView tv_view_plan, tv_view_offer, tv_cust_details, tv_details;
    private String operatorData;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private ProgressDialogFragment pd;
    private Context context;
    private String agentID, txn_key = "",tpinSTatus,tpin="";
    private ArrayList<OperatorModel> operatorList = new ArrayList<>();
    private OperatorModel opreatorModel = null;

    private String opcode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dth_recharge_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("OTT Subscriptions");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = SubscriptionRechargeActivity.this;

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        edit_account_no_dth = findViewById(R.id.edit_account_no_dth);
        edit_amount_dth = findViewById(R.id.edit_amount_dth);
        edit_operator = findViewById(R.id.edit_operator);
        linear_proceed_dth = (LinearLayout) findViewById(R.id.linear_proceed_dth);
        tv_view_plan = findViewById(R.id.tv_view_plan);
        tv_view_offer = findViewById(R.id.tv_view_offer);
        tv_cust_details = findViewById(R.id.tv_cust_details);
        tv_details = findViewById(R.id.tv_details);

        operatorsCodeRequest();

        edit_operator.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OperatorSearchActivity.class);
            intent.putExtra(Keys.ARRAY_LIST,Util.getJsonFromModel(operatorList));
            startActivityForResult(intent,REQUEST_OPRATPOR);
        });

        edit_account_no_dth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() > 5) {
                    if (opreatorModel != null) {
                        tv_view_offer.setVisibility(View.VISIBLE);
                        tv_cust_details.setVisibility(View.VISIBLE);
                    }
                } else {
                    tv_view_offer.setVisibility(View.GONE);
                    tv_cust_details.setVisibility(View.GONE);
                    tv_details.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        linear_proceed_dth.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                if (opreatorModel==null) {
                    Toast.makeText(context, "Select Operator !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_account_no_dth.getText().toString().trim()) && edit_account_no_dth.getText().toString().trim().length() < 5) {
                    edit_account_no_dth.requestFocus();
                    Toast.makeText(context, "Enter Account Number !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_amount_dth.getText().toString().trim())) {
                    edit_amount_dth.requestFocus();
                    Toast.makeText(context, "Enter Amount !!!", Toast.LENGTH_SHORT).show();
                } else {
                    proceedConfirmationDialog();
                }
            } else {
                Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
            }
        });

        tv_view_plan.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlansActivity.class);
            intent.putExtra("type", "dth");
            intent.putExtra("operator", opreatorModel.getDisplayName());
            intent.putExtra("mobile", "");
            startActivityForResult(intent, REQUEST_PLAN);
        });
        tv_view_offer.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlansActivity.class);
            intent.putExtra("type", "dth");
            intent.putExtra("operator", opreatorModel.getDisplayName());
            intent.putExtra("mobile", edit_account_no_dth.getText().toString());
            startActivityForResult(intent, REQUEST_PLAN);
        });
        tv_cust_details.setOnClickListener(v -> {
            customerDetailsRequest();

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
            request.setService("subscription");

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


    //===========CustomerRequest==============
    private void customerDetailsRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Customer Derails...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            PlanRequest request = new PlanRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setType("dthinfo");
            request.setOperator(opreatorModel.getDisplayName());
            request.setMobile(edit_account_no_dth.getText().toString().trim());
            request.setClient(AppMethods.CLIENT);
            RetrofitClient.getClient(getApplicationContext())
                    .getPlans(request).enqueue(new Callback<PlanResponse>() {
                @Override
                public void onResponse(@NotNull Call<PlanResponse> call, @NotNull retrofit2.Response<PlanResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        PlanResponse res = response.body();
                        if (res.getStatus()==1) {
                            StringBuilder builder = new StringBuilder();
                            tv_details.setVisibility(View.VISIBLE);

                            JSONArray array = null;
                            try {
                                array = new JSONArray(res.getRecords().toString());
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (object.has("customerName"))
                                        builder.append("Name : " + object.getString("customerName")+"\n");
                                    if (object.has("status"))
                                        builder.append(", Status : " + object.getString("status")+"\n");
                                    if (object.has("MonthlyRecharge"))
                                        builder.append(", Monthly Recharge : " + object.getString("MonthlyRecharge")+"\n");
                                    if (object.has("lastrechargeamount"))
                                        builder.append(", Last Recharge Amount : " + object.getString("lastrechargeamount")+"\n");
                                    if (object.has("lastrechargedate"))
                                        builder.append(", Last Recharge Date : " + object.getString("lastrechargedate")+"\n\n");
                                    if (object.has("planname")) {
                                        String planname= object.getString("planname");
                                        planname=planname.replaceAll(",","\n");
                                        builder.append(", Plan Name : " + planname);
                                    }
                                }
                            } catch (JSONException e) {
                                //e.printStackTrace();
                            }

                            tv_details.setText(builder.toString());
                        } else {
                            L.toastS(context, res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PlanResponse> call, @NotNull Throwable t) {
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

        tv_mobile_dth.setText("DTH Number");
        confirm_mobile.setText(edit_account_no_dth.getText().toString());
        confirm_operator.setText(opreatorModel.getOperatorName());
        confirm_amount.setText(edit_amount_dth.getText().toString());

        tv_recharge.setOnClickListener(view -> {
            try {

                d.dismiss();
                if("Y".equalsIgnoreCase(tpinSTatus)){
                    transactionPinDialog();
                }else{
                    dthRechargeRequest();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tv_cancel.setOnClickListener(v -> d.cancel());

        d.show();
    }

    //===========postpaidRechargeRequest==============

    private void dthRechargeRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Recharging...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            RechargeRequest request = new RechargeRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService("dth");
            request.setOperator(opreatorModel.getCode());
            request.setMobile_no(edit_account_no_dth.getText().toString().trim());
            request.setOpCode(opreatorModel.getOpCode());
            request.setRequest_id(RandomNumber.getTranId_14());
            request.setLatitude(Util.LoadPrefData(context, getString(R.string.latitude)));
            request.setLongitude(Util.LoadPrefData(context, getString(R.string.longitude)));
            request.setIp(Util.getIpAddress(context));
            request.setAmount(edit_amount_dth.getText().toString().trim());
            request.setTransactionPin(tpin);

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
                            in.putExtra("mobileno", edit_account_no_dth.getText().toString().trim());
                            in.putExtra("requesttype", "dth");
                            in.putExtra("operator", opreatorModel.getDisplayName());
                            in.putExtra("txnId", res.getTxnId());
                            in.putExtra("operatorId", res.getOperatorId());
                            in.putExtra("amount", edit_amount_dth.getText().toString().trim());
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

    private void transactionPinDialog() {
        final Dialog dialog_status = new Dialog(SubscriptionRechargeActivity.this);
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
                dthRechargeRequest();
            }
        });

        close_confirm_tpin.setOnClickListener(view -> {
            dialog_status.cancel();
            hideKeyBoard(til_enter_tpin.getEditText());
        });

        dialog_status.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLAN && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getStringExtra("price") != null)
                    edit_amount_dth.setText(data.getStringExtra("price"));
            }
        }else if (requestCode == REQUEST_OPRATPOR && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(Keys.ARRAY_LIST))
                    operatorList = (ArrayList<OperatorModel>) Util.getListFromJson(data.getStringExtra(Keys.ARRAY_LIST),OperatorModel.class);
                    opreatorModel = Util.getGson().fromJson(data.getStringExtra(Keys.OBJECT),OperatorModel.class);
                    edit_operator.setText(opreatorModel.getDisplayName());
            }
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
