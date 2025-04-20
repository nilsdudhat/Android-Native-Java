package com.cartoon2021.photo.editor.CartoonEditor.addnew;

public class PhotoLabEffect {
    private String pasktime;
    private String pcode;
    private String pimageurl;
    private String pposition;

    public String getPposition() {
        return this.pposition;
    }

    public void setPposition(String str) {
        this.pposition = str;
    }

    public String getPasktime() {
        return this.pasktime;
    }

    public void setPasktime(String str) {
        this.pasktime = str;
    }

    public String getPimageurl() {
        return this.pimageurl;
    }

    public void setPimageurl(String str) {
        this.pimageurl = str;
    }

    public String getPcode() {
        return this.pcode;
    }

    public void setPcode(String str) {
        this.pcode = str;
    }

    public String toString() {
        return "ClassPojo [pposition = " + this.pposition + ", pasktime = " + this.pasktime + ", pimageurl = " + this.pimageurl + ", pcode = " + this.pcode + "]";
    }
}
