package com.cartoon2021.photo.editor.CartoonEditor.Activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.GlobalActivities.AlbumActivity;
import com.cartoon2021.photo.editor.Glob;
import com.cartoon2021.photo.editor.R;

import java.io.IOException;

public class EditorHomeActivity extends BaseActivity {

    ImageView img_back;

    CardView card_edit;
    CardView card_album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_home);

        showInterstitial(EditorHomeActivity.this);
        showMiniNativeAd(EditorHomeActivity.this, findViewById(R.id.ad_mini_native));

        img_back = findViewById(R.id.img_back);

        card_edit = findViewById(R.id.card_edit);
        card_album = findViewById(R.id.card_album);

        img_back.setOnClickListener(v -> onBackPressed());

        card_album.setOnClickListener(v -> {
            Intent intentAlbum = new Intent(EditorHomeActivity.this, AlbumActivity.class);
            intentAlbum.putExtra("type", "cartoon editor");
            startActivity(intentAlbum);
        });

        card_edit.setOnClickListener(v -> {
            if (isOnline()) {
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 100);
            } else {
                Toast.makeText(EditorHomeActivity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1 && requestCode == 100) {
            try {
                Glob.gallery_piv = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(this, CropActivity.class));
//            finish();
        }
    }

//    @Override
//    public void onBackPressed() {
//        Glob.onBackPressedIntent(EditorHomeActivity.this, new Intent(EditorHomeActivity.this, DashBoardActivity.class));
//        finish();
//    }
}
