package msmartpay.in.busBooking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class BusBookingSearchActivity extends BaseActivity {

    private AutoCompleteTextView autoComplete1, autoComplete2;
    private Button btnSearchBus;
    private EditText editDOJ;
    private ImageView calenderView;
    private Spinner spinnerSeat;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String colToDate = "", currentDate = "", SeatSpinnerData;
    private ImageView imageViewInc, imageViewDec;
    private TextView tvPassenser;
    public static int passenser = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;
    private ImageView swapLocationImage;
    private boolean flag = true;
    private ArrayAdapter adapterFrom,adapterTo;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private ArrayList<String> CitySource;
    private ArrayList<String> CityIdSource;
    private ArrayList<String> CityDestination;
    private ArrayList<String> CityIdDestination;
    private String url_bus_source = HttpURL.BUS_SOURCE_URL;
    private String url_bus_destination = HttpURL.BUS_DESTINATION_URL;
    private String url_all_seat_details = HttpURL.ALL_SEAT_DETAILS_URL;
    private ArrayAdapter CitySourceAdaptor;
    private ArrayAdapter CityDestinationAdaptor;
    private String OriginID, DestinationID;
    public static JSONObject jsonObjectStaticAllDetails = new JSONObject();
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_booking_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Search");
/*
       //*ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.requestFocusFromTouch();
                return false;
            }
        });*//*

        //=========Get Source Station===============
        try {
            getSourceRequest();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong !!!", Toast.LENGTH_SHORT).show();
        }
        //==========================================

        sharedPreferences = getApplication().getSharedPreferences("Details", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        autoComplete1=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        autoComplete2=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView2);
        imageViewInc = (ImageView) findViewById(R.id.iv_adult_plus);
        imageViewDec = (ImageView) findViewById(R.id.iv_adult_min);
        swapLocationImage = (ImageView) findViewById(R.id.swap_location);
        tvPassenser = (TextView) findViewById(R.id.tv_adult);
        btnSearchBus =  findViewById(R.id.button);
        editDOJ =  findViewById(R.id.journey_date_edit);
        editDOJ.setEnabled(false);

        calenderView = (ImageView) findViewById(R.id.journey_calender);
        calenderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    setDateMethod();
                }
            }
        });


        try
        {
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String todayString = formatter.format(date);
            editDOJ.setText(todayString);
        } catch (Exception e){

        }

        imageViewInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passenser<6){
                    passenser++;
                    tvPassenser.setText(""+passenser);
                }
            }
        });

        imageViewDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passenser>1){
                    passenser--;
                    tvPassenser.setText(""+passenser);
                }
            }
        });

        //Imp
        scrollAndSelectionHide();

        swapLocationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromOrgin=null, toDestination=null;
                fromOrgin = autoComplete1.getText().toString().trim();
                toDestination = autoComplete2.getText().toString().trim();

                if(flag){
                    if(!TextUtils.isEmpty(autoComplete1.getText().toString()) && !TextUtils.isEmpty(autoComplete2.getText().toString())) {

                        Animation animation = AnimationUtils.loadAnimation(BusBookingSearchActivity.this, R.anim.rorate_imageview);
                        swapLocationImage.startAnimation(animation);

                        autoComplete1.setText(toDestination);
                        autoComplete2.setText(fromOrgin);
                        autoComplete1.clearFocus();
                        autoComplete2.clearFocus();
                        flag = true;
                    }else{
                        Toast.makeText(getApplicationContext(), "First select Orgin and Destination !!!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    if(!TextUtils.isEmpty(autoComplete1.getText().toString()) && !TextUtils.isEmpty(autoComplete2.getText().toString())) {

                        Animation animation = AnimationUtils.loadAnimation(BusBookingSearchActivity.this, R.anim.rorate_imageview);
                        swapLocationImage.startAnimation(animation);

                        autoComplete1.setText(fromOrgin);
                        autoComplete2.setText(toDestination);
                        autoComplete1.clearFocus();
                        autoComplete2.clearFocus();
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

                if (TextUtils.isEmpty(autoComplete1.getText().toString().trim())) {
                    autoComplete1.requestFocus();
                    showKeyBoard();
                    Toast.makeText(BusBookingSearchActivity.this, "Please Enter Source From !!!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(autoComplete2.getText().toString().trim())) {
                    autoComplete2.requestFocus();
                    showKeyBoard();
                    Toast.makeText(BusBookingSearchActivity.this, "Please Enter Destination To !!!", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(editDOJ.getText().toString().trim())){
                    editDOJ.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Set Your Date Of Journey !!!", Toast.LENGTH_SHORT).show();
                } else {
                    AllSeatDetailsRequest();
                }
            }
        });
    }

    //Important method
    private void scrollAndSelectionHide() {

        autoComplete1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                 //selected valu
                for(int i=0; i<CitySource.size(); i++){
                    if(autoComplete1.getText().toString().equalsIgnoreCase(CitySource.get(i))){
                        OriginID = CityIdSource.get(i).toString();
                        L.m2("check_OrginId--1>", OriginID.toString());
                        break;
                    }
                }
                L.m2("check_OrginId--2>", autoComplete1.getText().toString() +"-->"+ OriginID);

                getDestinationRequest(OriginID);
                hideKeyBoard(autoComplete1);
                autoComplete2.requestFocus();
            }

        });

        autoComplete1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyBoard(autoComplete1);
                }

            }
        });

        autoComplete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoComplete1.showDropDown();
            }
        });

        autoComplete2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                for(int i=0; i<CityDestination.size(); i++){
                    if(autoComplete2.getText().toString().equalsIgnoreCase(CityDestination.get(i))){
                        DestinationID = CityIdDestination.get(i).toString();
                        L.m2("check_DestinationID-->", DestinationID.toString());
                        break;
                    }
                }
                L.m2("check_DestinationID--2>", autoComplete2.getText().toString() +"-->"+ DestinationID);

                hideKeyBoard(autoComplete2);
            }

        });

        autoComplete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoComplete2.showDropDown();
            }
        });

        autoComplete2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyBoard(autoComplete2);
                }

            }
        });*/
    }

    private void setDateMethod() {

        //For Date Selection
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(BusBookingSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                colToDate=dateFormatter.format(newDate.getTime());
                currentDate = dateFormatter.format(Calendar.getInstance().getTime());
                L.m2("Date--->", colToDate);
                try {
                    Date todate = dateFormatter.parse(colToDate);
                    Date curdate = dateFormatter.parse(currentDate);
                    if(curdate.compareTo(todate)>0){
                        editDOJ.setText("");
                        Toast.makeText(getApplicationContext(), "Selected date should be greater than or equal to "+currentDate, Toast.LENGTH_SHORT).show();
                    }else{
                        editDOJ.setText(colToDate);
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
        pd = ProgressDialog.show(BusBookingSearchActivity.this, "", "Loading. Please wait...", true);
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
                            System.out.println("Object----SourceBusRequest>" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("status").equalsIgnoreCase("0")) {

                                    L.m2("url data--SourceBus", object.toString());

                                    JSONArray cityJsonArray = object.getJSONArray("City");
                                    CitySource = new ArrayList<>();
                                    CityIdSource = new ArrayList<>();
                                    for (int i = 0; i < cityJsonArray.length(); i++) {

                                        JSONObject obj=cityJsonArray.getJSONObject(i);
                                        CitySource.add(obj.getString("OriginName"));
                                        CityIdSource.add(obj.getString("OriginId"));
                                    }

                                    CitySourceAdaptor = new ArrayAdapter(BusBookingSearchActivity.this, android.R.layout.simple_list_item_1, CitySource);
                                    autoComplete1.setAdapter(CitySourceAdaptor);
                                    autoComplete1.setThreshold(1);
                                    L.m2("city_adaptor--->", CitySource.toString());
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
            Mysingleton.getInstance(BusBookingSearchActivity.this).addToRequsetque(jsonrequest);
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }
    }

    //===========Get Destination ==============

    private void getDestinationRequest(String destination) {

        pd = ProgressDialog.show(BusBookingSearchActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("OriginId", destination);
            L.m2("url-called--Desti", url_bus_destination);
            Log.e("Request--Desti",jsonObjectReq.toString());
            JsonObjectRequest  jsonrequest = new JsonObjectRequest(Request.Method.POST, url_bus_destination, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("Object---Desti>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url data--Desti", object.toString());

                                    JSONArray cityDestiJsonArray = object.getJSONArray("City");
                                    CityDestination = new ArrayList<>();
                                    CityIdDestination = new ArrayList<>();
                                    for (int i = 0; i < cityDestiJsonArray.length(); i++) {

                                        JSONObject obj=cityDestiJsonArray.getJSONObject(i);
                                        CityDestination.add(obj.getString("DestinationName"));
                                        CityIdDestination.add(obj.getString("DestinationId"));
                                    }

                                    CityDestinationAdaptor = new ArrayAdapter(BusBookingSearchActivity.this, android.R.layout.simple_list_item_1, CityDestination);
                                    autoComplete2.setAdapter(CityDestinationAdaptor);
                                    autoComplete2.setThreshold(1);
                                    L.m2("city_desti--->", CityDestination.toString());
                                }
                                else {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
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
            Mysingleton.getInstance(BusBookingSearchActivity.this).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    //===========Get Seat Details ==============

    //Json request for Seat Details
    private void AllSeatDetailsRequest() {
        pd = ProgressDialog.show(BusBookingSearchActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("OriginId", OriginID.toString())
                    .put("DestinationId", DestinationID.toString())
                    .put("TravelDate", editDOJ.getText().toString());
            L.m2("url-called--allDetails", url_all_seat_details);
            Log.e("Request--allDetails", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_all_seat_details, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObjectStaticAllDetails = object;
                            System.out.println("Object--allDetails>" + jsonObjectStaticAllDetails.toString());
                            try {
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    L.m2("url data--allDetails", object.toString());
                                    Toast.makeText(BusBookingSearchActivity.this, object.getString("message").toString(), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(BusBookingSearchActivity.this, DetailsOfBusListActivity.class);
                                    editor.putString("FromSource", autoComplete1.getText().toString());
                                    editor.putString("ToSource", autoComplete2.getText().toString());
                                    editor.putString("DateOfJourney", editDOJ.getText().toString());
                                    editor.commit();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(BusBookingSearchActivity.this, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
            Mysingleton.getInstance(BusBookingSearchActivity.this).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }

    //===============================================
    @Override
    protected void onResume() {
        super.onResume();
        tvPassenser.setText(""+passenser);
    }

    //=============Menu For Ticket Cancel==================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pnr_status, menu);

        this.menu=menu;
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
            onBackPressed();
        }
        return true;
    }

}
