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
import com.aepssdkssz.network.model.BankModel;
import com.aepssdkssz.network.model.BiometricDevice;
import com.aepssdkssz.network.model.fingpayonboard.FingpayStateData;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;

import java.util.ArrayList;
import java.util.List;


public class FingpayStateSearchDialogFrag extends DialogFragment{
    private ImageView iv_back;
    private RecyclerView rv_fingpayState;
    private EditText et_search;

    private List<FingpayStateData> list = new ArrayList<FingpayStateData>();
    private FingpayStateSearchAdapter adapter;
    private FingpayStateSelectedListener listener;

    private void setListener(FingpayStateSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fingpay_state_search_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_back = view.findViewById(R.id.iv_fingpay_state_back);
        rv_fingpayState = view.findViewById(R.id.rv_fingpay_state);
        et_search = view.findViewById(R.id.et_fingpay_state_search);

        et_search.setHint("Select Shop State");
        try {
            list = Utility.getListFromJson(Utility.getData(requireActivity(), Constants.FINGPAY_SATE_LIST), FingpayStateData.class);
        } catch (Exception e) {
            Utility.toast(requireActivity(), "States Not Available");
        }

        adapter = new FingpayStateSearchAdapter(list, fingpayState -> {
            listener.onStateSelected(fingpayState);
            dismiss();
        });
        rv_fingpayState.setAdapter(adapter);

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


    public static FingpayStateSearchDialogFrag newInstance(FingpayStateSelectedListener listener) {
        Bundle args = new Bundle();
        FingpayStateSearchDialogFrag fragment = new FingpayStateSearchDialogFrag();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public interface FingpayStateSelectedListener {
        void onStateSelected(FingpayStateData fingpayStateData);
    }
}
