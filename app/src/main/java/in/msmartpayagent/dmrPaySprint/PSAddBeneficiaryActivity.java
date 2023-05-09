package in.msmartpayagent.dmrPaySprint;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import in.msmartpayagent.R;
import in.msmartpayagent.dmrPaySprint.dashboard.PSMoneyTransferActivity;
import in.msmartpayagent.network.AppMethods;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.dmr.AccountVerifyRequest;
import in.msmartpayagent.network.model.dmr.AccountVerifyResponse;
import in.msmartpayagent.network.model.dmr.AddBeneficiaryRequest;
import in.msmartpayagent.network.model.dmr.AddBeneficiaryResponse;
import in.msmartpayagent.network.model.dmr.BankDetailsDmrResponse;
import in.msmartpayagent.network.model.dmr.BankListDmrResponse;
import in.msmartpayagent.network.model.dmr.BankListModel;
import in.msmartpayagent.network.model.dmr.BankRequest;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.network.model.dmr.SenderFindRequest;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class PSAddBeneficiaryActivity extends BaseActivity {

    /*private Spinner spinnerBankList;*/
    private EditText et_searchbank;
    private TextInputLayout til_ifsc;
    private TextView tv_bank_message;
    private EditText editIFSCcode, editAccountNo, editConfirmAccountNo, edit_sender_mobile, edit_beneficiary_mobile, edit_beneficiary_name;
    private Button btnAddBeneficiary;
    private ProgressDialogFragment pd;
    private JSONObject jsonObject;
    private ArrayList<BankListModel> bankList = new ArrayList<>();
    private BankListModel bankListModel;
    private String agentID, txnKey, mobileNumber;
    private Context context;
    private CheckBox addwithverify;
    private String Bankname = "", Bankcode = "";
    private String checked;
    int position = 0, varify=0;
    private boolean ifscrequired = false;
    private String isverificationavailable, available_channels, ifsc_status;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_dmr_add_beneficiary_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Beneficiary");

        context = PSAddBeneficiaryActivity.this;
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener((v, event) -> {
            v.requestFocusFromTouch();
            return false;
        });

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        mobileNumber = Util.LoadPrefData(getApplicationContext(), Keys.SENDER_MOBILE);

        til_ifsc = findViewById(R.id.til_ifsc);
        /* spinnerBankList = (Spinner) findViewById(R.id.spinner_transfer_type);*/
        et_searchbank = findViewById(R.id.et_searchbank);
        edit_sender_mobile = (EditText) findViewById(R.id.edit_sender_mobile);
        edit_sender_mobile.setVisibility(View.GONE);
        editIFSCcode = (EditText) findViewById(R.id.edit_ifsc_code);
        editAccountNo = (EditText) findViewById(R.id.edit_account_no);
        editConfirmAccountNo = (EditText) findViewById(R.id.edit_confirm_account_no);
        edit_beneficiary_mobile = (EditText) findViewById(R.id.edit_beneficiary_mobile);
        edit_beneficiary_name = (EditText) findViewById(R.id.edit_beneficiary_name);
        btnAddBeneficiary = (Button) findViewById(R.id.btn_add_beneficiary);
        addwithverify = (CheckBox) findViewById(R.id.addwithverify);
        tv_bank_message = findViewById(R.id.tv_bank_message);

        edit_sender_mobile.setText(mobileNumber);

        et_searchbank.setOnClickListener(v -> {

            if (bankList != null) {
                if (bankList.size() != 0) {
                    Intent intent = new Intent(context, PSBankSearchActivity.class);
                    intent.putExtra("bankList", Util.getGson().toJson(bankList));
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(context, "Bank List Not Available !!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Bank List Not Available !!!", Toast.LENGTH_SHORT).show();
            }
        });


        /*BankList Request*/

        getBankListRequest();

        addwithverify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // update your model (or other business logic) based on isChecked

            if (isChecked) {
                if (ifscrequired && TextUtils.isEmpty(editIFSCcode.getText().toString().trim())) {
                    addwithverify.setChecked(false);
                    editIFSCcode.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter IFSC Code!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(editAccountNo.getText().toString().trim())) {
                    addwithverify.setChecked(false);
                    editAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Account Number!", Toast.LENGTH_SHORT).show();
                } else if (!editConfirmAccountNo.getText().toString().equals(editAccountNo.getText().toString())) {
                    addwithverify.setChecked(false);
                    editConfirmAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Account No.!", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmationDialog(3, "Account verification fee will be applied on auto fetch beneficiary name");

                }

            }

        });

        btnAddBeneficiary.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(getCurrentFocus()!=null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (TextUtils.isEmpty(editAccountNo.getText().toString().trim())) {
                editAccountNo.requestFocus();
                Toast.makeText(getApplicationContext(), "Please Enter Account Number!", Toast.LENGTH_SHORT).show();
            } else if (!editConfirmAccountNo.getText().toString().equals(editAccountNo.getText().toString())) {
                editConfirmAccountNo.requestFocus();
                Toast.makeText(getApplicationContext(), "Please Enter Correct Account No.!", Toast.LENGTH_SHORT).show();
            } /*else if (TextUtils.isEmpty(edit_beneficiary_mobile.getText().toString())) {
                edit_beneficiary_mobile.requestFocus();
                Toast.makeText(getApplicationContext(), "Please Enter 10 digit Mobile No.!", Toast.LENGTH_SHORT).show();
            } */ else if (TextUtils.isEmpty(edit_beneficiary_name.getText().toString())) {
                edit_beneficiary_name.requestFocus();
                Toast.makeText(getApplicationContext(), "Please Enter Bene Name!", Toast.LENGTH_SHORT).show();
            } else {
                showConfirmationDialog(1, "Are you sure, you want to add Beneficiary!");
            }
        });
    }

    //getBankListRequest
    private void getBankListRequest() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Bank List...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            BankRequest request = new BankRequest();
            request.setAgentID(agentID);
            request.setKey(txnKey);
            request.setSenderId(mobileNumber);



            RetrofitClient.getClient(getApplicationContext())
                    .getBankListDmr(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+AppMethods.GetBankList,request).enqueue(new Callback<BankListDmrResponse>() {
                @Override
                public void onResponse(Call<BankListDmrResponse> call, retrofit2.Response<BankListDmrResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        BankListDmrResponse res = response.body();
                        if (res.getStatus().equalsIgnoreCase("0")) {
                            if (res.getBankList() != null && res.getBankList().size() > 0)
                                bankList = (ArrayList<BankListModel>) res.getBankList();
                        }

                    } else {

                        L.toastS(getApplicationContext(), "No Response");
                    }
                }

                @Override
                public void onFailure(Call<BankListDmrResponse> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(getApplicationContext(), "Error : " + t.getMessage());
                }
            });
        }
    }

    //Json request for add Beneficiary Confirm
    private void verifyAddBeneficiaryRequest() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Adding Beneficiary...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            AddBeneficiaryRequest request = new AddBeneficiaryRequest();
            request.setAgentID(agentID);
            request.setKey(txnKey);
            request.setSenderId(mobileNumber);
            request.setIFSC(editIFSCcode.getText().toString());
            request.setBankAccount(editAccountNo.getText().toString().trim());
            request.setBankCode(Bankcode);
            request.setBankName(Bankname);
            request.setBeneName(edit_beneficiary_name.getText().toString().trim());
            request.setBeneMobile(mobileNumber);
            request.setVarify(varify+"");

            RetrofitClient.getClient(getApplicationContext()).addBeneficiary(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+AppMethods.AddBeneAfterVerify,request)
                    .enqueue(new Callback<AddBeneficiaryResponse>() {
                        @Override
                        public void onResponse(Call<AddBeneficiaryResponse> call, retrofit2.Response<AddBeneficiaryResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                AddBeneficiaryResponse res = response.body();
                                if (res.getStatus().equalsIgnoreCase("0")) {

                                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                    showConfirmationDialog(2, res.getMessage());
                                } else {
                                    L.toastS(getApplicationContext(), res.getMessage());
                                }
                            } else {
                                L.toastS(getApplicationContext(), "No Response");
                            }
                        }

                        @Override
                        public void onFailure(Call<AddBeneficiaryResponse> call, Throwable t) {
                            L.toastS(getApplicationContext(), "Error : " + t.getMessage());
                            pd.dismiss();
                        }
                    });
        }
    }

    private void findSenderRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Latest Details of Sender...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        SenderFindRequest request = new SenderFindRequest();
        request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
        request.setSenderId(mobileNumber);
        RetrofitClient.getClient(context).findSenderDetails(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+ AppMethods.FindSender,request).enqueue(new Callback<SenderDetailsResponse>() {
            @Override
            public void onResponse(Call<SenderDetailsResponse> call, retrofit2.Response<SenderDetailsResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    SenderDetailsResponse res = response.body();
                    if (res.getStatus().equals("0")) {
                        Util.SavePrefData(context, Keys.SENDER_MOBILE, mobileNumber);
                        Util.SavePrefData(context, Keys.SENDER, Util.getGson().toJson(res));
                        Intent intent = new Intent(context, PSMoneyTransferActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity(intent);
                        finish();
                    }
                } else {
                    L.toastS(getApplicationContext(), "No Response");
                }
            }

            @Override
            public void onFailure(Call<SenderDetailsResponse> call, Throwable t) {
                pd.dismiss();
                L.toastS(getApplicationContext(), "Error : " + t.getMessage());
            }
        });
    }

    //Confirmation Dialog
    public void showConfirmationDialog(final int i, String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr_confirmation_dialog);

        final Button close_push_button = (Button) d.findViewById(R.id.close_push_button);
        final Button btnSubmit = (Button) d.findViewById(R.id.btn_push_submit);
        final Button btn_no = (Button) d.findViewById(R.id.btn_resend_otp);
        final TextView tvConfirmation = (TextView) d.findViewById(R.id.tv_confirmation_dialog);

        if (i == 1) {
            tvConfirmation.setText(msg);
        }
        if (i == 2 || i == 3) {
            tvConfirmation.setText(msg);
        }

        btnSubmit.setOnClickListener(v -> {
            if (i == 1) {
                verifyAddBeneficiaryRequest();
                d.dismiss();
            }

            if (i == 2) {
                findSenderRequest();
                d.dismiss();
            }
            if (i == 3) {
                VerifyAccountRequest(); //ye call hona hai ok pe
                d.dismiss();
            }
        });

        btn_no.setOnClickListener(view ->{
                if (i == 3) {
                    addwithverify.setChecked(false);
                }
                d.dismiss();
            }
        );

        close_push_button.setOnClickListener(view ->{
            if (i == 3) {
                addwithverify.setChecked(false);
            }
            d.dismiss();
        } );
        d.show();
    }

    private void VerifyAccountRequest() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {

            tv_bank_message.setText("");

            AccountVerifyRequest verifyRequest = new AccountVerifyRequest();
            verifyRequest.setAgentID(agentID);
            verifyRequest.setKey(txnKey);
            verifyRequest.setSenderId(mobileNumber);
            verifyRequest.setIFSC(editIFSCcode.getText().toString());
            verifyRequest.setBankAccount(editAccountNo.getText().toString().trim());
            verifyRequest.setBankCode(Bankcode);
            verifyRequest.setBankName(Bankname);
            verifyRequest.setBeneName(edit_beneficiary_name.getText().toString().trim());
            verifyRequest.setREQUEST_ID(String.valueOf((long) Math.floor(Math.random() * 90000000000000L) + 10000000000000L));

            RetrofitClient.getClient(getApplicationContext()).verifyAccount(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+AppMethods.AccountVerifyByBankAccountIFSC,verifyRequest).enqueue(new Callback<AccountVerifyResponse>() {
                @Override
                public void onResponse(Call<AccountVerifyResponse> call, retrofit2.Response<AccountVerifyResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        AccountVerifyResponse res = response.body();
                        if (res.getStatus().equals("0")) {
                            varify=1;
                            edit_beneficiary_name.setText(res.getBeneName());
                        } else {
                            varify=0;
                            addwithverify.setChecked(false);
                            tv_bank_message.setText(res.getMessage());
                            L.toastS(getApplicationContext(), res.getMessage());
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

    private void getBankDetails() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Bank Details...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());



            BankRequest request = new BankRequest();
            request.setAgentID(agentID);
            request.setKey(txnKey);
            request.setSenderId(mobileNumber);
            request.setBankCode(Bankcode);
            RetrofitClient.getClient(getApplicationContext())
                    .getBankDetailsDmr(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+AppMethods.GetBankList,request).enqueue(new Callback<BankDetailsDmrResponse>() {
                @Override
                public void onResponse(Call<BankDetailsDmrResponse> call, retrofit2.Response<BankDetailsDmrResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        BankDetailsDmrResponse res = response.body();
                        if (res.getStatus().equalsIgnoreCase("0")) {

                            isverificationavailable = res.getIsverificationavailable();
                            available_channels = res.getAvailable_channels();
                            ifsc_status = res.getIfsc_status();
                            if ("1".equalsIgnoreCase(isverificationavailable)) {

                                if ("4".equalsIgnoreCase(ifsc_status)) {
                                    //  respMsg="IFSCRequired";
                                    ifscrequired = true;

                                    tv_bank_message.setText("IFSC code required. Enter IFSC Code");
                                    til_ifsc.setHint("IFSC Required");
                                    editIFSCcode.setEnabled(true);

                                } else {

                                    tv_bank_message.setText("IFSC code not required.");
                                    til_ifsc.setHint("IFSC Code (optional)");
                                    editIFSCcode.setEnabled(false);
                                }
                            } else {

                                tv_bank_message.setText("Bank is not available for account verification.");

                            }
                        } else {
                            ifscrequired = true;

                            tv_bank_message.setText("IFSC code required. Enter IFSC Code");
                            til_ifsc.setHint("IFSC Required");
                            editIFSCcode.setEnabled(true);
                        }
                        Util.showView(tv_bank_message);
                    }
                }

                @Override
                public void onFailure(Call<BankDetailsDmrResponse> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(getApplicationContext(), "Error : " + t.getMessage());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            bankListModel = Util.getGson().fromJson(data.getStringExtra("bankModel"),BankListModel.class);
            if (bankListModel!=null) {
                Bankcode = bankListModel.getBankCode();
                Bankname = bankListModel.getBankName();
                et_searchbank.setText(bankListModel.getBankName());
                editIFSCcode.setText(bankListModel.getIfscCode());
                getBankDetails();
            }else {
                L.toastS(getApplicationContext(), "Please select your bank...");
            }
        } catch (Exception e) {
            L.toastS(getApplicationContext(), "Please select your bank...");

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}