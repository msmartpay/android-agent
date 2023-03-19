package in.msmartpayagent.user;

import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import in.msmartpayagent.R;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainRequest;
import in.msmartpayagent.network.model.user.ProfileResponse;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;


public class MyProfile extends BaseActivity {
    private String agentID,agentName="";
    private JSONObject data = null;
    private String agent_id_full;
    private String txn_key ;
    private TextView tv_profile_name, tv_profile_id;
    private Context context;
    private ProgressDialogFragment pd;
    private ProgressBar pb_p_district, pb_p_state;

    private ViewPager viewPager = null;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_new_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("My Profile");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        agent_id_full = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_FULL);
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        agentName = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_NAME);


        tv_profile_name=findViewById(R.id.tv_profile_name);
        tv_profile_id=findViewById(R.id.tv_profile_id);

        tv_profile_name.setText(agentName);
        tv_profile_id.setText("User Id : "+agent_id_full);

        getProfile();

        viewPager = (ViewPager) findViewById(R.id.profile_pager);
        viewPager.setAdapter(new MyProfilePagerAdapter(getSupportFragmentManager(),getApplicationContext()));    // Set up the ViewPager with the sections adapter.
        tabLayout = (TabLayout) findViewById(R.id.profile_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void getProfile() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Profile ...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            MainRequest request = new MainRequest();
            request.setAgentID(agentID);
            request.setTxn_key(txn_key);

            RetrofitClient.getClient(getApplicationContext())
                    .getProfile(request).enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(@NotNull Call<ProfileResponse> call, @NotNull retrofit2.Response<ProfileResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        ProfileResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {

                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_COMPANY,res.getProfile().getFirmname());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_DOB,res.getProfile().getDateOfBirth());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_MOB,res.getProfile().getMobile());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_GENDER,res.getProfile().getGender());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_EMAIL,res.getProfile().getEmailID());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_STATE,res.getProfile().getState());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_CITY,res.getProfile().getCity());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_ADDRESS,res.getProfile().getAddress());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_DISTRICT,res.getProfile().getDistrict());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_PAN,res.getProfile().getPannumber());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_AADHAAR,res.getProfile().getAdharnumber());

                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_OFF_ADD,res.getProfile().getOffice_address());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_OFF_COUNTRY,res.getProfile().getOffice_country());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_OFF_STATE,res.getProfile().getOffice_state());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_OFF_DISTRICT,res.getProfile().getOffice_district());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_OFF_CITY,res.getProfile().getOffice_city());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_OFFICE_PINCODE,res.getProfile().getOffice_pincode());
                            Util.SavePrefData(getApplicationContext(),Keys.AGENT_M_NAME,res.getProfile().getAgent_middle_name());

                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ProfileResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
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

    class MyProfilePagerAdapter extends FragmentStatePagerAdapter {
        private Context ctx;

        public MyProfilePagerAdapter(FragmentManager fm, Context ctx) {
            super(fm);
            this.ctx = ctx;
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new ProfileContactFragment();
            }
            if (position == 1) {
                fragment = new ProfileAddressFragment();
            }
            if (position == 2) {
                fragment = new ProfileOfficeAddressFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profile";
                case 1:
                    return "Address";
                case 2:
                    return "Office Address";

            }
            return null;
        }


    }
}
