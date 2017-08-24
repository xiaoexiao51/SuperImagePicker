package com.superimagepicker.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.superimagepicker.R;

/**
 * Created by lilei on 2017/8/22.
 */
public class GlideUtils {

    private GlideUtils() {
        throw new RuntimeException("GlideUtils cannot be initialized!");
    }

    //设置加载中以及加载失败图片
    public static void loadWithDefult(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.color_bg).error(R.color.color_bg).centerCrop().into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadWithBlack(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.colorPrimary).error(R.color.colorPrimary).fitCenter().into(mImageView);
    }

    // Bitmap
    public static void loadWithBitmap(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asBitmap().placeholder(R.color.color_bg).error(R.color.color_bg).centerCrop().into(mImageView);
    }

    // Bitmap
    public static void loadWithGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asGif().placeholder(R.color.color_bg).error(R.color.color_bg).centerCrop().into(mImageView);
    }
}