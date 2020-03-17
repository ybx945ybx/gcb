package com.gocashback.lib_common.imageload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.gocashback.lib_common.R;
import com.gocashback.lib_common.imageload.GlideCircleTransform;
import com.gocashback.lib_common.imageload.GlideRoundTransform;
import com.gocashback.lib_common.widget.GcbImageView;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Glide图片加载工具类
 * <p>
 * Created by yangboxue on 2018/4/11.
 */

public class ImageLoadManager {

    public final static DiskCacheStrategy sDiskCacheStrategy = DiskCacheStrategy.RESOURCE;
    private static      String            imgUrl             = "";    // 图片路径前缀

    public static void setImgUrl(String url) {
        imgUrl = url;
    }

//    static {
//        ViewTarget.setTagId(R.id.glide_tag);
//    }

    public static void loadImage(Context context, String originUrl, Target<Drawable> drawableTarget) {
        Glide.with(context).asDrawable().load(getImageFullUrl(originUrl)).into(drawableTarget);
    }

    /**
     * 加载一般图片
     */
    public static void loadImage(String originUrl, ImageView targetView) {
        loadImage(originUrl, targetView, null);
    }


    public static void loadImage(String originUrl, ImageView targetView, RequestListener requestListener) {
        loadImage(originUrl, 0, 0, 0, targetView, requestListener, false, 0, false, false);
    }

    /**
     * 加载一般图片(设置占位图)
     */
    public static void loadImage(String originUrl, int holderResId, ImageView targetView) {
        loadImage(originUrl, holderResId, targetView, null);
    }

    public static void loadImage(String originUrl, int holderResId, ImageView targetView, RequestListener requestListener) {
        loadImage(originUrl, holderResId, 0, 0, targetView, requestListener, false, 0, false, false);
    }

    /**
     * 加载一般图片(设置宽高和占位图)
     */
    public static void loadImage(String originUrl, int holderResId, int width, int height, ImageView targetView) {
        loadImage(originUrl, holderResId, width, height, targetView, null);
    }

    public static void loadImage(String originUrl, int holderResId, int width, int height, ImageView targetView, int radius) {
        loadImage(originUrl, holderResId, width, height, targetView, null, false, radius, false, false);
    }

    public static void loadImage(String originUrl, int holderResId, int width, int height, ImageView targetView, RequestListener requestListener) {
        loadImage(originUrl, holderResId, width, height, targetView, requestListener, false, 0, false, false);
    }


    public static void loadImage(String originUrl, ImageView targetView, int radius) {
        loadImage(originUrl, targetView, radius, null);
    }


    public static void loadImage(String originUrl, ImageView targetView, int radius, boolean centerCrop) {
        loadImage(originUrl, targetView, radius, null, centerCrop);
    }

    public static void loadImage(String originUrl, ImageView targetView, int radius, RequestListener requestListener) {
        loadImage(originUrl, 0, 0, 0, targetView, requestListener, false, radius, false, false);
    }

    public static void loadImage(String originUrl, ImageView targetView, int radius, RequestListener requestListener, boolean centerCrop) {
        loadImage(originUrl, 0, 0, 0, targetView, requestListener, false, radius, false, centerCrop);
    }

    /**
     * 加载圆角图片(设置占位图)
     */
    public static void loadImage(String originUrl, int holderResId, ImageView targetView, int radius) {
        loadImage(originUrl, holderResId, targetView, radius, null);
    }

    public static void loadImage(String originUrl, int holderResId, ImageView targetView, int radius, boolean centerCrop) {
        loadImage(originUrl, holderResId, 0, 0, targetView, null, false, radius, false, centerCrop);

    }

