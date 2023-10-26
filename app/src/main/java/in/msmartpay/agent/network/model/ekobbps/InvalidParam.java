package in.msmartpay.agent.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class InvalidParam {

	@SerializedName("reason")
	private String reason;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}