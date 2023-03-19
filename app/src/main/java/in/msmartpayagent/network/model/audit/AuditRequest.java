package in.msmartpayagent.network.model.audit;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditRequest {

    @SerializedName("auditRefId")
    @Expose
    private String auditId;
    private String agentId;
    private String auditFilePath;
    @SerializedName("auditStatus")
    @Expose
    private String status;
    @SerializedName("auditDate")
    @Expose
    private String requestDate;

    @SerializedName("auditTime")
    @Expose
    private String requestTime;
    @SerializedName("approvalDate")
    @Expose
    private String approvedDate;

    @SerializedName("transactions")
    @Expose
    private String transactions;

    private String remarks;


    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }


    public String getAuditId() {
    return auditId;
    }

    public void setAuditId(String auditId) {
    this.auditId = auditId;
    }

    public String getStatus() {
    return status;
    }

    public void setStatus(String status) {
    this.status = status;
    }

    public String getRequestDate() {
    return requestDate;
    }

    public void setRequestDate(String requestDate) {
    this.requestDate = requestDate;
    }

    public String getApprovedDate() {
    return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
    this.approvedDate = approvedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}