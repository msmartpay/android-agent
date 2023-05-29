package in.msmartpay.agent.network.model.aeps;

import com.google.gson.annotations.SerializedName;

public class AepsAccessKeyRequest {
    @SerializedName("AgentID")
    String agent_id;
    @SerializedName("Key")
    String txn_key;

    @SerializedName("provider")
    String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }
}
