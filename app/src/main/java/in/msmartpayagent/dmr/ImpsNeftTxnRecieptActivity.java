package in.msmartpayagent.dmr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.aepssdkssz.util.Utility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import in.msmartpayagent.R;
import in.msmartpayagent.dmr.dashboard.MoneyTransferActivity;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.network.model.dmr.SenderFindRequest;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.ImageUtils;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Smartkinda on 7/5/2017.
 */

public class ImpsNeftTxnRecieptActivity extends BaseActivity {

    private TextView tv_dmr_txn_status,tv_bank_rrn, tv_sender_id, tv_sender_name,  tv_bene_mobile, tviewTransferedAmount, tviewBeneficiaryName
            , tviewBankName, tviewAccountNo, tviewIFSCCode, tv_date_time,tv_message,tv_dmr_txn_tid;

    private String SenderId, SenderName, BeneMobile, accountNumber, bankName, beneName, Ifsc, BankRRN,
            amount, TxnDate, TxnTime,message,txnStatus,txnId="";
    private ImageView im_dmr_txn_status;
    private Button btnDone,btn_share_receipt,btn_print_receipt;
    private String  mobileNumber;
    private ProgressDialogFragment pd;
    private Context context;
    private LinearLayoutCompat ll_dmr_print_receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_imps_neft_txn_reciept);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction Receipt");

        context = ImpsNeftTxnRecieptActivity.this;
        mobileNumber = Util.LoadPrefData(getApplicationContext(), Keys.SENDER_MOBILE);

        txnId=getIntent().getStringExtra("tid");
        SenderId = getIntent().getStringExtra("SenderId");
        SenderName = getIntent().getStringExtra("SenderName");
        BeneMobile = getIntent().getStringExtra("BeneMobile");
        accountNumber = getIntent().getStringExtra("accountNumber");
        bankName = getIntent().getStringExtra("bankName");
        beneName = getIntent().getStringExtra("beneName");
        Ifsc = getIntent().getStringExtra("Ifsc");
        BankRRN = getIntent().getStringExtra("BankRRN");
        amount = getIntent().getStringExtra("amount");
        TxnDate = getIntent().getStringExtra("TxnDate");
        TxnTime = getIntent().getStringExtra("TxnTime");
        message=getIntent().getStringExtra("message");
        txnStatus=getIntent().getStringExtra("txnStatus");

        ll_dmr_print_receipt = findViewById(R.id.ll_dmr_print_receipt);
        btn_share_receipt = findViewById(R.id.btn_share_receipt);
        btn_print_receipt = findViewById(R.id.btn_print_receipt);
        btnDone = (Button) findViewById(R.id.btn_dmr_txn_done);

        tv_dmr_txn_status = (TextView)findViewById(R.id.tv_dmr_txn_status);
        tv_dmr_txn_tid = (TextView) findViewById(R.id.tv_dmr_txn_tid);
        tv_bank_rrn = (TextView) findViewById(R.id.tv_dmr_txn_bank_rrn);
        tv_sender_id = (TextView) findViewById(R.id.tv_dmr_txn_sender_id);
        tv_sender_name = (TextView) findViewById(R.id.tv_dmr_txn_sender_name);
        tv_bene_mobile = (TextView) findViewById(R.id.tv_dmr_txn_bene_mobile);
        tviewIFSCCode = (TextView) findViewById(R.id.tv_dmr_txn_ifsc_code);
        tviewTransferedAmount = (TextView) findViewById(R.id.tv_dmr_txn_amount);
        tviewBeneficiaryName = (TextView) findViewById(R.id.tv_dmr_txn_bene_name);
        tviewBankName = (TextView) findViewById(R.id.tv_dmr_txn_bank_name);
        tviewAccountNo = (TextView) findViewById(R.id.tv_dmr_txn_acc_number);
        tv_date_time = (TextView) findViewById(R.id.tv_dmr_txn_date_time);

        tv_message=(TextView) findViewById(R.id.tv_dmr_txn_message);
        im_dmr_txn_status=(ImageView) findViewById(R.id.im_dmr_txn_status);

        tv_dmr_txn_tid.setText(txnId);
        tv_bank_rrn.setText(BankRRN);
        tv_sender_id.setText(SenderId);
        tv_sender_name.setText(SenderName);
        tv_bene_mobile.setText(BeneMobile);
        tviewIFSCCode.setText(Ifsc);
        tviewTransferedAmount.setText("\u20B9 "+amount);
        tviewBeneficiaryName.setText(beneName);
        tviewBankName.setText(bankName);
        tviewAccountNo.setText(accountNumber);
        tviewIFSCCode.setText(Ifsc);
        tv_date_time.setText(TxnDate+" "+TxnTime);
        tv_message.setText(message);
        tv_dmr_txn_status.setText(txnStatus);

        if("Success".equalsIgnoreCase(txnStatus)){
            im_dmr_txn_status.setImageResource(R.drawable.tick_ok);
        }else if("Failure".equalsIgnoreCase(txnStatus)){
            im_dmr_txn_status.setImageResource(R.drawable.failure);
        }else if("Initiated".equalsIgnoreCase(txnStatus)){
            im_dmr_txn_status.setImageResource(R.drawable.pending);
        }else if("Refund Pending".equalsIgnoreCase(txnStatus)){
            im_dmr_txn_status.setImageResource(R.drawable.tick_ok);
        }else{
            im_dmr_txn_status.setImageResource(R.drawable.pending);
        }

        btn_print_receipt.setOnClickListener(v -> {
            myPermissions(true);
        });
        btn_share_receipt.setOnClickListener(v -> {
            myPermissions(false);
        });
        btnDone.setOnClickListener(v -> findSenderRequest());
    }

    private void findSenderRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Latest Details of Sender...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        SenderFindRequest request = new SenderFindRequest();
        request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
        request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
        request.setSenderId(mobileNumber);
        RetrofitClient.getClient(context).findSenderDetails(request).enqueue(new Callback<SenderDetailsResponse>() {
            @Override
            public void onResponse(Call<SenderDetailsResponse> call, retrofit2.Response<SenderDetailsResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    SenderDetailsResponse res = response.body();
                    if (res.getStatus().equals("0")) {
                        Util.SavePrefData(context, Keys.SENDER_MOBILE, mobileNumber);
                        Util.SavePrefData(context, Keys.SENDER, Util.getGson().toJson(res));
                        Intent intent = new Intent(context, MoneyTransferActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                       finish();

                    }
                } else {
                    L.toastS(context, "No Response");
                }
            }

            @Override
            public void onFailure(Call<SenderDetailsResponse> call, Throwable t) {
                pd.dismiss();
                L.toastS(context, "Error : " + t.getMessage());
            }
        });
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
                            Utility.loge("myPermissions", "areAllPermissionsGranted");
                            printData(isPrint);
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Utility.loge("myPermissions", "isAnyPermissionPermanentlyDenied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> Utility.loge("myPermissions", "dexterError : " + dexterError.name()))
                .onSameThread()
                .check();
    }
    private void printData(boolean isPrint) {
        if (ll_dmr_print_receipt!=null)
            ImageUtils.screenshot(ll_dmr_print_receipt,txnId + "_" + System.currentTimeMillis(),isPrint);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}