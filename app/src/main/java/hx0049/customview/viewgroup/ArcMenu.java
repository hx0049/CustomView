package hx0049.customview.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import hx0049.customview.R;

/**
 * Created by hx on 2016/12/15.
 */

public class ArcMenu extends ViewGroup {
    private static final int LEFT_TOP = 0;
    private static final int LEFT_BOTTOM = 1;
    private static final int RIGHT_TOP = 2;
    private static final int RIGHT_BOTTOM = 3;

    private int position;
    private int radius;
    private State state = State.CLOSE;
    private long Duration_All = 500;

    public enum State {
        OPEN, CLOSE
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ArcMenu);
        position = ta.getInt(R.styleable.ArcMenu_position, LEFT_TOP);
        radius = ta.getDimensionPixelSize(R.styleable.ArcMenu_radius, dp2px(30));
        ta.recycle();
    }

    public int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutButton();
            int count = getChildCount();
            for (int i = 0; i < count - 1; i++) {
                View child = getChildAt(i + 1);
                child.setVisibility(GONE);
                int cl = (int) (radius * Math.sin(Math.PI / 2 / (count - 2) * i));
                int ct = (int) (radius * Math.cos(Math.PI / 2 / (count - 2) * i));
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (position == RIGHT_BOTTOM || position == RIGHT_TOP) {
                    cl = getMeasuredWidth() - childWidth - cl;
                }
                if (position == RIGHT_BOTTOM || position == LEFT_BOTTOM) {
                    ct = getMeasuredHeight() - childHeight - ct;
                }
                child.layout(cl, ct, cl + childWidth, ct + childHeight);
            }
        }
    }

    private void layoutButton() {
        View child = getChildAt(0);
        int left = 0;
        int top = 0;
        switch (position) {
            case LEFT_TOP:
                break;
            case LEFT_BOTTOM:
                top = getMeasuredHeight() - child.getMeasuredHeight();
                break;
            case RIGHT_TOP:
                left = getMeasuredWidth() - child.getMeasuredWidth();
                break;
            case RIGHT_BOTTOM:
                top = getMeasuredHeight() - child.getMeasuredHeight();
                left = getMeasuredWidth() - child.getMeasuredWidth();
                break;
            default:
        }
        child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenu();
            }
        });
    }

    private void toggleMenu() {
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View child = getChildAt(i + 1);
            int xFlag = 1;
            int yFlag = 1;
            if (position == LEFT_TOP || position == LEFT_BOTTOM) {
                xFlag = -1;
            }
            if (position == RIGHT_TOP || position == LEFT_TOP) {
                yFlag = -1;
            }
            // child left
            int cl = (int) (radius * Math.sin(Math.PI / 2 / (count - 2) * i));
            // child top
            int ct = (int) (radius * Math.cos(Math.PI / 2 / (count - 2) * i));
            AnimationSet animationSet = new AnimationSet(true);
            Animation animation = null;
            if (state == State.CLOSE) {
                animation = new TranslateAnimation(xFlag * cl, 0f, yFlag * ct, 0f);
                child.setClickable(true);
                child.setFocusable(true);
            } else {
                animation = new TranslateAnimation(0f, xFlag * cl, 0f, yFlag * ct);
                child.setClickable(false);
                child.setFocusable(false);
            }
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (state == State.CLOSE) {
                        child.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            animation.setDuration(Duration_All);
            animation.setFillAfter(true);
            animation.setStartOffset(300 * i / (count - 1));
            RotateAnimation rotate = new RotateAnimation(0, 720,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(Duration_All);
            rotate.setFillAfter(true);
            animationSet.addAnimation(rotate);
            animationSet.addAnimation(animation);
            child.startAnimation(animationSet);
            final int index = i + 1;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null) {
                        onMenuItemClickListener.onClick(child, index - 1);
                    }
                    menuItemAnim(index - 1);
                    changeStatus();

                }
            });
        }

        changeStatus();
    }

    private void menuItemAnim(int position) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);
            if (i == position) {
                childView.startAnimation(scaleBigAnim(300));
            } else {
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);

        }

    }

    private Animation scaleSmallAnim(int position) {
        Animation anim = new ScaleAnimation(1.0f, 0f, 1.0f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(Duration_All);
        anim.setFillAfter(true);
        return anim;
    }


    private Animation scaleBigAnim(int position) {
        AnimationSet animationset = new AnimationSet(true);

        Animation anim = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        animationset.addAnimation(anim);
        animationset.addAnimation(alphaAnimation);
        animationset.setDuration(Duration_All);
        animationset.setFillAfter(true);
        return animationset;
    }

    private void changeStatus() {
        int ii = radius;
        int jj = radius;
        if (state == State.CLOSE) {
            for (int i = 0; i < getChildCount() - 1; i++) {
                getChildAt(i + 1).setVisibility(VISIBLE);
            }
        } else {
            for (int i = 0; i < getChildCount() - 1; i++) {
                getChildAt(i + 1).setVisibility(GONE);
            }
        }
        state = state == State.CLOSE ? State.OPEN : State.CLOSE;

    }


    public interface OnMenuItemClickListener {
        void onClick(View view, int position);
    }

    private OnMenuItemClickListener onMenuItemClickListener = null;

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }
}
