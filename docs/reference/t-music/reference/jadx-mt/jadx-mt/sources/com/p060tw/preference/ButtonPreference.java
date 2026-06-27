package com.p060tw.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/* JADX INFO: loaded from: classes4.dex */
public class ButtonPreference extends LinearLayout {
    private Context context;

    /* JADX INFO: renamed from: de */
    private TextView f1213de;

    /* JADX INFO: renamed from: ee */
    private Button f1214ee;

    /* JADX INFO: renamed from: fe */
    private Drawable f1215fe;

    /* JADX INFO: renamed from: ge */
    private InterfaceC0796a f1216ge;
    private Drawable itemBackground;
    private int paddingLeft;
    private int paddingRight;
    private int rightIconHeight;
    private int rightIconWidth;
    private int summaryTextColor;
    private String title;
    private int titleTextColor;
    private float titleTextSize;

    /* JADX INFO: renamed from: com.tw.preference.ButtonPreference$a */
    public interface InterfaceC0796a {
        /* JADX INFO: renamed from: a */
        void m1535a(ButtonPreference buttonPreference);
    }

    public ButtonPreference(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    /* JADX INFO: renamed from: Ce */
    private void m1532Ce() {
        setPadding(this.paddingLeft, 0, this.paddingRight, 0);
        this.f1213de.setText(this.title);
        this.f1213de.setTextColor(this.titleTextColor);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1214ee.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = this.rightIconWidth;
            layoutParams.height = this.rightIconHeight;
            this.f1214ee.setLayoutParams(layoutParams);
        }
        if (this.itemBackground == null) {
            this.itemBackground = this.context.getDrawable(R$drawable.preference_item_bg);
        }
        setBackground(this.itemBackground);
        Drawable drawable = this.f1215fe;
        if (drawable != null) {
            this.f1214ee.setBackground(drawable);
        }
    }

    /* JADX INFO: renamed from: a */
    private void m1533a(AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = this.context.obtainStyledAttributes(attributeSet, R$styleable.ButtonPreference);
        this.itemBackground = typedArrayObtainStyledAttributes.getDrawable(R$styleable.TogglePreference_android_background);
        this.title = typedArrayObtainStyledAttributes.getString(R$styleable.ButtonPreference_android_text);
        this.titleTextSize = typedArrayObtainStyledAttributes.getDimension(R$styleable.ButtonPreference_android_textSize, getResources().getDimension(R$dimen.tw_dp_h32));
        this.titleTextColor = typedArrayObtainStyledAttributes.getColor(R$styleable.ButtonPreference_android_textColor, ViewCompat.MEASURED_SIZE_MASK);
        this.f1215fe = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ButtonPreference_rightIconBackground);
        this.paddingLeft = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.ButtonPreference_paddingLeft, getResources().getDimension(R$dimen.tw_dp_w64));
        this.paddingRight = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.ButtonPreference_paddingRight, getResources().getDimension(R$dimen.tw_dp_w64));
        this.rightIconWidth = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.ButtonPreference_rightIconWidth, getResources().getDimension(R$dimen.tw_dp_h150));
        this.rightIconHeight = (int) typedArrayObtainStyledAttributes.getDimension(R$styleable.ButtonPreference_rightIconHeight, getResources().getDimension(R$dimen.tw_dp_h75));
        typedArrayObtainStyledAttributes.recycle();
        m1532Ce();
    }

    private void initView(Context context) {
        setOrientation(0);
        setGravity(16);
        setBackgroundResource(R$drawable.preference_item_bg);
        this.f1213de = new TextView(context);
        this.f1213de.setSingleLine();
        this.f1213de.setGravity(16);
        this.f1213de.setTextSize(0, context.getResources().getDimension(R$dimen.tw_dp_h32));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2);
        layoutParams.weight = 1.0f;
        addView(this.f1213de, layoutParams);
        this.f1214ee = new Button(context);
        this.f1214ee.setBackgroundResource(R$drawable.btn_restore);
        this.f1214ee.setOnClickListener(new ViewOnClickListenerC0799a(this));
        addView(this.f1214ee, new LinearLayout.LayoutParams((int) context.getResources().getDimension(R$dimen.tw_dp_h150), (int) context.getResources().getDimension(R$dimen.tw_dp_h75)));
        this.itemBackground = getResources().getDrawable(R$drawable.preference_item_bg, null);
        this.titleTextSize = getResources().getDimension(R$dimen.tw_dp_h32);
        this.titleTextColor = -1;
        this.f1215fe = getResources().getDrawable(R$drawable.btn_restore);
        this.paddingLeft = (int) getResources().getDimension(R$dimen.tw_dp_w64);
        this.paddingRight = (int) getResources().getDimension(R$dimen.tw_dp_w64);
        this.rightIconWidth = (int) getResources().getDimension(R$dimen.tw_dp_h150);
        this.rightIconHeight = (int) getResources().getDimension(R$dimen.tw_dp_h75);
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
        return this.f1215fe;
    }

    public int getRightIconHeight() {
        return this.rightIconHeight;
    }

    public int getRightIconWidth() {
        return this.rightIconWidth;
    }

    public int getSummaryTextColor() {
        return this.summaryTextColor;
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

    public void setItemBackground(Drawable drawable) {
        this.itemBackground = drawable;
        setBackground(drawable);
    }

    public void setOnButtonClickListener(InterfaceC0796a interfaceC0796a) {
        this.f1216ge = interfaceC0796a;
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
        this.f1215fe = drawable;
        this.f1214ee.setBackground(drawable);
    }

    public void setRightIconHeight(int i) {
        this.rightIconHeight = i;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1214ee.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = this.rightIconWidth;
            layoutParams.height = i;
            this.f1214ee.setLayoutParams(layoutParams);
        }
    }

    public void setRightIconWidth(int i) {
        this.rightIconWidth = i;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1214ee.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = i;
            layoutParams.height = this.rightIconHeight;
            this.f1214ee.setLayoutParams(layoutParams);
        }
    }

    public void setSummaryTextColor(int i) {
        this.summaryTextColor = i;
    }

    public void setTitle(String str) {
        this.title = str;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f1213de.setText(str);
    }

    public void setTitleTextColor(int i) {
        this.titleTextColor = i;
        this.f1213de.setTextColor(i);
    }

    public void setTitleTextSize(int i) {
        this.f1213de.setTextSize(0, i);
    }

    public void setTitleTextColor(ColorStateList colorStateList) {
        this.f1213de.setTextColor(colorStateList);
    }

    public ButtonPreference(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView(context);
        m1533a(attributeSet);
    }

    public ButtonPreference(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.context = context;
        initView(context);
        m1533a(attributeSet);
    }
}
