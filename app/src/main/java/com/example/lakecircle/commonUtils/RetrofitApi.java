package com.example.lakecircle.commonUtils;

import com.example.lakecircle.ui.circle.Circle;
import com.example.lakecircle.ui.circle.PostCircleBean;
import com.example.lakecircle.ui.home.activities.Activities;
import com.example.lakecircle.ui.home.activities.ActivityApplyBean;
import com.example.lakecircle.ui.home.activities.ActivityInfoResponse;
import com.example.lakecircle.ui.home.journey.lake.LakeIntroBean;
import com.example.lakecircle.ui.home.journey.lake.MilesBean;
import com.example.lakecircle.ui.home.journey.lake.MilesResponse;
import com.example.lakecircle.ui.home.light.model.LakeNameBean;
import com.example.lakecircle.ui.home.light.model.LakeRankBean;
import com.example.lakecircle.ui.home.light.model.LakeUrlResponse;
import com.example.lakecircle.ui.home.light.model.StarResponse;
import com.example.lakecircle.ui.home.light.model.UserInfoBean;
import com.example.lakecircle.ui.home.light.model.UserRankBean;
import com.example.lakecircle.ui.home.merchant.MerchantInfoResponse;
import com.example.lakecircle.ui.home.merchant.MerchantListResponse;
import com.example.lakecircle.ui.home.merchant.MerchantNameBean;
import com.example.lakecircle.ui.home.merchant.detail.coupon.ExchangeCouponBean;
import com.example.lakecircle.ui.home.merchant.detail.coupon.MerchantCouponListFragment;
import com.example.lakecircle.ui.home.merchant.detail.special.MerchantSpecialListFragment;
import com.example.lakecircle.ui.home.merchant.detail.special.SpecialListResponse;
import com.example.lakecircle.ui.home.realtime.RealtimeResponse;
import com.example.lakecircle.ui.home.upimage.QuestionBean;
import com.example.lakecircle.ui.home.upimage.QuestionResponse;
import com.example.lakecircle.ui.home.upimage.UrlResponse;
import com.example.lakecircle.ui.login.login.model.LoginResponse;
import com.example.lakecircle.ui.login.login.model.UserBean;
import com.example.lakecircle.ui.login.logup.model.LogupRequireBody;
import com.example.lakecircle.ui.mine.MineFragment;
import com.example.lakecircle.ui.mine.SimpleResponse;
import com.example.lakecircle.ui.mine.certificate.GovCerPostBean;
import com.example.lakecircle.ui.mine.certificate.MerCerPostBean;
import com.example.lakecircle.ui.mine.coupon.Coupon;
import com.example.lakecircle.ui.mine.govern.activitypost.ActivityPostBean;
import com.example.lakecircle.ui.mine.govern.problemview.ProblemListResponseBean;
import com.example.lakecircle.ui.mine.govern.problemview.ProblemResponse;
import com.example.lakecircle.ui.mine.govern.problemview.SolveProblemPostBean;
import com.example.lakecircle.ui.mine.mer.InfoPostBean;
import com.example.lakecircle.ui.mine.mer.SpecialPostBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {

    @POST("http://pinpin.muxixyz.com/api/v1/login")
    Observable<LoginResponse> login(@Body UserBean user);

    @POST("http://pinpin.muxixyz.com/api/v1/register")
    Observable<String> logup(@Body LogupRequireBody body);


    //点亮河湖
    @GET("http://pinpin.muxixyz.com/api/v1/user/info")
    Observable<UserInfoBean> getUserInfo();

    @POST("http://pinpin.muxixyz.com/api/v1/user/mileage")
    Observable<MilesResponse> addMiles(@Body MilesBean milesBean);

    @POST("http://pinpin.muxixyz.com/api/v1/lake/star")
    Observable<StarResponse> starLake(@Body LakeNameBean lakeNameBean);


    @POST("http://pinpin.muxixyz.com/api/v1/lake/is_star")
    Observable<LakeUrlResponse> getStartInfo(@Body LakeNameBean lakeName);

    //点亮河湖排行榜
    @GET("http://pinpin.muxixyz.com/api/v1/rank/user/")
    Observable<BaseResponseModel<UserRankBean>> getUserRank();

    @GET("http://pinpin.muxixyz.com/api/v1/rank/lake/")
    Observable<BaseResponseModel<LakeRankBean>> getLakeRank();

    //实时河湖
    @GET("http://pinpin.muxixyz.com/api/v1/lake/info/{id}")
    Observable<RealtimeResponse> getLakeInfo(@Path("id") int lakeId);

    //实时河湖搜索
    @GET("http://pinpin.muxixyz.com/api/v1/lake/intro")
    Observable<BaseResponseModel<LakeIntroBean>> searchLakeIntro(@Query(value = "st", encoded = true) String lakename);

    //上传问题
    @POST("http://pinpin.muxixyz.com/api/v1/question/new")
    Observable<QuestionResponse> newQuestion(@Body QuestionBean question);

    //图片上传
    @Multipart
    @POST("http://pinpin.muxixyz.com/api/v1/upload/image")
    Observable<UrlResponse> uploadImage(@Part() MultipartBody.Part file);

    //河湖信息
    @GET("http://pinpin.muxixyz.com/api/v1/lake/intro/list")
    Observable<BaseResponseModel<LakeIntroBean>> getAllLakeIntro();

    //发布活动
    @POST("http://pinpin.muxixyz.com/api/v1/activity")
    Observable<SimpleResponse> postActivity(@Body ActivityPostBean activityPostBean);

    //提交商家认证
    @POST("http://pinpin.muxixyz.com/api/v1/auth/business")
    Observable<SimpleResponse> postMerCer(@Body MerCerPostBean merCerPostBean);

    //提交政府认证
    @POST("http://pinpin.muxixyz.com/api/v1/auth/government")
    Observable<SimpleResponse> postGovCer(@Body GovCerPostBean govCerPostBean);

    //获取已解决的问题列表
    ///todo fix
    @GET("http://pinpin.muxixyz.com/api/v1/question/solved")
    Observable<ProblemListResponseBean> getSolvedProblems(@Query("page") String page, @Query("limit") String limit);

    //获取待解决的问题列表
    ///todo fix
    @GET("http://pinpin.muxixyz.com/api/v1/question/unsolved")
    Observable<ProblemListResponseBean> getUnsolvedProblems(@Query("page") String page, @Query("limit") String limit);

    //获取一个问题的详细信息
    @GET("http://pinpin.muxixyz.com/api/v1/question/info/{id}")
    Observable<ProblemResponse> getProblem(@Path("id") int id);

    //解决一个问题
    @POST("http://pinpin.muxixyz.com/api/v1/question/solve")
    Observable<SimpleResponse> postSolution(@Body SolveProblemPostBean solveProblemPostBean);

    //提交一个新的头像
    @POST("http://pinpin.muxixyz.com/api/v1/user/avatar")
    Observable<SimpleResponse> postAvatar(@Body MineFragment.AvatarPostBean bean);

    @GET("http://pinpin.muxixyz.com/api/v1/coupon/list")
    Observable<BaseResponseModel<Coupon>> getCoupons(@Query("page") String page, @Query("limit") String limit);

    @POST("http://pinpin.muxixyz.com/api/v1/business/info")
    Observable<SimpleResponse> postMerInfo(@Body InfoPostBean infoPostBean);

    @POST("http://pinpin.muxixyz.com/api/v1/business/special")
    Observable<SimpleResponse> postMerSpecial(@Body SpecialPostBean specialPostBean);

    @GET("http://pinpin.muxixyz.com/api/v1/moment/list")
    Observable<BaseResponseModel<Circle>> getCircles(@Query("page") String page, @Query("limit") String limit);

    @POST("http://pinpin.muxixyz.com/api/v1/moment")
    Observable<SimpleResponse> postCircle(@Body PostCircleBean postCircleBean);

    @GET("http://pinpin.muxixyz.com/api/v1/activity/unfinished")
    Observable<BaseResponseModel<Activities>> getUnfinishedActivities(@Query("page") String page, @Query("limit") String limit);

    @GET("http://pinpin.muxixyz.com/api/v1/activity/finished")
    Observable<BaseResponseModel<Activities>> getFinishedActivities(@Query("page") String page, @Query("limit") String limit);

    @GET("http://pinpin.muxixyz.com/api/v1/activity/info/{id}")
    Observable<ActivityInfoResponse> getActivity(@Path("id") int id);

    @POST("http://pinpin.muxixyz.com/api/v1/activity/{id}")
    Observable<SimpleResponse> applyActivity(@Path("id") int id, @Body ActivityApplyBean activityApplyBean);

    @POST("http://pinpin.muxixyz.com/api/v1/coupon")
    Observable<SimpleResponse> exchangeCoupon(@Body ExchangeCouponBean exchangeCouponBean);

    @GET("http://pinpin.muxixyz.com/api/v1/business/specials/{id}")
    Observable<BaseResponseModel<SpecialListResponse>> getSpecials(@Path("id") int id, @Query("page") String page, @Query("limit") String limit);

    //@GET("http://pinpin.muxixyz.com/api/v1/business/list")
    @HTTP(method = "GET", path = "http://pinpin.muxixyz.com/api/v1/business/list", hasBody = true)
    Observable<MerchantListResponse> getMerchants(@Query("page")String page, @Query("limit")String limit, @Body MerchantNameBean merchantNameBean);

    @GET("http://pinpin.muxixyz.com/api/v1/business/info/{id}")
    Observable<MerchantInfoResponse> getMerchant(@Path("id")int id);

}
