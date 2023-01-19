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
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;

import java.util.ArrayList;
import java.util.List;


public class SSZAePSBankSearchDialogFrag extends DialogFragment {
    private ImageView iv_back;
    private RecyclerView rv_bank,rv_bank_preferred;
    private EditText et_search;

    private List<BankModel> list = new ArrayList<BankModel>();
    private List<BankModel> preferredList = new ArrayList<BankModel>();
    private SSZAePSBankSearchAdapter adapter;
    private SSZAePSBankPreferredAdapter preferredAdapter;
    private BankSelectedListener listener;

    private void setListener(BankSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_aeps_search_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_back = view.findViewById(R.id.iv_back);
        rv_bank = view.findViewById(R.id.rv_bank);
        et_search = view.findViewById(R.id.et_search);
        rv_bank_preferred= view.findViewById(R.id.rv_bank_preferred);
        try {
            list = Utility.getListFromJson(Utility.getData(requireActivity(), Constants.BANK_LIST), BankModel.class);
            preferredList= Utility.getListFromJson(Utility.getData(requireActivity(), Constants.BANK_LIST_PREFERRED), BankModel.class);
        } catch (Exception e) {
            Utility.toast(requireActivity(), "Bank List Not Available");
        }

        adapter = new SSZAePSBankSearchAdapter(list, bankModel -> {
            listener.onBankSelected(bankModel);
            dismiss();
        });
        rv_bank.setAdapter(adapter);

        preferredAdapter = new SSZAePSBankPreferredAdapter(preferredList, bankModel -> {
            listener.onBankSelected(bankModel);
            dismiss();
        });
        rv_bank_preferred.setAdapter(preferredAdapter);

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

    public static SSZAePSBankSearchDialogFrag newInstance(BankSelectedListener listener) {
        Bundle args = new Bundle();
        SSZAePSBankSearchDialogFrag fragment = new SSZAePSBankSearchDialogFrag();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public interface BankSelectedListener {
        void onBankSelected(BankModel bankModel);
    }
}
