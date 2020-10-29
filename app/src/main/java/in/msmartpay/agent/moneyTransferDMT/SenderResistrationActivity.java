package in.msmartpay.agent.moneyTransferDMT;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

/**
 * Created by Smartkinda on 7/5/2017.
 */

public class SenderResistrationActivity extends BaseActivity {

    private EditText editMobileNo, editOTP, editFirstName, editLastName, editDOB, editPinCode, editAddress;
    private Button btnResister;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_confirm_sender_resister = HttpURL.CONFIRMATION_SENDER_RESISTRATION;
    private String url_find_sender = HttpURL.FIND_SENDER;
    private String agentID, txnKey, SessionID, mobileNumber;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog chddDatePickerDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_sender_resisteration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sender Resistration");

        context = SenderResistrationActivity.this;
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
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
        });

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        mobileNumber = getIntent().getStringExtra("MobileNo");

        editMobileNo =  findViewById(R.id.sender_sender_mobile);
        editOTP =  findViewById(R.id.sender_otp);
        editFirstName =  findViewById(R.id.sender_first_name);
        editLastName =  findViewById(R.id.sender_last_name);
        editDOB =  findViewById(R.id.sender_dob);
        editPinCode =  findViewById(R.id.sender_pincode);
        editAddress =  findViewById(R.id.sender_address);
        btnResister =  findViewById(R.id.btn_resister);

        editMobileNo.setText(mobileNumber);
        editMobileNo.setEnabled(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chddDatePickerDialog.show();
            }
        });
        findViewsById();
        setchDDDateTimeField();


        btnResister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(editMobileNo.getText().toString().trim())) {
                    editMobileNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editOTP.getText().toString().trim())) {
                    editOTP.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editFirstName.getText().toString().trim())) {
                    editFirstName.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editLastName.getText().toString().trim())) {
                    editLastName.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editDOB.getText().toString().trim())) {
                    editDOB.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Set Date Of Birth", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editPinCode.getText().toString().trim())) {
                    editPinCode.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Pin Code", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editAddress.getText().toString().trim())) {
                    editAddress.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                }else{
                     confirmationSenderResisterRequest();
                }
            }
        });
    }

    //For Set Date Of Birth
    private void findViewsById() {
        editDOB.setInputType(InputType.TYPE_NULL);
    }

    private void setchDDDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        chddDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                System.out.println("DateOfBirth--->"+editDOB.getText().toString());
                editDOB.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    //Json request for confirmation Sender Resister
    private void confirmationSenderResisterRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", editMobileNo.getText().toString().trim())
                    .put("otp", editOTP.getText().toString())
                    .put("FirstName", editFirstName.getText().toString().trim())
                    .put("LastName", editLastName.getText().toString().trim())
                    .put("dob", editDOB.getText().toString().trim())
                    .put("Pincode", editPinCode.getText().toString().trim())
                    .put("Address", editAddress.getText().toString().trim());
            Log.e("Request",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_confirm_sender_resister, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----confirmationSenderResister>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called", url_confirm_sender_resister);
                                    L.m2("url data", object.toString());
                                    editor.putString("ResisteredMobileNo", editMobileNo.getText().toString().trim());
                                    editor.commit();
                                    showConfirmationDialog(object.getString("message").toString());
                                }
                                else {
                                    pd.dismiss();
                                    showConfirmationDialog(object.getString("message").toString());
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
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
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    //Confirmation Dialog
    public void showConfirmationDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr_sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit =  d.findViewById(R.id.btn_resister_ok);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);
        final TextView title = (TextView) d.findViewById(R.id.title);
        final TextView Message = (TextView) d.findViewById(R.id.tv_confirmation_dialog);

        btnClosed.setVisibility(View.GONE);
        btnSubmit.setText("OK");
        title.setText("Confirmation");
        Message.setText(msg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(jsonObject.getString("Status").equalsIgnoreCase("0")){
                        findSenderRequest();
                        d.dismiss();
                    }else{
                        d.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                d.cancel();
            }
        });
        btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                d.cancel();
            }
        });
        d.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void findSenderRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txnKey)
                    .put("SessionId", SessionID)
                    .put("SenderId", mobileNumber);
            Log.e("Request",jsonObjectReq.toString());
            L.m2("url-called", url_find_sender);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("url data", object.toString());
                            try {

                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                   MainActivity.jsonObjectStatic=object;
                                    editor.putString("ResisteredMobileNo", mobileNumber.toString());
                                    editor.commit();
                                    Intent intent = new Intent(context, MoneyTransferActivity1.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }
}