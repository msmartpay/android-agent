package com.aepssdkssz.paysprint;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aepssdkssz.R;
import com.aepssdkssz.network.SSZAePSAPIError;
import com.aepssdkssz.network.SSZAePSRetrofitClient;
import com.aepssdkssz.network.model.BanksStatus;
import com.aepssdkssz.network.model.ValidateUserData;
import com.aepssdkssz.network.model.ValidateUserRequest;
import com.aepssdkssz.network.model.ValidateUserResponse;
import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.DialogProgressFragment;
import com.aepssdkssz.util.Utility;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PSAePSHomeActivity extends AppCompatActivity {

    private ImageView iv_comLogo;
    private TextView tv_com_name;
    private LinearLayout ll_screen, ll_error;
    private Button btn_onboard;
    private TextView tv_message;
    private ViewPager viewPager;
    private TabLayout tabs;
    private String initiator_id;
    private String environment;

    //private String primary_theme_code;
    //private String secondary_theme_code;
    private String token;
    private String merchant_id;

    private String morpho;
    private String startek;
    private String mantra;
    private String secugen;
    private String precision;
    private String tatvik;

    int requestCode;
    StringBuilder reportData;
    JSONObject jsonObjPidData;
    private JSONObject transactionInfo;
    String morphoDeviceSerno;

    private DialogProgressFragment pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_aeps_activity_home);
        ll_error = findViewById(R.id.ll_error);
        ll_screen = findViewById(R.id.ll_screen);
        btn_onboard = findViewById(R.id.btn_onboard);
        Button btn_back = findViewById(R.id.btn_back);
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        tv_message = findViewById(R.id.tv_message);
        iv_comLogo = findViewById(R.id.iv_comLogo);
        tv_com_name = findViewById(R.id.tv_com_name);

        this.morpho = "Morpho";
        this.startek = "Startek";
        this.mantra = "Mantra";
        this.secugen = "Secugen";
        this.precision = "PB510";
        this.tatvik = "TATVIK";

        Intent intent = getIntent();

        if (intent != null) {

            initiator_id = intent.getStringExtra(Constants.INITIATOR_ID);
            environment = intent.getStringExtra(Constants.ENVIRONMENT);
            merchant_id = intent.getStringExtra(Constants.MERCHANT_ID);
            token = intent.getStringExtra(Constants.TOKEN);


            Utility.saveData(getApplicationContext(), Constants.TRANSACTION_TYPE, intent.getStringExtra(Constants.TRANSACTION_TYPE));
            Utility.saveData(getApplicationContext(), Constants.AGENCY_NAME,intent.getStringExtra(Constants.AGENCY_NAME));
            Utility.saveData(getApplicationContext(), Constants.PARTNER_LOGO_URL,intent.getStringExtra(Constants.PARTNER_LOGO_URL));
            Utility.saveData(getApplicationContext(), Constants.PARTNER_NAME,intent.getStringExtra(Constants.PARTNER_NAME));
            Utility.saveData(getApplicationContext(), Constants.PARTNER_POWERED_BY,intent.getStringExtra(Constants.PARTNER_POWERED_BY));
            Utility.saveData(getApplicationContext(), Constants.PARTNER_CONTACT_US,intent.getStringExtra(Constants.PARTNER_CONTACT_US));

            Utility.saveData(getApplicationContext(), Constants.SOURCE_IP,intent.getStringExtra(Constants.SOURCE_IP));
            Utility.saveData(getApplicationContext(), Constants.LATITUDE,intent.getStringExtra(Constants.LATITUDE));
            Utility.saveData(getApplicationContext(), Constants.LONGITUDE,intent.getStringExtra(Constants.LONGITUDE));
            //setPages();
            checkValidateUserRequest();
        } else {
            Utility.toast(getApplicationContext(), "Invalid Merchant Information.");
            finish();
        }

        btn_back.setOnClickListener(view20 -> {
            finish();
        });
    }

    private void checkValidateUserRequest() {
        if (Utility.checkConnection(getApplicationContext())) {
            pd = DialogProgressFragment.newInstance("Loading. Please wait...", "Validating access...");
            DialogProgressFragment.showDialog(pd, getSupportFragmentManager());

            ValidateUserRequest request = new ValidateUserRequest();
            request.setToken(token);
            request.setMerchant_id(merchant_id);

            SSZAePSRetrofitClient.getClient(getApplicationContext())
                    .validatePSUser(request)
                    .enqueue(new Callback<ValidateUserResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<ValidateUserResponse> call, @NotNull Response<ValidateUserResponse> response) {
                            pd.dismiss();
                            try {
                                ValidateUserResponse res = response.body();
                                if (res != null) {
                                    if (res.getStatus() == 0 && res.getData() != null && res.getData().isAllow()) {
                                        ValidateUserData data = res.getData();
                                        transactionInfo = new JSONObject();
                                        //Company and Merchant Identifier
                                        //transactionInfo.put(Constants.INITIATOR_ID, data.getInitiator_id());
                                        transactionInfo.put(Constants.MERCHANT_ID, data.getMerchant_id());
                                        //transactionInfo.put(Constants.MERCHANT_NAME, data.getMerchant_name());
                                        //transactionInfo.put(Constants.MERCHANT_LOCATION, data.getMerchant_location());
                                        transactionInfo.put(Constants.AUTHORIZATION_KEY, data.getAuthorization_key());
                                        //transactionInfo.put(Constants.USER_CODE, data.getUser_code());

                                        BanksStatus banksStatus=data.getBank();
                                        Utility.saveData(getApplicationContext(), Constants.AEPS_BANK1, banksStatus.getBank1());
                                        Utility.saveData(getApplicationContext(), Constants.AEPS_BANK2, banksStatus.getBank2());
                                        Utility.saveData(getApplicationContext(), Constants.AEPS_BANK3, banksStatus.getBank3());

                                        Utility.saveData(getApplicationContext(), Constants.ENVIRONMENT, environment);
                                        Utility.saveData(getApplicationContext(), Constants.TOKEN, token);

                                        Utility.saveData(getApplicationContext(), Constants.DEVICE_TYPE, data.getDevice_type());
                                        Utility.saveData(getApplicationContext(), Constants.DEVICE_NUMBER, data.getDevice_number());
                                        Utility.saveData(getApplicationContext(), Constants.BANK_LIST_PREFERRED, Utility.getGson().toJson(res.getData().getPreferred_bank_list()));
                                        Utility.saveData(getApplicationContext(), Constants.BANK_LIST, Utility.getGson().toJson(res.getData().getBank_list()));
                                        Utility.saveData(getApplicationContext(), Constants.BIOMETRIC_PROVIDERS, Utility.getGson().toJson(res.getData().getBiometric_providers()));
                                        Utility.saveData(getApplicationContext(), Constants.INITIATOR_ID,data.getInitiator_id());
                                        Utility.saveData(getApplicationContext(), Constants.MERCHANT_ID,data.getMerchant_id());
                                        Utility.saveData(getApplicationContext(), Constants.MERCHANT_NAME,data.getMerchant_name());
                                        Utility.saveData(getApplicationContext(), Constants.MERCHANT_EMAIL,data.getMerchant_email());
                                        Utility.saveData(getApplicationContext(), Constants.MERCHANT_MOBILE,data.getMerchant_mobile());
                                        Utility.saveData(getApplicationContext(), Constants.MERCHANT_COMPANY,data.getMerchant_company());
                                        Utility.saveData(getApplicationContext(), Constants.MERCHANT_PAN,data.getMerchant_pan());
                                        Utility.saveData(getApplicationContext(), Constants.MERCHANT_LOCATION,data.getMerchant_location());
                                        Utility.saveData(getApplicationContext(), Constants.PARTNER_NAME,data.getPartner_name());

                                        Utility.saveData(getApplicationContext(), Constants.PS_TWO_FACTOR_REG, data.getPsTwofactorReg());
                                        Utility.saveData(getApplicationContext(), Constants.PS_TWO_FACTOR_AUTH, data.getPsTwofactorAuth());


                                        tv_com_name.setText(data.getPartner_name());
                                        //Picasso.get().load(Utility.getData(getApplicationContext(),Constants.PARTNER_LOGO_URL)).into(iv_comLogo);

                                        Utility.showView(ll_screen);
                                        Utility.hideView(ll_error);
                                        setPages();
                                    } else if (res.getStatus() == 2) {
                                        Utility.hideView(ll_screen);
                                        Utility.showView(ll_error);
                                        Utility.hideView(btn_onboard);
                                        tv_message.setText(res.getMessage());
                                    }  else {
                                        Utility.hideView(ll_screen);
                                        Utility.showView(ll_error);
                                        Utility.hideView(btn_onboard);
                                        tv_message.setText(res.getMessage());
                                    }
                                } else {
                                    Utility.hideView(ll_screen);
                                    Utility.showView(ll_error);
                                    Utility.hideView(btn_onboard);
                                    SSZAePSAPIError error = SSZAePSAPIError.parseError(response, getApplicationContext());
                                    tv_message.setText(error.message());
                                }
                            } catch (Exception e) {
                                Utility.hideView(ll_screen);
                                Utility.showView(ll_error);
                                Utility.hideView(btn_onboard);
                                tv_message.setText(e.getMessage());
                                Utility.loge("validate User", "" + e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ValidateUserResponse> call, @NotNull Throwable t) {
                            pd.dismiss();
                            Utility.hideView(ll_screen);
                            Utility.showView(ll_error);
                            Utility.hideView(btn_onboard);
                            tv_message.setText(t.getMessage());
                            Utility.loge("validate User", "" + t.getMessage());
                        }
                    });
        }
    }

    private void setPages() {
        PSAePSSectionsPagerAdapter sectionsPagerAdapter = new PSAePSSectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    public void updatePidJson(final JSONObject jsonObjPidData) {
        this.jsonObjPidData = jsonObjPidData;
    }

    public JSONObject getPieJsonData() {
        return jsonObjPidData;
    }

    public JSONObject getTransactionInfo() {
        return transactionInfo;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public String getMorphoDeviceSerno() {
        return this.morphoDeviceSerno;
    }

    public Intent checkBiometricProvider(final String device_type, final String biometricFormat) {
        try {
            if (device_type.trim().equalsIgnoreCase(this.morpho)) {
                if (this.searchPackageName(this.morpho)) {
                    final Intent intent = new Intent("in.gov.uidai.rdservice.fp.INFO");
                    intent.setPackage("com.scl.rdservice");
                    this.requestCode = 1100;
                    return intent;
                }
            } else if (device_type.trim().equalsIgnoreCase(this.tatvik)) {
                if (this.searchPackageName(this.tatvik)) {
                    final Intent intent = new Intent("in.gov.uidai.rdservice.fp.INFO");
                    intent.setPackage("com.tatvik.bio.tmf20");
                    this.requestCode = 1100;
                    return intent;
                }
            } else if (device_type.trim().equalsIgnoreCase(this.startek)) {
                if (this.searchPackageName(this.startek)) {
                    final Intent intent = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
                    intent.setPackage("com.acpl.registersdk");
                    final String pidOptXML = this.createPidOptXML(biometricFormat);
                    if (pidOptXML.equals("")) {
                        Utility.showMessageDialogue(getApplicationContext(), "EXCEPTION- In Creating PID Request Data!", "EXCEPTION");
                        return null;
                    }
                    intent.putExtra("PID_OPTIONS", pidOptXML);
                    this.requestCode = 1500;
                    return intent;
                }
            } else if (device_type.trim().equalsIgnoreCase(this.mantra)) {
                if (this.searchPackageName(this.mantra)) {
                    //Utility.toast(getApplicationContext(),"Device : "+device_type);
                    final Intent intent2 = new Intent();
                    intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                    intent2.setPackage("com.mantra.rdservice");
                    final String pidOptXML = this.createPidOptXML(biometricFormat);
                    intent2.putExtra("PID_OPTIONS", pidOptXML);
                    this.requestCode = 3500;
                    return intent2;
                }
            } else if (device_type.trim().equalsIgnoreCase(this.secugen)) {
                if (this.searchPackageName(this.secugen)) {
                    final Intent intent2 = new Intent();
                    intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                    intent2.setPackage("com.secugen.rdservice");
                    final String pidOptXML = this.createSecugenPidXML(biometricFormat);
                    intent2.putExtra("PID_OPTIONS", pidOptXML);
                    this.requestCode = 4500;
                    return intent2;
                }
            } else if (device_type.trim().equalsIgnoreCase(this.precision) && this.searchPackageName(this.precision)) {
                final Intent intent2 = new Intent();
                intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                intent2.setPackage("com.precision.pb510.rdservice");
                final String pidOptXML = this.createPrecisionPidOptXML(biometricFormat);
                intent2.putExtra("PID_OPTIONS", pidOptXML);
                this.requestCode = 5500;
                return intent2;
            }
        } catch (Exception e) {
            this.reportData.append("Excep message: ");
            this.reportData.append(e.getMessage());
            this.reportData.append(", Cause: ");
            this.reportData.append(e.getMessage());
            Utility.sendReportEmail(getApplicationContext(), "checkBiometricProvider()", this.reportData.toString());
            this.reportData.setLength(0);
            Utility.showMessageDialogue(getApplicationContext(), "EXCEPTION- " + e.getMessage(), "EXCEPTION");
        }
        return null;
    }

    private boolean searchPackageName(final String deviceName) {
        final PackageManager pm = this.getPackageManager();
        String string = "";
        String message = "";
        Intent intent = new Intent();
        if (deviceName.equalsIgnoreCase(this.morpho)) {
            string = "com.scl.rdservice";
            message = "Please install `Morpho SCL RDService` App.";
            intent.setAction("in.gov.uidai.rdservice.fp.INFO");
        } else if (deviceName.equalsIgnoreCase(this.startek)) {
            string = "com.acpl.registersdk";
            message = "Please install `ACPL FM220 Registered Device` Service.";
            intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
        } else if (deviceName.equalsIgnoreCase(this.mantra)) {
            string = "com.mantra.rdservice";
            message = "Please install `Mantra RDService` App.";
            intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
        } else if (deviceName.equalsIgnoreCase(this.secugen)) {
            string = "com.secugen.rdservice";
            message = "Please install `SecuGen RD Service` App.";
            intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
        } else if (deviceName.equalsIgnoreCase(this.tatvik)) {
            string = "com.tatvik.bio.tmf20";
            message = "Please install `Tatvik TMF20 RDService` App.";
            intent.setAction("in.gov.uidai.rdservice.fp.INFO");
        } else if (deviceName.equalsIgnoreCase(this.precision)) {
            //Utility.toast(getApplicationContext(), "Feature will available soon.");
            intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
        } else {
            string = "com.precision.pb510.rdservice";
            message = "Please install `PB510 RDService` App.";
            intent.setAction("iin.gov.uidai.rdservice.fp.CAPTURE");
        }
        if (message.isEmpty() ) {
            return false;
        }
        String packageName = string;

        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(intent, 0);
        //Utility.toast(getApplicationContext(), "resolveInfoList : " +packageName+" : "+resolveInfoList.size());
        if (resolveInfoList.isEmpty()) {
            new AlertDialog.Builder(getApplicationContext()).setCancelable(true).setTitle("Message").setMessage(message).setPositiveButton("OK", (dialog, which) -> {
                final Intent intentPlay = new Intent("android.intent.action.VIEW");
                intentPlay.setData(Uri.parse("market://details?id=" + packageName));
                startActivity(intentPlay);
                dialog.cancel();
            }).show();
            return false;
        }
        return true;
    }

    protected String createPidOptXML(final String biometricFormat) {
        try {
            //Utility.toast(getApplicationContext(),"createPidOptXML : "+biometricFormat);
            final String fTypeStr = "2";
            String formatStr;
            if (biometricFormat != null && biometricFormat.length() > 0) {
                formatStr = biometricFormat;
            } else {
                formatStr = "1";
            }
            final String timeOutStr = "20000";
            final String envStr = "P";
            final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            final Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            final Element rootElement = doc.createElement("PidOptions");
            doc.appendChild(rootElement);
            final Attr attrs = doc.createAttribute("ver");
            attrs.setValue("1.0");
            rootElement.setAttributeNode(attrs);
            final Element opts = doc.createElement("Opts");
            rootElement.appendChild(opts);
            Attr attr = doc.createAttribute("fCount");
            attr.setValue("1");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("fType");
            attr.setValue(fTypeStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("iCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("iType");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pType");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("format");
            attr.setValue(formatStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pidVer");
            attr.setValue("2.0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("timeout");
            attr.setValue(timeOutStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("otp");
            attr.setValue("");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("env");
            attr.setValue(envStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("wadh");
            attr.setValue("");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("posh");
            attr.setValue("UNKNOWN");
            opts.setAttributeNode(attr);
            final Element demo = doc.createElement("Demo");
            demo.setTextContent("");
            rootElement.appendChild(demo);
            final Element custotp = doc.createElement("CustOpts");
            rootElement.appendChild(custotp);
            final Element param = doc.createElement("Param");
            custotp.appendChild(param);
            attr = doc.createAttribute("name");
            attr.setValue("ValidationKey");
            param.setAttributeNode(attr);
            attr = doc.createAttribute("value");
            attr.setValue("ONLY USE FOR LOCKED DEVICES.");
            param.setAttributeNode(attr);
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("standalone", "yes");
            final DOMSource source = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String tmpOptXml = writer.getBuffer().toString().replaceAll("[\n\r]", "");
            tmpOptXml = tmpOptXml.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            //Utility.toast(getApplicationContext(),"Pid Options"+tmpOptXml);
            return tmpOptXml;
        } catch (Exception ex) {
            this.reportData.append("Excep message: ");
            this.reportData.append(ex.getMessage());
            this.reportData.append(", Cause: ");
            this.reportData.append(ex.getMessage());
            Utility.sendReportEmail(getApplicationContext(), "createPidOptXml()", this.reportData.toString());
            this.reportData.setLength(0);
            Utility.showMessageDialogue(getApplicationContext(), "EXCEPTION- " + ex.getMessage(), "EXCEPTION");
            return "";
        }
    }

    protected String createSecugenPidXML(final String biometricFormat) {
        try {
            final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            final Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            final Element rootElement = doc.createElement("Auth");
            doc.appendChild(rootElement);
            Attr attrs = doc.createAttribute("uid");
            rootElement.setAttributeNode(attrs);
            attrs = doc.createAttribute("rc");
            rootElement.setAttributeNode(attrs);
            attrs = doc.createAttribute("tid");
            rootElement.setAttributeNode(attrs);
            attrs = doc.createAttribute("ac");
            rootElement.setAttributeNode(attrs);
            attrs = doc.createAttribute("sa");
            rootElement.setAttributeNode(attrs);
            attrs = doc.createAttribute("ver");
            rootElement.setAttributeNode(attrs);
            attrs = doc.createAttribute("txn");
            rootElement.setAttributeNode(attrs);
            attrs = doc.createAttribute("lk");
            rootElement.setAttributeNode(attrs);
            final Element uses = doc.createElement("Uses");
            rootElement.appendChild(uses);
            attrs = doc.createAttribute("pi");
            uses.setAttributeNode(attrs);
            attrs = doc.createAttribute("pa");
            uses.setAttributeNode(attrs);
            attrs = doc.createAttribute("pfa");
            uses.setAttributeNode(attrs);
            attrs = doc.createAttribute("bio");
            uses.setAttributeNode(attrs);
            attrs = doc.createAttribute("pt");
            uses.setAttributeNode(attrs);
            attrs = doc.createAttribute("pin");
            uses.setAttributeNode(attrs);
            attrs = doc.createAttribute("otp");
            uses.setAttributeNode(attrs);
            final Element meta = doc.createElement("Meta");
            rootElement.appendChild(meta);
            attrs = doc.createAttribute("udc");
            meta.setAttributeNode(attrs);
            attrs = doc.createAttribute("rdsId");
            meta.setAttributeNode(attrs);
            attrs = doc.createAttribute("rdsVer");
            meta.setAttributeNode(attrs);
            attrs = doc.createAttribute("dpId");
            meta.setAttributeNode(attrs);
            attrs = doc.createAttribute("dc");
            meta.setAttributeNode(attrs);
            attrs = doc.createAttribute("mi");
            meta.setAttributeNode(attrs);
            attrs = doc.createAttribute("mc");
            meta.setAttributeNode(attrs);
            final Element skey = doc.createElement("Skey");
            rootElement.appendChild(skey);
            attrs = doc.createAttribute("ci");
            skey.setAttributeNode(attrs);
            final Element hmac = doc.createElement("Hmac");
            rootElement.appendChild(hmac);
            final Element data = doc.createElement("Data");
            rootElement.appendChild(data);
            String formatStr;
            if (biometricFormat != null && biometricFormat.length() > 0) {
                formatStr = biometricFormat;
            } else {
                formatStr = "1";
            }
            attrs = doc.createAttribute("type");
            if (formatStr.equals("0")) {
                attrs.setValue("X");
            } else if (formatStr.equals("1")) {
                attrs.setValue("P");
            }
            data.setAttributeNode(attrs);
            final Element sign = doc.createElement("Signature");
            rootElement.appendChild(sign);
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("standalone", "yes");
            final DOMSource source = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String tmpOptXml = writer.getBuffer().toString().replaceAll("[\n\r]", "");
            tmpOptXml = tmpOptXml.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            //Utility.loge("Auth", tmpOptXml);
            return tmpOptXml;
        } catch (Exception ex) {
            this.reportData.append("Excep message: ");
            this.reportData.append(ex.getMessage());
            this.reportData.append(", Cause: ");
            this.reportData.append(ex.getMessage());
            Utility.sendReportEmail(getApplicationContext(), "createSecugenPidXml()", this.reportData.toString());
            this.reportData.setLength(0);
            Utility.showMessageDialogue(getApplicationContext(), "EXCEPTION- " + ex.getMessage(), "EXCEPTION");
            return "";
        }
    }

    protected String createPrecisionPidOptXML(final String biometricFormat) {
        try {
            final String fTypeStr = "2";
            String formatStr;
            if (biometricFormat != null && biometricFormat.length() > 0) {
                formatStr = biometricFormat;
            } else {
                formatStr = "1";
            }
            final String timeOutStr = "20000";
            final String envStr = "P";
            final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            final Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            final Element rootElement = doc.createElement("PidOptions");
            doc.appendChild(rootElement);
            final Attr attrs = doc.createAttribute("ver");
            attrs.setValue("1.0");
            rootElement.setAttributeNode(attrs);
            final Element opts = doc.createElement("Opts");
            rootElement.appendChild(opts);
            Attr attr = doc.createAttribute("fCount");
            attr.setValue("1");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("fType");
            attr.setValue(fTypeStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("iCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("iType");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pType");
            attr.setValue("");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("format");
            attr.setValue(formatStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pidVer");
            attr.setValue("2.0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("timeout");
            attr.setValue(timeOutStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("otp");
            attr.setValue("");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("env");
            attr.setValue(envStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("wadh");
            attr.setValue("");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("posh");
            attr.setValue("UNKNOWN");
            opts.setAttributeNode(attr);
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            final DOMSource source = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String tmpOptXml = writer.getBuffer().toString().replaceAll("[\n\r]", "");
            tmpOptXml = tmpOptXml.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            //Utility.loge("Precision Pid Options", tmpOptXml);
            return tmpOptXml;
        } catch (Exception ex) {
            this.reportData.append("Excep message: ");
            this.reportData.append(ex.getMessage());
            this.reportData.append(", Cause: ");
            this.reportData.append(ex.getMessage());
            Utility.sendReportEmail(getApplicationContext(), "createPrecitionOptXml()", this.reportData.toString());
            this.reportData.setLength(0);
            Utility.showMessageDialogue(getApplicationContext(), "EXCEPTION- " + ex.getMessage(), "EXCEPTION");
            return "";
        }
    }

    public Intent verifyActivityResult(final int reqCode, final Intent data, final String biometricFormat) {
        Bundle b = null;
        if (data != null) {
            b = data.getExtras();
        } else {
            Utility.toast(getApplicationContext(), "Finger capture failed! Please retry.");
        }
        if (b != null && reqCode == 1100) {
            try {
                final String deviceInfo = data.getStringExtra("DEVICE_INFO");
                final String dnc = data.getStringExtra("DNC");
                final String dnr = data.getStringExtra("DNR");
                if (!TextUtils.isEmpty(dnc)) {
                    //Utility.toast(getApplicationContext(), dnc);
                    return null;
                }
                if (!TextUtils.isEmpty(dnr)) {
                    //Utility.toast(getApplicationContext(), dnr);
                    return null;
                }
                final JSONObject jsonObjData = XML.toJSONObject(deviceInfo);
                final JSONObject jsonDeviceInfo = jsonObjData.getJSONObject("DeviceInfo");
                if (jsonDeviceInfo.getString("dpId").toUpperCase().contains(this.morpho.toUpperCase())) {
                    if (jsonDeviceInfo.has("additional_info")) {
                        final JSONObject jsonAdditInfo = jsonDeviceInfo.getJSONObject("additional_info");
                        if (jsonAdditInfo.get("Param") instanceof JSONObject) {
                            final JSONObject jsonObject = jsonAdditInfo.getJSONObject("Param");
                            final String name = jsonObject.getString("name");
                            if (name.equalsIgnoreCase("serial_number")) {
                                this.morphoDeviceSerno = jsonObject.getString("value");
                                final Intent intent = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
                                intent.setPackage("com.scl.rdservice");
                                final String pidOptXML = this.createPidOptXML(biometricFormat);
                                intent.putExtra("PID_OPTIONS", pidOptXML);
                                this.requestCode = 1200;
                                return intent;
                            }
                        } else {
                            final JSONArray jsonArray = jsonAdditInfo.getJSONArray("Param");
                            if(jsonArray!=null) {
                                for (int i = 0; i < jsonArray.length(); ++i) {
                                    final JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    final String name2 = jsonObject2.getString("name");
                                    if (name2.equalsIgnoreCase("serial_number")) {
                                        this.morphoDeviceSerno = jsonObject2.getString("value");
                                        final Intent intent2 = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
                                        intent2.setPackage("com.scl.rdservice");
                                        final String pidOptXML2 = this.createPidOptXML(biometricFormat);
                                        intent2.putExtra("PID_OPTIONS", pidOptXML2);
                                        this.requestCode = 1200;
                                        return intent2;
                                    }
                                }
                            }
                        }
                    }
                } else if (jsonDeviceInfo.getString("dpId").toUpperCase().contains(this.tatvik.toUpperCase()) && jsonDeviceInfo.has("additional_info")) {
                    final JSONObject jsonAdditInfo = jsonDeviceInfo.getJSONObject("additional_info");
                    final JSONObject jsonObject = jsonAdditInfo.getJSONObject("Param");
                    final String name = jsonObject.getString("name");
                    if (name.equalsIgnoreCase("srno")) {
                        this.morphoDeviceSerno = jsonObject.getString("value");
                        final Intent intent = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
                        intent.setPackage("com.tatvik.bio.tmf20");
                        final String pidOptXML = this.createPidOptXML(biometricFormat);
                        intent.putExtra("PID_OPTIONS", pidOptXML);
                        this.requestCode = 1300;
                        return intent;
                    }
                }
            } catch (JSONException e) {

                this.reportData.append("Excep message: ");
                this.reportData.append(e.getMessage());
                this.reportData.append(", Cause: ");
                this.reportData.append(e.getMessage());
                Utility.sendReportEmail(this, "verifyActivityResult()", this.reportData.toString());
                this.reportData.setLength(0);
                e.printStackTrace();
            }
        }
        return null;
    }
}
