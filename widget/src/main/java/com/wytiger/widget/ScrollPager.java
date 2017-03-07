package com.wytiger.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;


/**
 * 滚动组件
 * 可以设置是否自动滚动, 滚动到边界时循环滚动或者将事件传递到上一层
 * @author wytiger
 * @date 2016年2月26日
 */
public class ScrollPager extends ViewPager {

    public static final int DEFAULT_INTERVAL = 3000;
    public static final int SCROLL_WHAT = 0;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    /**滑动到边界之后的模式*/
    /**
     * 当滑到第一个或者最后一个时: 啥也不做
     * do nothing when sliding at the last or first item
     **/
    public static final int SLIDE_BORDER_MODE_NONE = 0;
    /**
     * 当滑到第一个或者最后一个时: 循环滚动
     * cycle when sliding at the last or first item
     **/
    public static final int SLIDE_BORDER_MODE_CYCLE = 1;
    /**
     * 当滑到第一个或者最后一个时: 将事件传递给父view
     * deliver event to parent when sliding at the last or first item
     **/
    public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;

    
    
    /**
     * 时间间隔: auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
     **/
    private long mInterval = DEFAULT_INTERVAL;
    /**
     * 滚动方向: auto scroll direction, default is {@link #RIGHT}
     **/
    private int mDirection = RIGHT;
    
    
    /**
     * 当滑到第一个或者最后一个时自动循环
     * whether automatic cycle when auto scroll reaching the last or first item, default is true
     **/
    private boolean mIsCycle = true;
    
    /**
     * 手指触摸时停止
     * whether stop auto scroll when touching, default is true
     **/
    private boolean mStopScrollWhenTouch = true;
    
    
    /**
     * 滑到到边界时的模式,默认是啥也不做
     * how to process when sliding at the last or first item, default is
     * {@link #SLIDE_BORDER_MODE_NONE}
     **/
    private int mSlideBorderMode = SLIDE_BORDER_MODE_NONE;
    /**
     * 滑到到边界时是否需要动画效果,默认需要
     * whether animating when auto scroll at the last or first item
     **/
    private boolean mIsBorderAnimation = true;

    
    
    private Handler mHandler;
    private boolean mIsAutoScroll;
    private boolean mIsStopByTouch;
    private float mDownX;
    private MyScroller mScroller;

    /**
     * 构造函数
     *
     * @param context 上下文对象
     */
    public ScrollPager(Context context) {
    	this(context, null);
    }

