package com.udemy.paggination.app.api;

import androidx.annotation.NonNull;

import com.udemy.paggination.app.utils.Constants;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * this class acts as a central configuration point for defining
 * how HTTP requests and responses should be handled
 */
public class APIClient {

    static APIInterface apiInterface;

    public static APIInterface getApiInterface() {
        if (apiInterface == null) {
            OkHttpClient.Builder client = getBuilder();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();

            apiInterface = retrofit.create(APIInterface.class);
        }
        return apiInterface;
    }

    @NonNull
    private static OkHttpClient.Builder getBuilder() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.addInterceptor(chain -> {
            Request request = chain.request();
            HttpUrl httpUrl = request.url();
            HttpUrl url = httpUrl
                    .newBuilder()
                    .addQueryParameter("api_key", Constants.API_KEY)
                    .build();

            Request.Builder builder = request.newBuilder().url(url);
            Request newRequest = builder.build();

            return chain.proceed(newRequest);
        });
        return client;
    }
}
