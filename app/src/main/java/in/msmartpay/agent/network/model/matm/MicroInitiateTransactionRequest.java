package in.msmartpay.agent.network.model.matm;

import com.google.gson.annotations.SerializedName;

public class MicroInitiateTransactionRequest {

    @SerializedName("AgentID")
    private String agentID;
    @SerializedName("Key")
    private String key;
    private MicroInitiateTransactionData data;

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MicroInitiateTransactionData getData() {
        return data;
    }

    public void setData(MicroInitiateTransactionData data) {
        this.data = data;
    }
}
