package in.msmartpay.agent.network.model.dmr;

public class BankListModel {

    private String bname;
    private String bcode;

    public String getBankName() {
        return bname;
    }

    public void setBankName(String bankName) {
        bname = bankName;
    }

    public String getBankCode() {
        return bcode;
    }

    public void setBankCode(String bankCode) {
        bcode = bankCode;
    }

    @Override
    public String toString() {
        return bname;
    }
}