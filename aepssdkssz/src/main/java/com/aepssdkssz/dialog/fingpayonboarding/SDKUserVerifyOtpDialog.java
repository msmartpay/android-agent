package com.aepssdkssz.dialog.fingpayonboarding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aepssdkssz.FingpayAePSBiometricFragment;
import com.aepssdkssz.FingpayAePSBiometricPagerAdapter;
import com.aepssdkssz.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.fingpayonboard.FingpayOnboardResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayUserRequest;
import com.aepssdkssz.network.model.fingpayonboard.FingpayUserRequestData;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.DialogProgressFragment;
import com.aepssdkssz.util.Utility;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SDKUserVerifyOtpDialog extends DialogFragment {

    private String otp = "",aadharNumber="",deviceNumber="",encodeFPTxnId="",primaryKeyId="";
    private VerifyOTPListener listener;
    private TextView tv_verify_otp_message;


    public void setListener(VerifyOTPListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_fingpay_verify_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        TextView ssz_tv_resend = view.findViewById(R.id.ssz_tv_resend);
        TextInputLayout til_otp = view.findViewById(R.id.til_otp);
        Button btn_verify = view.findViewById(R.id.btn_verify);
        tv_verify_otp_message =view.findViewById(R.id.tv_verify_otp_message);
        Utility.hideView(tv_verify_otp_message);

        tv_toolbar_title.setText("KYC Verify OTP");
        Utility.hideView(tv_done);

        Bundle bundle = getArguments();
        if (bundle != null) {
            aadharNumber = bundle.getString(Constants.AADHAR_NUMBER);
            deviceNumber = bundle.getString(Constants.DEVICE_NUMBER);
            sendOtp();
        }


        iv_close.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent();
            intent.putExtra("Message","Closed by user");
            intent.putExtra("StatusCode","001");
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();
        });

        btn_verify.setOnClickListener(v -> {
            otp = Objects.requireNonNull(til_otp.getEditText()).getText().toString().trim();
            if (otp.isEmpty()) {
                Utility.toast(requireActivity(), "Enter OTP");
            } else {
                verifyOtp();
            }
        });
        tv_done.setOnClickListener(v -> {
            otp = Objects.requireNonNull(til_otp.getEditText()).getText().toString().trim();
            if (otp.isEmpty()) {
                Utility.toast(requireActivity(), "Enter OTP");
            } else {
                verifyOtp();
            }
        });
        ssz_tv_resend.setOnClickListener(v -> {
            resendOtp();
        });
    }

    private void sendOtp() {
        if (Utility.checkConnection(requireActivity())) {
            DialogProgressFragment pd = DialogProgressFragment.newInstance("Loading. Please wait...", "Sending Otp...");
            DialogProgressFragment.showDialog(pd, requireActivity().getSupportFragmentManager());
            FingpayUserRequest req = new FingpayUserRequest();
            req.setKey(Utility.getData(requireActivity(), Constants.TOKEN));
            req.setAgentID(Utility.getData(requireActivity(), Constants.MERCHANT_ID));

            FingpayUserRequestData data=new FingpayUserRequestData();

            data.setLatitude(Utility.getData(requireActivity(),Constants.LATITUDE));
            data.setLongitude(Utility.getData(requireActivity(),Constants.LONGITUDE));
            data.setDeviceIMEI(deviceNumber);
            data.setAadhaarNumber(aadharNumber);
            req.setData(data);


            SSZAePSRetrofitClient.getClient(requireActivity())
                    .fingpayUserRequestOTP(req)
                    .enqueue(new Callback<FingpayOnboardResponse>() {
                        @Override
                        public void onResponse(Call<FingpayOnboardResponse> call, Response<FingpayOnboardResponse> response) {
                            try {
                                pd.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    FingpayOnboardResponse res = response.body();
                                    if ("0".equalsIgnoreCase(res.getStatus())) {

                                        primaryKeyId=res.getData().getPrimaryKeyId();
                                        encodeFPTxnId=res.getData().getEncodeFPTxnId();

                                        tv_verify_otp_message.setText(res.getMessage());
                                        Utility.showView(tv_verify_otp_message);
                                        Utility.toast(requireActivity(), res.getMessage());

                                    } else {
                                        Utility.toast(requireActivity(), res.getMessage());
                                        SDKUserNumberDialog.showDialog(requireActivity().getSupportFragmentManager(),aadharNumber,deviceNumber,res.getMessage());
                                    }
                                } else {
                                    Utility.toast(requireActivity(), "Technical Error!");
                                    SDKUserNumberDialog.showDialog(requireActivity().getSupportFragmentManager(),aadharNumber,deviceNumber,"Technical Error!");
                                }
                            } catch (Exception e) {
                                SDKUserNumberDialog.showDialog(requireActivity().getSupportFragmentManager(),aadharNumber,deviceNumber,"Parser Error : " + e.getLocalizedMessage());
                                Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<FingpayOnboardResponse> call, Throwable t) {
                            pd.dismiss();
                            Utility.toast(requireActivity(), "Error : " + t.getLocalizedMessage());
                            //showGetCustomerDialog();

                        }
                    });
        }
    }

    private void resendOtp() {
        if (Utility.checkConnection(requireActivity())) {
            DialogProgressFragment pd = DialogProgressFragment.newInstance("Loading. Please wait...", "Sending Otp...");
            DialogProgressFragment.showDialog(pd, requireActivity().getSupportFragmentManager());
            FingpayUserRequest req = new FingpayUserRequest();
            req.setKey(Utility.getData(requireActivity(), Constants.TOKEN));
            req.setAgentID(Utility.getData(requireActivity(), Constants.MERCHANT_ID));

            FingpayUserRequestData data=new FingpayUserRequestData();
            data.setEncodeFPTxnId(encodeFPTxnId);
            data.setDeviceIMEI(deviceNumber);
            data.setPrimaryKeyId(primaryKeyId);

            req.setData(data);


            SSZAePSRetrofitClient.getClient(requireActivity())
                    .fingpayUserResentOTP(req)
                    .enqueue(new Callback<FingpayOnboardResponse>() {
                        @Override
                        public void onResponse(Call<FingpayOnboardResponse> call, Response<FingpayOnboardResponse> response) {
                            try {
                                pd.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    FingpayOnboardResponse res = response.body();
                                    if ("0".equalsIgnoreCase(res.getStatus())) {
                                        Utility.toast(requireActivity(), res.getMessage());
                                        primaryKeyId=res.getData().getPrimaryKeyId();
                                        encodeFPTxnId=res.getData().getEncodeFPTxnId();

                                        tv_verify_otp_message.setText(res.getMessage());
                                        Utility.showView(tv_verify_otp_message);
                                    } else {
                                        tv_verify_otp_message.setText(res.getMessage());
                                        Utility.showView(tv_verify_otp_message);
                                        Utility.toast(requireActivity(), res.getMessage());
                                        showGetCustomerDialog();
                                    }
                                } else {
                                    showGetCustomerDialog();
                                }
                            } catch (Exception e) {
                                Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());

                                showGetCustomerDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<FingpayOnboardResponse> call, Throwable t) {
                            pd.dismiss();
                            Utility.toast(requireActivity(), "Error : " + t.getLocalizedMessage());
                            showGetCustomerDialog();

                        }
                    });
        }
    }

    private void showGetCustomerDialog() {
        dismiss();
        SDKUserNumberDialog.showDialog(requireActivity().getSupportFragmentManager(), aadharNumber,deviceNumber);
    }

    private void verifyOtp() {
        if (Utility.checkConnection(requireActivity())) {
            DialogProgressFragment pd = DialogProgressFragment.newInstance("Loading. Please wait...", "Verify OTP...");
            DialogProgressFragment.showDialog(pd, getChildFragmentManager());
            FingpayUserRequest req = new FingpayUserRequest();
            req.setKey(Utility.getData(requireActivity(), Constants.TOKEN));
            req.setAgentID(Utility.getData(requireActivity(), Constants.MERCHANT_ID));

            FingpayUserRequestData data=new FingpayUserRequestData();
            data.setOtp(otp);
            data.setDeviceIMEI(deviceNumber);
            data.setEncodeFPTxnId(encodeFPTxnId);
            data.setPrimaryKeyId(primaryKeyId);

            req.setData(data);

            SSZAePSRetrofitClient.getClient(requireActivity())
                    .fingpayUserVerifyOTP(req).enqueue(new Callback<FingpayOnboardResponse>() {
                @Override
                public void onResponse(Call<FingpayOnboardResponse> call, Response<FingpayOnboardResponse> response) {
                    try {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            FingpayOnboardResponse res = response.body();
                            if ("0".equalsIgnoreCase(res.getStatus())) {
                                primaryKeyId=res.getData().getPrimaryKeyId();
                                encodeFPTxnId=res.getData().getEncodeFPTxnId();
                                //tv_verify_otp_message.setText(res.getMessage());
                                //Utility.showView(tv_verify_otp_message);
                                //SDKUserBiometricDialog.showDialog(requireActivity().getSupportFragmentManager(),aadharNumber,deviceNumber,encodeFPTxnId,primaryKeyId);
                                Utility.saveData(requireActivity(),Constants.AADHAR_NUMBER,aadharNumber);
                                Utility.saveData(requireActivity(),Constants.DEVICE_NUMBER,deviceNumber);
                                Utility.saveData(requireActivity(),Constants.PRIMARY_KeyId,primaryKeyId);
                                Utility.saveData(requireActivity(),Constants.ENCODED_FPTxnId,encodeFPTxnId);

                                dismiss();
                            } else {
                                Utility.toast(requireActivity(), res.getMessage());
                                tv_verify_otp_message.setText(res.getMessage());
                                Utility.showView(tv_verify_otp_message);
                            }
                        }
                    } catch (Exception e) {
                        Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());
                        Utility.loge("Parser Error", e.getLocalizedMessage());
                        tv_verify_otp_message.setText("Parser Error : " + e.getLocalizedMessage());
                        Utility.showView(tv_verify_otp_message);
                    }
                }

                @Override
                public void onFailure(Call<FingpayOnboardResponse> call, Throwable t) {
                    Utility.toast(requireActivity(), "Error : " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }



    public interface VerifyOTPListener {
        void onVerifyOtp();
    }

    public static SDKUserVerifyOtpDialog newInstance(String aadhar_number,String device_number) {
        Bundle args = new Bundle();
        SDKUserVerifyOtpDialog fragment = new SDKUserVerifyOtpDialog();
        args.putString(Constants.AADHAR_NUMBER, aadhar_number);
        args.putString(Constants.DEVICE_NUMBER, device_number);
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager, String aadhar_number,String device_number) {
        SDKUserVerifyOtpDialog dialog = SDKUserVerifyOtpDialog.newInstance(aadhar_number,device_number);
        dialog.show(manager, "Show Dialog");
    }
}
