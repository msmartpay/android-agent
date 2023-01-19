package com.aepssdkssz.network.model.fingpayonboard;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FingpayOnboardResponse implements Serializable {
    private String statusCode;
    @SerializedName("Status")
    private String status;
    @SerializedName("message")
    private String message;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    private FingpayUserRequestData data;

    public FingpayUserRequestData getData() {
        return data;
    }

    public void setData(FingpayUserRequestData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public class FingpayUserRequestData{

        private String encodeFPTxnId;
        private String primaryKeyId;

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
    }
}
