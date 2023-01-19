package com.aepssdkssz.dialog.fingpayonboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.aepssdkssz.dialog.FingpayStateSearchDialogFrag;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.fingpayonboard.FingpayOnboardResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayOnboardUserData;
import com.aepssdkssz.network.model.fingpayonboard.FingpayOnboardUserRequest;
import com.aepssdkssz.network.model.fingpayonboard.FingpayStateData;
import com.aepssdkssz.network.model.fingpayonboard.FingpayStateResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayUserRequest;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.DialogProgressFragment;
import com.aepssdkssz.util.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.aepssdkssz.R;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SDKUserRegistrationDialog extends DialogFragment {

    private RegisterListener listener;
    private String onboardAadharNumber, onboardShopAddress, onboardShopState;
    private List<FingpayStateData> stateList;
    private FingpayStateData fingpayState;

    public void setListener(RegisterListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_onboard_fingpay_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        Button btn_register = view.findViewById(R.id.btn_register);

        getState();

        TextInputLayout til_onboardAadhaarNumber = view.findViewById(R.id.til_onboardAadharNumber);
        TextInputLayout til_onboardShopAddress = view.findViewById(R.id.til_onboardShopAddress);

        TextInputEditText et_fingpay_onboard_state = view.findViewById(R.id.et_fingpay_onboard_state);

        Utility.hideView(tv_done);

        tv_toolbar_title.setText("KYC User Onboard");

        et_fingpay_onboard_state.setOnClickListener(v -> {
            FingpayStateSearchDialogFrag dialogFrag = FingpayStateSearchDialogFrag.newInstance(model -> {
                onboardShopState=model.getStateId();
                et_fingpay_onboard_state.setText(model.getState());
            });
            dialogFrag.show(requireActivity().getSupportFragmentManager(), "Search State");
        });

        iv_close.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent();
            intent.putExtra("Message","Closed by user");
            intent.putExtra("StatusCode","001");
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();
        });

        btn_register.setOnClickListener(v -> {
            onboardAadharNumber = Objects.requireNonNull(til_onboardAadhaarNumber.getEditText()).getText().toString().trim();
            onboardShopAddress = Objects.requireNonNull(til_onboardShopAddress.getEditText()).getText().toString().trim();
            //onboardShopState = Objects.requireNonNull().getText().toString().trim();

            if (onboardAadharNumber.isEmpty()) {
                Utility.toast(requireActivity(), "Enter Aadhaar Number");
            } else if (onboardShopAddress.isEmpty()) {
                Utility.toast(requireActivity(), "Enter Shop Address");
            } else {
                onboardUser();
            }
        });

    }

    private void onboardUser() {
        if (Utility.checkConnection(requireActivity())) {
            DialogProgressFragment pd = DialogProgressFragment.newInstance("Loading. Please wait...", "User On Boarding...");
            DialogProgressFragment.showDialog(pd, getChildFragmentManager());

            FingpayOnboardUserRequest registerRequest = new FingpayOnboardUserRequest();
            registerRequest.setAgentID(Utility.getData(requireActivity(), Constants.MERCHANT_ID));
            registerRequest.setKey(Utility.getData(requireActivity(), Constants.TOKEN));

            FingpayOnboardUserData data=new FingpayOnboardUserData();

            data.setAadhaarNumber(onboardAadharNumber);
            data.setMerchantAddress(onboardShopAddress);
            data.setMerchantState(onboardShopState);
            data.setLongitude(Utility.getData(requireActivity(),Constants.LONGITUDE));
            data.setLatitude(Utility.getData(requireActivity(),Constants.LATITUDE));
            registerRequest.setData(data);

            SSZAePSRetrofitClient.getClient(requireActivity())
                    .fingpayUserOnboard(registerRequest).enqueue(new Callback<FingpayOnboardResponse>() {
                @Override
                public void onResponse(Call<FingpayOnboardResponse> call, Response<FingpayOnboardResponse> response) {
                    pd.dismiss();
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            FingpayOnboardResponse res = response.body();
                            if ("0".equalsIgnoreCase(res.getStatus())) {

                                SDKUserNumberDialog.showDialog(requireActivity().getSupportFragmentManager(),onboardAadharNumber,"",res.getMessage());
                                //SDKUserSuccessDialog.showDialog(requireActivity().getSupportFragmentManager());
                                dismiss();
                            } else {
                                Utility.toast(requireActivity(), res.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());
                        Utility.loge("Parser Error", e.getLocalizedMessage());
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

    private void getState() {
        if (Utility.checkConnection(requireActivity())) {
            DialogProgressFragment pd = DialogProgressFragment.newInstance("Loading. Please wait...", "User On Boarding...");
            DialogProgressFragment.showDialog(pd, getChildFragmentManager());
            FingpayUserRequest request = new FingpayUserRequest();
            request.setAgentID(Utility.getData(requireActivity(), Constants.MERCHANT_ID));
            request.setKey(Utility.getData(requireActivity(), Constants.TOKEN));

            SSZAePSRetrofitClient.getClient(requireActivity())
                    .getFingpayState(request).enqueue(new Callback<FingpayStateResponse>() {
                @Override
                public void onResponse(@NotNull Call<FingpayStateResponse> call, @NotNull retrofit2.Response<FingpayStateResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        FingpayStateResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {

                            stateList=res.getStateList();

                            if (stateList != null)
                                Utility.saveData(requireActivity(), Constants.FINGPAY_SATE_LIST, Utility.getGson().toJson(stateList));
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FingpayStateResponse> call, @NotNull Throwable t) {
                    Utility.toast(requireActivity(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    public interface RegisterListener {
        void onRegister();
    }

    public static SDKUserRegistrationDialog newInstance() {
        //Bundle args = new Bundle();
        SDKUserRegistrationDialog fragment = new SDKUserRegistrationDialog();
        //args.putString(Constants.CUSTOMER_MOBILE, number);
        //fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager) {
        SDKUserRegistrationDialog dialog = SDKUserRegistrationDialog.newInstance();
        dialog.show(manager, "Show Dialog");
    }
}
