package com.gocashback.lib_common.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.gocashback.lib_common.GcbRouterKt;
import com.gocashback.lib_common.R;
import com.gocashback.lib_common.alibc.AlibcManagerKt;
import com.gocashback.lib_common.base.GcbBaseActivity;
import com.gocashback.lib_common.event.LoginChangeEvent;
import com.gocashback.lib_common.imageload.ImageLoadManagerKt;
import com.gocashback.lib_common.model.TaobaoIfModel;
import com.gocashback.lib_common.model.TransferExtraIfModel;
import com.gocashback.lib_common.network.BaseObserver;
import com.gocashback.lib_common.network.ResponseTransformer;
import com.gocashback.lib_common.network.RetrofitFactoryKt;
import com.gocashback.lib_common.network.api.TransferApi;
import com.gocashback.lib_common.network.model.transfer.JumpTransferIfModel;
import com.gocashback.lib_common.utils.CommonUtilsKt;
import com.gocashback.lib_common.widget.GcbHeadView;
import com.gocashback.lib_common.widget.GcbImageView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import kotlin.jvm.JvmField;

import static com.gocashback.lib_common.RouterPathKt.ACTIVITY_WEB;
import static com.gocashback.lib_common.network.UserManagerKt.getUser;
import static com.gocashback.lib_common.network.UserManagerKt.getUserKey;
import static com.gocashback.lib_common.network.UserManagerKt.getUserSecret;
import static com.gocashback.lib_common.network.UserManagerKt.isLogin;

/**
 * @Author 55HAITAO
 * @Date 2019-07-05 13:37
 */
@Route(path = ACTIVITY_WEB)
public class WebActivity extends GcbBaseActivity {
    @JvmField
    @Autowired(name = "url")
    String url;

    @JvmField
    @Autowired(name = "transferExtraIfModel")
    TransferExtraIfModel transferExtraIfModel;

    private String mTitle = "";


    private ConstraintLayout clyt_transfer;
    private GcbHeadView      gcbHeadView;
    private GcbImageView     iv_store;
    private TextView         tv_rebate;
    private TextView         tv_code;
    private TextView         tv_code_copied;
    private TextView         tv_tips;

    private WebView webView;

    private ProgressBar mProgress;

    //    private ConstraintLayout clyt_transfer;
//    private ConstraintLayout clyt_transfer;
    @Override
    public int setLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initVars() {
        registerEventBus();

    }

    @Override
    public void initViews() {
        getImmersionBar().fitsSystemWindows(true).statusBarColor(R.color.white).statusBarDarkFont(true).init();


        clyt_transfer = findViewById(R.id.clyt_transfer);
        gcbHeadView = findViewById(R.id.gcbHeadView);
        iv_store = findViewById(R.id.iv_store);
        tv_rebate = findViewById(R.id.tv_rebate);
        tv_code = findViewById(R.id.tv_code);
        tv_code_copied = findViewById(R.id.tv_code_copied);
        tv_tips = findViewById(R.id.tv_tips);

        webView = findViewById(R.id.webView);
        mProgress = findViewById(R.id.progress_webpage);

        if (transferExtraIfModel != null) {
            clyt_transfer.setVisibility(View.VISIBLE);
            gcbHeadView.setCenterText(getResources().getString(R.string.go_buy_transfer));

            ImageLoadManagerKt.loadImage(transferExtraIfModel.getStore_logo(), 0, 0, 0, iv_store, false, 0, false, false);
            tv_rebate.setText(transferExtraIfModel.getRebate());
            if (TextUtils.isEmpty(transferExtraIfModel.getCoupon_code())) {
                tv_code.setVisibility(View.GONE);
                tv_code_copied.setVisibility(View.GONE);
            } else {
                tv_code.setText(transferExtraIfModel.getCoupon_code());
                tv_code.setVisibility(View.VISIBLE);
                tv_code_copied.setVisibility(View.VISIBLE);
                CommonUtilsKt.copyToClipboard(this, transferExtraIfModel.getCoupon_code(), "");
            }
            if (TextUtils.isEmpty(transferExtraIfModel.getRebate_explain())) {
                tv_tips.setText(getResources().getString(R.string.go_buy_how_to_tis));
            } else {
                tv_tips.setText(transferExtraIfModel.getRebate_explain());
//                tv_tips.setText("-" +transferExtraIfModel.getRebate_explain().replaceFirst("-", "").replace("-", "\n-"));

            }
        } else {
            clyt_transfer.setVisibility(View.GONE);
            gcbHeadView.setCenterText(mTitle);

        }

        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(true); //显示放大缩小 controler
        settings.setSupportZoom(true); //可以缩放
        settings.setDisplayZoomControls(false); // 隐藏缩放控件
        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE); //默认缩放模式
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        // useragent后面拼接"(WWHT,Android,V版本号)"
//        settings.setUserAgentString(settings.getUserAgentString() + " " + String.format("(WWHT,Android,V%s)", BuildConfig.VERSION_NAME));
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        // 允许https http内容混合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 开启调试
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setVerticalScrollBarEnabled(false);
        webView.addJavascriptInterface(new GcbJavascriptinterface(this), "android");

    }

    @Override
    public void initEvent() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.d("url=" + "shouldOverrideUrlLoading");
                if (url.startsWith("http")) {
                    view.loadUrl(processUrl(url));

                }
                return true;
