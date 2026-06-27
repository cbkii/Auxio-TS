package com.p073tw.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.Arrays;

/* loaded from: classes4.dex */
public class SingleChoosePreference extends LinearLayout {
    private Context context;

    /* renamed from: de */
    private TextView f1225de;

    /* renamed from: fe */
    private Drawable f1226fe;

    /* renamed from: he */
    private LinearLayout f1227he;

    /* renamed from: ie */
    private TextView f1228ie;
    private Drawable itemBackground;

    /* renamed from: je */
    private TextView f1229je;

    /* renamed from: le */
    private ImageView f1230le;
    private CharSequence[] mEntries;
    private String mValue;

    /* renamed from: me */
    private CharSequence[] f1231me;

    /* renamed from: ne */
    private int f1232ne;

    /* renamed from: oe */
    private PopupWindow f1233oe;
    private int paddingLeft;
    private int paddingRight;

    /* renamed from: pe */
    private Drawable f1234pe;
    private int popItemHeight;
    private int popItemTextColor;
    private int popItemTextSize;
    private int popItemWidth;
    private Drawable popupBackground;

    /* renamed from: qe */
    private Drawable f1235qe;

    /* renamed from: re */
    private Drawable f1236re;
    private int rightIconHeight;
    private int rightIconWidth;

    /* renamed from: se */
    private InterfaceC0797a f1237se;
    private String subTitle;
    private int subTitleTextColor;
    private int subTitleTextSize;
    private String summary;
    private int summaryTextColor;
    private int summaryTextSize;

    /* renamed from: te */
    private View.OnClickListener f1238te;
    private String title;
    private int titleTextColor;
    private int titleTextSize;

    /* renamed from: com.tw.preference.SingleChoosePreference$a */
    public interface InterfaceC0797a {
        /* renamed from: a */
        void m1551a(SingleChoosePreference singleChoosePreference, Object obj);
    }

    public SingleChoosePreference(Context context) {
        super(context);
        this.popItemWidth = 64;
        this.popItemHeight = 64;
        this.popItemTextSize = 26;
        this.popItemTextColor = -1;
        this.f1238te = new ViewOnClickListenerC0802d(this);
        initView(context);
    }

