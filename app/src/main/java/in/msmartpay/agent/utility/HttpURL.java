package in.msmartpay.agent.utility;

/**
 * Created by Harendra on 4/29/2017.
 */

public interface HttpURL {

    int MY_SOCKET_TIMEOUT_MS             = 60 * 1000;
    String DSID                          = "DSUP33273001";
    String CLIENT = "MSMARTPAY";
    String BASE_URL                      = "https://msmartpay.in/MRA1.0/resources/";
    String FORGET_PASSWORD               = BASE_URL + "SKLogin/ForgetPass";
    String LOGIN_URL                     = BASE_URL + "SKLogin/Login";
    String CHANGE_PASSWORD               = BASE_URL + "SKLogin/ChangePass";

    String NEW_MOBILE_REGISTER           = BASE_URL + "SKLogin/mobileValidation";
    String OTP_MOBILE_REGISTER           = BASE_URL + "SKLogin/OtpValidation";
    String REGISTER_URL                  = BASE_URL + "SKLogin/SKRegister";
    String PROFILE_URL                   = BASE_URL + "SKLogin/getProfile";
    String PROFILE_UPDATE_URL            = BASE_URL + "SKLogin/getUpdateProfile";
    String STATE_URL                     = BASE_URL + "SKLogin/getState";
    String DISTRICT_BY_STATE_URL         = BASE_URL + "SKLogin/getStateDistrict";
    String KYC_UPDATE_URL                = BASE_URL + "SKLogin/getRequestKyc";
    String KYC_DETAILS_URL               = BASE_URL + "SKLogin/getKycDetails";
    String OPERATOR_CODE_URL             = BASE_URL + "SKPOST/billpayOperator";
    String RECHARGE_URL                  = BASE_URL + "SKPRE/Recharge";
    String BILL_PAY                      = BASE_URL + "SKPOST/Billpay";
    String SEARCH_TRAIN                  = BASE_URL + "SKTH/SearchTran";
    String STATEMENT_TRAIN               = BASE_URL + "SKTH/TranHistory";
    String TranHistoryByDate             = BASE_URL + "SKTH/TranHistoryByDate";

    String WALLET_BASE                   = "SKWS/";
    String TURN_OVER                     = BASE_URL + WALLET_BASE + "TurnOver";

    String TICKET_URL                    = BASE_URL + WALLET_BASE + "TicketT";
    String WALLET_BELL_REQ               = BASE_URL + WALLET_BASE + "WalletBalRequest";
    String WALLET_TRANSFER               = BASE_URL + WALLET_BASE + "WalletTransfer";
    String WALLET_BELL_REQ_DETAIL        = BASE_URL + WALLET_BASE + "WalletBalReqDetails";
    String TICKET_DETAILS                = BASE_URL + WALLET_BASE + "TicketDetails";
    String WALLET_BALANCE                = BASE_URL + WALLET_BASE + "WalletBalance";
    String UPDATE_DETAILS                = BASE_URL + WALLET_BASE + "UpdateDetails";
    String ORDER_HISTORY                 = BASE_URL + "SKTH/OrderHistory";
    String ORDER_HISTORY_BY_ORDER_ID     = BASE_URL + "SKTH/OrderDetailById";

    String CollectBankDetails = BASE_URL+ WALLET_BASE +"CollectionBanks";


    /***** website url ***/


    //String ABOUT_US                      = "http://smartkinda.com/About.jsp";
    //String SMART_KINDA_SELLER            = "http://seller.smartkinda.com/SmartKindaSeller/";
    //String SMART_KINDA_PARTNER           = "http://partner.smartkinda.com/SmartKindaSeller/";
    //String ADD_MONEY_WALLET_REQUEST      ="http://smartkinda.com/AG/PGRequest.jsp?";
    //String ADD_MONEY_WALLET_RESPONSE     ="http://smartkinda.com/AG/PGSendResponseforApp.jsp?";
    /*   Bus URL ***/
    //http://smartkinda.com/B2CMRA/resources/BUS/GetSeatMapRequest
        //Bus Booking System

    String BUS_SOURCE_URL                  = BASE_URL +"BUS/getSource";
    String BUS_DESTINATION_URL             = BASE_URL +"BUS/getDestination";
    String ALL_SEAT_DETAILS_URL            = BASE_URL +"BUS/getBusList";
    String SEAT_MAP_URL                    = BASE_URL +"BUS/GetSeatMapRequest";
    String SEAT_BLOCKING_URL               = BASE_URL +"BUS/getSeatBlockRequest";
    String SEAT_CONFIRM_BOOKING_URL        = BASE_URL +"BUS/getBookingRequest";
    String PNR_STATUS_PENALITY_URL         = BASE_URL +"BUS/GetCancellationPenalty";
    String CANCELLATION_TICKET_URL         = BASE_URL +"BUS/GetCancellationRequest";
    String BOOKED_HISTORY_URL              = BASE_URL +"BUS/GetBookedHistoryRequest";

    /****  Flights URls  ****/

    String FLIGHT_DEPARTURE_URL             = BASE_URL +"flight/getDeparture";
    String FLIGHT_ARRIVAL_URL               = BASE_URL +"flight/getArrival";
    String FLIGHT_AVAILABILITY_REQUEST_URL  = BASE_URL +"flight/GetAvailabilityRequest";
    String FLIGHT_TAX_REQUEST_URL           = BASE_URL +"flight/getTaxRequest";
    String FLIGHT_BOOK_REQUEST_URL          = BASE_URL +"flight/getGetBookRequest";
    String FLIGHT_CANCELLATION_REQUEST_URL  = BASE_URL +"flight/GetCancellation";

