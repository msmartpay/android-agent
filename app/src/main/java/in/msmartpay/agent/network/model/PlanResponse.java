package in.msmartpay.agent.network.model;

import com.google.gson.JsonElement;

public class PlanResponse {

    int status;
    String message;
    private String msg;
    JsonElement records;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public JsonElement getRecords() {
        return records;
    }

    public void setRecords(JsonElement records) {
        this.records = records;
    }
}
