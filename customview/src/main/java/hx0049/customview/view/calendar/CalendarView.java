package hx0049.customview.view.calendar;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Calendar;

import hx0049.customview.R;
import hx0049.customview.view.calendar.callback.TimeCallBack;

/**
 * Created by hx on 2016/12/23.
 */

public class CalendarView extends LinearLayout {
    private TimeCallBack timeCallBack;
    private CalendarViewPager viewPager;
    private CalenderPagerAdapter calenderPagerAdapter;

    private int currentYear;
    private int currentMonth;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.calendar_view, this);
        viewPager = (CalendarViewPager) view.findViewById(R.id.view_pager);

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);


        calenderPagerAdapter = new CalenderPagerAdapter(getContext().getApplicationContext());
        viewPager.setAdapter(calenderPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (timeCallBack != null) {
                    timeCallBack.currentTime(position / CalenderPagerAdapter.PERIOD_YEAR,
                            position % CalenderPagerAdapter.PERIOD_YEAR);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(currentYear * CalenderPagerAdapter.PERIOD_YEAR + currentMonth);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timeCallBack != null) {
                    timeCallBack.currentTime(currentYear, currentMonth);
                }
            }
        }, 50);

        calenderPagerAdapter.setGetIdCallBack(viewPager);

    }

    public void setTimeCallBack(TimeCallBack timeCallBack) {
        this.timeCallBack = timeCallBack;
    }
    public void setOffset(int height){
        calenderPagerAdapter.setOffset(height);
    }
    public void setEnd(){
        calenderPagerAdapter.setEnd();
    }

}
