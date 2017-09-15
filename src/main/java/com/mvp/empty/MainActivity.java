package com.mvp.empty;

import com.mvp.empty.activity.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    //业务逻辑
    @Override
    public void doBusiness() {
        setTitle("首页");
        mBaseComponent.inject(this);//添加依赖
    }
}
