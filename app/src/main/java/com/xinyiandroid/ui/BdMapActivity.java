package com.xinyiandroid.ui;

import com.xylibrary.base.RxPresenter;
import com.xymaplibrary.base.BaseMapActivity;

public class BdMapActivity extends BaseMapActivity {


    @Override
    public RxPresenter inject() {
        return new RxPresenter();
    }
}
