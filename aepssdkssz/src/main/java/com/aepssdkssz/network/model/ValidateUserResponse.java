package com.aepssdkssz.network.model;

public class ValidateUserResponse {
    int status;
    String message;
    ValidateUserData data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidateUserData getData() {
        return data;
    }

    public void setData(ValidateUserData data) {
        this.data = data;
    }
}
