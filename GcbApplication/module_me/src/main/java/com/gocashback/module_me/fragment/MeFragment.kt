package com.gocashback.module_me.fragment

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.*
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.network.*
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.model.user.UserIfModel
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.module_me.R
import com.gocashback.module_me.event.WithdrawSuccessEvent
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019/4/8 2:34 PM
 */
@Route(path = FRAGMENT_ME)
class MeFragment : GcbBaseFragment() {

    override fun setLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun initVars() {
        registerEventBus()

    }

    override fun initView() {
        if (isLogin()) {
            tv_go_login.visibility = View.GONE
            tv_user_name.visibility = View.VISIBLE
            group_my_rebate.visibility = View.VISIBLE
        } else {
            iv_head.setImageResource(R.mipmap.ic_head_default)
            tv_go_login.visibility = View.VISIBLE
            tv_user_name.visibility = View.GONE
            group_my_rebate.visibility = View.GONE
        }
        iv_invite.setImageResource(if (getLanguage(mActivity) == LOCALE_CHINESE) R.mipmap.ic_invite_bg else R.mipmap.ic_invite_bg_en)
    }

    override fun initEvent() {
        // 去登录
        tv_go_login.setOnClickListener { startLoginActivity(mActivity) }
        iv_head.setOnClickListener { if (!isLogin()) startLoginActivity(mActivity) }
        // 去登录部分我的返利  group_my_rebate
        tv_cash_count.setOnClickListener { startMyRebateActivity(mActivity) }
        my_cash.setOnClickListener { startMyRebateActivity(mActivity) }
        // 邀请赚钱
        iv_invite.setOnClickListener { startInviteActivity(mActivity) }
        // 我的返利
        tvRebate.setOnClickListener { startMyRebateActivity(mActivity) }
        // 我的订单
        tvOrder.setOnClickListener { startMyOrderActivity(mActivity) }
        // 找回订单
        tvFindOrder.setOnClickListener { startFindOrderActivity(mActivity) }
        // 申请提现
        tvWithdraw.setOnClickListener { startWithdrawActivity(mActivity) }
        // 提现记录
        tvWithdrawRecord.setOnClickListener { startWithdrawRecordActivity(mActivity) }
        // 我的礼品卡
        tvGiftCard.setOnClickListener { startGiftCardActivity(mActivity) }
        // 我的收藏
        gmfitv_collect.setOnClickListener { startMyCollectActivity(mActivity) }
        // 浏览记录
        gmfitv_record.setOnClickListener { startRecordActivity(mActivity) }
        // 邀请赚钱
        gmfitv_invite.setOnClickListener { startInviteActivity(mActivity) }
        // 消息中心
        gmfitv_message_center.setOnClickListener { startMessageActivity(mActivity) }
        // 帮助中心
        gmfitv_help.setOnClickListener { startHelpActivity(mActivity) }
        // 设置
        gmfitv_setting.setOnClickListener { startSettingActivity(mActivity) }

    }

    override fun initData() {
        if (isLogin())
            getUserInfo()

    }

    /**
     * 获取用户信息
     */
    private fun getUserInfo() {
        createService(UserApi::class.java)
                .user()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<UserIfModel>(mActivity) {
                    override fun onSuccess(t: UserIfModel) {
                        // 保存用户信息
                        getUser()?.apply {
                            available = t.available
                            amount = t.amount
                            avatar = t.avatar
                            is_pay_pwd = t.is_pay_pwd
                            is_payment = t.is_payment
                        }?.let {
                            setUser(it)
                        }
                        // 页面渲染
                        renderView(t)

                    }
                })
    }

    /**
     * 渲染用户信息
     * @param t UserIfModel
     */
    private fun renderView(t: UserIfModel) {
        t.run {
            // 头像
            // 用户名
            tv_user_name.text = user_name
            // 我的返利
            tv_cash_count.text = moneyFormat(amount)
        }

    }

    @Subscribe
    fun onLoginChange(loginChangeEvent: LoginChangeEvent) {
        initView()
        if (loginChangeEvent.isLogin) {
            getUserInfo()
        }
    }

    @Subscribe
    fun onLoginChange(withdrawSuccessEvent: WithdrawSuccessEvent) {
        if (isLogin()) {
            getUserInfo()
        }
    }


}