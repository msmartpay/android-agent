package in.msmartpay.agent.dmr2Moneytrasfer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.Util;

/**
 * Created by Smartkinda on 7/5/2017.
 */

public class AddBeneficiaryActivity extends BaseActivity {

    /*private Spinner spinnerBankList;*/
    private EditText et_searchbank;

    private EditText editIFSCcode, editAccountNo, editConfirmAccountNo, edit_sender_mobile, edit_beneficiary_mobile, edit_beneficiary_name;
    private TextInputLayout til_ifsc;
   private TextView tv_bank_message;
    private Button btnAddBeneficiary;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private ArrayList<BankListModel> BankListArray = null;
    private CustomAdaptorClass bankListAdaptor;
    private String url_bank_list = HttpURL.GET_BANK_LIST_Dmr2;
    private String url_bank_details = HttpURL.BankDetails_Dmr2;
    private String url_verify_add_beneficiary = HttpURL.VERIFY_ADD_BENEFICIARY_Dmr2;
    private String url_add_bene_by_ifsc = HttpURL.ADD_BENEFICIARY_IFSC_CODE_Dmr2;
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
    private String Bankname = "", Bankcode = "";
    private String checked;
    int position = 0;
    private boolean ifscrequired;
    private String isverificationavailable, available_channels, ifsc_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr2_add_beneficiary_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Add Beneficiary");

