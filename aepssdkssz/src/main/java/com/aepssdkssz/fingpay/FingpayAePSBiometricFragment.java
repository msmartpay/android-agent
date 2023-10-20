package com.aepssdkssz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aepssdkssz.dialog.SSZAePSBankSearchDialogFrag;
import com.aepssdkssz.dialog.SSZAePSDeviceSearchDialogFrag;
import com.aepssdkssz.dialog.SSZAePSPrinterDialogFrag;
import com.aepssdkssz.dialog.fingpayonboarding.SDKUserNumberDialog;
import com.aepssdkssz.network.SSZAePSAPIError;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.BankModel;
import com.aepssdkssz.network.model.BiometricDevice;
import com.aepssdkssz.network.model.aepstransaction.AepsRequest;
import com.aepssdkssz.network.model.aepstransaction.AepsRequestData;
import com.aepssdkssz.network.model.aepstransaction.AepsResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayOnboardResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayUserRequest;
import com.aepssdkssz.network.model.fingpayonboard.FingpayUserRequestData;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.DialogProgressFragment;
import com.aepssdkssz.util.Utility;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A placeholder fragment containing a simple view.
 */
public class FingpayAePSBiometricFragment extends Fragment {



    private RadioButton rb_morph;
    private Button kyc_btn_send;
    private String source_ip="",xmlData="";
    private String biometricFormat;
    private String device_type;
    private StringBuilder reportData;
    private BiometricDevice selectedBiometricDevice;
    private ProgressDialog progressDialog;
    private ImageView ssz_iv_fingerprint;
    private List<String> modelList;
    private TextView cv_capture_score;
    private String aadhar_number, device_number, encodeFPTxnId, primaryKeyId;
    private SmartMaterialSpinner ssz_sp_model_type;
    private RadioButton ssz_rb_morph;

