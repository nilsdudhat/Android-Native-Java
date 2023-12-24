package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.Constant;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.ActivityMainBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.ListModel;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.preferences.ThemePreferences;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.fragments.HomeFragment;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.fragments.SettingsFragment;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.AnimationUtils;

import java.util.Objects;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    HomeFragment homeFragment;
    SettingsFragment settingsFragment;

    SharedPreferences sharedPreferences;

    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemePreferences.setTheme(); // applying theme (Light/Dark)
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide(); // hiding Title Bar

        loadInterstitialAd(MainActivity.this);

        // initiating default share preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        // Home Tab click listener
        binding.tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callHomeFragment();
            }
        });

        // Settings Tab click listener
        binding.tabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSettingsFragment();
            }
        });

        // search image
        binding.searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                Constant.listModel = new ListModel(0, homeFragment.getWallpaperList());
                showInterstitialAd(MainActivity.this, intent, null);
            }
        });

        progressDialog = new Dialog(MainActivity.this, R.style.CustomDialog);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_loader);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // getting last opened fragment to restore on theme change
        String last_fragment = sharedPreferences.getString("last_fragment", "Home");
        if (last_fragment.equals("Home")) {
            callHomeFragment(); // displaying Home Fragment as default
        } else if (last_fragment.equals("Settings")) {
            callSettingsFragment();
        }
    }

    private void callHomeFragment() {
        if (homeFragment == null) { // checking homeFragment is null or not
            homeFragment = new HomeFragment().getInstance();
        }
        replaceFragment(homeFragment); // Home Fragment Transaction
        binding.txtTitle.setText(getString(R.string.app_name));
        binding.searchCard.setVisibility(View.VISIBLE);

        binding.imgHome.setImageResource(R.drawable.ic_home_selected);
        binding.imgSettings.setImageResource(R.drawable.ic_settings);

        binding.indicatorHome.setVisibility(View.VISIBLE);
        binding.indicatorSettings.setVisibility(View.INVISIBLE);

        // storing value of current fragment to remember on theme change
        sharedPreferences.edit().putString("last_fragment", "Home").apply();
    }

    private void callSettingsFragment() {
        if (settingsFragment == null) { // checking settingsFragment is null or not
            settingsFragment = new SettingsFragment().getInstance();
        }
        replaceFragment(settingsFragment); // Settings Fragment Transaction

        binding.txtTitle.setText("Settings");
        binding.searchCard.setVisibility(View.GONE);

        binding.imgHome.setImageResource(R.drawable.ic_home);
        binding.imgSettings.setImageResource(R.drawable.ic_settings_selected);

        binding.indicatorHome.setVisibility(View.INVISIBLE);
        binding.indicatorSettings.setVisibility(View.VISIBLE);

        // storing value of current fragment to remember on theme change
        sharedPreferences.edit().putString("last_fragment", "Settings").apply();
    }

    private void replaceFragment(Fragment fragment) {
        // committing fragment transaction
        getSupportFragmentManager().beginTransaction().replace(binding.frameFragment.getId(), fragment).commit();
    }

    @Override
    public void onBackPressed() {

        Dialog dialog = new Dialog(MainActivity.this, R.style.CustomDialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        TextView txt_no = dialog.findViewById(R.id.txt_no);
        TextView txt_yes = dialog.findViewById(R.id.txt_yes);

        Objects.requireNonNull(txt_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.super.onBackPressed();
                AnimationUtils.closeActivity(MainActivity.this); // Close Activity Animation
            }
        });

        Objects.requireNonNull(txt_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void isConnected(boolean isConnected) {
        Log.d("--is_connected--", String.valueOf(isConnected));

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (homeFragment != null) {
                    homeFragment.isConnected(isConnected);
                }
            }
        });

        super.isConnected(isConnected);
    }
}