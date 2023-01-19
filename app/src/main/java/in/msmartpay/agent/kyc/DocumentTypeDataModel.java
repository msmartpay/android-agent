package in.msmartpay.agent.kyc;

public class DocumentTypeDataModel {
    private String documentType;
    private String sampleFilePath;
    private String displayName="";
    private String uploadUrl="";
    private String docNumber="";

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }


    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }


    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getSampleFilePath() {
        return sampleFilePath;
    }

    public void setSampleFilePath(String sampleFilePath) {
        this.sampleFilePath = sampleFilePath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "DocumentTypeDataModel{" +
                "documentType='" + documentType + '\'' +
                ", sampleFilePath='" + sampleFilePath + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
