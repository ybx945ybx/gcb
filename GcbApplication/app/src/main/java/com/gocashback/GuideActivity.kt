package com.gocashback

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_GUIDE
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.startMainActivity
import com.gocashback.lib_common.utils.SP_KEY_FIRST_OPEN
import com.gocashback.lib_common.utils.put
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_guide.*

/**
 * @Author 55HAITAO
 * @Date 2019-08-01 16:35
 */
@Route(path = ACTIVITY_GUIDE)
class GuideActivity : GcbBaseActivity() {

    private lateinit var images: IntArray

    override fun setLayoutId(): Int {
        return R.layout.activity_guide
    }

    override fun initVars() {
        Logger.d("开启App----------GuideActivity")

        put(this, SP_KEY_FIRST_OPEN, false)

        images = intArrayOf(if (LOCALE_CHINESE == getLanguage(this)) R.mipmap.splash_content_1 else R.mipmap.splash_content_1_en,
                if (LOCALE_CHINESE == getLanguage(this)) R.mipmap.splash_content_2 else R.mipmap.splash_content_2_en,
                if (LOCALE_CHINESE == getLanguage(this)) R.mipmap.splash_content_3 else R.mipmap.splash_content_3_en)
    }

    override fun initViews() {

        view_pager.adapter = ImageAdapter(this)

    }

    override fun initEvent() {
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                view_btn.visibility = if (position == 2) View.VISIBLE else View.GONE

            }
        })


        view_btn.setOnClickListener {
            startMainActivity(this)
            finish()
        }
    }

    inner class ImageAdapter(context: Context) : PagerAdapter() {

        private var inflater: LayoutInflater = LayoutInflater.from(context)

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getCount(): Int {
            return images.size
        }

        override fun instantiateItem(view: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.item_image, view, false)!!
            val imgContent = imageLayout.findViewById<ImageView>(R.id.img_content)
            imgContent.setImageResource(images[position])
            view.addView(imageLayout, 0)
            return imageLayout
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

        override fun saveState(): Parcelable? {
            return null
        }
    }
}