package in.msmartpay.agent.network.model;

public class CreditRefundOtpRequest {
    private String Key;
    private String AgentID;
    private String TransactionRefNo;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getTransactionRefNo() {
        return TransactionRefNo;
    }

    public void setTransactionRefNo(String transactionRefNo) {
        TransactionRefNo = transactionRefNo;
    }
}
