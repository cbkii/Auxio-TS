.class public Lcom/tw/preference/TogglePreference;
.super Landroid/widget/LinearLayout;
.source "TogglePreference.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/tw/preference/TogglePreference$a;
    }
.end annotation


# instance fields
.field private Cc:Lcom/tw/preference/TogglePreference$a;

.field private context:Landroid/content/Context;

.field private de:Landroid/widget/TextView;

.field private fe:Landroid/graphics/drawable/Drawable;

.field private he:Landroid/widget/LinearLayout;

.field private itemBackground:Landroid/graphics/drawable/Drawable;

.field private paddingLeft:I

.field private paddingRight:I

.field private rightIconHeight:I

.field private rightIconWidth:I

.field private title:Ljava/lang/String;

.field private titleTextColor:I

.field private titleTextSize:F

.field private ue:Landroid/widget/ToggleButton;


# direct methods
.method public constructor <init>(Landroid/content/Context;)V
    .locals 0

    .line 1
    invoke-direct {p0, p1}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    .line 2
    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->context:Landroid/content/Context;

    .line 3
    invoke-direct {p0, p1}, Lcom/tw/preference/TogglePreference;->initView(Landroid/content/Context;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;)V
    .locals 0
    .param p2    # Landroid/util/AttributeSet;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param

    .line 4
    invoke-direct {p0, p1, p2}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V

    .line 5
    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->context:Landroid/content/Context;

    .line 6
    invoke-direct {p0, p1}, Lcom/tw/preference/TogglePreference;->initView(Landroid/content/Context;)V

    .line 7
    invoke-direct {p0, p2}, Lcom/tw/preference/TogglePreference;->a(Landroid/util/AttributeSet;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V
    .locals 0
    .param p2    # Landroid/util/AttributeSet;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param

    .line 8
    invoke-direct {p0, p1, p2, p3}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V

    .line 9
    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->context:Landroid/content/Context;

    .line 10
    invoke-direct {p0, p1}, Lcom/tw/preference/TogglePreference;->initView(Landroid/content/Context;)V

    .line 11
    invoke-direct {p0, p2}, Lcom/tw/preference/TogglePreference;->a(Landroid/util/AttributeSet;)V

    return-void
.end method

.method private Ce()V
    .locals 3

    .line 1
    iget v0, p0, Lcom/tw/preference/TogglePreference;->paddingLeft:I

    iget v1, p0, Lcom/tw/preference/TogglePreference;->paddingRight:I

    const/4 v2, 0x0

    invoke-virtual {p0, v0, v2, v1, v2}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    .line 2
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    iget-object v1, p0, Lcom/tw/preference/TogglePreference;->title:Ljava/lang/String;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 3
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/TogglePreference;->titleTextColor:I

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setTextColor(I)V

    .line 4
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/TogglePreference;->titleTextSize:F

    invoke-virtual {v0, v2, v1}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 5
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {v0}, Landroid/widget/ToggleButton;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_0

    .line 6
    iget v1, p0, Lcom/tw/preference/TogglePreference;->rightIconWidth:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 7
    iget v1, p0, Lcom/tw/preference/TogglePreference;->rightIconHeight:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 8
    iget-object v1, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {v1, v0}, Landroid/widget/ToggleButton;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    .line 9
    :cond_0
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->fe:Landroid/graphics/drawable/Drawable;

    if-eqz v0, :cond_1

    .line 10
    iget-object v1, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {v1, v0}, Landroid/widget/ToggleButton;->setBackground(Landroid/graphics/drawable/Drawable;)V

    .line 11
    :cond_1
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    if-nez v0, :cond_2

    .line 12
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->context:Landroid/content/Context;

    sget v1, Lcom/tw/preference/R$drawable;->preference_item_bg:I

    invoke-virtual {v0, v1}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/TogglePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 13
    :cond_2
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    invoke-virtual {p0, v0}, Lcom/tw/preference/TogglePreference;->setBackground(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method static synthetic a(Lcom/tw/preference/TogglePreference;)Lcom/tw/preference/TogglePreference$a;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->Cc:Lcom/tw/preference/TogglePreference$a;

    return-object p0
.end method

.method private a(Landroid/util/AttributeSet;)V
    .locals 3

    .line 2
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->context:Landroid/content/Context;

    sget-object v1, Lcom/tw/preference/R$styleable;->TogglePreference:[I

    invoke-virtual {v0, p1, v1}, Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;

    move-result-object p1

    .line 3
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_android_background:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/TogglePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 4
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_android_text:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getString(I)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/TogglePreference;->title:Ljava/lang/String;

    .line 5
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_android_textSize:I

    .line 6
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 7
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    iput v0, p0, Lcom/tw/preference/TogglePreference;->titleTextSize:F

    .line 8
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_android_textColor:I

    const v1, 0xffffff

    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getColor(II)I

    move-result v0

    iput v0, p0, Lcom/tw/preference/TogglePreference;->titleTextColor:I

    .line 9
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_rightIconBackground:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/TogglePreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 10
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_paddingLeft:I

    .line 11
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 12
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/TogglePreference;->paddingLeft:I

    .line 13
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_paddingRight:I

    .line 14
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 15
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/TogglePreference;->paddingRight:I

    .line 16
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_rightIconWidth:I

    .line 17
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h120:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 18
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/TogglePreference;->rightIconWidth:I

    .line 19
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_rightIconHeight:I

    .line 20
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h70:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 21
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/TogglePreference;->rightIconHeight:I

    .line 22
    invoke-virtual {p1}, Landroid/content/res/TypedArray;->recycle()V

    .line 23
    invoke-direct {p0}, Lcom/tw/preference/TogglePreference;->Ce()V

    return-void
.end method

.method private initView(Landroid/content/Context;)V
    .locals 7

    .line 1
    new-instance v0, Landroid/widget/LinearLayout;

    invoke-direct {v0, p1}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/tw/preference/TogglePreference;->he:Landroid/widget/LinearLayout;

    .line 2
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->he:Landroid/widget/LinearLayout;

    const/16 v1, 0x10

    invoke-virtual {v0, v1}, Landroid/widget/LinearLayout;->setGravity(I)V

    .line 3
    new-instance v0, Landroid/widget/LinearLayout$LayoutParams;

    const/4 v1, -0x1

    invoke-direct {v0, v1, v1}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 4
    iget-object v2, p0, Lcom/tw/preference/TogglePreference;->he:Landroid/widget/LinearLayout;

    invoke-virtual {p0, v2, v0}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 5
    new-instance v0, Landroid/widget/LinearLayout;

    invoke-direct {v0, p1}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    const/4 v2, 0x1

    .line 6
    invoke-virtual {v0, v2}, Landroid/widget/LinearLayout;->setOrientation(I)V

    .line 7
    new-instance v3, Landroid/widget/LinearLayout$LayoutParams;

    const/4 v4, -0x2

    invoke-direct {v3, v4, v1}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    const/high16 v5, 0x3f800000    # 1.0f

    .line 8
    iput v5, v3, Landroid/widget/LinearLayout$LayoutParams;->weight:F

    .line 9
    iget-object v6, p0, Lcom/tw/preference/TogglePreference;->he:Landroid/widget/LinearLayout;

    invoke-virtual {v6, v0, v3}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 10
    new-instance v3, Landroid/widget/TextView;

    invoke-direct {v3, p1}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    iput-object v3, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    .line 11
    iget-object v3, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    const/16 v6, 0x13

    invoke-virtual {v3, v6}, Landroid/widget/TextView;->setGravity(I)V

    .line 12
    iget-object v3, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    invoke-virtual {v3, v2}, Landroid/widget/TextView;->setMaxLines(I)V

    .line 13
    iget-object v2, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    invoke-virtual {v2, v1}, Landroid/widget/TextView;->setTextColor(I)V

    .line 14
    iget-object v2, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    sget v6, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {v3, v6}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v3

    const/4 v6, 0x0

    invoke-virtual {v2, v6, v3}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 15
    new-instance v2, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v2, v1, v4}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 16
    iput v5, v2, Landroid/widget/LinearLayout$LayoutParams;->weight:F

    .line 17
    iget-object v3, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    invoke-virtual {v0, v3, v2}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 18
    new-instance v0, Landroid/widget/ToggleButton;

    invoke-direct {v0, p1}, Landroid/widget/ToggleButton;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    .line 19
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    const-string v2, ""

    invoke-virtual {v0, v2}, Landroid/widget/ToggleButton;->setTextOff(Ljava/lang/CharSequence;)V

    .line 20
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {v0, v2}, Landroid/widget/ToggleButton;->setTextOn(Ljava/lang/CharSequence;)V

    .line 21
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {v0, v2}, Landroid/widget/ToggleButton;->setText(Ljava/lang/CharSequence;)V

    .line 22
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    const/4 v2, 0x0

    invoke-virtual {v0, v2}, Landroid/widget/ToggleButton;->setButtonDrawable(Landroid/graphics/drawable/Drawable;)V

    .line 23
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    sget v3, Lcom/tw/preference/R$dimen;->tw_dp_h120:I

    invoke-virtual {v0, v3}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    float-to-int v0, v0

    .line 24
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v3, Lcom/tw/preference/R$dimen;->tw_dp_h70:I

    invoke-virtual {p1, v3}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    .line 25
    new-instance v3, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v3, v0, p1}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 26
    iget-object p1, p0, Lcom/tw/preference/TogglePreference;->he:Landroid/widget/LinearLayout;

    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {p1, v0, v3}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 27
    iget-object p1, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    new-instance v0, Lcom/tw/preference/e;

    invoke-direct {v0, p0}, Lcom/tw/preference/e;-><init>(Lcom/tw/preference/TogglePreference;)V

    invoke-virtual {p1, v0}, Landroid/widget/ToggleButton;->setOnCheckedChangeListener(Landroid/widget/CompoundButton$OnCheckedChangeListener;)V

    .line 28
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$drawable;->preference_item_bg:I

    invoke-virtual {p1, v0, v2}, Landroid/content/res/Resources;->getDrawable(ILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;

    move-result-object p1

    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 29
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    iput p1, p0, Lcom/tw/preference/TogglePreference;->titleTextSize:F

    .line 30
    iput v1, p0, Lcom/tw/preference/TogglePreference;->titleTextColor:I

    .line 31
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$drawable;->select_toggle_thumb:I

    invoke-virtual {p1, v0, v2}, Landroid/content/res/Resources;->getDrawable(ILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;

    move-result-object p1

    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 32
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/TogglePreference;->paddingLeft:I

    .line 33
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/TogglePreference;->paddingRight:I

    .line 34
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_h120:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/TogglePreference;->rightIconWidth:I

    .line 35
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_h70:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/TogglePreference;->rightIconHeight:I

    return-void
.end method


# virtual methods
.method public getItemBackground()Landroid/graphics/drawable/Drawable;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    return-object p0
.end method

.method public getPaddingLeft()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/TogglePreference;->paddingLeft:I

    return p0
.end method

.method public getPaddingRight()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/TogglePreference;->paddingRight:I

    return p0
.end method

.method public getRightIcon()Landroid/graphics/drawable/Drawable;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->fe:Landroid/graphics/drawable/Drawable;

    return-object p0
.end method

.method public getRightIconHeight()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/TogglePreference;->rightIconHeight:I

    return p0
.end method

.method public getRightIconWidth()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/TogglePreference;->rightIconWidth:I

    return p0
.end method

.method public getTitle()Ljava/lang/String;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->title:Ljava/lang/String;

    return-object p0
.end method

.method public getTitleTextColor()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/TogglePreference;->titleTextColor:I

    return p0
.end method

.method public getTitleTextSize()F
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/TogglePreference;->titleTextSize:F

    return p0
.end method

.method public setBackground(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    invoke-super {p0, p1}, Landroid/widget/LinearLayout;->setBackground(Landroid/graphics/drawable/Drawable;)V

    .line 2
    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    return-void
.end method

.method public setItemBackground(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 2
    invoke-virtual {p0, p1}, Lcom/tw/preference/TogglePreference;->setBackground(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method public setOnToggleStateChange(Lcom/tw/preference/TogglePreference$a;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->Cc:Lcom/tw/preference/TogglePreference$a;

    return-void
.end method

.method public setPaddingLeft(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/TogglePreference;->paddingLeft:I

    .line 2
    iget v0, p0, Lcom/tw/preference/TogglePreference;->paddingRight:I

    const/4 v1, 0x0

    invoke-virtual {p0, p1, v1, v0, v1}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    return-void
.end method

.method public setPaddingRight(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/TogglePreference;->paddingRight:I

    .line 2
    iget v0, p0, Lcom/tw/preference/TogglePreference;->paddingLeft:I

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1, p1, v1}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    return-void
.end method

.method public setRightIcon(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 2
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {p0, p1}, Landroid/widget/ToggleButton;->setBackgroundDrawable(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method public setRightIconHeight(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/TogglePreference;->rightIconHeight:I

    .line 2
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {v0}, Landroid/widget/ToggleButton;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_0

    .line 3
    iget v1, p0, Lcom/tw/preference/TogglePreference;->rightIconWidth:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 4
    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 5
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {p0, v0}, Landroid/widget/ToggleButton;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    :cond_0
    return-void
.end method

.method public setRightIconWidth(I)V
    .locals 1

    .line 1
    iput p1, p0, Lcom/tw/preference/TogglePreference;->rightIconWidth:I

    .line 2
    iget-object v0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {v0}, Landroid/widget/ToggleButton;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_0

    .line 3
    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 4
    iget p1, p0, Lcom/tw/preference/TogglePreference;->rightIconHeight:I

    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 5
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {p0, v0}, Landroid/widget/ToggleButton;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    :cond_0
    return-void
.end method

.method public setTitle(Ljava/lang/String;)V
    .locals 1

    .line 1
    iput-object p1, p0, Lcom/tw/preference/TogglePreference;->title:Ljava/lang/String;

    .line 2
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_0

    .line 3
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    :cond_0
    return-void
.end method

.method public setTitleTextColor(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/TogglePreference;->titleTextColor:I

    .line 2
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(I)V

    return-void
.end method

.method public setTitleTextColor(Landroid/content/res/ColorStateList;)V
    .locals 0

    .line 3
    iget-object p1, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    iget p0, p0, Lcom/tw/preference/TogglePreference;->titleTextColor:I

    invoke-virtual {p1, p0}, Landroid/widget/TextView;->setTextColor(I)V

    return-void
.end method

.method public setTitleTextSize(F)V
    .locals 1

    .line 1
    iput p1, p0, Lcom/tw/preference/TogglePreference;->titleTextSize:F

    .line 2
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->de:Landroid/widget/TextView;

    const/4 v0, 0x0

    invoke-virtual {p0, v0, p1}, Landroid/widget/TextView;->setTextSize(IF)V

    return-void
.end method

.method public setToggleState(Z)V
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/TogglePreference;->ue:Landroid/widget/ToggleButton;

    invoke-virtual {p0, p1}, Landroid/widget/ToggleButton;->setChecked(Z)V

    return-void
.end method
