package com.cartoon2021.photo.editor.CartoonEditor.Custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.cartoon2021.photo.editor.R;

public class MyAppStickerView extends RelativeLayout implements MyAppImageMultiTouchListener.TouchCallbackListener {
    public static final String TAG = "ResizableStickerView";
    private static final int SELF_SIZE_DP = 30;

    public int f18s;
    public int he;

    public TouchEventListener listener = null;
    public ImageView main_iv;
    public int wi;
    double angle = 0.0d;
    int baseh;
    int basew;
    int basex;
    int basey;
    float cX = 0.0f;
    float cY = 0.0f;
    double dAngle = 0.0d;
    int margl;
    int margt;
    double onTouchAngle = 0.0d;
    Animation scale;
    double tAngle = 0.0d;
    double vAngle = 0.0d;
    Animation zoomInScale;

    Animation zoomOutScale;
    private int alphaProg = 0;
    private ImageView border_iv;
    private Bitmap btmp = null;
    private final double centerX = 0.0d;
    private final double centerY = 0.0d;
    private String colorType = "color";
    private Context context;
    private ImageView delete_iv;
    private int drawableId;
    private ImageView edit_iv;
    private ImageView flip_iv;
    private final int hueProg = 0;
    private int imgColor = 0;
    private boolean isBorderVisible = false;
    private boolean isColorFilterEnable = false;
    private boolean isSticker = true;
    private final boolean isStrickerEditEnable = false;
    private final OnTouchListener mTouchListener1 = new C05784();
    private final OnTouchListener rTouchListener = new C05773();
    private Uri resUri = null;
    private float rotation;
    private ImageView scale_iv;
    private final double scale_orgHeight = -1.0d;
    private final double scale_orgWidth = -1.0d;
    private final float scale_orgX = -1.0f;

    private final float scale_orgY = -1.0f;
    private final float this_orgX = -1.0f;
    private final float this_orgY = -1.0f;
    private float yRotation;

    public MyAppStickerView(Context context2) {
        super(context2);
        init(context2);
    }

