package in.msmartpay.agent.busBooking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

/**
 * Created by Smartkinda on 7/14/2017.
 */

public class PnrDetailsActivity extends BaseActivity {

    private ListView listView;
    private Context context;
    private String PNRjsonData, PNRnumber;
    private String DepatureTime, BookedDate, PNRStatus, Origin, Destination, TravelDate, TransportName;
    private ArrayList<ConfirmTicketModel> pnrArrayList;
    private TextView tv_PNR, tv_OrginDestination, tv_TransportName, tv_DateTime;
    private Button btnProceedCancel;
    private ArrayList<Integer> checkBoxValue;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_cancellation_request = HttpURL.CANCELLATION_TICKET_URL;
    private HashMap<Integer,String> cancelTickets = new HashMap<>();
    private String ticketString = "";
    private HashMap<Integer,String> totalPenalityAmount = new HashMap<>();
    private int totalPenalityAmountData = 0, totalCancelTicket = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_pnr_details_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("PNR Details"); //change like app
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context  = PnrDetailsActivity.this;
        tv_PNR = (TextView) findViewById(R.id.tv_pnr_number);
        tv_OrginDestination = (TextView) findViewById(R.id.tv_orgin_to_destination);
        tv_TransportName = (TextView) findViewById(R.id.tv_transport_name);
        tv_DateTime = (TextView) findViewById(R.id.tv_date_time);
        btnProceedCancel =  findViewById(R.id.id_proceed);

        PNRjsonData = getIntent().getStringExtra("PnrStatusPenality");
        PNRnumber = getIntent().getStringExtra("PnrNumber");
        L.m2("PNRjsonData--->", PNRjsonData.toString() +"-->"+PNRnumber);

