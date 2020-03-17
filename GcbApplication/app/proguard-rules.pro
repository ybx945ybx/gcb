# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keepattributes InnerClasses
-keep class **.R$* {
    <fields>;
}


# # -------------------------------------------
# #  ############### Webview  ###############
# # -------------------------------------------
-dontwarn android.webkit.WebView
-keep public class android.webkit.**
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-keepclassmembers class com.gocashback.lib_common.web.WebActivity$JavaScriptinterface {
  public *;
}


# # -------------------------------------------
# #  ############### Arouter  ###############
# # -------------------------------------------
-dontwarn com.alibaba.**
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# If you use the byType method to obtain Service, add the following rules to protect the interface:
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# If single-type injection is used, that is, no interface is defined to implement IProvider, the following rules need to be added to protect the implementation
#-keep class * implements com.alibaba.android.arouter.facade.template.IProvider


# # -------------------------------------------
# #  ############### Eventbus  ###############
# # -------------------------------------------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


# # -------------------------------------------
# #  ############### Umeng  ###############
# # -------------------------------------------
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

#-keep public class com.umeng.socialize.** {*;}
#-dontwarn com.umeng.socialize.**

-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep class com.twitter.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature

-keep class com.umeng.** {*;}



# # -------------------------------------------
# #  ############### Retrofit2  ###############
# # -------------------------------------------
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>


# # -------------------------------------------
# #  ############### okhttp3  ###############
# # -------------------------------------------
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-dontwarn com.squareup.picasso.**


# # -------------------------------------------
# #  ############### akio  ###############
# # -------------------------------------------
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*


# # -------------------------------------------
# #  ############### glide  ###############
# # -------------------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


## ----------------------------------
##   ########## gson  ##########
## ----------------------------------
-keep class com.gocashback.lib_common.network.model.** {*;}
-keep class com.gocashback.lib_common.model.** {*;}
-keep class com.gocashback.lib_common.model.** {*;}



##==================gson==========================
#-dontwarn com.google.**
#-keep class com.google.gson.** {*;}
#-keep class com.google.protobuf.** {*;}
#
#
#-keepattributes SourceFile,LineNumberTable
#-keep class com.parse.*{ *; }
#-dontwarn com.parse.**
#
#-dontwarn org.apache.http.**
#-dontwarn org.apache.**
#
#-keep class com.alibaba.fastjson.** { *; }
#-keepattributes Signature
#
#-keep class com.haitao.data.model.** {*;}
#-keep class com.haitao.net.entity.** {*;}
#-keep class com.haitao.data.db.** {*;}
#-keep class com.haitao.ui.view.**{*;}
#-keep class com.haitao.utils.verifycode.** {*;}
#
#
##-keep class com.alipay.android.app.IAlixPay{*;}
##-keep class com.alipay.android.app.IAlixPay$Stub{*;}
##-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
##-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
##-keep class com.alipay.sdk.app.PayTask{ public *;}
##-keep class com.alipay.sdk.app.AuthTask{ public *;}
##-keep class com.alipay.mobilesecuritysdk.*
#-keep class com.ut.*
#
#-keepattributes Signature
#-dontwarn android.net.SSLCertificateSocketFactory
#-dontwarn android.app.Notification
#-dontwarn com.squareup.**
#-dontwarn okio.**
#
#
#-keep class org.jsoup.**
#
## OkHttp
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class com.squareup.okhttp.** { *; }
#-keep interface com.squareup.okhttp.** { *; }
#-dontwarn com.squareup.okhttp.**
## Okio
#-dontwarn java.nio.file.*
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-dontwarn okio.**
#-dontwarn rx.**
#
#-keepattributes *Annotation*
#-keepattributes *JavascriptInterface*
#
#-keep  class com.haitao.ui.activity.community.TopicDetailActivity{
#    public <methods>;
#}
# -keep class com.haitao.ui.activity.community.TopicDetailActivity$JavascriptInterface {
#    public <methods>;
# }
#-keep class com.haitao.ui.activity.community.TopicDetailActivity$JavascriptInterface {
#    *;
#}
#
#-keep class android.webkit.JavascriptInterface {*;}
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
# }
#
#-keep class com.facebook.** { *; }
#-keep @com.facebook.common.internal.DoNotStrip class *
#-keepclassmembers class * {
#    @com.facebook.common.internal.DoNotStrip *;
#}
#
## Keep native methods
#-keepclassmembers class * {
#    native <methods>;
#}
#
#-dontwarn okio.**
#-dontwarn javax.annotation.**
#-dontwarn com.android.volley.toolbox.**
#
#
#
#
#-keep class io.swagger.client.model.** {*;}
#
### ----------------------------------
###   ########## Gson混淆    ##########
### ----------------------------------
#-keepattributes Signature
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.examples.Android.model.** { *; }
#-keep class com.google.**{*;}
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#


### ----------------------------------
###   ########## RxGalleryFinal    ##########
### ----------------------------------
###1.support-v7-appcompat
##-keep public class android.support.v7.widget.** { *; }
##-keep public class android.support.v7.internal.widget.** { *; }
##-keep public class android.support.v7.internal.view.menu.** { *; }
##
##-keep public class * extends android.support.v4.view.ActionProvider {
##    public <init>(android.content.Context);
##}
##
###2.rx
##-dontwarn io.reactivex.**
##-keep io.reactivex.**
##-keepclassmembers class io.reactivex.** { *; }
#
##3.retrolambda
#-dontwarn java.lang.invoke.*
#
##4.support-v4
#-keep class android.support.v4.** { *; }
#-keep interface android.support.v4.** { *; }
#
#5.ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#6.photoview
-keep class uk.co.senab.photoview** { *; }
-keep interface uk.co.senab.photoview** { *; }

#7.rxgalleryfinal
-keep class cn.finalteam.rxgalleryfinal.ui.widget** { *; }
-keep class cn.finalteam.rxgalleryfinal.imageloader** { *; }
-dontwarn cn.finalteam.rxgalleryfinal**

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepattributes *Annotation*
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# # -------------------------------------------
# #  ############### 阿里百川电商  ###############
# # -------------------------------------------
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class org.json.** {*;}
-keep class com.ali.auth.**  {*;}

# # -------------------------------------------
# #  ############### fresco  ###############
# # -------------------------------------------

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**
