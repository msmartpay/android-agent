package in.msmartpayagent.kyc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Objects;

import in.msmartpayagent.R;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.utility.HttpURL;
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
    private List<String> requiredDocTypeList=new ArrayList<String>() ;
    private Button btn_submit_kyc;
    private static final int CHOOSE_IMAGE_CODE = 2001;
    private String fileName, file, filePath, fileType, fileTypeId, kycStatus = "";
    private ImageView iv_kyc_file;
    private TextView accept_kyc_tc_click, kyc_err_message, tv_mendatory_doc;
    private CheckBox kyc_tc_check;
    private LinearLayout ll_kyc_tc;
    private RecyclerView rv_document, rv_kyc_doc_type;
    private ArrayList<DocumentDataResponse> docList = new ArrayList<>();
    private ArrayList<DocumentTypeDataModel> docTypeList = new ArrayList<>();
    private DocumentAdapter docAdapter;
    private DocumentTypeAdapter docTypeAdapter;
    private DocumentDataResponseContainer container;
    private DocumentTypeDataModel typeDataModel;
    private int typeDataIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Documents");

        //Generate Id's
        setUpView();
        if(getIntent().hasExtra("message")){
            kyc_err_message.setText(getIntent().getStringExtra("message"));
            Util.showView(kyc_err_message);
        }

        kyc_err_message.setText("");
        Util.hideView(kyc_err_message);
        kycStatus = Util.LoadPrefData(getApplicationContext(), Keys.KYC_STATUS);
        if ("0".equalsIgnoreCase(kycStatus) || "3".equalsIgnoreCase(kycStatus)) {
            Util.showView(btn_submit_kyc);
            Util.showView(ll_kyc_tc);
        } else {
            Util.hideView(btn_submit_kyc);
            Util.hideView(ll_kyc_tc);
        }
        fetchKycDocumentType();

        /*
        sp_kyc_document_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fileType = data.get(i);
                Util.showView(til_kyc_document_number);
                if("Photo".equalsIgnoreCase(fileType)){
                    fileTypeIdList=new ArrayList<String>();
                    fileTypeIdList.add("Photo");
                    Util.hideView(til_kyc_document_number);
                }else if("IdentityProof".equalsIgnoreCase(fileType)){
                    fileTypeIdList=new ArrayList<String>();
                    fileTypeIdList.add("Aadhaar");
                    fileTypeIdList.add("Pancard");
                    fileTypeIdList.add("VoterId");
                    fileTypeIdList.add("DL");
                    fileTypeIdList.add("Passport");

                }else if("PermanentAddressProof".equalsIgnoreCase(fileType)){
                    fileTypeIdList=new ArrayList<String>();
                    fileTypeIdList.add("Aadhaar");
                    fileTypeIdList.add("VoterId");
                    fileTypeIdList.add("DL");
                    fileTypeIdList.add("Passport");

                }else if("ShopAddressProof".equalsIgnoreCase(fileType)){
                    fileTypeIdList=new ArrayList<String>();
                    fileTypeIdList.add("Aadhaar");
                    fileTypeIdList.add("VoterId");
                    fileTypeIdList.add("DL");
                    fileTypeIdList.add("Passport");
                    fileTypeIdList.add("Others");

                }else if("BankProof".equalsIgnoreCase(fileType)){
                    fileTypeIdList=new ArrayList<String>();
                    fileTypeIdList.add("BankStatement");
                    fileTypeIdList.add("Others");

                }else if("GSTCOPY".equalsIgnoreCase(fileType)){
                    fileTypeIdList=new ArrayList<String>();
                    fileTypeIdList.add("GSTCOPY");

                }else if("ShopPhoto".equalsIgnoreCase(fileType)){
                    fileTypeIdList=new ArrayList<String>();
                    fileTypeIdList.add("ShopPhoto");
                    fileTypeIdList.add("Others");

                }else if("Others".equalsIgnoreCase(fileType)){
                    fileTypeIdList=new ArrayList<String>();
                    fileTypeIdList.add("Others");
                }else{
                    fileTypeIdList=new ArrayList<String>();
                }
                sp_kyc_document_type_id.setItem(fileTypeIdList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fileTypeId=null;
            }
        });


        sp_kyc_document_type_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fileTypeId = data.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fileTypeId=null;
            }
        });
        */

    }

    private void setUpView() {
        iv_kyc_file = findViewById(R.id.iv_kyc_file);
        rv_document = findViewById(R.id.rv_kyc_documents);
        rv_kyc_doc_type = findViewById(R.id.rv_kyc_doc_type);

        btn_submit_kyc = findViewById(R.id.btn_submit_kyc);
        kyc_err_message = findViewById(R.id.kyc_err_message);
        ll_kyc_tc = findViewById(R.id.ll_kyc_tc);
        tv_mendatory_doc = findViewById(R.id.tv_mendatory_doc);

        accept_kyc_tc_click = findViewById(R.id.accept_kyc_tc_click);
        accept_kyc_tc_click.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


        kyc_tc_check = findViewById(R.id.kyc_tc_check);
        btn_submit_kyc.setOnClickListener(this);

        accept_kyc_tc_click.setOnClickListener(this);
        setUpDocumentTypeAdapter();
        setUpDocumentAdapter();
    }

    private void setUpDocumentTypeAdapter() {
        //Initialize Upload Document Adapter
        docTypeAdapter = new DocumentTypeAdapter(UploadDocumentActivity.this,docTypeList,  new DocumentTypeAdapter.DocumentTypeListener() {
            @Override
            public void onUploadImage(DocumentTypeDataModel model, int index) {
                typeDataModel = model;

                typeDataIndex = index;kyc_err_message.setText("");
                Util.showView(kyc_err_message);

                if(model.getUploadUrl().isEmpty()){
                    kyc_err_message.setText("Select Image of "+model.getDisplayName());
                    Util.showView(kyc_err_message);
                }else if(!"photo".equalsIgnoreCase(model.getDocumentType())
                        && !"shopphoto1".equalsIgnoreCase(model.getDocumentType())
                        && !"shopphoto2".equalsIgnoreCase(model.getDocumentType())
                        && model.getDocNumber().isEmpty()){
                    kyc_err_message.setText("Enter Document Number of "+model.getDisplayName());
                    Util.showView(kyc_err_message);
                    L.toastL(UploadDocumentActivity.this,"Enter Document Number of "+model.getDisplayName());
                }else {
                    uploadFile();
                }

            }

            @Override
            public void captureImage(DocumentTypeDataModel model, int index) {
                typeDataModel = model;
                typeDataIndex = index;
                requestMyPermissions();
            }

            @Override
            public void onDcoNumber(String docNumber, int index) {
                docTypeList.get(index).setDocNumber(docNumber);
            }
        });
        rv_kyc_doc_type.setAdapter(docTypeAdapter);
        rv_kyc_doc_type.setLayoutManager(new LinearLayoutManager(UploadDocumentActivity.this));
    }

    private void setUpDocumentAdapter() {
        //Initialize Existing KYC Document Adapter
        docAdapter = new DocumentAdapter(docList, UploadDocumentActivity.this);
        rv_document.setAdapter(docAdapter);
        rv_document.setLayoutManager(new LinearLayoutManager(UploadDocumentActivity.this));
    }

    private void refreshDocTypeList() {

        for (DocumentDataResponse doc : docList) {
            if ("Approved".equalsIgnoreCase(doc.getDocument_status()) || "Pending".equalsIgnoreCase(doc.getDocument_status())) {
                DocumentTypeDataModel tempRemovalDoc = null;
                for (DocumentTypeDataModel typeDataModel : docTypeList) {
                    if (typeDataModel.getDocumentType().equalsIgnoreCase(doc.getDocument_type()))
                        tempRemovalDoc = typeDataModel;

                }
                if (tempRemovalDoc != null) {
                    docTypeList.remove(tempRemovalDoc);
                }
            }
        }


        requiredDocTypeList.clear();
        for ( DocumentTypeDataModel typeDataModel : docTypeList) {
            requiredDocTypeList.add(typeDataModel.getDocumentType());
        }

        setMendatoryText(requiredDocTypeList);
    }

    private void setMendatoryText(List<String> data) {
        if(data!=null && data.size()>0) {
            L.m2("myData : ",data.size()+" data : "+data.toString() );
            tv_mendatory_doc.setText("** Mandatory Docs " + StringUtils.join(",", data));
            Util.showView(tv_mendatory_doc);
        }else{
            tv_mendatory_doc.setText("");
            Util.hideView(tv_mendatory_doc);
        }
    }

    private void fetchKycDocumentType() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Documents...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        BaseRequest request = new BaseRequest();
        request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(this, Keys.TXN_KEY));

        RetrofitClient.getClient(this)
                .fetchDocumentType(request).enqueue(new Callback<DocumentTypeResponseModel>() {
            @Override
            public void onResponse(Call<DocumentTypeResponseModel> call, Response<DocumentTypeResponseModel> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    DocumentTypeResponseModel res = response.body();
                    if (res.getStatus() != null && "0".equalsIgnoreCase(res.getStatus()) ) {
                        if (res.getData()!=null && res.getData().size()>0) {
                            docTypeList.clear();
                            docTypeList.addAll(res.getData());
                        }
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

    private void fetchKycData(){
        BaseRequest request = new BaseRequest();
        request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(this,Keys.TXN_KEY));
        RetrofitClient.getClient(this).fetchDocumentData(request).enqueue(new Callback<DocumentDataResponseContainer>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<DocumentDataResponseContainer> call, Response<DocumentDataResponseContainer> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    DocumentDataResponseContainer res = response.body();

                    if(res.getStatus()!=null && res.getStatus().equals("0")) {
                        if (res.getDocuments() != null && res.getDocuments().size() > 0) {
                            docList.clear();
                            docList.addAll(res.getDocuments());
                            refreshDocTypeList();


                            docTypeAdapter.notifyDataSetChanged();
                            docAdapter.notifyDataSetChanged();
                        }

                    }else{
                        L.toastS(UploadDocumentActivity.this,res.getMessage());
                    }

                }
            }


            @Override
            public void onFailure(Call<DocumentDataResponseContainer> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_submit_kyc){
            //TODO submit KYC
            if(kyc_tc_check.isChecked()){

                if(docTypeList.isEmpty()){
                    submitKycForVerification();
                }else{
                    String  errMessage = StringUtils.join(",",requiredDocTypeList.toString());
                    kyc_err_message.setText("Please upload "+errMessage+ " documents!!!");
                    Util.showView(kyc_err_message);
                }
            }else{
                kyc_err_message.setText("Please accept the terms and conditions!!!");
                Util.showView(kyc_err_message);
                L.toastS(getApplicationContext(),"Please accept the terms and conditions");
            }
        }
        if(view.getId()==R.id.accept_kyc_tc_click){
            confirmationTCDialog(getResources().getString(R.string.kyc_tc));
        }
//        if(view.getId()==R.id.tv_kyc_sample_click){
//            if(fileType==null || "".equalsIgnoreCase(fileType)){
//                L.toastS(UploadDocumentActivity.this,"Please select document type");
//            }else {
//                confirmationSampleDialog(fileType);
//            }
//        }
        //if(view.getId()==R.id.select_kyc_file){
           // requestMyPermissions();
        //}
//        if(view.getId()==R.id.btn_upload_kyc_document){
//            kyc_err_message.setText("");
//            Util.hideView(kyc_err_message);
//            if(fileType==null || fileType.isEmpty()){
//                L.toastS(UploadDocumentActivity.this,"Please select document type");
//                kyc_err_message.setText("Please select document type");
//                Util.showView(kyc_err_message);
//            } /*else if(fileTypeId==null || fileTypeId.isEmpty()){
//                L.toastS(UploadDocumentActivity.this,"Please select document type Id");
//            }*/else if((!"photo".equalsIgnoreCase(fileType) && !"shopphoto".equalsIgnoreCase(fileType)) && (ed_kyc_doc_no.getText()==null || "".equalsIgnoreCase(ed_kyc_doc_no.getText().toString()))){
//                L.toastS(UploadDocumentActivity.this,"Please enter document number");
//                kyc_err_message.setText("Please enter document number");
//                Util.showView(kyc_err_message);
//            } else if(file==null){
//                L.toastS(UploadDocumentActivity.this,"Please select File");
//                kyc_err_message.setText("Please select File");
//                Util.showView(kyc_err_message);
//            }else{
//                Util.hideView(kyc_err_message);
//                uploadFile();
//            }
//        }
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
                .cameraOnly()
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .start(CHOOSE_IMAGE_CODE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_IMAGE_CODE) {
                runOnUiThread(() -> {
                    Uri uri = data.getData();
                    String filePath=ImageUtils.getRealPathFromURI(UploadDocumentActivity.this,uri);
                     file = filePath;
                     L.m2("Image Path",file);
                     docTypeList.get(typeDataIndex).setUploadUrl(filePath);
                     docTypeAdapter.notifyDataSetChanged();
                });
            }
        }
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
                        FileUploadResponse res = response.body();
                        fileName = res.getFileName();
                        filePath = HttpURL.KYC_BASE_URL + res.getDownloadUri();
                        sendDocumentData();
                    } else {
                        L.toastS(getApplicationContext(), "Please Try Again");
                    }
                    iv_kyc_file.setVisibility(View.GONE);
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
        DocumentData data = new DocumentData();
        data.setDocumentType(typeDataModel.getDocumentType());
        data.setFilePath(filePath);
        data.setDocumentTypeId(fileTypeId);
        data.setDocumentNo(typeDataModel.getDocNumber());
        request.setData(data);
        RetrofitClient.getClient(this).kycUploadDoc(request).enqueue(new Callback<DocumentDataResponseContainer>() {
            @Override
            public void onResponse(Call<DocumentDataResponseContainer> call, Response<DocumentDataResponseContainer> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    DocumentDataResponseContainer res = response.body();
                    if(res.getStatus()!=null && res.getStatus().equals("0")){
                        confirmationDialog(res.getMessage(),1);
                    }
                    L.toastS(UploadDocumentActivity.this,res.getMessage());
                }
                resetFields();
            }

            @Override
            public void onFailure(Call<DocumentDataResponseContainer> call, Throwable t) {
                pd.dismiss();
                L.toastS(UploadDocumentActivity.this,"Please Try Again");
                resetFields();
            }
        });

    }

    private void resetFields() {
        filePath=null;
        file=null;
        fileType=null;
        fileTypeId=null;

    }

    private void submitKycForVerification(){
        BaseRequest request = new BaseRequest();
        request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(this,Keys.TXN_KEY));
        RetrofitClient.getClient(this).updateKYCStatus(request).enqueue(new Callback<DocumentDataResponseContainer>() {
            @Override
            public void onResponse(Call<DocumentDataResponseContainer> call, Response<DocumentDataResponseContainer> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    DocumentDataResponseContainer res = response.body();

                    if(res.getStatus()!=null && res.getStatus().equals("0")){
                        Util.SavePrefData(getApplicationContext(), Keys.KYC_STATUS,""+2);
                    }
                    confirmationDialog(res.getMessage(),3);

                }
            }


            @Override
            public void onFailure(Call<DocumentDataResponseContainer> call, Throwable t) {

            }
        });
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
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void confirmationDialog(String message,int i){
        final Dialog dialog_status = new Dialog(UploadDocumentActivity.this);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        statusImage.setImageResource(R.drawable.trnsuccess);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        text.setText(message);

        if(i==2){
            Util.hideView(statusImage);
            //text.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v -> {
            dialog_status.dismiss();

            if(i==1) {
                fetchKycData();
            }else if(i==2){
                kyc_tc_check.setChecked(true);
                Util.hideView(statusImage);
            }else if(i==3){
                Util.hideView(btn_submit_kyc);
                Util.hideView(ll_kyc_tc);
            }
        });
        dialog_status.show();
    }

    public void confirmationTCDialog(String message){
        final Dialog dialog_status = new Dialog(UploadDocumentActivity.this);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert_tc_tc);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        statusImage.setImageResource(R.drawable.trnsuccess);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        text.setText(message);

        Util.hideView(statusImage);

        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v -> {
            dialog_status.dismiss();
            kyc_tc_check.setChecked(true);
            Util.hideView(statusImage);
        });
        dialog_status.show();
    }

    public void confirmationSampleDialog(String imageName){
        final Dialog dialog_status = new Dialog(UploadDocumentActivity.this);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert_sample_image);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);

        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v -> {
            dialog_status.dismiss();
        });
        dialog_status.show();
    }

}