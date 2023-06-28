package in.msmartpay.agent.rechargeBillPay;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.myWallet.TransactionHistoryReceipt;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.BillPayRequest;
import in.msmartpay.agent.network.model.BillPayResponse;
import in.msmartpay.agent.network.model.post.OperatorData;
import in.msmartpay.agent.network.model.wallet.OperatorModel;
import in.msmartpay.agent.network.model.wallet.TransactionItems;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.RandomNumber;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

public class BillPayActivity extends BaseActivity {

    private EditText amtt;
    private TextView tv_cn_hint, tv_cn,tv_dueAmount, tv_amunt_hint, tv_oprator_hint, tv_oprator, tv_dueDate, tv_customerName, tv_service;
    private LinearLayout ll_customer, ll_dueAmount, ll_dueDate;
    private String connectionNo, amountString;
    private Context context;
    private ProgressDialogFragment pd;
    private String mob = "", agentID, txn_key,tpinStatus="",tpin="";
    private String AD1 = "", AD2 = "", AD3 = "", AD4 = "", CN = "", OP = "", operator, op, referenceId = "";
    //private String gTPin_Url = HttpURL.GetTPinVerified;
    private Dialog d;
    private OperatorModel operatorCodeModel;
    private OperatorData operatorData;
    private String requesdID;
    private Button btn_proceed;
    private int value = 0;
    private double dueAmt = 0;
    private String dueAmt_Req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_pay_activity);

        context = BillPayActivity.this;
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Confirm Payment");

        tv_amunt_hint = findViewById(R.id.tv_amunt_hint);
        tv_service = findViewById(R.id.tv_service);
        btn_proceed = findViewById(R.id.btn_proceed);
        tv_cn_hint = findViewById(R.id.tv_cn_hint);
        tv_cn = findViewById(R.id.tv_cn);
        amtt = findViewById(R.id.id_sure_ask_amount);
        tv_oprator_hint = findViewById(R.id.tv_oprator_hint);
        tv_oprator = findViewById(R.id.tv_oprator);
        tv_dueDate = findViewById(R.id.tv_dueDate);
        tv_customerName = findViewById(R.id.tv_customerName);
        tv_dueAmount = findViewById(R.id.tv_dueAmount);
        ll_customer = findViewById(R.id.ll_customer);
        ll_dueAmount = findViewById(R.id.ll_dueAmount);
        ll_dueDate = findViewById(R.id.ll_dueDate);


        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        tpinStatus = Util.LoadPrefData(getApplicationContext(), Keys.TPIN_STATUS);

        Util.hideView(ll_customer);
        Util.hideView(ll_dueDate);
        Util.hideView(ll_dueAmount);

        Intent intent = getIntent();
        if (intent != null) {
            connectionNo = intent.getStringExtra("CN");
            amountString = intent.getStringExtra("Amt");
            mob = intent.getStringExtra("Mob") == null ? "" : intent.getStringExtra("Mob");
            AD1 = intent.getStringExtra("AD1") == null ? "" : intent.getStringExtra("AD1");
            AD2 = intent.getStringExtra("AD2") == null ? "" : intent.getStringExtra("AD2");
            AD3 = intent.getStringExtra("AD3") == null ? "" : intent.getStringExtra("AD3");
            tv_cn_hint.setText(intent.getStringExtra("CN_hint"));
            operatorCodeModel = getGson().fromJson(intent.getStringExtra(getString(R.string.pay_operator_model)), OperatorModel.class);
            operatorData = getGson().fromJson(intent.getStringExtra(getString(R.string.pay_operator_data)), OperatorData.class);
        }
        tv_cn.setText(connectionNo);
        amtt.setText(amountString);
        tv_oprator.setText(operatorCodeModel.getDisplayName());
        tv_service.setText(operatorCodeModel.getService());

        if (operatorData.getViewbill().equalsIgnoreCase("1")) {
            btn_proceed.setText("Get Bill Details");
        }
        btn_proceed.setOnClickListener(v -> {
            if (operatorData.getViewbill().equalsIgnoreCase("1") && value == 0) {
                submitPayment("ViewBill");
            } else {
                boolean validationStatus=false;
                String message="";
                dueAmt_Req=amtt.getText().toString();
                if (value == 1) {
                    if (dueAmt > 0)
                        validationStatus=true;
                    else
                        message="Due amount is greater than 0";
                } else {
                    validationStatus=true;
                }
                if(validationStatus){
                    if("Y".equalsIgnoreCase(tpinStatus)){
                        transactionPinDialog();
                    }else{
                        submitPayment("Pay");
                    }
                }else{
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void transactionPinDialog() {
        final Dialog dialog_status = new Dialog(BillPayActivity.this);
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
                submitPayment("Pay");
            }

        });

        close_confirm_tpin.setOnClickListener(view -> {
            dialog_status.cancel();
            hideKeyBoard(til_enter_tpin.getEditText());
        });

        dialog_status.show();
    }

    private void submitPayment(final String reqType) {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {

            if (requesdID == null)
                requesdID = RandomNumber.getTranId_20(agentID);
            if (reqType.equalsIgnoreCase("ViewBill"))
                pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching your bill details.....");
            else
                pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Your payment request is processing.Please wait...");

            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            BillPayRequest request = new BillPayRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService(operatorCodeModel.getService());
            request.setOP(operatorCodeModel.getOpCode());
            request.setOPName(operatorCodeModel.getOperatorName());
            request.setReqType(reqType);
            request.setCN(connectionNo);
            request.setAD1(AD1);
            request.setAD2(AD2);
            request.setAD3(AD3);
            request.setAD4(mob);
            request.setREQUEST_ID(requesdID);
            request.setReference_id(referenceId);
            request.setLatitude(Util.LoadPrefData(context, getString(R.string.latitude)));
            request.setLongitude(Util.LoadPrefData(context, getString(R.string.longitude)));
            request.setIp(Util.getIpAddress(context));
            if (dueAmt > 0) {
                request.setAMT(dueAmt_Req);
            } else {
                request.setAMT(amountString);
            }
            RetrofitClient.getClient(getApplicationContext())
                    .billPay(request).enqueue(new Callback<BillPayResponse>() {
                @Override
                public void onResponse(@NotNull Call<BillPayResponse> call, @NotNull retrofit2.Response<BillPayResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        BillPayResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            if (value == 0 && reqType.equalsIgnoreCase("ViewBill")) {
                                value = 1;
                                tv_customerName.setText(res.getCustomername());
                                tv_dueDate.setText(res.getDuedate());
                                tv_dueAmount.setText(res.getDueamount());
                                amtt.setText(res.getDueamount());
                                tv_amunt_hint.setText("Due Amount");
                                dueAmt = Double.parseDouble(res.getDueamount());
                                dueAmt_Req = res.getDueamount();
                                btn_proceed.setText("Proceed to pay");
                                Util.showView(ll_customer);
                                Util.showView(ll_dueDate);
                                referenceId = res.getReference_id();
                                // ll_dueAmount.setVisibility(View.VISIBLE);
                            } else {

                                TransactionItems transactionModel = new TransactionItems();
                                transactionModel.setTran_id(res.getTid());
                                transactionModel.setMobile_operator(operatorCodeModel.getDisplayName());
                                transactionModel.setMobile_number(connectionNo);
                                transactionModel.setService("Utility");
                                transactionModel.setAction_on_bal_amt("Debit");
                                transactionModel.setTran_status(res.getTxn_status());
                                transactionModel.setTxnAmount(amtt.getText().toString());
                                transactionModel.setNet_amout(amtt.getText().toString());
                                transactionModel.setDeductedAmt(amtt.getText().toString());
                                transactionModel.setDot(new Date().toString());
                                transactionModel.setTot("NA");
                                transactionModel.setAgent_balAmt_b_Ded("0");
                                transactionModel.setAgent_F_balAmt("0");
                                transactionModel.setBankRefId(res.getOperatorId());
                                transactionModel.setOperatorId(res.getOperatorId());
                                transactionModel.setCommission("0");
                                transactionModel.setServiceCharge("0");
                                transactionModel.setRemark(res.getResponseMessage());
                                transactionModel.setBene_Account("NA");
                                transactionModel.setBene_Bank_IFSC("NA");
                                transactionModel.setCustomer_name(tv_customerName.getText()==null?"NA":tv_customerName.getText().toString());
                                transactionModel.setDue_date(tv_dueDate.getText()==null?"NA":tv_dueDate.getText().toString());

                                Intent intent = new Intent( getApplicationContext(), TransactionHistoryReceipt.class);
                                intent.putExtra("position", 1);
                                intent.putExtra("historyModel", Util.getGson().toJson(transactionModel));
                                startActivity(intent);
                                finish();
                            }
                        } else if (res.getResponseCode() != null && res.getResponseCode().equals("1")) {
                            L.toastS(context, res.getResponseMessage());
                        } else {
                            L.toastS(context, "Unable To Process Your Request. Please try later");
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BillPayResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
