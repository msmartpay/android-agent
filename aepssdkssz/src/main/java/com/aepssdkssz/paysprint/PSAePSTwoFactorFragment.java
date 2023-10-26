package com.aepssdkssz.paysprint;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aepssdkssz.R;
import com.aepssdkssz.util.Constants;
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

import com.aepssdkssz.dialog.SSZAePSDeviceSearchDialogFrag;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.BiometricDevice;
import com.aepssdkssz.network.model.paysprint.PaysprintTwoFactorRequest;
import com.aepssdkssz.network.model.paysprint.PaysprintTwoFactorRequestData;
import com.aepssdkssz.network.model.paysprint.PaysprintTwoFactorResponse;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.DialogProgressFragment;
import com.aepssdkssz.util.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A placeholder fragment containing a simple view.
 */
public class PSAePSTwoFactorFragment extends Fragment {

    private SmartMaterialSpinner sp_aeps_bank_type;
    private Button kyc_btn_send;
    private String source_ip="",xmlData="",twoFactorType="Registration",title="AePS Two Factor Registration";
    private String psTwofactorReg="0",psTwofactorAuthDate="";
    private String biometricFormat;
    private String device_type;
    private StringBuilder reportData;
    private BiometricDevice selectedBiometricDevice;
    private ProgressDialog progressDialog;
    private ImageView ssz_iv_fingerprint;
    private List<String> modelList;
    private TextView cv_capture_score,tv_two_factor_title,tv_select_device;
    private String aadhar_number, encodeFPTxnId, primaryKeyId,bankType="";
    private LinearLayout ll_select_device,ll_two_factor;
    private TextInputLayout til_aadhar_number;
    private RadioButton ssz_rb_morph;
    private List<String> bankTypeList;

    public static PSAePSTwoFactorFragment newInstance(int index) {
        PSAePSTwoFactorFragment fragment = new PSAePSTwoFactorFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ps_aeps_two_factor_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {

        ll_two_factor = view.findViewById(R.id.ll_two_factor);
        ssz_iv_fingerprint= view.findViewById(R.id.ssz_iv_fingerprint);
        cv_capture_score = view.findViewById(R.id.ssz_cv_capture_score);
        kyc_btn_send = view.findViewById(R.id.kyc_btn_send);
        sp_aeps_bank_type = view.findViewById(R.id.ssz_sp_aeps_bank_type);
        ssz_rb_morph = view.findViewById(R.id.ssz_rb_morph);
        til_aadhar_number = view.findViewById(R.id.til_aadhar_number);
        tv_two_factor_title = view.findViewById(R.id.tv_two_factor_title);
        ll_select_device = view.findViewById(R.id.ll_select_device);
        tv_select_device = view.findViewById(R.id.tv_select_device);

        device_type = Utility.getData(requireActivity(), Constants.DEVICE_TYPE);
        if(device_type!=null && !"".equalsIgnoreCase(device_type)) {
            tv_select_device.setText(device_type);
            Utility.setTextViewBG_TextColor(tv_select_device, true);
            ssz_rb_morph.setText(device_type);
        }else {
            device_type="MANTRA";
            ssz_rb_morph.setText(device_type);
            tv_select_device.setText(device_type);
            Utility.setTextViewBG_TextColor(tv_select_device, true);
        }
        bankTypeList = Arrays.asList(getResources().getStringArray(R.array.ssz_ps_aeps_bank));
        sp_aeps_bank_type.setItem(bankTypeList);
        sp_aeps_bank_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>-1){
                    bankType = bankTypeList.get(position);
                }else {
                    bankType="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ll_select_device.setOnClickListener(view17 -> {
            SSZAePSDeviceSearchDialogFrag dialogFrag = SSZAePSDeviceSearchDialogFrag.newInstance(biometricDevice -> {
                tv_select_device.setText(biometricDevice.getDevice_name());
                Utility.setTextViewBG_TextColor(tv_select_device, true);
                device_type = biometricDevice.getDevice_type();
                ssz_rb_morph.setText(biometricDevice.getDevice_type());
            });
            dialogFrag.show(requireActivity().getSupportFragmentManager(), "Search Device");
        });


        kyc_btn_send.setOnClickListener(v -> {

            aadhar_number = Objects.requireNonNull(til_aadhar_number.getEditText()).getText().toString().trim();
            if( bankType==null || "".equalsIgnoreCase(bankType)){
                Utility.toast(requireActivity(),"Select provider bank type");
            }
            else if(aadhar_number == null || "".equalsIgnoreCase(aadhar_number)){
                Utility.toast(requireActivity(),"Enter aadhaar number");
            }else if(device_type==null || "".equalsIgnoreCase(device_type)){
                Utility.toast(requireActivity(),"Select device type");
            }else {
                captureFingurePrint();
            }
        });
        Utility.showView(ll_two_factor);
        psTwofactorReg = Utility.getData(requireActivity(),Constants.PS_TWO_FACTOR_REG);
        psTwofactorAuthDate = Utility.getData(requireActivity(),Constants.PS_TWO_FACTOR_AUTH);
        String currentDate = Utility.getCurrentDate();
        int psTwofactorAuthStatus=0;
        if(currentDate.equalsIgnoreCase(psTwofactorAuthDate))
            psTwofactorAuthStatus=1;

