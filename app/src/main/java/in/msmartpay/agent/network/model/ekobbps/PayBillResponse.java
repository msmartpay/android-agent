package com.scinfotech.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class PayBillResponse{

	@SerializedName("response_status_id")
	private int responseStatusId;

	@SerializedName("data")
	private PayBillReceipt data;

	@SerializedName("response_type_id")
	private int responseTypeId;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setResponseStatusId(int responseStatusId){
		this.responseStatusId = responseStatusId;
	}

	public int getResponseStatusId(){
		return responseStatusId;
	}

	public void setData(PayBillReceipt data){
		this.data = data;
	}

	public PayBillReceipt getData(){
		return data;
	}

	public void setResponseTypeId(int responseTypeId){
		this.responseTypeId = responseTypeId;
	}

	public int getResponseTypeId(){
		return responseTypeId;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}