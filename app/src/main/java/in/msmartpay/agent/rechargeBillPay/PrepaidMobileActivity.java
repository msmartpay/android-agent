package in.msmartpay.agent.rechargeBillPay;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.location.GPSTrackerPresenter;
import in.msmartpay.agent.rechargeBillPay.plans.PlansActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.RandomNumber;
import in.msmartpay.agent.utility.Service;
import in.msmartpay.agent.utility.Util;

public class PrepaidMobileActivity extends BaseActivity implements GPSTrackerPresenter.LocationListener {
    public static final int REQ_LOCATION_CODE = 9001;
    private static final String TAG = PrepaidMobileActivity.class.getSimpleName();
    private String latitude = "";
    private String longitude = "";
    private static final int REQUEST_PLAN = 0212;
    private LinearLayout linear_proceed;
    private EditText edit_prepaid_mobile, edit_amount;
    private TextView tv_view_plan, tv_view_offer;
    private Spinner spinner_oprater;
    private ImageView image_contactlist;
    private String operatorData;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private ProgressDialog pd;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private String recharge_Url = HttpURL.RECHARGE_URL;
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private ArrayList<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;
    private String opcode = null;

    private GPSTrackerPresenter gpsTrackerPresenter = null;
    private boolean isTxnClick = false;
    private boolean isLocationGet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_prepaid_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Prepaid");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = PrepaidMobileActivity.this;
        gpsTrackerPresenter = new GPSTrackerPresenter(this, this, GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);

        edit_prepaid_mobile = findViewById(R.id.edit_prepaid_mobile);
        edit_amount = findViewById(R.id.edit_amount);
        spinner_oprater = findViewById(R.id.spinner_oprater);
        image_contactlist = (ImageView) findViewById(R.id.image_contactlist);
        linear_proceed = (LinearLayout) findViewById(R.id.linear_proceed);
        tv_view_plan = findViewById(R.id.tv_view_plan);
        tv_view_offer = findViewById(R.id.tv_view_offer);

