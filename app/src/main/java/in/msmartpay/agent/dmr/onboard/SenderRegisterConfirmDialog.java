package in.msmartpay.agent.dmr.onboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.dmr.dashboard.MoneyTransferActivity;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.dmr.SenderDetailsResponse;
import in.msmartpay.agent.network.model.dmr.SenderFindRequest;
import in.msmartpay.agent.network.model.dmr.SenderRegisterRequest;
import in.msmartpay.agent.network.model.dmr.SenderRegisterResponse;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SenderRegisterConfirmDialog extends DialogFragment {

    private Context context;
    private String number, name, address, dob, otp, pincode;
    private SenderRegisterRequest request;

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dmr_onboard_register_sende_confirmr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        Button btn_addCustomer = view.findViewById(R.id.btn_addCustomer);
        TextInputEditText tie_number = view.findViewById(R.id.tie_number);
        TextInputEditText tie_name = view.findViewById(R.id.tie_name);
        TextInputEditText tie_address = view.findViewById(R.id.tie_address);
        TextInputEditText tie_pincode = view.findViewById(R.id.tie_pincode);
        TextInputEditText tie_dob = view.findViewById(R.id.tie_dob);
        TextInputEditText tie_otp = view.findViewById(R.id.tie_otp);

        Util.hideView(tv_done);
        tv_toolbar_title.setText("Register Customer");
        btn_addCustomer.setText("Register Customer");

        number = Util.LoadPrefData(context, Keys.SENDER_MOBILE);
        request = Util.getGson().fromJson(Util.LoadPrefData(context, Keys.SENDER_Request), SenderRegisterRequest.class);
        if (request != null) {
            tie_name.setText(request.getSenderName());
            tie_address.setText(request.getAddress());
            tie_dob.setText(request.getDOB());
            tie_number.setText(request.getSenderId());
            tie_pincode.setText(request.getPincode());
        }

        tie_number.setText(number);
        tie_dob.setInputType(InputType.TYPE_NULL);
        tie_dob.setOnClickListener(v -> {
            Util.showCalender(tie_dob);
        });
        iv_close.setOnClickListener(v -> {
            dismiss();
        });
        btn_addCustomer.setOnClickListener(v -> {
            number = Objects.requireNonNull(tie_number.getText()).toString().trim();
            name = Objects.requireNonNull(tie_name.getText()).toString().trim();
            address = Objects.requireNonNull(tie_address.getText()).toString().trim();
            dob = Objects.requireNonNull(tie_dob.getText()).toString().trim();
            otp = Objects.requireNonNull(tie_otp.getText()).toString().trim();
            pincode = Objects.requireNonNull(tie_pincode.getText()).toString().trim();
            if (number.isEmpty()) {
                L.toastS(requireContext(), "Enter Customer Number");
            }
            if (name.isEmpty()) {
                L.toastS(requireContext(), "Enter Customer Name");
            }
            if (address.isEmpty()) {
                L.toastS(requireContext(), "Enter Address");
            }
            if (pincode.isEmpty()) {
                L.toastS(requireContext(), "Enter Pincode");
            }
            if (dob.isEmpty()) {
                L.toastS(requireContext(), "Enter Date of Birth");
            }

            if (otp.isEmpty()) {
                L.toastS(requireContext(), "Enter OTP");
            } else {
                confirmSenderRegister();
            }
        });
    }

    private void confirmSenderRegister() {
        if (NetworkConnection.isConnectionAvailable(context)) {
            ProgressDialogFragment pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Customer...");
            ProgressDialogFragment.showDialog(pd, getChildFragmentManager());

            request = new SenderRegisterRequest();
            request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
            request.setSenderId(number);
            request.setSenderName(name);
            request.setDOB(dob);
            request.setAddress(address);
            request.setPincode(pincode);
            request.setOTP(otp);
            RetrofitClient.getClient(context).registerSenderConfirm(request).enqueue(new Callback<SenderRegisterResponse>() {
                @Override
                public void onResponse(Call<SenderRegisterResponse> call, Response<SenderRegisterResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        SenderRegisterResponse res = response.body();
                        if (res.getStatus().equals("0")) {
                            Util.SavePrefData(context, Keys.SENDER_MOBILE, number);
                            findSenderRequest();
                        } else {
                            L.toastS(context, res.getMessage());
                        }
                    } else {
                        L.toastS(context, "Server Response Error");
                    }
                }

                @Override
                public void onFailure(Call<SenderRegisterResponse> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(context, "Server Response Error " + t.getMessage());
                }
            });
        }
    }

    private void findSenderRequest() {
        ProgressDialogFragment pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Latest Details of Sender...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
        SenderFindRequest request = new SenderFindRequest();
        request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
        request.setSenderId(number);
        RetrofitClient.getClient(context).findSenderDetails(request).enqueue(new Callback<SenderDetailsResponse>() {
            @Override
            public void onResponse(Call<SenderDetailsResponse> call, Response<SenderDetailsResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    SenderDetailsResponse res = response.body();
                    if (res.getStatus().equals("0")) {
                        Util.SavePrefData(context, Keys.SENDER_MOBILE, number);
                        Util.SavePrefData(context, Keys.SENDER, Util.getGson().toJson(res));
                        Intent intent = new Intent(context, MoneyTransferActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        dismiss();
                    }
                } else {
                    L.toastS(context, "No Response");
                }
            }

            @Override
            public void onFailure(Call<SenderDetailsResponse> call, Throwable t) {
                pd.dismiss();
                L.toastS(context, "Error : " + t.getMessage());
            }
        });
    }

    public static SenderRegisterConfirmDialog newInstance() {
        SenderRegisterConfirmDialog fragment = new SenderRegisterConfirmDialog();
        return fragment;
    }

    public static void showDialog(FragmentManager manager) {
        SenderRegisterConfirmDialog dialog = SenderRegisterConfirmDialog.newInstance();
        dialog.show(manager, "Show Dialog");
    }
}
