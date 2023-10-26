package com.aepssdkssz.ekoaeps.ekyc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.aepssdkssz.util.Utility;
import com.aepssdkssz.R;

public class EkoEKYCSuccessDialog extends DialogFragment {

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.eko_ekyc_status_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        Button btn_aeps_activate = view.findViewById(R.id.btn_aeps_activate);

        ImageView iv_success = view.findViewById(R.id.iv_success);
        TextView tv_success = view.findViewById(R.id.tv_success);

        tv_toolbar_title.setText("e-KYC Status");
        Utility.hideView(tv_done);

        iv_close.setOnClickListener(v -> {
            dismiss();
        });
        btn_aeps_activate.setOnClickListener(v -> {
            //startActivity(new Intent(requireActivity(),ActivateAepsActivity.class));
            dismiss();
        });


    }

    public static EkoEKYCSuccessDialog newInstance(String message) {
        Bundle args = new Bundle();
        EkoEKYCSuccessDialog fragment = new EkoEKYCSuccessDialog();
        args.putString("message",message);
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager,String message) {
        EkoEKYCSuccessDialog dialog = EkoEKYCSuccessDialog.newInstance(message);
        dialog.show(manager, "Show Dialog");
    }
}
