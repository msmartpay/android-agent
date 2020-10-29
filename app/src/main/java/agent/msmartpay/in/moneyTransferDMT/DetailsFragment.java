package agent.msmartpay.in.moneyTransferDMT;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

import static android.content.Context.MODE_PRIVATE;
import static agent.msmartpay.in.MainActivity.jsonObjectStatic;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class DetailsFragment extends Fragment implements View.OnClickListener {

    private EditText editMobileNo, editFirstName, editLastName, editDOB, editPinCode, editAddress;
    private Button btnSubmit;
    private String mobileNo;
    private DatePickerDialog chddDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_sender_update_details = HttpURL.SENDER_UPDATE_DETAILS;
    private JSONObject jsonObject1;
    private JSONObject object;
    private String agentID, txnKey, SessionID, mobileNumber;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dmr_sender_details_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getActivity();
        sharedPreferences = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        editMobileNo =  view.findViewById(R.id.id_mobile_number);
        editFirstName =  view.findViewById(R.id.id_first_name);
        editLastName =  view.findViewById(R.id.id_last_name);
        editDOB =  view.findViewById(R.id.id_dob);
        editPinCode =  view.findViewById(R.id.id_pincode);
        editAddress =  view.findViewById(R.id.id_address);
        btnSubmit =  view.findViewById(R.id.id_update_details);

        editMobileNo.setEnabled(false);
        editFirstName.requestFocus();
        L.m2("SenderLimit_Detail---1>", jsonObjectStatic.toString());

        try {
            object= jsonObjectStatic.getJSONObject("senderDetailMap");

            editMobileNo.setText(object.getString("SenderId"));
            editFirstName.setText(object.getString("FirstName"));
            editLastName.setText(object.getString("LastName"));
            editDOB.setText(object.getString("dob"));
            editPinCode.setText(object.getString("Pincode"));
            editAddress.setText(object.getString("Address"));

        } catch (JSONException e) {
            e.printStackTrace();
        }



        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chddDatePickerDialog.show();
            }
        });
        findViewsById();
        setchDDDateTimeField();

        //Validation start from here...
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(editMobileNo.getText().toString().trim())){
                    Toast.makeText(context, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editFirstName.getText().toString().trim())){
                    editFirstName.requestFocus();
                    Toast.makeText(context, "Please Enter First Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editLastName.getText().toString().trim())){
                    editLastName.requestFocus();
                    Toast.makeText(context, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editDOB.getText().toString().trim())){
                    editDOB.requestFocus();
                    Toast.makeText(context, "Please Set Your Date Of Birth", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editPinCode.getText().toString().trim())){
                    editPinCode.requestFocus();
                    Toast.makeText(context, "Please Enter Valid Pin Code", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editAddress.getText().toString().trim())){
                    editAddress.requestFocus();
                    Toast.makeText(context, "Please Enter Address", Toast.LENGTH_SHORT).show();
                } else{
                    SenderDetailsRequest();
                }

            }
        });


        return view;
    }
    @Override
    public void onClick(View v) {

    }

    //For Set Date Of Birth
    private void findViewsById() {
        editDOB.setInputType(InputType.TYPE_NULL);
    }

    private void setchDDDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        chddDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                System.out.println("DateOfBirth--->"+editDOB.getText().toString());
                editDOB.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void SenderDetailsRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", editMobileNo.getText().toString().trim())
                    .put("FirstName", editFirstName.getText().toString().trim())
                    .put("LastName", editLastName.getText().toString().trim())
                    .put("Dob", editDOB.getText().toString().trim())
                    .put("Pincode", editPinCode.getText().toString().trim())
                    .put("Address", editAddress.getText().toString().trim());
            Log.e("Request--PreSenderReq",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_sender_update_details, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----PreSenderReq>"+object.toString());
                            try {

                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called-PreSenderReq", url_sender_update_details);
                                    L.m2("url data-PreSenderReq", object.toString());

                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, SenderConfirmUpdateActivity.class);
                                    intent.putExtra("mobileNumber", editMobileNo.getText().toString().trim());
                                    intent.putExtra("FirstName", editFirstName.getText().toString().trim());
                                    intent.putExtra("LastName", editLastName.getText().toString().trim());
                                    intent.putExtra("Dob", editDOB.getText().toString().trim());
                                    intent.putExtra("Pincode", editPinCode.getText().toString().trim());
                                    intent.putExtra("Address", editAddress.getText().toString().trim());
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }


}
