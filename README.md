# CustomView
##Use this library:

compile 'hx0049.customview:CustomView:1.0.0'
##Introduction
###View
####TitleBar:Common title of all activities in your application
```java
 <hx0049.customview.view.titlebar.TitleBar
        android:id="@+id/title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        hx:canBack="true"
        hx:showRightIcon="false" />
```
####BannerView
```java
 <hx0049.customview.view.banner.BannerView
        android:id="@+id/pg"
        android:layout_width="match_parent"
        android:layout_height="400px"
        hx:mode="MODE_CIRCLE"
        hx:roll_time="2000"/>
```

####IndexView
```java
 <hx0049.customview.view.indexview.IndexView
        android:id="@+id/index_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        hx:show_all_letter="false"/>
```
####ExpandableRecyclerviewAdapter
####Calendar
```java
    <hx0049.customview.view.calendar.CalendarView
        android:id="@+id/calendar_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```
####SlideDeleteAdapter
###ViewGroup
####ActionSheetUtils
####ArcMenu
```java
```
####DrawerLayout
```java
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
####ComAdapter
