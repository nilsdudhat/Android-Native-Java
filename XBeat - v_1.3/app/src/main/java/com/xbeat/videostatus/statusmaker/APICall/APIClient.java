package com.xbeat.videostatus.statusmaker.APICall;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.HttpHost;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final int DISK_CACHE_SIZE = 52428800;
    public static Context context;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder().baseUrl("https://api.superherowall.in/api/v3/").addConverterFactory(GsonConverterFactory.create()).client(new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()).build();
        return retrofit;
    }

    private static OkHttpClient getOKHttpClient() {
        if (okHttpClient == null) {
            Cache cache = null;
            try {
                cache = new Cache(new File(context.getCacheDir(), HttpHost.DEFAULT_SCHEME_NAME), 52428800);
            } catch (Exception e) {
                e.printStackTrace();
            }
            OkHttpClient.Builder addInterceptor = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).writeTimeout(2, TimeUnit.MINUTES).cache(cache).addInterceptor(provideHttpLoggingInterceptor()).addInterceptor(new Interceptor() {
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request request = chain.request();
                    return chain.proceed(chain.request().newBuilder().header("Authorization", "aWpVLKDhhSXJlszc25aWXlRMmFrQjhUBKdsgeYoNpdgnpEdzU36KsOE4NnJMjsgyShdfnYWVFZx2GWhxbZk5c34d06eb3a").method(request.method(), request.body()).build());
                }
            });
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            addInterceptor.addInterceptor(httpLoggingInterceptor);
            okHttpClient = addInterceptor.build();
        }
        return okHttpClient;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            public void log(String str) {
                Log.d("Log", str);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
