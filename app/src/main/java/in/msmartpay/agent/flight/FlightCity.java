package in.msmartpay.agent.flight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
 * Created by Harendra on 9/8/2017.
 */

public class FlightCity extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener{
    private ListView listView;
    private SearchView sc;
    private Context context;
    private ArrayList<String> listCity;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_city_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getStringExtra("test")!=null&&getIntent().getStringExtra("test").equalsIgnoreCase("Departure")){
            setTitle("Select Departure City");
        }else {
            setTitle("Select Arrival City");
        }
        listCity = getIntent().getStringArrayListExtra("City");
        context=FlightCity.this;
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
        intent.putExtra("arrivalCity",getIntent().getStringExtra("arrivalCity"));
        intent.putExtra("classType",getIntent().getStringExtra("classType"));
        intent.putExtra("departureCity",getIntent().getStringExtra("departureCity"));
        intent.putExtra("returnDate",getIntent().getStringExtra("returnDate"));
        intent.putExtra("departDate",getIntent().getStringExtra("departDate"));
        intent.putExtra("colReturnDate",getIntent().getStringExtra("colReturnDate"));
        intent.putExtra("colDepartDate",getIntent().getStringArrayListExtra("colDepartDate"));
        intent.putExtra("adult",getIntent().getIntExtra("adult",0));
        intent.putExtra("child",getIntent().getIntExtra("child",0));
        intent.putExtra("infant",getIntent().getIntExtra("infant",0));
        intent.putExtra("total",getIntent().getIntExtra("total",0));
        setResult(2, intent);
        finish();
    }
}
