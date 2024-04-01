package com.udemy.tab.viewpager.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.udemy.tab.viewpager.app.R;
import com.udemy.tab.viewpager.app.adapters.ViewPagerAdapter;
import com.udemy.tab.viewpager.app.fragments.FirstFragment;
import com.udemy.tab.viewpager.app.fragments.SecondFragment;
import com.udemy.tab.viewpager.app.fragments.ThirdFragment;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;

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
        tabLayout = findViewById(R.id.tab_layout);

        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        pagerAdapter.addFragment(new FirstFragment());
        pagerAdapter.addFragment(new SecondFragment());
        pagerAdapter.addFragment(new ThirdFragment());

        // set the orientation in ViewPager2, decides the scrolling direction
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        // connecting the adapter to viewpager
        viewPager.setAdapter(pagerAdapter);

        // connecting TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText("Fragment " + (position + 1));
            }
        }).attach();
    }
}