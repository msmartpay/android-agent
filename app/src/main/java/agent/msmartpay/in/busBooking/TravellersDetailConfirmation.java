package agent.msmartpay.in.busBooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import java.util.ArrayList;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;
import agent.msmartpay.in.utility.TextDrawable;

import static agent.msmartpay.in.busBooking.BusBookingSearchActivity_H.passenser;
import static agent.msmartpay.in.busBooking.SeatSelectionActivity.seatList;

/**
 * Created by IRFAN on 8/1/2017.
 */

public class TravellersDetailConfirmation extends BaseActivity {

    private EditText editPassenser1 = null, editPassenser2 = null, editPassenser3 = null, editPassenser4 = null, editPassenser5 = null, editPassenser6 = null;
    private EditText editEmailId, editMobileNo, editPassenserAddress, editPassenserCity, editPassenserIdProofNo;
    private Spinner spinnerGender1, spinnerGender2, spinnerGender3, spinnerGender4, spinnerGender5, spinnerGender6, spinnerCountry, spinnerIdProof;
    private Spinner spinnerBoardingPassenger1, spinnerBoardingPassenger2, spinnerBoardingPassenger3, spinnerBoardingPassenger4, spinnerBoardingPassenger5, spinnerBoardingPassenger6;
    private Spinner spinnerDroppingPassenger1, spinnerDroppingPassenger2, spinnerDroppingPassenger3, spinnerDroppingPassenger4, spinnerDroppingPassenger5, spinnerDroppingPassenger6;
    private String[] Gender = {"Select Gender...", "Male", "Female"};
    private String[] IdProof = {"Select Id Proof...", "Passport", "Voter ID", "Driving License", "PAN Card"};
    private String[] Country = {"India"};
    private String genderData1, genderData2, genderData3, genderData4, genderData5, genderData6, countryData, idProofData;
    private String boardingData1, boardingData2, boardingData3, boardingData4, boardingData5, boardingData6, droppingData1, droppingData2, droppingData3, droppingData4, droppingData5, droppingData6;
    private EditText editAge1, editAge2, editAge3, editAge4, editAge5, editAge6;
    private EditText tvPassenser1, tvPassenser2, tvPassenser3, tvPassenser4, tvPassenser5, tvPassenser6;
    private TextView tvTotalFare;
    private LinearLayout makePayment;
    private ArrayList<SeatListModel> SeatList = new ArrayList<SeatListModel>();
    private ArrayList<BusDetailsModel> BoardingPointArrayList = new ArrayList<BusDetailsModel>();
    private ArrayList<BusDetailsModel> DroppingPointArrayList = new ArrayList<BusDetailsModel>();
    private ArrayList<BusDetailsModel> FaresArrayList = new ArrayList<BusDetailsModel>();
    private String SeatNoDetails, FullFareOfAllSeats;
    private LinearLayout linear_layout1, linear_layout2, linear_layout3, linear_layout4, linear_layout5, linear_layout6;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_blocking_seat_details = HttpURL.SEAT_BLOCKING_URL;
    private String url_confirm_booking_seat = HttpURL.SEAT_CONFIRM_BOOKING_URL;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String ScheduleId, StationId, TransportId, DateOfJourney, UserTrackId, AvailableBal, AgentID, FromSource,
            ToDestination, DepartureTime, ArrivalTime, SeatType, BusNumber, BusName, ConvenienceFee, SeatTypeId, ServiceTax,
            TravellerName;
    private ArrayList<String> BoardingPointList = new ArrayList<>();
    private ArrayList<String> DroppingPointList = new ArrayList<>();
    private int BoardingID1, BoardingID2, BoardingID3, BoardingID4, BoardingID5, BoardingID6;
    private int DroppingID1, DroppingID2, DroppingID3, DroppingID4, DroppingID5, DroppingID6;
    //private ArrayList<HashMap<String, String>> Seats;
    ArrayList<SeatModel> Seats;
    private JSONArray jsonArrayFinal;
    private ArrayList<BookedSeatModel> passengerDetailsConfirm = new ArrayList<BookedSeatModel>();
    private ArrayList<BookedSeatModel> passengerDetails = new ArrayList<BookedSeatModel>();
    private BookedSeatModel bookedSeatModel1, bookedSeatModel2, bookedSeatModel3, bookedSeatModel4, bookedSeatModel5, bookedSeatModel6;
    private Button btnEditContactDetails;
    private Button btnEditSeat1, btnEditSeat2, btnEditSeat3, btnEditSeat4, btnEditSeat5, btnEditSeat6;
    private LinearLayout linearLayoutHide1, linearLayoutHide2, linearLayoutHide3, linearLayoutHide4, linearLayoutHide5, linearLayoutHide6;
    private SeatListModel seatListModel, seatListModel1, seatListModel2, seatListModel3, seatListModel4, seatListModel5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_travellers_detail_confirm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Confirmation");
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
        initLinearLayoutHideShow();
        initEditableButton();

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

        passengerDetailsConfirm = (ArrayList<BookedSeatModel>) getIntent().getSerializableExtra("passengerDetails");
        L.m2("passengerDetailConfirm>", passengerDetailsConfirm.toString());
        SeatList = (ArrayList<SeatListModel>) getIntent().getSerializableExtra("SeatNoDetails");
        FaresArrayList = (ArrayList<BusDetailsModel>) getIntent().getSerializableExtra("FaresArrayList");
        FullFareOfAllSeats = getIntent().getStringExtra("AllSeatsFare");
        Seats = getIntent().getParcelableArrayListExtra("HasmapSeats");
        tvTotalFare.setText("\u20B9 " + FullFareOfAllSeats);
        L.m2("SeatNoDetails---6>", SeatList + "--->" + FullFareOfAllSeats + "-->" + Seats);

