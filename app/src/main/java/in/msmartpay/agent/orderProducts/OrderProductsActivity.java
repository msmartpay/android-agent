package in.msmartpay.agent.orderProducts;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.myWallet.BalanceHistoryFragment;
import in.msmartpay.agent.myWallet.BalanceRequestFragment;
import in.msmartpay.agent.utility.BaseActivity;


public class OrderProductsActivity extends BaseActivity {

    ViewPager viewPager = null;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_products_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Order Products");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        viewPager = (ViewPager) findViewById(R.id.pagerr);
        viewPager.setAdapter(new Myadapter(getSupportFragmentManager(),getApplicationContext()));    // Set up the ViewPager with the sections adapter.
        tabLayout = (TabLayout) findViewById(R.id.tabss);
        tabLayout.setupWithViewPager(viewPager);

    }

    class Myadapter extends FragmentStatePagerAdapter {
        private Context ctx;

        public Myadapter(FragmentManager fm, Context ctx) {
            super(fm);
            this.ctx = ctx;
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (isConnectionAvailable()) {
                if (position == 0) {
                    fragment = new OrderProductsAddFragment();
                }
                if (position == 1) {
                    fragment = new OrderProductsHistoryFragment();
                }
            } else {
                Toast.makeText(ctx, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Add Order";
                case 1:
                    return "Order History";
            }
            return null;
        }
    }
        @Override
        public boolean onSupportNavigateUp() {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

