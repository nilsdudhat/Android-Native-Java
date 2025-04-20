-flattenpackagehierarchy

# keep anything annotated with @SerializedName
-keepclassmembers public class * {
    @com.google.gson.annotations.SerializedName *;
}

#keep ffmpeg library to make video status
-keep class com.arthenica.mobileffmpeg.Config {
    native <methods>;
    void log(long, int, byte[]);
    void statistics(long, int, float, float, long , int, double, double);
}
-keep class com.arthenica.mobileffmpeg.AbiDetect {
    native <methods>;
}