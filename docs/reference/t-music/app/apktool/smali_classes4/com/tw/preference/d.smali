.class Lcom/tw/preference/d;
.super Ljava/lang/Object;
.source "SingleChoosePreference.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/tw/preference/SingleChoosePreference;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/tw/preference/SingleChoosePreference;


# direct methods
.method constructor <init>(Lcom/tw/preference/SingleChoosePreference;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 5

    const/4 v0, 0x0

    move v1, v0

    .line 1
    :goto_0
    :try_start_0
    iget-object v2, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {v2}, Lcom/tw/preference/SingleChoosePreference;->c(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/PopupWindow;

    move-result-object v2

    invoke-virtual {v2}, Landroid/widget/PopupWindow;->getContentView()Landroid/view/View;

    move-result-object v2

    check-cast v2, Landroid/view/ViewGroup;

    invoke-virtual {v2}, Landroid/view/ViewGroup;->getChildCount()I

    move-result v2

    if-ge v1, v2, :cond_2

    .line 2
    iget-object v2, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {v2}, Lcom/tw/preference/SingleChoosePreference;->c(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/PopupWindow;

    move-result-object v2

    invoke-virtual {v2}, Landroid/widget/PopupWindow;->getContentView()Landroid/view/View;

    move-result-object v2

    check-cast v2, Landroid/view/ViewGroup;

    invoke-virtual {v2, v1}, Landroid/view/ViewGroup;->getChildAt(I)Landroid/view/View;

    move-result-object v2

    check-cast v2, Landroid/widget/TextView;

    .line 3
    invoke-virtual {p1}, Landroid/view/View;->getTag()Ljava/lang/Object;

    move-result-object v3

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    if-ne v3, v4, :cond_0

    .line 4
    invoke-virtual {v2}, Landroid/widget/TextView;->getBackground()Landroid/graphics/drawable/Drawable;

    move-result-object v3

    const/4 v4, 0x1

    invoke-virtual {v3, v4}, Landroid/graphics/drawable/Drawable;->setLevel(I)Z

    .line 5
    iget-object v3, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {v3}, Lcom/tw/preference/SingleChoosePreference;->d(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/TextView;

    move-result-object v3

    invoke-virtual {v2}, Landroid/widget/TextView;->getText()Ljava/lang/CharSequence;

    move-result-object v2

    invoke-virtual {v3, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 6
    iget-object v2, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {v2, v1}, Lcom/tw/preference/SingleChoosePreference;->a(Lcom/tw/preference/SingleChoosePreference;I)I

    .line 7
    iget-object v2, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    iget-object v3, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {v3}, Lcom/tw/preference/SingleChoosePreference;->e(Lcom/tw/preference/SingleChoosePreference;)[Ljava/lang/CharSequence;

    move-result-object v3

    aget-object v3, v3, v1

    invoke-interface {v3}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v2, v3}, Lcom/tw/preference/SingleChoosePreference;->a(Lcom/tw/preference/SingleChoosePreference;Ljava/lang/String;)Ljava/lang/String;

    .line 8
    iget-object v2, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {v2}, Lcom/tw/preference/SingleChoosePreference;->f(Lcom/tw/preference/SingleChoosePreference;)Lcom/tw/preference/SingleChoosePreference$a;

    move-result-object v2

    if-eqz v2, :cond_1

    .line 9
    iget-object v2, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {v2}, Lcom/tw/preference/SingleChoosePreference;->f(Lcom/tw/preference/SingleChoosePreference;)Lcom/tw/preference/SingleChoosePreference$a;

    move-result-object v2

    iget-object v3, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    iget-object v4, p0, Lcom/tw/preference/d;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {v4}, Lcom/tw/preference/SingleChoosePreference;->e(Lcom/tw/preference/SingleChoosePreference;)[Ljava/lang/CharSequence;

    move-result-object v4

    aget-object v4, v4, v1

    invoke-interface {v2, v3, v4}, Lcom/tw/preference/SingleChoosePreference$a;->a(Lcom/tw/preference/SingleChoosePreference;Ljava/lang/Object;)V

    goto :goto_1

    .line 10
    :cond_0
    invoke-virtual {v2}, Landroid/widget/TextView;->getBackground()Landroid/graphics/drawable/Drawable;

    move-result-object v2

    invoke-virtual {v2, v0}, Landroid/graphics/drawable/Drawable;->setLevel(I)Z
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    :cond_1
    :goto_1
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :catch_0
    :cond_2
    return-void
.end method
