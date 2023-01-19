package in.msmartpay.agent.kyc;

public class BaseRequest {
    private String Key;
        private String AgentID;

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
}
