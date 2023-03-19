package in.msmartpayagent.network.model.matm;

import com.google.gson.annotations.SerializedName;

public class MicroPostTransactionRequest {

    @SerializedName("AgentID")
    private String agentID;
    @SerializedName("Key")
    private String key;
    private MicroPostTransactionData data;

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

    public MicroPostTransactionData getData() {
        return data;
    }

    public void setData(MicroPostTransactionData data) {
        this.data = data;
    }
}
