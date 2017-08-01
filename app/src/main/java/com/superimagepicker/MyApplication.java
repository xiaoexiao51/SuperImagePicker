package com.superimagepicker;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.superimagepicker.utils.LogUtils;

/**
 * Created by MMM on 2017/7/24.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.setDebugMode("NetUtil");//初始化LogUtils
        initImageLoader();
    }

    private void initImageLoader() {
        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(getApplicationContext()));
    }
}
