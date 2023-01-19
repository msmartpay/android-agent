package in.msmartpay.agent.network.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OperatorData {

@SerializedName("viewbill")
@Expose
private String viewbill;
@SerializedName("ad3_regex")
@Expose
private String ad3Regex;
@SerializedName("ad3_name")
@Expose
private String ad3Name;
@SerializedName("ad1_name")
@Expose
private String ad1Name;
@SerializedName("ad1_regex")
@Expose
private String ad1Regex;
@SerializedName("regex")
@Expose
private String regex;
@SerializedName("displayname")
@Expose
private String displayname;
@SerializedName("name")
@Expose
private String name;
@SerializedName("ad3_d_name")
@Expose
private String ad3DName;
@SerializedName("ad2_d_name")
@Expose
private String ad2DName;
@SerializedName("ad2_name")
@Expose
private String ad2Name;
@SerializedName("ad2_regex")
@Expose
private String ad2Regex;
@SerializedName("category")
@Expose
private String category;
@SerializedName("ad1_d_name")
@Expose
private String ad1DName;

public String getViewbill() {
return viewbill;
}

public void setViewbill(String viewbill) {
this.viewbill = viewbill;
}

public String getAd3Regex() {
return ad3Regex;
}

public void setAd3Regex(String ad3Regex) {
this.ad3Regex = ad3Regex;
}

public String getAd3Name() {
return ad3Name;
}

public void setAd3Name(String ad3Name) {
this.ad3Name = ad3Name;
}

public String getAd1Name() {
return ad1Name;
}

public void setAd1Name(String ad1Name) {
this.ad1Name = ad1Name;
}

public String getAd1Regex() {
return ad1Regex;
}

public void setAd1Regex(String ad1Regex) {
this.ad1Regex = ad1Regex;
}

public String getRegex() {
return regex;
}

public void setRegex(String regex) {
this.regex = regex;
}

public String getDisplayname() {
return displayname;
}

public void setDisplayname(String displayname) {
this.displayname = displayname;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getAd3DName() {
return ad3DName;
}

public void setAd3DName(String ad3DName) {
this.ad3DName = ad3DName;
}

public String getAd2DName() {
return ad2DName;
}

public void setAd2DName(String ad2DName) {
this.ad2DName = ad2DName;
}

public String getAd2Name() {
return ad2Name;
}

public void setAd2Name(String ad2Name) {
this.ad2Name = ad2Name;
}

public String getAd2Regex() {
return ad2Regex;
}

public void setAd2Regex(String ad2Regex) {
this.ad2Regex = ad2Regex;
}

public String getCategory() {
return category;
}

public void setCategory(String category) {
this.category = category;
}

public String getAd1DName() {
return ad1DName;
}

public void setAd1DName(String ad1DName) {
this.ad1DName = ad1DName;
}

}