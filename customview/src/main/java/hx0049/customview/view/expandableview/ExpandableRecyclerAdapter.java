package hx0049.customview.view.expandableview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hx0049.customview.view.expandableview.model.ExpandableModel;

/**
 * Created by hx on 2016/12/26.
 */

public abstract class ExpandableRecyclerAdapter extends RecyclerView.Adapter {
    public static final int TYPE_PARENT = 0;
    public static final int TYPE_CHILD = 1;

    private Context context;
    protected List<ExpandableModel> dataList;
    private SparseBooleanArray shrinkState;

    public ExpandableRecyclerAdapter(Context context, List<ExpandableModel> dataList) {
        this.context = context;
        this.dataList = dataList;
        shrinkState = new SparseBooleanArray();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PARENT) {
            return onCreateParentViewHolder(parent);
        } else if (viewType == TYPE_CHILD) {
            return onCreateChildViewHolder(parent);
        }
        return null;

    }

    protected abstract RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder onCreateParentViewHolder(ViewGroup parent);




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int index = getIndexFromPosition(position);
        final int childIndex = getChildIndexFromPosition(position);
        if (getItemViewType(position) == TYPE_PARENT) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shrinkState.get(index)) {
                        shrinkState.put(index, false);
                    } else {
                        shrinkState.put(index, true);
                    }
                    notifyDataSetChanged();
//                    notifyItemInserted(1);
                }
            });
            onBindParentViewHolder(holder,index);
        } else if (getItemViewType(position) == TYPE_CHILD) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(dataList.get(index),childIndex);
                    }
                }
            });
            onBindChildViewHolder(holder,index,childIndex);
        }
    }

    protected abstract void onBindChildViewHolder(RecyclerView.ViewHolder holder, int index, int childIndex);

    protected abstract void onBindParentViewHolder(RecyclerView.ViewHolder holder, int index);


    public int getChildIndexFromPosition(int position) {
        for (int i = 0; i < dataList.size(); i++) {
            int childSize = dataList.get(i).getChildList().size();
            if (position == 0) {
                return -1;
            } else if (shrinkState.get(i)) {
                if (position <= childSize) {
                    return position - 1;
                } else {
                    position -= (childSize + 1);
                }
            } else {
                position -= 1;
            }

        }
        return -1;
    }

    public int getIndexFromPosition(int position) {
        for (int i = 0; i < dataList.size(); i++) {
            int childSize = dataList.get(i).getChildList().size();
            if (position == 0) {
                return i;
            } else if (shrinkState.get(i)) {
                if (position <= childSize) {
                    return i;
                } else {
                    position -= (childSize + 1);
                }
            } else {
                position -= 1;
            }

        }
        return -1;
    }


    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < dataList.size(); i++) {
            int childSize = dataList.get(i).getChildList().size();
            if (position == 0) {
                return TYPE_PARENT;
            } else if (shrinkState.get(i)) {
                if (position <= childSize) {
                    return TYPE_CHILD;
                } else {
                    position -= (childSize + 1);
                }
            } else {
                position -= 1;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        int count = dataList.size();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (shrinkState.get(i)) {
                count += dataList.get(i).getChildList().size();
            }
        }
        return count;
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(ExpandableModel model,int position);
    }


}
