package com.mvp.empty.Util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mvp.empty.dto.ApiResponse;
import com.mvp.empty.dto.LoginResultDTO;
import com.mvp.empty.dto.TruckInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Gao on 2017/3/14.
 * 解析数据
 */

public class JsonUtil {
    /**
     * 将map参数转换成String格式
     *
     * @param map 参数map
     * @return 参数String
     */
    public static String reverJson(Map map) {
        return null;
    }

    /**
     * 解析返回值
     *
     * @param result 请求结果
     * @param url    接口
     * @return 处理后的返回值
     */
    public static ApiResponse getJsonResult(ApiResponse<Object> result, String url) {
        switch (url) {
            case MyUrl.LOGIN:
                result.setData(new Gson().fromJson(ToolString.checkGsonString(result.getData()), LoginResultDTO.class));
                break;
            case MyUrl.GETTRUCKINFOLISTBYTRUCKNO:
                result.setData(new Gson().fromJson(ToolString.checkGsonString(result.getData()), new TypeToken<List<TruckInfoDTO>>() {
                }.getType()));
                break;
        }
        return result;
    }
}