        context = AddBeneficiaryActivity.this;
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener((v, event) -> {
            // TODO Auto-generated method stub
            v.requestFocusFromTouch();
            return false;
        });

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);

        /* spinnerBankList =  findViewById(R.id.spinner_transfer_type);*/
        et_searchbank = findViewById(R.id.et_searchbank);
        edit_sender_mobile = findViewById(R.id.edit_sender_mobile);
        edit_sender_mobile.setVisibility(View.GONE);
        editIFSCcode = findViewById(R.id.edit_ifsc_code);
        til_ifsc = findViewById(R.id.til_ifsc);
        editAccountNo = findViewById(R.id.edit_account_no);
        editConfirmAccountNo = findViewById(R.id.edit_confirm_account_no);
        edit_beneficiary_mobile = findViewById(R.id.edit_beneficiary_mobile);
        edit_beneficiary_name = findViewById(R.id.edit_beneficiary_name);
        btnAddBeneficiary = findViewById(R.id.btn_add_beneficiary);
        addwithverify = findViewById(R.id.addwithverify);
        tv_bank_message = findViewById(R.id.tv_bank_message);

        edit_sender_mobile.setText(mobileNumber);


        et_searchbank.setOnClickListener(v -> {

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
        });


        /*BankList Request*/
        try {
            getBankListRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        addwithverify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // update your model (or other business logic) based on isChecked
            if (isChecked) {
                if (ifscrequired && TextUtils.isEmpty(editIFSCcode.getText().toString().trim())) {
                    addwithverify.setChecked(false);
                    editIFSCcode.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter IFSC Code!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(editAccountNo.getText().toString().trim())) {
                    addwithverify.setChecked(false);
                    editAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Account Number!", Toast.LENGTH_SHORT).show();
                } else if (!editConfirmAccountNo.getText().toString().equals(editAccountNo.getText().toString())) {
                    addwithverify.setChecked(false);
                    editConfirmAccountNo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Account No.!", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmationDialog(3, "Account verification fee will be applied on auto fetch beneficiary name");
                }
            }
        });
        btnAddBeneficiary.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (TextUtils.isEmpty(editAccountNo.getText().toString().trim())) {
                editAccountNo.requestFocus();
                Toast.makeText(getApplicationContext(), "Please Enter Account Number!", Toast.LENGTH_SHORT).show();
            } else if (!editConfirmAccountNo.getText().toString().equals(editAccountNo.getText().toString())) {
                editConfirmAccountNo.requestFocus();
                Toast.makeText(getApplicationContext(), "Please Enter Correct Account No.!", Toast.LENGTH_SHORT).show();
            } /*else if (TextUtils.isEmpty(edit_beneficiary_mobile.getText().toString())) {
                edit_beneficiary_mobile.requestFocus();
                Toast.makeText(getApplicationContext(), "Please Enter 10 digit Mobile No.!", Toast.LENGTH_SHORT).show();
            } */ else if (TextUtils.isEmpty(edit_beneficiary_name.getText().toString())) {
                edit_beneficiary_name.requestFocus();
                Toast.makeText(getApplicationContext(), "Please Enter Bene Name!", Toast.LENGTH_SHORT).show();
            } else {
                if (addwithverify.isChecked()) {
                    checked = "Y";
                } else {
                    checked = "N";
                }
                showConfirmationDialog(1, "");
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
                                BanknameList = new ArrayList<>();
                                BankcodeList = new ArrayList<>();
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
                }, new Response.ErrorListener() {
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
        d.setContentView(R.layout.dmr1_confirmation_dialog);

        final Button btnSubmit = d.findViewById(R.id.btn_push_submit);
        final Button btn_no = d.findViewById(R.id.btn_resend_otp);
        final TextView tvConfirmation = (TextView) d.findViewById(R.id.tv_confirmation_dialog);

        if (i == 1) {
            tvConfirmation.setText("Are you sure, you want to add Beneficiary!");
        }
        if (i == 2 || i == 3) {
            tvConfirmation.setText(msg);
        }

        btnSubmit.setOnClickListener(v -> {
            if (i == 1) {
                verifyAddBeneficiaryRequest();
                d.dismiss();
            }

            if (i == 2) {
                findSenderRequest();
                d.dismiss();
            }
            if (i == 3) {
                verifyAccountRequest();
                d.dismiss();
            }
        });

        btn_no.setOnClickListener(view -> d.dismiss());

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
                    .put("BankName", Bankname/* spinnerSelectedBank*/)
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
                            MainActivity.jsonObjectStatic = object;
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

    //Json request for add Beneficiary
    private void verifyAccountRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber)
                    .put("IFSC", editIFSCcode.getText().toString())
                    .put("BankAccount", editAccountNo.getText().toString().trim())
                    .put("BankCode", Bankcode/*listModel.getBankCode()*/)
                    .put("BankName", Bankname/* spinnerSelectedBank*/)
                    .put("REQUEST_ID", String.valueOf((long) Math.floor(Math.random() * 90000000000000L) + 10000000000000L));

            L.m2("Request--byIfsc", jsonObjectReq.toString());
            L.m2("url", url_add_bene_by_ifsc);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_add_bene_by_ifsc, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("addBeneByIfsc-->" + object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    if (object.has("BeneName"))
                                        edit_beneficiary_name.setText(object.getString("BeneName"));
                                } else {
                                    Toast.makeText(context, object.has("BeneName") ? object.getString("message") : "No Message", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    private void getBankDetails() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber)
                    .put("BankCode", Bankcode);
            L.m2("Request--byIfsc", jsonObjectReq.toString());
            L.m2("url", url_bank_details);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_bank_details, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("addBeneByIfsc-->" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {

                                    isverificationavailable = object.getString("isverificationavailable");
                                    available_channels = object.getString("available_channels");
                                    ifsc_status = object.getString("ifsc_status");

                                    if ("1".equalsIgnoreCase(isverificationavailable)) {
                                        if ("4".equalsIgnoreCase(ifsc_status)) {
                                            //  respMsg="IFSCRequired";
                                            ifscrequired = true;

                                            tv_bank_message.setText("IFSC code required. Enter IFSC Code");
                                            til_ifsc.setHint("IFSC Required");
                                            editIFSCcode.setEnabled(true);

                                        } else {

                                            tv_bank_message.setText("IFSC code not required.");
                                            til_ifsc.setHint("IFSC Code (optional)");
                                            editIFSCcode.setEnabled(false);
                                        }
                                    } else {
                                        tv_bank_message.setText("Bank is not available for account verification.");
                                    }
                                } else {
                                    ifscrequired = true;
                                    tv_bank_message.setText("IFSC code required. Enter IFSC Code");
                                    til_ifsc.setHint("IFSC Required");
                                    editIFSCcode.setEnabled(true);
                                }
                                Util.showView(tv_bank_message);
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
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
            position = BanknameList.indexOf(Bankname);
            Bankcode = BankcodeList.get(position);
            et_searchbank.setText(Bankname);
            //editIFSCcode.setText(Bankcode);
            getBankDetails();
        } catch (Exception e) {
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