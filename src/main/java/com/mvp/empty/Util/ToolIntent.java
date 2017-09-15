package com.mvp.empty.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by liwei on 2017/3/15.
 */

public class ToolIntent {
    private static Intent intent = null;
    public static final String DATA = "data";

    public static void ClassToActivity(Context context, Class aClass) {
        intent = new Intent();
        intent.setClass(context, aClass);
        context.startActivity(intent);
    }

    public static void ClassToLogin(Context context, Class aClass, int flage) {
        intent = new Intent();
        intent.setClass(context, aClass);
        intent.addFlags(flage);
        context.startActivity(intent);
    }

    public static void ClassToActivity(Context context, Class aClass, Bundle mBundle) {
        intent = new Intent();
        intent.setClass(context, aClass);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }


    public static void ClassToActivity(Context context, Class aClass, String mdate) {
        intent = new Intent();
        intent.setClass(context, aClass);
        intent.putExtra(DATA, mdate);
        context.startActivity(intent);
    }

    public static void ClassToActivity(Context context, Class aClass, String mdate, Bundle bundle) {
        intent = new Intent();
        intent.setClass(context, aClass);
        intent.putExtra(DATA, mdate);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    public static void ClassToActivityResult(Context context, Class aClass, int code) {
        intent = new Intent();
        intent.setClass(context, aClass);
        ((Activity) context).startActivityForResult(intent, code);
    }


    public static void ClassToActivityResult(Context context, Class aClass, Bundle mBundle, int code) {
        intent = new Intent();
        intent.putExtras(mBundle);
        intent.setClass(context, aClass);
        ((Activity) context).startActivityForResult(intent, code);
    }

    public static void ClassToActivity(Context context, Class aClass, int mdate) {
        intent = new Intent();
        intent.setClass(context, aClass);
        intent.putExtra(DATA, mdate);
        context.startActivity(intent);
    }

    public static void ClearOldActivity(Context context, Class aClass) {
        intent = new Intent();
        intent.setClass(context, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}