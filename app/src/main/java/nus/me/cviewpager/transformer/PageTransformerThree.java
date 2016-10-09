package nus.me.cviewpager.transformer;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class PageTransformerThree implements ViewPager.PageTransformer {
    private float MIN_SCALE = 0.5f;
    @SuppressLint("NewApi")
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
        view.setTranslationX(0);
        float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                * (1 - Math.abs(position));
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            view.getParent().requestLayout();
        }
    }
}