package in.msmartpay.agent.network.model.order;

import com.google.gson.annotations.SerializedName;

public class OrderProductDetailsResponse{

	@SerializedName("data")
	private OrderProductDetails data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public OrderProductDetails getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}