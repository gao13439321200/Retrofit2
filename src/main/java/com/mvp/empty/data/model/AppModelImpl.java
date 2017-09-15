package com.mvp.empty.data.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mvp.empty.R;
import com.mvp.empty.Util.JsonUtil;
import com.mvp.empty.Util.ToolLog;
import com.mvp.empty.data.BaseListen;
import com.mvp.empty.data.DataInterface;
import com.mvp.empty.data.dagger.BaseModul;
import com.mvp.empty.data.dagger.DaggerBaseComponent;
import com.mvp.empty.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gaopeng on 2016/11/4.
 * 网络请求
 */

public class AppModelImpl implements AppModel {
    @Inject
    DataInterface mDataInterface;
    Context mContext;
    private BaseListen mBaseListen;
    private static AppModel mAppModel;
    private Dialog dialog;

    public AppModelImpl(BaseListen baseListen, Context context) {
        DaggerBaseComponent.builder().baseModul(new BaseModul()).build().inject(this);
        this.mBaseListen = baseListen;
        this.mContext = context;
        dialog = createDialog();
    }

    /**
     * 单例模式
     *
     * @param baseListen 回调
     * @return 单例对象
     */
    public static AppModel getInstance(BaseListen baseListen, Context context) {
        if (mAppModel == null) {
            mAppModel = new AppModelImpl(baseListen, context);
        }
        return mAppModel;
    }


