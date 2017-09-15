package com.mvp.empty.data;

import android.content.Context;
import android.widget.Toast;

import com.mvp.empty.Util.ToolSP;
import com.mvp.empty.Util.ToolString;
import com.mvp.empty.Util.ToolToast;
import com.mvp.empty.data.model.AppModel;
import com.mvp.empty.data.model.AppModelImpl;
import com.mvp.empty.dto.ApiResponse;

/**
 * Created by gaopeng on 2016/11/8.
 * 请求和逻辑处理父类
 */
public class BasePresenter implements BaseListen {
    public AppModel mAppModel;//请求网络类
    public Context mContext;//上下文

    public BasePresenter(Context context) {
        this.mAppModel = new AppModelImpl(this, context);
        mContext = context;
    }

    public BasePresenter(Context context, BaseView view) {
        this.mAppModel = new AppModelImpl(this, context);
        mContext = context;
    }


    //请求成功
    @Override
    public void onSuccess(ApiResponse apiResponse, String url) {
    }

    //请求失败
    @Override
    public void onFailed(ApiResponse apiResponse, String url) {
        if (apiResponse.getCode() == 100) {
            ToolSP.clear(mContext);
            ToolToast.showShort(mContext, ToolString.str_r);
//            ToolIntent.ClearOldActivity(mContext, LoginActivity.class);
        } else {
                Toast.makeText(mContext, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
