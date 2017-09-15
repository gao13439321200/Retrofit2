package com.mvp.empty.data.dagger;


import com.mvp.empty.MainActivity;
import com.mvp.empty.data.model.AppModelImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gaopeng on 2016/11/4.
 * 依赖注入Component
 */
@Singleton
@Component(modules = BaseModul.class)
public interface BaseComponent {
    void inject(AppModelImpl appModel);

    void inject(MainActivity mainActivity);

}
