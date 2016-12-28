package hx0049.customview.view.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hx on 2016/12/16.
 * BannerView 辅助类
 */

public class PointView extends View {
    int number = 1;//小圆点个数
    int currentPosition = 0;
    int radius = 5;//px
    int gap = 20;//px
    int[] xPosition;
    int yPosition;
    Paint paint = new Paint();

    public PointView(Context context) {
        this(context, null);
    }

    public PointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setNumber(0);
    }


    public void setNumber(int number) {
        this.number = number;
        xPosition = new int[number];

        int height = 60;
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int leftGap = (screenWidth - (number - 1) * gap) / 2;
        for (int i = 0; i < number; i++) {
            xPosition[i] = leftGap + i * gap;
        }
        yPosition = height / 2;
    }


    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (xPosition != null) {
            paint.setColor(Color.rgb(100, 100, 100));
            for (int i = 0; i < number; i++) {
                if (i == currentPosition) {
                    continue;
                }
                canvas.drawCircle(xPosition[i], yPosition, radius, paint);
            }
            paint.setColor(Color.BLUE);
            canvas.drawCircle(xPosition[currentPosition], yPosition, radius, paint);
        }


    }
}