    public static void loadImage(String originUrl, int holderResId, ImageView targetView, int radius, RequestListener requestListener) {
        loadImage(originUrl, holderResId, 0, 0, targetView, requestListener, false, radius, false, false);
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(String originUrl, ImageView targetView) {
        loadCircleImage(originUrl, 0, 0, 0, targetView, null);
    }

    /**
     * 加载圆形图片（设置占位图）
     */
    public static void loadCircleImage(String originUrl, int holderResId, ImageView targetView) {
        loadCircleImage(originUrl, holderResId, 0, 0, targetView, null);
    }

    /**
     * 加载圆形图片（设置占位图和宽高）
     */
    public static void loadCircleImage(String originUrl, int holderResId, int width, int height, ImageView targetView) {
        loadCircleImage(originUrl, holderResId, width, height, targetView, null);
    }

    public static void loadCircleImage(String originUrl, int holderResId, int width, int height, ImageView targetView, RequestListener requestListener) {
        loadImage(originUrl, holderResId, width, height, targetView, requestListener, true, 0, false, false);
    }

    /**
     * 加载gif图片
     */
    public static void loadGifImage(String originUrl, int holderResId, int width, int height, ImageView targetView) {
        loadImage(originUrl, holderResId, width, height, targetView, null, false, 0, true, false);
    }

    /**
     * 加载图片
     */
    @SuppressLint("CheckResult")
    public static void loadImage(String originUrl, final int holderResId, int width, int height, final ImageView targetView, RequestListener requestListener, final boolean circle, final int radius, boolean asGif, boolean centerCrop) {

        if (targetView.getContext() instanceof Activity) {
            Activity activity = (Activity) targetView.getContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
            }
        }

        if (targetView.getContext() == null) return;
        // 补全图片路径
//        String url = getImageFullUrl(originUrl);
        String url = originUrl;
        // RequestOptions
        RequestOptions requestOptions = new RequestOptions();
        // 缓存策略
        requestOptions.diskCacheStrategy(sDiskCacheStrategy);

        if (holderResId > 0) {//占位图
            requestOptions.placeholder(holderResId);
        } else {
            requestOptions.placeholder(R.mipmap.ic_default_holder);
        }

        List<Transformation<Bitmap>> transformations = new ArrayList<>();
        if (centerCrop) {
            transformations.add(new CenterCrop());
        }

        // 圆形
        if (circle)
            transformations.add(new GlideCircleTransform(targetView.getContext()));
        // 圆角
        if (radius > 0)
            transformations.add(new GlideRoundTransform(targetView.getContext(), radius));

        if (transformations.size() > 0) {

            Transformation[] tr = new Transformation[transformations.size()];
            for (int i = 0; i < transformations.size(); i++) {
                tr[i] = transformations.get(i);
            }
            requestOptions.transforms(tr);
        }
        // 固定大小
        if (width > 0 && height > 0)
            requestOptions.override(width, height);
        // RequestBuilder
        RequestBuilder requestBuilder;
        // 是否是gif
        if (asGif) {
            requestBuilder = Glide.with(targetView).asGif().load(url);
        } else {
            requestBuilder = Glide.with(targetView).load(url);
        }
        // 监听
        if (requestListener != null) {
            requestBuilder.listener(requestListener);
        } else {
            requestBuilder.listener(new RequestListener() {//关于失败的ErrorView自己设置
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    if (holderResId > 0) {
                        targetView.setImageResource(holderResId);
                    } else {
                        if (circle || radius > 0) {
                            targetView.setImageResource(holderResId);
                        } else {
                            targetView.setBackgroundResource(R.mipmap.ic_default_holder);
                        }
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    targetView.setBackgroundResource(0);
                    return false;
                }
            });
        }
        // 加载图片
        requestBuilder
                .apply(requestOptions)
                .into(targetView);
    }


    public static void loadImage(Context context, String image_url, int res, final onBitmapListener listener) {
//        String url = image_url;
        String         url            = getImageFullUrl(image_url);
        RequestOptions requestOptions = new RequestOptions();
        // 缓存策略
        requestOptions.diskCacheStrategy(sDiskCacheStrategy);
        requestOptions.placeholder(res);
        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap();
        requestBuilder.load(url).apply(requestOptions).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (listener != null) {
                    listener.onResult(resource);
                }
            }
        });
    }

    public interface onBitmapListener {
        void onResult(Bitmap bitmap);
    }

    /**
     * 获取图片的全路径
     *
     * @param imageName
     * @return
     */
    public static String getImageFullUrl(String imageName) {
        if (imageName == null) {
            return null;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("http:")) {
            return imageName;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("https:")) {
            return imageName;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("file:")) {
            return imageName;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("/storage")) {
            return imageName;
        }

        String str = imageName.toLowerCase();
        if (str.endsWith(".gif")) {
            String format = "";
            int    index  = imageName.lastIndexOf(".");
            if (index > 0) {
                format = imageName.substring(index, imageName.length());
                imageName = imageName.substring(0, index);
            }
            return imgUrl + imageName + format;
        }
        return imgUrl + imageName;
    }


    /**
     * 加载网络图 详细
     *
     * @param imgUrl     url
     * @param imageView  图片
     * @param width      宽
     * @param height     高
     * @param loadingImg 占位图
     */
    public static void showOnlineImg(String imgUrl, GcbImageView imageView, int width, int height, @DrawableRes int loadingImg) {
        if (null == imgUrl) imgUrl = "";
        // 阿里云图片，需要拼接图片处理参数
//        if (isAliOssImg(imgUrl)) {
//            imgUrl = resizeAliOssImg(imgUrl, width, height);
//        }


        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imgUrl));
        // 非阿里云图片，宽高控制
//        if (!isAliOssImg(imgUrl) && width > 0 && height > 0) {
//            imageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
//        }
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequestBuilder.build())
                .setAutoPlayAnimations(true)
                .setOldController(imageView.getController())
                .build();
        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
        // 默认图
        if (loadingImg != -1) {
            hierarchy.setFailureImage(loadingImg, ScalingUtils.ScaleType.FIT_CENTER);
            hierarchy.setPlaceholderImage(loadingImg, ScalingUtils.ScaleType.FIT_CENTER);
        }
        imageView.setController(controller);
    }

}
