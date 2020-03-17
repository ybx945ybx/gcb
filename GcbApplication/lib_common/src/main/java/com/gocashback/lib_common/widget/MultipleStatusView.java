package com.gocashback.lib_common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gocashback.lib_common.R;

import pl.droidsonroids.gif.GifImageView;


/**
 * 类描述：  一个方便在多种状态切换的view
 * <p>
 * 创建人:   续写经典
 * 创建时间: 2016/1/15 10:20.
 */
public class MultipleStatusView extends RelativeLayout {
    public static final int STATUS_CONTENT    = 0x00;
    public static final int STATUS_LOADING    = 0x01;
    public static final int STATUS_EMPTY      = 0x02;
    public static final int STATUS_ERROR      = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;

    public static final String EMPTY_INFO       = "empty_info";
    public static final String ERROR_INFO       = "error_info";
    public static final String EMPTY_RETRY_TEXT = "empty_retry_text";
    public static final String ERROR_RETRY_TEXT = "error_retry_text";


    private static final int NULL_RESOURCE_ID = -1;

    private View     mEmptyView;
    private View     mErrorView;
    private View     mLoadingView;
    private View     mNoNetworkView;
    private View     mContentView;
    private TextView mEmptyRetryView;
    private TextView mEmptyInfoView;
    //    private TextView mEmptyBackView;
    private TextView mErrorInfoView;
    private TextView mErrorRetryView;
    private View     mNoNetworkRetryView;
    private int      mEmptyViewResId;
    private int      mErrorViewResId;
    private int      mLoadingViewResId;
    private int      mNoNetworkViewResId;
    private int      mContentViewResId;
    private int      mViewStatus;

    private String mEmptyInfo;
    private String mErrorInfo;
    private String mEmptyRetryText;
    private String mErrorRetryText;

