package msmartpay.in;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import msmartpay.in.helpAndSupport.HelpSupportActivity;
import msmartpay.in.moneyTransferNew.MoneyTransferActivity;
import msmartpay.in.moneyTransferNew.SenderHistoryActivity;
import msmartpay.in.moneyTransferNew.SenderRegistrationActivity;
import msmartpay.in.moneyTransferNew.SenderVerifyRegisterActivity;
import msmartpay.in.myWallet.BalanceRequestActivity;
import msmartpay.in.myWallet.MyEarningActivity;
import msmartpay.in.myWallet.QuickPayActivity;
import msmartpay.in.myWallet.TransactionSearchActivity;
import msmartpay.in.myWallet.WalletHistoryActivity;
import msmartpay.in.rechargeBillPay.DataCardRechargeActivity;
import msmartpay.in.rechargeBillPay.DthRechargeActivity;
import msmartpay.in.rechargeBillPay.ElectricityPayActivity;
import msmartpay.in.rechargeBillPay.GasPayActivity;
import msmartpay.in.rechargeBillPay.InsurancePayActivity;
import msmartpay.in.rechargeBillPay.LandlinePayActivity;
import msmartpay.in.rechargeBillPay.PostpaidMobileActivity;
import msmartpay.in.rechargeBillPay.PrepaidMobileActivity;
import msmartpay.in.rechargeBillPay.WaterPayActivity;
import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.HttpURL;
import msmartpay.in.utility.L;
import msmartpay.in.utility.Mysingleton;

public class DrawerActivity extends BaseActivity {
    public RelativeLayout drawerpane;
    private TextView draweragentid;
    private TextView draweragentemail, tv_version_code;
    public DrawerLayout mDrawer;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor editor;
    private RelativeLayout rl_recharge, rl_billpayment, rl_wallet,rl_finance;
    private ImageView iv_recharge, iv_billpayment, iv_wallet,iv_finance;
    private String agent_id_full, emailId, agentName;
    private LinearLayout ll_hide_recharge, ll_hide_billpayement, ll_hide_wallet,ll_hide_finance;
    private boolean flagRecharge = true, flagBill = true, flagWallet = true;
    private ProgressDialog pd;
    public static JSONObject jsonObjectStatic = new JSONObject();
    //private String url_get_session = HttpURL.GET_DMR_SESSION;
    private String url_find_sender = HttpURL.FIND_SENDER;
    private String url_sender_resistration = HttpURL.SENDER_RESISTRATION;
    private String url_sender_resend_otp = HttpURL.SENDER_RESEND_OTP;

