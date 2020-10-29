package in.msmartpay.agent.busBooking;

import java.io.Serializable;

/**
 * 
 * @author Saurabh tomar
 * 
 */

public class SeatListModel implements Serializable {
	private String SeatNumber;
	private String SeatFare;

	public String getSeatTypeIdNew() {
		return SeatTypeIdNew;
	}

	public void setSeatTypeIdNew(String seatTypeIdNew) {
		SeatTypeIdNew = seatTypeIdNew;
	}

	private String SeatTypeIdNew;


	public String getSeatNumber() {
		return SeatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		SeatNumber = seatNumber;
	}

	public String getSeatFare() {
		return SeatFare;
	}

	public void setSeatFare(String seatFare) {
		SeatFare = seatFare;
	}

}
