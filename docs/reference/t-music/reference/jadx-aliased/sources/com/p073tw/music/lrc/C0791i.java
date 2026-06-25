package com.p073tw.music.lrc;

import android.animation.ValueAnimator;

/* compiled from: LrcView.java */
/* renamed from: com.tw.music.lrc.i */
/* loaded from: classes3.dex */
class C0791i implements ValueAnimator.AnimatorUpdateListener {
    final /* synthetic */ LrcView this$0;

    C0791i(LrcView lrcView) {
        this.this$0 = lrcView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.this$0.mOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.this$0.invalidate();
    }
}
