package agent.msmartpay.in.moneyTransferNew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import agent.msmartpay.in.MainActivity;
import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;

/**
 * Created by Smartkinda on 7/4/2017.
 */

public class MoneyTransferActivity extends BaseActivity {

    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key, sessionID;
    private TabLayout tabLayout;
    private Stack<Integer> stackkk = new Stack<>(); // Edited
    private int tabPosition = 0, tabPositionUnselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dmr_money_transfer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transfer Money");

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        //agentID = sharedPreferences.getString("agentonlyid", null);
        //txn_key = sharedPreferences.getString("txn-key", null);
        //sessionID = sharedPreferences.getString("SessionID", null);

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
            } else if (position == 2) {
                fragment = new VerifyAccountFragment();
            } else if (position == 3) {
                fragment = new RefundFragment();
            } else if (position == 4) {
                fragment = new SenderHistoryFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "Sender Details";
            } else if (position == 1) {
                return "Bene List";
            } else if (position == 2) {
                return "Verify Account";
            } else if (position == 3) {
                return "Refund";
            } else if (position == 4) {
                return "Sender History";
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        /*if (stackkk.size() > 1) {
            stackkk.pop();
            viewPager.setCurrentItem(stackkk.lastElement());
        } else {
            finish();
        }*/
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
            new AlertDialog.Builder(MoneyTransferActivity.this)
                    .setIcon(R.drawable.warning_message_red)
                    .setTitle("Closing Application")
                    .setMessage("Are you sure you want to Exit ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MoneyTransferActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
    }
}
