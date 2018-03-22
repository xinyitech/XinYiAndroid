package com.xylibrary.base;

import com.blankj.utilcode.util.ToastUtils;
import com.xylibrary.application.Constants;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by codeest on 2016/8/2.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 * <p>
 */
public class RxPresenter<T extends IBaseView> {

	protected CompositeDisposable mCompositeDisposable;
	public T mView;


	public void attachView(T view) {
		this.mView = view;
	}


	public void detachView() {
		if (mCompositeDisposable != null) {
			mCompositeDisposable.clear();
		}
		mView=null;
	}

	public void addSubscribe(Disposable subscription) {
		if (mCompositeDisposable == null) {
			mCompositeDisposable = new CompositeDisposable();
		}
		mCompositeDisposable.add(subscription);
	}


	public Consumer<Throwable>throwableConsumer=new Consumer<Throwable>() {
		@Override
		public void accept(Throwable throwable) throws Exception {
			if (throwable instanceof UnknownHostException){
				ToastUtils.showShort(Constants.NETERROR);
			}else if (throwable instanceof SocketTimeoutException){
				ToastUtils.showShort(Constants.TIMEOUT);
			}else{
				ToastUtils.showShort(throwable.getMessage());
			}
		}
	};


}


