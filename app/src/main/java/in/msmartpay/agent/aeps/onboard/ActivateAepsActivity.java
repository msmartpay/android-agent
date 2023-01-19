package in.msmartpay.agent.aeps.onboard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import in.msmartpay.agent.kyc.UploadDocumentActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.ImageUtils;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;

public class ActivateAepsActivity extends BaseActivity {
    ProgressDialogFragment pd;

    private static final int CHOOSE_IMAGE_CODE = 2001;
    private EditText et_device_number,et_shop_pincode,et_shop_address,actv_state,et_shop_city;
    private SmartMaterialSpinner sp_model_type;
    private ImageView iv_pancard, iv_aadhar_front, iv_aadhar_back;

    private boolean panFlag = false, aadharFrontFlag = false, aadharBackFlag = false;
    private String panFile = "", aadharFrontFile = "", aadharBackFile = "";
    private String fileName = "", modelName = "",deviceNumber,address,state,city,pincode;
    private List<String> modelList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_service_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
                //.setTitle("Activate AePS Service");
        TextView tv_toolbar_title =findViewById(R.id.tv_toolbar_title);
        TextView tv_done = findViewById(R.id.tv_done);
        ImageView iv_close = findViewById(R.id.iv_close);
        Util.hideView(tv_done);
        tv_toolbar_title.setText("Activate Aeps Service");

        et_device_number = findViewById(R.id.et_device_number);
        et_shop_address = findViewById(R.id.et_shop_address);
        et_shop_city = findViewById(R.id.et_shop_city);
        et_shop_pincode = findViewById(R.id.et_shop_pincode);
        actv_state = findViewById(R.id.actv_state);
        sp_model_type = findViewById(R.id.sp_model_type);
        iv_aadhar_back = findViewById(R.id.iv_aadhar_back);
        iv_aadhar_front = findViewById(R.id.iv_aadhar_front);
        iv_pancard = findViewById(R.id.iv_pancard);

        modelList = Arrays.asList(getResources().getStringArray(R.array.model_type));
        sp_model_type.setItem(modelList);

        iv_close.setOnClickListener(v -> {
            finish();
        });

