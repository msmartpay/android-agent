package agent.msmartpay.in.dmr2Moneytrasfer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.HttpURL;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class RefundFragment extends Fragment {

    private EditText editRefundTransaction, editTransactionOTP;
    private Button btnRefundTransaction;
    private Button btnSubmit;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_refund_transaction = HttpURL.REFUND_TRANSACTION;
    private String agentID, txnKey, ResisteredMobileNo;
    private SharedPreferences sharedPreferences;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dmr2_refund_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getActivity();
        sharedPreferences = context.getSharedPreferences("myPrefs", MODE_PRIVATE);

        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        ResisteredMobileNo = sharedPreferences.getString("ResisteredMobileNo", null);

        editRefundTransaction =  view.findViewById(R.id.edit_transaction_refund);
        btnRefundTransaction =  view.findViewById(R.id.btn_refund_otp);

        btnRefundTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editRefundTransaction.getText().toString().trim())) {
                    Toast.makeText(context, "Please Enter Service Delivery TID", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, RefundLiveStatusActivity.class);
                    intent.putExtra("TranNo", editRefundTransaction.getText().toString().trim());
                    intent.putExtra("fromIntent", "RefundFragment");
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        editRefundTransaction.setText("");
    }
}
