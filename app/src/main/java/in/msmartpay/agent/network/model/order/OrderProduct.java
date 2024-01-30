package in.msmartpay.agent.network.model.order;

import com.google.gson.annotations.SerializedName;

public class OrderProduct {

	@SerializedName("sl_no")
	private String slNo;

	@SerializedName("product_type")
	private String productType;

	@SerializedName("ds_price")
	private String dsPrice;

	@SerializedName("mds_price")
	private String mdsPrice;

	@SerializedName("description")
	private String description;

	@SerializedName("agent_price")
	private int agentPrice;

	@SerializedName("product_name")
	private String productName="Select";

	@SerializedName("status")
	private String status;
	private int qty = 0;
	private double price = 0;
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public String getSlNo(){
		return slNo;
	}

	public String getProductType(){
		return productType;
	}

	public String getDsPrice(){
		return dsPrice;
	}

	public String getMdsPrice(){
		return mdsPrice;
	}

	public String getDescription(){
		return description;
	}

	public int getAgentPrice(){
		return agentPrice;
	}

	public String getProductName(){
		return productName;
	}

	public String getStatus(){
		return status;
	}
}