package in.msmartpayagent.dmrPaySprint.onboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.material.textfield.TextInputEditText;
import in.msmartpayagent.R;
import in.msmartpayagent.aeps.onboard.UserNumberDialog;
import in.msmartpayagent.dmrPaySprint.dashboard.MoneyTransferActivity;
import in.msmartpayagent.network.AppMethods;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.network.model.dmr.SenderFindRequest;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PSFindSenderDialog extends DialogFragment {

    private Context context;
    private String number;

    private String name = "",OTPREFID="";
    private int otpRequired=0;

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dmr_onboard_find_sender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        Button btn_findsender = view.findViewById(R.id.btn_findsender);
        TextInputEditText tie_number = view.findViewById(R.id.tie_number);

        Util.hideView(tv_done);
        tv_toolbar_title.setText("Find Sender");

        iv_close.setOnClickListener(v -> {
            dismiss();
        });
        btn_findsender.setOnClickListener(v -> {
            number = Objects.requireNonNull(tie_number.getText()).toString().trim();
            if (number.isEmpty()) {
                L.toastS(requireContext(), "Enter Number");
            } else {
                findSender();
            }
        });

    }


    private void findSender() {
        if (NetworkConnection.isConnectionAvailable(context)) {
            ProgressDialogFragment pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Finding Sender...");
            ProgressDialogFragment.showDialog(pd, getChildFragmentManager());

            SenderFindRequest request = new SenderFindRequest();
            request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
            request.setSenderId(number);
            RetrofitClient.getClient(context).findSenderDetails(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+ AppMethods.FindSender,request).enqueue(new Callback<SenderDetailsResponse>() {
                @Override
                public void onResponse(Call<SenderDetailsResponse> call, Response<SenderDetailsResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        SenderDetailsResponse res = response.body();
                        if (res.getStatus().equals("0")) {
                            Util.SavePrefData(context, Keys.SENDER_MOBILE, number);
                            Util.SavePrefData(context, Keys.SENDER, Util.getGson().toJson(res));
                            startActivity(new Intent(context, MoneyTransferActivity.class));
                            dismiss();
                        } else if (res.getStatus().equals("1")) {
                            showResponseDialog(res.getMessage(), 1);
                        } else if (res.getStatus().equals("2")) {
                            showResponseDialog(res.getMessage(), 2);
                        } else if (res.getStatus().equals("3")) {
                            otpRequired=res.getOtpRequired();
                            OTPREFID=res.getOtpRefId();
                            showResponseDialog(res.getMessage(), 3);
                        } else if (res.getStatus().equals("5")) {
                            showResponseDialog(res.getMessage(), 5);
                        } else {
                            L.toastS(context, res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SenderDetailsResponse> call, Throwable t) {
                    pd.dismiss();
                }
            });
        }
    }

    //================For Resistration===============
    public void showResponseDialog(String msg, final int i) {
        //TODO Auto-generated method stub
        @SuppressLint("PrivateResource") final Dialog d = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        Objects.requireNonNull(d.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr_sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnOK = (Button) d.findViewById(R.id.btn_resister_ok);
        Button btnNO = (Button) d.findViewById(R.id.btn_resister_no);
        TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        Button btnClosed = (Button) d.findViewById(R.id.close_push_button);

        tvMessage.setText(msg);

        btnOK.setOnClickListener(v -> {
            Util.SavePrefData(context, Keys.SENDER_MOBILE, number);
            if (i == 1) {
                //Failed
            } else if (i == 2) {
                PSSenderRegisterConfirmDialog.showDialog(requireActivity().getSupportFragmentManager());

                dismiss();
            } else if (i == 3) {
                //Register
                if(otpRequired==1){
                    Util.SavePrefData(context,Keys.OTPREFID,OTPREFID);
                    PSSenderRegisterConfirmDialog.showDialog(requireActivity().getSupportFragmentManager());
                }else{
                    PSSenderRegisterDialog.showDialog(requireActivity().getSupportFragmentManager());
                }
                dismiss();
            } else if (i == 5) {
                //Agent Activation process
                UserNumberDialog.showDialog(requireActivity().getSupportFragmentManager(),Util.LoadPrefData(requireActivity(),Keys.AGENT_MOB));
                dismiss();
            }
            d.dismiss();
        });

        btnNO.setOnClickListener(v -> d.dismiss());

        btnClosed.setOnClickListener(v -> {
            d.dismiss();
        });
        d.show();
    }


    public static PSFindSenderDialog newInstance() {
        Bundle args = new Bundle();
        PSFindSenderDialog fragment = new PSFindSenderDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static void showDialog(FragmentManager manager) {
        PSFindSenderDialog dialog = PSFindSenderDialog.newInstance();
        dialog.show(manager, "Show Dialog");
    }
}
