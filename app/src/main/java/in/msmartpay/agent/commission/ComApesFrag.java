package in.msmartpay.agent.commission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.commission.adapter.ApesAdapter;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.commission.ApesModel;
import in.msmartpay.agent.network.model.commission.CommissionModel;
import in.msmartpay.agent.network.model.commission.CommissionRequest;
import in.msmartpay.agent.network.model.commission.CommissionResponse;
import in.msmartpay.agent.utility.BaseFragment;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ComApesFrag extends BaseFragment {
    private RecyclerView rv_comm;
    private ProgressDialogFragment pd;
    private String txn_key,agentID;

    public ComApesFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.commision_frag, container, false);
        rv_comm = view.findViewById(R.id.rv_comm);
        rv_comm.setLayoutManager(new LinearLayoutManager(getActivity()));

        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);
        getCommissions();
        return  view;
    }
    private void getCommissions() {

        if (NetworkConnection.isConnectionAvailable2(getActivity())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching Commissions...");
            ProgressDialogFragment.showDialog(pd,getChildFragmentManager());


            CommissionRequest request = new CommissionRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService("AEPS");

            RetrofitClient.getClient(getActivity())
                    .getCommissions(request).enqueue(new Callback<CommissionResponse>() {
                @Override
                public void onResponse(@NotNull Call<CommissionResponse> call, @NotNull retrofit2.Response<CommissionResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        CommissionResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            ArrayList<ApesModel> list = new ArrayList<>();
                            if (res.getCommList()!=null)
                                for (CommissionModel resObj:res.getCommList()){
                                    ApesModel model = new ApesModel();
                                    model.setService(resObj.getService());
                                    model.setOperstor(resObj.getOperator());
                                    model.setRange(resObj.getStartRange()+" - "+resObj.getEndRange());
                                    model.setComm(resObj.getComm()+" - "+resObj.getCommType());
                                    list.add(model);
                                }

                            ApesAdapter adapter = new ApesAdapter(getActivity(),list);
                            rv_comm.setAdapter(adapter);
                        } else {
                            L.toastS(getActivity(), res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<CommissionResponse> call, @NotNull Throwable t) {
                    L.toastS(getActivity(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }


}
