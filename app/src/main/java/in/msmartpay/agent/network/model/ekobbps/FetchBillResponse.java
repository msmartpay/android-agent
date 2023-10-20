package com.scinfotech.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class FetchBillResponse{

	@SerializedName("response_status_id")
	private int responseStatusId;

	@SerializedName("data")
	private FetchDetails data;

	@SerializedName("response_type_id")
	private int responseTypeId;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	@SerializedName("invalid_params")
	private InvalidParam invalidParams;//Error case

	public int getResponseStatusId() {
		return responseStatusId;
	}

	public void setResponseStatusId(int responseStatusId) {
		this.responseStatusId = responseStatusId;
	}

	public FetchDetails getData() {
		return data;
	}

	public void setData(FetchDetails data) {
		this.data = data;
	}

	public int getResponseTypeId() {
		return responseTypeId;
	}

	public void setResponseTypeId(int responseTypeId) {
		this.responseTypeId = responseTypeId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public InvalidParam getInvalidParams() {
		return invalidParams;
	}

	public void setInvalidParams(InvalidParam invalidParams) {
		this.invalidParams = invalidParams;
	}
}