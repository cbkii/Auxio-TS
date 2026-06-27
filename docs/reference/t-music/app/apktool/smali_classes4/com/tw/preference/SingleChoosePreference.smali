.class public Lcom/tw/preference/SingleChoosePreference;
.super Landroid/widget/LinearLayout;
.source "SingleChoosePreference.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/tw/preference/SingleChoosePreference$a;
    }
.end annotation


# instance fields
.field private context:Landroid/content/Context;

.field private de:Landroid/widget/TextView;

.field private fe:Landroid/graphics/drawable/Drawable;

.field private he:Landroid/widget/LinearLayout;

.field private ie:Landroid/widget/TextView;

.field private itemBackground:Landroid/graphics/drawable/Drawable;

.field private je:Landroid/widget/TextView;

.field private le:Landroid/widget/ImageView;

.field private mEntries:[Ljava/lang/CharSequence;

.field private mValue:Ljava/lang/String;

.field private me:[Ljava/lang/CharSequence;

.field private ne:I

.field private oe:Landroid/widget/PopupWindow;

.field private paddingLeft:I

.field private paddingRight:I

.field private pe:Landroid/graphics/drawable/Drawable;

.field private popItemHeight:I

.field private popItemTextColor:I

.field private popItemTextSize:I

.field private popItemWidth:I

.field private popupBackground:Landroid/graphics/drawable/Drawable;

.field private qe:Landroid/graphics/drawable/Drawable;

.field private re:Landroid/graphics/drawable/Drawable;

.field private rightIconHeight:I

.field private rightIconWidth:I

.field private se:Lcom/tw/preference/SingleChoosePreference$a;

.field private subTitle:Ljava/lang/String;

.field private subTitleTextColor:I

.field private subTitleTextSize:I

.field private summary:Ljava/lang/String;

.field private summaryTextColor:I

.field private summaryTextSize:I

.field private te:Landroid/view/View$OnClickListener;

.field private title:Ljava/lang/String;

.field private titleTextColor:I

.field private titleTextSize:I


# direct methods
.method public constructor <init>(Landroid/content/Context;)V
    .locals 1

    .line 1
    invoke-direct {p0, p1}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    const/16 v0, 0x40

    .line 2
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    .line 3
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    const/16 v0, 0x1a

    .line 4
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextSize:I

    const/4 v0, -0x1

    .line 5
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextColor:I

    .line 6
    new-instance v0, Lcom/tw/preference/d;

    invoke-direct {v0, p0}, Lcom/tw/preference/d;-><init>(Lcom/tw/preference/SingleChoosePreference;)V

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->te:Landroid/view/View$OnClickListener;

    .line 7
    invoke-direct {p0, p1}, Lcom/tw/preference/SingleChoosePreference;->initView(Landroid/content/Context;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;)V
    .locals 1
    .param p2    # Landroid/util/AttributeSet;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param

    .line 8
    invoke-direct {p0, p1, p2}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V

    const/16 v0, 0x40

    .line 9
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    .line 10
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    const/16 v0, 0x1a

    .line 11
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextSize:I

    const/4 v0, -0x1

    .line 12
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextColor:I

    .line 13
    new-instance v0, Lcom/tw/preference/d;

    invoke-direct {v0, p0}, Lcom/tw/preference/d;-><init>(Lcom/tw/preference/SingleChoosePreference;)V

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->te:Landroid/view/View$OnClickListener;

    .line 14
    invoke-direct {p0, p1}, Lcom/tw/preference/SingleChoosePreference;->initView(Landroid/content/Context;)V

    .line 15
    invoke-direct {p0, p2}, Lcom/tw/preference/SingleChoosePreference;->a(Landroid/util/AttributeSet;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V
    .locals 0
    .param p2    # Landroid/util/AttributeSet;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param

    .line 16
    invoke-direct {p0, p1, p2, p3}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V

    const/16 p3, 0x40

    .line 17
    iput p3, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    .line 18
    iput p3, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    const/16 p3, 0x1a

    .line 19
    iput p3, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextSize:I

    const/4 p3, -0x1

    .line 20
    iput p3, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextColor:I

    .line 21
    new-instance p3, Lcom/tw/preference/d;

    invoke-direct {p3, p0}, Lcom/tw/preference/d;-><init>(Lcom/tw/preference/SingleChoosePreference;)V

    iput-object p3, p0, Lcom/tw/preference/SingleChoosePreference;->te:Landroid/view/View$OnClickListener;

    .line 22
    invoke-direct {p0, p1}, Lcom/tw/preference/SingleChoosePreference;->initView(Landroid/content/Context;)V

    .line 23
    invoke-direct {p0, p2}, Lcom/tw/preference/SingleChoosePreference;->a(Landroid/util/AttributeSet;)V

    return-void
.end method

.method private Ce()V
    .locals 3

    .line 1
    iget v0, p0, Lcom/tw/preference/SingleChoosePreference;->paddingLeft:I

    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->paddingRight:I

    const/4 v2, 0x0

    invoke-virtual {p0, v0, v2, v1, v2}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    .line 2
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->title:Ljava/lang/String;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 3
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextSize:I

    int-to-float v1, v1

    invoke-virtual {v0, v2, v1}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 4
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextColor:I

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setTextColor(I)V

    .line 5
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextSize:I

    int-to-float v1, v1

    invoke-virtual {v0, v2, v1}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 6
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextColor:I

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setTextColor(I)V

    .line 7
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitle:Ljava/lang/String;

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_0

    .line 8
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    const/16 v1, 0x8

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setVisibility(I)V

    goto :goto_0

    .line 9
    :cond_0
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    invoke-virtual {v0, v2}, Landroid/widget/TextView;->setVisibility(I)V

    .line 10
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->subTitle:Ljava/lang/String;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 11
    :goto_0
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->summary:Ljava/lang/String;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 12
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextSize:I

    int-to-float v1, v1

    invoke-virtual {v0, v2, v1}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 13
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextColor:I

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setTextColor(I)V

    .line 14
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {v0}, Landroid/widget/ImageView;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_1

    .line 15
    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconWidth:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 16
    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconHeight:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 17
    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {v1, v0}, Landroid/widget/ImageView;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    .line 18
    :cond_1
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->fe:Landroid/graphics/drawable/Drawable;

    if-eqz v0, :cond_2

    .line 19
    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {v1, v0}, Landroid/widget/ImageView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    .line 20
    :cond_2
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    if-nez v0, :cond_3

    .line 21
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    sget v1, Lcom/tw/preference/R$drawable;->preference_item_bg:I

    invoke-virtual {v0, v1}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 22
    :cond_3
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    invoke-virtual {p0, v0}, Lcom/tw/preference/SingleChoosePreference;->setBackground(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method private Ha(I)Ljava/lang/String;
    .locals 1

    if-ltz p1, :cond_1

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    array-length v0, p0

    if-le p1, v0, :cond_0

    goto :goto_0

    .line 2
    :cond_0
    aget-object p0, p0, p1

    invoke-interface {p0}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_1
    :goto_0
    const-string p0, ""

    return-object p0
.end method

.method private Ia(I)V
    .locals 10

    .line 1
    new-instance v0, Landroid/widget/LinearLayout;

    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    invoke-direct {v0, v1}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    const/4 v1, 0x1

    .line 2
    invoke-virtual {v0, v1}, Landroid/widget/LinearLayout;->setOrientation(I)V

    .line 3
    new-instance v2, Landroid/widget/LinearLayout$LayoutParams;

    const/4 v3, -0x2

    invoke-direct {v2, v3, v3}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 4
    invoke-virtual {v0, v2}, Landroid/widget/LinearLayout;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    const/16 v2, 0x11

    .line 5
    invoke-virtual {v0, v2}, Landroid/widget/LinearLayout;->setGravity(I)V

    const/4 v3, 0x4

    const/4 v4, 0x0

    .line 6
    invoke-virtual {v0, v4, v3, v4, v3}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    move v5, v4

    .line 7
    :goto_0
    iget-object v6, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    array-length v6, v6

    if-ge v5, v6, :cond_1

    .line 8
    new-instance v6, Landroid/widget/TextView;

    iget-object v7, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    invoke-direct {v6, v7}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    .line 9
    iget-object v7, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    aget-object v7, v7, v5

    invoke-virtual {v6, v7}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 10
    iget v7, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextColor:I

    invoke-virtual {v6, v7}, Landroid/widget/TextView;->setTextColor(I)V

    const/4 v7, -0x1

    .line 11
    invoke-virtual {v6, v7}, Landroid/widget/TextView;->setWidth(I)V

    .line 12
    invoke-virtual {v6, v2}, Landroid/widget/TextView;->setGravity(I)V

    .line 13
    iget v7, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextSize:I

    int-to-float v7, v7

    invoke-virtual {v6, v4, v7}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 14
    invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v7

    invoke-virtual {v6, v7}, Landroid/widget/TextView;->setTag(Ljava/lang/Object;)V

    .line 15
    new-instance v7, Landroid/graphics/drawable/LevelListDrawable;

    invoke-direct {v7}, Landroid/graphics/drawable/LevelListDrawable;-><init>()V

    .line 16
    iget-object v8, p0, Lcom/tw/preference/SingleChoosePreference;->qe:Landroid/graphics/drawable/Drawable;

    invoke-virtual {v7, v4, v4, v8}, Landroid/graphics/drawable/LevelListDrawable;->addLevel(IILandroid/graphics/drawable/Drawable;)V

    .line 17
    iget-object v8, p0, Lcom/tw/preference/SingleChoosePreference;->re:Landroid/graphics/drawable/Drawable;

    invoke-virtual {v7, v4, v1, v8}, Landroid/graphics/drawable/LevelListDrawable;->addLevel(IILandroid/graphics/drawable/Drawable;)V

    .line 18
    invoke-virtual {v6, v7}, Landroid/widget/TextView;->setBackground(Landroid/graphics/drawable/Drawable;)V

    .line 19
    iget v7, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    .line 20
    iget v8, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    .line 21
    new-instance v9, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v9, v7, v8}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 22
    invoke-virtual {v6, v9}, Landroid/widget/TextView;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    .line 23
    iget-object v7, p0, Lcom/tw/preference/SingleChoosePreference;->te:Landroid/view/View$OnClickListener;

    invoke-virtual {v6, v7}, Landroid/widget/TextView;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    if-ne p1, v5, :cond_0

    .line 24
    invoke-virtual {v6}, Landroid/widget/TextView;->getBackground()Landroid/graphics/drawable/Drawable;

    move-result-object v7

    invoke-virtual {v7, v1}, Landroid/graphics/drawable/Drawable;->setLevel(I)Z

    goto :goto_1

    .line 25
    :cond_0
    invoke-virtual {v6}, Landroid/widget/TextView;->getBackground()Landroid/graphics/drawable/Drawable;

    move-result-object v7

    invoke-virtual {v7, v4}, Landroid/graphics/drawable/Drawable;->setLevel(I)Z

    .line 26
    :goto_1
    invoke-virtual {v0, v6}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;)V

    add-int/lit8 v5, v5, 0x1

    goto :goto_0

    .line 27
    :cond_1
    new-instance p1, Landroid/widget/PopupWindow;

    invoke-direct {p1, v0}, Landroid/widget/PopupWindow;-><init>(Landroid/view/View;)V

    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    .line 28
    iget-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    iget v2, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    add-int/2addr v2, v3

    invoke-virtual {p1, v2}, Landroid/widget/PopupWindow;->setWidth(I)V

    .line 29
    iget-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    array-length v2, v2

    iget v3, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    mul-int/2addr v2, v3

    const/4 v3, 0x2

    add-int/2addr v2, v3

    invoke-virtual {p1, v2}, Landroid/widget/PopupWindow;->setHeight(I)V

    .line 30
    iget-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    invoke-virtual {p1, v1}, Landroid/widget/PopupWindow;->setFocusable(Z)V

    .line 31
    iget-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    invoke-virtual {p1, v0}, Landroid/widget/PopupWindow;->setContentView(Landroid/view/View;)V

    new-array p1, v3, [I

    .line 32
    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {v2, p1}, Landroid/widget/ImageView;->getLocationInWindow([I)V

    .line 33
    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    iget-object v5, p0, Lcom/tw/preference/SingleChoosePreference;->popupBackground:Landroid/graphics/drawable/Drawable;

    invoke-virtual {v2, v5}, Landroid/widget/PopupWindow;->setBackgroundDrawable(Landroid/graphics/drawable/Drawable;)V

    .line 34
    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-direct {p0, v2, v0}, Lcom/tw/preference/SingleChoosePreference;->a(Landroid/view/View;Landroid/view/View;)[I

    move-result-object v0

    .line 35
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "showChooseList: "

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v0}, Ljava/util/Arrays;->toString([I)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    const-string v5, "SingleChoosePreferenceT"

    invoke-static {v5, v2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 36
    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    invoke-virtual {v2}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/res/Resources;->getDisplayMetrics()Landroid/util/DisplayMetrics;

    move-result-object v2

    iget v2, v2, Landroid/util/DisplayMetrics;->widthPixels:I

    aget p1, p1, v4

    .line 37
    aget p1, v0, v4

    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {v2}, Landroid/widget/ImageView;->getMeasuredWidth()I

    move-result v2

    div-int/2addr v2, v3

    sub-int/2addr p1, v2

    aput p1, v0, v4

    .line 38
    iget-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    const v3, 0x800033

    aget v4, v0, v4

    aget v0, v0, v1

    invoke-virtual {p1, v2, v3, v4, v0}, Landroid/widget/PopupWindow;->showAtLocation(Landroid/view/View;III)V

    .line 39
    iget-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    new-instance v0, Lcom/tw/preference/c;

    invoke-direct {v0, p0}, Lcom/tw/preference/c;-><init>(Lcom/tw/preference/SingleChoosePreference;)V

    invoke-virtual {p1, v0}, Landroid/widget/PopupWindow;->setOnDismissListener(Landroid/widget/PopupWindow$OnDismissListener;)V

    return-void
.end method

.method static synthetic a(Lcom/tw/preference/SingleChoosePreference;I)I
    .locals 0

    .line 2
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->ne:I

    return p1
.end method

.method static synthetic a(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/ImageView;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    return-object p0
.end method

.method static synthetic a(Lcom/tw/preference/SingleChoosePreference;Ljava/lang/String;)Ljava/lang/String;
    .locals 0

    .line 3
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->mValue:Ljava/lang/String;

    return-object p1
.end method

.method private a(Landroid/util/AttributeSet;)V
    .locals 3

    .line 4
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    sget-object v1, Lcom/tw/preference/R$styleable;->SingleChoosePreference:[I

    invoke-virtual {v0, p1, v1}, Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;

    move-result-object p1

    .line 5
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_android_background:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 6
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_android_title:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getString(I)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->title:Ljava/lang/String;

    .line 7
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_subTitle:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getString(I)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitle:Ljava/lang/String;

    .line 8
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_android_summary:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getString(I)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->summary:Ljava/lang/String;

    .line 9
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_titleTextSize:I

    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    .line 10
    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    float-to-int v1, v1

    int-to-float v1, v1

    .line 11
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextSize:I

    .line 12
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_subTitleTextSize:I

    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    .line 13
    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h24:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    float-to-int v1, v1

    int-to-float v1, v1

    .line 14
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextSize:I

    .line 15
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_summaryTextSize:I

    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    .line 16
    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h28:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    float-to-int v1, v1

    int-to-float v1, v1

    .line 17
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextSize:I

    .line 18
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_titleTextColor:I

    const/4 v1, -0x1

    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getColor(II)I

    move-result v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextColor:I

    .line 19
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_subTitleTextColor:I

    const-string v1, "#919191"

    .line 20
    invoke-static {v1}, Landroid/graphics/Color;->parseColor(Ljava/lang/String;)I

    move-result v2

    .line 21
    invoke-virtual {p1, v0, v2}, Landroid/content/res/TypedArray;->getColor(II)I

    move-result v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextColor:I

    .line 22
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_summaryTextColor:I

    invoke-static {v1}, Landroid/graphics/Color;->parseColor(Ljava/lang/String;)I

    move-result v1

    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getColor(II)I

    move-result v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextColor:I

    .line 23
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_rightIconBackground:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 24
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_popItemWidth:I

    .line 25
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_w260:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    float-to-int v1, v1

    int-to-float v1, v1

    .line 26
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    .line 27
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_popItemHeight:I

    .line 28
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h78:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    float-to-int v1, v1

    int-to-float v1, v1

    .line 29
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    .line 30
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_popItemTextSize:I

    .line 31
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h24:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    float-to-int v1, v1

    int-to-float v1, v1

    .line 32
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextSize:I

    .line 33
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_popItemTextColor:I

    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextColor:I

    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getColor(II)I

    move-result v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextColor:I

    .line 34
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_android_entries:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getTextArray(I)[Ljava/lang/CharSequence;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    .line 35
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_android_entryValues:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getTextArray(I)[Ljava/lang/CharSequence;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->me:[Ljava/lang/CharSequence;

    .line 36
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_popBackground:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->popupBackground:Landroid/graphics/drawable/Drawable;

    .line 37
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->popupBackground:Landroid/graphics/drawable/Drawable;

    if-nez v0, :cond_0

    .line 38
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    sget v1, Lcom/tw/preference/R$drawable;->preference_single_choose_item_bg:I

    invoke-virtual {v0, v1}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->popupBackground:Landroid/graphics/drawable/Drawable;

    .line 39
    :cond_0
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_popItemNormal:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->qe:Landroid/graphics/drawable/Drawable;

    .line 40
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_popItemSelect:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->re:Landroid/graphics/drawable/Drawable;

    .line 41
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_paddingLeft:I

    .line 42
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 43
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->paddingLeft:I

    .line 44
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_paddingRight:I

    .line 45
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 46
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->paddingRight:I

    .line 47
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_rightIconWidth:I

    .line 48
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h75:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 49
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconWidth:I

    .line 50
    sget v0, Lcom/tw/preference/R$styleable;->SingleChoosePreference_rightIconHeight:I

    .line 51
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h75:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 52
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconHeight:I

    .line 53
    invoke-virtual {p1}, Landroid/content/res/TypedArray;->recycle()V

    .line 54
    invoke-direct {p0}, Lcom/tw/preference/SingleChoosePreference;->Ce()V

    return-void
.end method

.method private a(Landroid/view/View;Landroid/view/View;)[I
    .locals 7

    const/4 v0, 0x2

    new-array v1, v0, [I

    new-array v0, v0, [I

    .line 55
    invoke-virtual {p1, v0}, Landroid/view/View;->getLocationInWindow([I)V

    .line 56
    invoke-virtual {p1}, Landroid/view/View;->getHeight()I

    move-result p1

    .line 57
    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    invoke-virtual {v2}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/res/Resources;->getDisplayMetrics()Landroid/util/DisplayMetrics;

    move-result-object v2

    iget v2, v2, Landroid/util/DisplayMetrics;->heightPixels:I

    .line 58
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p0

    invoke-virtual {p0}, Landroid/content/res/Resources;->getDisplayMetrics()Landroid/util/DisplayMetrics;

    move-result-object p0

    iget p0, p0, Landroid/util/DisplayMetrics;->widthPixels:I

    const/4 v3, 0x0

    .line 59
    invoke-virtual {p2, v3, v3}, Landroid/view/View;->measure(II)V

    .line 60
    invoke-virtual {p2}, Landroid/view/View;->getMeasuredHeight()I

    move-result v4

    .line 61
    invoke-virtual {p2}, Landroid/view/View;->getMeasuredWidth()I

    move-result p2

    const/4 v5, 0x1

    .line 62
    aget v6, v0, v5

    sub-int/2addr v2, v6

    sub-int/2addr v2, p1

    if-ge v2, v4, :cond_0

    move v2, v5

    goto :goto_0

    :cond_0
    move v2, v3

    :goto_0
    if-eqz v2, :cond_1

    sub-int/2addr p0, p2

    aput p0, v1, v3

    .line 63
    aget p0, v0, v5

    sub-int/2addr p0, v4

    aput p0, v1, v5

    goto :goto_1

    :cond_1
    sub-int/2addr p0, p2

    aput p0, v1, v3

    .line 64
    aget p0, v0, v5

    add-int/2addr p0, p1

    aput p0, v1, v5

    :goto_1
    return-object v1
.end method

.method static synthetic b(Lcom/tw/preference/SingleChoosePreference;)I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->ne:I

    return p0
.end method

.method static synthetic b(Lcom/tw/preference/SingleChoosePreference;I)V
    .locals 0

    .line 2
    invoke-direct {p0, p1}, Lcom/tw/preference/SingleChoosePreference;->Ia(I)V

    return-void
.end method

.method static synthetic c(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/PopupWindow;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->oe:Landroid/widget/PopupWindow;

    return-object p0
.end method

.method static synthetic d(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/TextView;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    return-object p0
.end method

.method static synthetic e(Lcom/tw/preference/SingleChoosePreference;)[Ljava/lang/CharSequence;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->me:[Ljava/lang/CharSequence;

    return-object p0
.end method

.method static synthetic f(Lcom/tw/preference/SingleChoosePreference;)Lcom/tw/preference/SingleChoosePreference$a;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->se:Lcom/tw/preference/SingleChoosePreference$a;

    return-object p0
.end method

.method private initView(Landroid/content/Context;)V
    .locals 10

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->context:Landroid/content/Context;

    .line 2
    new-instance v0, Landroid/widget/LinearLayout;

    invoke-direct {v0, p1}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->he:Landroid/widget/LinearLayout;

    .line 3
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->he:Landroid/widget/LinearLayout;

    const/16 v1, 0x10

    invoke-virtual {v0, v1}, Landroid/widget/LinearLayout;->setGravity(I)V

    .line 4
    new-instance v0, Landroid/widget/LinearLayout$LayoutParams;

    const/4 v1, -0x1

    invoke-direct {v0, v1, v1}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 5
    iget-object v2, p0, Lcom/tw/preference/SingleChoosePreference;->he:Landroid/widget/LinearLayout;

    invoke-virtual {p0, v2, v0}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 6
    new-instance v0, Landroid/widget/LinearLayout;

    invoke-direct {v0, p1}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    const/4 v2, 0x1

    .line 7
    invoke-virtual {v0, v2}, Landroid/widget/LinearLayout;->setOrientation(I)V

    .line 8
    new-instance v3, Landroid/widget/LinearLayout$LayoutParams;

    const/4 v4, -0x2

    invoke-direct {v3, v4, v1}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    const/high16 v5, 0x3f800000    # 1.0f

    .line 9
    iput v5, v3, Landroid/widget/LinearLayout$LayoutParams;->weight:F

    .line 10
    iget-object v6, p0, Lcom/tw/preference/SingleChoosePreference;->he:Landroid/widget/LinearLayout;

    invoke-virtual {v6, v0, v3}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 11
    new-instance v3, Landroid/widget/TextView;

    invoke-direct {v3, p1}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    iput-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    .line 12
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    const/16 v6, 0x13

    invoke-virtual {v3, v6}, Landroid/widget/TextView;->setGravity(I)V

    .line 13
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    invoke-virtual {v3, v2}, Landroid/widget/TextView;->setMaxLines(I)V

    .line 14
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    invoke-virtual {v3, v1}, Landroid/widget/TextView;->setTextColor(I)V

    .line 15
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v7

    sget v8, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {v7, v8}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v7

    const/4 v8, 0x0

    invoke-virtual {v3, v8, v7}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 16
    new-instance v3, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v3, v1, v4}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 17
    iput v5, v3, Landroid/widget/LinearLayout$LayoutParams;->weight:F

    .line 18
    iget-object v7, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    invoke-virtual {v0, v7, v3}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 19
    new-instance v3, Landroid/widget/TextView;

    invoke-direct {v3, p1}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    iput-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    .line 20
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    invoke-virtual {v3, v6}, Landroid/widget/TextView;->setGravity(I)V

    .line 21
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    invoke-virtual {v3, v2}, Landroid/widget/TextView;->setMaxLines(I)V

    .line 22
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    const-string v6, "#919191"

    invoke-static {v6}, Landroid/graphics/Color;->parseColor(Ljava/lang/String;)I

    move-result v7

    invoke-virtual {v3, v7}, Landroid/widget/TextView;->setTextColor(I)V

    .line 23
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v7

    sget v9, Lcom/tw/preference/R$dimen;->tw_dp_h24:I

    invoke-virtual {v7, v9}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v7

    invoke-virtual {v3, v8, v7}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 24
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    const/16 v7, 0x8

    invoke-virtual {v3, v7}, Landroid/widget/TextView;->setVisibility(I)V

    .line 25
    new-instance v3, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v3, v1, v4}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 26
    iput v5, v3, Landroid/widget/LinearLayout$LayoutParams;->weight:F

    .line 27
    iget-object v5, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    invoke-virtual {v0, v5, v3}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 28
    new-instance v0, Landroid/widget/TextView;

    invoke-direct {v0, p1}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    .line 29
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    const/16 v3, 0x15

    invoke-virtual {v0, v3}, Landroid/widget/TextView;->setGravity(I)V

    .line 30
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    invoke-virtual {v0, v2}, Landroid/widget/TextView;->setMaxLines(I)V

    .line 31
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    invoke-static {v6}, Landroid/graphics/Color;->parseColor(Ljava/lang/String;)I

    move-result v3

    invoke-virtual {v0, v3}, Landroid/widget/TextView;->setTextColor(I)V

    .line 32
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    sget v5, Lcom/tw/preference/R$dimen;->tw_dp_h28:I

    invoke-virtual {v3, v5}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v3

    invoke-virtual {v0, v8, v3}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 33
    new-instance v0, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v0, v4, v1}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 34
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    sget v4, Lcom/tw/preference/R$dimen;->tw_dp_w32:I

    invoke-virtual {v3, v4}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v3

    float-to-int v3, v3

    .line 35
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v4

    sget v5, Lcom/tw/preference/R$dimen;->tw_dp_w32:I

    invoke-virtual {v4, v5}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v4

    float-to-int v4, v4

    .line 36
    invoke-virtual {v0, v3, v8, v4, v8}, Landroid/widget/LinearLayout$LayoutParams;->setMargins(IIII)V

    .line 37
    iget-object v3, p0, Lcom/tw/preference/SingleChoosePreference;->he:Landroid/widget/LinearLayout;

    iget-object v4, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    invoke-virtual {v3, v4, v0}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 38
    new-instance v0, Landroid/widget/ImageView;

    invoke-direct {v0, p1}, Landroid/widget/ImageView;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    .line 39
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    sget-object v3, Landroid/widget/ImageView$ScaleType;->CENTER_INSIDE:Landroid/widget/ImageView$ScaleType;

    invoke-virtual {v0, v3}, Landroid/widget/ImageView;->setScaleType(Landroid/widget/ImageView$ScaleType;)V

    .line 40
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v3, Lcom/tw/preference/R$dimen;->tw_dp_h75:I

    invoke-virtual {v0, v3}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    float-to-int v0, v0

    .line 41
    new-instance v3, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v3, v0, v0}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 42
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->he:Landroid/widget/LinearLayout;

    iget-object v4, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {v0, v4, v3}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 43
    invoke-virtual {p0, v2}, Landroid/widget/LinearLayout;->setClickable(Z)V

    .line 44
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    new-instance v2, Lcom/tw/preference/b;

    invoke-direct {v2, p0}, Lcom/tw/preference/b;-><init>(Lcom/tw/preference/SingleChoosePreference;)V

    invoke-virtual {v0, v2}, Landroid/widget/ImageView;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 45
    sget v0, Lcom/tw/preference/R$drawable;->preference_single_choose_item_bg:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->popupBackground:Landroid/graphics/drawable/Drawable;

    .line 46
    sget v0, Lcom/tw/preference/R$drawable;->lev_preference_single_choose_item_bg:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->pe:Landroid/graphics/drawable/Drawable;

    .line 47
    sget v0, Lcom/tw/preference/R$drawable;->preference_single_choose_item_nor:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->qe:Landroid/graphics/drawable/Drawable;

    .line 48
    sget v0, Lcom/tw/preference/R$drawable;->preference_single_choose_item_pres:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->re:Landroid/graphics/drawable/Drawable;

    .line 49
    sget v0, Lcom/tw/preference/R$drawable;->preference_item_bg:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 50
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextSize:I

    .line 51
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h24:I

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextSize:I

    .line 52
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h28:I

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextSize:I

    .line 53
    iput v1, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextColor:I

    .line 54
    invoke-static {v6}, Landroid/graphics/Color;->parseColor(Ljava/lang/String;)I

    move-result v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextColor:I

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextColor:I

    .line 55
    invoke-static {v6}, Landroid/graphics/Color;->parseColor(Ljava/lang/String;)I

    move-result v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextColor:I

    .line 56
    sget v0, Lcom/tw/preference/R$drawable;->lev_single_choose_expand:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 57
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_w260:I

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    .line 58
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h78:I

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    .line 59
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h24:I

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextSize:I

    .line 60
    iput v1, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextColor:I

    .line 61
    sget v0, Lcom/tw/preference/R$drawable;->preference_single_choose_item_bg:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->popupBackground:Landroid/graphics/drawable/Drawable;

    .line 62
    sget v0, Lcom/tw/preference/R$drawable;->lev_preference_single_choose_item_bg:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->pe:Landroid/graphics/drawable/Drawable;

    .line 63
    sget v0, Lcom/tw/preference/R$drawable;->preference_single_choose_item_nor:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->qe:Landroid/graphics/drawable/Drawable;

    .line 64
    sget v0, Lcom/tw/preference/R$drawable;->preference_single_choose_item_pres:I

    invoke-virtual {p1, v0}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object p1

    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->re:Landroid/graphics/drawable/Drawable;

    .line 65
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->paddingLeft:I

    .line 66
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->paddingRight:I

    .line 67
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_h75:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconWidth:I

    .line 68
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_h75:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconHeight:I

    return-void
.end method


# virtual methods
.method public findIndexOfValue(Ljava/lang/String;)I
    .locals 2

    if-eqz p1, :cond_1

    .line 1
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->me:[Ljava/lang/CharSequence;

    if-eqz v0, :cond_1

    .line 2
    array-length v0, v0

    add-int/lit8 v0, v0, -0x1

    :goto_0
    if-ltz v0, :cond_1

    .line 3
    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->me:[Ljava/lang/CharSequence;

    aget-object v1, v1, v0

    invoke-virtual {v1, p1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    return v0

    :cond_0
    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_1
    const/4 p0, -0x1

    return p0
.end method

.method public getEntries()[Ljava/lang/CharSequence;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    return-object p0
.end method

.method public getEntryValues()[Ljava/lang/CharSequence;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->me:[Ljava/lang/CharSequence;

    return-object p0
.end method

.method public getPaddingLeft()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->paddingLeft:I

    return p0
.end method

.method public getPaddingRight()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->paddingRight:I

    return p0
.end method

.method public getPopItemHeight()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    return p0
.end method

.method public getPopItemTextColor()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextColor:I

    return p0
.end method

.method public getPopItemTextSize()F
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextSize:I

    int-to-float p0, p0

    return p0
.end method

.method public getPopItemWidth()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    return p0
.end method

.method public getPopupBackground()Landroid/graphics/drawable/Drawable;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->popupBackground:Landroid/graphics/drawable/Drawable;

    return-object p0
.end method

.method public getPopupItemBackgroundNor()Landroid/graphics/drawable/Drawable;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->qe:Landroid/graphics/drawable/Drawable;

    return-object p0
.end method

.method public getPopupItemBackgroundPres()Landroid/graphics/drawable/Drawable;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->re:Landroid/graphics/drawable/Drawable;

    return-object p0
.end method

.method public getRightIcon()Landroid/graphics/drawable/Drawable;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->fe:Landroid/graphics/drawable/Drawable;

    return-object p0
.end method

.method public getRightIconHeight()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconHeight:I

    return p0
.end method

.method public getRightIconWidth()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconWidth:I

    return p0
.end method

.method public getSubTitle()Ljava/lang/String;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitle:Ljava/lang/String;

    return-object p0
.end method

.method public getSubTitleTextColor()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextColor:I

    return p0
.end method

.method public getSubTitleTextSize()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextSize:I

    return p0
.end method

.method public getSummary()Ljava/lang/String;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->summary:Ljava/lang/String;

    return-object p0
.end method

.method public getSummaryTextColor()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextColor:I

    return p0
.end method

.method public getSummaryTextSize()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextSize:I

    return p0
.end method

.method public getTitle()Ljava/lang/String;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->title:Ljava/lang/String;

    return-object p0
.end method

.method public getTitleTextColor()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextColor:I

    return p0
.end method

.method public getTitleTextSize()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextSize:I

    return p0
.end method

.method public getTvSubTitle()Landroid/widget/TextView;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    return-object p0
.end method

.method public getValue()Ljava/lang/String;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->mValue:Ljava/lang/String;

    return-object p0
.end method

.method public setBackground(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    invoke-super {p0, p1}, Landroid/widget/LinearLayout;->setBackground(Landroid/graphics/drawable/Drawable;)V

    .line 2
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    return-void
.end method

.method public setCurrentEntry(Ljava/lang/String;)V
    .locals 2

    .line 1
    invoke-virtual {p0, p1}, Lcom/tw/preference/SingleChoosePreference;->ua(Ljava/lang/String;)I

    move-result p1

    const/4 v0, -0x1

    if-eq p1, v0, :cond_1

    .line 2
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    array-length v1, v0

    add-int/lit8 v1, v1, -0x1

    if-le p1, v1, :cond_0

    goto :goto_0

    .line 3
    :cond_0
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->ne:I

    .line 4
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    aget-object p1, v0, p1

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    :cond_1
    :goto_0
    return-void
.end method

.method public setEntries([Ljava/lang/CharSequence;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    return-void
.end method

.method public setEntryValues([Ljava/lang/CharSequence;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->me:[Ljava/lang/CharSequence;

    return-void
.end method

.method public setItemBackground(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 2
    invoke-virtual {p0, p1}, Lcom/tw/preference/SingleChoosePreference;->setBackground(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method public setOnChooseValueChange(Lcom/tw/preference/SingleChoosePreference$a;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->se:Lcom/tw/preference/SingleChoosePreference$a;

    return-void
.end method

.method public setPaddingLeft(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->paddingLeft:I

    .line 2
    iget v0, p0, Lcom/tw/preference/SingleChoosePreference;->paddingRight:I

    const/4 v1, 0x0

    invoke-virtual {p0, p1, v1, v0, v1}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    return-void
.end method

.method public setPaddingRight(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->paddingRight:I

    .line 2
    iget v0, p0, Lcom/tw/preference/SingleChoosePreference;->paddingLeft:I

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1, p1, v1}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    return-void
.end method

.method public setPopItemHeight(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->popItemHeight:I

    return-void
.end method

.method public setPopItemTextColor(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextColor:I

    return-void
.end method

.method public setPopItemTextSize(F)V
    .locals 0

    float-to-int p1, p1

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->popItemTextSize:I

    return-void
.end method

.method public setPopItemWidth(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->popItemWidth:I

    return-void
.end method

.method public setPopupBackground(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->popupBackground:Landroid/graphics/drawable/Drawable;

    return-void
.end method

.method public setPopupItemBackgroundNor(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->qe:Landroid/graphics/drawable/Drawable;

    return-void
.end method

.method public setPopupItemBackgroundPres(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->re:Landroid/graphics/drawable/Drawable;

    return-void
.end method

.method public setRightIcon(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 2
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {p0, p1}, Landroid/widget/ImageView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method public setRightIconHeight(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconHeight:I

    .line 2
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {v0}, Landroid/widget/ImageView;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_0

    .line 3
    iget v1, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconWidth:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 4
    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 5
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {p0, v0}, Landroid/widget/ImageView;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    :cond_0
    return-void
.end method

.method public setRightIconWidth(I)V
    .locals 1

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconWidth:I

    .line 2
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {v0}, Landroid/widget/ImageView;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_0

    .line 3
    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 4
    iget p1, p0, Lcom/tw/preference/SingleChoosePreference;->rightIconHeight:I

    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 5
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->le:Landroid/widget/ImageView;

    invoke-virtual {p0, v0}, Landroid/widget/ImageView;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    :cond_0
    return-void
.end method

.method public setSubTitle(Ljava/lang/String;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->subTitle:Ljava/lang/String;

    .line 2
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    return-void
.end method

.method public setSubTitleTextColor(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextColor:I

    .line 2
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(I)V

    return-void
.end method

.method public setSubTitleTextColor(Landroid/content/res/ColorStateList;)V
    .locals 0

    .line 3
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(Landroid/content/res/ColorStateList;)V

    return-void
.end method

.method public setSubTitleTextSize(I)V
    .locals 1

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextSize:I

    .line 2
    iget-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    iget p0, p0, Lcom/tw/preference/SingleChoosePreference;->subTitleTextColor:I

    int-to-float p0, p0

    const/4 v0, 0x0

    invoke-virtual {p1, v0, p0}, Landroid/widget/TextView;->setTextSize(IF)V

    return-void
.end method

.method public setSummary(Ljava/lang/String;)V
    .locals 1

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->summary:Ljava/lang/String;

    .line 2
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_0

    .line 3
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    :cond_0
    return-void
.end method

.method public setSummaryTextColor(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextColor:I

    .line 2
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(I)V

    return-void
.end method

.method public setSummaryTextColor(Landroid/content/res/ColorStateList;)V
    .locals 0

    .line 3
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(Landroid/content/res/ColorStateList;)V

    return-void
.end method

.method public setSummaryTextSize(I)V
    .locals 1

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->summaryTextSize:I

    .line 2
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    int-to-float p1, p1

    const/4 v0, 0x0

    invoke-virtual {p0, v0, p1}, Landroid/widget/TextView;->setTextSize(IF)V

    return-void
.end method

.method public setTitle(Ljava/lang/String;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->title:Ljava/lang/String;

    .line 2
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    return-void
.end method

.method public setTitleTextColor(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextColor:I

    .line 2
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(I)V

    return-void
.end method

.method public setTitleTextColor(Landroid/content/res/ColorStateList;)V
    .locals 0

    .line 3
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(Landroid/content/res/ColorStateList;)V

    return-void
.end method

.method public setTitleTextSize(I)V
    .locals 1

    .line 1
    iput p1, p0, Lcom/tw/preference/SingleChoosePreference;->titleTextSize:I

    .line 2
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->de:Landroid/widget/TextView;

    int-to-float p1, p1

    const/4 v0, 0x0

    invoke-virtual {p0, v0, p1}, Landroid/widget/TextView;->setTextSize(IF)V

    return-void
.end method

.method public setTvSubTitle(Ljava/lang/String;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->subTitle:Ljava/lang/String;

    .line 2
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result p1

    if-eqz p1, :cond_0

    .line 3
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    const/16 p1, 0x8

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setVisibility(I)V

    goto :goto_0

    .line 4
    :cond_0
    iget-object p0, p0, Lcom/tw/preference/SingleChoosePreference;->ie:Landroid/widget/TextView;

    const/4 p1, 0x0

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setVisibility(I)V

    :goto_0
    return-void
.end method

.method public setValue(Ljava/lang/String;)V
    .locals 2

    .line 1
    invoke-virtual {p0, p1}, Lcom/tw/preference/SingleChoosePreference;->findIndexOfValue(Ljava/lang/String;)I

    move-result v0

    const/4 v1, -0x1

    if-ne v0, v1, :cond_0

    return-void

    .line 2
    :cond_0
    iput v0, p0, Lcom/tw/preference/SingleChoosePreference;->ne:I

    .line 3
    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->je:Landroid/widget/TextView;

    invoke-direct {p0, v0}, Lcom/tw/preference/SingleChoosePreference;->Ha(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 4
    iput-object p1, p0, Lcom/tw/preference/SingleChoosePreference;->mValue:Ljava/lang/String;

    return-void
.end method

.method public ua(Ljava/lang/String;)I
    .locals 2

    if-eqz p1, :cond_1

    .line 1
    iget-object v0, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    if-eqz v0, :cond_1

    .line 2
    array-length v0, v0

    add-int/lit8 v0, v0, -0x1

    :goto_0
    if-ltz v0, :cond_1

    .line 3
    iget-object v1, p0, Lcom/tw/preference/SingleChoosePreference;->mEntries:[Ljava/lang/CharSequence;

    aget-object v1, v1, v0

    invoke-virtual {v1, p1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    return v0

    :cond_0
    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_1
    const/4 p0, -0x1

    return p0
.end method
