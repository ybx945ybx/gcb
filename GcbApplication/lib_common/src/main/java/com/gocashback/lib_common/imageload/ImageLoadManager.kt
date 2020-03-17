package com.gocashback.lib_common.imageload

import android.net.Uri
import android.support.annotation.DrawableRes
import android.text.TextUtils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.gocashback.lib_common.R
import com.gocashback.lib_common.widget.GcbImageView

//object ImageLoadManager {
private val mDiskCacheStrategy = DiskCacheStrategy.RESOURCE

//    static {
////        ViewTarget.setTagId(R.id.glide_tag)
////    }

//fun loadImage(originUrl: String, targetView: GcbImageView) {
//    ImageLoadManager.showOnlineImg(originUrl, targetView, 0, 0, R.mipmap.ic_default_holder)
//
//
//}

fun loadImage(originUrl: String, holderResId: Int = 0, width: Int = 0, height: Int = 0, targetView: GcbImageView, circle: Boolean = false, radius: Int = 0, asGif: Boolean = false, centerCrop: Boolean = false) {

//    if (null == originUrl) originUrl = ""
    // 阿里云图片，需要拼接图片处理参数
//        if (isAliOssImg(imgUrl)) {
//            imgUrl = resizeAliOssImg(imgUrl, width, height);
//        }


    val imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(if (TextUtils.isEmpty(originUrl)) "" else originUrl))
    // 非阿里云图片，宽高控制
//        if (!isAliOssImg(imgUrl) && width > 0 && height > 0) {
//            imageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
//        }
    val controller = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequestBuilder.build())
            .setAutoPlayAnimations(true)
            .setOldController(targetView.controller)
            .build()
    val hierarchy = targetView.hierarchy
    hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
    // 默认图
    if (holderResId > 0) {
        hierarchy.setFailureImage(holderResId, ScalingUtils.ScaleType.FIT_CENTER)
        hierarchy.setPlaceholderImage(holderResId, ScalingUtils.ScaleType.FIT_CENTER)
    } else {
        hierarchy.setFailureImage(R.mipmap.ic_default_holder, ScalingUtils.ScaleType.FIT_CENTER)
        hierarchy.setPlaceholderImage(R.mipmap.ic_default_holder, ScalingUtils.ScaleType.FIT_CENTER)
    }
    targetView.controller = controller

////    ImageLoadManager.showOnlineImg(originUrl, targetView, 0, 0, holderResId)
//    ImageLoadManager.loadImage(originUrl, holderResId, width, height, targetView, null, circle, radius, asGif, centerCrop)
//    targetView.context?.let {
//        if (it is Activity) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                val activity = it
//                if (activity.isDestroyed || activity.isFinishing) return
//            }
//        }
//
//        RequestOptions().apply {
//            // 缓存策略
//            diskCacheStrategy(mDiskCacheStrategy)
//            // 占位图
////            targetView.setBackgroundColor(Color.parseColor("#F8F8F8"))
//            if (holderResId > 0)
//                placeholder(holderResId)
//            else
//                placeholder(R.mipmap.ic_default_holder)
//            var index = 0
//            // centercrop
//            if (centerCrop) {
//                index++
//            }
//            // 圆形
//            if (circle) {
//                index++
//            }
//            // 圆角
//            if (radius > 0) {
//                index++
//            }
//            val trans: Array<Transformation<Bitmap>?> = arrayOfNulls(index)
//            trans.apply {
//                var index = 0
//                // centercrop
//                if (centerCrop) {
//                    set(index, CenterCrop())
//                    index++
//                }
//                // 圆形
//                if (circle) {
//                    set(index, GlideCircleTransform(it))
//                    index++
//                }
//                // 圆角
//                if (radius > 0) {
//                    set(index, GlideRoundTransform(it, radius))
//                }
//
//            }
//            if (trans.isNotEmpty())
//                transforms(*trans)
//            // 固定大小
//            if (width > 0 && height > 0) override(width, height)
//            //requestBuilder
//        }.let { requestOptions ->
//            //            when {
////                asGif -> Glide.with(targetView).asGif().load(originUrl).listener(requestListener as RequestListener<GifDrawable>?
////                        ?: object : RequestListener<GifDrawable> {
////                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
////                            }
////
////                            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
////                            }
////                        })
//////                requestListener == null ->
////            }
//            Glide.with(targetView).load(originUrl).listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//
////                        if (holderResId > 0) {
////                            targetView.setImageResource(holderResId)
////                        } else {
////                            if (circle || radius > 0) {
////                                targetView.setImageResource(holderResId)
////                            } else {
////                                targetView.setBackgroundResource(R.drawable.livecover)
////                            }
////                        }
//                    return false
//                }
//
//                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
////                        targetView.setBackgroundResource(0)
//                    return false
//                }
//
//            }).apply(requestOptions).into(targetView)
//        }
//    }

}

////////////////////////////////////// 本地图 //////////////////////////////////////////////////
/**
 * 加载mipmap目录的图片
 *
 * @param imgRes 图片id
 * @param img    CustomImageView
 */
fun loadMipmapImg(@DrawableRes imgRes: Int, img: GcbImageView) {
    if (imgRes != 0) {
        val uri = Uri.parse("res://mipmap/$imgRes")
        img.setImageURI(uri)
    }
}