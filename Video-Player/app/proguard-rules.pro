-flattenpackagehierarchy

-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.models.FolderModel.** { *; }
-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.models.IntentModel.** { *; }
-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.models.** { *; }

-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.MyApplication.** { *; }
-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration.AdUtils.** { *; }
-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration.AppOpenAdManager.** { *; }
-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.** { *; }
-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.media.MediaLoader.** { *; }
-keep public class com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils.** { *; }

-keep class * implements java.io.Serializable { *; }

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep class com.esotericsoftware.** { *; }
-dontwarn com.esotericsoftware.**
-keep class de.javakaffee.kryoserializers.** { *; }
-dontwarn de.javakaffee.kryoserializers.**

