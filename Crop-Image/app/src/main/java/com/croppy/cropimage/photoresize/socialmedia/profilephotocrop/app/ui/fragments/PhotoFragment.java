package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.fragments;

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

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adapters.PhoneAlbumAdapter;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.models.PhoneAlbum;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.models.PhonePhoto;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.activities.GalleryListActivity;

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
    ArrayList<PhoneAlbum> phoneAlbums = new ArrayList<>();
    GalleryListActivity galleryListActivity;
    private GridLayoutManager gridLayoutManager;

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
                    PhonePhoto phonePhoto = new PhonePhoto();
                    phonePhoto.setAlbumName(string);
                    phonePhoto.setPhotoUri(string2);
                    phonePhoto.setId(Integer.parseInt(string3));
                    String str5 = "A photo was added to album => ";
                    String str6 = ".gif";
                    if (albumsNames.contains(string)) {
                        Iterator it = phoneAlbums.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            PhoneAlbum phoneAlbum = (PhoneAlbum) it.next();
                            if (phoneAlbum != null && phoneAlbum.getName() != null && phoneAlbum.getName().equalsIgnoreCase(string)) {
                                if (new File(string2).length() != 0) {
                                    if (!phonePhoto.getPhotoUri().endsWith(str6)) {
                                        phoneAlbum.getAlbumPhotos().add(phonePhoto);
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
                        PhoneAlbum phoneAlbum2 = new PhoneAlbum();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("A new album was created => ");
                        sb4.append(string);
                        Log.i(str4, sb4.toString());
                        phoneAlbum2.setId(phonePhoto.getId());
                        phoneAlbum2.setName(string);
                        phoneAlbum2.setCoverUri(phonePhoto.getPhotoUri());
                        if (!phonePhoto.getPhotoUri().endsWith(str6)) {
                            phoneAlbum2.getAlbumPhotos().add(phonePhoto);
                            phoneAlbums.add(phoneAlbum2);
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
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        albumAdapter = new PhoneAlbumAdapter(galleryListActivity, phoneAlbums);

//        rcv_album.addItemDecoration(new GridSpacingItemDecoration(2, 30, true));

        rcv_album.setLayoutManager(gridLayoutManager);

        rcv_album.setAdapter(albumAdapter);
    }

    public class LoadImageTask extends AsyncTask<Void, Void, Boolean> {
        public LoadImageTask() {
        }

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

                if (phoneAlbums != null) {
                    for (int i = 0; i < phoneAlbums.size(); i++) {
                        phoneAlbums.get(i).setCoverUri(phoneAlbums.get(i).getAlbumPhotos().get(phoneAlbums.get(i).getAlbumPhotos().size() - 1).getPhotoUri());
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
