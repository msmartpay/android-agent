package agent.msmartpay.in.flight;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.Service;

/**
 * Created by Harendra on 10/5/2017.
 */

public class PassengerDetails extends BaseActivity {

    private ArrayList<AvailSegmentsModel> availSegmentsArrayList = new ArrayList<>();
    private Context context;
    private String TravelDate,Origin,Destination,totalFare;
    private int infant,child,adult,position;
    private String promo="0.00",ClassCode;
    private LinearLayout ll_adult,ll_child,ll_infant;
    private TextView tv_date,tv_adult,tv_child,tv_infant;
    private TextView tv_total_fare,tv_promo_fare,tv_grand_fare;
    private Button btn_flight_proceed;
    private EditText et_mob,et_email,et_address,et_pincode;
    private CardView cv_child,cv_infant;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_passenger_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = PassengerDetails.this;

        cv_child = (CardView) findViewById(R.id.cv_child);
        cv_infant = (CardView) findViewById(R.id.cv_infant);
        cv_child.setVisibility(View.GONE);
        cv_infant.setVisibility(View.GONE);

        availSegmentsArrayList = (ArrayList<AvailSegmentsModel>) getIntent().getSerializableExtra("availSegmentsArrayList");

        TravelDate = getIntent().getStringExtra("TravelDate");
        Origin =getIntent().getStringExtra("Origin");
        Destination =getIntent().getStringExtra("Destination");
        infant =getIntent().getIntExtra("InfantCount",0);
        child =getIntent().getIntExtra("ChildCount",0);
        adult =getIntent().getIntExtra("AdultCount",1);
        totalFare=getIntent().getStringExtra("TotalFare");
        position=getIntent().getIntExtra("position",0);
        ClassCode=getIntent().getStringExtra("ClassCode");

        if(child>0)
            cv_child.setVisibility(View.VISIBLE);
        if (infant>0)
            cv_infant.setVisibility(View.VISIBLE);
        setTitle(Origin+" - "+Destination);


        et_mob = findViewById(R.id.et_mob);
        et_email = findViewById(R.id.et_email);
        et_address = findViewById(R.id.et_address);
        et_pincode = findViewById(R.id.et_pincode);

        btn_flight_proceed =  findViewById(R.id.btn_flight_proceed);
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
        tv_grand_fare.setText(Double.parseDouble(totalFare)+ Double.parseDouble(promo)+"");


