package agent.msmartpay.in.busBooking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.TextDrawable;

/**
 * Created by IRFAN on 8/1/2017.
 */

public class TravellersDetail extends BaseActivity {

    private EditText editPassenser1=null, editPassenser2=null, editPassenser3=null, editPassenser4=null, editPassenser5=null, editPassenser6=null;
    private EditText editEmailId, editMobileNo, editPassenserAddress, editPassenserCity, editPassenserIdProofNo;
    private Spinner spinnerGender1, spinnerGender2, spinnerGender3, spinnerGender4, spinnerGender5, spinnerGender6, spinnerCountry, spinnerIdProof;
    private Spinner spinnerBoardingPassenger1, spinnerBoardingPassenger2, spinnerBoardingPassenger3, spinnerBoardingPassenger4, spinnerBoardingPassenger5, spinnerBoardingPassenger6;
    private Spinner spinnerDroppingPassenger1, spinnerDroppingPassenger2, spinnerDroppingPassenger3, spinnerDroppingPassenger4, spinnerDroppingPassenger5, spinnerDroppingPassenger6;
    private String[] Gender={"Select Gender...","Male","Female"};
    private String[] IdProof = {"Select Id Proof...", "Passport", "Voter ID", "Driving License", "PAN Card"};
    private String[] Country = {"India"};
    private String genderData1, genderData2, genderData3, genderData4, genderData5, genderData6, countryData, idProofData;
    private String boardingData1, boardingData2, boardingData3, boardingData4, boardingData5, boardingData6, droppingData1, droppingData2, droppingData3, droppingData4, droppingData5, droppingData6;
    private EditText editAge1, editAge2, editAge3, editAge4, editAge5, editAge6;
    private EditText tvPassenser1, tvPassenser2, tvPassenser3, tvPassenser4, tvPassenser5, tvPassenser6;
    private TextView tvTotalFare;
    private LinearLayout makePayment;
    private ArrayList<SeatListModel> SeatList = new ArrayList<SeatListModel>();
    private ArrayList<BusDetailsModel> FaresArrayList = new ArrayList<BusDetailsModel>();
    private String SeatNoDetails, FullFareOfAllSeats;
    private LinearLayout linear_layout1,linear_layout2,linear_layout3,linear_layout4,linear_layout5,linear_layout6;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;
    private String ScheduleId, StationId, TransportId, DateOfJourney, UserTrackId, AvailableBal, AgentID, FromSource,
            ToDestination, DepartureTime, ArrivalTime, SeatType, BusNumber, BusName, ConvenienceFee, SeatTypeId, ServiceTax,
            TravellerName;
    private ArrayList<BusDetailsModel> BoardingPointArrayList = new ArrayList<BusDetailsModel>();
    private ArrayList<BusDetailsModel> DroppingPointArrayList = new ArrayList<BusDetailsModel>();
    private ArrayList<String> BoardingPointList = new ArrayList<>();
    private ArrayList<String> DroppingPointList = new ArrayList<>();
    private int BoardingID1, BoardingID2, BoardingID3, BoardingID4, BoardingID5, BoardingID6;
    private int DroppingID1, DroppingID2, DroppingID3, DroppingID4, DroppingID5, DroppingID6;
 //   private ArrayList<HashMap<String, String>> Seats;
    private ArrayList<SeatModel> Seats;
    private JSONArray jsonArrayFinal;
    private ArrayList<BookedSeatModel> passengerDetails = new ArrayList<BookedSeatModel>();
    private int genderPosition1, genderPosition2, genderPosition3, genderPosition4, genderPosition5, genderPosition6;
    private int countryPosition, IdProofPosition;
    private SeatListModel seatListModel, seatListModel1, seatListModel2, seatListModel3, seatListModel4, seatListModel5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_travellers_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Traveller Detail");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        sharedPreferences = getApplication().getSharedPreferences("Details", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences("myPrefs", MODE_PRIVATE);


        DateOfJourney = sharedPreferences.getString("DateOfJourney", null);
        ScheduleId = sharedPreferences.getString("ScheduleId", null);
        StationId = sharedPreferences.getString("StationId", null);
        TransportId = sharedPreferences.getString("TransportId", null);
        UserTrackId = sharedPreferences.getString("UserTrackId", null);
        AvailableBal = sharedPreferences1.getString("balance", null);
        AgentID = sharedPreferences1.getString("agentonlyid", null);
        FromSource = sharedPreferences.getString("FromSource", null);
        ToDestination = sharedPreferences.getString("ToSource", null);
        DepartureTime = sharedPreferences.getString("DepartureTime", null);
        SeatType = sharedPreferences.getString("SeatType", null);
        BusNumber = sharedPreferences.getString("BusNumber", null);
        BusName = sharedPreferences.getString("BusName", null);
        ConvenienceFee = sharedPreferences.getString("ConvenienceFee", null);
        SeatTypeId = sharedPreferences.getString("SeatTypeId", null);
        ServiceTax = sharedPreferences.getString("ServiceTax", null);
        TravellerName = sharedPreferences.getString("TravellerName", null);
        ArrivalTime = sharedPreferences.getString("ArrivalTime", null);


