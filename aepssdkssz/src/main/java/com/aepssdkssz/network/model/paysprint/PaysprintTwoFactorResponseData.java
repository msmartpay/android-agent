package in.aepssdkssz.network.model.paysprint;

import com.google.gson.annotations.SerializedName;

public class PaysprintTwoFactorResponseData {

    private String message;
    private String errorcode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
}