        sp_model_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>-1){
                    modelName = modelList.get(position);
                }else {
                    modelName="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void start(View view) {
        switch (view.getId()) {
            case R.id.iv_pancard:
                panFlag = true;
                aadharBackFlag = false;
                aadharFrontFlag = false;
                requestMyPermissions();
                break;
            case R.id.iv_aadhar_front:
                panFlag = false;
                aadharBackFlag = false;
                aadharFrontFlag = true;
                requestMyPermissions();
                break;
            case R.id.iv_aadhar_back:
                panFlag = false;
                aadharBackFlag = true;
                aadharFrontFlag = false;
                requestMyPermissions();
                break;
            case R.id.btn_activate:
                activateService();
                break;
        }
    }

    private void activateService() {
        deviceNumber = et_device_number.getText().toString().trim();
        address = et_shop_address.getText().toString().trim();
        state = actv_state.getText().toString().trim();
        city = et_shop_city.getText().toString().trim();
        pincode = et_shop_pincode.getText().toString().trim();
        if (panFile.isEmpty()) {
            L.toastS(getApplicationContext(), "Choose Pan Image");
        } else if (aadharFrontFile.isEmpty()) {
            L.toastS(getApplicationContext(), "Choose Aadhar Front Image");
        } else if (aadharBackFile.isEmpty()) {
            L.toastS(getApplicationContext(), "Choose Aadhar Back Image");
        } else if (modelName.isEmpty()) {
            L.toastS(getApplicationContext(), "Select Model Type");
        } else if (address.isEmpty()) {
            L.toastS(getApplicationContext(), "Enter Shop Address");
        } else if (state.isEmpty()) {
            L.toastS(getApplicationContext(), "Enter State");
        } else if (city.isEmpty()) {
            L.toastS(getApplicationContext(), "Enter City");
        } else if (pincode.isEmpty()) {
            L.toastS(getApplicationContext(), "Enter Pincode");
        } else {
            activateServiceRequest();
        }
    }

    private void activateServiceRequest() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            ProgressDialogFragment pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Profile...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());


            List<MultipartBody.Part> partList = new ArrayList<>();
            partList.add(prepareFilePart("pan_card", panFile));//Pan card
            partList.add(prepareFilePart("aadhar_front", aadharFrontFile));
            partList.add(prepareFilePart("aadhar_back", aadharBackFile));

            String userId = Util.LoadPrefData(getApplicationContext(),Keys.AGENT_ID);
            HashMap<String, RequestBody> partMap = new HashMap<String, RequestBody>();

            partMap.put("aadhaarFrontFileName", createPartFromString("aadharfront_" + userId + "" + System.currentTimeMillis()));
            partMap.put("aadhaarBackFileName", createPartFromString("aadharback_" +userId + "" + System.currentTimeMillis()));
            partMap.put("panCardFileName", createPartFromString("pancard" + userId + "" + System.currentTimeMillis()));

            partMap.put("Key",  createPartFromString(Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY)));
            partMap.put("AgentID",  createPartFromString(Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID)));
            partMap.put("device_number", createPartFromString(deviceNumber));
            partMap.put("model_name",  createPartFromString(modelName));
            partMap.put("shop_address",  createPartFromString(address));
            partMap.put("shop_state",  createPartFromString(state));
            partMap.put("shop_city",  createPartFromString(city));
            partMap.put("pincode",  createPartFromString(pincode));




            RetrofitClient.getClient(getApplicationContext()).activateService(partMap, partList)
                    .enqueue(new Callback<MainResponse2>() {
                        @Override
                        public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                            pd.dismiss();
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    MainResponse2 res = response.body();
                                    if (res.getStatus().equalsIgnoreCase("0")) {
                                        showSuccessDialog(res.getMessage(), true);
                                    } else {
                                        showSuccessDialog(res.getMessage(), false);
                                    }
                                } else {
                                    L.toastS(getApplicationContext(), "No Server Response");
                                }
                            } catch (Exception e) {
                                L.toastS(getApplicationContext(), "Parser Error : " + e.getLocalizedMessage());
                                L.m2("Parser Error", e.getLocalizedMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<MainResponse2> call, Throwable t) {
                            pd.dismiss();
                            L.m2("Parser Error", t.getLocalizedMessage());
                        }
                    });

        }
    }

    private RequestBody createPartFromString(String str) {
        return RequestBody.create(MultipartBody.FORM, str);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String filePath) {

        File file = new File(filePath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void requestMyPermissions() {
        L.m2("permissions", "Clicked");
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            L.m2("permissions", "Granted");
                            showChoosingFile();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            L.m2("permissions", "Denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .withErrorListener(dexterError -> L.m2("permissions", "Error" + dexterError.name()))
                .check();
    }

    private void showChoosingFile() {
        //fileName = "" + System.currentTimeMillis();
        //Intent intent = ImageUtils.getPickImageChooserIntent(getApplicationContext(), fileName, false);
        //startActivityForResult(Intent.createChooser(intent, "Choose Image"), CHOOSE_IMAGE_CODE);

        ImagePicker.with(ActivateAepsActivity.this)
                .galleryOnly()
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .start(CHOOSE_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_IMAGE_CODE) {
                runOnUiThread(() -> {

                    Uri uri = data.getData();
                    String filePath=ImageUtils.getRealPathFromURI(ActivateAepsActivity.this,uri);
                    if (filePath != null && !filePath.isEmpty()) {
                        L.m2("File Path", filePath);

                        if (panFlag) {
                            panFile = filePath;
                            iv_pancard.setImageURI(uri);
                        }
                        if (aadharFrontFlag) {
                            aadharFrontFile = filePath;
                            iv_aadhar_front.setImageURI(uri);
                        }
                        if (aadharBackFlag) {
                            aadharBackFile = filePath;
                            iv_aadhar_back.setImageURI(uri);
                        }

                    } else {
                        L.toastS(getApplicationContext(), "File is not added.");
                    }
                });
            }
        }
    }

    private void showSuccessDialog(String msg, boolean status) {
        new AlertDialog.Builder(this)
                .setIcon(status ? R.drawable.tick_ok : R.drawable.warning_message_red)
                .setTitle("Activate AePS Service")
                .setMessage(msg)
                .setPositiveButton(status ?"Done":"OK", (dialog, which) -> {
                    if (status)
                    finish();
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }

}
