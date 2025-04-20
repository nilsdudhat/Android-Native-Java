package com.cartoon2021.photo.editor.CartoonEditor.Custom;

import android.graphics.PointF;

public class MyAppCustomVector2D extends PointF {
    public MyAppCustomVector2D(float f, float f2) {
        super(f, f2);
    }

    public MyAppCustomVector2D() {
    }

    public static float getAngle(MyAppCustomVector2D myAppCustomVector2D, MyAppCustomVector2D myAppCustomVector2D2) {
        myAppCustomVector2D.normalize();
        myAppCustomVector2D2.normalize();
        return (float) ((Math.atan2(myAppCustomVector2D2.y, myAppCustomVector2D2.x) - Math.atan2(myAppCustomVector2D.y, myAppCustomVector2D.x)) * 57.29577951308232d);
    }

    public void normalize() {
        float sqrt = (float) Math.sqrt((this.x * this.x) + (this.y * this.y));
        this.x /= sqrt;
        this.y /= sqrt;
    }
}
