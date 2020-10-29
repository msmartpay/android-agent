package agent.msmartpay.in.moneyTransferDMT;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Stack;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.L;

/**
 * Created by Smartkinda on 7/4/2017.
 */

public class MoneyTransferActivity1 extends BaseActivity {

    private ViewPager viewPager;
    public static String SenderLimit_Detail_BeniList = null;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key, sessionID;
    private TabLayout tabLayout;
    private Stack<Integer> stackkk = new Stack<>(); // Edited
    private int tabPosition = 0, tabPositionUnselected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_money_transfer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transfer Money");

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txn_key = sharedPreferences.getString("txn-key", null);
        sessionID = sharedPreferences.getString("SessionID", null);
        L.m2("Session--->", sessionID);


        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));    // Set up the ViewPager with the sections adapter.
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());

                if (stackkk.empty())
                    stackkk.push(0);

                if (stackkk.contains(tabPosition)) {
                    stackkk.remove(stackkk.indexOf(tabPosition));
                    stackkk.push(tabPosition);
                } else {
                    stackkk.push(tabPosition);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabPositionUnselected = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
       getPagger();
    }

    private void getPagger() {
        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.whitecolor));
            drawable.setSize(5, 2);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
    }


    class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new DetailsFragment();
            } else if (position == 1) {
                fragment = new BeneficiaryListFragment();
            }/*else if (position == 2) {
                fragment = new VerifyAccountFragment();
            } */else if (position == 2) {
                fragment = new RefundFragment();
            }else if (position == 3) {
                fragment = new SenderHistoryFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "Sender Details";
            } else if (position == 1) {
                return "Bene List";
            } /*else if (position == 2) {
                return "Verify Acc";
            }*/else if (position == 2) {
                return "Refund";
            }else if (position == 3) {
                return "Sender History";
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        if (stackkk.size() > 1) {
            stackkk.pop();
            viewPager.setCurrentItem(stackkk.lastElement());
        } else {
            finish();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if (stackkk.size() > 1) {
            stackkk.pop();
            viewPager.setCurrentItem(stackkk.lastElement());
        } else {
            finish();
        }
    }
}
