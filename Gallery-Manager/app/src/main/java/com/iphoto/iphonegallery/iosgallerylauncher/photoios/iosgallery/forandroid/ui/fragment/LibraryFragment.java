package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.color.MaterialColors;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.FragmentLibraryBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.library.AllFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.library.DaysFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.library.MonthsFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.library.YearsFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.PreferenceUtils;

import java.util.ArrayList;

public class LibraryFragment extends Fragment {

    FragmentLibraryBinding binding;

    LibraryFragment fragment;
    Activity activity;

    AllFragment allFragment;
    DaysFragment daysFragment;
    MonthsFragment monthsFragment;
    YearsFragment yearsFragment;

    public LibraryFragment getInstance() {
        if (fragment == null) {
            fragment = new LibraryFragment();
        }
        return fragment;
    }

    public LibraryFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    CacheUtils.deleteCache(requireActivity());
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater, container, false);

        activity = requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button even
                ((MainActivity) activity).onBackPressed();
            }
        };

        ((MainActivity) activity).getOnBackPressedDispatcher().addCallback((MainActivity) activity, callback);

        String strFragment = PreferenceUtils.getString(activity, PreferenceUtils.LIBRARY_FRAGMENT);

        switch (strFragment) {
            case "DaysFragment":
                callDayFragment();
                break;
            case "MonthsFragment":
                callMonthFragment();
                break;
            case "YearsFragment":
                callYearFragment();
                break;
            case "AllFragment":
            default:
                callAllFragment();
                break;
        }

        binding.txtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callYearFragment();
            }
        });
        binding.txtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMonthFragment();
            }
        });
        binding.txtDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDayFragment();
            }
        });
        binding.txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAllFragment();
            }
        });
    }

    private void callDayFragment() {
        if (daysFragment == null) {
            daysFragment = new DaysFragment().getInstance();
            replaceFragment(daysFragment, "DaysFragment");
        } else {
            if (daysFragment.isAdded()) {
                getChildFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(daysFragment).commit();
            } else {
                replaceFragment(daysFragment, "DaysFragment");
            }
        }
        hideFragment(allFragment);
        hideFragment(monthsFragment);
        hideFragment(yearsFragment);

        daySelected();
    }

    private void callMonthFragment() {
        if (monthsFragment == null) {
            monthsFragment = new MonthsFragment().getInstance();
            replaceFragment(monthsFragment, "MonthsFragment");
        } else {
            if (monthsFragment.isAdded()) {
                getChildFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(monthsFragment).commit();
            } else {
                replaceFragment(monthsFragment, "MonthsFragment");
            }
        }
        hideFragment(allFragment);
        hideFragment(daysFragment);
        hideFragment(yearsFragment);

        monthSelected();
    }

    private void callYearFragment() {
        if (yearsFragment == null) {
            yearsFragment = new YearsFragment().getInstance();
            replaceFragment(yearsFragment, "YearsFragment");
        } else {
            if (yearsFragment.isAdded()) {
                getChildFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(yearsFragment).commit();
            } else {
                replaceFragment(yearsFragment, "YearsFragment");
            }
        }
        hideFragment(allFragment);
        hideFragment(daysFragment);
        hideFragment(monthsFragment);

        yearSelected();
    }

    private void callAllFragment() {
        if (allFragment == null) {
            allFragment = new AllFragment().getInstance();
            replaceFragment(allFragment, "AllFragment");
        } else {
            if (allFragment.isAdded()) {
                getChildFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(allFragment).commit();
            } else {
                replaceFragment(allFragment, "AllFragment");
            }
        }
        hideFragment(daysFragment);
        hideFragment(monthsFragment);
        hideFragment(yearsFragment);

        allSelected();
    }

    private void yearSelected() {
        binding.txtAll.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtDay.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtMonth.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtYear.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected));

        int color = MaterialColors.getColor(requireContext(), R.attr.txt_light, Color.GRAY);
        int color_selected = MaterialColors.getColor(requireContext(), R.attr.common, Color.WHITE);

        binding.txtAll.setTextColor(color);
        binding.txtDay.setTextColor(color);
        binding.txtMonth.setTextColor(color);
        binding.txtYear.setTextColor(color_selected);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.txtAll.setTextAppearance(R.style.Normal);
            binding.txtDay.setTextAppearance(R.style.Normal);
            binding.txtMonth.setTextAppearance(R.style.Normal);
            binding.txtYear.setTextAppearance(R.style.Bold);
        }
    }

    private void monthSelected() {
        binding.txtAll.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtDay.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtMonth.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected));
        binding.txtYear.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));

        int color = MaterialColors.getColor(requireContext(), R.attr.txt_light, Color.GRAY);
        int color_selected = MaterialColors.getColor(requireContext(), R.attr.common, Color.WHITE);

        binding.txtAll.setTextColor(color);
        binding.txtDay.setTextColor(color);
        binding.txtMonth.setTextColor(color_selected);
        binding.txtYear.setTextColor(color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.txtAll.setTextAppearance(R.style.Normal);
            binding.txtDay.setTextAppearance(R.style.Normal);
            binding.txtMonth.setTextAppearance(R.style.Bold);
            binding.txtYear.setTextAppearance(R.style.Normal);
        }
    }

    private void daySelected() {
        binding.txtAll.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtDay.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected));
        binding.txtMonth.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtYear.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));

        int color = MaterialColors.getColor(requireContext(), R.attr.txt_light, Color.GRAY);
        int color_selected = MaterialColors.getColor(requireContext(), R.attr.common, Color.WHITE);

        binding.txtAll.setTextColor(color);
        binding.txtDay.setTextColor(color_selected);
        binding.txtMonth.setTextColor(color);
        binding.txtYear.setTextColor(color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.txtAll.setTextAppearance(R.style.Normal);
            binding.txtDay.setTextAppearance(R.style.Bold);
            binding.txtMonth.setTextAppearance(R.style.Normal);
            binding.txtYear.setTextAppearance(R.style.Normal);
        }
    }

    private void allSelected() {
        binding.txtAll.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected));
        binding.txtDay.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtMonth.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));
        binding.txtYear.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_transparent));

        int color = MaterialColors.getColor(requireContext(), R.attr.txt_light, Color.GRAY);
        int color_selected = MaterialColors.getColor(requireContext(), R.attr.common, Color.WHITE);

        binding.txtAll.setTextColor(color_selected);
        binding.txtDay.setTextColor(color);
        binding.txtMonth.setTextColor(color);
        binding.txtYear.setTextColor(color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.txtAll.setTextAppearance(R.style.Bold);
            binding.txtDay.setTextAppearance(R.style.Normal);
            binding.txtMonth.setTextAppearance(R.style.Normal);
            binding.txtYear.setTextAppearance(R.style.Normal);
        }
    }

    private void replaceFragment(Fragment fragment, String fragmentName) {
        Log.d("--library_frag--", "replaceFragment: " + fragmentName);
        getChildFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragmentName).add(binding.fragmentContainer.getId(), fragment, fragmentName)
                .commit();

        PreferenceUtils.setString(activity, PreferenceUtils.LIBRARY_FRAGMENT, fragmentName);
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null) {
            if (fragment.isVisible()) {
                getChildFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .hide(fragment).commit();
            }
        }
    }

    public void yearToMonthTransition(String month) {
        callMonthFragment();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                monthsFragment.yearClicked(month);
            }
        });
    }

    public void monthToDayTransition(String day) {
        callDayFragment();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                daysFragment.monthClicked(day);
            }
        });
    }

    public void refreshData(ArrayList<MediaModel> mediaModels) {
        if (allFragment == null) {
            allFragment = new AllFragment().getInstance();
        }
        allFragment.refreshData(mediaModels);

        if (daysFragment == null) {
            daysFragment = new DaysFragment().getInstance();
        }
        daysFragment.refreshData(mediaModels);

        if (monthsFragment == null) {
            monthsFragment = new MonthsFragment().getInstance();
        }
        monthsFragment.refreshData(mediaModels);

        if (yearsFragment == null) {
            yearsFragment = new YearsFragment().getInstance();
        }
        yearsFragment.refreshData(mediaModels);
    }
}
