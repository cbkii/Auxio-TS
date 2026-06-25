.class Lcom/tw/preference/e;
.super Ljava/lang/Object;
.source "TogglePreference.java"

# interfaces
.implements Landroid/widget/CompoundButton$OnCheckedChangeListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/tw/preference/TogglePreference;->initView(Landroid/content/Context;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/tw/preference/TogglePreference;


# direct methods
.method constructor <init>(Lcom/tw/preference/TogglePreference;)V
    .locals 0

    .line 1
    iput-object p1, p0, Lcom/tw/preference/e;->this$0:Lcom/tw/preference/TogglePreference;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onCheckedChanged(Landroid/widget/CompoundButton;Z)V
    .locals 0

    .line 1
    iget-object p1, p0, Lcom/tw/preference/e;->this$0:Lcom/tw/preference/TogglePreference;

    invoke-static {p1}, Lcom/tw/preference/TogglePreference;->a(Lcom/tw/preference/TogglePreference;)Lcom/tw/preference/TogglePreference$a;

    move-result-object p1

    if-eqz p1, :cond_0

    .line 2
    iget-object p1, p0, Lcom/tw/preference/e;->this$0:Lcom/tw/preference/TogglePreference;

    invoke-static {p1}, Lcom/tw/preference/TogglePreference;->a(Lcom/tw/preference/TogglePreference;)Lcom/tw/preference/TogglePreference$a;

    move-result-object p1

    iget-object p0, p0, Lcom/tw/preference/e;->this$0:Lcom/tw/preference/TogglePreference;

    invoke-interface {p1, p0, p2}, Lcom/tw/preference/TogglePreference$a;->a(Lcom/tw/preference/TogglePreference;Z)V

    :cond_0
    return-void
.end method
