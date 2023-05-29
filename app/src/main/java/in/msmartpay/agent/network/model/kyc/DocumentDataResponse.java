package in.msmartpay.agent.network.model.kyc;

public class DocumentDataResponse {
    private String document_status;
    private String document_file_name;
    private String remark;
    private String document_type;
    private String document_number;
    private String sl_no;

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public String getDocument_status() {
        return document_status;
    }

    public void setDocument_status(String document_status) {
        this.document_status = document_status;
    }

    public String getDocument_file_name() {
        return document_file_name;
    }

    public void setDocument_file_name(String document_file_name) {
        this.document_file_name = document_file_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    @Override
    public String toString() {
        return "DocumentDataResponse{" +
                "document_status='" + document_status + '\'' +
                ", document_file_name='" + document_file_name + '\'' +
                ", remark='" + remark + '\'' +
                ", document_type='" + document_type + '\'' +
                ", sl_no='" + sl_no + '\'' +
                '}';
    }
}
