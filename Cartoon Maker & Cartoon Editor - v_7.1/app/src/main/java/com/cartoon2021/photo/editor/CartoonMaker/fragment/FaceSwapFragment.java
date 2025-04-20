package com.cartoon2021.photo.editor.CartoonMaker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import androidx.fragment.app.Fragment;

import com.cartoon2021.photo.editor.CartoonMaker.activities.GenderActivity;
import com.cartoon2021.photo.editor.CartoonMaker.adapter.ImojiPartAdapter;
import com.cartoon2021.photo.editor.CartoonMaker.adapter.ImojiPartFemaleAdapter;
import com.cartoon2021.photo.editor.CartoonMaker.helper.AppConfig;
import com.cartoon2021.photo.editor.CartoonMaker.interfaces.Communicator;
import com.cartoon2021.photo.editor.R;

import java.util.ArrayList;

public class FaceSwapFragment extends Fragment {
    public static ArrayList<ImojiPartFemaleAdapter> arrayFemaleImojiAdapter;
    public static ArrayList<ImojiPartAdapter> arrayMaleImojiAdapter;
    Communicator comm;
    int gpos;
    GridView gridView_face;
    ImojiPartAdapter imojiPartAdapter;
    ImojiPartFemaleAdapter imojiPartFemaleAdapter;
    View view;

    public FaceSwapFragment() {
        arrayMaleImojiAdapter = new ArrayList<>();
        arrayFemaleImojiAdapter = new ArrayList<>();
    }

    public void setFaceSwap(int i) {
        this.gpos = i;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.face, viewGroup, false);
        this.view = inflate;
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.comm = (Communicator) getActivity();
        this.gridView_face = (GridView) this.view.findViewById(R.id.grid_face);
        if (GenderActivity.isFemale) {
            ImojiPartFemaleAdapter imojiPartFemaleAdapter2 = new ImojiPartFemaleAdapter(getActivity(), AppConfig.TFIDS[this.gpos]);
            this.imojiPartFemaleAdapter = imojiPartFemaleAdapter2;
            arrayFemaleImojiAdapter.add(imojiPartFemaleAdapter2);
            this.gridView_face.setAdapter((ListAdapter) this.imojiPartFemaleAdapter);
        } else if (GenderActivity.isMale) {
            ImojiPartAdapter imojiPartAdapter2 = new ImojiPartAdapter(getActivity(), AppConfig.TRIDS[this.gpos]);
            this.imojiPartAdapter = imojiPartAdapter2;
            arrayMaleImojiAdapter.add(imojiPartAdapter2);
            this.gridView_face.setAdapter((ListAdapter) this.imojiPartAdapter);
        }
        this.gridView_face.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                FaceSwapFragment.this.comm.sendData(i);
            }
        });
    }
}
