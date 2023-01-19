package com.aepssdkssz.network;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by
 * anil on 09/05/18.
 */

public class SSZAePSRetrofitClient {

    private static Retrofit retrofit = null;

   /* private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
  */
   private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SSZAePSAppMethods.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(new OkHttpClient().newBuilder()
                            .connectTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .addInterceptor(interceptor)
                            .addInterceptor(new SSZAePSAuthenticationInterceptor(context))
                            .build())
                    .build();
        }
        return retrofit;
    }

    public static SSZAePSAppMethods getClient(Context context) {
        return getRetrofit(context).create(SSZAePSAppMethods.class);
    }
}
