package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0443R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes3.dex */
public class LinearLayoutCompat extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface DividerMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface OrientationMode {
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((ViewGroup.MarginLayoutParams) layoutParams).height == -1) {
                    int i4 = ((ViewGroup.MarginLayoutParams) layoutParams).width;
                    ((ViewGroup.MarginLayoutParams) layoutParams).width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    ((ViewGroup.MarginLayoutParams) layoutParams).width = i4;
                }
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((ViewGroup.MarginLayoutParams) layoutParams).width == -1) {
                    int i4 = ((ViewGroup.MarginLayoutParams) layoutParams).height;
                    ((ViewGroup.MarginLayoutParams) layoutParams).height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    ((ViewGroup.MarginLayoutParams) layoutParams).height = i4;
                }
            }
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    void drawDividersHorizontal(Canvas canvas) {
        int right;
        int left;
        int i;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i2)) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                drawVerticalDivider(canvas, isLayoutRtl ? virtualChildAt.getRight() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin : (virtualChildAt.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin) - this.mDividerWidth);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    left = virtualChildAt2.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin;
                    i = this.mDividerWidth;
                    right = left - i;
                } else {
                    right = virtualChildAt2.getRight() + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin;
                }
            } else if (isLayoutRtl) {
                right = getPaddingLeft();
            } else {
                left = getWidth() - getPaddingRight();
                i = this.mDividerWidth;
                right = left - i;
            }
            drawVerticalDivider(canvas, right);
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int virtualChildCount = getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; i++) {
            View virtualChildAt = getVirtualChildAt(i);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((ViewGroup.MarginLayoutParams) ((LayoutParams) virtualChildAt.getLayoutParams())).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            drawHorizontalDivider(canvas, virtualChildAt2 == null ? (getHeight() - getPaddingBottom()) - this.mDividerHeight : virtualChildAt2.getBottom() + ((ViewGroup.MarginLayoutParams) ((LayoutParams) virtualChildAt2.getLayoutParams())).bottomMargin);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    @Override // android.view.View
    public int getBaseline() {
        int i;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i2 = this.mBaselineAlignedChildIndex;
        if (childCount <= i2) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        View childAt = getChildAt(i2);
        int baseline = childAt.getBaseline();
        if (baseline == -1) {
            if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            }
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
        }
        int i3 = this.mBaselineChildTop;
        if (this.mOrientation == 1 && (i = this.mGravity & 112) != 48) {
            if (i == 16) {
                i3 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
            } else if (i == 80) {
                i3 = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
            }
        }
        return i3 + ((ViewGroup.MarginLayoutParams) ((LayoutParams) childAt.getLayoutParams())).topMargin + baseline;
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getGravity() {
        return this.mGravity;
    }

    int getLocationOffset(View view) {
        return 0;
    }

    int getNextLocationOffset(View view) {
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    protected boolean hasDividerBeforeChildAt(int i) {
        if (i == 0) {
            return (this.mShowDividers & 1) != 0;
        }
        if (i == getChildCount()) {
            return (this.mShowDividers & 4) != 0;
        }
        if ((this.mShowDividers & 2) == 0) {
            return false;
        }
        for (int i2 = i - 1; i2 >= 0; i2--) {
            if (getChildAt(i2).getVisibility() != 8) {
                return true;
            }
        }
        return false;
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00ec  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        boolean z;
        int i10;
        int i11;
        int i12;
        int i13;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop = getPaddingTop();
        int i14 = i4 - i2;
        int paddingBottom = i14 - getPaddingBottom();
        int paddingBottom2 = (i14 - paddingTop) - getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i15 = this.mGravity;
        int i16 = i15 & 112;
        boolean z2 = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(8388615 & i15, ViewCompat.getLayoutDirection(this));
        boolean z3 = true;
        int paddingLeft = absoluteGravity != 1 ? absoluteGravity != 5 ? getPaddingLeft() : ((getPaddingLeft() + i3) - i) - this.mTotalLength : getPaddingLeft() + (((i3 - i) - this.mTotalLength) / 2);
        if (isLayoutRtl) {
            i5 = virtualChildCount - 1;
            i6 = -1;
        } else {
            i5 = 0;
            i6 = 1;
        }
        int i17 = 0;
        while (i17 < virtualChildCount) {
            int i18 = i5 + (i6 * i17);
            View virtualChildAt = getVirtualChildAt(i18);
            if (virtualChildAt == null) {
                paddingLeft += measureNullChild(i18);
                z = z3;
                i7 = paddingTop;
                i8 = virtualChildCount;
                i9 = i16;
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i19 = i17;
                if (z2) {
                    i8 = virtualChildCount;
                    if (((ViewGroup.MarginLayoutParams) layoutParams).height != -1) {
                        i10 = virtualChildAt.getBaseline();
                        i11 = layoutParams.gravity;
                        if (i11 < 0) {
                            i11 = i16;
                        }
                        i12 = i11 & 112;
                        i9 = i16;
                        if (i12 != 16) {
                            z = true;
                            i13 = ((((paddingBottom2 - measuredHeight) / 2) + paddingTop) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                        } else if (i12 != 48) {
                            if (i12 != 80) {
                                i13 = paddingTop;
                            } else {
                                int i20 = (paddingBottom - measuredHeight) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                                if (i10 != -1) {
                                    i20 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - i10);
                                }
                                i13 = i20;
                            }
                            z = true;
                        } else {
                            int i21 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + paddingTop;
                            z = true;
                            if (i10 != -1) {
                                i21 += iArr[1] - i10;
                            }
                            i13 = i21;
                        }
                        if (hasDividerBeforeChildAt(i18)) {
                            paddingLeft += this.mDividerWidth;
                        }
                        int i22 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + paddingLeft;
                        i7 = paddingTop;
                        setChildFrame(virtualChildAt, i22 + getLocationOffset(virtualChildAt), i13, measuredWidth, measuredHeight);
                        int nextLocationOffset = i22 + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                        i17 = i19 + getChildrenSkipCount(virtualChildAt, i18);
                        paddingLeft = nextLocationOffset;
                        i17++;
                        virtualChildCount = i8;
                        i16 = i9;
                        z3 = z;
                        paddingTop = i7;
                    }
                } else {
                    i8 = virtualChildCount;
                }
                i10 = -1;
                i11 = layoutParams.gravity;
                if (i11 < 0) {
                }
                i12 = i11 & 112;
                i9 = i16;
                if (i12 != 16) {
                }
                if (hasDividerBeforeChildAt(i18)) {
                }
                int i222 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + paddingLeft;
                i7 = paddingTop;
                setChildFrame(virtualChildAt, i222 + getLocationOffset(virtualChildAt), i13, measuredWidth, measuredHeight);
                int nextLocationOffset2 = i222 + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                i17 = i19 + getChildrenSkipCount(virtualChildAt, i18);
                paddingLeft = nextLocationOffset2;
                i17++;
                virtualChildCount = i8;
                i16 = i9;
                z3 = z;
                paddingTop = i7;
            } else {
                i7 = paddingTop;
                i8 = virtualChildCount;
                i9 = i16;
                z = true;
            }
            i17++;
            virtualChildCount = i8;
            i16 = i9;
            z3 = z;
            paddingTop = i7;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00a0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int paddingLeft = getPaddingLeft();
        int i9 = i3 - i;
        int paddingRight = i9 - getPaddingRight();
        int paddingRight2 = (i9 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i10 = this.mGravity;
        int i11 = i10 & 112;
        int i12 = i10 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int paddingTop = i11 != 16 ? i11 != 80 ? getPaddingTop() : ((getPaddingTop() + i4) - i2) - this.mTotalLength : getPaddingTop() + (((i4 - i2) - this.mTotalLength) / 2);
        int i13 = 0;
        while (i13 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i13);
            if (virtualChildAt == null) {
                paddingTop += measureNullChild(i13);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i14 = layoutParams.gravity;
                if (i14 < 0) {
                    i14 = i12;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(i14, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    i5 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                    i6 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                } else if (absoluteGravity != 5) {
                    i7 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + paddingLeft;
                    int i15 = i7;
                    if (hasDividerBeforeChildAt(i13)) {
                        paddingTop += this.mDividerHeight;
                    }
                    int i16 = paddingTop + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                    setChildFrame(virtualChildAt, i15, i16 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                    int nextLocationOffset = i16 + measuredHeight + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                    i13 += getChildrenSkipCount(virtualChildAt, i13);
                    paddingTop = nextLocationOffset;
                    i8 = 1;
                    i13 += i8;
                } else {
                    i5 = paddingRight - measuredWidth;
                    i6 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                }
                i7 = i5 - i6;
                int i152 = i7;
                if (hasDividerBeforeChildAt(i13)) {
                }
                int i162 = paddingTop + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                setChildFrame(virtualChildAt, i152, i162 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                int nextLocationOffset2 = i162 + measuredHeight + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                i13 += getChildrenSkipCount(virtualChildAt, i13);
                paddingTop = nextLocationOffset2;
                i8 = 1;
                i13 += i8;
            }
            i8 = 1;
            i13 += i8;
        }
    }

    void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureHorizontal(int i, int i2) {
        int[] iArr;
        int i3;
        int i4;
        int i5;
        int max;
        int i6;
        int i7;
        int i8;
        float f;
        int i9;
        boolean z;
        int baseline;
        int i10;
        int i11;
        char c2;
        int i12;
        int i13;
        boolean z2;
        boolean z3;
        View view;
        int i14;
        boolean z4;
        int measuredHeight;
        int baseline2;
        int i15;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] iArr2 = this.mMaxAscent;
        int[] iArr3 = this.mMaxDescent;
        iArr2[3] = -1;
        iArr2[2] = -1;
        iArr2[1] = -1;
        iArr2[0] = -1;
        iArr3[3] = -1;
        iArr3[2] = -1;
        iArr3[1] = -1;
        iArr3[0] = -1;
        boolean z5 = this.mBaselineAligned;
        boolean z6 = this.mUseLargestChild;
        int i16 = 1073741824;
        boolean z7 = mode == 1073741824;
        int i17 = 0;
        int i18 = 0;
        int i19 = 0;
        int i20 = 0;
        int i21 = 0;
        boolean z8 = false;
        int i22 = 0;
        boolean z9 = false;
        boolean z10 = true;
        float f2 = 0.0f;
        while (true) {
            iArr = iArr3;
            if (i17 >= virtualChildCount) {
                break;
            }
            View virtualChildAt = getVirtualChildAt(i17);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(i17);
            } else if (virtualChildAt.getVisibility() == 8) {
                i17 += getChildrenSkipCount(virtualChildAt, i17);
            } else {
                if (hasDividerBeforeChildAt(i17)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                float f3 = f2 + layoutParams.weight;
                if (mode == i16 && ((ViewGroup.MarginLayoutParams) layoutParams).width == 0 && layoutParams.weight > 0.0f) {
                    if (z7) {
                        this.mTotalLength += ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                    } else {
                        int i23 = this.mTotalLength;
                        this.mTotalLength = Math.max(i23, ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + i23 + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin);
                    }
                    if (z5) {
                        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChildAt.measure(makeMeasureSpec, makeMeasureSpec);
                        i13 = i17;
                        z2 = z6;
                        z3 = z5;
                        view = virtualChildAt;
                    } else {
                        i13 = i17;
                        z2 = z6;
                        z3 = z5;
                        view = virtualChildAt;
                        z8 = true;
                        i14 = 1073741824;
                        if (mode2 == i14 && ((ViewGroup.MarginLayoutParams) layoutParams).height == -1) {
                            z4 = true;
                            z9 = true;
                        } else {
                            z4 = false;
                        }
                        int i24 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                        measuredHeight = view.getMeasuredHeight() + i24;
                        int combineMeasuredStates = View.combineMeasuredStates(i22, view.getMeasuredState());
                        if (z3 && (baseline2 = view.getBaseline()) != -1) {
                            i15 = layoutParams.gravity;
                            if (i15 < 0) {
                                i15 = this.mGravity;
                            }
                            int i25 = (((i15 & 112) >> 4) & (-2)) >> 1;
                            iArr2[i25] = Math.max(iArr2[i25], baseline2);
                            iArr[i25] = Math.max(iArr[i25], measuredHeight - baseline2);
                        }
                        int max2 = Math.max(i19, measuredHeight);
                        boolean z11 = !z10 && ((ViewGroup.MarginLayoutParams) layoutParams).height == -1;
                        if (layoutParams.weight <= 0.0f) {
                            if (!z4) {
                                i24 = measuredHeight;
                            }
                            i21 = Math.max(i21, i24);
                        } else {
                            int i26 = i21;
                            if (z4) {
                                measuredHeight = i24;
                            }
                            i20 = Math.max(i20, measuredHeight);
                            i21 = i26;
                        }
                        int i27 = i13;
                        i19 = max2;
                        i22 = combineMeasuredStates;
                        z10 = z11;
                        i17 = getChildrenSkipCount(view, i27) + i27;
                        f2 = f3;
                        i17++;
                        iArr3 = iArr;
                        z6 = z2;
                        z5 = z3;
                        i16 = 1073741824;
                    }
                } else {
                    if (((ViewGroup.MarginLayoutParams) layoutParams).width != 0 || layoutParams.weight <= 0.0f) {
                        c2 = 65534;
                        i12 = Integer.MIN_VALUE;
                    } else {
                        c2 = 65534;
                        ((ViewGroup.MarginLayoutParams) layoutParams).width = -2;
                        i12 = 0;
                    }
                    i13 = i17;
                    int i28 = i12;
                    z2 = z6;
                    z3 = z5;
                    measureChildBeforeLayout(virtualChildAt, i13, i, f3 == 0.0f ? this.mTotalLength : 0, i2, 0);
                    if (i28 != Integer.MIN_VALUE) {
                        ((ViewGroup.MarginLayoutParams) layoutParams).width = i28;
                    }
                    int measuredWidth = virtualChildAt.getMeasuredWidth();
                    if (z7) {
                        view = virtualChildAt;
                        this.mTotalLength += ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(view);
                    } else {
                        view = virtualChildAt;
                        int i29 = this.mTotalLength;
                        this.mTotalLength = Math.max(i29, i29 + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(view));
                    }
                    if (z2) {
                        i18 = Math.max(measuredWidth, i18);
                    }
                }
                i14 = 1073741824;
                if (mode2 == i14) {
                }
                z4 = false;
                int i242 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                measuredHeight = view.getMeasuredHeight() + i242;
                int combineMeasuredStates2 = View.combineMeasuredStates(i22, view.getMeasuredState());
                if (z3) {
                    i15 = layoutParams.gravity;
                    if (i15 < 0) {
                    }
                    int i252 = (((i15 & 112) >> 4) & (-2)) >> 1;
                    iArr2[i252] = Math.max(iArr2[i252], baseline2);
                    iArr[i252] = Math.max(iArr[i252], measuredHeight - baseline2);
                }
                int max22 = Math.max(i19, measuredHeight);
                if (z10) {
                }
                if (layoutParams.weight <= 0.0f) {
                }
                int i272 = i13;
                i19 = max22;
                i22 = combineMeasuredStates2;
                z10 = z11;
                i17 = getChildrenSkipCount(view, i272) + i272;
                f2 = f3;
                i17++;
                iArr3 = iArr;
                z6 = z2;
                z5 = z3;
                i16 = 1073741824;
            }
            z2 = z6;
            z3 = z5;
            i17++;
            iArr3 = iArr;
            z6 = z2;
            z5 = z3;
            i16 = 1073741824;
        }
        boolean z12 = z6;
        boolean z13 = z5;
        int i30 = i19;
        int i31 = i20;
        int i32 = i21;
        int i33 = i22;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
            i3 = i33;
        } else {
            i3 = i33;
            i30 = Math.max(i30, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
        }
        if (z12 && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            int i34 = 0;
            while (i34 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(i34);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(i34);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    i34 += getChildrenSkipCount(virtualChildAt2, i34);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    if (z7) {
                        this.mTotalLength += ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + i18 + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin + getNextLocationOffset(virtualChildAt2);
                    } else {
                        int i35 = this.mTotalLength;
                        i11 = i30;
                        this.mTotalLength = Math.max(i35, i35 + i18 + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin + getNextLocationOffset(virtualChildAt2));
                        i34++;
                        i30 = i11;
                    }
                }
                i11 = i30;
                i34++;
                i30 = i11;
            }
        }
        int i36 = i30;
        this.mTotalLength += getPaddingLeft() + getPaddingRight();
        int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumWidth()), i, 0);
        int i37 = (16777215 & resolveSizeAndState) - this.mTotalLength;
        if (z8 || (i37 != 0 && f2 > 0.0f)) {
            float f4 = this.mWeightSum;
            if (f4 > 0.0f) {
                f2 = f4;
            }
            iArr2[3] = -1;
            iArr2[2] = -1;
            iArr2[1] = -1;
            iArr2[0] = -1;
            iArr[3] = -1;
            iArr[2] = -1;
            iArr[1] = -1;
            iArr[0] = -1;
            this.mTotalLength = 0;
            int i38 = i31;
            int i39 = -1;
            int i40 = i3;
            float f5 = f2;
            int i41 = 0;
            while (i41 < virtualChildCount) {
                View virtualChildAt3 = getVirtualChildAt(i41);
                if (virtualChildAt3 == null || virtualChildAt3.getVisibility() == 8) {
                    i7 = i37;
                    i8 = virtualChildCount;
                } else {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                    float f6 = layoutParams3.weight;
                    if (f6 > 0.0f) {
                        int i42 = (int) ((i37 * f6) / f5);
                        float f7 = f5 - f6;
                        int i43 = i37 - i42;
                        i8 = virtualChildCount;
                        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom() + ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin, ((ViewGroup.MarginLayoutParams) layoutParams3).height);
                        if (((ViewGroup.MarginLayoutParams) layoutParams3).width == 0) {
                            i10 = 1073741824;
                            if (mode == 1073741824) {
                                if (i42 <= 0) {
                                    i42 = 0;
                                }
                                virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(i42, 1073741824), childMeasureSpec);
                                i40 = View.combineMeasuredStates(i40, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                                f5 = f7;
                                i7 = i43;
                            }
                        } else {
                            i10 = 1073741824;
                        }
                        int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i42;
                        if (measuredWidth2 < 0) {
                            measuredWidth2 = 0;
                        }
                        virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth2, i10), childMeasureSpec);
                        i40 = View.combineMeasuredStates(i40, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                        f5 = f7;
                        i7 = i43;
                    } else {
                        i7 = i37;
                        i8 = virtualChildCount;
                    }
                    if (z7) {
                        this.mTotalLength += virtualChildAt3.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin + getNextLocationOffset(virtualChildAt3);
                        f = f5;
                    } else {
                        int i44 = this.mTotalLength;
                        f = f5;
                        this.mTotalLength = Math.max(i44, virtualChildAt3.getMeasuredWidth() + i44 + ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin + getNextLocationOffset(virtualChildAt3));
                    }
                    boolean z14 = mode2 != 1073741824 && ((ViewGroup.MarginLayoutParams) layoutParams3).height == -1;
                    int i45 = ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin;
                    int measuredHeight2 = virtualChildAt3.getMeasuredHeight() + i45;
                    i39 = Math.max(i39, measuredHeight2);
                    if (!z14) {
                        i45 = measuredHeight2;
                    }
                    int max3 = Math.max(i38, i45);
                    if (z10) {
                        i9 = -1;
                        if (((ViewGroup.MarginLayoutParams) layoutParams3).height == -1) {
                            z = true;
                            if (!z13 && (baseline = virtualChildAt3.getBaseline()) != i9) {
                                int i46 = layoutParams3.gravity;
                                if (i46 < 0) {
                                    i46 = this.mGravity;
                                }
                                int i47 = (((i46 & 112) >> 4) & (-2)) >> 1;
                                iArr2[i47] = Math.max(iArr2[i47], baseline);
                                iArr[i47] = Math.max(iArr[i47], measuredHeight2 - baseline);
                            }
                            i38 = max3;
                            z10 = z;
                            f5 = f;
                        }
                    } else {
                        i9 = -1;
                    }
                    z = false;
                    if (!z13) {
                    }
                    i38 = max3;
                    z10 = z;
                    f5 = f;
                }
                i41++;
                i37 = i7;
                virtualChildCount = i8;
            }
            i4 = i2;
            i5 = virtualChildCount;
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            max = (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) ? i39 : Math.max(i39, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
            i3 = i40;
            i6 = i38;
        } else {
            i6 = Math.max(i31, i32);
            if (z12 && mode != 1073741824) {
                for (int i48 = 0; i48 < virtualChildCount; i48++) {
                    View virtualChildAt4 = getVirtualChildAt(i48);
                    if (virtualChildAt4 != null && virtualChildAt4.getVisibility() != 8 && ((LayoutParams) virtualChildAt4.getLayoutParams()).weight > 0.0f) {
                        virtualChildAt4.measure(View.MeasureSpec.makeMeasureSpec(i18, 1073741824), View.MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredHeight(), 1073741824));
                    }
                }
            }
            i4 = i2;
            i5 = virtualChildCount;
            max = i36;
        }
        if (z10 || mode2 == 1073741824) {
            i6 = max;
        }
        setMeasuredDimension(resolveSizeAndState | (i3 & ViewCompat.MEASURED_STATE_MASK), View.resolveSizeAndState(Math.max(i6 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i4, i3 << 16));
        if (z9) {
            forceUniformHeight(i5, i);
        }
    }

    int measureNullChild(int i) {
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:158:0x0333  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureVertical(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        float f;
        int i8;
        int i9;
        boolean z;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        View view;
        int max;
        boolean z2;
        int max2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int i19 = this.mBaselineAlignedChildIndex;
        boolean z3 = this.mUseLargestChild;
        int i20 = 0;
        int i21 = 0;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        int i25 = 0;
        boolean z4 = false;
        boolean z5 = false;
        float f2 = 0.0f;
        boolean z6 = true;
        while (true) {
            int i26 = 8;
            int i27 = i23;
            if (i25 >= virtualChildCount) {
                int i28 = i20;
                int i29 = i22;
                int i30 = i24;
                int i31 = virtualChildCount;
                int i32 = i21;
                int i33 = mode2;
                if (this.mTotalLength > 0) {
                    i3 = i31;
                    if (hasDividerBeforeChildAt(i3)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i3 = i31;
                }
                if (z3 && (i33 == Integer.MIN_VALUE || i33 == 0)) {
                    this.mTotalLength = 0;
                    int i34 = 0;
                    while (i34 < i3) {
                        View virtualChildAt = getVirtualChildAt(i34);
                        if (virtualChildAt == null) {
                            this.mTotalLength += measureNullChild(i34);
                        } else if (virtualChildAt.getVisibility() == i26) {
                            i34 += getChildrenSkipCount(virtualChildAt, i34);
                        } else {
                            LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                            int i35 = this.mTotalLength;
                            this.mTotalLength = Math.max(i35, i35 + i29 + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt));
                        }
                        i34++;
                        i26 = 8;
                    }
                }
                this.mTotalLength += getPaddingTop() + getPaddingBottom();
                int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), i2, 0);
                int i36 = (16777215 & resolveSizeAndState) - this.mTotalLength;
                if (z4 || (i36 != 0 && f2 > 0.0f)) {
                    float f3 = this.mWeightSum;
                    if (f3 > 0.0f) {
                        f2 = f3;
                    }
                    this.mTotalLength = 0;
                    float f4 = f2;
                    int i37 = 0;
                    int i38 = i30;
                    i4 = i28;
                    while (i37 < i3) {
                        View virtualChildAt2 = getVirtualChildAt(i37);
                        if (virtualChildAt2.getVisibility() == 8) {
                            f = f4;
                        } else {
                            LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            float f5 = layoutParams2.weight;
                            if (f5 > 0.0f) {
                                int i39 = (int) ((i36 * f5) / f4);
                                i7 = i36 - i39;
                                f = f4 - f5;
                                int childMeasureSpec = ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams2).width);
                                if (((ViewGroup.MarginLayoutParams) layoutParams2).height == 0) {
                                    i10 = 1073741824;
                                    if (i33 == 1073741824) {
                                        if (i39 <= 0) {
                                            i39 = 0;
                                        }
                                        virtualChildAt2.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(i39, 1073741824));
                                        i4 = View.combineMeasuredStates(i4, virtualChildAt2.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                                    }
                                } else {
                                    i10 = 1073741824;
                                }
                                int measuredHeight = virtualChildAt2.getMeasuredHeight() + i39;
                                if (measuredHeight < 0) {
                                    measuredHeight = 0;
                                }
                                virtualChildAt2.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(measuredHeight, i10));
                                i4 = View.combineMeasuredStates(i4, virtualChildAt2.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                            } else {
                                i7 = i36;
                                f = f4;
                            }
                            int i40 = ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin;
                            int measuredWidth = virtualChildAt2.getMeasuredWidth() + i40;
                            i32 = Math.max(i32, measuredWidth);
                            if (mode != 1073741824) {
                                i8 = i4;
                                i9 = -1;
                                if (((ViewGroup.MarginLayoutParams) layoutParams2).width == -1) {
                                    z = true;
                                    if (!z) {
                                        i40 = measuredWidth;
                                    }
                                    i38 = Math.max(i38, i40);
                                    boolean z7 = !z6 && ((ViewGroup.MarginLayoutParams) layoutParams2).width == i9;
                                    int i41 = this.mTotalLength;
                                    this.mTotalLength = Math.max(i41, virtualChildAt2.getMeasuredHeight() + i41 + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin + getNextLocationOffset(virtualChildAt2));
                                    z6 = z7;
                                    i36 = i7;
                                    i4 = i8;
                                }
                            } else {
                                i8 = i4;
                                i9 = -1;
                            }
                            z = false;
                            if (!z) {
                            }
                            i38 = Math.max(i38, i40);
                            if (z6) {
                            }
                            int i412 = this.mTotalLength;
                            this.mTotalLength = Math.max(i412, virtualChildAt2.getMeasuredHeight() + i412 + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin + getNextLocationOffset(virtualChildAt2));
                            z6 = z7;
                            i36 = i7;
                            i4 = i8;
                        }
                        i37++;
                        f4 = f;
                    }
                    i5 = i;
                    this.mTotalLength += getPaddingTop() + getPaddingBottom();
                    i6 = i38;
                } else {
                    i6 = Math.max(i30, i27);
                    if (z3 && i33 != 1073741824) {
                        for (int i42 = 0; i42 < i3; i42++) {
                            View virtualChildAt3 = getVirtualChildAt(i42);
                            if (virtualChildAt3 != null && virtualChildAt3.getVisibility() != 8 && ((LayoutParams) virtualChildAt3.getLayoutParams()).weight > 0.0f) {
                                virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(virtualChildAt3.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i29, 1073741824));
                            }
                        }
                    }
                    i5 = i;
                    i4 = i28;
                }
                if (z6 || mode == 1073741824) {
                    i6 = i32;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i5, i4), resolveSizeAndState);
                if (z5) {
                    forceUniformWidth(i3, i2);
                    return;
                }
                return;
            }
            View virtualChildAt4 = getVirtualChildAt(i25);
            if (virtualChildAt4 == null) {
                this.mTotalLength += measureNullChild(i25);
                i14 = virtualChildCount;
                i23 = i27;
            } else {
                int i43 = i20;
                if (virtualChildAt4.getVisibility() == 8) {
                    i25 += getChildrenSkipCount(virtualChildAt4, i25);
                    i14 = virtualChildCount;
                    i23 = i27;
                    i20 = i43;
                } else {
                    if (hasDividerBeforeChildAt(i25)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt4.getLayoutParams();
                    float f6 = f2 + layoutParams3.weight;
                    if (mode2 == 1073741824 && ((ViewGroup.MarginLayoutParams) layoutParams3).height == 0 && layoutParams3.weight > 0.0f) {
                        int i44 = this.mTotalLength;
                        this.mTotalLength = Math.max(i44, ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + i44 + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin);
                        max = i22;
                        view = virtualChildAt4;
                        i17 = i24;
                        i14 = virtualChildCount;
                        z4 = true;
                        i12 = i43;
                        i13 = i21;
                        i18 = i25;
                        i15 = mode2;
                        i16 = i27;
                    } else {
                        int i45 = i21;
                        if (((ViewGroup.MarginLayoutParams) layoutParams3).height != 0 || layoutParams3.weight <= 0.0f) {
                            i11 = Integer.MIN_VALUE;
                        } else {
                            ((ViewGroup.MarginLayoutParams) layoutParams3).height = -2;
                            i11 = 0;
                        }
                        i12 = i43;
                        int i46 = i11;
                        i13 = i45;
                        int i47 = i22;
                        i14 = virtualChildCount;
                        i15 = mode2;
                        i16 = i27;
                        i17 = i24;
                        i18 = i25;
                        measureChildBeforeLayout(virtualChildAt4, i25, i, 0, i2, f6 == 0.0f ? this.mTotalLength : 0);
                        if (i46 != Integer.MIN_VALUE) {
                            ((ViewGroup.MarginLayoutParams) layoutParams3).height = i46;
                        }
                        int measuredHeight2 = virtualChildAt4.getMeasuredHeight();
                        int i48 = this.mTotalLength;
                        view = virtualChildAt4;
                        this.mTotalLength = Math.max(i48, i48 + measuredHeight2 + ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin + getNextLocationOffset(view));
                        max = z3 ? Math.max(measuredHeight2, i47) : i47;
                    }
                    if (i19 >= 0 && i19 == i18 + 1) {
                        this.mBaselineChildTop = this.mTotalLength;
                    }
                    if (i18 < i19 && layoutParams3.weight > 0.0f) {
                        throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                    }
                    if (mode == 1073741824 || ((ViewGroup.MarginLayoutParams) layoutParams3).width != -1) {
                        z2 = false;
                    } else {
                        z2 = true;
                        z5 = true;
                    }
                    int i49 = ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin;
                    int measuredWidth2 = view.getMeasuredWidth() + i49;
                    int max3 = Math.max(i13, measuredWidth2);
                    int combineMeasuredStates = View.combineMeasuredStates(i12, view.getMeasuredState());
                    boolean z8 = z6 && ((ViewGroup.MarginLayoutParams) layoutParams3).width == -1;
                    if (layoutParams3.weight > 0.0f) {
                        if (!z2) {
                            i49 = measuredWidth2;
                        }
                        i16 = Math.max(i16, i49);
                        max2 = i17;
                    } else {
                        if (!z2) {
                            i49 = measuredWidth2;
                        }
                        max2 = Math.max(i17, i49);
                    }
                    int childrenSkipCount = getChildrenSkipCount(view, i18) + i18;
                    i22 = max;
                    z6 = z8;
                    i23 = i16;
                    f2 = f6;
                    i24 = max2;
                    i20 = combineMeasuredStates;
                    i25 = childrenSkipCount;
                    i21 = max3;
                    i25++;
                    mode2 = i15;
                    virtualChildCount = i14;
                }
            }
            i15 = mode2;
            i25++;
            mode2 = i15;
            virtualChildCount = i14;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            drawDividersVertical(canvas);
        } else {
            drawDividersHorizontal(canvas);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i >= 0 && i < getChildCount()) {
            this.mBaselineAlignedChildIndex = i;
            return;
        }
        throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable == this.mDivider) {
            return;
        }
        this.mDivider = drawable;
        if (drawable != null) {
            this.mDividerWidth = drawable.getIntrinsicWidth();
            this.mDividerHeight = drawable.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        setWillNotDraw(drawable == null);
        requestLayout();
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | ((-8388616) & i3);
            requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & (-113));
            requestLayout();
        }
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0443R.styleable.LinearLayoutCompat, i, 0);
        int i2 = obtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = obtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = obtainStyledAttributes.getBoolean(C0443R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(C0443R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(C0443R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(C0443R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(C0443R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0443R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(C0443R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }
}
