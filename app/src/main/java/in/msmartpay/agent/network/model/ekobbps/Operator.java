package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class Operator {

	@SerializedName("operator_id")
	private int operatorId=-1;

	@SerializedName("name")
	private String name;

	@SerializedName("billFetchResponse")
	private int billFetchResponse;

	@SerializedName("high_commission_channel")
	private int highCommissionChannel=-1;

	@SerializedName("kyc_required")
	private int kycRequired;

	@SerializedName("operator_category")
	private int operatorCategory;

	@SerializedName("location_id")
	private int locationId;

	public void setOperatorId(int operatorId){
		this.operatorId = operatorId;
	}

	public int getOperatorId(){
		return operatorId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setBillFetchResponse(int billFetchResponse){
		this.billFetchResponse = billFetchResponse;
	}

	public int getBillFetchResponse(){
		return billFetchResponse;
	}

	public void setHighCommissionChannel(int highCommissionChannel){
		this.highCommissionChannel = highCommissionChannel;
	}

	public int getHighCommissionChannel(){
		return highCommissionChannel;
	}

	public void setKycRequired(int kycRequired){
		this.kycRequired = kycRequired;
	}

	public int getKycRequired(){
		return kycRequired;
	}

	public void setOperatorCategory(int operatorCategory){
		this.operatorCategory = operatorCategory;
	}

	public int getOperatorCategory(){
		return operatorCategory;
	}

	public void setLocationId(int locationId){
		this.locationId = locationId;
	}

	public int getLocationId(){
		return locationId;
	}
}