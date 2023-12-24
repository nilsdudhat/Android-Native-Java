package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.eq;

import android.content.SharedPreferences;
import android.media.audiofx.Virtualizer;
import android.util.Log;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.PreferenceUtil;


public class Virtualizers {

    private static Virtualizer virtualizer = null;

    public Virtualizers() {
    }

    /*
     Init Virtualizer
    */
    public static void initVirtualizer(int audioID) {
        EndVirtual();
        try {
            virtualizer = new Virtualizer(0, audioID);
            short str = (short) PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.VIRTUAL_BOOST, 0);
            if (str > 0) {
                setVirtualizerStrength(str);
            } else {
                setVirtualizerStrength((short) 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVirtualizerStrength(short strength) {
        if (virtualizer != null && virtualizer.getStrengthSupported() && strength >= 0) {
            try {
                if (strength <= PreferenceUtil.Virtualizer_STRENGTH) {
                    virtualizer.setStrength(strength);
                    saveVirtual(strength);
                }
            } catch (IllegalArgumentException e) {
                Log.e("Virtualizers", "Virtualizers effect not supported");
            } catch (IllegalStateException e) {
                Log.e("Virtualizers", "Virtualizers cannot get strength supported");
            } catch (UnsupportedOperationException e) {
                Log.e("Virtualizers", "Virtualizers library not loaded");
            } catch (RuntimeException e) {
                Log.e("Virtualizers", "Virtualizers effect not found");
            }
        }

    }

    public static void EndVirtual() {
        if (virtualizer != null) {
            virtualizer.release();
            virtualizer = null;
        }
    }

    public static void saveVirtual(Short virtualstr) {
        SharedPreferences.Editor editor = PreferenceUtil.getInstance().saveEq().edit();
        int str = (int) virtualstr;
        editor.putInt(PreferenceUtil.VIRTUAL_BOOST, str);
        editor.apply();
    }

    public static void setEnabled(boolean enabled) {
        if (virtualizer != null) {
            virtualizer.setEnabled(enabled);
        }
    }
}
