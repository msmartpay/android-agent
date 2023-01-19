package in.msmartpay.agent.network.model.wallet;



public class TransactionItems {
    private String Id_No;
    private String tran_id;
    private String mobile_operator;
    private String mobile_number;
    private String service;
    private String Action_on_bal_amt;
    private String tran_status;
    private String txnAmount;
    private String net_amout;
    private String DeductedAmt;
    private String dot;
    private String tot;
    private String Agent_balAmt_b_Ded;
    private String Agent_F_balAmt;
    private String bankRefId;
    private String operatorId;
    private String commission;
    private String serviceCharge;
    private String remark;

    private String Bene_Name="";
    private String Bene_Account="";
    private String Bene_Bank_Name="";
    private String Bene_Bank_IFSC="";

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getBankRefId() {
        return bankRefId;
    }

    public void setBankRefId(String bankRefId) {
        this.bankRefId = bankRefId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId_No() {
        return Id_No;
    }

    public void setId_No(String id_No) {
        Id_No = id_No;
    }

    public String getTran_id() {
        return tran_id;
    }

    public void setTran_id(String tran_id) {
        this.tran_id = tran_id;
    }

    public String getMobile_operator() {
        return mobile_operator;
    }

    public void setMobile_operator(String mobile_operator) {
        this.mobile_operator = mobile_operator;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAction_on_bal_amt() {
        return Action_on_bal_amt;
    }

    public void setAction_on_bal_amt(String action_on_bal_amt) {
        Action_on_bal_amt = action_on_bal_amt;
    }

    public String getTran_status() {
        return tran_status;
    }

    public void setTran_status(String tran_status) {
        this.tran_status = tran_status;
    }

    public String getNet_amout() {
        return net_amout;
    }

    public void setNet_amout(String net_amout) {
        this.net_amout = net_amout;
    }

    public String getDeductedAmt() {
        return DeductedAmt;
    }

    public void setDeductedAmt(String deductedAmt) {
        DeductedAmt = deductedAmt;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getTot() {
        return tot;
    }

    public void setTot(String tot) {
        this.tot = tot;
    }

    public String getAgent_balAmt_b_Ded() {
        return Agent_balAmt_b_Ded;
    }

    public void setAgent_balAmt_b_Ded(String agent_balAmt_b_Ded) {
        Agent_balAmt_b_Ded = agent_balAmt_b_Ded;
    }

    public String getAgent_F_balAmt() {
        return Agent_F_balAmt;
    }

    public void setAgent_F_balAmt(String agent_F_balAmt) {
        Agent_F_balAmt = agent_F_balAmt;
    }

    public String getBene_Name() {
        return Bene_Name;
    }

    public void setBene_Name(String bene_Name) {
        Bene_Name = bene_Name;
    }

    public String getBene_Account() {
        return Bene_Account;
    }

    public void setBene_Account(String bene_Account) {
        Bene_Account = bene_Account;
    }

    public String getBene_Bank_Name() {
        return Bene_Bank_Name;
    }

    public void setBene_Bank_Name(String bene_Bank_Name) {
        Bene_Bank_Name = bene_Bank_Name;
    }

    public String getBene_Bank_IFSC() {
        return Bene_Bank_IFSC;
    }

    public void setBene_Bank_IFSC(String bene_Bank_IFSC) {
        Bene_Bank_IFSC = bene_Bank_IFSC;
    }
}
