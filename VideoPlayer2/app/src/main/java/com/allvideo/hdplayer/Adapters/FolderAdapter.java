package com.allvideo.hdplayer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allvideo.hdplayer.Activities.VideoFolderActivity;
import com.allvideo.hdplayer.Custom.Utils;
import com.allvideo.hdplayer.Models.VideoModel;
import com.allvideo.hdplayer.R;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyHolder> {

    ArrayList<String> folderArrayList;
    ArrayList<VideoModel> videoModelArrayList;
    Context context;

    public FolderAdapter(ArrayList<String> folderArrayList, ArrayList<VideoModel> videoModelArrayList, Context context) {
        this.folderArrayList = folderArrayList;
        this.videoModelArrayList = videoModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        int index = folderArrayList.get(position).lastIndexOf("/");
        String folder = folderArrayList.get(position).substring(index + 1);
        holder.txt_folder.setText(folder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Utils.videoModelArrayList.size(); i++) {
                    if (Utils.videoModelArrayList.get(i) == null) {
                        Utils.videoModelArrayList.remove(i);
                    }
                }
                Intent intent = new Intent(context, VideoFolderActivity.class);
                intent.putExtra("folderName", folderArrayList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView txt_folder;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            txt_folder = itemView.findViewById(R.id.text_folder_name);
        }
    }
}
