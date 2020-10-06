package msmartpay.in.myWallet;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;
import msmartpay.in.MainActivity;
import msmartpay.in.R;
import msmartpay.in.collectBanks.CollectBankModel;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.BaseFragment;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceRequestFragment extends BaseFragment {

    private LinearLayout icic_qr_code,upi_qr_code;
    private Communication comm;
    private MaterialSpinner bank_details, neftdetails;
    private EditText damount, b_refid, remarks, fromDateEtxt;
    private Button brequest;
    private ProgressDialog pd;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String agentID,selectedType, txn_key = "";
    private String balancerequrl= HttpURL.WALLET_BELL_REQ;
    private SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<CollectBankModel> bankList;
    private  CollectBankModel bankModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        comm= (Communication) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_wallet_request, container, false);

        context = getActivity();
        sharedPreferences = context.getSharedPreferences("myPrefs",MODE_PRIVATE);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txn_key = sharedPreferences.getString("txn-key", null);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        remarks = view.findViewById(R.id.id_balance_request_remarks);
        damount = view.findViewById(R.id.id_balance_request_amount);
        b_refid =  view.findViewById(R.id.id_balance_request_refId);
        fromDateEtxt =  view.findViewById(R.id.id_balance_request_ddate);
        bank_details =  view.findViewById(R.id.id_balancerequest_bank);
        neftdetails =  view.findViewById(R.id.id_balancerequest_type);
        brequest =  view.findViewById(R.id.id_balance_request_submit);
        icic_qr_code=view.findViewById(R.id.icic_qr_code);
        upi_qr_code=view.findViewById(R.id.upi_qr_code);

        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();
            }
        });

        getBankDetails();

        bank_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(position ==-1){
                    bankModel=null;
                }else {
                    bankModel = bankList.get(position);

                    if(bankModel!=null && bankModel.getBank_name().contains("ICICI Bank QR CODE")){
                        icic_qr_code.setVisibility(View.VISIBLE);
                        //upi_qr_code.setVisibility(View.GONE);
                    }/*else if(bankModel!=null && bankModel.getBank_name().contains("BHIM UPI")){
                        upi_qr_code.setVisibility(View.VISIBLE);
                        icic_qr_code.setVisibility(View.GONE);
                    }*/else{
                        //upi_qr_code.setVisibility(View.GONE);
                        icic_qr_code.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        neftdetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(position ==-1){
                    selectedType = null;
                }else {
                    selectedType = parent.getItemAtPosition(position).toString();
                    if (selectedType.equalsIgnoreCase("cash")) {
                        b_refid.setVisibility(View.GONE);
                    } else {
                        b_refid.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        brequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectionAvailable()){
                    if(bankModel == null){
                        Toast.makeText(context, "Select Bank Name !!!", Toast.LENGTH_SHORT).show();
                    }else if(selectedType == null){
                        Toast.makeText(context, "Select Payment Type !!!", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(damount.getText().toString().trim())){
                        damount.requestFocus();
                        Toast.makeText(context, "Enter Deposite Amount !!!", Toast.LENGTH_SHORT).show();
                    }else if (!selectedType.equalsIgnoreCase("cash") && (b_refid.getText().toString() != null && b_refid.getText().toString().length() <= 0)) {
                        b_refid.requestFocus();
                        Toast.makeText(context, "Enter Transaction Reference Id", Toast.LENGTH_LONG).show();
                    }else if (fromDateEtxt.getText().toString().trim().length() <= 0) {
                        Toast.makeText(context, "Select Deposit Date. ", Toast.LENGTH_SHORT).show();
                    }else{
                        balanceReqFragmentRequest();
                    }
                }else{
                    Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    return view;
    }

    private void balanceReqFragmentRequest() {
            pd = new ProgressDialog(context);
            pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            try{
                JSONObject jsonObjectReq = new JSONObject()
                        .put("agent_id", agentID)
                        .put("txn_key", txn_key)
                        .put("mode", selectedType)
                        .put("bankName", bankModel.getActual_bank_name())
                        .put("depositDate", fromDateEtxt.getText().toString().trim())
                        .put("refId", b_refid.getText().toString().trim())
                        .put("amount", damount.getText().toString().trim())
                        .put("remark", remarks.getText().toString().trim());

                L.m2("url-balReq", balancerequrl);
                L.m2("Request--balReq",jsonObjectReq.toString());
                JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, balancerequrl, jsonObjectReq, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {

                        pd.dismiss();
                        L.m2("Response--balReq", data.toString());
                        try {
                            if ((data.get("response-code") != null && data.get("response-code").equals("0"))) {
                                new AlertDialog.Builder(context)
                                        .setTitle("Status")
                                        .setIcon(R.drawable.trnsuccess)
                                        .setMessage(data.get("response-message")+"")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(context, MainActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            }
                                        })
                                        .show();
                            } else{
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Status")
                                        .setIcon(R.drawable.failed)
                                        .setMessage(data.get("response-message")+"")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(context, MainActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            }
                                        }).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                    }
                });
                BaseActivity.getSocketTimeOut(objectRequest);
                Mysingleton.getInstance(context).addToRequsetque(objectRequest);
            }

            catch (Exception e){
                e.printStackTrace();
            }
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.show();
    }

    public interface Communication {

    }


    private void getBankDetails()  {

        pd = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("txn_key", txn_key)
                    .put("agent_id", agentID);

            L.m2("summary_Request--1>", jsonObjectReq.toString());

            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, HttpURL.CollectBankDetails, jsonObjectReq,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            try {
                                L.m2("summary_Request--1>", object.toString());
                                if (object.get("response-code") != null && object.get("response-code").equals("0")) {
                                    final JSONArray parentArray = (JSONArray) object.get("data");
                                    if (bankList == null)
                                        bankList = new ArrayList<>();
                                    else
                                        bankList.clear();

                                    if (parentArray.length() > 0) {
                                        for (int i = 0; i < parentArray.length(); i++) {
                                            JSONObject obj = (JSONObject) parentArray.get(i);

                                            CollectBankModel bankDetailsItem = new CollectBankModel();

                                            bankDetailsItem.setBank_name(obj.get("bank_name")+"" + obj.get("bank_account") + obj.get("bnk_ifsc") );
                                            bankDetailsItem.setActual_bank_name(obj.get("bank_name") + "");
                                            bankDetailsItem.setBank_account(obj.get("bank_account") + "");
                                            bankDetailsItem.setBank_account_name(obj.get("bank_account_name") + "");
                                            bankDetailsItem.setBnk_ifsc(obj.get("bnk_ifsc") + "");

                                            bankList.add(bankDetailsItem);

                                        }
                                        ArrayAdapter<CollectBankModel> adapter = new ArrayAdapter<CollectBankModel>(context, R.layout.spinner_textview_layout, bankList);
                                        adapter.setDropDownViewResource(R.layout.spinner_textview_layout);
                                        bank_details.setAdapter(adapter);


                                    }
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
                    Toast.makeText(getActivity(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getActivity()).addToRequsetque(jsonrequest);
        } catch (Exception exp) {
            pd.dismiss();
            exp.printStackTrace();
        }

    }

}
