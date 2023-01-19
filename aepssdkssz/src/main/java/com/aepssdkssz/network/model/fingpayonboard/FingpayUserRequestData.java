package com.aepssdkssz.network.model.fingpayonboard;

import java.io.Serializable;

public class FingpayUserRequestData implements Serializable {

        private String latitude;
        private String longitude;
        private String aadhaarNumber;
        private String deviceIMEI;
        private String encodeFPTxnId;
        private String primaryKeyId;
        private String otp;
        private String xmlData;

        public String getXmlData() {
        return xmlData;
    }

        public void setXmlData(String xmlData) {
        this.xmlData = xmlData;
    }

        public String getEncodeFPTxnId() {
        return encodeFPTxnId;
    }

        public void setEncodeFPTxnId(String encodeFPTxnId) {
        this.encodeFPTxnId = encodeFPTxnId;
    }

        public String getPrimaryKeyId() {
        return primaryKeyId;
    }

        public void setPrimaryKeyId(String primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

        public String getOtp() {
        return otp;
    }

        public void setOtp(String otp) {
        this.otp = otp;
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

        public String getAadhaarNumber() {
        return aadhaarNumber;
    }

        public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

        public String getDeviceIMEI() {
        return deviceIMEI;
    }

        public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }


}
