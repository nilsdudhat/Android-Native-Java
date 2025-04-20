package com.cartoon2021.photo.editor.CartoonMaker.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cartoon2021.photo.editor.AdUtils.AdUtils;
import com.cartoon2021.photo.editor.AdUtils.AppUtility;
import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.AdUtils.Constant;
import com.cartoon2021.photo.editor.CartoonMaker.adapter.ImojiPartAdapter;
import com.cartoon2021.photo.editor.CartoonMaker.fragment.FaceSwapFragment;
import com.cartoon2021.photo.editor.CartoonMaker.helper.AppConfig;
import com.cartoon2021.photo.editor.CartoonMaker.helper.SettingManager;
import com.cartoon2021.photo.editor.CartoonMaker.interfaces.Communicator;
import com.cartoon2021.photo.editor.Glob;
import com.cartoon2021.photo.editor.GlobalActivities.ShareActivity;
import com.cartoon2021.photo.editor.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class MaleActivity extends BaseActivity implements Communicator {

    private static final int MENU_MERGE = 1;
    private static final int MENU_RANDOM = 0;
    ArrayList<ImageView> allImageLayers = new ArrayList<>();
    ArrayList<FaceSwapFragment> arrayFaceSwap = new ArrayList<>();
    RelativeLayout bContainer;
    int[] back = {R.drawable.emptyblank, R.drawable.border1, R.drawable.border_2, R.drawable.border_3, R.drawable.border_4, R.drawable.border_5, R.drawable.border_6, R.drawable.border_7, R.drawable.border_8, R.drawable.border_9, R.drawable.border_10, R.drawable.border_11, R.drawable.border_12, R.drawable.border_13, R.drawable.border_4, R.drawable.border_15, R.drawable.b16, R.drawable.border_7, R.drawable.b18, R.drawable.b19, R.drawable.b20, R.drawable.b21, R.drawable.b22, R.drawable.b23, R.drawable.b24, R.drawable.b24, R.drawable.b25, R.drawable.b26, R.drawable.b27, R.drawable.b28, R.drawable.b29, R.drawable.b30, R.drawable.b31, R.drawable.border_2, R.drawable.b33, R.drawable.b34, R.drawable.b35};
    public String check_Cat;
    private int colorPos;
    public int gridposition = 0;
    public String[] hairarray = {"#000000", "#3c7f4c", "#3e3432", "#8c500a", "#59c0e3", "#67b82d", "#31836c", "#94529a", "#282828", "#317582", "#320001", "#405200", "##a1362e", "#b8f0ed", "#b286b7", "#bd7018", "#bed000", "#c7d6c3", "#e95f45", "#ed7119", "#f5c83b", "#f99595", "#fe6055", "#fee372", "#ffffff"};
    private ImageView imageView;
    boolean inviteTAG;
    private int layerPostion = 0;
    private FrameLayout mContainer;
    private int mSaveIndex;
    private SettingManager mSettingManager;
    int[] neck = {R.drawable.dress_1, R.drawable.dress_2, R.drawable.dress_3, R.drawable.dress_4, R.drawable.dress_5, R.drawable.dress_6, R.drawable.dress_7, R.drawable.dress_8, R.drawable.dress_9, R.drawable.dress_10, R.drawable.dress_11, R.drawable.dress_12, R.drawable.dress_13, R.drawable.dress_14, R.drawable.dress_15, R.drawable.dress_16, R.drawable.dress_17, R.drawable.dress_18, R.drawable.dress_19, R.drawable.dress_20, R.drawable.dress_21, R.drawable.dress_22, R.drawable.dress_23, R.drawable.dress_24, R.drawable.dress_25, R.drawable.dress_26, R.drawable.dress_27};
    boolean rateTAG;
    TextView save_btn;
    public SharedPreferences sharedPreferences;
    public String[] skinarray = {"#fed9d0", "#fedcb6", "#fee1bf", "#feeac9", "#fef5ec", "#fefbf6", "#ffe4db", "#ffefdf", "#7d7256", "#8f7042", "#846a37", "#544933", "#a65a4a", "#a88067", "#b8866d", "#c7d6c3", "#c68f70", "#d29e77", "#e2ae86", "#ebbf9c", "#fdd2cc", "#fed4bb"};
    private TabLayout tabLayout;
    private int tabpos = 0;
    ViewPager viewPager;

    ImageView img_back;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mSettingManager = SettingManager.getInstance(this);
        setContentView(R.layout.activity_male);

        showInterstitial(MaleActivity.this);
        showBannerAd(MaleActivity.this, findViewById(R.id.ad_banner));

        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(v -> onBackPressed());

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = defaultSharedPreferences;
        rateTAG = defaultSharedPreferences.getBoolean("RateTAG", false);
        inviteTAG = sharedPreferences.getBoolean("InviteTAG", false);
        imageView = (ImageView) findViewById(R.id.neck_image);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        save_btn = (TextView) findViewById(R.id.btn_save);
        mContainer = (FrameLayout) findViewById(R.id.imoji_frame);
        bContainer = (RelativeLayout) findViewById(R.id.b_layout);
        for (int i = 0; i <= 13; i++) {
            FaceSwapFragment faceSwape = new FaceSwapFragment();
            faceSwape.setFaceSwap(i);
            arrayFaceSwap.add(faceSwape);
            TabLayout tabLayout2 = tabLayout;
            tabLayout2.addTab(tabLayout2.newTab());
        }
        tabLayout.setTabGravity(0);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new Pager(getSupportFragmentManager(), tabLayout.getTabCount()));
        tabLayout.setupWithViewPager(viewPager);
        createLayer();
        save_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new SaveImage().execute();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
                tabpos = i;
                Log.d("", i + "" + layerPostion);
                if (i == 0 || i == 1) {
                    layerPostion = 1;
                } else if (i == 2 || i == 3) {
                    layerPostion = 7;
                } else if (i == 4) {
                    layerPostion = 3;
                } else if (i == 5) {
                    layerPostion = 4;
                } else if (i == 6) {
                    layerPostion = 5;
                } else if (i == 7) {
                    layerPostion = 6;
                } else if (i == 8) {
                    layerPostion = 8;
                } else if (i == 9) {
                    layerPostion = 9;
                } else if (i == 10) {
                    layerPostion = 10;
                } else if (i == 11) {
                    layerPostion = 11;
                } else if (i == 12) {
                    layerPostion = 2;
                } else if (i == 13) {
//                    save_btn.setVisibility(View.VISIBLE);
                }
                mSettingManager.setSelectedLayer(mSaveIndex, layerPostion);
            }

            @Override
            public void onPageSelected(int i) {
                tabpos = i;
                Log.i("", i + "" + layerPostion);
                tabLayout.setSelected(true);
                createLayerforImoji();
                Log.d("", i + "" + layerPostion);
                if (i == 0 || i == 1) {
                    layerPostion = 1;
                } else if (i == 2 || i == 3) {
                    layerPostion = 7;
                } else if (i == 4) {
                    layerPostion = 3;
                } else if (i == 5) {
                    layerPostion = 4;
                } else if (i == 6) {
                    layerPostion = 5;
                } else if (i == 7) {
                    layerPostion = 6;
                } else if (i == 8) {
                    layerPostion = 8;
                } else if (i == 9) {
                    layerPostion = 9;
                } else if (i == 10) {
                    layerPostion = 10;
                } else if (i == 11) {
                    layerPostion = 11;
                } else if (i == 12) {
                    layerPostion = 2;
                } else if (i == 13) {
//                    save_btn.setVisibility(View.VISIBLE);
                }
                mSettingManager.setSelectedLayer(mSaveIndex, layerPostion);
            }
        });
    }

    private void createLayerforImoji() {
        layerPostion = mSettingManager.getSelectedLayer(mSaveIndex);
    }

    @SuppressLint("ResourceType")
    @Override
    public void sendData(int i) {
        if (ImojiPartAdapter.defaultFree >= i) {
            changeData(i);
        } else if (!Glob.isNetworkAvailable(this)) {
            new AlertDialog.Builder(this).setIcon(17301543).setMessage("No internet connection!").setNegativeButton(getResources().getString(R.string.no), (DialogInterface.OnClickListener) null).show();
        } else if (!rateTAG) {
            new AlertDialog.Builder(this).setIcon(17301543).setMessage("RateUs 5 Star and Review to get all images free.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String packageName = getPackageName();
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
                    } catch (ActivityNotFoundException unused) {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                    }
                    sharedPreferences.edit().putBoolean("RateTAG", true).apply();
                    rateTAG = true;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ImojiPartAdapter.defaultFree = 500;
                            for (int i = 0; i < FaceSwapFragment.arrayMaleImojiAdapter.size(); i++) {
                                FaceSwapFragment.arrayMaleImojiAdapter.get(i).notifyDataSetChanged();
                            }
                        }
                    }, 2000);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        } else if (!inviteTAG) {
            new AlertDialog.Builder(this).setIcon(17301543).setMessage("Please Invite your friends to unlock all images free.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", "I just love " + getResources().getString(R.string.app_name) + " App and hope you will love it too. \n https://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent, "Share App Via"));
                    sharedPreferences.edit().putBoolean("InviteTAG", true).apply();
                    inviteTAG = true;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ImojiPartAdapter.defaultFree = 500;
                            for (int i = 0; i < FaceSwapFragment.arrayMaleImojiAdapter.size(); i++) {
                                FaceSwapFragment.arrayMaleImojiAdapter.get(i).notifyDataSetChanged();
                            }
                        }
                    }, 2000);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        } else {
            new AlertDialog.Builder(this).setIcon(17301543).setMessage("Click & Download App to unlock all images free.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }
    }

    @Override
    public void changecolor(int i) {
        if (tabpos == 1) {
            colorPos = i;
            allImageLayers.get(layerPostion).setColorFilter(Color.parseColor(skinarray[i]), PorterDuff.Mode.MULTIPLY);
            allImageLayers.get(11).setColorFilter(Color.parseColor(skinarray[i]), PorterDuff.Mode.MULTIPLY);
            imageView.setColorFilter(Color.parseColor(skinarray[colorPos]), PorterDuff.Mode.MULTIPLY);
            return;
        }
        allImageLayers.get(layerPostion).setColorFilter(Color.parseColor(hairarray[i]), PorterDuff.Mode.MULTIPLY);
    }

    public void changeData(int i) {
        gridposition = i;
        int i2 = tabpos;
        if (i2 == 12) {
            for (int i3 = 3; i3 <= 6; i3++) {
                allImageLayers.get(i3).setVisibility(View.INVISIBLE);
                allImageLayers.get(i3).invalidate();
            }
            allImageLayers.get(2).setVisibility(View.VISIBLE);
            allImageLayers.get(2).invalidate();
        } else if (i2 == 4 || i2 == 5 || i2 == 6 || i2 == 7) {
            for (int i4 = 3; i4 <= 6; i4++) {
                allImageLayers.get(i4).setVisibility(View.VISIBLE);
                allImageLayers.get(i4).invalidate();
            }
            allImageLayers.get(2).setVisibility(View.INVISIBLE);
            allImageLayers.get(2).invalidate();
        }
        int i5 = tabpos;
        if (i5 == 1 || i5 == 3) {
            changecolor(i);
            return;
        }
        if (i5 == 10) {
            imageView.setImageResource(neck[i]);
        }
        if (tabpos == 13) {
            ((ImageView) findViewById(R.id.back_image)).setImageResource(back[i]);
        } else {
            allImageLayers.get(layerPostion).setImageResource(AppConfig.RIDS[layerPostion][i]);
            allImageLayers.get(layerPostion).invalidate();
        }
        if (tabpos == 11) {
            allImageLayers.get(11).setColorFilter(Color.parseColor(skinarray[colorPos]), PorterDuff.Mode.MULTIPLY);
            allImageLayers.get(11).invalidate();
        }
    }

    public class Pager extends FragmentStatePagerAdapter {
        int tabCount;

        public Pager(FragmentManager fragmentManager, int i) {
            super(fragmentManager);
            tabCount = i;
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return getResources().getStringArray(R.array.titlename)[i];
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            return arrayFaceSwap.get(i);
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    public void createLayer() {
        mContainer.removeAllViews();
        mContainer.requestLayout();
        mContainer.invalidate();
        for (int i = 0; i < AppConfig.RIDS.length; i++) {
            if (i < 8 || i == 10) {
                mSettingManager.getLayerItem(mSaveIndex, i);
                ImageView imageView2 = new ImageView(this);
                Log.e("Position of layer", i + "");
                imageView2.setImageResource(AppConfig.RIDS[i][0]);
                mContainer.addView(imageView2);
                allImageLayers.add(imageView2);
            } else {
                mSettingManager.getLayerItem(mSaveIndex, i);
                ImageView imageView3 = new ImageView(this);
                Log.e("Position of layer", i + "");
                mContainer.addView(imageView3);
                allImageLayers.add(imageView3);
            }
            if (i == 7) {
                allImageLayers.get(7).setColorFilter(Color.parseColor(hairarray[0]), PorterDuff.Mode.MULTIPLY);
                allImageLayers.get(7).invalidate();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SaveImage extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        Bitmap finalBitmap;
        ProgressDialog progress;

        SaveImage() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MaleActivity.this, R.style.MyAlertDialogStyle);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
//            progress = ProgressDialog.show(MaleActivity.this, "", "Loading...", true);
        }


        public Void doInBackground(Void... voidArr) {
            try {
                bContainer.setDrawingCacheEnabled(true);
                bitmap = bContainer.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
                bContainer.setDrawingCacheEnabled(false);

                finalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Glob.finalBitmap = finalBitmap;

                saveImage(finalBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(Void r4) {
            super.onPostExecute((Void) r4);
            progress.dismiss();

            AdUtils.fullscreenCount = Integer.parseInt(AppUtility.getString(MaleActivity.this, Constant.COUNTER, ""));

            Toast.makeText(getApplicationContext(), "Picture saved!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MaleActivity.this, ShareActivity.class);
            intent.putExtra("type", "cartoon maker");
            startActivity(intent);
        }
    }

    public void saveImage(Bitmap bitmap) {
        String path = getFullFileName(getApplicationContext().getString(R.string.app_name), "png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(path))));
//            Toast.makeText(getApplicationContext(), "Picture saved!", Toast.LENGTH_SHORT).show();

            Glob.shareUri = path;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFullFileName(String str, String str2) {
        return getFolderPath(str) + "/" + getDateFileName() + "." + str2;
    }

    public static String getFolderPath(String str) {
        if (str == null) {
            return null;
        }
        File fileApp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), str);
        if (!fileApp.exists()) {
            fileApp.mkdirs();
        }

        File fileCartoonMaker = new File(fileApp.getAbsoluteFile(), "Cartoon Maker");
        if (!fileCartoonMaker.exists()) {
            fileCartoonMaker.mkdirs();
        }

        return fileCartoonMaker.getAbsolutePath();
    }

    public static String getDateFileName() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
        } catch (Exception unused) {
            return Integer.toHexString(UUID.randomUUID().hashCode());
        }
    }

    @Override
    public void onBackPressed() {
        GenderActivity.isMale = false;
        super.onBackPressed();
    }
}