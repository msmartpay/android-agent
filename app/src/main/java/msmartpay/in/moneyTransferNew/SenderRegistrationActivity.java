package msmartpay.in.moneyTransferNew;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

public class SenderRegistrationActivity extends AppCompatActivity {

    private EditText register_sender_mobile, register_sender_name, register_sender_dob, register_sender_pincode;
    private Button btn_regis_resister;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog chddDatePickerDialog;
    private String agentID, txnKey, mobileNumber, SenderId, SenderName;
    private ProgressDialog pd;
    private String url_sender_resistration = HttpURL.SENDER_RESISTRATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dmr_sender_registration_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sender Registration");

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

        register_sender_mobile =  findViewById(R.id.register_sender_mobile);
        register_sender_name =  findViewById(R.id.register_sender_name);
        register_sender_dob =  findViewById(R.id.register_sender_dob);
        register_sender_pincode =  findViewById(R.id.register_sender_pincode);
        btn_regis_resister =  findViewById(R.id.btn_regis_resister);

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        register_sender_mobile.setText(mobileNumber);
        register_sender_mobile.setEnabled(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        register_sender_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chddDatePickerDialog.show();
            }
        });
        findViewsById();
        setchDDDateTimeField();

        btn_regis_resister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(register_sender_mobile.getText().toString().trim())) {
                    register_sender_mobile.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(register_sender_name.getText().toString().trim())) {
                    register_sender_name.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(register_sender_dob.getText().toString().trim())) {
                    register_sender_dob.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Set Date Of Birth", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(register_sender_pincode.getText().toString().trim())) {
                    register_sender_pincode.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Pin Code", Toast.LENGTH_SHORT).show();
                }else{
                    senderRegistrationRequest();
                }
            }
        });

    }

    //For Set Date Of Birth
    private void findViewsById() {
        register_sender_dob.setInputType(InputType.TYPE_NULL);
    }
    private void setchDDDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        chddDatePickerDialog = new DatePickerDialog(SenderRegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                System.out.println("DateOfBirth--->"+register_sender_dob.getText().toString());
                register_sender_dob.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    //RequestForRegister
    private void senderRegistrationRequest() {
        pd = ProgressDialog.show(SenderRegistrationActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txnKey)
                    .put("AgentID", agentID)
                    .put("SenderId", mobileNumber)
                    .put("SenderName", register_sender_name.getText().toString().trim())
                    .put("Address", register_sender_pincode.getText().toString().trim())
                    .put("DOB", register_sender_dob.getText().toString().trim());

            L.m2("Req-senderRegis", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_sender_resistration, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                           // jsonObjectStatic = object;
                            System.out.println("Object--senderResistration>" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called", url_sender_resistration);
                                    L.m2("resp-senderRegis", object.toString());

                                    Intent intent = new Intent(SenderRegistrationActivity.this, SenderVerifyRegisterActivity.class);
                                    intent.putExtra("MobileNo", mobileNumber);
                                    intent.putExtra("SenderName", register_sender_name.getText().toString().trim());
                                    intent.putExtra("Address", register_sender_pincode.getText().toString().trim());
                                    intent.putExtra("DOB", register_sender_dob.getText().toString().trim());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(SenderRegistrationActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
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
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

}
