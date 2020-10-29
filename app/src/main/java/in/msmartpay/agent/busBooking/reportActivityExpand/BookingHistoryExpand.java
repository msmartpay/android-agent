package in.msmartpay.agent.busBooking.reportActivityExpand;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

/**
 * Created by Smartkinda on 6/30/2017.
 */

public class BookingHistoryExpand extends BaseActivity {

    private ProgressDialog pd;
    private String url_booked_history = HttpURL.BOOKED_HISTORY_URL;
    private ExpandableListView expandableListView;
    private HashMap<ArrayList<GroupInfo>, ArrayList<ChildInfo>> groupParent = new HashMap<ArrayList<GroupInfo>,ArrayList<ChildInfo>>();
    private ArrayList<GroupInfo> itemGroupInfo = new ArrayList<GroupInfo>();
    private EditText editFromDate, editToDate;
    private Button btnSearchHistory;
    private DatePickerDialog toDatePickerDialog, fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String colFromDate = "", colToDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity_expand);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Booked History");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        editFromDate =  findViewById(R.id.edit_from_date);
        editToDate =  findViewById(R.id.edit_to_date);
        btnSearchHistory =  findViewById(R.id.btn_search_history);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        editFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(editFromDate);
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(BookingHistoryExpand.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        try {
                            colFromDate = dateFormatter.format(newDate.getTime());
                            if (!colFromDate.equalsIgnoreCase("")) {
                                editFromDate.setText(colFromDate);
                            } else {
                                Toast.makeText(BookingHistoryExpand.this, "Invalid From Date.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
            }
        });

        editToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(editToDate);
                Calendar newCalendar = Calendar.getInstance();
                toDatePickerDialog = new DatePickerDialog(BookingHistoryExpand.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        colToDate=dateFormatter.format(newDate.getTime());
                        Log.v(">>>>>>>>>>>>>>> ", editFromDate.getText()+" - "+colToDate);

                        if(!editFromDate.getText().equals("") && !colToDate.equals("")){

                            try {
                                colFromDate=editFromDate.getText().toString();
                                Date fdate = dateFormatter.parse(colFromDate);
                                Date todate = dateFormatter.parse(colToDate);

                                if(fdate.compareTo(todate)>0){

                                    editToDate.setText("");
                                    Toast.makeText(getApplicationContext(), "From date should be lesser than To Date.", Toast.LENGTH_SHORT).show();

                                }else{
                                    editToDate.setText(colToDate);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.v("Exception", "Execption for search");
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Something went wrong3.", Toast.LENGTH_SHORT).show();
                            }

                        }else{

                            Toast.makeText(getApplicationContext(), "Invalid Dates.", Toast.LENGTH_SHORT).show();
                        }
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                toDatePickerDialog.show();
            }
        });


        btnSearchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(TextUtils.isEmpty(editFromDate.getText().toString().trim())){
                    Toast.makeText(BookingHistoryExpand.this, "Please select from date !!!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editToDate.getText().toString().trim())){
                    Toast.makeText(BookingHistoryExpand.this, "Please select to date !!!", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        if(!itemGroupInfo.isEmpty()){
                            itemGroupInfo.clear();
                            BookedHistoryRequest();
                        }else{
                            BookedHistoryRequest();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //=================================================================
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        //===================================================================
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
              //  Toast.makeText(getApplicationContext(),groupPosition + " List Expanded.",Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
             //   Toast.makeText(getApplicationContext(),groupPosition + " List Collapsed.",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void BookedHistoryRequest() throws JSONException {
        pd = ProgressDialog.show(BookingHistoryExpand.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_booked_history,
                new JSONObject()
                        .put("FromDate", editFromDate.getText().toString().trim())
                        .put("ToDate", editToDate.getText().toString().trim())
                        .put("RecordsBy", "B"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        pd.dismiss();
                        System.out.println("Object----5>"+object.toString());
                        try {
                            if (object.getString("status").equalsIgnoreCase("0")) {
                                L.m2("BookedHistory--url: ", url_booked_history);
                                L.m2("BookedHistory--data: ", object.toString());

                                JSONObject jsonObjectCancellationPenaltyOutput = object.getJSONObject("CancellationPenaltyOutput");
                                JSONArray jsonObjectBookedTickets = jsonObjectCancellationPenaltyOutput.getJSONArray("BookedTickets");

                                JSONObject jsonObject;
                                for(int i=0; i<jsonObjectBookedTickets.length(); i++)
                                {
                                    jsonObject= jsonObjectBookedTickets.getJSONObject(i);
                                    GroupInfo groupInfo = new GroupInfo();
                                    groupInfo.setTransactionId(jsonObject.getString("TransactionId"));
                                    groupInfo.setBookedDateTime(jsonObject.getString("BookedDateTime"));
                                    groupInfo.setTicketStatus(jsonObject.getString("TicketStatus"));

                                    ChildInfo childInfo= new ChildInfo();
                                    childInfo.setHistoryAmount(jsonObject.getString("Amount"));
                                    childInfo.setOrigin(jsonObject.getString("Origin"));
                                    childInfo.setDestination(jsonObject.getString("Destination"));
                                    childInfo.setPenaltyAmount(jsonObject.getString("PenaltyAmount"));
                                    childInfo.setTotalTickets(jsonObject.getString("TotalTickets"));
                                    childInfo.setTransportName(jsonObject.getString("TransportName"));
                                    childInfo.setTravelDate(jsonObject.getString("TravelDate"));

                                    ArrayList<ChildInfo> itemChildInfo=new ArrayList<ChildInfo>();
                                    itemChildInfo.add(childInfo);

                                    groupInfo.setList(itemChildInfo);
                                    itemGroupInfo.add(groupInfo);
                                    groupParent.put(itemGroupInfo,itemChildInfo);
                                    for(int j=0; j<itemGroupInfo.size(); j++){
                                        L.m2("print--->", j+" "+itemGroupInfo.get(j).getList().size());
                                    }
                                   // agentLists.add(new ReportModel(jsonObject.getString("TransactionNo"),jsonObject.getString("DateOfTransaction")+" "+jsonObject.getString("TimeOfTransaction"),jsonObject.getString("Service"),jsonObject.getString("TransactionAmount"),jsonObject.getString("charge"),jsonObject.getString("NetTransactionAmount"),jsonObject.getString("ActionOnBalanceAmount"),jsonObject.getString("FinalBalanceAmount"),jsonObject.getString("TransactionStatus"),jsonObject.getString("Remarks")));
                                }
                                if(itemGroupInfo != null) {
                                    CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(BookingHistoryExpand.this, itemGroupInfo);
                                    expandableListView.setAdapter(adapter);
                                }else{
                                    Toast.makeText(BookingHistoryExpand.this, "Data not found !!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(BookingHistoryExpand.this, object.getString("Remarks"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BookingHistoryExpand.this, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        getSocketTimeOut(jsonrequest);
        Mysingleton.getInstance(BookingHistoryExpand.this).addToRequsetque(jsonrequest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
