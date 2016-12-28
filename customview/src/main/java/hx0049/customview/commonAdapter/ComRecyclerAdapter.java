package hx0049.customview.commonAdapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hx on 2016/7/7.
 * 使用说明：继承此Adapter，必须重写构造方法和以下3个方法
 * convert(item布局赋值),convert_head(head布局赋值),convert_foot(foot布局赋值)
 * 不需要有head布局或foot布局：对应的方法体直接置为空
 * 需要head布局或foot布局：调用setHeaderView或setFootView方法，并补充对应的方法体
 * 需要设置没有数据时显示EmptyView：调用setEmptyView
 */
public abstract class ComRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "ComRecyclerAdapter";
    //四种ViewType
    public static final int TYPE_FOOTER = 0;
    public static final int TYPE_BODY = 1;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_EMPTY = 3;
    //上下文
    protected Context context;
    //数据集合
    public List<T> list;
    //四种布局Id：item,head,foot,empty
    public int layoutId;
    public int layoutId_head;
    public int layoutId_foot;
    public int layoutId_empty = -1;
    //提示语
    public String notice;
    //是否有head布局，foot布局
    public boolean isHaveHeadView = false;
    public boolean isHaveFootView = false;
    //设置是否监听recyclerView到底部
    public boolean canNotReadBottom = true;

    /**
     * 构造方法
     * 参数：上下文，数据，item布局Id
     */
    public ComRecyclerAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    /**
     * 设置无数据时EmptyView
     * 参数：emptyView布局Id
     */
    public void setEmptyView(int layoutId_empty) {
        this.layoutId_empty = layoutId_empty;
        notice = "No Data!!!";
    }

    /**
     * 设置无数据时EmptyView
     * 参数：emptyView布局Id，提示语
     */
    public void setEmptyView(int layoutId_empty, String notice) {
        this.layoutId_empty = layoutId_empty;
        if (!TextUtils.isEmpty(notice)) {
            this.notice = notice;
        }
    }

    /**
     * 判断数据是否为空
     */
    public boolean isDataEmpty() {
        //判断是否有数据
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否有EmptyView
     */
    public boolean isHaveEmptyView() {
        //是否设置了EmptyView
        if (layoutId_empty == -1) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 设置是否监听底部
     */
    public void setCanNotReadBottom(boolean canNotReadBottom) {
        this.canNotReadBottom = canNotReadBottom;
    }

    public boolean isCanNotReadBottom() {
        return canNotReadBottom;
    }

    /**
     * 设置Head布局
     */
    public void setHeadViewId(int layoutId) {
        if (layoutId > 0) {
            isHaveHeadView = true;
            layoutId_head = layoutId;
        }
    }

    /**
     * 设置Foot布局
     */
    public void setFootViewId(int layoutId) {
        if (layoutId > 0) {
            isHaveFootView = true;
            layoutId_foot = layoutId;
        }
    }


    /**
     * 根据position设置ViewType
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isHaveHeadView) {
            return TYPE_HEADER;
        }
        if (isDataEmpty()) {
            return TYPE_EMPTY;
        }
        if (isHaveHeadView) {
            if (position == 0) {
                return TYPE_HEADER;
            }
        }
        if (isHaveFootView) {
            if (isHaveHeadView) {
                if (position == list.size() + 1) {
                    return TYPE_FOOTER;
                }
            } else {
                if (position == list.size()) {
                    return TYPE_FOOTER;
                }
            }
        }
        return TYPE_BODY;
    }

    /**
     * 根据ViewType创建View
     * 此方法只执行一次
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            //EmptyView布局
            if (!isHaveEmptyView()) {
                throw new NullPointerException("ComRecyclerAdapter: EmptyView must not be null!");
            }
            ComRecyclerViewHolder comRecyclerViewHolder = ComRecyclerViewHolder.getComRecyclerViewHolder(context, layoutId_empty, parent);
            return comRecyclerViewHolder;

        } else if (viewType == TYPE_HEADER) {
            //headView布局
            ComRecyclerViewHolder comRecyclerViewHolder = ComRecyclerViewHolder.getComRecyclerViewHolder(context, layoutId_head, parent);
            return comRecyclerViewHolder;
        } else if (viewType == TYPE_BODY) {
            ComRecyclerViewHolder comRecyclerViewHolder = ComRecyclerViewHolder.getComRecyclerViewHolder(context, layoutId, parent);
            return comRecyclerViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            //footView布局
            ComRecyclerViewHolder comRecyclerViewHolder = ComRecyclerViewHolder.getComRecyclerViewHolder(context, layoutId_foot, parent);
            return comRecyclerViewHolder;
        }
        return null;
    }

    /**
     * 根据位置给各个布局赋值
     * 循环复用，多次执行
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_EMPTY) {
            ComRecyclerViewHolder comRecyclerViewHolder = (ComRecyclerViewHolder) holder;
            convertEmpty(comRecyclerViewHolder);
        } else if (getItemViewType(position) == TYPE_HEADER) {
            ComRecyclerViewHolder comRecyclerViewHolder = (ComRecyclerViewHolder) holder;
            //赋值
            convertHeader(comRecyclerViewHolder);
        } else if (getItemViewType(position) == TYPE_BODY) {
            ComRecyclerViewHolder comRecyclerViewHolder = (ComRecyclerViewHolder) holder;
            if (!isHaveHeadView) {
                convert(comRecyclerViewHolder, list.get(position));
                comRecyclerViewHolder.getWholeView().setTag(position);
            } else {
                convert(comRecyclerViewHolder, list.get(position - 1));
                comRecyclerViewHolder.getWholeView().setTag(position - 1);
            }
            comRecyclerViewHolder.getWholeView().setOnClickListener(this);
            comRecyclerViewHolder.getWholeView().setOnLongClickListener(this);
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            ComRecyclerViewHolder comRecyclerViewHolder = (ComRecyclerViewHolder) holder;
            //赋值
            if (!canNotReadBottom) {
                if (onRecyclerViewBottomListener != null) {
                    onRecyclerViewBottomListener.OnBottom();
                }
            }
            convertFooter(comRecyclerViewHolder);
        }

    }

    /**
     * EmptyView默认设置提示语方法
     */
    public void convertEmpty(ComRecyclerViewHolder comRecyclerViewHolder) {
        try {
            ViewGroup viewGroup = (ViewGroup) comRecyclerViewHolder.getWholeView();
            TextView tv = getTextViewFromView(viewGroup);
            if(tv!=null){
                tv.setText(notice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextView getTextViewFromView(View view){
        if(view instanceof ViewGroup){
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = ((ViewGroup) view).getChildAt(i);
                if(childView instanceof TextView){
                    return (TextView) childView;
                }else if(childView instanceof ViewGroup){
                    TextView tv=getTextViewFromView(childView);
                    if(tv!=null){
                        return tv;
                    }
                }
            }
        }else if(view instanceof TextView){
            return (TextView) view;
        }
        return null;
    }

    /**
     * Item布局赋值方法，需要重写
     */
    public abstract void convert(ComRecyclerViewHolder comRecyclerViewHolder, T t);

    /**
     * Head布局赋值方法，按需要重写
     */
    public void convertHeader(ComRecyclerViewHolder comRecyclerViewHolder) {

    }

    /**
     * Foot布局赋值方法，按需要重写
     */
    public void convertFooter(ComRecyclerViewHolder comRecyclerViewHolder) {

    }

    /**
     * 总Item个数
     */
    @Override
    public int getItemCount() {
        if (isDataEmpty()) {
            if (isHaveHeadView) {
                return 1;
            } else if (isHaveEmptyView()) {
                return 1;
            } else {
                return 0;
            }
        }
        if (isHaveHeadView) {
            if (isHaveFootView) {
                return list.size() + 2;
            } else {
                return list.size() + 1;
            }
        } else {
            if (isHaveFootView) {
                return list.size() + 1;
            } else {
                return list.size();
            }
        }
    }


    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public OnItemClickListener listener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v, (int) v.getTag());
        }
    }

    /**
     * item长按事件
     */
    public interface OnItemLongClickListener {
        void OnLongClick(View view, int position);
    }

    public OnItemLongClickListener longClickListener = null;

    @Override
    public boolean onLongClick(View v) {
        if (longClickListener != null) {
            longClickListener.OnLongClick(v, (int) v.getTag());
        }
        return true;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    /**
     * 监听到recyclerView底部的监听器
     */
    public interface OnRecyclerViewBottomListener {
        void OnBottom();
    }

    public OnRecyclerViewBottomListener onRecyclerViewBottomListener = null;

    public void setOnRecyclerViewBottomListener(OnRecyclerViewBottomListener onRecyclerViewBottomListener) {
        this.onRecyclerViewBottomListener = onRecyclerViewBottomListener;
    }

}