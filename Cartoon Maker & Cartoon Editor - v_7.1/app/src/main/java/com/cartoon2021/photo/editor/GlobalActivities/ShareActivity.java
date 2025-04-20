package com.cartoon2021.photo.editor.GlobalActivities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.Glob;
import com.cartoon2021.photo.editor.R;

public class ShareActivity extends BaseActivity implements View.OnClickListener {

    ImageView img_back;
    ImageView img_home;
    ImageView finalimg;
    ImageView iv_facebook;
    ImageView iv_instragram;
    ImageView iv_more;
    ImageView iv_twitter;
    ImageView iv_whatsapp;
    public File outputFile;
    Intent shareIntent;
    TextView tvFinalImagePath;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_share);

        showInterstitial(ShareActivity.this);
        showMiniNativeAd(ShareActivity.this, findViewById(R.id.ad_mini_native));

        bindView();
        this.outputFile = new File(Glob.shareUri);
    }

    private void bindView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        img_home = (ImageView) findViewById(R.id.img_home);
        img_home.setOnClickListener(this);

        finalimg = (ImageView) findViewById(R.id.finalimg);

        finalimg.setImageBitmap(Glob.finalBitmap.copy(Bitmap.Config.ARGB_8888, true));

        tvFinalImagePath = (TextView) findViewById(R.id.tvFinalImagePath);
        tvFinalImagePath.setSelected(true);
        tvFinalImagePath.setText(Glob.shareUri);
        iv_whatsapp = (ImageView) findViewById(R.id.iv_whatsapp);
        iv_whatsapp.setOnClickListener(this);
        iv_facebook = (ImageView) findViewById(R.id.iv_facebook);
        iv_facebook.setOnClickListener(this);
        iv_instragram = (ImageView) findViewById(R.id.iv_instagram);
        iv_instragram.setOnClickListener(this);
        iv_more = (ImageView) findViewById(R.id.iv_share_more);
        iv_more.setOnClickListener(this);
        iv_twitter = (ImageView) findViewById(R.id.img_twitter);
        iv_twitter.setOnClickListener(this);
        this.img_home.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink));
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("image/*");
        shareIntent.putExtra("android.intent.extra.TEXT", Glob.app_name + " Created By : " + Glob.app_link);
        this.shareIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(Glob.shareUri)));
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                return;
            case R.id.img_home:
//                if (getIntent().getStringExtra("type").equalsIgnoreCase("cartoon editor")) {
//                    Intent intentEditor = new Intent(this, AlbumActivity.class);
//                    intentEditor.putExtra("type", "cartoon editor");
//                    startActivity(intentEditor);
//                    finishAffinity();
//                }
//                if (getIntent().getStringExtra("type").equalsIgnoreCase("cartoon maker")) {
//                    Intent intentEditor = new Intent(this, AlbumActivity.class);
//                    intentEditor.putExtra("type", "cartoon maker");
//                    startActivity(intentEditor);
//                    finishAffinity();
//                }
                Glob.onBackPressedIntent(ShareActivity.this, new Intent(ShareActivity.this, StartActivity.class));
                finishAffinity();
                return;
            case R.id.iv_facebook:
                Uri uriForFile2 = FileProvider.getUriForFile(this, "com.cartoon2021.photo.editor.provider", this.outputFile);
                Intent intent5 = new Intent("android.intent.action.SEND");
                intent5.setType("image/*");
                intent5.putExtra("android.intent.extra.TEXT", Glob.app_name + " Created By : " + Glob.app_link);
                if (Build.VERSION.SDK_INT >= 24) {
                    intent5.putExtra("android.intent.extra.STREAM", uriForFile2);
                } else {
                    intent5.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(Glob.shareUri)));
                }
                try {
                    intent5.setPackage("com.facebook.katana");
                    startActivity(intent5);
                    return;
                } catch (Exception unused2) {
                    Toast.makeText(getApplicationContext(), "Facebook doesn't installed", Toast.LENGTH_LONG).show();
                    return;
                }
            case R.id.iv_instagram:
                Uri uriForFile3 = FileProvider.getUriForFile(this, "com.cartoon2021.photo.editor.provider", this.outputFile);
                Intent intent6 = new Intent("android.intent.action.SEND");
                intent6.setType("image/*");
                intent6.putExtra("android.intent.extra.TEXT", Glob.app_name + " Created By : " + Glob.app_link);
                if (Build.VERSION.SDK_INT >= 24) {
                    intent6.putExtra("android.intent.extra.STREAM", uriForFile3);
                } else {
                    intent6.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(Glob.shareUri)));
                }
                try {
                    intent6.setPackage("com.instagram.android");
                    startActivity(intent6);
                    return;
                } catch (Exception unused3) {
                    Toast.makeText(getApplicationContext(), "Instragram doesn't installed", Toast.LENGTH_LONG).show();
                    return;
                }
            case R.id.iv_share_more:
                FileProvider.getUriForFile(this, "com.cartoon2021.photo.editor.provider", this.outputFile);
                Intent intent7 = new Intent("android.intent.action.SEND");
                intent7.setType("image/*");
                intent7.putExtra("android.intent.extra.TEXT", Glob.Edit_Folder_name + " Create By : " + Glob.app_link);
                intent7.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.cartoon2021.photo.editor.provider", new File(Glob.shareUri)));
                startActivity(Intent.createChooser(intent7, "Share Image using"));
                return;
            case R.id.img_twitter:
                Uri uriForFile4 = FileProvider.getUriForFile(this, "com.cartoon2021.photo.editor.provider", this.outputFile);
                Intent intent8 = new Intent("android.intent.action.SEND");
                intent8.setType("image/*");
                intent8.putExtra("android.intent.extra.TEXT", Glob.app_name + " Created By : " + Glob.app_link);
                if (Build.VERSION.SDK_INT >= 24) {
                    intent8.putExtra("android.intent.extra.STREAM", uriForFile4);
                } else {
                    intent8.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(Glob.shareUri)));
                }
                try {
                    intent8.setPackage("com.twitter.android");
                    startActivity(intent8);
                    return;
                } catch (Exception unused4) {
                    Toast.makeText(getApplicationContext(), "Twitter doesn't installed", Toast.LENGTH_LONG).show();
                    return;
                }
            case R.id.iv_whatsapp:
                Uri uriForFile5 = FileProvider.getUriForFile(this, "com.cartoon2021.photo.editor.provider", this.outputFile);
                Intent intent9 = new Intent("android.intent.action.SEND");
                intent9.setType("image/*");
                intent9.putExtra("android.intent.extra.TEXT", Glob.app_name + " Created By : " + Glob.app_link);
                if (Build.VERSION.SDK_INT >= 24) {
                    intent9.putExtra("android.intent.extra.STREAM", uriForFile5);
                } else {
                    intent9.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(Glob.shareUri)));
                }
                try {
                    intent9.setPackage("com.whatsapp");
                    startActivity(intent9);
                    return;
                } catch (Exception unused5) {
                    Toast.makeText(getApplicationContext(), "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
                    return;
                }
            default:
        }
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(ShareActivity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_share);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        ImageView img_close = dialog.findViewById(R.id.img_close);
        TextView txt_edit_yes = dialog.findViewById(R.id.txt_edit_yes);
        TextView txt_yes_home = dialog.findViewById(R.id.txt_yes_home);

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        txt_edit_yes.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        txt_yes_home.setOnClickListener(v -> {
            dialog.dismiss();
            Glob.onBackPressedIntent(ShareActivity.this, new Intent(ShareActivity.this, DashBoardActivity.class));
            finishAffinity();
        });
    }
}
