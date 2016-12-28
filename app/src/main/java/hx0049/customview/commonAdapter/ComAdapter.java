package hx0049.customview.commonAdapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hx on 2016/11/28.
 * 通用RecyclerView.Adapter
 */

public class ComAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_HEAD = 0;
    private final int TYPE_FOOT = 1;
    private final int TYPE_BODY = 2;
    private final int TYPE_EMPTY = 3;
    private Context context;
    private List<T> list;
    private int itemId;
    private int headId;
    private int footId;
    private int emptyId;
    private String notice = "No Data!";
    private String loadMore = "is loading...";
    private String loadDone = "load done!";
    private View emptyView = null;
    private ViewGroup loadView = null;
    private boolean haveFoot = false;
    private boolean haveHead = false;
    private boolean haveEmpty = false;
    private boolean isLoadMode = false;
    private long loadModeMaxSize = 0;
    private boolean canReadBottom = true;

    /**
     * 构造方法
     */
    public ComAdapter(Context context) {
        this.context = context;
    }

    /**
     * 初始化
     */
    public static ComAdapter with(Context context) {
        return new ComAdapter(context);
    }

    /**
     * 设置数据
     */
    public ComAdapter setData(List<T> list) {
        this.list = list;
        canReadBottom = true;
        return this;
    }

    /**
     * 设置ItemView的LayoutId
     */
    public ComAdapter setItemView(int itemId) {
        this.itemId = itemId;
        return this;
    }

    /**
     * 设置FootView的LayoutId
     */
    public ComAdapter setFootView(int footId) {
        this.footId = footId;
        haveFoot = true;
        return this;
    }

    /**
     * 设置HeadView的LayoutId
     */
    public ComAdapter setHeadView(int headId) {
        this.headId = headId;
        haveHead = true;
        return this;
    }


    /**
     * 设置EmptyView的LayoutId
     * 优先级较低
     */
    public ComAdapter setEmptyView(int emptyId) {
        this.emptyId = emptyId;
        haveEmpty = true;
        return this;
    }

    /**
     * 无数据提示语
     */
    public ComAdapter setNoDataNotice(String notice) {
        this.notice = notice;
        return this;
    }

    /**
     * load字符串
     */
    public ComAdapter setLoadString(String loadMore, String loadDone) {
        this.loadMore = loadMore;
        this.loadDone = loadDone;
        return this;
    }

    /**
     * 设置EmptyView
     * 优先级较高
     */
    public ComAdapter setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        haveEmpty = true;
        return this;
    }

    /**
     * 设置RecyclerView为垂直状态
     */
    public ComAdapter loadVertical(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(this);
        return this;
    }

    /**
     * 设置RecyclerView为水平状态
     */
    public ComAdapter loadHorizontal(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(this);
        return this;
    }

    /**
     * 设置RecyclerView为垂直网格状态
     */
    public ComAdapter loadGridVertical(RecyclerView rv, int spanCount) {
        rv.setLayoutManager(new GridLayoutManager(context, spanCount));
        rv.setAdapter(this);
        return this;
    }

    /**
     * 设置RecyclerView为水平网格状态
     */
    public ComAdapter loadGridHorizontal(RecyclerView rv, int spanCount) {
        rv.setLayoutManager(new GridLayoutManager(context, spanCount, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(this);
        return this;
    }

    /**
     * 设置RecyclerView为水平瀑布流
     */
    public ComAdapter loadStaggeredGridHorizontal(RecyclerView rv, int spanCount) {
        rv.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL));
        rv.setAdapter(this);
        return this;
    }

    /**
     * 设置RecyclerView为垂直瀑布流
     */
    public ComAdapter loadStaggeredGridVertical(RecyclerView rv, int spanCount) {
        rv.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(this);
        return this;
    }

    /**
     * footView以Load模式显示
     */
    public ComAdapter asLoadMode() {
        isLoadMode = true;
        haveFoot = true;
        loadView = new LinearLayout(context);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadView.setLayoutParams(params);
        TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(sp2px(10));
        tv.setPadding(0, dip2px(2), 0, dip2px(2));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadView.addView(tv, layoutParams);
        return this;
    }

    /**
     * 设置Load模式最大size
     */
    public ComAdapter setLoadModeMaxSize(long size) {
        this.loadModeMaxSize = size;
        this.notifyDataSetChanged();
        return this;
    }

    /**
     * 增加新数据
     */
    public ComAdapter setExtraData(List<T> extraData) {
        canReadBottom = true;
        if (list == null) {
            list = extraData;
        } else {
            list.addAll(extraData);
        }
        this.notifyDataSetChanged();
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        if (isDataEmpty()) {
            if (haveHead) {
                return TYPE_HEAD;
            } else {
                return TYPE_EMPTY;
            }
        } else {
            if (haveHead) {
                if (position == 0) {
                    return TYPE_HEAD;
                } else if (haveFoot && position == list.size() + 1) {
                    return TYPE_FOOT;
                } else {
                    return TYPE_BODY;
                }
            } else {
                if (haveFoot && position == list.size()) {
                    return TYPE_FOOT;
                } else {
                    return TYPE_BODY;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isDataEmpty()) {
            if (haveHead) {
                return 1;
            } else if (haveEmpty) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (haveHead) {
                if (haveFoot) {
                    return list.size() + 2;
                } else {
                    return list.size() + 1;
                }
            } else {
                if (haveFoot) {
                    return list.size() + 1;
                } else {
                    return list.size();
                }
            }
        }
    }

    /**
     * list是否无数据
     */
    private boolean isDataEmpty() {
        if (list == null) {
            return true;
        } else if (list.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            if (emptyView != null) {
                return new ComRecyclerViewHolder(context, emptyView);
            } else {
                return ComRecyclerViewHolder.getComRecyclerViewHolder(context, emptyId, parent);
            }
        } else if (viewType == TYPE_HEAD) {
            return ComRecyclerViewHolder.getComRecyclerViewHolder(context, headId, parent);
        } else if (viewType == TYPE_BODY) {
            return ComRecyclerViewHolder.getComRecyclerViewHolder(context, itemId, parent);
        } else if (viewType == TYPE_FOOT) {
            if (isLoadMode) {
                return new ComRecyclerViewHolder(context, loadView);
            } else {
                return ComRecyclerViewHolder.getComRecyclerViewHolder(context, footId, parent);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ComRecyclerViewHolder viewHolder = (ComRecyclerViewHolder) holder;
        if (viewType == TYPE_EMPTY) {
            View view = emptyView;
            if (view == null) {
                view = viewHolder.getWholeView();
            }
            showNotice(view);
        } else if (viewType == TYPE_HEAD) {
            if (showHead != null) {
                showHead.show(viewHolder);
            }
        } else if (viewType == TYPE_BODY) {
            final T t = list.get(haveHead ? position - 1 : position);
            if (showItem != null) {
                showItem.show(viewHolder, t);
            }
            viewHolder.getWholeView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(holder.getAdapterPosition(), t);
                    }
                }
            });
            viewHolder.getWholeView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemClick(holder.getAdapterPosition(), t);
                    }
                    return true;
                }
            });
        } else if (viewType == TYPE_FOOT) {
            if (isLoadMode) {
                showLoadString(viewHolder.getWholeView());
            } else {
                if (showFoot != null) {
                    showFoot.show(viewHolder);
                }
            }
            if (canReadBottom) {
                canReadBottom = false;
                if (onBottomLister != null) {
                    onBottomLister.onBottom();
                }
            }
        }
    }

    /**
     * 显示load提示字符串
     */
    private void showLoadString(View wholeView) {
        TextView tv = getTextViewFromView(wholeView);
        if (tv == null || list == null) {
            return;
        }
        if (list.size() != loadModeMaxSize) {
            tv.setText(loadMore);
        } else {
            tv.setText(loadDone);
        }
    }

    /**
     * 显示无数据提示字符串
     */
    private void showNotice(View view) {
        TextView tv = getTextViewFromView(view);
        if (tv != null) {
            tv.setText(notice);
        }
    }

    /**
     * 从View中获取第一个textView
     */
    private TextView getTextViewFromView(View view) {
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = ((ViewGroup) view).getChildAt(i);
                if (childView instanceof TextView) {
                    return (TextView) childView;
                } else if (childView instanceof ViewGroup) {
                    TextView tv = getTextViewFromView(childView);
                    if (tv != null) {
                        return tv;
                    }
                }
            }
        } else if (view instanceof TextView) {
            return (TextView) view;
        }
        return null;
    }

    /**
     * 点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }

    private OnItemClickListener onItemClickListener = null;

    public ComAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    /**
     * 长按事件
     */
    public interface OnItemLongClickListener {
        void onItemClick(int position, Object o);
    }

    private OnItemLongClickListener onItemLongClickListener = null;

    public ComAdapter setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
        return this;
    }

    /**
     * 底部监听
     */
    public interface OnBottomLister {
        void onBottom();
    }

    private OnBottomLister onBottomLister = null;

    public ComAdapter setOnBottomLister(OnBottomLister onBottomLister) {
        this.onBottomLister = onBottomLister;
        return this;
    }

    /**
     * item布局
     */
    public interface ShowItem {
        void show(ComRecyclerViewHolder viewHolder, Object object);
    }

    private ShowItem showItem = null;

    public ComAdapter setShowItem(ShowItem showItem) {
        this.showItem = showItem;
        return this;
    }

    /**
     * head布局
     */
    public interface ShowHead {
        void show(ComRecyclerViewHolder viewHolder);
    }

    private ShowHead showHead = null;

    public ComAdapter setShowHead(ShowHead showHead) {
        this.showHead = showHead;
        return this;
    }

    /**
     * foot布局
     */
    public interface ShowFoot {
        void show(ComRecyclerViewHolder viewHolder);
    }

    private ShowFoot showFoot = null;

    public ComAdapter setShowFoot(ShowFoot showFoot) {
        this.showFoot = showFoot;
        return this;
    }

    /**
     * 单位转换工具类
     */
    private float sp2px(int spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,context.getResources().getDisplayMetrics());
        return (int) (spValue * fontScale + 0.5f);
    }

    private int px2sp(float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    private int dip2px(float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 补充支持：gridLayout支持HeadView and FootView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_HEAD || getItemViewType(position) == TYPE_FOOT || getItemViewType(position) == TYPE_EMPTY) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    /**
     * 补充支持：staggeredGridLayout支持HeadView and FootView
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams params = ((ComRecyclerViewHolder) holder).getWholeView().getLayoutParams();
        if (params != null && params instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) params;
            int position = holder.getLayoutPosition();
            if (getItemViewType(position) == TYPE_HEAD || getItemViewType(position) == TYPE_FOOT || getItemViewType(position) == TYPE_EMPTY) {
                lp.setFullSpan(true);
            }
        }
    }

}
