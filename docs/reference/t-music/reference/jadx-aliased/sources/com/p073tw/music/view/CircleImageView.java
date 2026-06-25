package com.p073tw.music.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import com.p073tw.music.R;
import com.p073tw.music.R$styleable;

/* loaded from: classes3.dex */
public class CircleImageView extends ImageView {

    /* renamed from: Le */
    private static final ImageView.ScaleType f1188Le = ImageView.ScaleType.CENTER_INSIDE;

    /* renamed from: Me */
    private static final Bitmap.Config f1189Me = Bitmap.Config.ARGB_8888;

    /* renamed from: Ae */
    private int f1190Ae;

    /* renamed from: Be */
    private int f1191Be;

    /* renamed from: Ce */
    private float f1192Ce;

    /* renamed from: De */
    private float f1193De;

    /* renamed from: Ee */
    private boolean f1194Ee;

    /* renamed from: Fe */
    private boolean f1195Fe;

    /* renamed from: Ge */
    private boolean f1196Ge;

    /* renamed from: He */
    private float f1197He;

    /* renamed from: Ie */
    private int f1198Ie;

    /* renamed from: Je */
    private int f1199Je;

    /* renamed from: Ke */
    private C0795a f1200Ke;
    private float angle;
    private Bitmap mBitmap;
    private int mBitmapHeight;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private ColorFilter mColorFilter;
    private final Matrix mShaderMatrix;
    public int state;

    /* renamed from: we */
    private final RectF f1201we;

    /* renamed from: xe */
    private final RectF f1202xe;

    /* renamed from: ye */
    private final Paint f1203ye;

    /* renamed from: ze */
    private final Paint f1204ze;

    /* renamed from: com.tw.music.view.CircleImageView$a */
    public class C0795a extends RotateAnimation {
        public C0795a(float f, float f2, float f3, float f4) {
            super(f, f2, f3, f4);
        }

        @Override // android.view.animation.RotateAnimation, android.view.animation.Animation
        protected void applyTransformation(float f, Transformation transformation) {
            super.applyTransformation(f, transformation);
            CircleImageView.this.angle = f * 360.0f;
        }
    }

    public CircleImageView(Context context) {
        super(context);
        this.f1201we = new RectF();
        this.f1202xe = new RectF();
        this.mShaderMatrix = new Matrix();
        this.f1203ye = new Paint();
        this.f1204ze = new Paint();
        this.f1190Ae = ViewCompat.MEASURED_STATE_MASK;
        this.f1191Be = 0;
        init();
    }

    /* renamed from: De */
    private void m1521De() {
        float width;
        float height;
        this.mShaderMatrix.set(null);
        float f = 0.0f;
        if (this.mBitmapWidth * this.f1201we.height() > this.f1201we.width() * this.mBitmapHeight) {
            width = this.f1201we.height() / this.mBitmapHeight;
            height = 0.0f;
            f = (this.f1201we.width() - (this.mBitmapWidth * width)) * 0.5f;
        } else {
            width = this.f1201we.width() / this.mBitmapWidth;
            height = (this.f1201we.height() - (this.mBitmapHeight * width)) * 0.5f;
        }
        this.mShaderMatrix.setScale(width, width);
        Matrix matrix = this.mShaderMatrix;
        RectF rectF = this.f1201we;
        matrix.postTranslate(((int) (f + 0.5f)) + rectF.left, ((int) (height + 0.5f)) + rectF.top);
        this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
    }

