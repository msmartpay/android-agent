package agent.msmartpay.in.busBooking;

import android.content.Context;
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

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.L;

/**
 * Created by Smartkinda on 8/17/2017.
 */

public class ConfirmTicketDetailsExpandable extends BaseActivity {

    private String BookedJsonDetails;
    private String Commission, CancellationPolicy, ServiceTax, ConvenienceFee, TransactionId, BusName, DepartureTime,
            TransportPNR, TotalAmount, Origin, Destination, TotalTickets, TravelDate, ContactNumber, Address1, Address2,
            Address3, Website, Fax, EmailId, CityNamePinCode, TransportName;
    private ArrayList<GroupExpand> groupExpandsList = new ArrayList<>();
    private TextView tv_HeaderPNR, tv_HeaderOrginToDestination, tv_TransactionId, tv_BusName, tv_DepartureTime, tv_TransportPNR, tv_TotalAmount, tv_Origin,
            tv_TotalTickets, tv_TravelDate, tv_Destination, tv_ServiceTax, tv_Commission, tv_TransporterName;
    private LinearLayout linearLayoutDone;
    private Context context;
    private ExpandableListView expandableListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_confirm_booked_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Booked Ticket Details");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        BookedJsonDetails = getIntent().getStringExtra("ConfirmTicketDetails");
        L.m2("BookedJsonDetails--->", BookedJsonDetails.toString());

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
            Commission = jsonObject.getString("Commission");
            CancellationPolicy = jsonObject.getString("CancellationPolicy");
            ServiceTax = jsonObject.getString("ServiceTax");
            JSONObject jsonObjectPNRDetails = jsonObject.getJSONObject("PNRDetails");
            TransactionId = jsonObjectPNRDetails.getString("TransactionId");
            BusName = jsonObjectPNRDetails.getString("BusName");
            DepartureTime = jsonObjectPNRDetails.getString("DepartureTime");
            TransportPNR = jsonObjectPNRDetails.getString("TransportPNR");
            TotalAmount = jsonObjectPNRDetails.getString("TotalAmount");
            Origin = jsonObjectPNRDetails.getString("Origin");
            Destination = jsonObjectPNRDetails.getString("Destination");
            TotalTickets = jsonObjectPNRDetails.getString("TotalTickets");
            TravelDate = jsonObjectPNRDetails.getString("TravelDate");
            JSONObject jsonObjectTransportDetails = jsonObject.getJSONObject("TransportDetails");
            ContactNumber = jsonObjectTransportDetails.getString("ContactNumber");
            Address1 = jsonObjectTransportDetails.getString("Address1");
            Address2 = jsonObjectTransportDetails.getString("Address2");
            Address3 = jsonObjectTransportDetails.getString("Address3");
            Website = jsonObjectTransportDetails.getString("Website");
            Fax = jsonObjectTransportDetails.getString("Fax");
            EmailId = jsonObjectTransportDetails.getString("EmailId");
            CityNamePinCode = jsonObjectTransportDetails.getString("CityNamePinCode");
            TransportName = jsonObjectTransportDetails.getString("TransportName");

            tv_TransactionId.setText(TransactionId);
            tv_BusName.setText(BusName);
            tv_DepartureTime.setText(DepartureTime);
            tv_TransportPNR.setText(TransportPNR);
            tv_TotalAmount.setText(TotalAmount);
            tv_Origin.setText(Origin);
            tv_TotalTickets.setText(TotalTickets);
            tv_TravelDate.setText(TravelDate);
            tv_Destination.setText(Destination);
            tv_ServiceTax.setText(ServiceTax);
            tv_Commission.setText(Commission);
            tv_HeaderPNR.setText("PNR - "+ TransportPNR);
            tv_HeaderOrginToDestination.setText(Origin +" - "+ Destination);
            tv_TransporterName.setText(TransportName);

            JSONArray jsonArrayPaxList = jsonObject.getJSONArray("PaxList");
            for(int i=0; i< jsonArrayPaxList.length(); i++){
                ArrayList<ConfirmTicketModel> confirmArrayList = new ArrayList<>();
                JSONObject jsonObjectLoop = jsonArrayPaxList.getJSONObject(i);
                ConfirmTicketModel ticketModel = new ConfirmTicketModel();
                ticketModel.setConfirmFare(jsonObjectLoop.getString("Fare"));
                ticketModel.setConfirmPassengerName(jsonObjectLoop.getString("PassengerName"));
                ticketModel.setConfirmServiceCharges(jsonObjectLoop.getString("ServiceCharges"));
                ticketModel.setConfirmAge(jsonObjectLoop.getString("Age"));
                ticketModel.setConfirmSeatNo(jsonObjectLoop.getString("SeatNo"));
                ticketModel.setConfirmSeatType(jsonObjectLoop.getString("SeatType"));
                ticketModel.setConfirmGender(jsonObjectLoop.getString("Gender"));
                ticketModel.setConfirmReportingTime(jsonObjectLoop.getString("ReportingTime"));
                ticketModel.setConfirmTicketNumber(jsonObjectLoop.getString("TicketNumber"));
                ticketModel.setConfirmPNR(jsonObjectLoop.getString("PNR"));

               // JSONObject jsonObjectBoardingPoints = jsonObjectLoop.getJSONObject("BoardingPoints");
                ticketModel.setBoardingAddress(jsonObjectLoop.getString("BoardingAddress"));
                ticketModel.setBoardingPlace(jsonObjectLoop.getString("BoardingPlace"));
                ticketModel.setBoardingTime(jsonObjectLoop.getString("BoardingTime"));
                ticketModel.setBoardingLandMark(jsonObjectLoop.getString("BoardingLandMark"));
                ticketModel.setBoardingContactNumber(jsonObjectLoop.getString("BoardingContactNumber"));
                ticketModel.setBoardingBoardingId(jsonObjectLoop.getString("BoardingId"));

               // JSONObject jsonObjectDroppingPoints = jsonObjectLoop.getJSONObject("DroppingPoints");
                ticketModel.setDroppingAddress(jsonObjectLoop.getString("DroppingAddress"));
                ticketModel.setDroppingContactNumber(jsonObjectLoop.getString("DroppingContactNumber"));
                ticketModel.setDroppingId(jsonObjectLoop.getString("DroppingId"));
                ticketModel.setDroppingLandMark(jsonObjectLoop.getString("DroppingLandMark"));
                ticketModel.setDroppingPlace(jsonObjectLoop.getString("DroppingPlace"));
                ticketModel.setDroppingTime(jsonObjectLoop.getString("DroppingTime"));
                confirmArrayList.add(ticketModel);
                GroupExpand groupExpand = new GroupExpand(jsonObjectLoop.getString("PassengerName"),jsonObjectLoop.getString("SeatNo"),jsonObjectLoop.getString("BoardingPlace")+" - "+jsonObjectLoop.getString("DroppingPlace"),confirmArrayList);
                groupExpandsList.add(groupExpand);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ExpandableAdaptor expandableAdaptor = new ExpandableAdaptor(this, groupExpandsList);
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
                Intent intent = new Intent(ConfirmTicketDetailsExpandable.this, BusBookingSearchActivity_H.class);
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}