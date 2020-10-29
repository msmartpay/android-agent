package agent.msmartpay.in;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.Locale;

import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;


public class MyProfile extends BaseActivity {
    private TextView userName,name,mobileno,email;
    private String agentID;
    private String url= HttpURL.WALLET_BALANCE;
    private static final int SELECTED_PICTURE = 1;
    final int PIC_CROP = 3;
    private ImageView displayphoto;
    private JSONObject data = null;
    private Uri picUri;
    private SharedPreferences myPrefs;
    public static Bitmap thePic = null;
    private Button btn_addAmount,btn_kycApply,btn_edit;
    private String balance,agent_id_full,emailId,agentName,agentMobile,kycstatus;
    private String txn_key,addmoney,firmname;
    private TextView bal_value,tv_info_text;
    private ImageView iv_kyc;
    private AutoCompleteTextView actv_p_gender,actv_p_country,actv_p_state,actv_p_district,actv_p_city ;
    private EditText et_p_name,et_p_age,et_p_mob,et_p_email,et_p_address, et_p_adhar,et_p_pan_no,et_p_pin,et_p_last_name,et_p_holder_name;
    private TextInputLayout til_adhar_holder,til_adhar;
    private Context context;
    private String url_profile=HttpURL.PROFILE_URL;
    private String url_profile_update=HttpURL.PROFILE_UPDATE_URL;
    private String url_state=HttpURL.STATE_URL;
    private String url_district=HttpURL.DISTRICT_BY_STATE_URL;
    private String url_kyc=HttpURL.KYC_UPDATE_URL;
    private String url_kyc_DETAILS=HttpURL.KYC_DETAILS_URL;
    private ProgressDialog pd,pd2;
    private ProgressBar pb_p_district,pb_p_state;
    boolean flag=true, flagKYC=true;
    private SimpleDateFormat dateFormatter;
    private Bitmap dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sk_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Profile");
        context = MyProfile.this;
        myPrefs = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        addmoney=myPrefs.getString("addmoney",null);
        agent_id_full = myPrefs.getString("agent_id_full", null);
        agentID = myPrefs.getString("agentonlyid", null);
        emailId = myPrefs.getString("emailId", null);
        txn_key = myPrefs.getString("txn-key", null);
        agentName = myPrefs.getString("agentName", null);
        agentMobile = myPrefs.getString("agentMobile", null);
        balance = myPrefs.getString("balance", null);

        tv_info_text = (TextView) findViewById(R.id.tv_info_text);
        bal_value = (TextView) findViewById(R.id.tv_amount);
        bal_value.setText("\u20B9 " +balance);

        et_p_name =  findViewById(R.id.et_p_name);
        et_p_age =  findViewById(R.id.et_p_age);
        et_p_mob =  findViewById(R.id.et_p_mob);
        et_p_address =  findViewById(R.id.et_p_address);
        et_p_email =  findViewById(R.id.et_p_email);
        et_p_adhar =  findViewById(R.id.et_p_adhar);
        et_p_pan_no=  findViewById(R.id.et_p_pan_no);
        et_p_pin=  findViewById(R.id.et_p_pin);
        et_p_last_name=  findViewById(R.id.et_p_last_name);
        et_p_holder_name=  findViewById(R.id.et_p_holder_name);


        actv_p_gender = (AutoCompleteTextView) findViewById(R.id.actv_p_gender);
        actv_p_country= (AutoCompleteTextView) findViewById(R.id.actv_p_country);
        actv_p_state= (AutoCompleteTextView) findViewById(R.id.actv_p_state);
        actv_p_district= (AutoCompleteTextView) findViewById(R.id.actv_p_district);
        actv_p_city = (AutoCompleteTextView) findViewById(R.id.actv_p_city);
        pb_p_state = (ProgressBar) findViewById(R.id.pb_p_state);
        pb_p_district = (ProgressBar) findViewById(R.id.pb_p_district);
        //Non-Editable
        nonEditable();

        til_adhar = (TextInputLayout) findViewById(R.id.til_adhar);
        til_adhar_holder = (TextInputLayout) findViewById(R.id.til_adhar_holder);
        btn_kycApply =  findViewById(R.id.btn_kycApply);
        til_adhar_holder.setVisibility(View.GONE);
        til_adhar.setVisibility(View.GONE);

