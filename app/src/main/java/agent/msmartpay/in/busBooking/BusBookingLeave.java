package agent.msmartpay.in.busBooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;

/**
 * Created by Harendra on 9/7/2017.
 */

public class BusBookingLeave extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private SearchView sc;
    private Context context;
    private ArrayList<String> listCity;
    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_search_leave);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getStringExtra("test")!=null&&getIntent().getStringExtra("test").equalsIgnoreCase("Leave")){
            setTitle("Select Leave City");
        }else {
            setTitle("Select Destination City");
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        listCity = getIntent().getStringArrayListExtra("City");
        context=BusBookingLeave.this;
        listView = ((ListView)findViewById(R.id.listView1));
        sc = ((SearchView)findViewById(R.id.searchView1));
        sc.setOnQueryTextListener(this);
        listView.setOnItemClickListener(this);
        arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,listCity);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.arrayAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = ((TextView)view).getText().toString();
        Intent intent = new Intent();
        intent.putExtra("cityName", str);
        intent.putExtra("leaveCity",getIntent().getStringExtra("leaveCity"));
        intent.putExtra("destinationCity",getIntent().getStringExtra("destinationCity"));
        intent.putExtra("colToDate",getIntent().getStringExtra("colToDate"));
        intent.putExtra("passenser",getIntent().getIntExtra("passenser",0));
        intent.putExtra("OriginID",getIntent().getStringExtra("OriginID"));
        intent.putExtra("DestinationID",getIntent().getStringExtra("DestinationID"));
        intent.putExtra("CitySource",getIntent().getStringArrayListExtra("CitySource"));
        intent.putExtra("CityDestination",getIntent().getStringArrayListExtra("CityDestination"));
        intent.putExtra("CityIdSource",getIntent().getStringArrayListExtra("CityIdSource"));
        intent.putExtra("CityIdDestination",getIntent().getStringArrayListExtra("CityIdDestination"));
        setResult(2, intent);
        finish();
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("leaveCity",getIntent().getStringExtra("leaveCity"));
        intent.putExtra("destinationCity",getIntent().getStringExtra("destinationCity"));
        intent.putExtra("colToDate",getIntent().getStringExtra("colToDate"));
        intent.putExtra("passenser",getIntent().getIntExtra("passenser",0));
        intent.putExtra("OriginID",getIntent().getStringExtra("OriginID"));
        intent.putExtra("DestinationID",getIntent().getStringExtra("DestinationID"));
        intent.putExtra("CitySource",getIntent().getStringArrayListExtra("CitySource"));
        intent.putExtra("CityDestination",getIntent().getStringArrayListExtra("CityDestination"));
        intent.putExtra("CityIdSource",getIntent().getStringArrayListExtra("CityIdSource"));
        intent.putExtra("CityIdDestination",getIntent().getStringArrayListExtra("CityIdDestination"));
        setResult(3, intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        int i = paramMenuItem.getItemId();
        if (i == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("leaveCity",getIntent().getStringExtra("leaveCity"));
            intent.putExtra("destinationCity",getIntent().getStringExtra("destinationCity"));
            intent.putExtra("colToDate",getIntent().getStringExtra("colToDate"));
            intent.putExtra("passenser",getIntent().getIntExtra("passenser",0));
            intent.putExtra("OriginID",getIntent().getStringExtra("OriginID"));
            intent.putExtra("DestinationID",getIntent().getStringExtra("DestinationID"));
            intent.putExtra("CitySource",getIntent().getStringArrayListExtra("CitySource"));
            intent.putExtra("CityDestination",getIntent().getStringArrayListExtra("CityDestination"));
            intent.putExtra("CityIdSource",getIntent().getStringArrayListExtra("CityIdSource"));
            intent.putExtra("CityIdDestination",getIntent().getStringArrayListExtra("CityIdDestination"));
            setResult(3, intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return true;
    }
}
