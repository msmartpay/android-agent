package in.msmartpay.agent.network.model.order;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderProductListResponse{

	@SerializedName("data")
	private List<OrderProduct> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public List<OrderProduct> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}