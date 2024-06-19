package com.udemy.furniture.app.models;

public class SellingModel {

    String title;
    String subTitle;
    String price;
    int image;

    public SellingModel(String title, String subTitle, String price, int image) {
        this.title = title;
        this.subTitle = subTitle;
        this.price = price;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
