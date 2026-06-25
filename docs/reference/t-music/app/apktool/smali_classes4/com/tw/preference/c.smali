.class Lcom/tw/preference/c;
.super Ljava/lang/Object;
.source "SingleChoosePreference.java"

# interfaces
.implements Landroid/widget/PopupWindow$OnDismissListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/tw/preference/SingleChoosePreference;->Ia(I)V
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
    iput-object p1, p0, Lcom/tw/preference/c;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onDismiss()V
    .locals 1

    .line 1
    iget-object p0, p0, Lcom/tw/preference/c;->this$0:Lcom/tw/preference/SingleChoosePreference;

    invoke-static {p0}, Lcom/tw/preference/SingleChoosePreference;->a(Lcom/tw/preference/SingleChoosePreference;)Landroid/widget/ImageView;

    move-result-object p0

    invoke-virtual {p0}, Landroid/widget/ImageView;->getDrawable()Landroid/graphics/drawable/Drawable;

    move-result-object p0

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Landroid/graphics/drawable/Drawable;->setLevel(I)Z

    return-void
.end method
