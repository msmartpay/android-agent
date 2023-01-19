package in.msmartpay.agent.network.model.commission;

public class CommissionRequest {
    String agent_id;
    String txn_key;
    String service;

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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
