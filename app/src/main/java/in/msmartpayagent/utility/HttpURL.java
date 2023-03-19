package in.msmartpayagent.utility;

/**
 * Created by Harendra on 4/29/2017.
 */

public interface HttpURL {
    int MY_SOCKET_TIMEOUT_MS             = 60 * 1000;
    String DSID                          = "DS3001";
    String CLIENT = "SRK MONEY";

    String VERSION = "1.0";

    String Domain ="https://srkmoney.in/";
    String BASE_URL                          =  Domain+"MRA1.0/resources/";

    String PLAN_VIEW          = BASE_URL + "APIResponse/plan/";

    String FORGET_PASSWORD               = BASE_URL + "Login/ForgetPass";
    String LOGIN_URL                     = BASE_URL + "Login/Login";
    String CHANGE_PASSWORD               = BASE_URL + "Login/ChangePass";

    String NEW_MOBILE_REGISTER           = BASE_URL + "Login/mobileValidation";
    String OTP_MOBILE_REGISTER           = BASE_URL + "Login/OtpValidation";
    String REGISTER_URL                  = BASE_URL + "Login/SKRegister";
    String PROFILE_URL                   = BASE_URL + "Login/getProfile";
    String PROFILE_UPDATE_URL            = BASE_URL + "Login/getUpdateProfile";
    String STATE_URL                     = BASE_URL + "Login/getState";
    String DISTRICT_BY_STATE_URL         = BASE_URL + "Login/getStateDistrict";
    String KYC_UPDATE_URL                = BASE_URL + "Login/getRequestKyc";
    String KYC_DETAILS_URL               = BASE_URL + "Login/getKycDetails";

    String OPERATOR_CODE_URL             = BASE_URL + "POST/billpayOperator";
    String RECHARGE_URL                  = BASE_URL + "PRE/Recharge";
    //URL USE FOR --Dth_Recharge,DataCard_Recharge,PrepaidMobileActivity
    String BILL_PAY                      = BASE_URL + "POST/Billpay";
    //URL USE FOR --WaterPayment,GasPayment,InsurancePayment,LandlinePayment,PostpaidMobile,ElectricityPayment,

    String SEARCH_TRAIN                  = BASE_URL + "TH/SearchTran";
    String STATEMENT_TRAIN               = BASE_URL + "TH/TranHistory";
    String TranHistoryByDate               = BASE_URL + "TH/TranHistoryByDate";

    String WALLET_BASE                   = "WS/";
    String TURN_OVER                     = BASE_URL + WALLET_BASE + "TurnOver";

    String TICKET_URL                    = BASE_URL + WALLET_BASE + "TicketT";
    String WALLET_BELL_REQ               = BASE_URL + WALLET_BASE + "WalletBalRequest";
    String WALLET_TRANSFER               = BASE_URL + WALLET_BASE + "WalletTransfer";
    String WALLET_BELL_REQ_DETAIL        = BASE_URL + WALLET_BASE + "WalletBalReqDetails";
    String TICKET_DETAILS                = BASE_URL + WALLET_BASE + "TicketDetails";
    String WALLET_BALANCE                = BASE_URL + WALLET_BASE + "WalletBalance";
    String UPDATE_DETAILS                = BASE_URL + WALLET_BASE + "UpdateDetails";

    String CollectBankDetails = BASE_URL+ WALLET_BASE +"CollectBankDetails";
    String MY_COMMISSION = BASE_URL + WALLET_BASE + "MyCommission";

    String ORDER_HISTORY                 = BASE_URL + "TH/OrderHistory";
    String ORDER_HISTORY_BY_ORDER_ID     = BASE_URL + "TH/OrderDetailById";

    //---------------------dmr1--------------------------

    String FIND_SENDER_Dmr2      = BASE_URL + "EKODMR/EFindSender";
    String SENDER_RESEND_OTP_Dmr2 = BASE_URL + "EKODMR/EReSendOTPRegisterSender";
    String GET_BANK_LIST_Dmr2 = BASE_URL + "EKODMR/EGetBankList";
    String VERIFY_ADD_BENEFICIARY_Dmr2 = BASE_URL + "EKODMR/EAddBeneAfterVerify";
    String DELETE_BENE_URL_Dmr2 = BASE_URL + "EKODMR/EDeleteBene";
    String IMPS_NEFT_TRANSACTION_Dmr2 = BASE_URL + "EKODMR/EInitiateTransaction";
    String REFUND_TRANS_STATUS_Dmr2 = BASE_URL + "EKODMR/ETransStatus";
    String REFUND_TRANSACTION_Dmr2 = BASE_URL + "EKODMR/ERefundTransaction";
    String CONFIRM_REFUND_TRANSACTION_Dmr2 = BASE_URL + "EKODMR/ERefundDMRConfirm";
    String SENDER_HISTORY_Dmr2 = BASE_URL + "EKODMR/ESenderHistory";
    String SENDER_RESISTRATION_Dmr2 = BASE_URL + "EKODMR/ERegisterSender";
    String CONFIRMATION_SENDER_RESISTRATION_Dmr2 = BASE_URL + "EKODMR/EVerifySender";
    String BankDetails_Dmr2 = BASE_URL + "EKODMR/EBankDetails";
    String ADD_BENEFICIARY_IFSC_CODE_Dmr2 = BASE_URL + "EKODMR/EAccountVerifyByBankAccountIFSC";
    String KYC_URL = Domain+"agent/EkycMobileApp.jsp?";

    //---------------------dmr1--------------------------
    String FIND_SENDER      = BASE_URL + "SSZDMR/FindSender";
    String SENDER_RESISTRATION = BASE_URL + "SSZDMR/RegisterSender";
    String CONFIRMATION_SENDER_RESISTRATION = BASE_URL + "SSZDMR/VerifySender";
    String SENDER_RESEND_OTP = BASE_URL + "SSZDMR/ReSendOTPRegisterSender";
    String GET_BANK_LIST = BASE_URL + "SSZDMR/GetBankList";
    String VERIFY_BENEFICIARY_IFSC_CODE = BASE_URL + "SSZDMR/AccountVerifyByBankAccountIFSC";
    String VERIFY_ADD_BENEFICIARY = BASE_URL + "SSZDMR/AddBeneAfterVerify";
    String DELETE_BENE_URL = BASE_URL + "SSZDMR/DeleteBene";
    String SENDER_HISTORY = BASE_URL + "SSZDMR/SenderHistory";
    String REFUND_TRANS_STATUS = BASE_URL + "SSZDMR/TransStatus";
    String REFUND_TRANSACTION = BASE_URL + "SSZDMR/RefundTransaction";
    String CONFIRM_REFUND_TRANSACTION = BASE_URL + "SSZDMR/RefundDMRConfirm";
    String IMPS_NEFT_TRANSACTION = BASE_URL + "SSZDMR/InitiateTransaction";
    String BankDetails = BASE_URL + "SSZDMR/BankDetails";

  //AEPS
  String GET_AEPS_ACCESS_KEY=BASE_URL+"APIResponse/generateKey";

  String environment = "production";

  //MPOS
  String GET_MPOS_ACCESS_KEY=BASE_URL+"APIResponse/fetchBCDetails";
  String GET_MPOS_SETTLEMENT_DETAILS=BASE_URL+"mpos/settlement-details";
  String MPOS_SETTLEMENT_REQUEST=BASE_URL+"mpos/settlement-request";
  String KYC_BASE_URL = "https://srkmoney.in/FileServer";
}
