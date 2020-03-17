package com.gocashback.lib_common

import android.content.Context
import android.support.v4.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.gocashback.lib_common.annotation.SuccessType
import com.gocashback.lib_common.annotation.VerifyMailCodeType
import com.gocashback.lib_common.model.TransferExtraIfModel
import com.gocashback.lib_common.network.model.user.HeliPaymentIfModel
import com.gocashback.lib_common.network.model.user.PaymentIfModel
import java.text.FieldPosition

fun inject(o: Any) {
    ARouter.getInstance().inject(o)
}


/********************************************************************     activity     ***************************************************************/

/**
 * 首次安装引导activity
 * @param context Context
 */
fun startGuideActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_GUIDE)
            .navigation(context)
}

/**
 * 主activity
 * @param context Context
 */
fun startMainActivity(context: Context?, position: Int = 0) {
    ARouter.getInstance()
            .build(ACTIVITY_MAIN)
            .withInt("position", position)
            .navigation(context)
}

/**
 * 搜索activity
 * @param context Context
 */
fun startSearchActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_SEARCH)
            .navigation(context)
}

/**
 * 热门优惠activity
 * @param context Context
 */
fun startHotDealActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_HOT_DEAL)
            .navigation(context)
}

/**
 * 商家详情activity
 * @param context Context
 */
fun startStoreDetailActivity(context: Context?, flag: String) {
    ARouter.getInstance()
            .build(ACTIVITY_STORE_DETAIL)
            .withString("flag", flag)
            .navigation(context)
}

/**
 * 优惠详情activity
 * @param context Context
 */
fun startDealDetailActivity(context: Context?, id: String) {
    ARouter.getInstance()
            .build(ACTIVITY_DEAL_DETAIL)
            .withString("id", id)
            .navigation(context)
}

/**
 * 去购买中转页activity
 * @param context Context
 */
fun startGoBuyTransferActivity(context: Context?, flag: String) {
    ARouter.getInstance()
            .build(ACTIVITY_TRANSFER)
            .withString("flag", flag)
            .navigation(context)
}

/**
 * 登录 activity
 * @param context Context
 */
fun startLoginActivity(context: Context?, transferExtraIfModel: TransferExtraIfModel? = null) {
    ARouter.getInstance().build(ACTIVITY_LOGIN).apply {
        transferExtraIfModel?.let {
            withSerializable("transferExtraIfModel", transferExtraIfModel)
        }
        navigation(context)
    }

}

/**
 * 注册 activity
 * @param context Context
 */
fun startRegisterActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_REGISTER)
            .navigation(context)
}

/**
 * 忘记密码 activity
 * @param context Context
 */
fun startForgetPwdActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_FORGETPWD)
            .navigation(context)
}

/**
 * 更改密码 activity
 * @param context Context
 */
fun startUpdatePwdActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_UPDATEPWD)
            .navigation(context)
}


/**
 * 我的返利 activity
 * @param context Context
 */
fun startMyRebateActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_MYREBATE)
            .navigation(context)
}

/**
 * 返利详情 activity
 * @param context Context
 */
fun startRebateDetailActivity(context: Context?, id: String) {
    ARouter.getInstance()
            .build(ACTIVITY_MYREBATE_DETAIL)
            .withString("id", id)
            .navigation(context)
}

/**
 * 我的订单 activity
 * @param context Context
 */
fun startMyOrderActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_MYORDER)
            .navigation(context)
}

/**
 * 订单详情 activity
 * @param context Context
 */
fun startOrderDetailActivity(context: Context?, id: String) {
    ARouter.getInstance()
            .build(ACTIVITY_MYORDER_DETAIL)
            .withString("id", id)
            .navigation(context)
}

/**
 * 找回订单 activity
 * @param context Context
 */
fun startFindOrderActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_FINDORDER)
            .navigation(context)
}

/**
 * 丢单详情 activity
 * @param context Context
 */
fun startLostOrderDetailActivity(context: Context?, id: String) {
    ARouter.getInstance()
            .build(ACTIVITY_FINDORDER_DETAIL)
            .withString("id", id)
            .navigation(context)
}

/**
 * 提交找回订单 activity
 * @param context Context
 */
fun startFindOrderSubmitActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_FINDORDER_SUBMIT)
            .navigation(context)
}

/**
 * 提交找回订单选择商家 activity
 * @param context Context
 */
fun startFindOrderSubmitSelectStoreActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_FINDORDER_SUBMIT_SELECT_STORE)
            .navigation(context)
}

/**
 * 申请提现 activity
 * @param context Context
 */
fun startWithdrawActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_WITHDRAW)
            .navigation(context)
}

