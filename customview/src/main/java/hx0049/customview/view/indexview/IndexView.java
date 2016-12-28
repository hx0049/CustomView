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
        //get the attribute from xml
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.IndexView);
        showAllLetter = ta.getBoolean(R.styleable.IndexView_show_all_letter, true);
        showSearchView = ta.getBoolean(R.styleable.IndexView_show_search_view, true);
        showCenterLetter = ta.getBoolean(R.styleable.IndexView_show_center_letter, true);
        ta.recycle();

        //Inflater
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
        //clear the wrong string
        for (int i = stringList.size() - 1; i > 0; i--) {
            if (TextUtils.isEmpty(stringList.get(i))) {
                stringList.remove(i);
            }
        }
        //clear the EditText
        editText.setText("");
        //set mode no-filter
        isFilterMode = false;
        //init
        for (int i = stringList.size() - 1; i > 0; i--) {
            modelList.add(0, new StringModel(stringList.get(i)));
        }
        //sort the strings
        Collections.sort(modelList, comparator);
        //get the first letter position and number
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
        //show all number or not
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
        //notify the Adapter
        adapter.notifyDataSetChanged();
    }

    /**
     * 根据value过滤数据
     */
    public void setFilterData(String value) {
        //set mode filter
        isFilterMode = true;
        //filter the data
        for (int i = modelList.size() - 1; i >= 0; i--) {
            if (modelList.get(i).getContent().toLowerCase().indexOf(value.toLowerCase()) == 0 ||
                    modelList.get(i).getPingYin().toLowerCase().indexOf(value.toLowerCase()) == 0) {
                filterList.add(0, modelList.get(i));
            }
        }
        //sort
        Collections.sort(filterList, comparator);
        //get the first letter position and number
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
        //show all number  or not
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
        //notify the Adapter
        adapter.notifyDataSetChanged();
    }

    /**
     * scroll to the position of the letter
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
     * when the right letter was selected
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
     * when the right letter was selected canceled
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
     * EditText listener
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
            //show the different data for different mode
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
            //ensure count for different mode
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
     * item click event
     */
    public interface OnItemClickListener {
        void onItemClick(String content);
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /**
     * item long click event
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(String content);
    }
    private OnItemLongClickListener  onItemLongClickListener = null;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
