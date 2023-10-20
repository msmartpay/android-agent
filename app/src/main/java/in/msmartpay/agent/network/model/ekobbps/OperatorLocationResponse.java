package com.scinfotech.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperatorLocationResponse {

	@SerializedName("data")
	private List<OperatorLocation> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public List<OperatorLocation> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}