package com.xylibrary.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
}

/*
	public <K> ObservableTransformer<ResultEntity<K>, K> composeUtil(Type t) {
		ObservableTransformer<ResultEntity<K>, K> observableTransformer = upstream -> {
			return upstream
					.doOnSubscribe(disposable -> {
						if (mView!=null)
							mView.showLoading();
						if (!NetUtil.isNetworkAvailable(erpApplication.getApplication())){
							throw new Exception(ErpApplication.erpApplication.getApplication().getString(R.string.net_error));
						}
					})
					.doFinally(() -> {
						if (mView!=null)
							mView.hideLoading();
					})
					.subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
					.subscribeOn(Schedulers.io())//子线程
					.observeOn(AndroidSchedulers.mainThread())
					.flatMap(new Function<ResultEntity<K>, ObservableSource<K>>() {
						@Override
						public ObservableSource<K> apply(@NonNull ResultEntity<K> kResultEntity) throws Exception {
							if (kResultEntity != null && kResultEntity.getCode() == Constants.SUCCESS) {
								String s = g.toJson(kResultEntity.getData());
								if (!StringUtil.isEmpty(kResultEntity.getMessage()))
									ToastUtil.showToast(kResultEntity.getMessage());
								if ("null".equals(s) || StringUtil.isEmpty(s)) {
									return Observable.just(g.fromJson("test", t));
								}
								return Observable.just(g.fromJson(s, t));
							} else if (kResultEntity != null && kResultEntity.getCode() == UNAUTHORIZED) {
								throw new Exception(ErpApplication.erpApplication.getApplication().getResources().getString(R.string.unauthority));
							} else {
								if (kResultEntity != null){
									String s = g.toJson(kResultEntity.getData());
									if (!"null".equals(s)&&!StringUtil.isEmpty(s)){
										List<UploadErrorResultModel> uploadErrorResultModels=g.fromJson(s,new TypeToken<List<UploadErrorResultModel>>() {}.getType());
										ResultDataModel resultDataModel=new ResultDataModel(uploadErrorResultModels,kResultEntity.getMessage(),2);
										return Observable.just((K) resultDataModel);
									}
								}
								throw new Exception(!StringUtil.isEmpty(kResultEntity.getMessage()) ? kResultEntity.getMessage()
										:ErpApplication.erpApplication.getApplication().getResources().getString(R.string.unkown_error));
							}
						}
					});
		};
		return observableTransformer;
	}
	public Consumer<Throwable> throwError = e -> {
		if (mView!=null)
			mView.handlerException(e);
	};
}
*/

