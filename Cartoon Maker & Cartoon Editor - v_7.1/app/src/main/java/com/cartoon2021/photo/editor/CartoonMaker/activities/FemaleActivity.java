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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cartoon2021.photo.editor.AdUtils.AdUtils;
import com.cartoon2021.photo.editor.AdUtils.AppUtility;
import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.AdUtils.Constant;
import com.cartoon2021.photo.editor.CartoonMaker.adapter.ImojiPartFemaleAdapter;
import com.cartoon2021.photo.editor.CartoonMaker.fragment.FaceSwapFragment;
import com.cartoon2021.photo.editor.CartoonMaker.helper.AppConfig;
import com.cartoon2021.photo.editor.CartoonMaker.helper.SettingManager;
import com.cartoon2021.photo.editor.CartoonMaker.interfaces.Communicator;
import com.cartoon2021.photo.editor.Glob;
import com.cartoon2021.photo.editor.GlobalActivities.ShareActivity;
import com.cartoon2021.photo.editor.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class FemaleActivity extends BaseActivity implements Communicator {

    ArrayList<ImageView> allImageLayers = new ArrayList<>();
    ArrayList<FaceSwapFragment> arrayFaceSwap = new ArrayList<>();

    int[] back = {R.drawable.emptyblank, R.drawable.b1, R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5, R.drawable.b6, R.drawable.b7, R.drawable.b8, R.drawable.b9, R.drawable.b10, R.drawable.b11, R.drawable.b12, R.drawable.b13, R.drawable.b4, R.drawable.b15, R.drawable.b16, R.drawable.b7, R.drawable.b18, R.drawable.b19, R.drawable.b20, R.drawable.b21, R.drawable.b22, R.drawable.b23, R.drawable.b24, R.drawable.b24, R.drawable.b25, R.drawable.b26, R.drawable.b27, R.drawable.b28, R.drawable.b29, R.drawable.b30, R.drawable.b31, R.drawable.b2, R.drawable.b33, R.drawable.b34, R.drawable.b35};
    int[] neck = {R.drawable.gneck1, R.drawable.gneck2, R.drawable.gneck4, R.drawable.gneck6, R.drawable.gneck7, R.drawable.gneck8, R.drawable.gneck9, R.drawable.gneck10, R.drawable.gneck11, R.drawable.gneck12, R.drawable.gneck13, R.drawable.gneck14, R.drawable.gneck15, R.drawable.gneck16, R.drawable.gneck17, R.drawable.gneck18, R.drawable.gneck19, R.drawable.gneck20, R.drawable.gneck21, R.drawable.gneck22, R.drawable.gneck23, R.drawable.gneck24, R.drawable.gneck25};
    String[] hairarray = {"#000000", "#3c7f4c", "#3e3432", "#8c500a", "#59c0e3", "#67b82d", "#31836c", "#94529a", "#282828", "#317582", "#320001", "#405200", "##a1362e", "#b8f0ed", "#b286b7", "#bd7018", "#bed000", "#c7d6c3", "#e95f45", "#ed7119", "#f5c83b", "#f99595", "#fe6055", "#fee372", "#ffffff"};
    String[] skinarray = {"#fed9d0", "#fedcb6", "#fee1bf", "#feeac9", "#fef5ec", "#fefbf6", "#ffe4db", "#ffefdf", "#7d7256", "#8f7042", "#846a37", "#544933", "#a65a4a", "#a88067", "#b8866d", "#c7d6c3", "#c68f70", "#d29e77", "#e2ae86", "#ebbf9c", "#fdd2cc", "#fed4bb"};

    private int colorPos;
    boolean inviteTAG;
    private int layerPostion = 0;
    private int mSaveIndex;
    boolean rateTAG;
    private int tabpos = 0;

    RelativeLayout bContainer;
    ImageView img_back;
    ImageView imageView;
    FrameLayout mContainer;
    TextView save_btn;
    TabLayout tabLayout;
    ViewPager viewPager;

    SettingManager mSettingManager;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mSettingManager = SettingManager.getInstance(this);
        setContentView(R.layout.activity_female);

        showInterstitial(FemaleActivity.this);
        showBannerAd(FemaleActivity.this, findViewById(R.id.ad_banner));

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = defaultSharedPreferences;
        rateTAG = defaultSharedPreferences.getBoolean("RateTAG", false);
        inviteTAG = sharedPreferences.getBoolean("InviteTAG", false);
        ImojiPartFemaleAdapter.defaultFree = 500;

        imageView = (ImageView) findViewById(R.id.neck_image);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        save_btn = (TextView) findViewById(R.id.btn_save);
        img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContainer = (FrameLayout) findViewById(R.id.imoji_frame);
        bContainer = (RelativeLayout) findViewById(R.id.b_layout);
        for (int i = 0; i <= 13; i++) {
            FaceSwapFragment faceSwape = new FaceSwapFragment();
            faceSwape.setFaceSwap(i);
            arrayFaceSwap.add(faceSwape);
            tabLayout.addTab(tabLayout.newTab());
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
        if (ImojiPartFemaleAdapter.defaultFree >= i) {
            changeData(i);
        } else if (!Glob.isNetworkAvailable(this)) {
            new AlertDialog.Builder(this).setIcon(17301543).setMessage("No internet connection!").setNegativeButton(getResources().getString(R.string.no), (DialogInterface.OnClickListener) null).show();
        } else if (!rateTAG) {
            new AlertDialog.Builder(this).setIcon(17301543).setMessage("RateUs 5 Star and Review to get all assets free.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                            ImojiPartFemaleAdapter.defaultFree = 500;
//                            for (int i = 0; i < FaceSwapFragment.arrayFemaleImojiAdapter.size(); i++) {
//                                FaceSwapFragment.arrayFemaleImojiAdapter.get(i).notifyDataSetChanged();
//                            }
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
                            ImojiPartFemaleAdapter.defaultFree = 500;
//                            for (int i = 0; i < FaceSwapFragment.arrayFemaleImojiAdapter.size(); i++) {
//                                FaceSwapFragment.arrayFemaleImojiAdapter.get(i).notifyDataSetChanged();
//                            }
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
                    dialogInterface.dismiss();
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
            allImageLayers.get(layerPostion).setImageResource(AppConfig.FIDS[layerPostion][i]);
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

        @NotNull
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
        for (int i = 0; i < AppConfig.FIDS.length; i++) {
            if (i < 8 || i == 10) {
                mSettingManager.getLayerItem(mSaveIndex, i);
                ImageView imageView2 = new ImageView(this);
                Log.e("Position of layer", i + "");
                imageView2.setImageResource(AppConfig.FIDS[i][0]);
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
        Bitmap finalbmp;
        ProgressDialog progress;

        SaveImage() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(FemaleActivity.this, R.style.MyAlertDialogStyle);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
//            progress = ProgressDialog.show(FemaleActivity.this, "", "Loading...", true);
        }

        public Void doInBackground(Void... voidArr) {
            try {
                bContainer.setDrawingCacheEnabled(true);
                bitmap = bContainer.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
                bContainer.setDrawingCacheEnabled(false);

                finalbmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Glob.finalBitmap = finalbmp.copy(Bitmap.Config.ARGB_8888, true);

                saveImage(finalbmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(Void r4) {
            super.onPostExecute((Void) r4);
            progress.dismiss();

            AdUtils.fullscreenCount = Integer.parseInt(AppUtility.getString(FemaleActivity.this, Constant.COUNTER, ""));

            Toast.makeText(getApplicationContext(), "Picture saved!", Toast.LENGTH_SHORT).show();
            Intent intentFemale = new Intent(FemaleActivity.this, ShareActivity.class);
            intentFemale.putExtra("type", "cartoon maker");
            startActivity(intentFemale);
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
            return new SimpleDateFormat("HHmmss_dd_MM_yyyy").format(Calendar.getInstance().getTime());
        } catch (Exception unused) {
            return Integer.toHexString(UUID.randomUUID().hashCode());
        }
    }

    @Override
    public void onBackPressed() {
        GenderActivity.isFemale = false;
        super.onBackPressed();
    }
}
