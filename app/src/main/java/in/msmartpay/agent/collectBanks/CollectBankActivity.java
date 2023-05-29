package in.msmartpay.agent.collectBanks;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.BankCollectResponse;
import in.msmartpay.agent.network.model.MainRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CollectBankActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView rv_list_bank;
    private String txnKey, agent_id;
    private Context context;
    private ProgressDialogFragment pd;
   private ArrayList<CollectBankModel> bankList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_collect_activity);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        rv_list_bank = findViewById(R.id.rv_list_bank);

        getSupportActionBar().setTitle("Bank Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = CollectBankActivity.this;

        agent_id = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        getBankDetails();
    }

    private void getBankDetails()  {

        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching Bank Details...");
            ProgressDialogFragment.showDialog(pd,getSupportFragmentManager());


            MainRequest request = new MainRequest();
            request.setAgentID(agent_id);
            request.setTxn_key(txnKey);

            RetrofitClient.getClient(getApplicationContext())
                    .getBankDetails(request).enqueue(new Callback<BankCollectResponse>() {
                @Override
                public void onResponse(@NotNull Call<BankCollectResponse> call, @NotNull retrofit2.Response<BankCollectResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        BankCollectResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            if (bankList == null)
                                bankList = new ArrayList<>();
                            else
                                bankList.clear();

                            if (res.getBank_List()!=null && res.getBank_List().size()>0)
                            bankList = (ArrayList<CollectBankModel>) res.getBank_List();
                            CollectBankAdapter adapterCurrent = new CollectBankAdapter(getApplicationContext(), bankList);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
                            rv_list_bank.setLayoutManager(layoutManager);
                            rv_list_bank.setItemAnimator(new DefaultItemAnimator());
                            rv_list_bank.setAdapter(adapterCurrent);
                        } else {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BankCollectResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {

        return super.onSupportNavigateUp();
    }
}