        editEmailId.setText(getIntent().getStringExtra("editEmailId"));
        editMobileNo.setText(getIntent().getStringExtra("editMobileNo"));
        editPassenserAddress.setText(getIntent().getStringExtra("editPassenserAddress"));
        editPassenserCity.setText(getIntent().getStringExtra("editPassenserCity"));
        editPassenserIdProofNo.setText(getIntent().getStringExtra("editPassenserIdProofNo"));
        spinnerCountry.setSelection(getIntent().getIntExtra("editPassenserIdProofNo", 0));
        spinnerIdProof.setSelection(getIntent().getIntExtra("spinnerIdProof", 0));

        editEmailId.setEnabled(false);
        editEmailId.setFocusableInTouchMode(false);
        editMobileNo.setEnabled(false);
        editMobileNo.setFocusableInTouchMode(false);
        editPassenserAddress.setEnabled(false);
        editPassenserAddress.setFocusableInTouchMode(false);
        editPassenserCity.setEnabled(false);
        editPassenserCity.setFocusableInTouchMode(false);
        editPassenserIdProofNo.setEnabled(false);
        editPassenserIdProofNo.setFocusableInTouchMode(false);
        spinnerCountry.setEnabled(false);
        spinnerIdProof.setEnabled(false);

        btnEditContactDetails =  findViewById(R.id.btn_contact_deatails);
        btnEditContactDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEmailId.setEnabled(true);
                editEmailId.setFocusableInTouchMode(true);
                editEmailId.requestFocus();
                showKeyBoard();
                editMobileNo.setEnabled(true);
                editMobileNo.setFocusableInTouchMode(true);
                editPassenserAddress.setEnabled(true);
                editPassenserAddress.setFocusableInTouchMode(true);
                editPassenserCity.setEnabled(true);
                editPassenserCity.setFocusableInTouchMode(true);
                editPassenserIdProofNo.setEnabled(true);
                editPassenserIdProofNo.setFocusableInTouchMode(true);
                spinnerCountry.setEnabled(true);
                spinnerIdProof.setEnabled(true);
            }
        });


        btnEditSeat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassenser1.setEnabled(true);
                editPassenser1.setFocusableInTouchMode(true);
                editPassenser1.requestFocus();
                showKeyBoard();
                spinnerGender1.setEnabled(true);
                editAge1.setEnabled(true);
                editAge1.setFocusableInTouchMode(true);
                spinnerBoardingPassenger1.setEnabled(true);
                spinnerDroppingPassenger1.setEnabled(true);
            }
        });

        btnEditSeat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassenser2.setEnabled(true);
                editPassenser2.setFocusableInTouchMode(true);
                editPassenser2.requestFocus();
                showKeyBoard();
                spinnerGender2.setEnabled(true);
                editAge2.setEnabled(true);
                editAge2.setFocusableInTouchMode(true);
                spinnerBoardingPassenger2.setEnabled(true);
                spinnerDroppingPassenger2.setEnabled(true);
            }
        });

        btnEditSeat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassenser3.setEnabled(true);
                editPassenser3.setFocusableInTouchMode(true);
                editPassenser3.requestFocus();
                showKeyBoard();
                spinnerGender3.setEnabled(true);
                editAge3.setEnabled(true);
                editAge3.setFocusableInTouchMode(true);
                spinnerBoardingPassenger3.setEnabled(true);
                spinnerDroppingPassenger3.setEnabled(true);
            }
        });

        btnEditSeat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassenser4.setEnabled(true);
                editPassenser4.setFocusableInTouchMode(true);
                editPassenser4.requestFocus();
                showKeyBoard();
                spinnerGender4.setEnabled(true);
                editAge4.setEnabled(true);
                editAge4.setFocusableInTouchMode(true);
                spinnerBoardingPassenger4.setEnabled(true);
                spinnerDroppingPassenger4.setEnabled(true);
            }
        });

        btnEditSeat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassenser5.setEnabled(true);
                editPassenser5.setFocusableInTouchMode(true);
                editPassenser5.requestFocus();
                showKeyBoard();
                spinnerGender5.setEnabled(true);
                editAge5.setEnabled(true);
                editAge5.setFocusableInTouchMode(true);
                spinnerBoardingPassenger5.setEnabled(true);
                spinnerDroppingPassenger5.setEnabled(true);
            }
        });

        btnEditSeat6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassenser6.setEnabled(true);
                editPassenser6.setFocusableInTouchMode(true);
                editPassenser6.requestFocus();
                showKeyBoard();
                spinnerGender6.setEnabled(true);
                editAge6.setEnabled(true);
                editAge6.setFocusableInTouchMode(true);
                spinnerBoardingPassenger6.setEnabled(true);
                spinnerDroppingPassenger6.setEnabled(true);
            }
        });

        switch (SeatList.size()) {
            case 1:
                linear_layout2.setVisibility(View.GONE);
                linear_layout3.setVisibility(View.GONE);
                linear_layout4.setVisibility(View.GONE);
                linear_layout5.setVisibility(View.GONE);
                linear_layout6.setVisibility(View.GONE);

                //For Set Data from previous page
                setDataForSeat1();

                break;

            case 2:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.GONE);
                linear_layout4.setVisibility(View.GONE);
                linear_layout5.setVisibility(View.GONE);
                linear_layout6.setVisibility(View.GONE);

                //For Set Data from previous page
                setDataForSeat1();
                setDataForSeat2();

                break;

            case 3:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.VISIBLE);
                linear_layout4.setVisibility(View.GONE);
                linear_layout5.setVisibility(View.GONE);
                linear_layout6.setVisibility(View.GONE);


                //For Set Data from previous page
                setDataForSeat1();
                setDataForSeat2();
                setDataForSeat3();

                break;

            case 4:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.VISIBLE);
                linear_layout4.setVisibility(View.VISIBLE);
                linear_layout5.setVisibility(View.GONE);
                linear_layout6.setVisibility(View.GONE);

                //For Set Data from previous page
                setDataForSeat1();
                setDataForSeat2();
                setDataForSeat3();
                setDataForSeat4();
                break;

            case 5:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.VISIBLE);
                linear_layout4.setVisibility(View.VISIBLE);
                linear_layout5.setVisibility(View.VISIBLE);
                linear_layout6.setVisibility(View.GONE);

                //For Set Data from previous page
                setDataForSeat1();
                setDataForSeat2();
                setDataForSeat3();
                setDataForSeat4();
                setDataForSeat5();
                break;

            case 6:
                linear_layout2.setVisibility(View.VISIBLE);
                linear_layout3.setVisibility(View.VISIBLE);
                linear_layout4.setVisibility(View.VISIBLE);
                linear_layout5.setVisibility(View.VISIBLE);
                linear_layout6.setVisibility(View.VISIBLE);

                //For Set Data from previous page
                setDataForSeat1();
                setDataForSeat2();
                setDataForSeat3();
                setDataForSeat4();
                setDataForSeat5();
                setDataForSeat6();
                break;
            default:
                Toast.makeText(TravellersDetailConfirmation.this, "Wrong Selection !!!", Toast.LENGTH_SHORT).show();
                break;
        }

        makePayment = (LinearLayout) findViewById(R.id.tv_make_payment);
        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(editEmailId);
                L.m2("check--1>", SeatList.size() + "");

                if (TextUtils.isEmpty(editEmailId.getText().toString().trim())) {
                    editEmailId.requestFocus();
                    Toast.makeText(TravellersDetailConfirmation.this, "Please enter email id !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(editMobileNo.getText().toString().trim())) {
                    editMobileNo.requestFocus();
                    Toast.makeText(TravellersDetailConfirmation.this, "Please enter mobile number !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(editPassenserAddress.getText().toString().trim())) {
                    editPassenserAddress.requestFocus();
                    Toast.makeText(TravellersDetailConfirmation.this, "Please enter address !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(editPassenserCity.getText().toString().trim())) {
                    editPassenserCity.requestFocus();
                    Toast.makeText(TravellersDetailConfirmation.this, "Please enter address !!!", Toast.LENGTH_SHORT).show();
                } else if (idProofData == null) {
                    Toast.makeText(TravellersDetailConfirmation.this, "Please Select Id Proof !!!", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(editPassenserIdProofNo.getText().toString().trim())) {
                    editPassenserIdProofNo.requestFocus();
                    Toast.makeText(TravellersDetailConfirmation.this, "Please enter Id Proof !!!", Toast.LENGTH_SHORT).show();
                } else if (SeatList.size() != 0) {

                    switch (SeatList.size()) {
                        case 1:
                            if (TextUtils.isEmpty(editPassenser1.getText().toString().trim())) {
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())) {
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else {

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel = BoardingPointArrayList.get(BoardingID1 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));
                                L.m2("check---2>", passengerDetails.toString() + "");

                                //JsonCalling
                                ConfirmJsonRequest();
                            }
                            break;

                        case 2:
                            if (TextUtils.isEmpty(editPassenser1.getText().toString().trim())) {
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())) {
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser2.getText().toString().trim())) {
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())) {
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else {

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel = BoardingPointArrayList.get(BoardingID1 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1 = BoardingPointArrayList.get(BoardingID2 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));
//JsonCalling
                                ConfirmJsonRequest();

                            }
                            break;

                        case 3:
                            if (TextUtils.isEmpty(editPassenser1.getText().toString().trim())) {
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())) {
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser2.getText().toString().trim())) {
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())) {
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser3.getText().toString().trim())) {
                                editPassenser3.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge3.getText().toString().trim())) {
                                editAge3.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else {

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel = BoardingPointArrayList.get(BoardingID1 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1 = BoardingPointArrayList.get(BoardingID2 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));


                                SeatListModel SeatsHasmap2 = new SeatListModel();
                                SeatsHasmap2 = SeatList.get(2);

                                BusDetailsModel boardingModel2 = new BusDetailsModel();
                                boardingModel2 = BoardingPointArrayList.get(BoardingID3 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser3.getText().toString().trim(),
                                        genderData3, editAge3.getText().toString().trim(), SeatsHasmap2.getSeatNumber(), SeatsHasmap2.getSeatTypeIdNew(), SeatsHasmap2.getSeatFare(),
                                        boardingModel2.getBoardingId(), "", "", "", "", ""));

                                //JsonCalling
                                ConfirmJsonRequest();
                            }
                            break;

                        case 4:
                            if (TextUtils.isEmpty(editPassenser1.getText().toString().trim())) {
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())) {
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser2.getText().toString().trim())) {
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())) {
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser3.getText().toString().trim())) {
                                editPassenser3.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge3.getText().toString().trim())) {
                                editAge3.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser4.getText().toString().trim())) {
                                editPassenser4.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name  !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge4.getText().toString().trim())) {
                                editAge4.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else {


                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel = BoardingPointArrayList.get(BoardingID1 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1 = BoardingPointArrayList.get(BoardingID2 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));


                                SeatListModel SeatsHasmap2 = new SeatListModel();
                                SeatsHasmap2 = SeatList.get(2);

                                BusDetailsModel boardingModel2 = new BusDetailsModel();
                                boardingModel2 = BoardingPointArrayList.get(BoardingID3 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser3.getText().toString().trim(),
                                        genderData3, editAge3.getText().toString().trim(), SeatsHasmap2.getSeatNumber(), SeatsHasmap2.getSeatTypeIdNew(), SeatsHasmap2.getSeatFare(),
                                        boardingModel2.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap3 = new SeatListModel();
                                SeatsHasmap3 = SeatList.get(3);

                                BusDetailsModel boardingModel3 = new BusDetailsModel();
                                boardingModel3 = BoardingPointArrayList.get(BoardingID4 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser4.getText().toString().trim(),
                                        genderData4, editAge4.getText().toString().trim(), SeatsHasmap3.getSeatNumber(), SeatsHasmap3.getSeatTypeIdNew(), SeatsHasmap3.getSeatFare(),
                                        boardingModel3.getBoardingId(), "", "", "", "", ""));

                                //JsonCalling
                                ConfirmJsonRequest();
                            }
                            break;

                        case 5:
                            if (TextUtils.isEmpty(editPassenser1.getText().toString().trim())) {
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())) {
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser2.getText().toString().trim())) {
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())) {
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser3.getText().toString().trim())) {
                                editPassenser3.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge3.getText().toString().trim())) {
                                editAge3.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser4.getText().toString().trim())) {
                                editPassenser4.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge4.getText().toString().trim())) {
                                editAge4.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser5.getText().toString().trim())) {
                                editPassenser5.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData5 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge5.getText().toString().trim())) {
                                editAge5.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData5 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData5 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else {

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel = BoardingPointArrayList.get(BoardingID1 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1 = BoardingPointArrayList.get(BoardingID2 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));


                                SeatListModel SeatsHasmap2 = new SeatListModel();
                                SeatsHasmap2 = SeatList.get(2);

                                BusDetailsModel boardingModel2 = new BusDetailsModel();
                                boardingModel2 = BoardingPointArrayList.get(BoardingID3 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser3.getText().toString().trim(),
                                        genderData3, editAge3.getText().toString().trim(), SeatsHasmap2.getSeatNumber(), SeatsHasmap2.getSeatTypeIdNew(), SeatsHasmap2.getSeatFare(),
                                        boardingModel2.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap3 = new SeatListModel();
                                SeatsHasmap3 = SeatList.get(3);

                                BusDetailsModel boardingModel3 = new BusDetailsModel();
                                boardingModel3 = BoardingPointArrayList.get(BoardingID4 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser4.getText().toString().trim(),
                                        genderData4, editAge4.getText().toString().trim(), SeatsHasmap3.getSeatNumber(), SeatsHasmap3.getSeatTypeIdNew(), SeatsHasmap3.getSeatFare(),
                                        boardingModel3.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap4 = new SeatListModel();
                                SeatsHasmap4 = SeatList.get(4);

                                BusDetailsModel boardingModel4 = new BusDetailsModel();
                                boardingModel4 = BoardingPointArrayList.get(BoardingID5 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser5.getText().toString().trim(),
                                        genderData5, editAge5.getText().toString().trim(), SeatsHasmap4.getSeatNumber(), SeatsHasmap4.getSeatTypeIdNew(), SeatsHasmap4.getSeatFare(),
                                        boardingModel4.getBoardingId(), "", "", "", "", ""));

                                //JsonCalling
                                ConfirmJsonRequest();
                            }

                            break;

                        case 6:
                            if (TextUtils.isEmpty(editPassenser1.getText().toString().trim())) {
                                editPassenser1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge1.getText().toString().trim())) {
                                editAge1.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData1 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser2.getText().toString().trim())) {
                                editPassenser2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge2.getText().toString().trim())) {
                                editAge2.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData2 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser3.getText().toString().trim())) {
                                editPassenser3.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge3.getText().toString().trim())) {
                                editAge3.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData3 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser4.getText().toString().trim())) {
                                editPassenser4.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge4.getText().toString().trim())) {
                                editAge4.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData4 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser5.getText().toString().trim())) {
                                editPassenser5.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData5 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge5.getText().toString().trim())) {
                                editAge5.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData5 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData5 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editPassenser6.getText().toString().trim())) {
                                editPassenser6.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Name !!!", Toast.LENGTH_SHORT).show();
                            } else if (genderData6 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select gender !!!", Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(editAge6.getText().toString().trim())) {
                                editAge6.requestFocus();
                                Toast.makeText(TravellersDetailConfirmation.this, "Please enter Age !!!", Toast.LENGTH_SHORT).show();
                            } else if (boardingData6 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Boarding Place !!!", Toast.LENGTH_LONG).show();
                            } else if (droppingData6 == null) {
                                Toast.makeText(TravellersDetailConfirmation.this, "Please Select Dropping Place !!!", Toast.LENGTH_LONG).show();
                            } else {

                                SeatListModel SeatsHasmap = new SeatListModel();
                                SeatsHasmap = SeatList.get(0);

                                BusDetailsModel boardingModel = new BusDetailsModel();
                                boardingModel = BoardingPointArrayList.get(BoardingID1 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser1.getText().toString().trim(),
                                        genderData1, editAge1.getText().toString().trim(), SeatsHasmap.getSeatNumber(), SeatsHasmap.getSeatTypeIdNew(), SeatsHasmap.getSeatFare(),
                                        boardingModel.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap1 = new SeatListModel();
                                SeatsHasmap1 = SeatList.get(1);

                                BusDetailsModel boardingModel1 = new BusDetailsModel();
                                boardingModel1 = BoardingPointArrayList.get(BoardingID2 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser2.getText().toString().trim(),
                                        genderData2, editAge2.getText().toString().trim(), SeatsHasmap1.getSeatNumber(), SeatsHasmap1.getSeatTypeIdNew(), SeatsHasmap1.getSeatFare(),
                                        boardingModel1.getBoardingId(), "", "", "", "", ""));


                                SeatListModel SeatsHasmap2 = new SeatListModel();
                                SeatsHasmap2 = SeatList.get(2);

                                BusDetailsModel boardingModel2 = new BusDetailsModel();
                                boardingModel2 = BoardingPointArrayList.get(BoardingID3 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser3.getText().toString().trim(),
                                        genderData3, editAge3.getText().toString().trim(), SeatsHasmap2.getSeatNumber(), SeatsHasmap2.getSeatTypeIdNew(), SeatsHasmap2.getSeatFare(),
                                        boardingModel2.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap3 = new SeatListModel();
                                SeatsHasmap3 = SeatList.get(3);

                                BusDetailsModel boardingModel3 = new BusDetailsModel();
                                boardingModel3 = BoardingPointArrayList.get(BoardingID4 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser4.getText().toString().trim(),
                                        genderData4, editAge4.getText().toString().trim(), SeatsHasmap3.getSeatNumber(), SeatsHasmap3.getSeatTypeIdNew(), SeatsHasmap3.getSeatFare(),
                                        boardingModel3.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap4 = new SeatListModel();
                                SeatsHasmap4 = SeatList.get(4);

                                BusDetailsModel boardingModel4 = new BusDetailsModel();
                                boardingModel4 = BoardingPointArrayList.get(BoardingID5 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser5.getText().toString().trim(),
                                        genderData5, editAge5.getText().toString().trim(), SeatsHasmap4.getSeatNumber(), SeatsHasmap4.getSeatTypeIdNew(), SeatsHasmap4.getSeatFare(),
                                        boardingModel4.getBoardingId(), "", "", "", "", ""));

                                SeatListModel SeatsHasmap5 = new SeatListModel();
                                SeatsHasmap5 = SeatList.get(5);

                                BusDetailsModel boardingModel5 = new BusDetailsModel();
                                boardingModel5 = BoardingPointArrayList.get(BoardingID6 - 1);

                                passengerDetails.add(new BookedSeatModel(editPassenser6.getText().toString().trim(),
                                        genderData6, editAge6.getText().toString().trim(), SeatsHasmap5.getSeatNumber(), SeatsHasmap5.getSeatTypeIdNew(), SeatsHasmap5.getSeatFare(),
                                        boardingModel5.getBoardingId(), "", "", "", "", ""));
                                //JsonCalling
                                ConfirmJsonRequest();
                            }
                            break;
                    }

                }
            }
        });
    }

    //For Set Passengers details according from previous activity !!!
    private void setDataForSeat1() {

        //Set Seat Number from model class
        seatListModel = new SeatListModel();
        seatListModel = SeatList.get(0);
        tvPassenser1.setText(seatListModel.getSeatNumber());

        bookedSeatModel1 = new BookedSeatModel();
        bookedSeatModel1 = passengerDetailsConfirm.get(0);
        editPassenser1.setText(bookedSeatModel1.getPassengerName());
        spinnerGender1.setSelection(bookedSeatModel1.getGenderPosition());
        editAge1.setText(bookedSeatModel1.getAge());
        spinnerBoardingPassenger1.setSelection(bookedSeatModel1.getBoardingPosition());
        spinnerDroppingPassenger1.setSelection(bookedSeatModel1.getDroppingPosition());
        spinnerGender1.setEnabled(false);
        spinnerBoardingPassenger1.setEnabled(false);
        spinnerDroppingPassenger1.setEnabled(false);
    }

    private void setDataForSeat2() {

        //Set Seat Number from model class
        seatListModel1 = new SeatListModel();
        seatListModel1 = SeatList.get(1);
        tvPassenser2.setText(seatListModel1.getSeatNumber());

        bookedSeatModel2 = new BookedSeatModel();
        bookedSeatModel2 = passengerDetailsConfirm.get(1);
        editPassenser2.setText(bookedSeatModel2.getPassengerName());
        spinnerGender2.setSelection(bookedSeatModel2.getGenderPosition());
        editAge2.setText(bookedSeatModel2.getAge());
        spinnerBoardingPassenger2.setSelection(bookedSeatModel2.getBoardingPosition());
        spinnerDroppingPassenger2.setSelection(bookedSeatModel2.getDroppingPosition());
        spinnerGender2.setEnabled(false);
        spinnerBoardingPassenger2.setEnabled(false);
        spinnerDroppingPassenger2.setEnabled(false);
    }

    private void setDataForSeat3() {

        //Set Seat Number from model class
        seatListModel2 = new SeatListModel();
        seatListModel2 = SeatList.get(2);
        tvPassenser3.setText(seatListModel2.getSeatNumber());

        bookedSeatModel3 = new BookedSeatModel();
        bookedSeatModel3 = passengerDetailsConfirm.get(2);
        editPassenser3.setText(bookedSeatModel3.getPassengerName());
        spinnerGender3.setSelection(bookedSeatModel3.getGenderPosition());
        editAge3.setText(bookedSeatModel3.getAge());
        spinnerBoardingPassenger3.setSelection(bookedSeatModel3.getBoardingPosition());
        spinnerDroppingPassenger3.setSelection(bookedSeatModel3.getDroppingPosition());
        spinnerGender3.setEnabled(false);
        spinnerBoardingPassenger3.setEnabled(false);
        spinnerDroppingPassenger3.setEnabled(false);
    }

    private void setDataForSeat4() {

        //Set Seat Number from model class
        seatListModel3 = new SeatListModel();
        seatListModel3 = SeatList.get(3);
        tvPassenser4.setText(seatListModel3.getSeatNumber());

        bookedSeatModel4 = new BookedSeatModel();
        bookedSeatModel4 = passengerDetailsConfirm.get(3);
        editPassenser4.setText(bookedSeatModel4.getPassengerName());
        spinnerGender4.setSelection(bookedSeatModel4.getGenderPosition());
        editAge4.setText(bookedSeatModel4.getAge());
        spinnerBoardingPassenger4.setSelection(bookedSeatModel4.getBoardingPosition());
        spinnerDroppingPassenger4.setSelection(bookedSeatModel4.getDroppingPosition());
        spinnerGender4.setEnabled(false);
        spinnerBoardingPassenger4.setEnabled(false);
        spinnerDroppingPassenger4.setEnabled(false);
    }

    private void setDataForSeat5() {

        //Set Seat Number from model class
        seatListModel4 = new SeatListModel();
        seatListModel4 = SeatList.get(4);
        tvPassenser5.setText(seatListModel4.getSeatNumber());

        bookedSeatModel5 = new BookedSeatModel();
        bookedSeatModel5 = passengerDetailsConfirm.get(4);
        editPassenser5.setText(bookedSeatModel5.getPassengerName());
        spinnerGender5.setSelection(bookedSeatModel5.getGenderPosition());
        editAge5.setText(bookedSeatModel5.getAge());
        spinnerBoardingPassenger5.setSelection(bookedSeatModel5.getBoardingPosition());
        spinnerDroppingPassenger5.setSelection(bookedSeatModel5.getDroppingPosition());
        spinnerGender5.setEnabled(false);
        spinnerBoardingPassenger5.setEnabled(false);
        spinnerDroppingPassenger5.setEnabled(false);
    }

    private void setDataForSeat6() {

        //Set Seat Number from model class
        seatListModel5 = new SeatListModel();
        seatListModel5 = SeatList.get(5);
        tvPassenser6.setText(seatListModel5.getSeatNumber());

        bookedSeatModel6 = new BookedSeatModel();
        bookedSeatModel6 = passengerDetailsConfirm.get(5);
        editPassenser6.setText(bookedSeatModel6.getPassengerName());
        spinnerGender6.setSelection(bookedSeatModel6.getGenderPosition());
        editAge6.setText(bookedSeatModel6.getAge());
        spinnerBoardingPassenger6.setSelection(bookedSeatModel6.getBoardingPosition());
        spinnerDroppingPassenger6.setSelection(bookedSeatModel6.getDroppingPosition());
        spinnerGender6.setEnabled(false);
        spinnerBoardingPassenger6.setEnabled(false);
        spinnerDroppingPassenger6.setEnabled(false);
    }

    //For Confirm Json Request
    private void ConfirmJsonRequest() {
        jsonArrayFinal = new JSONArray();
        L.m2("Check _jsonArrayFinal1", passengerDetails.toString() + "--" + passengerDetails.size());
        for (int i = 0; i < passengerDetails.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            BookedSeatModel passengerDetailsModel = new BookedSeatModel();
            passengerDetailsModel = passengerDetails.get(i);
            try {
                jsonObject.put("PassengerName", passengerDetailsModel.getPassengerName());
                jsonObject.put("Gender", passengerDetailsModel.getGender());
                jsonObject.put("Age", passengerDetailsModel.getAge());
                jsonObject.put("SeatNo", passengerDetailsModel.getSeatNo());
                jsonObject.put("SeatTypeId", passengerDetailsModel.getSeatTypeId());
                jsonObject.put("Fare", passengerDetailsModel.getFare());
                jsonObject.put("BoardingId", passengerDetailsModel.getBoardingId());
                jsonObject.put("DroppingId", passengerDetailsModel.getDroppingId());
                jsonObject.put("Time", passengerDetailsModel.getTime());
                jsonObject.put("Address", passengerDetailsModel.getAddress());
                jsonObject.put("LandMark", passengerDetailsModel.getLandMark());
                jsonObject.put("ContactNumber", passengerDetailsModel.getContactNumber());
                jsonObject.put("place", "");
                jsonArrayFinal.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        L.m2("Check _jsonArrayFinal2", jsonArrayFinal.toString());
        getBlockingSeatRequest();
    }

    private void initEditableButton() {

        btnEditSeat1 =  findViewById(R.id.btn_seat_1);
        btnEditSeat2 =  findViewById(R.id.btn_seat_2);
        btnEditSeat3 =  findViewById(R.id.btn_seat_3);
        btnEditSeat4 =  findViewById(R.id.btn_seat_4);
        btnEditSeat5 =  findViewById(R.id.btn_seat_5);
        btnEditSeat6 =  findViewById(R.id.btn_seat_6);
    }

    private void initLinearLayoutHideShow() {

        linearLayoutHide1 = (LinearLayout) findViewById(R.id.linear_seat_1);
        linearLayoutHide2 = (LinearLayout) findViewById(R.id.linear_seat_2);
        linearLayoutHide3 = (LinearLayout) findViewById(R.id.linear_seat_3);
        linearLayoutHide4 = (LinearLayout) findViewById(R.id.linear_seat_4);
        linearLayoutHide5 = (LinearLayout) findViewById(R.id.linear_seat_5);
        linearLayoutHide6 = (LinearLayout) findViewById(R.id.linear_seat_6);
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


        ArrayAdapter GenderAdaptor1 = new ArrayAdapter(this, R.layout.spinner_textview_layout, Gender);
        GenderAdaptor1.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender1.setAdapter(GenderAdaptor1);

        ArrayAdapter GenderAdaptor2 = new ArrayAdapter(this, R.layout.spinner_textview_layout, Gender);
        GenderAdaptor2.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender2.setAdapter(GenderAdaptor2);

        ArrayAdapter GenderAdaptor3 = new ArrayAdapter(this, R.layout.spinner_textview_layout, Gender);
        GenderAdaptor3.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender3.setAdapter(GenderAdaptor3);

        ArrayAdapter GenderAdaptor4 = new ArrayAdapter(this, R.layout.spinner_textview_layout, Gender);
        GenderAdaptor4.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender4.setAdapter(GenderAdaptor4);

        ArrayAdapter GenderAdaptor5 = new ArrayAdapter(this, R.layout.spinner_textview_layout, Gender);
        GenderAdaptor5.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender5.setAdapter(GenderAdaptor5);

        ArrayAdapter GenderAdaptor6 = new ArrayAdapter(this, R.layout.spinner_textview_layout, Gender);
        GenderAdaptor6.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerGender6.setAdapter(GenderAdaptor6);

        ArrayAdapter CountryAdaptor = new ArrayAdapter(this, R.layout.spinner_textview_layout, Country);
        CountryAdaptor.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerCountry.setAdapter(CountryAdaptor);

        ArrayAdapter IdProofAdaptor = new ArrayAdapter(this, R.layout.spinner_textview_layout, IdProof);
        IdProofAdaptor.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerIdProof.setAdapter(IdProofAdaptor);

        ArrayAdapter BordingAdaptor1 = new ArrayAdapter(this, R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor1.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger1.setAdapter(BordingAdaptor1);

        ArrayAdapter DroppingAdaptor1 = new ArrayAdapter(this, R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor1.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger1.setAdapter(DroppingAdaptor1);

        ArrayAdapter BordingAdaptor2 = new ArrayAdapter(this, R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor2.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger2.setAdapter(BordingAdaptor2);

        ArrayAdapter DroppingAdaptor2 = new ArrayAdapter(this, R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor2.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger2.setAdapter(DroppingAdaptor2);

        ArrayAdapter BordingAdaptor3 = new ArrayAdapter(this, R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor3.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger3.setAdapter(BordingAdaptor3);

        ArrayAdapter DroppingAdaptor3 = new ArrayAdapter(this, R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor3.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger3.setAdapter(DroppingAdaptor3);

        ArrayAdapter BordingAdaptor4 = new ArrayAdapter(this, R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor4.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger4.setAdapter(BordingAdaptor4);

        ArrayAdapter DroppingAdaptor4 = new ArrayAdapter(this, R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor4.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger4.setAdapter(DroppingAdaptor4);

        ArrayAdapter BordingAdaptor5 = new ArrayAdapter(this, R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor5.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger5.setAdapter(BordingAdaptor5);

        ArrayAdapter DroppingAdaptor5 = new ArrayAdapter(this, R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor5.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger5.setAdapter(DroppingAdaptor5);

        ArrayAdapter BordingAdaptor6 = new ArrayAdapter(this, R.layout.spinner_textview_layout, BoardingPointList);
        BordingAdaptor6.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerBoardingPassenger6.setAdapter(BordingAdaptor6);

        ArrayAdapter DroppingAdaptor6 = new ArrayAdapter(this, R.layout.spinner_textview_layout, DroppingPointList);
        DroppingAdaptor6.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerDroppingPassenger6.setAdapter(DroppingAdaptor6);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                if (position == 0) {
                    genderData1 = null;
                } else {
                    genderData1 = parent.getItemAtPosition(position).toString();
                    if (genderData1.equalsIgnoreCase("Male")) {
                        genderData1 = "M";
                    } else if (genderData1.equalsIgnoreCase("Female")) {
                        genderData1 = "F";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    genderData2 = null;
                } else {
                    genderData2 = parent.getItemAtPosition(position).toString();
                    if (genderData2.equalsIgnoreCase("Male")) {
                        genderData2 = "M";
                    } else if (genderData2.equalsIgnoreCase("Female")) {
                        genderData2 = "F";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    genderData3 = null;
                } else {
                    genderData3 = parent.getItemAtPosition(position).toString();
                    if (genderData3.equalsIgnoreCase("Male")) {
                        genderData3 = "M";
                    } else if (genderData3.equalsIgnoreCase("Female")) {
                        genderData3 = "F";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    genderData4 = null;
                } else {
                    genderData4 = parent.getItemAtPosition(position).toString();
                    if (genderData4.equalsIgnoreCase("Male")) {
                        genderData4 = "M";
                    } else if (genderData4.equalsIgnoreCase("Female")) {
                        genderData4 = "F";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    genderData5 = null;
                } else {
                    genderData5 = parent.getItemAtPosition(position).toString();
                    if (genderData5.equalsIgnoreCase("Male")) {
                        genderData1 = "M";
                    } else if (genderData1.equalsIgnoreCase("Female")) {
                        genderData1 = "F";

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    genderData6 = null;
                } else {
                    genderData6 = parent.getItemAtPosition(position).toString();
                    if (genderData6.equalsIgnoreCase("Male")) {
                        genderData6 = "M";
                    } else if (genderData6.equalsIgnoreCase("Female")) {
                        genderData6 = "F";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    boardingData1 = null;
                } else {
                    BoardingID1 = position;
                    boardingData1 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    boardingData2 = null;
                } else {
                    BoardingID2 = position;
                    boardingData2 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    boardingData3 = null;
                } else {
                    BoardingID3 = position;
                    boardingData3 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    boardingData4 = null;
                } else {
                    BoardingID4 = position;
                    boardingData4 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    boardingData5 = null;
                } else {
                    BoardingID5 = position;
                    boardingData5 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBoardingPassenger6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    boardingData6 = null;
                } else {
                    BoardingID6 = position;
                    boardingData6 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    droppingData1 = null;
                } else {
                    DroppingID1 = position;
                    droppingData1 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    droppingData2 = null;
                } else {
                    DroppingID2 = position;
                    droppingData2 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    droppingData3 = null;
                } else {
                    DroppingID3 = position;
                    droppingData3 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    droppingData4 = null;
                } else {
                    DroppingID4 = position;
                    droppingData4 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDroppingPassenger5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
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

                if (position == 0) {
                    droppingData6 = null;
                } else {
                    DroppingID6 = position;
                    droppingData6 = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //===========Get Seat Map ==============
    private void getBlockingSeatRequest() {
        pd = ProgressDialog.show(TravellersDetailConfirmation.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JsonObjectRequest jsonrequest = null;
            JSONObject jsonObjectData = new JSONObject()
                    .put("UserTrackId", UserTrackId)
                    .put("avalBal", AvailableBal)
                    .put("agentID", AgentID)
                    .put("OriginName", FromSource)
                    .put("DestinationName", ToDestination)
                    .put("DepartureTime", DepartureTime)
                    .put("Title", "")
                    .put("Name", editPassenser1.getText().toString().trim())
                    .put("Address", editPassenserAddress.getText().toString().trim())
                    .put("ContactNumber", editMobileNo.getText().toString().trim())
                    .put("City", editPassenserAddress.getText().toString().trim())
                    .put("CountryId", "91")
                    .put("EmailId", editMobileNo.getText().toString().trim() + "@gmail.com")
                    .put("IdProofId", "")
                    .put("IdProofNumber", editPassenserIdProofNo.getText().toString().trim())
                    .put("IdProofType", "")
                    .put("BusType", SeatType)
                    .put("BusName", BusName)
                    .put("TotalServiceTax", ServiceTax)
                    .put("TotalConvenienceFee", ConvenienceFee)
                    .put("TransportName", TravellerName)
                    .put("ArrivalTime", ArrivalTime)
                    .put("TotalTickets", "1")
                    .put("TotalAmount", FullFareOfAllSeats.toString())
                    .put("TransportId", TransportId)
                    .put("ScheduleId", ScheduleId)
                    .put("StationId", StationId)
                    .put("TravelDate", DateOfJourney)
                    .put("PassengerList", jsonArrayFinal);

            L.m2("Request--BlockingSeat>", jsonObjectData.toString());
            jsonrequest = new JsonObjectRequest(Request.Method.POST, url_blocking_seat_details, jsonObjectData,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject = object;
                            System.out.println("Object---BlockingSeat>" + object.toString());
                            try {
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    L.m2("url-called--BlockingSea", url_blocking_seat_details);
                                    L.m2("url data--BlockingSeat", object.toString());
                                    getConfirmBookingRequest();
                                    Toast.makeText(TravellersDetailConfirmation.this, object.getString("Remarks").toString(), Toast.LENGTH_SHORT).show();

                                } else {
                                     Toast.makeText(TravellersDetailConfirmation.this, object.getString("Remarks").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(TravellersDetailConfirmation.this, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(TravellersDetailConfirmation.this).addToRequsetque(jsonrequest);
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }
    }

    //================================================
    private void getConfirmBookingRequest() {
        pd = ProgressDialog.show(TravellersDetailConfirmation.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JsonObjectRequest jsonrequest = null;
            JSONObject jsonObjectData = new JSONObject()
                    .put("UserTrackId", UserTrackId)
                    .put("avalBal", AvailableBal)
                    .put("agentID", AgentID)
                    .put("OriginName", FromSource)
                    .put("DestinationName", ToDestination)
                    .put("DepartureTime", DepartureTime)
                    .put("Title", "")
                    .put("Name", editPassenser1.getText().toString().trim())
                    .put("Address", editPassenserAddress.getText().toString().trim())
                    .put("ContactNumber", editMobileNo.getText().toString().trim())
                    .put("City", editPassenserAddress.getText().toString().trim())
                    .put("CountryId", "91")
                    .put("EmailId", editMobileNo.getText().toString().trim() + "@gmail.com")
                    .put("IdProofId", "")
                    .put("IdProofNumber", editPassenserIdProofNo.getText().toString().trim())
                    .put("IdProofType", "")
                    .put("BusType", SeatType)
                    .put("BusName", BusName)
                    .put("TotalServiceTax", ServiceTax)
                    .put("TotalConvenienceFee", ConvenienceFee)
                    .put("TransportName", TravellerName)
                    .put("ArrivalTime", ArrivalTime)
                    .put("TotalTickets", "1")
                    .put("TotalAmount", FullFareOfAllSeats.toString())
                    .put("TransportId", TransportId)
                    .put("ScheduleId", ScheduleId)
                    .put("StationId", StationId)
                    .put("TravelDate", DateOfJourney)
                    .put("PassengerList", jsonArrayFinal);

            L.m2("Request--ConfirmBook>", jsonObjectData.toString());
            jsonrequest = new JsonObjectRequest(Request.Method.POST, url_confirm_booking_seat, jsonObjectData,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("url-called--ConfirBook1", url_confirm_booking_seat);
                            jsonObject = new JSONObject();
                            jsonObject = object;
                            try {
                                L.m2("seat_booking-->", object.getString("Remarks").toString());
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    L.m2("url-called--ConfirmBook", url_confirm_booking_seat);
                                    L.m2("url data--ConfirmBook", object.toString());
                                    passenser = 1;
                                    seatList.clear();
                                    Intent intent = new Intent(TravellersDetailConfirmation.this, ConfirmTicketDetailsExpandable.class);
                                    intent.putExtra("ConfirmTicketDetails", object.toString());
                                    startActivity(intent);

                                    Toast.makeText(TravellersDetailConfirmation.this, object.getString("Remarks").toString(), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(TravellersDetailConfirmation.this, object.getString("Remarks").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(TravellersDetailConfirmation.this, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(TravellersDetailConfirmation.this).addToRequsetque(jsonrequest);
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }
    }
    //================================================

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
