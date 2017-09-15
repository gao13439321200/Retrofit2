package com.mvp.empty.data;


import com.mvp.empty.dto.ApiResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by gaopeng on 2016/11/4.
 * 网络请求
 */
public interface DataInterface {

    //POST请求(传递Map)
    @Headers("Authorization:Basic dXNlcjoxMjM0")
    @FormUrlEncoded
    @POST
    Call<ApiResponse<Object>> getData_FieldMap(@HeaderMap Map<String, Object> headers, @Url String url, @FieldMap Map<String, Object> map);

    //POST请求(传递对象)
    @Headers("Authorization:Basic dXNlcjoxMjM0")
    @POST
    Call<ApiResponse<Object>> getData_Body(@HeaderMap Map<String, Object> headers, @Url String url, @Body Object object);

    //GET请求
    @Headers("Authorization:Basic dXNlcjoxMjM0")
    @GET
    Call<ApiResponse<Object>> getData_Get(@HeaderMap Map<String, Object> headers, @Url String url, @QueryMap Map<String, Object> map);


    //PUT请求
    @Headers("Authorization:Basic dXNlcjoxMjM0")
    @PUT
    Call<ApiResponse<Object>> getData_Put_Object(@HeaderMap Map<String, Object> headers, @Url String url, @Body Object object);

    //PUT请求
    @Headers("Authorization:Basic dXNlcjoxMjM0")
    @FormUrlEncoded
    @PUT
    Call<ApiResponse<Object>> getData_Put_map(@HeaderMap Map<String, Object> headers, @Url String url, @FieldMap Map<String, Object> map);

    //DELETE请求
    @Headers("Authorization:Basic dXNlcjoxMjM0")
    @DELETE
    Call<ApiResponse<Object>> getData_Delete(@HeaderMap Map<String, Object> headers, @Url String url, @QueryMap Map<String, Object> map);


    //上传图片的
    @POST()
    Call<ApiResponse<Object>> up_file_post(@HeaderMap Map<String, Object> headers, @Url String url, @Body RequestBody Body);
}
