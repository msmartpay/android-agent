package in.msmartpay.agent.flight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.L;

/**
 * Created by Harendra on 10/10/2017.
 */

public class FlightBookingSucces extends BaseActivity {
    private String BookedJsonDetails;
    private String TotalTickets;
    private ArrayList<GroupExpandFlight> groupExpandsList = new ArrayList<>();
    private TextView tv_HeaderPNR, tv_HeaderOrginToDestination, tv_TransactionId, tv_BusName, tv_DepartureTime, tv_TransportPNR, tv_TotalAmount, tv_Origin,
            tv_TotalTickets, tv_TravelDate, tv_Destination, tv_ServiceTax, tv_Commission, tv_TransporterName;
    private LinearLayout linearLayoutDone;
    private ExpandableListView expandableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_booking_success);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Booked Ticket Details");

        BookedJsonDetails = getIntent().getStringExtra("json");
        L.m2("BookedJsonDetails--->", BookedJsonDetails);
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        linearLayoutDone = (LinearLayout) findViewById(R.id.tview_done);
        tv_HeaderPNR = (TextView) findViewById(R.id.tview_pnr);
        tv_HeaderOrginToDestination = (TextView) findViewById(R.id.tview_orgin_to_desti);
        tv_TransactionId = (TextView) findViewById(R.id.tview_transaction_id);
        tv_BusName = (TextView) findViewById(R.id.tview_bus_name);
        tv_DepartureTime = (TextView) findViewById(R.id.tview_departure_time);
        tv_TransportPNR = (TextView) findViewById(R.id.tview_transport_pnr);
        tv_TotalAmount = (TextView) findViewById(R.id.tview_total_amount);
        tv_Origin = (TextView) findViewById(R.id.tview_origin);
        tv_TotalTickets = (TextView) findViewById(R.id.tview_total_tickets);
        tv_TravelDate = (TextView) findViewById(R.id.tview_travel_date);
        tv_Destination = (TextView) findViewById(R.id.tview_destination);
        tv_ServiceTax = (TextView) findViewById(R.id.tview_service_tax);
        tv_Commission = (TextView) findViewById(R.id.tview_commission);
        tv_TransporterName = (TextView) findViewById(R.id.tview_transporter_name);

        try {
            JSONObject jsonObject = new JSONObject(BookedJsonDetails);
            JSONObject bookingDetails = jsonObject.getJSONObject("bookingDetails");
            JSONArray FlightTicketDetailsArray = bookingDetails.getJSONArray("FlightTicketDetails");
            JSONObject FlightTicketDetailsObject = FlightTicketDetailsArray.getJSONObject(0);

            // tv_ServiceTax.setText(ServiceTax);
            // tv_Commission.setText(Commission);

            tv_HeaderPNR.setText("PNR - "+ FlightTicketDetailsObject.getString("HermesPNR"));
            tv_TransactionId.setText( FlightTicketDetailsObject.getString("TransactionId"));
            tv_TotalAmount.setText(FlightTicketDetailsObject.getString("TotalAmount"));

            tv_Origin.setText(FlightTicketDetailsObject.getString("BaseOrigin"));
            tv_TravelDate.setText(FlightTicketDetailsObject.getString("IssueDateTime"));
            tv_Destination.setText(FlightTicketDetailsObject.getString("BaseDestination"));

            tv_HeaderOrginToDestination.setText(FlightTicketDetailsObject.getString("BaseOrigin") +" - "+ FlightTicketDetailsObject.getString("BaseDestination"));
           // TravelDate = FlightTicketDetailsObject.getString("IssueDateTime");
            tv_DepartureTime.setText(FlightTicketDetailsObject.getString("IssueDateTime"));

            JSONArray AirlineDetailsArray = FlightTicketDetailsObject.getJSONArray("AirlineDetails");
            JSONObject AirlineDetailsObject = AirlineDetailsArray.getJSONObject(0);

           /* ContactNumber = AirlineDetailsObject.getString("ContactNumber");
            Address1 = AirlineDetailsObject.getString("Address1");
            Address2 = AirlineDetailsObject.getString("Address2");
            Address3 = "";
            Website = AirlineDetailsObject.getString("EMailId");
            Fax = AirlineDetailsObject.getString("FaxNumber");
            EmailId = AirlineDetailsObject.getString("EMailId");
            TransportPNR = AirlineDetailsObject.getString("AirlinePNR");
            CityNamePinCode = AirlineDetailsObject.getString("City");*/

            tv_BusName.setText(AirlineDetailsObject.getString("AirlineName"));
            tv_TransportPNR.setText(AirlineDetailsObject.getString("AirlinePNR"));
            tv_TransporterName.setText(AirlineDetailsObject.getString("AirlineName"));

            JSONArray jsonArrayPaxList = FlightTicketDetailsObject.getJSONArray("PassengerDetails");
            TotalTickets = jsonArrayPaxList.length()+"";
            tv_TotalTickets.setText(TotalTickets);
            for(int i=0; i< jsonArrayPaxList.length(); i++){
                ArrayList<ConfirmFlightModel> confirmArrayList = new ArrayList<>();
                JSONObject jsonObjectLoop = jsonArrayPaxList.getJSONObject(i);
                ConfirmFlightModel ticketModel = new ConfirmFlightModel();
                ticketModel.setPassengerType(jsonObjectLoop.getString("PassengerType"));
               // ticketModel.setFare(jsonObjectLoop.getString("Fare"));
                 ticketModel.setAge(jsonObjectLoop.getString("Age"));
                //ticketModel.setGender(jsonObjectLoop.getString("Gender"));
                confirmArrayList.add(ticketModel);
                GroupExpandFlight groupExpand = new GroupExpandFlight(jsonObjectLoop.getString("FirstName")+" "+jsonObjectLoop.getString("LastName"),jsonObjectLoop.getString("TicketNumber"),confirmArrayList);
                groupExpandsList.add(groupExpand);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        FlightExpandableAdaptor expandableAdaptor = new FlightExpandableAdaptor(this, groupExpandsList);
        expandableListView.setAdapter(expandableAdaptor);
        setListViewHeight(expandableListView, 0);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                setListChildViewHeight(parent, groupPosition);

                return false;
            }
        });

        //==============Done Button========================

        linearLayoutDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlightBookingSucces.this, FlightActivity.class);
                SharedPreferences myPrefs = getSharedPreferences("Details", MODE_PRIVATE);
                SharedPreferences.Editor emyPrefs = myPrefs.edit();
                emyPrefs.clear();
                emyPrefs.commit();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setListChildViewHeight(ExpandableListView listView,
                                        int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight()+listView.getDividerHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight/6;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
