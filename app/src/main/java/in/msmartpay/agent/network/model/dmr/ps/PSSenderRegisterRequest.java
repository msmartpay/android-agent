package in.msmartpay.agent.network.model.dmr.ps;

import com.google.gson.annotations.SerializedName;

public class PSSenderRegisterRequest {
    String Key;
    String AgentID;
    String SenderId;
    String firstname;
    String lastname;
    String address;
    String otp;
    String pincode;
    String state;
    @SerializedName("otp_ref_id")
    String otpRefId;

    public String getOtpRefId() {
        return otpRefId;
    }

    public void setOtpRefId(String otpRefId) {
        this.otpRefId = otpRefId;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}