package com.aepssdkssz.fingpay;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aepssdkssz.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FingpayAePSBiometricPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_biometric_kyc};
    private final Context mContext;
    private String aadhar_number, device_number, encodeFPTxnId, primaryKeyId;

    public FingpayAePSBiometricPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.aadhar_number=aadhar_number;
        this.device_number=device_number;
        this.encodeFPTxnId=encodeFPTxnId;
        this.primaryKeyId=primaryKeyId;
    }

    @Override
    public Fragment getItem(int position) {

        return FingpayAePSBiometricFragment.newInstance(position + 1);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 1;
    }
}