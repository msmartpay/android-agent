package in.msmartpay.agent.aeps.onboard;

import android.content.Context;
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

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

public class UserNumberDialog extends DialogFragment {

    private String otp = "";
    private Context context;
    private ProgressDialogFragment pd;
    private UserNumberVerifyListener listener;
    private boolean isGenerateOtp;
    private String number, name;

    public void setListener(UserNumberVerifyListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activate_user_verfy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        Util.hideView(tv_done);
        tv_toolbar_title.setText("Activate Service");

        Button btn_send = view.findViewById(R.id.btn_send);
        TextInputLayout til_number = view.findViewById(R.id.til_number);


        Bundle bundle = getArguments();
        if (bundle != null) {
            number = bundle.getString(Keys.CUSTOMER_MOBILE);
            til_number.getEditText().setText(number);
            til_number.getEditText().setFocusable(false);
            til_number.getEditText().setFocusableInTouchMode(false);
        }


        iv_close.setOnClickListener(v -> {
            dismiss();
        });

        btn_send.setOnClickListener(v -> {
            number = Objects.requireNonNull(til_number.getEditText()).getText().toString().trim();
            if (number.isEmpty() || number.length()<10) {
                L.toastS(context, "Enter Number");
            } else {
                UserVerifyOtpDialog.showDialog(requireActivity().getSupportFragmentManager(), number);
                dismiss();
            }
        });
    }

    public interface UserNumberVerifyListener {
        void onNumberVerify();
    }

    public static UserNumberDialog newInstance(String number) {
        Bundle args = new Bundle();
        UserNumberDialog fragment = new UserNumberDialog();
        args.putString(Keys.CUSTOMER_MOBILE, number);
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager, String number) {
        UserNumberDialog dialog = UserNumberDialog.newInstance(number);
        dialog.show(manager, "Show Dialog");
    }
}