    /* renamed from: Ce */
    private void m1536Ce() {
        setPadding(this.paddingLeft, 0, this.paddingRight, 0);
        this.f1225de.setText(this.title);
        this.f1225de.setTextSize(0, this.titleTextSize);
        this.f1225de.setTextColor(this.titleTextColor);
        this.f1228ie.setTextSize(0, this.subTitleTextSize);
        this.f1228ie.setTextColor(this.subTitleTextColor);
        if (TextUtils.isEmpty(this.subTitle)) {
            this.f1228ie.setVisibility(8);
        } else {
            this.f1228ie.setVisibility(0);
            this.f1228ie.setText(this.subTitle);
        }
        this.f1229je.setText(this.summary);
        this.f1229je.setTextSize(0, this.summaryTextSize);
        this.f1229je.setTextColor(this.summaryTextColor);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1230le.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = this.rightIconWidth;
            layoutParams.height = this.rightIconHeight;
            this.f1230le.setLayoutParams(layoutParams);
        }
        Drawable drawable = this.f1226fe;
        if (drawable != null) {
            this.f1230le.setImageDrawable(drawable);
        }
        if (this.itemBackground == null) {
            this.itemBackground = this.context.getDrawable(R$drawable.preference_item_bg);
        }
        setBackground(this.itemBackground);
    }

    /* renamed from: Ha */
    private String m1537Ha(int i) {
        if (i < 0) {
            return "";
        }
        CharSequence[] charSequenceArr = this.mEntries;
        return i > charSequenceArr.length ? "" : charSequenceArr[i].toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ia */
    public void m1538Ia(int i) {
        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        linearLayout.setGravity(17);
        linearLayout.setPadding(0, 4, 0, 4);
        for (int i2 = 0; i2 < this.mEntries.length; i2++) {
            TextView textView = new TextView(this.context);
            textView.setText(this.mEntries[i2]);
            textView.setTextColor(this.popItemTextColor);
            textView.setWidth(-1);
            textView.setGravity(17);
            textView.setTextSize(0, this.popItemTextSize);
            textView.setTag(Integer.valueOf(i2));
            LevelListDrawable levelListDrawable = new LevelListDrawable();
            levelListDrawable.addLevel(0, 0, this.f1235qe);
            levelListDrawable.addLevel(0, 1, this.f1236re);
            textView.setBackground(levelListDrawable);
            textView.setLayoutParams(new LinearLayout.LayoutParams(this.popItemWidth, this.popItemHeight));
            textView.setOnClickListener(this.f1238te);
            if (i == i2) {
                textView.getBackground().setLevel(1);
            } else {
                textView.getBackground().setLevel(0);
            }
            linearLayout.addView(textView);
        }
        this.f1233oe = new PopupWindow(linearLayout);
        this.f1233oe.setWidth(this.popItemWidth + 4);
        this.f1233oe.setHeight((this.mEntries.length * this.popItemHeight) + 2);
        this.f1233oe.setFocusable(true);
        this.f1233oe.setContentView(linearLayout);
        int[] iArr = new int[2];
        this.f1230le.getLocationInWindow(iArr);
        this.f1233oe.setBackgroundDrawable(this.popupBackground);
        int[] m1543a = m1543a(this.f1230le, linearLayout);
        Log.d("SingleChoosePreferenceT", "showChooseList: " + Arrays.toString(m1543a));
        int i3 = this.context.getResources().getDisplayMetrics().widthPixels;
        int i4 = iArr[0];
        m1543a[0] = m1543a[0] - (this.f1230le.getMeasuredWidth() / 2);
        this.f1233oe.showAtLocation(this.f1230le, 8388659, m1543a[0], m1543a[1]);
        this.f1233oe.setOnDismissListener(new C0801c(this));
    }

    private void initView(Context context) {
        this.context = context;
        this.f1227he = new LinearLayout(context);
        this.f1227he.setGravity(16);
        addView(this.f1227he, new LinearLayout.LayoutParams(-1, -1));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        layoutParams.weight = 1.0f;
        this.f1227he.addView(linearLayout, layoutParams);
        this.f1225de = new TextView(context);
        this.f1225de.setGravity(19);
        this.f1225de.setMaxLines(1);
        this.f1225de.setTextColor(-1);
        this.f1225de.setTextSize(0, context.getResources().getDimension(R$dimen.tw_dp_h32));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.weight = 1.0f;
        linearLayout.addView(this.f1225de, layoutParams2);
        this.f1228ie = new TextView(context);
        this.f1228ie.setGravity(19);
        this.f1228ie.setMaxLines(1);
        this.f1228ie.setTextColor(Color.parseColor("#919191"));
        this.f1228ie.setTextSize(0, context.getResources().getDimension(R$dimen.tw_dp_h24));
        this.f1228ie.setVisibility(8);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams3.weight = 1.0f;
        linearLayout.addView(this.f1228ie, layoutParams3);
        this.f1229je = new TextView(context);
        this.f1229je.setGravity(21);
        this.f1229je.setMaxLines(1);
        this.f1229je.setTextColor(Color.parseColor("#919191"));
        this.f1229je.setTextSize(0, context.getResources().getDimension(R$dimen.tw_dp_h28));
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-2, -1);
        layoutParams4.setMargins((int) context.getResources().getDimension(R$dimen.tw_dp_w32), 0, (int) context.getResources().getDimension(R$dimen.tw_dp_w32), 0);
        this.f1227he.addView(this.f1229je, layoutParams4);
        this.f1230le = new ImageView(context);
        this.f1230le.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        int dimension = (int) context.getResources().getDimension(R$dimen.tw_dp_h75);
        this.f1227he.addView(this.f1230le, new LinearLayout.LayoutParams(dimension, dimension));
        setClickable(true);
        this.f1230le.setOnClickListener(new ViewOnClickListenerC0800b(this));
        this.popupBackground = context.getDrawable(R$drawable.preference_single_choose_item_bg);
        this.f1234pe = context.getDrawable(R$drawable.lev_preference_single_choose_item_bg);
        this.f1235qe = context.getDrawable(R$drawable.preference_single_choose_item_nor);
        this.f1236re = context.getDrawable(R$drawable.preference_single_choose_item_pres);
        this.itemBackground = context.getDrawable(R$drawable.preference_item_bg);
        this.titleTextSize = (int) context.getResources().getDimension(R$dimen.tw_dp_h32);
        this.subTitleTextSize = (int) context.getResources().getDimension(R$dimen.tw_dp_h24);
        this.summaryTextSize = (int) context.getResources().getDimension(R$dimen.tw_dp_h28);
        this.titleTextColor = -1;
        int parseColor = Color.parseColor("#919191");
        this.subTitleTextColor = parseColor;
        this.titleTextColor = parseColor;
        this.summaryTextColor = Color.parseColor("#919191");
        this.f1226fe = context.getDrawable(R$drawable.lev_single_choose_expand);
        this.popItemWidth = (int) getResources().getDimension(R$dimen.tw_dp_w260);
        this.popItemHeight = (int) getResources().getDimension(R$dimen.tw_dp_h78);
        this.popItemTextSize = (int) getResources().getDimension(R$dimen.tw_dp_h24);
        this.popItemTextColor = -1;
        this.popupBackground = context.getDrawable(R$drawable.preference_single_choose_item_bg);
        this.f1234pe = context.getDrawable(R$drawable.lev_preference_single_choose_item_bg);
        this.f1235qe = context.getDrawable(R$drawable.preference_single_choose_item_nor);
        this.f1236re = context.getDrawable(R$drawable.preference_single_choose_item_pres);
        this.paddingLeft = (int) getResources().getDimension(R$dimen.tw_dp_w64);
        this.paddingRight = (int) getResources().getDimension(R$dimen.tw_dp_w64);
        this.rightIconWidth = (int) getResources().getDimension(R$dimen.tw_dp_h75);
        this.rightIconHeight = (int) getResources().getDimension(R$dimen.tw_dp_h75);
    }

    public int findIndexOfValue(String str) {
        CharSequence[] charSequenceArr;
        if (str == null || (charSequenceArr = this.f1231me) == null) {
            return -1;
        }
        for (int length = charSequenceArr.length - 1; length >= 0; length--) {
            if (this.f1231me[length].equals(str)) {
                return length;
            }
        }
        return -1;
    }

    public CharSequence[] getEntries() {
        return this.mEntries;
    }

    public CharSequence[] getEntryValues() {
        return this.f1231me;
    }

    @Override // android.view.View
    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    @Override // android.view.View
    public int getPaddingRight() {
        return this.paddingRight;
    }

    public int getPopItemHeight() {
        return this.popItemHeight;
    }

    public int getPopItemTextColor() {
        return this.popItemTextColor;
    }

    public float getPopItemTextSize() {
        return this.popItemTextSize;
    }

    public int getPopItemWidth() {
        return this.popItemWidth;
    }

    public Drawable getPopupBackground() {
        return this.popupBackground;
    }

    public Drawable getPopupItemBackgroundNor() {
        return this.f1235qe;
    }

    public Drawable getPopupItemBackgroundPres() {
        return this.f1236re;
    }

    public Drawable getRightIcon() {
        return this.f1226fe;
    }

    public int getRightIconHeight() {
        return this.rightIconHeight;
    }

    public int getRightIconWidth() {
        return this.rightIconWidth;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public int getSubTitleTextColor() {
        return this.subTitleTextColor;
    }

    public int getSubTitleTextSize() {
        return this.subTitleTextSize;
    }

    public String getSummary() {
        return this.summary;
    }

    public int getSummaryTextColor() {
        return this.summaryTextColor;
    }

    public int getSummaryTextSize() {
        return this.summaryTextSize;
    }

    public String getTitle() {
        return this.title;
    }

    public int getTitleTextColor() {
        return this.titleTextColor;
    }

    public int getTitleTextSize() {
        return this.titleTextSize;
    }

    public TextView getTvSubTitle() {
        return this.f1228ie;
    }

    public String getValue() {
        return this.mValue;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        super.setBackground(drawable);
        this.itemBackground = drawable;
    }

    public void setCurrentEntry(String str) {
        int m1550ua = m1550ua(str);
        if (m1550ua != -1) {
            CharSequence[] charSequenceArr = this.mEntries;
            if (m1550ua > charSequenceArr.length - 1) {
                return;
            }
            this.f1232ne = m1550ua;
            this.f1229je.setText(charSequenceArr[m1550ua]);
        }
    }

    public void setEntries(CharSequence[] charSequenceArr) {
        this.mEntries = charSequenceArr;
    }

    public void setEntryValues(CharSequence[] charSequenceArr) {
        this.f1231me = charSequenceArr;
    }

    public void setItemBackground(Drawable drawable) {
        this.itemBackground = drawable;
        setBackground(drawable);
    }

    public void setOnChooseValueChange(InterfaceC0797a interfaceC0797a) {
        this.f1237se = interfaceC0797a;
    }

    public void setPaddingLeft(int i) {
        this.paddingLeft = i;
        setPadding(i, 0, this.paddingRight, 0);
    }

    public void setPaddingRight(int i) {
        this.paddingRight = i;
        setPadding(this.paddingLeft, 0, i, 0);
    }

    public void setPopItemHeight(int i) {
        this.popItemHeight = i;
    }

    public void setPopItemTextColor(int i) {
        this.popItemTextColor = i;
    }

    public void setPopItemTextSize(float f) {
        this.popItemTextSize = (int) f;
    }

    public void setPopItemWidth(int i) {
        this.popItemWidth = i;
    }

    public void setPopupBackground(Drawable drawable) {
        this.popupBackground = drawable;
    }

    public void setPopupItemBackgroundNor(Drawable drawable) {
        this.f1235qe = drawable;
    }

    public void setPopupItemBackgroundPres(Drawable drawable) {
        this.f1236re = drawable;
    }

    public void setRightIcon(Drawable drawable) {
        this.f1226fe = drawable;
        this.f1230le.setImageDrawable(drawable);
    }

    public void setRightIconHeight(int i) {
        this.rightIconHeight = i;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1230le.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = this.rightIconWidth;
            layoutParams.height = i;
            this.f1230le.setLayoutParams(layoutParams);
        }
    }

    public void setRightIconWidth(int i) {
        this.rightIconWidth = i;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f1230le.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = i;
            layoutParams.height = this.rightIconHeight;
            this.f1230le.setLayoutParams(layoutParams);
        }
    }

    public void setSubTitle(String str) {
        this.subTitle = str;
        this.f1228ie.setText(str);
    }

    public void setSubTitleTextColor(int i) {
        this.subTitleTextColor = i;
        this.f1228ie.setTextColor(i);
    }

    public void setSubTitleTextSize(int i) {
        this.subTitleTextSize = i;
        this.f1228ie.setTextSize(0, this.subTitleTextColor);
    }

    public void setSummary(String str) {
        this.summary = str;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f1229je.setText(str);
    }

    public void setSummaryTextColor(int i) {
        this.summaryTextColor = i;
        this.f1229je.setTextColor(i);
    }

    public void setSummaryTextSize(int i) {
        this.summaryTextSize = i;
        this.f1229je.setTextSize(0, i);
    }

    public void setTitle(String str) {
        this.title = str;
        this.f1225de.setText(str);
    }

    public void setTitleTextColor(int i) {
        this.titleTextColor = i;
        this.f1225de.setTextColor(i);
    }

    public void setTitleTextSize(int i) {
        this.titleTextSize = i;
        this.f1225de.setTextSize(0, i);
    }

    public void setTvSubTitle(String str) {
        this.subTitle = str;
        if (TextUtils.isEmpty(str)) {
            this.f1228ie.setVisibility(8);
        } else {
            this.f1228ie.setVisibility(0);
        }
    }

    public void setValue(String str) {
        int findIndexOfValue = findIndexOfValue(str);
        if (findIndexOfValue == -1) {
            return;
        }
        this.f1232ne = findIndexOfValue;
        this.f1229je.setText(m1537Ha(findIndexOfValue));
        this.mValue = str;
    }

    /* renamed from: ua */
    public int m1550ua(String str) {
        CharSequence[] charSequenceArr;
        if (str == null || (charSequenceArr = this.mEntries) == null) {
            return -1;
        }
        for (int length = charSequenceArr.length - 1; length >= 0; length--) {
            if (this.mEntries[length].equals(str)) {
                return length;
            }
        }
        return -1;
    }

    public void setSubTitleTextColor(ColorStateList colorStateList) {
        this.f1228ie.setTextColor(colorStateList);
    }

    public void setSummaryTextColor(ColorStateList colorStateList) {
        this.f1229je.setTextColor(colorStateList);
    }

    public void setTitleTextColor(ColorStateList colorStateList) {
        this.f1225de.setTextColor(colorStateList);
    }

    /* renamed from: a */
    private void m1542a(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = this.context.obtainStyledAttributes(attributeSet, R$styleable.SingleChoosePreference);
        this.itemBackground = obtainStyledAttributes.getDrawable(R$styleable.SingleChoosePreference_android_background);
        this.title = obtainStyledAttributes.getString(R$styleable.SingleChoosePreference_android_title);
        this.subTitle = obtainStyledAttributes.getString(R$styleable.SingleChoosePreference_subTitle);
        this.summary = obtainStyledAttributes.getString(R$styleable.SingleChoosePreference_android_summary);
        this.titleTextSize = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_titleTextSize, (int) this.context.getResources().getDimension(R$dimen.tw_dp_h32));
        this.subTitleTextSize = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_subTitleTextSize, (int) this.context.getResources().getDimension(R$dimen.tw_dp_h24));
        this.summaryTextSize = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_summaryTextSize, (int) this.context.getResources().getDimension(R$dimen.tw_dp_h28));
        this.titleTextColor = obtainStyledAttributes.getColor(R$styleable.SingleChoosePreference_titleTextColor, -1);
        this.subTitleTextColor = obtainStyledAttributes.getColor(R$styleable.SingleChoosePreference_subTitleTextColor, Color.parseColor("#919191"));
        this.summaryTextColor = obtainStyledAttributes.getColor(R$styleable.SingleChoosePreference_summaryTextColor, Color.parseColor("#919191"));
        this.f1226fe = obtainStyledAttributes.getDrawable(R$styleable.SingleChoosePreference_rightIconBackground);
        this.popItemWidth = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_popItemWidth, (int) getResources().getDimension(R$dimen.tw_dp_w260));
        this.popItemHeight = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_popItemHeight, (int) getResources().getDimension(R$dimen.tw_dp_h78));
        this.popItemTextSize = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_popItemTextSize, (int) getResources().getDimension(R$dimen.tw_dp_h24));
        this.popItemTextColor = obtainStyledAttributes.getColor(R$styleable.SingleChoosePreference_popItemTextColor, this.titleTextColor);
        this.mEntries = obtainStyledAttributes.getTextArray(R$styleable.SingleChoosePreference_android_entries);
        this.f1231me = obtainStyledAttributes.getTextArray(R$styleable.SingleChoosePreference_android_entryValues);
        this.popupBackground = obtainStyledAttributes.getDrawable(R$styleable.SingleChoosePreference_popBackground);
        if (this.popupBackground == null) {
            this.popupBackground = this.context.getDrawable(R$drawable.preference_single_choose_item_bg);
        }
        this.f1235qe = obtainStyledAttributes.getDrawable(R$styleable.SingleChoosePreference_popItemNormal);
        this.f1236re = obtainStyledAttributes.getDrawable(R$styleable.SingleChoosePreference_popItemSelect);
        this.paddingLeft = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_paddingLeft, getResources().getDimension(R$dimen.tw_dp_w64));
        this.paddingRight = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_paddingRight, getResources().getDimension(R$dimen.tw_dp_w64));
        this.rightIconWidth = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_rightIconWidth, getResources().getDimension(R$dimen.tw_dp_h75));
        this.rightIconHeight = (int) obtainStyledAttributes.getDimension(R$styleable.SingleChoosePreference_rightIconHeight, getResources().getDimension(R$dimen.tw_dp_h75));
        obtainStyledAttributes.recycle();
        m1536Ce();
    }

    public SingleChoosePreference(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.popItemWidth = 64;
        this.popItemHeight = 64;
        this.popItemTextSize = 26;
        this.popItemTextColor = -1;
        this.f1238te = new ViewOnClickListenerC0802d(this);
        initView(context);
        m1542a(attributeSet);
    }

    public SingleChoosePreference(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.popItemWidth = 64;
        this.popItemHeight = 64;
        this.popItemTextSize = 26;
        this.popItemTextColor = -1;
        this.f1238te = new ViewOnClickListenerC0802d(this);
        initView(context);
        m1542a(attributeSet);
    }

    /* renamed from: a */
    private int[] m1543a(View view, View view2) {
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        view.getLocationInWindow(iArr2);
        int height = view.getHeight();
        int i = this.context.getResources().getDisplayMetrics().heightPixels;
        int i2 = this.context.getResources().getDisplayMetrics().widthPixels;
        view2.measure(0, 0);
        int measuredHeight = view2.getMeasuredHeight();
        int measuredWidth = view2.getMeasuredWidth();
        if ((i - iArr2[1]) - height < measuredHeight) {
            iArr[0] = i2 - measuredWidth;
            iArr[1] = iArr2[1] - measuredHeight;
        } else {
            iArr[0] = i2 - measuredWidth;
            iArr[1] = iArr2[1] + height;
        }
        return iArr;
    }
}
