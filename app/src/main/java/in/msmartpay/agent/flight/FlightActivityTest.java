package in.msmartpay.agent.flight;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

/**
 * Created by Harendra on 7/27/2017.
 */
public class FlightActivityTest extends BaseActivity {
    //====Views
    private LinearLayout ll_one_way,ll_round_way;
    private RelativeLayout rl_return,rl_depart,rl_destination,rl_origin;
    private TextView tv_departure_date,tv_return_date,tv_departure_city,tv_arrival_city;
    private Spinner sp_class;
    private TextView tv_adult,tv_child,tv_infant,tv_adult_v,tv_child_v,tv_infant_v;
    private TextView tv_round_trip,tv_one_way;
    private Button btn_search_flight;
    private CardView cv_trip;
    ///===data
    private int adult=1,child=0,infant=0;
    private int total=0;
    private String tripType="";
    private SimpleDateFormat dateFormatter;
    private String colDepartDate = "", colReturnDate = "",departDate="",returnDate="",departureCity="",arrivalCity="",classType="";
    private ProgressDialog pd;
    private String url= HttpURL.FLIGHT_AVAILABILITY_REQUEST_URL;
    private Context context;
    private ArrayList<String> cityList;
    private ArrayList<AvailSegmentsModel> availSegmentsArrayList = new ArrayList<>();
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String adStr[]={"1","2","3","4","5","6","7","8","9"};
    private String chStr[]={"1","2","3","4","5","6","7","8"};
    private String inStr[]={"1","2","3","4","5","6","7","8","9"};
    private RecyclerView rv_adult;
    private TextView tv_error_sht,tv_type_b,tv_total_pess;
    private String status;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Flights Search");
        context = FlightActivityTest.this;
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        myPrefs = getSharedPreferences("flightPref", MODE_PRIVATE);
        prefsEditor = myPrefs.edit();