/**
 * 提现记录 activity
 * @param context Context
 */
fun startWithdrawRecordActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_WITHDRAWRECORD)
            .navigation(context)
}

/**
 * 提现记录详情 activity
 * @param context Context
 */
fun startWithdrawDetailActivity(context: Context?, id: String) {
    ARouter.getInstance()
            .build(ACTIVITY_WITHDRAWRECORD_DETAIL)
            .withString("id", id)
            .navigation(context)
}

/**
 * 我的礼品卡 activity
 * @param context Context
 */
fun startGiftCardActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_GIFTCARD)
            .navigation(context)
}

/**
 * 兑换礼品卡 activity
 * @param context Context
 */
fun startRedeemGiftCardActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_REDEEM_GIFTCARD)
            .navigation(context)
}

/**
 * 礼品卡详情 activity
 * @param context Context
 */
fun startGiftCardDetailActivity(context: Context?, id: String) {
    ARouter.getInstance()
            .build(ACTIVITY_GIFTCARD_DETAIL)
            .withString("id", id)
            .navigation(context)
}

/**
 * 我的收藏 activity
 * @param context Context
 */
fun startMyCollectActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_COLLECT)
            .navigation(context)
}

/**
 * 浏览记录 activity
 * @param context Context
 */
fun startRecordActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_RECORD)
            .navigation(context)
}

/**
 * 邀请赚钱 activity
 * @param context Context
 */
fun startInviteActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_INVITE)
            .navigation(context)
}

/**
 * 消息中心 activity
 * @param context Context
 */
fun startMessageActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_MESSAGE)
            .navigation(context)
}

/**
 * 消息详情 activity
 * @param context Context
 */
fun startMessageDetailActivity(context: Context?, id: String) {
    ARouter.getInstance()
            .build(ACTIVITY_MESSAGE_DETAIL)
            .withString("id", id)
            .navigation(context)
}

/**
 * 帮助中心 activity
 * @param context Context
 */
fun startHelpActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_HELP)
            .navigation(context)
}

/**
 * 帮助中心z子问题 activity
 * @param context Context
 */
fun startHelpChildActivity(context: Context?, id: String) {
    ARouter.getInstance()
            .build(ACTIVITY_HELP_CHILD)
            .withString("id", id)
            .navigation(context)
}

/**
 * 设置 activity
 * @param context Context
 */
fun startSettingActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_SETTING)
            .navigation(context)
}

/**
 * 关于我们 activity
 * @param context Context
 */
fun startAboutUsActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_ABOUT_US)
            .navigation(context)
}

/**
 * 支付方式 activity
 * @param context Context
 */
fun startPaymentMethodActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_PAYMENT_METHOD)
            .navigation(context)
}

/**
 * 编辑ali支付方式 activity
 * @param context Context
 */
fun startPaymentMethodEditAliActivity(context: Context?, paymentIfModel: PaymentIfModel? = null) {
    ARouter.getInstance().build(ACTIVITY_PAYMENT_METHOD_EDIT_ALI).apply {
        paymentIfModel?.let {
            withSerializable("paymentIfModel", paymentIfModel)
        }
        navigation(context)
    }

}

/**
 * 编辑合利宝支付方式 activity
 * @param context Context
 */
fun startPaymentMethodEditHeliActivity(context: Context?, heliPaymentIfModel: HeliPaymentIfModel? = null) {
    ARouter.getInstance().build(ACTIVITY_PAYMENT_METHOD_EDIT_HELI).apply {
        heliPaymentIfModel?.let {
            withSerializable("heliPaymentIfModel", heliPaymentIfModel)
        }
        navigation(context)
    }

}

/**
 * 编辑支票支付方式 activity
 * @param context Context
 */
fun startPaymentMethodEditCheckActivity(context: Context?, paymentIfModel: PaymentIfModel? = null) {
    ARouter.getInstance().build(ACTIVITY_PAYMENT_METHOD_EDIT_CHECK).apply {
        paymentIfModel?.let {
            withSerializable("paymentIfModel", paymentIfModel)
        }
        navigation(context)
    }
}

/**
 *
 * 编辑PayPal支付方式 activity
 * @param context Context
 */
fun startPaymentMethodEditPaypalActivity(context: Context?, paymentIfModel: PaymentIfModel? = null) {
    ARouter.getInstance().build(ACTIVITY_PAYMENT_METHOD_EDIT_PAYPAL).apply {
        paymentIfModel?.let {
            withSerializable("paymentIfModel", paymentIfModel)
        }
        navigation(context)
    }
}

/**
 *
 * 编辑PayPal支付方式选择地区 activity
 * @param context Context
 */
