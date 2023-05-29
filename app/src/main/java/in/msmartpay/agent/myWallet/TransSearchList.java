package in.msmartpay.agent.myWallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.myWallet.adapter.StatementListAdapter;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.wallet.TransactionItems;
import in.msmartpay.agent.network.model.wallet.WalletHistoryResponse;
import in.msmartpay.agent.network.model.wallet.WalletSearchHistoryRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class TransSearchList extends BaseActivity {
    private RecyclerView transactionList;
    private EditText et_search;
    private ProgressDialogFragment pd;
    private Context context = null;
    public ArrayList<TransactionItems> tranList = new ArrayList<TransactionItems>();

    private String connesctionNo = null;
    private String txn_key = "", agentID;

    private StatementListAdapter adaptor;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_search_list);
        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction History");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TransSearchList.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        connesctionNo = getIntent().getExtras().getString("connNo");

        transactionList = findViewById(R.id.id_transactionList);
        et_search = findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adaptor!=null)
                    adaptor.getFilter2().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getTransList();
    }

    private void getTransList(){
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())){
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching Transactions...");
            ProgressDialogFragment.showDialog(pd,getSupportFragmentManager());

            WalletSearchHistoryRequest request = new WalletSearchHistoryRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setConnection_no(connesctionNo);

            RetrofitClient.getClient(getApplicationContext())
                    .searchWalletHistory(request).enqueue(new Callback<WalletHistoryResponse>() {
                @Override
                public void onResponse(@NotNull Call<WalletHistoryResponse> call, @NotNull retrofit2.Response<WalletHistoryResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        WalletHistoryResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            if (tranList == null)
                                tranList = new ArrayList<>();
                            else
                                tranList.clear();
                            if (res.getStatement()!=null && res.getStatement().size()>0) {
                                tranList = (ArrayList<TransactionItems>) res.getStatement();
                                adaptor = new StatementListAdapter(tranList);
                                transactionList.setAdapter(adaptor);
                            }else {
                                L.toastS(getApplicationContext(), "No Data");
                            }
                        } else {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<WalletHistoryResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failure " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
