package in.msmartpay.agent.rechargeBillPay;

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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse;
import in.msmartpay.agent.network.model.OperatorsRequest;
import in.msmartpay.agent.network.model.OperatorsResponse;
import in.msmartpay.agent.network.model.PlanRequest;
import in.msmartpay.agent.network.model.PlanResponse;
import in.msmartpay.agent.network.model.RechargeRequest;
import in.msmartpay.agent.network.model.wallet.OperatorModel;
import in.msmartpay.agent.rechargeBillPay.operator.OperatorSearchActivity;
import in.msmartpay.agent.rechargeBillPay.plans.PlansActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.RandomNumber;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class PrepaidMobileActivity extends BaseActivity {

    private static final int REQUEST_OPRATPOR = 02120;
    private static final int REQUEST_PLAN = 0212;
    private LinearLayout linear_proceed;
    private EditText edit_prepaid_mobile, edit_amount;
    private MaterialAutoCompleteTextView edit_operator;
    private TextView tv_view_plan, tv_view_offer;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private ProgressDialogFragment pd;
    private Context context;
    private String agentID, txn_key = "",tpinSTatus,tpin="";
    private ArrayList<OperatorModel> operatorList = new ArrayList<>();
    private OperatorModel opreatorModel = null;
    private String opcode = null,circle=null;
    private Spinner spinner_circle;
    private String[] plan_circle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_prepaid_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Prepaid Mobile");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = PrepaidMobileActivity.this;

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        tpinSTatus = Util.LoadPrefData(context,Keys.TPIN_STATUS);

        edit_prepaid_mobile = (EditText) findViewById(R.id.edit_prepaid_mobile);
        edit_amount = (EditText) findViewById(R.id.edit_amount);
        edit_operator = findViewById(R.id.edit_operator);
        linear_proceed = (LinearLayout) findViewById(R.id.linear_proceed);
        tv_view_plan = findViewById(R.id.tv_view_plan);
        tv_view_offer = findViewById(R.id.tv_view_offer);
        spinner_circle = findViewById(R.id.spinner_circle);
        plan_circle = getResources().getStringArray(R.array.plan_circle);

        //For operatorList
        operatorsCodeRequest();

        edit_operator.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OperatorSearchActivity.class);
            intent.putExtra(Keys.ARRAY_LIST, Util.getJsonFromModel(operatorList));
            startActivityForResult(intent, REQUEST_OPRATPOR);
        });

        edit_prepaid_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() == 10) {
                    fetchMobileOperatorByNumber(edit_prepaid_mobile.getText().toString().trim());
                    if (opreatorModel != null)
                        tv_view_offer.setVisibility(View.VISIBLE);
                } else {
                    tv_view_offer.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        spinner_circle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1) {
                    circle = spinner_circle.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        linear_proceed.setOnClickListener(view -> {
             if (isConnectionAvailable()) {
                 if (TextUtils.isEmpty(edit_prepaid_mobile.getText().toString().trim()) || edit_prepaid_mobile.getText().toString().trim().length() < 10) {
                     edit_prepaid_mobile.requestFocus();
                     Toast.makeText(context, "Enter 10 digit mobile no. !!!", Toast.LENGTH_SHORT).show();
                 } else if (opreatorModel == null) {
                     Toast.makeText(context, "Select Operator !!!", Toast.LENGTH_SHORT).show();
                 } else if (TextUtils.isEmpty(edit_amount.getText().toString().trim())) {
                     edit_amount.requestFocus();
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
            intent.putExtra("type", "mobile");
            intent.putExtra("operator", opreatorModel.getDisplayName());
            intent.putExtra("mobile", "");
            intent.putExtra("circle", circle);
            startActivityForResult(intent, REQUEST_PLAN);
        });
        tv_view_offer.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlansActivity.class);
            intent.putExtra("type", "mobile");
            intent.putExtra("operator", opreatorModel.getDisplayName());
            intent.putExtra("mobile", edit_prepaid_mobile.getText().toString());
            intent.putExtra("circle", circle);
            startActivityForResult(intent, REQUEST_PLAN);
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
            request.setService("mobile");

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

    private void proceedConfirmationDialog() {
        final Dialog d = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView confirm_mobile, confirm_operator, confirm_amount, tv_cancel, tv_recharge;
        confirm_mobile = (TextView) d.findViewById(R.id.confirm_mobile);
        confirm_operator = (TextView) d.findViewById(R.id.confirm_operator);
        confirm_amount = (TextView) d.findViewById(R.id.confirm_amount);
        tv_cancel = (TextView) d.findViewById(R.id.tv_cancel);
        tv_recharge = (TextView) d.findViewById(R.id.tv_recharge);

        confirm_mobile.setText(edit_prepaid_mobile.getText().toString());
        confirm_operator.setText(opreatorModel.getDisplayName());
        confirm_amount.setText(edit_amount.getText().toString());

        tv_recharge.setOnClickListener(view -> {
            try {
                d.dismiss();
                if("Y".equalsIgnoreCase(tpinSTatus)){
                    transactionPinDialog();
                }else{
                    prepaidRechargeRequest();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tv_cancel.setOnClickListener(v -> d.cancel());

        d.show();
    }

    private void fetchMobileOperatorByNumber(String mobileNumber) {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Plans...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            PlanRequest request = new PlanRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setMobile(mobileNumber);
            request.setClient(AppMethods.CLIENT);
            request.setType("operatorcheck");


            RetrofitClient.getClient(getApplicationContext()).getPlans(request)
                    .enqueue(new Callback<PlanResponse>() {
                        @Override
                        public void onResponse(Call<PlanResponse> call, retrofit2.Response<PlanResponse> response) {
                            if(pd!=null)
                                pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                PlanResponse res = response.body();
                                if (res.getStatus()==0) {
                                    try {
                                        JSONObject recordsObject = new JSONObject(res.getRecords().toString());
                                        //JSONObject records=recordsObject.optJSONObject("records");
                                        if(recordsObject!=null){
                                            if(recordsObject.getInt("status")==1){

                                                String fetchOperatorName=recordsObject.getString("Operator");

                                                for (OperatorModel model :  operatorList){
                                                    if(model.getDisplayName().equalsIgnoreCase(fetchOperatorName)){
                                                        opcode=model.getOpCode();
                                                        opreatorModel=model;
                                                        edit_operator.setText(model.getDisplayName());
                                                    }
                                                }
                                                circle=recordsObject.getString("comcircle");
                                                if("Delhi".equalsIgnoreCase(circle))
                                                    circle = "Delhi NCR";
                                                //spinner_circle.getSelectedItem().
                                                for(int i =0;i<plan_circle.length;i++){
                                                    if(circle.equalsIgnoreCase(plan_circle[i])){
                                                        int position=i+1;
                                                        spinner_circle.setSelection(position);
                                                    }
                                                }
                                                Util.showView(tv_view_offer);
                                                Util.showView(tv_view_plan);

                                                Intent intent = new Intent(context, PlansActivity.class);
                                                intent.putExtra("type", "mobile");
                                                intent.putExtra("operator", fetchOperatorName);
                                                intent.putExtra("mobile", mobileNumber);
                                                intent.putExtra("circle", circle);
                                                startActivityForResult(intent, REQUEST_PLAN);
                                            }
                                        }

                                        L.m2("Operator Name",recordsObject.toString());
                                    }catch (Exception e){
                                        L.toastS(getApplicationContext(), e.getMessage());
                                    }

                                }
                            } else {
                                L.toastS(getApplicationContext(), "No Response");
                                //finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<PlanResponse> call, Throwable t) {
                            if(pd!=null)
                                pd.dismiss();
                            L.toastS(getApplicationContext(), "Error " + t.getMessage());
                            //finish();
                        }
                    });
        }
    }

    private void prepaidRechargeRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Recharging...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            RechargeRequest request = new RechargeRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService("Mobile");
            request.setOperator(opcode);
            request.setMobile_no(edit_prepaid_mobile.getText().toString().trim());
            request.setOpCode(opcode);
            request.setRequest_id(RandomNumber.getTranId_14());
            request.setLatitude(Util.LoadPrefData(context, getString(R.string.latitude)));
            request.setLongitude(Util.LoadPrefData(context, getString(R.string.longitude)));
            request.setIp(Util.getIpAddress(context));
            request.setAmount(edit_amount.getText().toString().trim());
            request.setTransactionPin(tpin);
            Log.d("RechargeRequest",request.toString());
            RetrofitClient.getClient(getApplicationContext())
                    .recharge(request).enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse> call, @NotNull retrofit2.Response<MainResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0") || res.getResponseCode().equals("2")) {
                            Intent in = new Intent(context, SuccessDetailActivity.class);
                            in.putExtra("responce", res.getResponseMessage());
                            in.putExtra("mobileno", edit_prepaid_mobile.getText().toString().trim());
                            in.putExtra("requesttype", "prepaid-mobile");
                            in.putExtra("operator", opreatorModel.getDisplayName());
                            in.putExtra("txnId", res.getTxnId());
                            in.putExtra("operatorId", res.getOperatorId());
                            in.putExtra("amount", edit_amount.getText().toString().trim());
                            startActivity(in);
                            finish();
                        } else {
                            L.toastS(context, res.getResponseMessage());
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
        final Dialog dialog_status = new Dialog(PrepaidMobileActivity.this);
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
                prepaidRechargeRequest();
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
                    edit_amount.setText(data.getStringExtra("price"));
            }
        }else if (requestCode == REQUEST_OPRATPOR && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(Keys.ARRAY_LIST))
                    operatorList = (ArrayList<OperatorModel>) Util.getListFromJson(data.getStringExtra(Keys.ARRAY_LIST),OperatorModel.class);
                    opreatorModel = Util.getGson().fromJson(data.getStringExtra(Keys.OBJECT),OperatorModel.class);
                    edit_operator.setText(opreatorModel.getDisplayName());
                    opcode=opreatorModel.getOpCode();

                tv_view_plan.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(edit_prepaid_mobile.getText().toString()) && edit_prepaid_mobile.getText().toString().length() == 10) {
                    tv_view_offer.setVisibility(View.VISIBLE);
                } else {
                    tv_view_offer.setVisibility(View.GONE);
                }
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
