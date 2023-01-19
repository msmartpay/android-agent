package com.aepssdkssz.dialog.fingpayonboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.aepssdkssz.R;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class SDKUserSuccessDialog extends DialogFragment {

    private String action="",message="";

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_fingpay_onboard_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        Button ssz_close = view.findViewById(R.id.ssz_close);
        tv_toolbar_title.setText("Notification");

        TextView ssz_tv_success = view.findViewById(R.id.ssz_tv_success);
        ImageView ssz_iv_success = view.findViewById(R.id.ssz_iv_success);
        ssz_tv_success.setText(message);

        Utility.hideView(tv_done);

        Bundle bundle = getArguments();
        if (bundle != null) {
            action=bundle.getString(Constants.ACTION);
            message = bundle.getString(Constants.MESSAGE);
        }
        if("1".equalsIgnoreCase(action)){
            ssz_iv_success.setBackground(getResources().getDrawable(R.drawable.ssz_aeps_tick_ok));
            iv_close.setOnClickListener(v -> {
                dismiss();
            });
            ssz_close.setOnClickListener(v -> {
                dismiss();
            });
        }else{
            ssz_iv_success.setBackground(getResources().getDrawable(R.drawable.ssz_failed));

            iv_close.setOnClickListener(v -> {
                dismiss();
            });
            ssz_close.setOnClickListener(v -> {
                dismiss();
            });
        }



    }

    public static SDKUserSuccessDialog newInstance() {
        Bundle args = new Bundle();
        SDKUserSuccessDialog fragment = new SDKUserSuccessDialog();
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }
    public static SDKUserSuccessDialog newInstance(String message,int action) {
        Bundle args = new Bundle();
        SDKUserSuccessDialog fragment = new SDKUserSuccessDialog();
        args.putString(Constants.MESSAGE, message);
        args.putString(Constants.ACTION, String.valueOf(action));
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager) {
        SDKUserSuccessDialog dialog = SDKUserSuccessDialog.newInstance();
        dialog.show(manager, "Show Dialog");
    }

    public static void showDialog(FragmentManager manager,String message,int action) {
        SDKUserSuccessDialog dialog = SDKUserSuccessDialog.newInstance();
        dialog.show(manager, "Show Dialog");
    }
}
