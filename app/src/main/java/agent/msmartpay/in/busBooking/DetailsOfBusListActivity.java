package agent.msmartpay.in.busBooking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

import static agent.msmartpay.in.busBooking.BusBookingSearchActivity_H.jsonObjectStaticAllDetails;
import static agent.msmartpay.in.busBooking.BusBookingSearchActivity_H.passenser;

/*
import static agent.msmartpay.in.BusBooking.BusBookingSearchActivity_H.jsonObjectStaticAllDetails;
import static agent.msmartpay.in.BusBooking.BusBookingSearchActivity_H.passenser;
*/

/**
 * Created by Smartkinda on 7/14/2017.
 */

public class DetailsOfBusListActivity extends BaseActivity {

    private TextView DOJwithSeats;
    private String FromSource, ToDestination, DateOfJourney, SelectedSeat;
    private ListView listView;
    private Context context;
    private FloatingActionButton floatMain, floatDeparture, floatSeat, floatPrice;
    private Animation FabOpen, FabClose, FabClockWise, FabAntiClockWise;
    boolean isOpen = false;
    private FrameLayout linearLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private JSONArray jsonArray;
    private TextView tvEmptyList;
    private ArrayList<BusDetailsModel> busListDetails;
    private String UserTrackId;
    private ArrayList<HashMap<String, Object>> AvailableBusesArrayList;
    private boolean sortFlag=false;
    private boolean sortFlag1=false;
    private boolean sortFlag2=false;
    private int countColumnLower=0;
    private int countColumnUpper=0;
    private ArrayList<BusDetailsModel> BoardingPointArrayList=null;
    private ArrayList<BusDetailsModel> DroppingPointArrayList=null;
    private ArrayList<BusDetailsModel> FaresArrayList=null;
    private ArrayList<SeatModel> LowerSeatsArrayList=null,UpperSeatsArrayList=null;
    private String url_seat_map = HttpURL.SEAT_MAP_URL;
    private ProgressDialog pd;
    private String upperStatus ="";
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        setContentView(R.layout.bus_details_list_activity);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        sharedPreferences = getApplication().getSharedPreferences("Details", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FromSource = sharedPreferences.getString("FromSource", null);
        ToDestination = sharedPreferences.getString("ToSource", null);
        DateOfJourney = sharedPreferences.getString("DateOfJourney", null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(FromSource+" - "+ToDestination); //change like app

        DOJwithSeats = (TextView) findViewById(R.id.doj_with_seats);
        DOJwithSeats.setText(DateOfJourney+"       "+passenser+" Seats");

        //======JsonDataForBusDetais=======================
        L.m2("check_jsonStaticData-->", jsonObjectStaticAllDetails.toString());
        tvEmptyList = (TextView) findViewById(R.id.tv_empty_list);
        try {
            jsonArray=jsonObjectStaticAllDetails.getJSONArray("City");
            AvailableBusesArrayList = new ArrayList<>();

            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject.getJSONArray("AvailableBuses");
                UserTrackId = jsonObject.getString("UserTrackId");
                editor.putString("UserTrackId", UserTrackId);
                editor.commit();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("BusType", jsonObject1.getString("BusType"));
                    hashMap.put("AvailableSeatCount", jsonObject1.getString("AvailableSeatCount"));
                    hashMap.put("TransportId", jsonObject1.getString("TransportId"));
                    hashMap.put("BusName", jsonObject1.getString("BusName"));
                    hashMap.put("Commission", jsonObject1.getString("Commission"));
                    hashMap.put("ScheduleId", jsonObject1.getString("ScheduleId"));
                    hashMap.put("DepartureTime", jsonObject1.getString("DepartureTime"));
                    hashMap.put("StationId", jsonObject1.getString("StationId"));
                    hashMap.put("ArrivalTime", jsonObject1.getString("ArrivalTime"));
                    hashMap.put("TransportName", jsonObject1.getString("TransportName"));
//////
                    ArrayList<HashMap<String, Object>> BoardingPointsArrayList = new ArrayList<>();
                    JSONArray BoardingPointsJsonArray = jsonObject1.getJSONArray("BoardingPoints");
                    for(int a = 0; a<BoardingPointsJsonArray.length(); a++){
                        JSONObject jsonObject2 = BoardingPointsJsonArray.getJSONObject(a);
                        HashMap<String, Object> hashMap1 = new HashMap<>();
                        hashMap1.put("Place", jsonObject2.getString("Place"));
                        hashMap1.put("ContactNumber", jsonObject2.getString("ContactNumber"));
                        hashMap1.put("Time", jsonObject2.getString("Time"));
                        hashMap1.put("Address", jsonObject2.getString("Address"));
                        hashMap1.put("BoardingId", jsonObject2.getString("BoardingId"));
                        hashMap1.put("LandMark", jsonObject2.getString("LandMark"));
                        BoardingPointsArrayList.add(hashMap1);
                    }
                    hashMap.put("BoardingPoints", BoardingPointsArrayList);
                  ///////
             //===========================================

                    ArrayList<HashMap<String, Object>> FaresArrayList = new ArrayList<>();
                    JSONArray FaresJsonArray = jsonObject1.getJSONArray("Fares");
                    for(int a = 0; a<FaresJsonArray.length(); a++){
                        JSONObject jsonObject3 = FaresJsonArray.getJSONObject(a);
                        HashMap<String, Object> hashMap2 = new HashMap<>();
                        hashMap2.put("Fare", jsonObject3.getString("Fare"));
                        hashMap2.put("SeatType", jsonObject3.getString("SeatType"));
                        hashMap2.put("ConvenienceFee", jsonObject3.getString("ConvenienceFee"));
                        hashMap2.put("SeatTypeId", jsonObject3.getString("SeatTypeId"));
                        hashMap2.put("ServiceTax", jsonObject3.getString("ServiceTax"));
                        FaresArrayList.add(hashMap2);
                    }
                    hashMap.put("Fares", FaresArrayList);
///////

                    ArrayList<HashMap<String, Object>> DroppingPointsArrayList = new ArrayList<>();
                    JSONArray DroppingPointsJsonArray = jsonObject1.getJSONArray("DroppingPoints");
                    for(int a = 0; a<DroppingPointsJsonArray.length(); a++){
                        JSONObject jsonObject4 = DroppingPointsJsonArray.getJSONObject(a);
                        HashMap<String, Object> hashMap4 = new HashMap<>();
                        hashMap4.put("DroppingId", jsonObject4.getString("DroppingId"));
                        hashMap4.put("Place", jsonObject4.getString("Place"));
                        hashMap4.put("ContactNumber", jsonObject4.getString("ContactNumber"));
                        hashMap4.put("Time", jsonObject4.getString("Time"));
                        hashMap4.put("Address", jsonObject4.getString("Address"));
                        hashMap4.put("LandMark", jsonObject4.getString("LandMark"));
                        DroppingPointsArrayList.add(hashMap4);
                    }
                    hashMap.put("DroppingPoints", DroppingPointsArrayList);
                    AvailableBusesArrayList.add(hashMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //=================================================
        context = this;
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new BusDetailsAdapterClass(context, AvailableBusesArrayList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSeatsMapRequest(position);


            }
        });
        //Float Coding
        linearLayout = (FrameLayout) findViewById(R.id.layout_float);
        floatMain = (FloatingActionButton) findViewById(R.id.float_main);
        floatDeparture = (FloatingActionButton) findViewById(R.id.float_departure);
        floatPrice = (FloatingActionButton) findViewById(R.id.float_fare);
        floatSeat = (FloatingActionButton) findViewById(R.id.float_seat);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anti_clockwise);

        linearLayout.setVisibility(View.GONE);

        floatMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                if(isOpen){
                    linearLayout.setVisibility(View.GONE);
                    floatDeparture.setAnimation(FabClose);
                    floatSeat.setAnimation(FabClose);
                    floatPrice.setAnimation(FabClose);
                    floatMain.startAnimation(FabAntiClockWise);
                    floatDeparture.setClickable(false);
                    floatPrice.setClickable(false);
                    isOpen = false;
                }else{
                    linearLayout.setVisibility(View.VISIBLE);
                    floatDeparture.setAnimation(FabOpen);
                    floatSeat.setAnimation(FabOpen);
                    floatPrice.setAnimation(FabOpen);
                    floatMain.startAnimation(FabClockWise);
                    floatDeparture.setClickable(true);
                    floatPrice.setClickable(true);
                    isOpen = true;
                }
            }
        });

        //================Sort By Price=====================

        floatPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sortFlag == false) {
                    Collections.sort(AvailableBusesArrayList, new Comparator<HashMap<String, Object>>() {
                        public int compare(HashMap<String, Object> p1, HashMap<String, Object> p2) {
                            return ((ArrayList<HashMap<String, Object>>)p1.get("Fares")).get(0).get("Fare").toString().compareTo(((ArrayList<HashMap<String, Object>>)p2.get("Fares")).get(0).get("Fare").toString());
                        }
                    });
                    sortFlag = true;
                }else{
                    Collections.reverse(AvailableBusesArrayList);
                    sortFlag = false;
                }
                listView.setAdapter(new BusDetailsAdapterClass(context, AvailableBusesArrayList));
            }
        });

        //================Sort By Seat=====================
        floatSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sortFlag1 == false) {
                    Collections.sort(AvailableBusesArrayList, new Comparator<HashMap<String, Object>>() {
                        public int compare(HashMap<String, Object> p1, HashMap<String, Object> p2) {
                            return (p1.get("AvailableSeatCount")).toString().compareTo(p2.get("AvailableSeatCount").toString());
                        }
                    });
                    sortFlag1 = true;
                }else{
                    Collections.reverse(AvailableBusesArrayList);
                    sortFlag1 = false;
                }
                listView.setAdapter(new BusDetailsAdapterClass(context, AvailableBusesArrayList));
            }
        });

        //================Sort By Departure=====================
        floatDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sortFlag2 == false) {
                    Collections.sort(AvailableBusesArrayList, new Comparator<HashMap<String, Object>>() {
                        public int compare(HashMap<String, Object> p1, HashMap<String, Object> p2) {
                            return (p1.get("DepartureTime")).toString().compareTo(p2.get("DepartureTime").toString());
                        }
                    });
                    sortFlag2 = true;
                }else{
                    Collections.reverse(AvailableBusesArrayList);
                    sortFlag2 = false;
                }
                listView.setAdapter(new BusDetailsAdapterClass(context, AvailableBusesArrayList));
            }
        });
    }

    //Adapter class
    public class BusDetailsAdapterClass extends BaseAdapter {

        private Context contextData;
        private ArrayList<HashMap<String, Object>> seatDetailsList;
        private TextView TravellerName, BusTiming, SeatType, PendingSeats, BusType, TotalFare;
        private ArrayList<String> intentArrayList = new ArrayList<>();
        private int lastPosition = -1;


        BusDetailsAdapterClass(Context context, ArrayList<HashMap<String, Object>> seatDetails)
        {
            contextData = context;
            seatDetailsList = seatDetails;
        }

        @Override
        public int getCount() {
            return seatDetailsList.size();
        }

        @Override
        public Object getItem(int position) {
            return seatDetailsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) contextData.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate (R.layout.bus_detail_items_list, parent, false);

            TravellerName = (TextView) view.findViewById(R.id.tv_traveler_name);
            BusTiming = (TextView) view.findViewById(R.id.tv_bus_timing);
            SeatType = (TextView) view.findViewById(R.id.tv_seat_type);
            PendingSeats = (TextView) view.findViewById(R.id.tv_pending_seats);
            BusType = (TextView) view.findViewById(R.id.tv_bus_type);
            TotalFare = (TextView) view.findViewById(R.id.tv_amount);


            HashMap<String, Object> objectHashMap = new HashMap<>();
            objectHashMap = seatDetailsList.get(position);
            intentArrayList.add(objectHashMap.get("TransportName").toString());
            TravellerName.setText(objectHashMap.get("TransportName").toString());
            BusTiming.setText(objectHashMap.get("DepartureTime").toString()+" - "+objectHashMap.get("ArrivalTime").toString());
            PendingSeats.setText(objectHashMap.get("AvailableSeatCount").toString()+" seats left");
            BusType.setText(objectHashMap.get("BusType").toString());
            ArrayList<HashMap<String, Object>> FareData = new ArrayList<>();
            FareData = (ArrayList<HashMap<String, Object>>) objectHashMap.get("Fares");
            for (Map<String, Object> entry : FareData) {
                TotalFare.setText("\u20B9 "+entry.get("Fare").toString());
                SeatType.setText("TYPE : "+entry.get("SeatType").toString());
            }

            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.slidein : R.anim.slideout);
            view.startAnimation(animation);
            lastPosition = position;

            return view;
        }
    }

    private void getSeatsMapRequest(final int pos) {

        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap = AvailableBusesArrayList.get(pos);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JsonObjectRequest jsonrequest = null;
            JSONObject jsonObjectData =  new JSONObject()
                    .put("ScheduleId", objectHashMap.get("ScheduleId").toString())
                    .put("StationId", objectHashMap.get("StationId").toString())
                    .put("TransportId", objectHashMap.get("TransportId").toString())
                    .put("TravelDate", DateOfJourney)
                    .put("UserTrackId", UserTrackId);
            L.m2("url--SeatsMap", url_seat_map);
            L.m2("Request--SeatsMap>", jsonObjectData.toString());
            jsonrequest = new JsonObjectRequest(Request.Method.POST, url_seat_map,jsonObjectData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("response--SeatsMap", object.toString());
                            try {
                                if (object.getString("status")!=null&&object.getString("status").equalsIgnoreCase("0")) {
                                    JSONObject jsonSeatMapDetails = object.getJSONObject("SeatMapDetails");
                                    if(!jsonSeatMapDetails.isNull("upperStatus")){
                                        upperStatus = jsonSeatMapDetails.getString("upperStatus");
                                    }else{
                                        upperStatus ="N";
                                    }
                                    L.m2("upperStatus",upperStatus);
                                    //System.out.println("check_city>" + jsonSeatMapDetails.toString());
                                     /*   editor.putString("BusNumber", jsonObject1.getString("BusNumber"));
                                          editor.commit();*/
                                    JSONArray BoardingPointsJsonArray = jsonSeatMapDetails.getJSONArray("BoardingPoints");
                                    BoardingPointArrayList = new ArrayList<>();
                                    for(int i=0; i<BoardingPointsJsonArray.length(); i++){
                                        JSONObject BoardingPointsJsonObject = BoardingPointsJsonArray.getJSONObject(i);
                                        BusDetailsModel detailsModel = new BusDetailsModel();
                                        detailsModel.setBoardingAddress(BoardingPointsJsonObject.getString("Address"));
                                        detailsModel.setBoardingContactNumber(BoardingPointsJsonObject.getString("ContactNumber"));
                                        detailsModel.setBoardingId(BoardingPointsJsonObject.getString("BoardingId"));
                                        detailsModel.setBoardingLandMark(BoardingPointsJsonObject.getString("LandMark"));
                                        detailsModel.setBoardingPlace(BoardingPointsJsonObject.getString("Place"));
                                        detailsModel.setBoardingTime(BoardingPointsJsonObject.getString("Time"));
                                        BoardingPointArrayList.add(detailsModel);
                                    }

                                    JSONArray DroppingPointsJsonArray = jsonSeatMapDetails.getJSONArray("DroppingPoints");
                                    DroppingPointArrayList = new ArrayList<>();
                                    for(int i=0; i<DroppingPointsJsonArray.length(); i++){
                                        JSONObject DroppingPointsJsonObject = DroppingPointsJsonArray.getJSONObject(i);
                                        BusDetailsModel detailsModel = new BusDetailsModel();
                                        detailsModel.setDroppingAddress(DroppingPointsJsonObject.getString("Address"));
                                        detailsModel.setDroppingContactNumber(DroppingPointsJsonObject.getString("ContactNumber"));
                                        detailsModel.setDroppingId(DroppingPointsJsonObject.getString("DroppingId"));
                                        detailsModel.setDroppingLandMark(DroppingPointsJsonObject.getString("LandMark"));
                                        detailsModel.setDroppingPlace(DroppingPointsJsonObject.getString("Place"));
                                        detailsModel.setDroppingTime(DroppingPointsJsonObject.getString("Time"));
                                        DroppingPointArrayList.add(detailsModel);
                                    }

                                    JSONArray FaresJsonArray = jsonSeatMapDetails.getJSONArray("Fares");
                                    FaresArrayList = new ArrayList<>();
                                    for(int i=0; i<FaresJsonArray.length(); i++){
                                        JSONObject FaresJsonObject = FaresJsonArray.getJSONObject(i);
                                        BusDetailsModel FaresModel = new BusDetailsModel();
                                        FaresModel.setFaresSingle(FaresJsonObject.getString("Fare"));
                                        FaresModel.setFaresConvenienceFee(FaresJsonObject.getString("ConvenienceFee"));
                                        FaresModel.setFaresSeatType(FaresJsonObject.getString("SeatType"));
                                        FaresModel.setFaresSeatTypeId(FaresJsonObject.getString("SeatTypeId"));
                                        FaresModel.setFaresServiceTax(FaresJsonObject.getString("ServiceTax"));
                                        FaresArrayList.add(FaresModel);
                                    }

                                    //======For Individual Lower seat==================================
                                    JSONObject jsonObject = jsonSeatMapDetails.getJSONObject("SeatLayout");
                                    JSONArray SeatColumnsJsonArray = jsonObject.getJSONArray("SeatColumns");
                                    LowerSeatsArrayList = new ArrayList<>();
                                    UpperSeatsArrayList = new ArrayList<>();
                                    countColumnLower=0;
                                    countColumnUpper=0;

                                    for(int j=0; j< SeatColumnsJsonArray.length(); j++) {
                                        JSONObject SeatColumnsjsonObject = SeatColumnsJsonArray.getJSONObject(j);
                                        JSONArray SeatsJsonArray = SeatColumnsjsonObject.getJSONArray("Seats");
                                        if(countColumnLower==0){
                                            countColumnLower=SeatsJsonArray.length();
                                        }

                                        for (int i = 0; i < SeatsJsonArray.length(); i++) {
                                            JSONObject SeatsjsonObject = SeatsJsonArray.getJSONObject(i);
                                            SeatModel SeatValue = new SeatModel();
                                            if(SeatsjsonObject.length()>0){
                                                SeatValue.setFare(SeatsjsonObject.getString("Fare"));
                                                SeatValue.setBerthType(SeatsjsonObject.getString("BerthType"));
                                                SeatValue.setSeatNo(SeatsjsonObject.getString("SeatNo"));
                                                SeatValue.setSeatType(SeatsjsonObject.getString("SeatType"));
                                                SeatValue.setGender(SeatsjsonObject.getString("Gender"));
                                                SeatValue.setConvenienceFee(SeatsjsonObject.getString("ConvenienceFee"));
                                                SeatValue.setSeatTypeId(SeatsjsonObject.getString("SeatTypeId"));
                                                SeatValue.setServiceTax(SeatsjsonObject.getString("ServiceTax"));
                                                SeatValue.setSeatStatus(SeatsjsonObject.getString("SeatStatus"));
                                            }else if(SeatsjsonObject.length()==0){
                                                SeatValue.setSeatNo("NA");
                                            }
                                            LowerSeatsArrayList.add(SeatValue);
                                        }

                                        JSONArray UpperSeatsJsonArray = SeatColumnsjsonObject.getJSONArray("UpperSeats");
                                        if(countColumnUpper==0){
                                            countColumnUpper=UpperSeatsJsonArray.length();
                                        }
                                        for (int i = 0; i < UpperSeatsJsonArray.length(); i++) {
                                            JSONObject UpperjsonObject = UpperSeatsJsonArray.getJSONObject(i);
                                            SeatModel SeatValue = new SeatModel();
                                            if(UpperjsonObject.length()>0){
                                                SeatValue.setFare(UpperjsonObject.getString("Fare"));
                                                SeatValue.setBerthType(UpperjsonObject.getString("BerthType"));
                                                SeatValue.setSeatNo(UpperjsonObject.getString("SeatNo"));
                                                SeatValue.setSeatType(UpperjsonObject.getString("SeatType"));
                                                SeatValue.setGender(UpperjsonObject.getString("Gender"));
                                                SeatValue.setConvenienceFee(UpperjsonObject.getString("ConvenienceFee"));
                                                SeatValue.setSeatTypeId(UpperjsonObject.getString("SeatTypeId"));
                                                SeatValue.setServiceTax(UpperjsonObject.getString("ServiceTax"));
                                                SeatValue.setSeatStatus(UpperjsonObject.getString("SeatStatus"));
                                            }else if(UpperjsonObject.length()==0){
                                                SeatValue.setSeatNo("NA");
                                            }
                                            UpperSeatsArrayList.add(SeatValue);
                                        }
                                    }

                                        HashMap<String, Object> objectHashMap = new HashMap<>();
                                        objectHashMap = AvailableBusesArrayList.get(pos);
                                        editor.putString("TravellerName", objectHashMap.get("TransportName").toString());
                                        editor.putString("BusTiming", objectHashMap.get("DepartureTime").toString() + " - " + objectHashMap.get("ArrivalTime").toString());
                                        editor.putString("ScheduleId", objectHashMap.get("ScheduleId").toString());
                                        editor.putString("BusName", objectHashMap.get("BusName").toString());
                                        editor.putString("StationId", objectHashMap.get("StationId").toString());
                                        editor.putString("TransportId", objectHashMap.get("TransportId").toString());
                                        editor.putString("DepartureTime", objectHashMap.get("DepartureTime").toString());
                                        editor.putString("ArrivalTime", objectHashMap.get("ArrivalTime").toString());
                                        editor.commit();

                                        Intent intent = new Intent(context, SeatSelectionActivity.class);
                                        intent.putExtra("PendingSeats", objectHashMap.get("AvailableSeatCount").toString() + " Seats Left");
                                        ArrayList<HashMap<String, Object>> FareData = new ArrayList<>();
                                        FareData = (ArrayList<HashMap<String, Object>>) objectHashMap.get("Fares");
                                        for (Map<String, Object> entry : FareData) {
                                            editor.putString("SeatType", entry.get("SeatType").toString());
                                            editor.putString("ConvenienceFee", entry.get("ConvenienceFee").toString());
                                            editor.putString("SeatTypeId", entry.get("SeatTypeId").toString());
                                            editor.putString("ServiceTax", entry.get("ServiceTax").toString());
                                            editor.commit();
                                            intent.putExtra("Fare", entry.get("Fare").toString());
                                        }
                                        intent.putExtra("upperStatus", upperStatus);
                                        intent.putExtra("BoardingPointArrayList", BoardingPointArrayList);
                                        intent.putExtra("DroppingPointArrayList", DroppingPointArrayList);
                                        intent.putExtra("FaresArrayList", FaresArrayList);
                                        intent.putExtra("LowerSeatsArrayList", LowerSeatsArrayList);
                                        intent.putExtra("UpperSeatsArrayList", UpperSeatsArrayList);
                                        intent.putExtra("countColumnLower", countColumnLower);
                                        intent.putExtra("countColumnUpper", countColumnUpper);
                                        startActivity(intent);

                                } else {
                                    if(object.getString("status").equalsIgnoreCase("1")&&object.getString("status")!=null){
                                        Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(DetailsOfBusListActivity.this).addToRequsetque(jsonrequest);
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DOJwithSeats.setText(DateOfJourney+"       "+passenser+" Seats");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        passenser =1;
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
