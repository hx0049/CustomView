package hx0049.customview.view.slidedelete;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hx on 2016/12/28.
 */

public abstract class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> implements OpenOrCloseCallBack {
    private List<SlideMenu> slideMenuList = new ArrayList<>();

    @Override
    public SlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SlideMenu slideMenu = new SlideMenu(parent.getContext());
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(), parent, false);
        layout.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv = new TextView(parent.getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setText("delete");
        layout.addView(tv, new LinearLayout.LayoutParams(getMenuWidthPixel(), ViewGroup.LayoutParams.MATCH_PARENT));
        slideMenu.addView(layout);
        slideMenu.setOpenOrCloseCallBack(this);
        slideMenuList.add(slideMenu);
        slideMenu.setRightLength(getMenuWidthPixel());
        return CreateSlideViewHolder(slideMenu);
    }

    protected abstract SlideViewHolder CreateSlideViewHolder(SlideMenu slideMenu);

    protected abstract int getItemLayoutId();

    protected abstract int getMenuWidthPixel();

    @Override
    public void getOpenState(boolean isOpen, SlideMenu slideMenu) {
        if (isOpen) {
            for (int i = slideMenuList.size() - 1; i >= 0; i--) {
                if (slideMenu != slideMenuList.get(i)) {
                    slideMenuList.get(i).closeMenu();
                }
            }
        }
    }



    private int rawY = 0;
    private boolean isFirst = true;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(!isFirst){
            return;
        }
        isFirst = false;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        rawY = (int) event.getRawY();
                       break;
                    case MotionEvent.ACTION_MOVE:
                      if(Math.abs(rawY-event.getRawY())>((RecyclerView)v).getChildAt(0).getMeasuredHeight()/2){
                          for (int i = slideMenuList.size() - 1; i >= 0; i--) {
                              if(slideMenuList.get(i).isMenuOpen()) {
                                  slideMenuList.get(i).closeQuietly();
                              }
                          }
                      }
                        Log.d("------", rawY + "  : "+(int)event.getRawY());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("------up", rawY + "  : "+(int)event.getRawY());
                        break;
                }
                return false;
            }
        });
    }

    public abstract class SlideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView tvDelete;
        public SlideViewHolder(View itemView) {
            super(itemView);
            tvDelete = (TextView)((LinearLayout)((SlideMenu)itemView).getChildAt(0)).getChildAt(1);
            this.itemView.setOnClickListener(this);
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteListener!=null){
                        deleteListener.delete(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface DeleteListener{
        void delete(int position);
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private DeleteListener deleteListener = null;
    private OnItemClickListener onItemClickListener=null;

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
