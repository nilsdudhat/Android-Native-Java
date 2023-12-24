package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.ForYouMediaAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityForYouBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.ForYouMediaClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ColorUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.LocationUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

public class ForYouActivity extends BaseActivity implements ForYouMediaClickListener {

    ActivityForYouBinding binding;

    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeUtils.setTheme(ForYouActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivityForYouBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        showBannerAd(ForYouActivity.this, binding.adBanner);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (!Constant.INTENT_FILE_MODEL_ARRAY_LIST.isEmpty()) {
            fileModelArrayList = new ArrayList<>(Constant.INTENT_FILE_MODEL_ARRAY_LIST);

            if (fileModelArrayList.isEmpty()) {
                finish();
            } else {

                // passing this array list inside our adapter class.
                ForYouMediaAdapter adapter = new ForYouMediaAdapter(ForYouActivity.this, fileModelArrayList, this);

                // below method is used to
                // setadapter to sliderview.
                binding.forYouSlider.setSliderAdapter(adapter);

                // below method is used to set auto cycle direction in left to
                // right direction you can change according to requirement.
                binding.forYouSlider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

                // below method is use to set
                // scroll time in seconds.
                binding.forYouSlider.setScrollTimeInSec(2);

                // to set it scrollable automatically
                // we use below method.
                binding.forYouSlider.setAutoCycle(true);

                // to start autocycle below method is used.
                binding.forYouSlider.startAutoCycle();


                // Setup Header
                binding.forYouSlider.setCurrentPagePosition(0);

                FileModel fileModel = fileModelArrayList.get(0);

                String fullDate = DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "dd MMMM, yyyy", fileModel.getDateModified());
                Log.d("--slide_date--", "full date: " + fullDate);

                binding.txtMediaDate.setText(fullDate);

                String address = LocationUtils.getAddressForYou(ForYouActivity.this, fileModel.getPath());

                if (!address.equals("")) {
                    if (address.isEmpty()) {
                        binding.txtAddress.setVisibility(View.GONE);
                    } else {
                        binding.txtAddress.setVisibility(View.VISIBLE);
                        binding.txtAddress.setText(address);
                    }
                }

                binding.forYouSlider.setCurrentPageListener(new SliderView.OnSliderPageListener() {
                    @Override
                    public void onSliderPageChanged(int position) {
                        FileModel fileModel = fileModelArrayList.get(position);

                        String fullDate = DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "dd MMMM, yyyy", fileModel.getDateModified());
                        Log.d("--slide_date--", "full date: " + fullDate);

                        binding.txtMediaDate.setText(fullDate);

                        String address = LocationUtils.getAddressForYou(ForYouActivity.this, fileModel.getPath());

                        if (!address.equals("")) {
                            if (address.isEmpty()) {
                                binding.txtAddress.setVisibility(View.GONE);
                            } else {
                                binding.txtAddress.setVisibility(View.VISIBLE);
                                binding.txtAddress.setText(address);
                            }
                        }
                    }
                });


                binding.imgPlayPause.setImageResource(R.drawable.ic_for_you_pause);
                binding.imgPlayPause.setTag(R.drawable.ic_for_you_pause);

                binding.imgPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((Integer) binding.imgPlayPause.getTag() == R.drawable.ic_for_you_pause) {
                            binding.imgPlayPause.setImageResource(R.drawable.ic_for_you_play);
                            binding.imgPlayPause.setTag(R.drawable.ic_for_you_play);
                            binding.forYouSlider.stopAutoCycle();
                            binding.forYouSlider.setAutoCycle(false);
                        } else if ((Integer) binding.imgPlayPause.getTag() == R.drawable.ic_for_you_play) {
                            binding.imgPlayPause.setImageResource(R.drawable.ic_for_you_pause);
                            binding.imgPlayPause.setTag(R.drawable.ic_for_you_pause);
                            binding.forYouSlider.setAutoCycle(true);
                            binding.forYouSlider.startAutoCycle();
                        }
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.rlTitle.setVisibility(View.GONE);
                        binding.imgPlayPause.setVisibility(View.GONE);
                        binding.forYouSlider.setBackgroundColor(Color.BLACK);

                        Window window = getWindow();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(ContextCompat.getColor(ForYouActivity.this, R.color.black));
                            window.setNavigationBarColor(ContextCompat.getColor(ForYouActivity.this, R.color.black));
                        }
                    }
                }, 5000);

                gestureDetector = new GestureDetector(ForYouActivity.this, new SwipeDetector());
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheUtils.deleteCache(ForYouActivity.this);
            }
        }).start();
    }

    @Override
    public void onClick() {

        Window window = getWindow();

        if (binding.rlTitle.getVisibility() == View.VISIBLE) {
            binding.rlTitle.setVisibility(View.GONE);
            binding.imgPlayPause.setVisibility(View.GONE);
            binding.forYouSlider.setBackgroundColor(Color.BLACK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(ForYouActivity.this, R.color.black));
                window.setNavigationBarColor(ContextCompat.getColor(ForYouActivity.this, R.color.black));
            }
        } else {
            binding.rlTitle.setVisibility(View.VISIBLE);
            binding.imgPlayPause.setVisibility(View.VISIBLE);
            binding.forYouSlider.setBackgroundColor(ColorUtils.getAttributeColor(ForYouActivity.this, R.attr.main_bg));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ColorUtils.getAttributeColor(ForYouActivity.this, R.attr.main_bg));
                window.setNavigationBarColor(ColorUtils.getAttributeColor(ForYouActivity.this, R.attr.main_bg));
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.rlTitle.setVisibility(View.GONE);
                    binding.imgPlayPause.setVisibility(View.GONE);
                    binding.forYouSlider.setBackgroundColor(Color.BLACK);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(ContextCompat.getColor(ForYouActivity.this, R.color.black));
                        window.setNavigationBarColor(ContextCompat.getColor(ForYouActivity.this, R.color.black));
                    }
                }
            }, 5000);
        }
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureDetector;

    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e1 != null && e2 != null) {
                // Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
                // then dismiss the swipe.
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                    return false;

                // Swipe from left to right.
                // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
                // and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
                if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    onBackPressed();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TouchEvent dispatcher.
        if (gestureDetector != null) {
            if (gestureDetector.onTouchEvent(ev))
                // If the gestureDetector handles the event, a swipe has been
                // executed and no more needs to be done.
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}