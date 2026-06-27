package com.p060tw.music.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.p060tw.music.R;
import com.p060tw.music.R$styleable;

/* loaded from: classes3.dex */
public class ImagViewAndTextView extends FrameLayout {

    /* renamed from: Xd */
    private int f1172Xd;

    /* renamed from: Yd */
    private String f1173Yd;

    /* renamed from: Zd */
    private ImageView f1174Zd;

    /* renamed from: ce */
    private TextView f1175ce;
    private View view;

    public ImagViewAndTextView(Context context) {
        super(context);
        m1511a(context, null);
    }

    /* renamed from: a */
    private void m1511a(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.inputView);
        this.f1172Xd = obtainStyledAttributes.getResourceId(0, R.drawable.ic_launcher);
        this.f1173Yd = obtainStyledAttributes.getString(1);
        obtainStyledAttributes.recycle();
        this.view = LayoutInflater.from(context).inflate(R.layout.input_icon_textview, (ViewGroup) null);
        this.f1174Zd = (ImageView) this.view.findViewById(R.id.input_icon);
        this.f1175ce = (TextView) this.view.findViewById(R.id.input_tx);
        this.f1174Zd.setImageResource(this.f1172Xd);
        this.f1175ce.setText(this.f1173Yd);
        addView(this.view);
    }

    public void setImageDrawable(Drawable drawable) {
        this.f1174Zd.setImageDrawable(drawable);
    }

    public void setTx(String str) {
        this.f1175ce.setText(str);
    }

    public void setTxColor(int i) {
        this.f1175ce.setTextColor(i);
    }

    public ImagViewAndTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        m1511a(context, attributeSet);
    }

    public ImagViewAndTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        m1511a(context, attributeSet);
    }

    @RequiresApi(api = 21)
    public ImagViewAndTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        m1511a(context, attributeSet);
    }
}
