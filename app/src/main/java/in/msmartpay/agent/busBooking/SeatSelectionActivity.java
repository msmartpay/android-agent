package in.msmartpay.agent.busBooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.L;

import static in.msmartpay.agent.busBooking.BusBookingSearchActivity_H.passenser;

/*import static agent.msmartpay.in.BusBooking.BusBookingSearchActivity_H.passenser;*/

/**
 * Created by Smartkinda on 7/15/2017.
 */

public class SeatSelectionActivity extends BaseActivity implements LowerSeatFragment.Communicator,UpperSeatFragment.Communicator {

    private Context context;
    private ViewPager viewPager;
    private String TravellerName, BusTiming, SeatType, PendingSeats, Fare, FromSource ,ToSource, DateOfJourney;
    private TextView DOJwithTimeing, TravellerNameText;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Menu menu;
    private MenuItem bedMenuItem;
    private String upperStatus =null;
    private ArrayList<String> AvailableSeatsArrayList;
    private ArrayList<BusDetailsModel> BoardingPointArrayList=null;
    private ArrayList<BusDetailsModel> DroppingPointArrayList=null;
    private ArrayList<BusDetailsModel> FaresArrayList=null;
    private ArrayList<SeatModel> LowerSeatsArrayList=null,UpperSeatsArrayList=null;
    public static ArrayList<SeatListModel> seatList = new ArrayList<SeatListModel>();
    private Double finalFare = 0.0;
    private TextView tvSeatNo,tvSeatFare,tvBookNow;
    private int countColumnLower=0;
    private int countColumnUpper=0;
    private TextView tv_Lower, tv_Upper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_seat_select_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = SeatSelectionActivity.this;
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        sharedPreferences = getApplication().getSharedPreferences("Details", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        FromSource = sharedPreferences.getString("FromSource", null);
        ToSource = sharedPreferences.getString("ToSource", null);
        DateOfJourney = sharedPreferences.getString("DateOfJourney", null);
        TravellerName = sharedPreferences.getString("TravellerName", null);
        BusTiming = sharedPreferences.getString("BusTiming", null);
        upperStatus =getIntent().getStringExtra("upperStatus");
        BoardingPointArrayList =(ArrayList<BusDetailsModel>)getIntent().getSerializableExtra("BoardingPointArrayList");
        DroppingPointArrayList =(ArrayList<BusDetailsModel>)getIntent().getSerializableExtra("DroppingPointArrayList");
        FaresArrayList =(ArrayList<BusDetailsModel>)getIntent().getSerializableExtra("FaresArrayList");
        LowerSeatsArrayList =getIntent().getParcelableArrayListExtra("LowerSeatsArrayList");
        UpperSeatsArrayList =getIntent().getParcelableArrayListExtra("UpperSeatsArrayList");
        countColumnLower = getIntent().getIntExtra("countColumnLower",0);
        countColumnUpper= getIntent().getIntExtra("countColumnUpper",0);
        PendingSeats = getIntent().getStringExtra("PendingSeats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(FromSource + " - " + ToSource); //change like app
        DOJwithTimeing = (TextView) findViewById(R.id.doj_with_timing);
        TravellerNameText = (TextView) findViewById(R.id.travel_agency);
        TravellerNameText.setText(TravellerName);
        DOJwithTimeing.setText(DateOfJourney+" - "+BusTiming);

        tvSeatNo = (TextView) findViewById(R.id.seat_no);
        tvSeatFare= (TextView) findViewById(R.id.seat_fare);
        tvBookNow = (TextView) findViewById(R.id.book_now);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),upperStatus,LowerSeatsArrayList,UpperSeatsArrayList));    // Set up the ViewPager with the sections adapter.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.whitecolor));
            drawable.setSize(5, 2);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
        tvBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seatList.size() == 0){
                    Toast.makeText(context, "Please select seat first !!!", Toast.LENGTH_SHORT).show();
                } else if(seatList.size() > 6){
                    Toast.makeText(context, "You can't select more than 6 seats at a time !!!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, TravellersDetail.class);
                    intent.putExtra("SeatNoDetails", seatList);
                    intent.putParcelableArrayListExtra("HasmapSeats", LowerSeatsArrayList);
                    intent.putParcelableArrayListExtra("UpperSeatsArrayList", UpperSeatsArrayList);
                    intent.putExtra("BoardingPointArrayList", BoardingPointArrayList);
                    intent.putExtra("DroppingPointArrayList", DroppingPointArrayList);
                    intent.putExtra("FaresArrayList", FaresArrayList);
                    intent.putExtra("AllSeatsFare", String.valueOf(finalFare));
                    L.m2("SeatNoDetails---1>", seatList+"->"+ String.valueOf(finalFare)+"->"+BoardingPointArrayList);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void respondSelected(String respond, int pos) {

        ArrayList<SeatModel> tempList = new ArrayList<>();
             tempList =  LowerSeatsArrayList;

        SeatModel seatModel=new SeatModel();
        seatModel = tempList.get(pos);
        SeatListModel seatListModel= new SeatListModel();
        seatListModel.setSeatFare(seatModel.getFare());
        seatListModel.setSeatNumber(seatModel.getSeatNo());
        seatListModel.setSeatTypeIdNew(seatModel.getSeatTypeId());
        seatList.add(seatListModel);


        tvSeatNo.setText("Seat No. "+ seatList.toString());
        L.m2("Fare--seatListLower", seatList.toString()+" ::: "+seatList.size());

        finalFare = 0.0;
        tvSeatNo.setText("Seat No. ");
        for(int i = 0; i< seatList.size(); i++){
            SeatListModel seatListModel1= new SeatListModel();
            seatListModel1= seatList.get(i);
            finalFare = finalFare + Double.parseDouble(seatListModel1.getSeatFare());

            if (tvSeatNo.getText().toString().equalsIgnoreCase("Seat No. ")) {
                tvSeatNo.append(seatListModel1.getSeatNumber() + "");
            }else{
                tvSeatNo.append("," + seatListModel1.getSeatNumber() + "");
            }
        }
        tvSeatFare.setText("\u20B9 "+ String.valueOf(finalFare)+" * "+(seatList.size()));
    }
    @Override
    public void respondDeSelected(String respond, int pos) {
        ArrayList<SeatModel> tempList = new ArrayList<>();
            tempList =  LowerSeatsArrayList;
        SeatModel seatModel = new SeatModel();
        seatModel=tempList.get(pos);
        if(seatList.size() == 0){
            tvSeatNo.setText("Seat No. ");
            tvSeatFare.setText("Fare ");
        }else {
            tvSeatNo.setText("Seat No. " + seatList.toString());
        }
        for(int k=0;k<seatList.size();k++) {
            SeatListModel seatListModel = new SeatListModel();
            seatListModel = seatList.get(k);
            if (seatListModel.getSeatNumber().equalsIgnoreCase(seatModel.getSeatNo())) {
                seatList.remove(k);
            } else if (seatList.isEmpty()) {
                seatList.remove(Integer.valueOf(0));

            }
        }
        finalFare = 0.0;
        tvSeatNo.setText("Seat No. ");
        for(int i = 0; i< seatList.size(); i++){
            SeatListModel seatListModel1= new SeatListModel();
            seatListModel1= seatList.get(i);
            finalFare = finalFare + Double.parseDouble(seatListModel1.getSeatFare());

            if (tvSeatNo.getText().toString().equalsIgnoreCase("Seat No. ")) {
                tvSeatNo.append(seatListModel1.getSeatNumber() + "");
            }else{
                tvSeatNo.append("," + seatListModel1.getSeatNumber() + "");
            }
        }
        tvSeatFare.setText("\u20B9 "+ String.valueOf(finalFare)+" * "+(seatList.size()));
    }

    @Override
    public void respondSelectedUpper(String respond, int pos) {
        ArrayList<SeatModel> tempList = new ArrayList<>();
        tempList = UpperSeatsArrayList;
        SeatModel seatModel = new SeatModel();
        seatModel = tempList.get(pos);

        SeatListModel seatListModel = new SeatListModel();
        seatListModel.setSeatFare(seatModel.getFare());
        seatListModel.setSeatNumber(seatModel.getSeatNo());
        seatListModel.setSeatTypeIdNew(seatModel.getSeatTypeId());
        seatList.add(seatListModel);


        tvSeatNo.setText("Seat No. " + seatList.toString());
        L.m2("Fare--seatListUpper", seatList.toString() + " ::: " + seatList.size());

        finalFare = 0.0;
        tvSeatNo.setText("Seat No. ");
        for (int i = 0; i < seatList.size(); i++) {
            SeatListModel seatListModel1 = new SeatListModel();
            seatListModel1 = seatList.get(i);
            finalFare = finalFare + Double.parseDouble(seatListModel1.getSeatFare());

            if (tvSeatNo.getText().toString().equalsIgnoreCase("Seat No. ")) {
                tvSeatNo.append(seatListModel1.getSeatNumber() + "");
            }else{
                tvSeatNo.append("," + seatListModel1.getSeatNumber() + "");
            }
        }

        tvSeatFare.setText("\u20B9 "+ String.valueOf(finalFare)+" * "+(seatList.size()));
    }

    @Override
    public void respondDeSelectedUpper(String respond, int pos) {
        ArrayList<SeatModel> tempList = new ArrayList<>();
        tempList =  UpperSeatsArrayList;

        SeatModel seatModel = new SeatModel();
        seatModel=tempList.get(pos);
        if(seatList.size() == 0){
            tvSeatNo.setText("Seat No. ");
            tvSeatFare.setText("Fare ");
        }else {
            tvSeatNo.setText("Seat No. " + seatList.toString());
        }
        for(int k=0;k<seatList.size();k++) {
            SeatListModel seatListModel = new SeatListModel();
            seatListModel = seatList.get(k);
            if (seatListModel.getSeatNumber().equalsIgnoreCase(seatModel.getSeatNo())) {
                seatList.remove(k);
            } else if (seatList.isEmpty()) {
                seatList.remove(Integer.valueOf(0));

            }
        }
        finalFare = 0.0;
        tvSeatNo.setText("Seat No. ");
        for(int i = 0; i< seatList.size(); i++){
            SeatListModel seatListModel1= new SeatListModel();
            seatListModel1= seatList.get(i);
            finalFare = finalFare + Double.parseDouble(seatListModel1.getSeatFare());

            if (tvSeatNo.getText().toString().equalsIgnoreCase("Seat No. ")) {
                tvSeatNo.append(seatListModel1.getSeatNumber() + "");
            }else{
                tvSeatNo.append("," + seatListModel1.getSeatNumber() + "");
            }
        }
        tvSeatFare.setText("\u20B9 "+ String.valueOf(finalFare)+" * "+(seatList.size()));
    }


    class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        int tab=0;
        private ArrayList<SeatModel> LowerSeatsArrayList, UpperSeatsArrayList;
        public MyViewPagerAdapter(FragmentManager fm , String status, ArrayList<SeatModel> LowerSeatsArrayList, ArrayList<SeatModel> UpperSeatsArrayList) {
            super(fm);
            this.LowerSeatsArrayList = LowerSeatsArrayList;
            this.UpperSeatsArrayList = UpperSeatsArrayList;
            if(status.equalsIgnoreCase("Y")){
                tab=2;
            }else{
                tab=1;
            }
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LowerSeatFragment.newInstance(LowerSeatsArrayList,countColumnLower);
                case 1:
                    return UpperSeatFragment.newInstance(UpperSeatsArrayList,countColumnUpper);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return tab;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Lower";
            }
           else if (position == 1) {
                return "Upper";
            }
            else {
                return null;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.increse_seat_number, paramMenu);
        menu = paramMenu;
        bedMenuItem = menu.findItem(R.id.seat_number);
        bedMenuItem.setTitle("" + passenser);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        int i = paramMenuItem.getItemId();
        switch (i){
            case R.id.increase_seat:
                if(passenser <6) {
                    passenser++;
                    bedMenuItem.setTitle("" + passenser);
                }else{
                    Toast.makeText(getApplicationContext(), "You can't select more than 6 seats at a time !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                finish();
                seatList.clear();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        seatList.clear();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
