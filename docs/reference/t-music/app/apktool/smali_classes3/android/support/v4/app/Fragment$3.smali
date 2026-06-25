.class Landroid/support/v4/app/Fragment$3;
.super Ljava/lang/Object;
.source "Fragment.java"

# interfaces
.implements Landroid/arch/lifecycle/e;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Landroid/support/v4/app/Fragment;->performCreateView(Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Landroid/support/v4/app/Fragment;


# direct methods
.method constructor <init>(Landroid/support/v4/app/Fragment;)V
    .locals 0

    .line 1
    iput-object p1, p0, Landroid/support/v4/app/Fragment$3;->this$0:Landroid/support/v4/app/Fragment;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public getLifecycle()Landroid/arch/lifecycle/Lifecycle;
    .locals 3

    .line 1
    iget-object v0, p0, Landroid/support/v4/app/Fragment$3;->this$0:Landroid/support/v4/app/Fragment;

    invoke-static {v0}, Landroid/support/v4/app/Fragment;->access$900(Landroid/support/v4/app/Fragment;)Landroid/arch/lifecycle/g;

    move-result-object v0

    if-nez v0, :cond_0

    .line 2
    iget-object v0, p0, Landroid/support/v4/app/Fragment$3;->this$0:Landroid/support/v4/app/Fragment;

    new-instance v1, Landroid/arch/lifecycle/g;

    iget-object v2, v0, Landroid/support/v4/app/Fragment;->mViewLifecycleOwner:Landroid/arch/lifecycle/e;

    invoke-direct {v1, v2}, Landroid/arch/lifecycle/g;-><init>(Landroid/arch/lifecycle/e;)V

    invoke-static {v0, v1}, Landroid/support/v4/app/Fragment;->access$902(Landroid/support/v4/app/Fragment;Landroid/arch/lifecycle/g;)Landroid/arch/lifecycle/g;

    .line 3
    :cond_0
    iget-object p0, p0, Landroid/support/v4/app/Fragment$3;->this$0:Landroid/support/v4/app/Fragment;

    invoke-static {p0}, Landroid/support/v4/app/Fragment;->access$900(Landroid/support/v4/app/Fragment;)Landroid/arch/lifecycle/g;

    move-result-object p0

    return-object p0
.end method