    /**
     * 构造函数
     *
     * @param context      上下文对象
     * @param attributeSet 属性集合
     */
    public ScrollPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mHandler = new MyHandler();
        setViewPagerScroller();
    }

    /**
     * start auto scroll, first scroll delay time is {@link #getInterval()}
     */
    public void startAutoScroll() {
        mIsAutoScroll = true;
        sendScrollMessage(mInterval);
    }

    /**
     * start auto scroll
     *
     * @param delayTimeInMills: first scroll delay time
     */
    public void startAutoScroll(int delayTimeInMills) {
        mIsAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        mIsAutoScroll = false;
        mHandler.removeMessages(SCROLL_WHAT);
    }

    /**
     * set the factor by which the duration of sliding animation will change
     *
     * @param scrollFactor 
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }    
   

    /**
     * scroll only once
     */
    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount;
        if (adapter == null || adapter.getCount() <= 1) {
            return;
        }
        totalCount = adapter.getCount();

        int nextItem = (mDirection == LEFT) ? --currentItem : ++currentItem;
        if (nextItem < 0) {
            if (mIsCycle) {
                setCurrentItem(totalCount - 1, mIsBorderAnimation);
            }
        } else if (nextItem == totalCount) {
            if (mIsCycle) {
                setCurrentItem(0, mIsBorderAnimation);
            }
        } else {
            setCurrentItem(nextItem, true);
        }
    }

    /**
     * if stopScrollWhenTouch is true
     * if event is down, stop auto scroll.
     * if event is up, start auto scroll again.
     *
     * @param ev MotionEvent
     * @return boolean ""
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float mTouchX;
        if (mStopScrollWhenTouch) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN && mIsAutoScroll) {
                mIsStopByTouch = true;
                stopAutoScroll();
            } else if (ev.getAction() == MotionEvent.ACTION_UP && mIsStopByTouch) {
                startAutoScroll();
            }
        }

        if (mSlideBorderMode == SLIDE_BORDER_MODE_TO_PARENT 
        		|| mSlideBorderMode == SLIDE_BORDER_MODE_CYCLE) {        	
            mTouchX = ev.getX();
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                mDownX = mTouchX;
            }
            
            int currentItem = getCurrentItem();
            PagerAdapter adapter = getAdapter();
            int pageCount = adapter == null ? 0 : adapter.getCount();
            
            /**
             * current index is first one and slide to right or 
             * current index is last one and slide to left.
             * 
             * if slide border mode is to parent, 
             * then requestDisallowInterceptTouchEvent false;
             * else scroll to last one when current item is first one, scroll to
             * first one when current item is last one.
             */
            if ((currentItem == 0 && mDownX <= mTouchX) || (currentItem == pageCount - 1 && mDownX >= mTouchX)) {
                if (mSlideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (pageCount > 1) {
                        setCurrentItem(pageCount - currentItem - 1, mIsBorderAnimation);
                    }
                    //不要拦截事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onTouchEvent(ev);
            }
        }
        //不要拦截事件
        getParent().requestDisallowInterceptTouchEvent(true);
        
        return super.onTouchEvent(ev);
    }

    
    
    /**
     * get auto scroll time in milliseconds, default is
     * {@link #DEFAULT_INTERVAL}
     *
     * @return the interval
     */
    public long getInterval() {
        return mInterval;
    }

    /**
     * set auto scroll time in milliseconds, default is
     * {@link #DEFAULT_INTERVAL}
     *
     * @param interval the interval to set
     */
    public void setInterval(long interval) {
        this.mInterval = interval;
    }

    /**
     * get auto scroll direction
     *
     * @return {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public int getDirection() {
        return (mDirection == LEFT) ? LEFT : RIGHT;
    }

    /**
     * set auto scroll direction
     *
     * @param direction {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public void setDirection(int direction) {
        this.mDirection = direction;
    }

    /**
     * whether automatic cycle when auto scroll reaching the last or first item,
     * default is true
     *
     * @return the isCycle
     */
    public boolean isCycle() {
        return mIsCycle;
    }

    /**
     * set whether automatic cycle when auto scroll reaching the last or first
     * item, default is true
     *
     * @param isCycle the isCycle to set
     */
    public void setCycle(boolean isCycle) {
        this.mIsCycle = isCycle;
    }

    /**
     * whether stop auto scroll when touching, default is true
     *
     * @return the stopScrollWhenTouch
     */
    public boolean isStopScrollWhenTouch() {
        return mStopScrollWhenTouch;
    }

    /**
     * set whether stop auto scroll when touching, default is true
     *
     * @param stopScrollWhenTouch 是否触摸停止滚动
     */
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.mStopScrollWhenTouch = stopScrollWhenTouch;
    }

    /**
     * get how to process when sliding at the last or first item
     *
     * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE},
     * {@link #SLIDE_BORDER_MODE_TO_PARENT},
     * {@link #SLIDE_BORDER_MODE_CYCLE}, default is
     * {@link #SLIDE_BORDER_MODE_NONE}
     */
    public int getSlideBorderMode() {
        return mSlideBorderMode;
    }

    /**
     * set how to process when sliding at the last or first item
     *
     * @param slideBorderMode {@link #SLIDE_BORDER_MODE_NONE},
     *                        {@link #SLIDE_BORDER_MODE_TO_PARENT},
     *                        {@link #SLIDE_BORDER_MODE_CYCLE}, default is
     *                        {@link #SLIDE_BORDER_MODE_NONE}
     */
    public void setSlideBorderMode(int slideBorderMode) {
        this.mSlideBorderMode = slideBorderMode;
    }

    /**
     * whether animating when auto scroll at the last or first item, default is
     * true
     *
     * @return 是否有切换动画
     */
    public boolean isBorderAnimation() {
        return mIsBorderAnimation;
    }

    /**
     * set whether animating when auto scroll at the last or first item, default
     * is true
     *
     * @param isBorderAnimation 是否有切换动画
     */
    public void setBorderAnimation(boolean isBorderAnimation) {
        this.mIsBorderAnimation = isBorderAnimation;
    }

    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        mHandler.removeMessages(SCROLL_WHAT);
        mHandler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    
    /**
     * set ViewPager scroller to change animation duration when sliding
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            mScroller = new MyScroller(getContext(), (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 内部类
     *
     * @author wytiger
     * @date 2016年2月26日
     */
    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    scrollOnce();
                    sendScrollMessage(mInterval);
                default:
                    break;
            }
        }
    }
    
    /**
     * 内部类
     *
     * @author wytiger
     * @date 2016年2月26日
     */
    private class MyScroller extends Scroller {
        private double mScrollFactor = 1;

        /**
         * 构造函数
         * @param context 上下文对象
         */
        public MyScroller(Context context) {
            super(context);
        }

        /**
         * 构造函数
         * @param context      上下文对象
         * @param interpolator 加速器
         */
        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }
       
        /**
         * Set the factor by which the duration will change
         *
         * @param scrollFactor 切换时间
         */
        public void setScrollDurationFactor(double scrollFactor) {
            this.mScrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            int scrollDuration = (int) (duration * mScrollFactor);
            super.startScroll(startX, startY, dx, dy, scrollDuration);
        }
    }

    
}
