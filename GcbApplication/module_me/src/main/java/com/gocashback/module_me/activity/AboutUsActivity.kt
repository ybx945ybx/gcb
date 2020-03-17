package com.gocashback.module_me.activity

import android.annotation.SuppressLint
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_ABOUT_US
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_about_us.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 15:47
 */
@Route(path = ACTIVITY_ABOUT_US)
class AboutUsActivity : GcbBaseActivity() {
    override fun setLayoutId(): Int {
        return R.layout.activity_about_us
    }

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        tv_Content.text = if (getLanguage(this) == LOCALE_CHINESE) "GoCashBack.com是目前最受华人信赖和欢迎的返利网站。\n" +
                "\n" +
                "拥有千余美国大牌商家的返利优惠。\n" +
                "\n" +
                "通过GoCashBack前往商家网站购物，用户最高可得消费金额的31%作为返利，并且可以叠加商家折扣及信用卡返现同时使用，让用户加倍获利。\n" +
                "\n" +
                "返利神器GoCashBack App的推出让用户更加方便的追踪返利详情和热门折扣，吃喝玩乐都能享受返利优惠。\n" +
                "\n" +
                "GoCashBack致力于让用户以最低花费过最好的生活。" +
                "\n" else "GoCashBack has grown to be one of the most trusted and fastest growing rebate websites since its founding in 2014.\n" +
                "\n" +
                "Our members around the world have received millions in cash back earnings from North America's beloved brands and merchants.\n" +
                "\n" +
                " Find exclusive and deep discounts from your favorite brands by checking our real-time “Deals” section.\n" +
                "\n" +
                "Don’t miss our Super Cash Back featured merchant of the day to earn a maximum rebate!\n" +
                "\n" +
                "Shop GoCashBack for higher rebate rates in Skincare, Cosmetics, Apparel, Shoes, Accessories, Home & Garden, Baby & Kids, Electronics, Travel, Leisure & many more!" +
                "\n"

    }
}