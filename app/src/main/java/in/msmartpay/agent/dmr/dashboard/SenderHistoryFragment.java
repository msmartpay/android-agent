package in.msmartpay.agent.dmr.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.msmartpay.agent.R;
import in.msmartpay.agent.dmr.adapter.SenderHistoryAdapter;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.dmr.SenderFindRequest;
import in.msmartpay.agent.network.model.dmr.SenderHistoryResponse;
import in.msmartpay.agent.network.model.dmr.Statement;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class SenderHistoryFragment extends Fragment {
    private ProgressDialogFragment pd;
    private RecyclerView listViewSender;
    private SwipeRefreshLayout swipe_refresh;
    private Context context;
    private ArrayList<Statement> arrayList = new ArrayList<Statement>();
    private String agentID, txnKey, mobileNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dmr_sender_history_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getActivity();

        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);
        mobileNumber = Util.LoadPrefData(getActivity(), Keys.SENDER_MOBILE);
        listViewSender = view.findViewById(R.id.list_view);
        swipe_refresh=view.findViewById(R.id.swipe_refresh);

        swipe_refresh.setOnRefreshListener(this::getSenderHistory);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSenderHistory();
    }

    private void getSenderHistory() {
        if (NetworkConnection.isConnectionAvailable(getActivity())) {
            swipe_refresh.setRefreshing(true);
            SenderFindRequest request = new SenderFindRequest();
            request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
            request.setSenderId(mobileNumber);
            RetrofitClient.getClient(context).senderHistory(request).enqueue(new Callback<SenderHistoryResponse>() {
                @Override
                public void onResponse(Call<SenderHistoryResponse> call, retrofit2.Response<SenderHistoryResponse> response) {
                    swipe_refresh.setRefreshing(false);
                    if (response.isSuccessful() && response.body() != null) {
                        SenderHistoryResponse res = response.body();
                        if (res.getStatus().equals("0")) {
                            arrayList = (ArrayList<Statement>) res.getStatement();
                            listViewSender.setAdapter(new SenderHistoryAdapter(context, arrayList));
                        }
                    } else {
                        L.toastS(context, "No Response");
                    }
                }

                @Override
                public void onFailure(Call<SenderHistoryResponse> call, Throwable t) {
                    L.toastS(context, "Error : " + t.getMessage());
                    swipe_refresh.setRefreshing(false);
                }
            });
        }else {
            swipe_refresh.setRefreshing(false);
        }
    }

}
