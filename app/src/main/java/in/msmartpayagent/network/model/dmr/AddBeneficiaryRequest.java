package in.msmartpayagent.network.model.dmr;

public class AddBeneficiaryRequest {
    private String AgentID;
    private String Key;
    private String SenderId;
    private String BankAccount;
    private String BeneName;
    private String IFSC;
    private String BankName;
    private String BeneMobile;
    private String varify;
    private String BankCode;

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

    public String getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(String bankAccount) {
        BankAccount = bankAccount;
    }

    public String getBeneName() {
        return BeneName;
    }

    public void setBeneName(String beneName) {
        BeneName = beneName;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
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

    public String getVarify() {
        return varify;
    }

    public void setVarify(String varify) {
        this.varify = varify;
    }

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }
}