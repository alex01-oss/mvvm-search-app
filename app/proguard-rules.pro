-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes SourceFile, LineNumberTable

-keepclasseswithmembers class * {
    native <methods>;
}

-keep enum * { *; }

-keep class kotlin.Metadata { *; }

-keepclassmembers class ** {
    @kotlin.jvm.JvmField <fields>;
}

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

-dontwarn dagger.hilt.internal.**

-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }

-keep @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
}

-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-dontnote kotlinx.serialization.SerializationKt

-keep,includedescriptorclasses class com.loc.searchapp.**$$serializer { *; }

-keepclassmembers class com.loc.searchapp.** {
    *** Companion;
}

-keepclasseswithmembers class com.loc.searchapp.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keepclassmembers @kotlinx.serialization.Serializable class com.loc.searchapp.** {
    <fields>;
    <init>(...);
}

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}

-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

-keep class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

-keep,allowobfuscation,allowshrinking class androidx.compose.runtime.State
-keep,allowobfuscation,allowshrinking class androidx.compose.runtime.MutableState
-keep,allowobfuscation,allowshrinking class androidx.compose.runtime.SnapshotMutationPolicy

-keep,allowobfuscation class * extends androidx.compose.runtime.CompositionLocal {
    <init>(...);
}

-keep class androidx.compose.runtime.RecomposeScopeImpl { *; }
-keep class androidx.compose.ui.platform.AndroidCompositionLocals_androidKt { *; }

-keep class androidx.compose.ui.tooling.preview.PreviewParameterProvider { *; }

-keepnames class * extends androidx.navigation.NavArgs

-keepclassmembers class * {
    @androidx.navigation.* <methods>;
}

-keep class coil.ImageLoader { *; }
-keep class * implements coil.fetch.Fetcher { *; }
-keep class * implements coil.decode.Decoder { *; }
-keep class * implements coil.transform.Transformation { *; }

-dontwarn coil.**

-keep class * extends androidx.paging.PagingSource { 
    <init>(...);
}

-keep class * extends androidx.paging.RemoteMediator {
    <init>(...);
}

-keep class androidx.paging.PagingData { *; }
-keep class androidx.paging.LoadState* { *; }

-keep class com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer { *; }
-keep class com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView { *; }
-keep interface com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.** { *; }

-dontwarn com.pierfrancescosoffritti.androidyoutubeplayer.**

-keep class * extends androidx.datastore.core.Serializer {
    <init>(...);
}