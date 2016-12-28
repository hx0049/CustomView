package hx0049.customview.view.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import hx0049.customview.view.calendar.callback.GetIdCallBack;

/**
 * Created by hx on 2016/12/23.
 */

public class CalendarViewPager extends ViewPager implements GetIdCallBack{
    private int currentViewId;


    public CalendarViewPager(Context context) {
        super(context);
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int height = 0;

            View child = findViewById(currentViewId);

            child.measure(widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();


            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (NullPointerException e) {
            int height = 0;
            //get all child's height and get the highest one
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height)
                    height = h;
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }



    @Override
    public void currentId(int viewId) {
        currentViewId = viewId;
    }

}
