
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FastagRechargeRequest {

    @SerializedName("AgentID")
    private String agentID;
    @Expose
    private String cname;
    @Expose
    private RechargeData data;
    @Expose
    private String ipaddress;
    @SerializedName("Key")
    private String key;

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public RechargeData getData() {
        return data;
    }

    public void setData(RechargeData data) {
        this.data = data;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
