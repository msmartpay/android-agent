package in.msmartpay.agent.dmrPaySprint;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import in.msmartpay.agent.R;
import in.msmartpay.agent.myWallet.TransactionHistoryReceipt;
import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.dmr.MoneyTransferRequest;
import in.msmartpay.agent.network.model.dmr.SenderDetailsResponse;
import in.msmartpay.agent.network.model.dmr.ps.PSMoneyTransferResponse;
import in.msmartpay.agent.network.model.wallet.TransactionItems;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Smartkinda on 6/19/2017.
 */

public class PSImpsNeftActivity extends BaseActivity {

    private Button btnProceed;
    private EditText editAmount, editRemark;
    private ProgressDialogFragment pd;
    private RadioGroup radioPayGroup;
    private RadioButton radioPayButton;
    private String radioButtonValue;
    private String BeneName, BeneAccountNumber, BeneBankName, IMPS, NEFT, IFSCcode, RecipientId, Channel, RecipientIdType, BeneMobile;
    private TextView tvBeneName, tvBeneAccountNumber, tvIFSCcode,dmr_txn_message;
    private SharedPreferences sharedPreferences;
    private String agentID, txnKey, mobileNumber,tpinSTatus,tpin="";
    private String transactionAmount, Remark;
    private String SenderId, SenderName;
    private Context context;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_dmr_imps_neft_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transfer Payment");

        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener((v, event) -> {
            // TODO Auto-generated method stub
            v.requestFocusFromTouch();
            return false;
        });

        context = PSImpsNeftActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        mobileNumber = Util.LoadPrefData(getApplicationContext(), Keys.SENDER_MOBILE);
        tpinSTatus = Util.LoadPrefData(context,Keys.TPIN_STATUS);
        SenderDetailsResponse sender = Util.getGson().fromJson(Util.LoadPrefData(getApplicationContext(), Keys.SENDER),SenderDetailsResponse.class);
        mobileNumber = Util.LoadPrefData(getApplicationContext(), Keys.SENDER_MOBILE);
        SenderId = sender.getSenderDetails().getSenderId();
        SenderName = sender.getSenderDetails().getName();

        BeneName = getIntent().getStringExtra("BeneName");
        BeneAccountNumber = getIntent().getStringExtra("BeneAccountNumber");
        BeneBankName = getIntent().getStringExtra("BeneBankName");
        IFSCcode = getIntent().getStringExtra("IFSCcode");
        RecipientId = getIntent().getStringExtra("RecipientId");
        IMPS = getIntent().getStringExtra("IMPS");
        NEFT = getIntent().getStringExtra("NEFT");
        Channel = getIntent().getStringExtra("Channel");
        RecipientIdType = getIntent().getStringExtra("RecipientIdType");
        BeneMobile = getIntent().getStringExtra("BeneMobile");

        dmr_txn_message = findViewById(R.id.dmr_txn_message);
        tvBeneName = (TextView) findViewById(R.id.tv_payee_name);
        tvBeneAccountNumber = (TextView) findViewById(R.id.tv_payee_acc_no);
        tvIFSCcode = (TextView) findViewById(R.id.tv_payee_ifsc);
        editAmount = (EditText) findViewById(R.id.edit_amount);
        editRemark = (EditText) findViewById(R.id.edit_remark);
        btnProceed = (Button) findViewById(R.id.txtsubmit);
        radioPayGroup = (RadioGroup) findViewById(R.id.radioGroup);
        editAmount.requestFocus();

        tvBeneName.setText(BeneName);
        tvBeneAccountNumber.setText(BeneAccountNumber);
        tvIFSCcode.setText(IFSCcode);

        RadioButton radio_Neft = (RadioButton) findViewById(R.id.radio_neft);
        RadioButton radio_Imps = (RadioButton) findViewById(R.id.radio_imps);
        TextView text_Message = (TextView) findViewById(R.id.text_Message);

        if ("0".equalsIgnoreCase(Channel)) {

            radio_Neft.setChecked(true);
            radio_Neft.setVisibility(View.VISIBLE);
            radio_Imps.setVisibility(View.GONE);
            text_Message.setText("IMPS is not available for selected bank");
            text_Message.setVisibility(View.VISIBLE);
        } else if ("1".equalsIgnoreCase(Channel)) {

            radio_Neft.setVisibility(View.VISIBLE);
            radio_Imps.setVisibility(View.VISIBLE);
        }else{
            radio_Neft.setVisibility(View.GONE);
            radio_Imps.setVisibility(View.GONE);
        }

