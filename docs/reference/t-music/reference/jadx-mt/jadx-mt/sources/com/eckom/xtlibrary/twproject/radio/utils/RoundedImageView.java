package com.eckom.xtlibrary.twproject.radio.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes3.dex */
public class RoundedImageView extends ImageView {
    private float cornerRadius;
    private Drawable drawable;
    private int padding;

    /* JADX INFO: renamed from: ve */
    private Drawable f896ve;

    public RoundedImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.cornerRadius = 10.0f;
        this.padding = 4;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.drawable == null) {
            super.onDraw(canvas);
            return;
        }
        Drawable drawable = this.f896ve;
        if (drawable == null) {
            super.onDraw(canvas);
            return;
        }
        drawable.setBounds(0, 0, getWidth(), getHeight());
        this.f896ve.draw(canvas);
        Path path = new Path();
        int i = this.padding;
        RectF rectF = new RectF(i + 0, i + 0, getWidth() - this.padding, getHeight() - this.padding);
        float f = this.cornerRadius;
        path.addRoundRect(rectF, f, f, Path.Direction.CW);
        int iSave = canvas.save();
        canvas.clipPath(path);
        int intrinsicWidth = this.drawable.getIntrinsicWidth();
        int intrinsicHeight = this.drawable.getIntrinsicHeight();
        int width = getWidth();
        int height = getHeight();
        float f2 = intrinsicWidth;
        float f3 = intrinsicHeight;
        float fMax = Math.max(width / f2, height / f3);
        int i2 = (int) (f2 * fMax);
        int i3 = (int) (f3 * fMax);
        int i4 = (width - i2) / 2;
        int i5 = (height - i3) / 2;
        new Rect(i4, i5, intrinsicWidth, intrinsicHeight);
        int i6 = this.padding;
        this.drawable.setBounds(new Rect(i4 + i6, i5 + i6, (i4 + i2) - i6, (i5 + i3) - i6));
        this.drawable.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        this.f896ve = drawable;
        super.setBackground(drawable);
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        this.drawable = drawable;
        super.setImageDrawable(drawable);
    }

    public RoundedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }
}
