package com.aepssdkssz.network.model.aepstransaction.ekoekyc;

import com.google.gson.annotations.SerializedName;

public class EKYCRequestOTPModal {

    @SerializedName("customer_id")
    private String agentRegisteredMobile;

    @SerializedName("user_code")
    private String ekoUserCode;

    @SerializedName("aadhar")
    private String agentAadhaarNumber;
    private String latlong;

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
