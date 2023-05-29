package in.msmartpay.agent.myWallet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.myWallet.adapter.ComplaintListAdapter;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.wallet.complaints.ComplaintHistoryRequest;
import in.msmartpay.agent.network.model.wallet.complaints.ComplaintsResponse;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;

public class ComplaintActivity extends BaseActivity {

    String agentID,txn_key;
    private ComplaintListAdapter adaptor;
    private RecyclerView id_complaintList;
    private EditText fromDateEtxt, toDateEtxt;
    private Button btn_fetch_complaints;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Context context;
    private ProgressDialogFragment pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaints_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Complaints");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = ComplaintActivity.this;

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fromDateEtxt = findViewById(R.id.eg_t_from_date);
        toDateEtxt = findViewById(R.id.eg_t_to_date);
        id_complaintList = findViewById(R.id.id_complaintList);
        btn_fetch_complaints = findViewById(R.id.btn_fetch_complaints);

        fromDateEtxt.requestFocus();
        fromDateEtxt.setOnClickListener(view -> setDateTimeField(fromDateEtxt));
        toDateEtxt.setOnClickListener(view -> setDateTimeField(toDateEtxt));

        btn_fetch_complaints.setOnClickListener(v -> {
            if(isConnectionAvailable()){
                getComplaintList();

            }else {
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });

        getComplaintList();
    }

    private void setDateTimeField(EditText editText) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.show();
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
        return super.onSupportNavigateUp();
    }

    private void getComplaintList(){
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())){
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching Transactions...");
            ProgressDialogFragment.showDialog(pd,getSupportFragmentManager());

            ComplaintHistoryRequest request = new ComplaintHistoryRequest();
            request.setAgentID(agentID);
            request.setTxn_key(txn_key);
            request.setFromDate(fromDateEtxt.getText().toString());
            request.setToDate(toDateEtxt.getText().toString());

            RetrofitClient.getClient(getApplicationContext())
                    .getComplaints(request).enqueue(new Callback<ComplaintsResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<ComplaintsResponse> call, @NotNull retrofit2.Response<ComplaintsResponse> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                ComplaintsResponse res = response.body();
                                if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {

                                    if (res.getData()!=null && res.getData().size()>0) {
                                        adaptor = new ComplaintListAdapter(res.getData());
                                        id_complaintList.setAdapter(adaptor);
                                    }else {
                                        L.toastS(getApplicationContext(), "No Data");
                                    }
                                } else {
                                    L.toastS(getApplicationContext(), res.getResponseMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ComplaintsResponse> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failure " + t.getLocalizedMessage());
                            pd.dismiss();
                        }
                    });
        }
    }
}
