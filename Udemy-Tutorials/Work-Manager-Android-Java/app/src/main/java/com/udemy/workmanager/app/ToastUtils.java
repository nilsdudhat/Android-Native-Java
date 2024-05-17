package com.udemy.workmanager.app;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    static Toast toast;

    public static void showShortToast(Context context, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongToast(Context context, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
