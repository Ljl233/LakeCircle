package com.example.lakecircle.commonUtils;

import com.example.lakecircle.ui.login.login.model.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("")
    Observable<LoginResponse> login(String account, String psw);
}
