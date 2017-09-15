package com.mvp.empty.data.dagger;

import android.app.Activity;
import android.content.Context;

import com.mvp.empty.Util.MyUrl;
import com.mvp.empty.activity.BaseActivity;
import com.mvp.empty.data.BaseView;
import com.mvp.empty.data.DataInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by gaopeng on 2016/11/4.
 * 依赖注入Modul
 */
@Module
public class BaseModul {
    private BaseView mBaseView;
    private Context mContext;

    public BaseModul(BaseView baseView, BaseActivity activity) {
        this.mBaseView = baseView;
        this.mContext = activity;
    }

//    public BaseModul(BaseView baseView, BaseFragment baseFragment) {
//        this.mBaseView = baseView;
//        this.mContext = baseFragment.getActivity();
//    }

    public BaseModul() {

    }

    @Provides
    @Singleton
    public Retrofit getRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(MyUrl.HTML + File.separator)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    @Provides
    @Singleton
    protected DataInterface getDataInterface(Retrofit retrofit) {
        return retrofit.create(DataInterface.class);
    }

    @Provides
    @Singleton
    protected BaseView getBaseView() {
        return mBaseView;
    }

    @Provides
    @Singleton
    protected Context getContext() {
        return mContext;
    }

    @Provides
    @Singleton
    protected Activity getActivity() {
        return (Activity) mContext;
    }

    @Provides
    @Singleton
    protected List getList() {
        return new ArrayList();
    }
}
