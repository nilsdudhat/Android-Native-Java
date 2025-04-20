package com.cartoon2021.photo.editor.CartoonEditor.TextDemo;


import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;

import java.util.Random;

public class GradientManager {
    private final Context mContext;
    private final Random mRandom = new Random();
    private final Point mSize;

    public GradientManager(Context context, Point point) {
        this.mContext = context;
        this.mSize = point;
    }

    public LinearGradient getRandomLinearGradient() {
        return new LinearGradient(0.0f, 0.0f, (float) this.mSize.x, (float) this.mSize.y, getRandomColorArray(), null, getRandomShaderTileMode());
    }

    public RadialGradient getRandomRadialGradient() {
        return new RadialGradient((float) this.mRandom.nextInt(this.mSize.x), (float) this.mRandom.nextInt(this.mSize.y), (float) this.mRandom.nextInt(this.mSize.x), getRandomColorArray(), null, getRandomShaderTileMode());
    }

    public SweepGradient getRandomSweepGradient() {
        return new SweepGradient((float) this.mRandom.nextInt(this.mSize.x), (float) this.mRandom.nextInt(this.mSize.y), getRandomColorArray(), null);
    }


    public Shader.TileMode getRandomShaderTileMode() {
        int nextInt = this.mRandom.nextInt(3);
        if (nextInt == 0) {
            return Shader.TileMode.CLAMP;
        }
        if (nextInt == 1) {
            return Shader.TileMode.MIRROR;
        }
        return Shader.TileMode.REPEAT;
    }


    public int[] getRandomColorArray() {
        int nextInt = this.mRandom.nextInt(13) + 3;
        int[] iArr = new int[nextInt];
        for (int i = 0; i < nextInt; i++) {
            iArr[i] = getRandomHSVColor();
        }
        return iArr;
    }


    public int getRandomHSVColor() {
        return Color.HSVToColor(255, new float[]{(float) this.mRandom.nextInt(361), 1.0f, 1.0f});
    }
}