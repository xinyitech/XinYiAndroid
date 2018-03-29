package com.xylibrary.utils;


import com.xylibrary.base.IBaseView;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jiajun.wang on 2018/3/19.
 */

public class ComposeUtil {



    //type 0表示obj  1表示List
    public static <K> ObservableTransformer<K, K> composeUtil(IBaseView mView) {
        ObservableTransformer<K, K> observableTransformer = (Observable<K> upstream) -> {
            return upstream
                    .doOnSubscribe(disposable -> { mView.showLoading();})
                    .doFinally(() -> {mView.hideLoading();})
                    .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                    .subscribeOn(Schedulers.io())//子线程
                    .observeOn(AndroidSchedulers.mainThread());
        };
        return observableTransformer;
    }



}
