package com.p073tw.music;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;
import android.widget.TextView;

/* compiled from: AudioPreview.java */
/* renamed from: com.tw.music.a */
/* loaded from: classes3.dex */
class C0764a extends AsyncQueryHandler {
    final /* synthetic */ AudioPreview this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0764a(AudioPreview audioPreview, ContentResolver contentResolver) {
        super(contentResolver);
        this.this$0 = audioPreview;
    }

    @Override // android.content.AsyncQueryHandler
    protected void onQueryComplete(int i, Object obj, Cursor cursor) {
        TextView textView;
        TextView textView2;
        TextView textView3;
        if (cursor == null || !cursor.moveToFirst()) {
            Log.w("AudioPreview", "empty cursor");
        } else {
            int columnIndex = cursor.getColumnIndex("title");
            int columnIndex2 = cursor.getColumnIndex("artist");
            int columnIndex3 = cursor.getColumnIndex("_id");
            int columnIndex4 = cursor.getColumnIndex("_display_name");
            if (columnIndex3 >= 0) {
                this.this$0.mMediaId = cursor.getLong(columnIndex3);
            }
            if (columnIndex >= 0) {
                String string = cursor.getString(columnIndex);
                textView2 = this.this$0.f1007Ec;
                textView2.setText(string);
                if (columnIndex2 >= 0) {
                    String string2 = cursor.getString(columnIndex2);
                    textView3 = this.this$0.f1008Fc;
                    textView3.setText(string2);
                }
            } else if (columnIndex4 >= 0) {
                String string3 = cursor.getString(columnIndex4);
                textView = this.this$0.f1007Ec;
                textView.setText(string3);
            } else {
                Log.w("AudioPreview", "Cursor had no names for us");
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        this.this$0.m1325Oa();
    }
}
