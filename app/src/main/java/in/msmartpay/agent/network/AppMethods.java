package in.msmartpay.agent.network;

import com.aepssdkssz.network.model.ValidateUserRequest;
import com.aepssdkssz.network.model.ValidateUserResponse;

import java.util.List;
import java.util.Map;

import in.msmartpay.agent.network.model.BankCollectResponse;
import in.msmartpay.agent.network.model.BillPayRequest;
import in.msmartpay.agent.network.model.BillPayResponse;
import in.msmartpay.agent.network.model.CreditOtpResponseContainer;
import in.msmartpay.agent.network.model.CreditRefundOtpRequest;
import in.msmartpay.agent.network.model.CreditRefundRequest;
import in.msmartpay.agent.network.model.MainRequest;
import in.msmartpay.agent.network.model.MainRequest2;
import in.msmartpay.agent.network.model.MainResponse;
import in.msmartpay.agent.network.model.MainResponse2;
import in.msmartpay.agent.network.model.OperatorsRequest;
import in.msmartpay.agent.network.model.OperatorsResponse;
import in.msmartpay.agent.network.model.OtpRequest;
import in.msmartpay.agent.network.model.PlanRequest;
import in.msmartpay.agent.network.model.PlanResponse;
import in.msmartpay.agent.network.model.RechargeRequest;
import in.msmartpay.agent.network.model.aeps.AddFundSettlementBankRequest;
import in.msmartpay.agent.network.model.aeps.AepsAccessKeyRequest;
import in.msmartpay.agent.network.model.aeps.AepsAccessKeyResponse;
import in.msmartpay.agent.network.model.aeps.FundSettlementDetailsRequest;
import in.msmartpay.agent.network.model.aeps.FundSettlementDetailsResponse;
import in.msmartpay.agent.network.model.aeps.FundSettlementRequest;
import in.msmartpay.agent.network.model.aeps.PayworldAccessKeyRequest;
import in.msmartpay.agent.network.model.aeps.PayworldAccessKeyResponse;
import in.msmartpay.agent.network.model.aeps.onboard.UserRegisterRequest;
import in.msmartpay.agent.network.model.aeps.onboard.UserRequest;
import in.msmartpay.agent.network.model.commission.CommissionRequest;
import in.msmartpay.agent.network.model.commission.CommissionResponse;
import in.msmartpay.agent.network.model.dmr.AccountVerifyRequest;
import in.msmartpay.agent.network.model.dmr.AccountVerifyResponse;
import in.msmartpay.agent.network.model.dmr.AddBeneficiaryRequest;
import in.msmartpay.agent.network.model.dmr.AddBeneficiaryResponse;
import in.msmartpay.agent.network.model.dmr.BankDetailsDmrResponse;
import in.msmartpay.agent.network.model.dmr.BankListDmrResponse;
import in.msmartpay.agent.network.model.dmr.BankRequest;
import in.msmartpay.agent.network.model.dmr.DeleteBeneRequest;
import in.msmartpay.agent.network.model.dmr.MoneyTransferRequest;
import in.msmartpay.agent.network.model.dmr.MoneyTransferResponse;
import in.msmartpay.agent.network.model.dmr.RefundLiveStatusRequest;
import in.msmartpay.agent.network.model.dmr.RefundLiveStatusResponse;
import in.msmartpay.agent.network.model.dmr.SenderDetailsResponse;
import in.msmartpay.agent.network.model.dmr.SenderFindRequest;
import in.msmartpay.agent.network.model.dmr.SenderHistoryResponse;
import in.msmartpay.agent.network.model.dmr.SenderRegisterRequest;
import in.msmartpay.agent.network.model.dmr.SenderRegisterResponse;
import in.msmartpay.agent.network.model.dmr.ps.PSMoneyTransferResponse;
import in.msmartpay.agent.network.model.dmr.ps.PSSenderRegisterRequest;
import in.msmartpay.agent.network.model.ekobbps.OperatorResponse;
import in.msmartpay.agent.network.model.ekobbps.BillPayMainRequest;
import in.msmartpay.agent.network.model.ekobbps.FetchBillRequest;
import in.msmartpay.agent.network.model.ekobbps.FetchBillResponse;
import in.msmartpay.agent.network.model.ekobbps.OperatorCategoryResponse;
import in.msmartpay.agent.network.model.ekobbps.OperatorLocationResponse;
import in.msmartpay.agent.network.model.ekobbps.OperatorParametersResponse;
import in.msmartpay.agent.network.model.ekobbps.PayBillRequest;
import in.msmartpay.agent.network.model.ekobbps.PayBillResponse;
import in.msmartpay.agent.network.model.fastag.FastagFetchRequest;
import in.msmartpay.agent.network.model.fastag.FastagFetchResponse;
import in.msmartpay.agent.network.model.fastag.FastagOperatorResponse;
import in.msmartpay.agent.network.model.fastag.FastagRechargeRequest;
import in.msmartpay.agent.network.model.fastag.FastagRechargeResponse;
import in.msmartpay.agent.network.model.kyc.*;
import in.msmartpay.agent.network.model.lic.LicBillFetchRequest;
import in.msmartpay.agent.network.model.lic.LicBillFetchResponse;
import in.msmartpay.agent.network.model.lic.LicBillpayResponse;
import in.msmartpay.agent.network.model.matm.MicroInitiateTransactionData;
import in.msmartpay.agent.network.model.matm.MicroInitiateTransactionRequest;
import in.msmartpay.agent.network.model.matm.MicroInitiateTransactionResponse;
import in.msmartpay.agent.network.model.matm.MicroPostTransactionRequest;
import in.msmartpay.agent.network.model.matm.MicroPostTransactionResponse;
import in.msmartpay.agent.network.model.order.OrderProductDetailsResponse;
import in.msmartpay.agent.network.model.order.OrderProductHistoryResponse;
import in.msmartpay.agent.network.model.order.OrderProductListResponse;
import in.msmartpay.agent.network.model.order.OrderProductsRequest;
import in.msmartpay.agent.network.model.post.EMIOperatorResponse;
import in.msmartpay.agent.network.model.setting.UpdateTPINRequest;
import in.msmartpay.agent.network.model.setting.UpdateTPINStatusRequest;
import in.msmartpay.agent.network.model.user.ChangePasswordRequest;
import in.msmartpay.agent.network.model.user.DistrictRequest;
import in.msmartpay.agent.network.model.user.DistrictResponse;
import in.msmartpay.agent.network.model.user.ForgotPasswordRequest;
import in.msmartpay.agent.network.model.user.KycStatusResponse;
import in.msmartpay.agent.network.model.user.KycUpdateRequest;
import in.msmartpay.agent.network.model.user.LoginRequest;
import in.msmartpay.agent.network.model.user.LoginResponse;
import in.msmartpay.agent.network.model.user.MobileValidationRequest;
import in.msmartpay.agent.network.model.user.ProfileResponse;
import in.msmartpay.agent.network.model.user.ProfileUpdateRequest;
import in.msmartpay.agent.network.model.user.RegisterRequest;
import in.msmartpay.agent.network.model.user.StateResponse;
import in.msmartpay.agent.network.model.wallet.BalHistoryResponse;
import in.msmartpay.agent.network.model.wallet.BalRequest;
import in.msmartpay.agent.network.model.wallet.BalanceRequest;
import in.msmartpay.agent.network.model.wallet.BalanceResponse;
import in.msmartpay.agent.network.model.wallet.MyEarningRequest;
import in.msmartpay.agent.network.model.wallet.MyEarningResponse;
import in.msmartpay.agent.network.model.wallet.TicketRequest;
import in.msmartpay.agent.network.model.wallet.WalletHistoryRequest;
import in.msmartpay.agent.network.model.wallet.WalletHistoryResponse;
import in.msmartpay.agent.network.model.wallet.WalletSearchHistoryRequest;
import in.msmartpay.agent.network.model.wallet.WalletTransferRequest;
import in.msmartpay.agent.network.model.wallet.complaints.ComplaintHistoryRequest;
import in.msmartpay.agent.network.model.wallet.complaints.ComplaintsResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface AppMethods {
    String VERSION = "1.3";
    String DOMAIN = "https://msmartpay.in/";
    String BASE_URL_NEW=DOMAIN+"agentapi/";

    String BASE_URL = DOMAIN + "ArpitAgentApi"+VERSION+"/resources/";
    String KYC_BASE_URL = DOMAIN+"FileServer";

    String DSID = "DSUP33273001";
    String CLIENT = "";

    String environment = "production";

    String DMR = "EKODMR2.0";
    String LOGIN = "Login";
    String WS = "WS";
    String TH = "TH";


    String DMR_PAYSPRINT="Paysprint";

    String FindSender="/FindSender";
    String DeleteBene="/DeleteBene";
    String RegisterSender="/RegisterSender";
    String VerifySender="/VerifySender";
    String BankDetails="/BankDetails";
    String GetBankList="/GetBankList";
    String SenderHistory="/SenderHistory";
    String AddBeneAfterVerify="/AddBeneAfterVerify";
    String AccountVerifyByBankAccountIFSC="/AccountVerifyByBankAccountIFSC";
    String InitiateTransaction="/InitiateTransaction";
    String TransStatus="/TransStatus";
    String RefundTransaction="/RefundTransaction";
    String RefundDMRConfirm="/RefundDMRConfirm";

    String SETTING = "setting";

    @POST(SETTING + "/tpin/otp/resend")
    Call<MainResponse2> tpinOtpResend(@Body UpdateTPINRequest request);

    @POST(SETTING + "/tpin/status")
    Call<MainResponse2> updateTPINStatus(@Body UpdateTPINStatusRequest request);

    @POST(SETTING + "/tpin/update")
    Call<MainResponse2> updateTPIN(@Body UpdateTPINRequest request);

    /******* DMT *******/

    @POST
    Call<SenderDetailsResponse> findSenderDetails(@Url String url, @Body SenderFindRequest request);

    @POST
    Call<SenderRegisterResponse> registerSender(@Url String url,@Body PSSenderRegisterRequest request);

    @POST
    Call<SenderRegisterResponse> registerSenderConfirm(@Url String url,@Body PSSenderRegisterRequest request);

    @POST
    Call<BankListDmrResponse> getBankListDmr(@Url String url,@Body BankRequest request);

    @POST
    Call<BankDetailsDmrResponse> getBankDetailsDmr(@Url String url,@Body BankRequest request);

    @POST
    Call<AddBeneficiaryResponse> addBeneficiary(@Url String url,@Body AddBeneficiaryRequest request);

    @POST
    Call<MainResponse2> deleteBeneficiary(@Url String url, @Body DeleteBeneRequest request);

    @POST
    Call<SenderHistoryResponse> senderHistory(@Url String url,@Body SenderFindRequest request);

    @POST
    Call<AccountVerifyResponse> verifyAccount(@Url String url,@Body AccountVerifyRequest request);

    @POST
    Call<PSMoneyTransferResponse> moneyTransfer(@Url String url, @Body MoneyTransferRequest request);

    @POST
    Call<RefundLiveStatusResponse> transStatus(@Url String url,@Body RefundLiveStatusRequest request);

    @POST
    Call<MainResponse2> refundTransaction(@Url String url,@Body RefundLiveStatusRequest request);

    @POST
    Call<MainResponse2> refundTransactionConfirm(@Url String url,@Body RefundLiveStatusRequest request);


    @POST(LOGIN + "/Login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST(LOGIN + "/ForgetPass")
    Call<MainResponse> forgetPass(@Body ForgotPasswordRequest request);

    @POST(LOGIN + "/mobileValidation")
    Call<MainResponse2> mobileValidation(@Body MobileValidationRequest request);

    @POST(LOGIN + "/OtpValidation")
    Call<MainResponse2> otpValidation(@Body MobileValidationRequest request);

    @POST(LOGIN + "/signup")
    Call<MainResponse2> register(@Body RegisterRequest request);

    @POST(LOGIN + "/ChangePass")
    Call<MainResponse> changePass(@Body ChangePasswordRequest request);

    @POST(LOGIN + "/getProfile")
    Call<ProfileResponse> getProfile(@Body MainRequest request);

    @POST(LOGIN + "/getUpdateProfile")
    Call<ProfileResponse> getUpdateProfile(@Body ProfileUpdateRequest request);

    @POST(LOGIN + "/getState")
    Call<StateResponse> getState(@Body MainRequest request);

    @POST(LOGIN + "/getStateDistrict")
    Call<DistrictResponse> getStateDistrict(@Body DistrictRequest request);

    @POST(LOGIN + "/getKycDetails")
    Call<KycStatusResponse> getKycDetails(@Body MainRequest request);

    @POST(LOGIN + "/getRequestKyc")
    Call<KycStatusResponse> updateKyc(@Body KycUpdateRequest request);

    @POST("APIResponse/plan/")
    Call<PlanResponse> getPlans(@Body PlanRequest request);

    @POST("POST/billpayOperator")
    Call<OperatorsResponse> operators(@Body OperatorsRequest request);

    @POST("POST/billpayOperatorStatus")
    Call<EMIOperatorResponse> getOperatorDetails(@Body OperatorsRequest request);

    @POST("PRE/Recharge")
    Call<MainResponse> recharge(@Body RechargeRequest request);

    @POST("POST/Billpay")
    Call<BillPayResponse> billPay(@Body BillPayRequest request);

    /******* Wallet *******/
    @POST(WS + "/WalletBalance")
    Call<BalanceResponse> getWalletBalance(@Body BalanceRequest request);

    @POST(WS + "/CollectBankDetails")
    Call<BankCollectResponse> getBankDetails(@Body MainRequest request);

    @POST(WS + "/MyCommission")
    Call<CommissionResponse> getCommissions(@Body CommissionRequest request);

    @POST(WS + "/WalletBalRequest")
    Call<MainResponse> getWalletBalReq(@Body BalRequest request);

    @POST(WS + "/WalletBalReqDetails")
    Call<BalHistoryResponse> getWalletBalReqHistory(@Body MainRequest request);

    @POST(WS + "/TurnOver")
    Call<MyEarningResponse> getMyEarning(@Body MyEarningRequest request);

    @POST(WS + "/validateWalletId")
    Call<MainResponse2> validateWalletId(@Body WalletTransferRequest request);

    @POST(WS + "/WalletTransfer")
    Call<MainResponse2> walletTransfer(@Body WalletTransferRequest request);

    @POST(WS + "/TicketT")
    Call<MainResponse2> getTicketRequest(@Body TicketRequest request);

    @POST(WS + "/complaints")
    Call<ComplaintsResponse> getComplaints(@Body ComplaintHistoryRequest request);


    @POST(TH + "/SearchTran")
    Call<WalletHistoryResponse> searchWalletHistory(@Body WalletSearchHistoryRequest request);

    @POST(TH + "/TranHistory")
    Call<WalletHistoryResponse> getWalletHistory(@Body WalletHistoryRequest request);

    @POST(TH + "/TranHistoryByDate")
    Call<WalletHistoryResponse> getWalletHistoryByDate(@Body WalletHistoryRequest request);


    /******* Wallet *******/

    /******* Micro Atm *******/

    @POST("mpos/fetchBCDetails")
    Call<MicroInitiateTransactionResponse> getMicroAtmAccessKey(@Body MicroInitiateTransactionData request);


    /******* End Micro Atm *******/

    /******* AEPS *******/

    //Activate User
    @POST("aeps/RequestOTP")
    Call<MainResponse2> activateRequestOTP(@Body UserRequest request);

    @POST("aeps/VerifyMobile")
    Call<MainResponse2> activateVerifyOTP(@Body UserRequest request);

    @POST("aeps/UserOnboard")
    Call<MainResponse2> activateRegister(@Body UserRegisterRequest request);


    @Multipart
    @PUT("eko/user/service/activate")
    Call<MainResponse2> activateService(@PartMap Map<String, RequestBody> partMap,
                                        @Part List<MultipartBody.Part> files);

    @POST("aeps/generateAccessKey")
    Call<AepsAccessKeyResponse> getAepsAccessKey(@Body AepsAccessKeyRequest request);

    @POST("APIResponse/fetchAePSBCDetails")
    Call<PayworldAccessKeyResponse> generatePayworldAccessKey(@Body PayworldAccessKeyRequest request);

    @POST("aeps/generateSSPLAccessKey")
    Call<AepsAccessKeyResponse> generateSSPLAccessKey(@Body AepsAccessKeyRequest request);

    @POST("aeps/add-bank")
    Call<MainResponse2> addFundSettlementBank(@Body AddFundSettlementBankRequest request);

    @POST("aeps/settlement-details")
    Call<FundSettlementDetailsResponse> getFundSettlementList(@Body FundSettlementDetailsRequest request);

    @POST("aeps/settlement-request")
    Call<MainResponse2> requestFundSettlement(@Body FundSettlementRequest request);
    /******* END AEPS *******/

    /******* DMT *******/

    @POST(DMR + "/FindSender")
    Call<SenderDetailsResponse> findSenderDetails(@Body SenderFindRequest request);

    @POST(DMR + "/RegisterSender")
    Call<SenderRegisterResponse> registerSender(@Body SenderRegisterRequest request);

    @POST(DMR + "/VerifySender")
    Call<SenderRegisterResponse> registerSenderConfirm(@Body SenderRegisterRequest request);

    @POST(DMR + "/GetBankList")
    Call<BankListDmrResponse> getBankListDmr(@Body BankRequest request);

    @POST(DMR + "/BankDetails")
    Call<BankDetailsDmrResponse> getBankDetailsDmr(@Body BankRequest request);

    @POST(DMR + "/AddBeneAfterVerify")
    Call<AddBeneficiaryResponse> addBeneficiary(@Body AddBeneficiaryRequest request);

    @POST(DMR + "/DeleteBene")
    Call<MainResponse2> deleteBeneficiary(@Body DeleteBeneRequest request);

    @POST(DMR + "/SenderHistory")
    Call<SenderHistoryResponse> senderHistory(@Body SenderFindRequest request);

    @POST(DMR + "/ClaimHistory")
    Call<SenderHistoryResponse> claimHistory(@Body SenderFindRequest request);

    @POST(DMR + "/AccountVerifyByBankAccountIFSC")
    Call<AccountVerifyResponse> verifyAccount(@Body AccountVerifyRequest request);

    @POST(DMR + "/InitiateTransaction")
    Call<MoneyTransferResponse> moneyTransfer(@Body MoneyTransferRequest request);

    @POST(DMR + "/TransStatus")
    Call<RefundLiveStatusResponse> transStatus(@Body RefundLiveStatusRequest request);

    @POST(DMR + "/RefundTransaction")
    Call<MainResponse2> refundTransaction(@Body RefundLiveStatusRequest request);

    @POST(DMR + "/RefundDMRConfirm")
    Call<MainResponse2> refundTransactionConfirm(@Body RefundLiveStatusRequest request);

    /****** Credit card payment   *********/
    @POST(BASE_URL + "Paysprint/ccpayment/generateotp")
    Call<CreditOtpResponseContainer> generateOtp(@Body OtpRequest request);

    @POST(BASE_URL + "Paysprint/ccpayment/paybill")
    Call<MainResponse2> requestCreditPay(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/ccpayment/resendotp")
    Call<MainResponse2> requestRefundOtp(@Body CreditRefundOtpRequest request);

    @POST(BASE_URL + "Paysprint/ccpayment/claimrefund")
    Call<MainResponse2> requestRefund(@Body CreditRefundRequest request);

    @POST(BASE_URL + "Paysprint/paytm/sendotp")
    Call<MainResponse2> getPaytmOtp(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/paytm/verifyotp")
    Call<MainResponse2> verifyPaytmOtp(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/paytm/checkout")
    Call<MainResponse2> getPaytmPay(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/fastag/operatorsList")
    Call<FastagOperatorResponse> getFastagOperator(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/finocms/generate_url")
    Call<MainResponse2> generateFinoCMCUrl(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/axisbank-utm/generateurl")
    Call<MainResponse2> generateAccountOpeningUrl(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/fastag/fetchConsumerDetails")
    Call<FastagFetchResponse> fetchFastagConsumerDetails(@Body FastagFetchRequest request);

    @POST(BASE_URL + "Paysprint/fastag/recharge")
    Call<FastagRechargeResponse> rechargeFastag(@Body FastagRechargeRequest request);

    @POST(BASE_URL + "lic/fetchlicbill")
    Call<LicBillFetchResponse> fetchLicBill(@Body LicBillFetchRequest request);

    @POST(BASE_URL + "lic/paylicbill")
    Call<LicBillpayResponse> payLicBill(@Body MainRequest2 request);

    /*******END DMT *******/

    /*********  MATM ********/

    @POST("matm/fingpay/user/validate")
    Call<ValidateUserResponse> fingpayValidateMatmUser(@Body ValidateUserRequest request);

    @POST("matm/fingpay/initiate")
    Call<MicroInitiateTransactionResponse> initiateFingpayMATMTransaction(@Body MicroInitiateTransactionRequest request);

    @POST("matm/fingpay/transaction/update")
    Call<MicroPostTransactionResponse> postFingpayMATMTransaction(@Body MicroPostTransactionRequest request);


    /********** MATM END ************/

    /****** KYC Documents *********/
    @POST(LOGIN + "/fetchKycDocumentType")
    Call<DocumentTypeResponseModel> fetchDocumentType(@Body BaseRequest baseRequest);

    @Multipart
    @POST(DOMAIN+"FileServer/uploadFile")
    Call<FileUploadResponse> kycUploadFile(@Part List<MultipartBody.Part> files);

    @POST(LOGIN + "/uploadKycDocument")
    Call<DocumentDataResponseContainer> kycUploadDoc(@Body DocumentDataRequestContainer requestContainer);
    @POST(LOGIN + "/fetchKycDocuments")
    Call<DocumentDataResponseContainer> fetchDocumentData(@Body BaseRequest baseRequest);

    @POST(LOGIN + "/updateKYC")
    Call<DocumentTypeResponseModel> updateKYCStatus(@Body BaseRequest baseRequest);

    /***** Eko Bill Pay ****/

    @POST(BASE_URL + "POST/eko/billpayments/operators_category")
    Call<OperatorCategoryResponse> operatorsCategoryRequest(@Body BillPayMainRequest request);

    @POST(BASE_URL + "POST/eko/billpayments/operators_locations")
    Call<OperatorLocationResponse>  operatorsLocationRequest(@Body BillPayMainRequest request);
    @POST(BASE_URL + "POST/eko/billpayments/operators")
    Call<OperatorResponse>  operatorsRequest(@Body BillPayMainRequest request);

    @POST(BASE_URL + "POST/eko/billpayments/operators_parameters")
    Call<OperatorParametersResponse>  operatorsParametersRequest(@Body BillPayMainRequest request);

    @POST(BASE_URL + "POST/eko/billpayments/fetchbill")
    Call<FetchBillResponse>  fetchBillRequest(@Body FetchBillRequest request);

    @POST(BASE_URL + "POST/eko/billpayments/paybill")
    Call<PayBillResponse>  payBillRequest(@Body PayBillRequest request);

    /***** End Eko Bill Pay ****/

    /***** ORDER PRODUCTS ****/
    @POST(BASE_URL + "OrderProduct/FetchProductTypeList")
    Call<OrderProductListResponse>  fetchProductTypeList(@Body MainRequest2 request);
    @POST(BASE_URL + "OrderProduct/FetchProductOrderDetailsList")
    Call<OrderProductHistoryResponse>  fetchProductOrderHistory(@Body MainRequest2 request);
    @POST(BASE_URL + "OrderProduct/FetchProductOrderDetails")
    Call<OrderProductDetailsResponse>  fetchProductOrderDetails(@Body MainRequest2 request);

    @POST(BASE_URL + "OrderProduct/cancelOrder")
    Call<MainResponse2>  cancelOrder(@Body MainRequest2 request);
    @POST(BASE_URL + "OrderProduct/SaveProductOrderDetails")
    Call<MainResponse2>  saveProductOrderDetails(@Body OrderProductsRequest request);
    /***** END ORDER PRODUCTS ****/

}
