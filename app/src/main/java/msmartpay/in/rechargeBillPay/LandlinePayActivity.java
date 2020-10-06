package msmartpay.in.rechargeBillPay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
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

import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;
import msmartpay.in.utility.RandomNumber;

public class LandlinePayActivity extends BaseActivity {

    private LinearLayout linear_proceed_landline;
    private EditText edit_std_landline, edit_amount_landline, edit_contact_landline,add1;
    private Spinner spinner_oprater_landline;
    private String operatorData;
    private ProgressDialog pd;
    private Context context;
    private String datacard_Url= HttpURL.RECHARGE_URL;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "";
    private String ser_sel = "MobileBill";
    private String landline_url= HttpURL.BILL_PAY;
    private String operator_code_url = HttpURL.OPERATOR_CODE_URL;
    private ArrayList<OperatorListModel> OperatorList = null;
    private CustomOperatorClass operatorAdaptor;
    private OperatorListModel listModel = null;

    private String mob, amt, operator,contactno,spinnerselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landline_bill_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Landline");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = LandlinePayActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);



        edit_std_landline =  findViewById(R.id.edit_std_landline);
        edit_contact_landline =  findViewById(R.id.edit_mobile_landline);
        edit_amount_landline =  findViewById(R.id.edit_amount_landline);
        add1=findViewById(R.id.id_landline_add1);
        spinner_oprater_landline =  findViewById(R.id.spinner_oprater_landline);
        linear_proceed_landline = (LinearLayout) findViewById(R.id.linear_proceed_landline);

        String code1 = "\u20B9   ";
        //edit_amount_landline.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
       // edit_amount_landline.setCompoundDrawablePadding(code1.length()*10);


       // Arrays.sort(OperatorCode.bill_landline_key);
       /* ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_textview_layout, OperatorCode.bill_landline_key);
        adapter.setDropDownViewResource(R.layout.spinner_textview_layout);
        spinner_oprater_landline.setAdapter(adapter);*/

        //For OperatorList
        try {
            operatorsCodeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }



        spinner_oprater_landline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listModel=OperatorList.get(position);
                String op = listModel.getDisplayName();
                add1.setVisibility(View.GONE);

