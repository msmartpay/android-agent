package in.msmartpay.agent.rechargeBillPay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

public class GasPayActivity extends BaseActivity {

    private LinearLayout linear_proceed_gas;
   // private EditText edit_consumer_no_gas, edit_amount_gas, edit_email_gas, edit_contact_gas,et_add1;
   EditText consumer_no, amount, contact_no,email,et_add1;
    //private Spinner spinner_oprater_gas;
    Spinner gas_spinner;
    private String operatorData;
    private ProgressDialog pd;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private String gas_url= HttpURL.BILL_PAY;
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private ArrayList<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;
    private String mob,amt,Email,consumerno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_bill_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gas");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = GasPayActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        amount =  findViewById(R.id.id_gas_amount);
       // email=findViewById(R.id.id_gas_email);
        contact_no =  findViewById(R.id.id_gas_contactno);
        consumer_no =  findViewById(R.id.id_gas_consumer_no);
        gas_spinner =  findViewById(R.id.id_gas_spinner);
        linear_proceed_gas = (LinearLayout) findViewById(R.id.linear_proceed_gas);
        et_add1 = findViewById(R.id.et_add1);
        try {
            operatorsCodeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        linear_proceed_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnectionAvailable()) {
                    //listModel = new OperatorListModel();
                   // listModel = (OperatorListModel) spinner_oprater_gas.getSelectedItem();

                    mob=contact_no.getText().toString();
                    //  Email=email.getText().toString();
                    consumerno = consumer_no.getText().toString();
                    amt = amount.getText().toString();
                    if(gas_spinner.getSelectedItem()==null){
                        Toast.makeText(GasPayActivity.this, "Select Operator", Toast.LENGTH_LONG).show();
                    }else
                    if ("".equals(consumerno) || consumerno.length() < 5) {
                        Toast.makeText(GasPayActivity.this, "Enter Valid Connection Number", Toast.LENGTH_LONG).show();
                    } else if ("".equals(amt) || Double.parseDouble(amt)<10) {
                        Toast.makeText(GasPayActivity.this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                    }/*else if ("".equals(Email)) {
                    Toast.makeText(GasPayment.this, "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                }
                else if (!Service.isValidEmail(Email)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                }*/

                    else if ("".equals(mob)) {
                        Toast.makeText(GasPayActivity.this, "Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent=new Intent(context,BillPayActivity.class);
                        intent.putExtra("CN",consumerno);
                        intent.putExtra("CN_hint",consumer_no.getHint().toString());
                        intent.putExtra("Amt",amt);
                        intent.putExtra("Mob",mob);
                        intent.putExtra("Add1",et_add1.getText().toString());
                        intent.putExtra(getString(R.string.pay_operator_model),getGson().toJson(listModel));
                        startActivity(intent);

                    }
                }else{
                    Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        gas_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>-1) {
                    listModel=OperatorList.get(position);
                    String op = listModel.getDisplayName();
                    et_add1.setVisibility(View.GONE);
                    if(op.equals("Mahanagar Gas"))
                    {
                        consumer_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
                        consumer_no.setHint("CA Number");
                        et_add1.setVisibility(View.VISIBLE);
                        et_add1.setHint("Bill Group Number");

                    }
                    else if(op.equals("Adani Gas - GUJARAT") || op.equals("Adani Gas - HARYANA" )|| op.equals("Gujarat Gas" )|| op.equals("Sabarmati Gas"))
                    {
                        consumer_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                        consumer_no.setHint("Customer ID");
                    }
                    else if(op.equals("Haryana City Gas")){
                        consumer_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                        consumer_no.setHint("CRN Number");
                    }else if(op.equals("Indraprastha Gas")){
                        consumer_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                        consumer_no.setHint("BP Number");
                    }else if(op.equals("Siti Energy")){
                        consumer_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
                        consumer_no.setHint("ARN Number");
                    }else if(op.equals("Tripura Natural Gas")){
                        consumer_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                        consumer_no.setHint("Consumer Number");
                    }else if(op.equals("Unique Central Piped Gases")){
                        consumer_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
                        consumer_no.setHint("Customer No");
                    }else if(op.equals("Vadodara Gas")){
                        consumer_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) });
                        consumer_no.setHint("Consumer Number");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     /*   spinner_oprater_gas =  findViewById(R.id.spinner_oprater_gas);
        linear_proceed_gas = (LinearLayout) findViewById(R.id.linear_proceed_gas);
        et_add1 = findViewById(R.id.et_add1);
        String code1 = "\u20B9   ";
        edit_amount_gas.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
        edit_amount_gas.setCompoundDrawablePadding(code1.length()*10);

        String code2 = "+91      ";
        edit_contact_gas.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code2), null, null, null);
        edit_contact_gas.setCompoundDrawablePadding(code2.length()*10);


       // Arrays.sort(OperatorCode.gas_operators);
        *//*ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_textview_layout, OperatorCode.gas_operators);
        adapter.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinner_oprater_gas.setAdapter(adapter);*//*

        //For OperatorList

        spinner_oprater_gas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                *//*if (position == 0) {
                    operatorData = null;
                } else {*//*
                    operatorData = parent.getItemAtPosition(position).toString();
                //}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

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
                    .put("service", "GAS");

            L.m2("url-operators", operator_code_url);
            L.m2("Request--operators", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, operator_code_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--operators", data.toString());
                            try {


                                if (data.getString("response-code").equalsIgnoreCase("0")) {
                                    OperatorList = new ArrayList<>();
                                    JSONArray jsonArray = data.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        OperatorListModel model = new OperatorListModel();
                                        model.setBillFetch(object1.getString("BillFetch"));
                                        model.setDisplayName(object1.getString("DisplayName"));
                                        model.setOpCode(object1.getString("OpCode"));
                                        model.setOperatorName(object1.getString("OperatorName"));
                                        model.setService(object1.getString("Service"));
                                        OperatorList.add(model);
                                    }
                                    operatorAdaptor = new CustomOperatorClass(context, OperatorList);
                                    gas_spinner.setAdapter(operatorAdaptor);
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

   /* private void proceedConfirmationDialog() {
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView consumerNumber, confirm_operator, confirm_amount, tv_cancel, tv_recharge, tv_rename, confirm_contact;
        consumerNumber = (TextView) d.findViewById(R.id.confirm_mobile);
        confirm_operator = (TextView) d.findViewById(R.id.confirm_operator);
        confirm_amount = (TextView) d.findViewById(R.id.confirm_amount);
        tv_cancel = (TextView) d.findViewById(R.id.tv_cancel);
        tv_recharge = (TextView) d.findViewById(R.id.tv_recharge);
        tv_rename = (TextView) d.findViewById(R.id.tv_rename);
        confirm_contact  = (TextView) d.findViewById(R.id.confirm_contact);

        tv_rename.setText("Account No.");
        consumerNumber.setText(edit_consumer_no_gas.getText().toString());
        confirm_operator.setText(listModel.getOperatorName());
        confirm_amount.setText(edit_amount_gas.getText().toString());
        confirm_contact.setText(edit_contact_gas.getText().toString());

        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   gasPayRequest();
                   d.dismiss();
               }catch (Exception e){
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
    }*/

    //===========LandLineRequest==============

   /* private void gasPayRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("CIR","ALL")
                    .put("txn_key", txn_key)
                    .put("ST", "Gas")
                    .put("OP", listModel.getCode())
                    .put("reqType", "Billpay")
                    .put("CN", edit_consumer_no_gas.getText().toString().trim())
                    .put("AMT", edit_amount_gas.getText().toString().trim())
                    .put("AD1", edit_email_gas.getText().toString().trim())
                    .put("AD2", edit_contact_gas.getText().toString().trim())
                    .put("AD3", "")
                    .put("AD4", "")
                    .put("request_id", RandomNumber.getTranId_14());

            L.m2("url-gas", gas_url);
            L.m2("Request--gas",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, gas_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--gas", data.toString());
                            try {
                                if (data.get("response-code") != null && (data.get("response-code").equals("0") || data.get("response-code").equals("1"))) {
                                        Intent in = new Intent(context, SuccessDetailActivity.class);
                                        in.putExtra("responce", data.get("response-message").toString());
                                        in.putExtra("Consumerno", edit_consumer_no_gas.getText().toString().trim());
                                        in.putExtra("requesttype", "gas-bill");
                                        in.putExtra("operator", listModel.getOperatorName());
                                        in.putExtra("amount", edit_amount_gas.getText().toString().trim());
                                        startActivity(in);
                                        finish();
                                } else if (data.get("response-code") != null && data.get("response-code").equals("2")) {
                                    Toast.makeText(context, data.getString("response-message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
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
    }*/

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
