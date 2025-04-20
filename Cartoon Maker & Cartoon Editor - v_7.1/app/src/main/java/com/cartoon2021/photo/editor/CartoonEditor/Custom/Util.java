package com.cartoon2021.photo.editor.CartoonEditor.Custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class Util {

    public static int exifOrientationToDegrees(int i) {
        if (i == 6) {
            return 90;
        }
        if (i == 3) {
            return 180;
        }
        if (i == 8) {
            return 270;
        }
        return 0;
    }

    public static void toast(Fragment fragment, String str) {
    }

    public static int dpToPx(Context context, int i) {
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    public static void toast(Context context, String str) {
        if (context != null) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    public static void toast(Context context, String str, int i) {
        if (context != null) {
            Toast.makeText(context, str, i).show();
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int i) {
        if (!(i == 0 || bitmap == null)) {
            Matrix matrix = new Matrix();
            matrix.setRotate((float) i, ((float) bitmap.getWidth()) / 2.0f, ((float) bitmap.getHeight()) / 2.0f);
            try {
                Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (bitmap == createBitmap) {
                    return bitmap;
                }
                bitmap.recycle();
                return createBitmap;
            } catch (OutOfMemoryError unused) {
                unused.printStackTrace();
            }
        }
        return bitmap;
    }
}
