package in.msmartpay.agent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.material.textfield.TextInputEditText;

import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainRequest;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.user.DistrictData;
import in.msmartpay.agent.network.model.user.DistrictRequest;
import in.msmartpay.agent.network.model.user.DistrictResponse;
import in.msmartpay.agent.network.model.user.RegisterRequest;
import in.msmartpay.agent.network.model.user.StateData;
import in.msmartpay.agent.network.model.user.StateResponse;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Service;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Harendra on 7/17/2017.
 */

public class RegisterComplete extends BaseActivity {
    private ProgressDialog pd;
    private Context context = null;
    private Dialog dialog_status;
    private Button btn_confirm_signup;
    private TextInputEditText ed_fName,ed_mName, ed_lName, ed_email, ed_mobile, ed_address, ed_pinCode,ed_city,
    ed_outletName,ed_DOB,ed_pan_number,ed_aadhaar_no;
    //private TextInputEditText ed_outletAddress,ed_outletCity,ed_outletPinCode;
    private String gender="", stateName = "",districtName="",outletStateName="",outletDistrictName="",outletType="",signupState="",signupDistrict="";
    //private CheckBox cb_signup_same;
    private boolean flag = false;
    private List<String> outletTypeList;
    private ArrayList<String> arrayListState, arrayListDistrict;
    private SmartMaterialSpinner sp_signup_outlet_type,signup_state, signup_district;
    //private AutoCompleteTextView signup_state, signup_district, outlet_state, outlet_district;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.signup_activity);
        context = RegisterComplete.this;
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        TextView tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        TextView tv_done = findViewById(R.id.tv_done);
        ImageView iv_close = findViewById(R.id.iv_close);

        Util.hideView(tv_done);
        tv_toolbar_title.setText("Complete Registration");

        btn_confirm_signup=findViewById(R.id.btn_confirm_signup);
        ed_fName = findViewById(R.id.fname);
        ed_mName = findViewById(R.id.mname);
        ed_lName =  findViewById(R.id.lname);
        ed_email =  findViewById(R.id.email);
        ed_mobile =  findViewById(R.id.mob);
        ed_address =  findViewById(R.id.address);
        ed_pinCode =  findViewById(R.id.pincode);
        ed_city= findViewById(R.id.signup_city);
        ed_outletName = findViewById(R.id.signup_outlet_name);
        //ed_outletAddress = findViewById(R.id.outlet_address);
        //ed_outletCity = findViewById(R.id.outlet_city);
        //ed_outletPinCode = findViewById(R.id.outlet_pincode);
        ed_DOB = findViewById(R.id.signup_dob);
        ed_pan_number = findViewById(R.id.pan_number);
        ed_aadhaar_no =findViewById(R.id.aadhaar_number);

        ed_DOB.setInputType(InputType.TYPE_NULL);
        ed_DOB.setOnClickListener(v -> {
            Util.showCalender(ed_DOB);
        });

        sp_signup_outlet_type = findViewById(R.id.sp_signup_outlet_type);
        signup_state = findViewById(R.id.signup_state);
        signup_district =findViewById(R.id.signup_district);
        //outlet_state = findViewById(R.id.outlet_state);
        //outlet_district = findViewById(R.id.outlet_district);
        //cb_signup_same = findViewById(R.id.cb_signup_same);


        RadioButton radio_male =  findViewById(R.id.radio_male);
        RadioButton radio_female = (RadioButton) findViewById(R.id.radio_female);
        if(radio_male.isChecked())
            gender=radio_male.getText().toString();
        if(radio_female.isChecked())
            gender=radio_female.getText().toString();

        ed_mobile.setText(getIntent().getStringExtra("mob"));
        ed_mobile.setEnabled(false);

