package hx0049.customview.view.slidedelete;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import hx0049.customview.R;


/**
 * Created by hx on 2016/12/27.
 */

public class SlideMenu extends HorizontalScrollView {
    private int rightLength;
    private OpenOrCloseCallBack openOrCloseCallBack = null;
    private boolean isMenuOpen = false;

    public void setOpenOrCloseCallBack(OpenOrCloseCallBack openOrCloseCallBack) {
        this.openOrCloseCallBack = openOrCloseCallBack;
    }

    public SlideMenu(Context context) {
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideMenu);
        rightLength = ta.getDimensionPixelSize(R.styleable.SlideMenu_right_length, 100);
        ta.recycle();
//        this.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if(openOrCloseCallBack !=null){
//                    openOrCloseCallBack.whenTouch(SlideMenu.this);
//                }
//                return false;
//            }
//        });
    }


    public void setRightLength(int pxValue) {
        this.rightLength = pxValue;
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
                if (scrollX > rightLength / 2) {
                    openMenu();
                } else {
                    closeMenu();
                }

                return true;
        }

        return super.onTouchEvent(ev);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            closeMenu();
        }
    }

    public void openMenu() {
        isMenuOpen = true;
        smoothScrollTo(rightLength, 0);
        if(openOrCloseCallBack!=null){
            openOrCloseCallBack.getOpenState(true,this);
        }
    }

    public void closeMenu() {
        isMenuOpen = false;
        smoothScrollTo(0, 0);
        if(openOrCloseCallBack!=null){
            openOrCloseCallBack.getOpenState(false,this);
        }
    }
    public void closeQuietly(){
        isMenuOpen = false;
        smoothScrollTo(0, 0);
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }
}
