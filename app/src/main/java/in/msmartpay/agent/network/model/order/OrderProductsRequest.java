package in.msmartpay.agent.network.model.order;

import com.google.gson.annotations.SerializedName;

public class OrderProductsRequest{

	@SerializedName("GstCompany")
	private String gstCompany;

	@SerializedName("OrderName")
	private String orderName;

	@SerializedName("GstNumber")
	private String gstNumber;

	@SerializedName("OrderPrice")
	private String orderPrice;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("OrderDescription")
	private String orderDescription;

	@SerializedName("OrderAddress")
	private String orderAddress;

	@SerializedName("AgentID")
	private String agentID;

	@SerializedName("transactionPin")
	private String transactionPin;

	@SerializedName("Key")
	private String key;

	@SerializedName("PinCode")
	private String pinCode;

	public String getGstCompany() {
		return gstCompany;
	}

	public void setGstCompany(String gstCompany) {
		this.gstCompany = gstCompany;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	public String getTransactionPin() {
		return transactionPin;
	}

	public void setTransactionPin(String transactionPin) {
		this.transactionPin = transactionPin;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
}