package hx0049.customview.view.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hx0049.customview.R;


/**
 * Created by hx on 2016/12/15.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {
    private int backGroundColor;
    private boolean canBack;
    private boolean showRightIcon;
    private int titleTextSize;
    private int titleTextColor;
    private String titleText;
    private boolean showLine;
    private int rightIconId;

    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivRight;



    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
        canBack = ta.getBoolean(R.styleable.TitleBar_canBack, true);
        showRightIcon = ta.getBoolean(R.styleable.TitleBar_showRightIcon, false);
        titleTextSize = (int) ta.getDimension(R.styleable.TitleBar_titleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.TitleBar_titleTextColor, Color.rgb(102, 102, 102));
        backGroundColor = ta.getColor(R.styleable.TitleBar_backgroundColor, Color.rgb(255, 255, 255));
        titleText = ta.getString(R.styleable.TitleBar_android_text);
        showLine = ta.getBoolean(R.styleable.TitleBar_showLine, true);
        rightIconId = ta.getResourceId(R.styleable.TitleBar_rightIcon,0);
        ta.recycle();
        init();
    }


    private void init() {
        //View
        View view = View.inflate(getContext(), R.layout.title_bar, this);
        RelativeLayout mainView = (RelativeLayout) view.findViewById(R.id.title_bar);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        LinearLayout llBack = (LinearLayout) view.findViewById(R.id.ll_back);
        LinearLayout llRight = (LinearLayout) view.findViewById(R.id.ll_right);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        View lineView = view.findViewById(R.id.v_line);
        //Click Event
        llBack.setOnClickListener(this);
        llRight.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        //initData
        if (!showRightIcon) {
            llRight.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(titleText)) {
            tvTitle.setText(titleText);
        }
        tvTitle.setTextColor(titleTextColor);
        if (titleTextSize != 0) {
            tvTitle.setTextSize(titleTextSize);
        }
        mainView.setBackgroundColor(backGroundColor);
        if (!showLine) {
            lineView.setVisibility(GONE);
        }
        if(rightIconId!=0){
            ivRight.setImageResource(rightIconId);
        }
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ll_back) {
            if (onLeftClickListener != null) {
                onLeftClickListener.onLeftClick();
            }else{
                if (canBack) {
                    ((Activity) getContext()).finish();
                }
            }
        } else if (viewId == R.id.ll_right) {
            if (onRightClickListener != null) {
                onRightClickListener.onRightClick();
            }
        } else if (viewId == R.id.tv_title) {
            if (onTitleClickListener != null) {
                onTitleClickListener.onTitleClick();
            }
        }
    }

    public void setLeftIcon(int drawableId) {
        ivBack.setImageResource(drawableId);
    }

    public void setRightIcon(int drawableId) {
        ivRight.setImageResource(drawableId);
    }

    public void setTitle(String titleText) {
        if (!TextUtils.isEmpty(titleText)) {
            tvTitle.setText(titleText);
        }
    }

    /**
     * right Icon click event
     */

    public interface OnRightClickListener {
        void onRightClick();
    }

    private OnRightClickListener onRightClickListener = null;

    public void setOnRightClickListener(OnRightClickListener onRightClickListener) {
        this.onRightClickListener = onRightClickListener;
    }

    /**
     * back click event
     */

    public interface OnLeftClickListener {
        void onLeftClick();
    }

    private OnLeftClickListener onLeftClickListener = null;

    public void setOnLeftClickListener(OnLeftClickListener onLeftClickListener) {
        this.onLeftClickListener = onLeftClickListener;
    }

    /**
     * title click event
     */
    public interface OnTitleClickListener {
        void onTitleClick();
    }

    private OnTitleClickListener onTitleClickListener = null;

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }
}
