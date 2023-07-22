package in.msmartpay.agent.aeps.onboard;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;
import in.msmartpay.agent.MainActivity;
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
    private static final int CHOOSE_IMAGE_CODE = 2001;
    ProgressDialog pd;
    private EditText et_device_number, et_shop_pincode, et_shop_address, actv_state, et_shop_city;
    private MaterialSpinner sp_model_type,sp_service_type;
    private ImageView iv_pancard, iv_aadhar_front, iv_aadhar_back;

    private boolean panFlag = false, aadharFrontFlag = false, aadharBackFlag = false;
    private String panFile = "", aadharFrontFile = "", aadharBackFile = "",serviceCode="",userCode="";
    private String serviceType=null, fileName = "", modelName = "", deviceNumber, address, state, city, pincode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_service_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        //.setTitle("Activate AePS Service");
        TextView tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        TextView tv_done = findViewById(R.id.tv_done);
        ImageView iv_close = findViewById(R.id.iv_close);
        Util.hideView(tv_done);
        tv_toolbar_title.setText("Activate Service");



        et_device_number = findViewById(R.id.et_device_number);
        et_shop_address = findViewById(R.id.et_shop_address);
        et_shop_city = findViewById(R.id.et_shop_city);
        et_shop_pincode = findViewById(R.id.et_shop_pincode);
        actv_state = findViewById(R.id.actv_state);
        sp_model_type = findViewById(R.id.sp_model_type);
        sp_service_type = findViewById(R.id.sp_service_type);
        iv_aadhar_back = findViewById(R.id.iv_aadhar_back);
        iv_aadhar_front = findViewById(R.id.iv_aadhar_front);
        iv_pancard = findViewById(R.id.iv_pancard);

        et_shop_address.setText(Util.LoadPrefData(getApplicationContext(), Keys.ADDRESS));
        et_shop_city.setText(Util.LoadPrefData(getApplicationContext(), Keys.DISTRICT));
        actv_state.setText(Util.LoadPrefData(getApplicationContext(), Keys.STATE));
        et_shop_pincode.setText(Util.LoadPrefData(getApplicationContext(), Keys.PINCODE));

        iv_close.setOnClickListener(v -> {
            finish();
        });

        sp_model_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1) {
                    modelName = parent.getItemAtPosition(position).toString();
                } else {
                    modelName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_service_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > -1) {
                    serviceType = parent.getItemAtPosition(position).toString();
                    if("AePS".equalsIgnoreCase(serviceType)){
                        serviceCode="43";//43,52
                    }else if("AePSAPI".equalsIgnoreCase(serviceType)){
                        serviceCode="52";//43,52
                    }else if("AadhaarPay".equalsIgnoreCase(serviceType)){
                        serviceCode="51";
                    }else if("BBPS".equalsIgnoreCase(serviceType)){
                        serviceCode="53";
                    }else if("Settlement".equalsIgnoreCase(serviceType)){
                        serviceCode="45";
                    }else{
                        serviceCode="43";
                    }
                } else {
                    serviceCode="43";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userCode=Util.LoadPrefData(getApplicationContext(), Keys.USER_CODE);
        if(StringUtils.isBlank(userCode)){
            UserRegistrationDialog.showDialog(getSupportFragmentManager(), Util.LoadPrefData(getApplicationContext(), Keys.AGENT_MOB));
            //UserNumberDialog.showDialog(getSupportFragmentManager(), Util.LoadPrefData(getApplicationContext(), Keys.AGENT_MOB));
        }
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
            Toast.makeText(getApplicationContext(), "Choose Pan Image", Toast.LENGTH_SHORT).show();;
        } else if (aadharFrontFile.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Choose Aadhaar Front Image", Toast.LENGTH_SHORT).show();;
        } else if (aadharBackFile.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Choose Aadhaar Back Image", Toast.LENGTH_SHORT).show();;
        } else if (modelName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Select Model Type", Toast.LENGTH_SHORT).show();;
        } else if (address.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Shop Address", Toast.LENGTH_SHORT).show();;
        } else if (state.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter State", Toast.LENGTH_SHORT).show();;
        } else if (city.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_SHORT).show();;
        } else if (pincode.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Pin code", Toast.LENGTH_SHORT).show();;
        } else {
            if("AePS ICICI/KOTAK".equalsIgnoreCase(serviceType))
                serviceType="43";
            else if("AePS FINO".equalsIgnoreCase(serviceType))
                serviceType="52";
            else if("Aadhaar Pay".equalsIgnoreCase(serviceType))
                serviceType="51";
            else
                serviceType="52";
            activateServiceRequest();
        }
    }

    private void activateServiceRequest() {
        if (isConnectionAvailable()) {
            pd = ProgressDialog.show(ActivateAepsActivity.this, "", "Loading. Please wait...", true);
            pd.setProgress(ProgressDialog.STYLE_HORIZONTAL);

            List<MultipartBody.Part> partList = new ArrayList<>();
            partList.add(prepareFilePart("pan_card", panFile));//Pan card
            partList.add(prepareFilePart("aadhar_front", aadharFrontFile));
            partList.add(prepareFilePart("aadhar_back", aadharBackFile));

            HashMap<String, RequestBody> partMap = new HashMap<String, RequestBody>();


            partMap.put("agentId", createPartFromString(Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID)));
            partMap.put("service_code", createPartFromString(serviceCode));
            partMap.put("devicenumber", createPartFromString(deviceNumber));
            partMap.put("modelname", createPartFromString(modelName));
            partMap.put("office_address_line", createPartFromString(address));
            partMap.put("office_state", createPartFromString(state));
            partMap.put("office_city", createPartFromString(city));
            partMap.put("office_pincode", createPartFromString(pincode));

            partMap.put("id_address_line", createPartFromString(Util.LoadPrefData(getApplicationContext(), Keys.ADDRESS)));
            partMap.put("id_state", createPartFromString(Util.LoadPrefData(getApplicationContext(), Keys.STATE)));
            partMap.put("id_city", createPartFromString(Util.LoadPrefData(getApplicationContext(), Keys.DISTRICT)));
            partMap.put("id_pincode", createPartFromString(Util.LoadPrefData(getApplicationContext(), Keys.PINCODE)));
            partMap.put("user_code", createPartFromString(userCode));


            RetrofitClient.getNewClient(getApplicationContext()).activateService(partMap, partList)
                    .enqueue(new Callback<MainResponse2>() {
                        @Override
                        public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                            pd.dismiss();
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    MainResponse2 res = response.body();
                                    if ("0".equalsIgnoreCase(res.getStatus())) {

                                        showSuccessDialog(res.getMessage(), true);
                                    } else {
                                        showSuccessDialog(res.getMessage(), false);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "No Server Response", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Parser Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                L.m2("Parser Error", Objects.requireNonNull(e.getLocalizedMessage()));
                            }
                        }

                        @Override
                        public void onFailure(Call<MainResponse2> call, Throwable t) {
                            pd.dismiss();
                            L.m2("Parser Error", Objects.requireNonNull(t.getLocalizedMessage()));
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
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
                .withPermissions(Util.storagePermissions())
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


                });
            }
        }
    }

    private void showSuccessDialog(String msg, boolean status) {
        new AlertDialog.Builder(this)
                .setIcon(status ? R.drawable.tick_ok : R.drawable.warning_message_red)
                .setTitle("AePS Activation")
                .setMessage(msg)
                .setPositiveButton(status ? "Done" : "OK", (dialog, which) -> {
                    if (status) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
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
