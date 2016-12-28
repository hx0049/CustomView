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
        //if ViewGroup was  wrap_content and  wrap_content
        //calculate the width and height
        int width = 0;
        int height = 0;
        //record the height of every line
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
                //need to change line，record the max line height
                width = Math.max(width,lineWidth);
                //reset lineWidth
                lineWidth = 0;
                //add all height
                height = height + lineHeight;
                //calculate the next line height
                lineHeight = childHeight;
            } else {
                //get the max line height
                lineHeight = Math.max(lineHeight, childHeight);
                //add line width
                lineWidth = lineWidth + childWidth;
            }

            if (i == childCount - 1) {
                //add the last line height
                height = height + lineHeight;
                //calculate the width
                width = Math.max(width, lineWidth);
            }
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    //restore child View
    private List<List<View>> mAllChildViews = new ArrayList<>();
    //record the height of every line
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean b, int A_left, int A_top, int A_right, int A_bottom) {
        mAllChildViews.clear();
        mLineHeight.clear();
        //get the height of AllView
        int width = getMeasuredWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        //record the view of current line
        List<View> lineViews = new ArrayList<>();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //need to change line
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
        //do something with the last line
        mLineHeight.add(lineHeight);
        mAllChildViews.add(lineViews);

        //set the position of child View
        int left = 0;
        int top = 0;
        //line number
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
     * generate LayoutParams for current view
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
