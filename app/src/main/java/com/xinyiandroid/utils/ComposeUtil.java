package com.xinyiandroid.utils;

import com.google.gson.Gson;
import com.xinyiandroid.api.ResultEntity;
import com.xinyiandroid.application.Constants;
import com.xylibrary.base.IBaseView;

import java.lang.reflect.Type;

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

    public static <K> ObservableTransformer<ResultEntity<K>, K> composeUtil(final Type t,  IBaseView mView) {
        ObservableTransformer<ResultEntity<K>, K> observableTransformer = upstream -> {
            return upstream
                    .doOnSubscribe(disposable -> { mView.showLoading();})
                    .doFinally(() -> {mView.hideLoading();})
                    .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                    .subscribeOn(Schedulers.io())//子线程
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap((Function<ResultEntity<K>, ObservableSource<K>>) kResultEntity -> {
                        if (kResultEntity != null && kResultEntity.getCode() == Constants.REQUESTSUCCESSCODE) {
                            Gson g=new Gson();
                            String s = g.toJson(kResultEntity.getData());
                            return Observable.just(g.fromJson(s, t));
                        }else {
                            throw new Exception(Constants.SERVERERROR);
                        }
                    });
        };
        return observableTransformer;
    }

}
