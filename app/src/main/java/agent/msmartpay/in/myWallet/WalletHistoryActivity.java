package agent.msmartpay.in.myWallet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import agent.msmartpay.in.MainActivity;
import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

public class WalletHistoryActivity extends BaseActivity {
    private ProgressDialog pd;
    private ArrayList<TransactionItems> tranList = new ArrayList<TransactionItems>();
    private ListView transactionList;
    private String agent_id = "", txn_key = "";
    private String wallet_history_url = HttpURL.STATEMENT_TRAIN;
    private String TranHistoryByDate= HttpURL.TranHistoryByDate;
    private SharedPreferences sharedPreferences;
    private String agentID, url;
    private Context context;
    private TextView tv_to,tv_from,tv_all,tv_in,tv_out;
    private LinearLayout ll_search;
    private StatementAdapter adapter;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private int i=0;
    private String toDate="",fromDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_history_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Wallet History");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        tv_to = findViewById(R.id.tv_to);
        tv_from =  findViewById(R.id.tv_from);
        ll_search =  findViewById(R.id.ll_search);
        tv_all = findViewById(R.id.tv_all);
        tv_in =  findViewById(R.id.tv_in);
        tv_out =  findViewById(R.id.tv_out);

        context = WalletHistoryActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        agentID = sharedPreferences.getString("agentonlyid", null);
        agent_id = sharedPreferences.getString("agent_id", null);
        txn_key = sharedPreferences.getString("txn-key", null);
        txn_key = txn_key.replaceAll("~", "/");
        transactionList = (ListView) findViewById(R.id.transactionList);


        walletHistoryRequest();

        tv_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                setDateTimeField(tv_to);
            }
        });

        tv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                setDateTimeField(tv_from);
            }
        });
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromDate.equalsIgnoreCase("")){
                    Toast.makeText(WalletHistoryActivity.this,"Select From Date", Toast.LENGTH_LONG).show();
                }else if(toDate.equalsIgnoreCase("")){
                    Toast.makeText(WalletHistoryActivity.this,"Select From Date", Toast.LENGTH_LONG).show();
                }else {

                        ll_search.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        walletHistoryRequestByDate();

                }
            }
        });

        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter("");
                tv_all.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tv_in.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_out.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        tv_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter("In");
                tv_all.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_in.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tv_out.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        tv_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter("Out");
                tv_all.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_in.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_out.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

    }

    private void walletHistoryRequest() {
        try {
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(false);
            pd.show();
            JSONObject jsonObjectReq = new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("id_no", "0");

            L.m2("url-quick", wallet_history_url);
            L.m2("Request--quick", jsonObjectReq.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, wallet_history_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Resp--tranHistory", data.toString());
                            try {
                                if (data.get("response-code") != null && data.get("response-code").equals("0")) {
                                    if (data.get("Statement") == null) {
                                        Toast.makeText(context, "No Transaction Available! ", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(context, MainActivity.class));
                                        finish();
                                    } else {
                                        final JSONArray parentArray = (JSONArray) data.get("Statement");
                                        if (parentArray.length() > 0) {
                                            for (int i = 0; i < parentArray.length(); i++) {
                                                final JSONObject obj = (JSONObject) parentArray.get(i);
                                                TransactionItems item = new TransactionItems(
                                                        obj.getString("Id_No") + "",
                                                        obj.getString("tran_id") + "",
                                                        obj.getString("mobile_operator") + "",
                                                        obj.getString("mobile_number") + "",
                                                        obj.getString("service") + "",
                                                        obj.getString("Action_on_bal_amt") + "",
                                                        obj.getString("tran_status") + "",
                                                        obj.getString("net_amout") + "",
                                                        obj.getString("DeductedAmt") + "",
                                                        obj.getString("dot") + "",
                                                        obj.getString("tot") + "",
                                                        obj.getString("Agent_balAmt_b_Ded") + "",
                                                        obj.getString("Agent_F_balAmt") + "");
                                                tranList.add(item);

                                            }
                                             adapter=new StatementAdapter(context, tranList);
                                            transactionList.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(context, "No Transaction Available! ", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(context, MainActivity.class));
                                            finish();
                                        }
                                    }
                                } else if (data.get("response-code") != null && data.get("response-code").equals("1") /*&& data.get("response-code").equals("2")*/) {
                                    Toast.makeText(context, (String) data.get("response-message"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                } else if (data.get("response-code") != null && data.get("response-code").equals("2")) {
                                    Toast.makeText(context, (String) data.get("response-message"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                } else {
                                    context = getApplicationContext();
                                    Toast.makeText(context, "This is a server error", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG);
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void walletHistoryRequestByDate() {
        try {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(false);

            JSONObject request=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("fromDate", fromDate)
                    .put("toDate", toDate);
            L.m2("called url", TranHistoryByDate);
            L.m2("Request--quick", request.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, TranHistoryByDate, request,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Resp--tranHistory", data.toString());
                            try {
                                if (data.get("response-code") != null && data.get("response-code").equals("0")) {
                                    if (data.get("Statement") == null) {
                                        Toast.makeText(context, "No Transaction Available! ", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(context, MainActivity.class));
                                        finish();
                                    } else {
                                        final JSONArray parentArray = (JSONArray) data.get("Statement");
                                        if (parentArray.length() > 0) {
                                            for (int i = 0; i < parentArray.length(); i++) {
                                                final JSONObject obj = (JSONObject) parentArray.get(i);
                                                TransactionItems item = new TransactionItems(
                                                        obj.getString("Id_No") + "",
                                                        obj.getString("tran_id") + "",
                                                        obj.getString("mobile_operator") + "",
                                                        obj.getString("mobile_number") + "",
                                                        obj.getString("service") + "",
                                                        obj.getString("Action_on_bal_amt") + "",
                                                        obj.getString("tran_status") + "",
                                                        obj.getString("net_amout") + "",
                                                        obj.getString("DeductedAmt") + "",
                                                        obj.getString("dot") + "",
                                                        obj.getString("tot") + "",
                                                        obj.getString("Agent_balAmt_b_Ded") + "",
                                                        obj.getString("Agent_F_balAmt") + "");
                                                tranList.add(item);

                                            }
                                            adapter=new StatementAdapter(context, tranList);
                                            transactionList.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(context, "No Transaction Available! ", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(context, MainActivity.class));
                                            finish();
                                        }
                                    }
                                } else if (data.get("response-code") != null && data.get("response-code").equals("1") /*&& data.get("response-code").equals("2")*/) {
                                    Toast.makeText(context, (String) data.get("response-message"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                } else if (data.get("response-code") != null && data.get("response-code").equals("2")) {
                                    Toast.makeText(context, (String) data.get("response-message"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                } else {
                                    context = getApplicationContext();
                                    Toast.makeText(context, "This is a server error", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG);
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wallet_history_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_refresh) {
            finish();
            Intent i = new Intent(context, WalletHistoryActivity.class);  //your class
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void setDateTimeField(final TextView myview) {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(WalletHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if(i==0) {
                    fromDate=dateFormatter.format(newDate.getTime());
                    myview.setText("From \n" +fromDate);

                } else {
                    toDate=dateFormatter.format(newDate.getTime());
                    myview.setText("To \n" + toDate);
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();

    }
}
