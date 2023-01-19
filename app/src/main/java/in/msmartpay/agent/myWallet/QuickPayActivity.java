package in.msmartpay.agent.myWallet;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.internal.LinkedTreeMap;

import in.msmartpay.agent.R;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.wallet.WalletTransferRequest;
import in.msmartpay.agent.rechargeBillPay.SuccessDetailActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Service;
import in.msmartpay.agent.utility.TextDrawable;
import in.msmartpay.agent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class QuickPayActivity extends BaseActivity {

    private Button btn_quick_pay,btn_validate_receiver;
    private EditText edit_mobile_quick, edit_amount_quick, edit_remark_quick,edit_name_quick;
    private ImageView image_contactlist_quick;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private ProgressDialogFragment pd;
    private Context context;
    private String agentID, txn_key = "",agent_id_full,receiverAgentId="";
    private LinearLayout ll_receiver_details;
    private TextView iv_quick_transfer_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_pay_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quick Wallet Transfer");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = QuickPayActivity.this;

        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        agent_id_full = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_FULL);

        edit_mobile_quick = (EditText) findViewById(R.id.edit_mobile_quick);
        edit_amount_quick = (EditText) findViewById(R.id.edit_amount_quick);
        edit_remark_quick = (EditText) findViewById(R.id.edit_remark_quick);
        edit_name_quick = (EditText) findViewById(R.id.edit_name_quick);
        //image_contactlist_quick = (ImageView) findViewById(R.id.image_contactlist_quick);
        btn_quick_pay =  findViewById(R.id.btn_quick_pay);
        btn_validate_receiver =  findViewById(R.id.btn_validate_receiver);

        ll_receiver_details =  findViewById(R.id.ll_receiver_details);
        iv_quick_transfer_message =  findViewById(R.id.iv_quick_transfer_message);

        String code = "+91      ";
        //edit_mobile_quick.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        //edit_mobile_quick.setCompoundDrawablePadding(code.length()*10);

        //String code1 = "\u20B9   ";
        //edit_amount_quick.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code1), null, null, null);
        //edit_amount_quick.setCompoundDrawablePadding(code1.length()*10);
        /*image_contactlist_quick.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_CONTACTS);
        });*/

        btn_quick_pay.setOnClickListener(v -> {
            if(isConnectionAvailable()) {
                if (TextUtils.isEmpty(edit_amount_quick.getText().toString().trim())) {
                    edit_amount_quick.requestFocus();
                    L.toastS(context, "Enter Amount !");
                }else if (TextUtils.isEmpty(edit_remark_quick.getText().toString().trim())) {
                    edit_remark_quick.requestFocus();
                    L.toastS(context, "Enter Remark !");
                } else {
                    quickPayRequest();
                }
            }else{
                L.toastS(context, "No Internet Connection !!!");
            }
        });

        btn_validate_receiver.setOnClickListener(v -> {
            if(isConnectionAvailable()) {
                if (TextUtils.isEmpty(edit_mobile_quick.getText().toString().trim())) {
                    edit_mobile_quick.requestFocus();
                    L.toastS(context, "Enter 10 digit mobile no or full agent Id. !!!");
                } else {
                    validateReceiverId();
                }
            }else{
                L.toastS(context, "No Internet Connection !!!");
            }
        });
    }



    //===========quickPayRequest==============

    private void quickPayRequest() {
        if(NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Quick Pay...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            WalletTransferRequest request = new WalletTransferRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setToAgentId(receiverAgentId);
            request.setRemark(edit_remark_quick.getText().toString().trim());
            request.setAmount(edit_amount_quick.getText().toString().trim());

            RetrofitClient.getClient(getApplicationContext())
                    .walletTransfer(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {

                            iv_quick_transfer_message.setText(res.getResponseMessage());
                            Util.showView(iv_quick_transfer_message);
                            edit_amount_quick.setText("");
                            edit_remark_quick.setText("");
                            edit_amount_quick.requestFocus();

                        } else {
                            iv_quick_transfer_message.setText(res.getResponseMessage());
                            Util.showView(iv_quick_transfer_message);

                        }
                    } else {
                        iv_quick_transfer_message.setText("Server Error");
                        Util.showView(iv_quick_transfer_message);

                        L.toastS(getApplicationContext(), "Server Error");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failure " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void validateReceiverId() {
        if(NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Quick Pay...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            WalletTransferRequest request = new WalletTransferRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setToAgentId(edit_mobile_quick.getText().toString().trim());


            RetrofitClient.getClient(getApplicationContext())
                    .validateWalletId(request).enqueue(new Callback<MainResponse2>() {
                        @Override
                        public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                            pd.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                MainResponse2 res = response.body();
                                if (res.getResponseCode()!= null && res.getResponseCode().equals("0")) {
                                    iv_quick_transfer_message.setText(res.getResponseMessage());
                                    Util.showView(iv_quick_transfer_message);
                                    LinkedTreeMap<String,String> data=(LinkedTreeMap<String,String>)res.getData();
                                    receiverAgentId=data.get("agentId");

                                    edit_name_quick.setText(data.get("name"));

                                    Util.showView(ll_receiver_details);
                                    Util.showView(btn_quick_pay);
                                    Util.hideView(btn_validate_receiver);
                                    edit_mobile_quick.setEnabled(false);
                                    edit_name_quick.setEnabled(false);
                                    edit_amount_quick.requestFocus();


                                } else {
                                    Util.showView(btn_validate_receiver);
                                    Util.hideView(ll_receiver_details);
                                    Util.hideView(btn_quick_pay);
                                    iv_quick_transfer_message.setText(res.getResponseMessage());
                                    Util.showView(iv_quick_transfer_message);

                                }
                            } else {
                                Util.showView(btn_validate_receiver);
                                Util.hideView(ll_receiver_details);
                                Util.hideView(btn_quick_pay);

                                iv_quick_transfer_message.setText("Server Error");
                                Util.showView(iv_quick_transfer_message);
                                L.toastS(getApplicationContext(), "Server Error");
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                            L.toastS(getApplicationContext(), "data failure " + t.getLocalizedMessage());
                            pd.dismiss();
                        }
                    });
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d("Contact", "Response: " + data.toString());
            uriContact = data.getData();
            retrieveContactNumber();
        }
    }*/

    /*private void retrieveContactNumber() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
            }
        } else {
            getContact();
        }

    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        getContact();
                    }
                } else {
                    L.toastS(this, "No Permission Granted");
                }
            }
        }
    }*/
    /*public void getContact() {
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
                L.toastS(context, "Invalid Number");
            } else if (cNumber.equals("") || cNumber.length() < 10) {

            }
            if (cNumber.length() >= 10) {
                Service sc = new Service();
                String correctno = sc.validateMobileNumber(cNumber);

                if (null != correctno) {
                    edit_mobile_quick.setText(correctno);
                } else {
                    L.toastS(context, "Invalid Contact Details");
                }
            }
            phones.close();
            Log.d("Contact", "Contact Phone Number: " + contactNumber);
        }
    }*/
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
