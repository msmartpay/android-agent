package in.msmartpay.agent.rechargeBillPay;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.RandomNumber;
import in.msmartpay.agent.utility.Service;

public class PostpaidMobileActivity extends BaseActivity {
    private LinearLayout linear_proceed_postpaid;
    private EditText edit_postpaid_mobile, edit_amount_postpaid;
    private Spinner spinner_oprater_postpaid;
    private ImageView image_contactlist_postpaid;
    private String operatorData;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private ProgressDialog pd;
    private Context context;
    private String billpay_Url = HttpURL.BILL_PAY;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private List<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;

    private String mob, amt, operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postpaid_recharge_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Postpaid");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = PostpaidMobileActivity.this;

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        edit_postpaid_mobile = findViewById(R.id.edit_postpaid_mobile);
        edit_amount_postpaid = findViewById(R.id.edit_amount_postpaid);
        spinner_oprater_postpaid = findViewById(R.id.spinner_oprater_postpaid);
        image_contactlist_postpaid = findViewById(R.id.image_contactlist_postpaid);
        linear_proceed_postpaid = findViewById(R.id.linear_proceed_postpaid);

        String code = "+91      ";
        // edit_postpaid_mobile.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        //edit_postpaid_mobile.setCompoundDrawablePadding(code.length()*10);

