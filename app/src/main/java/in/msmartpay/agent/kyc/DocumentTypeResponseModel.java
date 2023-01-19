package in.msmartpay.agent.kyc;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DocumentTypeResponseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<DocumentTypeDataModel> data;

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

    public ArrayList<DocumentTypeDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<DocumentTypeDataModel> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DocumentTypeResponseModel{" +
                "data=" + data +
                '}';
    }
}
