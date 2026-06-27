.class Lcom/tw/preference/a;
.super Ljava/lang/Object;
.source "ButtonPreference.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/tw/preference/ButtonPreference;->initView(Landroid/content/Context;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/tw/preference/ButtonPreference;


# direct methods
.method constructor <init>(Lcom/tw/preference/ButtonPreference;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/a;->this$0:Lcom/tw/preference/ButtonPreference;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 0

    .line 1
    iget-object p1, p0, Lcom/tw/preference/a;->this$0:Lcom/tw/preference/ButtonPreference;

    invoke-static {p1}, Lcom/tw/preference/ButtonPreference;->b(Lcom/tw/preference/ButtonPreference;)Lcom/tw/preference/ButtonPreference$a;

    move-result-object p1

    if-eqz p1, :cond_0

    .line 2
    iget-object p1, p0, Lcom/tw/preference/a;->this$0:Lcom/tw/preference/ButtonPreference;

    invoke-static {p1}, Lcom/tw/preference/ButtonPreference;->b(Lcom/tw/preference/ButtonPreference;)Lcom/tw/preference/ButtonPreference$a;

    move-result-object p1

    iget-object p0, p0, Lcom/tw/preference/a;->this$0:Lcom/tw/preference/ButtonPreference;

    invoke-interface {p1, p0}, Lcom/tw/preference/ButtonPreference$a;->a(Lcom/tw/preference/ButtonPreference;)V

    :cond_0
    return-void
.end method
