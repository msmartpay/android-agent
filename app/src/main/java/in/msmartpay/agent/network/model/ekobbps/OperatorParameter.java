package com.scinfotech.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

public class OperatorParameter {

    @SerializedName("error_message")
    private String errorMessage="Enter Value";

    @SerializedName("param_label")
    private String paramLabel = "";

    @SerializedName("regex")
    private String regex;

    @SerializedName("param_name")
    private String paramName;

    @SerializedName("param_id")
    private String paramId;

    @SerializedName("param_type")
    private String paramType = "";
    private String inputValue = "";

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public String getParamLabel() {
        return paramLabel;
    }

    public String getRegex() {
        return regex;
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamId() {
        return paramId;
    }

    public String getParamType() {
        return paramType;
    }

}