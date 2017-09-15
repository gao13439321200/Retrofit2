# Retrofit2
Retrofit2+MVP+Dagger2空框架
使用的时候需要在项目的build中添加：
dependencies {
        classpath 'com.android.tools.build:gradle:2.2.3'
        //需要添加下边这行代码
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

项目注意事项：
1、本项目使用的MVP已经配置完成，使用时需自己创建对应的presenter和view包，创建的presenter文件需继承
BasePresenter，view文件需继承BaseView，并且presenter的构造函数需要添加注解@Inject，直接使用mAppModel
调用对应的请求方式就可以网络请求了。创建的Activity需继承BaseActivity
2、dagger2的使用方法：在activity中的onBusiness方法中添加mBaseComponent.inject(this)，声明的presenter
变量需要添加注解@Inject，方可使用
3、Retrofit2的网络请求方式有8种
 （1）retrofit_Post
 （2）retrofit_Get
 （3）retrofit_Put
 （4）retrofit_delete
 （5）retrofit_Post_restful
 （6）retrofit_Get_restful
 （7）retrofit_Put_restful
 （8）retrofit_delete_restful
    八种均可以传递body或map格式参数
4、推荐将接口写在MyUrl类中，方便调用，在JsonUtil文件中使用gson来解析网络请求的数据
5、ButterKnife的使用，在BaseActivity中已经配置完了，无需在每个文件中配置
