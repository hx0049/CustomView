package hx0049.customview.viewgroup;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import hx0049.customview.R;


/**
 * reated by hx on 2016/12/27.
 */

public class ActionSheetUtils {
    private Context context;
    private Animation showAnimation;
    private Animation hideAnimation;
    private String[] strings;
    private static ActionSheetUtils instance;
    private View totalView;
    LinearLayout llOptions;

    private ActionSheetUtils(Context context) {
        initAnimation();
        this.context = context;
    }

    public synchronized static ActionSheetUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ActionSheetUtils(context);
        }
        return instance;
    }

    private void initAnimation() {
        showAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0
        );
        showAnimation.setDuration(300);
//        showAnimation.setFillAfter(true);
        hideAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1
        );
        hideAnimation.setDuration(300);
    }


    public void showView(View container, String... value) {
        if (totalView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            totalView = inflater.inflate(R.layout.actionsheet_back, null);
            llOptions = (LinearLayout) totalView.findViewById(R.id.ll_options);
            TextView tvCancel = (TextView) totalView.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideViewFromWindow();
                }
            });
            totalView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideViewFromWindow();
                }
            });
        }else{
            if(totalView.getParent()!=null) {
                ((ViewGroup)(totalView.getParent())).removeView(totalView);
            }
        }
        strings = value;
         llOptions.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params_line = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        for (int i = 0; i < value.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(value[i]);
            textView.setTextSize((20));
            textView.setPadding(0, dp2px(10), 0, dp2px(10));
            textView.setGravity(Gravity.CENTER);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onActionSheetClickListener != null) {
                        onActionSheetClickListener.onClick(finalI, strings[finalI]);
                        hideViewFromWindow();
                    }
                }
            });
            llOptions.addView(textView, params);
            View view = new View(context);
            view.setBackgroundColor(Color.rgb(226,226,226));
            llOptions.addView(view, params_line);

        }


        ((ViewGroup) (container.getRootView())).addView(totalView);
        showViewToWindow();
    }


    private boolean isValueSame(String[] value) {
        if (strings == null) {
            return false;
        } else if (value.length != strings.length) {
            return false;
        } else {
            for (int i = value.length - 1; i > 0; i--) {
                if (!value[i].equals(strings[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    private void hideViewFromWindow() {
        llOptions.startAnimation(hideAnimation);
        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                totalView.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showViewToWindow() {
        if (totalView == null) {
            return;
        }
        totalView.setVisibility(View.VISIBLE);
        llOptions.startAnimation(showAnimation);

    }

    private int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
    private int sp2px(int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }



    public interface OnActionSheetClickListener {
        void onClick(int position, String value);
    }

    private OnActionSheetClickListener onActionSheetClickListener = null;

    public void setOnActionSheetClickListener(OnActionSheetClickListener onActionSheetClickListener) {
        this.onActionSheetClickListener = onActionSheetClickListener;
    }
}
