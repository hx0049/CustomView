package hx0049.customview.view.indexview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hx on 2016/12/19.
 * SideBar
 */

public class SideBar extends View {
    private char[] allLetter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'};
    private int xPosition;
    private int[] yPosition;

    private int allHeight = 0;
    private int allWidth = 0;
    private Paint paint;
    private int pressPosition = -1;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        yPosition = new int[allLetter.length];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        if (allHeight == 0 || getMeasuredHeight() > allHeight) {
            allHeight = getMeasuredHeight();
            allWidth = getMeasuredWidth();
        }
        int subLen = allHeight / allLetter.length;
        int textSize = Math.min(allWidth, subLen);
        paint.setTextSize(textSize);

        paint.setTypeface(Typeface.DEFAULT_BOLD);
        for (int i = 0, len = allLetter.length; i < len; i++) {
            int letterWidth = (int) paint.measureText(String.valueOf(allLetter[i]));
            if (i == 0) {
                yPosition[i] = subLen - 3;
                xPosition = (allWidth - letterWidth) / 2;
            } else {
                yPosition[i] = yPosition[i - 1] + subLen;
                xPosition = (allWidth - letterWidth) / 2;
            }
            if (i != pressPosition) {
                paint.setColor(Color.BLACK);
                canvas.drawText(String.valueOf(allLetter[i]), xPosition, yPosition[i], paint);
            } else {
                if(allLetter[i]!='\0') {
                    paint.setColor(Color.BLUE);
                    canvas.drawRect(0, i == 0 ? 0 : yPosition[i - 1], allWidth, yPosition[i], paint);
                    paint.setColor(Color.WHITE);
                    canvas.drawText(String.valueOf(allLetter[i]), xPosition, yPosition[i], paint);
                }
            }
        }

    }

    @Override
    public void invalidate() {
        if (yPosition == null) {
            yPosition = new int[allLetter.length];
        } else if (yPosition.length != allLetter.length) {
            yPosition = new int[allLetter.length];
        }
        super.invalidate();
    }

    public void setLetters(char[] leaveLetter) {
        int startPosition = (allLetter.length - leaveLetter.length) / 2 - 1;
        for (int i = 0; i < allLetter.length; i++) {
            allLetter[i] = '\0';
        }
        for (int i = 0; i < leaveLetter.length; i++) {
            if(leaveLetter[i] =='#'){
                allLetter[startPosition + i] = leaveLetter[i];
            }else {
                allLetter[startPosition + i] = (char) (leaveLetter[i] - 32);
            }
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getPressPositionAndInvalidate(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                getPressPositionAndInvalidate(x, y);
                break;
            case MotionEvent.ACTION_UP:
                pressPosition = -1;
                invalidate();
                if (onSelectListener != null) {
                    onSelectListener.onEndSelect();
                }
                break;
        }
        return true;

    }

    /**
     * get the touch position and invalidate
     */
    private void getPressPositionAndInvalidate(float x, float y) {
        for (int i = 0; i < yPosition.length; i++) {
            if (yPosition[i] < y && yPosition[i + 1] > y) {
                pressPosition = i + 1;
                invalidate();
                if (onSelectListener != null) {
                    if(allLetter[pressPosition] != '\0') {
                        onSelectListener.onSelect(pressPosition, allLetter[pressPosition]);
                    }else{
                        onSelectListener.onEndSelect();
                    }
                }
                return;
            }
        }
    }

    /**
     * touch callBack
     */
    public interface OnSelectListener {
        void onSelect(int pressPosition, char letter);
        void onEndSelect();
    }

    private OnSelectListener onSelectListener = null;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
}
