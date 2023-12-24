package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> arrayList = new ArrayList<>();

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }

    public void addFragment(Fragment fragment) {
        arrayList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return arrayList.get(position);
    }
}