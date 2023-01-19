package in.msmartpay.agent.aeps;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.aeps.AepsSettlementDetails;
import in.msmartpay.agent.network.model.aeps.FundSettlementBank;
import in.msmartpay.agent.network.model.aeps.FundSettlementDetailsRequest;
import in.msmartpay.agent.network.model.aeps.FundSettlementDetailsResponse;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;

public class SettlementDetailsActivity extends BaseActivity implements SettlementBankAdapter.SettlementBankListener {

    private TextView tv_total_transaction, tv_settled_amount, tv_available_settlement_amount;
    private LinearLayout fab_add_bank;
    private RecyclerView rv_banks;
    private SettlementBankAdapter adapter;
    private List<FundSettlementBank> list = new ArrayList();
    private AepsSettlementDetails details;

    private Context context;
    private ProgressDialog pd;
    private String AgentId = "", txnKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aeps_settlement_details_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("AEPS Settlement Details");

        context = SettlementDetailsActivity.this;
        AgentId = Util.LoadPrefData(context, Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(context, Keys.TXN_KEY);

        tv_total_transaction = findViewById(R.id.tv_total_transaction);
        tv_settled_amount = findViewById(R.id.tv_settled_amount);
        tv_available_settlement_amount = findViewById(R.id.tv_available_settlement_amount);
        fab_add_bank = findViewById(R.id.fab_add_bank);
        rv_banks = findViewById(R.id.rv_banks);


        fab_add_bank.setOnClickListener(v -> {
            startActivity(new Intent(context, SettlementAddBankActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    //get Access key
    private void getList() {
        if (NetworkConnection.isConnectionAvailable(this)) {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgress(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(true);
            FundSettlementDetailsRequest request = new FundSettlementDetailsRequest();
            request.setAgentID(AgentId);
            request.setKey(txnKey);
            RetrofitClient.getClient(context).getFundSettlementList(request)
                    .enqueue(new Callback<FundSettlementDetailsResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(Call<FundSettlementDetailsResponse> call, Response<FundSettlementDetailsResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                FundSettlementDetailsResponse res = response.body();
                                if ("0".equals(res.getStatus())) {
                                    details = res.getData();
                                    if (details != null) {
                                        tv_total_transaction.setText(details.getTotal_transaction().toString());
                                        tv_settled_amount.setText(details.getSettled_amount().toString());
                                        tv_available_settlement_amount.setText(details.getAvailable_settlement_amount().toString());

                                        if (details.getFundTransferList() != null && details.getFundTransferList().size() > 0) {
                                            list = details.getFundTransferList();
                                            adapter = new SettlementBankAdapter(list, SettlementDetailsActivity.this);
                                            rv_banks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                            rv_banks.setAdapter(adapter);
                                        }
                                    }
                                } else {
                                    L.toastS(context, res.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FundSettlementDetailsResponse> call, Throwable t) {
                            pd.dismiss();
                        }
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onSettlementBankRequest(FundSettlementBank bank) {
        Intent intent = new Intent(context, SettlementRequestActivity.class);
        intent.putExtra(Keys.OBJECT, Util.getStringFromModel(bank));
        intent.putExtra(Keys.OBJECT2, tv_available_settlement_amount.getText().toString());
        startActivity(intent);
    }
}
