package in.msmartpayagent.network;

import android.content.Context;

import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private Context context;

    AuthenticationInterceptor(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder()
                .header("Authorization",
                        Credentials.basic(Util.LoadPrefData(context, Keys.AGENT_ID), Util.LoadPrefData(context, Keys.TXN_KEY)));
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }

}