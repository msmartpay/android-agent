package com.aepssdkssz.network;

import android.content.Context;

import com.aepssdkssz.util.Constants;
import com.aepssdkssz.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SSZAePSAuthenticationInterceptor implements Interceptor {
    private Context context;

    SSZAePSAuthenticationInterceptor(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder()
                .header("partner_code", Utility.getData(context, "partner_code"))
                .header("developer_key", Utility.getData(context, "developer_key"))
                .header("secret_key", Utility.getData(context, "secret_key"))
                .header("secret_key_timestamp", Utility.getData(context, "secret_key_timestamp"))
                .header("initiator_id", Utility.getData(context, "initiator_id"))
                .header("Authorization",
                        Credentials.basic(Utility.getData(context, Constants.MERCHANT_ID), Utility.getData(context, Constants.TOKEN)))
                .header("AuthId", Utility.getData(context, Constants.MERCHANT_ID))
                .header("AuthPassword", Utility.getData(context, Constants.TOKEN));;


        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }

}