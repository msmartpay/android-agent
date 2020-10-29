package agent.msmartpay.in.busBooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import agent.msmartpay.in.R;

/**
 * Created by Smartkinda on 8/18/2017.
 */

public class ExpandableAdaptor extends BaseExpandableListAdapter {

    private ArrayList<GroupExpand> groupExpandList;
    private Context context;

    public ExpandableAdaptor(Context context, ArrayList<GroupExpand> groupExpandList) {

        this.context=context;
        this.groupExpandList= groupExpandList;
    }

    @Override
    public int getGroupCount() {
        return groupExpandList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupExpandList.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupExpandList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupExpandList.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate (R.layout.bus_expand_group_list, parent, false);

        TextView PassengerName = (TextView) convertView.findViewById(R.id.tv_passenger_name);
        TextView SeatNumber = (TextView) convertView.findViewById(R.id.tv_seat_no);
        TextView BoardDropPlace = (TextView) convertView.findViewById(R.id.tv_board_drop_place);
        ImageView btnOpenCloseImage = (ImageView) convertView.findViewById(R.id.open_close_image);

        GroupExpand groupExpand = (GroupExpand) getGroup(groupPosition);
        PassengerName.setText("Name - "+groupExpand.passengerName);
        SeatNumber.setText("Seat No - "+groupExpand.seatNo);
        BoardDropPlace.setText(groupExpand.boardDropPlace);

        if (isExpanded) {
            btnOpenCloseImage.setImageResource(R.drawable.hide_icon);
        } else {
            btnOpenCloseImage.setImageResource(R.drawable.open_add);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate (R.layout.bus_confirm_booked_passenger_list, parent, false);

        TextView Fare = (TextView) view.findViewById(R.id.tview_fare_list);
        TextView PassengerName = (TextView) view.findViewById(R.id.tview_passenger_name_list);
        TextView Age = (TextView) view.findViewById(R.id.tview_age_list);
        TextView SeatNumber = (TextView) view.findViewById(R.id.tview_seat_no_list);
        TextView PNR = (TextView) view.findViewById(R.id.tview_pnr_list);
        TextView BoardingPlace = (TextView) view.findViewById(R.id.tview_boarding_place);
        TextView BoardingContactNumber = (TextView) view.findViewById(R.id.tview_boarding_contact_number);
        TextView BoardingTime = (TextView) view.findViewById(R.id.tview_boarding_time);
        TextView BoardingAddress = (TextView) view.findViewById(R.id.tview_boarding_address);
        TextView BoardingLandMark = (TextView) view.findViewById(R.id.tview_boarding_landmark);
        TextView BoardingReportingTime = (TextView) view.findViewById(R.id.tview_reporting_time);
        TextView BoardingTicketNumber = (TextView) view.findViewById(R.id.tview_ticket_number);
        TextView DroppingPlace = (TextView) view.findViewById(R.id.tview_dropping_place);
        TextView DroppingContactNumber = (TextView) view.findViewById(R.id.tview_dropping_contact_number);
        TextView DroppingTime = (TextView) view.findViewById(R.id.tview_dropping_time);
        TextView DroppingAddress = (TextView) view.findViewById(R.id.tview_dropping_address);
        TextView DroppingLandMark = (TextView) view.findViewById(R.id.tview_dropping_landmark);

        ConfirmTicketModel childExpandList = (ConfirmTicketModel)getChild(groupPosition, childPosition);
        Fare.setText(childExpandList.getConfirmFare());
        PassengerName.setText(childExpandList.getConfirmPassengerName());

        Age.setText(childExpandList.getConfirmAge());
        SeatNumber.setText(childExpandList.getConfirmSeatNo());
        PNR.setText(childExpandList.getConfirmPNR());
        BoardingPlace.setText(childExpandList.getBoardingPlace());
        BoardingContactNumber.setText(childExpandList.getBoardingContactNumber());
        BoardingTime.setText(childExpandList.getBoardingTime());
        BoardingAddress.setText(childExpandList.getBoardingAddress());
        BoardingLandMark.setText(childExpandList.getBoardingLandMark());
        BoardingReportingTime.setText(childExpandList.getConfirmReportingTime());
        BoardingTicketNumber.setText(childExpandList.getConfirmTicketNumber());
        DroppingPlace.setText(childExpandList.getDroppingPlace());
        DroppingContactNumber.setText(childExpandList.getDroppingContactNumber());
        DroppingTime.setText(childExpandList.getDroppingTime());
        DroppingAddress.setText(childExpandList.getDroppingAddress());
        DroppingLandMark.setText(childExpandList.getDroppingLandMark());

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
