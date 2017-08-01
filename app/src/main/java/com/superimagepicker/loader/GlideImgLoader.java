package com.superimagepicker.loader;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.lqr.imagepicker.loader.ImageLoader;

import java.io.File;


public class GlideImgLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        Glide.with(activity)
                .load(new File(path))
                .centerCrop()
                .thumbnail(0.5f)
                .override(width, height)
//                .animate(R.anim.image_picker_in)
//                .placeholder(R.mipmap.default_image)
//                .error(R.mipmap.default_image)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }

    ViewPropertyAnimation.Animator animator = new ViewPropertyAnimation.Animator() {
        @Override
        public void animate(View view) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
            ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(scaleX, scaleY, rotation);
            set.setDuration(500);
            set.start();
        }
    };
}
