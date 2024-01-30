package in.msmartpay.agent.network.model.order;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderProductHistoryResponse{

	@SerializedName("data")
	private List<OrderProductHistoryData> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public List<OrderProductHistoryData> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}