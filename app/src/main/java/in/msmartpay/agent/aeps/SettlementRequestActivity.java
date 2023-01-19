package in.msmartpay.agent.aeps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import in.msmartpay.agent.network.model.dmr.AccountVerifyRequest;
import in.msmartpay.agent.network.model.dmr.AccountVerifyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.aeps.FundSettlementBank;
import in.msmartpay.agent.network.model.aeps.FundSettlementRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;

public class SettlementRequestActivity extends BaseActivity {

    private EditText et_recipient_id, et_available_amount,et_wallet_balance, et_amount, et_remark,et_fund_account_no,et_fund_ifsc;
    private TextView tv_cashout_charges;
    private Button btn_request;
    private Context context;
    private ProgressDialog pd;
    private String account, amount, type, recipientId, remarks = "",radioButtonValue="";
    private String AgentId = "", txnKey = "";
    private FundSettlementBank bank;
    private Double minimumAount = 100.0, maximumAmount = 200000.0, enterAmount, availableAmount = 0.0;
    private RadioGroup radioPayGroup;
    private RadioButton radioPayButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aeps_request_settlement_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Move to Bank Request");
        context = SettlementRequestActivity.this;

        tv_cashout_charges = findViewById(R.id.tv_cashout_charges);
        et_recipient_id = findViewById(R.id.et_recipient_id);
        et_fund_account_no = findViewById(R.id.et_fund_account_no);
        et_fund_ifsc = findViewById(R.id.et_fund_ifsc);
        et_available_amount = findViewById(R.id.et_available_amount);
        et_wallet_balance = findViewById(R.id.et_wallet_balance);
        et_amount = findViewById(R.id.et_amount);
        et_remark = findViewById(R.id.et_remark);
        radioPayGroup = (RadioGroup) findViewById(R.id.radioGroup);

        btn_request = findViewById(R.id.btn_request);

        tv_cashout_charges.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        AgentId = Util.LoadPrefData(context, Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(context, Keys.TXN_KEY);

        et_wallet_balance.setText(Util.LoadPrefData(context, Keys.BALANCE));

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Keys.OBJECT)) {
                bank = Util.getGson().fromJson(intent.getStringExtra(Keys.OBJECT), FundSettlementBank.class);
                et_recipient_id.setText(bank.getBank_name());
                et_fund_account_no.setText(bank.getAccount());
                et_fund_ifsc.setText(bank.getIfsc());
                recipientId=bank.getRecipient_id();

            }
            if (intent.hasExtra(Keys.OBJECT2)) {
                availableAmount = Double.parseDouble(Util.getAmount(intent.getStringExtra(Keys.OBJECT2)));
                et_available_amount.setText(availableAmount.toString());
            }
        }

        btn_request.setOnClickListener(v -> {

            amount = et_amount.getText().toString().trim();
            remarks = et_remark.getText().toString().trim();

            enterAmount = Double.parseDouble(Util.getAmount(amount));
            if (TextUtils.isEmpty(amount)) {
                L.toastS(context, "Enter Amount");
            } else if (enterAmount < minimumAount) {
                L.toastS(context, "Minimum settlement limit is " + minimumAount);
            } else if (enterAmount > maximumAmount) {
                L.toastS(context, "Maximum settlement limit is " + maximumAmount);
            } else if (enterAmount > availableAmount) {
                L.toastS(context, "Amount should be less than or equal to available balance. Available balance is " + availableAmount );
            } else {

                int selectedId = radioPayGroup.getCheckedRadioButtonId();
                radioPayButton = (RadioButton) findViewById(selectedId);
                if (radioPayGroup.getCheckedRadioButtonId() != -1) {
                    radioButtonValue = radioPayButton.getText().toString();
                    requestFundSettlement();
                }else{
                    L.toastS(context, "Select Transfer Type");
                }

            }
        });

        tv_cashout_charges.setOnClickListener(v -> {
            chargesHelpDialog();

        });
    }

    private void requestFundSettlement() {
        if (NetworkConnection.isConnectionAvailable(this)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Processing request. Please wait...", true);
            pd.setProgress(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(true);
            FundSettlementRequest req = new FundSettlementRequest();
            req.setAgentID(AgentId);
            req.setKey(txnKey);
            req.setAmount(amount);
            req.setRecipient_id(recipientId);
            req.setRemark(remarks);
            req.setType("request");
            req.setPayment_mode(radioButtonValue);
            RetrofitClient.getClient(context).requestFundSettlement(req).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        if ("0".equals(res.getStatus())) {
                            L.toastS(context, res.getMessage());
                        } else {
                            L.toastS(context, res.getMessage());
                        }
                        showConfirmationDialog(res.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<MainResponse2> call, Throwable t) {
                    pd.dismiss();
                }
            });
        }
    }

    public void showConfirmationDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.common_confirmation_dialog);

        final TextView tv_confirmation_dialog = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        tv_confirmation_dialog.setText(msg);
        tv_confirmation_dialog.setVisibility(View.VISIBLE);
        final Button btn_ok = (Button) d.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(view ->{
            d.dismiss();
            finish();
        });

        d.show();
    }

    private void chargesHelpDialog() {
        final Dialog dialog_status = new Dialog(SettlementRequestActivity.this);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.charges_help_layout);
        dialog_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button close_cashout_charge_dialog =  dialog_status.findViewById(R.id.close_cashout_charge_dialog);

        close_cashout_charge_dialog.setOnClickListener(view -> {
            dialog_status.cancel();

        });

        dialog_status.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
