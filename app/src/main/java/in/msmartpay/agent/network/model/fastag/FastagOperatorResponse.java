
package in.msmartpay.agent.network.model.fastag;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FastagOperatorResponse {

    @Expose
    private List<FastagOperator> data;
    @Expose
    private String message;
    @SerializedName("Status")
    private String status;

    public List<FastagOperator> getData() {
        return data;
    }

    public void setData(List<FastagOperator> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
