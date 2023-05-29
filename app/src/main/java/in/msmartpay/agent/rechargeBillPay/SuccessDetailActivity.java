package in.msmartpay.agent.rechargeBillPay;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.Util;

/**
 * Created by Roran on 10/24/2017.
 */

public class SuccessDetailActivity extends BaseActivity {

    private String supportnumber1;
    private TextView operator_id,receipt_cus_name_hd,receipt_cus_name,success_txnid, success_amount, success_mobile, success_operator, suceess_status, success_mobile_dth, success_agentid;
    private Button btn_done;
    private ImageView success_issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_payment_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Status");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        supportnumber1 = Util.LoadPrefData(getApplicationContext(), Keys.SUPPORT_1);
        receipt_cus_name_hd = (TextView) findViewById(R.id.receipt_cus_name_hd);
        receipt_cus_name = (TextView) findViewById(R.id.receipt_cus_name);
        Util.hideView(receipt_cus_name);
        Util.hideView(receipt_cus_name_hd);

        operator_id = (TextView) findViewById(R.id.operator_id);
        suceess_status = (TextView) findViewById(R.id.suceess_status);
        success_txnid = (TextView) findViewById(R.id.success_txnid);
        success_amount = (TextView) findViewById(R.id.success_amount);
        success_mobile = (TextView) findViewById(R.id.success_mobile);
        success_operator = (TextView) findViewById(R.id.success_operator);
        success_mobile_dth = (TextView) findViewById(R.id.success_mobile_dth);
        success_agentid = (TextView) findViewById(R.id.success_agentid);
        btn_done = (Button) findViewById(R.id.btn_done);
        success_issue = (ImageView) findViewById(R.id.success_issue);

        String requestType = getIntent().getStringExtra("requesttype");
        Log.d("requestType--->", requestType);

        if(requestType != null && (requestType.equalsIgnoreCase("prepaid-mobile")
        || requestType.equals("dth"))){

            Util.showView(operator_id);
            success_mobile_dth.setText("Connection Number");
            suceess_status.setText(getIntent().getStringExtra("responce"));
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText(getIntent().getStringExtra("mobileno"));
            success_operator.setText(getIntent().getStringExtra("operator"));
            operator_id.setText("Operator Id : "+getIntent().getStringExtra("operatorId"));

            Util.showView(success_txnid);

        }
        else if(requestType != null && requestType.equalsIgnoreCase("LIC")){
            Util.showView(receipt_cus_name);
            Util.showView(receipt_cus_name_hd);
            Util.showView(success_txnid);
            Util.showView(operator_id);

            receipt_cus_name_hd.setText("Customer Name");
            receipt_cus_name.setText(getIntent().getStringExtra("cust_name"));
            success_mobile_dth.setText("Policy Number");
            suceess_status.setText(getIntent().getStringExtra("responce"));
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText(getIntent().getStringExtra("mobileno"));
            success_operator.setText(getIntent().getStringExtra("operator"));
            operator_id.setText("Operator Id : "+getIntent().getStringExtra("operatorId"));
        }
        else if(requestType != null && requestType.equalsIgnoreCase("fastag")){
            Util.showView(receipt_cus_name);
            Util.showView(receipt_cus_name_hd);
            Util.showView(success_txnid);

            receipt_cus_name_hd.setText("Customer Name");
            receipt_cus_name.setText(getIntent().getStringExtra("cust_name"));
            success_mobile_dth.setText("Vehicle Registration Number");
            suceess_status.setText(getIntent().getStringExtra("responce"));
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText(getIntent().getStringExtra("mobileno"));
            success_operator.setText(getIntent().getStringExtra("operator"));
        }else if(requestType != null && requestType.equalsIgnoreCase("postpaid-mobile")){

            success_mobile_dth.setText("Connection Number");

            suceess_status.setText(getIntent().getStringExtra("responce"));
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText("+91 "+getIntent().getStringExtra("mobileno"));
            success_operator.setText(getIntent().getStringExtra("operator"));

        }else if (requestType != null && requestType.equals("landline")) {
             suceess_status.setText(getIntent().getStringExtra("responce"));
            success_mobile_dth.setText("Landline Number");
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText("+91 "+getIntent().getStringExtra("mobileno"));
            success_operator.setText(getIntent().getStringExtra("operator"));
        }else if (requestType != null && requestType.equals("Electricity")) {
             suceess_status.setText(getIntent().getStringExtra("responce"));
            success_mobile_dth.setText("Connection Number");
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText("+91 "+getIntent().getStringExtra("mobileno"));
            success_operator.setText(getIntent().getStringExtra("operator"));
        }else if (requestType != null && requestType.equals("gas-bill")) {
             suceess_status.setText(getIntent().getStringExtra("responce"));
            success_mobile_dth.setText("Connection Number");
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText("+91 "+getIntent().getStringExtra("Consumerno"));
            success_operator.setText(getIntent().getStringExtra("operator"));

        }else if (requestType != null && requestType.equals("water-bill")) {
             suceess_status.setText(getIntent().getStringExtra("responce"));
            success_mobile_dth.setText("Bill Number");
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText("+91 "+getIntent().getStringExtra("Consumerno"));
            success_operator.setText(getIntent().getStringExtra("operator"));
        }else if (requestType != null && requestType.equals("insurance-bill")) {
            suceess_status.setText(getIntent().getStringExtra("responce"));
            success_mobile_dth.setText("Policy Number");
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText("+91 "+getIntent().getStringExtra("policyno"));
            success_operator.setText(getIntent().getStringExtra("operator"));

        }else if (requestType != null && requestType.equals("wallet")) {
             suceess_status.setText(getIntent().getStringExtra("responce"));
            success_mobile_dth.setText("Mobile Number");
            success_agentid.setText("Agent ID");
            success_txnid.setText("Txn ID : "+getIntent().getStringExtra("txnId"));
            success_amount.setText("\u20B9 "+getIntent().getStringExtra("amount"));
            success_mobile.setText("+91 "+getIntent().getStringExtra("mobileno"));
            success_operator.setText(getIntent().getStringExtra("agent_id"));
        }else {
            Toast.makeText(SuccessDetailActivity.this, "Recharge Failed\nPlease Try Later !!!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void process(View v){
        if(isConnectionAvailable()){
            if (v.getId() == R.id.btn_done) {
                Intent intent = new Intent(SuccessDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else if(v.getId() == R.id.success_issue){
                /*Toast.makeText(SuccessDetailActivity.this, "Click...."+supportnumber1, Toast.LENGTH_LONG).show();
                if (null != supportnumber1 && supportnumber1.length() >= 10) {
                    Service sc = new Service();
                    String correctno = sc.validateMobileNumber(supportnumber1);
                    if (null != correctno) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + "0" + correctno));
                        if (ActivityCompat.checkSelfPermission(SuccessDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent);
                    } else {

                        Toast.makeText(SuccessDetailActivity.this, "Dialed Number is Busy,\n Please Try Later", Toast.LENGTH_LONG).show();
                    }
                }*/
            }
        }else{
            Toast.makeText(SuccessDetailActivity.this, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SuccessDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
