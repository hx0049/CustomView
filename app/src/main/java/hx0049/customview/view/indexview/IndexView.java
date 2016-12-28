package hx0049.customview.view.indexview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hx0049.customview.R;
import hx0049.customview.view.indexview.model.StringModel;

/**
 * Created by hx on 2016/12/19.
 * IndexView
 */

public class IndexView extends LinearLayout implements SideBar.OnSelectListener, TextWatcher {
    private boolean showAllLetter = true;
    private boolean showSearchView = true;
    private boolean showCenterLetter = true;
    private boolean isFilterMode = false;
    private RecyclerView recyclerView;
    private SideBar sideBar;
    private EditText editText;
    private RelativeLayout rlCenter;
    private TextView tvCenter;
    private List<StringModel> modelList;
    private List<StringModel> filterList;
    private IndexAdapter adapter;
    private Comparator<StringModel> comparator = new Comparator<StringModel>() {
        @Override
        public int compare(StringModel o1, StringModel o2) {
            if (o1.getFirstLetter() == '#') {
                return 1;
            } else if (o2.getFirstLetter() == '#') {
                return -1;
            }
            if (o1.getFirstLetter() > o2.getFirstLetter()) {
                return 1;
            } else if (o1.getFirstLetter()< o2.getFirstLetter()) {
                return -1;
            } else {
                return o1.getPingYin().toLowerCase().compareTo(o2.getPingYin().toLowerCase());
            }
        }
    };


    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.IndexView);
        showAllLetter = ta.getBoolean(R.styleable.IndexView_show_all_letter, true);
        showSearchView = ta.getBoolean(R.styleable.IndexView_show_search_view, true);
        showCenterLetter = ta.getBoolean(R.styleable.IndexView_show_center_letter, true);
        ta.recycle();

        //Inflater的
        View view = LayoutInflater.from(getContext()).inflate(R.layout.index_view, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        sideBar = (SideBar) view.findViewById(R.id.side_view);
        editText = (EditText) view.findViewById(R.id.edit_text);
        rlCenter = (RelativeLayout) view.findViewById(R.id.rl_center);
        tvCenter = (TextView) view.findViewById(R.id.tv_center);
        sideBar.setOnSelectListener(this);
        editText.addTextChangedListener(this);

        modelList = new ArrayList<>();
        filterList = new ArrayList<>();
        adapter = new IndexAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (!showSearchView) {
            editText.setVisibility(GONE);
        }
    }

    public void setData(List<String> stringList) {
        //清空无效字符串
        for (int i = stringList.size() - 1; i > 0; i--) {
            if (TextUtils.isEmpty(stringList.get(i))) {
                stringList.remove(i);
            }
        }
        //清除EditText
        editText.setText("");
        //设置为非过滤状态
        isFilterMode = false;
        //初始赋值
        for (int i = stringList.size() - 1; i > 0; i--) {
            modelList.add(0, new StringModel(stringList.get(i)));
        }
        //数组排序
        Collections.sort(modelList, comparator);
        //获取首字母位置 && 首字母个数
        int charLength = 0;
        for (int i = 0, len = modelList.size(); i < len; i++) {
            if (i == 0) {
                modelList.get(i).setFirstPosition(true);
                charLength++;
            } else {
                if (modelList.get(i).getFirstLetter() == modelList.get(i - 1).getFirstLetter()) {
                    modelList.get(i).setFirstPosition(false);
                } else {
                    modelList.get(i).setFirstPosition(true);
                    charLength++;
                }
            }
        }
        //是否显示全部字母
        if (!showAllLetter) {
            char[] newLetters = new char[charLength];
            for (int i = 0, j = 0, len = modelList.size(); i < len; i++) {
                if (modelList.get(i).isFirstPosition()) {
                    newLetters[j] = modelList.get(i).getFirstLetter();
                    j++;
                }
            }
            sideBar.setLetters(newLetters);
        }
        //通知Adapter更新
        adapter.notifyDataSetChanged();
    }

    /**
     * 根据value过滤数据
     */
    public void setFilterData(String value) {
        //设置为过滤模式
        isFilterMode = true;
        //过滤数据
        for (int i = modelList.size() - 1; i >= 0; i--) {
            if (modelList.get(i).getContent().toLowerCase().indexOf(value.toLowerCase()) == 0 ||
                    modelList.get(i).getPingYin().toLowerCase().indexOf(value.toLowerCase()) == 0) {
                filterList.add(0, modelList.get(i));
            }
        }
        //排序
        Collections.sort(filterList, comparator);
        //获取首字母位置 && 首字母数量
        int charLength = 0;
        for (int i = 0, len = filterList.size(); i < len; i++) {
            if (i == 0) {
                filterList.get(i).setFirstPosition(true);
                charLength++;
            } else {
                if (filterList.get(i).getFirstLetter() == filterList.get(i - 1).getFirstLetter()) {
                    filterList.get(i).setFirstPosition(false);
                } else {
                    filterList.get(i).setFirstPosition(true);
                    charLength++;
                }
            }
        }
        //是否显示全部字母
        if (!showAllLetter) {
            char[] newLetters = new char[charLength];
            for (int i = 0, j = 0, len = filterList.size(); i < len; i++) {
                if (filterList.get(i).isFirstPosition()) {
                    newLetters[j] = filterList.get(i).getFirstLetter();
                    j++;
                }
            }
            sideBar.setLetters(newLetters);
        }
        //通知Adapter更新
        adapter.notifyDataSetChanged();
    }

    /**
     * 滑动到对应字母的位置
     */
    public void rollPositionForChar(char letter) {
        for (int i = 0, len = modelList.size(); i < len; i++) {
            if (modelList.get(i).isFirstPosition() && modelList.get(i).getFirstLetter() == letter + 32) {
                recyclerView.smoothScrollToPosition(i);
                break;
            }
        }
    }

    /**
     * 当对应的右侧sidebar字母被按下的显示状态
     */
    @Override
    public void onSelect(int pressPosition, char letter) {
        if (showCenterLetter) {
            rlCenter.setVisibility(VISIBLE);
            tvCenter.setText(String.valueOf(letter));
        }
        rollPositionForChar(letter);
    }

    /**
     * 当对应的右侧sidebar字母被取消按下的显示状态
     */
    @Override
    public void onEndSelect() {
        rlCenter.setVisibility(GONE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * EditText监听文本变化，变化后过滤
     */
    @Override
    public void afterTextChanged(Editable s) {
        String value = s.toString();
        if (!TextUtils.isEmpty(value)) {
            filterList.clear();
            setFilterData(value);
        } else {
            isFilterMode = false;
            int charLength = 0;
            for (int i = 0, len = modelList.size(); i < len; i++) {
                if (i == 0) {
                    modelList.get(i).setFirstPosition(true);
                    charLength++;
                } else {
                    if (modelList.get(i).getFirstLetter() == modelList.get(i - 1).getFirstLetter()) {
                        modelList.get(i).setFirstPosition(false);
                    } else {
                        modelList.get(i).setFirstPosition(true);
                        charLength++;
                    }
                }
            }
            if (!showAllLetter) {
                char[] newLetters = new char[charLength];
                for (int i = 0, j = 0, len = modelList.size(); i < len; i++) {
                    if (modelList.get(i).isFirstPosition()) {
                        newLetters[j] = modelList.get(i).getFirstLetter();
                        j++;
                    }
                }
                sideBar.setLetters(newLetters);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * recyclerView Adapter
     */
    class IndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.index_view_item, parent, false);
            return new IndexViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            StringModel model;
            //对不同的模式取对应的数据
            if (!isFilterMode) {
                model = modelList.get(position);
            } else {
                model = filterList.get(position);
            }
            IndexViewHolder viewHolder = (IndexViewHolder) holder;
            viewHolder.tvLetter.setText(String.valueOf(model.getFirstLetter()).toUpperCase());
            viewHolder.tvContent.setText(model.getContent());
            if (model.isFirstPosition()) {
                viewHolder.tvLetter.setVisibility(VISIBLE);
            } else {
                viewHolder.tvLetter.setVisibility(GONE);
            }
        }

        @Override
        public int getItemCount() {
            //对不同的模式给不同的count
            return isFilterMode ? filterList.size() : modelList.size();
        }

        class IndexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

            TextView tvLetter;
            TextView tvContent;

            IndexViewHolder(View itemView) {
                super(itemView);
                tvLetter = (TextView) itemView.findViewById(R.id.tv_letter);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                tvContent.setOnClickListener(this);
                tvContent.setOnLongClickListener(this);
            }


            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(
//                            isFilterMode ? filterList.get(getAdapterPosition()).getContent() : modelList.get(getAdapterPosition()).getContent());
                    onItemClickListener.onItemClick(tvContent.getText().toString());
                }

            }

            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
//                    onItemLongClickListener.onItemLongClick(
//                            isFilterMode ? filterList.get(getAdapterPosition()).getContent() : modelList.get(getAdapterPosition()).getContent());
                    onItemLongClickListener.onItemLongClick(tvContent.getText().toString());
                }
                return true;
            }
        }
    }

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(String content);
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /**
     * item长按事件
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(String content);
    }
    private OnItemLongClickListener  onItemLongClickListener = null;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
