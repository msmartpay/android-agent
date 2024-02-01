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
	private List<OrderProduct> orderDescription;

	@SerializedName("deliveryTrackingId")
	private String deliveryTrackingId;

	@SerializedName("deliveryStatus")
	private String deliveryStatus;

	@SerializedName("slNo")
	private String slNo;

	@SerializedName("orderAmount")
	private String orderAmount;
	private String netAmount;
	private String charge;
	private String orderName;
	@SerializedName("gst")
	private String gstAmount;
	private String mobile;
	private String txnStatus;

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderPincode() {
		return orderPincode;
	}

	public void setOrderPincode(String orderPincode) {
		this.orderPincode = orderPincode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderProduct> getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(List<OrderProduct> orderDescription) {
		this.orderDescription = orderDescription;
	}

	public String getDeliveryTrackingId() {
		return deliveryTrackingId;
	}

	public void setDeliveryTrackingId(String deliveryTrackingId) {
		this.deliveryTrackingId = deliveryTrackingId;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getSlNo() {
		return slNo;
	}

	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(String gstAmount) {
		this.gstAmount = gstAmount;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}