package com.aepssdkssz.network.model.fingpayonboard;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FingpayStateResponse  implements Serializable {
    @SerializedName("Status")
    String status;
    String message;

    @SerializedName("data")
    List<FingpayStateData> stateList;

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

    public List<FingpayStateData> getStateList() {
        return stateList;
    }

    public void setStateList(List<FingpayStateData> stateList) {
        this.stateList = stateList;
    }
}
