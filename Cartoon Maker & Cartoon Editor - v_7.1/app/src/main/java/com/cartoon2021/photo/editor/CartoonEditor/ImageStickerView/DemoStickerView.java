package com.cartoon2021.photo.editor.CartoonEditor.ImageStickerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cartoon2021.photo.editor.R;

public abstract class DemoStickerView extends FrameLayout implements View.OnClickListener {
    private static final int BUTTON_SIZE_DP = 30;
    private static final int SELF_SIZE_DP = 100;
    public static final String TAG = "com.stickerView";

    public double centerX;

    public double centerY;
    private BorderView iv_border;
    private ImageView iv_delete;
    private ImageView iv_flip;
    private ImageView iv_scale;
    private OnTouchListener mTouchListener = new C11461();

    public float move_orgX = -1.0f;

    public float move_orgY = -1.0f;
    OnTouchSticker onTouchSticker;
    float rotate_newX = -1.0f;
    float rotate_newY = -1.0f;
    float rotate_orgX = -1.0f;
    float rotate_orgY = -1.0f;
    double scale_orgHeight = -1.0d;
    double scale_orgWidth = -1.0d;
    float scale_orgX = -1.0f;
    float scale_orgY = -1.0f;
    float this_orgX = -1.0f;
    float this_orgY = -1.0f;

    public interface OnTouchSticker {
        void onTouchedSticker();
    }


    public abstract View getMainView();


    public void onRotating() {
    }

    public void onScaling(boolean z) {
    }

    private static class BorderView extends View {
        public BorderView(Context context) {
            super(context);
        }