        if("0".equalsIgnoreCase(psTwofactorReg)) {
            title = "AePS Two Factor Registration";
            twoFactorType = "Registration";
        }else if(0== psTwofactorAuthStatus) {
            title = "AePS Two Factor Authentication";
            twoFactorType = "Authenticate";
        }else{
            title = "Daily KYC Already Completed";
            Utility.hideView(ll_two_factor);
        }
        tv_two_factor_title.setText(title);
    }

    public void captureFingurePrint(){
        try {
            Utility.setCaptureFingerED(ssz_iv_fingerprint, false);

                if (this.getActivity() != null) {
                    final Intent intent = ((PSAePSHomeActivity) this.getActivity()).checkBiometricProvider(this.device_type, this.biometricFormat);
                    Utility.loge("Device App", Utility.getStringFromModel(intent));
                    if (intent != null) {
                        this.startActivityForResult(intent, ((PSAePSHomeActivity) this.getActivity()).getRequestCode());
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
                    final Intent intent = ((PSAePSHomeActivity) this.getActivity()).verifyActivityResult(requestCode, data, this.biometricFormat);
                    if (intent != null) {
                        this.startActivityForResult(intent, ((PSAePSHomeActivity) this.getActivity()).getRequestCode());
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
                            ((PSAePSHomeActivity) this.getActivity()).updatePidJson(jsonObjPidData);
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
            PaysprintTwoFactorRequest req = new PaysprintTwoFactorRequest();
            req.setTxnKey(Utility.getData(requireActivity(), Constants.TOKEN));
            req.setAgentId(Utility.getData(requireActivity(), Constants.MERCHANT_ID));
            req.setBankPipe(bankType);
            PaysprintTwoFactorRequestData data=new PaysprintTwoFactorRequestData();

            data.setAdhaarnumber(aadhar_number);
            data.setMobilenumber(Utility.getData(requireActivity(),Constants.MERCHANT_MOBILE));
            data.setAccessmodetype("APP");
            data.setData(xmlData);
            data.setLongitude(Utility.getData(requireActivity(), Constants.LONGITUDE));
            data.setLatitude(Utility.getData(requireActivity(), Constants.LATITUDE));
            data.setIpaddress(Utility.getData(requireActivity(), Constants.SOURCE_IP));

            req.setData(data);

            String url="";
            if("Registration".equalsIgnoreCase(twoFactorType)) {
                url="aeps/kyc/Twofactorkyc/registration";
            }else{
                url="aeps/kyc/Twofactorkyc/authentication";
            }

            SSZAePSRetrofitClient.getClient(requireActivity())
                    .psTwoFactorAuth(url,req).enqueue(new Callback<PaysprintTwoFactorResponse>() {
                @Override
                public void onResponse(Call<PaysprintTwoFactorResponse> call, Response<PaysprintTwoFactorResponse> response) {
                    try {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            PaysprintTwoFactorResponse res = response.body();
                            if(res.getData()!=null) {
                                String errorcode=res.getData().getErrorcode();
                                if("0".equalsIgnoreCase(errorcode)) {
                                    if("Registration".equalsIgnoreCase(twoFactorType)) {
                                        title = "AePS Two Factor Authentication";
                                        twoFactorType = "Authenticate";
                                    }else if("Authenticate".equalsIgnoreCase(twoFactorType)) {
                                        title = "Notification";
                                        twoFactorType = "AePS";
                                    }else  {
                                        title = "AePS Two Factor Registration";
                                        twoFactorType = "Registration";
                                    }
                                }else {
                                    if("Registration".equalsIgnoreCase(twoFactorType)) {
                                        title = "AePS Two Factor Registration";
                                        twoFactorType = "Registration";
                                    }else if("Authenticate".equalsIgnoreCase(twoFactorType)) {
                                        title = "AePS Two Factor Authentication";
                                        twoFactorType = "Authenticate";
                                    }
                                }
                            }else {
                                if("Registration".equalsIgnoreCase(twoFactorType)) {
                                    title = "AePS Two Factor Registration";
                                    twoFactorType = "Registration";
                                }else if("Authenticate".equalsIgnoreCase(twoFactorType)) {
                                    title = "AePS Two Factor Authentication";
                                    twoFactorType = "Authenticate";
                                }
                            }
                            showConfirmationDialog(res.getMessage(),res.getStatus());
                        }else{
                            Utility.toast(requireActivity(), "Technical failure");
                        }
                    } catch (Exception e) {
                        Utility.toast(requireActivity(), "Parser Error : " + e.getLocalizedMessage());
                        Utility.loge("Parser Error", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call<PaysprintTwoFactorResponse> call, Throwable t) {
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
        });
        btnClosed.setOnClickListener(v -> {
            d.cancel();
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