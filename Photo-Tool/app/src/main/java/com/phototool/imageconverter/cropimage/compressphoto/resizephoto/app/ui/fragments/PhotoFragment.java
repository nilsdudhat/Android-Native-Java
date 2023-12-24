package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters.PhoneAlbumAdapter;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.PhoneAlbumModel;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.PhonePhotoModel;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.GalleryListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class PhotoFragment extends Fragment {

    public PhoneAlbumAdapter albumAdapter;
    public LinearLayout lin_no_photo;
    public ProgressBar progressBar;
    public RecyclerView rcv_album;
    Vector<String> albumsNames = new Vector<>();
    ArrayList<PhoneAlbumModel> phoneAlbumModels = new ArrayList<>();
    GalleryListActivity galleryListActivity;

    public PhotoFragment(GalleryListActivity galleryListActivity) {
        this.galleryListActivity = galleryListActivity;
    }

    public static PhotoFragment newInstance(GalleryListActivity galleryListActivity) {
        Bundle bundle = new Bundle();
        PhotoFragment photoFragment = new PhotoFragment(galleryListActivity);
        photoFragment.setArguments(bundle);
        return photoFragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_photo, viewGroup, false);

        initView(view);
        new LoadImageTask().execute();
        setRecyclerView();

        return view;
    }

    private void initView(View view) {
        rcv_album = view.findViewById(R.id.rcv_album);
        lin_no_photo = (LinearLayout) view.findViewById(R.id.lin_no_photo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    public boolean initViewAction() {
        String str = "_id";
        String str2 = "_data";
        String str3 = "bucket_display_name";
        String str4 = "DeviceImageManager";
        try {
            String[] strArr = {str3, str2, str};
            Cursor query = requireActivity().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, strArr, null, null, null);
            if (query == null || query.getCount() <= 0) {
                return false;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(" query count=");
            sb.append(query.getCount());
            Log.i(str4, sb.toString());
            lin_no_photo.setVisibility(View.GONE);
            rcv_album.setVisibility(View.VISIBLE);
            if (query.moveToFirst()) {
                int columnIndex = query.getColumnIndex(str3);
                int columnIndex2 = query.getColumnIndex(str2);
                int columnIndex3 = query.getColumnIndex(str);
                while (true) {
                    String string = query.getString(columnIndex);
                    String string2 = query.getString(columnIndex2);
                    String string3 = query.getString(columnIndex3);
                    PhonePhotoModel phonePhotoModel = new PhonePhotoModel();
                    phonePhotoModel.setAlbumName(string);
                    phonePhotoModel.setPhotoUri(string2);
                    phonePhotoModel.setId(Integer.parseInt(string3));
                    String str5 = "A photo was added to album => ";
                    String str6 = ".gif";
                    if (albumsNames.contains(string)) {
                        Iterator it = phoneAlbumModels.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            PhoneAlbumModel phoneAlbumModel = (PhoneAlbumModel) it.next();
                            if (phoneAlbumModel != null && phoneAlbumModel.getName() != null && phoneAlbumModel.getName().equalsIgnoreCase(string)) {
                                if (new File(string2).length() != 0) {
                                    if (!phonePhotoModel.getPhotoUri().endsWith(str6)) {
                                        phoneAlbumModel.getAlbumPhotos().add(phonePhotoModel);
                                    }
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(str5);
                                    sb2.append(string);
                                    Log.i(str4, sb2.toString());
                                } else {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("data --> ");
                                    sb3.append(string2);
                                    sb3.append(" size --> ");
                                    sb3.append(new File(string2).length());
                                    Log.e("initViewAction: ", sb3.toString());
                                }
                            }
                        }
                    } else if (new File(string2).length() != 0) {
                        PhoneAlbumModel phoneAlbumModel2 = new PhoneAlbumModel();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("A new album was created => ");
                        sb4.append(string);
                        Log.i(str4, sb4.toString());
                        phoneAlbumModel2.setId(phonePhotoModel.getId());
                        phoneAlbumModel2.setName(string);
                        phoneAlbumModel2.setCoverUri(phonePhotoModel.getPhotoUri());
                        if (!phonePhotoModel.getPhotoUri().endsWith(str6)) {
                            phoneAlbumModel2.getAlbumPhotos().add(phonePhotoModel);
                            phoneAlbumModels.add(phoneAlbumModel2);
                            albumsNames.add(string);
                        }
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str5);
                        sb5.append(string);
                        Log.i(str4, sb5.toString());
                    }
                    if (!query.moveToNext()) {
                        break;
                    }
                }
            }
            query.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        albumAdapter = new PhoneAlbumAdapter(galleryListActivity, phoneAlbumModels);

//        rcv_album.addItemDecoration(new GridSpacingItemDecoration(2, 30, true));

        rcv_album.setLayoutManager(gridLayoutManager);

        rcv_album.setAdapter(albumAdapter);
    }

    public class LoadImageTask extends AsyncTask<Void, Void, Boolean> {

        public void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        public Boolean doInBackground(Void... voidArr) {
            return initViewAction();
        }

        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            progressBar.setVisibility(View.GONE);
            if (bool) {

                if (phoneAlbumModels != null) {
                    for (int i = 0; i < phoneAlbumModels.size(); i++) {
                        phoneAlbumModels.get(i).setCoverUri(phoneAlbumModels.get(i).getAlbumPhotos().get(phoneAlbumModels.get(i).getAlbumPhotos().size() - 1).getPhotoUri());
                    }
                }

                albumAdapter.notifyDataSetChanged();
                return;
            }
            rcv_album.setVisibility(View.GONE);
            lin_no_photo.setVisibility(View.VISIBLE);
        }
    }
}
