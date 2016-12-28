package hx0049.customview.commonAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hx on 2016/7/7.
 */
public class ComRecyclerViewHolder extends RecyclerView.ViewHolder {
    public SparseArray<View> mViews;
    public View wholeView;

    public Context context;

    public ComRecyclerViewHolder(Context context, View itemView) {
        super(itemView);
        wholeView = itemView;
        this.context = context;
        mViews = new SparseArray<View>();

    }

    public static ComRecyclerViewHolder getComRecyclerViewHolder(Context context, int layoutId, ViewGroup parent) {
        View wholeView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ComRecyclerViewHolder(context, wholeView);
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = wholeView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getWholeView() {
        return wholeView;
    }

    public ComRecyclerViewHolder setText(int viewId, String data) {
        TextView tv = getView(viewId);
        if (!TextUtils.isEmpty(data)) {
            tv.setText(data);
        } else {
            tv.setText("");
        }
        return this;
    }



    /**
     * 设置本地图片为背景
     */
    public ComRecyclerViewHolder setBackgroundResource(int viewId, int picId) {
        ImageView imageView = getView(viewId);
        imageView.setBackgroundResource(picId);
        return this;
    }

    /**
     * 加载网络图片
     */
    public ComRecyclerViewHolder loadImage(int viewId, String url) {
        ImageView imageView = getView(viewId);

        return this;
    }

    public ComRecyclerViewHolder loadImageToBackResource(int viewId, final String url) {
        ImageView imageView = getView(viewId);

        return this;
    }

    /**
     * 加载网络图片
     * 为使效果和其他医院列表一致专门写的方法
     * 只在导诊页医院列表使用
     */
    public ComRecyclerViewHolder loadImageToBackByTag(int viewId, final String url) {
        ImageView imageView = getView(viewId);

        return this;
    }

    /**
     * 加载网络图片
     * 裁剪成圆形
     */
    public ComRecyclerViewHolder loadRoundImageToBack(int viewId, final String url) {
        ImageView imageView = getView(viewId);

        return this;
    }

    /**
     * 加载本地图片
     * 不建议使用，重复创建Bitmap，耗内存情况较严重
     */
    public ComRecyclerViewHolder setBitmap(int viewId, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        ImageView imageView = getView(viewId);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        return this;
    }

    /**
     * 直接加载Bitmap
     * 建议使用（使用时应将多次重复使用的Bitmap作为成员变量在构造方法中初始化）
     */
    public ComRecyclerViewHolder setBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        return this;
    }

    /**
     * 设置字体颜色
     */
    public ComRecyclerViewHolder setTextColor(int viewId, int colorId) {
        TextView view = getView(viewId);
        view.setTextColor(colorId);
        return this;
    }

    /**
     * 设置背景颜色
     */
    public ComRecyclerViewHolder setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(context.getResources().getColor(colorId));
        return this;
    }

    /**
     * 设置View是否Gone
     */
    public ComRecyclerViewHolder setVisible(int viewId, boolean isVisible) {
        View view = getView(viewId);
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
        return this;
    }


}
