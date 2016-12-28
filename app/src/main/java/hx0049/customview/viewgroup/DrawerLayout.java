package hx0049.customview.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import hx0049.customview.R;

/**
 * Created by hx on 2016/12/15.
 */

public class DrawerLayout extends HorizontalScrollView {
    int screenWidth;
    int offset;
    int menuWidth;
    ViewGroup menu;
    ViewGroup page;
    boolean isMenuOpen = true;

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DrawerLayout);
        offset = ta.getDimensionPixelOffset(R.styleable.DrawerLayout_offset, 0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LinearLayout layout = (LinearLayout) getChildAt(0);
        menu = (ViewGroup) layout.getChildAt(0);
        page = (ViewGroup) layout.getChildAt(1);
        menuWidth = screenWidth - offset;
        menu.getLayoutParams().width = menuWidth;
        page.getLayoutParams().width = screenWidth;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            scrollTo(0, 0);
        }
    }

    private void openMenu() {
        if (isMenuOpen) {
            return;
        }
        scrollTo(0, 0);
        isMenuOpen = true;
    }

    private void closeMenu() {
        if (isMenuOpen) {
            scrollTo(menuWidth, 0);
            isMenuOpen = false;
        }
    }

    private void toggle() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > menuWidth / 2) {
                    scrollTo(menuWidth,0);
                    isMenuOpen = false;
                }else{
                    scrollTo(0,0);
                    isMenuOpen = true;
                }
                return true;

        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //l:scrollX
        //t:scrollY
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / menuWidth;  //0-1
        float leftScale = 1 - 0.3f * scale;     //1-0.7
        float rightScale = 0.8f + scale * 0.2f; //0.8-1

        menu.setScaleX(leftScale);
        menu.setScaleY(leftScale);
        menu.setAlpha(0.4f + 0.6f * (1 - scale));//1-0.4
        menu.setTranslationX(menuWidth * scale * 0.6f);

//        page.setPivotX(0);
//        page.setPivotY(page.getHeight() / 2);
        page.setScaleX(rightScale);
        page.setScaleY(rightScale);
        page.setAlpha(scale);//0-1
    }
}
