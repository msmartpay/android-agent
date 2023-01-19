package in.msmartpay.agent.dmr;

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

import java.security.Key;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.dmr.MoneyTransferRequest;
import in.msmartpay.agent.network.model.dmr.MoneyTransferResponse;
import in.msmartpay.agent.network.model.dmr.SenderDetailsResponse;
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

public class ImpsNeftActivity extends BaseActivity {

    private Button btnProceed;
    private TextInputLayout til_dmr_txn_amount, til_dmr_remark;
    private ProgressDialogFragment pd;
    private RadioGroup radioPayGroup;
    private RadioButton radioPayButton;
    private String radioButtonValue;
    private TextView tv_txn_bene_name, tv_txn_bank_name, tv_txn_account_no,tv_txn_bank_ifsc;
    private SharedPreferences sharedPreferences;
    private String agentID, txnKey;
    private String senderId, senderName,beneName,beneAccount,beneIFSC,beneBank,recipientId,IMPS,NEFT,Channel,
            RecipientIdType;
    private Context context;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_imps_neft_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Confirm Payment");

        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener((v, event) -> {
            // TODO Auto-generated method stub
            v.requestFocusFromTouch();
            return false;
        });

        context = ImpsNeftActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        SenderDetailsResponse sender = Util.getGson().fromJson(Util.LoadPrefData(getApplicationContext(), Keys.SENDER),SenderDetailsResponse.class);

        senderId = sender.getSenderDetails().getSenderId();
        senderName = sender.getSenderDetails().getName();

        beneName = getIntent().getStringExtra("BeneName");
        beneAccount = getIntent().getStringExtra("BeneAccountNumber");
        beneBank = getIntent().getStringExtra("BeneBankName");
        beneIFSC = getIntent().getStringExtra("IFSCcode");
        recipientId = getIntent().getStringExtra("RecipientId");
        IMPS = getIntent().getStringExtra("IMPS");
        NEFT = getIntent().getStringExtra("NEFT");
        Channel = getIntent().getStringExtra("Channel");
        RecipientIdType = getIntent().getStringExtra("RecipientIdType");


        tv_txn_bene_name =  findViewById(R.id.tv_txn_bene_name);
        tv_txn_account_no =  findViewById(R.id.tv_txn_account_no);
        tv_txn_bank_name =  findViewById(R.id.tv_txn_bank_name);
        tv_txn_bank_ifsc =  findViewById(R.id.tv_txn_bank_ifsc);
        til_dmr_txn_amount = findViewById(R.id.til_dmr_txn_amount);
        til_dmr_remark  = findViewById(R.id.til_dmr_remark);

        btnProceed = (Button) findViewById(R.id.txtsubmit);
        radioPayGroup = (RadioGroup) findViewById(R.id.radioGroup);
        til_dmr_txn_amount.getEditText().requestFocus();

        tv_txn_bene_name.setText(beneName);
        tv_txn_account_no.setText("A/C: "+beneAccount);
        tv_txn_bank_name.setText(beneBank);
        tv_txn_bank_ifsc.setText(beneIFSC);