fun startPaypalSelectAreaActivity(context: Context?) {
    ARouter.getInstance().build(ACTIVITY_PAYMENT_METHOD_EDIT_PAYPAL_SELECT_AREA).navigation(context)
}

/**
 *
 * 验证邮箱 activity
 * @param context Context
 */
fun startVerifyMailCodeActivity(context: Context?, @VerifyMailCodeType verifyMailCodeType: Int) {
    ARouter.getInstance()
            .build(ACTIVITY_VERIFY_MAIL_CODE)
            .withInt("verifyMailCodeType", verifyMailCodeType)
            .navigation(context)
}

/**
 *
 * 支付密码 activity
 * @param context Context
 */
fun startPaymentPasswordActivity(context: Context?, fromForget: Boolean = false) {
    ARouter.getInstance()
            .build(ACTIVITY_PAYMENT_PASSWORD)
            .withBoolean("fromForget", fromForget)
            .navigation(context)
}

/**
 * webview activity
 * @param context Context
 */
fun startWebviewActivity(context: Context?, url: String, transferExtraIfModel: TransferExtraIfModel? = null) {
    ARouter.getInstance().build(ACTIVITY_WEB).apply {
        withString("url", url)
        transferExtraIfModel?.let {
            withSerializable("transferExtraIfModel", transferExtraIfModel)
        }
        navigation(context)
    }

}

/**
 * 独家福利 activity
 * @param context Context
 */
fun startWelfareActivity(context: Context?) {
    ARouter.getInstance()
            .build(ACTIVITY_WELFARE)
            .navigation(context)
}

/**
 * 提交成功 activity
 * @param context Context
 */
fun startSuccessActivity(context: Context?, @SuccessType successType: Int) {
    ARouter.getInstance()
            .build(ACTIVITY_SUCCESS)
            .withInt("successType", successType)
            .navigation(context)
}

/********************************************************************     fragment     ***************************************************************/

/**
 * 创建首页的主fragment
 */
fun makeHomeFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_HOME)
            .navigation() as Fragment
}

/**
 * 创建首页的商家fragment
 */
fun makeHomeStoreFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_HOME_STORE)
            .navigation() as Fragment
}

/**
 * 创建首页的优惠fragment
 */
fun makeHomeDiscountFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_HOME_DISCOUNT)
            .navigation() as Fragment
}

/**
 * 创建首页优惠分类tab下fragment
 */
fun makeHomeDiscountCateDealFragment(cateId: String): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_HOME_DISCOUNT_CATE_DEAL)
            .withString("id", cateId)
            .navigation() as Fragment
}

/**
 * 创建商家主fragment
 */
fun makeStoresFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_STORES)
            .navigation() as Fragment
}

/**
 * 创建搜索商家fragment
 */
fun makeSearchStoreFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_SEARCH_STORE)
            .navigation() as Fragment
}

/**
 * 创建搜索优惠fragment
 */
fun makeSearchDealFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_SEARCH_DEAL)
            .navigation() as Fragment
}


/**
 * 创建发现的主fragment
 */
fun makeDiscoverFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_DISCOVER)
            .navigation() as Fragment
}

/**
 * 创建我的主fragment
 */
fun makeMeFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_ME)
            .navigation() as Fragment
}

/**
 * 创建我的订单fragment
 */
fun makeMyOrderFragment(type: Int): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_MY_ORDER)
            .withInt("type", type)
            .navigation() as Fragment
}

/**
 * 创建找回订单fragment
 */
fun makeFindOrderFragment(type: Int): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_FIND_ORDER)
            .withInt("type", type)
            .navigation() as Fragment
}

/**
 * 系统消息fragment
 */
fun makeMessageSystemFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_MESSAGE_SYSTEM)
            .navigation() as Fragment
}

/**
 * 优惠消息fragment
 */
fun makeMessageDealFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_MESSAGE_DEAL)
            .navigation() as Fragment
}

/**
 * 我的收藏商家fragment
 */
fun makeMyCollectionStoresFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_MY_COLLECTION_STORES)
            .navigation() as Fragment
}

/**
 * 我的收藏优惠fragment
 */
fun makeMyCollectionDealsFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_MY_COLLECTION_DEALS)
            .navigation() as Fragment
}

/**
 * 浏览记录商家fragment
 */
fun makeClickHistoryStoresFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_CLICK_HISTORY_STORES)
            .navigation() as Fragment
}

/**
 * 浏览记录优惠fragment
 */
fun makeClickHistoryDealsFragment(): Fragment {
    return ARouter.getInstance()
            .build(FRAGMENT_CLICK_HISTORY_DEALS)
            .navigation() as Fragment
}