    private       LayoutInflater         mInflater;
    private       OnClickListener        mOnRetryClickListener;
    private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public MultipleStatusView(Context context) {
        this(context, null);
    }

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0);

        mEmptyViewResId =
                a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.layout_empty_view);
        mErrorViewResId =
                a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.layout_error_view);
        mLoadingViewResId =
                a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.layout_loading_view);
        mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_noNetworkView,
                R.layout.no_network_view);
        mContentViewResId = a.getResourceId(R.styleable.MultipleStatusView_contentView,
                NULL_RESOURCE_ID);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInflater = LayoutInflater.from(getContext());
        showContent();
    }

    /**
     * 获取当前状态
     */
    public int getViewStatus() {
        return mViewStatus;
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    /**
     * 显示空视图
     */
    public final void showEmpty() {
        showEmpty("");
    }

    /**
     * 显示空视图
     *
     * @param emptyInfo 提示信息
     */
    public final void showEmpty(String emptyInfo) {
        showEmpty(emptyInfo, "");
    }

    /**
     * 显示为空视图
     *
     * @param emptyInfo 提示信息
     * @param retryText 重试按钮文本
     */
    public final void showEmpty(String emptyInfo, String retryText) {
        mViewStatus = STATUS_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewResId, null);
            //            mEmptyBackView = mEmptyView.findViewById(R.id.emtpy_back_view);
            mEmptyInfoView = mEmptyView.findViewById(R.id.tv_empty_info);
            mEmptyRetryView = mEmptyView.findViewById(R.id.empty_retry_view);
            // 按钮文本不为空，则显示按钮，并添加点击事件
            if (null != mOnRetryClickListener && null != mEmptyRetryView && !TextUtils.isEmpty(retryText)) {
                mEmptyRetryView.setVisibility(VISIBLE);
                mEmptyRetryText = retryText;
                mEmptyRetryView.setText(retryText);
                mEmptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            // 为空信息
            if (!TextUtils.isEmpty(emptyInfo) && null != mEmptyInfoView) {
                mEmptyInfoView.setText(emptyInfo);
                mEmptyInfo = emptyInfo;
            }
            // 返回按钮
            /*if (mEmptyBackView != null) {
                mEmptyBackView.setVisibility(showBack ? VISIBLE : GONE);
                mEmptyBackView.setOnClickListener(v -> ((BaseActivity) getContext()).onBackPressed());
            }*/
            addView(mEmptyView, 0, mLayoutParams);
        }
        setVisibieByStatus(mViewStatus);
    }

    public final View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 显示错误视图
     */
    public final void showError() {
        showError("", "");
    }

    /**
     * 显示错误视图
     */
    public final void showError(String errorInfo) {
        showError(errorInfo, "");
    }

    /**
     * 显示错误视图
     */
    public final void showError(String errorInfo, String retryText) {
        mViewStatus = STATUS_ERROR;
        if (null == mErrorView) {
            mErrorView = mInflater.inflate(mErrorViewResId, null);
            mErrorInfoView = mErrorView.findViewById(R.id.tv_error_info);
            mErrorRetryView = mErrorView.findViewById(R.id.error_retry_view);

            if (null != mOnRetryClickListener && null != mErrorRetryView) {
                mErrorRetryView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mErrorView, 0, mLayoutParams);
        }
        if (!TextUtils.isEmpty(errorInfo) && mErrorInfoView != null) {
            mErrorInfo = errorInfo;
            mErrorInfoView.setText(errorInfo);
        }
        if (!TextUtils.isEmpty(retryText) && mErrorRetryView != null) {
            mErrorRetryText = retryText;
            mErrorRetryView.setText(retryText);
        }
        setVisibieByStatus(mViewStatus);
    }

    /**
     * 显示加载中视图e
     */
    public final void showLoading() {
        mViewStatus = STATUS_LOADING;
        if (null == mLoadingView) {
            mLoadingView = mInflater.inflate(mLoadingViewResId, null);
            GifImageView imgLoading = mLoadingView.findViewById(R.id.img_loading);
            // TODO: 2019/4/8
//            ImageLoaderUtils.showOnlineImage("res://" + mLoadingView.getContext().getPackageName() + "/" + R.drawable.ic_loading, imgLoading);
            imgLoading.setImageResource(R.drawable.common_loading);

            addView(mLoadingView, 0, mLayoutParams);
        }
        setVisibieByStatus(mViewStatus);
    }

    /**
     * 显示无网络视图
     */
    public final void showNoNetwork() {
        mViewStatus = STATUS_NO_NETWORK;
        if (null == mNoNetworkView) {
            mNoNetworkView = mInflater.inflate(mNoNetworkViewResId, null);
            mNoNetworkRetryView = mNoNetworkView.findViewById(R.id.no_network_retry_view);
            if (null != mOnRetryClickListener && null != mNoNetworkRetryView) {
                mNoNetworkRetryView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mNoNetworkView, 0, mLayoutParams);
        }
        setVisibieByStatus(mViewStatus);
    }

    /**
     * 显示内容视图
     */
    public final void showContent() {
        mViewStatus = STATUS_CONTENT;
        if (null == mContentView) {
            if (mContentViewResId != NULL_RESOURCE_ID) {
                //                mContentView = mInflater.inflate(mContentViewResId, null);
                //                addView(mContentView, 0, mLayoutParams);
                mContentView = findViewById(mContentViewResId);
            } else {
                mContentView = findViewById(R.id.content_view);
            }
        }
        setVisibieByStatus(mViewStatus);
    }

    private void setVisibieByStatus(int viewStatus) {
        if (null != mLoadingView) {
            mLoadingView.setVisibility(viewStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(viewStatus == STATUS_EMPTY ? View.VISIBLE : View.GONE);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(viewStatus == STATUS_ERROR ? View.VISIBLE : View.GONE);
        }
        if (null != mNoNetworkView) {
            mNoNetworkView.setVisibility(viewStatus == STATUS_NO_NETWORK ? View.VISIBLE : View.GONE);
        }
        if (null != mContentView) {
            //            Logger.d("-msv- show content view");
            mContentView.setVisibility(viewStatus == STATUS_CONTENT ? View.VISIBLE : View.GONE);
        }
    }

//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        Bundle bundle = (Bundle) state;
//        super.onRestoreInstanceState(bundle.getParcelable(TransConstant.State.SUPER_DATA));
//        // 状态
//        mViewStatus = bundle.getInt(TransConstant.State.STATUS, -1);
//        // 根据状态显示对应的布局
//        switch (mViewStatus) {
//            case STATUS_ERROR:
//                showError(bundle.getString(ERROR_INFO, ""), bundle.getString(ERROR_RETRY_TEXT, ""));
//                break;
//            case STATUS_EMPTY:
//                showEmpty(bundle.getString(EMPTY_INFO, ""), bundle.getString(EMPTY_RETRY_TEXT, ""));
//                break;
//            case STATUS_NO_NETWORK:
//                showNoNetwork();
//                break;
//            case STATUS_LOADING:
//                showLoading();
//                break;
//        }
//    }
//
//    @Nullable
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(TransConstant.State.SUPER_DATA, super.onSaveInstanceState());
//        // 保存状态
//        bundle.putInt(TransConstant.State.STATUS, mViewStatus);
//        if (mViewStatus == STATUS_EMPTY) {
//            // 保存空状态显示的文本
//            bundle.putString(EMPTY_INFO, mEmptyInfo);
//            bundle.putString(EMPTY_RETRY_TEXT, mEmptyRetryText);
//        } else if (mViewStatus == STATUS_ERROR) {
//            // 保存错误状态显示的文本
//            bundle.putString(ERROR_INFO, mErrorInfo);
//            bundle.putString(ERROR_RETRY_TEXT, mErrorRetryText);
//        }
//        return bundle;
//    }

    public void setLoadingViewResId(int loadingViewResId) {
        mLoadingViewResId = loadingViewResId;
    }

    public void setEmptyViewResId(int emptyViewResId) {
        mEmptyViewResId = emptyViewResId;
    }

    public void setErrorViewResId(int errorViewResId) {
        mErrorViewResId = errorViewResId;
    }
}
