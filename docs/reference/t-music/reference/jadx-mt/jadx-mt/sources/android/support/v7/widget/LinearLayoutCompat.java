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

/* JADX INFO: loaded from: classes3.dex */
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
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((ViewGroup.MarginLayoutParams) layoutParams).height == -1) {
                    int i4 = ((ViewGroup.MarginLayoutParams) layoutParams).width;
                    ((ViewGroup.MarginLayoutParams) layoutParams).width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, iMakeMeasureSpec, 0);
                    ((ViewGroup.MarginLayoutParams) layoutParams).width = i4;
                }
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((ViewGroup.MarginLayoutParams) layoutParams).width == -1) {
                    int i4 = ((ViewGroup.MarginLayoutParams) layoutParams).height;
                    ((ViewGroup.MarginLayoutParams) layoutParams).height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, iMakeMeasureSpec, 0, i2, 0);
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
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i2)) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                drawVerticalDivider(canvas, zIsLayoutRtl ? virtualChildAt.getRight() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin : (virtualChildAt.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin) - this.mDividerWidth);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (zIsLayoutRtl) {
                    left = virtualChildAt2.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin;
                    i = this.mDividerWidth;
                    right = left - i;
                } else {
                    right = virtualChildAt2.getRight() + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin;
                }
            } else if (zIsLayoutRtl) {
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
        int bottom = this.mBaselineChildTop;
        if (this.mOrientation == 1 && (i = this.mGravity & 112) != 48) {
            if (i == 16) {
                bottom += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
            } else if (i == 80) {
                bottom = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
            }
        }
        return bottom + ((ViewGroup.MarginLayoutParams) ((LayoutParams) childAt.getLayoutParams())).topMargin + baseline;
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

    /* JADX WARN: Removed duplicated region for block: B:29:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0100  */
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
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop = getPaddingTop();
        int i13 = i4 - i2;
        int paddingBottom = i13 - getPaddingBottom();
        int paddingBottom2 = (i13 - paddingTop) - getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i14 = this.mGravity;
        int i15 = i14 & 112;
        boolean z2 = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(8388615 & i14, ViewCompat.getLayoutDirection(this));
        boolean z3 = true;
        int paddingLeft = absoluteGravity != 1 ? absoluteGravity != 5 ? getPaddingLeft() : ((getPaddingLeft() + i3) - i) - this.mTotalLength : getPaddingLeft() + (((i3 - i) - this.mTotalLength) / 2);
        if (zIsLayoutRtl) {
            i5 = virtualChildCount - 1;
            i6 = -1;
        } else {
            i5 = 0;
            i6 = 1;
        }
        int childrenSkipCount = 0;
        while (childrenSkipCount < virtualChildCount) {
            int i16 = i5 + (i6 * childrenSkipCount);
            View virtualChildAt = getVirtualChildAt(i16);
            if (virtualChildAt == null) {
                paddingLeft += measureNullChild(i16);
                z = z3;
                i7 = paddingTop;
                i8 = virtualChildCount;
                i9 = i15;
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i17 = childrenSkipCount;
                if (z2) {
                    i8 = virtualChildCount;
                    int baseline = ((ViewGroup.MarginLayoutParams) layoutParams).height != -1 ? virtualChildAt.getBaseline() : -1;
                    i10 = layoutParams.gravity;
                    if (i10 < 0) {
                        i10 = i15;
                    }
                    i11 = i10 & 112;
                    i9 = i15;
                    if (i11 != 16) {
                        z = true;
                        i12 = ((((paddingBottom2 - measuredHeight) / 2) + paddingTop) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                    } else if (i11 != 48) {
                        if (i11 != 80) {
                            i12 = paddingTop;
                        } else {
                            int measuredHeight2 = (paddingBottom - measuredHeight) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                            if (baseline != -1) {
                                measuredHeight2 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - baseline);
                            }
                            i12 = measuredHeight2;
                        }
                        z = true;
                    } else {
                        int i18 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + paddingTop;
                        z = true;
                        if (baseline != -1) {
                            i18 += iArr[1] - baseline;
                        }
                        i12 = i18;
                    }
                    if (hasDividerBeforeChildAt(i16)) {
                        paddingLeft += this.mDividerWidth;
                    }
                    int i19 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + paddingLeft;
                    i7 = paddingTop;
                    setChildFrame(virtualChildAt, i19 + getLocationOffset(virtualChildAt), i12, measuredWidth, measuredHeight);
                    int nextLocationOffset = i19 + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                    childrenSkipCount = i17 + getChildrenSkipCount(virtualChildAt, i16);
                    paddingLeft = nextLocationOffset;
                    childrenSkipCount++;
                    virtualChildCount = i8;
                    i15 = i9;
                    z3 = z;
                    paddingTop = i7;
                } else {
                    i8 = virtualChildCount;
                }
                i10 = layoutParams.gravity;
                if (i10 < 0) {
                }
                i11 = i10 & 112;
                i9 = i15;
                if (i11 != 16) {
                }
                if (hasDividerBeforeChildAt(i16)) {
                }
                int i192 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + paddingLeft;
                i7 = paddingTop;
                setChildFrame(virtualChildAt, i192 + getLocationOffset(virtualChildAt), i12, measuredWidth, measuredHeight);
                int nextLocationOffset2 = i192 + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                childrenSkipCount = i17 + getChildrenSkipCount(virtualChildAt, i16);
                paddingLeft = nextLocationOffset2;
                childrenSkipCount++;
                virtualChildCount = i8;
                i15 = i9;
                z3 = z;
                paddingTop = i7;
            } else {
                i7 = paddingTop;
                i8 = virtualChildCount;
                i9 = i15;
                z = true;
            }
            childrenSkipCount++;
            virtualChildCount = i8;
            i15 = i9;
            z3 = z;
            paddingTop = i7;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00a0  */
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
        int childrenSkipCount = 0;
        while (childrenSkipCount < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(childrenSkipCount);
            if (virtualChildAt == null) {
                paddingTop += measureNullChild(childrenSkipCount);
            } else {
                if (virtualChildAt.getVisibility() != 8) {
                    int measuredWidth = virtualChildAt.getMeasuredWidth();
                    int measuredHeight = virtualChildAt.getMeasuredHeight();
                    LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                    int i13 = layoutParams.gravity;
                    if (i13 < 0) {
                        i13 = i12;
                    }
                    int absoluteGravity = GravityCompat.getAbsoluteGravity(i13, ViewCompat.getLayoutDirection(this)) & 7;
                    if (absoluteGravity == 1) {
                        i5 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                        i6 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                    } else if (absoluteGravity != 5) {
                        i7 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + paddingLeft;
                        int i14 = i7;
                        if (hasDividerBeforeChildAt(childrenSkipCount)) {
                            paddingTop += this.mDividerHeight;
                        }
                        int i15 = paddingTop + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                        setChildFrame(virtualChildAt, i14, i15 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                        int nextLocationOffset = i15 + measuredHeight + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                        childrenSkipCount += getChildrenSkipCount(virtualChildAt, childrenSkipCount);
                        paddingTop = nextLocationOffset;
                        i8 = 1;
                    } else {
                        i5 = paddingRight - measuredWidth;
                        i6 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                    }
                    i7 = i5 - i6;
                    int i142 = i7;
                    if (hasDividerBeforeChildAt(childrenSkipCount)) {
                    }
                    int i152 = paddingTop + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                    setChildFrame(virtualChildAt, i142, i152 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                    int nextLocationOffset2 = i152 + measuredHeight + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                    childrenSkipCount += getChildrenSkipCount(virtualChildAt, childrenSkipCount);
                    paddingTop = nextLocationOffset2;
                    i8 = 1;
                }
                childrenSkipCount += i8;
            }
            i8 = 1;
            childrenSkipCount += i8;
        }
    }

    void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* JADX WARN: Removed duplicated region for block: B:198:0x0474  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureHorizontal(int i, int i2) {
        int[] iArr;
        int i3;
        int i4;
        int i5;
        int iMax;
        int iMax2;
        int i6;
        int i7;
        float f;
        int i8;
        int baseline;
        int i9;
        int i10;
        byte b2;
        int i11;
        int i12;
        boolean z;
        boolean z2;
        View view;
        int i13;
        boolean z3;
        int measuredHeight;
        int baseline2;
        int i14;
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
        boolean z4 = this.mBaselineAligned;
        boolean z5 = this.mUseLargestChild;
        int i15 = 1073741824;
        boolean z6 = mode == 1073741824;
        int childrenSkipCount = 0;
        int iMax3 = 0;
        int i16 = 0;
        int iMax4 = 0;
        int iMax5 = 0;
        boolean z7 = false;
        int i17 = 0;
        boolean z8 = false;
        boolean z9 = true;
        float f2 = 0.0f;
        while (true) {
            iArr = iArr3;
            if (childrenSkipCount >= virtualChildCount) {
                break;
            }
            View virtualChildAt = getVirtualChildAt(childrenSkipCount);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(childrenSkipCount);
            } else if (virtualChildAt.getVisibility() == 8) {
                childrenSkipCount += getChildrenSkipCount(virtualChildAt, childrenSkipCount);
            } else {
                if (hasDividerBeforeChildAt(childrenSkipCount)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                float f3 = f2 + layoutParams.weight;
                if (mode == i15 && ((ViewGroup.MarginLayoutParams) layoutParams).width == 0 && layoutParams.weight > 0.0f) {
                    if (z6) {
                        this.mTotalLength += ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                    } else {
                        int i18 = this.mTotalLength;
                        this.mTotalLength = Math.max(i18, ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + i18 + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin);
                    }
                    if (z4) {
                        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChildAt.measure(iMakeMeasureSpec, iMakeMeasureSpec);
                        i12 = childrenSkipCount;
                        z = z5;
                        z2 = z4;
                        view = virtualChildAt;
                    } else {
                        i12 = childrenSkipCount;
                        z = z5;
                        z2 = z4;
                        view = virtualChildAt;
                        z7 = true;
                        i13 = 1073741824;
                        if (mode2 == i13 && ((ViewGroup.MarginLayoutParams) layoutParams).height == -1) {
                            z3 = true;
                            z8 = true;
                        } else {
                            z3 = false;
                        }
                        int i19 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                        measuredHeight = view.getMeasuredHeight() + i19;
                        int iCombineMeasuredStates = View.combineMeasuredStates(i17, view.getMeasuredState());
                        if (z2 && (baseline2 = view.getBaseline()) != -1) {
                            i14 = layoutParams.gravity;
                            if (i14 < 0) {
                                i14 = this.mGravity;
                            }
                            int i20 = (((i14 & 112) >> 4) & (-2)) >> 1;
                            iArr2[i20] = Math.max(iArr2[i20], baseline2);
                            iArr[i20] = Math.max(iArr[i20], measuredHeight - baseline2);
                        }
                        int iMax6 = Math.max(i16, measuredHeight);
                        boolean z10 = !z9 && ((ViewGroup.MarginLayoutParams) layoutParams).height == -1;
                        if (layoutParams.weight <= 0.0f) {
                            if (!z3) {
                                i19 = measuredHeight;
                            }
                            iMax5 = Math.max(iMax5, i19);
                        } else {
                            int i21 = iMax5;
                            if (z3) {
                                measuredHeight = i19;
                            }
                            iMax4 = Math.max(iMax4, measuredHeight);
                            iMax5 = i21;
                        }
                        int i22 = i12;
                        i16 = iMax6;
                        i17 = iCombineMeasuredStates;
                        z9 = z10;
                        childrenSkipCount = getChildrenSkipCount(view, i22) + i22;
                        f2 = f3;
                        childrenSkipCount++;
                        iArr3 = iArr;
                        z5 = z;
                        z4 = z2;
                        i15 = 1073741824;
                    }
                } else {
                    if (((ViewGroup.MarginLayoutParams) layoutParams).width != 0 || layoutParams.weight <= 0.0f) {
                        b2 = -2;
                        i11 = Integer.MIN_VALUE;
                    } else {
                        b2 = -2;
                        ((ViewGroup.MarginLayoutParams) layoutParams).width = -2;
                        i11 = 0;
                    }
                    i12 = childrenSkipCount;
                    int i23 = i11;
                    z = z5;
                    z2 = z4;
                    measureChildBeforeLayout(virtualChildAt, i12, i, f3 == 0.0f ? this.mTotalLength : 0, i2, 0);
                    if (i23 != Integer.MIN_VALUE) {
                        ((ViewGroup.MarginLayoutParams) layoutParams).width = i23;
                    }
                    int measuredWidth = virtualChildAt.getMeasuredWidth();
                    if (z6) {
                        view = virtualChildAt;
                        this.mTotalLength += ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(view);
                    } else {
                        view = virtualChildAt;
                        int i24 = this.mTotalLength;
                        this.mTotalLength = Math.max(i24, i24 + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + getNextLocationOffset(view));
                    }
                    if (z) {
                        iMax3 = Math.max(measuredWidth, iMax3);
                    }
                }
                i13 = 1073741824;
                if (mode2 == i13) {
                    z3 = false;
                    int i192 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                    measuredHeight = view.getMeasuredHeight() + i192;
                    int iCombineMeasuredStates2 = View.combineMeasuredStates(i17, view.getMeasuredState());
                    if (z2) {
                        i14 = layoutParams.gravity;
                        if (i14 < 0) {
                        }
                        int i202 = (((i14 & 112) >> 4) & (-2)) >> 1;
                        iArr2[i202] = Math.max(iArr2[i202], baseline2);
                        iArr[i202] = Math.max(iArr[i202], measuredHeight - baseline2);
                    }
                    int iMax62 = Math.max(i16, measuredHeight);
                    if (z9) {
                        if (layoutParams.weight <= 0.0f) {
                        }
                        int i222 = i12;
                        i16 = iMax62;
                        i17 = iCombineMeasuredStates2;
                        z9 = z10;
                        childrenSkipCount = getChildrenSkipCount(view, i222) + i222;
                        f2 = f3;
                    }
                }
                childrenSkipCount++;
                iArr3 = iArr;
                z5 = z;
                z4 = z2;
                i15 = 1073741824;
            }
            z = z5;
            z2 = z4;
            childrenSkipCount++;
            iArr3 = iArr;
            z5 = z;
            z4 = z2;
            i15 = 1073741824;
        }
        boolean z11 = z5;
        boolean z12 = z4;
        int iMax7 = i16;
        int i25 = iMax4;
        int i26 = iMax5;
        int i27 = i17;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
            i3 = i27;
        } else {
            i3 = i27;
            iMax7 = Math.max(iMax7, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
        }
        if (z11 && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            int childrenSkipCount2 = 0;
            while (childrenSkipCount2 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(childrenSkipCount2);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(childrenSkipCount2);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    childrenSkipCount2 += getChildrenSkipCount(virtualChildAt2, childrenSkipCount2);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    if (z6) {
                        this.mTotalLength += ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + iMax3 + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin + getNextLocationOffset(virtualChildAt2);
                    } else {
                        int i28 = this.mTotalLength;
                        i10 = iMax7;
                        this.mTotalLength = Math.max(i28, i28 + iMax3 + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin + getNextLocationOffset(virtualChildAt2));
                        childrenSkipCount2++;
                        iMax7 = i10;
                    }
                }
                i10 = iMax7;
                childrenSkipCount2++;
                iMax7 = i10;
            }
        }
        int i29 = iMax7;
        this.mTotalLength += getPaddingLeft() + getPaddingRight();
        int iResolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumWidth()), i, 0);
        int i30 = (16777215 & iResolveSizeAndState) - this.mTotalLength;
        if (z7 || (i30 != 0 && f2 > 0.0f)) {
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
            int i31 = i25;
            int iMax8 = -1;
            int iCombineMeasuredStates3 = i3;
            float f5 = f2;
            int i32 = 0;
            while (i32 < virtualChildCount) {
                View virtualChildAt3 = getVirtualChildAt(i32);
                if (virtualChildAt3 == null || virtualChildAt3.getVisibility() == 8) {
                    i6 = i30;
                    i7 = virtualChildCount;
                } else {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                    float f6 = layoutParams3.weight;
                    if (f6 > 0.0f) {
                        int i33 = (int) ((i30 * f6) / f5);
                        float f7 = f5 - f6;
                        int i34 = i30 - i33;
                        i7 = virtualChildCount;
                        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom() + ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin, ((ViewGroup.MarginLayoutParams) layoutParams3).height);
                        if (((ViewGroup.MarginLayoutParams) layoutParams3).width == 0) {
                            i9 = 1073741824;
                            if (mode == 1073741824) {
                                if (i33 <= 0) {
                                    i33 = 0;
                                }
                                virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(i33, 1073741824), childMeasureSpec);
                            }
                            iCombineMeasuredStates3 = View.combineMeasuredStates(iCombineMeasuredStates3, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                            f5 = f7;
                            i6 = i34;
                        } else {
                            i9 = 1073741824;
                        }
                        int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i33;
                        if (measuredWidth2 < 0) {
                            measuredWidth2 = 0;
                        }
                        virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth2, i9), childMeasureSpec);
                        iCombineMeasuredStates3 = View.combineMeasuredStates(iCombineMeasuredStates3, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                        f5 = f7;
                        i6 = i34;
                    } else {
                        i6 = i30;
                        i7 = virtualChildCount;
                    }
                    if (z6) {
                        this.mTotalLength += virtualChildAt3.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin + getNextLocationOffset(virtualChildAt3);
                        f = f5;
                    } else {
                        int i35 = this.mTotalLength;
                        f = f5;
                        this.mTotalLength = Math.max(i35, virtualChildAt3.getMeasuredWidth() + i35 + ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin + getNextLocationOffset(virtualChildAt3));
                    }
                    boolean z13 = mode2 != 1073741824 && ((ViewGroup.MarginLayoutParams) layoutParams3).height == -1;
                    int i36 = ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin;
                    int measuredHeight2 = virtualChildAt3.getMeasuredHeight() + i36;
                    iMax8 = Math.max(iMax8, measuredHeight2);
                    if (!z13) {
                        i36 = measuredHeight2;
                    }
                    int iMax9 = Math.max(i31, i36);
                    if (z9) {
                        i8 = -1;
                        boolean z14 = ((ViewGroup.MarginLayoutParams) layoutParams3).height == -1;
                        if (!z12 && (baseline = virtualChildAt3.getBaseline()) != i8) {
                            int i37 = layoutParams3.gravity;
                            if (i37 < 0) {
                                i37 = this.mGravity;
                            }
                            int i38 = (((i37 & 112) >> 4) & (-2)) >> 1;
                            iArr2[i38] = Math.max(iArr2[i38], baseline);
                            iArr[i38] = Math.max(iArr[i38], measuredHeight2 - baseline);
                        }
                        i31 = iMax9;
                        z9 = z14;
                        f5 = f;
                    } else {
                        i8 = -1;
                    }
                    if (!z12) {
                        i31 = iMax9;
                        z9 = z14;
                        f5 = f;
                    }
                }
                i32++;
                i30 = i6;
                virtualChildCount = i7;
            }
            i4 = i2;
            i5 = virtualChildCount;
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            iMax = (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) ? iMax8 : Math.max(iMax8, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
            i3 = iCombineMeasuredStates3;
            iMax2 = i31;
        } else {
            iMax2 = Math.max(i25, i26);
            if (z11 && mode != 1073741824) {
                for (int i39 = 0; i39 < virtualChildCount; i39++) {
                    View virtualChildAt4 = getVirtualChildAt(i39);
                    if (virtualChildAt4 != null && virtualChildAt4.getVisibility() != 8 && ((LayoutParams) virtualChildAt4.getLayoutParams()).weight > 0.0f) {
                        virtualChildAt4.measure(View.MeasureSpec.makeMeasureSpec(iMax3, 1073741824), View.MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredHeight(), 1073741824));
                    }
                }
            }
            i4 = i2;
            i5 = virtualChildCount;
            iMax = i29;
        }
        if (z9 || mode2 == 1073741824) {
            iMax2 = iMax;
        }
        setMeasuredDimension(iResolveSizeAndState | (i3 & ViewCompat.MEASURED_STATE_MASK), View.resolveSizeAndState(Math.max(iMax2 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i4, i3 << 16));
        if (z8) {
            forceUniformHeight(i5, i);
        }
    }

    int measureNullChild(int i) {
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:151:0x0333  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0341  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureVertical(int i, int i2) {
        int i3;
        int iCombineMeasuredStates;
        int i4;
        int iMax;
        int i5;
        float f;
        int i6;
        int i7;
        boolean z;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int iMax2;
        int i14;
        int i15;
        View view;
        int iMax3;
        boolean z2;
        int iMax4;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int i16 = this.mBaselineAlignedChildIndex;
        boolean z3 = this.mUseLargestChild;
        int i17 = 0;
        int i18 = 0;
        int i19 = 0;
        int i20 = 0;
        int i21 = 0;
        int childrenSkipCount = 0;
        boolean z4 = false;
        boolean z5 = false;
        float f2 = 0.0f;
        boolean z6 = true;
        while (true) {
            int i22 = 8;
            int i23 = i20;
            if (childrenSkipCount >= virtualChildCount) {
                int i24 = i17;
                int i25 = i19;
                int i26 = i21;
                int i27 = virtualChildCount;
                int iMax5 = i18;
                int i28 = mode2;
                if (this.mTotalLength > 0) {
                    i3 = i27;
                    if (hasDividerBeforeChildAt(i3)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i3 = i27;
                }
                if (z3 && (i28 == Integer.MIN_VALUE || i28 == 0)) {
                    this.mTotalLength = 0;
                    int childrenSkipCount2 = 0;
                    while (childrenSkipCount2 < i3) {
                        View virtualChildAt = getVirtualChildAt(childrenSkipCount2);
                        if (virtualChildAt == null) {
                            this.mTotalLength += measureNullChild(childrenSkipCount2);
                        } else if (virtualChildAt.getVisibility() == i22) {
                            childrenSkipCount2 += getChildrenSkipCount(virtualChildAt, childrenSkipCount2);
                        } else {
                            LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                            int i29 = this.mTotalLength;
                            this.mTotalLength = Math.max(i29, i29 + i25 + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt));
                        }
                        childrenSkipCount2++;
                        i22 = 8;
                    }
                }
                this.mTotalLength += getPaddingTop() + getPaddingBottom();
                int iResolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), i2, 0);
                int i30 = (16777215 & iResolveSizeAndState) - this.mTotalLength;
                if (z4 || (i30 != 0 && f2 > 0.0f)) {
                    float f3 = this.mWeightSum;
                    if (f3 > 0.0f) {
                        f2 = f3;
                    }
                    this.mTotalLength = 0;
                    float f4 = f2;
                    int i31 = 0;
                    int iMax6 = i26;
                    iCombineMeasuredStates = i24;
                    while (i31 < i3) {
                        View virtualChildAt2 = getVirtualChildAt(i31);
                        if (virtualChildAt2.getVisibility() == 8) {
                            f = f4;
                        } else {
                            LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            float f5 = layoutParams2.weight;
                            if (f5 > 0.0f) {
                                int i32 = (int) ((i30 * f5) / f4);
                                i5 = i30 - i32;
                                f = f4 - f5;
                                int childMeasureSpec = ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams2).width);
                                if (((ViewGroup.MarginLayoutParams) layoutParams2).height == 0) {
                                    i8 = 1073741824;
                                    if (i28 == 1073741824) {
                                        if (i32 <= 0) {
                                            i32 = 0;
                                        }
                                        virtualChildAt2.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(i32, 1073741824));
                                    }
                                    iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, virtualChildAt2.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                                } else {
                                    i8 = 1073741824;
                                }
                                int measuredHeight = virtualChildAt2.getMeasuredHeight() + i32;
                                if (measuredHeight < 0) {
                                    measuredHeight = 0;
                                }
                                virtualChildAt2.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(measuredHeight, i8));
                                iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, virtualChildAt2.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                            } else {
                                i5 = i30;
                                f = f4;
                            }
                            int i33 = ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin;
                            int measuredWidth = virtualChildAt2.getMeasuredWidth() + i33;
                            iMax5 = Math.max(iMax5, measuredWidth);
                            if (mode != 1073741824) {
                                i6 = iCombineMeasuredStates;
                                i7 = -1;
                                z = ((ViewGroup.MarginLayoutParams) layoutParams2).width == -1;
                                if (!z) {
                                    i33 = measuredWidth;
                                }
                                iMax6 = Math.max(iMax6, i33);
                                boolean z7 = !z6 && ((ViewGroup.MarginLayoutParams) layoutParams2).width == i7;
                                int i34 = this.mTotalLength;
                                this.mTotalLength = Math.max(i34, virtualChildAt2.getMeasuredHeight() + i34 + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin + getNextLocationOffset(virtualChildAt2));
                                z6 = z7;
                                i30 = i5;
                                iCombineMeasuredStates = i6;
                            } else {
                                i6 = iCombineMeasuredStates;
                                i7 = -1;
                            }
                            if (!z) {
                            }
                            iMax6 = Math.max(iMax6, i33);
                            if (z6) {
                                int i342 = this.mTotalLength;
                                this.mTotalLength = Math.max(i342, virtualChildAt2.getMeasuredHeight() + i342 + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin + getNextLocationOffset(virtualChildAt2));
                                z6 = z7;
                                i30 = i5;
                                iCombineMeasuredStates = i6;
                            }
                        }
                        i31++;
                        f4 = f;
                    }
                    i4 = i;
                    this.mTotalLength += getPaddingTop() + getPaddingBottom();
                    iMax = iMax6;
                } else {
                    iMax = Math.max(i26, i23);
                    if (z3 && i28 != 1073741824) {
                        for (int i35 = 0; i35 < i3; i35++) {
                            View virtualChildAt3 = getVirtualChildAt(i35);
                            if (virtualChildAt3 != null && virtualChildAt3.getVisibility() != 8 && ((LayoutParams) virtualChildAt3.getLayoutParams()).weight > 0.0f) {
                                virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(virtualChildAt3.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i25, 1073741824));
                            }
                        }
                    }
                    i4 = i;
                    iCombineMeasuredStates = i24;
                }
                if (z6 || mode == 1073741824) {
                    iMax = iMax5;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(iMax + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i4, iCombineMeasuredStates), iResolveSizeAndState);
                if (z5) {
                    forceUniformWidth(i3, i2);
                    return;
                }
                return;
            }
            View virtualChildAt4 = getVirtualChildAt(childrenSkipCount);
            if (virtualChildAt4 == null) {
                this.mTotalLength += measureNullChild(childrenSkipCount);
                i12 = virtualChildCount;
                i20 = i23;
            } else {
                int i36 = i17;
                if (virtualChildAt4.getVisibility() == 8) {
                    childrenSkipCount += getChildrenSkipCount(virtualChildAt4, childrenSkipCount);
                    i12 = virtualChildCount;
                    i20 = i23;
                    i17 = i36;
                } else {
                    if (hasDividerBeforeChildAt(childrenSkipCount)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt4.getLayoutParams();
                    float f6 = f2 + layoutParams3.weight;
                    if (mode2 == 1073741824 && ((ViewGroup.MarginLayoutParams) layoutParams3).height == 0 && layoutParams3.weight > 0.0f) {
                        int i37 = this.mTotalLength;
                        this.mTotalLength = Math.max(i37, ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + i37 + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin);
                        iMax3 = i19;
                        view = virtualChildAt4;
                        i14 = i21;
                        i12 = virtualChildCount;
                        z4 = true;
                        i10 = i36;
                        i11 = i18;
                        i15 = childrenSkipCount;
                        i13 = mode2;
                        iMax2 = i23;
                    } else {
                        int i38 = i18;
                        if (((ViewGroup.MarginLayoutParams) layoutParams3).height != 0 || layoutParams3.weight <= 0.0f) {
                            i9 = Integer.MIN_VALUE;
                        } else {
                            ((ViewGroup.MarginLayoutParams) layoutParams3).height = -2;
                            i9 = 0;
                        }
                        i10 = i36;
                        int i39 = i9;
                        i11 = i38;
                        int i40 = i19;
                        i12 = virtualChildCount;
                        i13 = mode2;
                        iMax2 = i23;
                        i14 = i21;
                        i15 = childrenSkipCount;
                        measureChildBeforeLayout(virtualChildAt4, childrenSkipCount, i, 0, i2, f6 == 0.0f ? this.mTotalLength : 0);
                        if (i39 != Integer.MIN_VALUE) {
                            ((ViewGroup.MarginLayoutParams) layoutParams3).height = i39;
                        }
                        int measuredHeight2 = virtualChildAt4.getMeasuredHeight();
                        int i41 = this.mTotalLength;
                        view = virtualChildAt4;
                        this.mTotalLength = Math.max(i41, i41 + measuredHeight2 + ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin + getNextLocationOffset(view));
                        iMax3 = z3 ? Math.max(measuredHeight2, i40) : i40;
                    }
                    if (i16 >= 0 && i16 == i15 + 1) {
                        this.mBaselineChildTop = this.mTotalLength;
                    }
                    if (i15 < i16 && layoutParams3.weight > 0.0f) {
                        throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                    }
                    if (mode == 1073741824 || ((ViewGroup.MarginLayoutParams) layoutParams3).width != -1) {
                        z2 = false;
                    } else {
                        z2 = true;
                        z5 = true;
                    }
                    int i42 = ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin;
                    int measuredWidth2 = view.getMeasuredWidth() + i42;
                    int iMax7 = Math.max(i11, measuredWidth2);
                    int iCombineMeasuredStates2 = View.combineMeasuredStates(i10, view.getMeasuredState());
                    boolean z8 = z6 && ((ViewGroup.MarginLayoutParams) layoutParams3).width == -1;
                    if (layoutParams3.weight > 0.0f) {
                        if (!z2) {
                            i42 = measuredWidth2;
                        }
                        iMax2 = Math.max(iMax2, i42);
                        iMax4 = i14;
                    } else {
                        if (!z2) {
                            i42 = measuredWidth2;
                        }
                        iMax4 = Math.max(i14, i42);
                    }
                    int childrenSkipCount3 = getChildrenSkipCount(view, i15) + i15;
                    i19 = iMax3;
                    z6 = z8;
                    i20 = iMax2;
                    f2 = f6;
                    i21 = iMax4;
                    i17 = iCombineMeasuredStates2;
                    childrenSkipCount = childrenSkipCount3;
                    i18 = iMax7;
                    childrenSkipCount++;
                    mode2 = i13;
                    virtualChildCount = i12;
                }
            }
            i13 = mode2;
            childrenSkipCount++;
            mode2 = i13;
            virtualChildCount = i12;
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
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0443R.styleable.LinearLayoutCompat, i, 0);
        int i2 = tintTypedArrayObtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = tintTypedArrayObtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = tintTypedArrayObtainStyledAttributes.getBoolean(C0443R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = tintTypedArrayObtainStyledAttributes.getFloat(C0443R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = tintTypedArrayObtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = tintTypedArrayObtainStyledAttributes.getBoolean(C0443R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(C0443R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = tintTypedArrayObtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0443R.styleable.LinearLayoutCompat_dividerPadding, 0);
        tintTypedArrayObtainStyledAttributes.recycle();
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
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0443R.styleable.LinearLayoutCompat_Layout);
            this.weight = typedArrayObtainStyledAttributes.getFloat(C0443R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = typedArrayObtainStyledAttributes.getInt(C0443R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            typedArrayObtainStyledAttributes.recycle();
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
