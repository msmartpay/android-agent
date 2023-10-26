package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class FetchDetails {

	@SerializedName("amount")
	private String amount;

	@SerializedName("billerstatus")
	private String billerstatus;

	@SerializedName("bbpstrxnrefid")
	private String bbpstrxnrefid;

	@SerializedName("ifsc_status")
	private int ifscStatus;

	@SerializedName("utilitycustomername")
	private String utilitycustomername;

	@SerializedName("postalcode")
	private String postalcode;

	@SerializedName("billfetchresponse")
	private String billfetchresponse;

	@SerializedName("geocode")
	private String geocode;

	@SerializedName("billdate")
	private String billdate;

	@SerializedName("customer_id")
	private String customerId;

	@SerializedName("billDueDate")
	private String billDueDate;

	@SerializedName("billername")
	private String billername;
	@SerializedName("reason")
	private String reason;;//Error case

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}



	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBillerstatus() {
		return billerstatus;
	}

	public void setBillerstatus(String billerstatus) {
		this.billerstatus = billerstatus;
	}

	public String getBbpstrxnrefid() {
		return bbpstrxnrefid;
	}

	public void setBbpstrxnrefid(String bbpstrxnrefid) {
		this.bbpstrxnrefid = bbpstrxnrefid;
	}

	public int getIfscStatus() {
		return ifscStatus;
	}

	public void setIfscStatus(int ifscStatus) {
		this.ifscStatus = ifscStatus;
	}

	public String getUtilitycustomername() {
		return utilitycustomername;
	}

	public void setUtilitycustomername(String utilitycustomername) {
		this.utilitycustomername = utilitycustomername;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getBillfetchresponse() {
		return billfetchresponse;
	}

	public void setBillfetchresponse(String billfetchresponse) {
		this.billfetchresponse = billfetchresponse;
	}

	public String getGeocode() {
		return geocode;
	}

	public void setGeocode(String geocode) {
		this.geocode = geocode;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBillDueDate() {
		return billDueDate;
	}

	public void setBillDueDate(String billDueDate) {
		this.billDueDate = billDueDate;
	}

	public String getBillername() {
		return billername;
	}

	public void setBillername(String billername) {
		this.billername = billername;
	}




}