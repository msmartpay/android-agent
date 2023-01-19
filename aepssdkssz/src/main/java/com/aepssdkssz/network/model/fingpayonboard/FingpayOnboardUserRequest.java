package com.aepssdkssz.network.model.fingpayonboard;

public class FingpayOnboardUserRequest {
    private String Key;
    private String AgentID;
    private FingpayOnboardUserData data;

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

    public FingpayOnboardUserData getData() {
        return data;
    }

    public void setData(FingpayOnboardUserData data) {
        this.data = data;
    }

}