        try {
            jsonObject = new JSONObject(PNRjsonData.toString());
            JSONObject jsonObjectCancellationPenaltyOutput = jsonObject.getJSONObject("CancellationPenaltyOutput");
            JSONObject jsonObjectToCancelPNRDetails = jsonObjectCancellationPenaltyOutput.getJSONObject("ToCancelPNRDetails");
            DepatureTime = jsonObjectToCancelPNRDetails.getString("DepatureTime");
            BookedDate = jsonObjectToCancelPNRDetails.getString("BookedDate");
            PNRStatus = jsonObjectToCancelPNRDetails.getString("PNRStatus");
            Origin = jsonObjectToCancelPNRDetails.getString("Origin");
            Destination = jsonObjectToCancelPNRDetails.getString("Destination");
            TravelDate = jsonObjectToCancelPNRDetails.getString("TravelDate");
            TransportName = jsonObjectToCancelPNRDetails.getString("TransportName");

            tv_PNR.setText("PNR : "+PNRnumber+", "+PNRStatus);
            tv_OrginDestination.setText(Origin +" - "+Destination);
            tv_TransportName.setText(TransportName);
            tv_DateTime.setText(TravelDate+", "+DepatureTime);

            JSONArray jsonArrayToCancelPaxList = jsonObjectToCancelPNRDetails.getJSONArray("ToCancelPaxList");
            pnrArrayList = new ArrayList<>();
            for(int i=0; i<jsonArrayToCancelPaxList.length(); i++) {
                JSONObject jsonObject = jsonArrayToCancelPaxList.getJSONObject(i);
                ConfirmTicketModel confirmTicketModel = new ConfirmTicketModel();
                confirmTicketModel.setConfirmPassengerName(jsonObject.getString("PassengerName"));
                confirmTicketModel.setConfirmFare(jsonObject.getString("Fare"));
                confirmTicketModel.setConfirmAge(jsonObject.getString("Age"));
                confirmTicketModel.setConfirmSeatNo(jsonObject.getString("SeatNo"));
                confirmTicketModel.setPenatlyAmount(jsonObject.getString("PenatlyAmount"));
                confirmTicketModel.setConfirmGender(jsonObject.getString("Gender"));
                confirmTicketModel.setConfirmTicketNumber(jsonObject.getString("TicketNumber"));

                pnrArrayList.add(confirmTicketModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //=================================================

        if(pnrArrayList != null) {
            L.m2("pnrArrayList-->", pnrArrayList.toString());
            context = this;
            listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(new PNRDetailsAdapterClass(context, pnrArrayList));
        }
        if (pnrArrayList != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                    if(!checkBox.isChecked()){
                        checkBox.setChecked(true);
                    }else {
                        checkBox.setChecked(false);
                    }
                    if (checkBox.isChecked()) {
                        ConfirmTicketModel ticketModel = pnrArrayList.get(position);
                        cancelTickets.put(position,ticketModel.getConfirmTicketNumber());
                        totalPenalityAmount.put(position, ticketModel.getPenatlyAmount());
                        totalCancelTicket++;
                      //  Toast.makeText(context, "Added !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        if(cancelTickets.size() > 0) {
                            cancelTickets.remove(position);
                            totalPenalityAmount.remove(position);
                      //      Toast.makeText(context, "Removed !!!", Toast.LENGTH_SHORT).show();
                            totalCancelTicket--;
                        }
                    }

                }
            });
        }else {
            Toast.makeText(PnrDetailsActivity.this, "No Passenger Details available !!!", Toast.LENGTH_SHORT).show();
            PnrDetailsActivity.this.finish();
        }

        //Proceed Button
        btnProceedCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancelTickets.size() > 0){
                    ticketString = "";
                    Set entrySet = cancelTickets.entrySet();
                    Iterator it = entrySet.iterator();
                    while(it.hasNext()) {
                        Map.Entry ticket = (Map.Entry) it.next();
                        ticketString = ticketString+ticket.getValue()+",";
                    }

                    Set entrySet1 = totalPenalityAmount.entrySet();
                    Iterator it1 = entrySet1.iterator();
                    while(it1.hasNext()) {
                        Map.Entry ticketPenality = (Map.Entry) it1.next();
                        totalPenalityAmountData = totalPenalityAmountData+ Integer.parseInt(ticketPenality.getValue().toString());
                    }
                    confirmationDialog();
                }else{
                    Toast.makeText(context, "Please checked for cancel ticket !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Adapter class
    public class PNRDetailsAdapterClass extends BaseAdapter {

        private Context contextData;
        private ArrayList<ConfirmTicketModel> pnrDetailsList;
        private TextView PassengerName, PassSeatNumber, PassAge, PassFare, PassTicketNumber, PassPenalityAmount;
        private CheckBox checkBox;

        PNRDetailsAdapterClass(Context context, ArrayList<ConfirmTicketModel> pnrDetails)
        {

            contextData = context;
            pnrDetailsList = pnrDetails;
        }

        @Override
        public int getCount() {
            return pnrDetailsList.size();
        }

        @Override
        public Object getItem(int position) {
            return pnrDetailsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) contextData.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate (R.layout.bus_pnr_details_list, parent, false);

            PassengerName = (TextView) view.findViewById(R.id.tv_passenger_name);
            PassSeatNumber = (TextView) view.findViewById(R.id.tv_seat_number);
            PassAge = (TextView) view.findViewById(R.id.tv_age);
            PassFare = (TextView) view.findViewById(R.id.tv_fare);
            PassTicketNumber = (TextView) view.findViewById(R.id.tv_ticket_number);
            PassPenalityAmount = (TextView) view.findViewById(R.id.tv_penality);

            PassengerName.setText("Name : "+pnrDetailsList.get(position).getConfirmPassengerName());
            PassSeatNumber.setText("Seat No : "+pnrDetailsList.get(position).getConfirmSeatNo());
            PassAge.setText("Age : "+pnrDetailsList.get(position).getConfirmAge());
            PassFare.setText("Fare : "+pnrDetailsList.get(position).getConfirmFare());
            PassTicketNumber.setText("Ticket No : "+pnrDetailsList.get(position).getConfirmTicketNumber());
            PassPenalityAmount.setText("Penality Amt : "+pnrDetailsList.get(position).getPenatlyAmount());

            return view;
        }
    }

    //===========Get Seat Details ==============

    //Json request for cancel Ticket
    private void CancelTicketRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("TransactionId", PNRnumber.toString())
                    .put("TransportId", "24")
                    .put("TicketNumbers", ticketString)
                    .put("PenaltyAmount", totalPenalityAmountData)
                    .put("TotalTicketsToCancel", totalCancelTicket+"");

            L.m2("CancelTicket--url", url_cancellation_request);
            Log.e("CancelTicket--Request", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_cancellation_request, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            try {
                                if (object.getString("status").equalsIgnoreCase("0")) {
                                    L.m2("CancelTicket--data", object.toString());
                                    intentDialog();
                                } else {
                                    Toast.makeText(context, object.getString("Remarks").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
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
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(PnrDetailsActivity.this).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }

    //================For Confirm Dialog===============
    public void confirmationDialog() {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.bus_cancel_ticket_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK =  d.findViewById(R.id.btn_ok);
        final Button btnNO =  d.findViewById(R.id.btn_no);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelTicketRequest();
                d.dismiss();
            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
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
    //=======================================

    //================For only Dialog===============
    public void intentDialog() {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.bus_cancel_ticket_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK =  d.findViewById(R.id.btn_ok);
        final Button btnNO =  d.findViewById(R.id.btn_no);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final TextView tvTitle = (TextView) d.findViewById(R.id.title);
        tvTitle.setText("Information");
        tvMessage.setText("You have successfully cancelled desire seat number of ticket !!!");
        btnOK.setText("Done");
        btnClosed.setVisibility(View.GONE);
        btnNO.setVisibility(View.GONE);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PnrDetailsActivity.this.finish();
                d.dismiss();
            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
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
    //=======================================

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
