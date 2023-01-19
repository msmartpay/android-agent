package in.msmartpay.agent.network.model.dmr;

public class RefundLiveStatusRequest {

    private String Key;
    private String AgentID;
    private String SenderId;

    private String TransactionRefNo;
    private String OTP;//Confirm transaction only

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getTransactionRefNo() {
        return TransactionRefNo;
    }

    public void setTransactionRefNo(String transactionRefNo) {
        TransactionRefNo = transactionRefNo;
    }

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

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }
}