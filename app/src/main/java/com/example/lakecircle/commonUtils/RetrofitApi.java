package com.example.lakecircle.commonUtils;

import com.example.lakecircle.ui.login.login.model.LoginResponse;
import com.example.lakecircle.ui.login.logup.model.LogupRequireBody;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("/login")
    Observable<LoginResponse> login(@Field("password")String psw,@Field("username") String account );

    @POST("/register")
    Observable<String> logup(@FieldMap LogupRequireBody body);
}
