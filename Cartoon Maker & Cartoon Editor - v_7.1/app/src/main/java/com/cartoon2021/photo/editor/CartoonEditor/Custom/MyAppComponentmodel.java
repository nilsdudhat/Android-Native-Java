package com.cartoon2021.photo.editor.CartoonEditor.Custom;

import android.graphics.Bitmap;
import android.net.Uri;

public class MyAppComponentmodel {

    private Bitmap BITMAP;
    private String COLORTYPE;
    private int COMP_ID;
    private int HEIGHT;
    private int ORDER;
    private float POS_X;
    private float POS_Y;
    private int RES_ID;
    private Uri RES_URI;
    private float ROTATION;
    private int STC_COLOR;
    private int TEMPLATE_ID;
    private String TYPE = "";
    private int WIDTH;
    private float Y_ROTATION;

    public MyAppComponentmodel(int i, float f, float f2, int i2, int i3, float f3, float f4, int i4, Uri uri, Bitmap bitmap, String str, int i5, int i6, String str2) {
        this.TEMPLATE_ID = i;
        this.POS_X = f;
        this.POS_Y = f2;
        this.WIDTH = i2;
        this.HEIGHT = i3;
        this.ROTATION = f3;
        this.Y_ROTATION = f4;
        this.RES_ID = i4;
        this.RES_URI = uri;
        this.BITMAP = bitmap;
        this.TYPE = str;
        this.ORDER = i5;
        this.STC_COLOR = i6;
        this.COLORTYPE = str2;
    }

    public MyAppComponentmodel() {
    }

    public int getCOMP_ID() {
        return this.COMP_ID;
    }

    public void setCOMP_ID(int i) {
        this.COMP_ID = i;
    }

    public int getTEMPLATE_ID() {
        return this.TEMPLATE_ID;
    }

    public void setTEMPLATE_ID(int i) {
        this.TEMPLATE_ID = i;
    }

    public float getPOS_X() {
        return this.POS_X;
    }

    public void setPOS_X(float f) {
        this.POS_X = f;
    }

    public float getPOS_Y() {
        return this.POS_Y;
    }

    public void setPOS_Y(float f) {
        this.POS_Y = f;
    }

    public int getRES_ID() {
        return this.RES_ID;
    }

    public void setRES_ID(int i) {
        this.RES_ID = i;
    }

    public String getTYPE() {
        return this.TYPE;
    }

    public void setTYPE(String str) {
        this.TYPE = str;
    }

    public int getORDER() {
        return this.ORDER;
    }

    public void setORDER(int i) {
        this.ORDER = i;
    }

    public float getROTATION() {
        return this.ROTATION;
    }

    public void setROTATION(float f) {
        this.ROTATION = f;
    }

    public int getWIDTH() {
        return this.WIDTH;
    }

    public void setWIDTH(int i) {
        this.WIDTH = i;
    }

    public int getHEIGHT() {
        return this.HEIGHT;
    }

    public void setHEIGHT(int i) {
        this.HEIGHT = i;
    }

    public float getY_ROTATION() {
        return this.Y_ROTATION;
    }

    public void setY_ROTATION(float f) {
        this.Y_ROTATION = f;
    }

    public Uri getRES_URI() {
        return this.RES_URI;
    }

    public void setRES_URI(Uri uri) {
        this.RES_URI = uri;
    }

    public Bitmap getBITMAP() {
        return this.BITMAP;
    }

    public void setBITMAP(Bitmap bitmap) {
        this.BITMAP = bitmap;
    }

    public int getSTC_COLOR() {
        return this.STC_COLOR;
    }

    public void setSTC_COLOR(int i) {
        this.STC_COLOR = i;
    }

    public String getCOLORTYPE() {
        return this.COLORTYPE;
    }

    public void setCOLORTYPE(String str) {
        this.COLORTYPE = str;
    }
}
