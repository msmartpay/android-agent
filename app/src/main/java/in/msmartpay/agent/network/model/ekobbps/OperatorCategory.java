package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class OperatorCategory {

	@SerializedName("operator_category_name")
	private String operatorCategoryName;

	@SerializedName("operator_category_id")
	private int operatorCategoryId;

	@SerializedName("operator_category_group")
	private String operatorCategoryGroup;

	@SerializedName("status")
	private String status;

	public String getOperatorCategoryName(){
		return operatorCategoryName;
	}

	public int getOperatorCategoryId(){
		return operatorCategoryId;
	}

	public String getOperatorCategoryGroup(){
		return operatorCategoryGroup;
	}

	public String getStatus(){
		return status;
	}
}