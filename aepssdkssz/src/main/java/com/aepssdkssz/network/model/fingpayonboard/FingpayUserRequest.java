package com.aepssdkssz.network.model.fingpayonboard;

public class FingpayUserRequest {
    private String Key;
    private String AgentID;
    private FingpayUserRequestData data;

    public FingpayUserRequestData getData() {
        return data;
    }

    public void setData(FingpayUserRequestData data) {
        this.data = data;
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




}
