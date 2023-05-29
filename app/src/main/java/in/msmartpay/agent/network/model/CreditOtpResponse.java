package in.msmartpay.agent.network.model;

public class CreditOtpResponse {
    private String refid;
    private String name;
    private String mobile;
    private String card_number;
    private String amount;
    private String network;
    private String remarks;

    @Override
    public String toString() {
        return "CreditOtpResponse{" +
                "refid='" + refid + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", card_number='" + card_number + '\'' +
                ", amount='" + amount + '\'' +
                ", network='" + network + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
