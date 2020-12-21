package in.msmartpay.agent.dmr1MoneyTransfer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;

/**
 * Created by SSZ on 5/18/2018.
 */

public class Bank_name_search extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private SearchView sc;
    private Context context;
    private ArrayList<String> listBankname;
    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_name_search);
        setTitle("Search");



        listBankname = getIntent().getStringArrayListExtra("Bankname");
        context=Bank_name_search.this;
        listView = ((ListView)findViewById(R.id.listView1));
        sc = ((SearchView)findViewById(R.id.searchView1));
        sc.setOnQueryTextListener(this);
        listView.setOnItemClickListener(this);
        arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,listBankname);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String str = ((TextView)view).getText().toString();
        Intent intent = new Intent();
        intent.putExtra("Bankname", str);
        /*intent.putExtra("leaveCity",getIntent().getStringExtra("leaveCity"));
        intent.putExtra("destinationCity",getIntent().getStringExtra("destinationCity"));
        intent.putExtra("colToDate",getIntent().getStringExtra("colToDate"));
        intent.putExtra("passenser",getIntent().getIntExtra("passenser",1));
        intent.putExtra("OriginID",getIntent().getStringExtra("OriginID"));
        intent.putExtra("DestinationID",getIntent().getStringExtra("DestinationID"));
        intent.putExtra("CitySource",getIntent().getStringArrayListExtra("CitySource"));
        intent.putExtra("CityDestination",getIntent().getStringArrayListExtra("CityDestination"));
        intent.putExtra("CityIdSource",getIntent().getStringArrayListExtra("CityIdSource"));
        intent.putExtra("CityIdDestination",getIntent().getStringArrayListExtra("CityIdDestination"));*/
        setResult(2, intent);
        hideKeyBoard(sc);
        finish();

    }

    public boolean onQueryTextChange(String paramString) {
        arrayAdapter.getFilter().filter(paramString);
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
}
