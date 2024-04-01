package com.udemy.volume.calculator.models;


/**
 * This class is acting as "Model Class"
 * it represents the data structure or individual data items,
 * that your adapter is going to display
 */
public class Shape {

    int imgShape;
    String shapeName;

    public Shape(int imgShape, String shapeName) {
        this.imgShape = imgShape;
        this.shapeName = shapeName;
    }

    public int getImgShape() {
        return imgShape;
    }

    public void setImgShape(int imgShape) {
        this.imgShape = imgShape;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }
}
