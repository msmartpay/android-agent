package in.msmartpay.agent.rechargeBillPay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.internal.LinkedTreeMap;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.databinding.ActivityPaytmBinding;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainRequest2;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.PaytmWalletRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaytmActivity extends BaseActivity {

    private ActivityPaytmBinding binding;
    private EditText et_name;
    private EditText et_mobile;
    private EditText et_amount;
    private EditText et_otp;
    private Button btn_otp;
    private Button btn_verify_otp;
    private Button btn_payment;
    private TextView tv_otpMsg;
    private ProgressDialogFragment pd;
    private TextInputLayout il_otp;
    private String txntoken;
    private String reqMappingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPaytmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("PayTM Wallet Payment");
        et_mobile = binding.etPaytmMobile;
        et_name = binding.etPaytmName;
        et_otp = binding.etPaytmOtp;
        et_amount = binding.etPaytmAmount;
        btn_otp = binding.btnPaytmGetotp;
        btn_payment = binding.btnPaymentPayment;
        btn_verify_otp = binding.btnPaytmValidateOtp;
        il_otp = binding.tilPaytmOtp;
        tv_otpMsg = binding.tvOtpMsg;
        Util.hideView(btn_verify_otp);
        Util.hideView(btn_payment);
        Util.hideView(tv_otpMsg);


        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_mobile.getText().toString().trim())) {
                    L.toastS(getApplicationContext(), "Please enter mobile number");
                } else if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    L.toastS(getApplicationContext(), "Please enter beneficiary name");
                } else if (TextUtils.isEmpty(et_amount.getText().toString().trim())) {
                    L.toastS(getApplicationContext(), "Please enter amount");
                } else {
                    requestOtp();
                }
            }
        });

        btn_payment.setOnClickListener(view -> paytmPayRequest());

        btn_verify_otp.setOnClickListener(view -> {
                    String otp = et_mobile.getText().toString().trim();
                    if (TextUtils.isEmpty(otp)) {
                        L.toastS(getApplicationContext(), "Please enter valid otp");
                    } else {
                        verifyOtp();
                    }
                }
        );
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

    private void requestOtp() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "PayTM Wallet Payment...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        MainRequest2 request2 = new MainRequest2();
        PaytmWalletRequest data = new PaytmWalletRequest();
        data.setAmount(et_amount.getText().toString().trim());
        data.setMobile(et_mobile.getText().toString().trim());
        request2.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request2.setKey(Util.LoadPrefData(this, Keys.TXN_KEY));
        request2.setData(data);
        RetrofitClient.getClient(this)
                .getPaytmOtp(request2).enqueue(new Callback<MainResponse2>() {
            @Override
            public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    MainResponse2 res = response.body();
                    if (res.getStatus() != null && res.getStatus().equals("0")) {
                        Util.showView(tv_otpMsg);
                        tv_otpMsg.setText(res.getMessage());
                        showOtpText();
                    } else if (res.getStatus() != null && res.getStatus().equals("1")) {
                        L.toastS(getApplicationContext(), res.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse2> call, Throwable t) {
                pd.dismiss();
                L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());

            }
        });
    }

    private void verifyOtp() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "PayTM Wallet Payment...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        MainRequest2 request2 = new MainRequest2();
        PaytmWalletRequest data = new PaytmWalletRequest();
        data.setAmount(et_amount.getText().toString().trim());
        data.setMobile(et_mobile.getText().toString().trim());
        data.setOtp(et_otp.getText().toString().trim());
        request2.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request2.setKey(Util.LoadPrefData(this, Keys.TXN_KEY));
        request2.setData(data);
        RetrofitClient.getClient(this)
                .verifyPaytmOtp(request2).enqueue(new Callback<MainResponse2>() {
            @Override
            public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    MainResponse2 res = response.body();
                    if (res.getStatus() != null && res.getStatus().equals("0")) {
                        LinkedTreeMap map = (LinkedTreeMap) res.getData();
                        // PaytmWalletRequest data = (PaytmWalletRequest)res.getData();
                        if (map != null) {
                            txntoken = map.get("txntoken") + "";
                            reqMappingId = map.get("reqMappingId") + "";
                            Util.hideView(btn_otp);
                            Util.hideView(btn_verify_otp);
                            Util.showView(btn_payment);
                            Util.showView(tv_otpMsg);
                            Util.hideView(il_otp);
                            tv_otpMsg.setText(res.getMessage());
                        }

                    } else if (res.getStatus() != null && res.getStatus().equals("1")) {
                        L.toastS(getApplicationContext(), res.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<MainResponse2> call, Throwable t) {
                pd.dismiss();
                L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
            }
        });
    }

    private void paytmPayRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "PayTM Wallet Payment...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        MainRequest2 request2 = new MainRequest2();
        PaytmWalletRequest data = new PaytmWalletRequest();
        data.setAmount(et_amount.getText().toString().trim());
        data.setMobile(et_mobile.getText().toString().trim());
        data.setTxntoken(txntoken);
        data.setReqMappingId(reqMappingId);
        data.setBenename(et_name.getText().toString().trim());
        request2.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request2.setKey(Util.LoadPrefData(this, Keys.TXN_KEY));
        request2.setData(data);
        request2.setIp(Util.getIpAddress(this));
        RetrofitClient.getClient(this)
                .getPaytmPay(request2).enqueue(new Callback<MainResponse2>() {
            @Override
            public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                pd.dismiss();
                /*
                {"data":{"responsecode":1,"txnid":12933364,"mobile":"9716025028","name":"MANOJ KUMAR","amount":"100.00","comm":"0.19","tds":"0.01","type":"PAYTM","dateadded":"2022-03-13 20:25:43","refid":"ZANJ20222613082639","balance":13338.47,"txn_status":"Success","status":true,"message":"Transaction Successful"},"referenceid":"ZANJ20222613082639","Status":"0","message":"Transaction Successful"}
                * */
                if (response.isSuccessful() && response.body() != null) {
                    MainResponse2 res = response.body();
                    paytmPaymentResponseDialog(res.getMessage(), res.getStatus());
                }else {
                    paytmPaymentResponseDialog(getString(R.string.technical_error),"1");
                }
            }

            @Override
            public void onFailure(Call<MainResponse2> call, Throwable t) {
                pd.dismiss();
                paytmPaymentResponseDialog(getString(R.string.technical_error),"1");
            }
        });
    }

    private void showOtpText() {
        Util.showView(il_otp);
        et_name.setEnabled(false);
        et_mobile.setEnabled(false);
        et_amount.setEnabled(false);
        et_name.setEnabled(false);
        Util.showView(btn_verify_otp);
    }

    private void setPayButtonEnable() {
        btn_payment.setClickable(true);
    }

    private void setPayButtonDisable() {
        btn_payment.setClickable(false);
    }

    private void setVerifyButtonDisable() {
        btn_verify_otp.setClickable(false);
    }

    private void setVerifyButtonEnable() {
        btn_verify_otp.setClickable(true);
    }

    private void setOtpButtonDisable() {
        btn_otp.setClickable(false);
    }

    private void setOtpButtonEnable() {
        btn_otp.setClickable(true);
    }

    private void paytmPaymentResponseDialog(String msg, String status) {
        Dialog dialog_status = new Dialog(this);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.sk_maindialog);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);


        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        text.setText((String) msg);
        if ("0".equals(status))
            statusImage.setImageResource(R.drawable.trnsuccess);
        else
            statusImage.setImageResource(R.drawable.failed);
        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v12 -> {
            dialog_status.dismiss();
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        dialog_status.show();
    }

}