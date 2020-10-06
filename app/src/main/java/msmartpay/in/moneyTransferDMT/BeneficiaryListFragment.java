package msmartpay.in.moneyTransferDMT;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

import static android.content.Context.MODE_PRIVATE;
import static msmartpay.in.MainActivity.jsonObjectStatic;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class BeneficiaryListFragment extends Fragment {

    private ListView listView;
    private Dialog d;
    private FloatingActionButton floatingButton;
    private ArrayList<BankDetailsModel> customerDetails;
    private BankDetailsModel customerData;
    private TextView tvEmptyList;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_change_bene_status = HttpURL.CHANGE_BENE_STATUS;
    private String agentID, txnKey, SessionID, mobileNumber, BeneCodeForOTP;
    private SharedPreferences sharedPreferences;
    private String url_confirm_add_beneficiary = HttpURL.CONFIRM_ADD_BENEFICIARY;
    private String url_re_send_otp = HttpURL.RE_SEND_OTP;
    private String url_find_sender = HttpURL.FIND_SENDER;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dmr_money_trans_listview_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getActivity();
        listView = (ListView) view.findViewById(R.id.listview);
        tvEmptyList = (TextView) view.findViewById(R.id.tv_empty_list);
        L.m2("SenderLimit_Detail---2>", jsonObjectStatic.toString());

        sharedPreferences = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);
      //list
        getListBene();

        floatingButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent floatIntent = new Intent(context, AddBeneficiaryActivity.class);
                startActivity(floatIntent);
            }
        });
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Button btnPayOrActive =  view.findViewById(R.id.btn_pay_active_now);
                btnPayOrActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(customerDetails.get(position).getStatus().equalsIgnoreCase("Y")) {
                            Intent intent = new Intent(context, ImpsNeftActivity.class);
                            intent.putExtra("BeneName", customerDetails.get(position).getBeneficiaryName());
                            intent.putExtra("BeneAccountNumber", customerDetails.get(position).getAccountNumber());
                            intent.putExtra("BeneBankName", customerDetails.get(position).getBankName());
                            intent.putExtra("BeneStatus", customerDetails.get(position).getStatus());
                            intent.putExtra("BeneVarify", customerDetails.get(position).getVerifyStatus());
                            intent.putExtra("IFSCcode", customerDetails.get(position).getIFSCcode());
                            intent.putExtra("beneCodeData", customerDetails.get(position).getBeneCode());
                            startActivity(intent);
                            //context.finish();
                        }else{
                            ChangeSenderStatusRequest(position);
                        }
                    }
                });
            }
        });*/
        return view;
    }

    //Adapter Class
    public class bankDetailAdaptorClass extends BaseAdapter {

        private Context contextData;
        private ArrayList<BankDetailsModel> BeneListData;
        private TextView BeneName, BeneAccountNumber, BeneBankName, BeneVarify, BeneStatus;
        private Button btnPayOrActive;

        bankDetailAdaptorClass(Context context, ArrayList<BankDetailsModel> arrayList) {
            contextData = context;
            BeneListData = arrayList;
        }

        @Override
        public int getCount() {
            return BeneListData.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) contextData.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate (R.layout.listview_text, parent, false);

            BeneName = (TextView) view.findViewById(R.id.tv_bene_name);
            BeneAccountNumber = (TextView) view.findViewById(R.id.tv_account_no);
            BeneBankName = (TextView) view.findViewById(R.id.tv_bank_name);
            BeneStatus = (TextView) view.findViewById(R.id.btn_status);
            BeneVarify = (TextView) view.findViewById(R.id.tv_varify);
            btnPayOrActive =  view.findViewById(R.id.btn_pay_active_now);

            BeneName.setText(BeneListData.get(position).getBeneficiaryName());
            BeneAccountNumber.setText(BeneListData.get(position).getAccountNumber());
            BeneBankName.setText(BeneListData.get(position).getBankName());
            BeneStatus.setText(BeneListData.get(position).getStatus());
            BeneVarify.setText(BeneListData.get(position).getVerifyStatus());
            BeneCodeForOTP = BeneListData.get(position).getBeneCode();
            if(BeneListData.get(position).getStatus().equalsIgnoreCase("Y")){
                btnPayOrActive.setText("PAY NOW");
                BeneStatus.setText("Active");
                BeneStatus.setTextColor(Color.parseColor("#32CD32"));
                btnPayOrActive.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            }else{
                btnPayOrActive.setText("ACTIVE NOW");
                BeneStatus.setText("Deactive");
                BeneStatus.setTextColor(Color.parseColor("#FF0000"));
                btnPayOrActive.setBackground(getResources().getDrawable(R.drawable.rounded_button1));
            }

            if(BeneListData.get(position).getVerifyStatus().equalsIgnoreCase("Y")){
                BeneVarify.setText("Verified");
                BeneVarify.setTextColor(Color.parseColor("#32CD32"));
            }else{
                SpannableString spannableString = new SpannableString("Verify");
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                BeneVarify.setText(spannableString);
                BeneVarify.setTextColor(Color.parseColor("#FF0000"));
            }
            btnPayOrActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(BeneListData.get(position).getStatus().equalsIgnoreCase("Y")) {
                        Intent intent = new Intent(contextData, ImpsNeftActivity.class);
                        intent.putExtra("BeneName", BeneListData.get(position).getBeneficiaryName());
                        intent.putExtra("BeneAccountNumber", BeneListData.get(position).getAccountNumber());
                        intent.putExtra("BeneBankName", BeneListData.get(position).getBankName());
                        intent.putExtra("BeneStatus", BeneListData.get(position).getStatus());
                        intent.putExtra("BeneVarify", BeneListData.get(position).getVerifyStatus());
                        intent.putExtra("IFSCcode", BeneListData.get(position).getIFSCcode());
                        intent.putExtra("beneCodeData", BeneListData.get(position).getBeneCode());
                        contextData.startActivity(intent);
                    }else{
                       ChangeSenderStatusRequest(BeneListData.get(position).getBeneCode());
                    }
                }
            });

            //For click on view
  /*          view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contextData, UpdateBeneAccountDetailView.class);
                    intent.putExtra("BeneName", BeneListData.get(position).getBeneficiaryName());
                    intent.putExtra("BeneAccountNumber", BeneListData.get(position).getAccountNumber());
                    intent.putExtra("BeneBankName", BeneListData.get(position).getBankName());
                    intent.putExtra("BeneStatus", BeneListData.get(position).getStatus());
                    intent.putExtra("BeneVarify", BeneListData.get(position).getVerifyStatus());
                    intent.putExtra("IFSCcode", BeneListData.get(position).getIFSCcode());
                    contextData.startActivity(intent);
                }
            });
*/

            return view;
        }
    }

    //Json request for Activate Sender
    private void ChangeSenderStatusRequest(final String beneCode) {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {

            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", mobileNumber.toString())
                    .put("beneCode",beneCode);
            L.m2("url-called----ChangeSta", url_change_bene_status);
            Log.e("Request--verifyOTP",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_change_bene_status, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();

                            L.m2("url data----ChangeSta", object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {

                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();

                                    showMoneyTransferDMTDialog(object.getString("TransactionRefNo"),beneCode);
                                    /*Intent intent = new Intent(context, ChangeStatusOTPActivity.class);
                                    intent.putExtra("TransactionRefNo", object.getString("TransactionRefNo"));
                                    intent.putExtra("beneCode",customerDetails.get(position).getBeneCode());
                                    startActivity(intent);*/
                                }
                                else {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }
 private void getListBene(){
     try {

         JSONArray jsonArray = jsonObjectStatic.optJSONArray("beneList");
         L.m2("check_beneList--->", jsonArray.toString());
         if(jsonArray.length() != 0){

             tvEmptyList.setVisibility(View.GONE);
             listView.setVisibility(View.VISIBLE);

             JSONObject jsonObject;
             customerDetails= new ArrayList<>();
             customerDetails.clear();
             L.m2("Array lenght ",jsonArray.length()+"");

             for(int i=0; i<jsonArray.length(); i++){
                 jsonObject = jsonArray.getJSONObject(i);
                 customerData = new BankDetailsModel();
                 customerData.setBeneficiaryName(jsonObject.getString("Name"));
                 customerData.setAccountNumber(jsonObject.getString("BeneAccountNo"));
                 customerData.setBankName(jsonObject.getString("BeneBankName"));
                 customerData.setStatus(jsonObject.getString("Status"));
                 customerData.setVerifyStatus(jsonObject.getString("VerifyStatus"));
                 customerData.setIFSCcode(jsonObject.getString("BeneIFSC"));
                 customerData.setBeneCode(jsonObject.getString("BeneCode"));
                 customerDetails.add(customerData);
             }
             listView.setAdapter(new bankDetailAdaptorClass(context, customerDetails));
         }else{
             tvEmptyList.setVisibility(View.VISIBLE);
             listView.setVisibility(View.GONE);
         }
     } catch (JSONException e) {
         e.printStackTrace();
     }

 }
    //Json request for add Beneficiary Confirm

    public void showMoneyTransferDMTDialog(final String txnRefNo, final String beneCode) {
        // TODO Auto-generated method stub
         d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dialog_active_bene_dmt);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnOK=  d.findViewById(R.id.btn_ok);
        Button btnResend =  d.findViewById(R.id.btn_resend);
        Button btnClosed =  d.findViewById(R.id.close_push_button);
        final EditText editMobileNo =  d.findViewById(R.id.edit_push_balance);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editMobileNo.getText().toString().trim())) {
                    Toast.makeText(context, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                   String otp = editMobileNo.getText().toString().trim();
                    confirmAddBeneficiaryRequest(txnRefNo,beneCode,otp);
                }
            }
        });
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Re_sendOTPRequest(beneCode);
            }
        });
        btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                d.cancel();
            }
        });

        d.show();
    }
    private void confirmAddBeneficiaryRequest(String txnRefNo, String beneCode, String otp) {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", mobileNumber.toString())
                    .put("BeneCode", beneCode.toString())
                    .put("TransactionRefNo", txnRefNo.toString())
                    .put("otp", otp);
            L.m2("url-called-C_AddBene", url_confirm_add_beneficiary);
            Log.e("Request--confirmAddBene",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_confirm_add_beneficiary, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----ConfirmAddBeneficiary>"+object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    L.m2("url data-C_AddBene", object.toString());
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    d.dismiss();
                                    findSenderRequest();
                                }
                                else {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }
    private void Re_sendOTPRequest(String beneCode) {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            //  L.m2("check_beneCode--2>", beneCode.toString());
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", mobileNumber.toString())
                    .put("beneCode", beneCode.toString());
            L.m2("url-called--Re_send", url_re_send_otp);
            Log.e("Request--Re_send",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_re_send_otp, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            L.m2("url data--Re_send", object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {

                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }
    private void findSenderRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txnKey)
                    .put("SessionId", SessionID)
                    .put("SenderId", mobileNumber);

            L.m2("url-called", url_find_sender);
            Log.e("Request",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();

                            L.m2("url data", object.toString());
                            try {
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    jsonObjectStatic=object;
                                    getListBene();
                                }
                                else {
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse (VolleyError error){
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }
}
