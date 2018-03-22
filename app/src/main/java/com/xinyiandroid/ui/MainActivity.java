package com.xinyiandroid.ui;

import com.xinyiandroid.presenter.main.MainPresenter;
import com.xylibrary.base.RxPresenter;
import com.xymaplibrary.base.BaseMapActivity;

public class MainActivity extends BaseMapActivity<MainPresenter>{
    @Override
    public RxPresenter inject() {
        return new MainPresenter();
    }

}
