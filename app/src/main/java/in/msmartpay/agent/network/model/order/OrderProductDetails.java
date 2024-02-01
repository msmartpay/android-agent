package in.msmartpay.agent.network.model.order;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderProductDetails {

	@SerializedName("orderAddress")
	private String orderAddress;

	@SerializedName("orderPincode")
	private String orderPincode;

	@SerializedName("orderId")
	private String orderId;

	@SerializedName("gstCompany")
	private String gstCompany;

	@SerializedName("gstNo")
	private String gstNo;

	@SerializedName("orderDate")
	private String orderDate;

	@SerializedName("orderDescription")
	private List<OrderProduct> orderDescription;

	@SerializedName("deliveryTrackingId")
	private String deliveryTrackingId;

	@SerializedName("deliveryStatus")
	private String deliveryStatus;

	@SerializedName("orderName")
	private String orderName;

	public String getOrderAddress(){
		return orderAddress;
	}

	public String getOrderPincode(){
		return orderPincode;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getGstCompany(){
		return gstCompany;
	}

	public String getGstNo(){
		return gstNo;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public List<OrderProduct> getOrderDescription(){
		return orderDescription;
	}

	public String getDeliveryTrackingId(){
		return deliveryTrackingId;
	}

	public String getDeliveryStatus(){
		return deliveryStatus;
	}

	public String getOrderName(){
		return orderName;
	}
}