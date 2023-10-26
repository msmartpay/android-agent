package in.msmartpay.agent.network.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EMIOperatorResponse {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("data")
@Expose
private OperatorData data;
@SerializedName("message")
@Expose
private String message;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public OperatorData getData() {
return data;
}

public void setData(OperatorData data) {
this.data = data;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

}