    public MyAppStickerView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        init(context2);
    }

    public MyAppStickerView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        init(context2);
    }

    public MyAppStickerView setOnTouchCallbackListener(TouchEventListener touchEventListener) {
        this.listener = touchEventListener;
        return this;
    }

    public void init(Context context2) {
        this.context = context2;
        this.main_iv = new ImageView(this.context);
        this.scale_iv = new ImageView(this.context);
        this.border_iv = new ImageView(this.context);
        this.flip_iv = new ImageView(this.context);
        this.edit_iv = new ImageView(this.context);
        this.delete_iv = new ImageView(this.context);
        this.f18s = dpToPx(this.context, 25);
        this.wi = dpToPx(this.context, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.he = dpToPx(this.context, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.scale_iv.setImageResource(R.drawable.myicon_resize);
        this.border_iv.setImageResource(R.drawable.myapp_sticker_border);
        this.flip_iv.setImageResource(R.drawable.myicon_flip);
        this.edit_iv.setImageResource(R.drawable.myrotate);
        this.delete_iv.setImageResource(R.drawable.mysticker_delete);
        LayoutParams layoutParams = new LayoutParams(this.wi, this.he);
        LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        layoutParams2.setMargins(5, 5, 5, 5);
        layoutParams2.addRule(17);
        int i = this.f18s;
        LayoutParams layoutParams3 = new LayoutParams(i, i);
        layoutParams3.addRule(12);
        layoutParams3.addRule(11);
        layoutParams3.setMargins(5, 5, 5, 5);
        int i2 = this.f18s;
        LayoutParams layoutParams4 = new LayoutParams(i2, i2);
        layoutParams4.addRule(10);
        layoutParams4.addRule(11);
        layoutParams4.setMargins(5, 5, 5, 5);
        int i3 = this.f18s;
        LayoutParams layoutParams5 = new LayoutParams(i3, i3);
        layoutParams5.addRule(12);
        layoutParams5.addRule(9);
        layoutParams5.setMargins(5, 5, 5, 5);
        int i4 = this.f18s;
        LayoutParams layoutParams6 = new LayoutParams(i4, i4);
        layoutParams6.addRule(10);
        layoutParams6.addRule(9);
        layoutParams6.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams7 = new LayoutParams(-1, -1);
        setLayoutParams(layoutParams);
        addView(this.border_iv);
        this.border_iv.setLayoutParams(layoutParams7);
        this.border_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        this.border_iv.setTag("border_iv");
        addView(this.main_iv);
        this.main_iv.setLayoutParams(layoutParams2);
        this.main_iv.setTag("main_iv");
        addView(this.flip_iv);
        this.flip_iv.setLayoutParams(layoutParams4);
        this.flip_iv.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageView imageView = MyAppStickerView.this.main_iv;
                float f = -180.0f;
                if (MyAppStickerView.this.main_iv.getRotationY() == -180.0f) {
                    f = 0.0f;
                }
                imageView.setRotationY(f);
                MyAppStickerView.this.main_iv.invalidate();
                MyAppStickerView.this.requestLayout();
            }
        });
        addView(this.edit_iv);
        this.edit_iv.setLayoutParams(layoutParams5);
        this.edit_iv.setOnTouchListener(this.rTouchListener);
        addView(this.delete_iv);
        this.delete_iv.setLayoutParams(layoutParams6);
        this.delete_iv.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final ViewGroup viewGroup = (ViewGroup) MyAppStickerView.this.getParent();
                MyAppStickerView.this.zoomInScale.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        viewGroup.removeView(MyAppStickerView.this);
                    }
                });
                main_iv.startAnimation(zoomInScale);
                setBorderVisibility(false);
                if (listener != null) {
                    listener.onDelete();
                }
            }
        });
        addView(this.scale_iv);
        this.scale_iv.setLayoutParams(layoutParams3);
        this.scale_iv.setOnTouchListener(this.mTouchListener1);
        this.scale_iv.setTag("scale_iv");
        this.rotation = getRotation();
        this.scale = AnimationUtils.loadAnimation(getContext(), R.anim.myapp_sticker_scale);
        this.zoomOutScale = AnimationUtils.loadAnimation(getContext(), R.anim.myapp_sticker_zoom_out);
        this.zoomInScale = AnimationUtils.loadAnimation(getContext(), R.anim.myapp_sticker_zoom_in);
        setDefaultTouchListener();
    }

    public void setDefaultTouchListener() {
        setOnTouchListener(new MyAppImageMultiTouchListener().enableRotation(true).setOnTouchCallbackListener(this));
    }

    public void setBorderVisibility(boolean z) {
        this.isBorderVisible = z;
        if (!z) {
            this.border_iv.setVisibility(View.GONE);
            this.scale_iv.setVisibility(View.GONE);
            this.flip_iv.setVisibility(View.GONE);
            this.edit_iv.setVisibility(View.GONE);
            this.delete_iv.setVisibility(View.GONE);
            setBackgroundResource(0);
            if (this.isColorFilterEnable) {
                this.main_iv.setColorFilter(Color.parseColor("#303828"));
            }
        } else if (this.border_iv.getVisibility() != VISIBLE) {
            this.border_iv.setVisibility(View.VISIBLE);
            this.scale_iv.setVisibility(View.VISIBLE);
            if (this.isSticker) {
                this.flip_iv.setVisibility(View.VISIBLE);
            }
            this.edit_iv.setVisibility(View.VISIBLE);
            this.delete_iv.setVisibility(View.VISIBLE);
            this.main_iv.startAnimation(this.scale);
        }
    }

    public boolean getBorderVisbilty() {
        return this.isBorderVisible;
    }

    public int getHueProg() {
        return this.hueProg;
    }

    public String getColorType() {
        return this.colorType;
    }

    public void setColorType(String str) {
        this.colorType = str;
    }

    public int getAlphaProg() {
        return this.alphaProg;
    }

    public void setAlphaProg(int i) {
        this.alphaProg = i;
        this.main_iv.setImageAlpha(255 - i);
    }

    public int getColor() {
        return this.imgColor;
    }

    public void setColor(int i) {
        try {
            this.main_iv.setColorFilter(i);
            this.imgColor = i;
        } catch (Exception unused) {
        }
    }

    public void setBgDrawable(int i) {
        try {
            this.main_iv.setImageResource(i);
            this.drawableId = i;
            this.main_iv.startAnimation(this.zoomOutScale);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public Uri getMainImageUri() {
        return this.resUri;
    }

    public void setMainImageUri(Uri uri) {
        this.resUri = uri;
        this.main_iv.setImageURI(this.resUri);
    }

    public Bitmap getMainImageBitmap() {
        return this.btmp;
    }

    public void setMainImageBitmap(Bitmap bitmap) {
        this.main_iv.setImageBitmap(bitmap);
    }

    public void optimize(float f, float f2) {
        setX(getX() * f);
        setY(getY() * f2);
        getLayoutParams().width = (int) (((float) this.wi) * f);
        getLayoutParams().height = (int) (((float) this.he) * f2);
    }

    public MyAppComponentmodel getComponentInfo() {
        MyAppComponentmodel myAppComponentmodel = new MyAppComponentmodel();
        myAppComponentmodel.setPOS_X(getX());
        myAppComponentmodel.setPOS_Y(getY());
        myAppComponentmodel.setWIDTH(this.wi);
        myAppComponentmodel.setHEIGHT(this.he);
        myAppComponentmodel.setRES_ID(this.drawableId);
        myAppComponentmodel.setSTC_COLOR(this.imgColor);
        myAppComponentmodel.setRES_URI(this.resUri);
        myAppComponentmodel.setCOLORTYPE(this.colorType);
        myAppComponentmodel.setBITMAP(this.btmp);
        myAppComponentmodel.setROTATION(getRotation());
        myAppComponentmodel.setY_ROTATION(this.main_iv.getRotationY());
        return myAppComponentmodel;
    }

    public void setComponentInfo(MyAppComponentmodel myAppComponentmodel) {
        this.wi = myAppComponentmodel.getWIDTH();
        this.he = myAppComponentmodel.getHEIGHT();
        this.drawableId = myAppComponentmodel.getRES_ID();
        this.resUri = myAppComponentmodel.getRES_URI();
        this.btmp = myAppComponentmodel.getBITMAP();
        this.imgColor = myAppComponentmodel.getSTC_COLOR();
        this.rotation = myAppComponentmodel.getROTATION();
        this.yRotation = myAppComponentmodel.getY_ROTATION();
        this.colorType = myAppComponentmodel.getCOLORTYPE();
        setX(myAppComponentmodel.getPOS_X());
        setY(myAppComponentmodel.getPOS_Y());
        int i = this.drawableId;
        if (i == 0) {
            this.main_iv.setImageBitmap(this.btmp);
        } else {
            setBgDrawable(i);
        }
        setRotation(this.rotation);
        setColor(this.imgColor);
        setColorType(this.colorType);
        getLayoutParams().width = this.wi;
        getLayoutParams().height = this.he;
        if (myAppComponentmodel.getTYPE() == "SHAPE") {
            this.flip_iv.setVisibility(View.GONE);
            this.isSticker = false;
        }
        if (myAppComponentmodel.getTYPE() == "STICKER") {
            this.flip_iv.setVisibility(View.VISIBLE);
            this.isSticker = true;
        }
        this.main_iv.setRotationY(this.yRotation);
    }

    public int dpToPx(Context context2, int i) {
        context2.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    private double getLength(double d, double d2, double d3, double d4) {
        return Math.sqrt(Math.pow(d4 - d2, 2.0d) + Math.pow(d3 - d, 2.0d));
    }

    public void enableColorFilter(boolean z) {
        this.isColorFilterEnable = z;
    }

    public void onTouchCallback(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchDown(view);
        }
    }

    public void onTouchUpCallback(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchUp(view);
        }
    }

    public interface TouchEventListener {
        void onDelete();

        void onEdit(View view, String str);

        void onTouchDown(View view);

        void onTouchUp(View view);
    }

    class C05773 implements OnTouchListener {
        C05773() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                Rect rect = new Rect();
                ((View) view.getParent()).getGlobalVisibleRect(rect);
                cX = rect.exactCenterX();
                cY = rect.exactCenterY();
                vAngle = ((View) view.getParent()).getRotation();
                tAngle = (Math.atan2(cY - motionEvent.getRawY(), cX - motionEvent.getRawX()) * 180.0d) / 3.141592653589793d;
                dAngle = vAngle - tAngle;
                if (listener != null) {
                    listener.onEdit(MyAppStickerView.this, "gone");
                }
            } else if (action != 1) {
                if (action == 2) {
                    angle = (Math.atan2(cY - motionEvent.getRawY(), cX - motionEvent.getRawX()) * 180.0d) / 3.141592653589793d;
                    ((View) view.getParent()).setRotation((float) (angle + dAngle));
                    ((View) view.getParent()).invalidate();
                    ((View) view.getParent()).requestLayout();
                }
            } else if (listener != null) {
                listener.onEdit(MyAppStickerView.this, "view");
            }
            return true;
        }
    }

    class C05784 implements OnTouchListener {
        C05784() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            int action = motionEvent.getAction();
            if (action == 0) {
                invalidate();
                basex = rawX;
                basey = rawY;
                basew = getWidth();
                baseh = getHeight();
                getLocationOnScreen(new int[2]);
                margl = layoutParams.leftMargin;
                margt = layoutParams.topMargin;
                if (listener != null) {
                    listener.onEdit(MyAppStickerView.this, "gone");
                }
            } else if (action == 1) {
                wi = getLayoutParams().width;
                he = getLayoutParams().height;
                if (listener != null) {
                    listener.onEdit(MyAppStickerView.this, "view");
                }
            } else if (action == 2) {
                float degrees = (float) Math.toDegrees(Math.atan2(rawY - basey, rawX - basex));
                if (degrees < 0.0f) {
                    degrees += 360.0f;
                }
                int i = rawX - basex;
                int i2 = rawY - basey;
                int i3 = i2 * i2;
                int sqrt = (int) (Math.sqrt((i * i) + i3) * Math.cos(Math.toRadians(degrees - getRotation())));
                int sqrt2 = (int) (Math.sqrt((sqrt * sqrt) + i3) * Math.sin(Math.toRadians(degrees - getRotation())));
                int i4 = (sqrt * 2) + basew;
                int i5 = (sqrt2 * 2) + baseh;
                if (i4 > f18s) {
                    layoutParams.width = i4;
                    layoutParams.leftMargin = margl - sqrt;
                }
                if (i5 > f18s) {
                    layoutParams.height = i5;
                    layoutParams.topMargin = margt - sqrt2;
                }
                setLayoutParams(layoutParams);
                performLongClick();
            }
            return true;
        }
    }
}
