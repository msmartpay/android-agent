package in.msmartpayagent.myWallet;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpayagent.R;
import in.msmartpayagent.myWallet.adapter.BalanceListAdapter;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainRequest;
import in.msmartpayagent.network.model.wallet.BalHistoryModel;
import in.msmartpayagent.network.model.wallet.BalHistoryResponse;
import in.msmartpayagent.utility.BaseFragment;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceHistoryFragment extends BaseFragment {

    private String agentID, txn_key = "";
    private Context context;
    private ProgressDialogFragment pd;
    private RecyclerView rv_bal_list;
    private TextView nodata;
    ArrayList<BalHistoryModel> balHistoryArray = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_balance_history, container, false);

        context = getActivity();
        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);

        rv_bal_list = view.findViewById(R.id.rv_bal_list);
        nodata =  view.findViewById(R.id.nodata);

        balanceHistoryFragmentRequest();

    return view;
    }

    private void balanceHistoryFragmentRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching History...");
        ProgressDialogFragment.showDialog(pd,getChildFragmentManager());

        MainRequest request = new MainRequest();
        request.setAgentID(agentID);
        request.setTxn_key(txn_key);
        rv_bal_list.setVisibility(View.VISIBLE);
        nodata.setVisibility(View.GONE);
        RetrofitClient.getClient(getActivity())
                .getWalletBalReqHistory(request).enqueue(new Callback<BalHistoryResponse>() {
            @Override
            public void onResponse(@NotNull Call<BalHistoryResponse> call, @NotNull retrofit2.Response<BalHistoryResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    BalHistoryResponse res = response.body();
                    if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                        if (balHistoryArray == null)
                            balHistoryArray = new ArrayList<>();
                        else
                            balHistoryArray.clear();
                        if (res.getData()!=null && res.getData().size()>0) {
                            balHistoryArray = (ArrayList<BalHistoryModel>) res.getData();
                            rv_bal_list.setAdapter(new BalanceListAdapter(balHistoryArray));
                        }else {
                            rv_bal_list.setVisibility(View.GONE);
                            nodata.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rv_bal_list.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        L.toastS(getActivity(), res.getResponseMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BalHistoryResponse> call, @NotNull Throwable t) {
                L.toastS(getActivity(), "data failuer " + t.getLocalizedMessage());
                rv_bal_list.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                pd.dismiss();
            }
        });
    }
}
