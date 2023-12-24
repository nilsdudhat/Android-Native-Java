package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.PhoneAlbumModel;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.PhonePhotoModel;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.AlbumListActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.GalleryListActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.Share;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.views.SquareLayout;

import java.util.ArrayList;
import java.util.Collections;

public class PhoneAlbumAdapter extends RecyclerView.Adapter<PhoneAlbumAdapter.ViewHolder> {

    public ArrayList<PhoneAlbumModel> al_album = new ArrayList<>();
    Activity baseActivity;

    public PhoneAlbumAdapter(Activity baseActivity, ArrayList<PhoneAlbumModel> albumArrayList) {
        this.baseActivity = baseActivity;
        this.al_album = albumArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        int position = holder.getAdapterPosition();

        ((ViewHolder) holder).setIsRecyclable(false);
        ((ViewHolder) holder).progressBar_phone.setVisibility(View.VISIBLE);

        Glide.with(baseActivity).load(((PhoneAlbumModel) this.al_album.get(position)).getCoverUri()).listener(new RequestListener<Drawable>() {
            public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                ((ViewHolder) holder).progressBar_phone.setVisibility(View.GONE);
                return false;
            }

            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                ((ViewHolder) holder).progressBar_phone.setVisibility(View.GONE);
                return false;
            }
        }).into(((ViewHolder) holder).iv_album);
        ((ViewHolder) holder).tv_album_name.setText(((PhoneAlbumModel) this.al_album.get(position)).getName());

        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Share.lst_album_image.clear();
                for (int i = 0; i < ((PhoneAlbumModel) al_album.get(position)).getAlbumPhotos().size(); i++) {
                    Share.lst_album_image.add(((PhonePhotoModel) ((PhoneAlbumModel) al_album.get(position)).getAlbumPhotos().get(i)).getPhotoUri());
                }

                Collections.reverse(Share.lst_album_image);
                Intent intent = new Intent(baseActivity, AlbumListActivity.class);
                intent.putExtra(Share.KEYNAME.ALBUM_NAME, ((PhoneAlbumModel) al_album.get(position)).getName());
                ((GalleryListActivity) baseActivity).showInterstitialAd(baseActivity, intent, null);
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
        TextView tv_album_name;
        SquareLayout square_layout;

        public ViewHolder(View view) {
            super(view);
            this.tv_album_name = view.findViewById(R.id.tv_album_name);
            this.iv_album = view.findViewById(R.id.iv_album);
            this.progressBar_phone = view.findViewById(R.id.progressBar);
            this.square_layout = view.findViewById(R.id.square_layout);
        }
    }
}
