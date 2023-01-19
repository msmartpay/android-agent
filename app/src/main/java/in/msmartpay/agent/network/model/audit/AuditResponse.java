package in.msmartpay.agent.network.model.audit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditResponse {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<AuditRequest> auditRequest = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<AuditRequest> getAuditRequest() {
return auditRequest;
}

public void setAuditRequest(List<AuditRequest> auditRequest) {
this.auditRequest = auditRequest;
}

}