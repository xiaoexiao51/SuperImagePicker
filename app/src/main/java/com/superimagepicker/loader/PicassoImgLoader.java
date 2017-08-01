package com.superimagepicker.loader;

import android.app.Activity;
import android.widget.ImageView;

import com.lqr.imagepicker.loader.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by MMM on 2017/7/24.
 */

public class PicassoImgLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        Picasso.with(activity)
                .load(new File(path))
                .centerCrop()
                .resize(width / 4 * 3, height / 4 * 3)
//                .placeholder(R.mipmap.default_image)
//                .error(R.mipmap.default_image)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}