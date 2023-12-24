package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.interfaces.DeleteClickListener;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.interfaces.ImageClickListener;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    Activity activity;

    static ArrayList<ImageModel> pathList = new ArrayList<>();

    ImageClickListener imageClickListener;
    DeleteClickListener deleteClickListener;

    public ImageAdapter(Activity activity, ImageClickListener imageClickListener, DeleteClickListener deleteClickListener) {
        this.activity = activity;
        this.imageClickListener = imageClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            Glide.with(activity).load(pathList.get(holder.getAdapterPosition()).getPathList()).into(holder.iv_album);

            if (pathList.get(position).isChecked()) {
                holder.ll_checked.setVisibility(View.VISIBLE);
            } else {
                holder.ll_checked.setVisibility(View.GONE);
            }
            holder.bind(pathList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    public void swapList(ArrayList<ImageModel> pathList) {
        this.pathList = new ArrayList<>(pathList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView iv_album;
        ImageView img_sel_delete;
        LinearLayout ll_checked;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_album = itemView.findViewById(R.id.iv_album);
            ll_checked = itemView.findViewById(R.id.ll_checked);
            img_sel_delete = itemView.findViewById(R.id.img_sel_delete);
        }

        void bind(final ImageModel model) {
            if (model.isChecked()) ll_checked.setVisibility(View.VISIBLE);
            else ll_checked.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getSelected().size() == 0) {
                        if (imageClickListener != null) {
                            imageClickListener.onImageClick(pathList.get(getAdapterPosition()).getPathList());
                        }
                    } else {
                        if (model.isChecked()) {
                            model.setChecked(false);
                        } else {
                            model.setChecked(true);
                        }
                        ll_checked.setVisibility(model.isChecked() ? View.VISIBLE : View.GONE);

                        if (imageClickListener != null) {
                            imageClickListener.onItemSelected(getSelected().size(), pathList.size());
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (model.isChecked()) {
                        model.setChecked(false);
                    } else {
                        model.setChecked(true);
                    }
                    ll_checked.setVisibility(model.isChecked() ? View.VISIBLE : View.GONE);
                    if (imageClickListener != null) {
                        imageClickListener.onItemSelected(getSelected().size(), pathList.size());
                    }
                    return true;
                }
            });
        }

    }

    public void deleteSelectedImages() {
        List<ImageModel> deleteList = getSelected();
        deleteClickListener.onDeleteClick(deleteList);
        notifyDataSetChanged();
    }

    public static List<ImageModel> getSelected() {
        ArrayList<ImageModel> selected = new ArrayList<>();
        for (int i = 0; i < pathList.size(); i++) {
            if (pathList.get(i).isChecked()) {
                selected.add(pathList.get(i));
            }
        }
        return selected;
    }
}
