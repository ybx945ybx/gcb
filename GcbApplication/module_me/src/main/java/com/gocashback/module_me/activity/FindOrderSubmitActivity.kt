package com.gocashback.module_me.activity

import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ImageView
import cn.finalteam.rxgalleryfinal.RxGalleryFinal
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_FINDORDER_SUBMIT
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.IndexApi
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.index.upLoadItemModel
import com.gocashback.lib_common.network.model.user.LostOrderShowCurrencyIfModel
import com.gocashback.lib_common.network.model.user.LostOrderShowIfModel
import com.gocashback.lib_common.network.model.user.LostOrderShowStoreItemModel
import com.gocashback.lib_common.startFindOrderSubmitSelectStoreActivity
import com.gocashback.lib_common.utils.dateFormat
import com.gocashback.lib_common.utils.dateFormatNormal
import com.gocashback.lib_common.utils.show
import com.gocashback.lib_common.widget.BsSelectCurrencyDlg
import com.gocashback.lib_common.widget.GcbFindOrderEditView
import com.gocashback.lib_common.widget.datepicker.CustomDatePicker
import com.gocashback.lib_common.widget.datepicker.DateFormatUtils
import com.gocashback.module_me.R
import com.gocashback.module_me.event.SelectStoreEvent
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_find_order_submit.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.Subscribe
import java.io.File


/**
 * @Author 55HAITAO
 * @Date 2019-06-10 16:28
 */
@Route(path = ACTIVITY_FINDORDER_SUBMIT)
class FindOrderSubmitActivity : GcbBaseActivity() {

    var currency: LostOrderShowCurrencyIfModel? = null
    var store: LostOrderShowStoreItemModel? = null
    var imgList: ArrayList<String> = arrayListOf("", "", "")
    var upImgList: ArrayList<String> = arrayListOf("", "", "")

    private lateinit var mDatePicker: CustomDatePicker
    private var timestamp = 0L

    private var storeEnable = false
    private var dateEnable = true
    private var orderEnable = false
    private var amountEnable = false
    private var imgEnable = false

    private var currencyValue = "USD"

    private lateinit var bsSelectCurrencyDlg: BsSelectCurrencyDlg

    override fun setLayoutId(): Int {
        return R.layout.activity_find_order_submit
    }

    override fun initVars() {
        registerEventBus()
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        if (LOCALE_CHINESE == getLanguage(this)) {
            iv_screenshots_1.setBackgroundResource(R.mipmap.ic_add_screenshoot_zh)
            iv_screenshots_2.setBackgroundResource(R.mipmap.ic_add_screenshoot_zh)
            iv_screenshots_3.setBackgroundResource(R.mipmap.ic_add_screenshoot_zh)
        } else {
            iv_screenshots_1.setBackgroundResource(R.mipmap.ic_add_screenshoot)
            iv_screenshots_2.setBackgroundResource(R.mipmap.ic_add_screenshoot)
            iv_screenshots_3.setBackgroundResource(R.mipmap.ic_add_screenshoot)
        }

        initDatePicker()
    }

    private fun initDatePicker() {
        val beginTimestamp = DateFormatUtils.str2Long("1970-05-01", false)
        val endTimestamp = DateFormatUtils.str2Long("3000-05-01", false)
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = CustomDatePicker(this, CustomDatePicker.Callback { timestamp ->
            this@FindOrderSubmitActivity.timestamp = timestamp
            gfoev_date.setText(dateFormat(timestamp))
        }, beginTimestamp, endTimestamp).apply {
            // 不允许点击屏幕或物理返回键关闭
            setCancelable(false)
            // 不显示时和分
            setCanShowPreciseTime(false)
            // 不允许循环滚动
            setScrollLoop(false)
            // 不允许滚动动画
            setCanShowAnim(false)
        }

    }

