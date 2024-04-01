package com.udemy.viewpager.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.udemy.viewpager.app.R;
import com.udemy.viewpager.app.adapters.ViewPagerAdapter;
import com.udemy.viewpager.app.fragments.FirstFragment;
import com.udemy.viewpager.app.fragments.SecondFragment;
import com.udemy.viewpager.app.fragments.ThirdFragment;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager = findViewById(R.id.view_pager);

        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        pagerAdapter.addFragment(new FirstFragment());
        pagerAdapter.addFragment(new SecondFragment());
        pagerAdapter.addFragment(new ThirdFragment());

        // set the orientation in ViewPager2, decides the scrolling direction
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        // connecting the adapter to viewpager
        viewPager.setAdapter(pagerAdapter);
    }
}