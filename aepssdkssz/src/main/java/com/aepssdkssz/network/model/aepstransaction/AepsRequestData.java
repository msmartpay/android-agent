package com.aepssdkssz.network.model.aepstransaction;

public class AepsRequestData {

    private String type;
    private String customer_id;
    private String bc_aadhaar;
    private String bank_iin;
    private String amount;
    private String device_type;
    private String source_ip;
    private String latlong;
    private String xmlData;
    private String latitude;
    private String longitude;
    private String deviceserno;
    private String transaction_type;
    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }
    public String getDeviceserno() {
        return deviceserno;
    }

    public void setDeviceserno(String deviceserno) {
        this.deviceserno = deviceserno;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getSource_ip() {
        return source_ip;
    }

    public void setSource_ip(String source_ip) {
        this.source_ip = source_ip;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getBc_aadhaar() {
        return bc_aadhaar;
    }

    public void setBc_aadhaar(String bc_aadhaar) {
        this.bc_aadhaar = bc_aadhaar;
    }

    public String getBank_iin() {
        return bank_iin;
    }

    public void setBank_iin(String bank_iin) {
        this.bank_iin = bank_iin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getXmlData() {
        return xmlData;
    }

    public void setXmlData(String xmlData) {
        this.xmlData = xmlData;
    }

}
