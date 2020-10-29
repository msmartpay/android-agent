package in.msmartpay.agent.rechargeBillPay.plans;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PlanPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> arrayList;
    private ArrayList<ArrayList<PlanListModel>> plansList;

    public PlanPagerAdapter(FragmentManager fm, ArrayList<String> arrayList,ArrayList<ArrayList<PlanListModel>> plansList) {
        super(fm);
        this.arrayList = arrayList;
        this.plansList = plansList;
    }

    @Override
    public Fragment getItem(int position) {
        if(plansList.size()>0)
            return DynamicFragment.newInstance(plansList.get(position));
        else
            return null;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(arrayList.size()>0)
            return arrayList.get(position);
        else
            return null;
    }
}