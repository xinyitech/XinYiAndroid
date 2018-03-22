package com.xylibrary.utils;


import com.alibaba.fastjson.JSON;
import com.xylibrary.application.Constants;
import com.xylibrary.base.IBaseView;
import com.xylibrary.http.ResultEntity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jiajun.wang on 2018/3/19.
 */

public class ComposeUtil {



    //type 0表示obj  1表示List
    public static <K> ObservableTransformer<ResultEntity<K>, K> composeUtil(final Class  mClass, IBaseView mView,int type) {
        ObservableTransformer<ResultEntity<K>, K> observableTransformer = (Observable<ResultEntity<K>> upstream) -> {
            return upstream
                    .doOnSubscribe(disposable -> { mView.showLoading();})
                    .doFinally(() -> {mView.hideLoading();})
                    .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                    .subscribeOn(Schedulers.io())//子线程
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap((Function<ResultEntity<K>, ObservableSource<K>>) kResultEntity -> {
                        if (kResultEntity != null && kResultEntity.getCode() == Constants.REQUESTSUCCESSCODE) {
                            if (type==0) {
                                return Observable.just((K) JSON.parseObject(JSON.toJSONString(kResultEntity.getData()), mClass));
                            }else {
                                return Observable.just((K) JSON.parseArray(JSON.toJSONString(kResultEntity.getData()), mClass));
                            }
                        }else {
                            throw new Exception(Constants.REQUESTFAIL);
                        }
                    });
        };
        return observableTransformer;
    }



    //type 0表示obj  1表示List
    public static <K> ObservableTransformer<K, K> composeUtil(IBaseView mView) {
        ObservableTransformer <K,K>observableTransformer=new ObservableTransformer<K, K>() {
            @Override
            public Observable<K> apply(Observable<K> upstream) {
                return upstream
                        .doOnSubscribe(disposable -> { mView.showLoading();})
                        .doFinally(() -> {mView.hideLoading();})
                        .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                        .subscribeOn(Schedulers.io())//子线程
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
        return observableTransformer;
    }

}
