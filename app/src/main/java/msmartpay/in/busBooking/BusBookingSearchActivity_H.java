package msmartpay.in.busBooking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

/**
 * Created by Smartkinda on 7/13/2017.
 */

public class BusBookingSearchActivity_H extends BaseActivity {

    private FloatingActionButton btnSearchBus;
    private EditText et_destination, et_leave;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String colToDate = "", currentDate = "";
    private ImageView imageViewInc, imageViewDec;
    private ImageView iv_date, iv_swap;
    private TextView tvPassenser;
    public static int passenser = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;
    private boolean flag = true;
    private ProgressDialog pd;
    private ArrayList<String> CitySource;
    private ArrayList<String> CityIdSource;
    private ArrayList<String> CityDestination;
    private ArrayList<String> CityIdDestination;
    private String url_bus_source = HttpURL.BUS_SOURCE_URL;
    private String url_bus_destination = HttpURL.BUS_DESTINATION_URL;
    private String url_all_seat_details = HttpURL.ALL_SEAT_DETAILS_URL;
    private String OriginID, DestinationID;
    public static JSONObject jsonObjectStaticAllDetails = new JSONObject();
    private Context context;
    private String journeyDate = "", destinationCity="",leaveCity="";
    private TextView tview_journey_date, tview_journey_month_year;
    private LinearLayout linear_journey_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_booking_search_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Search Buses");
        context = BusBookingSearchActivity_H.this;
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        //=========Get Source Station===============
        try {
            getSourceRequest();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong !!!", Toast.LENGTH_SHORT).show();
        }
        //==========================================

        sharedPreferences = getApplication().getSharedPreferences("Details", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        et_leave =  findViewById(R.id.et_leave);
        et_destination =  findViewById(R.id.et_destination);
        tview_journey_date = (TextView) findViewById(R.id.tview_journey_date);
        tview_journey_month_year = (TextView) findViewById(R.id.tview_journey_month_year);
        linear_journey_date = (LinearLayout) findViewById(R.id.linear_journey_date);
        iv_date = (ImageView) findViewById(R.id.iv_date);
        iv_swap = (ImageView) findViewById(R.id.iv_swap);

        imageViewInc = (ImageView) findViewById(R.id.iv_adult_plus);
        imageViewDec = (ImageView) findViewById(R.id.iv_adult_min);
        tvPassenser = (TextView) findViewById(R.id.tv_adult);
        btnSearchBus = (FloatingActionButton) findViewById(R.id.button);
        imageViewDec.setVisibility(View.GONE);
        tvPassenser.setText(passenser+"");

        et_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CitySource != null) {
                    if(CitySource.size() != 0) {
                        Intent intent = new Intent(context, BusBookingLeave.class);
                        intent.putExtra("City", CitySource);
                        intent.putExtra("test", "Leave");
                        intent.putExtra("leaveCity", leaveCity);
                        intent.putExtra("destinationCity", destinationCity);
                        intent.putExtra("colToDate", colToDate);
                        intent.putExtra("passenser", passenser);
                        intent.putExtra("OriginID", OriginID);
                        intent.putExtra("DestinationID", DestinationID);
                        intent.putExtra("CitySource", CitySource);
                        intent.putExtra("CityDestination", CityDestination);
                        intent.putExtra("CityIdSource", CityIdSource);
                        intent.putExtra("CityIdDestination", CityIdDestination);
                        startActivityForResult(intent, 0);
                    }else{
                        Toast.makeText(context, "List Not Available !!!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "City Not Available !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(et_leave.getText().toString().trim())){
                    Toast.makeText(context,"Please first select Leaving City!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(context, BusBookingLeave.class);
                    intent.putExtra("City", CityDestination);
                    intent.putExtra("test","Destination");
                    intent.putExtra("leaveCity",leaveCity);
                    intent.putExtra("destinationCity",destinationCity);
                    intent.putExtra("colToDate",colToDate);
                    intent.putExtra("passenser",passenser);
                    intent.putExtra("OriginID",OriginID);
                    intent.putExtra("DestinationID",DestinationID);
                    intent.putExtra("CitySource",CitySource);
                    intent.putExtra("CityDestination",CityDestination);
                    intent.putExtra("CityIdSource",CityIdSource);
                    intent.putExtra("CityIdDestination",CityIdDestination);
                    startActivityForResult(intent, 1);
                }
            }
        });

        //Default today's date set
        todayDateTime();

