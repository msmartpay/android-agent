package in.msmartpay.agent.network.model.kyc;

public class DocumentDataRequestContainer extends BaseRequest {
    private DocumentData data;

    public DocumentData getData() {
        return data;
    }

    public void setData(DocumentData data) {
        this.data = data;
    }
}
