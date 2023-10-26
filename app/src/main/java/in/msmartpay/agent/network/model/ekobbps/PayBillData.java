package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class PayBillData {

	@SerializedName("amount")
	private int amount;

	@SerializedName("confirmation_mobile_no")
	private String confirmationMobileNo;

	@SerializedName("operator_id")
	private String operatorId;

	@SerializedName("latlong")
	private String latlong;

	@SerializedName("hc_channel")
	private String hcChannel;

	@SerializedName("utility_acc_no")
	private String utilityAccNo;

	@SerializedName("sender_name")
	private String senderName;

	@SerializedName("source_ip")
	private String sourceIp;

	public void setAmount(int amount){
		this.amount = amount;
	}

	public int getAmount(){
		return amount;
	}

	public void setConfirmationMobileNo(String confirmationMobileNo){
		this.confirmationMobileNo = confirmationMobileNo;
	}

	public String getConfirmationMobileNo(){
		return confirmationMobileNo;
	}

	public void setOperatorId(String operatorId){
		this.operatorId = operatorId;
	}

	public String getOperatorId(){
		return operatorId;
	}

	public void setLatlong(String latlong){
		this.latlong = latlong;
	}

	public String getLatlong(){
		return latlong;
	}

	public void setHcChannel(String hcChannel){
		this.hcChannel = hcChannel;
	}

	public String getHcChannel(){
		return hcChannel;
	}

	public void setUtilityAccNo(String utilityAccNo){
		this.utilityAccNo = utilityAccNo;
	}

	public String getUtilityAccNo(){
		return utilityAccNo;
	}

	public void setSenderName(String senderName){
		this.senderName = senderName;
	}

	public String getSenderName(){
		return senderName;
	}

	public void setSourceIp(String sourceIp){
		this.sourceIp = sourceIp;
	}

	public String getSourceIp(){
		return sourceIp;
	}
}