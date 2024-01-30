package in.msmartpay.agent.orderProducts;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import in.msmartpay.agent.R;
import in.msmartpay.agent.myWallet.adapter.BalanceListAdapter;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainRequest;
import in.msmartpay.agent.network.model.MainRequest2;
import in.msmartpay.agent.network.model.order.OrderProductHistoryData;
import in.msmartpay.agent.network.model.order.OrderProductHistoryResponse;
import in.msmartpay.agent.network.model.wallet.BalHistoryModel;
import in.msmartpay.agent.network.model.wallet.BalHistoryResponse;
import in.msmartpay.agent.utility.BaseFragment;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderProductsHistoryFragment extends BaseFragment {
    private String agentID, txn_key = "";
    private Context context;
    private ProgressDialogFragment pd;
    private RecyclerView rv_list;
    private TextView nodata;
    ArrayList<OrderProductHistoryData> list = new ArrayList<>();
    private OrderProductsHistoryAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_product_history, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);
        rv_list = view.findViewById(R.id.rv_list);
        nodata = view.findViewById(R.id.nodata);
        balanceHistoryFragmentRequest();
        setUpAdapter();
    }

    private void setUpAdapter() {
        adapter = new OrderProductsHistoryAdapter(list, modal -> {

        });
        rv_list.setAdapter(adapter);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setUpNewData(){
        adapter.notifyDataSetChanged();
        if (list.size()>0){
            Util.hideView(nodata);
        }else {
            Util.showView(nodata);
        }
    }

    private void balanceHistoryFragmentRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching History...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
        MainRequest2 request = new MainRequest2();
        request.setAgentID(agentID);
        request.setKey(txn_key);
        Util.hideView(nodata);
        RetrofitClient.getClient(getActivity())
                .fetchProductOrderHistory(request).enqueue(new Callback<OrderProductHistoryResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<OrderProductHistoryResponse> call, @NotNull retrofit2.Response<OrderProductHistoryResponse> response) {
                        pd.dismiss();
                        list.clear();
                        if (response.isSuccessful() && response.body() != null) {
                            OrderProductHistoryResponse res = response.body();
                            if ("0".equalsIgnoreCase(res.getStatus())) {
                                if (res.getData() != null && res.getData().size() > 0) {
                                    list.addAll((ArrayList<OrderProductHistoryData>) res.getData());
                                }
                            }
                        }
                        setUpNewData();
                    }

                    @Override
                    public void onFailure(@NotNull Call<OrderProductHistoryResponse> call, @NotNull Throwable t) {
                        pd.dismiss();
                        list.clear();
                        setUpNewData();
                    }
                });
    }
}
