package com.aepssdkssz.network;

import com.aepssdkssz.network.model.DeviceUpdateRequest;
import com.aepssdkssz.network.model.MainResponse;
import com.aepssdkssz.network.model.ValidateUserRequest;
import com.aepssdkssz.network.model.ValidateUserResponse;
import com.aepssdkssz.network.model.aepstransaction.AepsRequest;
import com.aepssdkssz.network.model.aepstransaction.AepsResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayOnboardResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayOnboardUserRequest;
import com.aepssdkssz.network.model.fingpayonboard.FingpayStateResponse;
import com.aepssdkssz.network.model.fingpayonboard.FingpayUserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SSZAePSAppMethods {
    String VERSION = "1.8";
    String DOMAIN = "https://srkmoney.in/";
    String BASE_URL = DOMAIN + "MRA"+VERSION+"/resources/";


    //EKO AEPS
    @POST("aeps/validate-aeps-user")
    Call<ValidateUserResponse> validateUser(@Body ValidateUserRequest request);

    @POST("aeps/aeps-callback")
    Call<AepsResponse> transaction(@Body AepsRequest request);

    @POST("aeps/ipay-aeps/update-device")
    Call<MainResponse> updateDevice(@Body DeviceUpdateRequest request);

    //FINGPAY AEPS
    @POST("fingpay/validate-aeps-user")
    Call<ValidateUserResponse> validateFingpayUser(@Body ValidateUserRequest request);

    @POST("fingpay/fetch-banks")
    Call<ValidateUserResponse> fingpayFetchBank(@Body ValidateUserRequest request);

    @POST("fingpay/aeps-callback")
    Call<AepsResponse> fingpayAepsTransaction(@Body AepsRequest request);

    @POST("fingpay/user/kyc/sentotp")
    Call<FingpayOnboardResponse> fingpayUserRequestOTP(@Body FingpayUserRequest request);

    @POST("fingpay/user/kyc/resendotp")
    Call<FingpayOnboardResponse> fingpayUserResentOTP(@Body FingpayUserRequest request);

    @POST("fingpay/user/kyc/validateotp")
    Call<FingpayOnboardResponse> fingpayUserVerifyOTP(@Body FingpayUserRequest request);

    @POST("fingpay/user/kyc/biometric")
    Call<FingpayOnboardResponse> fingpayBiometricKyc(@Body FingpayUserRequest request);

    @POST("fingpay/user/onboard")
    Call<FingpayOnboardResponse> fingpayUserOnboard(@Body FingpayOnboardUserRequest request);

    @POST("fingpay/user/onboard/state")
    Call<FingpayStateResponse> getFingpayState(@Body FingpayUserRequest request);
}
