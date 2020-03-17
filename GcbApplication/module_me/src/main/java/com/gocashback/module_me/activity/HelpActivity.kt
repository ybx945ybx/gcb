package com.gocashback.module_me.activity

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_HELP
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.HelpApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.help.HelpChildItemModel
import com.gocashback.lib_common.network.model.help.HelpItemModel
import com.gocashback.lib_common.startHelpChildActivity
import com.gocashback.lib_common.utils.copyToClipboard
import com.gocashback.lib_common.utils.show
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.HelpAdapter
import kotlinx.android.synthetic.main.activity_help.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 09:37
 */
@Route(path = ACTIVITY_HELP)
class HelpActivity : GcbBaseActivity() {

    private lateinit var helpAdapter: HelpAdapter

    override fun setLayoutId(): Int {
        return R.layout.activity_help
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

    }

    override fun initData() {
        createService(HelpApi::class.java)
                .help()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<List<HelpItemModel>>(this) {
                    override fun onSuccess(t: List<HelpItemModel>) {
                        // 添加其他问题
                        val list: MutableList<HelpItemModel> = mutableListOf()
                        list.addAll(t)
                        val helpItemModel: HelpItemModel = HelpItemModel().apply {
                            name = resources.getString(R.string.help_center_other_questions)
                            val helplist = mutableListOf<HelpChildItemModel>()
                            val helpChildItemModel = HelpChildItemModel().apply {
                                title = resources.getString(R.string.help_center_email)
                                right = resources.getString(R.string.help_center_feedback)

                            }
                            helplist.add(helpChildItemModel)
                            val helpChildItemModel1 = HelpChildItemModel().apply {
                                title = resources.getString(R.string.help_center_wx)
                                right = resources.getString(R.string.help_center_add_friend)

                            }
                            helplist.add(helpChildItemModel1)
                            help = helplist
                        }
                        list.add(helpItemModel)

                        elv_help.apply {
                            helpAdapter = HelpAdapter(list, this@HelpActivity)
                            setGroupIndicator(null)
                            setOnGroupClickListener { _, _, groupPosition, _ ->
                                if (elv_help.isGroupExpanded(groupPosition)) {
                                    elv_help.collapseGroup(groupPosition)
                                } else {
                                    elv_help.expandGroup(groupPosition)
                                }
                                true
                            }
                            setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                                if (groupPosition == list.size - 1) {
                                    if (childPosition + 1 == list[groupPosition].help?.size) {
                                        // 加微信
                                        copyToClipboard(this@HelpActivity, "gocashbackflm")
                                        show(this@HelpActivity, "微信号已复制到剪切板")
                                    } else {
                                        // 邮件
                                        val email = "support@gocashback.com"
                                        try {
                                            if (!TextUtils.isEmpty(email)) {
                                                val intent = Intent(Intent.ACTION_SENDTO)
                                                intent.data = Uri.parse("mailto:$email")
                                                startActivity(intent)
                                            }
                                        } catch (e: Exception) {
                                            copyToClipboard(this@HelpActivity, email)
                                            show(this@HelpActivity, "邮箱地址已复制到剪切板")
                                        }

                                    }
                                } else {
                                    startHelpChildActivity(this@HelpActivity, list[groupPosition].help?.get(childPosition)?.id
                                            ?: "")
                                }

                                true
                            }
                            setAdapter(helpAdapter)

                        }

                    }
                })
    }

}