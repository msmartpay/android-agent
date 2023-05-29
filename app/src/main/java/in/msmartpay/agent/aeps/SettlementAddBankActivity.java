package in.msmartpay.agent.aeps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.msmartpay.agent.network.model.dmr.AccountVerifyRequest;
import in.msmartpay.agent.network.model.dmr.AccountVerifyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import in.msmartpay.agent.R;
import in.msmartpay.agent.dialogs.BankSearchDialogFrag;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.aeps.AddFundSettlementBankRequest;
import in.msmartpay.agent.network.model.dmr.BankListDmrResponse;
import in.msmartpay.agent.network.model.dmr.BankListModel;
import in.msmartpay.agent.network.model.dmr.BankRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;

public class SettlementAddBankActivity extends BaseActivity {

    private EditText et_bank, et_ac,et_confirm_ac, et_ifsc, et_ac_holder;
    private Button btn_add,btn_verify;

    private Context context;
    private ProgressDialog pd;
    private String AgentId = "", txnKey = "",AGENT_MOB="";
    private List<BankListModel> bankList = new ArrayList<>();
    private BankListModel bankModel;
    private String account,confirmAccount, ifsc, beneficiaryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aeps_add_settlement_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Move to Bank");
        context = SettlementAddBankActivity.this;
        et_bank = findViewById(R.id.et_bank);
        et_ac = findViewById(R.id.et_ac);
        et_confirm_ac = findViewById(R.id.et_confirm_ac);
        et_ifsc = findViewById(R.id.et_ifsc);
        et_ac_holder = findViewById(R.id.et_ac_holder);
        btn_add = findViewById(R.id.btn_add);
        btn_verify = findViewById(R.id.btn_verify_settlement_bank);

        AgentId = Util.LoadPrefData(context, Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(context, Keys.TXN_KEY);
        AGENT_MOB=Util.LoadPrefData(context, Keys.AGENT_MOB);

        getBankList();
        et_bank.setOnClickListener(v -> {
            showSearchDialog();
        });

        btn_add.setOnClickListener(v -> {
            account = et_ac.getText().toString().trim();
            confirmAccount = et_confirm_ac.getText().toString().trim();
            ifsc = et_ifsc.getText().toString().trim();
            beneficiaryName = et_ac_holder.getText().toString().trim();

            if (bankModel == null) {
                L.toastS(context, "Select Bank");
            } else if (TextUtils.isEmpty(account)) {
                L.toastS(context, "Enter Bank Account");
            } else if (TextUtils.isEmpty(confirmAccount)) {
                L.toastS(context, "Confirm Bank Account");
            } else if (!account.equalsIgnoreCase(confirmAccount)) {
                L.toastS(context, "Bank Account not matched with Confirm Account Number");
            } else if (TextUtils.isEmpty(beneficiaryName)) {
                L.toastS(context, "Enter Account Holder");
            } else if (TextUtils.isEmpty(ifsc)) {
                L.toastS(context, "Enter Bank IFSC");
            } else {
                addSettlementBank();
            }
        });
        btn_verify.setOnClickListener(v -> {
            account = et_ac.getText().toString().trim();
            ifsc = et_ifsc.getText().toString().trim();
            beneficiaryName = et_ac_holder.getText().toString().trim();

            if (bankModel == null) {
                L.toastS(context, "Select Bank");
            } else if (TextUtils.isEmpty(account)) {
                L.toastS(context, "Enter Bank Account");
            } else if (TextUtils.isEmpty(beneficiaryName)) {
                L.toastS(context, "Enter Account Holder");
            } else if (TextUtils.isEmpty(ifsc)) {
                L.toastS(context, "Enter Bank IFSC");
            } else {
                VerifyAccountRequest();
            }
        });
    }

    private void getBankList(){
        if (NetworkConnection.isConnectionAvailable(context)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgress(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(true);
            BankRequest request = new BankRequest();
            request.setAgentID(AgentId);
            request.setKey(txnKey);
            request.setSenderId(AGENT_MOB);
            RetrofitClient.getClient(context).getBankListDmr(request)
                    .enqueue(new Callback<BankListDmrResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(Call<BankListDmrResponse> call, Response<BankListDmrResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                BankListDmrResponse res = response.body();
                                if ("0".equals(res.getStatus())) {
                                    if (res.getBankList()!=null && res.getBankList().size()>0){
                                        bankList = res.getBankList();
                                    }
                                } else {
                                    L.toastS(context,res.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BankListDmrResponse> call, Throwable t) {
                            pd.dismiss();
                        }
                    });
        }
    }

    private void addSettlementBank() {
        if (NetworkConnection.isConnectionAvailable(this)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgress(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(true);
            AddFundSettlementBankRequest req = new AddFundSettlementBankRequest();
            req.setAgentID(AgentId);
            req.setKey(txnKey);
            req.setAccount(account);
            req.setBank_name(bankModel.getBankName());
            req.setIfsc(ifsc);
            req.setBeneficiary_name(beneficiaryName);
            RetrofitClient.getClient(context).addFundSettlementBank(req).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        showConfirmationDialog(1,res.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<MainResponse2> call, Throwable t) {
                    pd.dismiss();
                }
            });
        }
    }

    private void VerifyAccountRequest() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {

            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...Verifying Account", true);
            pd.setProgress(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(true);

            AccountVerifyRequest verifyRequest = new AccountVerifyRequest();
            verifyRequest.setAgentID(AgentId);
            verifyRequest.setKey(txnKey);
            verifyRequest.setSenderId(AGENT_MOB);
            verifyRequest.setIFSC(ifsc);
            verifyRequest.setBankAccount(account.trim());
            verifyRequest.setBankCode(bankModel.getBankCode());
            verifyRequest.setBankName(bankModel.getBankName());
            verifyRequest.setREQUEST_ID(String.valueOf((long) Math.floor(Math.random() * 90000000000000L) + 10000000000000L));

            RetrofitClient.getClient(getApplicationContext()).verifyAccount(verifyRequest).enqueue(new Callback<AccountVerifyResponse>() {
                @Override
                public void onResponse(Call<AccountVerifyResponse> call, Response<AccountVerifyResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        AccountVerifyResponse res = response.body();
                        if (res.getStatus().equals("0")) {
                            et_ac_holder.setText(res.getBeneName());
                            showConfirmationDialog(2,res.getMessage());
                        } else {
                            L.toastS(getApplicationContext(), res.getMessage());
                            showConfirmationDialog(2,res.getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(Call<AccountVerifyResponse> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(getApplicationContext(), "Error : " + t.getMessage());
                }
            });
        }
    }

    public void showConfirmationDialog(int id,String msg) {
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
            if(id==1){
                finish();
            }
        });

        d.show();
    }

    private void showSearchDialog() {
        BankSearchDialogFrag dialogFrag = BankSearchDialogFrag.newInstance(bankList, bankModel -> {
            this.bankModel = bankModel;
            et_bank.setText(bankModel.getBankName());
        });
        dialogFrag.show(getSupportFragmentManager(), "Search Bank");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