        BoardingPointArrayList = (ArrayList<BusDetailsModel>) getIntent().getSerializableExtra("BoardingPointArrayList");
        DroppingPointArrayList = (ArrayList<BusDetailsModel>) getIntent().getSerializableExtra("DroppingPointArrayList");

        BoardingPointList.add("Select Boarding Station");
        for(int i = 0; i< BoardingPointArrayList.size(); i++){
            BusDetailsModel boardingModel = new BusDetailsModel();
            boardingModel=BoardingPointArrayList.get(i);
            BoardingPointList.add(boardingModel.getBoardingPlace());
        }

        DroppingPointList.add("Select Dropping Station");
        for(int i = 0; i< DroppingPointArrayList.size(); i++){
            BusDetailsModel droppingModel = new BusDetailsModel();
            droppingModel=DroppingPointArrayList.get(i);
            DroppingPointList.add(droppingModel.getDroppingPlace());
        }

        initLinearLayout();
        initTextViewPassenserData();
        initEditPassenserData();
        initSpinnerData();
        initEditAgeData();

        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.requestFocusFromTouch();
                hideKeyBoard(v);
                return false;

            }
        });

        String code = "+91       ";
        editMobileNo.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        editMobileNo.setCompoundDrawablePadding(code.length()*10);

        SeatList = (ArrayList<SeatListModel>) getIntent().getSerializableExtra("SeatNoDetails");
        FaresArrayList = (ArrayList<BusDetailsModel>) getIntent().getSerializableExtra("FaresArrayList");
        FullFareOfAllSeats = getIntent().getStringExtra("AllSeatsFare");
        Seats = getIntent().getParcelableArrayListExtra("HasmapSeats");
        L.m2("SeatNoDetails---2>", SeatList+"--->"+FullFareOfAllSeats+"-->"+Seats);
        tvTotalFare.setText("\u20B9 "+FullFareOfAllSeats);
        L.m2("SeatNoDetails---3>", SeatList+"--->"+FullFareOfAllSeats);


        switch (SeatList.size())
        {

            case 1:

                //Set Seat Number from model class
                seatListModel = new SeatListModel();
                seatListModel = SeatList.get(0);
                tvPassenser1.setText(seatListModel.getSeatNumber());

                linear_layout2.setVisibility(View.GONE);
                linear_layout3.setVisibility(View.GONE);
                linear_layout4.setVisibility(View.GONE);
                linear_layout5.setVisibility(View.GONE);
                linear_layout6.setVisibility(View.GONE);
                break;

            case 2:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.GONE);
                linear_layout4.setVisibility(View.GONE);
                linear_layout5.setVisibility(View.GONE);
                linear_layout6.setVisibility(View.GONE);
                tvPassenser1.setText(SeatList.get(0).toString());
                tvPassenser2.setText(SeatList.get(1).toString());

                //Set Seat Number from model class
                seatListModel = new SeatListModel();
                seatListModel = SeatList.get(0);
                tvPassenser1.setText(seatListModel.getSeatNumber());

                //Set Seat Number from model class
                seatListModel1 = new SeatListModel();
                seatListModel1 = SeatList.get(1);
                tvPassenser2.setText(seatListModel1.getSeatNumber());

                  break;

            case 3:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.VISIBLE);
                linear_layout4.setVisibility(View.GONE);
                linear_layout5.setVisibility(View.GONE);
                linear_layout6.setVisibility(View.GONE);

                //Set Seat Number from model class
                seatListModel = new SeatListModel();
                seatListModel = SeatList.get(0);
                tvPassenser1.setText(seatListModel.getSeatNumber());

                //Set Seat Number from model class
                seatListModel1 = new SeatListModel();
                seatListModel1 = SeatList.get(1);
                tvPassenser2.setText(seatListModel1.getSeatNumber());

                //Set Seat Number from model class
                seatListModel2 = new SeatListModel();
                seatListModel2 = SeatList.get(2);
                tvPassenser3.setText(seatListModel2.getSeatNumber());


             break;

            case 4:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.VISIBLE);
                linear_layout4.setVisibility(View.VISIBLE);
                linear_layout5.setVisibility(View.GONE);
                linear_layout6.setVisibility(View.GONE);

                //Set Seat Number from model class
                seatListModel = new SeatListModel();
                seatListModel = SeatList.get(0);
                tvPassenser1.setText(seatListModel.getSeatNumber());

                //Set Seat Number from model class
                seatListModel1 = new SeatListModel();
                seatListModel1 = SeatList.get(1);
                tvPassenser2.setText(seatListModel1.getSeatNumber());

                //Set Seat Number from model class
                seatListModel2 = new SeatListModel();
                seatListModel2 = SeatList.get(2);
                tvPassenser3.setText(seatListModel2.getSeatNumber());

                //Set Seat Number from model class
                seatListModel3 = new SeatListModel();
                seatListModel3 = SeatList.get(3);
                tvPassenser4.setText(seatListModel3.getSeatNumber());

        break;

            case 5:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.VISIBLE);
                linear_layout4.setVisibility(View.VISIBLE);
                linear_layout5.setVisibility(View.VISIBLE);
                linear_layout6.setVisibility(View.GONE);

                //Set Seat Number from model class
                seatListModel = new SeatListModel();
                seatListModel = SeatList.get(0);
                tvPassenser1.setText(seatListModel.getSeatNumber());

                //Set Seat Number from model class
                seatListModel1 = new SeatListModel();
                seatListModel1 = SeatList.get(1);
                tvPassenser2.setText(seatListModel1.getSeatNumber());

                //Set Seat Number from model class
                seatListModel2 = new SeatListModel();
                seatListModel2 = SeatList.get(2);
                tvPassenser3.setText(seatListModel2.getSeatNumber());

                //Set Seat Number from model class
                seatListModel3 = new SeatListModel();
                seatListModel3 = SeatList.get(3);
                tvPassenser4.setText(seatListModel3.getSeatNumber());

                //Set Seat Number from model class
                seatListModel4 = new SeatListModel();
                seatListModel4 = SeatList.get(4);
                tvPassenser5.setText(seatListModel4.getSeatNumber());

            break;

            case 6:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.VISIBLE);
                linear_layout4.setVisibility(View.VISIBLE);
                linear_layout5.setVisibility(View.VISIBLE);
                linear_layout6.setVisibility(View.VISIBLE);

                //Set Seat Number from model class
                seatListModel = new SeatListModel();
                seatListModel = SeatList.get(0);
                tvPassenser1.setText(seatListModel.getSeatNumber());

                //Set Seat Number from model class
                seatListModel1 = new SeatListModel();
                seatListModel1 = SeatList.get(1);
                tvPassenser2.setText(seatListModel1.getSeatNumber());

                //Set Seat Number from model class
                seatListModel2 = new SeatListModel();
                seatListModel2 = SeatList.get(2);
                tvPassenser3.setText(seatListModel2.getSeatNumber());

                //Set Seat Number from model class
                seatListModel3 = new SeatListModel();
                seatListModel3 = SeatList.get(3);
                tvPassenser4.setText(seatListModel3.getSeatNumber());

                //Set Seat Number from model class
                seatListModel4 = new SeatListModel();
                seatListModel4 = SeatList.get(4);
                tvPassenser5.setText(seatListModel4.getSeatNumber());

                //Set Seat Number from model class
                seatListModel5 = new SeatListModel();
                seatListModel5 = SeatList.get(5);
                tvPassenser6.setText(seatListModel5.getSeatNumber());

          break;
            default:
                Toast.makeText(TravellersDetail.this, "Wrong Selection !!!", Toast.LENGTH_SHORT).show();
                break;
        }

        makePayment = (LinearLayout) findViewById(R.id.tv_make_payment);
        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(editEmailId);
                L.m2("check--1>", SeatList.size()+"");

                if(TextUtils.isEmpty(editEmailId.getText().toString().trim())){
                    editEmailId.requestFocus();
                    Toast.makeText(TravellersDetail.this, "Please enter email id !!!", Toast.LENGTH_SHORT).show();
                    } else if(TextUtils.isEmpty(editMobileNo.getText().toString().trim())){
                    editMobileNo.requestFocus();
                    Toast.makeText(TravellersDetail.this, "Please enter mobile number !!!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editPassenserAddress.getText().toString().trim())){
                    editPassenserAddress.requestFocus();
                    Toast.makeText(TravellersDetail.this, "Please enter address !!!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editPassenserCity.getText().toString().trim())){
                    editPassenserCity.requestFocus();
                    Toast.makeText(TravellersDetail.this, "Please enter address !!!", Toast.LENGTH_SHORT).show();
                } else if(idProofData == null){
                    Toast.makeText(TravellersDetail.this, "Please Select Id Proof !!!", Toast.LENGTH_LONG).show();
                } else if(TextUtils.isEmpty(editPassenserIdProofNo.getText().toString().trim())){
                    editPassenserIdProofNo.requestFocus();
                    Toast.makeText(TravellersDetail.this, "Please enter Id Proof !!!", Toast.LENGTH_SHORT).show();
                } else if(SeatList.size() != 0) {

                    switch (SeatList.size())
                    {
                        case 1:
                            if(TextUtils.isEmpty(editPassenser1.getText().toString().trim())){
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())){
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            }else if(boardingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }
                            else{

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                 BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel=BoardingPointArrayList.get(BoardingID1-1);
                                passengerDetails.add(new BookedSeatModel(genderPosition1, BoardingID1, DroppingID1,editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));
                                L.m2("check---2>", passengerDetails.toString()+"");
                                ProceedIntentActivity();
                            }
                            break;

                        case 2:
                            if(TextUtils.isEmpty(editPassenser1.getText().toString().trim())){
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())){
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place  !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser2.getText().toString().trim())){
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())){
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place  !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place  !!!", Toast.LENGTH_LONG).show();
                            }else{

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel=BoardingPointArrayList.get(BoardingID1-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition1, BoardingID1, DroppingID1,editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1=BoardingPointArrayList.get(BoardingID2-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition2, BoardingID2, DroppingID2,editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));
                                ProceedIntentActivity();

                            }
                            break;

                        case 3:
                            if(TextUtils.isEmpty(editPassenser1.getText().toString().trim())){
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())){
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            }else if(boardingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place  !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place  !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser2.getText().toString().trim())){
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())){
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser3.getText().toString().trim())){
                                editPassenser3.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge3.getText().toString().trim())){
                                editAge3.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else{

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel=BoardingPointArrayList.get(BoardingID1-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition1, BoardingID1, DroppingID1,editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1=BoardingPointArrayList.get(BoardingID2-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition2, BoardingID2, DroppingID2,editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap2 = new SeatListModel();
                                SeatsHasmap2 = SeatList.get(2);

                                BusDetailsModel boardingModel2 = new BusDetailsModel();
                                boardingModel2=BoardingPointArrayList.get(BoardingID3-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition3, BoardingID3, DroppingID3,editPassenser3.getText().toString().trim(),
                                        genderData3, editAge3.getText().toString().trim(), SeatsHasmap2.getSeatNumber(), SeatsHasmap2.getSeatTypeIdNew(), SeatsHasmap2.getSeatFare(),
                                        boardingModel2.getBoardingId(), "", "", "", "", ""));
                                ProceedIntentActivity();
                            }
                            break;

                        case 4:
                            if(TextUtils.isEmpty(editPassenser1.getText().toString().trim())){
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())){
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser2.getText().toString().trim())){
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())){
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser3.getText().toString().trim())){
                                editPassenser3.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge3.getText().toString().trim())){
                                editAge3.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser4.getText().toString().trim())){
                                editPassenser4.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge4.getText().toString().trim())){
                                editAge4.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            }else if(boardingData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else{

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel=BoardingPointArrayList.get(BoardingID1-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition1, BoardingID1, DroppingID1,editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1=BoardingPointArrayList.get(BoardingID2-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition2, BoardingID2, DroppingID2,editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap2 = new SeatListModel();
                                SeatsHasmap2 = SeatList.get(2);

                                BusDetailsModel boardingModel2 = new BusDetailsModel();
                                boardingModel2=BoardingPointArrayList.get(BoardingID3-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition3, BoardingID3, DroppingID3,editPassenser3.getText().toString().trim(),
                                        genderData3, editAge3.getText().toString().trim(), SeatsHasmap2.getSeatNumber(), SeatsHasmap2.getSeatTypeIdNew(), SeatsHasmap2.getSeatFare(),
                                        boardingModel2.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap3 = new SeatListModel();
                                SeatsHasmap3 = SeatList.get(3);

                                BusDetailsModel boardingModel3 = new BusDetailsModel();
                                boardingModel3=BoardingPointArrayList.get(BoardingID4-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition4, BoardingID4, DroppingID4,editPassenser4.getText().toString().trim(),
                                        genderData4, editAge4.getText().toString().trim(), SeatsHasmap3.getSeatNumber(), SeatsHasmap3.getSeatTypeIdNew(), SeatsHasmap3.getSeatFare(),
                                        boardingModel3.getBoardingId(), "", "", "", "", ""));
                                ProceedIntentActivity();
                            }
                            break;

                        case 5:
                            if(TextUtils.isEmpty(editPassenser1.getText().toString().trim())){
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())){
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            }else if(boardingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser2.getText().toString().trim())){
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())){
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser3.getText().toString().trim())){
                                editPassenser3.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge3.getText().toString().trim())){
                                editAge3.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            }else if(boardingData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser4.getText().toString().trim())){
                                editPassenser4.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge4.getText().toString().trim())){
                                editAge4.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser5.getText().toString().trim())){
                                editPassenser5.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData5 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge5.getText().toString().trim())){
                                editAge5.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            }else if(boardingData5 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData5 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else{


                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel=BoardingPointArrayList.get(BoardingID1-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition1, BoardingID1, DroppingID1,editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1=BoardingPointArrayList.get(BoardingID2-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition2, BoardingID2, DroppingID2,editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap2 = new SeatListModel();
                                SeatsHasmap2 = SeatList.get(2);

                                BusDetailsModel boardingModel2 = new BusDetailsModel();
                                boardingModel2=BoardingPointArrayList.get(BoardingID3-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition3, BoardingID3, DroppingID3,editPassenser3.getText().toString().trim(),
                                        genderData3, editAge3.getText().toString().trim(), SeatsHasmap2.getSeatNumber(), SeatsHasmap2.getSeatTypeIdNew(), SeatsHasmap2.getSeatFare(),
                                        boardingModel2.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap3 = new SeatListModel();
                                SeatsHasmap3 = SeatList.get(3);

                                BusDetailsModel boardingModel3 = new BusDetailsModel();
                                boardingModel3=BoardingPointArrayList.get(BoardingID4-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition4, BoardingID4, DroppingID4,editPassenser4.getText().toString().trim(),
                                        genderData4, editAge4.getText().toString().trim(), SeatsHasmap3.getSeatNumber(), SeatsHasmap3.getSeatTypeIdNew(), SeatsHasmap3.getSeatFare(),
                                        boardingModel3.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap4 = new SeatListModel();
                                SeatsHasmap4 = SeatList.get(4);

                                BusDetailsModel boardingModel4 = new BusDetailsModel();
                                boardingModel4=BoardingPointArrayList.get(BoardingID5-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition5, BoardingID5, DroppingID5,editPassenser5.getText().toString().trim(),
                                        genderData5, editAge5.getText().toString().trim(), SeatsHasmap4.getSeatNumber(), SeatsHasmap4.getSeatTypeIdNew(), SeatsHasmap4.getSeatFare(),
                                        boardingModel4.getBoardingId(), "", "", "", "", ""));

                                ProceedIntentActivity();
                            }

                            break;

                        case 6:
                            if(TextUtils.isEmpty(editPassenser1.getText().toString().trim())){
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())){
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData1 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser2.getText().toString().trim())){
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())){
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData2 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser3.getText().toString().trim())){
                                editPassenser3.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge3.getText().toString().trim())){
                                editAge3.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            }else if(boardingData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData3 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser4.getText().toString().trim())){
                                editPassenser4.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge4.getText().toString().trim())){
                                editAge4.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData4 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser5.getText().toString().trim())){
                                editPassenser5.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData5 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge5.getText().toString().trim())){
                                editAge5.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if(boardingData5 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData5 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else if(TextUtils.isEmpty(editPassenser6.getText().toString().trim())){
                                editPassenser6.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if(genderData6 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge6.getText().toString().trim())){
                                editAge6.requestFocus();
                                Toast.makeText(TravellersDetail.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            }else if(boardingData6 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            }else if(droppingData6 == null){
                                Toast.makeText(TravellersDetail.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            }else{



                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel=BoardingPointArrayList.get(BoardingID1-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition1, BoardingID1, DroppingID1,editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1=BoardingPointArrayList.get(BoardingID2-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition2, BoardingID2, DroppingID2,editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap2 = new SeatListModel();
                                SeatsHasmap2 = SeatList.get(2);

                                BusDetailsModel boardingModel2 = new BusDetailsModel();
                                boardingModel2=BoardingPointArrayList.get(BoardingID3-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition3, BoardingID3, DroppingID3,editPassenser3.getText().toString().trim(),
                                        genderData3, editAge3.getText().toString().trim(), SeatsHasmap2.getSeatNumber(), SeatsHasmap2.getSeatTypeIdNew(), SeatsHasmap2.getSeatFare(),
                                        boardingModel2.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap3 = new SeatListModel();
                                SeatsHasmap3 = SeatList.get(3);

                                BusDetailsModel boardingModel3 = new BusDetailsModel();
                                boardingModel3=BoardingPointArrayList.get(BoardingID4-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition4, BoardingID4, DroppingID4,editPassenser4.getText().toString().trim(),
                                        genderData4, editAge4.getText().toString().trim(), SeatsHasmap3.getSeatNumber(), SeatsHasmap3.getSeatTypeIdNew(), SeatsHasmap3.getSeatFare(),
                                        boardingModel3.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap4 = new SeatListModel();
                                SeatsHasmap4 = SeatList.get(4);

                                BusDetailsModel boardingModel4 = new BusDetailsModel();
                                boardingModel4=BoardingPointArrayList.get(BoardingID5-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition5, BoardingID5, DroppingID5,editPassenser5.getText().toString().trim(),
                                        genderData5, editAge5.getText().toString().trim(), SeatsHasmap4.getSeatNumber(), SeatsHasmap4.getSeatTypeIdNew(), SeatsHasmap4.getSeatFare(),
                                        boardingModel4.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap5 = new SeatListModel();
                                SeatsHasmap5 = SeatList.get(5);

                                BusDetailsModel boardingModel5 = new BusDetailsModel();
                                boardingModel5=BoardingPointArrayList.get(BoardingID6-1);

                                passengerDetails.add(new BookedSeatModel(genderPosition6, BoardingID6, DroppingID6,editPassenser6.getText().toString().trim(),
                                        genderData6, editAge6.getText().toString().trim(), SeatsHasmap5.getSeatNumber(), SeatsHasmap5.getSeatTypeIdNew(), SeatsHasmap5.getSeatFare(),
                                        boardingModel5.getBoardingId(), "", "", "", "", ""));

                                ProceedIntentActivity();
                            }
                            break;
                        default:
                            Toast.makeText(TravellersDetail.this, "Wrong selection !!!", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }
        });
    }

    private void ProceedIntentActivity() {

        Intent intent = new Intent(TravellersDetail.this, TravellersDetailConfirmation.class);
        intent.putExtra("passengerDetails", passengerDetails);
        intent.putExtra("SeatNoDetails", SeatList);
        intent.putExtra("HasmapSeats", Seats);
        intent.putExtra("BoardingPointArrayList", BoardingPointArrayList);
        intent.putExtra("DroppingPointArrayList", DroppingPointArrayList);
        intent.putExtra("FaresArrayList", FaresArrayList);
        intent.putExtra("AllSeatsFare", FullFareOfAllSeats);
        intent.putExtra("editEmailId", editEmailId.getText().toString().trim());
        intent.putExtra("editMobileNo", editMobileNo.getText().toString().trim());
        intent.putExtra("editPassenserAddress", editPassenserAddress.getText().toString().trim());
        intent.putExtra("editPassenserCity", editPassenserCity.getText().toString().trim());
        intent.putExtra("editPassenserIdProofNo", editPassenserIdProofNo.getText().toString().trim());
        intent.putExtra("spinnerCountry", countryPosition);
        intent.putExtra("spinnerIdProof", IdProofPosition);
        startActivity(intent);
    }


    private void initLinearLayout() {
        linear_layout1 = (LinearLayout) findViewById(R.id.linear_layout1);
        linear_layout2 = (LinearLayout) findViewById(R.id.linear_layout2);
        linear_layout3 = (LinearLayout) findViewById(R.id.linear_layout3);
        linear_layout4 = (LinearLayout) findViewById(R.id.linear_layout4);
        linear_layout5 = (LinearLayout) findViewById(R.id.linear_layout5);
        linear_layout6 = (LinearLayout) findViewById(R.id.linear_layout6);
    }

    private void initTextViewPassenserData() {
        tvTotalFare = (TextView) findViewById(R.id.total_fare);
    }


    private void initEditPassenserData() {

        tvPassenser1 =  findViewById(R.id.tv_seat_number1);
        tvPassenser2 =  findViewById(R.id.tv_seat_number2);
        tvPassenser3 =  findViewById(R.id.tv_seat_number3);
        tvPassenser4 =  findViewById(R.id.tv_seat_number4);
        tvPassenser5 =  findViewById(R.id.tv_seat_number5);
        tvPassenser6 =  findViewById(R.id.tv_seat_number6);
        editPassenser1 =  findViewById(R.id.edit_passenser1_name);
        editPassenser2 =  findViewById(R.id.edit_passenser2_name);
        editPassenser3 =  findViewById(R.id.edit_passenser3_name);
        editPassenser4 =  findViewById(R.id.edit_passenser4_name);
        editPassenser5 =  findViewById(R.id.edit_passenser5_name);
        editPassenser6 =  findViewById(R.id.edit_passenser6_name);
        editEmailId =  findViewById(R.id.edit_passenser_email);
        editMobileNo =  findViewById(R.id.edit_mobile_number);
        editPassenserAddress =  findViewById(R.id.edit_passenser_address);
        editPassenserCity =  findViewById(R.id.edit_passenser_city);
        editPassenserIdProofNo =  findViewById(R.id.edit_passenser_id_proof_no);
    }

    private void initEditAgeData() {

        editAge1 =  findViewById(R.id.edit_passenser1_age);
        editAge2 =  findViewById(R.id.edit_passenser2_age);
        editAge3 =  findViewById(R.id.edit_passenser3_age);
        editAge4 =  findViewById(R.id.edit_passenser4_age);
        editAge5 =  findViewById(R.id.edit_passenser5_age);
        editAge6 =  findViewById(R.id.edit_passenser6_age);
    }

    private void initSpinnerData() {

        spinnerGender1 =  findViewById(R.id.spinner_passenser1_gender);
        spinnerGender2 =  findViewById(R.id.spinner_passenser2_gender);
        spinnerGender3 =  findViewById(R.id.spinner_passenser3_gender);
        spinnerGender4 =  findViewById(R.id.spinner_passenser4_gender);
        spinnerGender5 =  findViewById(R.id.spinner_passenser5_gender);
        spinnerGender6 =  findViewById(R.id.spinner_passenser6_gender);
        spinnerCountry =  findViewById(R.id.spinner_passenser_country);
        spinnerIdProof =  findViewById(R.id.spinner_passenser_id_card);
        spinnerBoardingPassenger1 =  findViewById(R.id.spinner_boarding_passenser1);
        spinnerBoardingPassenger2 =  findViewById(R.id.spinner_boarding_passenser2);
        spinnerBoardingPassenger3 =  findViewById(R.id.spinner_boarding_passenser3);
        spinnerBoardingPassenger4 =  findViewById(R.id.spinner_boarding_passenser4);
        spinnerBoardingPassenger5 =  findViewById(R.id.spinner_boarding_passenser5);
        spinnerBoardingPassenger6 =  findViewById(R.id.spinner_boarding_passenser6);
        spinnerDroppingPassenger1 =  findViewById(R.id.spinner_dropping_passenser1);
        spinnerDroppingPassenger2 =  findViewById(R.id.spinner_dropping_passenser2);
        spinnerDroppingPassenger3 =  findViewById(R.id.spinner_dropping_passenser3);
        spinnerDroppingPassenger4 =  findViewById(R.id.spinner_dropping_passenser4);
        spinnerDroppingPassenger5 =  findViewById(R.id.spinner_dropping_passenser5);
        spinnerDroppingPassenger6 =  findViewById(R.id.spinner_dropping_passenser6);


        ArrayAdapter GenderAdaptor1 = new ArrayAdapter(this,R.layout.spinner_textview_layout,Gender);
        GenderAdaptor1.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender1.setAdapter(GenderAdaptor1);

        ArrayAdapter GenderAdaptor2 = new ArrayAdapter(this,R.layout.spinner_textview_layout,Gender);
        GenderAdaptor2.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender2.setAdapter(GenderAdaptor2);

        ArrayAdapter GenderAdaptor3 = new ArrayAdapter(this,R.layout.spinner_textview_layout,Gender);
        GenderAdaptor3.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender3.setAdapter(GenderAdaptor3);

        ArrayAdapter GenderAdaptor4 = new ArrayAdapter(this,R.layout.spinner_textview_layout,Gender);
        GenderAdaptor4.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender4.setAdapter(GenderAdaptor4);

        ArrayAdapter GenderAdaptor5 = new ArrayAdapter(this,R.layout.spinner_textview_layout,Gender);
        GenderAdaptor5.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender5.setAdapter(GenderAdaptor5);

        ArrayAdapter GenderAdaptor6 = new ArrayAdapter(this,R.layout.spinner_textview_layout,Gender);
        GenderAdaptor6.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender6.setAdapter(GenderAdaptor6);

        ArrayAdapter CountryAdaptor = new ArrayAdapter(this,R.layout.spinner_textview_layout, Country);
        CountryAdaptor.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerCountry.setAdapter(CountryAdaptor);

        ArrayAdapter IdProofAdaptor = new ArrayAdapter(this,R.layout.spinner_textview_layout, IdProof);
        IdProofAdaptor.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerIdProof.setAdapter(IdProofAdaptor);

        ArrayAdapter BordingAdaptor1 = new ArrayAdapter(this,R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor1.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger1.setAdapter(BordingAdaptor1);

        ArrayAdapter DroppingAdaptor1 = new ArrayAdapter(this,R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor1.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger1.setAdapter(DroppingAdaptor1);

        ArrayAdapter BordingAdaptor2 = new ArrayAdapter(this,R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor2.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger2.setAdapter(BordingAdaptor2);

        ArrayAdapter DroppingAdaptor2 = new ArrayAdapter(this,R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor2.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger2.setAdapter(DroppingAdaptor2);

        ArrayAdapter BordingAdaptor3 = new ArrayAdapter(this,R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor3.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger3.setAdapter(BordingAdaptor3);

        ArrayAdapter DroppingAdaptor3 = new ArrayAdapter(this,R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor3.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger3.setAdapter(DroppingAdaptor3);

        ArrayAdapter BordingAdaptor4 = new ArrayAdapter(this,R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor4.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger4.setAdapter(BordingAdaptor4);

        ArrayAdapter DroppingAdaptor4 = new ArrayAdapter(this,R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor4.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger4.setAdapter(DroppingAdaptor4);

        ArrayAdapter BordingAdaptor5 = new ArrayAdapter(this,R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor5.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger5.setAdapter(BordingAdaptor5);

        ArrayAdapter DroppingAdaptor5 = new ArrayAdapter(this,R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor5.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger5.setAdapter(DroppingAdaptor5);

        ArrayAdapter BordingAdaptor6 = new ArrayAdapter(this,R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor6.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger6.setAdapter(BordingAdaptor6);

        ArrayAdapter DroppingAdaptor6 = new ArrayAdapter(this,R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor6.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger6.setAdapter(DroppingAdaptor6);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryPosition = position;
                countryData = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerIdProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    idProofData = null;
                } else {
                    IdProofPosition = position;
                    idProofData = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    genderData1 = null;
                }else {
                    genderPosition1 = position;
                    genderData1 = String.valueOf(position);
                    if(genderData1.equalsIgnoreCase("Male")){
                        genderData1 = "M";
                    }else if(genderData1.equalsIgnoreCase("Female")){
                        genderData1 = "F";
                    }
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    genderData2 = null;
                }else {
                    genderPosition2 = position;
                    genderData2 = parent.getItemAtPosition(position).toString();
                    if(genderData2.equalsIgnoreCase("Male")){
                        genderData2 = "M";
                    }else if(genderData2.equalsIgnoreCase("Female")){
                        genderData2 = "F";
                    }
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    genderData3 = null;
                }else {
                    genderPosition3 = position;
                    genderData3 = parent.getItemAtPosition(position).toString();
                    if(genderData3.equalsIgnoreCase("Male")){
                        genderData3 = "M";
                    }else if(genderData3.equalsIgnoreCase("Female")){
                        genderData3 = "F";
                    }
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    genderData4 = null;
                }else {
                    genderPosition4 = position;
                    genderData4 = parent.getItemAtPosition(position).toString();
                    if(genderData4.equalsIgnoreCase("Male")){
                        genderData4 = "M";
                    }else if(genderData4.equalsIgnoreCase("Female")){
                        genderData4 = "F";
                    }
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    genderData5 = null;
                }else {
                    genderPosition5 = position;
                    genderData5 = parent.getItemAtPosition(position).toString();
                    if(genderData5.equalsIgnoreCase("Male")){
                        genderData1 = "M";
                    }else if(genderData1.equalsIgnoreCase("Female")){
                        genderData1 = "F";

                    }
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    genderData6 = null;
                }else {
                    genderPosition6 = position;
                    genderData6 = parent.getItemAtPosition(position).toString();
                    if(genderData6.equalsIgnoreCase("Male")){
                        genderData6 = "M";
                    }else if(genderData6.equalsIgnoreCase("Female")){
                        genderData6 = "F";
                    }
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    boardingData1 = null;
                }else {
                    BoardingID1 = position;
                    boardingData1 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    boardingData2 = null;
                }else {
                    BoardingID2 = position;
                    boardingData2 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    boardingData3 = null;
                }else {
                    BoardingID3 = position;
                    boardingData3 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    boardingData4 = null;
                }else {
                    BoardingID4 = position;
                    boardingData4 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    boardingData5 = null;
                }else {
                    BoardingID5 = position;
                    boardingData5 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    boardingData6 = null;
                }else {
                    BoardingID6 = position;
                    boardingData6 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    droppingData1 = null;
                }else {
                    DroppingID1 = position;
                    droppingData1 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    droppingData2 = null;
                }else {
                    DroppingID2 = position;
                    droppingData2 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    droppingData3 = null;
                }else {
                    DroppingID3 = position;
                    droppingData3 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    droppingData4 = null;
                }else {
                    DroppingID4 = position;
                    droppingData4 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    droppingData5 = null;
                } else {
                    DroppingID5 = position;
                    droppingData5 = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    droppingData6 = null;
                }else {
                    DroppingID6 = position;
                    droppingData6 = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
