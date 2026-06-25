package com.p060tw.preference;

import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.p060tw.preference.SingleChoosePreference;

/* compiled from: SingleChoosePreference.java */
/* renamed from: com.tw.preference.d */
/* loaded from: classes4.dex */
class ViewOnClickListenerC0802d implements View.OnClickListener {
    final /* synthetic */ SingleChoosePreference this$0;

    ViewOnClickListenerC0802d(SingleChoosePreference singleChoosePreference) {
        this.this$0 = singleChoosePreference;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        PopupWindow popupWindow;
        PopupWindow popupWindow2;
        TextView textView;
        CharSequence[] charSequenceArr;
        SingleChoosePreference.InterfaceC0797a interfaceC0797a;
        SingleChoosePreference.InterfaceC0797a interfaceC0797a2;
        CharSequence[] charSequenceArr2;
        int i = 0;
        while (true) {
            try {
                popupWindow = this.this$0.f1233oe;
                if (i >= ((ViewGroup) popupWindow.getContentView()).getChildCount()) {
                    return;
                }
                popupWindow2 = this.this$0.f1233oe;
                TextView textView2 = (TextView) ((ViewGroup) popupWindow2.getContentView()).getChildAt(i);
                if (view.getTag() == Integer.valueOf(i)) {
                    textView2.getBackground().setLevel(1);
                    textView = this.this$0.f1229je;
                    textView.setText(textView2.getText());
                    this.this$0.f1232ne = i;
                    SingleChoosePreference singleChoosePreference = this.this$0;
                    charSequenceArr = this.this$0.f1231me;
                    singleChoosePreference.mValue = charSequenceArr[i].toString();
                    interfaceC0797a = this.this$0.f1237se;
                    if (interfaceC0797a != null) {
                        interfaceC0797a2 = this.this$0.f1237se;
                        SingleChoosePreference singleChoosePreference2 = this.this$0;
                        charSequenceArr2 = this.this$0.f1231me;
                        interfaceC0797a2.m1551a(singleChoosePreference2, charSequenceArr2[i]);
                    }
                } else {
                    textView2.getBackground().setLevel(0);
                }
                i++;
            } catch (Exception unused) {
                return;
            }
        }
    }
}
