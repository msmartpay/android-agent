package in.msmartpayagent.rechargeBillPay;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import in.msmartpayagent.R;
import in.msmartpayagent.dialogs.BankSearchDialogFrag;
import in.msmartpayagent.dialogs.FastagOperatorSearchDialogFrag;
import in.msmartpayagent.location.GPSTrackerPresenter;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainRequest2;
import in.msmartpayagent.network.model.OperatorsRequest;
import in.msmartpayagent.network.model.OperatorsResponse;
import in.msmartpayagent.network.model.fastag.BillFetch;
import in.msmartpayagent.network.model.fastag.FastagData;
import in.msmartpayagent.network.model.fastag.FastagFetchRequest;
import in.msmartpayagent.network.model.fastag.FastagFetchResponse;
import in.msmartpayagent.network.model.fastag.FastagOperator;
import in.msmartpayagent.network.model.fastag.FastagOperatorResponse;
import in.msmartpayagent.network.model.fastag.FastagRechargeRequest;
import in.msmartpayagent.network.model.fastag.FastagRechargeResponse;
import in.msmartpayagent.network.model.fastag.RechargeData;
import in.msmartpayagent.network.model.wallet.OperatorModel;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

public class FastTagActivity extends BaseActivity {

    private static final int REQUEST_OPRATPOR = 02120;
    private TextInputLayout til_operator, til_vehicle_no, til_amount, til_customer_name;
    private EditText et_operator, et_vehicle_no, et_customer_name,  et_amount;
    private TextView tv_fetch_bill, tv_confirm_request;
    private ProgressDialogFragment pd;
    private Context context;
    private String amount,vehicleNo,customerName;
    private ArrayList<FastagOperator> operatorList = new ArrayList<>();
    private FastagOperator opreatorModel = null;
    private  FastagFetchResponse fetchResponse = null;

    private String agentID, txn_key;
    private GPSTrackerPresenter gpsTrackerPresenter=null;
    private boolean isLocationEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fast_tag_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Fastag Recharges");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = FastTagActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        initViews();

        operatorsCodeRequest();

        et_operator.setOnClickListener(v -> {
            FastagOperatorSearchDialogFrag dialogFrag = FastagOperatorSearchDialogFrag.newInstance(operatorList, model -> {
                opreatorModel = model;
                et_operator.setText(model.getName());
                //et_vehicle_no.setHint(model.getDisplayname());
            });
            dialogFrag.show(getSupportFragmentManager(), "Search Operator");
        });

        tv_fetch_bill.setOnClickListener(v -> {
            vehicleNo = et_vehicle_no.getText().toString().trim();
            if(opreatorModel==null){
                L.toastS(this,"Select Fastag Operator");
            }else if(TextUtils.isEmpty(vehicleNo)){
                L.toastS(this,"Enter Fastag Register Number");
            }else{
                fetchConsumerDetailsRequest();
            }
        });
        tv_confirm_request.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                vehicleNo =et_vehicle_no.getText().toString().trim();
                amount = et_amount.getText().toString().trim();
                customerName = et_customer_name.getText().toString().trim();

