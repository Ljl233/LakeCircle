package com.example.lakecircle.ui.home.merchant;

import android.content.Context;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.Comparator;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MerchantDistanceComparator implements Comparator<Merchant> {

    private Context mContext;
    private LatLng mLocation;//我的坐标

    public MerchantDistanceComparator(Context context, LatLng location) {
        mContext = context;
        mLocation = location;
    }

    @Override
    public int compare(Merchant o1, Merchant o2) {

        return 0;
    }

    private interface Callback {
        void get(LatLng latLng);
    }

    //地址转换坐标
    private void getLatLng(String address, Callback callback) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) { }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                LatLonPoint l1 = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                callback.get(new LatLng(l1.getLatitude(), l1.getLongitude()));
            }
        });
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        geocoderSearch.getFromLocationNameAsyn(new GeocodeQuery(address, "武汉"));
    }

    private void calculate(String a1, String a2) {
        Observable<Float> observable1 = Observable
                .create((ObservableOnSubscribe<LatLng>) emitter ->
                        getLatLng(a1, new Callback() {
                            @Override
                            public void get(LatLng latLng) { emitter.onNext(latLng);}
                        }))
                .map(latLng -> AMapUtils.calculateLineDistance(latLng, mLocation));

        Observable<Float> observable2 = Observable
                .create((ObservableOnSubscribe<LatLng>) emitter ->
                        getLatLng(a2, new Callback() {
                            @Override
                            public void get(LatLng latLng) { emitter.onNext(latLng);}
                        }))
                .map(latLng -> AMapUtils.calculateLineDistance(latLng, mLocation));

        Observable.zip(observable1, observable2, (aFloat, aFloat2) -> (int) (aFloat - aFloat2))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Integer integer) {
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }

}
