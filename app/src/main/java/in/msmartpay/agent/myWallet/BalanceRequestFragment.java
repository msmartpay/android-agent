package in.msmartpay.agent.myWallet;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import in.msmartpay.agent.R;
import in.msmartpay.agent.collectBanks.CollectBankModel;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.BankCollectResponse;
import in.msmartpay.agent.network.model.MainRequest;
import in.msmartpay.agent.network.model.MainResponse;
import in.msmartpay.agent.network.model.wallet.BalRequest;
import in.msmartpay.agent.utility.BaseFragment;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceRequestFragment extends BaseFragment {

    private SmartMaterialSpinner sp_bank, sp_mode_type, sp_wallet_type;
    private EditText damount, b_refid, remarks, fromDateEtxt;
    private Button brequest;
    private ProgressDialogFragment pd;
    private DatePickerDialog fromDatePickerDialog;
    private String agentID, selectedValue1, selectedValue2, txn_key = "", walletTypeVal = "";

    private Context context;

    private List<CollectBankModel> bankList;
    private List<String> modeTypeList;
    private List<String> walletTypeList;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_request, container, false);

        context = getActivity();
        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);

        remarks = (EditText) view.findViewById(R.id.id_balance_request_remarks);
        damount = (EditText) view.findViewById(R.id.id_balance_request_amount);
        b_refid = (EditText) view.findViewById(R.id.id_balance_request_refId);
        fromDateEtxt = (EditText) view.findViewById(R.id.id_balance_request_ddate);
        sp_bank =view.findViewById(R.id.sp_bank);
        sp_mode_type = view.findViewById(R.id.sp_mode_type);
        sp_wallet_type = view.findViewById(R.id.sp_wallet_type);
        brequest = (Button) view.findViewById(R.id.id_balance_request_submit);


        modeTypeList = Arrays.asList(getResources().getStringArray(R.array.payment_type));
        walletTypeList = Arrays.asList(getResources().getStringArray(R.array.wallet_type));

        sp_mode_type.setItem(modeTypeList);
        sp_wallet_type.setItem(walletTypeList);


        //Set Bank list
        getBankDetails();

        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.setOnClickListener(view12 -> setDateTimeField());

        sp_mode_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedValue2 =modeTypeList.get(position);
                if (selectedValue2.equalsIgnoreCase("cash")) {
                    b_refid.setVisibility(View.GONE);
                } else {
                    b_refid.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_wallet_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if (position > -1) {
                    walletTypeVal = walletTypeList.get(position);
                    if (walletTypeVal.equalsIgnoreCase("B2B WALLET"))
                        walletTypeVal = "Main Wallet";

                } else {
                    walletTypeVal = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if (position > -1) {
                    selectedValue1 = bankList.get(position).getBank_name();
                } else {
                    selectedValue1 = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        brequest.setOnClickListener(view1 -> {
            if (isConnectionAvailable()) {
                if (selectedValue1 == null) {
                    Toast.makeText(context, "Select Bank Name !!!", Toast.LENGTH_SHORT).show();
                } else if (selectedValue2 == null) {
                    Toast.makeText(context, "Select Payment Type !!!", Toast.LENGTH_SHORT).show();
                } else if (walletTypeVal == null) {
                    Toast.makeText(context, "Select Wallet Type !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(damount.getText().toString().trim())) {
                    damount.requestFocus();
                    Toast.makeText(context, "Enter Deposite Amount !!!", Toast.LENGTH_SHORT).show();
                } else if (!selectedValue2.equalsIgnoreCase("cash") && (b_refid.getText().toString() != null && b_refid.getText().toString().length() <= 0)) {
                    b_refid.requestFocus();
                    Toast.makeText(context, "Enter Transaction Reference Id", Toast.LENGTH_LONG).show();
                } else if (fromDateEtxt.getText().toString().trim().length() <= 0) {
                    Toast.makeText(context, "Select Deposit Date. ", Toast.LENGTH_SHORT).show();
                } else {
                    balanceReqFragmentRequest();
                }
            } else {
                Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void balanceReqFragmentRequest() {

        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Balance Request...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());
        BalRequest request = new BalRequest();
        request.setAgent_id(agentID);
        request.setTxn_key(txn_key);
        request.setMode(selectedValue2);
        request.setBankName(selectedValue1);
        request.setDepositDate(fromDateEtxt.getText().toString().trim());
        request.setRefId(b_refid.getText().toString().trim());
        request.setAmount(damount.getText().toString().trim());
        request.setRemark(remarks.getText().toString().trim());
        request.setWalletType(walletTypeVal);

        RetrofitClient.getClient(getActivity())
                .getWalletBalReq(request).enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NotNull Call<MainResponse> call, @NotNull retrofit2.Response<MainResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    MainResponse res = response.body();
                    if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                        new AlertDialog.Builder(context)
                                .setTitle("Status")
                                .setIcon(R.drawable.trnsuccess)
                                .setMessage(res.getResponseMessage() + "")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    requireActivity().finish();
                                })
                                .show();
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Status")
                                .setIcon(R.drawable.failed)
                                .setMessage(res.getResponseMessage() + "")
                                .setPositiveButton("Ok", (dialog, which) -> {
                                    requireActivity().finish();
                                }).show();
                        L.toastS(getActivity(), res.getResponseMessage());
                    }
                }else{
                    L.toastS(getActivity(), "Server Error");
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponse> call, @NotNull Throwable t) {
                L.toastS(getActivity(), "data failuer " + t.getLocalizedMessage());
                pd.dismiss();
            }
        });
    }

    private void getBankDetails() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Bank Details...");
        ProgressDialogFragment.showDialog(pd, getChildFragmentManager());

        MainRequest request = new MainRequest();
        request.setAgentID(agentID);
        request.setTxn_key(txn_key);

        RetrofitClient.getClient(getActivity())
                .getBankDetails(request).enqueue(new Callback<BankCollectResponse>() {
            @Override
            public void onResponse(@NotNull Call<BankCollectResponse> call, @NotNull retrofit2.Response<BankCollectResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    BankCollectResponse res = response.body();
                    if ("0".equalsIgnoreCase(res.getResponseCode())) {
                        if (bankList == null)
                            bankList = new ArrayList<>();
                        else
                            bankList.clear();
                        if (res.getBank_List()!=null)
                        bankList = res.getBank_List();
                        sp_bank.setItem(bankList);
                    } else {
                        L.toastS(getActivity(), res.getResponseMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BankCollectResponse> call, @NotNull Throwable t) {
                L.toastS(getActivity(), "data failuer " + t.getLocalizedMessage());
                pd.dismiss();
            }
        });

    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            fromDateEtxt.setText(Util.getDate(year, monthOfYear, dayOfMonth));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.show();
    }


}
