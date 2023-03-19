package in.msmartpayagent.rechargeBillPay.operator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.recyclerview.widget.RecyclerView;

import in.msmartpayagent.R;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.OperatorsRequest;
import in.msmartpayagent.network.model.OperatorsResponse;
import in.msmartpayagent.network.model.wallet.OperatorModel;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

;

/**
 * Created by SSZ on 5/18/2018.
 */

public class OperatorSearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, OperatorAdapter.OperatorListener {

    private RecyclerView rv_bank_search;
    private SearchView searchView;
    private List<OperatorModel> list = new ArrayList<>();
    private OperatorAdapter adapter;
    private ProgressDialogFragment pd;
    private String txn_key,agentID,service;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");
        agentID = Util.LoadPrefData(getApplicationContext(),Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(),Keys.TXN_KEY);

        rv_bank_search = findViewById(R.id.rv_bank_search);
        searchView = findViewById(R.id.searchView1);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Select Operator");

        Intent intent= getIntent();
        if (intent!=null && intent.hasExtra(Keys.ARRAY_LIST)){
            list = Util.getListFromJson(intent.getStringExtra(Keys.ARRAY_LIST), OperatorModel.class);
            service = intent.getStringExtra(Keys.Service_OP);
            if (list!=null && list.size()>0){
            }else {
                list = new ArrayList<>();
            }
        }else {
            list = new ArrayList<>();
        }


        adapter = new OperatorAdapter(list,this);
        rv_bank_search.setAdapter(adapter);
        if(list.size()==0){
            operatorsCodeRequest();
        }
    }

    public boolean onQueryTextChange(String paramString) {
        adapter.getFilter().filter(paramString);
        return false;
    }
    @Override
    public boolean onQueryTextSubmit(String paramString) {
        return false;
    }
    public void onBackPressed() {
        Intent intent = new Intent();
        //intent.putExtra("Bankname", "");
        setResult(RESULT_CANCELED, intent);
        finish();
    }


    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        int i = paramMenuItem.getItemId();
        if (i == android.R.id.home) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        return true;
    }

    //===========operatorCodeRequest==============
    private void operatorsCodeRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Operators...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            OperatorsRequest request = new OperatorsRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService(service);

            RetrofitClient.getClient(getApplicationContext())
                    .operators(request).enqueue(new Callback<OperatorsResponse>() {
                @Override
                public void onResponse(@NotNull Call<OperatorsResponse> call, @NotNull retrofit2.Response<OperatorsResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        OperatorsResponse res = response.body();
                        if (res.getResponseCode().equalsIgnoreCase("0")){
                            list = res.getData();
                            adapter.notifyDataSetChanged();
                        }else {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    }else {

                    }
                }

                @Override
                public void onFailure(@NotNull Call<OperatorsResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }


    @Override
    public void onOperatorSelected(OperatorModel model) {
        Intent intent = new Intent();
        intent.putExtra(Keys.OBJECT, Util.getGson().toJson(model));
        intent.putExtra(Keys.ARRAY_LIST, Util.getGson().toJson(list));
        setResult(RESULT_OK, intent);
        finish();
    }
}
