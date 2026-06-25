package tv.danmaku.ijk.media.player.p069tw;

import android.view.View;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes4.dex */
public final class MeasureHelper {
    private int mCurrentAspectRatio = 0;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private WeakReference<View> mWeakView;

    public MeasureHelper(View view) {
        this.mWeakView = new WeakReference<>(view);
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0074 A[PHI: r5
      0x0074: PHI (r5v3 float) = (r5v2 float), (r5v5 float) binds: [B:30:0x0071, B:26:0x0065] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00ec A[PHI: r1 r12
      0x00ec: PHI (r1v6 int) = (r1v3 int), (r1v3 int), (r1v9 int), (r1v9 int) binds: [B:77:0x00fa, B:78:0x00fc, B:68:0x00e7, B:69:0x00e9] A[DONT_GENERATE, DONT_INLINE]
      0x00ec: PHI (r12v11 int) = (r12v7 int), (r12v7 int), (r12v5 int), (r12v5 int) binds: [B:77:0x00fa, B:78:0x00fc, B:68:0x00e7, B:69:0x00e9] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void doMeasure(int i, int i2) {
        int i3;
        float f;
        float f2;
        int i4;
        int i5;
        int i6 = this.mVideoRotationDegree;
        if (i6 == 90 || i6 == 270) {
            i2 = i;
            i = i2;
        }
        int defaultSize = View.getDefaultSize(this.mVideoWidth, i);
        int defaultSize2 = View.getDefaultSize(this.mVideoHeight, i2);
        if (this.mCurrentAspectRatio != 3) {
            if (this.mVideoWidth <= 0 || this.mVideoHeight <= 0) {
                i = defaultSize;
                i2 = defaultSize2;
            } else {
                int mode = View.MeasureSpec.getMode(i);
                i = View.MeasureSpec.getSize(i);
                int mode2 = View.MeasureSpec.getMode(i2);
                i2 = View.MeasureSpec.getSize(i2);
                if (mode == Integer.MIN_VALUE && mode2 == Integer.MIN_VALUE) {
                    float f3 = i;
                    float f4 = i2;
                    float f5 = f3 / f4;
                    int i7 = this.mCurrentAspectRatio;
                    if (i7 != 4) {
                        if (i7 != 5) {
                            f2 = this.mVideoWidth / this.mVideoHeight;
                            int i8 = this.mVideoSarNum;
                            if (i8 > 0 && (i5 = this.mVideoSarDen) > 0) {
                                f2 = (f2 * i8) / i5;
                            }
                        } else {
                            f = 1.3333334f;
                            int i9 = this.mVideoRotationDegree;
                            f2 = (i9 == 90 || i9 == 270) ? 0.75f : f;
                        }
                        boolean z = f2 <= f5;
                        i4 = this.mCurrentAspectRatio;
                        if (i4 == 0) {
                            if (z) {
                                i = (int) (f4 * f2);
                            } else {
                                i2 = (int) (f3 / f2);
                            }
                        } else if (i4 != 1) {
                            if (i4 != 4 && i4 != 5) {
                                if (z) {
                                    i = Math.min(this.mVideoWidth, i);
                                    i2 = (int) (i / f2);
                                } else {
                                    int iMin = Math.min(this.mVideoHeight, i2);
                                    i2 = iMin;
                                    i = (int) (iMin * f2);
                                }
                            }
                            if (z) {
                            }
                        } else if (z) {
                        }
                    } else {
                        f = 1.7777778f;
                        int i10 = this.mVideoRotationDegree;
                        if (i10 == 90 || i10 == 270) {
                            f2 = 0.5625f;
                        }
                        if (f2 <= f5) {
                        }
                        i4 = this.mCurrentAspectRatio;
                        if (i4 == 0) {
                        }
                    }
                } else if (mode == 1073741824 && mode2 == 1073741824) {
                    int i11 = this.mVideoWidth;
                    int i12 = i11 * i2;
                    int i13 = this.mVideoHeight;
                    if (i12 < i * i13) {
                        i = (i11 * i2) / i13;
                    } else if (i11 * i2 > i * i13) {
                        i2 = (i13 * i) / i11;
                    }
                } else if (mode == 1073741824) {
                    int i14 = (this.mVideoHeight * i) / this.mVideoWidth;
                    if (mode2 != Integer.MIN_VALUE || i14 <= i2) {
                        i2 = i14;
                    }
                } else if (mode2 == 1073741824) {
                    i3 = (this.mVideoWidth * i2) / this.mVideoHeight;
                    if (mode != Integer.MIN_VALUE || i3 <= i) {
                        i = i3;
                    }
                } else {
                    i3 = this.mVideoWidth;
                    int i15 = this.mVideoHeight;
                    if (mode2 != Integer.MIN_VALUE || i15 <= i2) {
                        i2 = i15;
                    } else {
                        i3 = (i3 * i2) / i15;
                    }
                    if (mode == Integer.MIN_VALUE && i3 > i) {
                        i2 = (this.mVideoHeight * i) / this.mVideoWidth;
                    }
                }
            }
        }
        this.mMeasuredWidth = i;
        this.mMeasuredHeight = i2;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public View getView() {
        WeakReference<View> weakReference = this.mWeakView;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public void setAspectRatio(int i) {
        this.mCurrentAspectRatio = i;
    }

    public void setVideoRotation(int i) {
        this.mVideoRotationDegree = i;
    }

    public void setVideoSampleAspectRatio(int i, int i2) {
        this.mVideoSarNum = i;
        this.mVideoSarDen = i2;
    }

    public void setVideoSize(int i, int i2) {
        this.mVideoWidth = i;
        this.mVideoHeight = i2;
    }
}