        ll_adult.removeAllViews();
        for (int i = 0; i < adult; i++) {
            LayoutInflater inflater = null;
            View localView;
            inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = inflater.inflate(R.layout.passenger_adult_child_infant, null);
            // Spinner sp_human_type =  localView.findViewById(R.id.sp_human_type);
            RadioButton rb1 = (RadioButton)localView.findViewById(R.id.rb1);
            RadioButton rb2 = (RadioButton)localView.findViewById(R.id.rb2);
            RadioButton rb3 = (RadioButton)localView.findViewById(R.id.rb3);
            TextView tv_cast = (TextView) localView.findViewById(R.id.tv_cast);
            tv_cast.setText("Adult #"+(i+1));

            final EditText et_dob = localView.findViewById(R.id.et_dob);
            et_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDate(et_dob,37);
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
            RadioButton rb1 = (RadioButton)localView.findViewById(R.id.rb1);
            RadioButton rb2 = (RadioButton)localView.findViewById(R.id.rb2);
            RadioButton rb3 = (RadioButton)localView.findViewById(R.id.rb3);
            rb1.setText("Mstr.");
            rb2.setText("Ms.");
            rb3.setVisibility(View.GONE);
            TextView tv_cast = (TextView) localView.findViewById(R.id.tv_cast);
            tv_cast.setText("Child #"+(i+1));

            final EditText et_dob = localView.findViewById(R.id.et_dob);
            et_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDate(et_dob,14);
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
            RadioButton rb1 = (RadioButton)localView.findViewById(R.id.rb1);
            RadioButton rb2 = (RadioButton)localView.findViewById(R.id.rb2);
            RadioButton rb3 = (RadioButton)localView.findViewById(R.id.rb3);
            rb1.setText("Male");
            rb2.setText("Female");
            rb3.setVisibility(View.GONE);
            TextView tv_cast = (TextView) localView.findViewById(R.id.tv_cast);
            tv_cast.setText("Infant #"+(i+1));

            final EditText et_dob = localView.findViewById(R.id.et_dob);
            et_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDate(et_dob,2);
                }
            });

            ll_infant.addView(localView);
        }

        btn_flight_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag=true;
                /*Log.e("LL ll_adult",ll_adult.getChildCount()+"");
                Log.e("LL ll_child",ll_child.getChildCount()+"");
                Log.e("LL ll_infant",ll_infant.getChildCount()+"");*/
                String mob=et_mob.getText().toString();
                String email = et_email.getText().toString();
                String address = et_address.getText().toString();
                String pincode = et_pincode.getText().toString();

                if(TextUtils.isEmpty(mob)){
                    Toast.makeText(context, "Enter Mobile Number !", Toast.LENGTH_SHORT).show();
                    flag=false;
                }else if (mob.length()!=10) {
                    Toast.makeText(context, "Enter Correct Mobile Number !", Toast.LENGTH_SHORT).show();
                    flag=false;
                }else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(context, "Enter Email Id !", Toast.LENGTH_SHORT).show();
                    flag=false;
                }else if (!Service.isValidEmail(email)) {
                    Toast.makeText(context, "Enter Correct Email !", Toast.LENGTH_SHORT).show();
                    flag=false;
                }else if (TextUtils.isEmpty(address)) {
                    Toast.makeText(context, "Enter Address !", Toast.LENGTH_SHORT).show();
                    flag=false;
                }else if (TextUtils.isEmpty(pincode)) {
                    Toast.makeText(context, "Enter Pin code !", Toast.LENGTH_SHORT).show();
                    flag=false;
                }else {
                    ArrayList<HashMap<String,PassengerModel>> arrayListAdult = new ArrayList<HashMap<String, PassengerModel>>();
                    ArrayList<HashMap<String,PassengerModel>> arrayListChild = new ArrayList<HashMap<String, PassengerModel>>();
                    ArrayList<HashMap<String,PassengerModel>> arrayListInfant = new ArrayList<HashMap<String, PassengerModel>>();
                    if(flag) {
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
                                flag=false;
                                break;
                            } else if (TextUtils.isEmpty(et_fNmae.getText().toString())) {
                                Toast.makeText(context, "Enter First Name of Adult #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
                                break;
                            }else if (TextUtils.isEmpty(et_lName.getText().toString())) {
                                Toast.makeText(context, "Enter Last Name of Adult #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
                                break;
                            }else if (TextUtils.isEmpty(et_dob.getText().toString())) {
                                Toast.makeText(context, "Enter DOB of Adult #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
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
                                arrayListAdult.add(hashMap);
                            }
                        }
                    }
                    if(flag) {
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
                                flag=false;
                                break;
                            } else if (TextUtils.isEmpty(et_fNmae.getText().toString())) {
                                Toast.makeText(context, "Enter First Name of Infant #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
                                break;
                            }else if (TextUtils.isEmpty(et_lName.getText().toString())) {
                                Toast.makeText(context, "Enter Last Name of Infant #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
                                break;
                            }else if (TextUtils.isEmpty(et_dob.getText().toString())) {
                                Toast.makeText(context, "Enter DOB of Infant #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
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
                                arrayListChild.add(hashMap);
                            }
                        }
                    }
                    if(flag) {
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
                                flag=false;
                                break;
                            } else if (TextUtils.isEmpty(et_fNmae.getText().toString())) {
                                Toast.makeText(context, "Enter First Name of Child #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
                                break;
                            }else if (TextUtils.isEmpty(et_lName.getText().toString())) {
                                Toast.makeText(context, "Enter Last Name of Child #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
                                break;
                            }else if (TextUtils.isEmpty(et_dob.getText().toString())) {
                                Toast.makeText(context, "Enter DOB of Child #" + (i + 1)+"!", Toast.LENGTH_SHORT).show();
                                flag=false;
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
                                arrayListInfant.add(hashMap);
                            }
                        }
                    }
                    if(flag) {
                        Log.e("Passenger flight ",arrayListAdult.size()+"-Adult "+arrayListChild.size()+"-Child "+arrayListInfant.size()+"-Infant");
                        Intent intent = new Intent(context, ConfirmFlightBooking.class);
                        intent.putExtra("availSegmentsArrayList", availSegmentsArrayList);
                        intent.putExtra("TravelDate", TravelDate);
                        intent.putExtra("Origin", Origin);
                        intent.putExtra("Destination", Destination);
                        intent.putExtra("InfantCount", infant);
                        intent.putExtra("ChildCount", child);
                        intent.putExtra("AdultCount", adult);
                        intent.putExtra("TotalFare", tv_grand_fare.getText().toString());
                        intent.putExtra("position",position);
                        intent.putExtra("ClassCode",ClassCode);
                        intent.putExtra("mobile", mob);
                        intent.putExtra("email", email);
                        intent.putExtra("address", address);
                        intent.putExtra("pincode", pincode);
                        intent.putExtra("arrayListAdult", arrayListAdult);
                        intent.putExtra("arrayListChild", arrayListChild);
                        intent.putExtra("arrayListInfant", arrayListInfant);
                        startActivity(intent);
                    }
                }
            }
        });
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
