.class public Lcom/tw/preference/ButtonPreference;
.super Landroid/widget/LinearLayout;
.source "ButtonPreference.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/tw/preference/ButtonPreference$a;
    }
.end annotation


# instance fields
.field private context:Landroid/content/Context;

.field private de:Landroid/widget/TextView;

.field private ee:Landroid/widget/Button;

.field private fe:Landroid/graphics/drawable/Drawable;

.field private ge:Lcom/tw/preference/ButtonPreference$a;

.field private itemBackground:Landroid/graphics/drawable/Drawable;

.field private paddingLeft:I

.field private paddingRight:I

.field private rightIconHeight:I

.field private rightIconWidth:I

.field private summaryTextColor:I

.field private title:Ljava/lang/String;

.field private titleTextColor:I

.field private titleTextSize:F


# direct methods
.method public constructor <init>(Landroid/content/Context;)V
    .locals 0

    .line 1
    invoke-direct {p0, p1}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    .line 2
    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->context:Landroid/content/Context;

    .line 3
    invoke-direct {p0, p1}, Lcom/tw/preference/ButtonPreference;->initView(Landroid/content/Context;)V

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
    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->context:Landroid/content/Context;

    .line 6
    invoke-direct {p0, p1}, Lcom/tw/preference/ButtonPreference;->initView(Landroid/content/Context;)V

    .line 7
    invoke-direct {p0, p2}, Lcom/tw/preference/ButtonPreference;->a(Landroid/util/AttributeSet;)V

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
    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->context:Landroid/content/Context;

    .line 10
    invoke-direct {p0, p1}, Lcom/tw/preference/ButtonPreference;->initView(Landroid/content/Context;)V

    .line 11
    invoke-direct {p0, p2}, Lcom/tw/preference/ButtonPreference;->a(Landroid/util/AttributeSet;)V

    return-void
.end method

.method private Ce()V
    .locals 3

    .line 1
    iget v0, p0, Lcom/tw/preference/ButtonPreference;->paddingLeft:I

    iget v1, p0, Lcom/tw/preference/ButtonPreference;->paddingRight:I

    const/4 v2, 0x0

    invoke-virtual {p0, v0, v2, v1, v2}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    .line 2
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    iget-object v1, p0, Lcom/tw/preference/ButtonPreference;->title:Ljava/lang/String;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 3
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    iget v1, p0, Lcom/tw/preference/ButtonPreference;->titleTextColor:I

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setTextColor(I)V

    .line 4
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {v0}, Landroid/widget/Button;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_0

    .line 5
    iget v1, p0, Lcom/tw/preference/ButtonPreference;->rightIconWidth:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 6
    iget v1, p0, Lcom/tw/preference/ButtonPreference;->rightIconHeight:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 7
    iget-object v1, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {v1, v0}, Landroid/widget/Button;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    .line 8
    :cond_0
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    if-nez v0, :cond_1

    .line 9
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->context:Landroid/content/Context;

    sget v1, Lcom/tw/preference/R$drawable;->preference_item_bg:I

    invoke-virtual {v0, v1}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/ButtonPreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 10
    :cond_1
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    invoke-virtual {p0, v0}, Landroid/widget/LinearLayout;->setBackground(Landroid/graphics/drawable/Drawable;)V

    .line 11
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->fe:Landroid/graphics/drawable/Drawable;

    if-eqz v0, :cond_2

    .line 12
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {p0, v0}, Landroid/widget/Button;->setBackground(Landroid/graphics/drawable/Drawable;)V

    :cond_2
    return-void
.end method

