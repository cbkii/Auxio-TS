package com.p060tw.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/* JADX INFO: loaded from: classes4.dex */
public class TogglePreference extends LinearLayout {

    /* JADX INFO: renamed from: Cc */
    private InterfaceC0798a f1239Cc;
    private Context context;

    /* JADX INFO: renamed from: de */
    private TextView f1240de;

    /* JADX INFO: renamed from: fe */
    private Drawable f1241fe;

    /* JADX INFO: renamed from: he */
    private LinearLayout f1242he;
    private Drawable itemBackground;
    private int paddingLeft;
    private int paddingRight;
    private int rightIconHeight;
    private int rightIconWidth;
    private String title;
    private int titleTextColor;
    private float titleTextSize;

    /* JADX INFO: renamed from: ue */
    private ToggleButton f1243ue;

    /* JADX INFO: renamed from: com.tw.preference.TogglePreference$a */
    public interface InterfaceC0798a {
        /* JADX INFO: renamed from: a */
        void mo1465a(TogglePreference togglePreference, boolean z);
    }

    public TogglePreference(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    /* JADX INFO: renamed from: Ce */
    private void m1552Ce() {
        setPadding(this.paddingLeft, 0, this.paddingRight, 0);
        this.f1240de.setText(this.title);
        this.f1240de.setTextColor(this.titleTextColor);
        this.f1240de.setTextSize(0, this.titleTextSize);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1243ue.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = this.rightIconWidth;
            layoutParams.height = this.rightIconHeight;
            this.f1243ue.setLayoutParams(layoutParams);
        }
        Drawable drawable = this.f1241fe;
        if (drawable != null) {
            this.f1243ue.setBackground(drawable);
        }
        if (this.itemBackground == null) {
            this.itemBackground = this.context.getDrawable(R$drawable.preference_item_bg);
        }
        setBackground(this.itemBackground);
    }

    private void initView(Context context) {
        this.f1242he = new LinearLayout(context);
        this.f1242he.setGravity(16);
        addView(this.f1242he, new LinearLayout.LayoutParams(-1, -1));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        layoutParams.weight = 1.0f;
        this.f1242he.addView(linearLayout, layoutParams);
        this.f1240de = new TextView(context);
        this.f1240de.setGravity(19);
        this.f1240de.setMaxLines(1);
        this.f1240de.setTextColor(-1);
        this.f1240de.setTextSize(0, context.getResources().getDimension(R$dimen.tw_dp_h32));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.weight = 1.0f;
        linearLayout.addView(this.f1240de, layoutParams2);
        this.f1243ue = new ToggleButton(context);
        this.f1243ue.setTextOff("");
        this.f1243ue.setTextOn("");
        this.f1243ue.setText("");
        this.f1243ue.setButtonDrawable((Drawable) null);
        this.f1242he.addView(this.f1243ue, new LinearLayout.LayoutParams((int) context.getResources().getDimension(R$dimen.tw_dp_h120), (int) context.getResources().getDimension(R$dimen.tw_dp_h70)));
        this.f1243ue.setOnCheckedChangeListener(new C0803e(this));
        this.itemBackground = getResources().getDrawable(R$drawable.preference_item_bg, null);
        this.titleTextSize = getResources().getDimension(R$dimen.tw_dp_h32);
        this.titleTextColor = -1;
        this.f1241fe = getResources().getDrawable(R$drawable.select_toggle_thumb, null);
        this.paddingLeft = (int) getResources().getDimension(R$dimen.tw_dp_w64);
        this.paddingRight = (int) getResources().getDimension(R$dimen.tw_dp_w64);
        this.rightIconWidth = (int) getResources().getDimension(R$dimen.tw_dp_h120);
        this.rightIconHeight = (int) getResources().getDimension(R$dimen.tw_dp_h70);
    }

    public Drawable getItemBackground() {
        return this.itemBackground;
    }

    @Override // android.view.View
    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    @Override // android.view.View
    public int getPaddingRight() {
        return this.paddingRight;
    }

