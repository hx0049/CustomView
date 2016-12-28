package hx0049.customview.view.banner;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;



import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hx0049.customview.R;


/**
 * Created by hx on 2016/12/15.
 */

public class BannerView extends LinearLayout {

    public static final int ROLL_TIME = 2000;
    public static final int MODE_ALL = 0; //有几个显示几个
    public static final int MODE_CIRCLE = 1; //循环显示


    private Timer timer = new Timer();
    private int mode = MODE_CIRCLE;
    private int rollTime = 0;
    private List<String> urlStringList;
    private int urlSize = 0;
    private List<View> viewListForRecycler = new ArrayList<>();
    ViewPager viewPager;
    PointView pointView;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);
        mode = ta.getInt(R.styleable.BannerView_mode, MODE_ALL);
        rollTime = ta.getInt(R.styleable.BannerView_roll_time, ROLL_TIME);
        ta.recycle();
        View view = View.inflate(context, R.layout.banner_view, this);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        pointView = (PointView) view.findViewById(R.id.point_view);

    }


    public void setUrlList(final List<String> urlStringList) {
        this.urlStringList = urlStringList;
        urlSize = urlStringList.size();
        pointView.setNumber(urlSize);
        if (viewPager.getAdapter() == null) {
            viewPager.setAdapter(new BannerAdapter());
            if (mode == MODE_ALL) {
                //最多list.size个页面
                viewPager.setCurrentItem(0);
                pointView.setCurrentPosition(0);
            } else {
                //最多Integer.MAX_VALUE个页面
                viewPager.setCurrentItem(getMiddlePosition());
                pointView.setCurrentPosition(getMiddlePosition() % urlSize);
            }
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pointView.setCurrentPosition(position % urlSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //开始轮播图片
        startRollPicture();
    }

    private int getMiddlePosition() {
        for (int i = Integer.MAX_VALUE / 2; i > 0; i--) {
            if (i % urlSize == 0) {
                return i;
            }
        }
        return 0;
    }

    private void startRollPicture() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int current = viewPager.getCurrentItem();
                            if (mode == MODE_ALL) {
                                if (current + 1 >= urlSize) {
                                    viewPager.setCurrentItem(0);
                                    pointView.setCurrentPosition(0);
                                } else {
                                    viewPager.setCurrentItem(current + 1);
                                    pointView.setCurrentPosition(current + 1);
                                }
                            } else {
                                viewPager.setCurrentItem(current + 1);
                                pointView.setCurrentPosition((current + 1) % urlSize);
                            }
                        } catch (Exception e) {
                            timer.cancel();
                        }
                    }
                });
            }
        }, rollTime, rollTime);
    }


    class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mode == MODE_ALL ? urlStringList.size() : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            viewListForRecycler.add((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View viewForRecycler = getRecycledView();
            if (viewForRecycler == null) {
                viewForRecycler = new ImageView(getContext());
            } else {
                viewListForRecycler.remove(viewForRecycler);
            }
            if (onLoadImageListener != null) {
                onLoadImageListener.loadImage(urlStringList.get(position % urlSize), (ImageView) viewForRecycler);
            } else {
                loadImage(urlStringList.get(position % urlSize), (ImageView) viewForRecycler);
            }
            container.addView(viewForRecycler);
            return viewForRecycler;
        }

        private View getRecycledView() {
            for (int i = 0; i < viewListForRecycler.size(); i++) {
                if (viewListForRecycler.get(i) == null) {
                    continue;
                }
                if (viewListForRecycler.get(i).getParent() == null) {
                    return viewListForRecycler.get(i);
                }
            }
            return null;
        }
    }



    public void loadImage(String urlStr, ImageView iv) {
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            iv.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface OnLoadImageListener {
        void loadImage(String url, ImageView iv);
    }

    private OnLoadImageListener onLoadImageListener = null;

    public void setOnLoadImageListener(OnLoadImageListener onLoadImageListener) {
        this.onLoadImageListener = onLoadImageListener;
    }


}
