package com.example.lihui20.testhttp.animation;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by lihui20 on 2017/5/9.
 */

public class AnimationUtils {
    // alpha
    public static void alphaAnimation(View view) {
        //
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f,
                0.15f);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(Animation.REVERSE);
        animator.setRepeatCount(Animation.INFINITE);// 重复次数
        animator.start();
        //
    }
}
