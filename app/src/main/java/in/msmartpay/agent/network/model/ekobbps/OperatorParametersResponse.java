package com.scinfotech.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class OperatorParametersResponse{

	@SerializedName("data")
	private OperatorParameterData data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public OperatorParameterData getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}