package com.example.lakecircle.commonUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetUtil {

    private final RetrofitApi api;

    private NetUtil() {
        api = new Retrofit.Builder()
                .baseUrl("http://")
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

    private RetrofitApi getApi() {
        return api;
    }
}
