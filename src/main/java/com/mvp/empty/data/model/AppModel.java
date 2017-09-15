package com.mvp.empty.data.model;

/**
 * Created by gaopeng on 2016/11/7.
 * 网络请求
 */
public interface AppModel {
    /**
     * POST请求(传递实体类或map)
     *
     * @param object 对象
     * @param url    请求的接口
     */
    void retrofit_Post(Object object, String url);

    /**
     * POST请求(传递实体类或map)
     *
     * @param object 对象
     * @param url    请求的接口
     * @param task   请求的唯一标识
     */
    void retrofit_Post_restful(Object object, String url, String task);


    /**
     * GET请求
     *
     * @param object 对象
     * @param url    请求的接口
     */
    void retrofit_Get(Object object, String url);

    /**
     * GET请求
     *
     * @param object 对象
     * @param url    请求的接口
     * @param task   请求的唯一标识
     */
    void retrofit_Get_restful(Object object, String url, String task);


    /**
     * PUT请求
     *
     * @param object 对象
     * @param url    请求的接口
     */
    void retrofit_Put(Object object, String url);

    /**
     * PUT请求
     *
     * @param object 对象
     * @param url    请求的接口
     * @param task   请求的唯一标识
     */
    void retrofit_Put_restful(Object object, String url, String task);

    /**
     * DELETE请求
     *
     * @param object 对象
     * @param url    请求的接口
     */
    void retrofit_delete(Object object, String url);

    /**
     * DELETE请求
     *
     * @param object 对象
     * @param url    请求的接口
     * @param task   请求的唯一标识
     */
    void retrofit_delete_restful(Object object, String url, String task);


}
