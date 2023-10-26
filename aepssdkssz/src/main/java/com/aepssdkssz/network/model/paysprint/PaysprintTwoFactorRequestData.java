package com.aepssdkssz.network.model.paysprint;

import java.io.Serializable;

public class PaysprintTwoFactorRequestData implements Serializable {

    private String latitude;
    private String longitude;
    private String adhaarnumber;
    private String mobilenumber;
    private String ipaddress;
    private String accessmodetype;
    private String data;

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

    public String getAdhaarnumber() {
        return adhaarnumber;
    }

    public void setAdhaarnumber(String adhaarnumber) {
        this.adhaarnumber = adhaarnumber;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getAccessmodetype() {
        return accessmodetype;
    }

    public void setAccessmodetype(String accessmodetype) {
        this.accessmodetype = accessmodetype;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
