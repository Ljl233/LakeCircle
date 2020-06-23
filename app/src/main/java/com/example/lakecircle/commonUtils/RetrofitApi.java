package com.example.lakecircle.commonUtils;

import com.example.lakecircle.ui.login.login.model.LoginResponse;
import com.example.lakecircle.ui.login.login.model.UserBean;
import com.example.lakecircle.ui.login.logup.model.LogupRequireBody;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("http://pinpin.muxixyz.com/api/v1/login")
    Observable<LoginResponse> login(@Body UserBean user);

    @POST("http://pinpin.muxixyz.com/api/v1/register")
    Observable<String> logup(@Body LogupRequireBody body);
}
