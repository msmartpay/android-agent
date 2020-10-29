package agent.msmartpay.in.rechargeBillPay;

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
import java.util.Collections;
import java.util.Comparator;

import agent.msmartpay.in.R;
import agent.msmartpay.in.utility.BaseActivity;
import agent.msmartpay.in.utility.HttpURL;
import agent.msmartpay.in.utility.L;
import agent.msmartpay.in.utility.Mysingleton;

public class ElectricityPayActivity extends BaseActivity {

    private LinearLayout linear_proceed_electricity;
    private EditText consumer_no, amount, edit_email_electricity, contact_no,id_add1;
    private Spinner id_electricity_spinner, spinner_additional_electricity;
    private String operatorData, operatorData1;
    private String spinnerselected;
    private ProgressDialog pd;
    private Context context;
    private String electricity_url = HttpURL.BILL_PAY;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private String response, AD1 = "", AD2 = "", AD3 = "", AD4 = "", CN = "", OP = "",operator,op;
    private String viewBill = null;
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private ArrayList<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;

    private String mob = "", amountString = "", connectionNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electricity_bill_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Electricity");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = ElectricityPayActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);

        consumer_no =  findViewById(R.id.id_electricity_consumer_no);
        amount =  findViewById(R.id.id_electricity_amount);
        edit_email_electricity =  findViewById(R.id.edit_email_electricity);
        contact_no =  findViewById(R.id.edit_customer_no_electricity);
        id_add1 =  findViewById(R.id.id_add1);
        id_electricity_spinner =  findViewById(R.id.id_electricity_spinner);
        linear_proceed_electricity = (LinearLayout) findViewById(R.id.linear_proceed_electricity);

        amount.setVisibility(View.GONE);
        contact_no.setVisibility(View.GONE);
        linear_proceed_electricity.setVisibility(View.VISIBLE);
        id_add1.setVisibility(View.GONE);
        try {
            operatorsCodeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        id_electricity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>-1){
                listModel = OperatorList.get(position);
                id_add1.setVisibility(View.GONE);
                amount.setText("");
                contact_no.setText(""); //contact_no
                consumer_no.setText("");  //consumer_no
                consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                id_add1.setText("");
                contact_no.setVisibility(View.VISIBLE);
                consumer_no.setHint("Enter Consumer Number");
                amount.setVisibility(View.VISIBLE);
                viewBill = OperatorList.get(position).getBillFetch();
                op = OperatorList.get(position).getDisplayName();
                operator = OperatorList.get(position).getOpCode();
                spinnerselected = OperatorList.get(position).getDisplayName();

                if (op.equals("UPPCL (RURAL) - UTTAR PRADESH") || op.equals("UPPCL (URBAN) - UTTAR PRADESH")) {

                } else if (op.equals("Ajmer Vidyut Vitran Nigam - RAJASTHAN") || op.equals("BESL - BHARATPUR") || op.equals("BkESL - BIKANER") || op.equals("Jaipur Vidyut Vitran Nigam - RAJASTHAN")
                        || op.equals("Jodhpur Vidyut Vitran Nigam - RAJASTHAN") || op.equals("Kota Electricity Distribution - RAJASTHAN") || op.equals("TPADL - AJMER")) {
                    consumer_no.setHint("K Number");
                } else if (op.equals("APDCL - ASSAM") || op.equals("CESC - WEST BENGAL") || op.equals("MEPDCL - MEGHALAYA") || op.equals("TSECL - TRIPURA")) {
                    consumer_no.setHint("Consumer ID");
                } else if (op.equals("APEPDCL - ANDHRA PRADESH") || op.equals("APSPDCL - ANDHRA PRADESH")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                    consumer_no.setHint("Service Number");
                } else if (op.equals("BESCOM - BENGALURU")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    consumer_no.setHint("Customer ID / Account ID");
                } else if (op.equals("CSPDCL - CHHATTISGARH") || op.equals("JUSCO - JAMSHEDPUR")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    consumer_no.setHint("Business Partner Number");
                } else if (op.equals("Daman and Diu Electricity") || op.equals("DHBVN - HARYANA") || op.equals("PSPCL - PUNJAB") || op.equals("UHBVN - HARYANA")) {
                    consumer_no.setHint("Account Number");
                } else if (op.equals("DNHPDCL - DADRA & NAGAR HAVELI")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                    consumer_no.setHint("Service Connection Number");
                } else if (op.equals("NBPDCL - BIHAR") || op.equals("SBPDCL - BIHAR")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                    consumer_no.setHint("CA Number");
                } else if (op.equals("APDCL - ASSAM")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                    consumer_no.setHint("K Number");
                } else if (op.equals("UPCL - UTTARAKHAND")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                    consumer_no.setHint("Service Connection Number");
                } else if (op.equals("Torrent Power")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                    consumer_no.setHint("Service Number");
                    id_add1.setVisibility(View.VISIBLE);
                    id_add1.setHint("City Name");
                } else if (op.equals("MSEDC - MAHARASHTRA")) {
                    id_add1.setVisibility(View.VISIBLE);
                    id_add1.setHint("Billing Unit");
                    id_add1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                } else if (op.equals("Reliance Energy")) {
                    id_add1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    consumer_no.setHint("Account Number");
                    id_add1.setVisibility(View.VISIBLE);
                    id_add1.setHint("Billing Unit");
                } else if (op.equals("Kerala State Electricity Board Ltd. (KSEB)")) {
                    consumer_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                    consumer_no.setHint("Enter Consumer Number");
                }
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        linear_proceed_electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectionAvailable()) {

                    connectionNo = consumer_no.getText().toString();
                    amountString = amount.getText().toString();
                    mob = contact_no.getText().toString();
                    AD1 = id_add1.getText().toString();
                    if(id_electricity_spinner.getSelectedItem()==null) {
                        Toast.makeText(context, "Select Operator", Toast.LENGTH_SHORT).show();
                    }else if ("".equals(connectionNo)) {
                        Toast.makeText(context, "Enter Valid " + consumer_no.getHint().toString(), Toast.LENGTH_LONG).show();
                    } else if ("".equals(amountString) || Double.parseDouble(amountString) < 10) {
                        Toast.makeText(context, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, BillPayActivity.class);
                        intent.putExtra("CN", connectionNo);
                        intent.putExtra("CN_hint", consumer_no.getHint().toString());
                        intent.putExtra("Amt", amountString);
                        intent.putExtra("Mob", mob);
                        intent.putExtra("Add1", AD1);
                        intent.putExtra(getString(R.string.pay_operator_model), getGson().toJson(listModel));
                        startActivity(intent);
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
                    .put("service", "ELECTRICITY");

            L.m2("url-operators", operator_code_url);
            L.m2("Request--operators", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, operator_code_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--operators", data.toString());
                            try {
                                if (data.getString("response-code")!=null&&data.getString("response-code").equalsIgnoreCase("0")) {
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

                                    Collections.sort(OperatorList, new Comparator<OperatorListModel>() {
                                        @Override
                                        public int compare(OperatorListModel model1, OperatorListModel model2) {

                                            return model1.getOperatorName().compareTo(model2.getOperatorName());
                                        }
                                    });

                                    operatorAdaptor = new CustomOperatorClass(context, OperatorList);
                                    id_electricity_spinner.setAdapter(operatorAdaptor);
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
