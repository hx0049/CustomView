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
     * set local resource picture to background
     */
    public ComRecyclerViewHolder setBackgroundResource(int viewId, int picId) {
        ImageView imageView = getView(viewId);
        imageView.setBackgroundResource(picId);
        return this;
    }

    /**
     * load the picture from internet
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
     * load the picture from internet
     * and clip to round
     */
    public ComRecyclerViewHolder loadRoundImageToBack(int viewId, final String url) {
        ImageView imageView = getView(viewId);

        return this;
    }

    /**
     * load the picture from resource as bitmap
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
     * load the bitmap to imageView
     */
    public ComRecyclerViewHolder setBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        return this;
    }

    /**
     * set text color
     */
    public ComRecyclerViewHolder setTextColor(int viewId, int colorId) {
        TextView view = getView(viewId);
        view.setTextColor(colorId);
        return this;
    }

    /**
     * set background color
     */
    public ComRecyclerViewHolder setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(context.getResources().getColor(colorId));
        return this;
    }

    /**
     * set view gone or not
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
