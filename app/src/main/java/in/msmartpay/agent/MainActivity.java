package in.msmartpay.agent;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import in.msmartpay.agent.aeps.AEPSActivity;
import in.msmartpay.agent.aeps.ActivateServiceActivity;
import in.msmartpay.agent.busBooking.BusBookingSearchActivity_H;
import in.msmartpay.agent.helpAndSupport.HelpSupportActivity;
import in.msmartpay.agent.location.LocationJobIntent;
import in.msmartpay.agent.location.LocationMonitoringService;
import in.msmartpay.agent.moneyTransferNew.MoneyTransferActivity;
import in.msmartpay.agent.moneyTransferNew.SenderHistoryActivity;
import in.msmartpay.agent.moneyTransferNew.SenderVerifyRegisterActivity;
import in.msmartpay.agent.rechargeBillPay.DataCardRechargeActivity;
import in.msmartpay.agent.rechargeBillPay.DthRechargeActivity;
import in.msmartpay.agent.rechargeBillPay.ElectricityPayActivity;
import in.msmartpay.agent.rechargeBillPay.GasPayActivity;
import in.msmartpay.agent.rechargeBillPay.InsurancePayActivity;
import in.msmartpay.agent.rechargeBillPay.LandlinePayActivity;
import in.msmartpay.agent.rechargeBillPay.PostpaidMobileActivity;
import in.msmartpay.agent.rechargeBillPay.PrepaidMobileActivity;
import in.msmartpay.agent.rechargeBillPay.WaterPayActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.Util;

public class MainActivity extends DrawerActivity {

    private ImageView img;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String agentID, txn_key = "", balance;
    private TextView balanceview;
    private String wallet_url = HttpURL.WALLET_BALANCE;
    private Context context;
    private LinearLayout my_profile, id_money_transfer, id_money_transfer2;
    private ProgressDialog pd;
    public static JSONObject jsonObjectStatic = new JSONObject();
    //private String url_get_session = HttpURL.GET_DMR_SESSION;
    private String url_find_sender = HttpURL.FIND_SENDER;
    private String url_find_sender_2 = HttpURL.FIND_SENDER_Dmr2;
    private String url_sender_resistration = HttpURL.SENDER_RESISTRATION;
    private String url_sender_resend_otp = HttpURL.SENDER_RESEND_OTP;
    private String url_sender_resend_otp_dmr2 = HttpURL.SENDER_RESEND_OTP_Dmr2;
    private String SessionID;
    private String MobileNo, dmrVendor;
    private FloatingActionButton floatingButtonCall;
    private LocationManager locationManager=null;
    private boolean mAlreadyStartedService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        getSupportActionBar().hide();
        mDrawer.addView(contentView, 0);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = MainActivity.this;

        jsonObjectStatic = null;
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        agentID = sharedPreferences.getString("agentonlyid", null);
        txn_key = sharedPreferences.getString("txn-key", null);
        balance = sharedPreferences.getString("balance", null);
        dmrVendor = sharedPreferences.getString("dmrVendor", "");
        L.m2("sharedPreferences->", " : Agent Id : " + agentID + " : Txn : " + txn_key + " : Bal : " + balance);

        balanceview = (TextView) findViewById(R.id.tv_bal);
        img = (ImageView) findViewById(R.id.id_drawer_icon);
        my_profile = (LinearLayout) findViewById(R.id.my_profile);
        floatingButtonCall = (FloatingActionButton) findViewById(R.id.fab);

        id_money_transfer = findViewById(R.id.id_money_transfer);
        id_money_transfer2 = findViewById(R.id.id_money_transfer2);

        balanceview.setText("\u20B9 " + balance);

        if (dmrVendor.equalsIgnoreCase("DMR1")) {
            id_money_transfer2.setVisibility(View.GONE);
            id_money_transfer.setVisibility(View.VISIBLE);
        } else if (dmrVendor.equalsIgnoreCase("DMR2")) {
            id_money_transfer.setVisibility(View.GONE);
            id_money_transfer2.setVisibility(View.VISIBLE);
        }else{
            id_money_transfer2.setVisibility(View.GONE);
            id_money_transfer.setVisibility(View.GONE);
        }

        img.setOnClickListener(v -> mDrawer.openDrawer(drawerpane));

        my_profile.setOnClickListener(view -> {
            Intent intent = new Intent(context, MyProfile.class);
            startActivity(intent);
        });

