package in.msmartpay.agent.myWallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.aepssdkssz.util.Utility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.helpAndSupport.AboutUsWebViewActivity;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.model.wallet.TransactionItems;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.ImageUtils;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.Util;
import java.text.DecimalFormat;
import java.util.List;


public class TransactionHistoryReceipt extends BaseActivity {
    private Button ticket,btn_print_receipt,btn_share_receipt;
    private TransactionItems model;
    private int position;
    private Context context;
    private String emailId, agentName, agentMobile;
    private TextView name, mobileno, email, transaction_id_value, date_value, ser_value, amt_value,
            tran_amt__value, charge_amt_value, tot_amt_value, tv_site,operator_id_value,operator_id_text;
    private float charge = 0l, tranAmount, tot;
    private TextView Bene_name, Bene_ac, Bank_name, Bank_ifsc;
    private LinearLayoutCompat comm_detail_value,Bene_Name_detail_value1, Bene_ac_detail_value2, Bank_Name_detail_value3, Bank_IFSC_detail_value4;
    private LinearLayoutCompat ll_print_receipt;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history_receipt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction History");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TransactionHistoryReceipt.this;

        emailId = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_EMAIL);
        agentName = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_NAME);
        agentMobile = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_MOB);

        initViews();
        Bene_Name_detail_value1.setVisibility(View.GONE);
        Bene_ac_detail_value2.setVisibility(View.GONE);
        Bank_Name_detail_value3.setVisibility(View.GONE);
        Bank_IFSC_detail_value4.setVisibility(View.GONE);


        position = getIntent().getIntExtra("position", -1);
        model = Util.getGson().fromJson(getIntent().getStringExtra("historyModel"), TransactionItems.class);


        if (position != -1 && model != null) {
            String service = model.getService();


            name.setText(agentName);
            mobileno.setText(agentMobile);
            email.setText(emailId);
            transaction_id_value.setText(model.getTran_id());
            date_value.setText(model.getDot() + "\n" + model.getTot());

            amt_value.setText("\u20B9"+model.getTxnAmount());
            tran_amt__value.setText(model.getTxnAmount() + "");
            DecimalFormat df = new DecimalFormat("#.00");
            tranAmount = Float.parseFloat(model.getTxnAmount());

            tot_amt_value.setText(tranAmount+ "");


            if (service.equalsIgnoreCase("Remittance") ||
                    service.equalsIgnoreCase("Verification") ||
                    service.equalsIgnoreCase("Fund-Settlement") ) {

                Util.showView(comm_detail_value);

                Bene_Name_detail_value1.setVisibility(View.VISIBLE);
                Bene_ac_detail_value2.setVisibility(View.VISIBLE);
                Bank_Name_detail_value3.setVisibility(View.VISIBLE);
                Bank_IFSC_detail_value4.setVisibility(View.VISIBLE);
                if ("".equals(model.getBene_Name()))
                    Bene_name.setText("n/a");
                else
                    Bene_name.setText(model.getBene_Name());
                if ("".equals(model.getBene_Account()))
                    Bene_ac.setText("n/a");
                else
                    Bene_ac.setText(model.getBene_Account());
                if ("".equals(model.getBene_Bank_Name()))
                    Bank_name.setText("n/a");
                else
                    Bank_name.setText(model.getBene_Bank_Name());
                if ("".equals(model.getBene_Bank_IFSC()))
                    Bank_ifsc.setText("n/a");
                else
                    Bank_ifsc.setText(model.getBene_Bank_IFSC());

                operator_id_text.setText("Bank RRN");
                operator_id_value.setText(model.getBankRefId());

                ser_value.setText(model.getService() +
                        " (" + model.getMobile_number() + ")");

                if(service.equalsIgnoreCase("Remittance") || service.equalsIgnoreCase("Remittance2")){
                    charge = tranAmount * 0.01f;
                    if(charge<10)
                        charge=10;
                    charge_amt_value.setText(df.format(charge) + "");
                    tot = Float.parseFloat(model.getTxnAmount());
                    tot_amt_value.setText(tot+charge + "");
                }else{
                    charge_amt_value.setText(model.getServiceCharge());
                    tot_amt_value.setText(model.getDeductedAmt());
                }

            }else if (service.contains("AePS-") || service.contains("AEPS-") || service.contains("MATM")){
                Util.hideView(comm_detail_value);
                operator_id_text.setText("Bank RRN");
                operator_id_value.setText(model.getBankRefId());

                ser_value.setText(model.getService()+"\n"+model.getRemark());
            }else{
                Util.hideView(comm_detail_value);
                operator_id_text.setText("Operator Id");
                operator_id_value.setText(model.getOperatorId());

                ser_value.setText(model.getService() + " ("+model.getMobile_operator()+") "+
                        "\nConnection: " + model.getMobile_number() );
            }

        } else {
            Toast.makeText(context, "No transaction found", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        ticket.setOnClickListener(v -> {
            if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
                Intent intent = new Intent(context, TicketActivity.class);
                intent.putExtra("Mobno", model.getMobile_number());
                intent.putExtra("trnsid", model.getTran_id());
                intent.putExtra("operator", model.getMobile_operator());
                intent.putExtra("status", model.getTran_status());
                startActivity(intent);
                finish();
            }
        });

        tv_site.setOnClickListener(v -> {
            Intent intent = new Intent(context, AboutUsWebViewActivity.class);
            intent.putExtra("url", "");
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

    private void initViews() {
        ll_print_receipt= findViewById(R.id.ll_print_receipt);
        ticket = (Button) findViewById(R.id.tiket);
        btn_share_receipt = (Button) findViewById(R.id.btn_share_receipt);
        btn_print_receipt = (Button) findViewById(R.id.btn_print_receipt);
        name = (TextView) findViewById(R.id.name);
        mobileno = (TextView) findViewById(R.id.mobileno);
        email = (TextView) findViewById(R.id.email);
        transaction_id_value = (TextView) findViewById(R.id.transaction_id_value);
        date_value = (TextView) findViewById(R.id.date_value);
        ser_value = (TextView) findViewById(R.id.ser_value);
        amt_value = (TextView) findViewById(R.id.amt_value);
        tran_amt__value = (TextView) findViewById(R.id.tran_amt_value);
        charge_amt_value = (TextView) findViewById(R.id.comm_amt_value);
        tot_amt_value = (TextView) findViewById(R.id.tot_amt_value);
        tv_site = (TextView) findViewById(R.id.tv_site);
        Bene_name = (TextView) findViewById(R.id.id_bene_name);
        Bene_ac = (TextView) findViewById(R.id.id_bene_ac);
        Bank_name = (TextView) findViewById(R.id.id_bank_name);
        Bank_ifsc = (TextView) findViewById(R.id.id_bank_ifsc);
        comm_detail_value =  findViewById(R.id.comm_detail_value);
        operator_id_text =  findViewById(R.id.operator_id_text);
        operator_id_value =  findViewById(R.id.operator_id_value);

        Bene_Name_detail_value1 = findViewById(R.id.Bene_Name_detail_value1);
        Bene_ac_detail_value2 = findViewById(R.id.Bene_ac_detail_value2);
        Bank_Name_detail_value3 = findViewById(R.id.Bank_Name_detail_value3);
        Bank_IFSC_detail_value4 = findViewById(R.id.Bank_IFSC_detail_value4);

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
            ImageUtils.screenshot(ll_print_receipt,model.getId_No() + "_" + System.currentTimeMillis(),isPrint);

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
