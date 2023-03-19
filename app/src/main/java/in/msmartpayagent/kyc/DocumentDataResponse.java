package in.msmartpayagent.kyc;

public class DocumentDataResponse {
    private String document_status;
    private String document_file_name;
    private String remark;
    private String document_type;
    private String sl_no;
    private String document_type_id;
    private String document_no;

    public String getDocument_type_id() {
        return document_type_id;
    }

    public void setDocument_type_id(String document_type_id) {
        this.document_type_id = document_type_id;
    }

    public String getDocument_no() {
        return document_no;
    }

    public void setDocument_no(String document_no) {
        this.document_no = document_no;
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


}
