package agent.msmartpay.in.flight;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

/**
 * Created by Harendra on 7/27/2017.
 */
public class FlightActivity extends BaseActivity {
    private LinearLayout ll_one_way,ll_round_way;
    private RelativeLayout rl_return,rl_depart;
    private View v_one_way,v_round_way;
    private EditText et_departure,et_arrival,et_depart,et_return;
    private AutoCompleteTextView actv_class;
    private TextView tv_adult,tv_child,tv_infant;
    private ImageView iv_adult_min,iv_adult_plus,iv_child_min,iv_child_plus,iv_infant_min,iv_infant_plus;
    private CheckBox cb_fare;
    private RadioGroup rg;
    private RadioButton rb;
    private Button btn_search_flight;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_search_h);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Flights");
        context = FlightActivity.this;
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        myPrefs = getSharedPreferences("flightPref", MODE_PRIVATE);
        prefsEditor = myPrefs.edit();

        //=== inisialize data's ====
        init();
        visibility();
        adultValue();
        childValue();
        infantVlaue();
        rl_return.setVisibility(View.INVISIBLE);
        //=======
       /* ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.states));
        actv_from_city.setAdapter(adapterCity);
        actv_from_city.setThreshold(1);
        actv_to_city.setAdapter(adapterCity);
        actv_to_city.setThreshold(1);
        */
        ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.flight_class));
        actv_class.setAdapter(adapterClass);
        actv_class.setThreshold(1);
        //======
        selectDate();
        et_departure.setText("MAA");
        et_arrival.setText("BLR");
       /* et_departure.setOnClickListener(new View.OnClickListener() {
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
        et_arrival.setOnClickListener(new View.OnClickListener() {
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
                startActivityForResult(intent,1);
            }
        });*/

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
        /*int id=rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(id);
        String typeN=rb.getText().toString();
        if(typeN.equalsIgnoreCase("Domestic")){

        }else {

        }*/
        try {
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("BookingType", tripType);
            jsonObject.put("Origin",  et_departure.getText().toString());
            jsonObject.put("Destination", et_arrival.getText().toString());
            jsonObject.put("ClassType", actv_class.getText().toString());
            jsonObject.put("InfantCount", ""+infant);
            jsonObject.put("ChildCount", ""+child);
            jsonObject.put("AdultCount", ""+adult);
            jsonObject.put("TravelDate",et_depart.getText().toString());
            jsonObject.put("ResidentofIndia","1");
            if(et_return.getVisibility()== View.VISIBLE) {
                jsonObject.put("ReturnDate", et_return.getText().toString());
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
                                    /*prefsEditor.putString("TravelDate",et_depart.getText().toString());
                                    prefsEditor.putString("Origin",et_departure.getText().toString());
                                    prefsEditor.putString("Destination",et_arrival.getText().toString());
                                    prefsEditor.putInt("InfantCount",infant);
                                    prefsEditor.putInt("ChildCount",child);
                                    prefsEditor.putInt("AdultCount",adult);*/
                                    Intent intent = new Intent(context,FlightList.class);
                                    intent.putExtra("availSegmentsArrayList",availSegmentsArrayList);
                                    intent.putExtra("TravelDate",et_depart.getText().toString());
                                    intent.putExtra("Origin",  et_departure.getText().toString());
                                    intent.putExtra("Destination", et_arrival.getText().toString());
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

        et_depart.setOnClickListener(new View.OnClickListener() {
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

                                et_depart.setText(colDepartDate);
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
        et_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_depart.getText())){
                    Calendar newCalendar = Calendar.getInstance();
                    DatePickerDialog toDatePickerDialog = new DatePickerDialog(FlightActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            colReturnDate = dateFormatter.format(newDate.getTime());
                            Log.v(">>>>>>>>>>>>>>> ", et_depart.getText() + " - " + colReturnDate);
                            if (!et_depart.getText().equals("") && !colReturnDate.equals("")) {
                                try {
                                    colDepartDate = et_depart.getText().toString();
                                    Date fdate = dateFormatter.parse(colDepartDate);
                                    Date todate = dateFormatter.parse(colReturnDate);

                                    if (fdate.compareTo(todate) > 0) {
                                        et_return.setText("");
                                        Toast.makeText(context, "Depart date should be lesser than Return Date.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        et_return.setText(colReturnDate);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.v("Exception", "Execption in Fund transfer");
                                    e.printStackTrace();
                                    Toast.makeText(context, "Something went wrong3.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                et_return.setText(colReturnDate);
                                et_depart.setText(colReturnDate);
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
        v_one_way.setBackgroundColor(getResources().getColor(R.color.whitecolor));
        v_round_way.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ll_one_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripType="O";
                rl_return.setVisibility(View.INVISIBLE);
                v_one_way.setBackgroundColor(getResources().getColor(R.color.whitecolor));
                v_round_way.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        ll_round_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripType="R";
                rl_return.setVisibility(View.VISIBLE);
                v_one_way.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                v_round_way.setBackgroundColor(getResources().getColor(R.color.whitecolor));
                et_return.setText("");
            }
        });
    }

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
    }

    private void init() {

        //===RadioGroup
       // rg=(RadioGroup) findViewById(R.id.rg);

        //===LinearLayout
        ll_one_way = (LinearLayout) findViewById(R.id.ll_one_way);
        ll_round_way = (LinearLayout) findViewById(R.id.ll_round_way);

        //==RelativeLayout
        rl_return = (RelativeLayout) findViewById(R.id.rl_return);
        rl_depart = (RelativeLayout) findViewById(R.id.rl_depart);

        //== View
        v_one_way= (View) findViewById(R.id.v_one_way);
        v_round_way= (View) findViewById(R.id.v_round_way);

        //====AutoCompleteTextView
        et_depart =  findViewById(R.id.et_depart);
        et_return =  findViewById(R.id.et_return);
        et_departure =  findViewById(R.id.et_departure);
        et_arrival =  findViewById(R.id.et_arrival);
        actv_class= (AutoCompleteTextView) findViewById(R.id.actv_class);

        //====TextView
        tv_adult=(TextView) findViewById(R.id.tv_adult);
        tv_child=(TextView) findViewById(R.id.tv_child);
        tv_infant=(TextView) findViewById(R.id.tv_infant);

        //===ImageView
        iv_adult_min=(ImageView) findViewById(R.id.iv_adult_min);
        iv_adult_plus=(ImageView) findViewById(R.id.iv_adult_plus);
        iv_child_min=(ImageView) findViewById(R.id.iv_child_min);
        iv_child_plus=(ImageView) findViewById(R.id.iv_child_plus);
        iv_infant_min=(ImageView) findViewById(R.id.iv_infant_min);
        iv_infant_plus=(ImageView) findViewById(R.id.iv_infant_plus);

        //===CheckBox
        cb_fare = (CheckBox) findViewById(R.id.cb_fare);

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

    private void bottomDialog(){
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = getLayoutInflater().inflate(R.layout.flight_passenger_bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
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
