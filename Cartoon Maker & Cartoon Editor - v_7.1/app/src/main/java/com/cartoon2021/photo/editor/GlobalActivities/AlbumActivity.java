package com.cartoon2021.photo.editor.GlobalActivities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.CartoonEditor.Adapter.GalleryAdapter;
import com.cartoon2021.photo.editor.R;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

public class AlbumActivity extends BaseActivity implements GalleryAdapter.OnDeleteClickListener {

    ArrayList<String> cartoonEditorArrayList = new ArrayList<>();
    ImageView Iv_back_creation;
    RecyclerView recycler_view_album;
    GalleryAdapter GalleryAdapter;

    ConstraintLayout constraint_no_data;
    ConstraintLayout constraint_data;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_album);

        showInterstitial(AlbumActivity.this);
        showBannerAd(AlbumActivity.this, findViewById(R.id.ad_banner));
        showMiniNativeAd(AlbumActivity.this, findViewById(R.id.ad_mini_native));

        constraint_data = findViewById(R.id.constraint_data);
        constraint_no_data = findViewById(R.id.constraint_no_data);
        recycler_view_album = (RecyclerView) findViewById(R.id.recycler_view_album);

        GalleryAdapter = new GalleryAdapter(AlbumActivity.this, AlbumActivity.this, cartoonEditorArrayList);
        cartoonEditorArrayList.clear();

        if (getIntent().getStringExtra("type").equalsIgnoreCase("cartoon maker")) {
            listAllImages(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + "Cartoon Photo Maker & Editor" + "/Cartoon Maker/"));
        }

        if (getIntent().getStringExtra("type").equalsIgnoreCase("cartoon editor")) {
            listAllImages(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + "Cartoon Photo Maker & Editor" + "/Cartoon Photo Editor/"));
        }

        recycler_view_album.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recycler_view_album.setAdapter(GalleryAdapter);
        Iv_back_creation = findViewById(R.id.img_back);
        Iv_back_creation.setOnClickListener(view -> onBackPressed());

        call_no_data();
    }

    public void call_no_data() {
        if (cartoonEditorArrayList.size() == 0) {
            ConstraintLayout constraint_no_data = findViewById(R.id.constraint_no_data);
            constraint_no_data.setVisibility(View.VISIBLE);
            findViewById(R.id.ad_banner).setVisibility(View.GONE);
        }
    }

    private void listAllImages(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length - 1; length >= 0; length--) {
                String file2 = listFiles[length].toString();
                File file3 = new File(file2);
                Log.d("" + file3.length(), "" + file3.length());
                if (file3.length() <= 1024) {
                    Log.e("Invalid Image", "Delete Image");
                } else if (file3.toString().contains(".jpg") || file3.toString().contains(".png") || file3.toString().contains(".jpeg")) {
                    cartoonEditorArrayList.add(file2);
                }
                System.out.println(file2);
            }
            return;
        }
        System.out.println("Empty Folder");
    }

    @Override
    public void onDeleteClick(ArrayList<String> imageGallery, int position) {

        Dialog dialog = new Dialog(AlbumActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_delete);
        dialog.setCancelable(true);
        dialog.show();

        Button buttonNo = dialog.findViewById(R.id.rl_no);
        buttonNo.setOnClickListener(
                v12 -> dialog.dismiss()
        );

        Button buttonYes = dialog.findViewById(R.id.yes);
        buttonYes.setOnClickListener((View.OnClickListener) v1 -> {
            if (imageGallery.get(position) != null) {
                new File(imageGallery.get(position));
                if (new File(imageGallery.get(position)).exists()) {
                    File file = new File(imageGallery.get(position));
                    if (file.exists()) {
                        if (file.delete()) {
                            try {
                                if (imageGallery.size() > position) {
                                    dialog.dismiss();
                                    imageGallery.remove(position);
                                    GalleryAdapter.notifyItemRemoved(position);
                                    call_no_data();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            GalleryAdapter.notifyDataSetChanged();
                        } else {
                            PrintStream printStream = System.out;
                            printStream.println("file not Deleted :" + imageGallery.get(position));
                        }
                    }
                }
            }
        });
    }
}