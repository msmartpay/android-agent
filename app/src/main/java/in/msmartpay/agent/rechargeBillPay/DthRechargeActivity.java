package in.msmartpay.agent.rechargeBillPay;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import in.msmartpay.agent.R;
import in.msmartpay.agent.location.GPSTrackerPresenter;
import in.msmartpay.agent.rechargeBillPay.plans.PlansActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.RandomNumber;
import in.msmartpay.agent.utility.Util;

public class DthRechargeActivity extends BaseActivity implements GPSTrackerPresenter.LocationListener {
    public static final int REQ_LOCATION_CODE = 9001;
    private static final String TAG = DthRechargeActivity.class.getSimpleName();
    private static final int REQUEST_PLAN = 0212;
    private LinearLayout linear_proceed_dth;
    private EditText edit_account_no_dth, edit_amount_dth;
    private TextView tv_view_plan, tv_view_offer, tv_cust_details, tv_details;

    private Spinner spinner_oprater_dth;
    private ProgressDialog pd;
    private Context context;
    private String dth_url = HttpURL.RECHARGE_URL;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private String cust_details_url = HttpURL.PLAN_VIEW;
    private ArrayList<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;
    private String opcode = null;

    private GPSTrackerPresenter gpsTrackerPresenter = null;
    private boolean isTxnClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dth_recharge_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("DTH");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = DthRechargeActivity.this;
        gpsTrackerPresenter = new GPSTrackerPresenter(this, this, GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        edit_account_no_dth = findViewById(R.id.edit_account_no_dth);
        edit_amount_dth = findViewById(R.id.edit_amount_dth);
        spinner_oprater_dth = findViewById(R.id.spinner_oprater_dth);
        linear_proceed_dth = (LinearLayout) findViewById(R.id.linear_proceed_dth);
        tv_view_plan = findViewById(R.id.tv_view_plan);
        tv_view_offer = findViewById(R.id.tv_view_offer);
        tv_cust_details = findViewById(R.id.tv_cust_details);
        tv_details = findViewById(R.id.tv_details);
        //For OperatorList
        try {
            operatorsCodeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_cust_details.setOnClickListener(v -> {
            if (isConnectionAvailable()) {
                customerDetailsRequest();
            } else {
                Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
            }
        });
        edit_account_no_dth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() > 5) {
                    if (spinner_oprater_dth.getSelectedItem() != null)
                        tv_view_offer.setVisibility(View.VISIBLE);
                    tv_cust_details.setVisibility(View.VISIBLE);
                } else {
                    tv_view_offer.setVisibility(View.GONE);
                    tv_cust_details.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        spinner_oprater_dth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1) {
                    listModel = OperatorList.get(position);
                    opcode = listModel.getOpCode();
                    tv_view_plan.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(edit_account_no_dth.getText().toString()) && edit_account_no_dth.getText().toString().length() > 5) {
                        tv_view_offer.setVisibility(View.VISIBLE);
                        tv_cust_details.setVisibility(View.VISIBLE);
                    } else {
                        tv_view_offer.setVisibility(View.GONE);
                        tv_cust_details.setVisibility(View.GONE);
                    }
                } else {
                    tv_view_plan.setVisibility(View.GONE);
                    tv_view_offer.setVisibility(View.GONE);
                    tv_cust_details.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        linear_proceed_dth.setOnClickListener(view -> {
            if (isConnectionAvailable()) {

                if (spinner_oprater_dth.getSelectedItem() == null) {
                    Toast.makeText(context, "Select Operator !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_account_no_dth.getText().toString().trim()) && edit_account_no_dth.getText().toString().trim().length() < 5) {
                    edit_account_no_dth.requestFocus();
                    Toast.makeText(context, "Enter Account Number !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_amount_dth.getText().toString().trim())) {
                    edit_amount_dth.requestFocus();
                    Toast.makeText(context, "Enter Amount !!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isTxnClick) {
                        isTxnClick = true;
                        gpsTrackerPresenter.checkGpsOnOrNot(GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);
                    }
                }
            } else {
                Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
            }
        });


        tv_view_plan.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlansActivity.class);
            intent.putExtra("type", "dth");
            intent.putExtra("operator", listModel.getDisplayName());
            intent.putExtra("mobile", "");
            startActivityForResult(intent, REQUEST_PLAN);
        });
        tv_view_offer.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlansActivity.class);
            intent.putExtra("type", "dth");
            intent.putExtra("operator", listModel.getDisplayName());
            intent.putExtra("mobile", edit_account_no_dth.getText().toString());
            startActivityForResult(intent, REQUEST_PLAN);
        });
    }

    private void startRechargeProcess() {
       /* if (latitude.isEmpty() || longitude.isEmpty()) {
            startActivityForResult(new Intent(getApplicationContext(), LocationResultActivity.class), REQ_LOCATION_CODE);
        } else {*/
        proceedConfirmationDialog();
        // }
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
                    .put("service", "dth");

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
                                    L.m2("Response--operators1", data.toString());
                                    JSONArray operatorJsonArray = data.getJSONArray("data");
                                    OperatorList = new ArrayList<>();

                                    for (int i = 0; i < operatorJsonArray.length(); i++) {
                                        JSONObject obj = operatorJsonArray.getJSONObject(i);

                                        if (obj.getString("Service").equalsIgnoreCase("Dth")) {
                                            OperatorListModel operatorListModel = new OperatorListModel();
                                            //operatorListModel.setSubService(obj.getString("SubService"));
                                            operatorListModel.setBillFetch(obj.getString("BillFetch"));
                                            operatorListModel.setService(obj.getString("Service"));
                                            operatorListModel.setOperatorName(obj.getString("OperatorName"));
                                            operatorListModel.setDisplayName(obj.getString("DisplayName"));
                                            operatorListModel.setOpCode(obj.getString("OpCode"));
                                            opcode = obj.getString("OpCode");
                                            /*editor.putString("OpCode",obj.getString("OpCode"));
                                            editor.commit();*/
                                            OperatorList.add(operatorListModel);
                                        }

                                        operatorAdaptor = new CustomOperatorClass(context, OperatorList);
                                        spinner_oprater_dth.setAdapter(operatorAdaptor);
                                        if (OperatorList.size() == 0) {
                                            Toast.makeText(context, "No Operator Available!", Toast.LENGTH_SHORT).show();
                                        }
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

        TextView confirm_mobile, confirm_operator, confirm_amount, tv_cancel, tv_recharge, tv_mobile_dth;
        confirm_mobile = (TextView) d.findViewById(R.id.confirm_mobile);
        confirm_operator = (TextView) d.findViewById(R.id.confirm_operator);
        confirm_amount = (TextView) d.findViewById(R.id.confirm_amount);
        tv_cancel = (TextView) d.findViewById(R.id.tv_cancel);
        tv_recharge = (TextView) d.findViewById(R.id.tv_recharge);
        tv_mobile_dth = (TextView) d.findViewById(R.id.tv_rename);

        tv_mobile_dth.setText("DTH Number");
        confirm_mobile.setText(edit_account_no_dth.getText().toString());
        confirm_operator.setText(listModel.getOperatorName());
        confirm_amount.setText(edit_amount_dth.getText().toString());

        tv_recharge.setOnClickListener(view -> {
            try {
                dthRechargeRequest();
                d.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tv_cancel.setOnClickListener(v -> d.cancel());

        d.show();
    }

    //===========postpaidRechargeRequest==============

    private void dthRechargeRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject();
            jsonObjectReq.put("agent_id", agentID);
            jsonObjectReq.put("txn_key", txn_key);
            jsonObjectReq.put("service", "dth");
            jsonObjectReq.put("operator", listModel.getCode());
            jsonObjectReq.put("mobile_no", edit_account_no_dth.getText().toString().trim());
            jsonObjectReq.put("amount", edit_amount_dth.getText().toString().trim());
            jsonObjectReq.put("request_id", RandomNumber.getTranId_14());
            jsonObjectReq.put("OpCode", opcode);
            jsonObjectReq.put("latitude", Util.LoadPrefData(context, getString(R.string.latitude)));
            jsonObjectReq.put("longitude", Util.LoadPrefData(context, getString(R.string.longitude)));
            jsonObjectReq.put("ip", Util.getIpAddress(context));

            L.m2("url-dth", dth_url);
            L.m2("Request--dth", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, dth_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--dth", data.toString());
                            try {
                                if (data.get("response-code") != null && (data.get("response-code").equals("0") || data.get("response-code").equals("1"))) {
                                    Intent in = new Intent(context, SuccessDetailActivity.class);
                                    in.putExtra("responce", data.get("response-message").toString());
                                    in.putExtra("mobileno", edit_account_no_dth.getText().toString().trim());
                                    in.putExtra("requesttype", "dth");
                                    in.putExtra("operator", listModel.getOperatorName());
                                    in.putExtra("amount", edit_amount_dth.getText().toString().trim());
                                    startActivity(in);
                                    finish();
                                } else if (data.get("Status") != null && data.get("Status").equals("2")) {
                                    Toast.makeText(context, data.getString("response-message").toString(), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(context, "Unable To Process Your Request. Please try later.", Toast.LENGTH_SHORT).show();
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

    private void customerDetailsRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("type", "dthinfo")
                    .put("client", HttpURL.CLIENT)
                    .put("operator", listModel.getDisplayName())
                    .put("mobile", edit_account_no_dth.getText().toString().trim());

            L.m2("url-cust", cust_details_url);
            L.m2("Request--cust", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, cust_details_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            try {
                                L.m2("Response--cust", data.toString());

                                if (data.getString("status") != null && (data.getString("status").equals("1"))) {
                                    StringBuilder builder = new StringBuilder();
                                    tv_details.setVisibility(View.VISIBLE);

                                    JSONArray array = data.getJSONArray("records");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        if (object.has("customerName"))
                                            builder.append("Name : " + object.getString("customerName"));
                                        if (object.has("status"))
                                            builder.append(", Status : " + object.getString("status"));
                                        if (object.has("MonthlyRecharge"))
                                            builder.append(", Monthly Recharge : " + object.getString("MonthlyRecharge"));
                                        if (object.has("lastrechargeamount"))
                                            builder.append(", Last Recharge Amount : " + object.getString("lastrechargeamount"));
                                        if (object.has("lastrechargedate"))
                                            builder.append(", Last Recharge Date : " + object.getString("lastrechargedate"));
                                        if (object.has("planname"))
                                            builder.append(", Plan Name : " + object.getString("planname"));
                                    }
                                    tv_details.setText(builder.toString());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLAN && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getStringExtra("price") != null)
                    edit_amount_dth.setText(data.getStringExtra("price"));
            }
        }
        if (requestCode == GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE && resultCode == Activity.RESULT_OK) {
            gpsTrackerPresenter.onStart();
        }
    }

    //====================================
    //--------------------------------------------GPS Tracker--------------------------------------------------------------

    @Override
    public void onLocationFound(Location location) {
        gpsTrackerPresenter.stopLocationUpdates();
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
