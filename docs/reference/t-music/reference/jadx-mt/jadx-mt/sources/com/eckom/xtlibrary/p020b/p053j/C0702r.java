package com.eckom.xtlibrary.p020b.p053j;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eckom.xtlibrary.R$drawable;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.r */
/* JADX INFO: compiled from: ToastCustom.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0702r {

    /* JADX INFO: renamed from: ce */
    private static TextView f845ce;
    private static View mView;
    private static WindowManager.LayoutParams params;

    /* JADX INFO: renamed from: qm */
    private static C0702r f846qm;

    /* JADX INFO: renamed from: rh */
    private static WindowManager f847rh;
    private a mHandler;
    private double time;

    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.r$a */
    /* JADX INFO: compiled from: ToastCustom.java */
    private class a extends Handler {
        private a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 0) {
                return;
            }
            Log.d("ToastCustom", "dismiss:");
            C0702r.this.cancel();
        }
    }

    private C0702r(Context context, CharSequence charSequence, double d2) {
        this.time = d2;
        if (f847rh == null) {
            f847rh = (WindowManager) context.getSystemService("window");
        }
        mView = m1036a(context, charSequence);
        m1035Ra(-1);
    }

    /* JADX INFO: renamed from: Ra */
    private void m1035Ra(int i) {
        params = new WindowManager.LayoutParams();
        WindowManager.LayoutParams layoutParams = params;
        layoutParams.height = -2;
        layoutParams.width = -2;
        layoutParams.format = 1;
        layoutParams.windowAnimations = i;
        layoutParams.type = 2002;
        layoutParams.flags = 8;
        layoutParams.gravity = 17;
        layoutParams.y = 0;
        layoutParams.x = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancel() {
        f847rh.removeView(mView);
        mView = null;
        f846qm = null;
        this.mHandler = null;
    }

    private static void setText(CharSequence charSequence) {
        f845ce.setText(charSequence);
    }

    public void show() {
        if (this.mHandler == null) {
            this.mHandler = new a();
            f847rh.addView(mView, params);
            Log.d("ToastCustom", "show:" + this.time);
            this.mHandler.removeMessages(0);
            this.mHandler.sendEmptyMessageDelayed(0, (long) this.time);
        }
    }

    /* JADX INFO: renamed from: a */
    private View m1036a(Context context, CharSequence charSequence) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundResource(R$drawable.toast_view_shape);
        f845ce = new TextView(context);
        f845ce.setText(charSequence);
        f845ce.setTextColor(Color.parseColor("#000000"));
        f845ce.setTextSize(20.0f);
        f845ce.setPadding(20, 12, 20, 12);
        f845ce.setGravity(17);
        linearLayout.addView(f845ce, 0);
        return linearLayout;
    }

    /* JADX INFO: renamed from: a */
    public static C0702r m1037a(Context context, CharSequence charSequence, double d2) {
        if (f846qm == null) {
            f846qm = new C0702r(context, charSequence, d2);
        } else {
            setText(charSequence);
        }
        return f846qm;
    }
}
