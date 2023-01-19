
package in.msmartpay.agent.network.model.dmr;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankListDmrResponse {

    private String message;
    private String Status;
    @SerializedName("BankList")
    private List<BankListModel> bankList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<BankListModel> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankListModel> bankList) {
        this.bankList = bankList;
    }
}
