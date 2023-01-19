package com.aepssdkssz.dialog.fingpayonboarding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.aepssdkssz.FingpayAePSHomeActivity;
import com.aepssdkssz.R;
import com.aepssdkssz.SSZAePSHomeActivity;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.fingpayonboard.FingpayOnboardResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayUserRequest;
import com.aepssdkssz.network.model.fingpayonboard.FingpayUserRequestData;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.DialogProgressFragment;
import com.aepssdkssz.util.Utility;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class SDKUserBiometricDialog extends DialogFragment {

    private String xmlData = "",aadhar_number="", device_number="",encodeFPTxnId="",primaryKeyId="",deviceName="";
    private Context context;
    private DialogProgressFragment pd;
    private UserBiometricKycListener listener;
    private boolean isGenerateOtp;
    private SmartMaterialSpinner ssz_sp_model_type;
    private List<String> modelList;
    private ImageView ssz_iv_fingerprint;
    private TextView ssz_cv_capture_score;
    private StringBuilder reportData;
    private RadioButton ssz_rb_morph;
    private Button kyc_btn_send;

    public void setListener(UserBiometricKycListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ssz_fingpay_biometric_kyc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        Utility.hideView(tv_done);
        tv_toolbar_title.setText("Biometric KYC");

        kyc_btn_send = view.findViewById(R.id.kyc_btn_send);
        TextInputLayout til_aadhar_number = view.findViewById(R.id.til_aadhar_number);
        TextInputLayout til_device_number = view.findViewById(R.id.til_device_number);
        ssz_sp_model_type = view.findViewById(R.id.ssz_sp_model_type);
        ssz_iv_fingerprint= view.findViewById(R.id.ssz_iv_fingerprint);
        ssz_cv_capture_score = view.findViewById(R.id.ssz_cv_capture_score);
        ssz_rb_morph=view.findViewById(R.id.ssz_rb_morph);


        Bundle bundle = getArguments();
        if (bundle != null) {
            aadhar_number=bundle.getString(Constants.AADHAR_NUMBER);
            device_number=bundle.getString(Constants.DEVICE_NUMBER);
            primaryKeyId=bundle.getString(Constants.PRIMARY_KeyId);
            encodeFPTxnId=bundle.getString(Constants.ENCODED_FPTxnId);

        }

        modelList = Arrays.asList(getResources().getStringArray(R.array.ssz_model_type));
        ssz_sp_model_type.setItem(modelList);

        ssz_sp_model_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>-1){
                    deviceName = modelList.get(position);
                    ssz_rb_morph.setText(deviceName);
                }else {
                    deviceName="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_close.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent();
            intent.putExtra("Message","Closed by user");
            intent.putExtra("StatusCode","001");
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();

        });

        kyc_btn_send.setOnClickListener(v -> {
            captureFingurePrint();
        });
    }

    public interface UserBiometricKycListener {
        void doBiometricKyc();
    }

    public static SDKUserBiometricDialog newInstance(String aadhar_number, String device_number,String encodeFPTxnId,String primaryKeyId) {
        Bundle args = new Bundle();
        SDKUserBiometricDialog fragment = new SDKUserBiometricDialog();

        args.putString(Constants.AADHAR_NUMBER, aadhar_number);
        args.putString(Constants.DEVICE_NUMBER, device_number);
        args.putString(Constants.ENCODED_FPTxnId, encodeFPTxnId);
        args.putString(Constants.PRIMARY_KeyId, primaryKeyId);
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager, String aadhar_number,String device_number,String encodeFPTxnId,String primaryKeyId) {
        SDKUserBiometricDialog dialog = SDKUserBiometricDialog.newInstance(aadhar_number,device_number,encodeFPTxnId,primaryKeyId);
        dialog.show(manager, "Show Dialog");
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
                                //showConfirmationDialog(res.getMessage(),res.getStatus());
                                kyc_btn_send.setText("Re-Try KYC");
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
            dismiss();

            Intent intent = new Intent();
            intent.putExtra("Message",msg);
            intent.putExtra("StatusCode",statusCode);
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();

        });
        btnClosed.setOnClickListener(v -> {
            d.cancel();
            dismiss();

            Intent intent = new Intent();
            intent.putExtra("Message",msg);
            intent.putExtra("StatusCode",statusCode);
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();
        });
        d.show();
    }

    public void captureFingurePrint(){
        try {
            Utility.setCaptureFingerED(ssz_iv_fingerprint, false);

                if (this.getActivity() != null) {
                    final Intent intent = ((FingpayAePSHomeActivity) this.getActivity()).checkBiometricProvider(deviceName, "",Constants.WADH);
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
                    final Intent intent = ((FingpayAePSHomeActivity) this.getActivity()).verifyActivityResult(requestCode, data, deviceName);
                    if (intent != null) {
                        this.startActivityForResult(intent, ((FingpayAePSHomeActivity) this.getActivity()).getRequestCode());
                    } else {
                        Utility.toast(requireActivity(), "Some error occurred!");
                        Utility.setCaptureFingerED(ssz_iv_fingerprint, true);
                    }
                }
            } else if (requestCode == 1200 || requestCode == 1500 || requestCode == 3500 || requestCode == 4500 || requestCode == 5500 || requestCode == 1300) {
                xmlData = data.getStringExtra("PID_DATA");
                if (xmlData != null) {

                    try {
                        final JSONObject jsonObjPidData = XML.toJSONObject(xmlData);

                        jsonObjPidData.put("xmlData",xmlData);
                        final JSONObject jsonPid = jsonObjPidData.getJSONObject("PidData");
                        final JSONObject jsonResp = jsonPid.getJSONObject("Resp");

                        ssz_cv_capture_score.setText(jsonResp.optString("errInfo")+" Score is "+jsonResp.optString("qScore")+"%");
                        ssz_cv_capture_score.setVisibility(View.VISIBLE);
                        final String errCode = jsonResp.getString("errCode");

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
    private void hideKeyboard(View view) {
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) this.requireActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
