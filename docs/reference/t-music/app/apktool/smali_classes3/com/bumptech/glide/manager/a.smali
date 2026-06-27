.class Lcom/bumptech/glide/manager/a;
.super Ljava/lang/Object;
.source "ActivityFragmentLifecycle.java"

# interfaces
.implements Lcom/bumptech/glide/manager/b;


# instance fields
.field private final Ef:Ljava/util/Set;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/Set<",
            "Lcom/bumptech/glide/manager/c;",
            ">;"
        }
    .end annotation
.end field

.field private Ff:Z

.field private Gf:Z


# direct methods
.method constructor <init>()V
    .locals 1

    .line 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 2
    new-instance v0, Ljava/util/WeakHashMap;

    invoke-direct {v0}, Ljava/util/WeakHashMap;-><init>()V

    invoke-static {v0}, Ljava/util/Collections;->newSetFromMap(Ljava/util/Map;)Ljava/util/Set;

    move-result-object v0

    iput-object v0, p0, Lcom/bumptech/glide/manager/a;->Ef:Ljava/util/Set;

    return-void
.end method


# virtual methods
.method onDestroy()V
    .locals 1

    const/4 v0, 0x1

    .line 1
    iput-boolean v0, p0, Lcom/bumptech/glide/manager/a;->Gf:Z

    .line 2
    iget-object p0, p0, Lcom/bumptech/glide/manager/a;->Ef:Ljava/util/Set;

    invoke-static {p0}, Lc/a/a/a/a;->a(Ljava/util/Collection;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/bumptech/glide/manager/c;

    .line 3
    invoke-interface {v0}, Lcom/bumptech/glide/manager/c;->onDestroy()V

    goto :goto_0

    :cond_0
    return-void
.end method

.method onStart()V
    .locals 1

    const/4 v0, 0x1

    .line 1
    iput-boolean v0, p0, Lcom/bumptech/glide/manager/a;->Ff:Z

    .line 2
    iget-object p0, p0, Lcom/bumptech/glide/manager/a;->Ef:Ljava/util/Set;

    invoke-static {p0}, Lc/a/a/a/a;->a(Ljava/util/Collection;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/bumptech/glide/manager/c;

    .line 3
    invoke-interface {v0}, Lcom/bumptech/glide/manager/c;->onStart()V

    goto :goto_0

    :cond_0
    return-void
.end method

.method onStop()V
    .locals 1

    const/4 v0, 0x0

    .line 1
    iput-boolean v0, p0, Lcom/bumptech/glide/manager/a;->Ff:Z

    .line 2
    iget-object p0, p0, Lcom/bumptech/glide/manager/a;->Ef:Ljava/util/Set;

    invoke-static {p0}, Lc/a/a/a/a;->a(Ljava/util/Collection;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/bumptech/glide/manager/c;

    .line 3
    invoke-interface {v0}, Lcom/bumptech/glide/manager/c;->onStop()V

    goto :goto_0

    :cond_0
    return-void
.end method
