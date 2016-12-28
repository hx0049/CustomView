package hx0049.customview.view.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hx0049.customview.R;
import hx0049.customview.view.calendar.model.DateModel;

/**
 * Created by hx on 2016/12/23.
 */

public class CalendarRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<DateModel> list;
    private Context context;

    public CalendarRecyclerAdapter(List<DateModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setData(List<DateModel> list){
        this.list = list;
        this.notifyDataSetChanged();
    }
    public List<DateModel> getData(){
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_view_item,parent,false);
        return new CalenderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CalenderViewHolder viewHolder  = (CalenderViewHolder)holder;
        viewHolder.pointView.setVisibility(View.GONE);
        DateModel model = list.get(position);
        if(model.isHasThingToDeal()){
            viewHolder.pointView.setVisibility(View.VISIBLE);
        }else{
            viewHolder.pointView.setVisibility(View.INVISIBLE);
        }
        if(model.isCurrentMonth()){
            viewHolder.tvNumber.setTextColor(Color.BLACK);
        }else{
            viewHolder.tvNumber.setTextColor(Color.GRAY);
        }
        viewHolder.tvNumber.setText(model.getNumberForShow());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CalenderViewHolder extends RecyclerView.ViewHolder{
        TextView tvNumber;
        View pointView;
        public CalenderViewHolder(View itemView) {
            super(itemView);
            tvNumber = (TextView)itemView.findViewById(R.id.tv_number);
            pointView = itemView.findViewById(R.id.v_point);
        }
    }

}