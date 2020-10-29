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

public class WaterPayActivity extends BaseActivity {

    private LinearLayout linear_proceed_water;
   // private EditText edit_consumer_no_water, edit_amount_water, edit_email_water, edit_contact_water,id_add1;
   EditText amount, consumer_no, contact_no,id_add1;

    private String operatorData;
    private ProgressDialog pd;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "",Consumerno, Contact, Amount;
    private String water_url = HttpURL.BILL_PAY;
    private String ser_sel = "Billpay";
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private ArrayList<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;
    Spinner water_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_bill_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Water");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = WaterPayActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);



        amount =  findViewById(R.id.id_water_amount);
        consumer_no =  findViewById(R.id.id_water_consumer_no);
       // contact_no =  findViewById(R.id.id_water_contactno);
        //contact_no =  findViewById(R.id.id_water_contactno);
        water_spinner =  findViewById(R.id.id_water_spinner);
        id_add1= findViewById(R.id.id_add1);


        //For OperatorList
        try {
            operatorsCodeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        linear_proceed_water = (LinearLayout) findViewById(R.id.linear_proceed_water);

        linear_proceed_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Consumerno = consumer_no.getText().toString();
                Amount = amount.getText().toString();
                //Contact = contact_no.getText().toString();
                if(water_spinner.getSelectedItem()==null){
                    Toast.makeText(WaterPayActivity.this, "Select Operator", Toast.LENGTH_LONG).show();
                } else if (Consumerno.equals("") ) {
                    Toast.makeText(WaterPayActivity.this, "Enter Correct A/c no", Toast.LENGTH_LONG).show();
                } else if ("".equals(Amount) || Double.parseDouble(Amount)<10) {
                    Toast.makeText(WaterPayActivity.this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(context,BillPayActivity.class);
                    intent.putExtra("CN",Consumerno);
                    intent.putExtra("CN_hint",consumer_no.getHint().toString());
                    intent.putExtra("Amt",Amount);
                    intent.putExtra("Mob",Contact);
                    intent.putExtra("Add1",id_add1.getText().toString());
                    intent.putExtra(getString(R.string.pay_operator_model),getGson().toJson(listModel));
                    startActivity(intent);
                }
            }
        });
        //id_add1.setVisibility(View.GONE);
        water_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>-1) {
                    listModel = OperatorList.get(position);
                    String op = listModel.getDisplayName();
                    id_add1.setVisibility(View.GONE);
                    if (op.equals("Delhi Jal Board")) {
                        consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        consumer_no.setHint("K No");
                    } else if (op.equals("Municipal Corporation of Gurugram")) {
                        consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                        consumer_no.setHint("K No");
                        id_add1.setVisibility(View.VISIBLE);
                        id_add1.setHint("Mobile Number");
                    } else if (op.equals("Urban Improvement Trust (UIT) - BHIWADI")) {
                        consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                        consumer_no.setHint("Customer ID");
                    } else if (op.equals("Uttarakhand Jal Sansthan")) {
                        consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                        consumer_no.setHint("Consumer Number (Last 7 Digits)");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*spinner_oprater_water =  findViewById(R.id.spinner_oprater_water);

        id_add1= findViewById(R.id.id_add1);
        String code1 = "\u20B9   ";
        edit_amount_water.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
        edit_amount_water.setCompoundDrawablePadding(code1.length() * 10);

        String code2 = "+91      ";
        edit_contact_water.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code2), null, null, null);
        edit_contact_water.setCompoundDrawablePadding(code2.length() * 10);


        // Arrays.sort(OperatorCode.waterkey);
        *//*ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_textview_layout, OperatorCode.waterkey);
        adapter.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinner_oprater_water.setAdapter(adapter);*//*



        spinner_oprater_water.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    .put("service", "WATER");

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
                                    OperatorList = new ArrayList();
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
                                    water_spinner.setAdapter(operatorAdaptor);
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

  /*  private void proceedConfirmationDialog() {
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
        confirm_contact = (TextView) d.findViewById(R.id.confirm_contact);

        tv_rename.setText("Account No.");
        consumerNumber.setText(edit_consumer_no_water.getText().toString());
        confirm_operator.setText(listModel.getOperatorName());
        confirm_amount.setText(edit_amount_water.getText().toString());
        confirm_contact.setText(edit_contact_water.getText().toString());

        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    waterPayRequest();
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
    }*/

    //===========waterRequest==============

  /*  private void waterPayRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("agent_id", agentID)
                    .put("CIR", "ALL")
                    .put("txn_key", txn_key)
                    .put("ST", ser_sel)
                    .put("OP", listModel.getCode())
                    .put("reqType", "Billpay")
                    .put("CN", edit_consumer_no_water.getText().toString().trim())
                    .put("AMT", edit_amount_water.getText().toString().trim())
                    .put("AD1", edit_email_water.getText().toString().trim())
                    .put("AD2", edit_contact_water.getText().toString().trim())
                    .put("AD3", "")
                    .put("AD4", "")
                    .put("request_id", RandomNumber.getTranId_14());

            L.m2("url-water", water_url);
            L.m2("Request--water", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, water_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--water", data.toString());
                            try {
                                if (data.get("response-code") != null && (data.get("response-code").equals("0") || data.get("response-code").equals("1"))) {
                                    Intent in = new Intent(context, SuccessDetailActivity.class);
                                    in.putExtra("responce", data.get("response-message").toString());
                                    in.putExtra("Consumerno", edit_consumer_no_water.getText().toString().trim());
                                    in.putExtra("requesttype", "water-bill");
                                    in.putExtra("operator", listModel.getOperatorName());
                                    in.putExtra("amount", edit_amount_water.getText().toString().trim());
                                    startActivity(in);
                                    finish();
                                } else if (data.get("response-code") != null && data.get("response-code").equals("2")) {
                                    Toast.makeText(context, data.getString("response-message"), Toast.LENGTH_SHORT).show();
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
*/
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
