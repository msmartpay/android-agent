package in.msmartpay.agent.network.model.ekobbps;

public class BillPayMainRequest {
    String category_id;
    String location;
    String operator_id;
    String agent_id;
    String txn_key;

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agentID) {
        this.agent_id = agentID;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }
}
