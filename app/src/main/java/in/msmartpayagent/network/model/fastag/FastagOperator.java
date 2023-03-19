
package in.msmartpayagent.network.model.fastag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FastagOperator {

    @SerializedName("ad1_regex")
    private String ad1Regex;
    @Expose
    private String category;
    @Expose
    private String displayname;
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String regex;
    @Expose
    private String viewbill;

    public String getAd1Regex() {
        return ad1Regex;
    }

    public void setAd1Regex(String ad1Regex) {
        this.ad1Regex = ad1Regex;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getViewbill() {
        return viewbill;
    }

    public void setViewbill(String viewbill) {
        this.viewbill = viewbill;
    }

}
