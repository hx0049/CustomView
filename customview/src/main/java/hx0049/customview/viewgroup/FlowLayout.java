package hx0049.customview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hx on 2016/12/15.
 */

public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //如果当前ViewGroup宽高为 wrap_content : wrap_content
        //记录计算的宽高
        int width = 0;
        int height = 0;
        //记录每一行的宽高
        int lineHeight = 0;
        int lineWidth = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (lineWidth + childWidth > widthSize) {
                //要换行，记录最大宽度
                width = Math.max(width,lineWidth);
                //重置lineWidth
                lineWidth = 0;
                //增加高度
                height = height + lineHeight;
                //计算下一行高度
                lineHeight = childHeight;
            } else {
                //获取最大行高
                lineHeight = Math.max(lineHeight, childHeight);
                //叠加行宽
                lineWidth = lineWidth + childWidth;
            }

            if (i == childCount - 1) {
                //增加最后一行的高度
                height = height + lineHeight;
                //计算最大宽度
                width = Math.max(width, lineWidth);
            }
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    //存储所有子View
    private List<List<View>> mAllChildViews = new ArrayList<>();
    //每一行的高度
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean b, int A_left, int A_top, int A_right, int A_bottom) {
        mAllChildViews.clear();
        mLineHeight.clear();
        //获取当前View的宽度
        int width = getMeasuredWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        //记录当前行的view
        List<View> lineViews = new ArrayList<>();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //需要换行
            if (childWidth + lineWidth > width) {
                mLineHeight.add(lineHeight);
                mAllChildViews.add(lineViews);
                lineViews = new ArrayList<>();
                lineHeight = childHeight;
                lineViews.add(child);
                lineWidth = childWidth;
            } else {
                lineWidth = lineWidth + childWidth;
                lineHeight = Math.max(childHeight, lineHeight);
                lineViews.add(child);
            }
        }
        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllChildViews.add(lineViews);

        //设置子View的位置
        int left = 0;
        int top = 0;
        //行数
        int lineNumber = mLineHeight.size();
        for (int i = 0; i < lineNumber; i++) {
            List<View> viewList = mAllChildViews.get(i);
            int heightOfLine = mLineHeight.get(i);
            for (int j = 0; j < viewList.size(); j++) {
                View child = viewList.get(j);
                if (child.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                child.layout(cLeft, cTop, cRight, cBottom);
                left = left + lp.leftMargin + lp.rightMargin + child.getMeasuredWidth();
            }
            left = 0;
            top = top +heightOfLine;
        }

    }
    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
