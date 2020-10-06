package msmartpay.in.myWallet;


import java.io.Serializable;

class TransactionItems implements Serializable {
    public String Id_No;
    public String tran_id;
    public String mobile_operator;
    public String mobile_number;
    public String service;
    public String Action_on_bal_amt;
    public String tran_status;
    public String net_amout;
    public String DeductedAmt;
    public String dot;
    public String tot;
    public String Agent_balAmt_b_Ded;
    public String Agent_F_balAmt;

    public TransactionItems(
                            String Id_No,
                            String tran_id,
                            String mobile_operator,
                            String mobile_number,
                            String service,
                            String Action_on_bal_amt,
                            String tran_status,
                            String net_amout,
                            String DeductedAmt,
                            String dot,
                            String tot,
                            String Agent_balAmt_b_Ded,
                            String Agent_F_balAmt) {

        this.Id_No = Id_No;
        this.tran_id = tran_id;
        this.mobile_operator = mobile_operator;
        this.mobile_number = mobile_number;
        this.service = service;
        this.Action_on_bal_amt = Action_on_bal_amt;
        this.tran_status = tran_status;
        this.net_amout = net_amout;
        this.DeductedAmt = DeductedAmt;
        this.dot = dot;
        this.tot = tot;
        this.Agent_balAmt_b_Ded = Agent_balAmt_b_Ded;
        this.Agent_F_balAmt = Agent_F_balAmt;
    }
 }
