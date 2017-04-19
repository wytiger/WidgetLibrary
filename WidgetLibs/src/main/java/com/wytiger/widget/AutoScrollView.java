package com.wytiger.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

/**
 * Author: xiongqi
 * Time: 2017-1-18
 * Desc: 当viewGroup发生改变时，追踪group内不应该被遮挡的在布局下方的控件anchorView，若被遮挡，则滚动。
 */
public class AutoScrollView extends ScrollView {

    private final String TAG = "WalletView";

    //最小移动距离
    private static final float MINIMUN_MOVE_Y = 10;

    private int anchorViewResId;
    private View anchorView;

    /**
     * 是否隐藏软键盘
     */
    private boolean hideKeyboard = true;

    /**
     * 不应该被遮挡的控件被遮住，应该滚动以显示
     */
    private boolean shouldScroll = false;

    /**
     * 滚动到底部以显示
     */
    private boolean scrollToAnchorView = true;

    private final Rect mTempRect = new Rect();

    public AutoScrollView(Context context) {
        super(context);
    }

    public AutoScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);

    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        getAttrs(attrs, defStyleAttr);
        //默认不显示滚动条
        setVerticalScrollBarEnabled(false);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.AutoScrollView, defStyleAttr, 0);
        if (ta != null) {
            anchorViewResId = ta.getResourceId(R.styleable.AutoScrollView_anchorView, 0);
            ta.recycle();
        }
    }

    float downx, upx;
    float downy, upy;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //LogUtils.d(TAG, "onTouchEvent");

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downx = ev.getRawX();
            downy = ev.getRawY();
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            hideKeyboard = false;
            scrollToAnchorView = false;
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            upx = ev.getRawX();
            upy = ev.getRawY();
            //只针对纵向滑动
            if (Math.abs(upy - downy) <= MINIMUN_MOVE_Y) {
                hideKeyboard = true;
            }
            //LogUtils.d(TAG, "onTouchEvent ACTION_UP");
            if (hideKeyboard) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //LogUtils.d(TAG, "onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //LogUtils.d(TAG, "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //LogUtils.d(TAG, String.format("onLayout begin{t:%d,b:%d}",getTop(),getBottom()) );
        super.onLayout(changed, l, t, r, b);
        //LogUtils.d(TAG, String.format("onLayout end{t:%d,b:%d}",getTop(),getBottom()) );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, String.format("onSizeChanged begin{w:%d,h:%d,oldw:%d,oldh:%d,t:%d,b:%d}", w, h, oldw, oldh, getTop(), getBottom()));
        super.onSizeChanged(w, h, oldw, oldh);
        //将不能遮盖的控件初始化
        if (anchorView == null && anchorViewResId != 0)
            anchorView = findViewById(anchorViewResId);

        //未设置，使用原始ScrollView的行为
        if (null == anchorView)
            return;

        int anchorScrollDelta = 0;
        View currentFocused = findFocus();
        if (null == currentFocused || this == currentFocused)
            return;

        if (isWithinDeltaOfScreen(anchorView, 0, oldh)) {

            anchorView.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(anchorView, mTempRect);
            anchorScrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
            // 若移动遮挡了有焦点的控件，则不移动
//            if(isWithinDeltaOfScreen(currentFocused,anchorScrollDelta,oldh)){
            doScrollY(anchorScrollDelta);
//            }

        }
    }

    private boolean isWithinDeltaOfScreen(View descendant, int delta, int height) {
        descendant.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(descendant, mTempRect);

        return (mTempRect.bottom + delta) >= getScrollY()
                && (mTempRect.top - delta) <= (getScrollY() + height);
    }

    /**
     * Smooth scroll by a Y delta
     *
     * @param delta the number of pixels to scroll by on the Y axis
     */
    private void doScrollY(int delta) {
        if (delta != 0) {
            if (isSmoothScrollingEnabled()) {
                smoothScrollBy(0, delta);
            } else {
                scrollBy(0, delta);
            }
        }
    }
}