    public Drawable getRightIcon() {
        return this.f1241fe;
    }

    public int getRightIconHeight() {
        return this.rightIconHeight;
    }

    public int getRightIconWidth() {
        return this.rightIconWidth;
    }

    public String getTitle() {
        return this.title;
    }

    public int getTitleTextColor() {
        return this.titleTextColor;
    }

    public float getTitleTextSize() {
        return this.titleTextSize;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        super.setBackground(drawable);
        this.itemBackground = drawable;
    }

    public void setItemBackground(Drawable drawable) {
        this.itemBackground = drawable;
        setBackground(drawable);
    }

    public void setOnToggleStateChange(InterfaceC0798a interfaceC0798a) {
        this.f1239Cc = interfaceC0798a;
    }

    public void setPaddingLeft(int i) {
        this.paddingLeft = i;
        setPadding(i, 0, this.paddingRight, 0);
    }

    public void setPaddingRight(int i) {
        this.paddingRight = i;
        setPadding(this.paddingLeft, 0, i, 0);
    }

    public void setRightIcon(Drawable drawable) {
        this.f1241fe = drawable;
        this.f1243ue.setBackgroundDrawable(drawable);
    }

    public void setRightIconHeight(int i) {
        this.rightIconHeight = i;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1243ue.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = this.rightIconWidth;
            layoutParams.height = i;
            this.f1243ue.setLayoutParams(layoutParams);
        }
    }

    public void setRightIconWidth(int i) {
        this.rightIconWidth = i;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1243ue.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = i;
            layoutParams.height = this.rightIconHeight;
            this.f1243ue.setLayoutParams(layoutParams);
        }
    }

    public void setTitle(String str) {
        this.title = str;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f1240de.setText(str);
    }

    public void setTitleTextColor(int i) {
        this.titleTextColor = i;
        this.f1240de.setTextColor(i);
    }

    public void setTitleTextSize(float f) {
        this.titleTextSize = f;
        this.f1240de.setTextSize(0, f);
    }

    public void setToggleState(boolean z) {
        this.f1243ue.setChecked(z);
    }

    /* JADX INFO: renamed from: a */
    private void m1554a(AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = this.context.obtainStyledAttributes(attributeSet, R$styleable.TogglePreference);
        this.itemBackground = typedArrayObtainStyledAttributes.getDrawable(R$styleable.TogglePreference_android_background);
        this.title = typedArrayObtainStyledAttributes.getString(R$styleable.TogglePreference_android_text);
        this.titleTextSize = typedArrayObtainStyledAttributes.getDimension(R$styleable.TogglePreference_android_textSize, getResources().getDimension(R$dimen.tw_dp_h32));
        this.titleTextColor = typedArrayObtainStyledAttributes.getColor(R$styleable.TogglePreference_android_textColor, ViewCompat.MEASURED_SIZE_MASK);
        this.f1241fe = typedArrayObtainStyledAttributes.getDrawable(R$styleable.TogglePreference_rightIconBackground);
        this.paddingLeft = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.TogglePreference_paddingLeft, getResources().getDimension(R$dimen.tw_dp_w64));
        this.paddingRight = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.TogglePreference_paddingRight, getResources().getDimension(R$dimen.tw_dp_w64));
        this.rightIconWidth = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.TogglePreference_rightIconWidth, getResources().getDimension(R$dimen.tw_dp_h120));
        this.rightIconHeight = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.TogglePreference_rightIconHeight, getResources().getDimension(R$dimen.tw_dp_h70));
        typedArrayObtainStyledAttributes.recycle();
        m1552Ce();
    }

    public void setTitleTextColor(ColorStateList colorStateList) {
        this.f1240de.setTextColor(this.titleTextColor);
    }

    public TogglePreference(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView(context);
        m1554a(attributeSet);
    }

    public TogglePreference(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.context = context;
        initView(context);
        m1554a(attributeSet);
    }
}
