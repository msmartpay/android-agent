package in.msmartpay.agent.network.model;

public class CreditRefundRequest {
    private String Key;
    private String AgentID;
    private String TransactionRefNo;
    private String OTP;

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

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }
}
