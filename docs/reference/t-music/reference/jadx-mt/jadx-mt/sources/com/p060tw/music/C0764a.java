package com.p060tw.music;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

/* JADX INFO: renamed from: com.tw.music.a */
/* JADX INFO: compiled from: AudioPreview.java */
/* JADX INFO: loaded from: classes3.dex */
class C0764a extends AsyncQueryHandler {
    final /* synthetic */ AudioPreview this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0764a(AudioPreview audioPreview, ContentResolver contentResolver) {
        super(contentResolver);
        this.this$0 = audioPreview;
    }

    @Override // android.content.AsyncQueryHandler
    protected void onQueryComplete(int i, Object obj, Cursor cursor) {
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
                this.this$0.f1007Ec.setText(cursor.getString(columnIndex));
                if (columnIndex2 >= 0) {
                    this.this$0.f1008Fc.setText(cursor.getString(columnIndex2));
                }
            } else if (columnIndex4 >= 0) {
                this.this$0.f1007Ec.setText(cursor.getString(columnIndex4));
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
