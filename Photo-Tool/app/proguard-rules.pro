-flattenpackagehierarchy


-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.Share.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.AppUtils.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.BitmapUtils.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.DeleteMediaManager.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.views.SquareLayout.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.PhoneAlbumModel.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.PhonePhotoModel.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.RatioModel.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.ImageModel.** { *; }
-keep public class com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.media.MediaLoader.** { *; }


# proguard for 'com.makeramen:roundedimageview:2.3.0'
-dontwarn com.squareup.okhttp.**


# References to Picasso are okay if the consuming app doesn't use it
-dontwarn com.squareup.picasso.Transformation


# for glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}