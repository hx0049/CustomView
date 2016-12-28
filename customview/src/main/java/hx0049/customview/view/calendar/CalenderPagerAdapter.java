package hx0049.customview.view.calendar;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hx0049.customview.R;
import hx0049.customview.view.calendar.callback.GetIdCallBack;
import hx0049.customview.view.calendar.model.DateModel;
import hx0049.customview.view.calendar.util.TimeUtil;

/**
 * Created by hx on 2016/12/23.
 */
class CalenderPagerAdapter extends PagerAdapter {
    private List<RecyclerView> offsetList = new ArrayList<>();

    public static final int PERIOD_YEAR = 12;
    private Context context;
    private List<View> viewForRecycler = new ArrayList<>();

    private int textView20spHeight = 0;
    private boolean isShrinkState = false;
    Handler handler = new Handler();

    public CalenderPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("------------------", "instantiateItem: ");
        View view = getRecycledView();
        final RecyclerView recyclerView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.calendar_view_child, container, false);
            recyclerView = (RecyclerView) (((ViewGroup) view).getChildAt(0));
            recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
            offsetList.add(recyclerView);
        } else {
            recyclerView = (RecyclerView) (((ViewGroup) view).getChildAt(0));
        }


        if (recyclerView.getAdapter() != null) {
            List<DateModel> data = ((CalendarRecyclerAdapter) recyclerView.getAdapter()).getData();
            data.clear();
            int year = position / PERIOD_YEAR;
            int month = position % PERIOD_YEAR;
            data.addAll(getDataByYearAndMonth(year, month));
            ((CalendarRecyclerAdapter) recyclerView.getAdapter()).setData(data);
        } else {
            int year = position / PERIOD_YEAR;
            int month = position % PERIOD_YEAR;
            CalendarRecyclerAdapter adapter = new CalendarRecyclerAdapter(getDataByYearAndMonth(year, month), context);
            recyclerView.setAdapter(adapter);
        }
        if (textView20spHeight == 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView20spHeight = ((ViewGroup) (recyclerView.getChildAt(0))).getChildAt(0).getMeasuredHeight();
                }
            }, 500);
        }

        if (isShrinkState) {
            if (((CalendarRecyclerAdapter) (recyclerView.getAdapter())).getData().size() == 35) {
                LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                pa.bottomMargin = -4 * (dp2px(15) + textView20spHeight);
                recyclerView.setLayoutParams(pa);
            } else if (((CalendarRecyclerAdapter) (recyclerView.getAdapter())).getData().size() == 42){
                LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                pa.bottomMargin = -5 * (dp2px(15) + textView20spHeight);
                recyclerView.setLayoutParams(pa);
            }else{
                LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                pa.bottomMargin = -3 * (dp2px(15) + textView20spHeight);
                recyclerView.setLayoutParams(pa);
            }
        } else {
            LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
            pa.bottomMargin = 0;
            recyclerView.setLayoutParams(pa);
        }
        container.addView(view);
        return view;
    }

    private View getRecycledView() {
        for (int i = 0, len = viewForRecycler.size(); i < len; i++) {
            if (viewForRecycler.get(i) == null) {
                continue;
            }
            if (viewForRecycler.get(i).getParent() == null) {
                return viewForRecycler.get(i);
            }
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        viewForRecycler.add((View) object);
    }

    /***********************************core method*********************************************/
    public List<DateModel> getDataByYearAndMonth(int year, int month) {
        List<DateModel> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int firstDay = TimeUtil.getDayNumForMonth(month == 0 ? year - 1 : year, month == 0 ? 11 : month - 1) - dayOfWeek + 2;
        for (int i = 1; i < dayOfWeek; i++) {
            result.add(new DateModel(month == 0 ? year - 1 : year, month == 0 ? 11 : month - 1, firstDay++, false));
        }
        int dayOfMonth = TimeUtil.getDayNumForMonth(year, month);
        for (int i = 1; i <= dayOfMonth; i++) {
            result.add(new DateModel(year, month, i, true));
        }
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        int lastPosition = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = lastPosition; i < 7; i++) {
            result.add(new DateModel(month == 11 ? year + 1 : year, month == 11 ? 0 : month - 1, i - lastPosition + 1, false));
        }
        return result;
    }

    /***********************************set Id and give it to ViewPager************BEGIN*********************************/
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        ((View) object).setId(position);
        if (getIdCallBack != null) {
            getIdCallBack.currentId(position);
        }
    }

    private GetIdCallBack getIdCallBack = null;

    public void setGetIdCallBack(GetIdCallBack getIdCallBack) {
        this.getIdCallBack = getIdCallBack;
    }
    /***********************************set Id and give it to ViewPage**************END*******************************/



    /***********************************set scroll event**************BEGIN*******************************/
    public void setOffset(int height) {
        for (int i = 0, len = offsetList.size(); i < len; i++) {
            LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) offsetList.get(i).getLayoutParams();
            if (height + pa.bottomMargin < 0) {
                pa.bottomMargin += height;
                if (((CalendarRecyclerAdapter) (offsetList.get(i).getAdapter())).getData().size() == 35) {
                    if (pa.bottomMargin >= -4 * (dp2px(15) + textView20spHeight)) {
                        offsetList.get(i).setLayoutParams(pa);
                    } else {
                        pa.bottomMargin = -4 * (dp2px(15) + textView20spHeight);
                        offsetList.get(i).setLayoutParams(pa);
                    }
                } else if (((CalendarRecyclerAdapter) (offsetList.get(i).getAdapter())).getData().size() == 42){
                    if (pa.bottomMargin >= -5 * (dp2px(15) + textView20spHeight)) {
                        offsetList.get(i).setLayoutParams(pa);
                    } else {
                        pa.bottomMargin = -5 * (dp2px(15) + textView20spHeight);
                        offsetList.get(i).setLayoutParams(pa);
                    }
                }else{
                    if (pa.bottomMargin >= -3 * (dp2px(15) + textView20spHeight)) {
                        offsetList.get(i).setLayoutParams(pa);
                    } else {
                        pa.bottomMargin = -3 * (dp2px(15) + textView20spHeight);
                        offsetList.get(i).setLayoutParams(pa);
                    }
                }
            }
        }
    }

    public void setEnd() {
        int height = 0;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) offsetList.get(0).getLayoutParams();
        height = params.bottomMargin;
        if (height == 0) {
            return;
        } else {
            if (-height > offsetList.get(0).getMeasuredHeight() / 2) {
                for (int i = 0, len = offsetList.size(); i < len; i++) {
                    if (((CalendarRecyclerAdapter) (offsetList.get(i).getAdapter())).getData().size() == 35) {
                        LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) offsetList.get(i).getLayoutParams();
                        pa.bottomMargin = -4 * (dp2px(15) + textView20spHeight);
                        offsetList.get(i).setLayoutParams(pa);
                        isShrinkState = true;
                    } else if (((CalendarRecyclerAdapter) (offsetList.get(i).getAdapter())).getData().size() == 42){
                        LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) offsetList.get(i).getLayoutParams();
                        pa.bottomMargin = -5 * (dp2px(15) + textView20spHeight);
                        offsetList.get(i).setLayoutParams(pa);
                        isShrinkState = true;
                    }else {
                        LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) offsetList.get(i).getLayoutParams();
                        pa.bottomMargin = -3 * (dp2px(15) + textView20spHeight);
                        offsetList.get(i).setLayoutParams(pa);
                        isShrinkState = true;
                    }
                }
            } else {
                for (int i = 0, len = offsetList.size(); i < len; i++) {
                    LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) offsetList.get(i).getLayoutParams();
                    pa.bottomMargin = 0;
                    offsetList.get(i).setLayoutParams(pa);
                    isShrinkState = false;
                }
            }
        }
    }
    /************************************set scroll event******************END*******************************/


    /***********************************change value among px sp and dp**************BEGIN*******************************/
    private int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    private int sp2px(int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
    /**********************************change value among px sp and dp*************END*******************************/
}