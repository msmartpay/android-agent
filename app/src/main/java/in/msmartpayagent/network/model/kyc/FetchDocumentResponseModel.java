package in.msmartpayagent.network.model.kyc;

import com.google.gson.annotations.SerializedName;

public class FetchDocumentResponseModel {
    @SerializedName("Status")
    String status;
    @SerializedName("message")
    String message;
}
