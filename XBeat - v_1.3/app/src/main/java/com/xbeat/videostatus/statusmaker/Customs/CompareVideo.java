package com.xbeat.videostatus.statusmaker.Customs;

import java.io.File;
import java.util.Comparator;

public class CompareVideo implements Comparator {
    public static final CompareVideo compareVideo = new CompareVideo();

    private CompareVideo() {
    }

    public final int compare(Object obj, Object obj2) {
        return Long.compare(((File) obj2).lastModified(), ((File) obj).lastModified());
    }
}
