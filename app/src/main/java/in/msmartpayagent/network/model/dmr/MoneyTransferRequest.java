package in.msmartpayagent.network.model.dmr;

// 102 -- Sender Not registered
public class MoneyTransferRequest {
    private String AgentID;
    private String Key;
    private String SenderId;
    private String SenderName;
    private String BeneAccount;
    private String BeneName;
    private String Ifsc;
    private String BankName;
    private String BeneMobile;
    private String RecipientId;
    private String TxnType;
    private String TxnAmount;
    private String REQUEST_ID;
    private String Remark;
    private String latitude;
    private String longitude;
    private String ip;
    private String transactionPin;

    public String getTransactionPin() {
        return transactionPin;
    }

    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }


    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getBeneAccount() {
        return BeneAccount;
    }

    public void setBeneAccount(String beneAccount) {
        BeneAccount = beneAccount;
    }

    public String getBeneName() {
        return BeneName;
    }

    public void setBeneName(String beneName) {
        BeneName = beneName;
    }

    public String getIfsc() {
        return Ifsc;
    }

    public void setIfsc(String ifsc) {
        Ifsc = ifsc;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBeneMobile() {
        return BeneMobile;
    }

    public void setBeneMobile(String beneMobile) {
        BeneMobile = beneMobile;
    }

    public String getRecipientId() {
        return RecipientId;
    }

    public void setRecipientId(String recipientId) {
        RecipientId = recipientId;
    }

    public String getTxnType() {
        return TxnType;
    }

    public void setTxnType(String txnType) {
        TxnType = txnType;
    }

    public String getTxnAmount() {
        return TxnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        TxnAmount = txnAmount;
    }

    public String getREQUEST_ID() {
        return REQUEST_ID;
    }

    public void setREQUEST_ID(String REQUEST_ID) {
        this.REQUEST_ID = REQUEST_ID;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}