                if(op.equals("Airtel Landline")) {

                    edit_std_landline.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
                    edit_std_landline.setHint("Landline Number");
                    add1.setVisibility(View.VISIBLE);
                    add1.setHint("STD Code");

                } else if(op.equals("BSNL" )|| op.equals("Reliance" )|| op.equals("Tata Docomo"))
                {
                    edit_std_landline.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
                    edit_std_landline.setHint("Landline Number");
                    add1.setVisibility(View.VISIBLE);
                    add1.setHint("STD Code");

                }else if(op.equals("MTNL - Delhi")){
                    edit_std_landline.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
                    edit_std_landline.setHint("Landline Number");
                    add1.setVisibility(View.VISIBLE);
                    add1.setHint("CA Number");
                }
                else if(op.equals("MTNL - Mumbai")){
                    edit_std_landline.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
                    edit_std_landline.setHint("Landline Number");
                    add1.setVisibility(View.VISIBLE);
                    add1.setHint("Account Number");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




       /* spinner_oprater_landline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        linear_proceed_landline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnectionAvailable()) {
                    listModel = new OperatorListModel();
                    listModel = (OperatorListModel) spinner_oprater_landline.getSelectedItem();

                    mob = edit_std_landline.getText().toString();
                    amt = edit_amount_landline.getText().toString();
                    contactno=edit_contact_landline.getText().toString();
                    if (spinner_oprater_landline.getSelectedItem() == null) {
                            Toast.makeText(context, "Select Operator !!!", Toast.LENGTH_SHORT).show();
                        } else if ("".equals(mob)) {
                        Toast.makeText(context, "Enter Valid LandLine Number", Toast.LENGTH_LONG).show();
                        }else if (amt.equals("")) {
                        Toast.makeText(context,"Enter Valid Amount", Toast.LENGTH_SHORT).show();
                        }else if ("".equals(contactno)) {
                        Toast.makeText(context, "Enter Valid Contact Number", Toast.LENGTH_LONG).show();
                        } else {
                            //proceedConfirmationDialog();

                        Intent intent=new Intent(context,BillPayActivity.class);
                        intent.putExtra("CN",mob);
                        intent.putExtra("CN_hint",edit_std_landline.getHint().toString());
                        intent.putExtra("Amt",amt);
                        intent.putExtra("Mob",contactno);
                        intent.putExtra("Add1",add1.getText().toString());
                        intent.putExtra(getString(R.string.pay_operator_model),getGson().toJson(listModel));
                        startActivity(intent);
                        }
                }else{
                    Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        add1.setVisibility(View.GONE);
        spinner_oprater_landline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>-1) {
                    listModel = OperatorList.get(position);
                    String op = listModel.getDisplayName();
                    add1.setVisibility(View.GONE);


                    if (op.equals("Airtel Landline")) {

                        edit_std_landline.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        edit_std_landline.setHint("Landline Number");
                        add1.setVisibility(View.VISIBLE);
                        add1.setHint("STD Code");

                    } else if (op.equals("BSNL") || op.equals("Reliance") || op.equals("Tata Docomo")) {
                        edit_std_landline.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        edit_std_landline.setHint("Landline Number");
                        add1.setVisibility(View.VISIBLE);
                        add1.setHint("STD Code");

                    } else if (op.equals("MTNL - Delhi")) {
                        edit_std_landline.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        edit_std_landline.setHint("Landline Number");
                        add1.setVisibility(View.VISIBLE);
                        add1.setHint("CA Number");
                    } else if (op.equals("MTNL - Mumbai")) {
                        edit_std_landline.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        edit_std_landline.setHint("Landline Number");
                        add1.setVisibility(View.VISIBLE);
                        add1.setHint("Account Number");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    .put("service", "LANDLINE");

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
                                    spinner_oprater_landline.setAdapter(operatorAdaptor);
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

        TextView landlineSTD, confirm_operator, confirm_amount, tv_cancel, tv_recharge, tv_mobile_dth, confirm_contact;
        LinearLayout ll_visible_gone;
        ll_visible_gone =(LinearLayout) d.findViewById(R.id.ll_visible_gone);
        ll_visible_gone.setVisibility(View.VISIBLE);
        landlineSTD = (TextView) d.findViewById(R.id.confirm_mobile);
        confirm_operator = (TextView) d.findViewById(R.id.confirm_operator);
        confirm_amount = (TextView) d.findViewById(R.id.confirm_amount);
        tv_cancel = (TextView) d.findViewById(R.id.tv_cancel);
        tv_recharge = (TextView) d.findViewById(R.id.tv_recharge);
        tv_mobile_dth = (TextView) d.findViewById(R.id.tv_rename);
        confirm_contact  = (TextView) d.findViewById(R.id.confirm_contact);

        confirm_contact.setText(edit_contact_landline.getText().toString());
        L.m2("landline-->", edit_contact_landline.getText().toString());
        tv_mobile_dth.setText("Landline No.");
        landlineSTD.setText(edit_std_landline.getText().toString());
        confirm_operator.setText(listModel.getOperatorName());
        confirm_amount.setText(edit_amount_landline.getText().toString());


        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LandLineRequest();
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

    //===========LandLineRequest==============

    private void LandLineRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("CIR", "ALL")
                    .put("txn_key", txn_key)
                    .put("ST",ser_sel)
                    .put("OP", listModel.getOpCode())
                    .put("reqType", "Billpay")
                    .put("CN", edit_std_landline.getText().toString().trim())
                    .put("AMT", edit_amount_landline.getText().toString().trim())
                    .put("AD1", edit_contact_landline.getText().toString().trim())
                    .put("AD2", "")
                    .put("AD3", "")
                    .put("AD4", "")
                    .put("request_id", RandomNumber.getTranId_14());

            L.m2("url-landline", landline_url);
            L.m2("Request--landline",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, landline_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--data", data.toString());
                            try {
                                if (data.get("response-code") != null && (data.get("response-code").equals("0") || data.get("response-code").equals("1"))) {
                                        Intent in = new Intent(context, SuccessDetailActivity.class);
                                        in.putExtra("responce", data.get("response-message").toString());
                                        in.putExtra("mobileno", edit_std_landline.getText().toString().trim());
                                        in.putExtra("requesttype", "landline");
                                        in.putExtra("operator", listModel.getOperatorName());
                                        in.putExtra("amount", edit_amount_landline.getText().toString().trim());
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
