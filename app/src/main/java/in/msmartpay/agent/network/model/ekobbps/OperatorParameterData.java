package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperatorParameterData {

	@SerializedName("operator_name")
	private String operatorName;

	@SerializedName("data")
	private List<OperatorParameter> data;

	@SerializedName("operator_id")
	private int operatorId;

	@SerializedName("fetchBill")
	private int fetchBill;

	@SerializedName("BBPS")
	private int bBPS;

	public String getOperatorName(){
		return operatorName;
	}

	public List<OperatorParameter> getData(){
		return data;
	}

	public int getOperatorId(){
		return operatorId;
	}

	public int getFetchBill(){
		return fetchBill;
	}

	public int getBBPS(){
		return bBPS;
	}
}