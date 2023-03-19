package in.msmartpayagent.myWallet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.msmartpayagent.R;
import in.msmartpayagent.utility.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TransactionSearchActivity extends BaseActivity {

    private EditText edit_connection_no;
    private Button btn_search_txn;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_search_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Search By Mobile");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = TransactionSearchActivity.this;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        edit_connection_no =  findViewById(R.id.edit_connection_no);
        btn_search_txn = findViewById(R.id.btn_search_txn);

        btn_search_txn.setOnClickListener(v -> {
            if(isConnectionAvailable()){
                if (edit_connection_no.getText().toString().trim() != null) {
                    Intent intent = new Intent(context, TransSearchList.class);
                    intent.putExtra("connNo", edit_connection_no.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(context, "Enter valid mobile number!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });
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
