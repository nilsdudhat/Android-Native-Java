package com.udemy.lib;

import java.util.Scanner;

public class AreaPerimeterExample {

    public static void main(String[] args) {
        System.out.println("Please enter Radius for Circle: ");

        Scanner s1 = new Scanner(System.in);
        int radius = s1.nextInt();

        Circle circle = new Circle(radius);
        System.out.println("Circle Area: " + circle.getArea());
        System.out.println("Circle Perimeter: " + circle.getPerimeter());
    }
}
