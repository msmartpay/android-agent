package in.msmartpay.agent.network.model.dmr;

public class AccountVerifyData {

    private String senderMobileNo;
    private String senderId;
    private String beneficiaryId;
    private String referenceNumber;
    private String transactionDatetime;
    private String transferType;
    private String transactionId;
    private String bankResponseCode;
    private AccountVerifyBeneficiaryData verifyBeneficiaryData;

    public String getSenderMobileNo() {
        return senderMobileNo;
    }

    public void setSenderMobileNo(String senderMobileNo) {
        this.senderMobileNo = senderMobileNo;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getTransactionDatetime() {
        return transactionDatetime;
    }

    public void setTransactionDatetime(String transactionDatetime) {
        this.transactionDatetime = transactionDatetime;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBankResponseCode() {
        return bankResponseCode;
    }

    public void setBankResponseCode(String bankResponseCode) {
        this.bankResponseCode = bankResponseCode;
    }

    public AccountVerifyBeneficiaryData getVerifyBeneficiaryData() {
        return verifyBeneficiaryData;
    }

    public void setVerifyBeneficiaryData(AccountVerifyBeneficiaryData verifyBeneficiaryData) {
        this.verifyBeneficiaryData = verifyBeneficiaryData;
    }

}