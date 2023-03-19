package in.msmartpayagent.network.model;

public class MainRequest {
    String agent_id;
    String txn_key;

    public String getAgentID() {
        return agent_id;
    }

    public void setAgentID(String agentID) {
        this.agent_id = agentID;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }
}
