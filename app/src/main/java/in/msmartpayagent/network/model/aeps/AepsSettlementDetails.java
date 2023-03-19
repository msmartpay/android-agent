package in.msmartpayagent.network.model.aeps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AepsSettlementDetails {
    private Double total_transaction;
    private Double settled_amount;
    private Double unsettled_fund;

    @SerializedName("fund_transfer_list")
    private List<FundSettlementBank> fundTransferList;

    public List<FundSettlementBank> getFundTransferList() {
        return fundTransferList;
    }

    public void setFundTransferList(List<FundSettlementBank> fundTransferList) {
        this.fundTransferList = fundTransferList;
    }
    public Double getTotal_transaction() {
        return total_transaction;
    }

    public void setTotal_transaction(Double total_transaction) {
        this.total_transaction = total_transaction;
    }

    public Double getSettled_amount() {
        return settled_amount;
    }

    public void setSettled_amount(Double settled_amount) {
        this.settled_amount = settled_amount;
    }

    public Double getAvailable_settlement_amount() {
        return unsettled_fund;
    }

    public void setAvailable_settlement_amount(Double available_settlement_amount) {
        this.unsettled_fund = available_settlement_amount;
    }
}
