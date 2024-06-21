package com.udemy.recyclerview.app.models;

public class SingleModel {

    boolean isChecked = false;
    String name;

    public SingleModel(boolean isChecked, String name) {
        this.isChecked = isChecked;
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
