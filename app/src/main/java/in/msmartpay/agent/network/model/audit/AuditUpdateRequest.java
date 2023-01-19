package in.msmartpay.agent.network.model.audit;

import in.msmartpay.agent.kyc.BaseRequest;

public class AuditUpdateRequest extends BaseRequest {

    private String auditRefId;
    private String filePath;

    public String getAuditRefId() {
        return auditRefId;
    }

    public void setAuditRefId(String auditRefId) {
        this.auditRefId = auditRefId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


}