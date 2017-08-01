package com.superimagepicker.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.superimagepicker.R;

import java.io.File;
import java.text.DecimalFormat;


/**
 * Created by lilei on 2017/5/9.
 */
public class GlideUtils {

    private static GlideUtils sGlideUtils;
    ImageView imageView;
    private DiskCacheStrategy diskCache = DiskCacheStrategy.ALL;//磁盘缓存
    private boolean isSkipMemoryCache = false;//禁止内存缓存

    private GlideUtils() {
    }

    public static GlideUtils getInstances() {
        if (sGlideUtils == null) {
            synchronized (GlideUtils.class) {
                if (sGlideUtils == null) {
                    sGlideUtils = new GlideUtils();
                }
            }
        }
        return sGlideUtils;
    }

    public GlideUtils attach(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public GlideUtils injectImage(String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(diskCache)
                .skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.color.color_bg)
                .crossFade()
                .into(imageView);
        return this;
    }

    // 占位图为灰色背景
    public GlideUtils injectImageWithNull(String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(diskCache)
                .skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.color.color_bg)
                .crossFade()
                .into(imageView);
        return this;
    }

    // 加载本地图片文件
    public GlideUtils injectImageWithFile(File file) {
        Glide.with(imageView.getContext())
                .load(file)
                .centerCrop()
                .thumbnail(0.5f)
                .diskCacheStrategy(diskCache)
                .skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.color.color_bg)
                .crossFade()
                .into(imageView);
        return this;
    }

    // 占位图为黑色背景
    public GlideUtils injectImageWithBlack(String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .fitCenter()
                .diskCacheStrategy(diskCache)
                .skipMemoryCache(isSkipMemoryCache)
                .placeholder(Color.parseColor("#000000"))
//                .placeholder(R.color.transparent)
                .crossFade()
                .into(imageView);
        return this;
    }

    // 没有缓存
    public GlideUtils injectImageWithoutCache(String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.color.color_bg)
                .crossFade()
                .into(imageView);
        return this;
    }

    // 加载Bitmap
    public GlideUtils injectImageWithBitmap(String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(diskCache)
                .skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.color.color_bg)
                .into(imageView);
        return this;
    }

    public GlideUtils injectTarget(String url, Target target
            , Context context, @Nullable RequestListener requestListener) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(diskCache)
                .listener(requestListener)
                .into(target);
        return this;
    }

    public GlideUtils clearImage() {
        Glide.clear(imageView);
        imageView.setImageResource(R.mipmap.default_image);
        return this;
    }

    public GlideUtils clearImage(int res) {
        Glide.clear(imageView);
        imageView.setImageResource(res);
        return this;
    }

//    public void downloadImage(String url, Target target) {
//        Glide.with(SmartSRApp.getContext())
//                .load(url)
//                .asBitmap()
//                .diskCacheStrategy(diskCache)
//                .into(target);
//    }

    public String getCacheSize(Context context) {
        File cacheDir = Glide.getPhotoCacheDir(context);
        DecimalFormat format = new DecimalFormat("##0.00");
        String cacheSize = format.format(getCacheSize(cacheDir));
        return cacheSize + "M";
    }

    public String clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
        return getCacheSize(context);
    }

    private float getCacheSize(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                float size = 0;
                for (File f : children) {
                    size += getCacheSize(f);
                }
                return size;
            } else {
                float size = (float) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            return 0.0f;
        }
    }

}