        public BorderView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public BorderView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
        }

        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            Log.v(TAG, "params.leftMargin: " + layoutParams.leftMargin);
            Rect rect = new Rect();
            rect.left = getLeft() - layoutParams.leftMargin;
            rect.top = getTop() - layoutParams.topMargin;
            rect.right = getRight() - layoutParams.rightMargin;
            rect.bottom = getBottom() - layoutParams.bottomMargin;
            Paint paint = new Paint();
            paint.setStrokeWidth(8.0f);
            paint.setColor(getResources().getColor(R.color.colorPrimary));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(rect, paint);
        }
    }

    class C11461 implements OnTouchListener {
        C11461() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int i;
            int i2 = 0;
            if (view.getTag().equals("DraggableViewGroup")) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    Log.v(TAG, "sticker view action down");
                    DemoStickerView.this.setControlItemsHidden(true);
                    DemoStickerView.this.onTouchSticker.onTouchedSticker();
                    float unused = DemoStickerView.this.move_orgX = motionEvent.getRawX();
                    float unused2 = DemoStickerView.this.move_orgY = motionEvent.getRawY();
                    return true;
                } else if (action == 1) {
                    Log.v(TAG, "sticker view action up");
                    DemoStickerView.this.setControlItemsHidden(false);
                    return true;
                } else if (action != 2) {
                    return true;
                } else {
                    Log.v(TAG, "sticker view action move");
                    float rawX = motionEvent.getRawX() - DemoStickerView.this.move_orgX;
                    float rawY = motionEvent.getRawY() - DemoStickerView.this.move_orgY;
                    setX(getX() + rawX);
                    setY(getY() + rawY);
                    DemoStickerView.this.move_orgX = motionEvent.getRawX();
                    DemoStickerView.this.move_orgY = motionEvent.getRawY();
                    return true;
                }
            } else if (!view.getTag().equals("iv_scale")) {
                return true;
            } else {
                int action2 = motionEvent.getAction();
                if (action2 == 0) {
                    Log.v(TAG, "iv_scale action down");
                    this_orgX = getX();
                    this_orgY = getY();
                    DemoStickerView.this.scale_orgX = motionEvent.getRawX();
                    DemoStickerView.this.scale_orgY = motionEvent.getRawY();
                    scale_orgWidth = (double) getLayoutParams().width;
                    scale_orgHeight = (double) getLayoutParams().height;
                    DemoStickerView.this.rotate_orgX = motionEvent.getRawX();
                    DemoStickerView.this.rotate_orgY = motionEvent.getRawY();
                    centerX = (double) (getX() + ((View) DemoStickerView.this.getParent()).getX() + (((float) DemoStickerView.this.getWidth()) / 2.0f));
                    int identifier = DemoStickerView.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (identifier > 0) {
                        i2 = DemoStickerView.this.getResources().getDimensionPixelSize(identifier);
                    }
                    double d = (double) i2;
                    double y = (double) (getY() + ((View) DemoStickerView.this.getParent()).getY());
                    Double.isNaN(y);
                    Double.isNaN(d);
                    double d2 = y + d;
                    double height = (double) (((float) DemoStickerView.this.getHeight()) / 2.0f);
                    Double.isNaN(height);
                    centerY = d2 + height;
                    return true;
                } else if (action2 == 1) {
                    Log.v(TAG, "iv_scale action up");
                    return true;
                } else if (action2 != 2) {
                    return true;
                } else {
                    Log.v(TAG, "iv_scale action move");
                    DemoStickerView.this.rotate_newX = motionEvent.getRawX();
                    DemoStickerView.this.rotate_newY = motionEvent.getRawY();
                    double atan2 = Math.atan2((double) (motionEvent.getRawY() - DemoStickerView.this.scale_orgY), (double) (motionEvent.getRawX() - scale_orgX));
                    double d3 = (double) DemoStickerView.this.scale_orgY;
                    double access$300 = DemoStickerView.this.centerY;
                    Double.isNaN(d3);
                    double d4 = d3 - access$300;
                    double d5 = (double) DemoStickerView.this.scale_orgX;
                    double access$200 = DemoStickerView.this.centerX;
                    Double.isNaN(d5);
                    double abs = (Math.abs(atan2 - Math.atan2(d4, d5 - access$200)) * 180.0d) / 3.141592653589793d;
                    Log.v(TAG, "angle_diff: " + abs);
                    double access$400 = getLength(centerX, centerY, (double) scale_orgX, (double) scale_orgY);
                    double access$2002 = centerX;
                    double access$3002 = centerY;
                    float rawX2 = motionEvent.getRawX();
                    String str = TAG;
                    double access$4002 = getLength(access$2002, access$3002, (double) rawX2, (double) motionEvent.getRawY());
                    int access$500 = convertDpToPixel(100.0f, getContext());
                    if (access$4002 > access$400 && (abs < 25.0d || Math.abs(abs - 180.0d) < 25.0d)) {
                        double round = (double) Math.round(Math.max((double) Math.abs(motionEvent.getRawX() - scale_orgX), (double) Math.abs(motionEvent.getRawY() - scale_orgY)));
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        double d6 = (double) layoutParams.width;
                        Double.isNaN(d6);
                        Double.isNaN(round);
                        layoutParams.width = (int) (d6 + round);
                        ViewGroup.LayoutParams layoutParams2 = getLayoutParams();
                        double d7 = (double) layoutParams2.height;
                        Double.isNaN(d7);
                        Double.isNaN(round);
                        layoutParams2.height = (int) (d7 + round);
                        onScaling(true);
                    } else if (access$4002 < access$400 && ((abs < 25.0d || Math.abs(abs - 180.0d) < 25.0d) && getLayoutParams().width > (i = access$500 / 2) && getLayoutParams().height > i)) {
                        double round2 = (double) Math.round(Math.max((double) Math.abs(motionEvent.getRawX() - scale_orgX), (double) Math.abs(motionEvent.getRawY() - scale_orgY)));
                        ViewGroup.LayoutParams layoutParams3 = getLayoutParams();
                        double d8 = (double) layoutParams3.width;
                        Double.isNaN(d8);
                        Double.isNaN(round2);
                        layoutParams3.width = (int) (d8 - round2);
                        ViewGroup.LayoutParams layoutParams4 = getLayoutParams();
                        double d9 = (double) layoutParams4.height;
                        Double.isNaN(d9);
                        Double.isNaN(round2);
                        layoutParams4.height = (int) (d9 - round2);
                        onScaling(false);
                    }
                    double rawY2 = (double) motionEvent.getRawY();
                    double access$3003 = centerY;
                    Double.isNaN(rawY2);
                    double d10 = rawY2 - access$3003;
                    double rawX3 = (double) motionEvent.getRawX();
                    double access$2003 = centerX;
                    Double.isNaN(rawX3);
                    double atan22 = (Math.atan2(d10, rawX3 - access$2003) * 180.0d) / 3.141592653589793d;
                    String str2 = str;
                    Log.v(str2, "log angle: " + atan22);
                    setRotation(((float) atan22) - 45.0f);
                    Log.v(str2, "getRotation(): " + getRotation());
                    onRotating();
                    rotate_orgX = rotate_newX;
                    rotate_orgY = rotate_newY;
                    scale_orgX = motionEvent.getRawX();
                    scale_orgY = motionEvent.getRawY();
                    postInvalidate();
                    requestLayout();
                    return true;
                }
            }
        }
    }

    public DemoStickerView(Context context, OnTouchSticker onTouchSticker2) {
        super(context);
        init(context);
        this.onTouchSticker = onTouchSticker2;
    }

    public DemoStickerView(Context context, AttributeSet attributeSet, OnTouchSticker onTouchSticker2) {
        super(context, attributeSet);
        init(context);
        this.onTouchSticker = onTouchSticker2;
    }

    public DemoStickerView(Context context, AttributeSet attributeSet, int i, OnTouchSticker onTouchSticker2) {
        super(context, attributeSet, i);
        init(context);
        this.onTouchSticker = onTouchSticker2;
    }

    private void init(Context context) {
        this.iv_border = new BorderView(context);
        this.iv_scale = new ImageView(context);
        this.iv_delete = new ImageView(context);
        this.iv_flip = new ImageView(context);
        this.iv_scale.setImageResource(R.drawable.zoominout);
        this.iv_delete.setImageResource(R.drawable.remove);
        this.iv_flip.setImageResource(R.drawable.flip2);
        setTag("DraggableViewGroup");
        this.iv_border.setTag("iv_border");
        this.iv_scale.setTag("iv_scale");
        this.iv_delete.setTag("iv_delete");
        this.iv_flip.setTag("iv_flip");
        int convertDpToPixel = convertDpToPixel(30.0f, getContext()) / 2;
        int convertDpToPixel2 = convertDpToPixel(100.0f, getContext());
        LayoutParams layoutParams = new LayoutParams(convertDpToPixel2, convertDpToPixel2);
        layoutParams.gravity = 17;
        LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        layoutParams2.setMargins(40, 40, 40, 40);
        LayoutParams layoutParams3 = new LayoutParams(-2, -2);
        layoutParams3.setMargins(convertDpToPixel, convertDpToPixel, convertDpToPixel, convertDpToPixel);
        LayoutParams layoutParams4 = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        layoutParams4.gravity = 85;
        LayoutParams layoutParams5 = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        layoutParams5.gravity = 53;
        LayoutParams layoutParams6 = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        layoutParams6.gravity = 51;
        setLayoutParams(layoutParams);
        addView(getMainView(), layoutParams2);
        addView(this.iv_border, layoutParams3);
        addView(this.iv_scale, layoutParams4);
        addView(this.iv_delete, layoutParams5);
        addView(this.iv_flip, layoutParams6);
        setOnTouchListener(this.mTouchListener);
        setOnClickListener(this);
        this.iv_scale.setOnTouchListener(this.mTouchListener);
        this.iv_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (getParent() != null) {
                    ((ViewGroup) getParent()).removeView(DemoStickerView.this);
                }
            }
        });
        this.iv_flip.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.v(TAG, "flip the view");
                View mainView = getMainView();
                float f = -180.0f;
                if (mainView.getRotationY() == -180.0f) {
                    f = 0.0f;
                }
                mainView.setRotationY(f);
                mainView.invalidate();
                requestLayout();
            }
        });
    }

    public boolean isFlip() {
        return getMainView().getRotationY() == -180.0f;
    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public double getLength(double d, double d2, double d3, double d4) {
        return Math.sqrt(Math.pow(d4 - d2, 2.0d) + Math.pow(d3 - d, 2.0d));
    }

    private float[] getRelativePos(float f, float f2) {
        Log.v("ken", "getRelativePos getX:" + ((View) getParent()).getX());
        Log.v("ken", "getRelativePos getY:" + ((View) getParent()).getY());
        float[] fArr = {f - ((View) getParent()).getX(), f2 - ((View) getParent()).getY()};
        Log.v(TAG, "getRelativePos absY:" + f2);
        Log.v(TAG, "getRelativePos relativeY:" + fArr[1]);
        return fArr;
    }

    public void setControlItemsHidden(boolean z) {
        if (z) {
            this.iv_border.setVisibility(View.INVISIBLE);
            this.iv_scale.setVisibility(View.INVISIBLE);
            this.iv_delete.setVisibility(View.INVISIBLE);
            this.iv_flip.setVisibility(View.INVISIBLE);
            return;
        }
        this.iv_border.setVisibility(View.VISIBLE);
        this.iv_scale.setVisibility(View.VISIBLE);
        this.iv_delete.setVisibility(View.VISIBLE);
        this.iv_flip.setVisibility(View.VISIBLE);
    }


    public View getImageViewFlip() {
        return this.iv_flip;
    }

    public void onClick(View view) {
        setControlItemsHidden(false);
    }


    public static int convertDpToPixel(float f, Context context) {
        return (int) ((((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * f);
    }

    public void setControlsVisibility(boolean z) {
        if (z) {
            this.iv_border.setVisibility(View.VISIBLE);
            this.iv_delete.setVisibility(View.VISIBLE);
            this.iv_flip.setVisibility(View.VISIBLE);
            this.iv_scale.setVisibility(View.VISIBLE);
            return;
        }
        this.iv_border.setVisibility(View.GONE);
        this.iv_delete.setVisibility(View.GONE);
        this.iv_flip.setVisibility(View.GONE);
        this.iv_scale.setVisibility(View.GONE);
    }
}
