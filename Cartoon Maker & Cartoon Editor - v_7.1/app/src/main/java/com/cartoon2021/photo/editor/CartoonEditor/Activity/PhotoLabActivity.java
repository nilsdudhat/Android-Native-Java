package com.cartoon2021.photo.editor.CartoonEditor.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.AdUtils.DebounceClickListener;
import com.cartoon2021.photo.editor.CartoonEditor.addnew.PhotoLabEffect;
import com.cartoon2021.photo.editor.R;
import com.cartoon2021.photo.editor.Glob;
import com.cartoon2021.photo.editor.CartoonEditor.addnew.PhotoLab;
import com.cartoon2021.photo.editor.CartoonEditor.addnew.StickerAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.cookie.ClientCookie;

public class PhotoLabActivity extends BaseActivity implements StickerAdapter.onItemClick {

    FrameLayout frameLayout;
    ImageView back;
    ImageView imageView;
    private String imagePath;
    RecyclerView photo_recyclerview;
    TextView txt_done;
    Uri selectedImageUri;
    String[] strings = {"L-zi41", "L-p250", "L-p365", "ice69", "L-p349", "L-pic31", "L-p126", "L-p242", "L-p229", "nahan28", "yiji45", "p134", "zhan47", "p123", "L-p269", "L-p244", "L-p284", "L-p257", "pa78", "man07", "L-p228"};
    ConstraintLayout rl_loader;
    TextView txt_cancel;
    int imgHeight;
    int imgWidth;

    class AsynchronousData extends AsyncHttpResponseHandler {
        private final Gson gson = new Gson();

        AsynchronousData() {
        }

        public void onStart() {
            super.onStart();
        }

        public void onSuccess(int i, Header[] headerArr, byte[] bArr) {
            new String(bArr);
            PhotoLab photoLab = (PhotoLab) gson.fromJson(new String(bArr), PhotoLab.class);
            if ((photoLab != null) && (!photoLab.getPid().isEmpty())) {
                getCategoryItem1(photoLab.getPid());
            }
        }

        public void onFailure(int i, Header[] headerArr, byte[] bArr, Throwable th) {
            rl_loader.setVisibility(View.GONE);
            Log.d("TAG", "onFailure: " + th);
            Toast.makeText(PhotoLabActivity.this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show();
        }
    }

    class AsynchronousData1 extends AsyncHttpResponseHandler {
        Gson gson = new Gson();

        AsynchronousData1() {
        }

        public void onStart() {
            super.onStart();
        }

        public void onSuccess(int i, Header[] headerArr, byte[] bArr) {
            String str = new String(bArr);
            Log.d("TAG", "onSuccess: " + str);
            Glide.with((FragmentActivity) PhotoLabActivity.this).asBitmap().load(((PhotoLabEffect) gson.fromJson(new String(bArr), PhotoLabEffect.class)).getPimageurl()).into(new SimpleTarget<Bitmap>() {
                public void onResourceReady(@NotNull Bitmap bitmap, Transition<? super Bitmap> transition) {
                    imageView.setImageBitmap(bitmap);
                    rl_loader.setVisibility(View.GONE);
                    txt_done.setVisibility(View.VISIBLE);
                }
            });
        }

        public void onFailure(int i, Header[] headerArr, byte[] bArr, Throwable th) {
            rl_loader.setVisibility(View.GONE);
            Log.d("TAG", "onFailure: " + th);
            Toast.makeText(PhotoLabActivity.this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_photo_lab);

        showInterstitial(PhotoLabActivity.this);
        showBannerAd(PhotoLabActivity.this, findViewById(R.id.ad_banner));
        showMiniNativeAd(PhotoLabActivity.this, findViewById(R.id.ad_mini_native));

        txt_cancel = findViewById(R.id.txt_cancel);
        rl_loader = findViewById(R.id.rl_loader);
        imageView = (ImageView) findViewById(R.id.effectid);
        frameLayout = (FrameLayout) findViewById(R.id.FLmain);

        imagePath = getIntent().getStringExtra("imageUri");
        back = (ImageView) findViewById(R.id.img_back);
        txt_done = findViewById(R.id.btn_done);

        txt_cancel.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                rl_loader.setVisibility(View.GONE);
                finish();
            }
        });

        getDropboxIMGSize(Uri.parse(imagePath));

