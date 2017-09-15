package com.mvp.empty.Util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by liwei on 2017/4/11.
 * 这个是缓存的东西。 设置了大小， 图片缓存的格式，之类的
 */

public class GlideCache implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //设置磁盘缓存目录（和创建的缓存目录相同）
        String storageDirectory = context.getCacheDir() + "/" + GlideCatchConfig.GLIDE_CARCH_DIR;
        //设置缓存大小
        builder.setDiskCache(new DiskLruCacheFactory(storageDirectory, GlideCatchConfig.GLIDE_CATCH_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
