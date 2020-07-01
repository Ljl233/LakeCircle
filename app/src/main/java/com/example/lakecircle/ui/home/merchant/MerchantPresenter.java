package com.example.lakecircle.ui.home.merchant;

import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.lakecircle.data.Lake.Lake;
import com.example.lakecircle.data.Merchant.Merchant;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MerchantPresenter {
    private MerchantListFragment mView;

    private MerchantDataSource mDataSource;

    public MerchantPresenter(MerchantListFragment view, MerchantDataSource dataSource) {
        mView = view;
        mDataSource = dataSource;
        mView.setPresenter(this);
    }

    public void getMerchants(LatLng position, boolean ifForceRefresh) {
        getAllLake(position).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onNext(String s) {
//                mDataSource.getMerchantList(ifForceRefresh, s, mView.getContext(), observable -> observable.observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<List<Merchant>>() {
//                            @Override
//                            public void onSubscribe(Disposable d) { }
//                            @Override
//                            public void onNext(List<Merchant> merchants) { mView.put(merchants); }
//                            @Override
//                            public void onError(Throwable e) {
//                                e.printStackTrace();
//                                mView.showError("加载失败");
//                            }
//                            @Override
//                            public void onComplete() { }
//                        }));
                mDataSource.getMerchantList(true,s, mView.getContext(), new MerchantDataSource.GetMerchantsCallback() {
                    @Override
                    public void onfinish(List<Merchant> merchants) {
                        mView.put(merchants);
                    }
                });
            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.showError("加载失败");
            }
            @Override
            public void onComplete() { }
        });
    }

    public void getMerchants(String name) {
//        mDataSource.getMerchantList(true,name, mView.getContext(), new MerchantDataSource.GetMerchantsCallback() {
////            @Override
////            public void onfinish(Observable<List<Merchant>> observable) {
////                observable.observeOn(AndroidSchedulers.mainThread())
////                        .subscribe(new Observer<List<Merchant>>() {
////                            @Override
////                            public void onSubscribe(Disposable d) { }
////                            @Override
////                            public void onNext(List<Merchant> merchants) { mView.put(merchants); }
////                            @Override
////                            public void onError(Throwable e) {
////                                e.printStackTrace();
////                                mView.showError("加载失败");
////                            }
////                            @Override
////                            public void onComplete() {
////                            }
////                        });
////            }
////        });
        mDataSource.getMerchantList(true,name, mView.getContext(), new MerchantDataSource.GetMerchantsCallback() {
            @Override
            public void onfinish(List<Merchant> merchants) {
                 mView.put(merchants);
            }
        });
    }

    public Observable<String> getAllLake(LatLng position) {
        return Observable.create(emitter -> mDataSource.getLakeList(
                observable -> observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Lake>>() {
                            @Override
                            public void onSubscribe(Disposable d) {}
                            @Override
                            public void onNext(List<Lake> lakes) {
                                Log.e("TAG",lakes.size()+"  get Lakes");
                                Lake near = lakes.get(0);
                                float d = AMapUtils.calculateLineDistance(position,
                                        new LatLng(near.getLatitude(), near.getLongitude()));
                                for (Lake l : lakes)
                                    if (d > AMapUtils.calculateLineDistance(position,
                                            new LatLng(l.getLatitude(), l.getLongitude())))
                                        near = l;

                                Log.e("TAG","get nearest is "+near.getName());
                                emitter.onNext(near.getName());
                            }
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                mView.showError("加载失败");
                            }
                            @Override
                            public void onComplete() { }
                        })));
    }



}
