package hx0049.customviewdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hx0049.customview.view.banner.BannerView;
import hx0049.customview.view.calendar.CalendarView;
import hx0049.customview.view.calendar.callback.TimeCallBack;
import hx0049.customview.view.indexview.IndexView;
import hx0049.customview.view.titlebar.TitleBar;
import hx0049.customview.viewgroup.ArcMenu;
import hx0049.customview.viewgroup.FlowLayout;

public class MainActivity extends AppCompatActivity {


    String allStringForChoose = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*()_+-=|[]{};,.<>?/";
    Random random = new Random();

    IndexView indexView;
    ArcMenu menu;
    TitleBar titleBar;
    FlowLayout layout;
    BannerView bannerView;


    List<View> allView = new ArrayList<>();


    private List<String> data;


    float downY;

    int lastHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int mode = 3;

        switch (mode){
            case 1:
                //显示日历
                initCalendarView();
                break;
            case 2:
                //显示Banner
                initBannerView();
                break;
            case 3:
                //显示IndexView
                initIndexView();
                break;
            case 4:
                //显示流式布局
                initFlowLayout();
                break;

        }






    }

    private void initCalendarView() {

        final TitleBar titleBar = (TitleBar)findViewById(R.id.title);
        final CalendarView calendar = (CalendarView)findViewById(R.id.calendar_view);
        calendar.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_touch).setVisibility(View.VISIBLE);
        calendar.setTimeCallBack(new TimeCallBack() {
            @Override
            public void currentTime(int year, int month) {
                titleBar.setTitle(year+"."+(month+1));
            }
        });
        TextView tvTouch = (TextView)findViewById(R.id.tv_touch);
        tvTouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int a=event.getAction();
                switch (a){
                    case MotionEvent.ACTION_DOWN:
                        downY =  event.getRawY();
                        Log.d("-----------", "onTouch: ---------down---------"+downY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float nowY = event.getRawY();

                        Log.d("-----------", "onTouch: ---------move------1---"+nowY);
                        Log.d("-----------", "onTouch: ---------move------2---"+lastHeight+"  "+(nowY-downY));
                        calendar.setOffset((int) (nowY - downY));
                        downY = nowY;
//                        downY = downY-(nowY - downY);
                        break;
                    case MotionEvent.ACTION_UP:
                        calendar.setEnd();
                }
                return true;
            }
        });
    }


    public List<String> getData() {
        data = new ArrayList<>();
        data.add("张三");
        data.add("李四");
        data.add("李五");
        data.add("王六");
        data.add("曲磊");
        data.add("马秋晓");
        data.add("黄祥");
        data.add("郑程鹏");
        data.add("谢晨俣");
        data.add("吴莹");
        data.add("杨帆");
        data.add("陈达");
        data.add("曲宁");
        data.add("朱恩凯");
        data.add("黄兴");
        data.add("孙悟空");
        data.add("猪八戒");
        data.add("唐僧");
        data.add("沙和尚");
        data.add("白龙马");
        data.add("盖伦");
        data.add("赵信");
        data.add("李青");
        data.add("aquery");
        data.add("@dfgsr");
        data.add("@dfgs111r");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("AAA");
        data.add("");
        return data;
    }

    public void initIndexView() {
        indexView = (IndexView) findViewById(R.id.index_view);
        indexView.setVisibility(View.VISIBLE);
        indexView.setData(getData());
        indexView.setOnItemClickListener(new IndexView.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
        indexView.setOnItemLongClickListener(new IndexView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(String content) {
                Toast.makeText(MainActivity.this, "长按 " + content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBannerView() {
        List<String> urlList = new ArrayList<>();
        urlList.add("http://img6.bdstatic.com/img/image/smallpic/chongwu1215.jpg");
        urlList.add("http://img1.imgtn.bdimg.com/it/u=2387069514,246472357&fm=23&gp=0.jpg");
        urlList.add("http://img0.imgtn.bdimg.com/it/u=3002675904,3202851880&fm=23&gp=0.jpg");
        urlList.add("http://img3.imgtn.bdimg.com/it/u=768118419,2774863002&fm=23&gp=0.jpg");
        urlList.add("http://img4.imgtn.bdimg.com/it/u=2532426396,3860903852&fm=23&gp=0.jpg");
        bannerView = (BannerView) findViewById(R.id.pg);
       bannerView.setUrlList(urlList);
        bannerView.setVisibility(View.VISIBLE);
    }

    private void initFlowLayout() {
        layout = (FlowLayout) findViewById(R.id.fl);
        layout.setVisibility(View.VISIBLE);
        for (int i = 0; i < 100; i++) {

            TextView textView = new TextView(this);
            textView.setText(getRandomString());
            textView.setBackgroundColor(getRandomColor());
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 5;
            lp.leftMargin = 4;
            lp.bottomMargin = 3;
            lp.topMargin = 6;

            layout.addView(textView, lp);

        }

    }

    public String getRandomString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        int length = random.nextInt(20);
        for (int i = 0; i < length; i++) {
            int RandomNumber = random.nextInt(allStringForChoose.length());
            sb.append(allStringForChoose.charAt(RandomNumber));
        }
        return sb.toString();
    }

    public int getRandomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Color.rgb(red, green, blue);
    }


}
