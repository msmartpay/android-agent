package in.msmartpayagent.network.model.aeps;

public class AddFundSettlementBankRequest {
    private String Key;
    private String AgentID;
    private String bank_name;
    private String account;
    private String receipient_name;
    private String ifsc;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBeneficiary_name() {
        return receipient_name;
    }

    public void setBeneficiary_name(String beneficiary_name) {
        this.receipient_name = beneficiary_name;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
}
