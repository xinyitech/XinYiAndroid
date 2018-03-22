package com.xinyiandroid.presenter.main;

import com.xinyiandroid.api.ApiManager;
import com.xinyiandroid.ui.model.LoginModel;
import com.xylibrary.base.RxPresenter;
import com.xylibrary.utils.ComposeUtil;

/**
 * Created by jiajun.wang on 2018/3/19.
 */

public class MainPresenter extends RxPresenter {

    public void Login(String... strings) {
        ApiManager.<LoginModel>Login()
                .compose(ComposeUtil.composeUtil(LoginModel.class, mView, 1))
                .subscribe(loginModel -> {

                }, throwableConsumer);
    }


}
