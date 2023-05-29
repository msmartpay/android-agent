package in.msmartpay.agent.network.model.lic;

import com.google.gson.annotations.SerializedName;

public class LicBillFetchResponseData {

    private String ad2;
    private String ad3;
    private String amount;
    private String duedate;
    private String name;

    @SerializedName("bill_fetch")
    private LicBillFetch billFetch;

    public String getAd2() {
        return ad2;
    }

    public void setAd2(String ad2) {
        this.ad2 = ad2;
    }

    public String getAd3() {
        return ad3;
    }

    public void setAd3(String ad3) {
        this.ad3 = ad3;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LicBillFetch getBillFetch() {
        return billFetch;
    }

    public void setBillFetch(LicBillFetch billFetch) {
        this.billFetch = billFetch;
    }
}
