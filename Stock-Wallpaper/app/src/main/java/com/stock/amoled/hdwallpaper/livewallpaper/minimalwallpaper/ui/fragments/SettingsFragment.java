package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adsintegration.AdUtils;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.FragmentSettingsBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities.GalleryActivity;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities.MainActivity;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.Utils;

public class SettingsFragment extends Fragment {

    Activity activity;

    FragmentSettingsBinding binding;

    SettingsFragment settingsFragment;

    public SettingsFragment getInstance() {
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }
        return settingsFragment;
    }

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        activity = requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) activity).showSmallNativeAd(activity, binding.adSmallNative);

        binding.containerDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("--gallery--", "Downloads: " + "interstitialCount: " + AdUtils.interstitialCount);

                Intent intent = new Intent(activity, GalleryActivity.class);
                intent.putExtra("gallery", "Downloads");
                ((MainActivity) activity).showInterstitialAd(activity, intent, null);
            }
        });

        binding.containerGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("--gallery--", "Gallery" + "interstitialCount: " + AdUtils.interstitialCount);

                Intent intent = new Intent(activity, GalleryActivity.class);
                intent.putExtra("gallery", "All");
                ((MainActivity) activity).showInterstitialAd(activity, intent, null);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();

        boolean isDark = sharedPreferences.getBoolean("is_dark_theme", false);
        binding.switchDarkMode.setChecked(isDark);

        binding.switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    sharedPreferenceEditor.putBoolean("is_dark_theme", true).apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    sharedPreferenceEditor.putBoolean("is_dark_theme", false).apply();
                }
            }
        });

        binding.llShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Utils.shareApp(activity);
            }
        });

        binding.llMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openBrowser(activity, "http://bluegalaxymobileapps.com/");
            }
        });

        binding.llRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.rateApp(activity);
            }
        });

        binding.llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openBrowser(activity, "http://bluegalaxymobileapps.com/privacy.html");
            }
        });
    }
}