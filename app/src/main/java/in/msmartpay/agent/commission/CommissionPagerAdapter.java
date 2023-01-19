package in.msmartpay.agent.commission;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CommissionPagerAdapter extends FragmentStatePagerAdapter {
    public CommissionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new ComRechargeFrag();
        if(position==1)
            return new ComUtilityFrag();
        if(position==2)
            return new ComDmrFrag();
        if(position==3)
            return new ComApesFrag();

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "Recharge";
        if(position==1)
            return "Utility";
        if(position==2)
            return "DMR";
        if(position==3)
            return "AEPS";

        return null;
    }
}