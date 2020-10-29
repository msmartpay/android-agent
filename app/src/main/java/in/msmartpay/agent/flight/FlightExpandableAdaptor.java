package in.msmartpay.agent.flight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.msmartpay.agent.R;

/**
 * Created by Smartkinda on 8/18/2017.
 */

public class FlightExpandableAdaptor extends BaseExpandableListAdapter {

    private ArrayList<GroupExpandFlight> groupExpandList;
    private Context context;

    public FlightExpandableAdaptor(Context context, ArrayList<GroupExpandFlight> groupExpandList) {

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
        convertView = layoutInflater.inflate (R.layout.flight_expend_group_list, parent, false);

        TextView PassengerName = (TextView) convertView.findViewById(R.id.tv_passenger_name);
        TextView ticketNumber = (TextView) convertView.findViewById(R.id.tv_ticket_no);
        ImageView btnOpenCloseImage = (ImageView) convertView.findViewById(R.id.open_close_image);

        GroupExpandFlight groupExpand = (GroupExpandFlight) getGroup(groupPosition);
        PassengerName.setText("Name - "+groupExpand.passengerName);
        ticketNumber.setText("Ticket No. - "+groupExpand.tiketNo);
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
        view = layoutInflater.inflate (R.layout.flight_expend_child_passenger, parent, false);

        TextView typePassenger = (TextView) view.findViewById(R.id.tv_type);
        TextView tv_fare = (TextView) view.findViewById(R.id.tv_fare);
        TextView Age = (TextView) view.findViewById(R.id.tview_age_list);
        TextView tv_gender = (TextView) view.findViewById(R.id.tv_gender);

        ConfirmFlightModel childExpandList = (ConfirmFlightModel)getChild(groupPosition, childPosition);
        typePassenger.setText(childExpandList.getPassengerType());
        tv_fare.setText(childExpandList.getFare()==null?"":childExpandList.getFare());
        Age.setText(childExpandList.getAge());
        tv_gender.setText(childExpandList.getGender()==null?"":childExpandList.getGender());

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
