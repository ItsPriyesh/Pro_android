package io.prolabs.pro.utils;

import android.view.View;

public class ViewUtils {

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

}
