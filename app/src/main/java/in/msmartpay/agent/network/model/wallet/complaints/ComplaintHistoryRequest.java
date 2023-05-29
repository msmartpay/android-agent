package in.msmartpay.agent.network.model.wallet.complaints;

public class ComplaintHistoryRequest {
    String agent_id;
    String txn_key;
    String fromDate;
    String toDate;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

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
