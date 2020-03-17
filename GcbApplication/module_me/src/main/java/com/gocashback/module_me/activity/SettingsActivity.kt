package com.gocashback.module_me.activity

import android.support.v4.content.ContextCompat
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.*
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.event.UpdateLocaleEvent
import com.gocashback.lib_common.language.*
import com.gocashback.lib_common.network.clearUser
import com.gocashback.lib_common.network.isLogin
import com.gocashback.lib_common.utils.*
import com.gocashback.lib_common.utils.FileUtils.CATCH_PATH
import com.gocashback.lib_common.utils.FileUtils.getAutoFileOrFilesSize
import com.gocashback.module_me.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_settings.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 10:40
 */
@Route(path = ACTIVITY_SETTING)
class SettingsActivity : GcbBaseActivity() {
    override fun setLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun initVars() {
        registerEventBus()
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        tv_logout.visibility = if (isLogin()) View.VISIBLE else View.GONE
    }

    override fun initEvent() {
        // 支付方式
        gseitv_payment_methods.setOnClickListener { startPaymentMethodActivity(this) }
        // 切换语言
        tv_en.setOnClickListener {
            changeLanguage()
        }
        tv_cn.setOnClickListener {
            changeLanguage()
        }
        // 更改密码
        gseitv_change_password.setOnClickListener { startUpdatePwdActivity(this) }
        // 更改支付密码
        gseitv_payment_password.setOnClickListener { startPaymentPasswordActivity(this) }
        // 关于我们
        gseitv_about_us.setOnClickListener { startAboutUsActivity(this) }
        // 清除缓存
        gseitv_clear_cache.setOnClickListener { clearAppCache() }
        // 退出登录
        tv_logout.setOnClickListener {
            clearUser()
            EventBus.getDefault().post(LoginChangeEvent(false))
//            show(this, R.string.setting_logout_success)
//            finish()
        }

    }

    private fun changeLanguage() {
        if (getLanguage(this) == LOCALE_CHINESE) {
            tv_en.setBackgroundResource(R.drawable.bg_redffe6e6_right_corner_4)
            tv_en.setTextColor(ContextCompat.getColor(this, R.color.redFFEEEE))

            tv_cn.setBackgroundResource(R.drawable.bg_redffeeee_left_corner_4)
            tv_cn.setTextColor(ContextCompat.getColor(this, R.color.redFF6E6E))

            updateLocale(this, Locale(LOCALE_ENGLISH))
        } else {
            tv_en.setBackgroundResource(R.drawable.bg_redffeeee_right_corner_4)
            tv_en.setTextColor(ContextCompat.getColor(this, R.color.redFF6E6E))

            tv_cn.setBackgroundResource(R.drawable.bg_redffe6e6_left_corner_4)
            tv_cn.setTextColor(ContextCompat.getColor(this, R.color.redFFEEEE))

            updateLocale(this, Locale(LOCALE_CHINESE))
        }

    }

    override fun initData() {
        // 语言
        val locale: Locale = getLocale(this)
        if (locale.language == LOCALE_CHINESE) {
            tv_en.setBackgroundResource(R.drawable.bg_redffeeee_right_corner_4)
            tv_en.setTextColor(ContextCompat.getColor(this, R.color.redFF6E6E))

            tv_cn.setBackgroundResource(R.drawable.bg_redffe6e6_left_corner_4)
            tv_cn.setTextColor(ContextCompat.getColor(this, R.color.redFFEEEE))

        } else {
            tv_en.setBackgroundResource(R.drawable.bg_redffe6e6_right_corner_4)
            tv_en.setTextColor(ContextCompat.getColor(this, R.color.redFFEEEE))

            tv_cn.setBackgroundResource(R.drawable.bg_redffeeee_left_corner_4)
            tv_cn.setTextColor(ContextCompat.getColor(this, R.color.redFF6E6E))

        }


        //缓存大小
        val catchStr = getAutoFileOrFilesSize(CATCH_PATH)
        gseitv_clear_cache.setText(resources.getString(R.string.settings_clear_cache) + "(" + catchStr + ")")

    }


    /**
     * 清除缓存
     */
    private fun clearAppCache() {
        AndPermission.with(this)
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted {
                    //                     deleteDir(FileUtils.CATCH_PATH)//删除文件夹下的所有文件，不包括文件夹
                    gseitv_clear_cache.setText(resources.getString(R.string.settings_clear_cache))
//                    Fresco.getImagePipeline().clearDiskCaches() // Fresco缓存
                    clearCookies(this) // 清除Cookies
//                    dialog.dismiss()
                    show(this, resources.getString(R.string.settings_clear_cache_success))

                }
                .onDenied {
                    show(this, "请授予读写权限，以清除应用缓存")

                }
                .start()
        /*mClearCacheDlg = new ConfirmDlg(mContext, "", getResources().getString(R.string.clear_cache_confirm),
                dialog -> {
                    FileUtils.deleteDir(FileUtils.CATCH_PATH);//删除文件夹下的所有文件，不包括文件夹
                    mTvClearCache.setSubText("0B");
                    Fresco.getImagePipeline().clearDiskCaches(); // Fresco缓存
                    CommonUtils.clearCookies(mContext); // 清除Cookies
                    dialog.dismiss();
                },
                Dialog::dismiss);*/
//        mClearCacheDlg = ConfirmDlg.Builder(mContext)
//                .setMessage(R.string.clear_cache_confirm)
//                .setConfirmListener(null, { dialog ->
//                    FileUtils.deleteDir(FileUtils.CATCH_PATH)//删除文件夹下的所有文件，不包括文件夹
//                    mTvClearCache.setSubText("0B")
//                    Fresco.getImagePipeline().clearDiskCaches() // Fresco缓存
//                    CommonUtils.clearCookies(mContext) // 清除Cookies
//                    dialog.dismiss()
//                })
//                .create()
//        mClearCacheDlg.show()
    }


    @Subscribe
    fun updateLocale(updateLocaleEvent: UpdateLocaleEvent) {
        recreate()
    }

    @Subscribe
    fun onLoginChange(loginChangeEvent: LoginChangeEvent) {
        tv_logout.visibility = if (isLogin()) View.VISIBLE else View.GONE
//
//        if (isLogin()) {
//            tv_logout.visibility = View.VISIBLE
//        } else {
//            tv_logout.visibility = View.GONE
//            finish()
//        }
    }
}