package com.allvideo.hdplayer.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allvideo.hdplayer.AdsIntegration.AdUtils;
import com.allvideo.hdplayer.AdsIntegration.AppUtility;
import com.allvideo.hdplayer.AdsIntegration.Constant;
import com.allvideo.hdplayer.BuildConfig;
import com.allvideo.hdplayer.Custom.Utils;
import com.allvideo.hdplayer.Fragments.FileFragment;
import com.allvideo.hdplayer.Fragments.FolderFragment;
import com.allvideo.hdplayer.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabLayout.Tab All;
    TabLayout.Tab Folder;
    ViewPager viewPager;

    ImageView menu;
    NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdUtils.showNativeAds(MainActivity.this, findViewById(R.id.ad_native));
        AdUtils.showInterstitial(MainActivity.this);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        All = tabLayout.newTab();
        Folder = tabLayout.newTab();

        tabLayout.setupWithViewPager(viewPager);

        setupViewPager(viewPager);

        setupDrawer();

        if (AppUtility.getInt(MainActivity.this, Constant.FORCEFULLY_UPDATE, BuildConfig.VERSION_CODE) > BuildConfig.VERSION_CODE) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.update_app_dlg);
            dialog.getWindow().setBackgroundDrawable(null);
            dialog.setCancelable(false);

            dialog.findViewById(R.id.btn_update).setOnClickListener(view -> {
                if (AppUtility.isNetworkAvailable(MainActivity.this)) {
                    dialog.dismiss();
                    finishAffinity();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + AppUtility.getString(MainActivity.this, Constant.FORCEFULLY_UPDATE_PKG, getPackageName()))));
                }
            });
            dialog.findViewById(R.id.btn_cancel).setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }

    private void setupDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        final LinearLayout holder = findViewById(R.id.holder);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float scaleFactor = 7f;
                float slideX = drawerView.getWidth() * slideOffset;
                holder.setTranslationX(slideX);
                holder.setScaleX(1 - (slideOffset / scaleFactor));
                holder.setScaleY(1 - (slideOffset / scaleFactor));
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        drawer.setScrimColor(Color.TRANSPARENT);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);

        navigationView.findViewById(R.id.nav_all).setOnClickListener(view -> {
            viewPager.setCurrentItem(0);
            drawer.closeDrawer(GravityCompat.START);
        });
        navigationView.findViewById(R.id.nav_folders).setOnClickListener(view -> {
            viewPager.setCurrentItem(1);
            drawer.closeDrawer(GravityCompat.START);
        });
        navigationView.findViewById(R.id.nav_share).setOnClickListener(view -> {
            drawer.closeDrawer(GravityCompat.START);
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        navigationView.findViewById(R.id.nav_rate).setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=PackageName")));
            drawer.closeDrawer(GravityCompat.START);
        });
        navigationView.findViewById(R.id.nav_privacy).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
//            intent.putExtra("web_view", "privacy");
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        });

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager(), 1);
        viewPagerAdapter.addFragment(new FileFragment(), "All");
        viewPagerAdapter.addFragment(new FolderFragment(), "Folder");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        ViewPagerAdapter(FragmentManager fragmentManager, int i) {
            super(fragmentManager, i);
        }

        @NotNull
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(str);
        }

        public CharSequence getPageTitle(int i) {
            return mFragmentTitleList.get(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (Utils.videoModelArrayList.size() > 0) {
            for (int i = 0; i < Utils.videoModelArrayList.size(); i++) {
                if (Utils.videoModelArrayList.get(i) == null) {
                    Utils.videoModelArrayList.remove(i);
                }
            }
        }
        super.onBackPressed();
    }
}