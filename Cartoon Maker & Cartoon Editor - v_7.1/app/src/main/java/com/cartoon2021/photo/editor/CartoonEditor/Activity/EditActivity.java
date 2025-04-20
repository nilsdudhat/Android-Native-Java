package com.cartoon2021.photo.editor.CartoonEditor.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.cartoon2021.photo.editor.AdUtils.AdUtils;
import com.cartoon2021.photo.editor.AdUtils.AppUtility;
import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.AdUtils.Constant;
import com.cartoon2021.photo.editor.CartoonEditor.Adapter.FrameAdapter;
import com.cartoon2021.photo.editor.CartoonEditor.Adapter.StickerAdapter;
import com.cartoon2021.photo.editor.CartoonEditor.Custom.MyAppComponentmodel;
import com.cartoon2021.photo.editor.CartoonEditor.Custom.MyAppStickerView;
import com.cartoon2021.photo.editor.CartoonEditor.Custom.Util;
import com.cartoon2021.photo.editor.CartoonEditor.ImageStickerView.DemoStickerView;
import com.cartoon2021.photo.editor.CartoonEditor.ImageStickerView.StickerImageView;
import com.cartoon2021.photo.editor.CartoonEditor.Model.FrameModel;
import com.cartoon2021.photo.editor.R;
import com.cartoon2021.photo.editor.CartoonEditor.TextStickerView.StickerTextView;
import com.cartoon2021.photo.editor.Glob;
import com.cartoon2021.photo.editor.CartoonEditor.View.HorizontalListView;
import com.cartoon2021.photo.editor.GlobalActivities.ShareActivity;

public class EditActivity extends BaseActivity implements MyAppStickerView.TouchEventListener {

    public Animation slideup;
    FrameAdapter frameAdapter;
    StickerAdapter stikerAdaptor;
    public String str;
    int view_id;
    private final DemoStickerView.OnTouchSticker onTouchSticker = new Demo();
    public Integer stickerId;

    LinearLayout frame;
    ImageView frame_bg;
    HorizontalListView frame_list;
    FrameLayout FlMainFrame;
    ImageView mImageView;
    public SeekBar size;
    LinearLayout ll_sticker;
    HorizontalListView HLSticker;
    LinearLayout text;
    public StickerImageView sticker;
    TextView save;

    ArrayList<Integer> stickerviewId = new ArrayList<>();
    ArrayList<Integer> stikerlist8;
    ArrayList<FrameModel> framemodel;
    final ArrayList<View> sticekrtextArray = new ArrayList<>();
    private View tempstickerview;
    private Animation slidedown;

