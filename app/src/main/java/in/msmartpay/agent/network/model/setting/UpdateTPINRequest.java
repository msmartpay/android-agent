package in.msmartpay.agent.network.model.setting;

import com.google.gson.annotations.SerializedName;

public class UpdateTPINRequest {

    @SerializedName("AgentID")
    String agentId;
    @SerializedName("Key")
    String txnKey;

    @SerializedName("transaction_pin")
    String tpin;
    String otp;

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getTxnKey() {
        return txnKey;
    }

    public void setTxnKey(String txnKey) {
        this.txnKey = txnKey;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
