package com.aepssdkssz.network.model.fingpayonboard;

import java.io.Serializable;

public class FingpayStateData implements Serializable {

    private String stateId;
    private String state;
    private String stateCode;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
