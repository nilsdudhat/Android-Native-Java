package com.demo.architecture.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Model extends Observable {

    // declaring a list of integer
    List<Integer> list;

    public Model() {
        list = new ArrayList<>(3);

        list.add(0);
        list.add(0);
        list.add(0);
    }

    // defining getter and setter functions

    // function to return appropriate count
    // value at correct index
    public int getValueAtIndex(final int index) throws IndexOutOfBoundsException {
        return list.get(index);
    }

    // function to make changes in the activity button's
    // count value when user touch it
    public void setValueAtIndex(final int index) throws IndexOutOfBoundsException {
        list.set(index, list.get(index) + 1);
        setChanged();
        notifyObservers();
    }
}
