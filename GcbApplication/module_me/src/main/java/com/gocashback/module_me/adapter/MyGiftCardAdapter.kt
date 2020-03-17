package com.gocashback.module_me.adapter

import android.app.Activity
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.model.user.UserGiftCardItemModel
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 15:11
 */
class MyGiftCardAdapter(private val activity: Activity?, list: List<UserGiftCardItemModel>) : BaseQuickAdapter<UserGiftCardItemModel, BaseViewHolder>(R.layout.item_my_gift_card, list) {

    private var countList = arrayListOf<TimerCountDown>()

    override fun convert(helper: BaseViewHolder, item: UserGiftCardItemModel) {
        with(item) {
            helper.setText(R.id.tv_gift_cards_store, brand_name)
                    .setText(R.id.tv_amount_bonus, moneyFormat(amount_bonus))
                    .setText(R.id.tv_reward_name, reward_name)
                    .setText(R.id.tv_add_time, activity?.resources?.getString(R.string.gift_cards_date) + " " + add_time)
                    .setGone(R.id.tv_gift_cards_resend, is_available == 0)
                    .setVisible(R.id.iv_gift_card_spent, is_available != 0)
                    .setText(R.id.tv_gift_cards_resend, time)
                    .addOnClickListener(R.id.tv_mark_as_spent)
                    .addOnClickListener(R.id.tv_gift_cards_resend)

            helper.getView<TextView>(R.id.tv_mark_as_spent).isEnabled = is_available == 0
            helper.getView<ConstraintLayout>(R.id.clyt_content).isEnabled = is_available == 0
            helper.getView<TextView>(R.id.tv_amount_bonus).isEnabled = is_available == 0
            helper.getView<TextView>(R.id.tv_reward_name).isEnabled = is_available == 0
            helper.getView<TextView>(R.id.tv_add_time).isEnabled = is_available == 0


        }
    }

    fun startCount(position: Int) {
        val mCountDownTimer = TimerCountDown((60 * 2000).toLong(), 1000, position)
        mCountDownTimer.start()
        countList.add(mCountDownTimer)

    }

    fun removeAllCount() {
        countList.forEach { it.cancel() }

    }

    internal inner class TimerCountDown(millisInFuture: Long, countDownInterval: Long, val position: Int) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            data[position].apply {
                isOnCount = true
                time = "" + millisUntilFinished / 1000 + "s"
            }
            notifyItemChanged(position)
        }

        override fun onFinish() {
            data[position].apply {
                isOnCount = false
                time = activity?.getString(R.string.gift_cards_resend) ?: ""
            }
            notifyItemChanged(position)
//            get_code.text = resources.getString(R.string.verify_mail_get_code)
        }
    }
}