                if (opreatorModel == null) {
                    L.toastS(this,"Select Fastag Operator");
                } else if (TextUtils.isEmpty(vehicleNo)) {
                    L.toastS(this,"Enter Fastag Register Number");
                } else if (TextUtils.isEmpty(amount) || Double.parseDouble(amount) < 100) {
                    L.toastS(this,"Enter valid amount with minimum of Rs. 100");
                }else if (TextUtils.isEmpty(customerName) ) {
                    L.toastS(this,"Enter Customer Number");
                }else {
                    rechargeFastagRequest();
                }
            } else {
                L.toastS(this,"No Internet Connection !!!");
            }
        });

        gpsTrackerPresenter = new GPSTrackerPresenter(this,new GPSTrackerPresenter.LocationListener(){

            @Override
            public void onLocationFound(Location location) {
                if (!isLocationEnable) {
                    isLocationEnable = true;
                }
            }

            @Override
            public void locationError(String msg) {

            }
        },GPSTrackerPresenter.RUN_TIME_PERMISSION_CODE);
    }


    private void initViews(){
        tv_fetch_bill = findViewById(R.id.tv_fetch_bill);
        tv_confirm_request = findViewById(R.id.tv_confirm_request);
        til_operator = findViewById(R.id.til_operator);
        til_amount = findViewById(R.id.til_amount);
        til_vehicle_no = findViewById(R.id.til_vehicle_no);
        til_customer_name = findViewById(R.id.til_customer_name);

        et_amount = findViewById(R.id.et_amount);
        et_operator = findViewById(R.id.et_operator);
        et_vehicle_no = findViewById(R.id.et_vehicle_no);
        et_customer_name = findViewById(R.id.et_customer_name);


        Util.hideView(tv_confirm_request);

        Util.hideView(til_amount);
        Util.hideView(til_customer_name);
    }

    //===========operatorCodeRequest==============
    private void operatorsCodeRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {

            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Operators...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            MainRequest2 request2 =new MainRequest2();
            request2.setAgentID(agentID);
            request2.setKey(txn_key);


            RetrofitClient.getClient(getApplicationContext())
                    .getFastagOperator(request2).enqueue(new Callback<FastagOperatorResponse>() {
                @Override
                public void onResponse(@NotNull Call<FastagOperatorResponse> call, @NotNull retrofit2.Response<FastagOperatorResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        FastagOperatorResponse res = response.body();
                        if ("0".equals(res.getStatus())) {
                            operatorList = (ArrayList<FastagOperator>) res.getData();
                        } else {
                            L.toastS(context, res.getMessage()!=null?res.getMessage():"Oops!");
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FastagOperatorResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    //===========fetchConsumerDetailsRequest==============
    private void fetchConsumerDetailsRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Consumer Details...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            FastagData data = new FastagData();
            data.setCanumber(vehicleNo);
            data.setOperator(opreatorModel.getId());
            FastagFetchRequest request2 =new FastagFetchRequest();
            request2.setAgentID(agentID);
            request2.setKey(txn_key);
            request2.setData(data);

            RetrofitClient.getClient(getApplicationContext())
                    .fetchFastagConsumerDetails(request2).enqueue(new Callback<FastagFetchResponse>() {
                @Override
                public void onResponse(@NotNull Call<FastagFetchResponse> call, @NotNull retrofit2.Response<FastagFetchResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        fetchResponse = response.body();
                        if ("0".equals(fetchResponse.getStatus())) {
                            ///operatorList = (ArrayList<FastagOperator>) res.getData();
                            Util.hideView(tv_fetch_bill);
                            Util.showView(tv_confirm_request);
                            Util.showView(til_amount);
                            Util.showView(til_customer_name);
                            //et_amount.setText(fetchResponse.getAmount());
                            et_amount.requestFocus();
                            et_customer_name.setText(fetchResponse.getData().getName());
                        } else {
                            L.toastS(context, fetchResponse.getMessage()!=null?fetchResponse.getMessage():"Oops!");
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FastagFetchResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }


    //===========rechargeFastagRequest==============
    private void rechargeFastagRequest() {
        if (NetworkConnection.isConnectionAvailable2(getApplicationContext())) {

            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Consumer Details...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

            RechargeData data = new RechargeData();
            data.setCanumber(vehicleNo);
            data.setOperator(opreatorModel.getId());
            data.setAmount(amount);
            data.setBillFetch(fetchResponse.getData().getBillFetch());
            data.setLatitude(Double.parseDouble("".equals(Util.LoadPrefData(this, Keys.LATITUDE)) ?"0.0":Util.LoadPrefData(this,Keys.LATITUDE)));
            data.setLongitude(Double.parseDouble("".equals(Util.LoadPrefData(this, Keys.LONGITUDE)) ?"0.0":Util.LoadPrefData(this,Keys.LONGITUDE)));

            FastagRechargeRequest request2 =new FastagRechargeRequest();
            request2.setAgentID(agentID);
            request2.setKey(txn_key);
            request2.setIpaddress(Util.getIpAddress(this));
            request2.setCname(customerName);
            request2.setData(data);

            RetrofitClient.getClient(getApplicationContext())
                    .rechargeFastag(request2).enqueue(new Callback<FastagRechargeResponse>() {
                @Override
                public void onResponse(@NotNull Call<FastagRechargeResponse> call, @NotNull retrofit2.Response<FastagRechargeResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        FastagRechargeResponse res = response.body();
                        if ("0".equals(res.getStatus())) {
                            Intent in = new Intent(context, SuccessDetailActivity.class);
                            in.putExtra("responce", res.getMessage());
                            in.putExtra("mobileno", vehicleNo);
                            in.putExtra("requesttype", "fastag");
                            in.putExtra("operator", opreatorModel.getName());
                            in.putExtra("cust_name", customerName);
                            in.putExtra("amount", amount);
                            in.putExtra("txnId", res.getClientRefId());
                            in.putExtra("txnStatus", res.getTxnStatus());
                            startActivity(in);
                            finish();
                            L.toastS(context, res.getMessage()!=null?res.getMessage():"Oops!");
                        } else {
                            L.toastS(context, res.getMessage()!=null?res.getMessage():"Oops!");
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FastagRechargeResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
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
