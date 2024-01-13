package com.udemy.lib;

public class Circle {

    private final float area;
    private final float perimeter;

    public Circle(int radius) {
        area = (float) (Math.PI * (radius * radius));
        perimeter = (float) (2 * Math.PI * radius);
    }

    public float getArea() {
        return area;
    }

    public float getPerimeter() {
        return perimeter;
    }
}
