package in.msmartpayagent.myWallet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import in.msmartpayagent.R;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.wallet.MyEarningRequest;
import in.msmartpayagent.network.model.wallet.MyEarningResponse;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class MyEarningActivity extends BaseActivity {
    private EditText fromDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private Button search;
    private ScrollView vi;
    private ProgressDialogFragment pd;
    private String txn_key = "", agentID;
    private Context context;
    private SimpleDateFormat dateFormatter;
    private TextView totRe, toterng, totde;
    private ImageView iv_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_earning_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Earning");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = MyEarningActivity.this;

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        vi = (ScrollView) findViewById(R.id.scroll);
        totRe = (TextView) findViewById(R.id.totalReq);
        totde = (TextView) findViewById(R.id.totaldeduction);
        toterng = (TextView) findViewById(R.id.totalcomm);
        fromDateEtxt = (EditText) findViewById(R.id.my_date);
        iv_calender = (ImageView) findViewById(R.id.iv_calender);
        search = (Button) findViewById(R.id.search);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fromDateEtxt.setOnClickListener(view -> setDateTimeField());

        iv_calender.setOnClickListener(view -> setDateTimeField());

        search.setOnClickListener(v -> {
            if (fromDateEtxt.getText().toString().trim().length() > 0) {
                myEarningRequest();
            } else {
                L.toastS(context, "Select Date. ");
            }
        });


    }

    private void myEarningRequest() {
        if (isConnectionAvailable()) {

            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching My Earnings...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            MyEarningRequest request = new MyEarningRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setDate(fromDateEtxt.getText().toString().trim());

            RetrofitClient.getClient(getApplicationContext())
                    .getMyEarning(request).enqueue(new Callback<MyEarningResponse>() {
                @Override
                public void onResponse(@NotNull Call<MyEarningResponse> call, @NotNull retrofit2.Response<MyEarningResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MyEarningResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            vi.setVisibility(View.VISIBLE);
                            totRe.setText(res.getResponseReqamt());
                            totde.setText(res.getResponseDeduction());
                            toterng.setText(res.getResponseComm());
                        } else {
                            vi.setVisibility(View.GONE);
                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    } else {
                        vi.setVisibility(View.GONE);
                        L.toastS(getApplicationContext(), "No Transactions available!");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MyEarningResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });

        }
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.show();
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
