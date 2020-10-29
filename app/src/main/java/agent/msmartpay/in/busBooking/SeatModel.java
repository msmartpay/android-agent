package agent.msmartpay.in.busBooking;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harendra on 9/13/2017.
 */

public class SeatModel implements Parcelable {
    private String Fare;
    private String BerthType;
    private String SeatNo;
    private String SeatType;
    private String Gender;
    private String ConvenienceFee;
    private String SeatTypeId;
    private String ServiceTax;
    private String SeatStatus;

    SeatModel(){}
    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public String getBerthType() {
        return BerthType;
    }

    public void setBerthType(String berthType) {
        BerthType = berthType;
    }

    public String getSeatNo() {
        return SeatNo;
    }

    public void setSeatNo(String seatNo) {
        SeatNo = seatNo;
    }

    public String getSeatType() {
        return SeatType;
    }

    public void setSeatType(String seatType) {
        SeatType = seatType;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getConvenienceFee() {
        return ConvenienceFee;
    }

    public void setConvenienceFee(String convenienceFee) {
        ConvenienceFee = convenienceFee;
    }

    public String getSeatTypeId() {
        return SeatTypeId;
    }

    public void setSeatTypeId(String seatTypeId) {
        SeatTypeId = seatTypeId;
    }

    public String getServiceTax() {
        return ServiceTax;
    }

    public void setServiceTax(String serviceTax) {
        ServiceTax = serviceTax;
    }

    public String getSeatStatus() {
        return SeatStatus;
    }

    public void setSeatStatus(String seatStatus) {
        SeatStatus = seatStatus;
    }


    protected SeatModel(Parcel in) {
        Fare = in.readString();
        BerthType = in.readString();
        SeatNo = in.readString();
        SeatType = in.readString();
        Gender = in.readString();
        ConvenienceFee = in.readString();
        SeatTypeId = in.readString();
        ServiceTax = in.readString();
        SeatStatus = in.readString();
    }

    public static final Creator<SeatModel> CREATOR = new Creator<SeatModel>() {
        @Override
        public SeatModel createFromParcel(Parcel in) {
            return new SeatModel(in);
        }
        @Override
        public SeatModel[] newArray(int size) {
            return new SeatModel[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Fare);
        dest.writeString(BerthType);
        dest.writeString(SeatNo);
        dest.writeString(SeatType);
        dest.writeString(Gender);
        dest.writeString(ConvenienceFee);
        dest.writeString(SeatTypeId);
        dest.writeString(ServiceTax);
        dest.writeString(SeatStatus);
    }
}
