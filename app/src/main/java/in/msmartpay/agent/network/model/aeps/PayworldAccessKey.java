package in.msmartpay.agent.network.model.aeps;

public class PayworldAccessKey {

    String bc_id;
    String merchant_id;
    String merchant_key;
    String header_secret_key;
    String content_secret_key;
    String merchant_service;
    String payworld_aeps_gateway_url;

    @Override
    public String toString() {
        return "PayworldAccessKey{" +
                "bc_id='" + bc_id + '\'' +
                ", merchant_id='" + merchant_id + '\'' +
                ", merchant_key='" + merchant_key + '\'' +
                ", header_secret_key='" + header_secret_key + '\'' +
                ", content_secret_key='" + content_secret_key + '\'' +
                ", merchant_service='" + merchant_service + '\'' +
                ", payworld_aeps_gateway_url='" + payworld_aeps_gateway_url + '\'' +
                '}';
    }

    public String getBc_id() {
        return bc_id;
    }

    public void setBc_id(String bc_id) {
        this.bc_id = bc_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_key() {
        return merchant_key;
    }

    public void setMerchant_key(String merchant_key) {
        this.merchant_key = merchant_key;
    }

    public String getHeader_secret_key() {
        return header_secret_key;
    }

    public void setHeader_secret_key(String header_secret_key) {
        this.header_secret_key = header_secret_key;
    }

    public String getContent_secret_key() {
        return content_secret_key;
    }

    public void setContent_secret_key(String content_secret_key) {
        this.content_secret_key = content_secret_key;
    }

    public String getMerchant_service() {
        return merchant_service;
    }

    public void setMerchant_service(String merchant_service) {
        this.merchant_service = merchant_service;
    }

    public String getPayworld_aeps_gateway_url() {
        return payworld_aeps_gateway_url;
    }

    public void setPayworld_aeps_gateway_url(String payworld_aeps_gateway_url) {
        this.payworld_aeps_gateway_url = payworld_aeps_gateway_url;
    }
}