.method private a(Landroid/util/AttributeSet;)V
    .locals 3

    .line 1
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->context:Landroid/content/Context;

    sget-object v1, Lcom/tw/preference/R$styleable;->ButtonPreference:[I

    invoke-virtual {v0, p1, v1}, Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;

    move-result-object p1

    .line 2
    sget v0, Lcom/tw/preference/R$styleable;->TogglePreference_android_background:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/ButtonPreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 3
    sget v0, Lcom/tw/preference/R$styleable;->ButtonPreference_android_text:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getString(I)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/ButtonPreference;->title:Ljava/lang/String;

    .line 4
    sget v0, Lcom/tw/preference/R$styleable;->ButtonPreference_android_textSize:I

    .line 5
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 6
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    iput v0, p0, Lcom/tw/preference/ButtonPreference;->titleTextSize:F

    .line 7
    sget v0, Lcom/tw/preference/R$styleable;->ButtonPreference_android_textColor:I

    const v1, 0xffffff

    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getColor(II)I

    move-result v0

    iput v0, p0, Lcom/tw/preference/ButtonPreference;->titleTextColor:I

    .line 8
    sget v0, Lcom/tw/preference/R$styleable;->ButtonPreference_rightIconBackground:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iput-object v0, p0, Lcom/tw/preference/ButtonPreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 9
    sget v0, Lcom/tw/preference/R$styleable;->ButtonPreference_paddingLeft:I

    .line 10
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 11
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/ButtonPreference;->paddingLeft:I

    .line 12
    sget v0, Lcom/tw/preference/R$styleable;->ButtonPreference_paddingRight:I

    .line 13
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 14
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/ButtonPreference;->paddingRight:I

    .line 15
    sget v0, Lcom/tw/preference/R$styleable;->ButtonPreference_rightIconWidth:I

    .line 16
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h150:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 17
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/ButtonPreference;->rightIconWidth:I

    .line 18
    sget v0, Lcom/tw/preference/R$styleable;->ButtonPreference_rightIconHeight:I

    .line 19
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h75:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    .line 20
    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v0

    float-to-int v0, v0

    iput v0, p0, Lcom/tw/preference/ButtonPreference;->rightIconHeight:I

    .line 21
    invoke-virtual {p1}, Landroid/content/res/TypedArray;->recycle()V

    .line 22
    invoke-direct {p0}, Lcom/tw/preference/ButtonPreference;->Ce()V

    return-void
.end method

.method static synthetic b(Lcom/tw/preference/ButtonPreference;)Lcom/tw/preference/ButtonPreference$a;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->ge:Lcom/tw/preference/ButtonPreference$a;

    return-object p0
.end method

.method private initView(Landroid/content/Context;)V
    .locals 4

    const/4 v0, 0x0

    .line 1
    invoke-virtual {p0, v0}, Landroid/widget/LinearLayout;->setOrientation(I)V

    const/16 v1, 0x10

    .line 2
    invoke-virtual {p0, v1}, Landroid/widget/LinearLayout;->setGravity(I)V

    .line 3
    sget v2, Lcom/tw/preference/R$drawable;->preference_item_bg:I

    invoke-virtual {p0, v2}, Landroid/widget/LinearLayout;->setBackgroundResource(I)V

    .line 4
    new-instance v2, Landroid/widget/TextView;

    invoke-direct {v2, p1}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    iput-object v2, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    .line 5
    iget-object v2, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    invoke-virtual {v2}, Landroid/widget/TextView;->setSingleLine()V

    .line 6
    iget-object v2, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    invoke-virtual {v2, v1}, Landroid/widget/TextView;->setGravity(I)V

    .line 7
    iget-object v1, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    sget v3, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {v2, v3}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v2

    invoke-virtual {v1, v0, v2}, Landroid/widget/TextView;->setTextSize(IF)V

    .line 8
    new-instance v1, Landroid/widget/LinearLayout$LayoutParams;

    const/4 v2, -0x2

    invoke-direct {v1, v0, v2}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    const/high16 v0, 0x3f800000    # 1.0f

    .line 9
    iput v0, v1, Landroid/widget/LinearLayout$LayoutParams;->weight:F

    .line 10
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, v0, v1}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 11
    new-instance v0, Landroid/widget/Button;

    invoke-direct {v0, p1}, Landroid/widget/Button;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    .line 12
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    sget v1, Lcom/tw/preference/R$drawable;->btn_restore:I

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setBackgroundResource(I)V

    .line 13
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    new-instance v1, Lcom/tw/preference/a;

    invoke-direct {v1, p0}, Lcom/tw/preference/a;-><init>(Lcom/tw/preference/ButtonPreference;)V

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 14
    new-instance v0, Landroid/widget/LinearLayout$LayoutParams;

    .line 15
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h150:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v1

    float-to-int v1, v1

    .line 16
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v2, Lcom/tw/preference/R$dimen;->tw_dp_h75:I

    invoke-virtual {p1, v2}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    invoke-direct {v0, v1, p1}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 17
    iget-object p1, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {p0, p1, v0}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 18
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$drawable;->preference_item_bg:I

    const/4 v1, 0x0

    invoke-virtual {p1, v0, v1}, Landroid/content/res/Resources;->getDrawable(ILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;

    move-result-object p1

    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 19
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_h32:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    iput p1, p0, Lcom/tw/preference/ButtonPreference;->titleTextSize:F

    const/4 p1, -0x1

    .line 20
    iput p1, p0, Lcom/tw/preference/ButtonPreference;->titleTextColor:I

    .line 21
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$drawable;->btn_restore:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object p1

    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 22
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/ButtonPreference;->paddingLeft:I

    .line 23
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_w64:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/ButtonPreference;->paddingRight:I

    .line 24
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_h150:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/ButtonPreference;->rightIconWidth:I

    .line 25
    invoke-virtual {p0}, Landroid/widget/LinearLayout;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    sget v0, Lcom/tw/preference/R$dimen;->tw_dp_h75:I

    invoke-virtual {p1, v0}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    float-to-int p1, p1

    iput p1, p0, Lcom/tw/preference/ButtonPreference;->rightIconHeight:I

    return-void
.end method


# virtual methods
.method public getItemBackground()Landroid/graphics/drawable/Drawable;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    return-object p0
.end method

.method public getPaddingLeft()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/ButtonPreference;->paddingLeft:I

    return p0
.end method

.method public getPaddingRight()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/ButtonPreference;->paddingRight:I

    return p0
.end method

.method public getRightIcon()Landroid/graphics/drawable/Drawable;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->fe:Landroid/graphics/drawable/Drawable;

    return-object p0
.end method

.method public getRightIconHeight()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/ButtonPreference;->rightIconHeight:I

    return p0
.end method

.method public getRightIconWidth()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/ButtonPreference;->rightIconWidth:I

    return p0
.end method

.method public getSummaryTextColor()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/ButtonPreference;->summaryTextColor:I

    return p0
.end method

.method public getTitle()Ljava/lang/String;
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->title:Ljava/lang/String;

    return-object p0
.end method

.method public getTitleTextColor()I
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/ButtonPreference;->titleTextColor:I

    return p0
.end method

.method public getTitleTextSize()F
    .locals 0

    .line 1
    iget p0, p0, Lcom/tw/preference/ButtonPreference;->titleTextSize:F

    return p0
.end method

.method public setItemBackground(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->itemBackground:Landroid/graphics/drawable/Drawable;

    .line 2
    invoke-virtual {p0, p1}, Landroid/widget/LinearLayout;->setBackground(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method public setOnButtonClickListener(Lcom/tw/preference/ButtonPreference$a;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->ge:Lcom/tw/preference/ButtonPreference$a;

    return-void
.end method

.method public setPaddingLeft(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/ButtonPreference;->paddingLeft:I

    .line 2
    iget v0, p0, Lcom/tw/preference/ButtonPreference;->paddingRight:I

    const/4 v1, 0x0

    invoke-virtual {p0, p1, v1, v0, v1}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    return-void
.end method

.method public setPaddingRight(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/ButtonPreference;->paddingRight:I

    .line 2
    iget v0, p0, Lcom/tw/preference/ButtonPreference;->paddingLeft:I

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1, p1, v1}, Landroid/widget/LinearLayout;->setPadding(IIII)V

    return-void
.end method

.method public setRightIcon(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->fe:Landroid/graphics/drawable/Drawable;

    .line 2
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {p0, p1}, Landroid/widget/Button;->setBackground(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method public setRightIconHeight(I)V
    .locals 2

    .line 1
    iput p1, p0, Lcom/tw/preference/ButtonPreference;->rightIconHeight:I

    .line 2
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {v0}, Landroid/widget/Button;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_0

    .line 3
    iget v1, p0, Lcom/tw/preference/ButtonPreference;->rightIconWidth:I

    iput v1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 4
    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 5
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {p0, v0}, Landroid/widget/Button;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    :cond_0
    return-void
.end method

.method public setRightIconWidth(I)V
    .locals 1

    .line 1
    iput p1, p0, Lcom/tw/preference/ButtonPreference;->rightIconWidth:I

    .line 2
    iget-object v0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {v0}, Landroid/widget/Button;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/widget/LinearLayout$LayoutParams;

    if-eqz v0, :cond_0

    .line 3
    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->width:I

    .line 4
    iget p1, p0, Lcom/tw/preference/ButtonPreference;->rightIconHeight:I

    iput p1, v0, Landroid/widget/LinearLayout$LayoutParams;->height:I

    .line 5
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->ee:Landroid/widget/Button;

    invoke-virtual {p0, v0}, Landroid/widget/Button;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    :cond_0
    return-void
.end method

.method public setSummaryTextColor(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/ButtonPreference;->summaryTextColor:I

    return-void
.end method

.method public setTitle(Ljava/lang/String;)V
    .locals 1

    .line 1
    iput-object p1, p0, Lcom/tw/preference/ButtonPreference;->title:Ljava/lang/String;

    .line 2
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_0

    .line 3
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    :cond_0
    return-void
.end method

.method public setTitleTextColor(I)V
    .locals 0

    .line 1
    iput p1, p0, Lcom/tw/preference/ButtonPreference;->titleTextColor:I

    .line 2
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(I)V

    return-void
.end method

.method public setTitleTextColor(Landroid/content/res/ColorStateList;)V
    .locals 0

    .line 3
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    invoke-virtual {p0, p1}, Landroid/widget/TextView;->setTextColor(Landroid/content/res/ColorStateList;)V

    return-void
.end method

.method public setTitleTextSize(I)V
    .locals 1

    .line 1
    iget-object p0, p0, Lcom/tw/preference/ButtonPreference;->de:Landroid/widget/TextView;

    int-to-float p1, p1

    const/4 v0, 0x0

    invoke-virtual {p0, v0, p1}, Landroid/widget/TextView;->setTextSize(IF)V

    return-void
.end method
