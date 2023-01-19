package in.msmartpay.agent.network.model;

public class PlanRequest {
    String agent_id;
    String txn_key;
    String type;
    String cricle;
    String operator;
    String client;
    String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCricle() {
        return cricle;
    }

    public void setCricle(String cricle) {
        this.cricle = cricle;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
