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
 */
public abstract class ComRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "ComRecyclerAdapter";
    //four ViewType
    public static final int TYPE_FOOTER = 0;
    public static final int TYPE_BODY = 1;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_EMPTY = 3;
    //context
    protected Context context;
    //data
    public List<T> list;
    //four Id：item,head,foot,empty
    public int layoutId;
    public int layoutId_head;
    public int layoutId_foot;
    public int layoutId_empty = -1;
    //tips
    public String notice;
    //has head,foot or not
    public boolean isHaveHeadView = false;
    public boolean isHaveFootView = false;
    //set recyclerView bottom listener work or not
    public boolean canNotReadBottom = true;

    /**
     * Constructor
     * parameters: context data itemLayout id
     */
    public ComRecyclerAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    /**
     * set no data EmptyViewId
     */
    public void setEmptyView(int layoutId_empty) {
        this.layoutId_empty = layoutId_empty;
        notice = "No Data!!!";
    }

    /**
     * set no data EmptyViewId and string
     */
    public void setEmptyView(int layoutId_empty, String notice) {
        this.layoutId_empty = layoutId_empty;
        if (!TextUtils.isEmpty(notice)) {
            this.notice = notice;
        }
    }

    /**
     * judge the data is empty or not
     */
    public boolean isDataEmpty() {
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * has EmptyView or not
     */
    public boolean isHaveEmptyView() {
        if (layoutId_empty == -1) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * set bottomListener work or not
     */
    public void setCanNotReadBottom(boolean canNotReadBottom) {
        this.canNotReadBottom = canNotReadBottom;
    }

    public boolean isCanNotReadBottom() {
        return canNotReadBottom;
    }

    /**
     * set Head layout id
     */
    public void setHeadViewId(int layoutId) {
        if (layoutId > 0) {
            isHaveHeadView = true;
            layoutId_head = layoutId;
        }
    }

    /**
     * set Foot layout id
     */
    public void setFootViewId(int layoutId) {
        if (layoutId > 0) {
            isHaveFootView = true;
            layoutId_foot = layoutId;
        }
    }


    /**
     *  get ViewType from position
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
     * create view by ViewType
     * only run when need create
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            //EmptyView layout
            if (!isHaveEmptyView()) {
                throw new NullPointerException("ComRecyclerAdapter: EmptyView must not be null!");
            }
            ComRecyclerViewHolder comRecyclerViewHolder = ComRecyclerViewHolder.getComRecyclerViewHolder(context, layoutId_empty, parent);
            return comRecyclerViewHolder;

        } else if (viewType == TYPE_HEADER) {
            //headView layout
            ComRecyclerViewHolder comRecyclerViewHolder = ComRecyclerViewHolder.getComRecyclerViewHolder(context, layoutId_head, parent);
            return comRecyclerViewHolder;
        } else if (viewType == TYPE_BODY) {
            ComRecyclerViewHolder comRecyclerViewHolder = ComRecyclerViewHolder.getComRecyclerViewHolder(context, layoutId, parent);
            return comRecyclerViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            //footView layout
            ComRecyclerViewHolder comRecyclerViewHolder = ComRecyclerViewHolder.getComRecyclerViewHolder(context, layoutId_foot, parent);
            return comRecyclerViewHolder;
        }
        return null;
    }

    /**
     * get the viewHolder for position and bind data
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_EMPTY) {
            ComRecyclerViewHolder comRecyclerViewHolder = (ComRecyclerViewHolder) holder;
            convertEmpty(comRecyclerViewHolder);
        } else if (getItemViewType(position) == TYPE_HEADER) {
            ComRecyclerViewHolder comRecyclerViewHolder = (ComRecyclerViewHolder) holder;
            //bind data
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
            //bind data
            if (!canNotReadBottom) {
                if (onRecyclerViewBottomListener != null) {
                    onRecyclerViewBottomListener.OnBottom();
                }
            }
            convertFooter(comRecyclerViewHolder);
        }

    }

    /**
     * make EmptyView show the right string
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
     * ItemLayout  bind data
     */
    public abstract void convert(ComRecyclerViewHolder comRecyclerViewHolder, T t);

    /**
     * HeadLayout bind data
     */
    public void convertHeader(ComRecyclerViewHolder comRecyclerViewHolder) {

    }

    /**
     * FootLayout bind data
     */
    public void convertFooter(ComRecyclerViewHolder comRecyclerViewHolder) {

    }

    /**
     * all Item number
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
     * item click event
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
     * item long click event
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
     * recyclerView bottom listener
     */
    public interface OnRecyclerViewBottomListener {
        void OnBottom();
    }

    public OnRecyclerViewBottomListener onRecyclerViewBottomListener = null;

    public void setOnRecyclerViewBottomListener(OnRecyclerViewBottomListener onRecyclerViewBottomListener) {
        this.onRecyclerViewBottomListener = onRecyclerViewBottomListener;
    }

}