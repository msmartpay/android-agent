package in.msmartpay.agent.aeps.onboard;

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

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.Util;

public class UserSuccessDialog extends DialogFragment {

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activate_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        Button btn_aeps_activate = view.findViewById(R.id.btn_aeps_activate);
        tv_toolbar_title.setText("Onboard Success");
        Util.hideView(tv_done);

        iv_close.setOnClickListener(v -> {
            dismiss();
        });
        btn_aeps_activate.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(),ActivateAepsActivity.class));
            dismiss();
        });


    }

    public static UserSuccessDialog newInstance() {
        Bundle args = new Bundle();
        UserSuccessDialog fragment = new UserSuccessDialog();
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager) {
        UserSuccessDialog dialog = UserSuccessDialog.newInstance();
        dialog.show(manager, "Show Dialog");
    }
}
