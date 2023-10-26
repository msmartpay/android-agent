package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperatorCategoryResponse {

	@SerializedName("data")
	private List<OperatorCategory> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public List<OperatorCategory> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}