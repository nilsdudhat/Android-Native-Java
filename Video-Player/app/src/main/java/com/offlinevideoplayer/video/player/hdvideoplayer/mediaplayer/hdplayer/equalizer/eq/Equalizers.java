package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.eq;

import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.PreferenceUtil;


public class Equalizers {


    private static Equalizer equalizer = null;
    private static short preset;

    public Equalizers() {
    }

    public static void initEq(int audioID) {
        EndEq();
        try {
            equalizer = new Equalizer(0, audioID);
            preset = (short) PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.SAVE_PRESET, 0);
            if (preset < equalizer.getNumberOfPresets()) {
                usePreset(preset);
            } else {
                for (short b = 0; b < equalizer.getNumberOfBands(); b++) {
                    short level = (short) PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.BAND_LEVEL + b, 0);
                    setBandLevel(b, level);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void EndEq() {
        if (equalizer != null) {
            equalizer.release();
            equalizer = null;
        }
    }

    public static short[] getBandLevelRange() {
        if (equalizer != null) {
            return equalizer.getBandLevelRange();
        } else {
            return null;
        }
    }

    public static short getPresetNo() {
        if (equalizer != null) {
            return equalizer.getNumberOfPresets();
        } else {
            return 0;
        }
    }

    public static String getPresetNames(short name) {
        if (equalizer != null) {
            return equalizer.getPresetName(name);
        } else {
            return " ";
        }
    }

    public static short getBandLevel(short band) {
        if (equalizer == null) {
            return 0;
        }
        return equalizer.getBandLevel(band);
    }

    public static void setEnabled(boolean enabled) {
        if (equalizer != null) {
            equalizer.setEnabled(enabled);
        }
    }

    public static void setBandLevel(short band, short level) {
        if (equalizer != null) {
            equalizer.setBandLevel(band, level);
        }
    }

    public static void usePreset(short presets) {
        if (equalizer != null) {
            try {
                if (presets >= 0) {
                    preset = presets;
                    equalizer.usePreset(presets);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static short getNumberOfBands() {
        if (equalizer != null) {
            return equalizer.getNumberOfBands();
        }
        return 0;
    }

    public static int getCenterFreq(short band) {
        if (equalizer != null) {
            return equalizer.getCenterFreq(band);
        }
        return 0;
    }

    public static void savePrefs(int band, int bandLevel) {
        if (equalizer == null) {
            return;
        }
        SharedPreferences.Editor editor = PreferenceUtil.getInstance().saveEq().edit();
        editor.putInt(PreferenceUtil.BAND_LEVEL + band, bandLevel);
        editor.apply();
    }

}
