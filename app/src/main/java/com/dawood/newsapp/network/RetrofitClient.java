package com.dawood.newsapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final File cacheDir = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
    private static final Cache cache = new Cache(cacheDir, 1024);
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    private static Retrofit retrofit = null;
    private static ApiServices apiServices = null;
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static final OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder().
            connectTimeout(20, TimeUnit.SECONDS).
            cache(cache).
            retryOnConnectionFailure(true).
            connectionPool(new ConnectionPool(15, 500000, TimeUnit.MILLISECONDS)).
            readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging);


    public static ApiServices getInterfaceInstance(String BASE_URL) {
        if (apiServices == null) {
            apiServices = RetrofitClient.getClient(BASE_URL).create(ApiServices.class);
            return apiServices;
        } else {
            return apiServices;
        }
    }


    private static Retrofit getClient(String BASE_URL) {
        logging.level(HttpLoggingInterceptor.Level.BASIC);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}