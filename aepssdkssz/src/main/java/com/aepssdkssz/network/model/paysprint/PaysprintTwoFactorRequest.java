package com.aepssdkssz.network.model.paysprint;


import com.google.gson.annotations.SerializedName;

public class PaysprintTwoFactorRequest {

    @SerializedName("AgentID")
    private String agentId;
    @SerializedName("Key")
    private String txnKey;

    @SerializedName("bankPipe")
    private String bankPipe;

    public String getBankPipe() {
        return bankPipe;
    }

    public void setBankPipe(String bankPipe) {
        this.bankPipe = bankPipe;
    }

    private PaysprintTwoFactorRequestData data;

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

    public PaysprintTwoFactorRequestData getData() {
        return data;
    }

    public void setData(PaysprintTwoFactorRequestData data) {
        this.data = data;
    }

}
