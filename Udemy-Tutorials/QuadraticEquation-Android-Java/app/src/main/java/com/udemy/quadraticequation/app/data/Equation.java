package com.udemy.quadraticequation.app.data;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.udemy.quadraticequation.app.databinding.ActivityMainBinding;

public class Equation extends BaseObservable {

    @Bindable
    String a, b, c;

    ActivityMainBinding binding;

    public Equation() {
    }

    public Equation(ActivityMainBinding binding) {
        this.binding = binding;
    }

    public void solveEquation(View view) {

        // converting string to int
        int a = Integer.parseInt(getA());
        int b = Integer.parseInt(getB());
        int c = Integer.parseInt(getC());

        // calculating discriminant
        double discriminant = (b * b) - (4 * a * c);

        // finding the solutions (roots)
        double x1, x2;
        if (discriminant > 0) {
            // two real and distinct roots available
            x1 = (-b + Math.sqrt(discriminant)) / (2 * a);  // Math.sqrt(25) = 5
            x2 = (-b - Math.sqrt(discriminant)) / (2 * a);  // Math.sqrt(25) = 5

            binding.txtSolution.setText(new StringBuilder().append("x1 = ").append(x1).append(", x2 = ").append(x2));
        } else if (discriminant < 0) {
            // no real roots available
            binding.txtSolution.setText(new StringBuilder("no real solutions available"));
        } else {
            // one real solution available (double root)
            x1 = (double) (-b / (2 * a));
            binding.txtSolution.setText(new StringBuilder("x = ").append(x1));
        }
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
}
