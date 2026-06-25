.class Lcom/tw/preference/b;
.super Ljava/lang/Object;
.source "SingleChoosePreference.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/tw/preference/SingleChoosePreference;->initView(Landroid/content/Context;)V
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
    iput-object p1, p0, Lcom/tw/preference/b;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 1

    .line 1
    iget-object p1, p0, Lcom/tw/preference/b;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {p1}, Lcom/tw/preference/SingleChoosePreference;->a(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/ImageView;

    move-result-object p1

    invoke-virtual {p1}, Landroid/widget/ImageView;->getDrawable()Landroid/graphics/drawable/Drawable;

    move-result-object p1

    if-eqz p1, :cond_0

    .line 2
    iget-object p1, p0, Lcom/tw/preference/b;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {p1}, Lcom/tw/preference/SingleChoosePreference;->a(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/ImageView;

    move-result-object p1

    invoke-virtual {p1}, Landroid/widget/ImageView;->getDrawable()Landroid/graphics/drawable/Drawable;

    move-result-object p1

    const/4 v0, 0x1

    invoke-virtual {p1, v0}, Landroid/graphics/drawable/Drawable;->setLevel(I)Z

    .line 3
    :cond_0
    iget-object p0, p0, Lcom/tw/preference/b;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {p0}, Lcom/tw/preference/SingleChoosePreference;->b(Lcom/tw/preference/SingleChoosePreference;)I

    move-result p1

    invoke-static {p0, p1}, Lcom/tw/preference/SingleChoosePreference;->b(Lcom/tw/preference/SingleChoosePreference;I)V

    return-void
.end method
