-flattenpackagehierarchy

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.AppViewModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.AppViewModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adsintegration.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adsintegration.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adsintegration.AppOpenAdManager.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.AppOpenAdManager.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.coverflow.CoverFlowLayoutManger.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.coverflow.CoverFlowLayoutManger.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDao.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDao.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDatabase.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDatabase.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumRepository.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumRepository.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumsViewModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumsViewModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaDao.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaDao.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaDatabase.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaDatabase.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaRepository.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaRepository.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaViewModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaViewModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouDao.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouDao.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouDatabase.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouDatabase.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouDBModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouDBModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.helper.WrapperGridLayoutManager.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.helper.WrapperGridLayoutManager.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DeleteMediaManager.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DeleteMediaManager.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.LocationUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.LocationUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.MathUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.MathUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.MediaUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.MediaUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.PreferenceUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.PreferenceUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ViewUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ViewUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.WallpaperUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.WallpaperUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.WebUtils.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.WebUtils.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.AlbumClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.AlbumClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.DayClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.DayClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.EventListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.EventListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.ForYouClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.ForYouClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.ForYouMediaClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.ForYouMediaClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaPagerClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaPagerClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MonthClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MonthClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MultiSelectListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MultiSelectListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.PlaceClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.PlaceClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.SelectionClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.SelectionClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.VideoClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.VideoClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.YearClickListener.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.YearClickListener.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaCursor.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaCursor.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressDetailModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressDetailModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AlbumModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AlbumModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.ForYouModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.ForYouModel.** { *; }

-keep class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.RatioModel.** { *; }
-keepclassmembers class com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.RatioModel.** { *; }