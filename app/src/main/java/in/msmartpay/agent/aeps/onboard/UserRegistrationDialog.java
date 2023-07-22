package in.msmartpay.agent.aeps.onboard;

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
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.aeps.onboard.UserRegisterRequest;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrationDialog extends DialogFragment {

    private RegisterListener listener;
    private String number, fName, lName, mName;

    public void setListener(RegisterListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activate_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        Button btn_register = view.findViewById(R.id.btn_register);
        TextInputLayout til_firstName, til_lastName, til_middleName, til_mob;
        til_firstName = view.findViewById(R.id.til_firstName);
        til_middleName = view.findViewById(R.id.til_middleName);
        til_lastName = view.findViewById(R.id.til_lastName);
        til_mob = view.findViewById(R.id.til_mob);

        try {
            String agentName=Util.LoadPrefData(requireActivity(), Keys.AGENT_NAME);
            String agentNameArr[] = new String[0];
            if(agentName!=null){
                agentNameArr=agentName.split(" ");
            }
            if(agentNameArr.length==1){
                til_firstName.getEditText().setText(agentNameArr[0]);

            }else if(agentNameArr.length==2){
                til_firstName.getEditText().setText(agentNameArr[0]);
                til_lastName.getEditText().setText(agentNameArr[1]);
            }else if(agentNameArr.length==3){
                til_firstName.getEditText().setText(agentNameArr[0]);
                til_middleName.getEditText().setText(agentNameArr[1]);
                til_lastName.getEditText().setText(agentNameArr[2]);
            }
        }catch (Exception e){

        }


        Util.hideView(tv_done);

        tv_toolbar_title.setText("User On board");

        Bundle bundle = getArguments();
        if (bundle != null) {
            number = bundle.getString(Keys.CUSTOMER_MOBILE);
            Objects.requireNonNull(til_mob.getEditText()).setText(number);
        }
        iv_close.setOnClickListener(v -> {
            dismiss();
        });

        btn_register.setOnClickListener(v -> {
            fName = Objects.requireNonNull(til_firstName.getEditText()).getText().toString().trim();
            lName = Objects.requireNonNull(til_lastName.getEditText()).getText().toString().trim();
            mName = Objects.requireNonNull(til_middleName.getEditText()).getText().toString().trim();
            if (fName.isEmpty()) {
                L.toastS(requireActivity(), "Enter First Name");
            }/* else if (lName.isEmpty()) {
                L.toastS(requireActivity(), "Enter Last Name");
            } */else {
                register();
            }
        });

    }

    private void register() {
        if (NetworkConnection.isConnectionAvailable(requireActivity())) {
            ProgressDialogFragment pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "User On Boarding...");
            ProgressDialogFragment.showDialog(pd, getChildFragmentManager());

            UserRegisterRequest registerRequest = new UserRegisterRequest();
            registerRequest.setAgentID(Util.LoadPrefData(requireActivity(), Keys.AGENT_ID));
            registerRequest.setKey(Util.LoadPrefData(requireActivity(), Keys.TXN_KEY));
            registerRequest.setFirstname(fName);
            registerRequest.setM_name(mName);
            registerRequest.setLastname(lName);
            registerRequest.setMobile(number);

            RetrofitClient.getClient(requireActivity())
                    .activateRegister(registerRequest).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                    pd.dismiss();
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            MainResponse2 res = response.body();
                            if ("0".equalsIgnoreCase(res.getStatus())) {
                                String userCode = res.getUserCode();
                                Util.SavePrefData(requireActivity(),Keys.USER_CODE,userCode);
                                UserSuccessDialog.showDialog(requireActivity().getSupportFragmentManager());
                                dismiss();
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

    public interface RegisterListener {
        void onRegister();
    }

    public static UserRegistrationDialog newInstance(String number) {
        Bundle args = new Bundle();
        UserRegistrationDialog fragment = new UserRegistrationDialog();
        args.putString(Keys.CUSTOMER_MOBILE, number);
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager, String number) {
        UserRegistrationDialog dialog = UserRegistrationDialog.newInstance(number);
        dialog.show(manager, "Show Dialog");
    }
}
