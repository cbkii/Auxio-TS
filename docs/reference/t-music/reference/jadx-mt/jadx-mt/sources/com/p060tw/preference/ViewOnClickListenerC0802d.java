package com.p060tw.preference;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/* JADX INFO: renamed from: com.tw.preference.d */
/* JADX INFO: compiled from: SingleChoosePreference.java */
/* JADX INFO: loaded from: classes4.dex */
class ViewOnClickListenerC0802d implements View.OnClickListener {
    final /* synthetic */ SingleChoosePreference this$0;

    ViewOnClickListenerC0802d(SingleChoosePreference singleChoosePreference) {
        this.this$0 = singleChoosePreference;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        for (int i = 0; i < ((ViewGroup) this.this$0.f1233oe.getContentView()).getChildCount(); i++) {
            try {
                TextView textView = (TextView) ((ViewGroup) this.this$0.f1233oe.getContentView()).getChildAt(i);
                if (view.getTag() == Integer.valueOf(i)) {
                    textView.getBackground().setLevel(1);
                    this.this$0.f1229je.setText(textView.getText());
                    this.this$0.f1232ne = i;
                    this.this$0.mValue = this.this$0.f1231me[i].toString();
                    if (this.this$0.f1237se != null) {
                        this.this$0.f1237se.m1551a(this.this$0, this.this$0.f1231me[i]);
                    }
                } else {
                    textView.getBackground().setLevel(0);
                }
            } catch (Exception unused) {
                return;
            }
        }
    }
}
