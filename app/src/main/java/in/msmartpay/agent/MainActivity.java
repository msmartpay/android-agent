package in.msmartpay.agent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import in.msmartpay.agent.aeps.AEPSActivity;
import in.msmartpay.agent.aeps.ActivateServiceActivity;
import in.msmartpay.agent.busBooking.BusBookingSearchActivity_H;
import in.msmartpay.agent.dmr1MoneyTransfer.MoneyTransferActivity;
import in.msmartpay.agent.dmr1MoneyTransfer.SenderHistoryActivity;
import in.msmartpay.agent.dmr1MoneyTransfer.SenderVerifyRegisterActivity;
import in.msmartpay.agent.helpAndSupport.HelpSupportActivity;
import in.msmartpay.agent.location.GPSTrackerPresenter;
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
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.MyAppUpdateManager;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.Util;

public class MainActivity extends DrawerActivity implements GPSTrackerPresenter.LocationListener {
    private ImageView img;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String agentID = "", txn_key = "", balance = "0.0";
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

    private GPSTrackerPresenter gpsTrackerPresenter = null;

    // Declare the UpdateManager
    private MyAppUpdateManager mUpdateManager;
    private CardView cv_app_update;
    private Button btnLater;
    private Button btnDownloadInstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_main, null, false);
            getSupportActionBar().hide();
            mDrawer.addView(contentView, 0);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            context = MainActivity.this;
            gpsTrackerPresenter = new GPSTrackerPresenter(this, this, GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);
            jsonObjectStatic = null;
            sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            agentID = sharedPreferences.getString("agentonlyid", "");
            txn_key = sharedPreferences.getString("txn-key", "");
            balance = sharedPreferences.getString("balance", "0.0");
            dmrVendor = sharedPreferences.getString("dmrVendor", "");
            L.m2("sharedPreferences->", " : Agent Id : " + agentID + " : Txn : " + txn_key + " : Bal : " + balance);

            balanceview = findViewById(R.id.tv_bal);
            img = findViewById(R.id.id_drawer_icon);
            my_profile = findViewById(R.id.my_profile);
            floatingButtonCall = findViewById(R.id.fab);

            id_money_transfer = findViewById(R.id.id_money_transfer);
            id_money_transfer2 = findViewById(R.id.id_money_transfer2);

            balanceview.setText("\u20B9 " + balance);

            if (dmrVendor.equalsIgnoreCase("DMR1")) {
                id_money_transfer2.setVisibility(View.GONE);
                id_money_transfer.setVisibility(View.VISIBLE);
            } else if (dmrVendor.equalsIgnoreCase("DMR2")) {
                id_money_transfer.setVisibility(View.GONE);
                id_money_transfer2.setVisibility(View.VISIBLE);
            } else {
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
            initializeAppUpdateManager();
        } catch (Exception e) {
            L.m2("Error", "OnCreate" + e.getLocalizedMessage());
            L.s(getApplicationContext(), "OnCreate = " + e.getLocalizedMessage());
        }
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
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr1_dialog_sender_mobile_dmt);
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
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
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
                    object -> {
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
                                Intent intent = new Intent(context, in.msmartpay.agent.dmr2Moneytrasfer.MoneyTransferActivity.class);
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
                                Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }
                    }, error -> {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(getApplicationContext()).addToRequsetque(jsonrequest);

        } catch (JSONException e) {
            pd.dismiss();
            L.m2("Error", "parser res- " + e.getMessage());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getBanlance();
    }

    private void getBanlance() {
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
                                L.m2("wallet-url", wallet_url);
                                L.m2("data-wallet", data.toString());

                                if (data.getString("response-code") != null && data.getString("response-code").equals("0")) {
                                    editor.putString("balance", "" + data.getString("updBal"));
                                    editor.commit();
                                    balanceview.setText("\u20B9 " + data.getString("updBal"));
                                    dmrVendor = data.getString("dmr_vendor");
                                    if (dmrVendor.equalsIgnoreCase("DMR1")) {
                                        id_money_transfer2.setVisibility(View.GONE);
                                        id_money_transfer.setVisibility(View.VISIBLE);
                                    } else if (dmrVendor.equalsIgnoreCase("DMR2")) {
                                        id_money_transfer.setVisibility(View.GONE);
                                        id_money_transfer2.setVisibility(View.VISIBLE);
                                    } else {
                                        id_money_transfer2.setVisibility(View.GONE);
                                        id_money_transfer.setVisibility(View.GONE);
                                    }
                                }
                                if (data.has("response-message")) {
                                    if (data.getString("response-message").equalsIgnoreCase("Invalid request!")) {
                                        editor.putString(Keys.USER_CREDENTIALS, null);
                                        editor.clear();
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            } catch (Exception e) {
                                L.m2("Error", "" + e.getLocalizedMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, " " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);

        } catch (Exception e) {
            L.m2("Error", "" + e.getLocalizedMessage());
        }
    }

    public void showMoneyTransferDMTDialog(final int i) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr1_dialog_sender_mobile_dmt);
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
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
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
                                    Intent intent = new Intent(context, MoneyTransferActivity.class);
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
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
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
            L.m2("Error", "parser res- " + e.getMessage());
        }
    }

    //================For Resistration===============
    public void resisterSenderDialog(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
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
        btnNO.setOnClickListener(v -> d.dismiss());

        btnOK.setOnClickListener(v -> {
            Intent intent = new Intent(context, in.msmartpay.agent.dmr2Moneytrasfer.SenderRegistrationActivity.class);
            startActivity(intent);
            d.dismiss();
        });

        btnClosed.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            d.cancel();
        });
        d.show();
    }

    //Json request for find Sender
    private void reSendOtpRequest(final String SenderName) {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
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

                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, SenderVerifyRegisterActivity.class);
                                    intent.putExtra("MobileNo", MobileNo);
                                    intent.putExtra("SenderName", SenderName);
                                    intent.putExtra("Address", "");
                                    intent.putExtra("DOB", "");
                                    startActivity(intent);
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
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
            L.m2("Error", "parser res- " + e.getMessage());
        }
    }

    private void reSendOtpRequest2(final String SenderName) {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
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

                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, in.msmartpay.agent.dmr2Moneytrasfer.SenderVerifyRegisterActivity.class);
                                    intent.putExtra("MobileNo", MobileNo);
                                    intent.putExtra("SenderName", SenderName);
                                    intent.putExtra("Address", "");
                                    intent.putExtra("DOB", "");
                                    startActivity(intent);
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
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
            L.m2("Error", "Parser res- " + e.getMessage());
        }
    }


    //================For Resistration===============
    public void activateServiceDialogBox(String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr1_sender_resister_dialog);

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

    //--------------------------------------------In-APP-Update-------------------------------------------------------------
    private void initializeAppUpdateManager() {

        cv_app_update = findViewById(R.id.cv_app_update);
        btnLater = findViewById(R.id.btnLater);
        btnDownloadInstall = findViewById(R.id.btnDownloadInstall);
        btnLater.setOnClickListener(v -> Util.hideView(cv_app_update));
        btnDownloadInstall.setOnClickListener(v -> Util.openPlayStoreApp(context));

        Util.hideView(cv_app_update);
        new CheckVersion().execute();
       /* // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = MyAppUpdateManager.Builder(this);

        // Callback from UpdateInfoListener
        // You can get the available version code of the apk in Google Play
        // Number of days passed since the user was notified of an update through the Google Play
        mUpdateManager.addUpdateInfoListener(new MyAppUpdateManager.UpdateInfoListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                L.m2("onReceiveVersionCode", String.valueOf(code));
                //txtAvailableVersion.setText(String.valueOf(code));
            }

            @Override
            public void onReceiveStalenessDays(final int days) {
                L.m2("onReceiveStalenessDays", String.valueOf(days));
                //txtStalenessDays.setText(String.valueOf(days));
            }
        });

        // Callback from Flexible Update Progress
        // This is only available for Flexible mode
        // Find more from https://developer.android.com/guide/playcore/in-app-updates#monitor_flexible
        mUpdateManager.addFlexibleUpdateDownloadListener((bytesDownloaded, totalBytes) -> {
            L.m2("addFlexibleUpdateDownload", "Downloading: " + bytesDownloaded + " / " + totalBytes);
        });
        callImmediateUpdate(balanceview);
        //callFlexibleUpdate(balanceview);

        */
    }

    public void callFlexibleUpdate(View view) {
        // Start a Flexible Update
        mUpdateManager.mode(MyAppUpdateManager.FLEXIBLE).start();
        //txtFlexibleUpdateProgress.setVisibility(View.VISIBLE);
    }

    public void callImmediateUpdate(View view) {
        // Start a Immediate Update
        mUpdateManager.mode(MyAppUpdateManager.IMMEDIATE).start();
    }

    @SuppressLint("StaticFieldLeak")
    class CheckVersion extends AsyncTask<Void, Void, Void> {
        String versionName = null;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "in.msmartpay.agent" /*+ "&hl=en"*/)
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements elements = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : elements) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElement : sibElemets) {
                                versionName = sibElement.text();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                L.m2("Error", "Async Do- " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (versionName != null)
                    L.m2("AppVersion", "In Play Store : " + versionName);
                L.m2("AppVersion", "In App : " + Util.getAppVersionName(getApplicationContext()));
                if (!versionName.equals(Util.getAppVersionName(getApplicationContext()))) {
                    Util.showView(cv_app_update);
                } else {
                    Util.hideView(cv_app_update);
                }
            } catch (Exception e) {
                L.m2("Error", "Async Post- " + e.getMessage());
            }
        }
    }
//--------------------------------------------GPS Tracker--------------------------------------------------------------

    @Override
    public void onLocationFound(Location location) {
        gpsTrackerPresenter.stopLocationUpdates();
    }

    @Override
    public void locationError(String msg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE && resultCode == Activity.RESULT_OK) {
            if (gpsTrackerPresenter != null)
                gpsTrackerPresenter.onStart();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gpsTrackerPresenter != null)
            gpsTrackerPresenter.onStart();
    }

    @Override
    public void onDestroy() {
        //Ends................................................
        if (gpsTrackerPresenter != null)
            gpsTrackerPresenter.onPause();
        super.onDestroy();
    }
//--------------------------------------------End GPS Tracker--------------------------------------------------------------

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
                    .setPositiveButton("Yes", (dialog, which) -> finishAffinity())
                    .setNegativeButton("No", null)
                    .show();
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
