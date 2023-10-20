package com.scinfotech.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class OperatorLocation {

	@SerializedName("operator_location_name")
	private String operatorLocationName;

	@SerializedName("operator_location_id")
	private String operatorLocationId;

	@SerializedName("abbreviation")
	private String abbreviation;

	public String getOperatorLocationName(){
		return operatorLocationName;
	}

	public String getOperatorLocationId(){
		return operatorLocationId;
	}

	public String getAbbreviation(){
		return abbreviation;
	}
}