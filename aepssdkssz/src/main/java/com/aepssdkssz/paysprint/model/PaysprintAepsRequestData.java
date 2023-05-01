package com.aepssdkssz.paysprint.model;

import com.google.gson.annotations.SerializedName;

public class PaysprintAepsRequestData {

    @SerializedName("transactiontype")
    private String type;

    @SerializedName("mobilenumber")
    private String customer_id;

    @SerializedName("adhaarnumber")
    private String bc_aadhaar;

    @SerializedName("nationalbankidentification")
    private String bank_iin;

    @SerializedName("amount")
    private String amount;

    @SerializedName("device_type")
    private String device_type;

    @SerializedName("ipaddress")
    private String source_ip;

    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;

    @SerializedName("accessmodetype")
    private String accessmodetype;

    @SerializedName("is_iris")
    private String isIris;

    @SerializedName("data")
    private String xmlData;
    @SerializedName("pipe")
    private String bankPipe;
    public String getBankPipe() {
        return bankPipe;
    }

    public void setBankPipe(String bankPipe) {
        this.bankPipe = bankPipe;
    }

    public String getAccessmodetype() {
        return accessmodetype;
    }

    public void setAccessmodetype(String accessmodetype) {
        this.accessmodetype = accessmodetype;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIsIris() {
        return isIris;
    }

    public void setIsIris(String isIris) {
        this.isIris = isIris;
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

    public String getXmlData() {
        return xmlData;
    }

    public void setXmlData(String xmlData) {
        this.xmlData = xmlData;
    }

}
