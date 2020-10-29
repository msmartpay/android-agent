package agent.msmartpay.in.flight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.L;

/**
 * Created by Harendra on 7/31/2017.
 */

public class FlightList extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView lv_flight;
    private TextView tv_price,tv_arrival,tv_departure,tv_filter,tv_date,tv_adult,tv_child,tv_infant;
    private ArrayList<AvailSegmentsModel> availSegmentsArrayList = new ArrayList<>();
    private Context context;
    private String TravelDate,Origin,Destination;
    private int infant,child,adult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flights_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = FlightList.this;

        TravelDate = getIntent().getStringExtra("TravelDate");
        Origin =getIntent().getStringExtra("Origin");
        Destination =getIntent().getStringExtra("Destination");
        infant =getIntent().getIntExtra("InfantCount",0);
        child =getIntent().getIntExtra("ChildCount",0);
        adult =getIntent().getIntExtra("AdultCount",1);
        setTitle(Origin+" - "+Destination);

        availSegmentsArrayList = (ArrayList<AvailSegmentsModel>) getIntent().getSerializableExtra("availSegmentsArrayList");
        lv_flight = (ListView) findViewById(R.id.lv_flight);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_arrival = (TextView) findViewById(R.id.tv_arrival);
        tv_departure = (TextView) findViewById(R.id.tv_departure);
        tv_filter = (TextView) findViewById(R.id.tv_filter);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_adult = (TextView) findViewById(R.id.tv_adult);
        tv_child = (TextView) findViewById(R.id.tv_child);
        tv_infant = (TextView) findViewById(R.id.tv_infant);
        tv_date.setText(TravelDate);
        tv_infant.setText(""+infant);
        tv_child.setText(""+child);
        tv_adult.setText(""+adult);

        lv_flight.setAdapter(new FlightListAdapter(context,availSegmentsArrayList,infant,child,adult));
        lv_flight.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView tv_flight_amount = (TextView) view.findViewById(R.id.tv_flight_amount);

        L.m2("tv_flight_amount",tv_flight_amount.getText().toString());

        AvailSegmentsModel availSegmentsModel = availSegmentsArrayList.get(position);
        ArrayList<AvailPaxFareDetailsModel> availPaxFareArrayList=availSegmentsModel.getAvailPaxFareDetailsModels();
        AvailPaxFareDetailsModel availPaxFareDetailsModel =  availPaxFareArrayList.get(0);
        Double GrossAmount = Double.parseDouble(availPaxFareDetailsModel.getAdultGrossAmount()) +  Double.parseDouble(availPaxFareDetailsModel.getChildGrossAmount()==null?"0.0":availPaxFareDetailsModel.getChildGrossAmount()) + Double.parseDouble(availPaxFareDetailsModel.getInfantGrossAmount()==null?"0.0":availPaxFareDetailsModel.getInfantGrossAmount());
        Intent intent = new Intent(context,PassengerDetails.class);
        intent.putExtra("availSegmentsArrayList",availSegmentsArrayList);
        intent.putExtra("TravelDate",TravelDate);
        intent.putExtra("Origin",  Origin);
        intent.putExtra("Destination", Destination);
        intent.putExtra("InfantCount", infant);
        intent.putExtra("ChildCount", child);
        intent.putExtra("AdultCount", adult);
        intent.putExtra("position",position);
        intent.putExtra("ClassCode",availPaxFareDetailsModel.getClassCode());
        intent.putExtra("TotalFare", GrossAmount+"");
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}