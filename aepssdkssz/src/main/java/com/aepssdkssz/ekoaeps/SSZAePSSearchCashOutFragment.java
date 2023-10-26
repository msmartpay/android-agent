package com.aepssdkssz.ekoaeps;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aepssdkssz.R;
import com.aepssdkssz.dialog.SSZAePSBankSearchDialogFrag;
import com.aepssdkssz.dialog.SSZAePSDeviceSearchDialogFrag;
import com.aepssdkssz.dialog.SSZAePSPrinterDialogFrag;
import com.aepssdkssz.network.SSZAePSAPIError;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.BankModel;
import com.aepssdkssz.network.model.BiometricDevice;
import com.aepssdkssz.network.model.aepstransaction.AepsRequest;
import com.aepssdkssz.network.model.aepstransaction.AepsRequestData;
import com.aepssdkssz.network.model.aepstransaction.AepsResponse;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class SSZAePSSearchCashOutFragment extends Fragment {

    //Cashout
    private TextView tv_partner_name,cv_capture_score;
    private EditText et_aadhaar, et_amount,et_customer_mobile;
    private TextView tv_select_device, tv_req_balance, tv_cash_withdraw, tv_mini_statement,tv_aadhar_pay;
    private TextView tv_select_bank;
    private LinearLayout ll_select_bank, ll_select_device,rl_amount;
    //private ImageView iv_fingerprint;

    //private RadioButton rb_morph;
    private Button fab_cashout;
    private String number, purposeOfPayment = "",source_ip="",latlong="28.6871302,77.4812148",serviceType="";
    private String biometricFormat;
    private String device_type;
    private int minAmount = 100;
    private int maxAmount = 10000;
    private StringBuilder reportData;
    private BankModel selectedBankModel;
    private BiometricDevice selectedBiometricDevice;
    private ProgressDialog progressDialog;
    private boolean isFingureprintCaptured=false;

    public static SSZAePSSearchCashOutFragment newInstance(int index) {
        SSZAePSSearchCashOutFragment fragment = new SSZAePSSearchCashOutFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        number = Utility.getData(requireActivity(), Constants.NUMBER);
        String d_type = Utility.getData(requireActivity(), Constants.DEVICE_TYPE);
        if (!d_type.isEmpty()) {
            device_type = d_type;
        }
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_aeps_search_customer_cashout_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {


        //Cashout
        cv_capture_score=view.findViewById(R.id.cv_capture_score);
        cv_capture_score.setVisibility(View.GONE);

        et_aadhaar = view.findViewById(R.id.et_aadhaar);
        et_amount = view.findViewById(R.id.et_amount);
        ll_select_device = view.findViewById(R.id.ll_select_device);
        tv_select_device = view.findViewById(R.id.tv_select_device);
        tv_select_bank = view.findViewById(R.id.tv_select_bank);
        ll_select_bank = view.findViewById(R.id.ll_select_bank);
        tv_cash_withdraw = view.findViewById(R.id.tv_cash_withdraw);
        tv_mini_statement = view.findViewById(R.id.tv_mini_statement);
        tv_req_balance = view.findViewById(R.id.tv_req_balance);
        tv_aadhar_pay = view.findViewById(R.id.tv_aadhar_pay);
        //iv_fingerprint = view.findViewById(R.id.iv_fingerprint);
        //rb_morph = view.findViewById(R.id.rb_morph);
        rl_amount = view.findViewById(R.id.rl_amount);
        et_customer_mobile= view.findViewById(R.id.et_customer_mobile);

        et_customer_mobile.requestFocus();

        fab_cashout = view.findViewById(R.id.fab_cashout);

        tv_partner_name = view.findViewById(R.id.tv_partner_name);
        tv_partner_name.setText(Utility.getData(requireActivity(), Constants.PARTNER_NAME).toUpperCase());

        purposeOfPayment=Utility.getData(requireActivity(), Constants.TRANSACTION_TYPE);

        //Set Transaction Type
        setTransactionType(purposeOfPayment);


        ll_select_device.setOnClickListener(view17 -> {
            SSZAePSDeviceSearchDialogFrag dialogFrag = SSZAePSDeviceSearchDialogFrag.newInstance(biometricDevice -> {
                tv_select_device.setText(biometricDevice.getDevice_name());
                Utility.setTextViewBG_TextColor(tv_select_device, true);
                selectedBiometricDevice = biometricDevice;
                device_type = biometricDevice.getDevice_type();
                biometricFormat = biometricDevice.getBiometric_format();
                //rb_morph.setText(biometricDevice.getDevice_type());
            });
            dialogFrag.show(requireActivity().getSupportFragmentManager(), "Search Device");
        });

        ll_select_bank.setOnClickListener(view17 -> {
            SSZAePSBankSearchDialogFrag dialogFrag = SSZAePSBankSearchDialogFrag.newInstance(bankModel -> {
                tv_select_bank.setText(bankModel.getBank());
                Utility.setTextViewBG_TextColor(tv_select_bank, true);
                selectedBankModel = bankModel;
            });
            dialogFrag.show(requireActivity().getSupportFragmentManager(), "Search Bank");

        });

        tv_cash_withdraw.setOnClickListener(view1 -> {

            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_selected_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);

            Utility.showView(rl_amount);
            purposeOfPayment = "Cash Withdrawal";
            serviceType="2";
        });

        tv_mini_statement.setOnClickListener(view12 -> {
            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_selected_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);

            Utility.hideView(rl_amount);
            purposeOfPayment = "Mini Statement";
            serviceType="4";
        });

        tv_req_balance.setOnClickListener(view13 -> {
            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_selected_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);

            Utility.hideView(rl_amount);
            purposeOfPayment = "Balance Enquiry";
            serviceType="3";
        });

        tv_aadhar_pay.setOnClickListener(view13 -> {
            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_selected_s);

            Utility.showView(rl_amount);
            purposeOfPayment = "Aadhaar Pay";
            serviceType="5";
        });

        //iv_fingerprint.setOnClickListener(view15 -> { });

        fab_cashout.setOnClickListener(view14 -> {
            Utility.loge("fab_cashout", "onclck");
            if (this.validateRequest()) {
                captureFingurePrint();

            }
        });
    }

    public void captureFingurePrint(){
        try {
            //Utility.setCaptureFingerED(iv_fingerprint, false);
            if (this.validateRequest()) {
                if (this.getActivity() != null) {
                    final Intent intent = ((SSZAePSHomeActivity) this.getActivity()).checkBiometricProvider(this.device_type, this.biometricFormat);
                    Utility.loge("Device App", Utility.getStringFromModel(intent));
                    if (intent != null) {
                        this.startActivityForResult(intent, ((SSZAePSHomeActivity) this.getActivity()).getRequestCode());
                    }
                } else {
                    //Utility.setCaptureFingerED(iv_fingerprint, true);
                }
            } else {
                //Utility.setCaptureFingerED(iv_fingerprint, true);
            }
        } catch (Exception e) {
            Utility.showMessageDialogue((Context) this.getActivity(), "EXCEPTION- " + e.getMessage(), "EXCEPTION");
            //Utility.setCaptureFingerED(iv_fingerprint, true);
        }
    }

    public void submitRequest() {
        if (Utility.checkConnection(requireActivity())) {

            try {
                progressDialog = Utility.getProgressDialog(requireActivity());
                progressDialog.show();

                JSONObject jsonObjPidData = ((SSZAePSHomeActivity) requireActivity()).getPieJsonData();
                JSONObject jsonBody = ((SSZAePSHomeActivity) requireActivity()).getTransactionInfo();
                if (jsonObjPidData != null) {
                    final JSONObject jsonPid = jsonObjPidData.getJSONObject("PidData");
                    final JSONObject jsonResp = jsonPid.getJSONObject("Resp");


                    final String errCode = jsonResp.getString("errCode");
                    if (errCode.equals("0")) {
                        final JSONObject jsonDeviceInfo = jsonPid.getJSONObject("DeviceInfo");
                        if (jsonBody == null) {
                            jsonBody = new JSONObject();
                        }

                        String deviceSerno = "";
                        if (jsonDeviceInfo.has("additional_info")) {
                            final JSONObject jsonAdditInfo = jsonDeviceInfo.getJSONObject("additional_info");
                            final JSONArray jsonArray = jsonAdditInfo.getJSONArray("Param");
                            for (int i = 0; i < jsonArray.length(); ++i) {
                                final JSONObject jsonSRno = jsonArray.getJSONObject(i);
                                final String name = jsonSRno.getString("name");
                                if (name.equalsIgnoreCase("srno")) {
                                    deviceSerno = jsonSRno.getString("value");
                                    break;
                                }
                            }
                        } else {
                            deviceSerno = ((SSZAePSHomeActivity) requireActivity()).getMorphoDeviceSerno();
                        }
                        //jsonBody.put("deviceserno", (Object) Utility.encodeBase64(deviceSerno));
                        //jsonBody.put("biodata", (Object) Utility.encodeBase64(jsonObjPidData.toString()));
                        //jsonBody.put("userdevice", (Object) "MOBILE");


                        AepsRequest request = Utility.getGson().fromJson(jsonBody.toString(), AepsRequest.class);
                        AepsRequestData data=new AepsRequestData();
                        data.setSource_ip(Utility.getData(requireActivity(),Constants.SOURCE_IP));//Need to initialize source ip
                        data.setLatlong(Utility.getData(requireActivity(),Constants.LAT_LONG));//Need to initialize source latlong
                        data.setType(serviceType);
                        data.setXmlData(jsonObjPidData.getString("xmlData"));
                        data.setBank_iin(selectedBankModel.getBank_iin());
                        data.setDevice_type(device_type);
                        data.setCustomer_id(et_customer_mobile.getText().toString());
                        data.setBc_aadhaar(this.et_aadhaar.getText().toString());

                        //For displaying on receipt
                        Utility.saveData(requireActivity(),Constants.BANK_NAME,selectedBankModel.getBank());

                        String amount="0";
                        if("2".equalsIgnoreCase(serviceType) || "5".equalsIgnoreCase(serviceType))
                            amount=et_amount.getText().toString();
                        else
                            amount="0";

                        data.setAmount(amount);
                        request.setData(data);

                        SSZAePSRetrofitClient.getClient(requireActivity())
                                .transaction(request)
                                .enqueue(new Callback<AepsResponse>() {
                                    @Override
                                    public void onResponse(@NotNull Call<AepsResponse> call, @NotNull Response<AepsResponse> response) {
                                        progressDialog.dismiss();
                                        try {
                                            if (response.isSuccessful() && response.body() != null) {
                                                AepsResponse res = response.body();
                                                if (res.getStatus() != 1) {
                                                    SSZAePSPrinterDialogFrag printerDialogFrag = SSZAePSPrinterDialogFrag.newInstance(Utility.getStringFromModel(res), () -> {
                                                        freshData();
                                                    });
                                                    printerDialogFrag.show(requireActivity().getSupportFragmentManager(), "Show Status");
                                                } else {
                                                    Utility.showMessageDialogue(requireActivity(), "" + res.getMessage(), "Submit Request");
                                                }
                                            } else {
                                                SSZAePSAPIError error = SSZAePSAPIError.parseError(response, requireActivity());
                                                Utility.showMessageDialogue(requireActivity(), error.message(), "Submit Request");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utility.showMessageDialogue(requireActivity(), "" + response.message(), "Submit Request");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<AepsResponse> call, @NotNull Throwable t) {
                                        progressDialog.dismiss();
                                        Utility.showMessageDialogue(requireActivity(), t.getMessage(), "Submit Request");
                                        Utility.loge("validate User", t.getMessage());
                                    }
                                });

                    } else {
                        Utility.showMessageDialogue(requireActivity(), jsonResp.toString(), "Submit Request");
                    }
                } else {
                    Utility.toast(requireActivity(), "Capture Data Not Found.");
                }
            } catch (JSONException e) {
                this.reportData.append("Excep message: ");
                this.reportData.append(e.getMessage());
                this.reportData.append(", Cause: ");
                this.reportData.append(e.getMessage());
                Utility.sendReportEmail(requireActivity(), "submitRequest()", this.reportData.toString());
                this.reportData.setLength(0);
                e.printStackTrace();
                Utility.toast(requireActivity(), "JSON Error");

                progressDialog.dismiss();
            }

        }
    }

    private JSONObject mergeJSONObjects(final JSONObject json1, final JSONObject json2) {
        try {
            final Iterator iterator = json1.keys();
            while (iterator.hasNext()) {
                final String key = iterator.next().toString();
                json2.put(key, json1.get(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException("JSON Exception" + e);
        }
        return json2;
    }

    private boolean validateRequest() {
        boolean flag = true;
        final String aadhaarNo = this.et_aadhaar.getText().toString().trim();
        if (this.et_customer_mobile.isShown()) {
            final String mobileNo = this.et_customer_mobile.getText().toString().trim();
            if (TextUtils.isEmpty((CharSequence) mobileNo)) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.enter_customer_mobile));
                this.et_customer_mobile.requestFocus();
                flag = false;
            } else if (mobileNo.length() < 10) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.ten_digit_mobile));
                this.et_customer_mobile.requestFocus();
                flag = false;
            }
        }
        if (flag) {
            if (aadhaarNo.isEmpty()) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.enter_aadhaar_number));
                et_aadhaar.requestFocus();
                flag = false;
            } else if (Double.parseDouble(aadhaarNo) <= 0.0 || aadhaarNo.substring(0, 1).equals("0")
                    || aadhaarNo.substring(0, 1).equals("1")) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.enter_valid_adhar));
                et_aadhaar.requestFocus();
                flag = false;
            } else if (aadhaarNo.length() < 12) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.twenter_adhar));
                et_aadhaar.requestFocus();
                flag = false;
            }
            /*else if (VerhoeffAlgorithm.validateVerhoeff(aadhaarNo)) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.enter_valid_adhar));
                 et_aadhaar.requestFocus();
                flag = false;
            }*/
            else if (this.tv_select_device.getText().toString().trim().equals(this.getResources().getString(R.string.payment_sdus_device))) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.select_device_provider));
                flag = false;
            } else if (this.tv_select_bank.getText().toString().trim().equals(this.getResources().getString(R.string.payment_sdus_bank))) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.select_bank_option));
                flag = false;
            }
        }
        if (flag && this.et_amount.isShown()) {
            final String amount = this.et_amount.getText().toString().trim();
            if (TextUtils.isEmpty((CharSequence) amount)) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.enter_amount));
                this.et_amount.requestFocus();
                flag = false;
            } else if (Integer.parseInt(amount) <= 0) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.enter_valid_amount));
                this.et_amount.requestFocus();
                flag = false;
            } else if (Integer.parseInt(amount) > this.maxAmount) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.enter_less_amount));
                this.et_amount.requestFocus();
                flag = false;
            } else if (Integer.parseInt(amount) < this.minAmount) {
                Utility.toast(requireActivity(), this.getResources().getString(R.string.enter_more_amount));
                this.et_amount.requestFocus();
                flag = false;
            }
        }
        /*if (flag) {
            if (this.providerList.size() > 0 && this.device_type.isEmpty()) {
                Toast.makeText((Context)this.getActivity(), (CharSequence)this.getResources().getString(R.string.select_device_provider), Toast.LENGTH_SHORT).show();
                this.spnDeviceType.requestFocus();
                flag = false;
            }
            else if (this.cbTncCustomer.getVisibility() == View.VISIBLE && !this.cbTncCustomer.isChecked()) {
                Toast.makeText((Context)this.getActivity(), (CharSequence)this.getResources().getString(R.string.error_aeps_customer_tnc), Toast.LENGTH_SHORT).show();
                flag = false;
            }
            else if (this.cbTncAgent.getVisibility() == View.VISIBLE && !this.cbTncAgent.isChecked()) {
                Toast.makeText((Context)this.getActivity(), (CharSequence)this.getResources().getString(R.string.error_aeps_agent_tnc), Toast.LENGTH_SHORT).show();
                flag = false;
            }
        }*/
        return flag;
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        //Utility.setCaptureFingerED(iv_fingerprint, true);

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText((Context) this.getActivity(), (CharSequence) "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {

                    final JSONObject jsonObjData = XML.toJSONObject(result.getContents());
                    final JSONObject json = jsonObjData.getJSONObject("PrintLetterBarcodeData");
                    final String uid = json.getString("uid");
                    //this.etAadhaarNo.getEditText().setText((CharSequence)uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText((Context) this.getActivity(), (CharSequence) "Invalid QR Code", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1000) {
                if (resultCode == -1) {
                    // this.fragmentBecameVisible();
                }
            } else if (requestCode == 1100) {
                if (this.getActivity() != null) {
                    final Intent intent = ((SSZAePSHomeActivity) this.getActivity()).verifyActivityResult(requestCode, data, this.biometricFormat);
                    if (intent != null) {
                        this.startActivityForResult(intent, ((SSZAePSHomeActivity) this.getActivity()).getRequestCode());
                    } else {
                        Utility.toast(requireActivity(), "Some error occurred!");
                        //Utility.setCaptureFingerED(iv_fingerprint, true);
                    }
                }
            } else if (requestCode == 1200 || requestCode == 1500 || requestCode == 3500 || requestCode == 4500 || requestCode == 5500 || requestCode == 1300) {
                final String pidData = data.getStringExtra("PID_DATA");
                if (pidData != null) {

                    try {
                        final JSONObject jsonObjPidData = XML.toJSONObject(pidData);

                         jsonObjPidData.put("xmlData",pidData);
                        final JSONObject jsonPid = jsonObjPidData.getJSONObject("PidData");
                        final JSONObject jsonResp = jsonPid.getJSONObject("Resp");

                        cv_capture_score.setText(jsonResp.optString("errInfo")+" Score is "+jsonResp.optString("qScore")+"%");
                        cv_capture_score.setVisibility(View.VISIBLE);
                        final String errCode = jsonResp.getString("errCode");
                        if (this.getActivity() != null) {
                            ((SSZAePSHomeActivity) this.getActivity()).updatePidJson(jsonObjPidData);
                        }
                        if (errCode.equals("0")) {

                            submitRequest();

                        } else if (jsonResp.has("errInfo")) {
                            final String errInfo = jsonResp.getString("errInfo");
                            if (!TextUtils.isEmpty((CharSequence) errInfo)) {
                                Utility.toast(requireActivity(), errInfo);
                            }
                        }
                    } catch (JSONException e2) {
                        this.reportData.append("Excep message: ");
                        this.reportData.append(e2.getMessage());
                        this.reportData.append(", Cause: ");
                        this.reportData.append(e2.getMessage());
                        Utility.sendReportEmail((Context) this.getActivity(), "onActivityResultCW()", this.reportData.toString());
                        this.reportData.setLength(0);
                        e2.printStackTrace();
                    }
                } else {
                    Utility.showMessageDialogue((Context) this.getActivity(), "Empty Response!", "PID DATA XML");
                }
            } else if (resultCode == 0) {
                Utility.showMessageDialogue((Context) this.getActivity(), "Scan Failed/Aborted!", "CAPTURE RESULT");
                //Utility.setCaptureFingerED(iv_fingerprint, true);
            } else {
                Utility.showMessageDialogue((Context) this.getActivity(), "Please Connect Device", "RESULT");
                //Utility.setCaptureFingerED(iv_fingerprint, true);
            }
            final View view = this.requireActivity().getCurrentFocus();
            hideKeyboard(view);
        }
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) this.requireActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void freshData() {
        cv_capture_score.setText("");
        cv_capture_score.setVisibility(View.INVISIBLE);
        et_aadhaar.setText("");
        et_customer_mobile.setText("");
        et_amount.setText("");
        tv_select_bank.setText("");
        Utility.setTextViewBG_TextColor(tv_select_bank, false);
        tv_select_device.setText("");
        Utility.setTextViewBG_TextColor(tv_select_device, false);

        ((SSZAePSHomeActivity) requireActivity()).updatePidJson(null);
        //Utility.setCaptureFingerED(iv_fingerprint, false);
    }

    private void setTransactionType(String purposeOfPayment){
        if(Constants.CASH_WITHDRAWAL.equalsIgnoreCase(purposeOfPayment)){

            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_selected_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);

            Utility.showView(rl_amount);
            serviceType="2";
        }else if(Constants.MINI_STATEMENT.equalsIgnoreCase(purposeOfPayment)){
            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_selected_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);

            Utility.hideView(rl_amount);
            serviceType="4";
        }else if(Constants.BALANCE_ENQUIRY.equalsIgnoreCase(purposeOfPayment)){
            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_selected_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);

            Utility.hideView(rl_amount);
            serviceType="3";
        }else if(Constants.AADHAAR_PAY.equalsIgnoreCase(purposeOfPayment)){
            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_selected_s);

            Utility.showView(rl_amount);
            serviceType="5";
        }else{
            tv_cash_withdraw.setBackgroundResource(R.drawable.ssz_cash_withdrawal_selected_s);
            tv_mini_statement.setBackgroundResource(R.drawable.ssz_mini_statement_s);
            tv_req_balance.setBackgroundResource(R.drawable.ssz_balance_inquiry_s);
            tv_aadhar_pay.setBackgroundResource(R.drawable.ssz_cash_withdrawal_s);

            Utility.showView(rl_amount);
            serviceType="2";
        }
    }
}