        //outlet type
        outletTypeList = Arrays.asList(getResources().getStringArray(R.array.outlet_type));
        sp_signup_outlet_type.setItem(outletTypeList);
        sp_signup_outlet_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>-1){
                    outletType = outletTypeList.get(position);
                }else {
                    outletType="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getState();

        //signup_state.setOnItemClickListener((adapterView, view, i, l) ->
                //getDistrict((String) adapterView.getItemAtPosition(i),signup_district));

        //outlet_state.setOnItemClickListener((adapterView, view, i, l) ->
                //getDistrict((String) adapterView.getItemAtPosition(i),outlet_district));

        signup_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>-1){
                    signupState=arrayListState.get(position);
                    getDistrict(signupState,signup_district);
                }else {
                    outletType="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        signup_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>-1){
                    signupDistrict=arrayListDistrict.get(position);

                }else {
                    outletType="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        iv_close.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btn_confirm_signup.setOnClickListener(v -> getRequest());
        /*
        cb_signup_same.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    ed_outletAddress.setText(ed_address.getText().toString());
                    ed_outletCity.setText(ed_city.getText().toString());
                    ed_outletPinCode.setText(ed_pinCode.getText().toString());
                    outlet_state.requestFocus();

                }else {

                }
            }
        });*/
    }


    public void getRequest() {

        if (TextUtils.isEmpty(ed_fName.getText().toString())) {
            Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
        } else if ( TextUtils.isEmpty(ed_DOB.getText().toString())) {
            Toast.makeText(this, "Please Select Date Of Birth", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_email.getText().toString())) {
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
        } else if (!Service.isValidEmail(ed_email.getText().toString())) {
            Toast.makeText(this, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_mobile.getText().toString()) || ed_mobile.getText().toString().length() < 10) {
            Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_address.getText().toString())) {
            Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(signupState)) {
            Toast.makeText(this, "Please Select State", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(signupDistrict)) {
            Toast.makeText(this, "Please Select District", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_city.getText().toString())) {
            Toast.makeText(this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_pinCode.getText().toString()) || ed_pinCode.getText().toString().length() < 6) {
            Toast.makeText(this, "Please Enter Valid Pin code", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(outletType) ) {
            Toast.makeText(this, "Please Select Company Type", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_outletName.getText().toString()) ) {
            Toast.makeText(this, "Please Enter Company Name", Toast.LENGTH_SHORT).show();
        } /*else if (TextUtils.isEmpty(ed_outletAddress.getText().toString())) {
            Toast.makeText(this, "Please Enter Outlet Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(outlet_state.getText().toString())) {
            Toast.makeText(this, "Please Select Outlet State", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(outlet_district.getText().toString())) {
            Toast.makeText(this, "Please Select Outlet District", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_outletCity.getText().toString())) {
            Toast.makeText(this, "Please Enter Outlet City Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_outletPinCode.getText().toString()) || ed_pinCode.getText().toString().length() < 6) {
            Toast.makeText(this, "Please Enter Valid Outlet Pin code", Toast.LENGTH_SHORT).show();
        } */else {

            if (NetworkConnection.isConnectionAvailable2(context)) {
                pd = new ProgressDialog(context);
                pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                RegisterRequest request = new RegisterRequest();
                request.setFirstname(ed_fName.getText().toString());
                request.setMiddlename(ed_mName.getText().toString());
                request.setLastname(ed_lName.getText().toString());
                request.setEmail(ed_email.getText().toString());
                request.setMobileno(ed_mobile.getText().toString());
                request.setGender(gender);
                request.setDateOfBirth(ed_DOB.getText().toString());

                request.setAddress(ed_address.getText().toString().trim());
                request.setState(signupState);
                request.setDistrict(signupDistrict);
                request.setCity(ed_city.getText().toString().trim());
                request.setPincode(ed_pinCode.getText().toString());

                request.setOfficeAddress("");
                request.setOfficeCountry("");
                request.setOfficeState("");
                request.setOfficeDistrict("");
                request.setOfficeCity("");
                request.setOfficePincode("0");

                request.setFirmname(ed_outletName.getText().toString());
                request.setCompanyType(outletType);
                request.setPanNumber(ed_pan_number.getText().toString().trim());
                request.setAadhaarNo(ed_aadhaar_no.getText().toString().trim());

                request.setLatitude("");
                request.setLatitude("");

                RetrofitClient.getClient(getApplicationContext())
                        .register(request).enqueue(new Callback<MainResponse2>() {
                    @Override
                    public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            MainResponse2 res = response.body();
                            if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                                flag = true;
                                showDialog(res.getResponseMessage());
                                L.toastS(getApplicationContext(), res.getResponseMessage());
                            } else if (res.getResponseCode() != null && res.getResponseCode().equals("2")) {
                                flag = true;
                                showDialog(res.getResponseMessage());
                                L.toastS(getApplicationContext(), res.getResponseMessage());
                            } else if (res.getResponseCode() != null && res.getResponseCode().equals("1")) {
                                flag = true;
                                showDialog(res.getResponseMessage());
                                L.toastS(getApplicationContext(), res.getResponseMessage());
                            } else {
                                flag = false;
                                L.toastS(context, "Wrong UserName or Password! ");

                            }
                        } else {
                            flag = false;
                            L.toastS(context, "Something went wrong!!!");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                        L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                        pd.dismiss();
                    }
                });
            }

        }
    }

    private void showDialog (String msg){
        dialog_status = new Dialog(RegisterComplete.this);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        statusImage.setImageResource(R.drawable.about);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        text.setText(msg);

        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v -> {
            if (flag == false) {
                dialog_status.dismiss();
            } else {
                dialog_status.dismiss();
                Intent intent = new Intent();
                intent.setClass(RegisterComplete.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        dialog_status.show();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDistrict(String state,SmartMaterialSpinner callerId) {
        if (NetworkConnection.isConnectionAvailable2(context)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            DistrictRequest request = new DistrictRequest();
            request.setState(state);

            RetrofitClient.getClient(getApplicationContext())
                    .getStateDistrict(request).enqueue(new Callback<DistrictResponse>() {
                @Override
                public void onResponse(@NotNull Call<DistrictResponse> call, @NotNull retrofit2.Response<DistrictResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        DistrictResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            arrayListDistrict = new ArrayList<>();
                            if (res.getDistrictList() != null)
                                for (DistrictData data : res.getDistrictList()) {
                                    arrayListDistrict.add(data.getDistrict());
                                }
                            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListDistrict);
                            //callerId.setAdapter(adapter);
                            callerId.setItem(arrayListDistrict);
                            //signup_district.setThreshold(1);
                        } else {
                            L.toastS(getApplicationContext(), res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DistrictResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failure " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void getState() {
        if (NetworkConnection.isConnectionAvailable2(context)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            MainRequest request = new MainRequest();

            RetrofitClient.getClient(getApplicationContext())
                    .getState(request).enqueue(new Callback<StateResponse>() {
                @Override
                public void onResponse(@NotNull Call<StateResponse> call, @NotNull retrofit2.Response<StateResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        StateResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            arrayListState = new ArrayList<>();
                            if (res.getStateList() != null)
                                for (StateData data : res.getStateList()) {
                                    arrayListState.add(data.getState());
                                }
                            signup_state.setItem(arrayListState);
                            //outlet_state.setAdapter(adapter);

                            L.toastS(getApplicationContext(), res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<StateResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failure " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }
}