    public void removeImageViewControl() {
        int childCount = FlMainFrame.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = FlMainFrame.getChildAt(i);
            if (childAt instanceof MyAppStickerView) {
                ((MyAppStickerView) childAt).setBorderVisibility(false);
            }
        }
    }

    public void removeBorder() {
        for (int i = 0; i < stickerviewId.size(); i++) {
            View findViewById = FlMainFrame.findViewById(stickerviewId.get(i));
            if (findViewById instanceof StickerImageView) {
                ((StickerImageView) findViewById).setControlItemsHidden(true);
            }
        }
        removeImageViewControl();
    }

    public void removeBorder1() {
        for (int i = 0; i < sticekrtextArray.size(); i++) {
            if (sticekrtextArray.get(i) != null) {
                ((StickerTextView) sticekrtextArray.get(i)).setControlItemsHidden(true);
            }
        }
        removeImageViewControl();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_edit);

        showInterstitial(EditActivity.this);
        showBannerAd(EditActivity.this, findViewById(R.id.ad_banner));

        mImageView = (ImageView) findViewById(R.id.mainImageView);
        mImageView.setImageBitmap(Glob.finalBitmap1);
        frame = (LinearLayout) findViewById(R.id.frame);
        ll_sticker = (LinearLayout) findViewById(R.id.ll_sticker);
        text = (LinearLayout) findViewById(R.id.text);
        FlMainFrame = (FrameLayout) findViewById(R.id.glviewFrameLayout);
        frame_list = (HorizontalListView) findViewById(R.id.framelist);
        frame_bg = (ImageView) findViewById(R.id.frame_bg);
        HLSticker = (HorizontalListView) findViewById(R.id.stickerlist);
        save = findViewById(R.id.btn_save);
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(v -> onBackPressed());

        slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_dow);
        FlMainFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeBorder();
                removeBorder1();
                removeImageViewControl();
            }
        });
        frame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                removeBorder1();
                if (frame_list.getVisibility() == View.VISIBLE) {
                    frame_list.setVisibility(View.GONE);
                    frame_list.setAnimation(slidedown);
                } else {
                    frame_list.startAnimation(slideup);
                    HLSticker.setVisibility(View.GONE);
                    frame_list.setVisibility(View.VISIBLE);
                }
                setframebackground();
            }
        });
        ll_sticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                removeBorder();
                removeBorder1();
                if (HLSticker.getVisibility() == View.VISIBLE) {
                    HLSticker.setVisibility(View.GONE);
                    HLSticker.setAnimation(slidedown);
                } else {
                    HLSticker.startAnimation(slideup);
                    frame_list.setVisibility(View.GONE);
                    HLSticker.setVisibility(View.VISIBLE);
                }
                setStickerList8();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                removeBorder();
                removeBorder1();
                HLSticker.setVisibility(View.GONE);
                frame_list.setVisibility(View.GONE);
                startActivityForResult(new Intent(getApplicationContext(), AddTextActivity.class), 101);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                removeBorder();
                removeBorder1();
                frame_list.setVisibility(View.GONE);
                HLSticker.setVisibility(View.GONE);
                create_Save_Image();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1) {
            if (requestCode == 101) {
                addtext();
            }
        }
    }

    private void addtext() {
        sticker = new StickerImageView(getApplicationContext(), onTouchSticker);
        sticker.setImageBitmap(AddTextActivity.finalBitmapText);
        view_id = new Random().nextInt();
        int i = view_id;
        if (i < 0) {
            view_id = i - (i * 2);
        }
        sticker.setId(view_id);
        stickerviewId.add(view_id);
        sticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sticker.setControlItemsHidden(false);
            }
        });
        FlMainFrame.addView(sticker);
    }

    public void onDelete() {
        Log.e("On Delete", "  ");
    }

    public void onEdit(View view, String str) {
        Log.e("On Edit " + view, " : " + str);
        if (str.equals("gone")) {
            if (view != tempstickerview) {
                tempstickerview = view;
                boolean z = tempstickerview instanceof MyAppStickerView;
            }
            boolean z2 = tempstickerview instanceof MyAppStickerView;
            return;
        }
        visibleIs();
    }

    public void onTouchDown(View view) {
        if (view != tempstickerview) {
            Log.e("On Touch Down " + view, " : ");
            removeImageViewControl();
            tempstickerview = view;
            boolean z = tempstickerview instanceof MyAppStickerView;
        }
    }

    public void onTouchUp(View view) {
        visibleIs();
    }

    private void visibleIs() {
        View view = tempstickerview;
        if (view instanceof MyAppStickerView) {
            ((MyAppStickerView) view).getColorType().equals("white");
        }
    }

    class Demo implements DemoStickerView.OnTouchSticker {
        Demo() {
        }

        public void onTouchedSticker() {
            removeBorder();
            removeBorder1();
        }
    }

    public void setframebackground() {
        ArrayList<FrameModel> arrayList = new ArrayList<>();
        framemodel = arrayList;
        arrayList.add(new FrameModel(R.drawable.bh1, R.drawable.border1));
        framemodel.add(new FrameModel(R.drawable.bh2, R.drawable.border_2));
        framemodel.add(new FrameModel(R.drawable.bh3, R.drawable.border_3));
        framemodel.add(new FrameModel(R.drawable.bh4, R.drawable.border_4));
        framemodel.add(new FrameModel(R.drawable.bh5, R.drawable.border_5));
        framemodel.add(new FrameModel(R.drawable.bh6, R.drawable.border_6));
        framemodel.add(new FrameModel(R.drawable.bh7, R.drawable.border_7));
        framemodel.add(new FrameModel(R.drawable.bh8, R.drawable.border_8));
        framemodel.add(new FrameModel(R.drawable.bh9, R.drawable.border_9));
        framemodel.add(new FrameModel(R.drawable.bh10, R.drawable.border_10));
        framemodel.add(new FrameModel(R.drawable.bh11, R.drawable.border_11));
        framemodel.add(new FrameModel(R.drawable.bh12, R.drawable.border_12));
        framemodel.add(new FrameModel(R.drawable.bh13, R.drawable.border_13));
        framemodel.add(new FrameModel(R.drawable.bh14, R.drawable.border_14));
        framemodel.add(new FrameModel(R.drawable.bh15, R.drawable.border_15));
        frameAdapter = new FrameAdapter(this, framemodel);
        frame_list.setAdapter(frameAdapter);
        frame_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                for (int i2 = 0; i2 < adapterView.getChildCount(); i2++) {
                    adapterView.getChildAt(i2).setBackgroundColor(getResources().getColor(R.color.tran));
                }
                FlMainFrame.getLayoutParams().width = Glob.finalBitmap1.getWidth();
                FlMainFrame.getLayoutParams().height = Glob.finalBitmap1.getHeight();
                FlMainFrame.requestLayout();

                FlMainFrame.setForeground(ContextCompat.getDrawable(EditActivity.this, framemodel.get(i).getFrame()));
            }
        });
    }

    private void setStickerList8() {
        setArraylistForSticker8();
        stikerAdaptor = new StickerAdapter(this, stikerlist8);
        HLSticker.setAdapter(stikerAdaptor);
        HLSticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                stickerId = stikerlist8.get(i);
                MyAppComponentmodel myAppComponentmodel = new MyAppComponentmodel();
                myAppComponentmodel.setPOS_X((float) ((FlMainFrame.getWidth() / 2) - Util.dpToPx(getApplicationContext(), 70)));
                myAppComponentmodel.setPOS_Y((float) ((FlMainFrame.getHeight() / 2) - Util.dpToPx(getApplicationContext(), 70)));
                myAppComponentmodel.setWIDTH(Util.dpToPx(getApplicationContext(), 140));
                myAppComponentmodel.setHEIGHT(Util.dpToPx(getApplicationContext(), 140));
                myAppComponentmodel.setROTATION(0.0f);
                myAppComponentmodel.setRES_ID(stickerId);
                myAppComponentmodel.setCOLORTYPE("white");
                myAppComponentmodel.setTYPE("STICKER");
                MyAppStickerView myAppStickerView = new MyAppStickerView(getApplicationContext());
                myAppStickerView.setComponentInfo(myAppComponentmodel);
                FlMainFrame.addView(myAppStickerView);
                myAppStickerView.setOnTouchCallbackListener(EditActivity.this);
                myAppStickerView.setBorderVisibility(true);
                HLSticker.setVisibility(View.GONE);
            }
        });
    }

    private void setArraylistForSticker8() {
        stikerlist8 = new ArrayList<>();
        stikerlist8.add(R.mipmap.s1);
        stikerlist8.add(R.mipmap.s2);
        stikerlist8.add(R.mipmap.s3);
        stikerlist8.add(R.mipmap.s4);
        stikerlist8.add(R.mipmap.s5);
        stikerlist8.add(R.mipmap.s6);
        stikerlist8.add(R.mipmap.s7);
        stikerlist8.add(R.mipmap.s8);
        stikerlist8.add(R.mipmap.s9);
        stikerlist8.add(R.mipmap.s10);
        stikerlist8.add(R.mipmap.s11);
        stikerlist8.add(R.mipmap.s13);
        stikerlist8.add(R.mipmap.s14);
        stikerlist8.add(R.mipmap.s18);
        stikerlist8.add(R.mipmap.s19);
        stikerlist8.add(R.mipmap.s25);
        stikerlist8.add(R.mipmap.s26);
        stikerlist8.add(R.mipmap.s27);
        stikerlist8.add(R.mipmap.s28);
        stikerlist8.add(R.mipmap.s29);
        stikerlist8.add(R.mipmap.s30);
        stikerlist8.add(R.mipmap.s31);
        stikerlist8.add(R.mipmap.s32);
        stikerlist8.add(R.mipmap.s33);
        stikerlist8.add(R.mipmap.s34);
        stikerlist8.add(R.mipmap.s35);
        stikerlist8.add(R.mipmap.s36);
        stikerlist8.add(R.mipmap.s37);
    }

    public void create_Save_Image() {
        AdUtils.fullscreenCount = Integer.parseInt(AppUtility.getString(EditActivity.this, Constant.COUNTER, ""));

        Glob.finalBitmap = getBitmap(FlMainFrame);
        saveImage(Glob.finalBitmap);
        Intent intentShare = new Intent(EditActivity.this, ShareActivity.class);
        intentShare.putExtra("type", "cartoon editor");
        startActivity(intentShare);
    }

    public void saveImage(Bitmap bitmap) {
        Log.v("TAG", "saveImageInCache is called");
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(externalStoragePublicDirectory.getAbsolutePath() + "/" + "Cartoon Photo Maker & Editor" + "/Cartoon Photo Editor");
        file.mkdirs();
        String str2 = new SimpleDateFormat("HHmmss_dd_MM_yyyy").format(new Date()) + ".jpeg";
        File file2 = new File(file, str2);
        file2.renameTo(file2);
        String str3 = "file://" + externalStoragePublicDirectory.getAbsolutePath() + "/" + "Cartoon Photo Maker & Editor" + "/Cartoon Photo Editor/" + str2;
        Glob.shareUri = externalStoragePublicDirectory.getAbsolutePath() + "/" + "Cartoon Photo Maker & Editor" + "/Cartoon Photo Editor/" + str2;
        Log.d("cache uri=", str3);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(str3))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return CropBitmapTransparency(createBitmap);
    }

    public Bitmap CropBitmapTransparency(Bitmap bitmap) {
        int width2 = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = -1;
        int i2 = -1;
        for (int i3 = 0; i3 < bitmap.getHeight(); i3++) {
            for (int i4 = 0; i4 < bitmap.getWidth(); i4++) {
                if (((bitmap.getPixel(i4, i3) >> 24) & 255) > 0) {
                    if (i4 < width2) {
                        width2 = i4;
                    }
                    if (i4 > i) {
                        i = i4;
                    }
                    if (i3 < height) {
                        height = i3;
                    }
                    if (i3 > i2) {
                        i2 = i3;
                    }
                }
            }
        }
        if (i < width2 || i2 < height) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, width2, height, (i - width2) + 1, (i2 - height) + 1);
    }
}