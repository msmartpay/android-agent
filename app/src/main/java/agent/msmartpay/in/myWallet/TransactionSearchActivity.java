package agent.msmartpay.in.myWallet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;

public class TransactionSearchActivity extends BaseActivity {

    private EditText fromDateEtxt, edit_connection_no;
    private LinearLayout submit;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_search_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Search");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TransactionSearchActivity.this;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fromDateEtxt =  findViewById(R.id.edit_select_date);
        edit_connection_no =  findViewById(R.id.edit_connection_no);
        submit = (LinearLayout) findViewById(R.id.linear_proceed_trans);

        fromDateEtxt.requestFocus();
        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectionAvailable()){
                if (edit_connection_no.getText().toString().trim() != null) {
                    if (fromDateEtxt.getText().toString().trim().length() > 0) {
                        Intent intent = new Intent(context, TransSearchList.class);
                        intent.putExtra("connNo", edit_connection_no.getText().toString().trim());
                        intent.putExtra("date", fromDateEtxt.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Date. ", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            }
        });
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
}
