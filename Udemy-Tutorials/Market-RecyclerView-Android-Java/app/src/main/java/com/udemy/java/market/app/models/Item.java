package com.udemy.java.market.app.models;

public class Item {

    int itemImg;
    String itemName, itemDescription;

    public Item(int itemImg, String itemName, String itemDescription) {
        this.itemImg = itemImg;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    public int getItemImg() {
        return itemImg;
    }

    public void setItemImg(int itemImg) {
        this.itemImg = itemImg;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
