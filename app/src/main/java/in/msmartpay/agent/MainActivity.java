package in.msmartpay.agent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.multidex.BuildConfig;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.internal.LinkedTreeMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import in.msmartpay.agent.aeps.AEPSSDKActivity;
import in.msmartpay.agent.aeps.EkoAEPSGatewayActivity;
import in.msmartpay.agent.aeps.SettlementDetailsActivity;
import in.msmartpay.agent.aeps.onboard.UserNumberDialog;
import in.msmartpay.agent.claimrefund.ClaimHistoryActivity;
import in.msmartpay.agent.collectBanks.CollectBankActivity;
import in.msmartpay.agent.dmr.onboard.FindSenderDialog;
import in.msmartpay.agent.dmrPaySprint.onboard.PSFindSenderDialog;
import in.msmartpay.agent.fingpaymatm.FingpayMATMActivity;
import in.msmartpay.agent.helpAndSupport.AboutUsWebViewActivity;
import in.msmartpay.agent.helpAndSupport.HelpSupportActivity;
import in.msmartpay.agent.helpAndSupport.WebViewActivity;
import in.msmartpay.agent.kyc.UploadDocumentActivity;
import in.msmartpay.agent.location.GPSTrackerPresenter;
import in.msmartpay.agent.myWallet.BalanceRequestActivity;
import in.msmartpay.agent.myWallet.ComplaintActivity;
import in.msmartpay.agent.myWallet.QuickPayActivity;
import in.msmartpay.agent.myWallet.TransactionSearchActivity;
import in.msmartpay.agent.myWallet.WalletHistoryActivity;
import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.MainRequest2;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.wallet.BalanceRequest;
import in.msmartpay.agent.network.model.wallet.BalanceResponse;
import in.msmartpay.agent.rechargeBillPay.CreditCardActivity;
import in.msmartpay.agent.rechargeBillPay.LICPremiumActivity;
import in.msmartpay.agent.rechargeBillPay.SubscriptionRechargeActivity;
import in.msmartpay.agent.rechargeBillPay.DthRechargeActivity;
import in.msmartpay.agent.rechargeBillPay.ElectricityPayActivity;
import in.msmartpay.agent.rechargeBillPay.FastTagActivity;
import in.msmartpay.agent.rechargeBillPay.GasPayActivity;
import in.msmartpay.agent.rechargeBillPay.InsurancePayActivity;
import in.msmartpay.agent.rechargeBillPay.LandlinePayActivity;
import in.msmartpay.agent.rechargeBillPay.PaytmActivity;
import in.msmartpay.agent.rechargeBillPay.PostpaidMobileActivity;
import in.msmartpay.agent.rechargeBillPay.PrepaidMobileActivity;
import in.msmartpay.agent.rechargeBillPay.WaterPayActivity;
import in.msmartpay.agent.user.MyProfile;
import in.msmartpay.agent.user.ResetPasswordActivity;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;
import in.msmartpay.agent.utility.appupdate.UpdateManager;
import in.msmartpay.agent.utility.appupdate.UpdateManagerConstant;
import in.msmartpay.agent.utility.pagger.AutoScrollViewPager;
import in.msmartpay.agent.utility.pagger.SliderPagerAdapter;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends BaseActivity{

    private String agentID, txn_key = "",message="", debit_balance,credit_balance,alertMessage="";
    private Context context;
    private ProgressDialogFragment pd;
    private int kycStatus=0;
    private String dmrVendor;
    private String agent_id_full, emailId, agentName;

    private TextView balanceview,tv_main_wallet,tv_cash_in_wallet;
    private ImageView img, iv_activate;
    private LinearLayout id_fastag,my_profile, id_money_transfer, id_money_transfer2,id_fino_cms,id_claim_refund;

    private FloatingActionButton floatingButtonCall;
    private RelativeLayout drawerpane, rl_activate;
    private TextView draweragentid;
    private TextView draweragentemail, tv_version_code,tv_agent_name;
    private DrawerLayout mDrawer;
    private AutoScrollViewPager mDemoSlider;

    String aepsStatus,aepsStatusSszpl,aadharpayStatus;
    // Declare the UpdateManager
    UpdateManager mUpdateManager;
    private GPSTrackerPresenter gpsTrackerPresenter=null;
    private boolean isLocationEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        context = MainActivity.this;

        aepsStatus = Util.LoadPrefData(getApplicationContext(), Keys.AEPS_STATUS);
        aadharpayStatus = Util.LoadPrefData(getApplicationContext(), Keys.AADHARPAY_STATUS);
        aepsStatusSszpl = Util.LoadPrefData(getApplicationContext(), Keys.AEPS_STATUS_SSZPL);
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        debit_balance = Util.LoadPrefData(getApplicationContext(), Keys.BALANCE);
        credit_balance= Util.LoadPrefData(getApplicationContext(), Keys.BALANCE_CASH);
        dmrVendor = Util.LoadPrefData(getApplicationContext(), Keys.DMR_VENDOR);

        agent_id_full = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_FULL);
        emailId = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_EMAIL);
        agentName = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_NAME);
        alertMessage= Util.LoadPrefData(getApplicationContext(),Keys.TICKER_MESSAGE);

        homeInitialize();
        drawerInitialize();
        initAutoUpdateApp();
        initializeSlider();

        gpsTrackerPresenter = new GPSTrackerPresenter(this,new GPSTrackerPresenter.LocationListener(){

            @Override
            public void onLocationFound(Location location) {
                if (!isLocationEnable) {
                    isLocationEnable = true;
                }
            }

            @Override
            public void locationError(String msg) {

            }
        },GPSTrackerPresenter.RUN_TIME_PERMISSION_CODE);

    }

    private void initializeSlider() {
        ArrayList<String> list = new ArrayList<>();
        String random=RandomStringUtils.randomNumeric(15);
        list.add(AppMethods.DOMAIN+"documents/banners/10001/banner5.jpg?"+ random);
        list.add(AppMethods.DOMAIN+"documents/banners/10001/banner6.jpg?"+ random);
        list.add(AppMethods.DOMAIN+"documents/banners/10001/banner7.jpg?"+ random);
        list.add(AppMethods.DOMAIN+"documents/banners/10001/banner8.jpg?"+ random);
        mDemoSlider.setAdapter( new SliderPagerAdapter(this,list));
        mDemoSlider.setCycle(true);
        mDemoSlider.startAutoScroll();
        //mDemoSlider.setPageMargin(5);
    }

    private void homeInitialize() {

        id_claim_refund=findViewById(R.id.id_claim_refund);
        img = findViewById(R.id.id_drawer_icon);
        id_fastag=  findViewById(R.id.id_fastag);
        floatingButtonCall =  findViewById(R.id.fab);
        id_money_transfer = findViewById(R.id.id_dmr1);
        id_money_transfer2 = findViewById(R.id.id_dmr1);
        tv_main_wallet= findViewById(R.id.tv_main_wallet);
        tv_cash_in_wallet= findViewById(R.id.tv_cash_in_wallet);
        mDemoSlider =  findViewById(R.id.slider);
        TextView marqueText =findViewById(R.id.marqueText);
        marqueText.setSelected(true);
        marqueText.setText(alertMessage);
        tv_main_wallet.setText("\u20B9 " + debit_balance);
        tv_cash_in_wallet.setText("\u20B9 " + credit_balance);

        if (dmrVendor.equalsIgnoreCase("DMR1")) {
            id_money_transfer2.setVisibility(View.GONE);
            id_money_transfer.setVisibility(View.VISIBLE);
        } else if (dmrVendor.equalsIgnoreCase("DMR2")) {
            id_money_transfer.setVisibility(View.GONE);
            id_money_transfer2.setVisibility(View.VISIBLE);
        } else {
            id_money_transfer.setVisibility(View.GONE);
            id_money_transfer2.setVisibility(View.GONE);
        }

        img.setOnClickListener(v -> mDrawer.openDrawer(drawerpane));

//        my_profile.setOnClickListener(view -> {
//            Intent intent = new Intent(context, MyProfile.class);
//            startActivity(intent);
//        });

        floatingButtonCall.setOnClickListener(view -> {
            Intent intent = new Intent(context, HelpSupportActivity.class);
            startActivity(intent);
        });
    }

    private void drawerInitialize() {
        drawerpane = (RelativeLayout) findViewById(R.id.left_side);
        draweragentid = (TextView) findViewById(R.id.drawer_agent_id);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        draweragentemail = (TextView) findViewById(R.id.drawer_email_address);
        tv_agent_name = (TextView) findViewById(R.id.drawer_agent_name);
        iv_activate = findViewById(R.id.iv_activate);
        rl_activate = findViewById(R.id.rl_activate);

        if (agentName == null && agent_id_full == null) {

        } else {
            draweragentid.setText(agent_id_full + "");
            draweragentemail.setText(emailId + "");
            tv_agent_name.setText(agentName);
            tv_version_code.setText("Version " + BuildConfig .VERSION_NAME);
        }

    }

    public void start(View v) {
        if (isConnectionAvailable()) {
            if (v.getId() == R.id.rl_activate) {
                mDrawer.closeDrawers();
                UserNumberDialog.showDialog(getSupportFragmentManager(), Util.LoadPrefData(getApplicationContext(), Keys.AGENT_MOB));
            } else if (v.getId() == R.id.ll_wallet_bal_req) {
                startActivity(new Intent(context, BalanceRequestActivity.class));
                mDrawer.closeDrawers();
            } else if (v.getId() == R.id.ll_wallet_trans_search) {
                startActivity(new Intent(context, TransactionSearchActivity.class));
                mDrawer.closeDrawers();
            } else if (v.getId() == R.id.ll_wallet_history) {

                Intent in=new Intent(context, WalletHistoryActivity.class);
                in.putExtra("service","all");
                startActivity(in);
                mDrawer.closeDrawers();
            } /*else if (v.getId() == R.id.ll_wallet_earning) {
                Intent intent = new Intent(context, MyEarningActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            } */ else if (v.getId() == R.id.mprofile) {
                Intent intent = new Intent(context, MyProfile.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            } else if (v.getId() == R.id.complaints) {
                Intent intent = new Intent(context, ComplaintActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            } else if (v.getId() == R.id.cpassword) {
                Intent intent = new Intent(context, ResetPasswordActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            } else if (v.getId() == R.id.about_us) {
                Intent in = new Intent(context, AboutUsWebViewActivity.class);
                in.putExtra("url", Util.LoadPrefData(getApplicationContext(), Keys.ABOUT_US_URL));
                startActivity(in);
                mDrawer.closeDrawers();
            } else if (v.getId() == R.id.terms_conditions) {
                Intent in = new Intent(context, AboutUsWebViewActivity.class);
                in.putExtra("url", Util.LoadPrefData(getApplicationContext(), Keys.TERMS_AND_CONDITIONS_URL));
                startActivity(in);
                mDrawer.closeDrawers();
            } else if (v.getId() == R.id.help_support) {
                Intent intent = new Intent(context, HelpSupportActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            } else if (v.getId() == R.id.logout) {
                Util.clearMyPref(getApplicationContext());
                //createSourceFile(getApplicationContext(), "", Keys.MY_FILE);
                L.toastS(getApplicationContext(), "Thank you for visiting here !!!");
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }else if (v.getId() == R.id.ll_kyc_upload){
                Intent intent = new Intent(context, UploadDocumentActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
            }
        } else {
            L.toastS(getApplicationContext(), "No Internet Connection!");
        }
    }

    public void click(View view) {
        if (isConnectionAvailable()) {
            //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            if (view.getId() == R.id.id_fastag) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, FastTagActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_mobile_prepaid) {
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
                Intent in = new Intent(context, SubscriptionRechargeActivity.class);
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
            }else if (view.getId() == R.id.id_micro_atm) {
                view.getResources().getColor(R.color.active_tab);
                Intent intent = new Intent(this, FingpayMATMActivity.class);
                startActivity(intent);
            }  /* else if (view.getId() == R.id.id_Aeps) {
                view.getResources().getColor(R.color.active_tab);
                if ("1".equalsIgnoreCase(aepsStatus)) {
                    Intent intent = new Intent(this, AEPSActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Activate AePS service", Toast.LENGTH_SHORT).show();
                    UserNumberDialog.showDialog(getSupportFragmentManager(), Util.LoadPrefData(getApplicationContext(), Keys.AGENT_MOB));
                }

            }*/else if (view.getId() == R.id.id_eko_aeps_api) {
                view.getResources().getColor(R.color.active_tab);
                Intent intent = new Intent(this, AEPSSDKActivity.class);
                intent.putExtra("transaction_type",Keys.EKO_API);

                startActivity(intent);
            }else if (view.getId() == R.id.id_paysprint_aeps) {
                view.getResources().getColor(R.color.active_tab);
                Intent intent = new Intent(this, AEPSSDKActivity.class);
                intent.putExtra("transaction_type",Keys.PAY_SPRINT);

                startActivity(intent);
            }else if (view.getId() == R.id.id_eko_aeps_gateway) {
                view.getResources().getColor(R.color.active_tab);
                Intent intent = new Intent(this, EkoAEPSGatewayActivity.class);
                intent.putExtra("transaction_type","Cash Withdrawal");
                startActivity(intent);
            }else if (view.getId() == R.id.id_aadhar_pay) {
                view.getResources().getColor(R.color.active_tab);
                Intent intent = new Intent(this, AEPSSDKActivity.class);
                intent.putExtra("transaction_type","Aadhaar Pay");
                startActivity(intent);
            }else if (view.getId() == R.id.id_dmr1 ) {
                view.getResources().getColor(R.color.active_tab);
                FindSenderDialog.showDialog(getSupportFragmentManager());
            }
            else if (view.getId() == R.id.id_dmr2 ) {
                view.getResources().getColor(R.color.active_tab);
                Util.SavePrefData(getApplicationContext(), Keys.DYNAMIC_DMR_VENDOR, AppMethods.BASE_URL + AppMethods.DMR_PAYSPRINT);
                PSFindSenderDialog.showDialog(getSupportFragmentManager());
            }
            else if (view.getId() == R.id.id_account_opening) {
                generateAccountOpeningURL();
            }
            else if (view.getId() == R.id.id_fino_cms) {
                generateCMSURL();
            }
            else if (view.getId() == R.id.id_claim_refund) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, ClaimHistoryActivity.class);
                in.putExtra("service","refund_pending");
                startActivity(in);
            }
            else if (view.getId() == R.id.id_wallet_transfer) {
                view.getResources().getColor(R.color.active_tab);
                Intent intent = new Intent(this, QuickPayActivity.class);
                startActivity(intent);
            }
            else if (view.getId() == R.id.id_fund_settlement) {
                view.getResources().getColor(R.color.active_tab);
                Intent intent = new Intent(this, SettlementDetailsActivity.class);
                startActivity(intent);
            }
            else if (view.getId() == R.id.id_wallet_history) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, WalletHistoryActivity.class);
                in.putExtra("service","all");
                startActivity(in);
            } else if (view.getId() == R.id.id_balance_request) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, BalanceRequestActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_bank_details) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, CollectBankActivity.class);
                startActivity(in);
            } /*else if (view.getId() == R.id.id_commission) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, CommissionActivity.class);
                startActivity(in);
            } */else if (view.getId() == R.id.id_credit) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, CreditCardActivity.class);
                startActivity(in);
            } else if (view.getId() == R.id.id_paytm) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, PaytmActivity.class);
                startActivity(in);
            }else if (view.getId() == R.id.id_lic) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, LICPremiumActivity.class);
                startActivity(in);
            }/*else if (view.getId() == R.id.id_audit_request) {
                view.getResources().getColor(R.color.active_tab);
                Intent in = new Intent(context, AuditRequestActivity.class);
                startActivity(in);
            }*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        aepsStatus = Util.LoadPrefData(getApplicationContext(), Keys.AEPS_STATUS);
        if (kycStatus==1) {
            rl_activate.setClickable(false);
            iv_activate.setImageResource(R.drawable.tick_ok);
        } else {
            rl_activate.setClickable(true);
            iv_activate.setImageResource(R.drawable.warning_message_red);
        }
        getBalance();
    }
    private void generateAccountOpeningURL() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Balance...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            MainRequest2 request = new MainRequest2();
            request.setAgentID(agentID);
            request.setKey(txn_key);

            LinkedTreeMap data=new LinkedTreeMap();
            data.put("merchantcode",agentID);
            data.put("type",1);

            request.setData(data);

            RetrofitClient.getClient(getApplicationContext())
                    .generateAccountOpeningUrl(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();

                        LinkedTreeMap map = (LinkedTreeMap) res.getData();
                        if (res.getStatus() != null && res.getStatus().equals("0")) {
                            boolean status=(boolean)map.get("status");
                            message=res.getMessage();
                            if(status){
                                String response_code=map.get("response_code")+"";
                                //if("1".equalsIgnoreCase(response_code)){
                                Intent in = new Intent(context, WebViewActivity.class);
                                in.putExtra("url", map.get("data")+"");
                                in.putExtra("title", "Axis Bank Account");
                                startActivity(in);
                                L.toastS(getApplicationContext(), message);
                                //}
                            }


                        } else {
                            if ("Invalid request!".equalsIgnoreCase(res.getResponseMessage())){
                                startLoginActivity();
                            }

                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    } else {

                        L.toastS(getApplicationContext(), "Server Error");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }


    private void generateCMSURL() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Balance...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            MainRequest2 request = new MainRequest2();
            request.setAgentID(agentID);
            request.setKey(txn_key);

            RetrofitClient.getClient(getApplicationContext())
                    .generateFinoCMCUrl(request).enqueue(new Callback<MainResponse2>() {
                @Override
                public void onResponse(@NotNull Call<MainResponse2> call, @NotNull retrofit2.Response<MainResponse2> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        MainResponse2 res = response.body();

                        LinkedTreeMap map = (LinkedTreeMap) res.getData();
                        if (res.getStatus() != null && res.getStatus().equals("1")) {
                            boolean status=(boolean)map.get("status");
                            message=map.get("message")+"";
                            if(status){
                                String response_code=map.get("response_code")+"";
                                //if("1".equalsIgnoreCase(response_code)){
                                Intent in = new Intent(context, WebViewActivity.class);
                                in.putExtra("url", map.get("redirecturl")+"");
                                in.putExtra("title", "CMS");
                                startActivity(in);
                                L.toastS(getApplicationContext(), message);
                                //}
                            }


                        } else {
                            if ("Invalid request!".equalsIgnoreCase(res.getMessage())){
                                startLoginActivity();
                            }

                            L.toastS(getApplicationContext(), res.getMessage());
                        }
                    } else {

                        L.toastS(getApplicationContext(), "Server Error");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MainResponse2> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void getBalance() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Fetching Balance...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            BalanceRequest request = new BalanceRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);

            RetrofitClient.getClient(getApplicationContext())
                    .getWalletBalance(request).enqueue(new Callback<BalanceResponse>() {
                @Override
                public void onResponse(@NotNull Call<BalanceResponse> call, @NotNull retrofit2.Response<BalanceResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        BalanceResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            Util.SavePrefData(getApplicationContext(), Keys.BALANCE, "" + res.getDebit());
                            Util.SavePrefData(getApplicationContext(), Keys.BALANCE_CASH, "" + res.getCredit());
                            Util.SavePrefData(getApplicationContext(), Keys.KYC_STATUS, "" + res.getKycStatus());

                            kycStatus=res.getKycStatus();

                            tv_main_wallet.setText("\u20B9 " + res.getDebit());
                            tv_cash_in_wallet.setText("\u20B9 " + res.getCredit());

                            if (res.getKycStatus() == 0 || res.getKycStatus() == 2 || res.getKycStatus() == 3) {

                                final Dialog dialog_status = new Dialog(MainActivity.this);
                                dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog_status.setContentView(R.layout.alert);
                                dialog_status.setCancelable(false);
                                ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
                                statusImage.setImageResource(R.drawable.about);
                                statusImage.setColorFilter(ContextCompat.getColor(statusImage.getContext(), R.color.colorPrimary));
                                TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);

                                final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
                                String txt="";
                                if(res.getKycStatus() == 0) {
                                    txt="Upload KYC Documents";
                                    text.setText(txt);
                                }
                                if(res.getKycStatus() == 3) {
                                    txt="KYC Rejected. Re-Upload KYC Documents";
                                    text.setText(txt);
                                }
                                if(res.getKycStatus() == 2){
                                    txt="Your KYC Verification is pending. Please contact to support.";
                                    text.setText(txt);
                                }
                                final String msg=txt;
                                trans_status.setOnClickListener(v -> {
                                    dialog_status.dismiss();
                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), UploadDocumentActivity.class);
                                    intent.putExtra("message",msg);
                                    startActivity(intent);
                                    finish();
                                });

                                /*if(res.getKycStatus() == 2){
                                    text.setText("Your KYC Verification is pending. Please contact to support.");
                                    trans_status.setText("Close");
                                    trans_status.setOnClickListener(v -> {
                                        dialog_status.dismiss();
                                        Intent intent = new Intent();
                                        intent.setClass(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                                }*/

                                dialog_status.show();
                            }
                        } else {
                            if ("Invalid request!".equalsIgnoreCase(res.getResponseMessage())){
                                startLoginActivity();
                            }

                            L.toastS(getApplicationContext(), res.getResponseMessage());
                        }
                    } else {

                        L.toastS(getApplicationContext(), "Server Error");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BalanceResponse> call, @NotNull Throwable t) {
                    L.toastS(getApplicationContext(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

    private void initAutoUpdateApp() {
        // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = UpdateManager.Builder(this);

        // Callback from UpdateInfoListener
        // You can get the available version code of the apk in Google Play
        // Number of days passed since the user was notified of an update through the Google Play
        mUpdateManager.addUpdateInfoListener(new UpdateManager.UpdateInfoListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                L.m2("App Version", String.valueOf(code));
                if (BuildConfig.VERSION_CODE != code) {
                    callImmediateUpdate();
                }

            }

            @Override
            public void onReceiveStalenessDays(final int days) {
                L.m2("Staleness Days", String.valueOf(days));
            }
        });

        // Callback from Flexible Update Progress
        // This is only available for Flexible mode
        // Find more from https://developer.android.com/guide/playcore/in-app-updates#monitor_flexible
        mUpdateManager.addFlexibleUpdateDownloadListener((bytesDownloaded, totalBytes) -> L.m2("App Download", "Downloading: " + bytesDownloaded + " / " + totalBytes));

    }


    public void callFlexibleUpdate() {
        // Start a Flexible Update
        mUpdateManager.mode(UpdateManagerConstant.FLEXIBLE).start();

    }

    public void callImmediateUpdate() {
        // Start a Immediate Update
        mUpdateManager.mode(UpdateManagerConstant.IMMEDIATE).start();
    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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