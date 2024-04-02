package com.udemy.livedata.app.data;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {

    MutableLiveData<Integer> counter = new MutableLiveData<>();

    public void increaseCounter(View view) {
        // retrieve the current value from livedata object
        int value = (counter.getValue() != null) ? counter.getValue() : 0;

        // increase value by 1
        value++;

        // set the value to live data object
        counter.setValue(value);
    }

    public LiveData<Integer> getCounter() {
        return counter;
    }

}
