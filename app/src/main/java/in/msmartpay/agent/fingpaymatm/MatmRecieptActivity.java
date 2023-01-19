package in.msmartpay.agent.fingpaymatm;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.ImageUtils;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

public class MatmRecieptActivity extends BaseActivity {

    private String txnStatus,txnType;
    private ImageView im_matm_txn_status;
    private Button btnDone,btn_share_receipt,btn_print_receipt;
    private String  txnId;
    private ProgressDialogFragment pd;
    private Context context;
    private LinearLayoutCompat ll_account_bal,ll_print_receipt;
    private TextView tv_matm_txn_message, tv_matm_terminal_id, tv_matm_card_no,  tv_matm_card_type,
            tv_matm_txn_bank_name, tv_matm_txn_mobile, tv_matm_txn_bank_rrn, tv_matm_txn_tid,tv_matm_txn_status,
            tv_matm_txn_date_time,tv_matm_txn_amount,tv_matm_txn_amount_val,tv_matm_txn_type,tv_matm_account_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matm_receipt_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction Receipt");

        context = MatmRecieptActivity.this;

        btnDone = (Button) findViewById(R.id.btn_matm_txn_done);
        btn_share_receipt = (Button) findViewById(R.id.btn_share_receipt);
        btn_print_receipt = (Button) findViewById(R.id.btn_print_receipt);

        ll_print_receipt = findViewById(R.id.ll_matm_print_receipt);
        ll_account_bal= findViewById(R.id.ll_account_bal);

        tv_matm_txn_status = findViewById(R.id.tv_matm_txn_status);
        tv_matm_account_balance = findViewById(R.id.tv_matm_account_balance);
        tv_matm_txn_message = findViewById(R.id.tv_matm_txn_message);
        tv_matm_terminal_id = findViewById(R.id.tv_matm_terminal_id);
        tv_matm_card_no = findViewById(R.id.tv_matm_card_no);
        tv_matm_card_type = findViewById(R.id.tv_matm_card_type);
        tv_matm_txn_bank_name = findViewById(R.id.tv_matm_txn_bank_name);
        tv_matm_txn_bank_rrn = findViewById(R.id.tv_matm_txn_bank_rrn);
        tv_matm_txn_tid = findViewById(R.id.tv_matm_txn_tid);
        tv_matm_txn_date_time = findViewById(R.id.tv_matm_txn_date_time);
        tv_matm_txn_mobile = findViewById(R.id.tv_matm_txn_mobile);
        tv_matm_txn_type = findViewById(R.id.tv_matm_txn_type);
        tv_matm_txn_amount = findViewById(R.id.tv_matm_txn_amount);
        tv_matm_txn_amount_val = findViewById(R.id.tv_matm_txn_amount_val);

        im_matm_txn_status = findViewById(R.id.im_matm_txn_status);



        txnType = getIntent().getStringExtra(Keys.TYPE);
        txnStatus=getIntent().getStringExtra(Keys.RESPONSE_CODE);
        txnId=getIntent().getStringExtra(Keys.TXN_ID);

        if("CW".equalsIgnoreCase(txnType) || "SALE".equalsIgnoreCase(txnType)){
            tv_matm_txn_amount.setText("Transaction Amount");
            tv_matm_txn_amount_val.setText("\u20B9 "+getIntent().getDoubleExtra(Keys.TRANS_AMOUNT,0));

            tv_matm_account_balance.setText("\u20B9 "+getIntent().getDoubleExtra(Keys.BALANCE_AMOUNT,0));
            Util.showView(ll_account_bal);
        }else{
            tv_matm_txn_amount.setText("Account Balance");
            tv_matm_txn_amount_val.setText("\u20B9 "+getIntent().getDoubleExtra(Keys.BALANCE_AMOUNT,0));
            Util.hideView(ll_account_bal);
        }

        Util.showView(tv_matm_txn_type);
        if("CW".equalsIgnoreCase(txnType)){
            tv_matm_txn_type.setText("MATM CASH WITHDRAWAL");
        }else if("BE".equalsIgnoreCase(txnType)){
            tv_matm_txn_type.setText("MATM BALANCE ENQUIRY");
        }else if("SALE".equalsIgnoreCase(txnType)){
            tv_matm_txn_type.setText("MATM PURCHASE");
        }else{
            Util.hideView(tv_matm_txn_type);
            tv_matm_txn_type.setText("");
        }
        tv_matm_terminal_id.setText(getIntent().getStringExtra(Keys.TERMINAL_ID));
        tv_matm_card_no.setText(getIntent().getStringExtra(Keys.CARD_NUM));
        tv_matm_card_type.setText(getIntent().getStringExtra(Keys.CARD_TYPE));
        tv_matm_txn_bank_name.setText(getIntent().getStringExtra(Keys.BANK_NAME));
        tv_matm_txn_bank_rrn.setText(getIntent().getStringExtra(Keys.RRN));
        tv_matm_txn_tid.setText(txnId);

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateObj = new Date();
        String datetime = df.format(dateObj);
        tv_matm_txn_date_time.setText(datetime);
        tv_matm_txn_mobile.setText(getIntent().getStringExtra(Keys.MOBILE));

        tv_matm_txn_message.setText(getIntent().getStringExtra(Keys.MESSAGE));

        if("00".equalsIgnoreCase(txnStatus)){
            im_matm_txn_status.setImageResource(R.drawable.tick_ok);
            tv_matm_txn_status.setText("Success");
        }else{
            im_matm_txn_status.setImageResource(R.drawable.failure);
            tv_matm_txn_status.setText("Failure");
        }

        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(MatmRecieptActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btn_print_receipt.setOnClickListener(v -> {
            myPermissions(true);
        });
        btn_share_receipt.setOnClickListener(v -> {
            myPermissions(false);
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
        if (ll_print_receipt!=null)
            ImageUtils.screenshot(ll_print_receipt,txnId + "_" + System.currentTimeMillis(),isPrint);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}