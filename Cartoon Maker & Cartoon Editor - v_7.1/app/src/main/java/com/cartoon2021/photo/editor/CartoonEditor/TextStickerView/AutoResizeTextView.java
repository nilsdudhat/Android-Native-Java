package com.cartoon2021.photo.editor.CartoonEditor.TextStickerView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;

public class AutoResizeTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final int NO_LINE_LIMIT = -1;
    private RectF mAvailableSpaceRect;
    private boolean mEnableSizeCache = true;
    private boolean mInitiallized;
    private int mMaxLines;
    private float mMaxTextSize;
    private float mMinTextSize = 20.0f;

    public TextPaint mPaint;
    private final SizeTester mSizeTester = new C11751();

    public float mSpacingAdd = 0.0f;

    public float mSpacingMult = 1.0f;
    private SparseIntArray mTextCachedSizes;

    public RectF mTextRect = new RectF();

    public int mWidthLimit;

    private interface SizeTester {
        int onTestSize(int i, RectF rectF);
    }

    public void setSize(int i) {
    }

    class C11751 implements SizeTester {
        C11751() {
        }

        public int onTestSize(int i, RectF rectF) {
            AutoResizeTextView.this.mPaint.setTextSize((float) i);
            String charSequence = AutoResizeTextView.this.getText().toString();
            if (AutoResizeTextView.this.getMaxLines() == 1) {
                AutoResizeTextView.this.mTextRect.bottom = AutoResizeTextView.this.mPaint.getFontSpacing();
                AutoResizeTextView.this.mTextRect.right = AutoResizeTextView.this.mPaint.measureText(charSequence);
            } else {
                StaticLayout staticLayout = new StaticLayout(charSequence, AutoResizeTextView.this.mPaint, AutoResizeTextView.this.mWidthLimit, Layout.Alignment.ALIGN_NORMAL, AutoResizeTextView.this.mSpacingMult, AutoResizeTextView.this.mSpacingAdd, true);
                if (AutoResizeTextView.this.getMaxLines() != -1 && staticLayout.getLineCount() > AutoResizeTextView.this.getMaxLines()) {
                    return 1;
                }
                AutoResizeTextView.this.mTextRect.bottom = (float) staticLayout.getHeight();
                int i2 = -1;
                for (int i3 = 0; i3 < staticLayout.getLineCount(); i3++) {
                    if (((float) i2) < staticLayout.getLineWidth(i3)) {
                        i2 = (int) staticLayout.getLineWidth(i3);
                    }
                }
                AutoResizeTextView.this.mTextRect.right = (float) i2;
            }
            AutoResizeTextView.this.mTextRect.offsetTo(0.0f, 0.0f);
            if (rectF.contains(AutoResizeTextView.this.mTextRect)) {
                return -1;
            }
            return 1;
        }
    }

    public AutoResizeTextView(Context context) {
        super(context);
        initialize();
    }

    public AutoResizeTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    public AutoResizeTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize();
    }

    private void initialize() {
        this.mPaint = new TextPaint(getPaint());
        this.mMaxTextSize = getTextSize();
        this.mAvailableSpaceRect = new RectF();
        this.mTextCachedSizes = new SparseIntArray();
        if (this.mMaxLines == 0) {
            this.mMaxLines = -1;
        }
        this.mInitiallized = true;
    }

    public void setText(CharSequence charSequence, BufferType bufferType) {
        super.setText(charSequence, bufferType);
        adjustTextSize(charSequence.toString());
    }

    public void setTextSize(float f) {
        this.mMaxTextSize = f;
        this.mTextCachedSizes.clear();
        adjustTextSize(getText().toString());
    }

    public void setMaxLines(int i) {
        super.setMaxLines(i);
        this.mMaxLines = i;
        reAdjust();
    }

    public int getMaxLines() {
        return this.mMaxLines;
    }

    public void setSingleLine() {
        super.setSingleLine();
        this.mMaxLines = 1;
        reAdjust();
    }

    public void setSingleLine(boolean z) {
        super.setSingleLine(z);
        if (z) {
            this.mMaxLines = 1;
        } else {
            this.mMaxLines = -1;
        }
        reAdjust();
    }

    public void setLines(int i) {
        super.setLines(i);
        this.mMaxLines = i;
        reAdjust();
    }

    public void setTextSize(int i, float f) {
        Resources resources;
        Context context = getContext();
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        this.mMaxTextSize = TypedValue.applyDimension(i, f, resources.getDisplayMetrics());
        this.mTextCachedSizes.clear();
        adjustTextSize(getText().toString());
    }

    public void setLineSpacing(float f, float f2) {
        super.setLineSpacing(f, f2);
        this.mSpacingMult = f2;
        this.mSpacingAdd = f;
    }

    public void setMinTextSize(float f) {
        this.mMinTextSize = f;
        reAdjust();
    }

    private void reAdjust() {
        adjustTextSize(getText().toString());
    }

    private void adjustTextSize(String str) {
        if (this.mInitiallized) {
            int measuredHeight = (getMeasuredHeight() - getCompoundPaddingBottom()) - getCompoundPaddingTop();
            this.mWidthLimit = (getMeasuredWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight();
            RectF rectF = this.mAvailableSpaceRect;
            rectF.right = (float) this.mWidthLimit;
            rectF.bottom = (float) measuredHeight;
            super.setTextSize(0, (float) efficientTextSizeSearch((int) this.mMinTextSize, (int) this.mMaxTextSize, this.mSizeTester, rectF));
        }
    }

    public void enableSizeCache(boolean z) {
        this.mEnableSizeCache = z;
        this.mTextCachedSizes.clear();
        adjustTextSize(getText().toString());
    }

    private int efficientTextSizeSearch(int i, int i2, SizeTester sizeTester, RectF rectF) {
        int i3;
        if (!this.mEnableSizeCache) {
            return binarySearch(i, i2, sizeTester, rectF);
        }
        String charSequence = getText().toString();
        if (charSequence == null) {
            i3 = 0;
        } else {
            i3 = charSequence.length();
        }
        int i4 = this.mTextCachedSizes.get(i3);
        if (i4 != 0) {
            return i4;
        }
        int binarySearch = binarySearch(i, i2, sizeTester, rectF);
        this.mTextCachedSizes.put(i3, binarySearch);
        return binarySearch;
    }

    private static int binarySearch(int i, int i2, SizeTester sizeTester, RectF rectF) {
        int i3 = i2 - 1;
        int i4 = i;
        while (i <= i3) {
            int i5 = (i + i3) >>> 1;
            int onTestSize = sizeTester.onTestSize(i5, rectF);
            if (onTestSize < 0) {
                int i6 = i5 + 1;
                i4 = i;
                i = i6;
            } else if (onTestSize <= 0) {
                return i5;
            } else {
                i4 = i5 - 1;
                i3 = i4;
            }
        }
        return i4;
    }


    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        super.onTextChanged(charSequence, i, i2, i3);
        reAdjust();
    }


    public void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mTextCachedSizes.clear();
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3 || i2 != i4) {
            reAdjust();
        }
    }
}
