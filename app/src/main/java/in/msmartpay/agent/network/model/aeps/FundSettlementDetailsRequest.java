package in.msmartpay.agent.network.model.aeps;

public class FundSettlementDetailsRequest {
    private String AgentID;
    private String Key;

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
}
