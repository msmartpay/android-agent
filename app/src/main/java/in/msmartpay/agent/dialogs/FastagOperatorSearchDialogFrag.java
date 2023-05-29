package in.msmartpay.agent.dialogs;

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

import java.util.ArrayList;
import java.util.List;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.model.fastag.FastagOperator;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;


public class FastagOperatorSearchDialogFrag extends DialogFragment {
    private ImageView iv_back;
    private RecyclerView rv_bank;
    private EditText et_search;
    private List<FastagOperator> list = new ArrayList<>();
    private FastagOperatorSearchAdapter adapter;
    private OperatorSelectedListener listener;

    private void setListener(OperatorSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aeps_search_bank_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_back = view.findViewById(R.id.iv_back);
        rv_bank = view.findViewById(R.id.rv_bank);
        et_search = view.findViewById(R.id.et_search);

        if (getArguments() != null) {
            String strJson = getArguments().getString("OperatorList");
            if (strJson != null) {
                list = Util.getListFromJson(strJson, FastagOperator.class);
                if (list != null && list.size() > 0) {

                } else {
                    dismiss();
                }
            }
        }

        L.m2("List Size", "" + list.size());
        adapter = new FastagOperatorSearchAdapter(list, model -> {
            listener.onOperatorSelected(model);
            dismiss();
        });
        rv_bank.setAdapter(adapter);

        iv_back.setOnClickListener(v -> {
            dismiss();
        });

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

    public static FastagOperatorSearchDialogFrag newInstance(List<FastagOperator> operatorList, OperatorSelectedListener listener) {
        Bundle args = new Bundle();
        args.putString("OperatorList", Util.getStringFromModel(operatorList));
        FastagOperatorSearchDialogFrag fragment = new FastagOperatorSearchDialogFrag();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public interface OperatorSelectedListener {
        void onOperatorSelected(FastagOperator model);
    }
}
