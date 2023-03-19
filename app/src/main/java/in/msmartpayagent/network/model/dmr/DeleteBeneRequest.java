package in.msmartpayagent.network.model.dmr;

public class DeleteBeneRequest {

    private String Key;
    private String AgentID;
    private String SenderId;
    private String BeneficiaryId;

    public String getBeneficiaryId() {
        return BeneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        BeneficiaryId = beneficiaryId;
    }

    private String BankCode;//For Bank Details  only

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
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