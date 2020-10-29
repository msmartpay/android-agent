package agent.msmartpay.in.flight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import agent.msmartpay.in.R;

/**
 * Created by Harendra on 9/23/2017.
 */

public class FlightListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AvailSegmentsModel> availSegmentsArrayList;
    private int infant,child,adult;

    public FlightListAdapter(Context context, ArrayList<AvailSegmentsModel> availSegmentsArrayList, int infant, int child, int adult){
        this.context = context;
        this.availSegmentsArrayList=availSegmentsArrayList;
        this.infant=infant;
        this.child=child;
        this.adult=adult;
    }
    @Override
    public int getCount() {
        return availSegmentsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return availSegmentsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.flight_list_row, null);
        }
        else {
            view = convertView;
        }

        TextView tv_comp_model_code = (TextView) view.findViewById(R.id.tv_comp_model_code);
        TextView tv_flight_arefund = (TextView) view.findViewById(R.id.tv_flight_arefund);
        TextView tv_flight_amount = (TextView) view.findViewById(R.id.tv_flight_amount);
        TextView tv_depart_time = (TextView) view.findViewById(R.id.tv_depart_time);
        TextView tv_arrival_time = (TextView) view.findViewById(R.id.tv_arrival_time);
        TextView tv_stops = (TextView) view.findViewById(R.id.tv_stops);
        TextView tv_duration_time = (TextView) view.findViewById(R.id.tv_duration_time);
        ImageView iv_flight_refund = (ImageView)view.findViewById(R.id.iv_flight_refund);
        AvailSegmentsModel availSegmentsModel = new AvailSegmentsModel();
        availSegmentsModel = availSegmentsArrayList.get(position);
        ArrayList<AvailPaxFareDetailsModel> availPaxFareArrayList=availSegmentsModel.getAvailPaxFareDetailsModels();
        AvailPaxFareDetailsModel availPaxFareDetailsModel = new AvailPaxFareDetailsModel();
        availPaxFareDetailsModel  = availPaxFareArrayList.get(0);


        String departure =availSegmentsModel.getDepartureDateTime();
        if(departure!=null&&!departure.equalsIgnoreCase("")) {
            Date departureDate = null;
            try {
                departureDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(departure);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String departureDateTime = new SimpleDateFormat("H:mm").format(departureDate); // 9:00

            tv_depart_time.setText(departureDateTime);
        }
        if(departure!=null&&!departure.equalsIgnoreCase("")) {
            String arrival = availSegmentsModel.getArrivalDateTime();
            Date arrivalDate = null;
            try {
                arrivalDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(arrival);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String arrivalDateTime = new SimpleDateFormat("H:mm").format(arrivalDate); // 9:00
            tv_arrival_time.setText(arrivalDateTime);
        }
        tv_comp_model_code.setText(availSegmentsModel.getAirlineCode()+"-"+availSegmentsModel.getFlightNumber());
        if(!availSegmentsModel.getNumberofStops().equalsIgnoreCase("0")) {
            tv_stops.setText(availSegmentsModel.getNumberofStops());
        }else {
            tv_stops.setText("Non Stop");
        }
        Double totalAmount=0.0;
        if(child!=0){
            if(availPaxFareDetailsModel.getChildGrossAmount()!=null&&availPaxFareDetailsModel.getChildGrossAmount().equalsIgnoreCase("null"))
            totalAmount =totalAmount+(Double.parseDouble(availPaxFareDetailsModel.getChildGrossAmount()));
        }
        if(infant!=0){
            if(availPaxFareDetailsModel.getInfantGrossAmount()!=null&&availPaxFareDetailsModel.getInfantGrossAmount().equalsIgnoreCase("null"))
            totalAmount =totalAmount+(Double.parseDouble(availPaxFareDetailsModel.getInfantGrossAmount()));
        }
       // if(availPaxFareDetailsModel.getAdultGrossAmount()!=null&&availPaxFareDetailsModel.getAdultGrossAmount().equalsIgnoreCase("null"))
        totalAmount =totalAmount+(Double.parseDouble(availPaxFareDetailsModel.getAdultGrossAmount()));
        tv_flight_amount.setText(totalAmount+"");
        tv_duration_time.setText(availSegmentsModel.getDuration().replace(" ",""));
        String fareType = availPaxFareDetailsModel.getAdultFareType().trim();
        if((!fareType.trim().equalsIgnoreCase("Non Refundable"))){
            iv_flight_refund.setVisibility(View.VISIBLE);
            tv_flight_arefund.setText(fareType);
        }else{
            iv_flight_refund.setVisibility(View.GONE);
            tv_flight_arefund.setText(fareType);
        }

        return view;
    }
}
