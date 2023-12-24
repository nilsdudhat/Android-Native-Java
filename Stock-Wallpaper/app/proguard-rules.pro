-flattenpackagehierarchy

-keep public class com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.** { *; }
-keep public class com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.api.** { *; }
-keep public class com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.network.** { *; }
-keep public class com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.** { *; }
-keep public class com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.mediastore.** { *; }
-keep public class com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.preferences.** { *; }
-keep public class com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.MyApplication.** { *; }
-keep public class com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.Constant.** { *; }

-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** {*;}