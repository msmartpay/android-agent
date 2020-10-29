package in.msmartpay.agent.rechargeBillPay;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.RandomNumber;

public class InsurancePayActivity extends BaseActivity {

    private LinearLayout linear_proceed_insur;
    EditText policyno, preninum_amount, contact_no,dob;
    private EditText edit_holder_name_insur, edit_holder_no_insur, edit_holder_dob_insur, edit_amount_insur, edit_email_insur, edit_contact_insur;
    //private Spinner spinner_oprater_insur;
    Spinner insurance_spinner;
    private String operatorData;
    private ProgressDialog pd;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private String insurance_url = HttpURL.BILL_PAY;
    private String ser_sel = "Insurance";
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private ArrayList<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;

    String mob,Mobile, amt, Email, Holdername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insurance_bill_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Insurance");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = InsurancePayActivity.this;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);

        insurance_spinner =  findViewById(R.id.id_insurance_spinner);

        policyno =  findViewById(R.id.id_insurance_policy_no);
        preninum_amount =  findViewById(R.id.id_insurance_amount);
        contact_no =  findViewById(R.id.id_insurance_contactno);
        dob=findViewById(R.id.id_insurance_dob);

        setDateTimeField();

        dob.setInputType(InputType.TYPE_NULL);

        //For OperatorList
        try {
            operatorsCodeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        linear_proceed_insur = (LinearLayout) findViewById(R.id.linear_proceed_insur);
        linear_proceed_insur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mob = policyno.getText().toString();
                amt = preninum_amount.getText().toString();
                Mobile=  contact_no.getText().toString();
                if(insurance_spinner.getSelectedItem()==null){
                    Toast.makeText(InsurancePayActivity.this, "Select Operator", Toast.LENGTH_LONG).show();
                }else if ("".equals(mob)) {
                    Toast.makeText(InsurancePayActivity.this, "Enter Correct Policy Number", Toast.LENGTH_LONG).show();
                } else if ("".equals(amt)) {
                    Toast.makeText(InsurancePayActivity.this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }
                else if ("".equals(Mobile)) {
                    Toast.makeText(InsurancePayActivity.this, "Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent=new Intent(context,BillPayActivity.class);
                    intent.putExtra("CN",mob);
                    intent.putExtra("CN_hint",policyno.getHint().toString());
                    intent.putExtra("Amt",amt);
                    intent.putExtra("Mob",Mobile);
                    intent.putExtra("Add1",dob.getText().toString());
                    intent.putExtra(getString(R.string.pay_operator_model),getGson().toJson(listModel));
                    startActivity(intent);
                }

            }
        });
        dob.setVisibility(View.GONE);

        insurance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>-1) {
                    listModel = OperatorList.get(position);
                    String op = listModel.getDisplayName();
                    dob.setVisibility(View.GONE);
                    if (op.equals("ICICI Prudential Life Insurance")) {
                        policyno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        policyno.setHint("Policy Number");
                        dob.setVisibility(View.VISIBLE);
                        dob.setHint("Date of Birth");

                    } else if (op.equals("Tata AIA Life Insurance")) {
                        policyno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        policyno.setHint("Policy Number");
                    } else if (op.equals("Tata AIG General Insurance")) {
                        policyno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        policyno.setHint("Policy Number");
                        dob.setVisibility(View.VISIBLE);
                        dob.setHint("Date of Birth");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  spinner_oprater_insur =  findViewById(R.id.spinner_oprater_insur);


        edit_holder_dob_insur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();
            }
        });

        String code1 = "\u20B9   ";
        edit_amount_insur.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
        edit_amount_insur.setCompoundDrawablePadding(code1.length() * 10);

        String code2 = "+91      ";
        edit_contact_insur.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code2), null, null, null);
        edit_contact_insur.setCompoundDrawablePadding(code2.length() * 10);


        // Arrays.sort(OperatorCode.insurance_operators);
       *//* ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_textview_layout, OperatorCode.insurance_operators);
        adapter.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinner_oprater_insur.setAdapter(adapter);*//*



        spinner_oprater_insur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


    private void setDateTimeField() {
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.id_insurance_dob) {
                    fromDatePickerDialog.show();
                }
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


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
                    .put("service", "INSURANCE");

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
                                    insurance_spinner.setAdapter(operatorAdaptor);
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

        TextView consumerNumber, confirm_operator, confirm_amount, tv_cancel, tv_recharge, tv_rename, confirm_contact, operator_rename, confirm_policy_h_name;
        LinearLayout ll_visible_gone = d.findViewById(R.id.ll_visible_gone);
        ll_visible_gone.setVisibility(View.VISIBLE);
        consumerNumber = (TextView) d.findViewById(R.id.confirm_mobile);
        operator_rename = (TextView) d.findViewById(R.id.operator_rename);
        confirm_operator = (TextView) d.findViewById(R.id.confirm_operator);
        confirm_amount = (TextView) d.findViewById(R.id.confirm_amount);
        tv_cancel = (TextView) d.findViewById(R.id.tv_cancel);
        tv_recharge = (TextView) d.findViewById(R.id.tv_recharge);
        tv_rename = (TextView) d.findViewById(R.id.tv_rename);
        confirm_policy_h_name = (TextView) d.findViewById(R.id.confirm_policy_h_name);
        confirm_contact = (TextView) d.findViewById(R.id.confirm_contact);

        tv_rename.setText("Policy No.");
        operator_rename.setText("Policy Type");
        confirm_policy_h_name.setText("Holder Name");
        consumerNumber.setText(edit_holder_no_insur.getText().toString());
        confirm_operator.setText(listModel.getOperatorName());
        confirm_amount.setText(edit_amount_insur.getText().toString());
        confirm_contact.setText(edit_contact_insur.getText().toString());
        confirm_contact.setText(edit_holder_name_insur.getText().toString().trim());

        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    insurancePayRequest();
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

    //===========insurancePayRequest==============

    private void insurancePayRequest() {
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
                    .put("CN", edit_holder_no_insur.getText().toString().trim())
                    .put("AMT", edit_amount_insur.getText().toString().trim())
                    .put("AD1", edit_holder_name_insur.getText().toString().trim())
                    .put("AD2", edit_holder_dob_insur.getText().toString().trim())
                    .put("AD3", edit_email_insur.getText().toString().trim())
                    .put("AD4", edit_contact_insur.getText().toString().trim())
                    .put("request_id", RandomNumber.getTranId_14());

            L.m2("url-insur", insurance_url);
            L.m2("Request--insur", jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, insurance_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--insur", data.toString());
                            try {
                                if (data.get("response-code") != null && (data.get("response-code").equals("0") || data.get("response-code").equals("1"))) {
                                    Intent in = new Intent(context, SuccessDetailActivity.class);
                                    in.putExtra("responce", data.get("response-message").toString());
                                    in.putExtra("policyno", edit_holder_no_insur.getText().toString().trim());
                                    in.putExtra("requesttype", "insurance-bill");
                                    in.putExtra("operator", listModel.getOperatorName());
                                    in.putExtra("amount", edit_amount_insur.getText().toString().trim());
                                    startActivity(in);
                                    finish();
                                } else if (data.get("response-code") != null && data.get("response-code").equals("2")) {
                                    Toast.makeText(context, data.getString("response-message").toString(), Toast.LENGTH_SHORT).show();
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
   /* private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edit_holder_dob_insur.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.show();
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
