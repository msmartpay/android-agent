package msmartpay.in.busBooking.reportActivityExpand;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import msmartpay.in.R;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<GroupInfo> itemGroupInfo = new ArrayList<GroupInfo>();
    private ArrayList<ChildInfo> itemChildInfo = new ArrayList<ChildInfo>();
    private HashMap<ArrayList<GroupInfo>, ArrayList<ChildInfo>> groupParent=new HashMap<ArrayList<GroupInfo>, ArrayList<ChildInfo>>();

    public CustomExpandableListAdapter(Context context, ArrayList<GroupInfo> itemGroupInfo) {
        this.context = context;
        this.itemGroupInfo = itemGroupInfo;
        this.itemChildInfo = itemChildInfo;
        this.groupParent = groupParent;
        Log.e("Expended","Constructor");
    }

    @Override
    public ArrayList<ChildInfo> getChild(int listPosition, int expandedListPosition) {
        Log.e("count--->", listPosition+"-->"+expandedListPosition);
        return this.itemGroupInfo.get(listPosition).getList();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ArrayList<ChildInfo> childInfoArrayList=(ArrayList<ChildInfo>) getChild(listPosition, expandedListPosition);
        ChildInfo childInfo = childInfoArrayList.get(expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_items, null);
        }
        Log.e("Expended","hello22222");
        TextView TransportName = (TextView) convertView.findViewById(R.id.child_transport_name);
        TextView TravellingPlace = (TextView) convertView.findViewById(R.id.child_origin_destination);
        TextView TravelDate = (TextView) convertView.findViewById(R.id.child_travel_date);
        TextView TotalTicket = (TextView) convertView.findViewById(R.id.child_total_ticket);
        TextView Amount = (TextView) convertView.findViewById(R.id.child_amount);
        TextView PenalityAmount = (TextView) convertView.findViewById(R.id.child_penality_amount);

        TransportName.setText(childInfo.getTransportName());
        TravellingPlace.setText(childInfo.getOrigin()+" - "+childInfo.getDestination());
        TravelDate.setText(childInfo.getTravelDate());
        TotalTicket.setText(childInfo.getTotalTickets());
        Amount.setText(childInfo.getHistoryAmount());
        PenalityAmount.setText(childInfo.getPenaltyAmount());

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        ArrayList<ChildInfo> itemChildInfo=this.itemGroupInfo.get(listPosition).getList();
        Log.e("count---2>", listPosition+"-->"+itemChildInfo.size()+"");
        return itemChildInfo.size();
    }

    @Override
    public GroupInfo getGroup(int listPosition) {
        return this.itemGroupInfo.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.itemGroupInfo.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupInfo groupInfo = (GroupInfo) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_items, null);
        }
        TextView txn_id = (TextView) convertView.findViewById(R.id.history_transaction_id);
        TextView transactionDate = (TextView) convertView.findViewById(R.id.history_date_time);
        TextView transactionStatus = (TextView) convertView.findViewById(R.id.history_status);
        ImageView btnOpenCloseImage = (ImageView) convertView.findViewById(R.id.open_close_image);


        txn_id.setText("Txn ID : "+groupInfo.getTransactionId());
        transactionDate.setText(groupInfo.getBookedDateTime());
        transactionStatus.setText(groupInfo.getTicketStatus());

        if (isExpanded) {
            btnOpenCloseImage.setImageResource(R.drawable.hide_icon);
        } else {
            btnOpenCloseImage.setImageResource(R.drawable.open_add);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}