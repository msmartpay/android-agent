package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperatorResponse {

	@SerializedName("data")
	private List<Operator> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(List<Operator> data){
		this.data = data;
	}

	public List<Operator> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}