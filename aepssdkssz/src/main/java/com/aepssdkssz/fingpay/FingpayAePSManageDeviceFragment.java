package com.aepssdkssz.fingpay;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.aepssdkssz.R;
import com.aepssdkssz.dialog.SSZAePSDeviceSearchDialogFrag;
import com.aepssdkssz.network.SSZAePSAPIError;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.BiometricDevice;
import com.aepssdkssz.network.model.DeviceUpdateRequest;
import com.aepssdkssz.network.model.MainResponse;
import com.aepssdkssz.util.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class FingpayAePSManageDeviceFragment extends Fragment {

    private TextView tv_partner_name;
    private TextInputLayout til_d_number, til_d_type;
    private EditText et_d_type;
    private FloatingActionButton fab_manage;
    private String device_number, device_type;
    private ProgressDialog progressDialog;
    private BiometricDevice selectedBiometricDevice;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_aeps_manage_device_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        Utility.setErrorFalseOfTextInputLayout(til_d_number);
        Utility.setErrorFalseOfTextInputLayout(til_d_type);

        til_d_type.setOnClickListener(view12 -> {
            showDeviceDialog();
        });
        et_d_type.setOnClickListener(view12 -> {
            showDeviceDialog();
        });

        fab_manage.setOnClickListener(view1 -> {
            boolean flag = true;
            device_number = Objects.requireNonNull(til_d_number.getEditText()).getText().toString().trim();
            device_type = Objects.requireNonNull(til_d_type.getEditText()).getText().toString().trim();
            if (device_number.isEmpty()) {
                til_d_number.setError("Enter Device Number");
                flag = false;
            }
            if (device_type.isEmpty()) {
                til_d_type.setError("Select Device Type");
                flag = false;
            }

            if (flag) {
                updateDevice();
            }
        });
    }

    private void showDeviceDialog(){
        SSZAePSDeviceSearchDialogFrag dialogFrag = SSZAePSDeviceSearchDialogFrag.newInstance(biometricDevice -> {
            Objects.requireNonNull(til_d_type.getEditText()).setText(biometricDevice.getDevice_name());
            selectedBiometricDevice = biometricDevice;
        });
        dialogFrag.show(requireActivity().getSupportFragmentManager(), "Search Device");
    }


    private void updateDevice() {
        if (Utility.checkConnection(requireActivity())) {
            progressDialog = Utility.getProgressDialog(requireActivity());
            progressDialog.show();
            DeviceUpdateRequest request = new DeviceUpdateRequest();
            request.setDevice_number(device_number);
            request.setDevice_type(device_type);
            SSZAePSRetrofitClient.getClient(requireActivity())
                    .updateDevice(request)
                    .enqueue(new Callback<MainResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<MainResponse> call, @NotNull Response<MainResponse> response) {
                            progressDialog.dismiss();
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    MainResponse res = response.body();
                                    if (res.getStatus() == 0) {
                                        showsSuccessDialog(res.getMessage());
                                    } else {
                                        Utility.showMessageDialogue(requireActivity(), res.getMessage(), "Update Device");
                                    }
                                }else {
                                    SSZAePSAPIError error = SSZAePSAPIError.parseError(response,requireActivity());
                                    Utility.showMessageDialogue(requireActivity(), "" + error.message(), "Update Device");

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utility.showMessageDialogue(requireActivity(), "" + e.getMessage(), "Update Device");
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<MainResponse> call, @NotNull Throwable t) {
                            progressDialog.dismiss();
                            Utility.showMessageDialogue(requireActivity(), t.getMessage(), "Update Device");
                            Utility.loge("validate User", t.getMessage());
                        }
                    });
        }
    }
    private void showsSuccessDialog(String message) {
        new AlertDialog.Builder(requireActivity())
                .setTitle("Manage Device")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    et_d_type.setText("");
                    Objects.requireNonNull(til_d_number.getEditText()).setText("");
                }).show();
    }
    private void initViews(View view) {
        tv_partner_name = view.findViewById(R.id.tv_partner_name);
        til_d_number = view.findViewById(R.id.til_d_number);
        til_d_type = view.findViewById(R.id.til_d_type);
        et_d_type = view.findViewById(R.id.et_d_type);
        fab_manage = view.findViewById(R.id.fab_manage);

    }

    public static FingpayAePSManageDeviceFragment newInstance(int index) {
        FingpayAePSManageDeviceFragment fragment = new FingpayAePSManageDeviceFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

}