package com.gocashback.lib_common.share

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.constraint.Group
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.gocashback.lib_common.R
import com.gocashback.lib_common.utils.copyToClipboard
import com.gocashback.lib_common.utils.show
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 10:14
 */

object ShareUtils {

    private lateinit var shareDlg: Dialog

    fun showShareBoard(activity: Activity, title: String, text: String, url: String, imageUrl: String, onlyImg: Boolean = false, extra: Boolean = true) {   //, shareExtraInfo: ShareExtraInfo

        shareDlg = Dialog(activity).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(LayoutInflater.from(activity).inflate(R.layout.dlg_share_board, null).also { view ->
                // 分享监听
                object : View.OnClickListener {
                    override fun onClick(v: View) {
                        var platform: Int = Share_WX
                        when (v.id) {
                            R.id.iv_share_close -> {
                                dismissShareBoard()
                                return
                            }
                            R.id.tv_copy -> {
                                copyToClipboard(activity, url, activity.resources.getString(R.string.copy_success))
                                dismissShareBoard()
                                return
                            }
                            R.id.tv_wx -> {
                                platform = Share_WX
                            }
                            R.id.tv_weibo -> {
                                platform = Share_Sina
                            }
                            R.id.tv_wx_circle -> {
                                platform = Share_WX_CIRCLE
                            }
                            R.id.tv_qq -> {
                                platform = Share_QQ
                            }
                            R.id.tv_tw -> {
                                platform = Share_Twitter
                            }
                            R.id.tv_fb -> {
                                platform = Share_Facebook
                            }
                            R.id.tv_google -> {
                                platform = Share_Google
                            }
                        }

                        //无网
//                        if (!AndroidUtils.isNetWorkAvalible(activity)) {
//                            show(activity, activity.getString(R.string.error_label_popup_wlyc))
//                            return
//                        }

                        if ((platform == Share_WX || platform == Share_WX_CIRCLE) && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN)) {
                            show(activity, R.string.share_need_wx)
                            return
                        }

                        if (platform == Share_QQ && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.QQ)) {
                            show(activity, R.string.share_need_qq)
                            return
                        }

                        if (platform == Share_Sina && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.SINA)) {
                            show(activity, R.string.share_need_weibo)
                            return
                        }

                        if (platform == Share_Facebook && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.FACEBOOK)) {
                            show(activity, R.string.share_need_fb)
                            return
                        }

                        if (platform == Share_Twitter && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.TWITTER)) {
                            show(activity, R.string.share_need_tw)
                            return
                        }

                        if (platform == Share_Google && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.GOOGLEPLUS)) {
                            show(activity, R.string.share_need_google)
                            return
                        }

                        doShare(platform, activity, title, text, url, imageUrl, onlyImg)

                    }
                }.let {
                    // 设置监听
                    // 基本分享
                    view.findViewById<ImageView>(R.id.iv_share_close).setOnClickListener(it)
                    view.findViewById<TextView>(R.id.tv_wx).setOnClickListener(it)
                    view.findViewById<TextView>(R.id.tv_weibo).setOnClickListener(it)
                    view.findViewById<TextView>(R.id.tv_wx_circle).setOnClickListener(it)
                    view.findViewById<TextView>(R.id.tv_qq).setOnClickListener(it)
                    view.findViewById<TextView>(R.id.tv_fb).setOnClickListener(it)
                    view.findViewById<TextView>(R.id.tv_tw).setOnClickListener(it)
                    view.findViewById<TextView>(R.id.tv_google).setOnClickListener(it)
                    view.findViewById<TextView>(R.id.tv_copy).setOnClickListener(it)

                    view.findViewById<Group>(R.id.group_extra).visibility = if (extra) View.VISIBLE else View.GONE

                }
            })
            window.apply {
                setGravity(Gravity.LEFT or Gravity.BOTTOM)
                attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            }

        }

        shareDlg.show()
    }

    /**
     * 隐藏分享面板
     */
    private fun dismissShareBoard() {
        if (shareDlg != null && shareDlg.isShowing) {
            shareDlg.dismiss()
        }
    }

    /**
     * @param platform 平台
     * @param activity Activity
     * @param title    标题
     * @param text     文本
     * @param url      small_icon
     * @param image imageUrl
     * @param onlyImg 图片分享
     */
    fun doShare(platform: Int, activity: Activity, title: String, text: String, url: String, image: String, onlyImg: Boolean = false, dismiss: Boolean = true) {
        val plat = platform.let {
            when (it) {
                Share_WX -> SHARE_MEDIA.WEIXIN
                Share_WX_CIRCLE -> SHARE_MEDIA.WEIXIN_CIRCLE
                Share_QQ -> SHARE_MEDIA.QQ
                Share_Sina -> SHARE_MEDIA.SINA
                Share_Twitter -> SHARE_MEDIA.TWITTER
                Share_Facebook -> SHARE_MEDIA.FACEBOOK
                Share_Google -> SHARE_MEDIA.GOOGLEPLUS
                else -> SHARE_MEDIA.WEIXIN
            }
        }
        ShareAction(activity).apply {
            setPlatform(plat)
            setCallback(object : UMShareListener {
                override fun onResult(p0: SHARE_MEDIA?) {
//                    show(activity, "分享成功")
                }

                override fun onCancel(p0: SHARE_MEDIA?) {
//                    show(activity, "分享取消")

                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
//                    show(activity, "分享失败$p1")

                }

                override fun onStart(p0: SHARE_MEDIA?) {
                    if (dismiss)
                        dismissShareBoard()
                }
            })

            when {
                onlyImg -> withMedia(UMImage(activity, image))
                plat == SHARE_MEDIA.SINA -> {
                    var shareText = ""
                    if (!TextUtils.isEmpty(title)) {
                        shareText += title
                    }
                    if (!TextUtils.isEmpty(text)) {
                        shareText += text
                    }
                    if (!TextUtils.isEmpty(url)) {
                        shareText += url
                    }

                    withMedia(UMImage(activity, image))
                    withText(shareText)

                }
                else -> withMedia(
                        UMWeb(url).apply {
                            setTitle(title)
                            setThumb(UMImage(activity, image))
                            description = text
                        })
            }
        }.share()

    }

    /**QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中，添加：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现**/
    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data)
    }

}