        RadioButton radio_Neft = (RadioButton) findViewById(R.id.radio_neft);
        RadioButton radio_Imps = (RadioButton) findViewById(R.id.radio_imps);
        TextView text_Message = (TextView) findViewById(R.id.tv_txn_transfer_message);
        if ("1".equalsIgnoreCase(Channel)) {

            radio_Neft.setChecked(true);
            radio_Neft.setVisibility(View.VISIBLE);
            radio_Imps.setVisibility(View.GONE);
            text_Message.setText("IMPS is not available for selected bank");
            text_Message.setVisibility(View.VISIBLE);
            Util.showView(text_Message);
        } else if ("2".equalsIgnoreCase(Channel)) {
            radio_Imps.setChecked(true);
            radio_Neft.setVisibility(View.GONE);
            radio_Imps.setVisibility(View.VISIBLE);
            text_Message.setVisibility(View.VISIBLE);
            text_Message.setText("NEFT is not available for selected bank");
            Util.showView(text_Message);
        } else if ("0".equalsIgnoreCase(Channel)) {

            radio_Neft.setVisibility(View.VISIBLE);
            radio_Imps.setVisibility(View.VISIBLE);
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

            if (TextUtils.isEmpty(til_dmr_txn_amount.getEditText().getText().toString().trim())) {
                Toast.makeText(context, "Please enter correct amount!", Toast.LENGTH_SHORT).show();
                til_dmr_txn_amount.getEditText().requestFocus();
            } else if(Integer.parseInt(til_dmr_txn_amount.getEditText().getText().toString()) < 100){
                Toast.makeText(context, "Please enter amount equal to or more than \u20B9 100!", Toast.LENGTH_SHORT).show();
                til_dmr_txn_amount.getEditText().requestFocus();
            }else if (radioPayGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(context, "Please Select Pay Option", Toast.LENGTH_SHORT).show();
            } else {

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
        d.setContentView(R.layout.dmr_confirm_transfer_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

         Button btnOK = (Button) d.findViewById(R.id.btn_dmr_cnf_confirm);
         Button btnNO = (Button) d.findViewById(R.id.btn_dmr_cnf_cancel);
         Button close_dmr_cnf_button = (Button) d.findViewById(R.id.close_dmr_confirm);
         TextView tv_txn_cnf_bene_name =  d.findViewById(R.id.tv_txn_cnf_bene_name);
         TextView tv_txn_cnf_bene_bank =  d.findViewById(R.id.tv_txn_cnf_bene_bank);
         TextView tv_txn_cnf_bene_account =  d.findViewById(R.id.tv_txn_cnf_bene_account);
         TextView tv_txn_cnf_bank_ifsc =  d.findViewById(R.id.tv_txn_cnf_bank_ifsc);
         TextView tv_txn_cnf_amount =  d.findViewById(R.id.tv_txn_cnf_amount);
         TextView tv_txn_cnf_transfer_type =  d.findViewById(R.id.tv_txn_cnf_transfer_type);

        tv_txn_cnf_bene_name.setText("Name: "+beneName);
        tv_txn_cnf_bene_bank.setText("Bank: "+beneBank);
        tv_txn_cnf_bene_account.setText("A/C: "+beneAccount);
        tv_txn_cnf_bank_ifsc.setText("IFSC: "+beneIFSC);
        tv_txn_cnf_amount.setText("Amount: \u20B9 "+til_dmr_txn_amount.getEditText().getText().toString().trim());
        tv_txn_cnf_transfer_type.setText("Transfer Type: "+radioPayButton.getText().toString());

        btnNO.setOnClickListener(v -> d.dismiss());

        btnOK.setOnClickListener(v -> {
            ImpsNeftTransactionRequest();
            d.dismiss();
        });

        close_dmr_cnf_button.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            d.cancel();
        });
        d.show();
    }

    //==========For Imps Neft Transaction=================
    private void ImpsNeftTransactionRequest() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Money Transfer...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            MoneyTransferRequest request = new MoneyTransferRequest();
            request.setAgentID(agentID);
            request.setKey(txnKey);
            request.setSenderId(senderId);
            request.setSenderName(senderName);
            request.setBeneMobile(senderId);
            request.setBeneAccount(beneAccount);
            request.setBeneName(beneName);
            request.setBankName(beneBank);
            request.setTxnType(radioButtonValue);
            request.setIfsc(beneIFSC);
            request.setRecipientId(recipientId);
            request.setTxnAmount(til_dmr_txn_amount.getEditText().getText().toString().trim());
            request.setRemark(til_dmr_remark.getEditText().getText().toString() == null ? "" : til_dmr_remark.getEditText().getText().toString().trim());
            request.setREQUEST_ID(String.valueOf((long) Math.floor(Math.random() * 90000000000000L) + 10000000000000L));
            request.setLatitude(Util.LoadPrefData(context, Keys.LATITUDE));
            request.setLongitude(Util.LoadPrefData(context,Keys.LONGITUDE));
            request.setIp(Util.getIpAddress(context));
            RetrofitClient.getClient(getApplicationContext()).moneyTransfer(request)
                    .enqueue(new Callback<MoneyTransferResponse>() {
                        @Override
                        public void onResponse(Call<MoneyTransferResponse> call, retrofit2.Response<MoneyTransferResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                MoneyTransferResponse res = response.body();
                                if (res.getStatus().equalsIgnoreCase("0")) {
                                    Intent intent = new Intent(ImpsNeftActivity.this, ImpsNeftTxnRecieptActivity.class);
                                    intent.putExtra("SenderId", res.getSenderId());
                                    intent.putExtra("SenderName", res.getSenderName());
                                    intent.putExtra("BeneMobile",res.getBeneMobile());
                                    intent.putExtra("Ifsc", res.getIfsc());
                                    intent.putExtra("accountNumber",res.getBeneAccount());
                                    intent.putExtra("beneName", res.getBeneName());
                                    intent.putExtra("bankName", res.getBankName());
                                    intent.putExtra("amount", res.getTxnAmount());
                                    intent.putExtra("BankRRN", res.getBankRRN());
                                    intent.putExtra("TxnDate",res.getTxnDate());
                                    intent.putExtra("TxnTime", res.getTxnTime());
                                    intent.putExtra("message",res.getMessage());
                                    intent.putExtra("txnStatus",res.getTxnStatus());
                                    intent.putExtra("tid",res.getTid());
                                    startActivity(intent);
                                    ImpsNeftActivity.this.finish();
                                } else {
                                    L.toastS(getApplicationContext(), res.getMessage());
                                }
                            } else {
                                L.toastS(getApplicationContext(), "No Response");
                            }
                        }

                        @Override
                        public void onFailure(Call<MoneyTransferResponse> call, Throwable t) {
                            L.toastS(getApplicationContext(), "Error : " + t.getMessage());
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