    /* renamed from: J */
    private Bitmap m1522J(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap createBitmap = drawable instanceof ColorDrawable ? Bitmap.createBitmap(2, 2, f1189Me) : Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), f1189Me);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return createBitmap;
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    private void setup() {
        if (!this.f1194Ee) {
            this.f1195Fe = true;
            return;
        }
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null) {
            return;
        }
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        this.mBitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        this.f1203ye.setAntiAlias(true);
        this.f1203ye.setShader(this.mBitmapShader);
        this.f1204ze.setStyle(Paint.Style.STROKE);
        this.f1204ze.setAntiAlias(true);
        this.f1204ze.setColor(this.f1190Ae);
        this.f1204ze.setStrokeWidth(this.f1191Be);
        this.mBitmapHeight = this.mBitmap.getHeight();
        this.mBitmapWidth = this.mBitmap.getWidth();
        this.f1202xe.set(0.0f, 0.0f, getWidth(), getHeight());
        this.f1193De = Math.min((this.f1202xe.height() - this.f1191Be) / 2.0f, (this.f1202xe.width() - this.f1191Be) / 2.0f);
        this.f1201we.set(this.f1202xe);
        if (!this.f1196Ge) {
            RectF rectF = this.f1201we;
            int i = this.f1191Be;
            rectF.inset(i, i);
        }
        this.f1192Ce = Math.min(this.f1201we.height() / 2.0f, this.f1201we.width() / 2.0f);
        m1521De();
        invalidate();
    }

    /* renamed from: Ua */
    public void m1524Ua() {
        this.f1197He = (float) (((this.f1197He + this.angle) % 360.0f) - 0.2d);
        C0795a c0795a = this.f1200Ke;
        if (c0795a != null) {
            c0795a.cancel();
        }
        this.state = 2;
        invalidate();
    }

    /* renamed from: Va */
    public void m1525Va() {
        this.f1200Ke = new C0795a(0.0f, 360.0f, getResources().getDimension(R.dimen.tw_dp_w80), getResources().getDimension(R.dimen.tw_dp_w80));
        this.f1200Ke.setDuration(8000L);
        this.f1200Ke.setInterpolator(new LinearInterpolator());
        this.f1200Ke.setRepeatCount(-1);
        startAnimation(this.f1200Ke);
        this.state = 1;
    }

    public int getBorderColor() {
        return this.f1190Ae;
    }

    public int getBorderWidth() {
        return this.f1191Be;
    }

    @Override // android.widget.ImageView
    public ImageView.ScaleType getScaleType() {
        return f1188Le;
    }

    public void init() {
        super.setScaleType(f1188Le);
        this.f1194Ee = true;
        this.state = 3;
        this.angle = 0.0f;
        this.f1197He = 0.0f;
        this.f1198Ie = 0;
        this.f1199Je = 0;
        if (this.f1195Fe) {
            setup();
            this.f1195Fe = false;
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        canvas.rotate(this.f1197He, this.f1198Ie / 2, this.f1199Je / 2);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, this.f1192Ce, this.f1203ye);
        if (this.f1191Be != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, this.f1193De, this.f1204ze);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.f1198Ie = getMeasuredWidth();
        this.f1199Je = getMeasuredHeight();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setup();
    }

    @Override // android.widget.ImageView
    public void setAdjustViewBounds(boolean z) {
        if (z) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    public void setBorderColor(int i) {
        if (i == this.f1190Ae) {
            return;
        }
        this.f1190Ae = i;
        this.f1204ze.setColor(this.f1190Ae);
        invalidate();
    }

    public void setBorderColorResource(int i) {
        setBorderColor(getContext().getResources().getColor(i));
    }

    public void setBorderOverlay(boolean z) {
        if (z == this.f1196Ge) {
            return;
        }
        this.f1196Ge = z;
        setup();
    }

    public void setBorderWidth(int i) {
        if (i == this.f1191Be) {
            return;
        }
        this.f1191Be = i;
        setup();
    }

    @Override // android.widget.ImageView
    public void setColorFilter(ColorFilter colorFilter) {
        if (colorFilter == this.mColorFilter) {
            return;
        }
        this.mColorFilter = colorFilter;
        this.f1203ye.setColorFilter(this.mColorFilter);
        invalidate();
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.mBitmap = bitmap;
        setup();
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        this.mBitmap = m1522J(drawable);
        setup();
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i) {
        super.setImageResource(i);
        this.mBitmap = m1522J(getDrawable());
        setup();
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.mBitmap = m1522J(getDrawable());
        setup();
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType != f1188Le) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    public CircleImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f1201we = new RectF();
        this.f1202xe = new RectF();
        this.mShaderMatrix = new Matrix();
        this.f1203ye = new Paint();
        this.f1204ze = new Paint();
        this.f1190Ae = ViewCompat.MEASURED_STATE_MASK;
        this.f1191Be = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CircleImageView, i, 0);
        this.f1191Be = obtainStyledAttributes.getDimensionPixelSize(2, 0);
        this.f1190Ae = obtainStyledAttributes.getColor(0, ViewCompat.MEASURED_STATE_MASK);
        this.f1196Ge = obtainStyledAttributes.getBoolean(1, false);
        obtainStyledAttributes.recycle();
        init();
    }
}
