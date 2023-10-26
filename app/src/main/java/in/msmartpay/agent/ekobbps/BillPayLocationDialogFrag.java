package in.msmartpay.agent.ekobbps;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import in.msmartpay.agent.R;
import in.msmartpay.agent.databinding.EkoBbpsSearchDialogBinding;
import in.msmartpay.agent.network.model.ekobbps.OperatorLocation;
import in.msmartpay.agent.utility.L;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BillPayLocationDialogFrag extends DialogFragment {
    private Context context;
    private ProgressDialog pd;
    private List<OperatorLocation> list = new ArrayList<>();
    private BillPayLocationAdapter adapter;
    private SelectedListener listener;
    private EkoBbpsSearchDialogBinding binding;

    public void setListener(SelectedListener listener){
        this.listener = listener;
    }
    public void setList(List<OperatorLocation> list){
        this.list = list;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EkoBbpsSearchDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        L.m2("List Size",""+list.size());
        adapter = new BillPayLocationAdapter(list, model -> {
            listener.onSelected(model);
            dismiss();
        });
        binding.rvList.setAdapter(adapter);

        binding.ivBack.setOnClickListener(v -> {
            dismiss();
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            adapter.getFilter().filter( Objects.requireNonNull(binding.etSearch.getText()).toString());
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public interface SelectedListener{
        void onSelected(OperatorLocation model);
    }
}
