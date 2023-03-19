package in.msmartpayagent.dmrPaySprint.onboard;

import android.content.Context;
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

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import in.msmartpayagent.R;
import in.msmartpayagent.dmrPaySprint.dashboard.MoneyTransferActivity;
import in.msmartpayagent.network.AppMethods;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.network.model.dmr.SenderFindRequest;
import in.msmartpayagent.network.model.dmr.SenderRegisterResponse;
import in.msmartpayagent.network.model.dmr.ps.PSSenderRegisterRequest;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PSSenderRegisterDialog extends DialogFragment {

    private Context context;
    private String number = "", name = "", lname = "", address = "", pincode = "";

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dmr_onboard_register_sender, container, false);
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
        TextInputEditText tie_lname = view.findViewById(R.id.tie_lname);

        Util.hideView(tv_done);
        tv_toolbar_title.setText("Add Customer");

        number = Util.LoadPrefData(context, Keys.SENDER_MOBILE);
        tie_number.setText(number);
        //tie_dob.setInputType(InputType.TYPE_NULL);
        //tie_dob.setOnClickListener(v -> {
        //Util.showCalender(tie_dob);
        // });
        iv_close.setOnClickListener(v -> {
            dismiss();
        });

        btn_addCustomer.setOnClickListener(v -> {
            number = Objects.requireNonNull(tie_number.getText()).toString().trim();
            name = Objects.requireNonNull(tie_name.getText()).toString().trim();
            address = Objects.requireNonNull(tie_address.getText()).toString().trim();
            lname = Objects.requireNonNull(tie_lname.getText()).toString().trim();
            pincode = Objects.requireNonNull(tie_pincode.getText()).toString().trim();
            if (number.isEmpty()) {
                L.toastS(requireContext(), "Enter Customer Number");
            } else if (name.isEmpty()) {
                L.toastS(requireContext(), "Enter Customer Name");
            } else if (address.isEmpty()) {
                L.toastS(requireContext(), "Enter Address");
            } else if (pincode.isEmpty()) {
                L.toastS(requireContext(), "Enter Pincode");
            }
            //if (dob.isEmpty()) {
            //  L.toastS(requireContext(), "Enter Date of Birth");
            //}
            else {
                registerSender();
            }
        });
    }

    private void registerSender() {
        if (NetworkConnection.isConnectionAvailable(context)) {
            ProgressDialogFragment pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Customer...");
            ProgressDialogFragment.showDialog(pd, getChildFragmentManager());

            PSSenderRegisterRequest request = new PSSenderRegisterRequest();
            request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
            request.setSenderId(number);
            request.setFirstname(name);
            request.setLastname(lname);
            request.setAddress(address);
            request.setPincode(pincode);
            RetrofitClient.getClient(context)
                    .registerSender(Util.LoadPrefData(context, Keys.DYNAMIC_DMR_VENDOR) + AppMethods.RegisterSender, request)
                    .enqueue(new Callback<SenderRegisterResponse>() {
                @Override
                public void onResponse(Call<SenderRegisterResponse> call, Response<SenderRegisterResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        SenderRegisterResponse res = response.body();
                        if (res.getStatus().equals("2")) {
                            Util.SavePrefData(context, Keys.SENDER_MOBILE, number);
                            Util.SavePrefData(context, Keys.REGISTER_STATE, res.getData().getState());
                            Util.SavePrefData(context, Keys.SENDER_Request, Util.getJsonFromModel(request));
                            Util.SavePrefData(context, Keys.SENDER, Util.getJsonFromModel(res));
                            PSSenderRegisterConfirmDialog.showDialog(requireActivity().getSupportFragmentManager());
                            dismiss();
                        } else if (res.getStatus().equals("3")) {
                            Util.SavePrefData(context, Keys.SENDER_MOBILE, number);
                            Util.SavePrefData(context, Keys.REGISTER_STATE, res.getData().getState());
                            Util.SavePrefData(context, Keys.SENDER_Request, Util.getJsonFromModel(request));
                            Util.SavePrefData(context, Keys.SENDER, Util.getJsonFromModel(res));
                            PSSenderRegisterConfirmDialog.showDialog(requireActivity().getSupportFragmentManager());
                            dismiss();
                        } else if (res.getStatus().equals("0")) {
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
        RetrofitClient.getClient(context).findSenderDetails(Util.LoadPrefData(context, Keys.DYNAMIC_DMR_VENDOR) + AppMethods.FindSender, request).enqueue(new Callback<SenderDetailsResponse>() {
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

    public static PSSenderRegisterDialog newInstance() {
        PSSenderRegisterDialog fragment = new PSSenderRegisterDialog();
        return fragment;
    }

    public static void showDialog(FragmentManager manager) {
        PSSenderRegisterDialog dialog = PSSenderRegisterDialog.newInstance();
        dialog.show(manager, "Show Dialog");
    }
}
