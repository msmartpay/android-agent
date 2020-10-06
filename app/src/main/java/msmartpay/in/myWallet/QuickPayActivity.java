package msmartpay.in.myWallet;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import msmartpay.in.R;
import msmartpay.in.rechargeBillPay.SuccessDetailActivity;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;
import msmartpay.in.utility.Service;
import msmartpay.in.utility.TextDrawable;

public class QuickPayActivity extends BaseActivity {

    private LinearLayout linear_proceed_quick;
    private EditText edit_mobile_quick, edit_amount_quick, edit_remark_quick;
    private ImageView image_contactlist_quick;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private ProgressDialog pd;
    private Context context;
    private String quick_url = HttpURL.WALLET_TRANSFER;
    private SharedPreferences sharedPreferences;
    private String agentID, txn_key = "", agent_id_full, agentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_pay_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = QuickPayActivity.this;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txn_key = sharedPreferences.getString("txn-key", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        agent_id_full = sharedPreferences.getString("agent_id_full", null);
        agentName = sharedPreferences.getString("agentName", null);
        edit_mobile_quick =  findViewById(R.id.edit_mobile_quick);
        edit_amount_quick =  findViewById(R.id.edit_amount_quick);
        edit_remark_quick =  findViewById(R.id.edit_remark_quick);
        image_contactlist_quick = (ImageView) findViewById(R.id.image_contactlist_quick);
        linear_proceed_quick = (LinearLayout) findViewById(R.id.linear_proceed_quick);

        String code = "+91      ";
        edit_mobile_quick.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        edit_mobile_quick.setCompoundDrawablePadding(code.length()*10);

        String code1 = "\u20B9   ";
        edit_amount_quick.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
        edit_amount_quick.setCompoundDrawablePadding(code1.length()*10);
        image_contactlist_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_CONTACTS);
            }
        });

        linear_proceed_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnectionAvailable()) {
                    if (TextUtils.isEmpty(edit_mobile_quick.getText().toString().trim()) || edit_mobile_quick.getText().toString().trim().length() < 10) {
                        edit_mobile_quick.requestFocus();
                        Toast.makeText(context, "Enter 10 digit mobile no. !!!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(edit_amount_quick.getText().toString().trim())) {
                        edit_amount_quick.requestFocus();
                        Toast.makeText(context, "Enter Amount !!!", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(edit_remark_quick.getText().toString().trim())) {
                        edit_remark_quick.requestFocus();
                        Toast.makeText(context, "Enter Remark !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        proceedConfirmationDialog();
                    }
                }else{
                    Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void proceedConfirmationDialog() {
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.confirmation_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView confirm_mobile, confirm_operator, confirm_amount, tv_cancel, tv_recharge, operator_rename;
        confirm_mobile = (TextView) d.findViewById(R.id.confirm_mobile);
        operator_rename = (TextView) d.findViewById(R.id.operator_rename);
        confirm_operator = (TextView) d.findViewById(R.id.confirm_operator);
        confirm_amount = (TextView) d.findViewById(R.id.confirm_amount);
        tv_cancel = (TextView) d.findViewById(R.id.tv_cancel);
        tv_recharge = (TextView) d.findViewById(R.id.tv_recharge);

        operator_rename.setText("Remark");
        tv_recharge.setText("Transfer");
        confirm_mobile.setText(edit_mobile_quick.getText().toString());
        confirm_operator.setText(edit_remark_quick.getText().toString());
        confirm_amount.setText(edit_amount_quick.getText().toString());

        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            try {
                   quickPayRequest();
                    d.hide();
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
    }

    //===========quickPayRequest==============

    private void quickPayRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("toAgentId", edit_mobile_quick.getText().toString().trim())
                    .put("amount", edit_amount_quick.getText().toString().trim())
                    .put("remark", edit_remark_quick.getText().toString().trim());

            L.m2("url-quick", quick_url);
            L.m2("Request--quick",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, quick_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            pd.dismiss();
                            L.m2("Response--quick", data.toString());
                            try {
                                if (null != data.getString("response-code") && (data.getString("response-code").equalsIgnoreCase("0")
                                        || data.getString("response-code").equalsIgnoreCase("1"))) {
                                    Intent in = new Intent(context, SuccessDetailActivity.class);
                                    in.putExtra("responce", data.get("response-message").toString());
                                    in.putExtra("mobileno", edit_mobile_quick.getText().toString().trim());
                                    in.putExtra("agent_id", agent_id_full);
                                    in.putExtra("amount", edit_amount_quick.getText().toString());
                                    in.putExtra("requesttype", "wallet");

                                    startActivity(in);
                                    finish();
                                } else {
                                    Toast.makeText(context, data.getString("response-message").toString(), Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            L.m2("Contact", "Response: " + data.toString());
            uriContact = data.getData();
            retrieveContactNumber();
        }
    }
    private void retrieveContactNumber() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
            }
        } else {
            getContact();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        getContact();
                    }
                } else {
                    Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void getContact() {
        String contactNumber = null;
        Cursor c = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (c.moveToFirst()) {
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            phones.moveToFirst();
            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (cNumber.equals(null)) {
                Toast.makeText(context, "Invalid Number", Toast.LENGTH_LONG).show();
            } else if (cNumber.equals("") || cNumber.length() < 10) {

            }
            if (cNumber.length() >= 10) {
                Service sc = new Service();
                String correctno = sc.validateMobileNumber(cNumber);

                if (null != correctno) {
                    edit_mobile_quick.setText(correctno);
                } else {
                    Toast.makeText(context, "Invalid Contact Details", Toast.LENGTH_LONG).show();
                }
            }
            phones.close();
            L.m2("Contact", "Contact Phone Number: " + contactNumber);
        }
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
