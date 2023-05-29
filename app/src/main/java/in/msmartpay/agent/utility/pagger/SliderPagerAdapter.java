package in.msmartpay.agent.utility.pagger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.msmartpay.agent.R;

public class SliderPagerAdapter extends PagerAdapter {
    private ArrayList<String> list = null;
    private Context mContext;

    public SliderPagerAdapter(Context mContext, ArrayList<String> list) {
        this.list = list;
        this.mContext = mContext;
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.fragment_second, null);
        LinearLayout ll_ii = layout.findViewById(R.id.ll_ii);
        ImageView iv = layout.findViewById(R.id.iv_img);

        Picasso.get().load(list.get(position)).into(iv);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}