        String code1 = "\u20B9   ";
        // edit_amount_postpaid.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
        // edit_amount_postpaid.setCompoundDrawablePadding(code1.length()*10);
        image_contactlist_postpaid.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_CONTACTS);
        });

        //For OperatorList
        operatorsCodeRequest();

        spinner_oprater_postpaid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1) {
                    listModel = OperatorList.get(position);
                    operator = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        linear_proceed_postpaid.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                mob = edit_postpaid_mobile.getText().toString();
                amt = edit_amount_postpaid.getText().toString();
                if (TextUtils.isEmpty(edit_postpaid_mobile.getText().toString().trim()) || edit_postpaid_mobile.getText().toString().trim().length() < 10) {
                    edit_postpaid_mobile.requestFocus();
                    Toast.makeText(context, "Enter 10 digit mobile no. !!!", Toast.LENGTH_SHORT).show();
                } else if (spinner_oprater_postpaid.getSelectedItem() == null) {
                    Toast.makeText(context, "Select Operator !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_amount_postpaid.getText().toString().trim())) {
                    edit_amount_postpaid.requestFocus();
                    Toast.makeText(context, "Enter Amount !!!", Toast.LENGTH_SHORT).show();
                } else {
                    //proceedConfirmationDialog();
                    Intent intent = new Intent(context, BillPayActivity.class);
                    intent.putExtra("CN", mob);
                    intent.putExtra("CN_hint", "Mobile Number");
                    intent.putExtra("Amt", amt);
                    intent.putExtra("Mob", "");
                    intent.putExtra("Add1", "");
                    intent.putExtra(getString(R.string.pay_operator_model), getGson().toJson(listModel));
                    startActivity(intent);
                }
            } else {
                Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //===========operatorCodeRequest==============
    private void operatorsCodeRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("service", "POSTPAID");

            L.m2("url-operators", operator_code_url);
            L.m2("Request--operators", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, operator_code_url, jsonObjectReq,
                    data -> {
                        pd.dismiss();
                        L.m2("Response--operators", data.toString());
                        try {
                            if (data.getString("response-code").equalsIgnoreCase("0")) {
                                L.m2("Response--operators1", data.toString());
                                JSONArray operatorJsonArray = data.getJSONArray("data");
                                OperatorList = new ArrayList<>();


                                //opratorList = new ArrayList();
                                //  JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < operatorJsonArray.length(); i++) {
                                    JSONObject object1 = operatorJsonArray.getJSONObject(i);
                                    OperatorListModel model = new OperatorListModel();
                                    model.setBillFetch(object1.getString("BillFetch"));
                                    model.setDisplayName(object1.getString("DisplayName"));
                                    model.setOpCode(object1.getString("OpCode"));
                                    model.setOperatorName(object1.getString("OperatorName"));
                                    model.setService(object1.getString("Service"));
                                    OperatorList.add(model);
                                }
                                operatorAdaptor = new CustomOperatorClass(context, OperatorList);
                                spinner_oprater_postpaid.setAdapter(operatorAdaptor);
                                if (OperatorList.size() == 0) {
                                    Toast.makeText(context, "No Operator Available!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, data.getString("response-message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                pd.dismiss();
                Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }
    }

    private void proceedConfirmationDialog() {
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView confirm_mobile, confirm_operator, confirm_amount, tv_cancel, tv_recharge;
        confirm_mobile = (TextView) d.findViewById(R.id.confirm_mobile);
        confirm_operator = (TextView) d.findViewById(R.id.confirm_operator);
        confirm_amount = (TextView) d.findViewById(R.id.confirm_amount);
        tv_cancel = (TextView) d.findViewById(R.id.tv_cancel);
        tv_recharge = (TextView) d.findViewById(R.id.tv_recharge);

        confirm_mobile.setText(edit_postpaid_mobile.getText().toString());
        confirm_operator.setText(listModel.getOperatorName());
        confirm_amount.setText(edit_amount_postpaid.getText().toString());

        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postpaidRechargeRequest();
                    d.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

        d.show();
    }

    //===========postpaidRechargeRequest==============
    private void postpaidRechargeRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("agent_id", agentID)
                    .put("CIR", "")
                    .put("txn_key", txn_key)
                    .put("ST", "Billpay")
                    .put("OP", listModel.getCode())
                    .put("reqType", "Billpay")
                    .put("CN", edit_postpaid_mobile.getText().toString().trim())
                    .put("AMT", edit_amount_postpaid.getText().toString().trim())
                    .put("AD1", "")
                    .put("AD2", "")
                    .put("AD3", "")
                    .put("AD4", "")
                    .put("request_id", RandomNumber.getTranId_14());

            L.m2("url-postpaid", billpay_Url);
            L.m2("Request--postpaid", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, billpay_Url, jsonObjectReq,
                    data -> {
                        pd.dismiss();
                        L.m2("Response--postpaid", data.toString());
                        try {
                            if (data.getString("response-code") != null && (data.getString("response-code").equals("0") || data.get("response-code").equals("1"))) {
                                Intent in = new Intent(context, SuccessDetailActivity.class);
                                in.putExtra("responce", data.get("response-message").toString());
                                in.putExtra("mobileno", edit_postpaid_mobile.getText().toString().trim());
                                in.putExtra("requesttype", "postpaid-mobile");
                                in.putExtra("operator", listModel.getOperatorName());
                                in.putExtra("amount", edit_amount_postpaid.getText().toString().trim());
                                startActivity(in);
                                finish();
                            } else if (data.get("Status") != null && data.get("Status").equals("2")) {
                                Toast.makeText(context, data.getString("response-message"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, "Unable To Process Your Request. Please try later.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                pd.dismiss();
                Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }
    }

    //====================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            L.m2("Contact", "Response: " + data.toString());
            uriContact = data.getData();
            retrieveContactNumber();
        }
    }

    private void retrieveContactNumber() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
            }
        } else {
            getContact();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        getContact();
                    }
                } else {
                    Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void getContact() {
        String contactNumber = null;
        Cursor c = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (c.moveToFirst()) {
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            phones.moveToFirst();
            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (cNumber.equals(null)) {
                Toast.makeText(context, "Invalid Number", Toast.LENGTH_LONG).show();
            } else if (cNumber.equals("") || cNumber.length() < 10) {

            }
            if (cNumber.length() >= 10) {
                Service sc = new Service();
                String correctno = sc.validateMobileNumber(cNumber);

                if (null != correctno) {
                    edit_postpaid_mobile.setText(correctno);
                } else {
                    Toast.makeText(context, "Invalid Contact Details", Toast.LENGTH_LONG).show();
                }
            }
            phones.close();
            L.m2("Contact", "Contact Phone Number: " + contactNumber);
        }
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
        return true;
    }
}
