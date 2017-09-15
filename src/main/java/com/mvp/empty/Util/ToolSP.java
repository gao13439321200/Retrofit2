package com.mvp.empty.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Date： 2016/8/30.
 * Created by： v7428477
 * email：764213580@qq.com
 */
public class ToolSP {

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";
    public static final String USERID = "userid";
    public static final String CHILDNO = "childNo";
    public static final String PUSHCHILDNO = "ushchildNo";
    public static final String SEARCH = "search";
    public static final String MOBILE = "mobile";
    public static final String IDCARD = "idCard";
    public static final String TOKEN = "token";
    public static final String STATUS = "status";
    public static final String HEADIMG = "headImg";
    public static final String UNIONID = "unionid";
    public static final String ACCESSTOKEN = "AccessToken";
    public static final String DRIVERTOKEN = "DriverToken";
    public static final String DRIVERNAME = "driverName";//添加司机回写用的
    public static final String NAME = "name";
    public static final String BANNER = "banner";
    public static final String USERCODE = "userCode";
    public static final String LONGITUDE = "Longitude";//经度
    public static final String LATITUDE = "Latitude";//纬度
    public static final String SYSTEMMSGID = "systemMsgId";//系统消息id
    public static final String ORDERMSGID = "orderMsyId";//货单消息id
    public static final String OVERTIME = "overtime";//超时时间
    public static final String APPROVENAME = "oapproveName";//认证名字
    public static final String APPROVEIDCARD = "approveIdcard";//认证的身份证号码
    public static final String SALEFEE = "salefee";
    public static final String PWD = "password";
    public static final String USERNAME = "username";



    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        if (object != null) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            } else {
                editor.putString(key, object.toString());
            }

            SharedPreferencesCompat.apply(editor);

        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return null;
        }

    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(USERID);
        editor.remove(DRIVERNAME);//添加司机回写用的，需清掉
        editor.remove(USERNAME);//添加司机回写用的，需清掉
        editor.remove(PWD);//添加司机回写用的，需清掉
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }




    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}