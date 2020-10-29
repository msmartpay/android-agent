package in.msmartpay.agent.flight;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.HashMap;
import java.util.Locale;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.Service;

/**
 * Created by Harendra on 10/6/2017.
 */

public class ConfirmFlightBooking extends BaseActivity {

    private ArrayList<AvailSegmentsModel> availSegmentsArrayList = new ArrayList<>();
    private ArrayList<HashMap<String,PassengerModel>> arrayListAdult = new ArrayList<HashMap<String, PassengerModel>>();
    private ArrayList<HashMap<String,PassengerModel>> arrayListChild = new ArrayList<HashMap<String, PassengerModel>>();
    private ArrayList<HashMap<String,PassengerModel>> arrayListInfant = new ArrayList<HashMap<String, PassengerModel>>();
    ArrayList<HashMap<String,PassengerModel>> arrayListAdult1 = new ArrayList<HashMap<String, PassengerModel>>();
    ArrayList<HashMap<String,PassengerModel>> arrayListChild1 = new ArrayList<HashMap<String, PassengerModel>>();
    ArrayList<HashMap<String,PassengerModel>> arrayListInfant1 = new ArrayList<HashMap<String, PassengerModel>>();
    private Context context;
    private String TravelDate,Origin,Destination,totalFare,mob,email,address,pincode;
    private int infant,child,adult,position;
    private String promo="0.00",ClassCode;
    private LinearLayout ll_adult,ll_child,ll_infant;
    private TextView tv_date,tv_adult,tv_child,tv_infant;
    private TextView tv_total_fare,tv_promo_fare,tv_grand_fare;
    private Button btn_flight_proceed;
    private EditText et_mob,et_email,et_address,et_pincode;
    private AvailSegmentsModel availSegmentsModel;
    private String getTaxUrl= HttpURL.FLIGHT_TAX_REQUEST_URL;
    private String getFlightBookingUrl= HttpURL.FLIGHT_BOOK_REQUEST_URL;
    private ProgressDialog pd;
    private SharedPreferences myPrefs;
    private CardView cv_child,cv_infant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_passenger_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = ConfirmFlightBooking.this;

        myPrefs = getSharedPreferences("flightPref", Context.MODE_PRIVATE);
        availSegmentsArrayList = (ArrayList<AvailSegmentsModel>) getIntent().getSerializableExtra("availSegmentsArrayList");
        availSegmentsModel = new AvailSegmentsModel();
        availSegmentsModel = availSegmentsArrayList.get(position);

        TravelDate = getIntent().getStringExtra("TravelDate");
        Origin =getIntent().getStringExtra("Origin");
        Destination =getIntent().getStringExtra("Destination");
        infant =getIntent().getIntExtra("InfantCount",0);
        child =getIntent().getIntExtra("ChildCount",0);
        adult =getIntent().getIntExtra("AdultCount",1);
        totalFare=getIntent().getStringExtra("TotalFare");
        position=getIntent().getIntExtra("position",0);
        ClassCode=getIntent().getStringExtra("ClassCode");

        mob = getIntent().getStringExtra("mobile");
        email =getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        pincode =getIntent().getStringExtra("pincode");

        arrayListAdult = (ArrayList<HashMap<String, PassengerModel>>) getIntent().getSerializableExtra("arrayListAdult");
        arrayListChild = (ArrayList<HashMap<String, PassengerModel>>) getIntent().getSerializableExtra("arrayListChild");
        arrayListInfant = (ArrayList<HashMap<String, PassengerModel>>) getIntent().getSerializableExtra("arrayListInfant");

        setTitle(Origin+" - "+Destination);

        cv_child = (CardView) findViewById(R.id.cv_child);
        cv_infant = (CardView) findViewById(R.id.cv_infant);
        cv_child.setVisibility(View.GONE);
        cv_infant.setVisibility(View.GONE);

        if(child>0)
            cv_child.setVisibility(View.VISIBLE);
        if (infant>0)
            cv_infant.setVisibility(View.VISIBLE);

        et_mob = findViewById(R.id.et_mob);
        et_email = findViewById(R.id.et_email);
        et_address = findViewById(R.id.et_address);
        et_pincode = findViewById(R.id.et_pincode);
        et_mob.setText(mob);
        et_email.setText(email);
        et_address.setText(address);
        et_pincode.setText(pincode);
        btn_flight_proceed =  findViewById(R.id.btn_flight_proceed);
        btn_flight_proceed.setText("Confirm");
        ll_adult = (LinearLayout) findViewById(R.id.ll_adult);
        ll_child = (LinearLayout) findViewById(R.id.ll_child);
        ll_infant = (LinearLayout) findViewById(R.id.ll_infant);

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_adult = (TextView) findViewById(R.id.tv_adult);
        tv_child = (TextView) findViewById(R.id.tv_child);
        tv_infant = (TextView) findViewById(R.id.tv_infant);

