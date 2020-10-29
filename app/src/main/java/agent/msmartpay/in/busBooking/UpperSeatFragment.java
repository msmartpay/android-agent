package agent.msmartpay.in.busBooking;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.L;

import static android.content.Context.MODE_PRIVATE;
import static agent.msmartpay.in.busBooking.BusBookingSearchActivity_H.passenser;
import static agent.msmartpay.in.busBooking.SeatSelectionActivity.seatList;


/**
 * Created by Smartkinda on 7/8/2017.
 */

public class UpperSeatFragment extends Fragment {

    private GridView gridSeat;
    private LinearLayout layoutBookNow;
    private ArrayList<Item> gridArray = new ArrayList<Item>();
    public Bitmap seatIcon, seatSelect, bookedIcon, transparentIcon,ladiesIcon;
    public Bitmap seatIconBerth, seatSelectBerth, bookedIconBerth,ladiesIconBerth;
    private String ScheduleId, StationId, TransportId, DateOfJourney, UserTrackId;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private SeatAllocationAdapterClass allocationAdapterClass ;
    private static final String BOARDING_ARRAY_LIST  = "boarding";
    private static final String DROPPING_ARRAY_LIST  = "dropping";
    private static final String UPPPER_ARRAY_LIST = "upper";
    private static final String FARE_ARRAY_LIST = "fare";
    private ArrayList<BusDetailsModel> BoardingPointArrayList;
    private ArrayList<BusDetailsModel> FaresArrayList;
    private ArrayList<SeatModel> UpperSeats;
    private int countColumnUpper=0;
    private Communicator comm;


    public interface Communicator {
        public void respondSelectedUpper(String respond, int pos);
        public void respondDeSelectedUpper(String respond, int pos);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.comm = (UpperSeatFragment.Communicator) activity;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstanceState = getArguments();
        if (null != savedInstanceState) {
            UpperSeats = savedInstanceState.getParcelableArrayList(UPPPER_ARRAY_LIST);
            countColumnUpper = savedInstanceState.getInt("countColumnUpper");
        }

        //============================================

