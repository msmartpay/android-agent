package in.msmartpay.agent.myWallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.wallet.TicketRequest;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class TicketActivity extends BaseActivity {
    private Dialog dialog_status;
    private TextView operatorID, MobileNo, TransId;
    private Button ticket;
    private EditText remarkEdit;
    private String tranStatus, tranId, remark;
    private Context context;
    private ProgressDialogFragment pd;
    private String txn_key = "";
    private String mobile_num, operator, agentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Submit a complaint");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TicketActivity.this;

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        mobile_num = getIntent().getStringExtra("Mobno");
        operator = getIntent().getStringExtra("operator");
        tranId = getIntent().getStringExtra("trnsid");
        tranStatus = getIntent().getStringExtra("status");

        operatorID = (TextView) findViewById(R.id.operator_ticketactivity);
        MobileNo = (TextView) findViewById(R.id.mobileno_ticketactivity);
        TransId = (TextView) findViewById(R.id.transaction_ticketactivity);
        remarkEdit = (EditText) findViewById(R.id.remark);
        ticket = (Button) findViewById(R.id.ticketsubmit);

        operatorID.setText(operator);
        MobileNo.setText(mobile_num);
        TransId.setText(tranId);

        ticket.setOnClickListener(v -> {
            remark = remarkEdit.getText().toString();
          remark=  remark.replaceAll(" ","_");
            if (remark != null && remark.length() > 0) {
                ticketRequest();
            } else {
               L.toastS(context, "Please Enter Remark.");
            }
        });
    }

    private void ticketRequest(){
        if (isConnectionAvailable()) {

            pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching Ticket...");
            ProgressDialogFragment.showDialog(pd,getSupportFragmentManager());

            TicketRequest request = new TicketRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setTicketmessage(tranStatus);
            request.setTransactionId(tranId);

            RetrofitClient.getClient(getApplicationContext())
                    .getTicketRequest(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        dialog_status = new Dialog(context);
                        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_status.setContentView(R.layout.sk_maindialog);
                        dialog_status.setCancelable(true);
                        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);

                        MainResponse2 res = response.body();

                        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
                        text.setText((String) res.getResponseMessage());
                        statusImage.setImageResource(R.drawable.trnsuccess);
                        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
                        trans_status.setOnClickListener(v12 -> {
                            dialog_status.dismiss();
                            Intent intent = new Intent();
                            intent.setClass(context, ComplaintActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        });

                        dialog_status.show();
                    } else {
                        L.toastS(getApplicationContext(), "Server Error");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });

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
