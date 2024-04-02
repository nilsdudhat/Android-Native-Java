package com.udemy.viewmodel.app.data;

import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {

    int counter = 0;

    public void increaseCounter() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }

}
