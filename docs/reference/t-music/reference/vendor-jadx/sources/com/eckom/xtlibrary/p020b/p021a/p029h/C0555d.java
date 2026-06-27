package com.eckom.xtlibrary.p020b.p021a.p029h;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.SystemProperties;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.eckom.xtlibrary.R$dimen;
import com.eckom.xtlibrary.R$id;
import com.eckom.xtlibrary.R$layout;
import com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h;

/* compiled from: VoiceCallView.java */
/* renamed from: com.eckom.xtlibrary.b.a.h.d */
/* loaded from: classes3.dex */
public class C0555d {
    private LinearLayout ll_anim_all;
    private LinearLayout ll_anim_list;
    private Context mContext;
    private WindowManager.LayoutParams mLayoutParams;
    private View mView;

    /* renamed from: rh */
    private WindowManager f441rh;

    /* renamed from: sh */
    private SharedPreferences f442sh;

    /* renamed from: th */
    private double f443th;
    private float startX = 0.0f;
    private float startY = 0.0f;

    /* renamed from: uh */
    private float f444uh = 0.0f;

    /* renamed from: vh */
    private float f445vh = 0.0f;
    private Handler handler = new Handler();

    public C0555d(Context context, AbstractC0546h abstractC0546h) {
        this.mContext = context;
        this.f442sh = context.getSharedPreferences("VoiceCallView", 0);
        this.f441rh = (WindowManager) context.getSystemService("window");
        int[] iArr = {context.getResources().getDimensionPixelSize(R$dimen.tw_dp_w220), context.getResources().getDimensionPixelSize(R$dimen.tw_dp_w60)};
        this.mLayoutParams = new WindowManager.LayoutParams(iArr[0], iArr[1], 0, 0, 2002, 40, 1);
        this.mLayoutParams.gravity = 51;
        this.f443th = Math.ceil(context.getResources().getDisplayMetrics().density * 25.0f);
        this.mView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R$layout.anim_voice_call, (ViewGroup) null);
        this.ll_anim_list = (LinearLayout) this.mView.findViewById(R$id.ll_anim_list);
        this.ll_anim_all = (LinearLayout) this.mView.findViewById(R$id.ll_anim_all);
        this.mView.findViewById(R$id.iv_voice_call).setOnClickListener(new ViewOnClickListenerC0552a(this, abstractC0546h));
        m373c(iArr);
        this.mView.setOnTouchListener(new ViewOnTouchListenerC0553b(this));
    }

    public void hide() {
        if (this.mView.getParent() != null) {
            this.f441rh.removeView(this.mView);
        }
    }

    public void show() {
        if (this.mView.getParent() == null) {
            this.ll_anim_all.setVisibility(0);
            this.f441rh.addView(this.mView, this.mLayoutParams);
            AnimationDrawable animationDrawable = (AnimationDrawable) this.ll_anim_list.getBackground();
            if (animationDrawable.isRunning()) {
                return;
            }
            animationDrawable.start();
            this.handler.postDelayed(new RunnableC0554c(this), SystemProperties.getInt("persist.bt.voiceView.delayed", 10000));
        }
    }

    /* renamed from: c */
    private void m373c(int[] iArr) {
        int i = this.f442sh.getInt("CallingViewX", -1);
        int i2 = this.f442sh.getInt("CallingViewY", -1);
        if (i != -1) {
            this.mLayoutParams.x = i;
        } else {
            this.mLayoutParams.x = (this.f441rh.getDefaultDisplay().getWidth() - iArr[0]) / 2;
        }
        if (i2 != -1) {
            this.mLayoutParams.y = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: a */
    public void m368a(float f, float f2) {
        WindowManager.LayoutParams layoutParams = this.mLayoutParams;
        int i = (int) f;
        layoutParams.x = i;
        int i2 = (int) f2;
        layoutParams.y = i2;
        this.f442sh.edit().putInt("CallingViewX", i).apply();
        this.f442sh.edit().putInt("CallingViewY", i2).apply();
        this.f441rh.updateViewLayout(this.mView, this.mLayoutParams);
    }
}
