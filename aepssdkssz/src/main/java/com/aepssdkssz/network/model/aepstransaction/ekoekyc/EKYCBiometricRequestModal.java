package com.aepssdkssz.network.model.aepstransaction.ekoekyc;

import com.google.gson.annotations.SerializedName;

public class EKYCBiometricRequestModal {

    @SerializedName("customer_id")
    private String agentRegisteredMobile;
    @SerializedName("user_code")
    private String ekoUserCode;
    @SerializedName("aadhar")
    private String agentAadhaarNumber;
    private String latlong;
    @SerializedName("otp")
    private String otp;
    @SerializedName("otp_ref_id")
    private String otpRefId;
    @SerializedName("reference_tid")
    private String referenceTid;
    @SerializedName("bank_code")
    private String bankCode;

    @SerializedName("piddata")
    private String piddata;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getPiddata() {
        return piddata;
    }

    public void setPiddata(String piddata) {
        this.piddata = piddata;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpRefId() {
        return otpRefId;
    }

    public void setOtpRefId(String otpRefId) {
        this.otpRefId = otpRefId;
    }

    public String getReferenceTid() {
        return referenceTid;
    }

    public void setReferenceTid(String referenceTid) {
        this.referenceTid = referenceTid;
    }

    public String getAgentRegisteredMobile() {
        return agentRegisteredMobile;
    }

    public void setAgentRegisteredMobile(String agentRegisteredMobile) {
        this.agentRegisteredMobile = agentRegisteredMobile;
    }

    public String getEkoUserCode() {
        return ekoUserCode;
    }

    public void setEkoUserCode(String ekoUserCode) {
        this.ekoUserCode = ekoUserCode;
    }

    public String getAgentAadhaarNumber() {
        return agentAadhaarNumber;
    }

    public void setAgentAadhaarNumber(String agentAadhaarNumber) {
        this.agentAadhaarNumber = agentAadhaarNumber;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }
}
