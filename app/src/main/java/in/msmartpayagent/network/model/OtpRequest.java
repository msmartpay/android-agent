package in.msmartpayagent.network.model;

public class OtpRequest {
//fsJson.put("Key", key);
//			fsJson.put("AgentID", agentId);
//			fsJson.put("mobile", mobile);
//			fsJson.put("name", name);
//			fsJson.put("card_number", card_number);
//			fsJson.put("amount", amount);
//			fsJson.put("network", network);
//			fsJson.put("remarks", remarks);
    private String mobile;
    private String AgentID;
    private String Key;
    private String name;
    private String card_number;
    private int amount;
    private String network;
    private String remarks;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



}
