package com.gocashback.lib_common.widget

import android.content.Context
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.gocashback.lib_common.R
import com.gocashback.lib_common.widget.GcbStatusView.StatusCategory.Companion.FINDORDER
import com.gocashback.lib_common.widget.GcbStatusView.StatusCategory.Companion.ORDER
import com.gocashback.lib_common.widget.GcbStatusView.StatusCategory.Companion.REBATE
import com.gocashback.lib_common.widget.GcbStatusView.StatusCategory.Companion.WITHDRAW
import kotlinx.android.synthetic.main.view_gcb_status_view.view.*

/**
 * 通用状态标签
 *
 * @Author 55HAITAO
 * @Date 2019-06-05 09:29
 */
class GcbStatusView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    /**
     * 标签类型
     *
     * 我的返利   0待生效，1已生效
     * 提现记录   0支付中，1已支取，2提现失败 3提现驳回
     * 找回订单   0待审核 1已添加 2关闭 3驳回 45678待确认
     * 我的订单    0,1,5待生效 4已生效
     *
     */
    @IntDef(REBATE, WITHDRAW, FINDORDER, ORDER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class StatusCategory {
        companion object {
            const val REBATE = 0    // 我的返利
            const val WITHDRAW = 1  // 提现记录
            const val FINDORDER = 2 // 找回订单
            const val ORDER = 3     // 我的订单
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_status_view, this)
    }

    fun setViewStatus(@StatusCategory statusCategory: Int, status: Int) {
        tv_status.apply {
            when (statusCategory) {
                REBATE -> {
                    text = if (status == 0) {
                        setBackgroundResource(R.drawable.bg_status_yellow)
                        context.getString(R.string.status_cash_pending)
                    } else {
                        setBackgroundResource(R.drawable.bg_status_green)
                        context.getString(R.string.status_cash_available)
                    }

                }
                WITHDRAW -> {
                    text = when (status) {
                        0 -> {
                            setBackgroundResource(R.drawable.bg_status_blue)
                            context.getString(R.string.status_withdraw_processing)
                        }
                        1 -> {
                            setBackgroundResource(R.drawable.bg_status_green)
                            context.getString(R.string.status_withdraw_paid)
                        }
                        else -> {
                            setBackgroundResource(R.drawable.bg_status_grey)
                            context.getString(R.string.status_withdraw_failed)
                        }
                    }
                }
                FINDORDER -> {
                    text = when (status) {
                        0 -> {
                            setBackgroundResource(R.drawable.bg_status_yellow)
                            context.getString(R.string.status_find_order_pending)
                        }
                        1 -> {
                            setBackgroundResource(R.drawable.bg_status_green)
                            context.getString(R.string.status_find_order_added)
                        }
                        2 -> {
                            setBackgroundResource(R.drawable.bg_status_orange)
                            context.getString(R.string.status_find_order_closed)
                        }
                        3 -> {
                            setBackgroundResource(R.drawable.bg_status_grey)
                            context.getString(R.string.status_find_order_invalid)
                        }
                        else -> {
                            setBackgroundResource(R.drawable.bg_status_blue)
                            context.getString(R.string.status_find_order_reviewing)
                        }
                    }
                }
                ORDER -> {
                    text = if (status == 4) {
                        setBackgroundResource(R.drawable.bg_status_green)
                        context.getString(R.string.status_order_available)
                    } else {
                        setBackgroundResource(R.drawable.bg_status_yellow)
                        context.getString(R.string.status_order_pending)
                    }
                }

            }
        }
    }

    companion object {
        fun transferStatus(context: Context, @StatusCategory statusCategory: Int, status: Int): String {
            when (statusCategory) {
                REBATE -> {
                    return if (status == 0) {
                        context.getString(R.string.status_cash_pending)
                    } else {
                        context.getString(R.string.status_cash_available)
                    }

                }
                WITHDRAW -> {
                    return when (status) {
                        0 -> {
                            context.getString(R.string.status_withdraw_processing)
                        }
                        1 -> {
                            context.getString(R.string.status_withdraw_paid)
                        }
                        else -> {
                            context.getString(R.string.status_withdraw_failed)
                        }
                    }
                }
                FINDORDER -> {
                    return when (status) {
                        0 -> {
                            context.getString(R.string.status_find_order_pending)
                        }
                        1 -> {
                            context.getString(R.string.status_find_order_added)
                        }
                        2 -> {
                            context.getString(R.string.status_find_order_closed)
                        }
                        3 -> {
                            context.getString(R.string.status_find_order_invalid)
                        }
                        else -> {
                            context.getString(R.string.status_find_order_reviewing)
                        }
                    }
                }
                ORDER -> {
                    return if (status == 4) {
                        context.getString(R.string.status_order_available)
                    } else {
                        context.getString(R.string.status_order_pending)
                    }
                }
                else -> return ""
            }

        }

        fun transferRebateType(context: Context, type: Int): String {
            return when (type) { // 1订单返利，2朋友订单返利，3注册奖励，4邀请奖励，6活动奖励，7线下活动
                1 -> context.getString(R.string.rebate_type_order)
                2 -> context.getString(R.string.rebate_type_friend_order)
                3 -> context.getString(R.string.rebate_type_register)
                4 -> context.getString(R.string.rebate_type_invite)
                6 -> context.getString(R.string.rebate_type_activity)
                7 -> context.getString(R.string.rebate_type_offline)
                else -> ""
            }
        }
        fun transferWithdrawType(context: Context, type: Int): String {
            return when (type) { // 0全部 1paypal 3支票 5支付宝 6礼品卡
                1 -> context.getString(R.string.withdraw_recorder_type_paypal)
                3 -> context.getString(R.string.withdraw_recorder_type_check)
                5 -> context.getString(R.string.withdraw_recorder_type_alipay)
                6 -> context.getString(R.string.withdraw_recorder_type_gift)
                else -> ""
            }
        }
    }
}