        btnProceed.setOnClickListener((View.OnClickListener) v -> {

            int selectedId = radioPayGroup.getCheckedRadioButtonId();
            radioPayButton = (RadioButton) findViewById(selectedId);
            if (radioPayGroup.getCheckedRadioButtonId() != -1) {
                radioButtonValue = radioPayButton.getText().toString();
                if (radioButtonValue.equalsIgnoreCase("NEFT")) {
                    radioButtonValue = "1";
                } else if (radioButtonValue.equalsIgnoreCase("IMPS")) {
                    radioButtonValue = "2";
                }
            }

            if (TextUtils.isEmpty(editAmount.getText().toString().trim())) {
                Toast.makeText(context, "Please enter correct amount!", Toast.LENGTH_SHORT).show();
                editAmount.requestFocus();
            } else if(Integer.parseInt(editAmount.getText().toString()) < 100){
                Toast.makeText(context, "Please enter amount equal to or more than \u20B9 100!", Toast.LENGTH_SHORT).show();
                editAmount.requestFocus();
            }else if (radioPayGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(context, "Please Select Pay Option", Toast.LENGTH_SHORT).show();
            } else {
                transactionAmount = editAmount.getText().toString().trim();
                Remark = editRemark.getText().toString().trim();
                PaymentConfirmDialog();
            }
        });
    }

    //================For Delete Bene===============
    public void PaymentConfirmDialog() {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.ps_dmr_sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK = (Button) d.findViewById(R.id.btn_resister_ok);
        final Button btnNO = (Button) d.findViewById(R.id.btn_resister_no);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final TextView title = (TextView) d.findViewById(R.id.title);
        final Button btnClosed = (Button) d.findViewById(R.id.close_push_button);

        title.setText("Payment Confirmation");
        tvMessage.setText("Are you sure, you want to pay \u20B9 " + transactionAmount + " to " + BeneName);

        btnNO.setOnClickListener(v -> d.dismiss());

        btnOK.setOnClickListener(v -> {
            d.dismiss();
            if("Y".equalsIgnoreCase(tpinSTatus)){
                transactionPinDialog();
            }else{
                impsNeftTransactionRequest();
            }

        });

        btnClosed.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            d.cancel();
        });
        d.show();
    }

    //==========For Imps Neft Transaction=================
    private void impsNeftTransactionRequest() {
        Util.hideView(dmr_txn_message);
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Money Transfer...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            MoneyTransferRequest request = new MoneyTransferRequest();
            request.setAgentID(agentID);
            request.setKey(txnKey);
            request.setSenderId(SenderId);
            request.setSenderName(SenderName);
            request.setBeneMobile(BeneMobile);
            request.setBeneAccount(BeneAccountNumber);
            request.setBeneName(BeneName);
            request.setBankName(BeneBankName);
            request.setTxnType(radioButtonValue);
            request.setIfsc(IFSCcode);
            request.setRecipientId(RecipientId);
            request.setTxnAmount(editAmount.getText().toString().trim());
            request.setRemark(Remark == null ? "" : Remark);
            request.setREQUEST_ID(String.valueOf((long) Math.floor(Math.random() * 90000000000000L) + 10000000000000L));
            request.setLatitude(Util.LoadPrefData(context,getString(R.string.latitude)));
            request.setLongitude(Util.LoadPrefData(context,getString(R.string.longitude)));
            request.setIp(Util.getIpAddress(context));
            request.setTransactionPin(Util.SHA1(tpin));

            RetrofitClient.getClient(getApplicationContext()).moneyTransfer(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+ AppMethods.InitiateTransaction,request)
                    .enqueue(new Callback<PSMoneyTransferResponse>() {
                        @Override
                        public void onResponse(Call<PSMoneyTransferResponse> call, retrofit2.Response<PSMoneyTransferResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                PSMoneyTransferResponse res = response.body();
                                if (res.getStatus().equalsIgnoreCase("0")) {

                                    TransactionItems transactionModel = new TransactionItems();
                                    transactionModel.setTran_id(res.getTid());
                                    transactionModel.setMobile_operator("");
                                    transactionModel.setMobile_number(res.getSenderId());
                                    transactionModel.setService("Remittance2");
                                    transactionModel.setAction_on_bal_amt("Debit");
                                    transactionModel.setTran_status(res.getResponseStatus());
                                    transactionModel.setTxnAmount(res.getTxnAmount());
                                    transactionModel.setNet_amout(res.getTxnAmount());
                                    transactionModel.setDeductedAmt(res.getTxnAmount());
                                    transactionModel.setDot(res.getTxnDate());
                                    transactionModel.setTot(res.getTxnTime());
                                    transactionModel.setAgent_balAmt_b_Ded("0");
                                    transactionModel.setAgent_F_balAmt("0");
                                    transactionModel.setBankRefId(res.getBankRRN());
                                    transactionModel.setOperatorId(res.getBankRRN());
                                    transactionModel.setCommission("0");
                                    transactionModel.setServiceCharge("0");
                                    transactionModel.setRemark(res.getMessage());
                                    transactionModel.setBene_Account(res.getBeneAccount());
                                    transactionModel.setBene_Bank_IFSC(res.getIfsc());
                                    transactionModel.setBene_Name(res.getBeneName());
                                    transactionModel.setBene_Bank_Name(res.getBankName());

                                    Intent intent = new Intent( getApplicationContext(), TransactionHistoryReceipt.class);
                                    intent.putExtra("position", 1);
                                    intent.putExtra("historyModel", Util.getGson().toJson(transactionModel));
                                    startActivity(intent);
                                    PSImpsNeftActivity.this.finish();

                                } else {
                                    Util.showView(dmr_txn_message);
                                    dmr_txn_message.setText(res.getMessage());
                                    L.toastS(getApplicationContext(), res.getMessage());
                                }
                            } else {
                                L.toastS(getApplicationContext(), "No Response");
                            }
                        }

                        @Override
                        public void onFailure(Call<PSMoneyTransferResponse> call, Throwable t) {
                            L.toastS(getApplicationContext(), "Error : " + t.getMessage());
                            pd.dismiss();
                        }
                    });
        }
    }

    private void transactionPinDialog() {
        final Dialog dialog_status = new Dialog(PSImpsNeftActivity.this);
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
                impsNeftTransactionRequest();
            }

        });

        close_confirm_tpin.setOnClickListener(view -> {
            dialog_status.cancel();
            hideKeyBoard(til_enter_tpin.getEditText());
        });

        dialog_status.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
