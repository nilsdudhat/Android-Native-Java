package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.color.MaterialColors;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityMainBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.ForYouModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.AlbumFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.ForYouFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.LibraryFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.SettingsFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.PreferenceUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    LibraryFragment libraryFragment;
    ForYouFragment forYouFragment;
    AlbumFragment albumFragment;
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(MainActivity.this); // providing theme for activity before @onCreate
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // inflating binding
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide(); // removing Action Bar

        loadInterstitialAd(MainActivity.this);

        loadDataInLoop();

        loadAddressData();

        String strFragment = PreferenceUtils.getString(MainActivity.this, PreferenceUtils.MAIN_ACTIVITY);

        switch (strFragment) {
            case "ForYouFragment":
                callForYouFragment();
                break;
            case "AlbumFragment":
                callAlbumsFragment();
                break;
            case "SettingsFragment":
                callSettingsFragment();
                break;
            case "LibraryFragment":
            default:
                callLibraryFragment();
                break;
        }

        binding.tabLibrary.setOnClickListener(view -> {
            callLibraryFragment();
        });

        binding.tabForYou.setOnClickListener(view -> { // click of for you tab on bottom
            callForYouFragment();
        });

        binding.tabAlbums.setOnClickListener(view -> { // click of for you tab on bottom
            callAlbumsFragment();
        });

        binding.tabSettings.setOnClickListener(view -> { // click of for you tab on bottom
            callSettingsFragment();
        });
    }

    private void callSettingsFragment() {
        if (settingsFragment == null) { // if SettingsFragment is null
            settingsFragment = new SettingsFragment().getInstance(); // initiating SettingsFragment
            replaceFragment(settingsFragment, "SettingsFragment"); // if SettingsFragment is not null
        } else { // if SettingsFragment is not null
            if (settingsFragment.isAdded()) { // if SettingsFragment is already added to fragment transaction
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(settingsFragment).commit(); // displaying SettingsFragment with fade animation
            } else {
                replaceFragment(settingsFragment, "SettingsFragment"); // replacing SettingsFragment with current fragment
            }
        }
        // hiding other fragments
        hideFragment(libraryFragment);
        hideFragment(forYouFragment);
        hideFragment(albumFragment);

        // show SettingsFragment as selected
        settingsSelected();
    }

    private void callAlbumsFragment() {
        if (albumFragment == null) { // if AlbumFragment is null
            albumFragment = new AlbumFragment().getInstance(); // initiating AlbumFragment
            replaceFragment(albumFragment, "AlbumFragment"); // if AlbumFragment is not null
        } else { // if AlbumFragment is not null
            if (albumFragment.isAdded()) { // if AlbumFragment is already added to fragment transaction
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(albumFragment).commit(); // displaying AlbumFragment with fade animation
            } else {
                replaceFragment(albumFragment, "AlbumFragment"); // replacing AlbumFragment with current fragment
            }
        }
        // hiding other fragments
        hideFragment(libraryFragment);
        hideFragment(forYouFragment);
        hideFragment(settingsFragment);

        // show AlbumFragment as selected
        albumsSelected();
    }

    private void callForYouFragment() {
        if (forYouFragment == null) { // if ForYouFragment is null
            forYouFragment = new ForYouFragment().getInstance(); // initiating ForYouFragment
            replaceFragment(forYouFragment, "ForYouFragment"); // if ForYouFragment is not null
        } else { // if ForYouFragment is not null
            if (forYouFragment.isAdded()) { // if ForYouFragment is already added to fragment transaction
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(forYouFragment).commit(); // displaying ForYouFragment with fade animation
            } else {
                replaceFragment(forYouFragment, "ForYouFragment"); // replacing ForYouFragment with current fragment
            }
        }
        // hiding other fragments
        hideFragment(libraryFragment);
        hideFragment(albumFragment);
        hideFragment(settingsFragment);

        // show ForYouFragment as selected
        forYouSelected();
    }

    private void callLibraryFragment() {
        if (libraryFragment == null) { // if LibraryFragment is null
            libraryFragment = new LibraryFragment().getInstance(); // initiating LibraryFragment
            replaceFragment(libraryFragment, "LibraryFragment"); // replacing LibraryFragment with current
        } else { // if LibraryFragment is not null
            if (libraryFragment.isAdded()) { // if LibraryFragment is already added to fragment transaction
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(libraryFragment).commit(); // displaying LibraryFragment with fade animation
            } else { // if library fragment not added to fragment transaction
                replaceFragment(libraryFragment, "LibraryFragment"); // replacing LibraryFragment with current fragment
            }
        }
        // hiding other fragments
        hideFragment(forYouFragment);
        hideFragment(albumFragment);
        hideFragment(settingsFragment);

        // show LibraryFragment as selected
        librarySelected();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // thread to remove cache files
        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheUtils.deleteCache(MainActivity.this);
            }
        }).start();
    }

    private void settingsSelected() {
        // managing visibility of Indicator
        binding.indicatorLibrary.setVisibility(View.INVISIBLE);
        binding.indicatorForYou.setVisibility(View.INVISIBLE);
        binding.indicatorAlbums.setVisibility(View.INVISIBLE);
        binding.indicatorSettings.setVisibility(View.VISIBLE);

        if (ThemeUtils.isDark(MainActivity.this)) { // if current theme is Dark
            // Managing tab icons for dark theme
            binding.imgLibrary.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_library_light));
            binding.imgForYou.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_for_you_light));
            binding.imgAlbums.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_albums_light));
            binding.imgSettings.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_settings_light_selected));
        } else { // if current theme is Light
            // Managing tab icons for light theme
            binding.imgLibrary.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_library_dark));
            binding.imgForYou.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_for_you_dark));
            binding.imgAlbums.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_albums_dark));
            binding.imgSettings.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_settings_dark_selected));
        }

        int selectedColor = MaterialColors.getColor(MainActivity.this, R.attr.txt_color, Color.WHITE); // selected text color from created attr
        int color = MaterialColors.getColor(MainActivity.this, R.attr.txt_light, Color.GRAY); // light text color from created attr

        // managing text color of text in tabs
        binding.txtLibrary.setTextColor(color);
        binding.txtForYou.setTextColor(color);
        binding.txtAlbums.setTextColor(color);
        binding.txtSettings.setTextColor(selectedColor);

        // managing boldness of texts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.txtLibrary.setTextAppearance(R.style.Normal);
            binding.txtForYou.setTextAppearance(R.style.Normal);
            binding.txtAlbums.setTextAppearance(R.style.Normal);
            binding.txtSettings.setTextAppearance(R.style.Bold);
        }
    }

    private void albumsSelected() {
        // managing visibility of Indicator
        binding.indicatorLibrary.setVisibility(View.INVISIBLE);
        binding.indicatorForYou.setVisibility(View.INVISIBLE);
        binding.indicatorAlbums.setVisibility(View.VISIBLE);
        binding.indicatorSettings.setVisibility(View.INVISIBLE);

        if (ThemeUtils.isDark(MainActivity.this)) { // if current theme is Dark
            // Managing tab icons for dark theme
            binding.imgLibrary.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_library_light));
            binding.imgForYou.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_for_you_light));
            binding.imgAlbums.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_albums_light_selected));
            binding.imgSettings.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_settings_light));
        } else { // if current theme is Light
            // Managing tab icons for light theme
            binding.imgLibrary.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_library_dark));
            binding.imgForYou.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_for_you_dark));
            binding.imgAlbums.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_albums_dark_selected));
            binding.imgSettings.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_settings_dark));
        }

        int selectedColor = MaterialColors.getColor(MainActivity.this, R.attr.txt_color, Color.WHITE); // selected text color from created attr
        int color = MaterialColors.getColor(MainActivity.this, R.attr.txt_light, Color.GRAY); // light text color from created attr

        // managing text color of text in tabs
        binding.txtLibrary.setTextColor(color);
        binding.txtForYou.setTextColor(color);
        binding.txtAlbums.setTextColor(selectedColor);
        binding.txtSettings.setTextColor(color);

        // managing boldness of texts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.txtLibrary.setTextAppearance(R.style.Normal);
            binding.txtForYou.setTextAppearance(R.style.Normal);
            binding.txtAlbums.setTextAppearance(R.style.Bold);
            binding.txtSettings.setTextAppearance(R.style.Normal);
        }
    }

    private void forYouSelected() {
        // managing visibility of Indicator
        binding.indicatorLibrary.setVisibility(View.INVISIBLE);
        binding.indicatorForYou.setVisibility(View.VISIBLE);
        binding.indicatorAlbums.setVisibility(View.INVISIBLE);
        binding.indicatorSettings.setVisibility(View.INVISIBLE);

        if (ThemeUtils.isDark(MainActivity.this)) { // if current theme is Dark
            // Managing tab icons for dark theme
            binding.imgLibrary.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_library_light));
            binding.imgForYou.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_for_you_light_selected));
            binding.imgAlbums.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_albums_light));
            binding.imgSettings.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_settings_light));
        } else { // if current theme is Light
            // Managing tab icons for light theme
            binding.imgLibrary.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_library_dark));
            binding.imgForYou.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_for_you_dark_selected));
            binding.imgAlbums.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_albums_dark));
            binding.imgSettings.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_settings_dark));
        }

        int selectedColor = MaterialColors.getColor(MainActivity.this, R.attr.txt_color, Color.WHITE); // selected text color from created attr
        int color = MaterialColors.getColor(MainActivity.this, R.attr.txt_light, Color.GRAY); // light text color from created attr

        // managing text color of text in tabs
        binding.txtLibrary.setTextColor(color);
        binding.txtForYou.setTextColor(selectedColor);
        binding.txtAlbums.setTextColor(color);
        binding.txtSettings.setTextColor(color);

        // managing boldness of texts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.txtLibrary.setTextAppearance(R.style.Normal);
            binding.txtForYou.setTextAppearance(R.style.Bold);
            binding.txtAlbums.setTextAppearance(R.style.Normal);
            binding.txtSettings.setTextAppearance(R.style.Normal);
        }
    }

    private void librarySelected() {
        // managing visibility of Indicator
        binding.indicatorLibrary.setVisibility(View.VISIBLE);
        binding.indicatorForYou.setVisibility(View.INVISIBLE);
        binding.indicatorAlbums.setVisibility(View.INVISIBLE);
        binding.indicatorSettings.setVisibility(View.INVISIBLE);

        if (ThemeUtils.isDark(MainActivity.this)) { // if current theme is Dark
            // Managing tab icons for dark theme
            binding.imgLibrary.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_library_light_selected));
            binding.imgForYou.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_for_you_light));
            binding.imgAlbums.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_albums_light));
            binding.imgSettings.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_settings_light));
        } else { // if current theme is Light
            // Managing tab icons for light theme
            binding.imgLibrary.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_library_dark_selected));
            binding.imgForYou.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_for_you_dark));
            binding.imgAlbums.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_albums_dark));
            binding.imgSettings.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_settings_dark));
        }

        int selectedColor = MaterialColors.getColor(MainActivity.this, R.attr.txt_color, Color.WHITE); // selected text color from created attr
        int color = MaterialColors.getColor(MainActivity.this, R.attr.txt_light, Color.GRAY); // light text color from created attr

        // managing text color of text in tabs
        binding.txtLibrary.setTextColor(selectedColor);
        binding.txtForYou.setTextColor(color);
        binding.txtAlbums.setTextColor(color);
        binding.txtSettings.setTextColor(color);

        // managing boldness of texts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.txtLibrary.setTextAppearance(R.style.Bold);
            binding.txtForYou.setTextAppearance(R.style.Normal);
            binding.txtAlbums.setTextAppearance(R.style.Normal);
            binding.txtSettings.setTextAppearance(R.style.Normal);
        }
    }

    private void replaceFragment(Fragment fragment, String fragmentName) {
        Log.d("--main_frag--", "replaceFragment: " + fragmentName);

        // replacing fragment with current fragment
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) // fragment animation
                .addToBackStack(fragmentName) // to get remembered fragment state
                .add(binding.fragmentContainer.getId(), fragment, fragmentName) // adding fragment to fragment transaction
                .commit();

        PreferenceUtils.setString(MainActivity.this, PreferenceUtils.MAIN_ACTIVITY, fragmentName);
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null) { // if fragment is null, no need to do anything
            if (fragment.isVisible()) { // if fragment is not visible then no need to do anything
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) // fragment animation
                        .hide(fragment) // hiding fragment
                        .commit();
            }
        }
    }

    @Override
    protected void refreshData(ArrayList<MediaModel> mediaModels) {
        super.refreshData(mediaModels);

        if (libraryFragment == null) {
            libraryFragment = new LibraryFragment().getInstance();
        }
        libraryFragment.refreshData(mediaModels);

        if (albumFragment == null) {
            albumFragment = new AlbumFragment().getInstance();
        }
        albumFragment.refreshData(mediaModels);
    }

    @Override
    protected void refreshFavoritesData(List<AlbumDBModel.FavoriteDBModel> favoriteDBModels) {
        super.refreshFavoritesData(favoriteDBModels);
        Log.d("--fav_list--", "MainActivity: " + favoriteDBModels.size());

        if (albumFragment == null) {
            albumFragment = new AlbumFragment();
        }
//        albumFragment.refreshFavoritesData(favoriteDBModels);
    }

    @Override
    public void refreshForYouData(ForYouModel forYouModel) {
        super.refreshForYouData(forYouModel);

        if (forYouFragment == null) {
            forYouFragment = new ForYouFragment().getInstance();
        }
        forYouFragment.refreshForYouData(forYouModel);
    }

    @Override
    public void onBackPressed() {

        Dialog dialog = new Dialog(MainActivity.this, R.style.CustomDialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_back_pressed);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        TextView txt_title = dialog.findViewById(R.id.txt_title);
        TextView txt_exit = dialog.findViewById(R.id.txt_exit);
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);

        txt_title.setText(getResources().getString(R.string.close_app));

        txt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                QuickQuitActivity.exitApplication(MainActivity.this);
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}