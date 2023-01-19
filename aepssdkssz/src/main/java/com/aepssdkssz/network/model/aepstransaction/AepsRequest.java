package com.aepssdkssz.network.model.aepstransaction;


public class AepsRequest {

    private String merchant_id;
    private String authorization_key;
    private AepsRequestData data;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }

    public AepsRequestData getData() {
        return data;
    }

    public void setData(AepsRequestData data) {
        this.data = data;
    }

}
