package hx0049.customviewdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hx0049.customview.view.expandableview.ExpandableRecyclerAdapter;
import hx0049.customview.view.expandableview.model.ExpandableModel;
import hx0049.customview.view.slidedelete.SlideAdapter;
import hx0049.customview.view.slidedelete.SlideMenu;
import hx0049.customview.viewgroup.ActionSheetUtils;

/**
 * Created by hx on 2016/12/26.
 */

public class TestActivity extends Activity {

    Random random = new Random();
    private List<String> data1;

    public void test(View v) {
        ActionSheetUtils actionSheetUtils = ActionSheetUtils.getInstance(this);
        actionSheetUtils.showView(findViewById(R.id.rv).getRootView(), "happy", "sad", "cry", "laugh");
        actionSheetUtils.setOnActionSheetClickListener(new ActionSheetUtils.OnActionSheetClickListener() {
            @Override
            public void onClick(int position, String value) {
                Toast.makeText(TestActivity.this, "position = " + position + " " + value, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //--------------可展开的RecyclerView
//        MyAdapter adapter = new MyAdapter(getApplicationContext(), getData());
//        adapter.setOnItemClickListener(new ExpandableRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(ExpandableModel model, int position) {
//                Toast.makeText(TestActivity.this, (String) (model.getChildList().get(position)), Toast.LENGTH_SHORT).show();
//            }
//        });
//        recyclerView.setAdapter(adapter);

        //--------------可侧滑删除的RecyclerView
        final MyAdapter3 adapter3 = new MyAdapter3(this, getData1());
        recyclerView.setAdapter(adapter3);
        adapter3.setDeleteListener(new SlideAdapter.DeleteListener() {
            @Override
            public void delete(int position) {
                adapter3.list.remove(position);
            }
        });


    }



    public List<ExpandableModel> getData() {
        List<ExpandableModel> result = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            List<String> data = new ArrayList<>();
            for (int j = 0; j < 1 + random.nextInt(4); j++) {
                data.add("第" + i + "类 " + "第" + j + "子数据");
            }
            result.add(new ExpandableModel<String>("第" + i + "大类", data));
        }
        return result;
    }

    public List<String> getData1() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 63; i++) {
            result.add("第" + i + "大类");
        }
        return result;
    }

    class MyAdapter3 extends SlideAdapter {
        Context context;
        List<String> list;
        public MyAdapter3(Context context, List<String> list) {
            this.context = context;
            this.list = list;

        }
        @Override
        protected SlideAdapter.SlideViewHolder CreateSlideViewHolder(SlideMenu slideMenu) {
            return new MySlideViewHolder(slideMenu);
        }

        @Override
        protected int getItemLayoutId() {
            return  R.layout.activity_test_item;
        }

        @Override
        protected int getMenuWidthPixel() {
            return 200;
        }

        @Override
        public void onBindViewHolder(SlideAdapter.SlideViewHolder holder, int position) {
            MySlideViewHolder viewHolder=(MySlideViewHolder) holder;
            viewHolder.textView.setText(list.get(position));
            viewHolder.textView.setTextColor(Color.BLUE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MySlideViewHolder extends SlideViewHolder {
            TextView textView;

            public MySlideViewHolder(View itemView) {
                super(itemView);
                textView = (TextView)itemView.findViewById(R.id.text1);
            }
        }
    }



    class MyAdapter extends ExpandableRecyclerAdapter {

        public MyAdapter(Context context, List<ExpandableModel> dataList) {
            super(context, dataList);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateParentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.select_dialog_item, parent, false);
            return new ParentViewHolder(view);
        }


        @Override
        protected void onBindChildViewHolder(RecyclerView.ViewHolder holder, int index, int childIndex) {
            ChildViewHolder viewHolder = (ChildViewHolder) holder;
            viewHolder.tvChild.setText((String) (dataList.get(index).getChildList().get(childIndex)));
        }

        @Override
        protected void onBindParentViewHolder(RecyclerView.ViewHolder holder, int index) {
            ParentViewHolder viewHolder = (ParentViewHolder) holder;
            viewHolder.tvParent.setText(dataList.get(index).getName());
        }

        class ParentViewHolder extends RecyclerView.ViewHolder {
            TextView tvParent;

            public ParentViewHolder(View itemView) {
                super(itemView);
                tvParent = (TextView) itemView.findViewById(android.R.id.text1);
                tvParent.setTextColor(Color.BLUE);
            }
        }

        class ChildViewHolder extends RecyclerView.ViewHolder {
            TextView tvChild;

            public ChildViewHolder(View itemView) {
                super(itemView);
                tvChild = (TextView) itemView.findViewById(android.R.id.text1);
                tvChild.setTextColor(Color.DKGRAY);
            }
        }
    }

}
