package com.aepssdkssz.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aepssdkssz.R;
import com.aepssdkssz.network.model.BiometricDevice;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;

import java.util.ArrayList;
import java.util.List;


public class SSZAePSDeviceSearchDialogFrag extends DialogFragment{
    private ImageView iv_back;
    private RecyclerView rv_bank;
    private EditText et_search;

    private List<BiometricDevice> list = new ArrayList<BiometricDevice>();
    private SSZAePSDeviceSearchAdapter adapter;
    private DeviceSelectedListener listener;

    private void setListener(DeviceSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_aeps_device_search_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_back = view.findViewById(R.id.iv_back);
        rv_bank = view.findViewById(R.id.rv_bank);
        et_search = view.findViewById(R.id.et_search);

        et_search.setHint("Select Device");
        try {
            list = Utility.getListFromJson(Utility.getData(requireActivity(), Constants.BIOMETRIC_PROVIDERS), BiometricDevice.class);
        } catch (Exception e) {
            Utility.toast(requireActivity(), "Biometric Device List Not Available");
        }

        adapter = new SSZAePSDeviceSearchAdapter(list, deviceModel -> {
            listener.onDeviceSelected(deviceModel);
            dismiss();
        });
        rv_bank.setAdapter(adapter);

        iv_back.setOnClickListener(view1 -> dismiss());

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(et_search.getText().toString());
            }
        });
    }

    public static SSZAePSDeviceSearchDialogFrag newInstance(DeviceSelectedListener listener) {
        Bundle args = new Bundle();
        SSZAePSDeviceSearchDialogFrag fragment = new SSZAePSDeviceSearchDialogFrag();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public interface DeviceSelectedListener {
        void onDeviceSelected(BiometricDevice biometricDevice);
    }
}