        floatingButtonCall.setOnClickListener(view -> {
            Intent intent = new Intent(context, HelpSupportActivity.class);
            startActivity(intent);
        });

    }

    public void click(View view) {
        if (isConnectionAvailable()) {
            //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            if (view.getId() == R.id.id_mobile_prepaid) {

                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, PrepaidMobileActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_mobile_postpaid) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, PostpaidMobileActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_dth) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, DthRechargeActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_datacard) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, DataCardRechargeActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_landline) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, LandlinePayActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_electricity) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, ElectricityPayActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_gas) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, GasPayActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_water) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, WaterPayActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_insurence) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, InsurancePayActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_indo_nepal) {
                view.getResources().getColor(R.color.active_tab);
                Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show();
            } else if (view.getId() == R.id.id_Aeps) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, AEPSActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_money_transfer) {
                view.getResources().getColor(R.color.active_tab);
                showMoneyTransferDMTDialog(1);
            } else if (view.getId() == R.id.id_money_transfer2) {
                view.getResources().getColor(R.color.active_tab);
                showMoneyTransferDMTDialog2(1);
            } else if (view.getId() == R.id.id_bus) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, BusBookingSearchActivity_H.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_airplane) {
                view.getResources().getColor(R.color.active_tab);

                Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
            } else if (view.getId() == R.id.id_hotel) {
                view.getResources().getColor(R.color.active_tab);
                Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void showMoneyTransferDMTDialog2(final int i) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(MainActivity.this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.new_dmr_dialog_sender_mobile_dmt);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit = d.findViewById(R.id.btn_push_submit);
        final Button btnClosed = d.findViewById(R.id.close_push_button);
        final EditText editMobileNo = d.findViewById(R.id.edit_push_balance);
        final TextView title = (TextView) d.findViewById(R.id.title);
        if (i == 1) {
            title.setText("Find Sender");
        }
        if (i == 2) {
            title.setText("Sender Mobile No.");
        }

        btnSubmit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editMobileNo.getText().toString().trim())) {
                Toast.makeText(context, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
            } else {
                if (i == 1) {
                    MobileNo = editMobileNo.getText().toString().trim();
                    findSenderRequest2();
                    d.dismiss();
                }
                if (i == 2) {
                    Intent intent = new Intent(context, SenderHistoryActivity.class);
                    intent.putExtra("MobileNo", editMobileNo.getText().toString().trim());
                    startActivity(intent);
                    d.dismiss();
                }
            }
        });

        btnClosed.setOnClickListener(v -> {
            // TODO Auto-generated method stub

            d.cancel();
        });

        d.show();
    }

    private void findSenderRequest2() {
        pd = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txn_key)
                    .put("AgentID", agentID)
                    .put("SenderId", MobileNo);

            L.m2("Req-findSender", jsonObjectReq.toString());
            L.m2("Url-findSender", url_find_sender_2);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender_2, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObjectStatic = object;
                            System.out.println("Object--findSender" + object.toString());
                            try {
                                pd.dismiss();

                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    L.m2("url-called", url_find_sender_2);
                                    L.m2("resp-findSender", object.toString());

                                    JSONObject senderDetailsObject = object.getJSONObject("SenderDetails");
                                    editor.putString("SenderId", senderDetailsObject.getString("SenderId"));
                                    editor.putString("SenderName", senderDetailsObject.getString("Name"));
                                    editor.putString("ResisteredMobileNo", MobileNo);
                                    editor.commit();
                                    Intent intent = new Intent(MainActivity.this, in.msmartpay.agent.dmr2Moneytrasfer.MoneyTransferActivity.class);
                                    intent.putExtra("SenderLimit_Detail_BeniList", object.toString());
                                    startActivity(intent);
                                    finish();
                                } else if (object.getString("Status").equalsIgnoreCase("2")) {
                                    JSONObject senderDetailsObject = object.getJSONObject("SenderDetails");
                                    editor.putString("SenderId", senderDetailsObject.getString("SenderId"));
                                    editor.putString("SenderName", senderDetailsObject.getString("Name"));
                                    editor.putString("ResisteredMobileNo", MobileNo);
                                    editor.commit();

                                    reSendOtpRequest2(senderDetailsObject.getString("Name"));
                                    //resisterSenderDialog(object.getString("Status"), object.getString("message"));
                                } else if (object.getString("Status").equalsIgnoreCase("3")) {
                                    editor.putString("ResisteredMobileNo", MobileNo);
                                    editor.commit();

                                    resisterSenderDialog(object.getString("message"));
                                } else if (object.getString("Status").equalsIgnoreCase("5")) {

                                    activateServiceDialogBox("Please activate your DMR Service...");
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getBanlance();
        requestMyPermissions();
    }

    private void getBanlance() {
        /*pd = new ProgressDialog(MainActivity.this);
        pd = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(true);*/
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key);
            L.m2("url-wallet", wallet_url);
            L.m2("Request--wallet", jsonObjectReq.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, wallet_url, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            try {
                                // pd.dismiss();
                                L.m2("wallet-url", wallet_url);
                                L.m2("data-wallet", data.toString());

                                if (data.getString("response-code") != null && data.getString("response-code").equals("0")) {
                                    editor.putString("balance", "" + data.getString("updBal"));
                                    editor.commit();
                                    balanceview.setText("\u20B9 " + data.getString("updBal"));
                                    dmrVendor=data.getString("dmr_vendor");
                                    if (dmrVendor.equalsIgnoreCase("DMR1")) {
                                        id_money_transfer2.setVisibility(View.GONE);
                                        id_money_transfer.setVisibility(View.VISIBLE);
                                    } else if (dmrVendor.equalsIgnoreCase("DMR2")) {
                                        id_money_transfer.setVisibility(View.GONE);
                                        id_money_transfer2.setVisibility(View.VISIBLE);
                                    }else{
                                        id_money_transfer2.setVisibility(View.GONE);
                                        id_money_transfer.setVisibility(View.GONE);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // pd.dismiss();
                    Toast.makeText(MainActivity.this, " " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMoneyTransferDMTDialog(final int i) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(MainActivity.this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.new_dmr_dialog_sender_mobile_dmt);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit = d.findViewById(R.id.btn_push_submit);
        final Button btnClosed = d.findViewById(R.id.close_push_button);
        final EditText editMobileNo = d.findViewById(R.id.edit_push_balance);
        final TextView title = (TextView) d.findViewById(R.id.title);
        if (i == 1) {
            title.setText("Find Sender");
        }
        if (i == 2) {
            title.setText("Sender Mobile No.");
        }

        btnSubmit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editMobileNo.getText().toString().trim())) {
                Toast.makeText(context, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
            } else {
                if (i == 1) {
                    MobileNo = editMobileNo.getText().toString().trim();
                    findSenderRequest();
                    d.dismiss();
                }
                if (i == 2) {
                    Intent intent = new Intent(context, SenderHistoryActivity.class);
                    intent.putExtra("MobileNo", editMobileNo.getText().toString().trim());
                    startActivity(intent);
                    d.dismiss();
                }
            }
        });

        btnClosed.setOnClickListener(v -> {
            // TODO Auto-generated method stub

            d.cancel();
        });

        d.show();
    }

    //Json request for FindRegister
    private void findSenderRequest() {
        pd = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("Key", txn_key)
                    .put("AgentID", agentID)
                    .put("SenderId", MobileNo);

            L.m2("Req-findSender", jsonObjectReq.toString());
            L.m2("Url-findSender", url_find_sender);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObjectStatic = object;
                            System.out.println("Object--findSender" + object.toString());
                            try {
                                pd.dismiss();

                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    L.m2("url-called", url_find_sender);
                                    L.m2("resp-findSender", object.toString());

                                    JSONObject senderDetailsObject = object.getJSONObject("SenderDetails");
                                    editor.putString("SenderId", senderDetailsObject.getString("SenderId"));
                                    editor.putString("SenderName", senderDetailsObject.getString("Name"));
                                    editor.putString("ResisteredMobileNo", MobileNo);
                                    editor.commit();
                                    Intent intent = new Intent(MainActivity.this, MoneyTransferActivity.class);
                                    intent.putExtra("SenderLimit_Detail_BeniList", object.toString());
                                    startActivity(intent);
                                    finish();
                                } else if (object.getString("Status").equalsIgnoreCase("2")) {
                                    JSONObject senderDetailsObject = object.getJSONObject("SenderDetails");
                                    editor.putString("SenderId", senderDetailsObject.getString("SenderId"));
                                    editor.putString("SenderName", senderDetailsObject.getString("Name"));
                                    editor.putString("ResisteredMobileNo", MobileNo);
                                    editor.commit();

                                    reSendOtpRequest(senderDetailsObject.getString("Name"));
                                    //resisterSenderDialog(object.getString("Status"), object.getString("message"));
                                } else if (object.getString("Status").equalsIgnoreCase("3")) {
                                    editor.putString("ResisteredMobileNo", MobileNo);
                                    editor.commit();

                                    resisterSenderDialog(object.getString("message"));
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    //================For Resistration===============
    public void resisterSenderDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(MainActivity.this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK = d.findViewById(R.id.btn_resister_ok);
        final Button btnNO = d.findViewById(R.id.btn_resister_no);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final Button btnClosed = d.findViewById(R.id.close_push_button);

        tvMessage.setText(msg);
        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, in.msmartpay.agent.dmr2Moneytrasfer.SenderRegistrationActivity.class);
                startActivity(intent);
                d.dismiss();
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

    //Json request for find Sender
    private void reSendOtpRequest(final String SenderName) {
        pd = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txn_key)
                    .put("SenderId", MobileNo);

            L.m2("Req-senderResend", jsonObjectReq.toString());
            L.m2("url-called", url_sender_resend_otp);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_sender_resend_otp, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            //jsonObjectStatic = object;
                            System.out.println("Object--senderResendOtp>" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    L.m2("resp-senderResend", object.toString());

                                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, SenderVerifyRegisterActivity.class);
                                    intent.putExtra("MobileNo", MobileNo);
                                    intent.putExtra("SenderName", SenderName);
                                    intent.putExtra("Address", "");
                                    intent.putExtra("DOB", "");
                                    startActivity(intent);
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    private void reSendOtpRequest2(final String SenderName) {
        pd = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txn_key)
                    .put("SenderId", MobileNo);

            L.m2("Req-senderResend", jsonObjectReq.toString());
            L.m2("url-called", url_sender_resend_otp_dmr2);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_sender_resend_otp_dmr2, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            //jsonObjectStatic = object;
                            System.out.println("Object--senderResendOtp>" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    L.m2("resp-senderResend", object.toString());

                                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, in.msmartpay.agent.dmr2Moneytrasfer.SenderVerifyRegisterActivity.class);
                                    intent.putExtra("MobileNo", MobileNo);
                                    intent.putExtra("SenderName", SenderName);
                                    intent.putExtra("Address", "");
                                    intent.putExtra("DOB", "");
                                    startActivity(intent);
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }
    /*//Json request for get Session
    private void getDMRSessionRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key);
            Log.e("Request",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_get_session, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("Object----session>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called", url_get_session);
                                    L.m2("url data", object.toString());
                                    editor.putString("SessionID", object.getString("SessionId"));
                                    editor.commit();
                                    SessionID = object.getString("SessionId");
                                    showMoneyTransferDMTDialog();
                                }
                                else {
                                    pd.dismiss();
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
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    public void showMoneyTransferDMTDialog() {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dialog_sender_mobile_dmt);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnSubmit =  d.findViewById(R.id.btn_push_submit);
        Button btnClosed =  d.findViewById(R.id.close_push_button);
        final EditText editMobileNo =  d.findViewById(R.id.edit_push_balance);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editMobileNo.getText().toString().trim())) {
                    Toast.makeText(context, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    MobileNo = editMobileNo.getText().toString().trim();
                    findSenderRequest();
                    d.dismiss();
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

    //Json request for resistration
    private void findSenderRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("SessionId", SessionID)
                    .put("SenderId", MobileNo);
            Log.e("Request",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_find_sender, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObjectStatic=object;
                            System.out.println("Object----findSender>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called", url_find_sender);
                                    L.m2("url data", object.toString());
                                    editor.putString("ResisteredMobileNo", MobileNo.toString());
                                    editor.commit();
                                    Intent intent = new Intent(context, MoneyTransferActivity1.class);
                                    intent.putExtra("SenderLimit_Detail_BeniList", object.toString());
                                    startActivity(intent);
                                }
                                else {
                                    pd.dismiss();
                                    Toast.makeText(context, object.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                    resisterSenderDialog(object.getString("message").toString());
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
                    Toast.makeText(getApplicationContext(), "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    //================For Resistration===============
    public void resisterSenderDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);

        d.setCancelable(false);

        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        d.setContentView(R.layout.dmr_sender_resister_dialog);

        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK =  d.findViewById(R.id.btn_resister_ok);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);

        tvMessage.setText(msg);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senderResistrationRequest();
                d.dismiss();
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

    //Json request for find Sender
    private void senderResistrationRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq=new JSONObject()
                    .put("agent_id", agentID)
                    .put("txn_key", txn_key)
                    .put("SessionId", SessionID)
                    .put("SenderId", MobileNo);
            Log.e("Request",jsonObjectReq.toString());
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_sender_resistration, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            jsonObjectStatic=object;
                            System.out.println("Object----senderResistration>"+object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url-called", url_sender_resistration);
                                    L.m2("url data", object.toString());

                                    Intent intent = new Intent(context, SenderResistrationActivity.class);
                                    intent.putExtra("MobileNo", MobileNo);
                                    startActivity(intent);
                                }
                                else {
                                    pd.dismiss();
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
                    Toast.makeText(getApplicationContext(), "Server Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(context)
                    .setIcon(R.drawable.warning_message_red)
                    .setTitle("Closing Application")
                    .setMessage("Are you sure you want to Exit ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //================For Resistration===============
    public void activateServiceDialogBox(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.new_dmr_sender_resister_dialog);

        final Button btnOK = d.findViewById(R.id.btn_resister_ok);
        final Button btnNO = d.findViewById(R.id.btn_resister_no);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final TextView title = (TextView) d.findViewById(R.id.title);
        title.setText("Activate Service");
        tvMessage.setText(msg);

        btnNO.setOnClickListener(v -> d.dismiss());

        btnOK.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivateServiceActivity.class);
            startActivity(intent);
            d.dismiss();
        });

        d.show();
    }


    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestMyPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Dexter.withActivity(this)
                    .withPermissions(Arrays.asList(
                            Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                            if (report.areAllPermissionsGranted()) {
                                 locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                //Check gps is enable or not
                                assert locationManager != null;
                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    //Write Function To enable gps
                                    Util.onGPS(context);
                                } else {
                                    //GPS is already On then
                                    startWorkerManager();
                                }


                                 /* //Worker
                                try {
                                    if (!Util.isWorkScheduled(MyLocationWorker.getWorkInfoList(getApplicationContext()))) {
                                        L.m2("isWorkScheduled","false");
                                        MyLocationWorker.startLocationWorker(getApplicationContext());
                                    }else {
                                        L.m2("isWorkScheduled","true");
                                    }
                                }catch (Exception e){
                                    L.m2("isWorkScheduled","error - "+e.getLocalizedMessage());
                                }
                                //Service

                                // foregroundOnlyLocationService.subscribeToLocationUpdates();
*/
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            Dexter.withActivity(this)
                    .withPermissions(Arrays.asList(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                startWorkerManager();

                               /* //Worker
                                try {
                                    if (!Util.isWorkScheduled(MyLocationWorker.getWorkInfoList(getApplicationContext()))) {
                                        L.m2("isWorkScheduled","false");
                                        MyLocationWorker.startLocationWorker(getApplicationContext());
                                    }else {
                                        L.m2("isWorkScheduled","true");
                                    }
                                }catch (Exception e){
                                    L.m2("isWorkScheduled","error - "+e.getLocalizedMessage());
                                }
                                //Service

                                // foregroundOnlyLocationService.subscribeToLocationUpdates();
*/
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void startWorkerManager() {
          LocationJobIntent.start(MainActivity.this);
          startStep1();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Location Permission");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        //builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void startStep1() {
        //Check whether this user has installed Google play service which is being used by Location updates.
        if (Util.isGooglePlayServicesAvailable(this)) {

            //Passing null to indicate that it is executing for the first time.
            startStep3();

        } else {
            Toast.makeText(getApplicationContext(), "Google Play Service is not available.", Toast.LENGTH_LONG).show();
        }

    }

    private void startStep3() {
        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService) {
            L.m2("Location", "Service Started");
            //mMsgView.setText(R.string.msg_location_service_started);

            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }

    }

    @Override
    public void onDestroy() {


        //Stop location sharing service to app server.........
        //startWorkerManager();
        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = true;
        //Ends................................................

        super.onDestroy();
    }
}