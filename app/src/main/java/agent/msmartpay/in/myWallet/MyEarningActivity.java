package agent.msmartpay.in.myWallet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

public class MyEarningActivity extends BaseActivity {
    private EditText fromDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private Button search;
    private ScrollView vi;
    private ProgressDialog pd;
    private String agent_id = "", txn_key = "", agentID;
    private Context context;
    private SimpleDateFormat dateFormatter;
    private TextView totRe,toterng,totde;
    private String my_earning_url= HttpURL.TURN_OVER;
    private SharedPreferences sharedPreferences;
    private ImageView iv_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_earning_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Earning");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context=MyEarningActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        agent_id=sharedPreferences.getString("agent_id", null);

        vi=(ScrollView)findViewById(R.id.scroll);
        totRe=(TextView)findViewById(R.id.totalReq);
        totde=(TextView)findViewById(R.id.totaldeduction);
        toterng=(TextView)findViewById(R.id.totalcomm);
        fromDateEtxt =  findViewById(R.id.my_date);
        iv_calender = (ImageView) findViewById(R.id.iv_calender);
        search= findViewById(R.id.search);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();
            }
        });

        iv_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEarningRequest();
            }
        });


    }

    private void myEarningRequest() {
        if(isConnectionAvailable()){
            if (fromDateEtxt.getText().toString().trim().length() > 0) {
                pd = new ProgressDialog(context);
                pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setCancelable(true);

                try{
                    JSONObject jsonObjectReq=new JSONObject()
                            .put("agent_id", agentID)
                            .put("date", fromDateEtxt.getText().toString().trim())
                            .put("txn_key", txn_key);

                    L.m2("url-earning", my_earning_url);
                    L.m2("Request--earning",jsonObjectReq.toString());
                    JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, my_earning_url, jsonObjectReq, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--earning", data.toString());
                            try {
                                if (data.get("response-code") != null && data.get("response-code").equals("0")) {
                                    vi.setVisibility(View.VISIBLE);
                                    totRe.setText(data.getString("response-reqamt"));
                                    totde.setText(data.getString("response-deduction"));
                                    toterng.setText(data.getString("response-comm"));
                                } else if (data.get("response-code") != null && data.get("response-code").equals("1")) {
                                    vi.setVisibility(View.GONE);
                                    Toast.makeText(context, data.getString("response-message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    vi.setVisibility(View.GONE);
                                    Toast.makeText(context, "No Transaction Available! ", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(context, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    getSocketTimeOut(objectRequest);
                    Mysingleton.getInstance(context).addToRequsetque(objectRequest);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Select Date. ", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