        image_contactlist.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_CONTACTS);
        });

        //For OperatorList
        try {
            operatorsCodeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        edit_prepaid_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() == 10) {
                    if (spinner_oprater.getSelectedItem() != null)
                        tv_view_offer.setVisibility(View.VISIBLE);
                } else {
                    tv_view_offer.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        spinner_oprater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1) {
                    listModel = OperatorList.get(position);
                    opcode = listModel.getOpCode();
                    tv_view_plan.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(edit_prepaid_mobile.getText().toString()) && edit_prepaid_mobile.getText().toString().length() == 10) {
                        tv_view_offer.setVisibility(View.VISIBLE);
                    } else {
                        tv_view_offer.setVisibility(View.GONE);
                    }
                } else {
                    tv_view_plan.setVisibility(View.GONE);
                    tv_view_offer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        linear_proceed.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                if (TextUtils.isEmpty(edit_prepaid_mobile.getText().toString().trim()) || edit_prepaid_mobile.getText().toString().trim().length() < 10) {
                    edit_prepaid_mobile.requestFocus();
                    Toast.makeText(context, "Enter 10 digit mobile no. !!!", Toast.LENGTH_SHORT).show();
                } else if (spinner_oprater.getSelectedItem() == null) {
                    Toast.makeText(context, "Select Operator !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_amount.getText().toString().trim())) {
                    edit_amount.requestFocus();
                    Toast.makeText(context, "Enter Amount !!!", Toast.LENGTH_SHORT).show();
                } else {
                    startRechargeProcess();
                }
            } else {
                Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
            }
        });

        tv_view_plan.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlansActivity.class);
            intent.putExtra("type", "mobile");
            intent.putExtra("operator", listModel.getDisplayName());
            intent.putExtra("mobile", "");
            startActivityForResult(intent, REQUEST_PLAN);
        });
        tv_view_offer.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlansActivity.class);
            intent.putExtra("type", "mobile");
            intent.putExtra("operator", listModel.getDisplayName());
            intent.putExtra("mobile", edit_prepaid_mobile.getText().toString());
            startActivityForResult(intent, REQUEST_PLAN);
        });
    }

    private void startRechargeProcess() {
        /*if (Util.isPowerSaveMode(context)) {
            proceedConfirmationDialog();
        } else {*/
            if (isLocationGet) {
                proceedConfirmationDialog();
            } else {
                if (!isTxnClick) {
                    isTxnClick = true;
                    gpsTrackerPresenter.checkGpsOnOrNot(GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);
                }
            }
        //}
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
                    .put("service", "mobile");
            L.m2("url-operators", operator_code_url);
            L.m2("Request--operators", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, operator_code_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--operators", data.toString());
                            try {
                                if (data.getString("response-code") != null && (data.getString("response-code").equals("0"))) {
                                    JSONArray operatorJsonArray = data.getJSONArray("data");
                                    OperatorList = new ArrayList<>();

                                    for (int i = 0; i < operatorJsonArray.length(); i++) {
                                        JSONObject obj = operatorJsonArray.getJSONObject(i);

                                        if (obj.getString("Service").equalsIgnoreCase("mobile")) {
                                            OperatorListModel operatorListModel = new OperatorListModel();
                                            //operatorListModel.setSubService(obj.getString("SubService"));
                                            operatorListModel.setBillFetch(obj.getString("BillFetch"));
                                            operatorListModel.setService(obj.getString("Service"));
                                            operatorListModel.setOperatorName(obj.getString("OperatorName"));
                                            operatorListModel.setDisplayName(obj.getString("DisplayName"));
                                            operatorListModel.setCode(obj.getString("OpCode"));
                                            operatorListModel.setOpCode(obj.getString("OpCode"));
                                            opcode = obj.getString("OpCode");
                                            /*editor.putString("OpCode",obj.getString("OpCode"));
                                            editor.commit();*/
                                            OperatorList.add(operatorListModel);
                                        }
                                    }
                                    operatorAdaptor = new CustomOperatorClass(context, OperatorList);
                                    spinner_oprater.setAdapter(operatorAdaptor);
                                    if (OperatorList.size() == 0) {
                                        Toast.makeText(context, "No Operator Available!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, data.getString("response-message"), Toast.LENGTH_SHORT).show();
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

        confirm_mobile.setText(edit_prepaid_mobile.getText().toString());
        confirm_operator.setText(listModel.getDisplayName());
        confirm_amount.setText(edit_amount.getText().toString());

        tv_recharge.setOnClickListener(view -> {
            try {
                prepaidRechargeRequest();
                d.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tv_cancel.setOnClickListener(v -> d.cancel());

        d.show();
    }

    //===========prepaidRechargeRequest==============
    private void prepaidRechargeRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject();
            jsonObjectReq.put("agent_id", agentID);
            jsonObjectReq.put("txn_key", txn_key);
            jsonObjectReq.put("service", "Mobile");
            jsonObjectReq.put("operator", opcode);
            jsonObjectReq.put("mobile_no", edit_prepaid_mobile.getText().toString().trim());
            jsonObjectReq.put("amount", edit_amount.getText().toString().trim());
            jsonObjectReq.put("request_id", RandomNumber.getTranId_14());
            jsonObjectReq.put("OpCode", opcode);
            jsonObjectReq.put("power_mode", Util.LoadPrefBoolean(context, Keys.POWER_MODE));
            jsonObjectReq.put("latitude", Util.LoadPrefData(context, getString(R.string.latitude)));
            jsonObjectReq.put("longitude", Util.LoadPrefData(context, getString(R.string.longitude)));
            jsonObjectReq.put("ip", Util.getIpAddress(context));


            L.m2("url-prepaid", recharge_Url);
            L.m2("Request--prepaid", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, recharge_Url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--prepaid", data.toString());
                            try {
                                // if (data.get("Status") != null && (data.get("Status").equals("0") || data.get("Status").equals("2") || data.get("Status").equals("1"))) {
                                if (data.get("response-code") != null && (data.get("response-code").equals("0") || data.get("response-code").equals("2") || data.get("response-code").equals("1"))) {
                                    Intent in = new Intent(context, SuccessDetailActivity.class);
                                    in.putExtra("responce", data.getString("response-message"));
                                    in.putExtra("mobileno", edit_prepaid_mobile.getText().toString().trim());
                                    in.putExtra("requesttype", "prepaid-mobile");
                                    in.putExtra("operator", listModel.getDisplayName());
                                    in.putExtra("amount", edit_amount.getText().toString().trim());
                                    startActivity(in);
                                    finish();
                                } else {
                                    Toast.makeText(context, data.getString("response-message"), Toast.LENGTH_SHORT).show();
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
        } else if (requestCode == REQUEST_PLAN && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getStringExtra("price") != null)
                    edit_amount.setText(data.getStringExtra("price"));
            }
        }
        if (requestCode == GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE && resultCode == Activity.RESULT_OK) {
            gpsTrackerPresenter.onStart();
        }
    }

    private void retrieveContactNumber() {

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
            }
        } else {
            getContact();
        }*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACTS: {
               /* if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        getContact();
                    }
                } else {
                    Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT).show();
                }*/
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
                    edit_prepaid_mobile.setText(correctno);
                } else {
                    Toast.makeText(context, "Invalid Contact Details", Toast.LENGTH_LONG).show();
                }
            }
            phones.close();
            L.m2("Contact", "Contact Phone Number: " + contactNumber);
        }
    }

    //--------------------------------------------GPS Tracker--------------------------------------------------------------

    @Override
    public void onLocationFound(Location location) {
        gpsTrackerPresenter.stopLocationUpdates();
        isLocationGet = true;
        if (isTxnClick) {
            isTxnClick = false;
            startRechargeProcess();
        }
    }

    @Override
    public void locationError(String msg) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        gpsTrackerPresenter.onStart();
    }

//--------------------------------------------End GPS Tracker--------------------------------------------------------------


    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsTrackerPresenter.onPause();
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
