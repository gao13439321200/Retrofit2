package com.mvp.empty.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.mvp.empty.R;
import com.mvp.empty.Util.ToolSP;
import com.mvp.empty.common.GlideRoundTransform;
import com.mvp.empty.common.MyDialog;
import com.mvp.empty.common.MyDialogOnClick;
import com.mvp.empty.data.BaseView;
import com.mvp.empty.data.dagger.BaseComponent;
import com.mvp.empty.data.dagger.BaseModul;
import com.mvp.empty.data.dagger.DaggerBaseComponent;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Gao on 2017/3/14.
 * 基类
 */

public abstract class BaseActivity extends Activity implements BaseView {

    @BindView(R.id.base_tv_left)
    public TextView mBaseTvLeft;
    @BindView(R.id.base_ll_left)
    public LinearLayout mBaseLLLeft;
    @BindView(R.id.base_tv_title)
    public TextView mBaseTvTitle;
    @BindView(R.id.base_tv_right)
    public TextView mBaseTvRight;
    @BindView(R.id.base_ll_right)
    public LinearLayout mBaseLLRight;
    @BindView(R.id.base_Relative_title)
    public RelativeLayout mBaseRelativeTitle;
    protected RelativeLayout mBaseRelative;
    public PopupWindow popupWindow;
    //    public TimePickerView pvTime;
    public BaseComponent mBaseComponent;
    //    public int userID = 0;//用户id
    public String childNo;
    public String data;
    public String mobile = "13439321200";//用户手机号
    public int page = 1;//页数
    public int pageSize = 10;//每页条数

//    public int CurrentPage = 1;//默认为1
//    public int PageSize = 10;//默认为10


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PushAgent.getInstance(this).onAppStart();//TODO 数据统计！！！这里需要 开启一下
        BaseSetContentView();//加载布局
        systembar(); //系统通知栏
//      initState(); //这个是 沉浸式的一种
        mBaseComponent = DaggerBaseComponent.builder().baseModul(new BaseModul(this, this)).build();
        overridePendingTransition(R.anim.zoomout, R.anim.zoomin); //补间动画动画
        doBusiness();//处理业务逻辑
    }

    /**
     * 获取子类的布局
     *
     * @return 布局id
     */
    public abstract int getLayoutID();

    /**
     * 获取子类的布局
     *
     * @return 布局id
     */
    public abstract void doBusiness();


    /**
     * 加载标题布局和主界面布局
     */
    private void BaseSetContentView() {
        setContentView(R.layout.activity_base);//加载标题布局
        mBaseRelative = (RelativeLayout) findViewById(R.id.base_Relative);//需要手动找此控件，否则会报空指针，因为没有绑定ButterKnife
        mBaseRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(getLayoutID(), null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        if (view != null)
            mBaseRelative.addView(view, layoutParams);
        ButterKnife.bind(this);//绑定ButterKnife
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mBaseTvTitle.setText(title);
    }

    /**
     * 隐藏标题栏
     */
    public void hideTitle() {
        mBaseRelativeTitle.setVisibility(View.GONE);
    }

    public void hideLeftBtn() {
        mBaseLLLeft.setVisibility(View.GONE);
    }

    public void showLeftBtn() {
        mBaseLLLeft.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左上角按钮
     */
    public void setBaseTvLeft(String text, int drawableID) {
        mBaseLLLeft.setVisibility(View.VISIBLE);
        mBaseTvLeft.setText(text);
        mBaseTvLeft.setBackgroundResource(drawableID);
    }

    /**
     * 设置左上角按钮
     */
    public void setBaseTvLeft(String text) {
        mBaseLLLeft.setVisibility(View.VISIBLE);
        mBaseTvLeft.setText(text);
    }


    /**
     * 设置右上角按钮
     */
    public void setBaseTvRight(String text, int drawableID) {
        mBaseLLRight.setVisibility(View.VISIBLE);
        mBaseTvRight.setText(text);
        mBaseTvRight.setBackgroundResource(drawableID);
    }

    /**
     * 设置右上角按钮
     */
    public void setBaseTvRight(String text) {
        mBaseLLRight.setVisibility(View.VISIBLE);
        mBaseTvRight.setText(text);
    }

    @OnClick({R.id.base_ll_left, R.id.base_ll_right})
    public void onTitleClick(View view) {
        switch (view.getId()) {
            case R.id.base_ll_left:
                setLeftClick(view);
                break;
            case R.id.base_ll_right:
                setRightClick(view);
                break;
        }
    }

    //右上角按钮的点击事件
    public void setRightClick(View v) {
    }

    //左上角按钮的点击事件
    public void setLeftClick(View v) {
        this.finish();
    }


//    //弹出时间选择器
//    public void showTimeDialog(TimePickerView.Type type, int year) {
//        if (pvTime != null) {
//            pvTime.dismiss();
//        }
//        //时间选择器
//        pvTime = new TimePickerView.Builder(this, this).setType(type)
//                .setStartYear(year)
//                .setEndYear(2100)
//                .setLabel("", "", "", "", "", "")
//                .setDividerColor(Color.DKGRAY)
//                .setContentSize(20)
//                .build();
//        pvTime.show();
//    }

//    @Override
//    public void onTimeSelect(Date date, View v) {//选中事件回调
//    }

    /**
     * 打电话弹窗
     *
     * @param msg      提示信息
     * @param phoneNum 电话号码
     */
//    private MyDialog myDialogPhone;
//    public void showPhoneDialog(String msg, final String phoneNum) {
//        if (ToolString.checkPermission(this, "android.permission.CALL_PHONE")) {
//            MyDialog myDialogPhone = new MyDialog(this, new MyDialogOnClick() {
//                @Override
//                public void onOKClick() {
////                Toast.makeText(BaseActivity.this, "呼叫番薯！我是土豆！", Toast.LENGTH_SHORT).show();
//                    Intent mIntent = new Intent(Intent.ACTION_CALL);
//                    mIntent.setData(Uri.parse("tel:" + phoneNum));
//                    if (ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    startActivity(mIntent);
//                }
//
//                @Override
//                public void onCancelClick() {
//                }
//            }, 2);
//            myDialogPhone.setNameText(msg);
//            myDialogPhone.setButtonOK("呼叫");
//            myDialogPhone.setButtonCancel("取消");
//            myDialogPhone.show();
//        } else {
//            showPermissionDialog("请开启拨打电话权限", true).show();
//        }
//    }


    /**
     * 设置权限弹窗
     *
     * @param msg    提示信息
     * @param cancel 可不可以取消
     * @return 弹窗对象
     */
    public MyDialog showPermissionDialog(String msg, final boolean cancel) {
        final MyDialog myDialog = new MyDialog(this, new MyDialogOnClick() {
            @Override
            public void onOKClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
                    // 进入设置系统应用权限界面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                    finish();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
                    // 进入设置系统应用权限界面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelClick() {
                if (!cancel) {//未设置定位需要退出程序
                    finish();
                    System.exit(0);
                }
            }
        }, 2);
        myDialog.setNameText(msg);
        myDialog.setButtonOK("去设置");
        myDialog.setButtonCancel("偏偏不要");
        myDialog.setCancelable(cancel);
        return myDialog;
    }


    //沉浸式
    private void systembar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(Color.parseColor("#00000000"));// 通知栏所需颜色
    }

    //沉浸式
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //获取本地userID
    public int getUserID() {
//        return userID = (int) ToolSP.get(this, "userId", 0);
//        return userID = (int) (Math.random() * 100000);
//        return 5486;
//        return 5486;//车辆管理测试使用这个id
//        return 37;//车辆管理测试使用这个id
        return (int) ToolSP.get(this, ToolSP.USERID, -1);
    }

    //加载图片
    public void setImageView(String url, ImageView imageView) {
        Glide.with(this).load(url).signature(new StringSignature(Math.random() + "")).transform(new GlideRoundTransform(this, 20)).centerCrop().into(imageView);
    }



}
