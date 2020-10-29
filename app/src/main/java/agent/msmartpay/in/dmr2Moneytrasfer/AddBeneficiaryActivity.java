package agent.msmartpay.in.dmr2Moneytrasfer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import agent.msmartpay.in.MainActivity;
import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

/**
 * Created by Smartkinda on 7/5/2017.
 */

public class AddBeneficiaryActivity extends BaseActivity {

    /*private Spinner spinnerBankList;*/
    private EditText et_searchbank;

    private EditText editIFSCcode, editAccountNo, editConfirmAccountNo, edit_sender_mobile, edit_beneficiary_mobile, edit_beneficiary_name;
    private Button btnAddBeneficiary;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private ArrayList<BankListModel> BankListArray = null;
    private CustomAdaptorClass bankListAdaptor;
    private String url_bank_list = HttpURL.GET_BANK_LIST_Dmr2;
    private String url_verify_add_beneficiary = HttpURL.VERIFY_ADD_BENEFICIARY_Dmr2;
    private String url_find_sender = HttpURL.FIND_SENDER_Dmr2;
    private String agentID, txnKey, SessionID, mobileNumber;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText editTextOTP;
    private String spinnerSelectedBank, TransactionRefNo, BeneCode;
    private Context context;
    private BankListModel listModel = null;

    private ArrayList<String> BanknameList;
    private ArrayList<String> BankcodeList;
    private CheckBox addwithverify;
    private String Bankname="",Bankcode="";
    private String checked ;
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr2_add_beneficiary_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Beneficiary");

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

       /* spinnerBankList =  findViewById(R.id.spinner_transfer_type);*/
        et_searchbank = findViewById(R.id.et_searchbank);
        edit_sender_mobile =  findViewById(R.id.edit_sender_mobile);
        edit_sender_mobile.setVisibility(View.GONE);
        editIFSCcode =  findViewById(R.id.edit_ifsc_code);
        editAccountNo =  findViewById(R.id.edit_account_no);
        editConfirmAccountNo =  findViewById(R.id.edit_confirm_account_no);
        edit_beneficiary_mobile =  findViewById(R.id.edit_beneficiary_mobile);
        edit_beneficiary_name =  findViewById(R.id.edit_beneficiary_name);
        btnAddBeneficiary =  findViewById(R.id.btn_add_beneficiary);
        addwithverify = (CheckBox)findViewById(R.id.addwithverify);
        edit_sender_mobile.setText(mobileNumber);


