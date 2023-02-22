package com.aepssdkssz.paysprint.model;


import com.google.gson.annotations.SerializedName;

public class PaysprintAepsRequest {

    @SerializedName("AgentID")
    String agentId;
    @SerializedName("Key")
    String txnKey;

    private PaysprintAepsRequestData data;

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

    public PaysprintAepsRequestData getData() {
        return data;
    }

    public void setData(PaysprintAepsRequestData data) {
        this.data = data;
    }

}
