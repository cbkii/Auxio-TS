package com.p060tw.music.lrc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import com.p060tw.music.R;
import com.p060tw.music.R$styleable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class LrcView extends View {

    /* renamed from: Ne */
    private List<C0783a> f1145Ne;

    /* renamed from: Oe */
    private TextPaint f1146Oe;

    /* renamed from: Pe */
    private TextPaint f1147Pe;

    /* renamed from: Qe */
    private Paint.FontMetrics f1148Qe;

    /* renamed from: Re */
    private Drawable f1149Re;

    /* renamed from: Se */
    private long f1150Se;

    /* renamed from: Te */
    private int f1151Te;

    /* renamed from: Ue */
    private float f1152Ue;

    /* renamed from: Ve */
    private int f1153Ve;

    /* renamed from: We */
    private float f1154We;

    /* renamed from: Xe */
    private int f1155Xe;

    /* renamed from: Ye */
    private int f1156Ye;

    /* renamed from: Ze */
    private int f1157Ze;

    /* renamed from: df */
    private int f1158df;

    /* renamed from: ef */
    private int f1159ef;

    /* renamed from: ff */
    private String f1160ff;

    /* renamed from: gf */
    private float f1161gf;

    /* renamed from: hf */
    private InterfaceC0782a f1162hf;

    /* renamed from: if */
    private Object f1163if;

    /* renamed from: jf */
    private boolean f1164jf;

    /* renamed from: kf */
    private boolean f1165kf;

    /* renamed from: lf */
    private boolean f1166lf;
    private ValueAnimator mAnimator;
    private int mCurrentLine;
    private float mDividerHeight;
    private GestureDetector mGestureDetector;
    private float mOffset;
    private Scroller mScroller;

    /* renamed from: mf */
    private int f1167mf;

    /* renamed from: nf */
    private GestureDetector.SimpleOnGestureListener f1168nf;

    /* renamed from: of */
    private Runnable f1169of;

    /* renamed from: com.tw.music.lrc.LrcView$a */
    public interface InterfaceC0782a {
        /* renamed from: f */
        boolean mo1464f(long j);
    }

    public LrcView(Context context) {
        this(context, null);
    }

    /* renamed from: Ee */
    private void m1466Ee() {
        m1477a(getCenterLine(), 1000L);
    }

    /* renamed from: Fe */
    private void m1467Fe() {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.mAnimator.end();
    }

    /* renamed from: Ge */
    private void m1468Ge() {
        if (!m1500Wa() || getWidth() == 0) {
            return;
        }
        Collections.sort(this.f1145Ne);
        Iterator<C0783a> it = this.f1145Ne.iterator();
        while (it.hasNext()) {
            it.next().m1506a(this.f1146Oe, (int) getLrcWidth(), this.f1167mf);
        }
        this.mOffset = getHeight() / 2;
    }

    /* renamed from: He */
    private void m1469He() {
        try {
            Field declaredField = ValueAnimator.class.getDeclaredField("sDurationScale");
            declaredField.setAccessible(true);
            declaredField.setFloat(null, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ja */
    public int m1470Ja(int i) {
        int size = this.f1145Ne.size();
        int i2 = 0;
        while (i2 <= size) {
            int i3 = (i2 + size) / 2;
            long j = i;
            if (j < this.f1145Ne.get(i3).getTime()) {
                size = i3 - 1;
            } else {
                i2 = i3 + 1;
                if (i2 >= this.f1145Ne.size() || j < this.f1145Ne.get(i2).getTime()) {
                    return i3;
                }
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ka */
    public float m1471Ka(int i) {
        if (this.f1145Ne.get(i).getOffset() == Float.MIN_VALUE) {
            float height = getHeight() / 2;
            for (int i2 = 1; i2 <= i; i2++) {
                height -= ((this.f1145Ne.get(i2 - 1).getHeight() + this.f1145Ne.get(i2).getHeight()) / 2) + this.mDividerHeight;
            }
            this.f1145Ne.get(i).setOffset(height);
        }
        return this.f1145Ne.get(i).getOffset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: La */
    public void m1472La(int i) {
        m1477a(i, this.f1150Se);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCenterLine() {
        float f = Float.MAX_VALUE;
        int i = 0;
        for (int i2 = 0; i2 < this.f1145Ne.size(); i2++) {
            if (Math.abs(this.mOffset - m1471Ka(i2)) < f) {
                f = Math.abs(this.mOffset - m1471Ka(i2));
                i = i2;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object getFlag() {
        return this.f1163if;
    }

    private float getLrcWidth() {
        return getWidth() - (this.f1161gf * 2.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFlag(Object obj) {
        this.f1163if = obj;
    }

    /* renamed from: Wa */
    public boolean m1500Wa() {
        return !this.f1145Ne.isEmpty();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.mOffset = this.mScroller.getCurrY();
            invalidate();
        }
        if (this.f1166lf && this.mScroller.isFinished()) {
            this.f1166lf = false;
            if (!m1500Wa() || this.f1165kf) {
                return;
            }
            m1466Ee();
            postDelayed(this.f1169of, 4000L);
        }
    }

    /* renamed from: fa */
    public void m1501fa(int i) {
        m1489c(new RunnableC0788f(this, i));
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.f1169of);
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight() / 2;
        if (!m1500Wa()) {
            this.f1146Oe.setColor(this.f1153Ve);
            m1478a(canvas, new StaticLayout(this.f1160ff, this.f1146Oe, (int) getLrcWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false), height);
            return;
        }
        int centerLine = getCenterLine();
        if (this.f1164jf) {
            this.f1149Re.draw(canvas);
            this.f1147Pe.setColor(this.f1156Ye);
            float f = height;
            canvas.drawLine(this.f1159ef, f, getWidth() - this.f1159ef, f, this.f1147Pe);
            this.f1147Pe.setColor(this.f1157Ze);
            String m1508g = C0784b.m1508g(this.f1145Ne.get(centerLine).getTime());
            float width = getWidth() - (this.f1159ef / 2);
            Paint.FontMetrics fontMetrics = this.f1148Qe;
            canvas.drawText(m1508g, width, f - ((fontMetrics.descent + fontMetrics.ascent) / 2.0f), this.f1147Pe);
        }
        float f2 = 0.0f;
        canvas.translate(0.0f, this.mOffset);
        for (int i = 0; i < this.f1145Ne.size(); i++) {
            if (i > 0) {
                f2 += ((this.f1145Ne.get(i - 1).getHeight() + this.f1145Ne.get(i).getHeight()) / 2) + this.mDividerHeight;
            }
            if (i == this.mCurrentLine) {
                this.f1146Oe.setTextSize(this.f1154We);
                this.f1146Oe.setColor(this.f1153Ve);
            } else if (this.f1164jf && i == centerLine) {
                this.f1146Oe.setColor(this.f1155Xe);
            } else {
                this.f1146Oe.setTextSize(this.f1152Ue);
                this.f1146Oe.setColor(this.f1151Te);
            }
            m1478a(canvas, this.f1145Ne.get(i).m1507kd(), f2);
        }
        postInvalidateDelayed(100L);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            m1468Ge();
            int i5 = (this.f1159ef - this.f1158df) / 2;
            int height = getHeight() / 2;
            int i6 = this.f1158df;
            int i7 = height - (i6 / 2);
            this.f1149Re.setBounds(i5, i7, i5 + i6, i6 + i7);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.f1165kf = false;
            if (m1500Wa() && !this.f1166lf) {
                m1466Ee();
                postDelayed(this.f1169of, 4000L);
            }
        }
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    public void reset() {
        m1467Fe();
        this.mScroller.forceFinished(true);
        this.f1164jf = false;
        this.f1165kf = false;
        this.f1166lf = false;
        removeCallbacks(this.f1169of);
        this.f1145Ne.clear();
        this.mOffset = 0.0f;
        this.mCurrentLine = 0;
        invalidate();
    }

    public void setCurrentColor(int i) {
        this.f1153Ve = i;
        postInvalidate();
    }

    public void setCurrentTextSize(float f) {
        this.f1154We = f;
    }

    public void setLabel(String str) {
        m1489c(new RunnableC0785c(this, str));
    }

    public void setNormalColor(int i) {
        this.f1151Te = i;
        postInvalidate();
    }

    public void setNormalTextSize(float f) {
        this.f1152Ue = f;
    }

    public void setOnPlayClickListener(InterfaceC0782a interfaceC0782a) {
        this.f1162hf = interfaceC0782a;
    }

    public void setTimeTextColor(int i) {
        this.f1157Ze = i;
        postInvalidate();
    }

    public void setTimelineColor(int i) {
        this.f1156Ye = i;
        postInvalidate();
    }

    public void setTimelineTextColor(int i) {
        this.f1155Xe = i;
        postInvalidate();
    }

    /* renamed from: va */
    public void m1502va(String str) {
        m1489c(new RunnableC0787e(this, str));
    }

    public LrcView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* renamed from: b */
    static /* synthetic */ float m1483b(LrcView lrcView, float f) {
        float f2 = lrcView.mOffset + f;
        lrcView.mOffset = f2;
        return f2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: f */
    public void m1495f(List<C0783a> list) {
        if (list != null && !list.isEmpty()) {
            this.f1145Ne.addAll(list);
        }
        m1468Ge();
        invalidate();
    }

    public LrcView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f1145Ne = new ArrayList();
        this.f1146Oe = new TextPaint();
        this.f1147Pe = new TextPaint();
        this.f1168nf = new C0789g(this);
        this.f1169of = new RunnableC0790h(this);
        m1485b(attributeSet);
    }

    /* renamed from: c */
    private void m1489c(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    /* renamed from: b */
    private void m1485b(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.LrcView);
        this.f1154We = obtainStyledAttributes.getDimension(9, getResources().getDimension(R.dimen.lrc_cur_text_size));
        this.f1152Ue = obtainStyledAttributes.getDimension(5, getResources().getDimension(R.dimen.lrc_text_size));
        if (this.f1152Ue == 0.0f) {
            this.f1152Ue = this.f1154We;
        }
        this.mDividerHeight = obtainStyledAttributes.getDimension(2, getResources().getDimension(R.dimen.lrc_divider_height));
        this.f1150Se = obtainStyledAttributes.getInt(0, 1000);
        this.f1151Te = obtainStyledAttributes.getColor(4, getResources().getColor(android.R.color.white));
        this.f1153Ve = obtainStyledAttributes.getColor(1, getResources().getColor(R.color.text_green));
        this.f1155Xe = obtainStyledAttributes.getColor(14, getResources().getColor(android.R.color.white));
        this.f1160ff = obtainStyledAttributes.getString(3);
        this.f1160ff = TextUtils.isEmpty(this.f1160ff) ? "nolrc" : this.f1160ff;
        this.f1161gf = obtainStyledAttributes.getDimension(6, 0.0f);
        this.f1156Ye = obtainStyledAttributes.getColor(12, getResources().getColor(android.R.color.darker_gray));
        float dimension = obtainStyledAttributes.getDimension(13, 1.0f);
        this.f1149Re = obtainStyledAttributes.getDrawable(7);
        Drawable drawable = this.f1149Re;
        if (drawable == null) {
            drawable = getResources().getDrawable(R.drawable.select_lrc_play);
        }
        this.f1149Re = drawable;
        this.f1157Ze = obtainStyledAttributes.getColor(10, getResources().getColor(android.R.color.darker_gray));
        float dimension2 = obtainStyledAttributes.getDimension(11, 14.0f);
        this.f1167mf = obtainStyledAttributes.getInteger(8, 2);
        obtainStyledAttributes.recycle();
        this.f1158df = (int) getResources().getDimension(R.dimen.lrc_drawable_width);
        this.f1159ef = (int) getResources().getDimension(R.dimen.lrc_time_width);
        this.f1146Oe.setAntiAlias(true);
        this.f1146Oe.setTextSize(this.f1154We);
        this.f1146Oe.setTextAlign(Paint.Align.LEFT);
        this.f1147Pe.setAntiAlias(true);
        this.f1147Pe.setTextSize(dimension2);
        this.f1147Pe.setTextAlign(Paint.Align.CENTER);
        this.f1147Pe.setStrokeWidth(dimension);
        this.f1147Pe.setStrokeCap(Paint.Cap.ROUND);
        this.f1148Qe = this.f1147Pe.getFontMetrics();
        this.mGestureDetector = new GestureDetector(getContext(), this.f1168nf);
        this.mGestureDetector.setIsLongpressEnabled(false);
        this.mScroller = new Scroller(getContext());
    }

    /* renamed from: a */
    private void m1478a(Canvas canvas, StaticLayout staticLayout, float f) {
        try {
            canvas.save();
            canvas.translate(this.f1161gf, f - (staticLayout.getHeight() / 2));
            staticLayout.draw(canvas);
            canvas.restore();
        } catch (Exception e) {
            Log.i("md", "" + e.toString());
        }
    }

    /* renamed from: a */
    private void m1477a(int i, long j) {
        float m1471Ka = m1471Ka(i);
        m1467Fe();
        this.mAnimator = ValueAnimator.ofFloat(this.mOffset, m1471Ka);
        this.mAnimator.setDuration(j);
        this.mAnimator.setInterpolator(new LinearInterpolator());
        this.mAnimator.addUpdateListener(new C0791i(this));
        m1469He();
        this.mAnimator.start();
    }
}
