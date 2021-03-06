package com.ksdc.slidrecycler;

/**
 * 这是滑动删除的自定义控件
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


public class SlidingMenu extends HorizontalScrollView {


    //菜单占屏幕宽度比
    private static final float radio = 0.2f;
    private final int mScreenWidth; //屏幕宽
    private final int mMenuWidth; //滑动宽


    private boolean once = true;
    public boolean isOpen;

    public SlidingMenu(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = ScreenUtil.getScreenWidth(context);
        mMenuWidth = (int) (mScreenWidth * radio);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
    }

    /**
     * 关闭菜单
     */

    public void closeMenu() {
        this.smoothScrollTo(0, 0);
        isOpen = false;
    }

    /**
     * 菜单是否打开
     */
    public boolean isOpen() {
        return isOpen;
    }


    /**
     * 获取 adapter
     */
    private MyAdapter getAdapter() {
        View view = this;
        while (true) {
            view = (View) view.getParent();
            if (view instanceof RecyclerView) {
                break;
            }
        }
        return (MyAdapter) ((RecyclerView) view).getAdapter();
    }

    /**
     * 当打开菜单时记录此 view ，方便下次关闭
     */
    private void onOpenMenu() {
        getAdapter().holdOpenMenu(this);
        isOpen = true;
    }

    /**
     * 当触摸此 item 时，关闭上一次打开的 item
     */
    private void closeOpenMenu() {
        if (!isOpen) {
            getAdapter().closeOpenMenu();
        }
    }

    /**
     * 获取正在滑动的 item
     */
    public SlidingMenu getScrollingMenu() {
        return getAdapter().getScrollingMenu();
    }

    /**
     * 设置本 item 为正在滑动 item
     */
    public void setScrollingMenu(SlidingMenu scrollingMenu) {
        getAdapter().setScrollingMenu(scrollingMenu);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量子view宽的比例，重新赋值
        if (once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            //内容的view宽
            wrapper.getChildAt(0).getLayoutParams().width = mScreenWidth;
            //侧滑的view宽
            wrapper.getChildAt(1).getLayoutParams().width = mMenuWidth;
            once = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (getScrollingMenu() != null && getScrollingMenu() != this) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                closeOpenMenu();
                setScrollingMenu(this);
                break;
            case MotionEvent.ACTION_UP:
                setScrollingMenu(null);
                int scrollX = getScrollX();
                if (System.currentTimeMillis() - downTime <= 100 && scrollX == 0) {
                    if (mCustomOnClickListener != null) {
                        mCustomOnClickListener.onClick();
                    }
                    return false;
                }
                if (Math.abs(scrollX) > mMenuWidth / 2) {
                    //滑动最多不会超过menu的宽度
                    onOpenMenu();
                    this.smoothScrollTo(mMenuWidth, 0);
                } else {
                    this.smoothScrollTo(0, 0);
                }
                return false;
        }
        return super.onTouchEvent(ev);
    }
    long downTime = 0;


    interface CustomOnClickListener {
        void onClick();
    }

    private CustomOnClickListener mCustomOnClickListener;

    void setCustomOnClickListener(CustomOnClickListener listener) {
        this.mCustomOnClickListener = listener;
    }


}
