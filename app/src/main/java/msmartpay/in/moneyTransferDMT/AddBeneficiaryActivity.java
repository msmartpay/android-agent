package msmartpay.in.moneyTransferDMT;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;

/**
 * Created by Smartkinda on 7/5/2017.
 */

public class AddBeneficiaryActivity extends BaseActivity {

    private Spinner spinnerTransferType, spinnerAccountType;
    private String[] TransferType={"Select Transfer Type","NEFT","IMPS"};
    private String[] AccountType={"Select Account Type","Saving Account","Current Account"};
    private String dataTransferType, dataAccountType;
    private EditText editIFSCcode, editAccountNo, editConfirmAccountNo, editBeneficiaryName, editBeneficiaryMobile;
    private Button btnAddBeneficiary;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    //private String url_add_beneficiary = HttpURL.ADD_BENEFICIARY;
    //private String url_confirm_add_beneficiary = HttpURL.CONFIRM_ADD_BENEFICIARY;
    private String url_find_sender = HttpURL.FIND_SENDER;
    private String agentID, txnKey, SessionID, mobileNumber;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText editTextOTP;
    private String TransactionRefNo, BeneCode;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr_add_beneficiary_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Beneficiary");

        context = AddBeneficiaryActivity.this;
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.requestFocusFromTouch();
                return false;
            }
        });

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        spinnerTransferType =  findViewById(R.id.spinner_transfer_type);
        spinnerAccountType =  findViewById(R.id.spinner_account_type);

        editIFSCcode =  findViewById(R.id.edit_ifsc_code);
        editAccountNo =  findViewById(R.id.edit_account_no);
        editConfirmAccountNo =  findViewById(R.id.edit_confirm_account_no);
        editBeneficiaryName =  findViewById(R.id.edit_beneficiary_name);
        editBeneficiaryMobile =  findViewById(R.id.edit_beneficiary_mobile);
        btnAddBeneficiary =  findViewById(R.id.btn_add_beneficiary);

        final ArrayAdapter transferTypeAdaptor = new ArrayAdapter(this,R.layout.spinner_textview_layout, TransferType);
        transferTypeAdaptor.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerTransferType.setAdapter(transferTypeAdaptor);

        final ArrayAdapter accountTypeAdaptor = new ArrayAdapter(this,R.layout.spinner_textview_layout, AccountType);
        accountTypeAdaptor.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinnerAccountType.setAdapter(accountTypeAdaptor);

        spinnerTransferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    dataTransferType = null;
                }else {
                    dataTransferType = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    dataAccountType = null;
                }else{
                    dataAccountType = parent.getItemAtPosition(position).toString();
                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataTransferType == null){
                    Toast.makeText(context, "Please Select Transfer Type", Toast.LENGTH_LONG).show();
                } else if(dataAccountType == null){
                    Toast.makeText(context, "Please Select Account Type", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(editIFSCcode.getText().toString().trim())) {
                    editIFSCcode.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter IFSC Code", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editAccountNo.getText().toString().trim())) {
                    editAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Account Number", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editConfirmAccountNo.getText().toString().trim())) {
                    editConfirmAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Confirm Account No.", Toast.LENGTH_SHORT).show();
                }else if(!editConfirmAccountNo.getText().toString().trim().equals(editAccountNo.getText().toString().trim())){
                    editConfirmAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Account No and Confirm Account No should be same !!!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editBeneficiaryName.getText().toString().trim())) {
                    editBeneficiaryName.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Beneficiary Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(editBeneficiaryMobile.getText().toString().trim())) {
                    editBeneficiaryMobile.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Beneficiary Mobile", Toast.LENGTH_SHORT).show();
                }else{
                     //AddBeneficiaryRequest();
                }
            }
        });
    }

  /*  //Json request for add Beneficiary
    private void AddBeneficiaryRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txnKey)
                    .put("SessionId", SessionID)
                    .put("SenderId", mobileNumber.toString())
                    .put("beneName", editBeneficiaryName.getText().toString())
                    .put("beneMobileNo", editBeneficiaryMobile.getText().toString())
                    .put("bankIfsce", editIFSCcode.getText().toString())
                    .put("bankAccountNumbr", editAccountNo.getText().toString())
                    .put("accountType", spinnerAccountType.getSelectedItem().toString())
                    .put("transferType", spinnerTransferType.getSelectedItem().toString());
            L.m2("Request_addBene->",jsonObjectReq.toString());
            L.m2("Url_addBene->", url_add_beneficiary);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_add_beneficiary, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            L.m2("Resp_addBene->", object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    showConfirmationDialog(object.getString("message").toString());
                                    TransactionRefNo = object.getString("TransactionRefNo");
                                    BeneCode = object.getString("BeneCode");
                                    L.m2("check_TransactionRef-->", TransactionRefNo.toString());
                                    L.m2("check_BeneCode-->", BeneCode.toString());
                                }else {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    //Confirmation Dialog
    public void showConfirmationDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr_add_bene_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit =  d.findViewById(R.id.btn_push_submit);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);
        editTextOTP =  d.findViewById(R.id.edit_otp);
        final TextView tvConfirmation = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        tvConfirmation.setText(msg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(jsonObject.getString("Status").equalsIgnoreCase("0")) {
                        if(TextUtils.isEmpty(editTextOTP.getText().toString().trim())){
                            Toast.makeText(context, "Please enter OTP first !!!", Toast.LENGTH_SHORT).show();
                        }else{
                            confirmAddBeneficiaryRequest();
                            d.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                d.cancel();
            }
        });
        d.show();
    }

    //Json request for add Beneficiary Confirm
    private void confirmAddBeneficiaryRequest() {
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
                    .put("SenderId", mobileNumber.toString())
                    .put("BeneCode", BeneCode.toString())
                    .put("TransactionRefNo", TransactionRefNo.toString())
                    .put("otp", editTextOTP.getText().toString().trim());
            L.m2("Request--confirmBene-",jsonObjectReq.toString());
            L.m2("url-ConfirmBene-", url_confirm_add_beneficiary);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_confirm_add_beneficiary, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            L.m2("Resp-ConfirmBene-", object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    findSenderRequest();
                                }else {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
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
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }
    private void findSenderRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txnKey)
                    .put("SessionId", SessionID)
                    .put("SenderId", mobileNumber);
            L.m2("Request_sender-",jsonObjectReq.toString());
            L.m2("url-called", url_find_sender);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("Resp_findSender-", object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    MainActivity.jsonObjectStatic=object;
                                    editor.putString("ResisteredMobileNo", mobileNumber.toString());
                                    editor.commit();
                                    Intent intent = new Intent(context, MoneyTransferActivity1.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : ", Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }
*/
    @Override
    public boolean onSupportNavigateUp() {
      finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}