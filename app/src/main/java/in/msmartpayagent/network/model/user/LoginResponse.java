package in.msmartpayagent.network.model.user;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("response-message")
    private String responseMessage;
    @SerializedName("response-code")
    private String responseCode;
    @SerializedName("agent-id")
    private String agentId;
    @SerializedName("agent-initial")
    private String agentInitial;
    @SerializedName("agent-dist-id")
    private String agentDistId;
    private String txn_key;
    private String balance;
    private String agentName;
    private String emailId;
    @SerializedName("mobile-number")
    private String mobileNumber;
    private String walletStatus;
    private String support1;
    private String support2;
    @SerializedName("app-text")
    private String app_text;
    private String DMR;
    private String DMRUrl;
    private String supportEmail;
    private String dmrVendor;
    private String time;
    @SerializedName("aeps_status")
    private String aepsStatus;
    @SerializedName("aadharpay_status")
    private String aadharpayStatus;
    private String about;

    private String pan_number;

    private String pin;
    private String agency_name;
    private String address;

    private String kycStatus;

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    private String termsAndConditions;

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getAadharpayStatus() {
        return aadharpayStatus;
    }

    public void setAadharpayStatus(String aadharpayStatus) {
        this.aadharpayStatus = aadharpayStatus;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }




    public String getPan_number() {
        return pan_number;
    }

    public void setPan_number(String pan_number) {
        this.pan_number = pan_number;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAepsStatus() {
        return aepsStatus;
    }

    public void setAepsStatus(String aepsStatus) {
        this.aepsStatus = aepsStatus;
    }




    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentInitial() {
        return agentInitial;
    }

    public void setAgentInitial(String agentInitial) {
        this.agentInitial = agentInitial;
    }

    public String getAgentDistId() {
        return agentDistId;
    }

    public void setAgentDistId(String agentDistId) {
        this.agentDistId = agentDistId;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(String walletStatus) {
        this.walletStatus = walletStatus;
    }

    public String getSupport1() {
        return support1;
    }

    public void setSupport1(String support1) {
        this.support1 = support1;
    }

    public String getSupport2() {
        return support2;
    }

    public void setSupport2(String support2) {
        this.support2 = support2;
    }

    public String getApp_text() {
        return app_text;
    }

    public void setApp_text(String app_text) {
        this.app_text = app_text;
    }

    public String getDMR() {
        return DMR;
    }

    public void setDMR(String DMR) {
        this.DMR = DMR;
    }

    public String getDMRUrl() {
        return DMRUrl;
    }

    public void setDMRUrl(String DMRUrl) {
        this.DMRUrl = DMRUrl;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getDmrVendor() {
        return dmrVendor;
    }

    public void setDmrVendor(String dmrVendor) {
        this.dmrVendor = dmrVendor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
