package in.msmartpayagent.network.model.aeps;

import java.util.List;

public class BankListResponse {
    String Status;
    String Description;

    List<BankModel> BankList;

    public List<BankModel> getBankList() {
        return BankList;
    }

    public void setBankList(List<BankModel> bankList) {
        BankList = bankList;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
