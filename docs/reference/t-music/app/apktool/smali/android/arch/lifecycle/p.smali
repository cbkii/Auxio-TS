.class public Landroid/arch/lifecycle/p;
.super Ljava/lang/Object;
.source "ViewModelProvider.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Landroid/arch/lifecycle/p$a;
    }
.end annotation


# instance fields
.field private final mFactory:Landroid/arch/lifecycle/p$a;

.field private final mViewModelStore:Landroid/arch/lifecycle/q;


# direct methods
.method public constructor <init>(Landroid/arch/lifecycle/q;Landroid/arch/lifecycle/p$a;)V
    .locals 0
    .param p1    # Landroid/arch/lifecycle/q;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Landroid/arch/lifecycle/p$a;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 2
    iput-object p2, p0, Landroid/arch/lifecycle/p;->mFactory:Landroid/arch/lifecycle/p$a;

    .line 3
    iput-object p1, p0, Landroid/arch/lifecycle/p;->mViewModelStore:Landroid/arch/lifecycle/q;

    return-void
.end method


# virtual methods
.method public a(Ljava/lang/String;Ljava/lang/Class;)Landroid/arch/lifecycle/o;
    .locals 2
    .param p1    # Ljava/lang/String;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Ljava/lang/Class;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .annotation build Landroid/support/annotation/MainThread;
    .end annotation

    .annotation build Landroid/support/annotation/NonNull;
    .end annotation

    .annotation system Ldalvik/annotation/Signature;
        value = {
            "<T:",
            "Landroid/arch/lifecycle/o;",
            ">(",
            "Ljava/lang/String;",
            "Ljava/lang/Class<",
            "TT;>;)TT;"
        }
    .end annotation

    .line 1
    iget-object v0, p0, Landroid/arch/lifecycle/p;->mViewModelStore:Landroid/arch/lifecycle/q;

    invoke-virtual {v0, p1}, Landroid/arch/lifecycle/q;->get(Ljava/lang/String;)Landroid/arch/lifecycle/o;

    move-result-object v0

    .line 2
    invoke-virtual {p2, v0}, Ljava/lang/Class;->isInstance(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    return-object v0

    .line 3
    :cond_0
    iget-object v0, p0, Landroid/arch/lifecycle/p;->mFactory:Landroid/arch/lifecycle/p$a;

    invoke-interface {v0, p2}, Landroid/arch/lifecycle/p$a;->create(Ljava/lang/Class;)Landroid/arch/lifecycle/o;

    move-result-object p2

    .line 4
    iget-object p0, p0, Landroid/arch/lifecycle/p;->mViewModelStore:Landroid/arch/lifecycle/q;

    invoke-virtual {p0, p1, p2}, Landroid/arch/lifecycle/q;->a(Ljava/lang/String;Landroid/arch/lifecycle/o;)V

    return-object p2
.end method

.method public c(Ljava/lang/Class;)Landroid/arch/lifecycle/o;
    .locals 3
    .param p1    # Ljava/lang/Class;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .annotation build Landroid/support/annotation/MainThread;
    .end annotation

    .annotation build Landroid/support/annotation/NonNull;
    .end annotation

    .annotation system Ldalvik/annotation/Signature;
        value = {
            "<T:",
            "Landroid/arch/lifecycle/o;",
            ">(",
            "Ljava/lang/Class<",
            "TT;>;)TT;"
        }
    .end annotation

    .line 1
    invoke-virtual {p1}, Ljava/lang/Class;->getCanonicalName()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    .line 2
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "android.arch.lifecycle.ViewModelProvider.DefaultKey:"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0, p1}, Landroid/arch/lifecycle/p;->a(Ljava/lang/String;Ljava/lang/Class;)Landroid/arch/lifecycle/o;

    move-result-object p0

    return-object p0

    .line 3
    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "Local and anonymous classes can not be ViewModels"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method
