package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.eq;

import android.content.SharedPreferences;
import android.media.audiofx.LoudnessEnhancer;
import android.util.Log;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.PreferenceUtil;


public class Loud {

    private static LoudnessEnhancer loudnessEnhancer = null;

    public Loud() {
    }

    /*
     Init LoudnessEnhancer
    */
    public static void initLoudnessEnhancer(int audioID) {
        EndLoudnessEnhancer();
        try {
            loudnessEnhancer = new LoudnessEnhancer(audioID);
            int loud = PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.LOUD_BOOST, 0);
            if (loud > 0) {
                setLoudnessEnhancerGain(loud);
            } else {
                setLoudnessEnhancerGain(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setLoudnessEnhancerGain(int gain) {
        if (loudnessEnhancer != null && gain >= 0) {
            try {
                if (gain <= PreferenceUtil.GAIN_MAX) {
                    loudnessEnhancer.setTargetGain(gain);
                    saveLoudnessEnhancer(gain);
                }
            } catch (IllegalArgumentException e) {
                Log.e("Loud", "Loud effect not supported");
            } catch (IllegalStateException e) {
                Log.e("Loud", "Loud cannot get gain supported");
            } catch (UnsupportedOperationException e) {
                Log.e("Loud", "Loud library not loaded");
            } catch (RuntimeException e) {
                Log.e("Loud", "Loud effect not found");
            }
        }

    }

    public static void EndLoudnessEnhancer() {
        if (loudnessEnhancer != null) {
            loudnessEnhancer.release();
            loudnessEnhancer = null;
        }
    }


    public static void saveLoudnessEnhancer(int Gain) {
        SharedPreferences.Editor editor = PreferenceUtil.getInstance().saveEq().edit();
        editor.putInt(PreferenceUtil.LOUD_BOOST, Gain);
        editor.apply();
    }

    public static void setEnabled(boolean enabled) {
        if (loudnessEnhancer != null) {
            loudnessEnhancer.setEnabled(enabled);
        }
    }
}
