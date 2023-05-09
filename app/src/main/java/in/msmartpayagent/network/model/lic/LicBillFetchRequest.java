package in.msmartpayagent.network.model.lic;

public class LicBillFetchRequest {

    private String AgentID;
    private String Key;
    private LicBillFetchRequestData data;

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

    public LicBillFetchRequestData getData() {
        return data;
    }

    public void setData(LicBillFetchRequestData data) {
        this.data = data;
    }
}
