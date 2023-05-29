package in.msmartpay.agent.rechargeBillPay;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.CreditRefundOtpRequest;
import in.msmartpay.agent.network.model.CreditRefundRequest;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreditRefundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditRefundFragment extends Fragment {
private EditText et_tid;
private  EditText et_otp;
private Button btn_otp;
private  Button btn_refund;
private Context context;
private ProgressDialogFragment pd;
private TextInputLayout ilOtp;

    public CreditRefundFragment() {
        // Required empty public constructor
    }
    public static CreditRefundFragment newInstance() {
        CreditRefundFragment fragment = new CreditRefundFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_refund, container, false);
        et_otp = view.findViewById(R.id.id_credit_refund_otp);
        et_tid = view.findViewById(R.id.id_credit_refund_tid);
        btn_otp = view.findViewById(R.id.refund_request_otp);
        btn_refund = view.findViewById(R.id.refund_request);
        ilOtp = view.findViewById(R.id.til_credit_refund_otp);
       context = getActivity();

        btn_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(et_tid.getText().toString().trim())){
                    Toast.makeText(context, "Please enter service delivery TID", Toast.LENGTH_LONG).show();
                } if(TextUtils.isEmpty(et_otp.getText().toString().trim())){
                    Toast.makeText(context, "Please enter otp", Toast.LENGTH_LONG).show();
                }else{
                        requestRefund();
                }
            }
        });

        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(et_tid.getText().toString().trim())){
                    Toast.makeText(context, "Please enter service delivery TID", Toast.LENGTH_LONG).show();
                }else {
                    getOtp();
                }
            }
        });
        btn_refund.setClickable(false);
        return view;
    }
    private void  getOtp(){
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Credit Card Refund...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
        CreditRefundOtpRequest request = new CreditRefundOtpRequest();
        request.setAgentID(Util.LoadPrefData(getActivity(), Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(getActivity(),Keys.TXN_KEY));
        request.setTransactionRefNo(et_tid.getText().toString().trim());
        RetrofitClient.getClient(getActivity())
                .requestRefundOtp(request).enqueue(new Callback<MainResponse2>() {
            @Override
            public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    MainResponse2 res = response.body();
                    if (res.getStatus() != null && res.getStatus().equals("0")) {
                        showOtpText();
                        new AlertDialog.Builder(context)
                                .setTitle("Status")
                                .setIcon(R.drawable.trnsuccess)
                                .setMessage(res.getMessage() + "")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    requireActivity().finish();
                                })
                                .show();
                    }else {
                        new AlertDialog.Builder(context)
                                .setTitle("Status")
                                .setIcon(R.drawable.failed)
                                .setMessage("Failed")
                                .setPositiveButton("Ok", (dialog, which) -> {
                                    requireActivity().finish();
                                }).show();
                    }}
            }

            @Override
            public void onFailure(Call<MainResponse2> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void  requestRefund(){
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Credit Card Refund...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
        CreditRefundRequest request = new CreditRefundRequest();
        request.setAgentID(Util.LoadPrefData(getActivity(), Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(getActivity(),Keys.TXN_KEY));
        request.setTransactionRefNo(et_tid.getText().toString().trim());
        request.setOTP(et_otp.getText().toString().trim());
        RetrofitClient.getClient(getActivity())
                .requestRefund(request).enqueue(new Callback<MainResponse2>() {
            @Override
            public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<MainResponse2> call, Throwable t) {
                pd.dismiss();
            }
        });
    }
    private void showOtpText(){
        ilOtp.setVisibility(View.VISIBLE);
        et_tid.setEnabled(false);
        btn_refund.setClickable(true);

    }
}