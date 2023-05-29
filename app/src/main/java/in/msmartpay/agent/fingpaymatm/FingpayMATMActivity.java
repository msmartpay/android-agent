package in.msmartpay.agent.fingpaymatm;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.aepssdkssz.network.model.ValidateUserRequest;
import com.aepssdkssz.network.model.ValidateUserResponse;
import com.aepssdkssz.util.DialogProgressFragment;
import com.aepssdkssz.util.Utility;
import com.fingpay.microatmsdk.MATMHistoryScreen;
import com.fingpay.microatmsdk.MicroAtmLoginScreen;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.matm.MicroInitiateTransactionData;
import in.msmartpay.agent.network.model.matm.MicroInitiateTransactionRequest;
import in.msmartpay.agent.network.model.matm.MicroInitiateTransactionResponse;
import in.msmartpay.agent.network.model.matm.MicroPostTransactionData;
import in.msmartpay.agent.network.model.matm.MicroPostTransactionRequest;
import in.msmartpay.agent.network.model.matm.MicroPostTransactionResponse;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FingpayMATMActivity extends BaseActivity {
    private Context context;

    private EditText mobileEt, amountEt, remarksEt;
    private RadioGroup radioGroup;
    private Button fingPayBtn, historyBtn;
    private TextView respTv;

    private static final int CODE = 1;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.READ_PHONE_STATE,
    };
    private SharedPreferences permissionStatus;
    private DialogProgressFragment pd;
    private int fpTxnType=0;
    String agentID="",txnKey="",superMerchantId="", serviceType="",mobile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingpay_matm);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Micro ATM");

        permissionStatus = getSharedPreferences("microatm_sample", 0);
        context = FingpayMATMActivity.this;

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        mobileEt = findViewById(R.id.et_mobile);
        amountEt = findViewById(R.id.et_amount);
        remarksEt = findViewById(R.id.et_remarks);

        radioGroup = findViewById(R.id.rg_type);

        fingPayBtn = findViewById(R.id.btn_fingpay);
        fingPayBtn.setOnClickListener(listener);

        historyBtn = findViewById(R.id.btn_history);
        historyBtn.setOnClickListener(listener);

        respTv = findViewById(R.id.tv_transaction);

        checkValidateUserRequest();

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String imei = Util.getDeviceId(getApplicationContext());
            //String imei = "1234";
            switch (view.getId()) {
                case R.id.btn_fingpay:

                    mobile = mobileEt.getText().toString().trim();
                    String amount = amountEt.getText().toString().trim();
                    String remarks = remarksEt.getText().toString().trim();

                    int id = radioGroup.getCheckedRadioButtonId();
                    switch (id) {
                        case R.id.rb_cw:
                            serviceType="CW";
                            fpTxnType=Keys.CASH_WITHDRAWAL;
                            break;
                        case R.id.rb_cd:
                            serviceType="CD";
                            fpTxnType=Keys.CASH_DEPOSIT;
                            break;
                        case R.id.rb_be:
                            serviceType="BE";
                            fpTxnType=Keys.BALANCE_ENQUIRY;
                            break;
                        case R.id.rb_ms:
                            serviceType="MN";
                            fpTxnType=Keys.MINI_STATEMENT;
                            break;
                        case R.id.rb_rp:
                            serviceType="PIN_RESET";
                            fpTxnType=Keys.PIN_RESET;
                            break;
                        case R.id.rb_cp:
                            serviceType="CHANGE_PIN";
                            fpTxnType=Keys.CHANGE_PIN;
                            break;
                        case R.id.rb_ca:
                            serviceType="CARD_ACTIVATION";
                            fpTxnType=Keys.CARD_ACTIVATION;
                            break;
                        case R.id.rb_purchase:
                            serviceType="Sale";
                            fpTxnType=Keys.PURCHASE;
                            break;
                    }
                    boolean proceed=false;
                    if(mobile==null || "".equalsIgnoreCase(mobile)){
                        mobileEt.requestFocus();
                        L.toastL(context,"Enter Valid Mobile Number!");
                        break;
                    }else {
                        if("CW".equalsIgnoreCase(serviceType) || "Sale".equalsIgnoreCase(serviceType)){
                            if(amount== null || "".equalsIgnoreCase(amount) || Integer.parseInt(amount)<1){
                                amountEt.requestFocus();
                                L.toastL(context,"Enter Valid Amount between (Rs. 100-10000)!");
                                break;
                            }else{
                                proceed=true;
                            }
                        }else{
                            amount="0";
                            proceed=true;
                        }
                    }
                    if(proceed)
                        initiateTransaction(mobile,amount,remarks,imei);
                    else
                        L.toastL(context,"Incomplete Parameters!");
                    break;


                case R.id.btn_history:
                    String mId = agentID;
                    String pswd = agentID;
//                    Utils.dissmissKeyboard(merchIdEt);
                    Intent intent = new Intent(FingpayMATMActivity.this, MATMHistoryScreen.class);
                    intent.putExtra(Keys.MERCHANT_USERID, mId);
                    // this MERCHANT_USERID be given by FingPay depending on the merchant, only that value need to sent from App to SDK

                    intent.putExtra(Keys.MERCHANT_PASSWORD, pswd);
                    // this MERCHANT_PASSWORD be given by FingPay depending on the merchant, only that value need to sent from App to SDK

                    intent.putExtra(Keys.SUPER_MERCHANTID, superMerchantId);
                    // this SUPER_MERCHANT_ID be given by FingPay to you, only that value need to sent from App to SDK

                    intent.putExtra(Keys.IMEI, imei);

                    startActivity(intent);

                    break;


                default:
                    break;
            }
        }
    };

    private void initiateMatmService(String amount,String mobile,String remarks,String imei,String clientRefId,String superMerchantId){

        if (isValidString(agentID)) {
            if (isValidString(agentID)) {

                Intent intent = new Intent(FingpayMATMActivity.this, MicroAtmLoginScreen.class);
                intent.putExtra(Keys.MERCHANT_USERID, agentID);
                intent.putExtra(Keys.MERCHANT_PASSWORD, agentID);
                intent.putExtra(Keys.AMOUNT, amount);
                intent.putExtra(Keys.REMARKS, remarks);
                intent.putExtra(Keys.MOBILE_NUMBER, mobile);
                intent.putExtra(Keys.AMOUNT_EDITABLE, false);
                intent.putExtra(Keys.TXN_ID, clientRefId);
                intent.putExtra(Keys.SUPER_MERCHANTID, superMerchantId);
                intent.putExtra(Keys.IMEI, imei);

                String lat = Util.LoadPrefData(context, Keys.LATITUDE);
                String lng = Util.LoadPrefData(context, Keys.LONGITUDE);
                intent.putExtra(Keys.LATITUDE, lat!=null?Double.parseDouble(lat):0.0);
                intent.putExtra(Keys.LONGITUDE,  lng!=null?Double.parseDouble(lng):0.0);

                intent.putExtra(Keys.TYPE, fpTxnType);

                intent.putExtra(Keys.MICROATM_MANUFACTURER, Keys.MoreFun);

                Log.d("Intent Data : ",intent.getExtras().toString());

                startActivityForResult(intent, CODE);
            } else {
                Toast.makeText(context, "Please enter the password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Please enter the merchant id", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidString(String str) {
        if (str != null) {
            str = str.trim();
            if (str.length() > 0)
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE) {
            //Toast.makeText(context, "res" + data.getExtras().toString(), Toast.LENGTH_SHORT).show();
            //Utils.logD(data.getExtras().toString());
            boolean status = data.getBooleanExtra(Keys.TRANS_STATUS, false);
            String message = data.getStringExtra(Keys.MESSAGE);
            double transAmount = data.getDoubleExtra(Keys.TRANS_AMOUNT, 0);
            double balAmount = data.getDoubleExtra(Keys.BALANCE_AMOUNT, 0);
            int type = data.getIntExtra(Keys.TYPE, Keys.CASH_WITHDRAWAL);
            String cardNum =data.getStringExtra(Keys.CARD_NUM);
            String rrn = data.getStringExtra(Keys.RRN);
            String txnType = data.getStringExtra(Keys.TRANS_TYPE);
            String bankName = data.getStringExtra(Keys.BANK_NAME);
            String cardType = data.getStringExtra(Keys.CARD_TYPE);
            String terminalId = data.getStringExtra(Keys.TERMINAL_ID);
            String fpId = data.getStringExtra(Keys.FP_TRANS_ID);
            String transId =data.getStringExtra(Keys.TXN_ID);
            String errorCode = data.getStringExtra(Keys.RESPONSE_CODE);
            long statusCode = data.getLongExtra(Keys.STATUS_CODE,-1);

           /* if (type == Keys.MINI_STATEMENT) {
                List<MiniStatementModel> l = data.getParcelableArrayListExtra(Keys.LIST);
//                if (Utils.isValidArrayList((ArrayList<?>) l)) {
//                    Utils.logD(l.toString());
//                }
            }*/

            if(status) {
                if (isValidString(message)) {

                    Intent intent = new Intent(FingpayMATMActivity.this, MatmRecieptActivity.class);
                    intent.putExtra(Keys.MESSAGE, message);
                    intent.putExtra(Keys.TRANS_AMOUNT, transAmount);
                    intent.putExtra(Keys.BALANCE_AMOUNT, balAmount);
                    intent.putExtra(Keys.CARD_NUM, cardNum);
                    intent.putExtra(Keys.CARD_TYPE, cardType);
                    intent.putExtra(Keys.TERMINAL_ID, terminalId);
                    intent.putExtra(Keys.FP_TRANS_ID, fpId);
                    intent.putExtra(Keys.TXN_ID, transId);
                    intent.putExtra(Keys.RESPONSE_CODE, errorCode);
                    intent.putExtra(Keys.RRN, rrn);
                    intent.putExtra(Keys.BANK_NAME, bankName);
                    intent.putExtra(Keys.MOBILE, mobile);
                    intent.putExtra(Keys.TYPE, serviceType);
                    startActivity(intent);
                    finish();

                }
                postTransactionDetails(status, message, errorCode, String.valueOf(statusCode), transAmount, balAmount, fpId, transId, bankName, rrn, terminalId, txnType, cardType, cardNum);
            }else{
                L.toastL(context,message);
            }
        }
        else if (requestCode == REQUEST_PERMISSION_SETTING) {
            //checkPermission();
            if (ActivityCompat.checkSelfPermission(FingpayMATMActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(context, "cancelled", Toast.LENGTH_SHORT).show();
            respTv.setText("");
        }

    }


    private List<String> getUngrantedPermissions() {
        List<String> permissions = new ArrayList<>();

        for (String s : permissionsRequired) {
            if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED)
                permissions.add(s);
        }

        return permissions;
    }

    private void checkPermissions() {
        List<String> permissions = getUngrantedPermissions();
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(FingpayMATMActivity.this,
                    permissions.toArray(new String[permissions.size()]),
                    PERMISSION_CALLBACK_CONSTANT);

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
/*
            if (allgranted) {

            }
            else {
                Toast.makeText(getBaseContext(), getString(R.string.unable_toget_permission), Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private void checkValidateUserRequest() {
        if (Utility.checkConnection(getApplicationContext())) {
            pd = DialogProgressFragment.newInstance("Loading. Please wait...", "Validating access...");
            DialogProgressFragment.showDialog(pd, getSupportFragmentManager());

            ValidateUserRequest request = new ValidateUserRequest();
            request.setToken(txnKey);
            request.setMerchant_id(agentID);

            RetrofitClient.getClient(getApplicationContext())
                    .fingpayValidateMatmUser(request)
                    .enqueue(new Callback<ValidateUserResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<ValidateUserResponse> call,
                                               @NotNull Response<ValidateUserResponse> response) {
                            pd.dismiss();
                            try {
                                ValidateUserResponse res = response.body();
                                if (res != null) {
                                    if (res.getStatus() == 0 && res.getData() != null) {
                                        superMerchantId=res.getData().getInitiator_id();
                                        checkPermissions();
                                    }else{
                                        validateConfirmationDialog(res.getStatus(),res.getMessage());
                                    }
                                } else {
                                    validateConfirmationDialog(1,"User Validation Failure");
                                }
                            } catch (Exception e) {

                                validateConfirmationDialog(1,e.getMessage());

                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ValidateUserResponse> call, @NotNull Throwable t) {
                            pd.dismiss();
                            validateConfirmationDialog(1,"Validation failure");
                            Utility.loge("validate User", "" + t.getMessage());
                        }
                    });
        }
    }

    private void postTransactionDetails(boolean status,String message,String errorCode,String statusCode,
                                        double transAmount,double balAmount,String fpId,String transId,
                                        String bankName,String rrn,
                                        String terminalId,String txnType,String cardType,String cardNum) {
        if (Utility.checkConnection(getApplicationContext())) {
            /*pd = DialogProgressFragment.newInstance("Loading. Please wait...", "Validating access...");
            DialogProgressFragment.showDialog(pd, getSupportFragmentManager());*/

            MicroPostTransactionRequest request = new MicroPostTransactionRequest();
            request.setKey(txnKey);
            request.setAgentID(agentID);

            MicroPostTransactionData data=new MicroPostTransactionData();
            data.setStatus(status);
            data.setMessage(message);
            data.setBankName(bankName);
            data.setBalAmount(balAmount);
            data.setTransAmount(transAmount);
            data.setClientRefId(transId);
            data.setFpTid(fpId);
            data.setBankRrn(rrn);
            data.setTerminalId(terminalId);
            data.setCardNum(cardNum);
            data.setCardType(cardType);
            data.setTransType(txnType);
            data.setErrorCode(errorCode);
            data.setStatusCode(statusCode);
            data.setMobile(mobile);
            data.setIpAddress(Util.getIpAddress(context));
            request.setData(data);

            RetrofitClient.getClient(getApplicationContext())
                    .postFingpayMATMTransaction(request)
                    .enqueue(new Callback<MicroPostTransactionResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<MicroPostTransactionResponse> call,
                                               @NotNull Response<MicroPostTransactionResponse> response) {
                            //pd.dismiss();
                            try {
                                MicroPostTransactionResponse res=response.body();
                            } catch (Exception e) {
                                L.toastL(context,e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<MicroPostTransactionResponse> call, @NotNull Throwable t) {
                            //pd.dismiss();
                            L.toastL(context,t.getMessage());
                        }
                    });
        }
    }

    private void initiateTransaction(String mobile,String amount,String remarks,String iemi) {
        if (Utility.checkConnection(getApplicationContext())) {
            pd = DialogProgressFragment.newInstance("Loading. Please wait...", "Validating access...");
            DialogProgressFragment.showDialog(pd, getSupportFragmentManager());

            MicroInitiateTransactionRequest request = new MicroInitiateTransactionRequest();
            request.setKey(txnKey);
            request.setAgentID(agentID);

            MicroInitiateTransactionData data = new MicroInitiateTransactionData();
            data.setServiceType(serviceType);
            data.setAmount(amount);
            data.setMobile(mobile);
            data.setIp(Util.getIpAddress(context));

            request.setData(data);

            RetrofitClient.getClient(getApplicationContext())
                    .initiateFingpayMATMTransaction(request)
                    .enqueue(new Callback<MicroInitiateTransactionResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<MicroInitiateTransactionResponse> call,
                                               @NotNull Response<MicroInitiateTransactionResponse> response) {
                            pd.dismiss();
                            try {
                                MicroInitiateTransactionResponse res = response.body();
                                if("0".equalsIgnoreCase(res.getStatus())){

                                    MicroInitiateTransactionData data=res.getData();
                                    initiateMatmService(amount,mobile,remarks,iemi,data.getClientRefId(), superMerchantId);

                                }else{
                                    validateConfirmationDialog(1,res.getMessage());
                                }

                            } catch (Exception e) {
                                L.toastL(context,e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<MicroInitiateTransactionResponse> call, @NotNull Throwable t) {
                            pd.dismiss();

                            L.toastL(context,t.getMessage());
                        }
                    });
        }
    }

    private void validateConfirmationDialog(int statusCode,String message) {
        final Dialog d = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.common_dialog_yes_no);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_confirmation_message =  d.findViewById(R.id.tv_confirmation_message);
        Button btn_confirm_no = d.findViewById(R.id.btn_confirm_no);
        Button btn_confirm_yes = d.findViewById(R.id.btn_confirm_yes);
        Button close_confirm_button = d.findViewById(R.id.close_confirm_button);

        tv_confirmation_message.setText(message);
        btn_confirm_no.setText("Close");

        if(statusCode==5){
            Util.showView(btn_confirm_yes);
            btn_confirm_yes.setOnClickListener(view -> {
                d.dismiss();
                L.toastL(getApplicationContext(),"Coming Soon");
            });
        }else{
            Util.hideView(btn_confirm_yes);
        }
        btn_confirm_no.setOnClickListener(v -> {
            d.dismiss();
            Intent intent = new Intent(FingpayMATMActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        close_confirm_button.setOnClickListener(v -> {
            d.dismiss();
            Intent intent = new Intent(FingpayMATMActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        d.show();
    }

    public boolean onSupportNavigateUp() {

        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}




