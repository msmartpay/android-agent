package com.aepssdkssz.network;

import android.content.Context;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class SSZAePSAPIError {

    private int statusCode;
    private String message;

    public SSZAePSAPIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    public static SSZAePSAPIError parseError(Response<?> response, Context context){
        Converter<ResponseBody, SSZAePSAPIError> converter =
                SSZAePSRetrofitClient.getRetrofit(context)
                        .responseBodyConverter(SSZAePSAPIError.class, new Annotation[0]);

        SSZAePSAPIError error;

        try {
            assert response.errorBody() != null;
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new SSZAePSAPIError();
        }

        return error;
    }
}