        btn_edit =  findViewById(R.id.btn_edit);
        btn_addAmount =  findViewById(R.id.btn_addAmount);
        btn_addAmount.setVisibility(View.GONE);
        iv_kyc = (ImageView) findViewById(R.id.tv_kyc);
        getProfile();
        btn_kycApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = btn_kycApply.getText().toString();
                if(text.equalsIgnoreCase("Request Now")){
                    btn_kycApply.setText("Apply");
                    et_p_adhar.setEnabled(true);
                    et_p_adhar.setFocusableInTouchMode(true);
                    et_p_holder_name.setEnabled(true);
                    et_p_holder_name.setFocusableInTouchMode(true);
                    til_adhar_holder.setVisibility(View.VISIBLE);
                    til_adhar.setVisibility(View.VISIBLE);
                }else if(text.equalsIgnoreCase("Apply")){
                    if (TextUtils.isEmpty(et_p_adhar.getText().toString())) {
                        Toast.makeText(context, "  Enter Adhar Number!  ", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(et_p_holder_name.getText().toString())) {
                        Toast.makeText(context, "  Enter Adhar Holder Name!  ", Toast.LENGTH_SHORT).show();
                    } else {
                        getKYC();
                    }
                }else if(text.equalsIgnoreCase("View")){
                    btn_kycApply.setText("Hide");
                    et_p_adhar.setEnabled(false);
                    et_p_adhar.setFocusableInTouchMode(false);
                    et_p_holder_name.setEnabled(false);
                    et_p_holder_name.setFocusableInTouchMode(false);
                    til_adhar_holder.setVisibility(View.VISIBLE);
                    til_adhar.setVisibility(View.VISIBLE);
                }else if(text.equalsIgnoreCase("Hide")){
                    btn_kycApply.setText("View");
                    til_adhar_holder.setVisibility(View.GONE);
                    til_adhar.setVisibility(View.GONE);
                }
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit_done=  btn_edit.getText().toString();
                if(edit_done.equalsIgnoreCase("Edit")){
                    getState();
                    btn_edit.setText("Done");
                    editable();
                }else {
                    getProfileUpdate();
                }
            }
        });
        et_p_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        try {
                           String colDepartDate = dateFormatter.format(newDate.getTime());
                            if (!colDepartDate.equalsIgnoreCase("")) {

                                et_p_age.setText(colDepartDate);
                            } else {
                                Toast.makeText(context, "Invalid From Date.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                //fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDatePickerDialog.show();
            }
        });

        actv_p_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getDistrict((String) adapterView.getItemAtPosition(i));
            }
        });


        displayphoto = (ImageView) findViewById(R.id.id_my_profile_picc);
        if(null!= dp)
        {
            displayphoto.setImageBitmap(dp);
        }

     /*    if(addmoney.equalsIgnoreCase("N")){
            btn_addAmount.setVisibility(View.INVISIBLE);
        }

        //Payment Gateway
       btn_addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAmountInWalletActivity.class);
                startActivity(intent);
            }
        });*/

       //chang picture
        displayphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
            }
        });
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        TextView tv_message= (TextView) findViewById(R.id.tv_message);
        getBalance();
        if(getIntent().getStringExtra("message")!=null){
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setText(getIntent().getStringExtra("message"));
        }else {
            tv_message.setVisibility(View.GONE);
        }
    }*/

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBalance(){
        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject()
                            .put("agent_id", agentID)
                            .put("txn_key", txn_key),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            try {
                                L.m2("called url", url);
                                L.m2("data from url", data.toString());
                                if (data.getString("Status") != null && data.getString("Status").equals("0")) {
                                    myPrefs = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor prefsEditor = myPrefs.edit();
                                    prefsEditor.putString("balance", "" + data.getString("updBal"));
                                    prefsEditor.commit();
                                    bal_value.setText("\u20B9 " + data.getString("updBal"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Some Technical Problem !", Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getProfile(){
        pd2 = new ProgressDialog(context);
        pd2 = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            L.m2("url", url_profile);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url_profile,
                    new JSONObject()
                            .put("agentID", agentID)
                            .put("txn_key", txn_key),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd2.dismiss();
                            try {
                                L.m2("response", data.toString());
                                if (data.getString("status") != null && data.getString("status").equals("0")) {
                                    JSONObject jsonObject= data.getJSONObject("profile");
                                    kycstatus=jsonObject.getString("kycstatus");
                                    getKYCStatus();
                                    if(kycstatus.equalsIgnoreCase("1")){//0-not Activated and 1-Activated
                                        btn_kycApply.setText("View");
                                    }else if(kycstatus.equalsIgnoreCase("0")){
                                        btn_kycApply.setText("Apply");
                                    }
                                    firmname = jsonObject.getString("firmname");
                                    et_p_name.setText(jsonObject.getString("firstname"));
                                    et_p_last_name.setText(jsonObject.getString("lastname"));
                                    et_p_age.setText(jsonObject.getString("DateOfBirth"));
                                    et_p_mob.setText(jsonObject.getString("mobile"));
                                    actv_p_gender.setText(jsonObject.getString("gender"));
                                    et_p_email.setText(jsonObject.getString("emailID"));
                                    actv_p_country.setText(jsonObject.getString("country"));
                                    actv_p_state.setText(jsonObject.getString("state"));
                                    actv_p_city.setText(jsonObject.getString("city"));
                                    et_p_address.setText(jsonObject.getString("address"));
                                    actv_p_district.setText(jsonObject.getString("district"));
                                    et_p_pan_no.setText(jsonObject.getString("pannumber"));
                                    et_p_pin.setText(jsonObject.getString("pincode"));
                                    et_p_adhar.setText(jsonObject.getString("adharnumber"));
                                }else {
                                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd2.dismiss();
                    Toast.makeText(context, "Some Technical Problem !", Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getKYCStatus(){
        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            L.m2("url", url_kyc_DETAILS);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url_kyc_DETAILS,
                    new JSONObject()
                            .put("agentID", agentID)
                            .put("txn_key", txn_key),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            try {
                                L.m2("response", data.toString());
                                if (data.getString("status") != null && data.getString("status").equals("0")) {//Successfully applied kyc
                                    til_adhar_holder.setVisibility(View.GONE);
                                    til_adhar.setVisibility(View.GONE);
                                    tv_info_text.setVisibility(View.GONE);
                                    til_adhar_holder.setVisibility(View.GONE);
                                    til_adhar.setVisibility(View.GONE);
                                    iv_kyc.setVisibility(View.GONE);
                                    btn_kycApply.setVisibility(View.VISIBLE);
                                    tv_info_text.setVisibility(View.VISIBLE);
                                    et_p_adhar.setText(data.getString("adharcardNo"));
                                    et_p_holder_name.setText(data.getString("adharHoldername"));
                                    if(!data.getString("kycRequeststatus").equalsIgnoreCase("Pending")){
                                        tv_info_text.setText(data.getString("kycRequeststatus"));
                                        btn_kycApply.setText("View");
                                    }else {
                                        btn_kycApply.setText("View");
                                        tv_info_text.setText("Your request is under process.");
                                    }
                                   // Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }else  if (data.getString("status") != null && data.getString("status").equals("1")) {//Enable to Applied kyc
                                    iv_kyc.setVisibility(View.GONE);
                                    tv_info_text.setText("Upgrade KYC,to get exclusive benefits.");
                                    btn_kycApply.setText("Request Now");
                                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "No Response", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Some Technical Problem !", Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getKYC(){
        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            L.m2("url", url_kyc);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url_kyc,
                    new JSONObject()
                            .put("agentID", agentID)
                            .put("txn_key", txn_key)
                            .put("adharcardNo",et_p_adhar.getText().toString())
                            .put("adharHoldername",et_p_holder_name.getText().toString())
                            .put("email",et_p_email.getText().toString())
                            .put("mobileno",et_p_mob.getText().toString()),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            try {
                                L.m2("response", data.toString());
                                if (data.getString("status") != null && data.getString("status").equals("0")) {//Successfully applied kyc
                                    til_adhar_holder.setVisibility(View.GONE);
                                    til_adhar.setVisibility(View.GONE);
                                    tv_info_text.setVisibility(View.VISIBLE);
                                    tv_info_text.setText( data.getString("message"));
                                    btn_kycApply.setVisibility(View.GONE);
                                    iv_kyc.setVisibility(View.GONE);
                                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }else  if (data.getString("status") != null && data.getString("status").equals("1")) {//Enable to Applied kyc
                                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }else if (data.getString("status") != null && data.getString("status").equals("2")) {//Already  Applied kyc
                                    til_adhar_holder.setVisibility(View.GONE);
                                    til_adhar.setVisibility(View.GONE);
                                    tv_info_text.setVisibility(View.GONE);
                                    btn_kycApply.setVisibility(View.GONE);
                                    iv_kyc.setVisibility(View.GONE);
                                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "No Response", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Some Technical Problem !", Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getState(){
        try {
            pb_p_state.setVisibility(View.VISIBLE);
            L.m2("url", url_state);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url_state,
                    new JSONObject()
                            .put("agentID", agentID)
                            .put("txn_key", txn_key),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pb_p_state.setVisibility(View.GONE);
                            try {
                                L.m2("response", data.toString());
                                if (data.getString("status") != null && data.getString("status").equals("0")) {
                                    JSONArray jsonArray= data.getJSONArray("StateList");
                                    ArrayList<String> arrayListState = new ArrayList<>();
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        arrayListState.add(jsonObject.getString("state"));
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arrayListState);
                                    actv_p_state.setAdapter(adapter);
                                    actv_p_state.setThreshold(1);
                                    if(flag){
                                        flag=false;
                                        getDistrict(actv_p_state.getText().toString());
                                    }
                                }else {
                                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pb_p_state.setVisibility(View.GONE);
                    Toast.makeText(context, "Some Technical Problem !", Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getDistrict(String state){
        pb_p_district.setVisibility(View.VISIBLE);
        try {
            L.m2("url", url_district);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url_district,
                    new JSONObject()
                            .put("agentID", agentID)
                            .put("txn_key", txn_key)
                            .put("state",state),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pb_p_district.setVisibility(View.GONE);
                            try {

                                L.m2("response", data.toString());
                                if (data.getString("status") != null && data.getString("status").equals("0")) {
                                    JSONArray jsonArray= data.getJSONArray("districtList");
                                    ArrayList<String> arrayListDistrict = new ArrayList<>();
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        arrayListDistrict.add(jsonObject.getString("district"));
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arrayListDistrict);
                                    actv_p_district.setAdapter(adapter);
                                    actv_p_district.setThreshold(1);

                                }else {
                                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pb_p_district.setVisibility(View.GONE);
                    Toast.makeText(context, "Some Technical Problem !", Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getProfileUpdate(){
        if(getValidation()){
            pd2 = new ProgressDialog(context);
            pd2 = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("agentID", agentID)
                        .put("txn_key", txn_key)
                        .put("firmname", firmname)
                        .put("firstname", et_p_name.getText().toString())
                        .put("lastname", et_p_last_name.getText().toString())
                        .put("DateOfBirth", et_p_age.getText().toString())
                        .put("mobileno", et_p_mob.getText().toString())
                        .put("gender", actv_p_gender.getText().toString())
                        .put("email", et_p_email.getText().toString())
                        .put("country", actv_p_country.getText().toString())
                        .put("state", actv_p_state.getText().toString())
                        .put("city", actv_p_city.getText().toString())
                        .put("address", et_p_address.getText().toString())
                        .put("District", actv_p_district.getText().toString())
                        .put("pannumber", et_p_pan_no.getText().toString())
                        .put("pincode", et_p_pin.getText().toString());
                L.m2("url", url_profile_update);
                L.m2("request", jsonObject.toString());
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url_profile_update, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject data) {
                                pd2.dismiss();
                                try {
                                    L.m2("response", data.toString());
                                    if (data.getString("status") != null && data.getString("status").equals("0")) {
                                        btn_edit.setText("Edit");
                                        nonEditable();
                                        Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, data.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd2.dismiss();
                        Toast.makeText(context, "Some Technical Problem !", Toast.LENGTH_SHORT).show();
                    }
                });
                getSocketTimeOut(objectRequest);
                Mysingleton.getInstance(context).addToRequsetque(objectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void nonEditable(){
        et_p_last_name.setEnabled(false);
        et_p_last_name.setFocusableInTouchMode(false);
        et_p_pin.setEnabled(false);
        et_p_pin.setFocusableInTouchMode(false);
         et_p_pan_no.setEnabled(false);
        et_p_pan_no.setFocusableInTouchMode(false);
        et_p_name.setEnabled(false);
        et_p_name.setFocusableInTouchMode(false);
        et_p_age.setEnabled(false);
        et_p_age.setFocusableInTouchMode(false);
        et_p_mob.setEnabled(false);
        et_p_mob.setFocusableInTouchMode(false);
        actv_p_gender.setEnabled(false);
        actv_p_gender.setFocusableInTouchMode(false);
        et_p_email.setEnabled(false);
        et_p_email.setFocusableInTouchMode(false);
        actv_p_country.setEnabled(false);
        actv_p_country.setFocusableInTouchMode(false);
        actv_p_state.setEnabled(false);
        actv_p_state.setFocusableInTouchMode(false);
        actv_p_city.setEnabled(false);
        actv_p_city.setFocusableInTouchMode(false);
        et_p_address.setEnabled(false);
        et_p_address.setFocusableInTouchMode(false);
        actv_p_district.setEnabled(false);
        actv_p_district.setFocusableInTouchMode(false);
    }
    private void editable(){
        et_p_last_name.setEnabled(true);
        et_p_last_name.setFocusableInTouchMode(true);
        et_p_pin.setEnabled(true);
        et_p_pin.setFocusableInTouchMode(true);
         et_p_pan_no.setEnabled(true);
        et_p_pan_no.setFocusableInTouchMode(true);
        et_p_name.setEnabled(true);
        et_p_name.setFocusableInTouchMode(true);
        et_p_age.setEnabled(true);
        et_p_age.setFocusableInTouchMode(true);
        actv_p_gender.setEnabled(true);
        actv_p_gender.setFocusableInTouchMode(true);
        actv_p_district.setEnabled(true);
        actv_p_district.setFocusableInTouchMode(true);
       // actv_p_country.setEnabled(true);
       // actv_p_country.setFocusableInTouchMode(true);
        actv_p_state.setEnabled(true);
        actv_p_state.setFocusableInTouchMode(true);
        actv_p_city.setEnabled(true);
        actv_p_city.setFocusableInTouchMode(true);
        et_p_address.setEnabled(true);
        et_p_address.setFocusableInTouchMode(true);
    }

    private boolean getValidation(){

        if(TextUtils.isEmpty(et_p_name.getText().toString())){
            Toast.makeText(context, "Please First Name!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(et_p_last_name.getText().toString())){
            Toast.makeText(context, "Please Last Name!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(et_p_age.getText().toString())){
            Toast.makeText(context, "Please Enter Age!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(actv_p_gender.getText().toString())){
            Toast.makeText(context, "Please Enter Gender Type!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(et_p_pin.getText().toString())){
            Toast.makeText(context, "Please Enter Pin Code!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(actv_p_state.getText().toString())){
            Toast.makeText(context, "Please Enter State Name!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(actv_p_district.getText().toString())){
            Toast.makeText(context, "Please Enter District Name!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(actv_p_city.getText().toString())){
            Toast.makeText(context, "Please Enter City Name!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(et_p_address.getText().toString())){
            Toast.makeText(context, "Please Enter Address!", Toast.LENGTH_SHORT).show();
        }else {
            return true;
        }

        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECTED_PICTURE:
                if (resultCode == RESULT_OK) {
                    picUri = data.getData();
                    performCrop();
                }
                break;
            case PIC_CROP:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    thePic = (Bitmap) extras.get("data");
                    displayphoto.setImageBitmap(thePic);
                }
                break;
            default:
                break;
        }
    }
    public void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch(ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
