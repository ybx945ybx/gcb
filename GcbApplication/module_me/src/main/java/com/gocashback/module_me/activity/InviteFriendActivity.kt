package com.gocashback.module_me.activity

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_INVITE
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.InviteIfModel
import com.gocashback.lib_common.share.ShareUtils
import com.gocashback.lib_common.share.ShareUtils.doShare
import com.gocashback.lib_common.share.ShareUtils.showShareBoard
import com.gocashback.lib_common.share.Share_QQ
import com.gocashback.lib_common.share.Share_Sina
import com.gocashback.lib_common.share.Share_WX
import com.gocashback.lib_common.utils.*
import com.gocashback.lib_common.utils.FileUtils.saveFileToSD
import com.gocashback.lib_common.widget.GcbConfirmDlg
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.InviteMyReferralsAdapter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_invite_friend.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-24 17:35
 */
@Route(path = ACTIVITY_INVITE, extras = LOGIN_EXTRA)
class InviteFriendActivity : GcbBaseActivity() {

    private lateinit var inviteMyReferralsAdapter: InviteMyReferralsAdapter
    private var qrBitmap: Bitmap? = null

    private lateinit var inviteReward: String
    private lateinit var shareTitle: String
    private lateinit var shareText: String
    private lateinit var shareUrl: String
    private lateinit var shareImg: String

    private lateinit var gcbConfirmDlg: GcbConfirmDlg

    override fun setLayoutId(): Int {
        return R.layout.activity_invite_friend
    }

    override fun initVars() {
        inviteReward = getString(this, SP_KEY_REGISTER_REWARD, "") ?: ""

        shareTitle = String.format(resources.getString(R.string.invite_share_title), inviteReward)
        shareText = "www.gocashback.com"
        shareUrl = "https://www.gocashback.com"
        shareImg = "https://d1t4h9gelh7map.cloudfront.net/data/upload/channel/201904/5cb1afd2cf1d9.png"

    }

    override fun isImmersionBarEnabled(): Boolean {
        return false
    }

    override fun initViews() {
//        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        iv_top.setImageResource(if (getLanguage(this) == LOCALE_CHINESE) R.mipmap.ic_invite_top_cn else R.mipmap.ic_invite_top)
        tv_invite_bonus.text = String.format(getString(R.string.invite_bonus), inviteReward)
        rycv_my_referrals.apply {
            layoutManager = LinearLayoutManager(this@InviteFriendActivity)
            inviteMyReferralsAdapter = InviteMyReferralsAdapter(listOf())
            adapter = inviteMyReferralsAdapter
            isNestedScrollingEnabled = false

        }
    }

    override fun initEvent() {
        // 个人链接
        tv_copy.setOnClickListener {
            copyToClipboard(this@InviteFriendActivity, tv_unique_link.text.toString())
            show(this@InviteFriendActivity, resources.getString(R.string.copy_success))
        }
        // 发送邮箱
        tv_send.setOnClickListener {
            if (TextUtils.isEmpty(et_email.text)) {

            } else {
                // 邮件
                val email = et_email.text.toString()
                createService(UserApi::class.java)
                        .inviteEmail(email)
                        .compose(ResponseTransformer.handleResult())
                        .compose(bindToLifecycle())
                        .subscribe(object : BaseObserver<Any>(this) {
                            override fun onSuccess(t: Any) {
                                show(this@InviteFriendActivity, resources.getString(R.string.send_success))

                            }
                        })
//                try {
//                    if (!TextUtils.isEmpty(email)) {
//                        val intent = Intent(Intent.ACTION_SENDTO)
//                        intent.data = Uri.parse("mailto:$email")
//                        startActivity(intent)
//                    }
//                } catch (e: Exception) {
//                    copyToClipboard(this@InviteFriendActivity, email)
//                    show(this@InviteFriendActivity, "邮箱地址已复制到剪切板")
//                }
            }
        }
        // 长按二维码
        iv_qr.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                gcbConfirmDlg = GcbConfirmDlg.Builder(this@InviteFriendActivity)
                        .setMessage(R.string.invite_referral_qr_code_save)
                        .setConfirmListener("", object : GcbConfirmDlg.OnConfirmListener {
                            override fun onConfirm(dlg: GcbConfirmDlg) {
                                AndPermission.with(this@InviteFriendActivity)
                                        .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
                                        .onGranted {
                                            val fileName = System.currentTimeMillis().toString() + ".png"
                                            saveFileToSD(this@InviteFriendActivity, fileName, qrBitmap)
                                        }
                                        .onDenied {

                                        }
                                        .start()
                            }
                        })
                        .setCancelListener("", object : GcbConfirmDlg.OnCancelListener {
                            override fun onCancel(dlg: GcbConfirmDlg) {
                                gcbConfirmDlg.dismiss()
                            }
                        })
                        .create()
                gcbConfirmDlg.show()
                return false
            }
        })
        // 微信
        iv_share_wx.setOnClickListener {
            doShare(Share_WX, this, shareTitle, shareText, shareUrl, shareImg, dismiss = false)
        }
        // 微博
        iv_share_weibo.setOnClickListener {
            doShare(Share_Sina, this, shareTitle, shareText, shareUrl, shareImg, dismiss = false)
        }
        // QQ
        iv_share_qq.setOnClickListener {
            doShare(Share_QQ, this, shareTitle, shareText, shareUrl, shareImg, dismiss = false)
        }
        // 更多
        iv_share_more.setOnClickListener {
            showShareBoard(this, shareTitle, shareText, shareUrl, shareImg)
        }
    }

    override fun initData() {
        createService(UserApi::class.java)
                .invite()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<InviteIfModel>(this) {
                    override fun onSuccess(t: InviteIfModel) {
                        with(t) {
                            shareImg = invite_share_img
                            shareUrl = invite_url

                            tv_total_invite.text = total
                            tv_total_reward.text = cumulative_award
                            tv_unique_link.text = invite_url
                            qrBitmap = QRCodeUtils.createQRCodeBitmap(invite_url, 144, 144)
                            iv_qr.setImageBitmap(qrBitmap)
                            if (data.isNullOrEmpty()) {
                                group_my_referrals.visibility = View.GONE
                            } else {
                                group_my_referrals.visibility = View.VISIBLE
                                inviteMyReferralsAdapter.setNewData(data)

                            }
                        }

                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ShareUtils.onActivityResult(this, requestCode, resultCode, data)
    }
}