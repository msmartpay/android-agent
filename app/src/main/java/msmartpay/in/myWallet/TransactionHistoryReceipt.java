package msmartpay.in.myWallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import msmartpay.in.helpAndSupport.AboutUsWebViewActivity;
import msmartpay.in.MainActivity;
import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;


public class TransactionHistoryReceipt extends BaseActivity {
    private Button ticket;
    private ArrayList<TransactionItems> tranList = new ArrayList<TransactionItems>();
    private int position;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String emailId, agentName, agentMobile;
    private TextView name, mobileno, email, order_value, date_value, ser_value, amt_value,
            tran_amt__value, comm_amt_value, tot_amt_value, tv_site;
    private double comm = 0l, tranAmount, netAmount, tot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history_receipt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction History");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TransactionHistoryReceipt.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        emailId = sharedPreferences.getString("emailId", null);
        agentName = sharedPreferences.getString("agentName", null);
        agentMobile = sharedPreferences.getString("agentMobile", null);

        ticket =  findViewById(R.id.tiket);
        name = (TextView) findViewById(R.id.name);
        mobileno = (TextView) findViewById(R.id.mobileno);
        email = (TextView) findViewById(R.id.email);
        order_value = (TextView) findViewById(R.id.order_value);
        date_value = (TextView) findViewById(R.id.date_value);
        ser_value = (TextView) findViewById(R.id.ser_value);
        amt_value = (TextView) findViewById(R.id.amt_value);
        tran_amt__value = (TextView) findViewById(R.id.tran_amt_value);
        comm_amt_value = (TextView) findViewById(R.id.comm_amt_value);
        tot_amt_value = (TextView) findViewById(R.id.tot_amt_value);
        tv_site = (TextView)findViewById(R.id.tv_site);

        position = getIntent().getIntExtra("position",-1);
        tranList = (ArrayList<TransactionItems>) getIntent().getSerializableExtra("tranList");

        if (position != -1 && tranList != null) {
                name.setText(agentName);
                mobileno.setText(agentMobile);
                email.setText(emailId);
                order_value.setText(tranList.get(position).tran_id);
                date_value.setText(tranList.get(position).dot+"\n"+tranList.get(position).tot);
                ser_value.setText(tranList.get(position).service +
                        " (" + tranList.get(position).mobile_number+ ")" + " " + tranList.get(position).mobile_operator);
                amt_value.setText(tranList.get(position).net_amout + "");
                tran_amt__value.setText(tranList.get(position).DeductedAmt + "");
                DecimalFormat df = new DecimalFormat("#.00");
                tranAmount = Double.parseDouble(tranList.get(position).net_amout);
                netAmount = Double.parseDouble(tranList.get(position).DeductedAmt);
                comm = tranAmount - netAmount;
                comm_amt_value.setText(df.format(comm) + "");
                tot = Double.parseDouble(tranList.get(position).DeductedAmt);
                tot_amt_value.setText(df.format(tot) + "");
            } else {
                Toast.makeText(context, "No transaction found", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }

        ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectionAvailable()){
                    Intent intent = new Intent(context, TicketActivity.class);
                    intent.putExtra("Mobno", tranList.get(position).mobile_number);
                    intent.putExtra("trnsid", tranList.get(position).tran_id);
                    intent.putExtra("operator", tranList.get(position).mobile_operator);
                    intent.putExtra("status", tranList.get(position).tran_status);
                    startActivity(intent);
                    finish();
                }else {
                        Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        tv_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, AboutUsWebViewActivity.class);
                intent.putExtra("url","http://smartkinda.com/");
                startActivity(intent);
                finish();
            }
        });
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
