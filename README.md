# CustomView
##Use this library:

compile 'hx0049.customview:CustomView:1.0.0'
##Introduction
###View
####TitleBar:Common title of all activities in your application
```java
 <hx0049.customview.view.titlebar.TitleBar
        android:id="@+id/titlebar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        hx:canBack="true"
        hx:showRightIcon="false" />
```
```java
        TitleBar titleBar = (TitleBar)findViewById(R.id.title);
        titleBar.setTitle("I am title");
        titleBar.setRightIcon(R.mipmap.ic_launcher);
        titleBar.setOnRightClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                Toast.makeText(MainActivity.this, "right click", Toast.LENGTH_SHORT).show();
            }
        });
```
####BannerView: rolling picture automatically
```java
 <hx0049.customview.view.banner.BannerView
        android:id="@+id/pg"
        android:layout_width="match_parent"
        android:layout_height="400px"
        hx:mode="MODE_CIRCLE"
        hx:roll_time="2000"/>
```

####IndexView: just like the address list
```java
 <hx0049.customview.view.indexview.IndexView
        android:id="@+id/index_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        hx:show_all_letter="false"/>
```

```java
        IndexView indexView = (IndexView) findViewById(R.id.index_view);
        indexView.setData(getData());
        indexView.setOnItemClickListener(new IndexView.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
        indexView.setOnItemLongClickListener(new IndexView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(String content) {
                Toast.makeText(MainActivity.this, "long click " + content, Toast.LENGTH_SHORT).show();
            }
        });
```

####ExpandableRecyclerviewAdapter: just like the ExpandableListView
```java
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
```
```java
public class ExpandableModel<T> {
    String name;
    List<T> childList;

    public ExpandableModel(String name, List<T> childList) {
        this.name = name;
        this.childList = childList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getChildList() {
        return childList;
    }

    public void setChildList(List<T> childList) {
        this.childList = childList;
    }
}
```
```java
        MyAdapter adapter = new MyAdapter(getApplicationContext(), getData());
        adapter.setOnItemClickListener(new ExpandableRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ExpandableModel model, int position) {
                Toast.makeText(TestActivity.this, (String) (model.getChildList().get(position)), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
```
####Calendar:for practice
```java
    <hx0049.customview.view.calendar.CalendarView
        android:id="@+id/calendar_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```
```java
        CalendarView calendar = (CalendarView)findViewById(R.id.calendar_view);
        calendar.setTimeCallBack(new TimeCallBack() {
            @Override
            public void currentTime(int year, int month) {
                Toast.makeText(MainActivity.this, year+"."+(month+1), Toast.LENGTH_SHORT).show();
            }
        });
```
####SlideDeleteAdapter
```java
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
```
```java
        MyAdapter3 adapter3 = new MyAdapter3(this, getData1());
        recyclerView.setAdapter(adapter3);
        adapter3.setDeleteListener(new SlideAdapter.DeleteListener() {
            @Override
            public void delete(int position) {
                adapter3.list.remove(position);
            }
        });
```
###ViewGroup
####ActionSheetUtils
```java
        ActionSheetUtils actionSheetUtils = ActionSheetUtils.getInstance(this);
        actionSheetUtils.showView(findViewById(R.id.rv).getRootView(), "happy", "sad", "cry", "laugh");
        actionSheetUtils.setOnActionSheetClickListener(new ActionSheetUtils.OnActionSheetClickListener() {
            @Override
            public void onClick(int position, String value) {
                Toast.makeText(TestActivity.this, "position = " + position + " " + value, Toast.LENGTH_SHORT).show();
            }
        });
```
####ArcMenu
```java
<hx0049.customview.viewgroup.ArcMenu
        android:layout_width="match_parent"
        hx:Radius="300px"
        hx:position="LEFT_BOTTOM"
        android:layout_height="match_parent">
        <ImageView
            android:layout_width="60px"
            android:layout_height="60px"
            android:background="@drawable/spots_blue_true"/>
        <ImageView
            android:layout_width="60px"
            android:layout_height="60px"
            android:background="@drawable/spots_blue_true"/>
        <ImageView
            android:layout_width="60px"
            android:layout_height="60px"
            android:background="@drawable/spots_blue_true"/>
        <ImageView
            android:layout_width="60px"
            android:layout_height="60px"
            android:background="@drawable/spots_blue_true"/>
        <ImageView
            android:layout_width="60px"
            android:layout_height="60px"
            android:background="@drawable/spots_blue_true"/>


    </hx0049.customview.viewgroup.ArcMenu>
```
####DrawerLayout
```java
<hx0049.customview.viewgroup.DrawerLayout
        android:id="@+id/myscrollview"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        hx:offset="300px">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:orientation="horizontal">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="#0f0">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:textSize="50px"
                    android:text="111222333444555666777999999888" />
            </LinearLayout>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="#00f">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:textSize="50px"
                    android:text="1222211222333444555666777999999888"
                    />
            </LinearLayout>
        </LinearLayout>

    </hx0049.customview.viewgroup.DrawerLayout>
```
####FlowLayout
```java
 <hx0049.customview.viewgroup.FlowLayout
        android:id="@+id/fl"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```
###RecyclerViewCommonAdapter
####ComRecyclerViewAdapter
```java
    class MyAdapter extends ComRecyclerAdapter<String>{
        
        public MyAdapter(Context context, List<String> list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        public void convert(ComRecyclerViewHolder viewHolder, String s) {
            viewHolder.setText(android.R.id.text1,s);
        }
    }
```
```java
        MyAdapter myAdapter = new MyAdapter(this,getData(),android.R.layout.simple_list_item_1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
```
####ComAdapter
```java
        ComAdapter<String> adapter = new ComAdapter<String>(this);
        adapter.setData(getData())
                .setItemView(android.R.layout.simple_list_item_1)
                .setShowItem(new ComAdapter.ShowItem() {
                    @Override
                    public void show(ComRecyclerViewHolder viewHolder, Object object) {
                        viewHolder.setText(android.R.id.text1,(String)object);
                       
                    }
                })
                .setHeadView(android.R.layout.select_dialog_item)
                .setShowHead(new ComAdapter.ShowHead() {
                    @Override
                    public void show(ComRecyclerViewHolder viewHolder) {
                        TextView tv = viewHolder.getView(android.R.id.text1);
                        tv.setText("I am head!");
                    }
                })
                .loadVertical(recyclerView);
```
