package msmartpay.in.moneyTransferDMT;

/**
 * Created by Smartkinda on 6/18/2017.
 */

public class BankDetailsModel {

    private String beneficiaryName;
    private String accountNumber;
    private String BankName;
    private String Status;
    private String verifyStatus;
    private String IFSCcode;
    private String BeneCode;


    //==========================================================================

    public String getBeneCode() {
        return BeneCode;
    }

    public void setBeneCode(String beneCode) {
        BeneCode = beneCode;
    }

    public String getIFSCcode() {
        return IFSCcode;
    }

    public void setIFSCcode(String IFSCcode) {
        this.IFSCcode = IFSCcode;
    }


    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }


    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }



}