        et_searchbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BanknameList != null) {
                    if (BanknameList.size() != 0) {
                        Intent intent = new Intent(context, Bank_name_search.class);
                        intent.putExtra("Bankname", BanknameList);
                        startActivityForResult(intent, 0);
                    } else {
                        Toast.makeText(context, "Bank List Not Available !!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Bank List Not Available !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*BankList Request*/
        try {
            getBankListRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }

      /*  spinnerBankList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    dataBankList = null;
                } else {
                    dataBankList = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        btnAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                /*listModel = new BankListModel();
                listModel = (BankListModel) spinnerBankList.getSelectedItem();*/
               /* spinnerSelectedBank = listModel.getBankName();

                if (spinnerSelectedBank == null) {
                    Toast.makeText(AddBeneficiaryActivity.this, "Please Select Bank List!", Toast.LENGTH_LONG).show();
                } else*/ /*if (TextUtils.isEmpty(editIFSCcode.getText().toString().trim())) {
                    editIFSCcode.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter IFSC Code!", Toast.LENGTH_SHORT).show();
                } else */if (TextUtils.isEmpty(editAccountNo.getText().toString().trim())) {
                    editAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Account Number!", Toast.LENGTH_SHORT).show();
                } else if (!editConfirmAccountNo.getText().toString().equals(editAccountNo.getText().toString())) {
                    editConfirmAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Account No.!", Toast.LENGTH_SHORT).show();
                } /*else if (TextUtils.isEmpty(edit_beneficiary_mobile.getText().toString())) {
                    edit_beneficiary_mobile.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter 10 digit Mobile No.!", Toast.LENGTH_SHORT).show();
                } */else if (TextUtils.isEmpty(edit_beneficiary_name.getText().toString())) {
                    edit_beneficiary_name.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Bene Name!", Toast.LENGTH_SHORT).show();
                } else {
                    if (addwithverify.isChecked()) {
                        checked = "Y";
                    }
                    else{
                        checked = "N";
                    }
                    showConfirmationDialog(1, "");
                }
            }
        });
    }

    //getBankListRequest
    private void getBankListRequest() throws JSONException {
        pd = ProgressDialog.show(AddBeneficiaryActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        JSONObject jsonObjectReq = new JSONObject()
                .put("AgentID", agentID)
                .put("Key", txnKey)
                .put("SenderId", mobileNumber);

        L.m2("Request--banklist>", jsonObjectReq.toString());
        JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_bank_list,
                jsonObjectReq,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        pd.dismiss();
                        System.out.println("object--banklist>" + object.toString());
                        try {
                            if (object.getString("Status").equalsIgnoreCase("0")) {
                                L.m2("Resp--banklist>", object.toString());
                                JSONArray stateJsonArray = object.getJSONArray("BankList");
                                /*BankListArray = new ArrayList<>();*/
                                BanknameList= new ArrayList<>();
                                BankcodeList= new ArrayList<>();
                                for (int i = 0; i < stateJsonArray.length(); i++) {
                                    JSONObject obj = stateJsonArray.getJSONObject(i);

                                    BanknameList.add(obj.getString("bname"));
                                    BankcodeList.add(obj.getString("bcode"));


                                    /*BankListModel bankListModel = new BankListModel();
                                    bankListModel.setBankName(obj.getString("bname"));
                                    bankListModel.setBankCode(obj.getString("bcode"));
                                    BankListArray.add(bankListModel);*/
                                }
                               /* bankListAdaptor = new CustomAdaptorClass(context, BankListArray);
                                spinnerBankList.setAdapter(bankListAdaptor);
                                L.m2("BankList--->", BankListArray.toString());*/
                            } else {
                                pd.dismiss();
                                Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        getSocketTimeOut(jsonrequest);
        Mysingleton.getInstance(context).addToRequsetque(jsonrequest);

    }

    //Confirmation Dialog
    public void showConfirmationDialog(final int i, String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.new_dmr_confirmation_dialog);

        final Button btnSubmit =  d.findViewById(R.id.btn_push_submit);
        final Button btn_no =  d.findViewById(R.id.btn_resend_otp);
        final TextView tvConfirmation = (TextView) d.findViewById(R.id.tv_confirmation_dialog);

        if (i == 1) {
            tvConfirmation.setText("Are you sure, you want to add Beneficiary!");
        }
        if (i == 2) {
            tvConfirmation.setText(msg);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==1) {
                    verifyAddBeneficiaryRequest();
                    d.dismiss();
                }

                if(i==2){
                    findSenderRequest();
                    d.dismiss();
                }
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        d.show();
    }

    //Json request for add Beneficiary Confirm
    private void verifyAddBeneficiaryRequest() {
        pd = ProgressDialog.show(AddBeneficiaryActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber)
                    .put("BankAccount", editAccountNo.getText().toString())
                    .put("BeneName", edit_beneficiary_name.getText().toString().trim())
                    .put("IFSC", editIFSCcode.getText().toString())
                    .put("BankName",Bankname/* spinnerSelectedBank*/)
                    .put("BeneMobile", mobileNumber/*edit_beneficiary_mobile.getText().toString()*/)
                    .put("varify", checked)
                    .put("BankCode", Bankcode);

            L.m2("url-AddBene", url_verify_add_beneficiary);
            Log.e("Request--verifyAddBene", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_verify_add_beneficiary, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject = object;
                            System.out.println("Object--verify>" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url data", object.toString());

                                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                    showConfirmationDialog(2, object.getString("message"));
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    private void findSenderRequest() {
        pd = ProgressDialog.show(AddBeneficiaryActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txnKey)
                    .put("AgentID", agentID)
                    .put("SenderId", mobileNumber);
            Log.e("Request", jsonObjectReq.toString());
            L.m2("url-called", url_find_sender);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            L.m2("url data", object.toString());
                            MainActivity.jsonObjectStatic  = object;
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    JSONObject senderDetailsObject = object.getJSONObject("SenderDetails");
                                    editor.putString("SenderId", senderDetailsObject.getString("SenderId"));
                                    editor.putString("SenderName", senderDetailsObject.getString("Name"));
                                    editor.putString("ResisteredMobileNo", mobileNumber);
                                    editor.commit();
                                    Intent intent = new Intent(context, MoneyTransferActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("SenderLimit_Detail_BeniList", object.toString());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AddBeneficiaryActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(AddBeneficiaryActivity.this, "Server Error : ", Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Bankname = data.getStringExtra("Bankname");
            position=  BanknameList.indexOf(Bankname);
            Bankcode=BankcodeList.get(position);
            et_searchbank.setText(Bankname);
            //editIFSCcode.setText(Bankcode);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Please select your bank...", Toast.LENGTH_SHORT).show();
        }



    }

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