        linear_journey_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateMethod();
            }
        });

        imageViewInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passenser<6){
                    passenser++;
                    tvPassenser.setText(""+passenser);
                }
                if(passenser>1){
                    imageViewDec.setVisibility(View.VISIBLE);
                }
                if(passenser==6){
                    imageViewInc.setVisibility(View.INVISIBLE);
                }
            }
        });

        imageViewDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passenser>1){
                    passenser--;
                    tvPassenser.setText(""+passenser);
                    imageViewInc.setVisibility(View.VISIBLE);
                }
                if(passenser==1){
                    imageViewInc.setVisibility(View.VISIBLE);
                    imageViewDec.setVisibility(View.INVISIBLE);
                }

            }
        });
        
        iv_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromOrgin = et_leave.getText().toString().trim();
                String toDestination = et_destination.getText().toString().trim();
                if(flag){
                    if(!TextUtils.isEmpty(fromOrgin) && !TextUtils.isEmpty(toDestination)) {
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rorate_imageview);
                        iv_swap.startAnimation(animation);
                        et_leave.setText(toDestination);
                        et_destination.setText(fromOrgin);
                        String temp =OriginID;
                        OriginID = DestinationID;
                        DestinationID = temp;
                        flag = true;
                    }else{
                        Toast.makeText(getApplicationContext(), "First select Orgin and Destination !!!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(!TextUtils.isEmpty(fromOrgin) && !TextUtils.isEmpty(toDestination)) {
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rorate_imageview);
                        iv_swap.startAnimation(animation);
                        et_leave.setText(fromOrgin);
                        et_destination.setText(toDestination);
                        String temp =OriginID;
                        OriginID = DestinationID;
                        DestinationID = temp;
                        flag = false;
                    }else{
                        Toast.makeText(getApplicationContext(), "First select Orgin and Destination !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSearchBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayDateTime();
                String fromOrgin = et_leave.getText().toString().trim();
                String toDestination = et_destination.getText().toString().trim();
                if (TextUtils.isEmpty(fromOrgin)) {
                    Toast.makeText(context, "Please Select Leave From !!!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(toDestination)) {
                    Toast.makeText(context, "Please Select Destination To !!!", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(journeyDate)){
                    Toast.makeText(context, "Please Set Your Date Of Journey !!!", Toast.LENGTH_SHORT).show();
                } else {
                    AllSeatDetailsRequest();
                }
            }
        });
    }
    //================Today Date Format======================================
    private void todayDateTime() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String todayString = formatter.format(date);
        journeyDate = todayString;
        String dayOfTheWeek = (String) DateFormat.format("EEE", date);
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        String year1         = (String) DateFormat.format("yyyy", date);
        tview_journey_date.setText(day);
        tview_journey_month_year.setText(monthString + " " + year1 + ", " + dayOfTheWeek);
    }

    //=======================================================================
    private void setDateMethod() {
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                colToDate=dateFormatter.format(newDate.getTime());
                String dayOfTheWeek = (String) DateFormat.format("EEE", newDate);
                String day          = (String) DateFormat.format("dd",   newDate); // 20
                String monthString  = (String) DateFormat.format("MMM",  newDate); // Jun
                String year1         = (String) DateFormat.format("yyyy", newDate);

                tview_journey_date.setText(day);
                tview_journey_month_year.setText(monthString + " " + year1 + ", " + dayOfTheWeek);
                currentDate = dateFormatter.format(Calendar.getInstance().getTime());

               L.m2("Date--->", colToDate);
                try {
                    Date todate = dateFormatter.parse(colToDate);
                    Date curdate = dateFormatter.parse(currentDate);
                    if(curdate.compareTo(todate) > 0){
                       // journeyDate.setText("");
                        journeyDate = "";
                        Toast.makeText(getApplicationContext(), "Selected date should be greater than or equal to "+ currentDate, Toast.LENGTH_SHORT).show();
                    }else{
                        //journeyDate.setText(colToDate);
                        journeyDate = colToDate;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("Exception", "Something went wrong !");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    //===========Get Source ==============
    private void getSourceRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
           L.m2("url-called--SourceBus", url_bus_source);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_bus_source,
                    new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("Response--SourceBus", object.toString());
                            try {
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    JSONArray cityJsonArray = object.getJSONArray("City");
                                    CitySource = new ArrayList<>();
                                    CityIdSource = new ArrayList<>();
                                    for (int i = 0; i < cityJsonArray.length(); i++) {

                                        JSONObject obj=cityJsonArray.getJSONObject(i);
                                        CitySource.add(obj.getString("OriginName"));
                                        CityIdSource.add(obj.getString("OriginId"));
                                    }
                                   /* CitySourceAdaptor = new ArrayAdapter(context, android.R.layout.simple_list_item_1, CitySource);
                                    autoComplete1.setAdapter(CitySourceAdaptor);
                                    autoComplete1.setThreshold(1);
                                   L.m2("city_adaptor--->", CitySource.toString());*/
                                } else {
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                         //   CitySourceAdaptor.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(BusBookingSearchActivity_H.this).addToRequsetque(jsonrequest);
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }
    }

    //===========Get Destination ==============

    private void getDestinationRequest(String destination) {

        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("OriginId", destination);
           L.m2("url-called--Desti", url_bus_destination);
            L.m2("Request--Desti",jsonObjectReq.toString());
            JsonObjectRequest  jsonrequest = new JsonObjectRequest(Request.Method.POST, url_bus_destination, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            L.m2("response --Desti", object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("status").equalsIgnoreCase("0")) {

                                    JSONArray cityDestiJsonArray = object.getJSONArray("City");
                                    CityDestination = new ArrayList<>();
                                    CityIdDestination = new ArrayList<>();
                                    for (int i = 0; i < cityDestiJsonArray.length(); i++) {

                                        JSONObject obj=cityDestiJsonArray.getJSONObject(i);
                                        CityDestination.add(obj.getString("DestinationName"));
                                        CityIdDestination.add(obj.getString("DestinationId"));
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //CityDestinationAdaptor.notifyDataSetChanged();
                        }
                    },new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(BusBookingSearchActivity_H.this).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }

    //===========Get Seat Details ==============

    //Json request for Seat Details
    private void AllSeatDetailsRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("OriginId", OriginID)
                    .put("DestinationId", DestinationID)
                    .put("TravelDate", journeyDate);
            L.m2("Url-called::", url_all_seat_details);
            L.m2("Request::",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_all_seat_details, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                              L.m2("Response::",object.toString());
                            try {
                                jsonObjectStaticAllDetails = object;
                                if(object.getString("status")!=null){
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, DetailsOfBusListActivity.class);
                                    editor.putString("FromSource", et_leave.getText().toString());
                                    editor.putString("ToSource", et_destination.getText().toString());
                                    editor.putString("DateOfJourney", journeyDate);
                                    editor.commit();
                                    startActivity(intent);
                                } else {
                                        Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                                }else{
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(BusBookingSearchActivity_H.this).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }


//======================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        leaveCity = data.getStringExtra("leaveCity");
        destinationCity = data.getStringExtra("destinationCity");
        colToDate = data.getStringExtra("colToDate");
        passenser = data.getIntExtra("passenser",0);
        OriginID = data.getStringExtra("OriginID");
        DestinationID = data.getStringExtra("DestinationID");
        CitySource = data.getStringArrayListExtra("CitySource");
        CityDestination = data.getStringArrayListExtra("CityDestination");
        CityIdSource = data.getStringArrayListExtra("CityIdSource");
        CityIdDestination = data.getStringArrayListExtra("CityIdDestination");
        switch (requestCode){
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
        }
    }
//===========

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        leaveCity = inState.getString("leaveCity");
        destinationCity = inState.getString("destinationCity");
        colToDate = inState.getString("colToDate");
        passenser = inState.getInt("passenser");
        OriginID = inState.getString("OriginID");
        DestinationID = inState.getString("DestinationID");
        CitySource = inState.getStringArrayList("CitySource");
        CityDestination = inState.getStringArrayList("CityDestination");
        CityIdSource = inState.getStringArrayList("CityIdSource");
        CityIdDestination = inState.getStringArrayList("CityIdDestination");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("leaveCity",leaveCity);
        outState.putString("destinationCity",destinationCity);
        outState.putString("colToDate",colToDate);
        outState.putInt("passenser",passenser);
        outState.putString("OriginID",OriginID);
        outState.putString("DestinationID",DestinationID);
        outState.putStringArrayList("CitySource",CitySource);
        outState.putStringArrayList("CityDestination",CityDestination);
        outState.putStringArrayList("CityIdSource",CityIdSource);
        outState.putStringArrayList("CityIdDestination",CityIdDestination);
    }

    //===============================================
    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent()!=null){
            tvPassenser.setText(""+passenser);
            et_destination.setText(destinationCity);
            et_leave.setText(leaveCity);
            journeyDate = colToDate;

        }
    }


    //=============Menu For Ticket Cancel==================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pnr_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(i == R.id.pnr_status){
            Intent intent = new Intent(getApplicationContext(), CheckPnrStatusActivity.class);
            startActivity(intent);
        }
        if(i==android.R.id.home){
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return true;
    }

}