    override fun initEvent() {
        gfoev_store.setOnClickListener {
            gfoev_order_id.clearEditFocus()
            gfoev_amount.clearEditFocus()
            startFindOrderSubmitSelectStoreActivity(this)
        }
        gfoev_store.onRightClickListener = object : GcbFindOrderEditView.OnRightClickListener {
            override fun onRightClick() {
                startFindOrderSubmitSelectStoreActivity(this@FindOrderSubmitActivity)
            }
        }
        gfoev_date.setOnClickListener {
            gfoev_order_id.clearEditFocus()
            gfoev_amount.clearEditFocus()
            if (TextUtils.isEmpty(gfoev_date.getText())) {
                mDatePicker.show(System.currentTimeMillis())
            } else {
                mDatePicker.show(timestamp)
            }
        }
        gfoev_order_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                orderEnable = !TextUtils.isEmpty(s)
                checkSubmitEnable()
            }
        })
        gfoev_amount.onCurrencyClickListener = object : GcbFindOrderEditView.OnCurrencyClickListener {
            override fun onCurrencyClick() {
                if (currency == null) {
                    show(this@FindOrderSubmitActivity, "数据加载中")
                } else {
                    gfoev_order_id.clearEditFocus()
                    gfoev_amount.clearEditFocus()
                    bsSelectCurrencyDlg.show()
                }

            }
        }
        gfoev_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                amountEnable = !TextUtils.isEmpty(s)
                checkSubmitEnable()
            }
        })

        iv_screenshots_1.setOnClickListener {
            gfoev_order_id.clearEditFocus()
            gfoev_amount.clearEditFocus()
            selectPhoto(iv_screenshots_1)
        }
        iv_screenshots_2.setOnClickListener {
            gfoev_order_id.clearEditFocus()
            gfoev_amount.clearEditFocus()
            selectPhoto(iv_screenshots_2)
        }
        iv_screenshots_3.setOnClickListener {
            gfoev_order_id.clearEditFocus()
            gfoev_amount.clearEditFocus()
            selectPhoto(iv_screenshots_3)
        }

        tv_submit.setOnClickListener {
            tv_submit.isEnabled = false
            uploadImg(0)
        }

    }

    private fun uploadImg(position: Int) {
        if (!TextUtils.isEmpty(imgList[position])) {
            // 创建 RequestBody，用于封装构建RequestBody
            val file = File(imgList[position])
            if (!file.exists()) {
                file.mkdirs()
            }
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            // MultipartBody.Part  和后端约定好Key，这里的partName是用image
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)


            createService(IndexApi::class.java)
                    .upLoad(body)
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<List<upLoadItemModel>>(this) {
                        override fun onSuccess(t: List<upLoadItemModel>) {
                            upImgList[position] = t[0].path

                            if (position < 2) {
                                uploadImg(position + 1)
                            } else {
                                var imgs = ""
                                upImgList.forEach {
                                    if (!TextUtils.isEmpty(it)) {
                                        imgs = if (TextUtils.isEmpty(imgs)) {
                                            it
                                        } else {
                                            "$imgs,$it"
                                        }
                                    }
                                }
                                createService(UserApi::class.java)
                                        .retrieveostOrderShow(store?.id ?: "",
                                                dateFormatNormal(timestamp),
//                                                gfoev_date.getText().toString(),
                                                gfoev_order_id.getText().toString(),
                                                currencyValue,
                                                gfoev_amount.getText().toString(),
                                                imgs)
                                        .compose(ResponseTransformer.handleResult())
                                        .compose(bindToLifecycle())
                                        .subscribe(object : BaseObserver<Any>(this@FindOrderSubmitActivity) {
                                            override fun onSuccess(t: Any) {
                                                show(this@FindOrderSubmitActivity, resources.getString(R.string.send_success))
                                                finishDelay(300)
                                            }

                                            override fun onFail(code: Int, msg: String) {
                                                super.onFail(code, msg)
                                                tv_submit.isEnabled = true

                                            }
                                        })
                            }
                        }

                        override fun onFail(code: Int, msg: String) {
                            super.onFail(code, msg)
                            tv_submit.isEnabled = true

                        }
                    })
        } else {
            if (position < 2) {
                uploadImg(position + 1)
            } else {
                var imgs = ""
                upImgList.forEach {
                    if (!TextUtils.isEmpty(it)) {
                        imgs = if (TextUtils.isEmpty(imgs)) {
                            it
                        } else {
                            "$imgs,$it"
                        }
                    }
                }
                createService(UserApi::class.java)
                        .retrieveostOrderShow(store?.id ?: "",
                                dateFormatNormal(timestamp),
//                                gfoev_date.getText().toString(),
                                gfoev_order_id.getText().toString(),
                                currencyValue,
                                gfoev_amount.getText().toString(),
                                imgs)
                        .compose(ResponseTransformer.handleResult())
                        .compose(bindToLifecycle())
                        .subscribe(object : BaseObserver<Any>(this@FindOrderSubmitActivity) {
                            override fun onSuccess(t: Any) {
                                show(this@FindOrderSubmitActivity, resources.getString(R.string.send_success))
                                finishDelay(300)
                            }

                            override fun onFail(code: Int, msg: String) {
                                super.onFail(code, msg)
                                tv_submit.isEnabled = true

                            }
                        })
            }
        }
    }

    private fun selectPhoto(target: ImageView) {
        AndPermission.with(this@FindOrderSubmitActivity)
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
                .onGranted {
                    //自定义方法的单选
                    RxGalleryFinal
                            .with(this)
                            .image()
                            .radio()
//                    .crop()
                            .imageLoader(ImageLoaderType.PICASSO)
                            .subscribe(object : RxBusResultDisposable<ImageRadioResultEvent>() {
                                override fun onEvent(t: ImageRadioResultEvent?) {
                                    val imagePath = "file://" + t?.result?.originalPath
                                    val uri = Uri.parse(imagePath)
                                    target.setImageURI(uri)
                                    imgEnable = true
                                    checkSubmitEnable()
                                    when (target.id) {
                                        R.id.iv_screenshots_1 -> imgList[0] = t?.result?.originalPath
                                                ?: ""
                                        R.id.iv_screenshots_2 -> imgList[1] = t?.result?.originalPath
                                                ?: ""
                                        R.id.iv_screenshots_3 -> imgList[2] = t?.result?.originalPath
                                                ?: ""

                                    }
                                }

                            })
                            .openGallery()
                }
                .onDenied {

                }
                .start()


    }

    private fun checkSubmitEnable() {
        tv_submit.isEnabled = storeEnable && dateEnable && orderEnable && amountEnable && imgEnable

    }

    override fun initData() {
        createService(UserApi::class.java)
                .lostOrderShow(0, 20, "")
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<LostOrderShowIfModel>(this) {
                    override fun onSuccess(t: LostOrderShowIfModel) {
                        if (currency == null) {
                            currency = t.currency
                            bsSelectCurrencyDlg = BsSelectCurrencyDlg(this@FindOrderSubmitActivity, t.currency!!,
                                    object : BsSelectCurrencyDlg.OnDlgItemClickListener {
                                        override fun onDlgItemClick(currencyValue: String) {
                                            this@FindOrderSubmitActivity.currencyValue = currencyValue
                                            gfoev_amount.setCurrency(currencyValue)
                                        }
                                    })
                        }

                    }
                })
    }

    @Subscribe
    fun storeSelect(storeEvent: SelectStoreEvent) {
        store = storeEvent.lostOrderShowStoreItemModel
        gfoev_store.setText(store?.name ?: "")
        storeEnable = true
        checkSubmitEnable()
    }
}