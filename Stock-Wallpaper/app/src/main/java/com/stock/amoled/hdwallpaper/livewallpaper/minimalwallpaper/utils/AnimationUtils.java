package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils;

import android.app.Activity;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;

public class AnimationUtils {

    public static void closeActivity(Activity activity) {
        activity.overridePendingTransition(R.anim.anim_activity_stay, R.anim.anim_activity_slide_to_bottom);
    }

    public static void startActivity(Activity activity) {
        activity.overridePendingTransition(R.anim.anim_activity_slide_from_bottom, R.anim.anim_activity_stay);
    }
}
