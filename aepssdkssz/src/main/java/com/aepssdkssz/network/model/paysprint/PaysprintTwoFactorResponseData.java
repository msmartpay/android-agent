package com.aepssdkssz.network.model.paysprint;

public class PaysprintTwoFactorResponseData {

    private String message;
    private String errorcode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
}
