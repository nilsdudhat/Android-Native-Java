package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.FragmentSettingsBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ViewUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.WebUtils;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;

    SettingsFragment fragment;
    Activity activity;

    public SettingsFragment getInstance() {
        if (fragment == null) {
            fragment = new SettingsFragment();
        }
        return fragment;
    }

    public SettingsFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    CacheUtils.deleteCache(activity);
                }
            }
        }).start();
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


        ((MainActivity) activity).showSmallNativeAd(activity, binding.adMiniNative);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();

        boolean isAutoTheme = sharedPreferences.getBoolean("auto_theme", true);
        if (isAutoTheme) {
            binding.switchAutoTheme.setChecked(true);
            ViewUtils.setViewAndChildrenEnabled(binding.llDarkTheme, false);
        } else {
            binding.switchAutoTheme.setChecked(false);
            ViewUtils.setViewAndChildrenEnabled(binding.llDarkTheme, true);
        }

        boolean isDark = sharedPreferences.getBoolean("is_dark_theme", false);
        binding.switchDarkMode.setChecked(isDark);

        binding.switchAutoTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ViewUtils.setViewAndChildrenEnabled(binding.llDarkTheme, false);

                    sharedPreferenceEditor.putBoolean("auto_theme", true).apply();
                } else {
                    ViewUtils.setViewAndChildrenEnabled(binding.llDarkTheme, true);

                    boolean isDark = sharedPreferences.getBoolean("is_dark_theme", false);
                    if (isDark) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }

                    sharedPreferenceEditor.putBoolean("auto_theme", false).apply();
                }
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                activity.finish();
            }
        });

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
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                activity.finish();
            }
        });

        binding.llShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.llMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebUtils.openBrowser(activity, "https://play.google.com/store/apps/dev?id=4710251405227521498");
            }
        });

        binding.llRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName());
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                }
            }
        });

        binding.llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebUtils.openBrowser(activity, "http://bluemoonmobileapps.com/privacy.html");
            }
        });
    }
}