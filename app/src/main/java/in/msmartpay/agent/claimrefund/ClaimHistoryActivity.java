package in.msmartpay.agent.claimrefund;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.dmr.SenderFindRequest;
import in.msmartpay.agent.network.model.dmr.SenderHistoryResponse;
import in.msmartpay.agent.network.model.dmr.Statement;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

public class ClaimHistoryActivity extends BaseActivity {

    private Context context;
    private String agentID, txnKey;
    private RecyclerView listViewSender;
    private ProgressDialogFragment pd;
    private ArrayList<Statement> arrayList = new ArrayList<Statement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_sender_history_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("DMR Claim History");

        context = ClaimHistoryActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        listViewSender = findViewById(R.id.list_view);

        getClaimHistory();

    }

    //===========SenderHistory========================
    //For Json Request
    private void getClaimHistory() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Available Claim History...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            SenderFindRequest request = new SenderFindRequest();
            request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
            RetrofitClient.getClient(context).claimHistory(request).enqueue(new Callback<SenderHistoryResponse>() {
                @Override
                public void onResponse(Call<SenderHistoryResponse> call, retrofit2.Response<SenderHistoryResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        SenderHistoryResponse res = response.body();
                        if (res.getStatus().equals("0")) {
                            arrayList = (ArrayList<Statement>) res.getStatement();
                            listViewSender.setAdapter(new ClaimHistoryAdapter(context, arrayList));
                        }
                    } else {
                        L.toastS(context, "No Response");
                    }
                }

                @Override
                public void onFailure(Call<SenderHistoryResponse> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(context, "Error : " + t.getMessage());
                }
            });
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
