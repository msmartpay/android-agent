package in.msmartpay.agent.myWallet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.aepssdkssz.util.Utility;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import in.msmartpay.agent.R;
import in.msmartpay.agent.myWallet.adapter.StatementListAdapter;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.wallet.TransactionItems;
import in.msmartpay.agent.network.model.wallet.WalletHistoryRequest;
import in.msmartpay.agent.network.model.wallet.WalletHistoryResponse;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class WalletHistoryActivity extends BaseActivity {
    private ProgressDialogFragment pd;
    private ArrayList<TransactionItems> tranList = new ArrayList<TransactionItems>();
    private RecyclerView transactionList;
    private EditText et_search;
    private String  txn_key = "";
    private String agentID, url;
    private Context context;
    private TextView tv_to,tv_from,tv_all,tv_in,tv_out;
    private LinearLayout ll_search;
    private StatementListAdapter adapter;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private int i=0;
    private String toDate="",fromDate="",service="all";
    private SmartMaterialSpinner sp_service_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_history_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Wallet History");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        et_search = findViewById(R.id.et_search);
        tv_to = findViewById(R.id.tv_to);
        tv_from =  findViewById(R.id.tv_from);
        ll_search =  findViewById(R.id.ll_search);
        tv_all = findViewById(R.id.tv_all);
        tv_in =  findViewById(R.id.tv_in);
        tv_out =  findViewById(R.id.tv_out);
        sp_service_type = findViewById(R.id.sp_service_type);

        context = WalletHistoryActivity.this;

        Intent in=getIntent();
        service=in.getStringExtra("service");


        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        transactionList =  findViewById(R.id.transactionList);


        walletHistoryRequest(service);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter!=null)
                    adapter.getFilter2().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sp_service_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service=sp_service_type.getSelectedItem().toString();
                if(service!=null && !"".equalsIgnoreCase(service)) {
                    if (!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {
                        walletHistoryRequestByDate(service);
                    } else {
                        walletHistoryRequest(service);

                    }
                }else{
                    Utility.toast(getApplicationContext(),"Please select Service");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tv_to.setOnClickListener(v -> {
            i=1;
            setDateTimeField(tv_to);
        });

        tv_from.setOnClickListener(v -> {
            i=0;
            setDateTimeField(tv_from);
        });
        ll_search.setOnClickListener(v -> {
            if(fromDate.equalsIgnoreCase("")){
                Toast.makeText(WalletHistoryActivity.this,"Select From Date", Toast.LENGTH_LONG).show();
            }else if(toDate.equalsIgnoreCase("")){
                Toast.makeText(WalletHistoryActivity.this,"Select From Date", Toast.LENGTH_LONG).show();
            }else {

                    ll_search.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    walletHistoryRequestByDate(service);

            }
        });

        tv_all.setOnClickListener(v -> {
            adapter.getFilter().filter("");
            tv_all.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            tv_in.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_out.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        });

        tv_in.setOnClickListener(v -> {
            adapter.getFilter().filter("In");
            tv_all.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_in.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            tv_out.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        });
        tv_out.setOnClickListener(v -> {
            adapter.getFilter().filter("Out");
            tv_all.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_in.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_out.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        });

    }

    private void walletHistoryRequest(String service) {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())){
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching History...");
            ProgressDialogFragment.showDialog(pd,getSupportFragmentManager());

            WalletHistoryRequest request = new WalletHistoryRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService(service);
            RetrofitClient.getClient(getApplicationContext())
                    .getWalletHistory(request).enqueue(new Callback<WalletHistoryResponse>() {
                @Override
                public void onResponse(@NotNull Call<WalletHistoryResponse> call, @NotNull retrofit2.Response<WalletHistoryResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        WalletHistoryResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {

                            if(res.getServices()!=null && !res.getServices().isEmpty()){
                                sp_service_type.setItem(new ArrayList());
                                sp_service_type.setItem(res.getServices());
                            }
                            if (tranList == null)
                                tranList = new ArrayList<>();
                            else
                                tranList.clear();
                            if (res.getStatement()!=null && res.getStatement().size()>0) {
                                tranList = (ArrayList<TransactionItems>) res.getStatement();
                                adapter = new StatementListAdapter(tranList);
                                transactionList.setAdapter(adapter);
                            }else {
                                L.toastS(getApplicationContext(), "No Data");
                            }
                        } else {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<WalletHistoryResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }
    private void walletHistoryRequestByDate(String service) {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())){
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching History...");
            ProgressDialogFragment.showDialog(pd,getSupportFragmentManager());

            WalletHistoryRequest request = new WalletHistoryRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setFromDate(fromDate);
            request.setToDate(toDate);
            request.setService(service);
            RetrofitClient.getClient(getApplicationContext())
                    .getWalletHistoryByDate(request).enqueue(new Callback<WalletHistoryResponse>() {
                @Override
                public void onResponse(@NotNull Call<WalletHistoryResponse> call, @NotNull retrofit2.Response<WalletHistoryResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        WalletHistoryResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {

                            if(res.getServices()!=null && !res.getServices().isEmpty()){
                                sp_service_type.setItem(new ArrayList());
                                sp_service_type.setItem(res.getServices());
                            }

                            if (tranList == null)
                                tranList = new ArrayList<>();
                            else
                                tranList.clear();
                            if (res.getStatement()!=null && res.getStatement().size()>0) {
                                tranList = (ArrayList<TransactionItems>) res.getStatement();
                                adapter = new StatementListAdapter(tranList);
                                transactionList.setAdapter(adapter);
                            }else {
                                L.toastS(getApplicationContext(), "No Data");
                            }
                        } else {
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<WalletHistoryResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wallet_history_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_refresh) {
            finish();
            Intent i = new Intent(context, WalletHistoryActivity.class);  //your class
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void setDateTimeField(final TextView myview) {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(WalletHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if(i==0) {
                    fromDate=dateFormatter.format(newDate.getTime());
                    myview.setText("From \n" +fromDate);

                } else {
                    toDate=dateFormatter.format(newDate.getTime());
                    myview.setText("To \n" + toDate);
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();

    }
}