//                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                isLoadOver = true;
                loadOver();

                super.onPageFinished(view, url);
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //                if (TextUtils.isEmpty(mTitle)) {
                mTitle = title;
                if (isTimeout3000 && isLoadOver) {
                    gcbHeadView.setCenterText(title);
                }

                //                }
            }

            public void onProgressChanged(WebView view, int progress) {
                // 增加Javascript异常监控
                //                CrashReport.setJavascriptMonitor(view, true);

                mProgress.setProgress(progress);
                if (progress == 100) {
                    mProgress.setVisibility(View.GONE);
                }

                if (progress == 100) {
                    mProgress.setVisibility(View.GONE);
                } else {
                    if (mProgress.getVisibility() == View.GONE)
                        mProgress.setVisibility(View.VISIBLE);
                    mProgress.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });


    }


    private Boolean isTimeout3000 = false;
    private Boolean isLoadOver    = false;

    @Override
    public void initData() {
        syncCookie();
        webView.loadUrl(processUrl(url));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isTimeout3000 = true;
                loadOver();
            }
        }, 3000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoadOver = true;
                loadOver();
            }
        }, 8000);

    }

    private void loadOver() {
        if (isLoadOver && isTimeout3000) {
            clyt_transfer.setVisibility(View.GONE);
            gcbHeadView.setCenterText(mTitle);

        }

    }

    private String processUrl(String url) {
        if (!url.contains("fromapp")) {
            if (url.contains("?")) {
                url = url + "&fromapp=1";
            } else {
                url = url + "?fromapp=1";
            }
        }
//        if (isLogin()) {
//            url = url + "&uid=" + getUser().getUid();
////            url = url + "&key=" + getUserKey() ;
////            url = url + "&secret=" + getUserSecret();
//        }
        Logger.d("url = " + url);
        return url;
    }

    /**
     * 同步cookie
     */
    protected void syncCookie() {

        CookieManager cookieManager = CookieManager.getInstance();
        CookieSyncManager.createInstance(this);
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        if (isLogin()) {
            String keyCookie       = "key=" + getUserKey();
            String secretCookie    = "secret=" + getUserSecret();
            String uidCookie       = "uid=" + getUser().getUid();
            String userNameCookie  = "userName=" + getUser().getUser_name();
            String userEmailCookie = "userEmail=" + getUser().getEmail();

            cookieManager.setCookie(RetrofitFactoryKt.getDEFAULT_DOMAIN(), keyCookie);
            cookieManager.setCookie(RetrofitFactoryKt.getDEFAULT_DOMAIN(), secretCookie);
            cookieManager.setCookie(RetrofitFactoryKt.getDEFAULT_DOMAIN(), uidCookie);
            cookieManager.setCookie(RetrofitFactoryKt.getDEFAULT_DOMAIN(), userNameCookie);
            cookieManager.setCookie(RetrofitFactoryKt.getDEFAULT_DOMAIN(), userEmailCookie);
        }
        cookieManager.setCookie(RetrofitFactoryKt.getDEFAULT_DOMAIN(), "fromapp=1");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }


    /**
     * JS交互方法
     *
     * @property context Context
     * @constructor
     */
    public class GcbJavascriptinterface {

        private Context mContext;

        public GcbJavascriptinterface(Context context) {
            mContext = context;
        }

        /**
         * 去商家详情
         */
        @JavascriptInterface
        public void goStore(String flag) {
            GcbRouterKt.startStoreDetailActivity(mContext, flag);
        }

        /**
         * 去优惠详情
         */
        @JavascriptInterface
        public void goDeal(String id) {
            GcbRouterKt.startDealDetailActivity(mContext, id);
        }

        /**
         * 登录
         */
        @JavascriptInterface
        public void goLogin() {
            GcbRouterKt.startLoginActivity(mContext, null);
        }


        /**
         * 去购买
         */
        @JavascriptInterface
        public void goTransfer(String gotobuy_url) {
            CommonUtilsKt.goToBuy(WebActivity.this, gotobuy_url, "");
        }


        /**
         * 打开淘宝客商品
         */
        @android.webkit.JavascriptInterface
        public void goTaobao(String json) { //    {"product_id":"571549260432","special_id":"2204145903","redirect_uri":"https://oauth.m.taobao.com/authorize?response_type=code&client_id=25642064&redirect_uri=www.gocashback.net/taobao&state=1212&view=wap","gotobuy_url":"/go/taobao/571549260432__1.4"}
            if (isLogin()) {
                final TaobaoIfModel taobaoObject = JSON.parseObject(json, TaobaoIfModel.class);
                if (null != taobaoObject) {
                    if (TextUtils.isEmpty(taobaoObject.getSpecial_id())) {
                        // 去授权
                        webView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl(taobaoObject.getRedirect_uri());
                            }
                        }, 300);
                    } else {
                        final String productId          = taobaoObject.getProduct_id();
                        String       gotobuy_url        = taobaoObject.getGotobuy_url();
                        String[]     list               = gotobuy_url.split("/");
                        String       productId_with_tag = list[list.length - 1];
                        RetrofitFactoryKt.createService(TransferApi.class)
                                .jumpTransfer("taobao", productId_with_tag, "", "6435")
                                .compose(ResponseTransformer.Companion.<JumpTransferIfModel>handleResult())
                                .subscribe(new BaseObserver<JumpTransferIfModel>(WebActivity.this) {
                                    @Override
                                    public void onSuccess(JumpTransferIfModel jumpTransferIfModel) {
                                        AlibcManagerKt.showAliItemDetail(WebActivity.this, productId);
                                    }
                                });

                    }
                }
            } else {
                GcbRouterKt.startLoginActivity(WebActivity.this, null);
            }
        }

    }

    @Subscribe
    public void onLoginChange(LoginChangeEvent loginChangeEvent) {
        syncCookie();
        webView.loadUrl(processUrl(url));

    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
