package in.msmartpayagent.dmrPaySprint;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.recyclerview.widget.RecyclerView;

import in.msmartpayagent.R;
import in.msmartpayagent.dmrPaySprint.adapter.PSBankSearchAdapter;
import in.msmartpayagent.network.model.dmr.BankListModel;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSZ on 5/18/2018.
 */

public class PSBankSearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, PSBankSearchAdapter.BankListener {

    private RecyclerView rv_bank_search;
    private SearchView sc;
    private List<BankListModel> list;
    private PSBankSearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_activity);
        setTitle("Select Bank");

        list = Util.getListFromJson(getIntent().getStringExtra("bankList"),BankListModel.class);
        rv_bank_search = findViewById(R.id.rv_bank_search);
        sc = ((SearchView)findViewById(R.id.searchView1));
        sc.setOnQueryTextListener(this);
        sc.setQueryHint("Search Bank");
        adapter = new PSBankSearchAdapter(getApplicationContext(), (ArrayList<BankListModel>) list,this);
        rv_bank_search.setAdapter(adapter);
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
        setResult(3, intent);
        hideKeyBoard(sc);
        finish();
    }


    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        int i = paramMenuItem.getItemId();
        if (i == android.R.id.home) {
            Intent intent = new Intent();
            setResult(3, intent);
            hideKeyBoard(sc);
            finish();
        }
        return true;
    }

    @Override
    public void onBankSelected(BankListModel model) {
        Intent intent = new Intent();
        intent.putExtra("bankModel", Util.getGson().toJson(model));
        setResult(2, intent);
        hideKeyBoard(sc);
        finish();
    }
}
