package com.aepssdkssz.ekoaeps.ekyc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.aepssdkssz.dialog.SSZAePSBankSearchDialogFrag;
import com.aepssdkssz.dialog.SSZAePSDeviceSearchDialogFrag;
import com.aepssdkssz.ekoaeps.SSZAePSHomeActivity;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.BankModel;
import com.aepssdkssz.network.model.BiometricDevice;
import com.aepssdkssz.network.model.aepstransaction.ekoekyc.EKYCBiometricRequestModal;
import com.aepssdkssz.network.model.aepstransaction.ekoekyc.EKYCResponseModal;
import com.aepssdkssz.network.model.aepstransaction.ekoekyc.EKYCVerifyOTPModal;
import com.aepssdkssz.util.Utility;

import com.aepssdkssz.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EkoEKYCBiometricDialog extends DialogFragment {

    private LinearLayout ll_select_device,ll_select_bank;
    private TextView tv_select_device,tv_select_bank,cv_capture_score;
    private BankModel selectedBankModel;
    private BiometricDevice selectedBiometricDevice;
    private String device_type,biometricFormat;
    private ProgressDialog progressDialog;
    private RadioButton ssz_rb_morph;
    private EKYCBiometricRequestModal biometricRequest;
    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.eko_aeps_biometric_kyc_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
        TextView tv_done = view.findViewById(R.id.tv_done);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        Utility.hideView(tv_done);
        tv_toolbar_title.setText("Biometric e-KYC");


        Button kyc_btn_send = view.findViewById(R.id.kyc_btn_send);
        ll_select_device = view.findViewById(R.id.ll_select_device);
        tv_select_device = view.findViewById(R.id.tv_select_device);
        tv_select_bank = view.findViewById(R.id.tv_select_bank);
        ll_select_bank = view.findViewById(R.id.ll_select_bank);
        ssz_rb_morph = view.findViewById(R.id.ssz_rb_morph);
        cv_capture_score = view.findViewById(R.id.ssz_cv_capture_score);


        Bundle bundle = getArguments();
        if (bundle != null) {
            //number = bundle.getString(Keys.CUSTOMER_MOBILE);
            biometricRequest = new EKYCBiometricRequestModal();
            biometricRequest.setAgentAadhaarNumber(bundle.getString("AgentAadhaarNumber"));
            biometricRequest.setAgentRegisteredMobile(bundle.getString("AgentRegisteredMobile"));
            biometricRequest.setOtpRefId(bundle.getString("OtpRefId"));
            biometricRequest.setReferenceTid(bundle.getString("ReferenceTid"));
        }

        ll_select_device.setOnClickListener(view17 -> {
            SSZAePSDeviceSearchDialogFrag dialogFrag = SSZAePSDeviceSearchDialogFrag.newInstance(biometricDevice -> {
                tv_select_device.setText(biometricDevice.getDevice_name());
                Utility.setTextViewBG_TextColor(tv_select_device, true);
                selectedBiometricDevice = biometricDevice;
                device_type = biometricDevice.getDevice_type();
                biometricFormat = biometricDevice.getBiometric_format();
                ssz_rb_morph.setText(biometricDevice.getDevice_type());
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

        kyc_btn_send.setOnClickListener(v -> {

            if(device_type==null || "".equalsIgnoreCase(device_type)){
                Utility.toast(requireActivity(),"Please select device type");
            }else{
                captureFingurePrint();

                //biometricRequest.setBankCode(selectedBankModel.getBank_iin());
            }

        });
        iv_close.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void submitBiometricEKyc() {
        if (Utility.checkConnection(requireActivity())) {
            progressDialog = Utility.getProgressDialog(requireActivity());
            progressDialog.show();

            SSZAePSRetrofitClient.getClient(requireActivity())
                    .ekoAePSEKyc(biometricRequest).enqueue(new Callback<EKYCResponseModal>() {
                @Override
                public void onResponse(Call<EKYCResponseModal> call, Response<EKYCResponseModal> response) {
                    progressDialog.dismiss();
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            EKYCResponseModal res = response.body();
                            if ("0".equalsIgnoreCase(res.getStatus())) {

                                EkoEKYCSuccessDialog.showDialog(requireActivity().getSupportFragmentManager(),res.getMessage());
                                dismiss();
                            } else {
                                Utility.toast(requireActivity(), res.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());

                    }
                }

                @Override
                public void onFailure(Call<EKYCResponseModal> call, Throwable t) {
                    Utility.toast(requireActivity(), "Error : " + t.getLocalizedMessage());
                    progressDialog.dismiss();
                }
            });
        }
    }


    public static EkoEKYCBiometricDialog newInstance(EKYCVerifyOTPModal ekycVerifyOTPRequest, EKYCResponseModal ekycRequestOTPResponse) {
        Bundle args = new Bundle();
        EkoEKYCBiometricDialog fragment = new EkoEKYCBiometricDialog();
        args.putString("OtpRefId", ekycRequestOTPResponse.getData().getOtpRefId());
        args.putString("ReferenceTid", ekycRequestOTPResponse.getData().getReferenceTid());
        args.putString("AgentAadhaarNumber", ekycVerifyOTPRequest.getAgentAadhaarNumber());
        args.putString("AgentRegisteredMobile", ekycVerifyOTPRequest.getAgentRegisteredMobile());
        fragment.setArguments(args);
        // fragment.setListener(listener);
        return fragment;
    }

    public static void showDialog(FragmentManager manager, EKYCVerifyOTPModal ekycVerifyOTPRequest, EKYCResponseModal ekycRequestOTPResponse) {
        EkoEKYCBiometricDialog dialog = EkoEKYCBiometricDialog.newInstance(ekycVerifyOTPRequest,ekycRequestOTPResponse);
        dialog.show(manager, "Show Dialog");
    }

    public void captureFingurePrint(){
        try {

            if (this.getActivity() != null) {
                final Intent intent = ((SSZAePSHomeActivity) this.getActivity()).checkBiometricProvider(this.device_type, this.biometricFormat);
                Utility.loge("Device App", Utility.getStringFromModel(intent));
                if (intent != null) {
                    this.startActivityForResult(intent, ((SSZAePSHomeActivity) this.getActivity()).getRequestCode());
                }
            } else {
                //Utility.setCaptureFingerED(iv_fingerprint, true);
            }
        } catch (Exception e) {
            Utility.showMessageDialogue((Context) this.getActivity(), "EXCEPTION- " + e.getMessage(), "EXCEPTION");

        }
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

                            submitBiometricEKyc();

                        } else if (jsonResp.has("errInfo")) {
                            final String errInfo = jsonResp.getString("errInfo");
                            if (!TextUtils.isEmpty((CharSequence) errInfo)) {
                                Utility.toast(requireActivity(), errInfo);
                            }
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                        Utility.toast(requireActivity(),e2.getMessage());
                    }
                } else {
                    Utility.showMessageDialogue((Context) this.getActivity(), "Empty Response!", "PID DATA XML");
                }
            } else if (resultCode == 0) {
                Utility.showMessageDialogue((Context) this.getActivity(), "Scan Failed/Aborted!", "CAPTURE RESULT");

            } else {
                Utility.showMessageDialogue((Context) this.getActivity(), "Please Connect Device", "RESULT");

            }
            final View view = this.requireActivity().getCurrentFocus();

        }
    }
}
