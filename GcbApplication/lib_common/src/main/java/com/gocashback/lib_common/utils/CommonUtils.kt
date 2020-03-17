package com.gocashback.lib_common.utils

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.gocashback.lib_common.model.JumpIfModel
import com.gocashback.lib_common.model.TransferExtraIfModel
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.TransferApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.isLogin
import com.gocashback.lib_common.network.model.transfer.JumpTransferIfModel
import com.gocashback.lib_common.startInviteActivity
import com.gocashback.lib_common.startLoginActivity
import com.gocashback.lib_common.startWebviewActivity
import com.gocashback.lib_common.startWelfareActivity
import com.gocashback.lib_common.widget.GotoBuyLoginDlg
import java.io.File

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 15:20
 */


/**
 * 实现文本复制功能
 *
 * @param mContext
 * @param content
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
fun copyToClipboard(mContext: Context, content: String, toast: String = "") {
    // 得到剪贴板管理器
    val cmb = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cmb.text = content.trim()
    if (!TextUtils.isEmpty(toast)) show(mContext, toast)
}


/**
 * 清除WebView cookie缓存
 *
 * @param context context
 */
fun clearCookies(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    } else {
        val cookieSyncMngr = CookieSyncManager.createInstance(context)
        cookieSyncMngr.startSync()
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
        cookieManager.removeSessionCookie()
        cookieSyncMngr.stopSync()
        cookieSyncMngr.sync()
    }
}

/**
 *
 * 去购买
 *
 * @param activity GcbBaseActivity
 */
fun goToBuy(activity: Activity?, gotobuy_url: String, couponCode: String = "") {
    //"/go/store/2428",  ”go/AVRL31?isoutsidelink = 1”
//    * type	是	string	商家“store”，折扣“deal” ，“code”， aliexpress “alie”
//    * id	是	int	商家id、折扣id、code
//    * gotag	否	string	gotag
//    * store_id	否	int	type为alie时传 值为802
//    with(jumpIfModel) {
    var type = ""
    var id = ""
    var gotag = ""
    var storeId = ""

    val list: List<String> = gotobuy_url.split("/")
    if (list.size == 2) {   // code类型
        type = "code"
        id = list[1].split("?")[0]
    } else if (list.size == 4) {  // {"" "go" "" ""}
        type = list[2]
        id = list[3].split("?")[0]

    }

//通过URI的getQueryParameter()获取参数值
    val uri = Uri.parse(gotobuy_url)
    if (gotobuy_url.contains("gotag"))
        gotag = uri.getQueryParameter("gotag")//uid 值 111

    createService(TransferApi::class.java)
            .jumpTransfer(type, id, gotag, storeId)
            .compose(ResponseTransformer.handleResult())
            .subscribe(object : BaseObserver<JumpTransferIfModel>(activity) {
                override fun onSuccess(t: JumpTransferIfModel) {
                    if (isLogin()) {
                        startWebviewActivity(activity, t.jump_url, TransferExtraIfModel().apply {
                            store_logo = t.store_info?.store_logo ?: ""
                            rebate = t.store_info?.rebate ?: ""
                            rebate_explain = t.store_info?.rebate_explain ?: ""
                            coupon_code = couponCode
                        })
                    } else {
                        GotoBuyLoginDlg(activity!!, t.store_info!!, object : GotoBuyLoginDlg.OnClickListener {
                            override fun onLoginClick(dlg: Dialog) {
                                startLoginActivity(activity, TransferExtraIfModel().apply {
                                    store_logo = t.store_info?.store_logo ?: ""
                                    rebate = t.store_info?.rebate ?: ""
                                    rebate_explain = t.store_info?.rebate_explain ?: ""
                                    coupon_code = couponCode
                                    jump_url = t.jump_url
                                })
                                dlg.dismiss()
                            }

                            override fun onUnLoginClick(dlg: Dialog) {
                                startWebviewActivity(activity, t.jump_url)
                                dlg.dismiss()
                            }
                        }).show()
                    }

                }
            })
//    }

}

/**
 * 根据类型跳转
 */
fun jump(activity: Activity?, jumpIfModel: JumpIfModel) {
    with(jumpIfModel) {
        when (click_type) {
            "store" -> {
            }
            "web" -> {
                if (gotobuy_url.startsWith("go")) {
                    goToBuy(activity, gotobuy_url)
                } else {
                    if (gotobuy_url.contains("isoutsidelink=1")) {   // 外链的情况 https://www.gocashback.net/zh-hans/go/JHbBK?isoutsidelink=1&gotag=phb_sephoraca
                        //"/go/store/2428",  ”go/AVRL31?isoutsidelink = 1”
                        var list = gotobuy_url.split("/go")
                        when {
                            gotobuy_url.contains("/go/store/") -> goToBuy(activity, "/go" + list[1])
                            gotobuy_url.contains("/go/store/") -> goToBuy(activity, "/go" + list[1])
                            else -> goToBuy(activity, "go" + list[1])
                        }
                    } else if ("https://api.gocashback.com/misc/discover_v3?l=en&v=v3.1.1&type=1" == gotobuy_url || "https://api.gocashback.com/misc/discover_v3?l=cn@&v=v3.1.1@&type=1" == gotobuy_url) {
                        startWelfareActivity(activity)
                    } else {
                        startWebviewActivity(activity, url)
                    }
                }
            }
            "inviteFriend" -> {
                startInviteActivity(activity)
            }
            "aroundRests" -> {
            }

        }
    }
}


/**
 * 只删除相关的文件，不做其它操作
 *
 * @param path
 */
fun deleteDir(path: String) {
    val dir = File(path)
    if (dir == null || !dir.exists() || !dir.isDirectory)
        return

    for (file in dir.listFiles()) {
        if (file.isFile)
            file.delete() // 删除所有文件
        else if (file.isDirectory)
            deleteDir(file.path) // 递规的方式删除文件夹
    }
}

fun isEmail(strEmail: String): Boolean {
    val strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$"
    return if (TextUtils.isEmpty(strPattern)) {
        false
    } else {
        strEmail.matches(strPattern.toRegex())
    }
}

fun getFuzzyString(str: String): String {
    var result = str
    if (isEmail(str)) {
        val strings: List<String> = str.split("@")
        if (strings[0].length > 3) {
            result = strings[0].substring(0, 3) + "***" + "@" + strings[1]
        } else {
            result = strings[0].substring(0, 2) + "***" + "@" + strings[1]

        }

    } else {
        if (str.length > 7) {
            result = str.substring(0, 3) + "***" + str.substring(str.length - 4, str.length)
        } else {
            result = str.substring(0, str.length / 2 - 1) + "***" + str.substring(str.length / 2 + 1, str.length)

        }

    }
    return result
}


