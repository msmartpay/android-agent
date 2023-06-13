package in.msmartpay.agent.network.model.setting;

import com.google.gson.annotations.SerializedName;

public class UpdateTPINStatusRequest {

    @SerializedName("AgentID")
    String agentId;
    @SerializedName("Key")
    String txnKey;

    @SerializedName("tpin_status")
    String tpinStatus;
    String otp;

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

    public String getTpinStatus() {
        return tpinStatus;
    }

    public void setTpinStatus(String tpinStatus) {
        this.tpinStatus = tpinStatus;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