    /**
     * POST请求
     *
     * @param object 参数
     * @param url    请求的接口
     */
    @Override
    public void retrofit_Post(Object object, final String url) {
        showDialog(url);
        Call<ApiResponse<Object>> call;
        if (object instanceof HashMap) {
            Log.d(ToolLog.GETDATA, "接口:" + url + ",参数:" + object);
            call = mDataInterface.getData_FieldMap(getHeaderMap(), url, (Map<String, Object>) object);//传递表单
        } else if (object instanceof RequestBody) {
            call = mDataInterface.up_file_post(getHeaderMap(), url, (RequestBody) object);//上传文件对象
        } else {
            call = mDataInterface.getData_Body(getHeaderMap(), url, object);//传递对象
        }
        if (call != null) {
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    dismissDialog();
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 0) {
                        Log.d(ToolLog.GETDATA, "接口成功:" + url + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg());
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(apiResponse, url), url);
                    } else {
                        Log.d(ToolLog.GETDATA, apiResponse != null ? "接口失败:" + url + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg() : "返回值为空");
                        mBaseListen.onFailed(apiResponse == null ? new ApiResponse() : apiResponse, url);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    dismissDialog();
                    mBaseListen.onFailed(new ApiResponse(), url);
                }
            });
        } else {
            dismissDialog();
            mBaseListen.onFailed(new ApiResponse(), url);
        }
    }

    @Override
    public void retrofit_Post_restful(Object object, String url, final String task) {
        Log.d(ToolLog.GETDATA, "请求地址：" + url);
        showDialog(url);
        Call<ApiResponse<Object>> call;
        if (object == null) {
            call = mDataInterface.getData_FieldMap(getHeaderMap(), url, new HashMap<String, Object>());
        } else if (object instanceof HashMap) {
            call = mDataInterface.getData_FieldMap(getHeaderMap(), url, (Map<String, Object>) object);//传递表单
        } else if (object instanceof RequestBody) {
            call = mDataInterface.up_file_post(getHeaderMap(), url, (RequestBody) object);//上传文件对象
        } else {
            call = mDataInterface.getData_Body(getHeaderMap(), url, object);//传递对象
        }
        if (call != null) {
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    dismissDialog();
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 0) {
                        Log.d(ToolLog.GETDATA, "接口成功:" + task + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg());
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(apiResponse, task), task);
                    } else {
                        Log.d(ToolLog.GETDATA, apiResponse != null ? "接口失败:" + task + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg() : "返回值为空");
                        mBaseListen.onFailed(apiResponse == null ? new ApiResponse() : apiResponse, task);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    dismissDialog();
                    mBaseListen.onFailed(new ApiResponse(), task);
                }
            });
        } else {
            dismissDialog();
            mBaseListen.onFailed(new ApiResponse(), task);
        }
    }

    /**
     * GET请求
     *
     * @param object 参数
     * @param url    请求的接口
     */
    @Override
    public void retrofit_Get(Object object, final String url) {
        showDialog(url);
        Call<ApiResponse<Object>> call = mDataInterface.getData_Get(getHeaderMap(), url, (Map<String, Object>) object);//传递表单;
        if (call != null) {
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    dismissDialog();
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 0) {
                        Log.d(ToolLog.GETDATA, "接口成功:" + url + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg());
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(apiResponse, url), url);
                    } else {
                        Log.d(ToolLog.GETDATA, apiResponse != null ? "接口失败:" + url + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg() : "接口失败:" + url + "返回值为空");
                        mBaseListen.onFailed(apiResponse == null ? new ApiResponse() : apiResponse, url);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
//                    Log.d(Const.LOGINFO,"接口:"+url + ",返回值:" + t);
                    dismissDialog();
                    mBaseListen.onFailed(new ApiResponse(), url);
                }
            });
        } else {
            dismissDialog();
//            Log.d(Const.LOGINFO,"接口:"+url + ",返回值:null");
            mBaseListen.onFailed(new ApiResponse(), url);
        }
    }

    @Override
    public void retrofit_Get_restful(Object object, String url, final String task) {
        showDialog(url);
        Log.d(ToolLog.GETDATA, "请求地址：" + url);
        Call<ApiResponse<Object>> call = mDataInterface.getData_Get(getHeaderMap(), url, new HashMap<String, Object>());
        if (call != null) {
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    dismissDialog();
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 0) {
                        Log.d(ToolLog.GETDATA, "接口成功:" + task + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg());
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(apiResponse, task), task);
                    } else {
                        mBaseListen.onFailed(apiResponse == null ? new ApiResponse() : apiResponse, task);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    dismissDialog();
                    mBaseListen.onFailed(new ApiResponse(), task);
                }
            });
        } else {
            dismissDialog();
            mBaseListen.onFailed(new ApiResponse(), task);
        }
    }

    /**
     * PUT请求
     *
     * @param object 参数
     * @param gurl   请求的接口
     */
    @Override
    public void retrofit_Put(Object object, final String gurl) {
        showDialog(gurl);
        Call<ApiResponse<Object>> call;
        if (object instanceof HashMap) {
            Log.d(ToolLog.GETDATA, "接口:" + gurl + ",参数:" + JsonUtil.reverJson((Map) object));
            call = mDataInterface.getData_Put_map(getHeaderMap(), gurl, (Map<String, Object>) object);//传递表单
        } else {
            call = mDataInterface.getData_Put_Object(getHeaderMap(), gurl, object);//
        }
        final String url; // 这里是 put  map 和 object 的两种 混合传参
        if (gurl.contains("?")) {
            String[] sts = gurl.split("\\?");
            url = sts[0];
        } else {
            url = gurl;
        }
        if (call != null) {
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    dismissDialog();
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 0) {
                        Log.d(ToolLog.GETDATA, "接口成功:" + url + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg());
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(apiResponse, url), url);
                    } else {
                        Log.d(ToolLog.GETDATA, apiResponse != null ? "接口失败:" + url + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg() : "返回值为空");
                        mBaseListen.onFailed(apiResponse == null ? new ApiResponse() : apiResponse, url);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    dismissDialog();
                    mBaseListen.onFailed(new ApiResponse(), url);
                }
            });
        } else {
            dismissDialog();
            mBaseListen.onFailed(new ApiResponse(), url);
        }
    }

    @Override
    public void retrofit_Put_restful(Object object, String url, final String task) {
        Log.d(ToolLog.GETDATA, "请求地址：" + url);
        showDialog(url);
        Call<ApiResponse<Object>> call;
        if (object == null) {
            call = mDataInterface.getData_Put_map(getHeaderMap(), url, new HashMap<String, Object>());
        } else if (object instanceof HashMap) {
            call = mDataInterface.getData_Put_map(getHeaderMap(), url, (Map<String, Object>) object);//传递表单
        } else {
            call = mDataInterface.getData_Put_Object(getHeaderMap(), url, object);//
        }
        if (call != null) {
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    dismissDialog();
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 0) {
                        Log.d(ToolLog.GETDATA, "接口成功:" + task + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg());
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(apiResponse, task), task);
                    } else {
                        Log.d(ToolLog.GETDATA, apiResponse != null ? "接口失败:" + task + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg() : "返回值为空");
                        mBaseListen.onFailed(apiResponse == null ? new ApiResponse() : apiResponse, task);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    dismissDialog();
                    mBaseListen.onFailed(new ApiResponse(), task);
                }
            });
        } else {
            dismissDialog();
            mBaseListen.onFailed(new ApiResponse(), task);
        }
    }

    /**
     * PUT请求
     *
     * @param object 参数
     * @param url    请求的接口
     */
    @Override
    public void retrofit_delete(Object object, final String url) {
        showDialog(url);
        Call<ApiResponse<Object>> call;
        if (object instanceof HashMap) {
            Log.d(ToolLog.GETDATA, "接口:" + url + ",参数:" + JsonUtil.reverJson((Map) object));
            call = mDataInterface.getData_Delete(getHeaderMap(), url, (Map<String, Object>) object);//传递表单
        } else {
            call = mDataInterface.getData_Body(getHeaderMap(), url, object);//传递对象
        }
        if (call != null) {
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    dismissDialog();
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 0) {
                        Log.d(ToolLog.GETDATA, "接口成功:" + url + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg());
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(apiResponse, url), url);
                    } else {
                        Log.d(ToolLog.GETDATA, apiResponse != null ? "接口失败:" + url + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg() : "返回值为空");
                        mBaseListen.onFailed(apiResponse == null ? new ApiResponse() : apiResponse, url);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    dismissDialog();
                    mBaseListen.onFailed(new ApiResponse(), url);
                }
            });
        } else {
            dismissDialog();
            mBaseListen.onFailed(new ApiResponse(), url);
        }
    }

    @Override
    public void retrofit_delete_restful(Object object, String url, final String task) {
        Log.d(ToolLog.GETDATA, "请求地址：" + url);
        showDialog(url);
        Call<ApiResponse<Object>> call;
        if (object == null) {
            call = mDataInterface.getData_Delete(getHeaderMap(), url, new HashMap<String, Object>());//传递表单
        } else {
            call = mDataInterface.getData_Body(getHeaderMap(), url, object);//传递对象
        }
        if (call != null) {
            call.enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    dismissDialog();
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getCode() == 0) {
                        Log.d(ToolLog.GETDATA, "接口成功:" + task + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg());
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(apiResponse, task), task);
                    } else {
                        Log.d(ToolLog.GETDATA, apiResponse != null ? "接口失败:" + task + ",返回值code:" + apiResponse.getCode() + ",msg:" + apiResponse.getMsg() : "返回值为空");
                        mBaseListen.onFailed(apiResponse == null ? new ApiResponse() : apiResponse, task);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    dismissDialog();
                    mBaseListen.onFailed(new ApiResponse(), task);
                }
            });
        } else {
            dismissDialog();
            mBaseListen.onFailed(new ApiResponse(), task);
        }
    }


    private Dialog createDialog() {
        if (dialog == null && mContext != null) {
            dialog = new Dialog(mContext, R.style.loading_dialog);
            dialog.setContentView(R.layout.dialog_black_loading_layout);
//            GifImageView animationIV = (GifImageView) dialog.findViewById(R.id.gifview);
            ImageView animationIV = (ImageView) dialog.findViewById(R.id.gifview);
            Glide.with(mContext)
                    .load("http://clx-prod.oss-cn-beijing.aliyuncs.com/appImages/dialog.gif")
                    .into(animationIV);
            dialog.setCancelable(false);
            return dialog;
        }
        return dialog;
    }

    private void showDialog(String url) {
        if (dialog != null && !dialog.isShowing()
//                && !MyUrl.POINTRECORDS.equals(url)
//                && !MyUrl.LOCATION.equals(url)
//                && !MyUrl.GETSYSTEMVERSIONBYNUMBER.equals(url)
//                && !MyUrl.GETSTATUSBYCHILDNO.equals(url)
//                && !MyUrl.GETORDERINFOLOCATIONLIST.equals(url)
//                && !MyUrl.GETORDERCHILDBYDRIVERID.equals(url)
//                && !MyUrl.CAPTCHA.equals(url)
//                && !MyUrl.CAPTCHAREG.equals(url)
//                && !MyUrl.SUM.equals(url)
//                && !MyUrl.UPFILE.equals(url)
                ) {
            dialog.show();
        }
    }


    private void dismissDialog() {
        if (dialog != null && dialog.isShowing() && mContext != null && !((Activity) mContext).isFinishing()) {
            dialog.dismiss();
        }

    }

    //获取请求头的userid和token
    private Map<String, Object> getHeaderMap() {
//        Log.d("AppModelImpl", "MyUrl.userId:" + MyUrl.userId);
//        Log.d("AppModelImpl", "MyUrl.accessToken:" + MyUrl.accessToken);
//        Log.d("AppModelImpl", "sp里的userid：" + (int) ToolSP.get(mContext, ToolSP.USERID, -1));
//        Log.d("AppModelImpl", "sp里的accessToken：" + (String) ToolSP.get(mContext, ToolSP.ACCESSTOKEN, ""));
//
//        MyUrl.userId = (int) ToolSP.get(mContext, ToolSP.USERID, -1);
//        MyUrl.accessToken = (String) ToolSP.get(mContext, ToolSP.ACCESSTOKEN, "");
//
        Map<String, Object> map = new HashMap<>();
//        map.put("userId", MyUrl.userId);
//        map.put("accessToken", MyUrl.accessToken);
//        map.put("userType", "driver");
        return map;
    }
}
