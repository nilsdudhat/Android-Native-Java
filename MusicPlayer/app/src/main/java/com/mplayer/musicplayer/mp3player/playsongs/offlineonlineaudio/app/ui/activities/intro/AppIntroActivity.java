package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.intro;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;

public class AppIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonCtaVisible(true);
        setButtonNextVisible(false);
        setButtonBackVisible(false);

        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.app_name)
                .description(R.string.welcome_to_mplayer)
                .image(R.drawable.logo)
                .background(com.kabouzeid.appthemehelper.R.color.md_blue_grey_100)
                .backgroundDark(com.kabouzeid.appthemehelper.R.color.md_blue_grey_200)
                .layout(R.layout.fragment_simple_slide_large_image)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.label_playing_queue)
                .description(R.string.open_playing_queue_instruction)
                .image(R.drawable.tutorial_queue_swipe_up)
                .background(com.kabouzeid.appthemehelper.R.color.md_deep_purple_500)
                .backgroundDark(com.kabouzeid.appthemehelper.R.color.md_deep_purple_600)
                .layout(R.layout.fragment_simple_slide_large_image)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.label_playing_queue)
                .description(R.string.rearrange_playing_queue_instruction)
                .image(R.drawable.tutorial_rearrange_queue)
                .background(com.kabouzeid.appthemehelper.R.color.md_indigo_500)
                .backgroundDark(com.kabouzeid.appthemehelper.R.color.md_indigo_600)
                .layout(R.layout.fragment_simple_slide_large_image)
                .build());
    }
}
