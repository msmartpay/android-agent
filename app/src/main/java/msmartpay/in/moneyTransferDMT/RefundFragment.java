package msmartpay.in.moneyTransferDMT;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import msmartpay.in.R;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class RefundFragment extends Fragment {

    private EditText editRefundTransaction, editTransactionOTP;
    private Button btnRefundTransaction;
    private Button btnSubmit;
    private ProgressDialog pd;
    private JSONObject jsonObject;
    private String url_refund_transaction = HttpURL.REFUND_TRANSACTION;
    private String agentID, txnKey, SessionID, ResisteredMobileNo;
    private SharedPreferences sharedPreferences;
    private String OrgTransactionRefNo, TransferType, Amount, BeneCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dmr_refund_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);

        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);
        SessionID = sharedPreferences.getString("SessionID", null);
        ResisteredMobileNo = sharedPreferences.getString("ResisteredMobileNo", null);

        editRefundTransaction =  view.findViewById(R.id.edit_transaction_refund);
        btnRefundTransaction =  view.findViewById(R.id.btn_refund_otp);

        btnRefundTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editRefundTransaction.getText().toString().trim())){
                    Toast.makeText(getActivity(), "Please Enter Service Delivery TID", Toast.LENGTH_SHORT).show();
                }else {
                    RefundTransactionRequest();
                }
            }
        });
        return view;
    }

    //For Json Request
    private void RefundTransactionRequest() {
        pd = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID.toString())
                    .put("txn_key", txnKey.toString())
                    .put("SessionId", SessionID.toString())
                    .put("SenderId", ResisteredMobileNo.toString())
                    .put("TransactionRefNo", editRefundTransaction.getText().toString().trim());
            Log.e("Request--Refund",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_refund_transaction, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObject = new JSONObject();
                            jsonObject=object;
                            System.out.println("Object----Refund>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called--Refund", url_refund_transaction);
                                    L.m2("url data--Refund", object.toString());

                                    Toast.makeText(getActivity(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();

                                /*    OrgTransactionRefNo = object.getString("OrgTransactionRefNo").toString();
                                    TransferType = object.getString("TransferType").toString();
                                    Amount = object.getString("Amount").toString();
                                    BeneCode = object.getString("BeneCode").toString();
                                */
                                    Intent intent = new Intent(getActivity(), RefundConfirmActivity.class);
                                    intent.putExtra("OrgTransactionRefNo", object.getString("OrgTransactionRefNo").toString());
                                    intent.putExtra("TransferType", object.getString("TransferType").toString());
                                    intent.putExtra("Amount", object.getString("Amount").toString());
                                    intent.putExtra("BeneCode", object.getString("BeneCode").toString());
                                    startActivity(intent);

                     //               refundConfirmDialog(object.getString("message").toString());
                                }
                                else {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), object.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getActivity()).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }

    }

    //=================================================
    public void refundConfirmDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(getActivity(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr_add_bene_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnSubmit =  d.findViewById(R.id.btn_push_submit);
        btnSubmit.setClickable(true);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);
        editTransactionOTP =  d.findViewById(R.id.edit_otp);
        editTransactionOTP.setText("");
        final TextView tvConfirmation = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        tvConfirmation.setText(msg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(jsonObject.getString("Status").equalsIgnoreCase("0")) {
                        if(TextUtils.isEmpty(editTransactionOTP.getText().toString().trim())){
                            Toast.makeText(getActivity(), "Please enter OTP first !!!", Toast.LENGTH_SHORT).show();
                        }else{
    //                        confirmRefundTransactionRequest();
                            d.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

}
