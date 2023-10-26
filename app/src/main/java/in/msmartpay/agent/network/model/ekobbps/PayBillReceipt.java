package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class PayBillReceipt {

	@SerializedName("txn_status")
	private String txStatus;

	@SerializedName("tds")
	private String tds;

	@SerializedName("bbpstrxnrefid")
	private String bbpstrxnrefid;

	@SerializedName("txstatus_desc")
	private String txstatusDesc;

	@SerializedName("utilitycustomername")
	private String utilitycustomername;

	@SerializedName("fee")
	private String fee;

	@SerializedName("discount")
	private String discount;

	@SerializedName("tid")
	private String tid;

	@SerializedName("sender_id")
	private String senderId;

	@SerializedName("balance")
	private String balance;

	@SerializedName("customerconveniencefee")
	private String customerconveniencefee;

	@SerializedName("commission")
	private String commission;

	@SerializedName("state")
	private String state;

	@SerializedName("recipient_id")
	private String recipientId;

	@SerializedName("timestamp")
	private String timestamp;

	@SerializedName("amount")
	private String amount;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("reference_tid")
	private String referenceTid;

	@SerializedName("serial_number")
	private String serialNumber;

	@SerializedName("customermobilenumber")
	private String customermobilenumber;

	@SerializedName("payment_mode_desc")
	private String paymentModeDesc;

	@SerializedName("last_used_okekey")
	private String lastUsedOkekey;

	@SerializedName("operator_name")
	private String operatorName;

	@SerializedName("totalamount")
	private String totalamount;

	@SerializedName("billnumber")
	private String billnumber;

	@SerializedName("billdate")
	private String billdate;

	@SerializedName("approvalreferencenumber")
	private String approvalreferencenumber;

	@SerializedName("status_text")
	private String statusText;

	@SerializedName("account")
	private String account;
	@SerializedName("api_txn_id")
	private String clientRefId;

	public String getClientRefId() {
		return clientRefId;
	}

	public void setClientRefId(String clientRefId) {
		this.clientRefId = clientRefId;
	}

	public void setTxStatus(String txStatus){
		this.txStatus = txStatus;
	}

	public String getTxStatus(){
		return txStatus;
	}

	public void setTds(String tds){
		this.tds = tds;
	}

	public String getTds(){
		return tds;
	}

	public void setBbpstrxnrefid(String bbpstrxnrefid){
		this.bbpstrxnrefid = bbpstrxnrefid;
	}

	public String getBbpstrxnrefid(){
		return bbpstrxnrefid;
	}

	public void setTxstatusDesc(String txstatusDesc){
		this.txstatusDesc = txstatusDesc;
	}

	public String getTxstatusDesc(){
		return txstatusDesc;
	}

	public void setUtilitycustomername(String utilitycustomername){
		this.utilitycustomername = utilitycustomername;
	}

	public String getUtilitycustomername(){
		return utilitycustomername;
	}

	public void setFee(String fee){
		this.fee = fee;
	}

	public String getFee(){
		return fee;
	}

	public void setDiscount(String discount){
		this.discount = discount;
	}

	public String getDiscount(){
		return discount;
	}

	public void setTid(String tid){
		this.tid = tid;
	}

	public String getTid(){
		return tid;
	}

	public void setSenderId(String senderId){
		this.senderId = senderId;
	}

	public String getSenderId(){
		return senderId;
	}

	public void setBalance(String balance){
		this.balance = balance;
	}

	public String getBalance(){
		return balance;
	}

	public void setCustomerconveniencefee(String customerconveniencefee){
		this.customerconveniencefee = customerconveniencefee;
	}

	public String getCustomerconveniencefee(){
		return customerconveniencefee;
	}

	public void setCommission(String commission){
		this.commission = commission;
	}

	public String getCommission(){
		return commission;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setRecipientId(String recipientId){
		this.recipientId = recipientId;
	}

	public String getRecipientId(){
		return recipientId;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setReferenceTid(String referenceTid){
		this.referenceTid = referenceTid;
	}

	public String getReferenceTid(){
		return referenceTid;
	}

	public void setSerialNumber(String serialNumber){
		this.serialNumber = serialNumber;
	}

	public String getSerialNumber(){
		return serialNumber;
	}

	public void setCustomermobilenumber(String customermobilenumber){
		this.customermobilenumber = customermobilenumber;
	}

	public String getCustomermobilenumber(){
		return customermobilenumber;
	}

	public void setPaymentModeDesc(String paymentModeDesc){
		this.paymentModeDesc = paymentModeDesc;
	}

	public String getPaymentModeDesc(){
		return paymentModeDesc;
	}

	public void setLastUsedOkekey(String lastUsedOkekey){
		this.lastUsedOkekey = lastUsedOkekey;
	}

	public String getLastUsedOkekey(){
		return lastUsedOkekey;
	}

	public void setOperatorName(String operatorName){
		this.operatorName = operatorName;
	}

	public String getOperatorName(){
		return operatorName;
	}

	public void setTotalamount(String totalamount){
		this.totalamount = totalamount;
	}

	public String getTotalamount(){
		return totalamount;
	}

	public void setBillnumber(String billnumber){
		this.billnumber = billnumber;
	}

	public String getBillnumber(){
		return billnumber;
	}

	public void setBilldate(String billdate){
		this.billdate = billdate;
	}

	public String getBilldate(){
		return billdate;
	}

	public void setApprovalreferencenumber(String approvalreferencenumber){
		this.approvalreferencenumber = approvalreferencenumber;
	}

	public String getApprovalreferencenumber(){
		return approvalreferencenumber;
	}

	public void setStatusText(String statusText){
		this.statusText = statusText;
	}

	public String getStatusText(){
		return statusText;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getAccount(){
		return account;
	}
}