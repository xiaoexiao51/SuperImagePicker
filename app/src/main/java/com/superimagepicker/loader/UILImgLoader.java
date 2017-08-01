package com.superimagepicker.loader;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;


public class UILImgLoader implements com.lqr.imagepicker.loader.ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        ImageSize size = new ImageSize(width, height);
        ImageLoader.getInstance().displayImage(Uri.parse("file://" + path).toString(), imageView, size);
    }

    @Override
    public void clearMemoryCache() {
    }
}
