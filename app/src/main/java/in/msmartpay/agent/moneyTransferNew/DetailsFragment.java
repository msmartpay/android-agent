package in.msmartpay.agent.moneyTransferNew;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.L;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class DetailsFragment extends Fragment implements View.OnClickListener {

    private EditText editMobileNo, editFirstName, editLastName, limit, usedLimit, availableLimit;
    private Button btnSubmit;
    private String mobileNo;
    private DatePickerDialog chddDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private JSONObject object,sender_limit_details;
    private String SenderLimit_Detail_BeniList;
    private String agentID, txnKey, SessionID, mobileNumber;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_dmr_sender_details_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        editMobileNo =  view.findViewById(R.id.id_mobile_number);
        editFirstName =  view.findViewById(R.id.id_first_name);
        editLastName =  view.findViewById(R.id.id_last_name);
        limit =  view.findViewById(R.id.limit);
        usedLimit =  view.findViewById(R.id.used_limit);
        availableLimit =  view.findViewById(R.id.available_limit);


        editMobileNo.setEnabled(false);
        editFirstName.requestFocus();
        L.m2("SenderLimit_Detail---1>", MainActivity.jsonObjectStatic.toString());

        try {
            object= MainActivity.jsonObjectStatic.getJSONObject("SenderDetails");
            sender_limit_details= MainActivity.jsonObjectStatic.getJSONObject("SenderLimitDetails");

            editMobileNo.setText(object.getString("SenderId"));
            editFirstName.setText(object.getString("Name"));
            /*editLastName.setText(object.getString("LastName"));*/
            limit.setText(sender_limit_details.getString("SenderLimit"));
            usedLimit.setText(sender_limit_details.getString("UsedLimit"));
            availableLimit.setText(sender_limit_details.getString("AvailableLimit"));

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return view;
    }
    @Override
    public void onClick(View v) {

    }




}
