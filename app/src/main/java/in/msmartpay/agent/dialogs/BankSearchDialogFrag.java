package in.msmartpay.agent.dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.dmr.BankListDmrResponse;
import in.msmartpay.agent.network.model.dmr.BankListModel;
import in.msmartpay.agent.network.model.dmr.BankRequest;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;


public class BankSearchDialogFrag extends DialogFragment implements BankSearchAdapter.BankListener {
        private ImageView iv_back;
    private RecyclerView rv_bank;
    private EditText et_search;

    private Context context;
    private ProgressDialog pd;
    private List<BankListModel> list = new ArrayList<>();
    private BankSearchAdapter adapter;
    private BankSelectedListener listener;

    private void setListener(BankSelectedListener listener){
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

        context = requireActivity();

       if(getArguments()!=null){
           String strJson = getArguments().getString("BankList");
          if (strJson!=null){
              list = Util.getListFromJson(strJson,BankListModel.class);
              if (list!=null && list.size()>0){
              }else {
                  getBankList();
              }
          }
       }

       L.m2("List Size",""+list.size());
        adapter = new BankSearchAdapter(list, this);
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

    public static BankSearchDialogFrag newInstance(List<BankListModel> bankList, BankSelectedListener listener) {
        Bundle args = new Bundle();
        args.putString("BankList",Util.getStringFromModel(bankList));
        BankSearchDialogFrag fragment = new BankSearchDialogFrag();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    //get Access key
    private void getBankList(){
        if (NetworkConnection.isConnectionAvailable(context)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgress(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(true);
            BankRequest request = new BankRequest();
            request.setAgentID(Util.LoadPrefData(requireActivity(), Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(requireActivity(), Keys.TXN_KEY));
            request.setSenderId("9711402774");
            RetrofitClient.getClient(context).getBankListDmr(request)
                    .enqueue(new Callback<BankListDmrResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(Call<BankListDmrResponse> call, Response<BankListDmrResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                BankListDmrResponse res = response.body();
                                if ("0".equals(res.getStatus())) {
                                    if (res.getBankList()!=null && res.getBankList().size()>0){
                                        list = res.getBankList();
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    L.toastS(context,res.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BankListDmrResponse> call, Throwable t) {
                            pd.dismiss();
                        }
                    });
        }
    }

    @Override
    public void onBankSelected(BankListModel bankModel) {
        listener.onBankSelected(bankModel);
        dismiss();
    }

    public interface BankSelectedListener{
        void onBankSelected(BankListModel bankModel);
    }
}
