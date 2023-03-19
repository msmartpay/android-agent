package in.msmartpayagent.commission;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import in.msmartpayagent.R;
import in.msmartpayagent.utility.BaseActivity;

import java.util.Objects;

public class CommissionActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commission_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Commission");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        TabLayout tab_layout = findViewById(R.id.tab_layout);
        ViewPager view_pager = findViewById(R.id.view_pager);
        CommissionPagerAdapter commPagerAdapter = new CommissionPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(commPagerAdapter);
        tab_layout.setupWithViewPager(view_pager);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
