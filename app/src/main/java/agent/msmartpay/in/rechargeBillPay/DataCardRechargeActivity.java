package agent.msmartpay.in.rechargeBillPay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;
import agent.msmartpay.in.utility.RandomNumber;
import agent.msmartpay.in.utility.TextDrawable;

public class DataCardRechargeActivity extends BaseActivity {

    private LinearLayout linear_proceed_data;
    private EditText edit_customer_id_data, edit_amount_data;
    private Spinner spinner_oprater_data;
    private String operatorData;
    private ProgressDialog pd;
    private Context context;
    private String datacard_Url = HttpURL.RECHARGE_URL;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private ArrayList<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;

   /* private SharedPreferences sharedPreferencesOpcode;
    private SharedPreferences.Editor editor;
    */
    private String opcode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_card_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Data Card");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = DataCardRechargeActivity.this;

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        edit_customer_id_data =  findViewById(R.id.edit_customer_id_data);
        edit_amount_data =  findViewById(R.id.edit_amount_data);
        spinner_oprater_data =  findViewById(R.id.spinner_oprater_data);
        linear_proceed_data = (LinearLayout) findViewById(R.id.linear_proceed_data);

        String code1 = "\u20B9   ";
        edit_amount_data.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
        edit_amount_data.setCompoundDrawablePadding(code1.length() * 10);


        // Arrays.sort(OperatorCode.datacard_key);
       /* ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_textview_layout, OperatorCode.datacard_key);
        adapter.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinner_oprater_data.setAdapter(adapter);*/

        //For OperatorList
        try {
            operatorsCodeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*sharedPreferencesOpcode = getSharedPreferences("opcode", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        opcode = sharedPreferencesOpcode.getString("OpCode","");*/

        spinner_oprater_data.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if(position>-1)
                  listModel = OperatorList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        linear_proceed_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectionAvailable()) {
                    if (spinner_oprater_data.getSelectedItem() == null) {
                        Toast.makeText(context, "Select Operator !!!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(edit_customer_id_data.getText().toString().trim()) || edit_customer_id_data.getText().toString().trim().length() < 10) {
                        edit_customer_id_data.requestFocus();
                        Toast.makeText(context, "Enter Account Number !!!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(edit_amount_data.getText().toString().trim())) {
                        edit_amount_data.requestFocus();
                        Toast.makeText(context, "Enter Amount !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        proceedConfirmationDialog();
                    }
                } else {
                    Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
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
                    .put("service","datacard");

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

                                        if (obj.getString("Service").equalsIgnoreCase("datacard")) {
                                            OperatorListModel operatorListModel = new OperatorListModel();
                                            //operatorListModel.setSubService(obj.getString("SubService"));
                                            operatorListModel.setBillFetch(obj.getString("BillFetch"));
                                            operatorListModel.setService(obj.getString("Service"));
                                            operatorListModel.setOperatorName(obj.getString("OperatorName"));
                                            operatorListModel.setDisplayName(obj.getString("DisplayName"));
                                            operatorListModel.setOpCode(obj.getString("OpCode"));

                                            /*editor.putString("OpCode",obj.getString("OpCode"));
                                            editor.commit();*/
                                            OperatorList.add(operatorListModel);
                                        }
                                    }

                                    operatorAdaptor = new CustomOperatorClass(context, OperatorList);
                                    spinner_oprater_data.setAdapter(operatorAdaptor);
                                    if (OperatorList.size()==0){
                                        Toast.makeText(context, "No Operator Available!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, data.getString("response-message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
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

        tv_mobile_dth.setText("Customer ID");
        confirm_mobile.setText(edit_customer_id_data.getText().toString());
        confirm_operator.setText(listModel.getOperatorName());
        confirm_amount.setText(edit_amount_data.getText().toString());

        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DataCardRequest();
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

    //===========DataCardRequest==============

    private void DataCardRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("service", "datacard")
                    .put("operator", listModel.getCode())
                    .put("mobile_no", edit_customer_id_data.getText().toString().trim())
                    .put("amount", edit_amount_data.getText().toString().trim())
                    .put("request_id", RandomNumber.getTranId_14())
                    .put("operator", listModel.getOpCode());

            L.m2("url-data", datacard_Url);
            L.m2("Request--data", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, datacard_Url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--data", data.toString());
                            try {
                                if (data.get("response-code") != null && (data.get("response-code").equals("0") || data.get("response-code").equals("1"))) {
                                    Intent in = new Intent(context, SuccessDetailActivity.class);
                                    in.putExtra("responce", data.get("response-message").toString());
                                    in.putExtra("mobileno", edit_customer_id_data.getText().toString().trim());
                                    in.putExtra("requesttype", "datacard");
                                    in.putExtra("operator", listModel.getOperatorName());
                                    in.putExtra("amount", edit_amount_data.getText().toString().trim());
                                    startActivity(in);
                                    finish();
                                } else if (data.get("response-code") != null && data.get("response-code").equals("2")) {
                                    Toast.makeText(context, data.getString("response-code").toString(), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(context, "Unable To Process Your Request. Please try later.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
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
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }
    }

    //====================================
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