    private String SessionID;
    private String MobileNo;
    private Context context;
    private String agentID, txn_key = "", balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.drawer_layout);

        context = DrawerActivity.this;
        jsonObjectStatic=null;
        myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = myPrefs.edit();
        agentID = myPrefs.getString("agentonlyid", null);
        txn_key = myPrefs.getString("txn-key", null);
        agent_id_full = myPrefs.getString("agent_id_full", null);
        emailId = myPrefs.getString("emailId", null);
        agentName = myPrefs.getString("agentName", null);
        drawerpane = (RelativeLayout) findViewById(R.id.left_side);
        draweragentid = (TextView) findViewById(R.id.drawer_agent_id);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        draweragentemail = (TextView) findViewById(R.id.drawer_email_address);
        iv_recharge = (ImageView) findViewById(R.id.iv_recharge);
        iv_billpayment = (ImageView) findViewById(R.id.iv_billpayment);
        iv_wallet = (ImageView) findViewById(R.id.iv_wallet);
        iv_finance = findViewById(R.id.iv_finance);

        rl_recharge = (RelativeLayout) findViewById(R.id.rl_recharge);
        rl_billpayment= (RelativeLayout) findViewById(R.id.rl_billpayment);
        rl_wallet= (RelativeLayout) findViewById(R.id.rl_wallet);
        rl_finance = findViewById(R.id.rl_finance);


        ll_hide_recharge = (LinearLayout) findViewById(R.id.ll_hide_recharge);
        ll_hide_billpayement= (LinearLayout) findViewById(R.id.ll_hide_billpayement);
        ll_hide_wallet= (LinearLayout) findViewById(R.id.ll_hide_wallet);
        ll_hide_finance = findViewById(R.id.ll_hide_finance);
        ll_hide_recharge.setVisibility(View.GONE);
        ll_hide_billpayement.setVisibility(View.GONE);
        ll_hide_wallet.setVisibility(View.GONE);
        ll_hide_finance.setVisibility(View.GONE);

        rl_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagRecharge) {
                    ll_hide_recharge.setVisibility(View.VISIBLE);
                    ll_hide_billpayement.setVisibility(View.GONE);
                    ll_hide_wallet.setVisibility(View.GONE);
                    iv_recharge.setImageResource(R.drawable.hide_icon);
                    iv_billpayment.setImageResource(R.drawable.open_add);
                    iv_wallet.setImageResource(R.drawable.open_add);
                    flagRecharge = false;
                }else{
                    ll_hide_recharge.setVisibility(View.GONE);
                    iv_recharge.setImageResource(R.drawable.open_add);
                    flagRecharge = true;
                }
            }
        });

        rl_billpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagBill) {
                    ll_hide_billpayement.setVisibility(View.VISIBLE);
                    ll_hide_recharge.setVisibility(View.GONE);
                    ll_hide_wallet.setVisibility(View.GONE);
                    ll_hide_finance.setVisibility(View.GONE);
                    iv_billpayment.setImageResource(R.drawable.hide_icon);
                    iv_recharge.setImageResource(R.drawable.open_add);
                    iv_wallet.setImageResource(R.drawable.open_add);
                    iv_finance.setImageResource(R.drawable.open_add);
                    flagBill = false;
                }else{
                    ll_hide_billpayement.setVisibility(View.GONE);
                    iv_billpayment.setImageResource(R.drawable.open_add);
                    flagBill = true;
                }
            }
        });

        rl_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagBill) {
                   ll_hide_finance.setVisibility(View.VISIBLE);
                   iv_finance.setImageResource(R.drawable.hide_icon);
                    ll_hide_recharge.setVisibility(View.GONE);
                    ll_hide_wallet.setVisibility(View.GONE);
                    iv_recharge.setImageResource(R.drawable.open_add);
                    iv_wallet.setImageResource(R.drawable.open_add);

                   flagBill = false;
                }else{
                    ll_hide_finance.setVisibility(View.GONE);
                    iv_finance.setImageResource(R.drawable.open_add);
                    flagBill = true;
                }
            }
        });

        rl_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagWallet) {
                    ll_hide_wallet.setVisibility(View.VISIBLE);
                    ll_hide_billpayement.setVisibility(View.GONE);
                    ll_hide_recharge.setVisibility(View.GONE);
                    ll_hide_finance.setVisibility(View.GONE);
                    iv_wallet.setImageResource(R.drawable.hide_icon);
                    iv_billpayment.setImageResource(R.drawable.open_add);
                    iv_recharge.setImageResource(R.drawable.open_add);
                    flagWallet = false;
                }else{
                    ll_hide_wallet.setVisibility(View.GONE);
                    iv_wallet.setImageResource(R.drawable.open_add);
                    flagWallet = true;
                }
            }
        });




        if (agentName == null && agent_id_full == null) {
        } else {
            draweragentid.setText(agent_id_full);
            draweragentemail.setText(emailId);
            tv_version_code.setText("Version " + BuildConfig.VERSION_NAME);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ll_hide_recharge.setVisibility(View.GONE);
        ll_hide_billpayement.setVisibility(View.GONE);
        ll_hide_wallet.setVisibility(View.GONE);
        iv_recharge.setImageResource(R.drawable.open_add);
        iv_billpayment.setImageResource(R.drawable.open_add);
        iv_wallet.setImageResource(R.drawable.open_add);
    }

    public void start(View v) {
        if (isConnectionAvailable()) {
            if (v.getId() == R.id.id_w2w) {
                Intent intent = new Intent(context, QuickPayActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_recharge_prepaid){
                Intent intent = new Intent(context, PrepaidMobileActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_recharge_pendrive){
                Intent intent = new Intent(context, DataCardRechargeActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_recharge_dth){
                Intent intent = new Intent(context, DthRechargeActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_billpayment_postpaid){
                Intent intent = new Intent(context, PostpaidMobileActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_billpayment_landline){
                Intent intent = new Intent(context, LandlinePayActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_billpayment_electric){
                Intent intent = new Intent(context, ElectricityPayActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_billpayment_insurance){
                Intent intent = new Intent(context, InsurancePayActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_billpayment_water){
                Intent intent = new Intent(context, WaterPayActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_billpayment_gas){
                Intent intent = new Intent(context, GasPayActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_dmr){
                //getDMRSessionRequest();
                showMoneyTransferDMTDialog(1);
              //  Toast.makeText(context, "Pending...", Toast.LENGTH_SHORT).show();
            }else if(v.getId() == R.id.ll_wallet_bal_req){
                Intent intent = new Intent(context, BalanceRequestActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_wallet_trans_search){
                Intent intent = new Intent(context, TransactionSearchActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_wallet_history){
                Intent intent = new Intent(context, WalletHistoryActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if(v.getId() == R.id.ll_wallet_earning){
                Intent intent = new Intent(context, MyEarningActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if (v.getId() == R.id.mprofile) {
                Intent intent = new Intent(context, MyProfile.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }else if (v.getId() == R.id.cpassword) {
                Intent intent = new Intent(context, ResetPasswordActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }/*else if (v.getId() == R.id.about_us) {
                Intent in = new Intent(context, AboutUsWebViewActivity.class);
                in.putExtra("url", HttpURL.ABOUT_US);
                startActivity(in);
                mDrawer.closeDrawers();
            }*/ else if (v.getId() == R.id.help_support) {
                Intent intent = new Intent(context, HelpSupportActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }/*else if (v.getId() == R.id.qr_code_drawer) {
                Intent intent = new Intent(context, QRActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }*/ else if (v.getId() == R.id.logout) {
                myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor emyPrefs = myPrefs.edit();
                emyPrefs.clear();
                emyPrefs.commit();
                Toast.makeText(this, "Thank you for visiting here !!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else if (v.getId() == R.id.ll_finance_indo_nepal) {
               /* Intent intent = new Intent(context, HelpSupportActivity.class);
                startActivity(intent);*/

                Toast.makeText(this, "Coming soon...", Toast.LENGTH_LONG).show();
                mDrawer.closeDrawers();
            }else if (v.getId() == R.id.ll_aeps) {
               /* Intent intent = new Intent(context, HelpSupportActivity.class);
                startActivity(intent);*/

                Toast.makeText(this, "Coming soon...", Toast.LENGTH_LONG).show();
                mDrawer.closeDrawers();
            }else if (v.getId() == R.id.ll_money_transfer) {
               /* Intent intent = new Intent(context, HelpSupportActivity.class);
                startActivity(intent);*/

                showMoneyTransferDMTDialog(1);
                mDrawer.closeDrawers();
            }
        } else {
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
    public void showMoneyTransferDMTDialog(final int i) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(DrawerActivity.this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.new_dmr_dialog_sender_mobile_dmt);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnSubmit =  d.findViewById(R.id.btn_push_submit);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);
        final EditText editMobileNo =  d.findViewById(R.id.edit_push_balance);
        final TextView title = (TextView) d.findViewById(R.id.title);
        if (i == 1) {
            title.setText("Find Sender");
        }
        if (i == 2) {
            title.setText("Sender Mobile No.");
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    //Json request for FindRegister
    private void findSenderRequest() {
        pd = ProgressDialog.show(DrawerActivity.this, "", "Loading. Please wait...", true);
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
                                    Intent intent = new Intent(DrawerActivity.this, MoneyTransferActivity.class);
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
                                    Toast.makeText(DrawerActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
        final Dialog d = new Dialog(DrawerActivity.this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.sender_resister_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnOK =  d.findViewById(R.id.btn_resister_ok);
        final Button btnNO =  d.findViewById(R.id.btn_resister_no);
        final TextView tvMessage = (TextView) d.findViewById(R.id.tv_confirmation_dialog);
        final Button btnClosed =  d.findViewById(R.id.close_push_button);

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
                Intent intent = new Intent(DrawerActivity.this, SenderRegistrationActivity.class);
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
        pd = ProgressDialog.show(DrawerActivity.this, "", "Loading. Please wait...", true);
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

                                    Toast.makeText(DrawerActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DrawerActivity.this, SenderVerifyRegisterActivity.class);
                                    intent.putExtra("MobileNo", MobileNo);
                                    intent.putExtra("SenderName", SenderName);
                                    intent.putExtra("Address", "");
                                    intent.putExtra("DOB", "");
                                    startActivity(intent);
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(DrawerActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
   //============DMR=========================================
   //Json request for get Session
  /* private void getDMRSessionRequest() {
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
}