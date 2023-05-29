package in.msmartpay.agent.network.model.wallet;

public class WalletTransferRequest {
    String agent_id;
    String txn_key;
    String toAgentId;
    String amount;
    String remark;

    public String getToAgentId() {
        return toAgentId;
    }

    public void setToAgentId(String toAgentId) {
        this.toAgentId = toAgentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
