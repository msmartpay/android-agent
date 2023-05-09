package in.msmartpayagent.kyc;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

import in.msmartpayagent.LoginActivity;
import in.msmartpayagent.R;
import in.msmartpayagent.network.AppMethods;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.kyc.*;
import in.msmartpayagent.utility.ImageUtils;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocumentActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialogFragment pd;
    private List<String> data = new ArrayList<>();
    private SmartMaterialSpinner sp_type;
    private EditText edit_document_number;
    private Button btn_select_doc,btn_submit_kyc,btn_kyc_upload;
    private static final int CHOOSE_IMAGE_CODE = 2001;
    private String fileName, file, filePath,fileType,kycStatus;
    private ImageView iv_file;
    private RecyclerView rv_document;
    private  DocumentAdapter adapter;
    private TextView tv_error_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Documents");

        edit_document_number = findViewById(R.id.edit_document_number);
        sp_type = findViewById(R.id.sp_kyc_document);
        btn_select_doc = findViewById(R.id.btn_kyc_select);
        btn_kyc_upload = findViewById(R.id.btn_kyc_upload);
        btn_submit_kyc = findViewById(R.id.btn_submit_kyc);

        iv_file = findViewById(R.id.iv_file);
        tv_error_msg = findViewById(R.id.tv_error_msg);
        rv_document = findViewById(R.id.rv_kyc);

        btn_select_doc.setOnClickListener(this);
        btn_kyc_upload.setOnClickListener(this);
        btn_submit_kyc.setOnClickListener(this);

        kycStatus = Util.LoadPrefData(getApplicationContext(), Keys.KYC_STATUS);
        if("3".equalsIgnoreCase(kycStatus)
        || "0".equalsIgnoreCase(kycStatus)){
            Util.showView(btn_submit_kyc);
        }else{
            Util.hideView(btn_submit_kyc);
        }

        fetchDocumentType();

        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fileType = data.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_kyc_select){
            requestMyPermissions();
        }
        if(view.getId()==R.id.btn_kyc_upload){
            if(fileType==null || fileType.isEmpty()){
                L.toastS(UploadDocumentActivity.this,"Please select document type");
            } else if(file==null){
                L.toastS(UploadDocumentActivity.this,"Please select File");

            }if(fileType!=null && !"photo".equalsIgnoreCase(fileType) && TextUtils.isEmpty(edit_document_number.getText().toString().trim())){
                L.toastS(UploadDocumentActivity.this,"Please enter document number");
            } else{
                if(StringUtils.isBlank(file)){
                    L.toastS(UploadDocumentActivity.this,"Please select File");
                }else{
                    uploadFile();
                }

            }
        }
        if(view.getId()==R.id.btn_submit_kyc){
            if(data!=null && data.size()==0){
                submitForKycVerification();
            }else {
                tv_error_msg.setText("Upload mandatory documents ["+String.join(",", data)+"]");
            }

        }
    }

    private void fetchDocumentType(){
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Documents...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        BaseRequest request = new BaseRequest();
        request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(this,Keys.TXN_KEY));

        RetrofitClient.getClient(this)
                .fetchDocumentType(request).enqueue(new Callback<DocumentTypeResponseModel>() {
                    @Override
                    public void onResponse(Call<DocumentTypeResponseModel> call, Response<DocumentTypeResponseModel> response) {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            DocumentTypeResponseModel res = response.body();
                            if (res.getStatus() != null && res.getStatus().equals("0")) {
                                data = res.getData();
                                sp_type.setItem(data);
                                fetchKycData();

                            }else {
                                L.toastS(UploadDocumentActivity.this, res.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DocumentTypeResponseModel> call, Throwable t) {
                        pd.dismiss();
                    }
                });
    }

    private void submitForKycVerification(){
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Processing your request...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        BaseRequest request = new BaseRequest();
        request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(this,Keys.TXN_KEY));

        RetrofitClient.getClient(this)
                .updateKYCStatus(request).enqueue(new Callback<DocumentTypeResponseModel>() {
                    @Override
                    public void onResponse(Call<DocumentTypeResponseModel> call, Response<DocumentTypeResponseModel> response) {
                        pd.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            DocumentTypeResponseModel res = response.body();
                            if (res.getStatus() != null && res.getStatus().equals("0")) {
                                Util.SavePrefData(getApplicationContext(),Keys.KYC_STATUS,""+2);
                                Util.hideView(btn_submit_kyc);
                                alertDialog(res.getMessage(),true);
                            }else {
                                alertDialog(res.getMessage(),false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DocumentTypeResponseModel> call, Throwable t) {
                        pd.dismiss();
                    }
                });
    }

    private void uploadFile(){
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Uploading File...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            List<MultipartBody.Part> partList = new ArrayList<>();
            partList.add(prepareFilePart("file", file));
            RetrofitClient.getClient(getApplicationContext()).kycUploadFile(partList).enqueue(new Callback<FileUploadResponse>() {
                @Override
                public void onResponse(Call<FileUploadResponse> call, Response<FileUploadResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        FileUploadResponse res =  response.body();
                        fileName = res.getFileName();
                        filePath = AppMethods.KYC_BASE_URL+res.getDownloadUri();
                        sendDocumentData();

                    }else{
                        L.toastS(getApplicationContext(), "Please Try Again");
                    }
                    iv_file.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<FileUploadResponse> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(getApplicationContext(), t.getMessage());
                }
            });
        }
    }

    private void sendDocumentData(){
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Uploading File...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        DocumentDataRequestContainer request = new DocumentDataRequestContainer();
        request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(this,Keys.TXN_KEY));

        DocumentData reqData = new DocumentData();
        reqData.setDocumentNumber(edit_document_number.getText().toString());
        reqData.setDocumentType(fileType);
        reqData.setFilePath(filePath);

        request.setData(reqData);
        RetrofitClient.getClient(this).kycUploadDoc(request).enqueue(new Callback<DocumentDataResponseContainer>() {
            @Override
            public void onResponse(Call<DocumentDataResponseContainer> call, Response<DocumentDataResponseContainer> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    DocumentDataResponseContainer res = response.body();
                    if(res.getStatus()!=null && res.getStatus().equals("0")){
                        edit_document_number.setText("");
                        if(res.getDocuments()!=null) {
                            adapter.refreshAdapter(res.getDocuments());
                            refreshDocumentTypeSpinner(res.getDocuments());
                        }

                        alertDialog(res.getMessage(),true);
                    }else{
                        alertDialog(res.getMessage(),false);
                    }
                }
            }

            @Override
            public void onFailure(Call<DocumentDataResponseContainer> call, Throwable t) {
                pd.dismiss();
                L.toastS(UploadDocumentActivity.this,"Please Try Again");
            }
        });

    }

    private void fetchKycData(){
        BaseRequest request = new BaseRequest();
        request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(this,Keys.TXN_KEY));
        RetrofitClient.getClient(this).fetchDocumentData(request).enqueue(new Callback<DocumentDataResponseContainer>() {
            @Override
            public void onResponse(Call<DocumentDataResponseContainer> call, Response<DocumentDataResponseContainer> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    DocumentDataResponseContainer res = response.body();

                    if(res.getStatus()!=null && res.getStatus().equals("0")){
                        if(res.getDocuments()!=null) {
                            adapter = new DocumentAdapter(res.getDocuments(),UploadDocumentActivity.this);
                            rv_document.setAdapter(adapter);
                            rv_document.setLayoutManager(new LinearLayoutManager(UploadDocumentActivity.this));

                            refreshDocumentTypeSpinner(res.getDocuments());
                        }

                    }else{

                    }

                }
            }


            @Override
            public void onFailure(Call<DocumentDataResponseContainer> call, Throwable t) {

            }
        });
    }


    private void requestMyPermissions() {
        L.m2("permissions", "Clicked");
        Dexter.withContext(UploadDocumentActivity.this)
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
        //Intent intent = ImageUtils.getPickImageChooserIntent(getApplicationContext(), fileName, true, false);
        //startActivityForResult(Intent.createChooser(intent, "Choose Image"), CHOOSE_IMAGE_CODE);
        ImagePicker.with(UploadDocumentActivity.this)
                .galleryOnly()
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .start(CHOOSE_IMAGE_CODE);
    }

    private void alertDialog(String msg,boolean status) {
        final Dialog dialog_status = new Dialog(UploadDocumentActivity.this, R.style.AppCompatAlertDialogStyle);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.alert);

        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);

        text.setText(msg);
        if(status)
            statusImage.setImageResource(R.drawable.trnsuccess);
        else
            statusImage.setImageResource(R.drawable.failed);

        trans_status.setOnClickListener(view -> {

            dialog_status.dismiss();

        });
        dialog_status.show();
    }

    private void refreshDocumentTypeSpinner(ArrayList<DocumentDataResponse> documents){
        for (DocumentDataResponse doc : documents) {

            if("Pending".equalsIgnoreCase(doc.getDocument_status())
                    || "Approved".equalsIgnoreCase(doc.getDocument_status()))
                data.remove(doc.getDocument_type());
        }

        sp_type.setItem(data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_IMAGE_CODE) {

                runOnUiThread(() -> {

                    Uri uri = data.getData();
                    String filePath= ImageUtils.getRealPathFromURI(UploadDocumentActivity.this,uri);

                        file = filePath;
                        iv_file.setImageURI(uri);
                    iv_file.setVisibility(View.VISIBLE);

                });
            }
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if("2".equalsIgnoreCase(kycStatus)) {
            Intent intent = new Intent(UploadDocumentActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            finish();
        }

    }

}