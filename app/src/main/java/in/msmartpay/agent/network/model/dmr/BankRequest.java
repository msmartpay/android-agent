package in.msmartpay.agent.network.model.dmr;

public class BankRequest {

    private String Key;
    private String AgentID;

    @Override
    public String toString() {
        return "BankRequest{" +
                "Key='" + Key + '\'' +
                ", AgentID='" + AgentID + '\'' +
                ", SenderId='" + SenderId + '\'' +
                ", BankCode='" + BankCode + '\'' +
                '}';
    }

    private String SenderId;



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
        this.Key = key;
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