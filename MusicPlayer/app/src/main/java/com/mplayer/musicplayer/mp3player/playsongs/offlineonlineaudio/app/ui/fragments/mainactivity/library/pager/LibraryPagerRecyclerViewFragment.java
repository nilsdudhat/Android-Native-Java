package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.library.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.ViewUtil;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

public abstract class LibraryPagerRecyclerViewFragment<A extends RecyclerView.Adapter, LM extends RecyclerView.LayoutManager> extends LibraryPagerFragment implements OnOffsetChangedListener {

    View container;
    RecyclerView recyclerView;
    TextView empty;

    private A adapter;
    private LM layoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);

        this.container = view.findViewById(R.id.container);
        recyclerView = view.findViewById(R.id.recycler_view);
        empty = view.findViewById(android.R.id.empty);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLibraryFragment().addOnAppBarOffsetChangedListener(this);

        initLayoutManager();
        initAdapter();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        if (recyclerView instanceof FastScrollRecyclerView) {
            ViewUtil.setUpFastScrollRecyclerViewColor(getActivity(), ((FastScrollRecyclerView) recyclerView), ThemeStore.accentColor(getActivity()));
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    protected void invalidateLayoutManager() {
        initLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
    }

    protected void invalidateAdapter() {
        initAdapter();
        checkIsEmpty();
        recyclerView.setAdapter(adapter);
    }

    private void initAdapter() {
        adapter = createAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIsEmpty();
            }
        });
    }

    private void initLayoutManager() {
        layoutManager = createLayoutManager();
    }

    protected A getAdapter() {
        return adapter;
    }

    protected LM getLayoutManager() {
        return layoutManager;
    }

    protected RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        container.setPadding(container.getPaddingLeft(), container.getPaddingTop(), container.getPaddingRight(), getLibraryFragment().getTotalAppBarScrollingRange() + i);
    }

    private void checkIsEmpty() {
        if (empty != null) {
            empty.setText(getEmptyMessage());
            empty.setVisibility(adapter == null || adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    @StringRes
    protected int getEmptyMessage() {
        return R.string.empty;
    }

    @LayoutRes
    protected int getLayoutRes() {
        return R.layout.fragment_main_activity_recycler_view;
    }

    protected abstract LM createLayoutManager();

    @NonNull
    protected abstract A createAdapter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLibraryFragment().removeOnAppBarOffsetChangedListener(this);
    }
}
