package com.cartoon2021.photo.editor.CartoonEditor.addnew;

public class PhotoLab {
    private String pcode;
    private String pid;
    private String pimageid;
    private String pposition;

    public String getPposition() {
        return this.pposition;
    }

    public void setPposition(String str) {
        this.pposition = str;
    }

    public String getPimageid() {
        return this.pimageid;
    }

    public void setPimageid(String str) {
        this.pimageid = str;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String str) {
        this.pid = str;
    }

    public String getPcode() {
        return this.pcode;
    }

    public void setPcode(String str) {
        this.pcode = str;
    }

    public String toString() {
        return "ClassPojo [pposition = " + this.pposition + ", pimageid = " + this.pimageid + ", pid = " + this.pid + ", pcode = " + this.pcode + "]";
    }
}