        back.setOnClickListener(view -> onBackPressed());

//        Glide.with(PhotoLabActivity.this).load(imagePath).into(imageView);

//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        imageView.getLayoutParams().width = displayMetrics.widthPixels;
//        imageView.getLayoutParams().height = displayMetrics.widthPixels;
//        imageView.requestLayout();

        photo_recyclerview = (RecyclerView) findViewById(R.id.photo_recyclerview);
        photo_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        photo_recyclerview.setAdapter(new StickerAdapter(getApplicationContext(), this, 3));

        txt_done.setOnClickListener(view -> {
            frameLayout.getLayoutParams().height = imgHeight;
            frameLayout.getLayoutParams().width = imgWidth;
            frameLayout.requestLayout();

            Glob.finalBitmap1 = getBitmap(frameLayout);
            startActivity(new Intent(PhotoLabActivity.this, EditActivity.class));
        });
    }

    private void getDropboxIMGSize(Uri uri){
        Log.d("cropped_image", "uri.toString(): " + uri.toString());
        Log.d("cropped_image", "uri.getPath(): " + uri.getPath());

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(String.valueOf(uri))));

            Log.d("cropped_image", "bitmap.getWidth(): " + bitmap.getWidth());
            Log.d("cropped_image", "bitmap.getHeight(): " + bitmap.getHeight());

            imgWidth = bitmap.getWidth();
            imgHeight = bitmap.getHeight();

            imageView.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(FrameLayout view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public Bitmap CropBitmapTransparency(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = -1;
        int i2 = -1;
        for (int i3 = 0; i3 < bitmap.getHeight(); i3++) {
            for (int i4 = 0; i4 < bitmap.getWidth(); i4++) {
                if (((bitmap.getPixel(i4, i3) >> 24) & 255) > 0) {
                    if (i4 < width) {
                        width = i4;
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
        if (i < width || i2 < height) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, width, height, (i - width) + 1, (i2 - height) + 1);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && intent != null) {
            selectedImageUri = intent.getData();
            PrintStream printStream = System.out;
            printStream.println("datavccnv" + intent.getData());
            String path = getPath(selectedImageUri);
            imagePath = path;
            BitmapFactory.decodeFile(path);
            PrintStream printStream2 = System.out;
            printStream2.println(ClientCookie.PATH_ATTR + imagePath);
            PrintStream printStream3 = System.out;
            printStream3.println("imageptath" + selectedImageUri.toString());
        }
    }

    public String getPath(Uri uri) {
        Cursor managedQuery = managedQuery(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
        int columnIndexOrThrow = managedQuery.getColumnIndexOrThrow("_data");
        managedQuery.moveToFirst();
        return managedQuery.getString(columnIndexOrThrow);
    }

    public void getCategoryItem(String str) {
        rl_loader.setVisibility(View.VISIBLE);
        txt_done.setVisibility(View.GONE);
        RequestParams requestParams = new RequestParams();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(60000);
        asyncHttpClient.addHeader(HttpHeaders.REFERER, "https://www.picas.tech/online.php");
        asyncHttpClient.addHeader("Origin", "https://www.picas.tech");
        asyncHttpClient.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
        if (imagePath != null) {
            File file = new File(imagePath);
            Log.e("filezzxfvzfv", String.valueOf(file));
            try {
                requestParams.put("file", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        requestParams.put("effect", str);
        asyncHttpClient.post("https://webapi.picas.tech/ajax.php?act=create&_t=1515142853928", requestParams, new AsynchronousData());
    }

    public void onItemClick(int i, int i2) {
        getCategoryItem(strings[i]);
    }

    public void getCategoryItem1(String str) {
        new RequestParams();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(60000);
        asyncHttpClient.addHeader(HttpHeaders.REFERER, "https://www.picas.tech/online.php");
        asyncHttpClient.addHeader("Origin", "https://www.picas.tech");
        asyncHttpClient.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str2 = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
        Log.d("TAG", "getCategoryItem1: " + str2);
        asyncHttpClient.post("https://webapi.picas.tech/ajax.php?act=progress&id=" + str + "&_t=" + str2, new AsynchronousData1());
    }

    @Override
    public void onBackPressed() {
        if (rl_loader.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Please wait for sometime... effects are applying!!", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(PhotoLabActivity.this)
            .setMessage("Going back?... It will remove all your editing progress on this photo!!")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
//                    Glob.onBackPressedIntent(PhotoLabActivity.this, new Intent(PhotoLabActivity.this, EditorHomeActivity.class));
//                    finish();
                    PhotoLabActivity.super.onBackPressed();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .create().show();
        }
    }
}
