
package in.msmartpayagent.network.model.fastag;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FastagFetchRequest {

    @SerializedName("AgentID")
    private String agentID;
    @Expose
    private FastagData data;
    @SerializedName("Key")
    private String key;

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public FastagData getData() {
        return data;
    }

    public void setData(FastagData data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
