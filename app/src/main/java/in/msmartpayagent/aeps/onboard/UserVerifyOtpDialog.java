package in.msmartpayagent.aeps.onboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import in.msmartpayagent.R;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainResponse2;
import in.msmartpayagent.network.model.aeps.onboard.UserRequest;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserVerifyOtpDialog extends DialogFragment {

    private String otp = "";
    private VerifyOTPListener listener;
    private String number;

    public void setListener(VerifyOTPListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activate_verify_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        TextView tv_resend = view.findViewById(R.id.tv_resend);
        TextInputLayout til_otp = view.findViewById(R.id.til_otp);
        Button btn_verify = view.findViewById(R.id.btn_verify);

        tv_toolbar_title.setText("Verify OTP");
        tv_done.setText("Verify");

        Bundle bundle = getArguments();
        if (bundle != null) {
            number = bundle.getString(Keys.CUSTOMER_MOBILE);
            sendOtp();
        }


        iv_close.setOnClickListener(v -> {
            dismiss();
        });

        btn_verify.setOnClickListener(v -> {
            otp = Objects.requireNonNull(til_otp.getEditText()).getText().toString().trim();
            if (otp.isEmpty()) {
                L.toastS(requireActivity(), "Enter OTP");
            } else {
                verifyOtp();
            }
        });
        tv_done.setOnClickListener(v -> {
            otp = Objects.requireNonNull(til_otp.getEditText()).getText().toString().trim();
            if (otp.isEmpty()) {
                L.toastS(requireActivity(), "Enter OTP");
            } else {
                verifyOtp();
            }
        });
        tv_resend.setOnClickListener(v -> {
            sendOtp();
        });
    }

    private void sendOtp() {
        if (NetworkConnection.isConnectionAvailable(requireActivity())) {
            ProgressDialogFragment pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Sending Otp...");
            ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
            UserRequest req = new UserRequest();
            req.setKey(Util.LoadPrefData(requireActivity(), Keys.TXN_KEY));
            req.setAgentID(Util.LoadPrefData(requireActivity(), Keys.AGENT_ID));
            req.setMobile(number);
            RetrofitClient.getClient(requireActivity())
                    .activateRequestOTP(req)
                    .enqueue(new Callback<MainResponse2>() {
                        @Override
                        public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                            try {
                                pd.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    MainResponse2 res = response.body();
                                    if ("0".equalsIgnoreCase(res.getStatus())) {
                                        L.toastS(requireActivity(), res.getMessage());
                                    } else if ("2".equalsIgnoreCase(res.getStatus())) {
                                        showConfirmationDialog(res.getMessage());
                                    } else {
                                        L.toastS(requireActivity(), res.getMessage());
                                        showGetCustomerDialog();
                                    }
                                } else {
                                    showGetCustomerDialog();
                                }
                            } catch (Exception e) {
                                L.toastS(requireActivity(), "Parser Error : " + e.getLocalizedMessage());
                                L.m2("Parser Error", e.getLocalizedMessage());
                                showGetCustomerDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<MainResponse2> call, Throwable t) {
                            pd.dismiss();
                            L.toastS(requireActivity(), "Error : " + t.getLocalizedMessage());
                            showGetCustomerDialog();

                        }
                    });
        }
    }

    private void showGetCustomerDialog() {
        dismiss();
        UserNumberDialog.showDialog(requireActivity().getSupportFragmentManager(), number);
    }

    private void verifyOtp() {
        if (NetworkConnection.isConnectionAvailable(requireActivity())) {
            ProgressDialogFragment pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Verify OTP...");
            ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
            UserRequest req = new UserRequest();
            req.setKey(Util.LoadPrefData(requireActivity(), Keys.TXN_KEY));
            req.setAgentID(Util.LoadPrefData(requireActivity(), Keys.AGENT_ID));
            req.setMobile(number);
            req.setOTP(otp);
            RetrofitClient.getClient(requireActivity())
                    .activateVerifyOTP(req).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                    try {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            MainResponse2 res = response.body();
                            if ("0".equalsIgnoreCase(res.getStatus())) {
                                String msg = "";
                                if (res.getMessage() != null && res.getMessage() != null) {
                                    msg = res.getMessage();
                                } else {
                                    msg = "Successfully verified";
                                }
                                showConfirmationDialog(msg);
                            } else {
                                L.toastS(requireActivity(), res.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        L.toastS(requireActivity(), "Parser Error : " + e.getLocalizedMessage());
                        L.m2("Parser Error", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call<MainResponse2> call, Throwable t) {
                    L.toastS(requireActivity(), "Error : " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    //Confirmation Dialog
    private void showConfirmationDialog(String msg) {
        // TODO Auto-generated method stub
        @SuppressLint("PrivateResource") final
        Dialog d = new Dialog(requireActivity(), R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        Objects.requireNonNull(d.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr_confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit = d.findViewById(R.id.btn_push_submit);
        final Button btnClosed = d.findViewById(R.id.close_push_button);
        Util.hideView(btnClosed);
        final TextView tvConfirmation = d.findViewById(R.id.tv_confirmation_dialog);
        tvConfirmation.setText(msg);

        btnSubmit.setOnClickListener(v -> {
            UserRegistrationDialog.showDialog(requireActivity().getSupportFragmentManager(), number);
            d.cancel();
            dismiss();
        });
        btnClosed.setOnClickListener(v -> {
            d.cancel();
            dismiss();
        });
        d.show();
    }

    public interface VerifyOTPListener {
        void onVerifyOtp();
    }

    public static UserVerifyOtpDialog newInstance(String number) {
        Bundle args = new Bundle();
        UserVerifyOtpDialog fragment = new UserVerifyOtpDialog();
        args.putString(Keys.CUSTOMER_MOBILE, number);
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager, String number) {
        UserVerifyOtpDialog dialog = UserVerifyOtpDialog.newInstance(number);
        dialog.show(manager, "Show Dialog");
    }
}
