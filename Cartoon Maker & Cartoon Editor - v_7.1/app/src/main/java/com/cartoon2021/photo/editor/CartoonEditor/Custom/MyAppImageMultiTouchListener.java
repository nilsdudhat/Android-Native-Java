package com.cartoon2021.photo.editor.CartoonEditor.Custom;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyAppImageMultiTouchListener implements View.OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    public boolean isRotateEnabled = true;
    public boolean isRotationEnabled = false;
    public boolean isTranslateEnabled = true;
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;
    Bitmap bitmap = null;
    boolean bt = false;
    private TouchCallbackListener listener = null;
    private int mActivePointerId = -1;
    private float mPrevX;
    private float mPrevY;
    private final MyAppCustomScaleGesture mScaleGestureDetector = new MyAppCustomScaleGesture(new ScaleGestureListener());

    private static float adjustAngle(float f) {
        return f > 180.0f ? f - 360.0f : f < -180.0f ? f + 360.0f : f;
    }

    private static void adjustTranslation(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }

    private static void computeRenderOffset(View view, float f, float f2) {
        if (view.getPivotX() != f || view.getPivotY() != f2) {
            float[] fArr = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr);
            view.setPivotX(f);
            view.setPivotY(f2);
            float[] fArr2 = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr2);
            float f3 = fArr2[1] - fArr[1];
            view.setTranslationX(view.getTranslationX() - (fArr2[0] - fArr[0]));
            view.setTranslationY(view.getTranslationY() - f3);
        }
    }

    public MyAppImageMultiTouchListener setOnTouchCallbackListener(TouchCallbackListener touchCallbackListener) {
        this.listener = touchCallbackListener;
        return this;
    }

    public MyAppImageMultiTouchListener enableRotation(boolean z) {
        this.isRotationEnabled = z;
        return this;
    }

    public MyAppImageMultiTouchListener setMinScale(float f) {
        this.minimumScale = f;
        return this;
    }


    public void move(View view, TransformInfo transformInfo) {
        if (this.isRotationEnabled) {
            view.setRotation(adjustAngle(view.getRotation() + transformInfo.deltaAngle));
        }
    }

    public boolean handleTransparency(View view, MotionEvent motionEvent) {
        try {
            if (((MyAppStickerView) view).getBorderVisbilty()) {
                return false;
            }
            boolean z = true;
            if (motionEvent.getAction() == 2 && this.bt) {
                return true;
            }
            if (motionEvent.getAction() != 1 || !this.bt) {
                int[] iArr = new int[2];
                view.getLocationOnScreen(iArr);
                int rawY = (int) (motionEvent.getRawY() - ((float) iArr[1]));
                float rotation = view.getRotation();
                Matrix matrix = new Matrix();
                matrix.postRotate(-rotation);
                float[] fArr = {(float) ((int) (motionEvent.getRawX() - ((float) iArr[0]))), (float) rawY};
                matrix.mapPoints(fArr);
                int i = (int) fArr[0];
                int i2 = (int) fArr[1];
                if (motionEvent.getAction() == 0) {
                    this.bt = false;
                    view.setDrawingCacheEnabled(true);
                    this.bitmap = Bitmap.createBitmap(view.getDrawingCache());
                    i = (int) (((float) i) * (((float) this.bitmap.getWidth()) / (((float) this.bitmap.getWidth()) * view.getScaleX())));
                    i2 = (int) (((float) i2) * (((float) this.bitmap.getHeight()) / (((float) this.bitmap.getHeight()) * view.getScaleX())));
                    view.setDrawingCacheEnabled(false);
                }
                if (i >= 0 && i2 >= 0 && i <= this.bitmap.getWidth()) {
                    if (i2 <= this.bitmap.getHeight()) {
                        if (this.bitmap.getPixel(i, i2) != 0) {
                            z = false;
                        }
                        if (motionEvent.getAction() != 0) {
                            return z;
                        }
                        this.bt = z;
                        return z;
                    }
                }
                return false;
            }
            this.bt = false;
            if (this.bitmap == null) {
                return true;
            }
            this.bitmap.recycle();
            return true;
        } catch (Exception unused) {
            unused.printStackTrace();
        }
        return true;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mScaleGestureDetector.onTouchEvent(view, motionEvent);
        int i = 0;
        if (handleTransparency(view, motionEvent)) {
            return false;
        }
        if (!this.isTranslateEnabled) {
            return true;
        }
        int action = motionEvent.getAction();
        int actionMasked = motionEvent.getActionMasked() & action;
        if (actionMasked == 6) {
            int i2 = (65280 & action) >> 8;
            if (motionEvent.getPointerId(i2) != this.mActivePointerId) {
                return true;
            }
            if (i2 == 0) {
                i = 1;
            }
            this.mPrevX = motionEvent.getX(i);
            this.mPrevY = motionEvent.getY(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            return true;
        } else if (actionMasked == 0) {
            TouchCallbackListener touchCallbackListener = this.listener;
            if (touchCallbackListener != null) {
                touchCallbackListener.onTouchCallback(view);
            }
            view.bringToFront();
            if (view instanceof MyAppStickerView) {
                ((MyAppStickerView) view).setBorderVisibility(true);
            }
            this.mPrevX = motionEvent.getX();
            this.mPrevY = motionEvent.getY();
            this.mActivePointerId = motionEvent.getPointerId(0);
            return true;
        } else if (actionMasked == 1) {
            this.mActivePointerId = -1;
            TouchCallbackListener touchCallbackListener2 = this.listener;
            if (touchCallbackListener2 != null) {
                touchCallbackListener2.onTouchUpCallback(view);
            }
            float rotation = view.getRotation();
            if (Math.abs(90.0f - Math.abs(rotation)) <= 5.0f) {
                rotation = rotation > 0.0f ? 90.0f : -90.0f;
            }
            if (Math.abs(0.0f - Math.abs(rotation)) <= 5.0f) {
                rotation = rotation > 0.0f ? 0.0f : -0.0f;
            }
            if (Math.abs(180.0f - Math.abs(rotation)) <= 5.0f) {
                rotation = rotation > 0.0f ? 180.0f : -180.0f;
            }
            view.setRotation(rotation);
            Log.i("testing", "Final Rotation : " + rotation);
            return true;
        } else if (actionMasked == 2) {
            int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
            if (findPointerIndex == -1) {
                return true;
            }
            float x = motionEvent.getX(findPointerIndex);
            float y = motionEvent.getY(findPointerIndex);
            if (this.mScaleGestureDetector.isInProgress()) {
                return true;
            }
            adjustTranslation(view, x - this.mPrevX, y - this.mPrevY);
            return true;
        } else if (actionMasked != 3) {
            return true;
        } else {
            this.mActivePointerId = -1;
            return true;
        }
    }

    public interface TouchCallbackListener {
        void onTouchCallback(View view);

        void onTouchUpCallback(View view);
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    private class ScaleGestureListener extends MyAppCustomScaleGesture.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private final MyAppCustomVector2D mPrevSpanVector;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new MyAppCustomVector2D();
        }

        public boolean onScaleBegin(View view, MyAppCustomScaleGesture myAppCustomScaleGesture) {
            this.mPivotX = myAppCustomScaleGesture.getFocusX();
            this.mPivotY = myAppCustomScaleGesture.getFocusY();
            this.mPrevSpanVector.set(myAppCustomScaleGesture.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, MyAppCustomScaleGesture myAppCustomScaleGesture) {
            TransformInfo transformInfo = new TransformInfo();
            float f = 0.0f;
            transformInfo.deltaAngle = MyAppImageMultiTouchListener.this.isRotateEnabled ? MyAppCustomVector2D.getAngle(this.mPrevSpanVector, myAppCustomScaleGesture.getCurrentSpanVector()) : 0.0f;
            transformInfo.deltaX = MyAppImageMultiTouchListener.this.isTranslateEnabled ? myAppCustomScaleGesture.getFocusX() - this.mPivotX : 0.0f;
            if (MyAppImageMultiTouchListener.this.isTranslateEnabled) {
                f = myAppCustomScaleGesture.getFocusY() - this.mPivotY;
            }
            transformInfo.deltaY = f;
            transformInfo.pivotX = this.mPivotX;
            transformInfo.pivotY = this.mPivotY;
            transformInfo.minimumScale = MyAppImageMultiTouchListener.this.minimumScale;
            transformInfo.maximumScale = MyAppImageMultiTouchListener.this.maximumScale;
            MyAppImageMultiTouchListener.this.move(view, transformInfo);
            return false;
        }
    }
}
