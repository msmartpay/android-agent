package com.aepssdkssz.network.model;

import java.util.List;

public class BankListResponse {
    private String status;
    private String message;
    private List<BankModel> data;

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

    public List<BankModel> getData() {
        return data;
    }

    public void setData(List<BankModel> data) {
        this.data = data;
    }
}
