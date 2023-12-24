package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.eq;

import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.util.Log;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.PreferenceUtil;


public class BassBoosts {

    private static BassBoost bassBoost = null;

    public BassBoosts() {
    }

    public static void initBass(int audioID) {
        EndBass();
        try {
            bassBoost = new BassBoost(0, audioID);
            short savestr = (short) PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.BASS_BOOST, 0);
            if (savestr > 0) {
                setBassBoostStrength(savestr);
            } else {
                setBassBoostStrength((short) 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void EndBass() {
        if (bassBoost != null) {
            bassBoost.release();
            bassBoost = null;
        }
    }

    public static void setBassBoostStrength(short strength) {
        if (bassBoost != null && bassBoost.getStrengthSupported() && strength >= 0) {
            try {
                if (strength <= PreferenceUtil.BASSBOOST_STRENGTH) {
                    bassBoost.setStrength(strength);
                    saveBass(strength);
                }
            } catch (IllegalArgumentException e) {
                Log.e("BassBoosts", "Bassboost effect not supported");
            } catch (IllegalStateException e) {
                Log.e("BassBoosts", "Bassboost cannot get strength supported");
            } catch (UnsupportedOperationException e) {
                Log.e("BassBoosts", "Bassboost library not loaded");
            } catch (RuntimeException e) {
                Log.e("BassBoosts", "Bassboost effect not found");
            }
        }
    }


    public static void saveBass(short strength) {
        SharedPreferences.Editor editor = PreferenceUtil.getInstance().saveEq().edit();
        int str = (int) strength;
        editor.putInt(PreferenceUtil.BASS_BOOST, str);
        editor.apply();
        Log.e("strength ", " : " + strength);

        short savestr = (short) PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.BASS_BOOST, 0);

        Log.e("savestr ", " : " + savestr);
    }

    public static void setEnabled(boolean enabled) {
        if (bassBoost != null) {
            bassBoost.setEnabled(enabled);
        }
    }
}
