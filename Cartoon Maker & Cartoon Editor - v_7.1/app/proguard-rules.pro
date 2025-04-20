-flattenpackagehierarchy
-ignorewarnings

# keep anything annotated with @SerializedName
-keepclassmembers public class * {
    @com.google.gson.annotations.SerializedName *;
}

##--- Begin:GSON ----
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe.** { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Retrofit
-dontwarn retrofit2.**
-dontwarn org.codehaus.mojo.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

#keep packages
-keep class com.cartoon2021.photo.editor.CartoonEditor.Activity.PhotoLabActivity.** { *; }
-keep class com.cartoon2021.photo.editor.CartoonEditor.addnew.** { *; }
-keep class com.cartoon2021.photo.editor.AdUtils.** { *; }