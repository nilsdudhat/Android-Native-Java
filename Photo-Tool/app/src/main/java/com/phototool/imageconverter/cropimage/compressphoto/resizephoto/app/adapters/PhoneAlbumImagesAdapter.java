package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.AlbumListActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.CropActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.Share;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.views.SquareLayout;

import java.util.ArrayList;

public class PhoneAlbumImagesAdapter extends RecyclerView.Adapter<PhoneAlbumImagesAdapter.ViewHolder> {

    public Activity baseActivity;
    public ArrayList<String> al_album = new ArrayList<>();

    public PhoneAlbumImagesAdapter(Activity baseActivity, ArrayList<String> arrayList) {
        this.baseActivity = baseActivity;
        this.al_album = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int i = holder.getAdapterPosition();

        ViewHolder viewHolder = (ViewHolder) holder;

//        Log.d("adapter_position", "i % 3: " + (i % 3));
//        if (i == 0) {
//            Log.d("adapter_position", "listDelta: " + i);
//            setMargins(viewHolder.square_layout, 0, 10, 10, 10);
//        } else {
//            if ((i % 3) == 0) {
//                Log.d("adapter_position", "(i % 3) == 0: " + i);
//                setMargins(viewHolder.square_layout, 0, 10, 10, 10);
//            } else if ((i % 3) == 1) {
//                Log.d("adapter_position", "(i % 3) == 1: " + i);
//                setMargins(viewHolder.square_layout, 5, 10, 5, 10);
//            } else if ((i % 3) == 2) {
//                Log.d("adapter_position", "(i % 3) == 2: " + i);
//                setMargins(viewHolder.square_layout, 10, 10, 0, 10);
//            }
//        }
//        Log.d("adapter_position", "=================================");

        viewHolder.setIsRecyclable(false);
        viewHolder.progressBar_phone.setVisibility(View.VISIBLE);

        Glide.with(this.baseActivity).load((String) this.al_album.get(i)).listener(new RequestListener<Drawable>() {
            public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                viewHolder.progressBar_phone.setVisibility(View.GONE);
                return false;
            }

            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                viewHolder.progressBar_phone.setVisibility(View.GONE);
                return false;
            }
        }).into(viewHolder.iv_album);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("--url--", "onClick: " + al_album.get(i));
                String sb2 = "file:///" + al_album.get(i);
                Share.SAVED_BITMAP = Uri.parse(sb2);
                Share.originalPath = (String) al_album.get(i);
                Intent intent = new Intent(baseActivity, CropActivity.class);
                intent.putExtra(Share.KEYNAME.SELECTED_PHONE_IMAGE, (String) al_album.get(i));
                intent.setData(Share.SAVED_BITMAP);
                ((AlbumListActivity) baseActivity).showInterstitialAd(baseActivity, intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_album;
        ProgressBar progressBar_phone;
        SquareLayout square_layout;

        public ViewHolder(View view) {
            super(view);
            this.iv_album = view.findViewById(R.id.iv_album);
            this.progressBar_phone = (ProgressBar) view.findViewById(R.id.progressBar);
            this.square_layout = view.findViewById(R.id.square_layout);
        }
    }
}
