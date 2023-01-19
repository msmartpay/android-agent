package in.msmartpay.agent.network;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.msmartpay.agent.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by
 * anil on 09/05/18.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(BuildConfig.DEBUG?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppMethods.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(new OkHttpClient().newBuilder()
                            .connectTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .addInterceptor(interceptor)
                            //.addInterceptor(new AuthenticationInterceptor(context))
                            .build())
                    .build();
        }
        return retrofit;
    }

    public static AppMethods getClient(Context context){
        return getRetrofit(context).create(AppMethods.class);
    }
}
