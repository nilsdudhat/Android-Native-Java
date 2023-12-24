package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class NativeAdsAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final AdsBaseActivity adsBaseActivity;
    @NativeSize
    private final int size;
    private static final int LIST_AD_DELTA = 7;
    public static final int AD = 1;

    public NativeAdsAdapter(AdsBaseActivity adsBaseActivity, @NativeSize int size) {
        this.adsBaseActivity = adsBaseActivity;
        this.size = size;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == AD) {
            return new AdRecyclerHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ad_admob_native, viewGroup, false));
        }
        return createView(viewGroup, viewType);
    }

    static class AdRecyclerHolder extends RecyclerView.ViewHolder {
        public RelativeLayout native_ad_container;

        AdRecyclerHolder(View view) {
            super(view);
            this.native_ad_container = view.findViewById(R.id.ad_native);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder baseHolder, int position) {
        if (getItemViewType(position) == AD) {
            AdRecyclerHolder holder = (AdRecyclerHolder) baseHolder;
            if (size == NativeSize.BIG) {
                adsBaseActivity.showNativeAd(adsBaseActivity, holder.native_ad_container);
            } else {
                adsBaseActivity.showSmallNativeAd(adsBaseActivity, holder.native_ad_container);
            }
        } else {
            Log.d("TAG", "onBindViewHolder: " + position + " " + getRealPosition(position) + " " + getItemCount() + " " + itemCount());
            bindView(baseHolder, getRealPosition(position));
        }
    }

    public abstract void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int position);

    public abstract RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType);

    private int getRealPosition(int position) {
        if (LIST_AD_DELTA <= 0) {
            return position;
        } else {
            int additionalContent = 0;
            for (int i = 0; i <= position; i++) {
                if (getItemViewType(i) == AD) {
                    additionalContent++;
                }
            }
            return position - additionalContent;
        }
    }

    public abstract T itemAt(int position);

    public abstract int viewType(int position);

    public abstract int itemCount();

    @Override
    public int getItemCount() {
        int viewItems = itemCount();
        int additionalContent = 0;
        for (int i = 0; i < viewItems; i++) {
            if (getItemViewType(i) == AD) {
                additionalContent++;
            }
        }
        return viewItems + additionalContent;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % LIST_AD_DELTA == 0) {
            return AD;
        }
        return viewType(position);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NativeSize {
        int BIG = 0;
        int SMALL = 1;
    }
}
