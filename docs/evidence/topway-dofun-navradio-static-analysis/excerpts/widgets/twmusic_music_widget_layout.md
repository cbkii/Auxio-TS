# Evidence excerpt: twmusic_music_widget_layout.md

 Source APK/variant: `com.tw.music_TW_THEME.20240715`
 Source path: `com.tw.music_TW_THEME.20240715/apktool/res/layout/music_widget.xml`
 Source lines: `1-31`
 Status: observation from static decompile/extract.
 Why it matters: Stock widget RemoteViews layout IDs: albumart, title, artist, control_prev, control_play, control_next, current/duration text, progress bar.

 ```xml
     1: <?xml version="1.0" encoding="utf-8"?>
 2: <LinearLayout android:orientation="horizontal" android:layout_width="wrap_content" android:layout_height="wrap_content"
 3:   xmlns:android="http://schemas.android.com/apk/res/android" xmlns:app="http://schemas.android.com/apk/res-auto">
 4:     <LinearLayout android:gravity="center_vertical" android:id="@id/music_widget" android:background="@drawable/music_widget_bg" android:layout_width="@dimen/tw_dp_w490" android:layout_height="@dimen/tw_dp_h225">
 5:         <ImageView android:id="@id/albumart" android:layout_width="@dimen/tw_dp_w150" android:layout_height="@dimen/tw_dp_w150" android:layout_marginLeft="@dimen/tw_dp_w20" android:src="@drawable/album" android:scaleType="fitCenter" />
 6:         <LinearLayout android:gravity="center" android:orientation="vertical" android:layout_width="fill_parent" android:layout_height="fill_parent">
 7:             <TextView android:textSize="@dimen/tw_dp_h18" android:textColor="#ffebebeb" android:gravity="center" android:id="@id/title" android:layout_width="fill_parent" android:layout_height="@dimen/tw_dp_h24" android:layout_marginBottom="@dimen/tw_dp_h12" android:singleLine="true" />
 8:             <TextView android:textSize="@dimen/tw_dp_h16" android:textColor="#ff9c9c9c" android:gravity="center" android:id="@id/artist" android:layout_width="fill_parent" android:layout_height="@dimen/tw_dp_h24" android:layout_marginBottom="@dimen/tw_dp_h12" android:singleLine="true" />
 9:             <LinearLayout android:gravity="center_horizontal" android:layout_width="fill_parent" android:layout_height="@dimen/tw_dp_h23">
10:                 <ImageView android:id="@id/control_prev" android:clickable="true" android:layout_width="fill_parent" android:layout_height="wrap_content" android:src="@drawable/widget_prev" android:scaleType="centerInside" android:layout_weight="1.0" />
11:                 <ImageView android:id="@id/control_play" android:clickable="true" android:layout_width="fill_parent" android:layout_height="wrap_content" android:src="@drawable/widget_pp" android:scaleType="centerInside" android:layout_weight="1.0" />
12:                 <ImageView android:id="@id/control_next" android:clickable="true" android:layout_width="fill_parent" android:layout_height="wrap_content" android:src="@drawable/widget_next" android:scaleType="centerInside" android:layout_weight="1.0" />
13:             </LinearLayout>
14:             <LinearLayout android:gravity="center" android:layout_width="@dimen/tw_dp_w234" android:layout_height="wrap_content" android:layout_marginTop="@dimen/tw_dp_h12">
15:                 <TextView android:textSize="15.0dip" android:textColor="#ffebebeb" android:gravity="center|left" android:id="@id/tv_current_time" android:layout_width="wrap_content" android:layout_height="wrap_content" />
16:                 <LinearLayout android:layout_width="0.0dip" android:layout_height="wrap_content" android:layout_weight="1.0" />
17:                 <TextView android:textSize="15.0dip" android:textColor="#ffebebeb" android:gravity="center|right" android:id="@id/tv_duration" android:layout_width="wrap_content" android:layout_height="wrap_content" />
18:             </LinearLayout>
19:             <ProgressBar android:id="@id/seek_bar_progress" android:paddingLeft="@dimen/tw_dp_w10" android:paddingRight="@dimen/tw_dp_w10" android:layout_width="@dimen/tw_dp_w254" android:layout_height="@dimen/tw_dp_h20" android:layout_marginTop="@dimen/tw_dp_h12" android:maxHeight="@dimen/tw_dp_h3" android:progressDrawable="@drawable/seek_bar_style" android:minHeight="@dimen/tw_dp_h3" style="?android:progressBarStyleHorizontal" />
20:         </LinearLayout>
21:     </LinearLayout>
22: </LinearLayout>
 ```