    public static FingpayAePSBiometricFragment newInstance(int index) {
        FingpayAePSBiometricFragment fragment = new FingpayAePSBiometricFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fingpay_aeps_biometric_kyc_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {

        ssz_iv_fingerprint= view.findViewById(R.id.ssz_iv_fingerprint);
        cv_capture_score = view.findViewById(R.id.ssz_cv_capture_score);
        kyc_btn_send = view.findViewById(R.id.kyc_btn_send);
        ssz_sp_model_type = view.findViewById(R.id.ssz_sp_model_type);
        ssz_rb_morph = view.findViewById(R.id.ssz_rb_morph);

        modelList = Arrays.asList(getResources().getStringArray(R.array.ssz_model_type));
        ssz_sp_model_type.setItem(modelList);

        ssz_sp_model_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>-1){
                    device_type = modelList.get(position);
                    ssz_rb_morph.setText(device_type);
                }else {
                    device_type="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        kyc_btn_send.setOnClickListener(v -> {
            if(device_type==null || "".equalsIgnoreCase(device_type)){
                Utility.toast(requireActivity(),"Select Device Type");
            }else {

                aadhar_number=Utility.getData(requireActivity(),Constants.AADHAR_NUMBER);
                device_number=Utility.getData(requireActivity(),Constants.DEVICE_NUMBER);
                encodeFPTxnId=Utility.getData(requireActivity(),Constants.ENCODED_FPTxnId);
                primaryKeyId=Utility.getData(requireActivity(),Constants.PRIMARY_KeyId);

                captureFingurePrint();
            }
        });
    }

    public void captureFingurePrint(){
        try {
            Utility.setCaptureFingerED(ssz_iv_fingerprint, false);

                if (this.getActivity() != null) {
                    final Intent intent = ((FingpayAePSHomeActivity) this.getActivity()).checkBiometricProvider(this.device_type, this.biometricFormat,Constants.WADH);
                    Utility.loge("Device App", Utility.getStringFromModel(intent));
                    if (intent != null) {
                        this.startActivityForResult(intent, ((FingpayAePSHomeActivity) this.getActivity()).getRequestCode());
                    }
                } else {
                    Utility.setCaptureFingerED(ssz_iv_fingerprint, true);
                }

        } catch (Exception e) {
            Utility.showMessageDialogue((Context) this.getActivity(), "EXCEPTION- " + e.getMessage(), "EXCEPTION");
            Utility.setCaptureFingerED(ssz_iv_fingerprint, true);
        }
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Utility.setCaptureFingerED(ssz_iv_fingerprint, true);

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
                    final Intent intent = ((FingpayAePSHomeActivity) this.getActivity()).verifyActivityResult(requestCode, data, this.biometricFormat);
                    if (intent != null) {
                        this.startActivityForResult(intent, ((FingpayAePSHomeActivity) this.getActivity()).getRequestCode());
                    } else {
                        Utility.toast(requireActivity(), "Some error occurred!");
                        Utility.setCaptureFingerED(ssz_iv_fingerprint, true);
                    }
                }
            } else if (requestCode == 1200 || requestCode == 1500 || requestCode == 3500 || requestCode == 4500 || requestCode == 5500 || requestCode == 1300) {
                final String pidData = data.getStringExtra("PID_DATA");
                if (pidData != null) {

                    try {
                        final JSONObject jsonObjPidData = XML.toJSONObject(pidData);
                        xmlData=pidData;
                        jsonObjPidData.put("xmlData",pidData);
                        final JSONObject jsonPid = jsonObjPidData.getJSONObject("PidData");
                        final JSONObject jsonResp = jsonPid.getJSONObject("Resp");

                        cv_capture_score.setText(jsonResp.optString("errInfo")+" Score is "+jsonResp.optString("qScore")+"%");
                        cv_capture_score.setVisibility(View.VISIBLE);
                        final String errCode = jsonResp.getString("errCode");
                        if (this.getActivity() != null) {
                            ((FingpayAePSHomeActivity) this.getActivity()).updatePidJson(jsonObjPidData);
                        }
                        if (errCode.equals("0")) {

                            doBiometricKyc();

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
                Utility.setCaptureFingerED(ssz_iv_fingerprint, true);
            } else {
                Utility.showMessageDialogue((Context) this.getActivity(), "Please Connect Device", "RESULT");
                Utility.setCaptureFingerED(ssz_iv_fingerprint, true);
            }
            final View view = this.requireActivity().getCurrentFocus();
            hideKeyboard(view);
        }
    }

    private void doBiometricKyc() {
        if (Utility.checkConnection(requireActivity())) {
            DialogProgressFragment pd = DialogProgressFragment.newInstance("Loading. Please wait...", "Validating KYC...");
            DialogProgressFragment.showDialog(pd, getChildFragmentManager());
            FingpayUserRequest req = new FingpayUserRequest();
            req.setKey(Utility.getData(requireActivity(), Constants.TOKEN));
            req.setAgentID(Utility.getData(requireActivity(), Constants.MERCHANT_ID));

            FingpayUserRequestData data=new FingpayUserRequestData();
            data.setEncodeFPTxnId(encodeFPTxnId);
            data.setPrimaryKeyId(primaryKeyId);
            data.setAadhaarNumber(aadhar_number);
            data.setDeviceIMEI(device_number);
            data.setXmlData(xmlData);
            req.setData(data);

            SSZAePSRetrofitClient.getClient(requireActivity())
                    .fingpayBiometricKyc(req).enqueue(new Callback<FingpayOnboardResponse>() {
                @Override
                public void onResponse(Call<FingpayOnboardResponse> call, Response<FingpayOnboardResponse> response) {
                    try {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            FingpayOnboardResponse res = response.body();
                            if ("0".equalsIgnoreCase(res.getStatus())) {

                                showConfirmationDialog(res.getMessage(),res.getStatus());
                            } else {
                                SDKUserNumberDialog.showDialog(requireActivity().getSupportFragmentManager(),aadhar_number,"",res.getMessage());
                                Utility.toast(requireActivity(), res.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());
                        Utility.loge("Parser Error", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call<FingpayOnboardResponse> call, Throwable t) {
                    Utility.toast(requireActivity(), "Error : " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    //Confirmation Dialog
    private void showConfirmationDialog(String msg,String statusCode) {
        // TODO Auto-generated method stub
        @SuppressLint("PrivateResource") final Dialog d = new Dialog(requireActivity(), R.style.AppCompatAlertDialogStyle);
        d.setCancelable(false);
        Objects.requireNonNull(d.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.ssz_aeps_confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit = d.findViewById(R.id.btn_push_submit);
        final Button btnClosed = d.findViewById(R.id.close_push_button);
        //Utility.hideView(btnClosed);
        final TextView tvConfirmation = d.findViewById(R.id.tv_confirmation_dialog);
        tvConfirmation.setText(msg);

        btnSubmit.setOnClickListener(v -> {
            d.cancel();

            Intent intent = new Intent();
            intent.putExtra("Message",msg);
            intent.putExtra("StatusCode",statusCode);
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();

        });
        btnClosed.setOnClickListener(v -> {
            d.cancel();

            Intent intent = new Intent();
            intent.putExtra("Message",msg);
            intent.putExtra("StatusCode",statusCode);
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();
        });
        d.show();
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) this.requireActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}