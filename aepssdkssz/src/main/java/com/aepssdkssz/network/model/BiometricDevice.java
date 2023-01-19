package com.aepssdkssz.network.model;

public class BiometricDevice {

    private int id;
    private String device_name;
    private String device_type;
    private String biometric_format;
    private String image;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getBiometric_format() {
        return biometric_format;
    }

    public void setBiometric_format(String biometric_format) {
        this.biometric_format = biometric_format;
    }
}
