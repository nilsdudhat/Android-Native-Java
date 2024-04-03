package com.udemy.contactmanager.app.clicks;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.udemy.contactmanager.app.activities.AddNewContactActivity;

public class MainActivityClickHandlers {

    Context context;

    public MainActivityClickHandlers(Context context) {
        this.context = context;
    }

    public void onAddContactClicked(View view) {
        Intent intent = new Intent(context, AddNewContactActivity.class);
        context.startActivity(intent);
    }
}
