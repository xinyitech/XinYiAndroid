package com.xinyiandroid.presenter.main;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.xinyiandroid.api.ApiManager;
import com.xinyiandroid.ui.model.LoginModel;
import com.xinyiandroid.utils.ComposeUtil;
import com.xinyiandroid.utils.GsonUtil;
import com.xylibrary.base.RxPresenter;

/**
 * Created by jiajun.wang on 2018/3/19.
 */

public class MainPresenter extends RxPresenter {

    public void Login(String...strings){
        ApiManager.<LoginModel>Login(strings)
                .compose(ComposeUtil.composeUtil(new TypeToken<LoginModel>() {}.getType(),mView))
                .subscribe(loginModel -> ToastUtils.showShort(GsonUtil.obj2String(loginModel)));
    }
}