        //=== inisialize data's ====
       tripType="R";
        init();
        visibility();
        rl_return.setVisibility(View.VISIBLE);
        status = "AD";
        MyAdapter mAdapter = new MyAdapter(adStr,"AD");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false);
      //  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv_adult.setLayoutManager(mLayoutManager);
        rv_adult.setItemAnimator(new DefaultItemAnimator());
        rv_adult.setAdapter(mAdapter);
        Calendar newDate = Calendar.getInstance();
       tv_departure_date.setText(dateFormatter.format(newDate.getTime()));
        //=======
        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.states));
        //======
        selectDate();

       /* rl_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FlightCity.class);
                intent.putExtra("City",cityList);
                intent.putExtra("test","Leave");
                intent.putExtra("arrivalCity",arrivalCity);
                intent.putExtra("classType",classType);
                intent.putExtra("departureCity",departureCity);
                intent.putExtra("returnDate",returnDate);
                intent.putExtra("departDate",departDate);
                intent.putExtra("colReturnDate",colReturnDate);
                intent.putExtra("colDepartDate",colDepartDate);
                intent.putExtra("adult",adult);
                intent.putExtra("child",child);
                intent.putExtra("infant",infant);
                intent.putExtra("total",total);
                startActivityForResult(intent,0);
            }
        });
        rl_origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FlightCity.class);
                intent.putExtra("City",cityList);
                intent.putExtra("test","Leave");
                intent.putExtra("arrivalCity",arrivalCity);+
/               intent.putExtra("classType",classType);
                intent.putExtra("departureCity",departureCity);
                intent.putExtra("returnDate",returnDate);
                intent.putExtra("departDate",departDate);
                intent.putExtra("colReturnDate",colReturnDate);
                intent.putExtra("colDepartDate",colDepartDate);
                intent.putExtra(2587
                [
                '\'\'+adult",adult);
                intent.putExtra("child",child);
                intent.putExtra("infant",infant);
                intent.putExtra("total",total);
                startActivityForResult(intent,1);
            }
        });*/
        tv_adult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_type_b.setText("Adult  |  ");
               tv_adult.setTextColor(getResources().getColor(R.color.whitecolor));
              tv_adult.setBackground(getResources().getDrawable(R.drawable.round_layout_p));
                tv_child.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_child.setBackground(getResources().getDrawable(R.drawable.round_layout_strok));
                tv_infant.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_infant.setBackground(getResources().getDrawable(R.drawable.round_layout_strok));
                setRecyCleList("AD");
            }
        });
        tv_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adult!=9) {
                    tv_type_b.setText("Child  |  ");
                    tv_adult.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_adult.setBackground(getResources().getDrawable(R.drawable.round_layout_strok));
                    tv_child.setTextColor(getResources().getColor(R.color.whitecolor));
                    tv_child.setBackground(getResources().getDrawable(R.drawable.round_layout_p));
                    tv_infant.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_infant.setBackground(getResources().getDrawable(R.drawable.round_layout_strok));
                    setRecyCleList("CH");
                }else {
                    Toast.makeText(context,"You are already Selected max(Adult+Child) = 9 passengers", Toast.LENGTH_LONG).show();
                }
            }
        });
        tv_infant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_type_b.setText("Infant  |  ");
                tv_adult.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_adult.setBackground(getResources().getDrawable(R.drawable.round_layout_strok));
                tv_child.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_child.setBackground(getResources().getDrawable(R.drawable.round_layout_strok));
                tv_infant.setTextColor(getResources().getColor(R.color.whitecolor));
                tv_infant.setBackground(getResources().getDrawable(R.drawable.round_layout_p));
                setRecyCleList("IN");
            }
        });
        btn_search_flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSearchFlight();
            }
        });
    }

    private void requestSearchFlight() {
        SharedPreferences myPrefs =context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        L.m2("txn_key", myPrefs.getString("txn-key", null));
        try {
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("BookingType", tripType);
            jsonObject.put("Origin",  tv_departure_city.getText().toString());
            jsonObject.put("Destination", tv_arrival_city.getText().toString());
            jsonObject.put("ClassType", sp_class.getSelectedItem().toString());
            jsonObject.put("InfantCount", ""+infant);
            jsonObject.put("ChildCount", ""+child);
            jsonObject.put("AdultCount", ""+adult);
            jsonObject.put("TravelDate",tv_departure_date.getText().toString());
            jsonObject.put("ResidentofIndia","1");
            if(cv_trip.getVisibility()== View.VISIBLE) {
                jsonObject.put("ReturnDate", tv_return_date.getText().toString());
            }else {
                jsonObject.put("ReturnDate", "");
            }
            L.m2("called url", url);
            L.m2("request", jsonObject.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url,jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            try {
                                pd.dismiss();
                                L.m2("response", data.toString());
                                if (data.getString("status") != null && data.getString("status").equals("0")) {
                                    JSONArray AvailabilityOutputJSONArray =data.getJSONArray("AvailabilityOutput");
                                    JSONObject AvailabilityOutputJSONObject = AvailabilityOutputJSONArray.getJSONObject(0);
                                    prefsEditor.putString("UserTrackId",AvailabilityOutputJSONObject.getString("UserTrackId"));
                                    prefsEditor.commit();
                                    JSONObject AvailabilityOutput = AvailabilityOutputJSONObject.getJSONObject("AvailabilityOutput");
                                    JSONArray OngoingFlightsJSONArray =AvailabilityOutput.getJSONArray("OngoingFlights");
                                    for(int i=0;i<OngoingFlightsJSONArray.length();i++){
                                        JSONObject OngoingFlightsJSONObject = OngoingFlightsJSONArray.getJSONObject(i);
                                        JSONArray AvailSegmentsJSONArray =OngoingFlightsJSONObject.getJSONArray("AvailSegments");
                                        for(int j=0;j<AvailSegmentsJSONArray.length();j++){
                                            JSONObject AvailSegmentsJSONObject = AvailSegmentsJSONArray.getJSONObject(j);
                                            AvailSegmentsModel availSegmentsModel = new AvailSegmentsModel();
                                            availSegmentsModel.setAirCraftType(AvailSegmentsJSONObject.getString("AirCraftType"));
                                            availSegmentsModel.setNumberofStops(AvailSegmentsJSONObject.getInt("NumberofStops")+"");
                                            availSegmentsModel.setOriginAirportTerminal(AvailSegmentsJSONObject.getString("OriginAirportTerminal"));
                                            availSegmentsModel.setArrivalDateTime(AvailSegmentsJSONObject.getString("ArrivalDateTime"));
                                            availSegmentsModel.setDepartureDateTime(AvailSegmentsJSONObject.getString("DepartureDateTime"));
                                            availSegmentsModel.setDuration(AvailSegmentsJSONObject.getString("Duration"));
                                            availSegmentsModel.setFlightNumber(AvailSegmentsJSONObject.getString("FlightNumber"));
                                            availSegmentsModel.setCurrency_Conversion_Rate(AvailSegmentsJSONObject.getString("Currency_Conversion_Rate"));
                                            availSegmentsModel.setVia(AvailSegmentsJSONObject.getString("Via"));
                                            availSegmentsModel.setFlightId(AvailSegmentsJSONObject.getString("FlightId"));
                                            availSegmentsModel.setOrigin(AvailSegmentsJSONObject.getString("Origin"));
                                            availSegmentsModel.setSupplierId(AvailSegmentsJSONObject.getString("SupplierId"));
                                            availSegmentsModel.setCurrencyCode(AvailSegmentsJSONObject.getString("CurrencyCode"));
                                            availSegmentsModel.setDestination(AvailSegmentsJSONObject.getString("Destination"));
                                            availSegmentsModel.setAirlineCode(AvailSegmentsJSONObject.getString("AirlineCode"));
                                            availSegmentsModel.setDestinationAirportTerminal(AvailSegmentsJSONObject.getString("DestinationAirportTerminal"));

                                            ArrayList<AvailPaxFareDetailsModel> AvailPaxFareDetailsArrayList = new ArrayList<>();
                                            JSONArray availPaxFareDetailsJSONArray =AvailSegmentsJSONObject.getJSONArray("AvailPaxFareDetails");
                                            for(int k=0;k<availPaxFareDetailsJSONArray.length();k++) {
                                                AvailPaxFareDetailsModel availPaxFareDetailsModel = new AvailPaxFareDetailsModel();
                                                JSONObject availPaxFareDetailsJSONObject = availPaxFareDetailsJSONArray.getJSONObject(k);
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setChangePenalty(availPaxFareDetailsJSONObject.getString("ChangePenalty"));
                                                availPaxFareDetailsModel.setCancellationCharges(availPaxFareDetailsJSONObject.getString("CancellationCharges"));
                                                availPaxFareDetailsModel.setClassCodeDesc(availPaxFareDetailsJSONObject.getString("ClassCodeDesc"));

                                                JSONObject jsonObjectAdult = availPaxFareDetailsJSONObject.getJSONObject("Adult");
                                                if(jsonObjectAdult.length()!=0){
                                                    availPaxFareDetailsModel.setAdultTotalTaxAmount(jsonObjectAdult.getString("TotalTaxAmount"));
                                                    availPaxFareDetailsModel.setAdultCommission(jsonObjectAdult.getString("Commission"));
                                                    availPaxFareDetailsModel.setAdultBasicAmount(jsonObjectAdult.getString("BasicAmount"));
                                                    availPaxFareDetailsModel.setAdultFareType(jsonObjectAdult.getString("FareType"));
                                                    availPaxFareDetailsModel.setAdultGrossAmount(jsonObjectAdult.getString("GrossAmount"));
                                                    availPaxFareDetailsModel.setAdultFareBasis(jsonObjectAdult.getString("FareBasis"));
                                                    if(jsonObjectAdult.has("YQ"))
                                                    availPaxFareDetailsModel.setAdultYQ(jsonObjectAdult.getString("YQ"));
                                                    else
                                                        availPaxFareDetailsModel.setAdultYQ("0");
                                                    HashMap<String,String> hashMapTaxAdult = new HashMap<>();
                                                    JSONArray jSONArrayTaxAdult = jsonObjectAdult.getJSONArray("TaxDetails");
                                                    for (int l=0;l<jSONArrayTaxAdult.length();l++) {
                                                        JSONObject jsonObject1 = jSONArrayTaxAdult.getJSONObject(l);
                                                        hashMapTaxAdult.put(jsonObject1.getString("Description"), jsonObject1.getString("Amount"));
                                                    }
                                                    availPaxFareDetailsModel.setAdultTaxDetails(hashMapTaxAdult);
                                                }
                                                JSONObject jsonObjectChild = availPaxFareDetailsJSONObject.getJSONObject("Child");
                                                if(jsonObjectChild.length()!=0){
                                                    availPaxFareDetailsModel.setChildTotalTaxAmount(jsonObjectChild.getString("TotalTaxAmount"));
                                                    availPaxFareDetailsModel.setChildCommission(jsonObjectChild.getString("Commission"));
                                                    availPaxFareDetailsModel.setChildBasicAmount(jsonObjectChild.getString("BasicAmount"));
                                                    availPaxFareDetailsModel.setChildFareType(jsonObjectChild.getString("FareType"));
                                                    availPaxFareDetailsModel.setChildGrossAmount(jsonObjectChild.getString("GrossAmount"));
                                                    availPaxFareDetailsModel.setChildFareBasis(jsonObjectChild.getString("FareBasis"));
                                                    if(jsonObjectChild.has("YQ"))
                                                        availPaxFareDetailsModel.setAdultYQ(jsonObjectChild.getString("YQ"));
                                                    else
                                                        availPaxFareDetailsModel.setAdultYQ("0");

                                                    HashMap<String,String> hashMapTaxChild = new HashMap<>();
                                                    JSONArray jSONArrayTaxChild = jsonObjectChild.getJSONArray("TaxDetails");
                                                    for (int l=0;l<jSONArrayTaxChild.length();l++) {
                                                        JSONObject jsonObject1 = jSONArrayTaxChild.getJSONObject(l);
                                                        hashMapTaxChild.put(jsonObject1.getString("Description"), jsonObject1.getString("Amount"));
                                                    }
                                                    availPaxFareDetailsModel.setChildTaxDetails(hashMapTaxChild);
                                                }
                                                JSONObject jsonObjectInfant = availPaxFareDetailsJSONObject.getJSONObject("Infant");
                                                if(jsonObjectInfant.length()!=0){
                                                    availPaxFareDetailsModel.setInfantTotalTaxAmount(jsonObjectInfant.getString("TotalTaxAmount"));
                                                    availPaxFareDetailsModel.setInfantCommission(jsonObjectInfant.getString("Commission"));
                                                    availPaxFareDetailsModel.setInfantBasicAmount(jsonObjectInfant.getString("BasicAmount"));
                                                    availPaxFareDetailsModel.setInfantFareType(jsonObjectInfant.getString("FareType"));
                                                    availPaxFareDetailsModel.setInfantGrossAmount(jsonObjectInfant.getString("GrossAmount"));
                                                    availPaxFareDetailsModel.setInfantFareBasis(jsonObjectInfant.getString("FareBasis"));
                                                    if(jsonObjectInfant.has("YQ"))
                                                        availPaxFareDetailsModel.setAdultYQ(jsonObjectInfant.getString("YQ"));
                                                    else
                                                        availPaxFareDetailsModel.setAdultYQ("0");

                                                    HashMap<String,String> hashMapTaxInfant = new HashMap<>();
                                                    JSONArray jSONArrayTaxInfant = jsonObjectInfant.getJSONArray("TaxDetails");
                                                    for (int l=0;l<jSONArrayTaxInfant.length();l++) {
                                                        JSONObject jsonObject1 = jSONArrayTaxInfant.getJSONObject(l);
                                                        hashMapTaxInfant.put(jsonObject1.getString("Description"), jsonObject1.getString("Amount"));
                                                    }
                                                    availPaxFareDetailsModel.setInfantTaxDetails(hashMapTaxInfant);
                                                }
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));
                                                availPaxFareDetailsModel.setClassCode(availPaxFareDetailsJSONObject.getString("ClassCode"));


                                                AvailPaxFareDetailsArrayList.add(availPaxFareDetailsModel);
                                            }
                                            availSegmentsModel.setAvailPaxFareDetailsModels(AvailPaxFareDetailsArrayList);
                                            availSegmentsArrayList.add(availSegmentsModel);
                                        }
                                    }
                                    L.m2("test",availSegmentsArrayList.toString());
                                    /*prefsEditor.putString("TravelDate",tv_departure_date.getText().toString());
                                    prefsEditor.putString("Origin",tv_departure_city.getText().toString());
                                    prefsEditor.putString("Destination",tv_arrival_city.getText().toString());
                                    prefsEditor.putInt("InfantCount",infant);
                                    prefsEditor.putInt("ChildCount",child);
                                    prefsEditor.putInt("AdultCount",adult);*/
                                    Intent intent = new Intent(context,FlightList.class);
                                    intent.putExtra("availSegmentsArrayList",availSegmentsArrayList);
                                    intent.putExtra("TravelDate",tv_departure_date.getText().toString());
                                    intent.putExtra("Origin",  tv_departure_city.getText().toString());
                                     intent.putExtra("Destination", tv_arrival_city.getText().toString());
                                    intent.putExtra("InfantCount", infant);
                                    intent.putExtra("ChildCount", child);
                                    intent.putExtra("AdultCount", adult);
                                    startActivity(intent);
                                }else if (data.getString("status") != null && data.getString("status").equals("1")) {
                                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context,"Unable to process your request", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, " "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectDate() {

        rl_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        try {
                            colDepartDate = dateFormatter.format(newDate.getTime());
                            if (!colDepartDate.equalsIgnoreCase("")) {

                                tv_departure_date.setText(colDepartDate);
                            } else {
                                Toast.makeText(context, "Invalid From Date.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDatePickerDialog.show();
            }
        });
        rl_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_departure_date.getText())){
                    Calendar newCalendar = Calendar.getInstance();
                    DatePickerDialog toDatePickerDialog = new DatePickerDialog(FlightActivityTest.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            colReturnDate = dateFormatter.format(newDate.getTime());
                            Log.v(">>>>>>>>>>>>>>> ", tv_departure_date.getText() + " - " + colReturnDate);
                            if (!tv_departure_date.getText().equals("") && !colReturnDate.equals("")) {
                                try {
                                    colDepartDate = tv_departure_date.getText().toString();
                                    Date fdate = dateFormatter.parse(colDepartDate);
                                    Date todate = dateFormatter.parse(colReturnDate);

                                    if (fdate.compareTo(todate) > 0) {
                                        tv_return_date.setText("");
                                        Toast.makeText(context, "Depart date should be lesser than Return Date.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        tv_return_date.setText(colReturnDate);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.v("Exception", "Execption in Fund transfer");
                                    e.printStackTrace();
                                    Toast.makeText(context, "Something went wrong3.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                tv_return_date.setText(colReturnDate);
                                tv_departure_date.setText(colReturnDate);
                                // Toast.makeText(FlightActivity.this, "Please select first Deapart date!",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    toDatePickerDialog.show();
            }else{
                Toast.makeText(context, "Please select first departure date!", Toast.LENGTH_SHORT).show();
            }
                }

        });
    }

    private void visibility() {
        tripType="O";
        rl_return.setVisibility(View.INVISIBLE);
         ll_one_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripType="O";
                ll_one_way.setBackground(getResources().getDrawable(R.drawable.round_layout_p));
                ll_round_way.setBackground(getResources().getDrawable(R.drawable.round_layout_w));
                cv_trip.setVisibility(View.GONE);
                tv_one_way.setTextColor(getResources().getColor(R.color.whitecolor));
                tv_round_trip.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        ll_round_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripType="R";
                ll_one_way.setBackground(getResources().getDrawable(R.drawable.round_layout_w));
                ll_round_way.setBackground(getResources().getDrawable(R.drawable.round_layout_p));
                tv_one_way.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_round_trip.setTextColor(getResources().getColor(R.color.whitecolor));
                cv_trip.setVisibility(View.VISIBLE);
                tv_return_date.setText("Choose Date");
            }
        });
    }
/*
    private void infantVlaue() {
        iv_infant_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(infant>0){
                    infant--;
                    tv_infant.setText(""+infant);
                }
            }
        });
        iv_infant_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adult>infant){
                    infant++;
                    tv_infant.setText(""+infant);
                }
            }
        });
    }

    private void childValue() {

        iv_child_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (child > 0) {
                    child--;
                    tv_child.setText("" + child);
                }
            }
        });

        iv_child_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total =adult+child;
                if(total<9){
                    child++;
                    tv_child.setText(""+child);
                }
            }
        });
    }

    private void adultValue() {
        iv_adult_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adult>1){
                    adult--;
                    tv_adult.setText(""+adult);
                    if(infant>adult){
                        infant--;
                        tv_infant.setText(""+infant);
                    }
                }
            }
        });
        iv_adult_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total =adult+child;
                if(total<9){
                    adult++;
                    tv_adult.setText("" + adult);
                }
            }
        });
    }*/

    private void init() {
        //===Spinner
        sp_class =  findViewById(R.id.sp_class);
         //==CardView
        cv_trip = (CardView) findViewById(R.id.cv_trip);
        //===LinearLayout
        ll_one_way = (LinearLayout) findViewById(R.id.ll_one_way);
        ll_round_way = (LinearLayout) findViewById(R.id.ll_round_way);

        //==RelativeLayout
        rl_return = (RelativeLayout) findViewById(R.id.rl_return);
        rl_depart = (RelativeLayout) findViewById(R.id.rl_depart);

        //====AutoCompleteTextView
        rl_destination = (RelativeLayout) findViewById(R.id.rl_destination);
        rl_origin = (RelativeLayout) findViewById(R.id.rl_origin);

     //   et_tot_passenger =  findViewById(R.id.et_tot_passenger);
        //====TextView
        tv_adult=(TextView) findViewById(R.id.tv_adult);
        tv_child=(TextView) findViewById(R.id.tv_child);
        tv_infant=(TextView) findViewById(R.id.tv_infant);
        tv_adult_v=(TextView) findViewById(R.id.tv_adult_v);
        tv_child_v=(TextView) findViewById(R.id.tv_child_v);
        tv_infant_v=(TextView) findViewById(R.id.tv_infant_v);
        tv_departure_date=(TextView) findViewById(R.id.tv_departure_date);
        tv_return_date=(TextView) findViewById(R.id.tv_return_date);
        tv_departure_city=(TextView) findViewById(R.id.tv_departure_city);
        tv_arrival_city=(TextView) findViewById(R.id.tv_arrival_city);
        tv_one_way=(TextView) findViewById(R.id.tv_one_way);
        tv_round_trip=(TextView) findViewById(R.id.tv_round_trip);

        rv_adult = (RecyclerView) findViewById(R.id.rv_adult);
        tv_error_sht = (TextView)findViewById(R.id.tv_error_sht);
        tv_type_b= (TextView)findViewById(R.id.tv_type_b);
        tv_total_pess= (TextView)findViewById(R.id.tv_total_pess);
        //===Button
        btn_search_flight= findViewById(R.id.btn_search_flight);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        colDepartDate = data.getStringExtra("colDepartDate");
        colReturnDate = data.getStringExtra("colReturnDate");
        departDate = data.getStringExtra("departDate");
        returnDate = data.getStringExtra("returnDate");
        departureCity = data.getStringExtra("departureCity");
        arrivalCity = data.getStringExtra("arrivalCity");
        classType = data.getStringExtra("classType");
        adult = data.getIntExtra("adult",0);
        child = data.getIntExtra("child",0);
        infant = data.getIntExtra("infant",0);
        total = data.getIntExtra("total",0);
        /*switch (requestCode){
            case 0:
                if(resultCode==2){
                    leaveCity = data.getStringExtra("cityName");
                    et_leave.setText(leaveCity);
                    L.m2("city source--1>", CitySource.toString());
                    for(int i=0; i<CitySource.size(); i++){
                        if(leaveCity.equalsIgnoreCase(CitySource.get(i))){
                            OriginID = CityIdSource.get(i).toString();
                            L.m2("check_OrginId--2>", OriginID.toString());
                            break;
                        }
                    }
                    getDestinationRequest(OriginID);
                }
                break;
            case 1:
                if(resultCode==2){

                    destinationCity = data.getStringExtra("cityName");
                    et_destination.setText(destinationCity);
                    for(int i=0; i<CityDestination.size(); i++){
                        if(destinationCity.equalsIgnoreCase(CityDestination.get(i))){
                            DestinationID = CityIdDestination.get(i).toString();
                            L.m2("check_DestinationID-->", DestinationID.toString());
                            break;
                        }
                    }
                }
                break;
        }*/
    }

    private void bottomDialog(int ad,int ch,int in,String status){
        String str="Adult-"+ad+" Child-"+ch+" Infant-"+in;

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = getLayoutInflater().inflate(R.layout.flight_passenger_bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private String[] list;
        private String type;

        public MyAdapter(String[]list, String type) {
            this.list = list;
            this.type = type;
        }
        public class MyHolder  extends RecyclerView.ViewHolder {
            public TextView tv_count;

            public MyHolder (View view) {
                super(view);
                tv_count = (TextView) view.findViewById(R.id.tv_count);

            }
        }
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.flight_bottom_sheet, parent, false);
            return new MyHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, int position) {
            if(type.equalsIgnoreCase("IN")) {
                if(position<adult) {
                    holder.tv_count.setText(list[position]);
                    if(position==(infant-1)) {
                        holder.tv_count.setBackground(getResources().getDrawable(R.drawable.tv_border_p));
                        holder.tv_count.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else {
                        holder.tv_count.setBackground(getResources().getDrawable(R.drawable.tv_border_h));
                        holder.tv_count.setTextColor(getResources().getColor(R.color.hintcolor));
                    }
                }
                else {
                    holder.tv_count.setVisibility(View.GONE);
                }

            }else if(type.equalsIgnoreCase("CH")) {
                if(position<9-adult) {
                    holder.tv_count.setText(list[position]);
                    if(position==(child-1)) {
                        holder.tv_count.setBackground(getResources().getDrawable(R.drawable.tv_border_p));
                        holder.tv_count.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }else {
                        holder.tv_count.setBackground(getResources().getDrawable(R.drawable.tv_border_h));
                        holder.tv_count.setTextColor(getResources().getColor(R.color.hintcolor));
                    }
                }
                else {
                    holder.tv_count.setVisibility(View.GONE);
                }

            }else {
                holder.tv_count.setText(list[position]);
                if(position<9-child){
                if(position==(adult-1)) {
                    holder.tv_count.setBackground(getResources().getDrawable(R.drawable.tv_border_p));
                    holder.tv_count.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else {
                    holder.tv_count.setBackground(getResources().getDrawable(R.drawable.tv_border_h));
                    holder.tv_count.setTextColor(getResources().getColor(R.color.hintcolor));
                }
                }else{
                    holder.tv_count.setVisibility(View.GONE);
                }
            }

            holder.tv_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int dummy= Integer.parseInt( holder.tv_count.getText().toString());
                    if(type.equalsIgnoreCase("AD")){
                        if((dummy + child )<=9) {
                            adult=dummy;
                            tv_adult_v.setText(adult + "");
                        }else {
                            Toast.makeText(context,"You are already Selected max(Adult+Child) = 9 passengers", Toast.LENGTH_LONG).show();
                        }
                         }
                    if(type.equalsIgnoreCase("CH")){
                        if((adult + dummy )<=9) {
                            child = dummy;
                            tv_child_v.setText(child + "");
                        }else {
                            Toast.makeText(context,"You are already Selected max(Adult+Child) = 9 passengers", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(type.equalsIgnoreCase("IN")){
                            infant = dummy;
                            tv_infant_v.setText(infant + "");
                    }
                    tv_total_pess.setText((adult + child + infant) + "");
                    setRecyCleList(type);
                }
            });

        }
        @Override
        public int getItemCount() {
            return list.length;
        }
    }
    private void setRecyCleList(String type){
        MyAdapter mAdapter=null;
        RecyclerView.LayoutManager mLayoutManager=null;
        if(type.equalsIgnoreCase("AD"))
         mAdapter = new MyAdapter(adStr, type);
        mLayoutManager = new GridLayoutManager(context, 6, GridLayoutManager.VERTICAL, false);
        if(type.equalsIgnoreCase("CH"))
            mAdapter = new MyAdapter(chStr, type);
       mLayoutManager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        if(type.equalsIgnoreCase("IN"))
            mAdapter = new MyAdapter(inStr, type);
        mLayoutManager = new GridLayoutManager(context, 6, GridLayoutManager.VERTICAL, false);

        //  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv_adult.setLayoutManager(mLayoutManager);
        rv_adult.setItemAnimator(new DefaultItemAnimator());
        rv_adult.setAdapter(mAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pnr_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(i == R.id.pnr_status){
            Intent intent = new Intent(context, FlightPnrStatusActivity.class);
            startActivity(intent);
        }
        if(i==android.R.id.home){
            finish();
        }
        return true;
    }

}
