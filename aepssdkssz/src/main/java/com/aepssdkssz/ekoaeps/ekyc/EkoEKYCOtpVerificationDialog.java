package com.aepssdkssz.ekoaeps.ekyc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.aepstransaction.ekoekyc.EKYCRequestOTPModal;
import com.aepssdkssz.network.model.aepstransaction.ekoekyc.EKYCResponseModal;
import com.aepssdkssz.network.model.aepstransaction.ekoekyc.EKYCVerifyOTPModal;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;
import com.google.android.material.textfield.TextInputLayout;

import com.aepssdkssz.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EkoEKYCOtpVerificationDialog extends DialogFragment {

    private String otp = "";
    private Context context;
    private ProgressDialog progressDialog;
    private String number, name;
    private TextInputLayout til_eko_registered_mobile,til_eko_registered_aadhar,til_verification_otp;

    private boolean isRequestOtp = true;
    private EKYCVerifyOTPModal ekycVerifyOTPRequest;

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.eko_ekyc_otp_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        Utility.hideView(tv_done);
        tv_toolbar_title.setText("e-KYC OTP Verification");
        Button btn_send = view.findViewById(R.id.btn_send);

        //Form Fields
        til_verification_otp = view.findViewById(R.id.til_verification_otp);
        til_eko_registered_mobile = view.findViewById(R.id.til_eko_registered_mobile);
        til_eko_registered_aadhar = view.findViewById(R.id.til_eko_registered_aadhar);

        if(isRequestOtp)
            Utility.hideView(til_verification_otp);

        Bundle bundle = getArguments();
        if (bundle != null) {
            number = bundle.getString(Constants.MERCHANT_MOBILE);
            til_eko_registered_mobile.getEditText().setText(number);
            til_eko_registered_mobile.getEditText().setFocusable(false);
            til_eko_registered_mobile.getEditText().setFocusableInTouchMode(false);
        }


        iv_close.setOnClickListener(v -> {
            dismiss();
        });

        btn_send.setOnClickListener(v -> {
            if (til_eko_registered_aadhar.getEditText().getText().toString()==null || til_eko_registered_aadhar.getEditText().getText().toString().trim().length()<12) {
                Utility.toast(context, "Enter Valid Aadhaar Number");
            } else {
                if(isRequestOtp)
                    requestEKYCOTP();
                else {
                    if (til_verification_otp.getEditText().getText().toString()==null || "".equalsIgnoreCase(til_verification_otp.getEditText().getText().toString())) {
                        Utility.toast(context, "Enter Valid Aadhaar Verification OTP");
                    } else {
                        verifyEKYCOTP();
                    }
                }
            }
        });
    }

    public static EkoEKYCOtpVerificationDialog newInstance() {
        Bundle args = new Bundle();
        EkoEKYCOtpVerificationDialog fragment = new EkoEKYCOtpVerificationDialog();
        //args.putString(Keys.CUSTOMER_MOBILE, number);
        //fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager) {
        EkoEKYCOtpVerificationDialog dialog = EkoEKYCOtpVerificationDialog.newInstance();
        dialog.show(manager, "Show Dialog");
    }

    private void requestEKYCOTP() {
        if (Utility.checkConnection(requireActivity())) {
            progressDialog = Utility.getProgressDialog(requireActivity());
            progressDialog.show();
            EKYCRequestOTPModal req = new EKYCRequestOTPModal();
            req.setAgentRegisteredMobile(til_eko_registered_mobile.getEditText().getText().toString().trim());
            req.setAgentAadhaarNumber(til_eko_registered_aadhar.getEditText().getText().toString().trim());
            req.setLatlong(Utility.getData(requireActivity(),Constants.LAT_LONG));
            SSZAePSRetrofitClient.getClient(requireActivity())
                    .ekoEKycRequestOtp(req)
                    .enqueue(new Callback<EKYCResponseModal>() {
                        @Override
                        public void onResponse(Call<EKYCResponseModal> call, Response<EKYCResponseModal> response) {
                            try {
                                progressDialog.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    EKYCResponseModal res = response.body();
                                    if ("0".equalsIgnoreCase(res.getStatus())) {

                                        ekycVerifyOTPRequest = new EKYCVerifyOTPModal();
                                        ekycVerifyOTPRequest.setOtpRefId(res.getData().getOtpRefId());
                                        ekycVerifyOTPRequest.setReferenceTid(res.getData().getReferenceTid());

                                    } else if ("2".equalsIgnoreCase(res.getStatus())) {

                                    } else {

                                    }
                                } else {

                                }
                            } catch (Exception e) {
                                Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());

                            }
                        }

                        @Override
                        public void onFailure(Call<EKYCResponseModal> call, Throwable t) {
                            progressDialog.dismiss();
                            Utility.toast(requireActivity(), "Error : " + t.getLocalizedMessage());
                        }
                    });
        }
    }

    private void verifyEKYCOTP() {
        if (Utility.checkConnection(requireActivity())) {
            progressDialog = Utility.getProgressDialog(requireActivity());
            progressDialog.show();

            ekycVerifyOTPRequest.setAgentRegisteredMobile(til_eko_registered_mobile.getEditText().getText().toString().trim());
            ekycVerifyOTPRequest.setAgentAadhaarNumber(til_eko_registered_aadhar.getEditText().getText().toString().trim());
            ekycVerifyOTPRequest.setLatlong(Utility.getData(requireActivity(),Constants.LAT_LONG));
            ekycVerifyOTPRequest.setOtp(til_verification_otp.getEditText().getText().toString().trim());
            SSZAePSRetrofitClient.getClient(requireActivity())
                    .ekoEKycVerifyOtp(ekycVerifyOTPRequest)
                    .enqueue(new Callback<EKYCResponseModal>() {
                        @Override
                        public void onResponse(Call<EKYCResponseModal> call, Response<EKYCResponseModal> response) {
                            try {
                                progressDialog.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    EKYCResponseModal res = response.body();
                                    if ("0".equalsIgnoreCase(res.getStatus())) {
                                        EkoEKYCBiometricDialog.showDialog(requireActivity().getSupportFragmentManager(),ekycVerifyOTPRequest,res);
                                    } else {

                                    }
                                } else {

                                }
                            } catch (Exception e) {
                                Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());

                            }
                        }

                        @Override
                        public void onFailure(Call<EKYCResponseModal> call, Throwable t) {
                            progressDialog.dismiss();
                            Utility.toast(requireActivity(), "Error : " + t.getLocalizedMessage());
                        }
                    });
        }
    }
}
