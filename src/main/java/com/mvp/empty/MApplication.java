package com.mvp.empty;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Iterator;
import java.util.List;


/**
 * Created by liwei on 2017/3/15.
 */

/**
 * 增加android 全局异常捕获弹出提示退出应用，收集错误信息
 * Date： 2016/8/30.
 * Created by： v7428477
 * email：764213580@qq.com
 * 测试 新电脑github上传
 */
public class MApplication extends Application {//Application：用来管理应用程序的全局状态。在应用程序启动时Application会首先创建，然后才会根据情况(Intent)来启动相应的Activity和Service。本示例中将在自定义加强版的Application中注册未捕获异常处理器。
    /**
     * 对外提供整个应用生命周期的Context
     **/
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化微信
//        IWXAPI mIWXAPI = WXAPIFactory.createWXAPI(getApplicationContext(), MyUrl.APP_ID, true);
//        mIWXAPI.registerApp(MyUrl.APP_ID);
//        EMOptions options = new EMOptions();
//         默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//        初始化
//        EMClient.getInstance().init(instance, options);
//        在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(false);
//        int pid = android.os.Process.myPid();
//        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回


//        Intent intent = new Intent(gainContext(), LocationService.class);
//        startService(intent);


        /********************************************************这个是推送服务********************************************************/
//        PushAgent mPushAgent = PushAgent.getInstance(getApplicationContext());
//        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回device token\
//                ToolLog.e("deviceToken", deviceToken);
//                ToolSP.put(getApplicationContext(), ToolSP.DRIVERTOKEN, deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                ToolLog.e("deviceToken2", s + " " + s1);
//            }
//        });
//        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
//        mPushAgent.setDebugMode(false);//友盟
//
//        if (processAppName == null || !processAppName.equalsIgnoreCase(instance.getPackageName())) {
//            Log.e("", "enter the service process!");
//
//            // 则此application::onCreate 是被service 调用的，直接返回
//            return;
//
//        }
//
//        /*******************************************************这个是统计服务的*******************************************************/
//        if (!ToolLog.isDebug) {
//            MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(getApplicationContext(), "590954deb27b0a609b000ead", "");
//            MobclickAgent.startWithConfigure(config);
//            MobclickAgent.setDebugMode(true);
//
//        }
        //将崩溃日志记录到文件里
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);

//        MyUrl.setUrl(BuildConfig.ENVIRONMENT);//true 是测试的  false 是正式的

    }

    /**
     * 对外提供Application Context
     *
     * @return
     */
    public static Context gainContext() {
        return instance;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}