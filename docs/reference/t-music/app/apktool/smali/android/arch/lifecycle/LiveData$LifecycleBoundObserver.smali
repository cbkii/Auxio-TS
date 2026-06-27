.class Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;
.super Landroid/arch/lifecycle/LiveData$b;
.source "LiveData.java"

# interfaces
.implements Landroid/arch/lifecycle/GenericLifecycleObserver;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Landroid/arch/lifecycle/LiveData;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = "LifecycleBoundObserver"
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Landroid/arch/lifecycle/LiveData<",
        "TT;>.b;",
        "Landroid/arch/lifecycle/GenericLifecycleObserver;"
    }
.end annotation


# instance fields
.field final mOwner:Landroid/arch/lifecycle/e;
    .annotation build Landroid/support/annotation/NonNull;
    .end annotation
.end field

.field final synthetic this$0:Landroid/arch/lifecycle/LiveData;


# direct methods
.method constructor <init>(Landroid/arch/lifecycle/LiveData;Landroid/arch/lifecycle/e;Landroid/arch/lifecycle/m;)V
    .locals 0
    .param p1    # Landroid/arch/lifecycle/LiveData;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/arch/lifecycle/e;",
            "Landroid/arch/lifecycle/m<",
            "-TT;>;)V"
        }
    .end annotation

    .line 1
    iput-object p1, p0, Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;->this$0:Landroid/arch/lifecycle/LiveData;

    .line 2
    invoke-direct {p0, p1, p3}, Landroid/arch/lifecycle/LiveData$b;-><init>(Landroid/arch/lifecycle/LiveData;Landroid/arch/lifecycle/m;)V

    .line 3
    iput-object p2, p0, Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;->mOwner:Landroid/arch/lifecycle/e;

    return-void
.end method


# virtual methods
.method public a(Landroid/arch/lifecycle/e;Landroid/arch/lifecycle/Lifecycle$Event;)V
    .locals 0

    .line 1
    iget-object p1, p0, Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;->mOwner:Landroid/arch/lifecycle/e;

    invoke-interface {p1}, Landroid/arch/lifecycle/e;->getLifecycle()Landroid/arch/lifecycle/Lifecycle;

    move-result-object p1

    invoke-virtual {p1}, Landroid/arch/lifecycle/Lifecycle;->getCurrentState()Landroid/arch/lifecycle/Lifecycle$State;

    move-result-object p1

    sget-object p2, Landroid/arch/lifecycle/Lifecycle$State;->DESTROYED:Landroid/arch/lifecycle/Lifecycle$State;

    if-ne p1, p2, :cond_0

    .line 2
    iget-object p1, p0, Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;->this$0:Landroid/arch/lifecycle/LiveData;

    iget-object p0, p0, Landroid/arch/lifecycle/LiveData$b;->mObserver:Landroid/arch/lifecycle/m;

    invoke-virtual {p1, p0}, Landroid/arch/lifecycle/LiveData;->removeObserver(Landroid/arch/lifecycle/m;)V

    return-void

    .line 3
    :cond_0
    invoke-virtual {p0}, Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;->ya()Z

    move-result p1

    invoke-virtual {p0, p1}, Landroid/arch/lifecycle/LiveData$b;->y(Z)V

    return-void
.end method

.method g(Landroid/arch/lifecycle/e;)Z
    .locals 0

    .line 1
    iget-object p0, p0, Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;->mOwner:Landroid/arch/lifecycle/e;

    if-ne p0, p1, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method xa()V
    .locals 1

    .line 1
    iget-object v0, p0, Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;->mOwner:Landroid/arch/lifecycle/e;

    invoke-interface {v0}, Landroid/arch/lifecycle/e;->getLifecycle()Landroid/arch/lifecycle/Lifecycle;

    move-result-object v0

    invoke-virtual {v0, p0}, Landroid/arch/lifecycle/Lifecycle;->b(Landroid/arch/lifecycle/d;)V

    return-void
.end method

.method ya()Z
    .locals 1

    .line 1
    iget-object p0, p0, Landroid/arch/lifecycle/LiveData$LifecycleBoundObserver;->mOwner:Landroid/arch/lifecycle/e;

    invoke-interface {p0}, Landroid/arch/lifecycle/e;->getLifecycle()Landroid/arch/lifecycle/Lifecycle;

    move-result-object p0

    invoke-virtual {p0}, Landroid/arch/lifecycle/Lifecycle;->getCurrentState()Landroid/arch/lifecycle/Lifecycle$State;

    move-result-object p0

    sget-object v0, Landroid/arch/lifecycle/Lifecycle$State;->STARTED:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-virtual {p0, v0}, Landroid/arch/lifecycle/Lifecycle$State;->isAtLeast(Landroid/arch/lifecycle/Lifecycle$State;)Z

    move-result p0

    return p0
.end method
