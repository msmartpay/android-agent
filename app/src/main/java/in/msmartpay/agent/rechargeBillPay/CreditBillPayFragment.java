package in.msmartpay.agent.rechargeBillPay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.dialogs.StringDialogFrag;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.CreditBillRequest;
import in.msmartpay.agent.network.model.CreditOtpResponseContainer;
import in.msmartpay.agent.network.model.MainRequest2;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.OtpRequest;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreditBillPayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditBillPayFragment extends Fragment {

    EditText mobileNumber;
    EditText customerName;
    EditText cardNumber;
    EditText et_amount;
    EditText remarks;
    EditText otp;
    Button payment;
    Button requestOtp;
    TextView tv_otpMsg;
    MaterialAutoCompleteTextView et_network;
    private ProgressDialogFragment pd;
    private Context context;
    int amount=0;
    private List<String> networkList;
    private String networkValue;
    private String refId;
    private TextInputLayout ilOtp;
    public CreditBillPayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreditBillPayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditBillPayFragment newInstance() {
        CreditBillPayFragment fragment = new CreditBillPayFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit_bill_pay, container, false);
        mobileNumber = (EditText) view.findViewById(R.id.id_credit_bill_mobile);
        customerName = (EditText) view.findViewById(R.id.id_credit_bill_name);
        cardNumber = (EditText) view.findViewById(R.id.id_credit_bill_card_number);
        et_amount = (EditText) view.findViewById(R.id.id_credit_bill_card_amount);
        remarks= (EditText) view.findViewById(R.id.id_credit_bill_card_remark);
        otp = (EditText) view.findViewById(R.id.id_credit_bill_card_otp);
        requestOtp = view.findViewById(R.id.id_balance_request_otp);
        et_network = view.findViewById(R.id.et_network);
        payment = view.findViewById(R.id.id_balance_request_submit);
        ilOtp = view.findViewById(R.id.til_credit_bill_card_otp);
        tv_otpMsg = view.findViewById(R.id.tv_otpMsg);

        Util.hideView(tv_otpMsg);
        Util.hideView(payment);
        context = getActivity();
        networkList = Arrays.asList(getResources().getStringArray(R.array.network_type));
        et_network.setOnClickListener(v -> showNetworkDialog());
        et_network.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                networkValue = networkList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        requestOtp.setOnClickListener(view1 -> {
            if(!TextUtils.isEmpty(et_amount.getText().toString().trim())){
                amount = Integer.parseInt(et_amount.getText().toString());
            }
            if(TextUtils.isEmpty(mobileNumber.getText().toString().trim())){
                Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_LONG).show();
            }else if(mobileNumber.getText().toString().length()!=10){
                Toast.makeText(context, "Please enter correct mobile number", Toast.LENGTH_LONG).show();
            }else if (TextUtils.isEmpty(customerName.getText().toString().trim())){
                Toast.makeText(context, "Please enter card holder name", Toast.LENGTH_LONG).show();
            }
            else if (TextUtils.isEmpty(cardNumber.getText().toString().trim())){
                Toast.makeText(context, "Please enter card number", Toast.LENGTH_LONG).show();
            }
            else if (TextUtils.isEmpty(et_amount.getText().toString().trim())){
                Toast.makeText(context, "Please enter amount", Toast.LENGTH_LONG).show();
            }else if (TextUtils.isEmpty(networkValue)){
                Toast.makeText(context, "Please select network type", Toast.LENGTH_LONG).show();

            } else if (amount>49000||amount==0){
                Toast.makeText(context, "Please enter correct amount", Toast.LENGTH_LONG).show();
            }
            else{
                requestOtp();
            }
        });
        payment.setOnClickListener(view12 -> {
             if (TextUtils.isEmpty(otp.getText().toString())){
                Toast.makeText(context, "Please enter OTP", Toast.LENGTH_LONG).show();
            }else{
                requestPayment();
            }
        });

        return view;
    }

    private void showNetworkDialog(){
        StringDialogFrag dialogFrag = StringDialogFrag.newInstance(networkList, model -> {
            networkValue = model;
            et_network.setText(model);
        });
        dialogFrag.show(getChildFragmentManager(), "Search Operator");
    }

    private  void  requestPayment(){
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Credit Card Payment...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
        CreditBillRequest data = new CreditBillRequest();
        data.setAmount(Integer.parseInt(et_amount.getText().toString()));
        data.setCard_number(cardNumber.getText().toString());
        data.setMobile(mobileNumber.getText().toString());
        data.setName(customerName.getText().toString());
        data.setOtp(Integer.parseInt(otp.getText().toString()));
        data.setRefid(refId);
        if(!TextUtils.isEmpty(remarks.getText().toString().trim()))
            data.setRemarks(remarks.getText().toString());
        data.setNetwork(networkValue);
        MainRequest2 request = new MainRequest2();
        request.setAgentID(Util.LoadPrefData(getActivity(), Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(getActivity(),Keys.TXN_KEY));
        request.setIpaddress(Util.getIpAddress(context));
        request.setData(data);
        RetrofitClient.getClient(getActivity())
                .requestCreditPay(request).enqueue(new Callback<MainResponse2>() {
            @Override
            public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                pd.dismiss();
                L.toastS(getActivity(), "data success " +response.body());
                if (response.isSuccessful() && response.body() != null) {
                    MainResponse2 res = response.body();
                    cardPaymentResponseDialog(res.getMessage() + "", res.getStatus());
                }else {
                    cardPaymentResponseDialog( requireActivity().getString(R.string.technical_error),"1");
                }
            }

            @Override
            public void onFailure(Call<MainResponse2> call, Throwable t) {
                pd.dismiss();
                cardPaymentResponseDialog( requireActivity().getString(R.string.technical_error),"1");

            }
        });
    }

    private void  requestOtp(){
        tv_otpMsg.setText("");
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Credit Card Payment...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
        OtpRequest request = new OtpRequest();
        request.setAmount(Integer.parseInt(et_amount.getText().toString()));
        request.setCard_number(cardNumber.getText().toString());
        request.setMobile(mobileNumber.getText().toString());
        request.setName(customerName.getText().toString());
        if(!TextUtils.isEmpty(remarks.getText().toString().trim()))
        request.setRemarks(remarks.getText().toString());
        request.setNetwork(networkValue);
        request.setAgentID(Util.LoadPrefData(getActivity(), Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(getActivity(),Keys.TXN_KEY));
        RetrofitClient.getClient(getActivity())
                .generateOtp(request).enqueue(new Callback<CreditOtpResponseContainer>() {
            @Override
            public void onResponse(Call<CreditOtpResponseContainer> call, Response<CreditOtpResponseContainer> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    CreditOtpResponseContainer res = response.body();
                    if (res.getStatus() != null && res.getStatus().equals("0")) {
                        refId = res.getData().getRefid();
                        Util.showView(payment);
                        Util.showView(tv_otpMsg);
                        tv_otpMsg.setText(res.getMessage());
                        showOtpText();
                    }else {
                        L.toastS(requireActivity(), res.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<CreditOtpResponseContainer> call, Throwable t) {
                L.toastS(getActivity(), "data failuer " + t.getLocalizedMessage());
                pd.dismiss();
            }
        });
    }

    private  void showOtpText(){
        mobileNumber.setEnabled(false);
        customerName.setEnabled(false);
        et_network.setEnabled(false);
        cardNumber.setEnabled(false);
        et_amount.setEnabled(false);
        payment.setClickable(true);
        remarks.setEnabled(false);
        ilOtp.setVisibility(View.VISIBLE);

    }
    private  void enableText(){
        mobileNumber.setEnabled(true);
        customerName.setEnabled(true);
        et_network.setEnabled(true);
        cardNumber.setEnabled(true);
        et_amount.setEnabled(true);
        remarks.setEnabled(true);
        ilOtp.setVisibility(View.GONE);
    }
    private void cardPaymentResponseDialog(String msg, String status) {
        Dialog dialog_status = new Dialog(requireActivity());
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.sk_maindialog);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);


        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        text.setText((String) msg);
        if ("0".equals(status))
            statusImage.setImageResource(R.drawable.trnsuccess);
        else
            statusImage.setImageResource(R.drawable.failed);
        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v12 -> {
            dialog_status.dismiss();
            Intent intent = new Intent();
            intent.setClass(requireActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            requireActivity().finish();
        });

        dialog_status.show();
    }
}