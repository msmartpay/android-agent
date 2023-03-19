package in.msmartpayagent.audit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.msmartpayagent.R;
import in.msmartpayagent.kyc.BaseRequest;
import in.msmartpayagent.kyc.FileUploadResponse;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainResponse2;
import in.msmartpayagent.network.model.audit.AuditRequest;
import in.msmartpayagent.network.model.audit.AuditResponse;
import in.msmartpayagent.network.model.audit.AuditUpdateRequest;
import in.msmartpayagent.network.model.audit.Transaction;
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

public class AuditRequestActivity extends AppCompatActivity {
    private ProgressDialogFragment pd;
    private static final int CHOOSE_IMAGE_CODE = 2001;
    private RecyclerView rv_list;
    private ArrayList<AuditRequest> list = new ArrayList<>();

    private AuditAdapter adapter;
    private AuditRequest selectedModal;
    private String file, filePath;
    private int selectedIndex;
    private LinearLayoutCompat ll_print;
    private ImageView img;
    private Dialog popupTxn;
    private List<Transaction> listTxn = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audit_request_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Audit Request");
        //Generate Id's
        setUpView();

        fetchAuditRequest();

    }

    private void setUpView() {
        rv_list = findViewById(R.id.rv_list);
        adapter = new AuditAdapter(AuditRequestActivity.this, list, (model, index) -> {
            selectedIndex = index;
            selectedModal = model;
            listTxn = Util.getListFromJson(selectedModal.getTransactions(), Transaction.class);
            if (listTxn!=null && listTxn.size()>0)
                showTxnPopup();
        });
        rv_list.setAdapter(adapter);
        rv_list.setLayoutManager(new LinearLayoutManager(AuditRequestActivity.this));
    }

    private void fetchAuditRequest() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Audit Request...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            BaseRequest request = new BaseRequest();
            request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(this, Keys.TXN_KEY));
            RetrofitClient.getClient(this).fetchAuditRequest(request).enqueue(new Callback<AuditResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<AuditResponse> call, Response<AuditResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        AuditResponse res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            if (res.getAuditRequest() != null && res.getAuditRequest().size() > 0) {
                                list.clear();
                                list.addAll(res.getAuditRequest());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            L.toastS(AuditRequestActivity.this, res.getMessage());
                        }

                    }
                }


                @Override
                public void onFailure(Call<AuditResponse> call, Throwable t) {

                }
            });
        }
    }

    private void showTxnPopup() {
        popupTxn = new Dialog(AuditRequestActivity.this);
        popupTxn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupTxn.setContentView(R.layout.audit_txn_popup);
        popupTxn.setCancelable(true);
        ll_print = popupTxn.findViewById(R.id.ll_print);
        img = popupTxn.findViewById(R.id.iv_img);
        Util.hideView(img);
        RecyclerView rv_txn = popupTxn.findViewById(R.id.rv_txn);
        final Button btn_print = popupTxn.findViewById(R.id.btn_print);
        final Button btn_close = popupTxn.findViewById(R.id.btn_close);
        final Button btn_select_file = popupTxn.findViewById(R.id.btn_select_file);
        final Button btn_submit = (Button) popupTxn.findViewById(R.id.btn_submit);
        if("pending".equalsIgnoreCase(selectedModal.getStatus())
                || "rejected".equalsIgnoreCase(selectedModal.getStatus())){
            Util.showView(btn_select_file);
            Util.showView(btn_submit);
        }else{
            Util.hideView(btn_select_file);
            Util.hideView(btn_submit);
        }

        rv_txn.setAdapter(new AuditTxnAdapter(AuditRequestActivity.this, listTxn));
        rv_txn.setLayoutManager(new LinearLayoutManager(AuditRequestActivity.this));

        btn_select_file.setOnClickListener(v -> {
            requestMyPermissions(false);
        });
        btn_print.setOnClickListener(v -> {
            requestMyPermissions(true);
        });
        btn_close.setOnClickListener(v ->{
            popupTxn.dismiss();
        });
        btn_submit.setOnClickListener(v -> {
            if (file!=null && !file.isEmpty())
                uploadFile();
            else
                L.toastL(AuditRequestActivity.this,"Select File");
        });
        popupTxn.show();
    }

    private void requestMyPermissions(boolean isPrint) {
        L.m2("permissions", "Clicked");
        Dexter.withContext(AuditRequestActivity.this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            L.m2("permissions", "Granted");
                            if (isPrint) {
                                runOnUiThread(() -> {
                                    Util.screenshot(ll_print, System.currentTimeMillis() + "");
                                });
                            } else {
                                showChoosingFile();
                            }
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
        ImagePicker.with(AuditRequestActivity.this)
                .galleryOnly()
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .start(CHOOSE_IMAGE_CODE);
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

    private void uploadFile() {
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
                        filePath = HttpURL.KYC_BASE_URL + res.getDownloadUri();
                        updateAudit();
                    } else {
                        L.toastS(getApplicationContext(), "Please Try Again");
                    }
                }

                @Override
                public void onFailure(Call<FileUploadResponse> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(getApplicationContext(), t.getMessage());
                }
            });
        }
    }

    private void updateAudit() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            AuditUpdateRequest request = new AuditUpdateRequest();
            request.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(this, Keys.TXN_KEY));
            request.setFilePath(filePath);
            request.setAuditRefId(selectedModal.getAuditId());
            RetrofitClient.getClient(this).updateAuditDetails(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(Call<MainResponse2> call, Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            Util.SavePrefData(getApplicationContext(), Keys.KYC_STATUS, "" + 2);
                        }
                        popupTxn.dismiss();
                        confirmationDialog(res.getMessage());
                        fetchAuditRequest();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse2> call, Throwable t) {

                }
            });
        }
    }

    public void confirmationDialog(String message) {
        final Dialog dialog_status = new Dialog(AuditRequestActivity.this);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        statusImage.setImageResource(R.drawable.trnsuccess);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        text.setText(message);
        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v -> {
            dialog_status.dismiss();
        });
        dialog_status.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_IMAGE_CODE) {
                runOnUiThread(() -> {
                    Uri uri = data.getData();
                    String filePath = ImageUtils.getRealPathFromURI(AuditRequestActivity.this, uri);
                    file = filePath;
                    if (file != null && !file.isEmpty()) {
                        Util.showView(img);
                        Picasso.get().load(new File(file)).into(img);
                    }
                });
            }
        }
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


}