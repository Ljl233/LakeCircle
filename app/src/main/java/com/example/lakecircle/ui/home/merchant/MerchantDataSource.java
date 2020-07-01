package com.example.lakecircle.ui.home.merchant;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.lakecircle.MyApp;
import com.example.lakecircle.commonUtils.BaseResponseModel;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.data.Lake.Lake;
import com.example.lakecircle.data.Lake.LakeDao;
import com.example.lakecircle.data.Merchant.MerchantDao;
import com.example.lakecircle.ui.home.journey.lake.LakeIntroBean;
import com.example.lakecircle.data.Merchant.Merchant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MerchantDataSource {

    private static MerchantDataSource INSTANCE;

    private boolean ifSaveLake = false;
    private boolean ifSaveMerchant = false;

    private LakeDao mLakeDao;
    private MerchantDao mMerchantDao;

    private List<Merchant> merchants = new ArrayList<>();

    private void print(){
        for (Merchant merchant : merchants ) {
            System.out.print("id = "+merchant.getId());
            System.out.print(", name = "+merchant.getName());
            System.out.print(", avatar_url = "+merchant.getAvatar_url());
            System.out.print(", address = "+merchant.getAddress());
            System.out.print(", near = "+merchant.getNear());
            System.out.print(", latitude = "+merchant.getLatitude());
            System.out.print(", lonitude = "+merchant.getLongitude());
            System.out.println();
        }
    }

    public static MerchantDataSource getInstance(LakeDao lakeDao, MerchantDao merchantDao) {
        if (INSTANCE == null) {
            synchronized (MerchantDataSource.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = new MerchantDataSource(lakeDao, merchantDao);
                }
            }
        }
        return INSTANCE;
    }

    private MerchantDataSource(LakeDao lakeDao, MerchantDao merchantDao) {
        mLakeDao = lakeDao;
        mMerchantDao = merchantDao;
    }

    public void getLakeList(GetLakesCallback callback) {
        if ( !ifSaveLake ) {
            getLakes(new GetLakesCallback() {
                @Override
                public void onfinish(Observable<List<Lake>> observable) {
                    callback.onfinish(observable);
                }
            });
        } else {
            callback.onfinish(getAllLake());
        }
    }

    public void getMerchantList(boolean ifForceRefresh, String name, Context context, GetMerchantsCallback callback) {
//        if ( !ifSaveMerchant || ifForceRefresh ) {
//            ifSaveMerchant = false;
//            deleteAllMerchant();
//            getMerchants(name, new GetMerchantsCallback() {
//                @Override
//                public void onfinish(Observable<List<Merchant>> observable) {
//                    callback.onfinish(observable);
//                }
//            }, context);
//        } else {
//            callback.onfinish(getAllMerchant());
//        }
        getMerchants(name, new GetMerchantsCallback() {
            @Override
            public void onfinish(List<Merchant> list) {
                callback.onfinish(list);
            }
        }, context);
    }

    public void getLakes(GetLakesCallback callback) {
        NetUtil.getInstance().getApi().getAllLakeIntro()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<LakeIntroBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(BaseResponseModel<LakeIntroBean> lakeIntroBeanBaseResponseModel) {
                        Log.e("TAG", "get all lake");
                        saveAllLake(lakeIntroBeanBaseResponseModel.getData(), callback);
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("TAG", "get all lake fail");
                    }
                    @Override
                    public void onComplete() {
                        Log.e("TAG", "get all lake done");
                    }
                });
    }

    public void saveAllLake(List<LakeIntroBean> list, GetLakesCallback callback) {
        for (int i = 0 ; i < list.size() ; i++ ) {
            LakeIntroBean b = list.get(i);
            int finalI = i;
            Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
                Log.e("TAG", "save a lake");
                String[] strings = b.getLongitude_and_latitude().split(",");
                double latitude = Double.parseDouble(strings[1]);
                double longitude = Double.parseDouble(strings[0]);
                Lake lake = new Lake(b.getId(), b.getName(), latitude, longitude);
                insertLake(lake);
                emitter.onNext(finalI);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) { }
                        @Override
                        public void onNext(Integer integer) {
                            Log.e("TAG", "save lake "+integer);
                            if( integer == list.size() -1 ) {
                                Log.e("TAG", "save all lake done");
                                ifSaveLake = true;
                                callback.onfinish(getAllLake());
                            }
                        }
                        @Override
                        public void onError(Throwable e) { e.printStackTrace();}
                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    public void insertLake(Lake lake) {
        Observable.create((ObservableOnSubscribe<Void>) emitter -> mLakeDao.addLake(lake))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public Observable<List<Lake>> getAllLake() {
        return Observable.create((ObservableOnSubscribe<List<Lake>>) emitter ->
                emitter.onNext(mLakeDao.getLakeList())).subscribeOn(Schedulers.io());
    }
    
    public void getMerchants(String LakeName, GetMerchantsCallback callback, Context context) {
        NetUtil.getInstance().getApi().getMerchants("1","50",new MerchantNameBean(LakeName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(MerchantListResponse merchantListResponse) {
                        if (merchantListResponse.getData().getSum() == 0) return;
                        List<MerchantListResponse.DataBeanX.DataBean> t = merchantListResponse.getData().getData();
                        if (t == null || t.size() == 0) return;
                        for (MerchantListResponse.DataBeanX.DataBean b : t) {
                            Merchant merchant = new Merchant(b.getId(), b.getChange_num(),
                                    b.getAvatar_url(), "", b.getName(), b.getIntro(), "",
                                    0, 0, "");
                            merchants.add(merchant);
                            insertMerchant(merchant);
                        }
                        Log.e("TAG","initial save merchant list");
                        print();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG","to get merchant info");
                        getMerchant(callback, context);
                    }
                });
    }
    
    public void getMerchant(GetMerchantsCallback callback, Context context) {
//        Observable.create((ObservableOnSubscribe<List<Merchant>>) emitter -> {
//            Log.e("TAG","to get all merchant save before");
//            emitter.onNext(mMerchantDao.getAllMerchant());
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Merchant>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) { }
//                    @Override
//                    public void onNext(List<Merchant> merchants) {
//                        Log.e("TAG","get all merchant save before");
//                        if ( merchants == null || merchants.size() == 0 ) return;
//
//                        for (Merchant merchan : merchants) {
        for ( int i =0 ; i < merchants.size() ;i++) {
            final Merchant merchan = merchants.get(i);
            final int I = i;
//        Log.e("TAG","the merchant "+merchan.getId());
            Log.e("TAG","the merchant "+merchants.get(i).getId());
                    //        getMerchantInfo(merchan.getId())
            getMerchantInfo(merchants.get(i).getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(merchant -> {
                                        Log.e("TAG","get merchant "+merchan.getId()+" info");
                                        Merchant m = new Merchant(merchan.getId(), merchan.getChange_num(),
                                                merchan.getAvatar_url(), merchant.getNear(),
                                                merchan.getName(), merchan.getIntro(),
                                                merchant.getAddress(), 0, 0, merchant.getTel());
                                        merchants.set(I,m);
                                        insertMerchant(m);
                                        print();
                                    }).observeOn(Schedulers.io())
                                    .concatMap((Function<Merchant, Observable<Merchant>>)
                                            merchant -> {
                                                Log.e("TAG", "to get merchant " + merchant.getId() + " position");
                                                return getMerchantPosition(merchant, context);
                                            })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<Merchant>() {
                                        @Override
                                        public void onSubscribe(Disposable d) { }

                                        @Override
                                        public void onNext(Merchant merchant) {
                                            Log.e("TAG", " after get merchant " + merchant.getId() + "  position ");
                                            print();
                                            merchants.set(I,merchant);
                                            insertMerchant(merchant);
                                        }

                                        @Override
                                        public void onError(Throwable e) { e.printStackTrace(); }

                                        @Override
                                        public void onComplete() {
                                            ifSaveMerchant = true;
                                            Log.e("Tag","done");
                                            callback.onfinish(merchants);
                                        }
                                    });
                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) { e.printStackTrace(); }
//
//                    @Override
//                    public void onComplete() { }
//                });
    }

    public Observable<Merchant> getMerchantInfo(int id) {
        return NetUtil.getInstance().getApi().getMerchant(id)
                .subscribeOn(Schedulers.io())
                .map(new Function<MerchantInfoResponse, Merchant>() {
                    @Override
                    public Merchant apply(MerchantInfoResponse merchantInfoResponse) throws Exception {
                        Log.e("TAG", "get merchant " + merchantInfoResponse.getData().getIntro()+"  info");
                        MerchantInfoResponse.DataBean t =merchantInfoResponse.getData();
                        return new Merchant(id, 0,"", t.getNear(), "",
                                "", t.getAddress(),0, 0,t.getTel());
                    }
                });
    }

    public Observable<Merchant> getMerchantPosition(Merchant merchant, Context context) {
        return Observable.create(new ObservableOnSubscribe<Merchant>() {
            @Override
            public void subscribe(ObservableEmitter<Merchant> emitter) throws Exception {
                Log.e("TAG", "ready to get merchant " + merchant.getId() + " position");
                getLatLng(merchant.getAddress(), context, new GetLatLngCallback() {
                    @Override
                    public void get(LatLng latLng) {
                        Log.e("TAG", "get merchant " + merchant.getId() + " position");
                        emitter.onNext(new Merchant(merchant.getId(), merchant.getChange_num(),
                                merchant.getAvatar_url(),merchant.getNear(), merchant.getName(),
                                merchant.getIntro(), merchant.getAddress(), latLng.latitude,
                                latLng.longitude, merchant.getTel()));
                    }
                });
            }
        });
    }

    private void getLatLng(String address, Context context, GetLatLngCallback callback) {
        Log.e("TAG", "to get merchant " + address + " position");
        if ( address.length() == 0 )  {
            callback.get(new LatLng(0,0));
            return;
        }
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) { }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if ( geocodeResult == null ){
                    callback.get(new LatLng(0,0));
                    return;
                }
                LatLonPoint l1 = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                callback.get(new LatLng(l1.getLatitude(), l1.getLongitude()));
            }
        });
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        geocoderSearch.getFromLocationNameAsyn(new GeocodeQuery(address, "武汉"));
    }

    public void deleteAllMerchant(){
        Observable.create((ObservableOnSubscribe<Void>) emitter -> {
            if ( mMerchantDao.getAllMerchant().size() != 0 )
                mMerchantDao.deleteAll();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void insertMerchant(Merchant merchant) {
        Observable.create((ObservableOnSubscribe<Void>) emitter -> mMerchantDao.insertMerchant(merchant))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public Observable<List<Merchant>> getAllMerchant() {
        return Observable.create((ObservableOnSubscribe<List<Merchant>>)
                emitter -> emitter.onNext(mMerchantDao.getAllMerchant())).subscribeOn(Schedulers.io());
    }


    public interface GetLatLngCallback {
        void get(LatLng latLng);
    }

    public interface GetMerchantsCallback{
        //void onfinish(Observable<List<Merchant>> observable);
        void onfinish(List<Merchant> list);
    }
    public interface GetLakesCallback{
        void onfinish(Observable<List<Lake>> observable);
    }

}
