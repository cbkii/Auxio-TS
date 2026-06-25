package com.p060tw.music.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.audiofx.Visualizer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import com.p060tw.music.MusicActivity;
import java.io.FileOutputStream;
import java.util.Locale;

/* loaded from: classes3.dex */
public class BaseVisualizerView extends View implements Visualizer.OnDataCaptureListener {
    protected byte[] mData;
    protected Paint mPaint;

    /* renamed from: pf */
    private int f1180pf;

    /* renamed from: qf */
    private int f1181qf;

    /* renamed from: rf */
    private int f1182rf;

    /* renamed from: sf */
    private float f1183sf;
    private float strokeWidth;

    /* renamed from: tf */
    protected Visualizer f1184tf;

    /* renamed from: uf */
    boolean f1185uf;

    /* renamed from: vf */
    private int f1186vf;

    /* renamed from: wf */
    private int f1187wf;

    public BaseVisualizerView(Context context) {
        super(context);
        this.f1180pf = 0;
        this.f1181qf = 0;
        this.f1182rf = 0;
        this.strokeWidth = 0.0f;
        this.f1183sf = 0.0f;
        this.f1184tf = null;
        this.mPaint = null;
        this.mData = new byte[13];
        this.f1185uf = true;
        this.f1186vf = -864368;
        this.f1187wf = -11581428;
        this.mPaint = new Paint();
    }

    /* renamed from: ha */
    public static void m1517ha(int i) {
        if (i == -16777216) {
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("/sys/class/tw/misc/led");
            try {
                fileOutputStream.write(String.format(Locale.US, "0x%08x", Integer.valueOf(i)).getBytes());
            } catch (Exception unused) {
            } catch (Throwable th) {
                fileOutputStream.close();
                throw th;
            }
            fileOutputStream.close();
        } catch (Exception unused2) {
        }
    }

    /* renamed from: a */
    protected void m1518a(Canvas canvas, float f, byte b2) {
        if (b2 == 0) {
            b2 = 1;
        }
        for (int i = 0; i < b2; i++) {
            int height = getHeight();
            int i2 = this.f1181qf;
            float f2 = (height - (i * i2)) - i2;
            int height2 = getHeight() / 2;
            this.mPaint.setColor(-1);
            canvas.drawLine(f, f2, f + this.f1183sf, f2, this.mPaint);
        }
    }

    /* renamed from: ga */
    public int m1519ga(int i) {
        int i2 = (i * 12) / 10;
        if (i2 > 255) {
            return 255;
        }
        return i2;
    }

    /* renamed from: j */
    public void m1520j(int i, int i2) {
        this.f1186vf = i;
        this.f1187wf = i2;
        if (this.f1186vf == 0) {
            this.f1186vf = -6830853;
        }
        if (this.f1187wf == 0) {
            this.f1187wf = -15387218;
        }
        Log.d("BaseVisualizerView", "setCylinderColor: " + Integer.toHexString(this.f1186vf));
        invalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        this.mPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, MusicActivity.f1016Dc, new int[]{this.f1186vf, this.f1187wf}, (float[]) null, Shader.TileMode.MIRROR));
        for (int i = 0; i < 2; i++) {
            float f = this.strokeWidth / 2.0f;
            int i2 = this.f1180pf;
            m1518a(canvas, f + i2 + (i * (i2 + this.f1183sf)), this.mData[i]);
        }
        int i3 = -4;
        for (int i4 = 13; i4 >= 2; i4--) {
            i3++;
            float f2 = this.strokeWidth / 2.0f;
            int i5 = this.f1180pf;
            m1518a(canvas, f2 + i5 + (((i3 + 6) - 1) * (i5 + this.f1183sf)), this.mData[i4 - 1]);
        }
    }

    @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
    public void onFftDataCapture(Visualizer visualizer, byte[] bArr, int i) {
        int i2 = 2;
        byte[] bArr2 = new byte[(bArr.length / 2) + 1];
        if (this.f1185uf) {
            bArr2[0] = (byte) Math.abs((int) bArr[1]);
            int i3 = 1;
            while (i2 < bArr.length) {
                bArr2[i3] = (byte) Math.hypot(bArr[i2], bArr[i2 + 1]);
                i2 += 2;
                i3++;
            }
        } else {
            for (int i4 = 0; i4 < 13; i4++) {
                bArr2[i4] = 0;
            }
        }
        for (int i5 = 0; i5 < 13; i5++) {
            byte abs = (byte) (Math.abs((int) bArr2[13 - i5]) / this.f1182rf);
            byte[] bArr3 = this.mData;
            byte b2 = bArr3[i5];
            if (abs > b2) {
                bArr3[i5] = abs;
            } else if (b2 > 0) {
                bArr3[i5] = (byte) (bArr3[i5] - 1);
            }
        }
        if (Settings.System.getInt(getContext().getContentResolver(), "Ambientlight", 0) == 3) {
            m1517ha(Color.rgb(m1519ga((this.mData[3] * 255) / 16), m1519ga((this.mData[7] * 255) / 16), m1519ga((this.mData[10] * 255) / 16)));
        }
        postInvalidate();
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        float f = i3 - i;
        float f2 = i4 - i2;
        this.strokeWidth = (f2 / 360.0f) * 25.0f;
        this.f1183sf = (f / 470.0f) * 15.0f;
        this.f1180pf = (int) ((f - (this.f1183sf * 13.0f)) / 14.0f);
        this.f1181qf = (int) ((f2 / 32.0f) * 2.0f);
        this.mPaint.setStrokeWidth(this.strokeWidth);
    }

    @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bArr, int i) {
    }

    public void setVisualizer(Visualizer visualizer) {
        if (visualizer != null) {
            if (!visualizer.getEnabled()) {
                visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
            }
            this.f1182rf = 7;
            visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, false, true);
            visualizer.setEnabled(true);
            this.f1184tf = visualizer;
            return;
        }
        Visualizer visualizer2 = this.f1184tf;
        if (visualizer2 != null) {
            visualizer2.setEnabled(false);
            this.f1184tf.release();
            this.f1184tf = null;
        }
    }
}
