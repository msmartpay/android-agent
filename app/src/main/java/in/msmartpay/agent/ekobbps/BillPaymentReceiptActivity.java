package in.msmartpay.agent.ekobbps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.databinding.EkoBbpsPaymentTxnRecieptBinding;
import in.msmartpay.agent.network.model.ekobbps.PayBillReceipt;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.ImageUtils;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BillPaymentReceiptActivity extends BaseActivity {

    private Context context;
    private String txnMessage="",opName="",dueDate = "",txnId="",customerName="";
    private EkoBbpsPaymentTxnRecieptBinding binding;
    private HashMap<String, Object> billRequestData;
    private PayBillReceipt data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EkoBbpsPaymentTxnRecieptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bill Payment Receipt");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        context = BillPaymentReceiptActivity.this;

        Intent intent = getIntent();
        txnMessage = intent.getStringExtra(Keys.MESSAGE);
        opName = intent.getStringExtra(Keys.Service_OP);
        dueDate = intent.getStringExtra(Keys.DUE_DATE);
        customerName = intent.getStringExtra(Keys.CUSTOMER_NAME);


        if (intent != null && intent.hasExtra(Keys.OBJECT)) {
            data = Util.getGson().fromJson(intent.getStringExtra(Keys.OBJECT), PayBillReceipt.class);
            txnId = data.getClientRefId();
            if (data != null) {
                billRequestData = Util.getGson().fromJson(intent.getStringExtra(Keys.OBJECT2),HashMap.class);
                setUPScreen();
            } else {
                finish();
            }
        } else {
            finish();
        }
        binding.btnPrintReceipt.setOnClickListener(v -> {
            myPermissions(true);
        });
        binding.btnShareReceipt.setOnClickListener(v -> {
            myPermissions(false);
        });
        binding.btnTxnDone.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        });
    }

    private void setUPScreen() {

        String txnMode = "Online";
        if("1".equalsIgnoreCase(billRequestData.get("hc_channel").toString()))
            txnMode = "Offline";
        else
            txnMode = "Online";

        binding.tvTxnDateTime.setText(Util.getCurrentDate());
        binding.tvTxnId.setText(txnId);
        binding.tvPaymentMode.setText(txnMode);

        if (data.getBbpstrxnrefid() != null) {
            Util.showView(binding.llOperatorId);
            binding.tvOperatorId.setText(data.getBbpstrxnrefid());
        } else {
            Util.hideView(binding.llOperatorId);
        }


        binding.tvCustomerName.setText(customerName);
        binding.tvCustomerMobile.setText(billRequestData.get("confirmation_mobile_no")==null?"NA":billRequestData.get("confirmation_mobile_no").toString());
        binding.tvBillNumber.setText(billRequestData.get("utility_acc_no").toString());
        binding.tvBillDueDate.setText(dueDate);
        binding.tvOperator.setText(opName);
        binding.tvTxnAmount.setText("\u20B9 "+(billRequestData.get("amount")==null?"00":billRequestData.get("amount").toString()));
        binding.tvTxnStatus.setText(data.getTxStatus());
        binding.tvTxnMessage.setText(txnMessage);

        String status = data.getTxStatus();
        binding.tvTxnStatus.setText(status);
        if ("Success".equalsIgnoreCase(status)) {//Success
            binding.imTxnStatus.setImageResource(R.drawable.tick_ok);
            binding.tvTxnStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if ("Failed".equalsIgnoreCase(status) || "Failure".equalsIgnoreCase(status)) {//Failed
            binding.imTxnStatus.setImageResource(R.drawable.failure);
            binding.tvTxnStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else if ("Initiated".equalsIgnoreCase(status)) {//Initiated
            binding.imTxnStatus.setImageResource(R.drawable.pending);
            binding.tvTxnStatus.setTextColor(ContextCompat.getColor(context, R.color.orange_logo_icon));
        }else{
            binding.imTxnStatus.setImageResource(R.drawable.pending);
            binding.tvTxnStatus.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        }

    }

    private void myPermissions(boolean isPrint) {
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Util.loge("myPermissions", "areAllPermissionsGranted");
                            printData(isPrint);
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Util.loge("myPermissions", "isAnyPermissionPermanentlyDenied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> Util.loge("myPermissions", "dexterError : " + dexterError.name()))
                .onSameThread()
                .check();
    }
    private void printData(boolean isPrint) {
        if (binding.llBillpayPrintReceipt!=null)
            ImageUtils.screenshot(binding.llBillpayPrintReceipt,txnId + "_" + System.currentTimeMillis(),isPrint);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }
}
