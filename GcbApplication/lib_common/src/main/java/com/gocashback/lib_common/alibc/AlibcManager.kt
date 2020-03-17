package com.gocashback.lib_common.alibc

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import com.alibaba.baichuan.android.trade.AlibcTrade
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback
import com.alibaba.baichuan.android.trade.model.AlibcShowParams
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams
import com.alibaba.baichuan.android.trade.model.OpenType
import com.alibaba.baichuan.android.trade.model.TradeResult
import com.alibaba.baichuan.android.trade.page.AlibcBasePage
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage
import com.gocashback.lib_common.base.GcbBaseApplication
import com.gocashback.lib_common.utils.SP_KEY_TAOBAO_PID
import com.gocashback.lib_common.utils.getString

/**
 * @Author 55HAITAO
 * @Date 2019-07-10 14:18
 */

fun showAliItemDetail(context: Activity, goId: String) {
    //提供给三方传递配置参数
    //        Map<String, String> exParams = new HashMap<>();
    //        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
    val detailPage: AlibcBasePage
    //商品详情page
//    if (TextUtils.isEmpty(taokeUrl)) {
    detailPage = AlibcDetailPage(goId)
//    } else {
//        detailPage = AlibcPage(taokeUrl)    //  淘宝优惠券
//    }
    //实例化店铺打开page
    //                        AlibcBasePage shopPage = new AlibcShopPage(shopId);
    //实例化URL打开page
    //                        AlibcBasePage page = new AlibcPage(taokeUrl);
    //设置页面打开方式
    val showParams = AlibcShowParams(if (isAppInstalled(context, "com.taobao.taobao")) OpenType.Native else OpenType.Auto, true)
    // 淘客信息
//    val taokeParams = AlibcTaokeParams(AppConfig.ALIMAMA_PID, "", "")
    val taokeParams = AlibcTaokeParams(getString(GcbBaseApplication.application, SP_KEY_TAOBAO_PID, "")
            , "", "")
    //使用百川sdk提供默认的Activity打开detail
    AlibcTrade.show(context, detailPage, showParams, taokeParams, null,
            object : AlibcTradeCallback {
                override fun onTradeSuccess(tradeResult: TradeResult) {
                    //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                }

                override fun onFailure(code: Int, msg: String) {
                    //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                }
            })


}


private fun isAppInstalled(context: Context, uri: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}