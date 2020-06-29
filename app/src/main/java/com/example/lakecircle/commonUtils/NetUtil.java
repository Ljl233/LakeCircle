package com.example.lakecircle.commonUtils;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetUtil {

    private final RetrofitApi api;
    private final Gson gson;
    private final OkHttpClient client;

    private NetUtil() {
        gson = new Gson();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC);

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new AddTokenInterceptor("pinpin.muxixyz.com"))
                .build();

        api = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://pinpin.muxixyz.com/api/v1/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitApi.class);
    }

    public static NetUtil getInstance() {
        return NetUtilHolder.INSTANCE;
    }

    private static class NetUtilHolder {
        static NetUtil INSTANCE = new NetUtil();
    }

    public RetrofitApi getApi() {
        return api;
    }
}
