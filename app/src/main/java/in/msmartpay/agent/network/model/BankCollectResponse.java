package in.msmartpay.agent.network.model;

import com.google.gson.annotations.SerializedName;
import in.msmartpay.agent.collectBanks.CollectBankModel;

import java.util.List;

public class BankCollectResponse {
    @SerializedName("response-message")
    String responseMessage;
    @SerializedName("response-code")
    String responseCode;

    List<CollectBankModel> Bank_List;

    public List<CollectBankModel> getBank_List() {
        return Bank_List;
    }

    public void setBank_List(List<CollectBankModel> bank_List) {
        Bank_List = bank_List;
    }


    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
