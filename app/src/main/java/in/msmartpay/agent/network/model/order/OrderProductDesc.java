package in.msmartpay.agent.network.model.order;

import com.google.gson.annotations.SerializedName;

public class OrderProductDesc {

	@SerializedName("product_qty")
	private String productQty;

	@SerializedName("product_price")
	private String productPrice;

	@SerializedName("product_name")
	private String productName;

	public String getProductQty(){
		return productQty;
	}

	public String getProductPrice(){
		return productPrice;
	}

	public String getProductName(){
		return productName;
	}
}