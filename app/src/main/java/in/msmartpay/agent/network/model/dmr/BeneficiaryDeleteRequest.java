package in.msmartpay.agent.network.model.dmr;

public class BeneficiaryDeleteRequest {

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