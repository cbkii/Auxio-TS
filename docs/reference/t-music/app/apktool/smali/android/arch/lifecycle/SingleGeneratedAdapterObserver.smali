.class public Landroid/arch/lifecycle/SingleGeneratedAdapterObserver;
.super Ljava/lang/Object;
.source "SingleGeneratedAdapterObserver.java"

# interfaces
.implements Landroid/arch/lifecycle/GenericLifecycleObserver;


# annotations
.annotation build Landroid/support/annotation/RestrictTo;
    value = {
        .enum Landroid/support/annotation/RestrictTo$Scope;->LIBRARY_GROUP:Landroid/support/annotation/RestrictTo$Scope;
    }
.end annotation


# instance fields
.field private final Oa:Landroid/arch/lifecycle/c;


# direct methods
.method constructor <init>(Landroid/arch/lifecycle/c;)V
    .locals 0

    .line 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 2
    iput-object p1, p0, Landroid/arch/lifecycle/SingleGeneratedAdapterObserver;->Oa:Landroid/arch/lifecycle/c;

    return-void
.end method


# virtual methods
.method public a(Landroid/arch/lifecycle/e;Landroid/arch/lifecycle/Lifecycle$Event;)V
    .locals 3

    .line 1
    iget-object v0, p0, Landroid/arch/lifecycle/SingleGeneratedAdapterObserver;->Oa:Landroid/arch/lifecycle/c;

    const/4 v1, 0x0

    const/4 v2, 0x0

    invoke-interface {v0, p1, p2, v2, v1}, Landroid/arch/lifecycle/c;->a(Landroid/arch/lifecycle/e;Landroid/arch/lifecycle/Lifecycle$Event;ZLandroid/arch/lifecycle/k;)V

    .line 2
    iget-object p0, p0, Landroid/arch/lifecycle/SingleGeneratedAdapterObserver;->Oa:Landroid/arch/lifecycle/c;

    const/4 v0, 0x1

    invoke-interface {p0, p1, p2, v0, v1}, Landroid/arch/lifecycle/c;->a(Landroid/arch/lifecycle/e;Landroid/arch/lifecycle/Lifecycle$Event;ZLandroid/arch/lifecycle/k;)V

    return-void
.end method
