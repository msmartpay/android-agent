package com.aepssdkssz.dialog.fingpayonboarding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.DialogProgressFragment;
import com.aepssdkssz.util.Utility;
import com.google.android.material.textfield.TextInputLayout;
import com.aepssdkssz.R;
import java.util.Objects;


public class SDKUserNumberDialog extends DialogFragment {

    private String otp = "",aadhar_number="", device_number="";
    private Context context;
    private DialogProgressFragment pd;
    private UserNumberVerifyListener listener;
    private boolean isGenerateOtp;

    public void setListener(UserNumberVerifyListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_fingpay_user_verfy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        Utility.hideView(tv_done);

        tv_toolbar_title.setText("KYC Request OTP");

        Button btn_send = view.findViewById(R.id.btn_send);
        TextInputLayout til_aadhar_number = view.findViewById(R.id.til_aadhar_number);
        TextInputLayout til_device_number = view.findViewById(R.id.til_device_number);
        TextView tv_request_otp_message = view.findViewById(R.id.tv_request_otp_message);
        Utility.hideView(tv_request_otp_message);
        til_device_number.getEditText().setText(Utility.getData(requireActivity(),Constants.DEVICE_IMEI));
        til_device_number.getEditText().setEnabled(false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String aadharNo=bundle.getString(Constants.AADHAR_NUMBER);
            if(null==aadharNo || "".equalsIgnoreCase(aadharNo)){
                til_aadhar_number.getEditText().setText("");
                til_aadhar_number.getEditText().setEnabled(true);
            }else{
                til_aadhar_number.getEditText().setText(bundle.getString(Constants.AADHAR_NUMBER));
                til_aadhar_number.getEditText().setEnabled(false);
            }
            String deviceNo=bundle.getString(Constants.DEVICE_NUMBER);
            if(null==deviceNo || "".equalsIgnoreCase(deviceNo)){
                til_device_number.getEditText().setText("");
                til_device_number.getEditText().setEnabled(true);
            }else{
                til_device_number.getEditText().setText(bundle.getString(Constants.DEVICE_NUMBER));
                til_device_number.getEditText().setEnabled(false);
            }
            String message=bundle.getString(Constants.MESSAGE);
            if(null==message || "".equalsIgnoreCase(message)){
                Utility.hideView(tv_request_otp_message);
            }else{
                tv_request_otp_message.setText(message);
                Utility.showView(tv_request_otp_message);
            }
        }


        iv_close.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent();
            intent.putExtra("Message","Closed by user");
            intent.putExtra("StatusCode","001");
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();
        });

        btn_send.setOnClickListener(v -> {
            aadhar_number = Objects.requireNonNull(til_aadhar_number.getEditText()).getText().toString().trim();
            device_number = Utility.getData(requireActivity(),Constants.DEVICE_IMEI);/*Objects.requireNonNull(til_device_number.getEditText()).getText().toString().trim();*/
            if (aadhar_number.isEmpty() || aadhar_number.length()<12) {
                Utility.toast(context, "Enter Aadhaar Number");
            } else if (device_number.isEmpty()) {
                Utility.toast(context, "Enter Device Number");
            } else {
                SDKUserVerifyOtpDialog.showDialog(requireActivity().getSupportFragmentManager(), aadhar_number,device_number);
                dismiss();
            }
        });
    }

    public interface UserNumberVerifyListener {
        void onNumberVerify();
    }

    public static SDKUserNumberDialog newInstance(String aadhar_number,String device_number) {
        Bundle args = new Bundle();
        SDKUserNumberDialog fragment = new SDKUserNumberDialog();

        args.putString(Constants.AADHAR_NUMBER, aadhar_number);
        args.putString(Constants.DEVICE_NUMBER, device_number);
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static SDKUserNumberDialog newInstance(String aadhar_number,String device_number,String message) {
        Bundle args = new Bundle();
        SDKUserNumberDialog fragment = new SDKUserNumberDialog();

        args.putString(Constants.AADHAR_NUMBER, aadhar_number);
        args.putString(Constants.DEVICE_NUMBER, device_number);
        args.putString(Constants.MESSAGE, message);
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager, String aadhar_number,String device_number) {
        SDKUserNumberDialog dialog = SDKUserNumberDialog.newInstance(aadhar_number,device_number);
        dialog.show(manager, "Show Dialog");
    }
    public static void showDialog(FragmentManager manager, String aadhar_number,String device_number,String message) {
        SDKUserNumberDialog dialog = SDKUserNumberDialog.newInstance(aadhar_number,device_number,message);
        dialog.show(manager, "Show Dialog");
    }
}