        sharedPreferences = getActivity().getApplication().getSharedPreferences("Details", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        DateOfJourney = sharedPreferences.getString("DateOfJourney", null);
        ScheduleId = sharedPreferences.getString("ScheduleId", null);
        StationId = sharedPreferences.getString("StationId", null);
        TransportId = sharedPreferences.getString("TransportId", null);
        UserTrackId = sharedPreferences.getString("UserTrackId", null);

        seatIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.available_icon);
        seatSelect = BitmapFactory.decodeResource(this.getResources(), R.drawable.selected_icon);
        bookedIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.booked_icon);
        ladiesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ladies_booked);

        transparentIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.transparent_chair);

        seatIconBerth= BitmapFactory.decodeResource(this.getResources(), R.drawable.available_berth);
        seatSelectBerth = BitmapFactory.decodeResource(this.getResources(), R.drawable.selected_berth);
        bookedIconBerth= BitmapFactory.decodeResource(this.getResources(), R.drawable.booked_berth);
        ladiesIconBerth= BitmapFactory.decodeResource(this.getResources(), R.drawable.ladies_booked_berth);

        if(UpperSeats!=null) {
            for (int i = 0; i < UpperSeats.size(); i++) {
                SeatModel seatModel = new SeatModel();
                seatModel =UpperSeats.get(i);
                if (seatModel.getSeatType()!=null&&seatModel.getSeatType().equalsIgnoreCase("Sleeper")) {
                    if (seatModel.getSeatStatus()!=null&&seatModel.getSeatStatus().equalsIgnoreCase("Available")) {
                        if(seatModel.getGender().equalsIgnoreCase("M")){
                            gridArray.add(new Item(seatIconBerth, "seat " + UpperSeats.get(i)));
                        }else {
                            gridArray.add(new Item(ladiesIconBerth, "seat " + UpperSeats.get(i)));
                        }
                    } else  if (seatModel.getSeatStatus()!=null&&seatModel.getSeatStatus().equalsIgnoreCase("Booked")) {
                        gridArray.add(new Item(bookedIconBerth, "seat " + UpperSeats.get(i)));
                    }else{}
                } else if(seatModel.getSeatType()!=null&&seatModel.getSeatType().equalsIgnoreCase("Seater")) {
                    if (seatModel.getSeatStatus().equalsIgnoreCase("Available")) {
                        if(seatModel.getGender().equalsIgnoreCase("M")) {
                            gridArray.add(new Item(seatIcon, "seat " + UpperSeats.get(i)));
                        }else {
                            gridArray.add(new Item(ladiesIcon, "seat " + UpperSeats.get(i)));
                        }
                    } else {
                        gridArray.add(new Item(bookedIcon, "seat " + UpperSeats.get(i)));
                    }
                } else if(seatModel.getSeatNo()!=null&&seatModel.getSeatNo().equalsIgnoreCase("NA")) {
                    gridArray.add(new Item(transparentIcon, "seat " + UpperSeats.get(i)));
                }
            }

            context = getActivity();
            allocationAdapterClass = new SeatAllocationAdapterClass(context, gridArray);
        }else{
            L.s(getActivity(),"No Response lowerlist = ");
        }

        //===============================================

    }
    //===== getArray List
    public static UpperSeatFragment newInstance(ArrayList<SeatModel> arrayListFrag, int ColumnUpper) {
        UpperSeatFragment listFragment= new UpperSeatFragment();
        Bundle audioArgs = new Bundle();
        audioArgs.putParcelableArrayList(UPPPER_ARRAY_LIST, arrayListFrag);
        audioArgs.putInt("countColumnUpper",ColumnUpper);
        listFragment.setArguments(audioArgs);
        return listFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_upper_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        L.m2("Check_UserTrackId", UserTrackId.toString());
        layoutBookNow = (LinearLayout) view.findViewById(R.id.linear_book_now);
        gridSeat = (GridView) view.findViewById(R.id.gridView_list);

        if(UpperSeats!=null&&!UpperSeats.isEmpty()) {
            gridSeat.setNumColumns(countColumnUpper);
            gridSeat.setAdapter(allocationAdapterClass);
        }

        //onClick=====
        gridSeat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = gridArray.get(position);
                Bitmap seatcompare = item.getImage();
                if (seatcompare == seatIcon||seatcompare==seatIconBerth||seatcompare==ladiesIcon||seatcompare==ladiesIconBerth) {
                    if(seatList.size() < passenser) {
                        seatSelected(position,seatcompare);
                    } else{
                        Toast.makeText(getActivity(), "Already selected desired number of seats !!!", Toast.LENGTH_SHORT).show();
                    }
                } else if(seatcompare == bookedIcon||seatcompare==bookedIconBerth){
                    Toast.makeText(getActivity(), "This seat already booked !!!", Toast.LENGTH_SHORT).show();
                } else if(seatcompare == transparentIcon) {
                }else {
                    seatDeselcted(position,seatcompare);
                }
            }
        });

        return view;
    }

    public void seatSelected(int pos,Bitmap seatcompare)
    {
        gridArray.remove(pos);
        if(seatcompare==seatIcon||seatcompare==ladiesIcon){
            gridArray.add(pos, new Item(seatSelect, "select"));
        }else if(seatcompare==seatIconBerth||seatcompare==ladiesIconBerth){
            gridArray.add(pos, new Item(seatSelectBerth, "select"));
        }
        allocationAdapterClass.notifyDataSetChanged();
        comm.respondSelectedUpper("Upper",pos);
    }

    public void seatDeselcted(int pos,Bitmap seatcompare)
    {
        gridArray.remove(pos);
        int i = pos + 1;
        SeatModel seatModel = new SeatModel();
        seatModel= UpperSeats.get(pos);
        if(seatcompare==seatSelect){
            if(seatModel.getGender().equalsIgnoreCase("M")){
                gridArray.add(pos, new Item(seatIcon,  "seat" + seatModel.getSeatNo()));
            }else {
                gridArray.add(pos, new Item(ladiesIcon,  "seat" + seatModel.getSeatNo()));
            }
        }else if(seatcompare==seatSelectBerth){
            if(seatModel.getGender().equalsIgnoreCase("M")){
                gridArray.add(pos, new Item(seatIconBerth,  "seat" + seatModel.getSeatNo()));
            }else {
                gridArray.add(pos, new Item(ladiesIconBerth,  "seat" + seatModel.getSeatNo()));
            }
        }
        allocationAdapterClass.notifyDataSetChanged();
        comm.respondDeSelectedUpper("Upper",pos);
    }

}
