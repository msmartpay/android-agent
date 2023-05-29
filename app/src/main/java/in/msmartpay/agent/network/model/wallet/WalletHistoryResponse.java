package in.msmartpay.agent.network.model.wallet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletHistoryResponse {
    @SerializedName("response-code")
    String responseCode;
    @SerializedName("response-message")
    String responseMessage;
    List<TransactionItems> Statement;
    List<String> services;

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }



    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<TransactionItems> getStatement() {
        return Statement;
    }

    public void setStatement(List<TransactionItems> statement) {
        Statement = statement;
    }
}