    String GET_DMR_SESSION                   = BASE_URL + "DMR/GetDMRSession";
    //public static final String FIND_SENDER                       = BASE_URL + "DMR/FindSender";
    //public static final String SENDER_RESISTRATION               = BASE_URL + "DMR/RegisterSender";
    //public static final String CONFIRMATION_SENDER_RESISTRATION  = BASE_URL + "DMR/ConfirmRegisterSender";
    String ADD_BENEFICIARY                   = BASE_URL + "DMR/AddBene";
    String CONFIRM_ADD_BENEFICIARY           = BASE_URL + "DMR/ConfirmAddBene";
    String CHANGE_BENE_STATUS                = BASE_URL + "DMR/VerifyAddBene";
    String RE_SEND_OTP                       = BASE_URL + "DMR/AddBeneReSendOtp";
    //public static final String IMPS_NEFT_TRANSACTION             = BASE_URL + "DMR/Transaction";
    String SENDER_UPDATE_DETAILS             = BASE_URL + "DMR/UpdateSender";
    String SENDER_CONFIRM_DETAILS            = BASE_URL + "DMR/ConfirmUpdateSender";
    //public static final String REFUND_TRANSACTION                = BASE_URL + "DMR/RefundTransaction";
    //public static final String CONFIRM_REFUND_TRANSACTION        = BASE_URL + "DMR/RefundDMRConfirm";
    //public static final String SENDER_HISTORY                    = BASE_URL + "DMR/SenderHistory";

    //---------------------dmr1--------------------------
    String FIND_SENDER      = BASE_URL + "SKDMR/FindSender";
    String SENDER_RESISTRATION = BASE_URL + "SKDMR/RegisterSender";
    String CONFIRMATION_SENDER_RESISTRATION = BASE_URL + "SKDMR/VerifySender";
    String SENDER_RESEND_OTP = BASE_URL + "SKDMR/ReSendOTPRegisterSender";
    String GET_BANK_LIST = BASE_URL + "SKDMR/GetBankList";
    String ADD_BENEFICIARY_IFSC_CODE = BASE_URL + "SKDMR/AccountVerifyByBankAccountIFSC";
    String VERIFY_ADD_BENEFICIARY = BASE_URL + "SKDMR/AddBeneAfterVerify";
    String DELETE_BENE_URL = BASE_URL + "SKDMR/DeleteBene";
    String SENDER_HISTORY = BASE_URL + "SKDMR/SenderHistory";
    String REFUND_TRANS_STATUS = BASE_URL + "SKDMR/TransStatus";
    String REFUND_TRANSACTION = BASE_URL + "SKDMR/RefundTransaction";
    String CONFIRM_REFUND_TRANSACTION = BASE_URL + "SKDMR/RefundDMRConfirm";
    String IMPS_NEFT_TRANSACTION = BASE_URL + "SKDMR/InitiateTransaction";
    String BankDetails = BASE_URL + "SKDMR/BankDetails";

  //---------------------dmr2-------------------------
  String FIND_SENDER_Dmr2      = BASE_URL + "AEDMR/EFindSender";
  String SENDER_RESEND_OTP_Dmr2 = BASE_URL + "AEDMR/EReSendOTPRegisterSender";
  String GET_BANK_LIST_Dmr2 = BASE_URL + "AEDMR/EGetBankList";
  String VERIFY_ADD_BENEFICIARY_Dmr2 = BASE_URL + "AEDMR/EAddBeneAfterVerify";
  String DELETE_BENE_URL_Dmr2 = BASE_URL + "AEDMR/EDeleteBene";
  String IMPS_NEFT_TRANSACTION_Dmr2 = BASE_URL + "AEDMR/EInitiateTransaction";
  String REFUND_TRANS_STATUS_Dmr2 = BASE_URL + "AEDMR/ETransStatus";
  String REFUND_TRANSACTION_Dmr2 = BASE_URL + "AEDMR/ERefundTransaction";
  String CONFIRM_REFUND_TRANSACTION_Dmr2 = BASE_URL + "AEDMR/ERefundDMRConfirm";
  String SENDER_HISTORY_Dmr2 = BASE_URL + "AEDMR/ESenderHistory";
  String SENDER_RESISTRATION_Dmr2 = BASE_URL + "AEDMR/ERegisterSender";
  String CONFIRMATION_SENDER_RESISTRATION_Dmr2 = BASE_URL + "AEDMR/EVerifySender";
  String BankDetails_Dmr2 = BASE_URL + "AEDMR/EBankDetails";
  String ADD_BENEFICIARY_IFSC_CODE_Dmr2 = BASE_URL + "AEDMR/EAccountVerifyByBankAccountIFSC";

  String PLAN_VIEW          = BASE_URL + "ezypay/plan/";
  //String PLAN_VIEW          = "http://smartkinda.com/api/resources/ezypay/plan/";


    //AEPS
    String GET_AEPS_ACCESS_KEY=BASE_URL + "AEPS/generateKey";
    String EKO_AEPS_CALLBACK_URL="https://msmartpay.in/MRA1.0/resources/AEPS/hook";
    String LOGO = "https://msmartpay.in/agent/images/ARPIT%20ENTERPRISES/M%20smart%20Pay.PNG";
    String environment = "production";
    String AS_RequestOTP = BASE_URL + "AEPS/RequestOTP";
    String AS_VerifyMobile = BASE_URL + "AEPS/VerifyMobile";
    String AS_UserOnboard = BASE_URL + "AEPS/UserOnboard";


    String LOCATION = BASE_URL + "location/save";


}
