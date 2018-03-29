package com.xinyiandroid.presenter.main;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ToastUtils;
import com.xinyiandroid.api.ApiManager;
import com.xinyiandroid.ui.model.LoginModel;
import com.xylibrary.base.RxPresenter;
import com.xylibrary.utils.ComposeUtil;

/**
 * Created by jiajun.wang on 2018/3/19.
 */

public class MainPresenter extends RxPresenter {

    public void Login(String... strings) {
        addSubscribe(ApiManager.<LoginModel>Login(new TypeReference<LoginModel>() {}, strings)
                .compose(ComposeUtil.composeUtil(mView))
                .subscribe(loginModel -> {
                    ToastUtils.showShort(JSONObject.toJSONString(loginModel));
                }, throwableConsumer));
    }


}
