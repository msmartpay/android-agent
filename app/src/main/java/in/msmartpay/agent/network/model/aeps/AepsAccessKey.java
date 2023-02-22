package in.msmartpay.agent.network.model.aeps;

public class AepsAccessKey {

    String partner_code;
    String secret_key_timestamp;
    String secret_key;
    String developer_key;
    String initiator_id;
    String user_code;
    String partner_name;
    String callback_url;
    String logo;
    String aeps_status;
    String aadharpay_status;
    String psPartnerId;
    String psPartnerKey;

    public String getPsPartnerId() {
        return psPartnerId;
    }

    public void setPsPartnerId(String psPartnerId) {
        this.psPartnerId = psPartnerId;
    }

    public String getPsPartnerKey() {
        return psPartnerKey;
    }

    public void setPsPartnerKey(String psPartnerKey) {
        this.psPartnerKey = psPartnerKey;
    }


    public String getAeps_status() {
        return aeps_status;
    }

    public void setAeps_status(String aeps_status) {
        this.aeps_status = aeps_status;
    }

    public String getAadharpay_status() {
        return aadharpay_status;
    }

    public void setAadharpay_status(String aadharpay_status) {
        this.aadharpay_status = aadharpay_status;
    }

    public String getPartner_code() {
        return partner_code;
    }

    public void setPartner_code(String partner_code) {
        this.partner_code = partner_code;
    }
    public String getSecret_key_timestamp() {
        return secret_key_timestamp;
    }

    public void setSecret_key_timestamp(String secret_key_timestamp) {
        this.secret_key_timestamp = secret_key_timestamp;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getDeveloper_key() {
        return developer_key;
    }

    public void setDeveloper_key(String developer_key) {
        this.developer_key = developer_key;
    }

    public String getInitiator_id() {
        return initiator_id;
    }

    public void setInitiator_id(String initiator_id) {
        this.initiator_id = initiator_id;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }
    public String getCallback_url() {
        return callback_url;
    }


    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
