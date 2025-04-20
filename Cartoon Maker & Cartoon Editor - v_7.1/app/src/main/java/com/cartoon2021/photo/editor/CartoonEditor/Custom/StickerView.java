package com.cartoon2021.photo.editor.CartoonEditor.Custom;


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

public abstract class StickerView extends FrameLayout {

    private static final int BUTTON_SIZE_DP = 30;
    private static final int SELF_SIZE_DP = 100;
    public static final String TAG = "com.knef.stickerView";

    public double centerX;

    public double centerY;
    private BorderView iv_border;
    public static ImageView iv_delete;
    private ImageView iv_flip;
    private ImageView iv_scale;

    private final OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int i;
            if (!view.getTag().equals("DraggableViewGroup") && view.getTag().equals("iv_scale")) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    int i2 = 0;
                    Log.v(StickerView.TAG, "iv_scale action down");
                    StickerView stickerView = StickerView.this;
                    float unused = stickerView.this_orgX = stickerView.getX();
                    StickerView stickerView2 = StickerView.this;
                    float unused2 = stickerView2.this_orgY = stickerView2.getY();
                    float unused3 = StickerView.this.scale_orgX = motionEvent.getRawX();
                    float unused4 = StickerView.this.scale_orgY = motionEvent.getRawY();
                    StickerView stickerView3 = StickerView.this;
                    double unused5 = stickerView3.scale_orgWidth = (double) stickerView3.getLayoutParams().width;
                    StickerView stickerView4 = StickerView.this;
                    double unused6 = stickerView4.scale_orgHeight = (double) stickerView4.getLayoutParams().height;
                    float unused7 = StickerView.this.rotate_orgX = motionEvent.getRawX();
                    float unused8 = StickerView.this.rotate_orgY = motionEvent.getRawY();
                    StickerView stickerView5 = StickerView.this;
                    double unused9 = stickerView5.centerX = (double) (stickerView5.getX() + ((View) StickerView.this.getParent()).getX() + (((float) StickerView.this.getWidth()) / 2.0f));
                    int identifier = StickerView.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (identifier > 0) {
                        i2 = StickerView.this.getResources().getDimensionPixelSize(identifier);
                    }
                    StickerView stickerView6 = StickerView.this;
                    double y = (double) (stickerView6.getY() + ((View) StickerView.this.getParent()).getY());
                    double d = (double) i2;
                    Double.isNaN(y);
                    Double.isNaN(d);
                    double d2 = y + d;
                    double height = (double) (((float) StickerView.this.getHeight()) / 2.0f);
                    Double.isNaN(height);
                    double unused10 = stickerView6.centerY = d2 + height;
                } else if (action == 1) {
                    Log.v(StickerView.TAG, "iv_scale action up");
                } else if (action == 2) {
                    Log.v(StickerView.TAG, "iv_scale action move");
                    float unused11 = StickerView.this.rotate_newX = motionEvent.getRawX();
                    float unused12 = StickerView.this.rotate_newY = motionEvent.getRawY();
                    double atan2 = Math.atan2((double) (motionEvent.getRawY() - StickerView.this.scale_orgY), (double) (motionEvent.getRawX() - StickerView.this.scale_orgX));
                    double access$300 = (double) StickerView.this.scale_orgY;
                    double access$900 = StickerView.this.centerY;
                    Double.isNaN(access$300);
                    double d3 = access$300 - access$900;
                    double access$200 = (double) StickerView.this.scale_orgX;
                    double access$800 = StickerView.this.centerX;
                    Double.isNaN(access$200);
                    double abs = (Math.abs(atan2 - Math.atan2(d3, access$200 - access$800)) * 180.0d) / 3.141592653589793d;
                    Log.v(StickerView.TAG, "angle_diff: " + abs);
                    StickerView stickerView7 = StickerView.this;
                    double access$1200 = stickerView7.getLength(stickerView7.centerX, StickerView.this.centerY, (double) StickerView.this.scale_orgX, (double) StickerView.this.scale_orgY);
                    StickerView stickerView8 = StickerView.this;
                    double d4 = abs;
                    double access$12002 = stickerView8.getLength(stickerView8.centerX, StickerView.this.centerY, (double) motionEvent.getRawX(), (double) motionEvent.getRawY());
                    int access$1300 = StickerView.convertDpToPixel(100.0f, StickerView.this.getContext());
                    if (access$12002 > access$1200 && (d4 < 25.0d || Math.abs(d4 - 180.0d) < 25.0d)) {
                        double round = (double) Math.round(Math.max((double) Math.abs(motionEvent.getRawX() - StickerView.this.scale_orgX), (double) Math.abs(motionEvent.getRawY() - StickerView.this.scale_orgY)));
                        LayoutParams layoutParams = (LayoutParams) StickerView.this.getLayoutParams();
                        double d5 = (double) layoutParams.width;
                        Double.isNaN(d5);
                        Double.isNaN(round);
                        layoutParams.width = (int) (d5 + round);
                        LayoutParams layoutParams2 = (LayoutParams) StickerView.this.getLayoutParams();
                        double d6 = (double) layoutParams2.height;
                        Double.isNaN(d6);
                        Double.isNaN(round);
                        layoutParams2.height = (int) (d6 + round);
                        StickerView.this.onScaling(true);
                    } else if (access$12002 < access$1200 && ((d4 < 25.0d || Math.abs(d4 - 180.0d) < 25.0d) && StickerView.this.getLayoutParams().width > (i = access$1300 / 2) && StickerView.this.getLayoutParams().height > i)) {
                        double round2 = (double) Math.round(Math.max((double) Math.abs(motionEvent.getRawX() - StickerView.this.scale_orgX), (double) Math.abs(motionEvent.getRawY() - StickerView.this.scale_orgY)));
                        LayoutParams layoutParams3 = (LayoutParams) StickerView.this.getLayoutParams();
                        double d7 = (double) layoutParams3.width;
                        Double.isNaN(d7);
                        Double.isNaN(round2);
                        layoutParams3.width = (int) (d7 - round2);
                        LayoutParams layoutParams4 = (LayoutParams) StickerView.this.getLayoutParams();
                        double d8 = (double) layoutParams4.height;
                        Double.isNaN(d8);
                        Double.isNaN(round2);
                        layoutParams4.height = (int) (d8 - round2);
                        StickerView.this.onScaling(false);
                    }
                    double rawY = (double) motionEvent.getRawY();
                    double access$9002 = StickerView.this.centerY;
                    Double.isNaN(rawY);
                    double d9 = rawY - access$9002;
                    double rawX = (double) motionEvent.getRawX();
                    double access$8002 = StickerView.this.centerX;
                    Double.isNaN(rawX);
                    double atan22 = (Math.atan2(d9, rawX - access$8002) * 180.0d) / 3.141592653589793d;
                    Log.v(StickerView.TAG, "log angle: " + atan22);
                    StickerView.this.setRotation(((float) atan22) - 45.0f);
                    Log.v(StickerView.TAG, "getRotation(): " + StickerView.this.getRotation());
                    StickerView.this.onRotating();
                    StickerView stickerView9 = StickerView.this;
                    float unused13 = stickerView9.rotate_orgX = stickerView9.rotate_newX;
                    StickerView stickerView10 = StickerView.this;
                    float unused14 = stickerView10.rotate_orgY = stickerView10.rotate_newY;
                    float unused15 = StickerView.this.scale_orgX = motionEvent.getRawX();
                    float unused16 = StickerView.this.scale_orgY = motionEvent.getRawY();
                    StickerView.this.postInvalidate();
                    StickerView.this.requestLayout();
                }
            }
            int action2 = motionEvent.getAction();
            if (action2 == 0) {
                Log.v(StickerView.TAG, "sticker view action down");
                float unused17 = StickerView.this.move_orgX = motionEvent.getRawX();
                float unused18 = StickerView.this.move_orgY = motionEvent.getRawY();
                return true;
            } else if (action2 == 1) {
                Log.v(StickerView.TAG, "sticker view action up");
                return true;
            } else if (action2 != 2) {
                return true;
            } else {
                Log.v(StickerView.TAG, "sticker view action move");
                float rawY2 = motionEvent.getRawY() - StickerView.this.move_orgY;
                StickerView stickerView11 = StickerView.this;
                stickerView11.setX(stickerView11.getX() + (motionEvent.getRawX() - StickerView.this.move_orgX));
                StickerView stickerView12 = StickerView.this;
                stickerView12.setY(stickerView12.getY() + rawY2);
                float unused19 = StickerView.this.move_orgX = motionEvent.getRawX();
                float unused20 = StickerView.this.move_orgY = motionEvent.getRawY();
                return true;
            }
        }
    };

    public float move_orgX = -1.0f;

    public float move_orgY = -1.0f;

    public float rotate_newX = -1.0f;

    public float rotate_newY = -1.0f;

    public float rotate_orgX = -1.0f;

    public float rotate_orgY = -1.0f;

    public double scale_orgHeight = -1.0d;

    public double scale_orgWidth = -1.0d;

    public float scale_orgX = -1.0f;

    public float scale_orgY = -1.0f;

    public float this_orgX = -1.0f;

    public float this_orgY = -1.0f;


    public abstract View getMainView();


    public void onRotating() {
    }


    public void onScaling(boolean z) {
    }

    private class BorderView extends View {
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
            Log.v(StickerView.TAG, "params.leftMargin: " + layoutParams.leftMargin);
            Rect rect = new Rect();
            rect.left = getLeft() - layoutParams.leftMargin;
            rect.top = getTop() - layoutParams.topMargin;
            rect.right = getRight() - layoutParams.rightMargin;
            rect.bottom = getBottom() - layoutParams.bottomMargin;
            Paint paint = new Paint();
            paint.setStrokeWidth(6.0f);
            paint.setColor(-1);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(rect, paint);
        }
    }

    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
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
        layoutParams2.setMargins(convertDpToPixel, convertDpToPixel, convertDpToPixel, convertDpToPixel);
        LayoutParams layoutParams3 = new LayoutParams(-1, -1);
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
        this.iv_scale.setOnTouchListener(this.mTouchListener);
//        this.iv_delete.setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                if (StickerView.this.getParent() != null) {
//                    ((ViewGroup) StickerView.this.getParent()).removeView(StickerView.this);
//                }
//            }
//        });
        this.iv_flip.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.v(StickerView.TAG, "flip the view");
                View mainView = StickerView.this.getMainView();
                float f = -180.0f;
                if (mainView.getRotationY() == -180.0f) {
                    f = 0.0f;
                }
                mainView.setRotationY(f);
                mainView.invalidate();
                StickerView.this.requestLayout();
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


    public static int convertDpToPixel(float f, Context context) {
        return (int) (f * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
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
