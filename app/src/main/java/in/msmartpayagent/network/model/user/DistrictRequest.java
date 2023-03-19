package in.msmartpayagent.network.model.user;

public class DistrictRequest {
    String agent_id;
    String txn_key;
    String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getAgentID() {
        return agent_id;
    }

    public void setAgentID(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }
}