        tv_date.setText(TravelDate);
        tv_infant.setText(""+infant);
        tv_child.setText(""+child);
        tv_adult.setText(""+adult);

        tv_total_fare = (TextView) findViewById(R.id.tv_total_fare);
        tv_promo_fare = (TextView) findViewById(R.id.tv_promo_fare);
        tv_grand_fare = (TextView) findViewById(R.id.tv_grand_fare);

        tv_total_fare.setText(totalFare);
        tv_promo_fare.setText(promo);
        tv_grand_fare.setText(Double.parseDouble(totalFare) + Double.parseDouble(promo)+"");


        ll_adult.removeAllViews();
        for (int i = 0; i < adult; i++) {
            LayoutInflater inflater = null;
            final View localView;
            inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = inflater.inflate(R.layout.passenger_adult_child_infant, null);
            // Spinner sp_human_type =  localView.findViewById(R.id.sp_human_type);
            final EditText et_fNmae = localView.findViewById(R.id.et_fNmae);
            final EditText et_lName = localView.findViewById(R.id.et_lName);
            final EditText et_dob = localView.findViewById(R.id.et_dob);

            RadioButton rb1 = (RadioButton)localView.findViewById(R.id.rb1);
            RadioButton rb2 = (RadioButton)localView.findViewById(R.id.rb2);
            RadioButton rb3 = (RadioButton)localView.findViewById(R.id.rb3);
            TextView tv_cast = (TextView) localView.findViewById(R.id.tv_cast);
            tv_cast.setText("Adult #"+(i+1));


            final RadioGroup rg = (RadioGroup)localView.findViewById(R.id.rg) ;

            for (int j = 0; j < rg.getChildCount(); j++) {
                rg.getChildAt(j).setEnabled(false);
            }

            HashMap<String,PassengerModel> hashMap = new HashMap<>();
            hashMap = arrayListAdult.get(i);
            PassengerModel passengerModel = new PassengerModel();
            passengerModel = hashMap.get("Adult"+i+1);
            final RadioButton radioButton =  (RadioButton) localView.findViewById(passengerModel.getRbId());

            radioButton.setEnabled(true);
            radioButton.setFocusableInTouchMode(false);
            radioButton.setChecked(true);
            et_fNmae.setEnabled(false);
            et_fNmae.setFocusableInTouchMode(false);
            et_lName.setEnabled(false);
            et_lName.setFocusableInTouchMode(false);
            et_dob.setEnabled(false);
            et_dob.setClickable(false);
            et_dob.setFocusableInTouchMode(false);
            et_fNmae.setText(passengerModel.getFirstName());
            et_lName.setText(passengerModel.getLastName());
            et_dob.setText(passengerModel.getDOB());
            et_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDate(et_dob,37);
                }
            });
            final boolean flag = true;
            Button btn_edit = localView.findViewById(R.id.btn_edit);
            btn_edit.setVisibility(View.VISIBLE);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(flag) {
                        for (int j = 0; j < rg.getChildCount(); j++) {
                            rg.getChildAt(j).setEnabled(true);
                        }
                        et_fNmae.setEnabled(true);
                        et_fNmae.setFocusableInTouchMode(true);
                        et_lName.setEnabled(true);
                        et_lName.setFocusableInTouchMode(true);
                        et_dob.setEnabled(true);
                        et_dob.setClickable(true);
                        et_dob.setFocusableInTouchMode(false);
                    }else {
                        for (int j = 0; j < rg.getChildCount(); j++) {
                            rg.getChildAt(j).setEnabled(false);
                            rg.getChildAt(j).setFocusableInTouchMode(false);
                        }
                        radioButton.setEnabled(true);
                        radioButton.setFocusableInTouchMode(false);
                        radioButton.setChecked(true);
                        et_fNmae.setEnabled(false);
                        et_fNmae.setFocusableInTouchMode(false);
                        et_lName.setEnabled(false);
                        et_lName.setFocusableInTouchMode(false);
                        et_dob.setEnabled(false);
                        et_dob.setClickable(false);
                        et_dob.setFocusableInTouchMode(false);
                    }
                }
            });
            ll_adult.addView(localView);

        }
        ll_child.removeAllViews();
        for (int i = 0; i < child; i++) {
            LayoutInflater inflater1 = null;
            View localView;
            inflater1 = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = inflater1.inflate(R.layout.passenger_adult_child_infant, null);
            //Spinner sp_human_type =  localView.findViewById(R.id.sp_human_type);
            final EditText et_fNmae = localView.findViewById(R.id.et_fNmae);
            final EditText et_lName = localView.findViewById(R.id.et_lName);
            final EditText et_dob = localView.findViewById(R.id.et_dob);
            RadioButton rb1 = (RadioButton)localView.findViewById(R.id.rb1);
            RadioButton rb2 = (RadioButton)localView.findViewById(R.id.rb2);
            RadioButton rb3 = (RadioButton)localView.findViewById(R.id.rb3);
            rb1.setText("Mstr.");
            rb2.setText("Ms.");
            rb3.setVisibility(View.GONE);
            TextView tv_cast = (TextView) localView.findViewById(R.id.tv_cast);
            tv_cast.setText("Child #"+(i+1));

            final RadioGroup rg = (RadioGroup)localView.findViewById(R.id.rg) ;

            for (int j = 0; j < rg.getChildCount(); j++) {
                rg.getChildAt(j).setEnabled(false);
            }
            HashMap<String,PassengerModel> hashMap = new HashMap<>();
            hashMap = arrayListChild.get(i);

            PassengerModel passengerModel = new PassengerModel();
            passengerModel = hashMap.get("Child"+i+1);

            final RadioButton radioButton =  (RadioButton) localView.findViewById(passengerModel.getRbId());
            radioButton.setEnabled(true);
            radioButton.setFocusableInTouchMode(false);
            radioButton.setChecked(true);

            et_fNmae.setEnabled(false);
            et_fNmae.setFocusableInTouchMode(false);
            et_lName.setEnabled(false);
            et_lName.setFocusableInTouchMode(false);
            et_dob.setEnabled(false);
            et_dob.setClickable(false);
            et_dob.setFocusableInTouchMode(false);
            et_fNmae.setText(passengerModel.getFirstName());
            et_lName.setText(passengerModel.getLastName());
            et_dob.setText(passengerModel.getDOB());
            et_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDate(et_dob,14);
                }
            });
            final boolean flag = true;
            Button btn_edit = localView.findViewById(R.id.btn_edit);
            btn_edit.setVisibility(View.VISIBLE);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(flag) {
                        for (int j = 0; j < rg.getChildCount(); j++) {
                            rg.getChildAt(j).setEnabled(true);
                        }
                        et_fNmae.setEnabled(true);
                        et_fNmae.setFocusableInTouchMode(true);
                        et_lName.setEnabled(true);
                        et_dob.setClickable(true);
                        et_lName.setFocusableInTouchMode(true);
                        et_dob.setEnabled(true);
                        et_dob.setFocusableInTouchMode(false);
                    }else {
                        for (int j = 0; j < rg.getChildCount(); j++) {
                            rg.getChildAt(j).setEnabled(false);
                            rg.getChildAt(j).setFocusableInTouchMode(false);
                        }
                        radioButton.setEnabled(true);
                        radioButton.setFocusableInTouchMode(false);
                        radioButton.setChecked(true);
                        et_fNmae.setEnabled(false);
                        et_fNmae.setFocusableInTouchMode(false);
                        et_lName.setEnabled(false);
                        et_lName.setFocusableInTouchMode(false);
                        et_dob.setEnabled(false);
                        et_dob.setClickable(false);
                        et_dob.setFocusableInTouchMode(false);
                    }
                }
            });

            ll_child.addView(localView);
        }
        ll_infant.removeAllViews();
        for (int i = 0; i < infant; i++) {
            LayoutInflater inflater2 = null;
            View localView;
            inflater2 = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = inflater2.inflate(R.layout.passenger_adult_child_infant, null);
            //Spinner sp_human_type =  localView.findViewById(R.id.sp_human_type);
            final EditText et_fNmae = localView.findViewById(R.id.et_fNmae);
            final EditText et_lName = localView.findViewById(R.id.et_lName);
            final EditText et_dob = localView.findViewById(R.id.et_dob);
            RadioButton rb1 = (RadioButton)localView.findViewById(R.id.rb1);
            RadioButton rb2 = (RadioButton)localView.findViewById(R.id.rb2);
            RadioButton rb3 = (RadioButton)localView.findViewById(R.id.rb3);
            rb1.setText("Male");
            rb2.setText("Female");
            rb3.setVisibility(View.GONE);
            TextView tv_cast = (TextView) localView.findViewById(R.id.tv_cast);
            tv_cast.setText("Infant #"+(i+1));

            final RadioGroup rg = (RadioGroup)localView.findViewById(R.id.rg) ;

            for (int j = 0; j < rg.getChildCount(); j++) {
                rg.getChildAt(j).setEnabled(false);
            }
            HashMap<String,PassengerModel> hashMap = new HashMap<>();
            hashMap = arrayListInfant.get(i);

            PassengerModel passengerModel = new PassengerModel();
            passengerModel = hashMap.get("Infant"+i+1);

            final RadioButton radioButton =  (RadioButton) localView.findViewById(passengerModel.getRbId());
            radioButton.setEnabled(true);
            radioButton.setFocusableInTouchMode(false);
            radioButton.setChecked(true);

            et_fNmae.setEnabled(false);
            et_fNmae.setFocusableInTouchMode(false);
            et_lName.setEnabled(false);
            et_lName.setFocusableInTouchMode(false);
            et_dob.setEnabled(false);
            et_dob.setClickable(false);
            et_dob.setFocusableInTouchMode(false);
            et_fNmae.setText(passengerModel.getFirstName());
            et_lName.setText(passengerModel.getLastName());
            et_dob.setText(passengerModel.getDOB());
            et_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDate(et_dob,2);
                }
            });
            final boolean flag = true;
            Button btn_edit = localView.findViewById(R.id.btn_edit);
            btn_edit.setVisibility(View.VISIBLE);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(flag) {
                        for (int j = 0; j < rg.getChildCount(); j++) {
                            rg.getChildAt(j).setEnabled(true);
                        }
                        et_fNmae.setEnabled(true);
                        et_fNmae.setFocusableInTouchMode(true);
                        et_lName.setEnabled(true);
                        et_lName.setFocusableInTouchMode(true);
                        et_dob.setEnabled(true);
                        et_dob.setClickable(true);
                        et_dob.setFocusableInTouchMode(false);
                    }else {
                        for (int j = 0; j < rg.getChildCount(); j++) {
                            rg.getChildAt(j).setEnabled(false);
                            rg.getChildAt(j).setFocusableInTouchMode(false);
                        }
                        radioButton.setEnabled(true);
                        radioButton.setFocusableInTouchMode(false);
                        radioButton.setChecked(true);
                        et_fNmae.setEnabled(false);
                        et_fNmae.setFocusableInTouchMode(false);
                        et_lName.setEnabled(false);
                        et_lName.setFocusableInTouchMode(false);
                        et_dob.setEnabled(false);
                        et_dob.setClickable(false);
                        et_dob.setFocusableInTouchMode(false);
                    }
                }
            });

            ll_infant.addView(localView);
        }

        btn_flight_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flagTest=true;
                /*Log.e("LL ll_adult",ll_adult.getChildCount()+"");
                Log.e("LL ll_child",ll_child.getChildCount()+"");
                Log.e("LL ll_infant",ll_infant.getChildCount()+"");*/
                String mob=et_mob.getText().toString();
                String email = et_email.getText().toString();
                String address = et_address.getText().toString();
                String pincode = et_pincode.getText().toString();

                if(TextUtils.isEmpty(mob)){
                    Toast.makeText(context, "Enter Mobile Number!", Toast.LENGTH_SHORT).show();
                    flagTest=false;
                }else if (mob.length()>10) {
                    Toast.makeText(context, "Enter Correct Mobile Number!", Toast.LENGTH_SHORT).show();
                    flagTest=false;
                }else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(context, "Enter Email Id!", Toast.LENGTH_SHORT).show();
                    flagTest=false;
                }else if (!Service.isValidEmail(email)) {
                    Toast.makeText(context, "Enter Correct Email!", Toast.LENGTH_SHORT).show();
                    flagTest=false;
                }else {
                    if(flagTest) {
                        for (int i=0; i < ll_adult.getChildCount(); i++){
                            View view1 = (View)ll_adult.getChildAt(i);
                            RadioGroup rg = (RadioGroup)view1.findViewById(R.id.rg) ;
                            int idRadio = rg.getCheckedRadioButtonId();
                            RadioButton rb = (RadioButton) view1.findViewById(idRadio);
                            EditText et_fNmae = view1.findViewById(R.id.et_fNmae);
                            EditText et_lName = view1.findViewById(R.id.et_lName);
                            EditText et_dob = view1.findViewById(R.id.et_dob);
                            if (rg.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(context, "Select gender of Adult #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            } else if (TextUtils.isEmpty(et_fNmae.getText().toString())) {
                                Toast.makeText(context, "Enter First Name of Adult #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else if (TextUtils.isEmpty(et_lName.getText().toString())) {
                                Toast.makeText(context, "Enter Last Name of Adult #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else if (TextUtils.isEmpty(et_dob.getText().toString())) {
                                Toast.makeText(context, "Enter DOB of Adult #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else{
                                HashMap<String, PassengerModel> hashMap  =new HashMap<String, PassengerModel>();
                                PassengerModel passengerModel = new PassengerModel();
                                passengerModel.setType(rb.getText().toString());
                                passengerModel.setFirstName(et_fNmae.getText().toString());
                                passengerModel.setLastName(et_lName.getText().toString());
                                passengerModel.setDOB(et_dob.getText().toString());
                                passengerModel.setRbId(idRadio);
                                hashMap.put("Adult"+i+1,passengerModel);
                                arrayListAdult1.add(hashMap);
                            }
                        }
                    }
                    if(flagTest) {
                        for (int i = 0; i < ll_child.getChildCount(); i++) {
                            View view1 = (View) ll_child.getChildAt(i);

                            RadioGroup rg = (RadioGroup)view1.findViewById(R.id.rg) ;
                            int idRadio = rg.getCheckedRadioButtonId();
                            RadioButton rb = (RadioButton) view1.findViewById(idRadio);
                            EditText et_fNmae =  view1.findViewById(R.id.et_fNmae);
                            EditText et_lName = view1.findViewById(R.id.et_lName);
                            EditText et_dob = view1.findViewById(R.id.et_dob);
                            if (rg.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(context, "Select gender of Infant #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            } else if (TextUtils.isEmpty(et_fNmae.getText().toString())) {
                                Toast.makeText(context, "Enter First Name of Infant #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else if (TextUtils.isEmpty(et_lName.getText().toString())) {
                                Toast.makeText(context, "Enter Last Name of Infant #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else if (TextUtils.isEmpty(et_dob.getText().toString())) {
                                Toast.makeText(context, "Enter DOB of Infant #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else{
                                HashMap<String, PassengerModel> hashMap  =new HashMap<String, PassengerModel>();
                                PassengerModel passengerModel = new PassengerModel();
                                passengerModel.setType(rb.getText().toString());
                                passengerModel.setFirstName(et_fNmae.getText().toString());
                                passengerModel.setLastName(et_lName.getText().toString());
                                passengerModel.setDOB(et_dob.getText().toString());
                                passengerModel.setRbId(idRadio);
                                hashMap.put("Child"+i+1,passengerModel);
                                arrayListChild1.add(hashMap);
                            }
                        }
                    }
                    if(flagTest) {
                        for (int i = 0; i < ll_infant.getChildCount(); i++) {
                            View view1 = (View) ll_infant.getChildAt(i);
                            RadioGroup rg = (RadioGroup)view1.findViewById(R.id.rg) ;
                            int idRadio = rg.getCheckedRadioButtonId();
                            RadioButton rb = (RadioButton) view1.findViewById(idRadio);
                            EditText et_fNmae =  view1.findViewById(R.id.et_fNmae);
                            EditText et_lName = view1.findViewById(R.id.et_lName);
                            EditText et_dob = view1.findViewById(R.id.et_dob);
                            if (rg.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(context, "Select gender of Child #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            } else if (TextUtils.isEmpty(et_fNmae.getText().toString())) {
                                Toast.makeText(context, "Enter First Name of Child #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else if (TextUtils.isEmpty(et_lName.getText().toString())) {
                                Toast.makeText(context, "Enter Last Name of Child #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else if (TextUtils.isEmpty(et_dob.getText().toString())) {
                                Toast.makeText(context, "Enter DOB of Child #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flagTest=false;
                                break;
                            }else{
                                HashMap<String, PassengerModel> hashMap  =new HashMap<String, PassengerModel>();
                                PassengerModel passengerModel = new PassengerModel();
                                passengerModel.setType(rb.getText().toString());
                                passengerModel.setFirstName(et_fNmae.getText().toString());
                                passengerModel.setLastName(et_lName.getText().toString());
                                passengerModel.setDOB(et_dob.getText().toString());
                                passengerModel.setRbId(idRadio);
                                hashMap.put("Infant"+i+1,passengerModel);
                                arrayListInfant1.add(hashMap);
                            }
                        }
                    }
                    L.m2("flagTest",flagTest+"");
                    if(flagTest) {

                        try {
                            pd = new ProgressDialog(context);
                            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
                            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("UserTrackId", myPrefs.getString("UserTrackId",null));
                            jsonObject.put("FlightId", availSegmentsModel.getFlightId());
                            jsonObject.put("AirlineCode", availSegmentsModel.getAirlineCode());
                            jsonObject.put("ETicketFlag", 1);
                            jsonObject.put("BasicAmount", tv_grand_fare.getText().toString());
                            jsonObject.put("SupplierId", availSegmentsModel.getSupplierId());
                            jsonObject.put("AdultCount", adult);
                            jsonObject.put("ChildCount", child);
                            jsonObject.put("InfantCount", infant);
                            jsonObject.put("ClassCode", ClassCode);
                            L.m2("request", jsonObject.toString());
                            L.m2("called url", getTaxUrl);
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, getTaxUrl,jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject data) {
                                            try {
                                                pd.dismiss();
                                                L.m2("response", data.toString());
                                                if (data.getString("status") != null && data.getString("status").equals("0")) {
                                                    Double GrossAmount = 0.0;
                                                    JSONObject TaxOutputJSONObject = data.getJSONObject("textOutput");
                                                    JSONArray TaxResFlightSegmentsJSONArray = TaxOutputJSONObject.getJSONArray("TaxResFlightSegments");
                                                    JSONObject TaxResFlightSegmentsJSONObject = TaxResFlightSegmentsJSONArray.getJSONObject(0);
                                                    JSONObject AdultTaxJSONObject = TaxResFlightSegmentsJSONObject.getJSONObject("AdultTax");
                                                    GrossAmount=GrossAmount+ Double.parseDouble(AdultTaxJSONObject.getString("GrossAmount"));
                                                    JSONObject ChildTaxJSONObject = TaxResFlightSegmentsJSONObject.getJSONObject("ChildTax");
                                                    if(ChildTaxJSONObject.length()!=0) {
                                                        GrossAmount = GrossAmount + Double.parseDouble(ChildTaxJSONObject.getString("GrossAmount"));
                                                    }
                                                    JSONObject InfantTaxJSONObject = TaxResFlightSegmentsJSONObject.getJSONObject("InfantTax");
                                                    if(InfantTaxJSONObject.length()!=0) {
                                                        GrossAmount = GrossAmount + Double.parseDouble(InfantTaxJSONObject.getString("GrossAmount"));
                                                    }
                                                    tv_grand_fare.setText(GrossAmount+"");
                                                    getBookingRequest();
                                                    Toast.makeText(context, data.getString("message")+" Tax", Toast.LENGTH_SHORT).show();
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
                }
            }
        });

    }
   private void getBookingRequest(){
       pd = new ProgressDialog(context);
       pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
       pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

       boolean flag2=true;
       String title="",name="";
       JSONObject mainJSONObject = new JSONObject();
       JSONArray FlightBookingDetailsJsonArray = new JSONArray();
       JSONObject FlightBookingDetailsJsonObject = new JSONObject();
       try{
       JSONArray PassengerDetailsJSONArray = new JSONArray();
           Log.e("Confirm flight ",arrayListAdult1.size()+"-Adult "+arrayListAdult1.size()+"-Child "+arrayListAdult1.size()+"-Infant");
       for(int i=0;i<arrayListAdult1.size();i++){

               JSONObject PassengerDetailsJSONObject = new JSONObject();
               PassengerDetailsJSONObject.put("PassengerType", "ADULT");
               HashMap<String, PassengerModel> hashMap1 =arrayListAdult1.get(i);
               PassengerModel passengerModel1 =hashMap1.get("Adult"+i+1);
           if(flag2){
                 title = passengerModel1.getType();
                 name  = passengerModel1.getFirstName()+" "+passengerModel1.getLastName();
               flag2=false;
           }
               PassengerDetailsJSONObject.put("Title",passengerModel1.getType());
               PassengerDetailsJSONObject.put("FirstName",passengerModel1.getFirstName());
               PassengerDetailsJSONObject.put("LastName",passengerModel1.getLastName());
               PassengerDetailsJSONObject.put("Gender","M");
               PassengerDetailsJSONObject.put("Age","25");
               PassengerDetailsJSONObject.put("DateofBirth",passengerModel1.getDOB());
               PassengerDetailsJSONObject.put("IdentityProofId","");
               PassengerDetailsJSONObject.put("IdentityProofNumber","");
               PassengerDetailsJSONObject.put("LCCBaggageRequest",null);
               PassengerDetailsJSONObject.put("LCCMealsRequest",null);
               PassengerDetailsJSONObject.put("OtherSSRRequest ",null);
               PassengerDetailsJSONObject.put("SeatRequest",null);

               JSONArray jsonArray = new JSONArray();
               JSONObject jsonObject = new JSONObject();
               jsonObject.put("FlightId",availSegmentsModel.getFlightId());
               jsonObject.put("ClassCode",ClassCode);
               jsonObject.put("SpecialServiceCode","");
               jsonObject.put("FrequentFlyerId","");
               jsonObject.put("FrequentFlyerNumber","");
               jsonObject.put("MealCode","");
               jsonObject.put("SeatPreferId","");
               jsonObject.put("SupplierId",availSegmentsModel.getSupplierId());

               jsonArray.put(jsonObject);
               PassengerDetailsJSONObject.put("BookingSegments",jsonArray);
               PassengerDetailsJSONArray.put(PassengerDetailsJSONObject);
       }
       for(int i=0;i<arrayListChild1.size();i++){
               JSONObject PassengerDetailsJSONObject = new JSONObject();
               PassengerDetailsJSONObject.put("PassengerType", "CHILD");
               HashMap<String, PassengerModel> hashMap2 =arrayListChild1.get(i);
               PassengerModel passengerModel2 = hashMap2.get("Child"+i+1);

               PassengerDetailsJSONObject.put("Title",passengerModel2.getType());
               PassengerDetailsJSONObject.put("FirstName",passengerModel2.getFirstName());
               PassengerDetailsJSONObject.put("LastName",passengerModel2.getLastName());
               PassengerDetailsJSONObject.put("Gender","M");
               PassengerDetailsJSONObject.put("Age","25");
               PassengerDetailsJSONObject.put("DateofBirth",passengerModel2.getDOB());
               PassengerDetailsJSONObject.put("IdentityProofId","");
               PassengerDetailsJSONObject.put("IdentityProofNumber","");
               PassengerDetailsJSONObject.put("LCCBaggageRequest",null);
               PassengerDetailsJSONObject.put("LCCMealsRequest",null);
               PassengerDetailsJSONObject.put("OtherSSRRequest ",null);
               PassengerDetailsJSONObject.put("SeatRequest",null);
               JSONArray jsonArray = new JSONArray();
               JSONObject jsonObject = new JSONObject();
               jsonObject.put("FlightId",availSegmentsModel.getFlightId());
               jsonObject.put("ClassCode",ClassCode);
               jsonObject.put("SpecialServiceCode","");
               jsonObject.put("FrequentFlyerId","");
               jsonObject.put("FrequentFlyerNumber","");
               jsonObject.put("MealCode","");
               jsonObject.put("SeatPreferId","");
               jsonObject.put("SupplierId",availSegmentsModel.getSupplierId());

               jsonArray.put(jsonObject);
               PassengerDetailsJSONObject.put("BookingSegments",jsonArray);
               PassengerDetailsJSONArray.put(PassengerDetailsJSONObject);

       }
       for(int i=0;i<arrayListInfant1.size();i++){
               JSONObject PassengerDetailsJSONObject = new JSONObject();
               PassengerDetailsJSONObject.put("PassengerType", "INFANT");
               HashMap<String, PassengerModel> hashMap3 = arrayListInfant1.get(i);
               PassengerModel passengerModel3 = hashMap3.get("Infant"+i+1);

               PassengerDetailsJSONObject.put("Title",passengerModel3.getType());
               PassengerDetailsJSONObject.put("FirstName",passengerModel3.getFirstName());
               PassengerDetailsJSONObject.put("LastName",passengerModel3.getLastName());
               PassengerDetailsJSONObject.put("Gender","M");
               PassengerDetailsJSONObject.put("Age","25");
               PassengerDetailsJSONObject.put("DateofBirth",passengerModel3.getDOB());
               PassengerDetailsJSONObject.put("IdentityProofId","");
               PassengerDetailsJSONObject.put("IdentityProofNumber","");
               PassengerDetailsJSONObject.put("LCCBaggageRequest",null);
               PassengerDetailsJSONObject.put("LCCMealsRequest",null);
               PassengerDetailsJSONObject.put("OtherSSRRequest ",null);
               PassengerDetailsJSONObject.put("SeatRequest",null);
               JSONArray jsonArray = new JSONArray();
               JSONObject jsonObject = new JSONObject();
               jsonObject.put("FlightId",availSegmentsModel.getFlightId());
               jsonObject.put("ClassCode",ClassCode);
               jsonObject.put("SpecialServiceCode","");
               jsonObject.put("FrequentFlyerId","");
               jsonObject.put("FrequentFlyerNumber","");
               jsonObject.put("MealCode","");
               jsonObject.put("SeatPreferId","");
               jsonObject.put("SupplierId",availSegmentsModel.getSupplierId());

               jsonArray.put(jsonObject);
               PassengerDetailsJSONObject.put("BookingSegments",jsonArray);
               PassengerDetailsJSONArray.put(PassengerDetailsJSONObject);

       }

           FlightBookingDetailsJsonObject.put("PassengerDetails", PassengerDetailsJSONArray);
           FlightBookingDetailsJsonArray.put(FlightBookingDetailsJsonObject);

           mainJSONObject.put("UserTrackId",myPrefs.getString("UserTrackId",null));
           mainJSONObject.put("Title",title);
           mainJSONObject.put("Name",name);
           mainJSONObject.put("Address",address);
           mainJSONObject.put("City",Origin);
           mainJSONObject.put("CountryId","91");
           mainJSONObject.put("ContactNumber",mob);
           mainJSONObject.put("EmailId",email);
           mainJSONObject.put("PinCode",pincode);
           mainJSONObject.put("SpecialRemarks","");
           mainJSONObject.put("NotifyByMail",0);
           mainJSONObject.put("NotifyBySMS",0);
           mainJSONObject.put("AdultCount",adult);
           mainJSONObject.put("ChildCount",child);
           mainJSONObject.put("InfantCount",infant);
           mainJSONObject.put("BookingType","O");
           mainJSONObject.put("TotalAmount", Double.parseDouble(tv_grand_fare.getText().toString()));
           mainJSONObject.put("FrequentFlyerRequest",null);
           mainJSONObject.put("SpecialServiceRequest",null);
           mainJSONObject.put("FSCMealsRequest",null);
           mainJSONObject.put("AirlineCode",availSegmentsModel.getAirlineCode());

           FlightBookingDetailsJsonObject.put("AirlineCode",availSegmentsModel.getAirlineCode());
           FlightBookingDetailsJsonObject.put("TourCode","");

           JSONObject PaymentDetailsJsonObject = new JSONObject();
           PaymentDetailsJsonObject.put("CurrencyCode",availSegmentsModel.getCurrencyCode());
           PaymentDetailsJsonObject.put("Amount", Double.parseDouble(tv_grand_fare.getText().toString()));
           FlightBookingDetailsJsonObject.put("PaymentDetails",PaymentDetailsJsonObject);
           mainJSONObject.put("FlightBookingDetails", FlightBookingDetailsJsonArray);
           L.m2("url",getFlightBookingUrl);
           L.m2("Request",mainJSONObject.toString());
           JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, getFlightBookingUrl,mainJSONObject,
                   new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject data) {
                           try {
                               pd.dismiss();
                               L.m2("Response", data.toString());
                               if (data.getString("status") != null && data.getString("status").equals("0")) {
                                   L.m2("Request",data.getString("message"));
                                   Intent intent = new Intent(context,FlightBookingSucces.class);
                                   intent.putExtra("json",data.toString());
                                   startActivity(intent);
                                   Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                               }else if (data.getString("status") != null && data.getString("status").equals("1")) {
                                   Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                               }else{
                                   Toast.makeText(context,"Unable to process your request", Toast.LENGTH_SHORT).show();
                               }
                           } catch (Exception e) {
                               pd.dismiss();
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
       }catch (Exception e){
           pd.dismiss();
           e.printStackTrace();
       }
    }
    private void getDate(final EditText v, final int y) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                try {
                    String colDepartDate = dateFormatter.format(newDate.getTime());
                    if (!colDepartDate.equalsIgnoreCase("")) {
                        v.setText(colDepartDate);
                    } else {
                        Toast.makeText(context, "Invalid From Date.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, newCalendar.get(Calendar.YEAR)-y, newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        //  fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.show();
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
