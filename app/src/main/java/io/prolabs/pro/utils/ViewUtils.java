package io.prolabs.pro.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

import io.prolabs.pro.R;

public class ViewUtils {

    public static final int ANIM_TURN_UP = R.anim.turn_up;
    public static final int ANIM_SLIDE_IN_TOP = R.anim.abc_slide_in_top;

    public static void hide(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public static void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static boolean isHidden(View view) {
        return view.getVisibility() == View.INVISIBLE;
    }

    /*public static void animateTurnUp(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, ANIM_TURN_UP));
        view.setVisibility(View.VISIBLE);
    }

    public static void animateSlideInTop(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, ANIM_SLIDE_IN_TOP));
        view.setVisibility(View.VISIBLE);
    }*/

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}
