package com.example.lakecircle.commonUtils;

import com.example.lakecircle.ui.home.light.model.LakeRankBean;
import com.example.lakecircle.ui.home.light.model.UserRankBean;
import com.example.lakecircle.ui.login.login.model.LoginResponse;
import com.example.lakecircle.ui.login.login.model.UserBean;
import com.example.lakecircle.ui.login.logup.model.LogupRequireBody;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("http://pinpin.muxixyz.com/api/v1/login")
    Observable<LoginResponse> login(@Body UserBean user);

    @POST("http://pinpin.muxixyz.com/api/v1/register")
    Observable<String> logup(@Body LogupRequireBody body);


    //点亮河湖排行榜
    @GET("http://pinpin.muxixyz.com/api/v1/rank/user/")
    Observable<BaseResponseModel<UserRankBean>> getUserRank();

    @GET("http://pinpin.muxixyz.com/api/v1/rank/lake/")
    Observable<BaseResponseModel<LakeRankBean>> getLakeRank();
}
