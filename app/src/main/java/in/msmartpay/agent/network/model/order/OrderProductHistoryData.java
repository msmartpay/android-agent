package in.msmartpay.agent.network.model.order;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderProductHistoryData {

	@SerializedName("orderAddress")
	private String orderAddress;

	@SerializedName("orderTime")
	private String orderTime;

	@SerializedName("orderPincode")
	private String orderPincode;

	@SerializedName("orderId")
	private String orderId;

	@SerializedName("gstNo")
	private String gstNo;

	@SerializedName("orderDetail")
	private String orderDetail;

	@SerializedName("remark")
	private String remark;

	@SerializedName("orderDate")
	private String orderDate;

	@SerializedName("orderDescription")
	private List<OrderProductDesc> orderDescription;

	@SerializedName("deliveryTrackingId")
	private String deliveryTrackingId;

	@SerializedName("deliveryStatus")
	private String deliveryStatus;

	@SerializedName("slNo")
	private String slNo;

	public String getOrderAddress(){
		return orderAddress;
	}

	public String getOrderTime(){
		return orderTime;
	}

	public String getOrderPincode(){
		return orderPincode;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getGstNo(){
		return gstNo;
	}

	public String getOrderDetail(){
		return orderDetail;
	}

	public String getRemark(){
		return remark;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public List<OrderProductDesc> getOrderDescription(){
		return orderDescription;
	}

	public String getDeliveryTrackingId(){
		return deliveryTrackingId;
	}

	public String getDeliveryStatus(){
		return deliveryStatus;
	}

	public String getSlNo(){
		return slNo;
	}
}