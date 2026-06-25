.class public Landroid/arch/lifecycle/g;
.super Landroid/arch/lifecycle/Lifecycle;
.source "LifecycleRegistry.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Landroid/arch/lifecycle/g$a;
    }
.end annotation


# instance fields
.field private Da:Landroid/arch/core/b/a;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroid/arch/core/b/a<",
            "Landroid/arch/lifecycle/d;",
            "Landroid/arch/lifecycle/g$a;",
            ">;"
        }
    .end annotation
.end field

.field private Ea:I

.field private Fa:Z

.field private Ga:Z

.field private Ha:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Landroid/arch/lifecycle/Lifecycle$State;",
            ">;"
        }
    .end annotation
.end field

.field private final mLifecycleOwner:Ljava/lang/ref/WeakReference;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/lang/ref/WeakReference<",
            "Landroid/arch/lifecycle/e;",
            ">;"
        }
    .end annotation
.end field

.field private mState:Landroid/arch/lifecycle/Lifecycle$State;


# direct methods
.method public constructor <init>(Landroid/arch/lifecycle/e;)V
    .locals 1
    .param p1    # Landroid/arch/lifecycle/e;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    invoke-direct {p0}, Landroid/arch/lifecycle/Lifecycle;-><init>()V

    .line 2
    new-instance v0, Landroid/arch/core/b/a;

    invoke-direct {v0}, Landroid/arch/core/b/a;-><init>()V

    iput-object v0, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    const/4 v0, 0x0

    .line 3
    iput v0, p0, Landroid/arch/lifecycle/g;->Ea:I

    .line 4
    iput-boolean v0, p0, Landroid/arch/lifecycle/g;->Fa:Z

    .line 5
    iput-boolean v0, p0, Landroid/arch/lifecycle/g;->Ga:Z

    .line 6
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Landroid/arch/lifecycle/g;->Ha:Ljava/util/ArrayList;

    .line 7
    new-instance v0, Ljava/lang/ref/WeakReference;

    invoke-direct {v0, p1}, Ljava/lang/ref/WeakReference;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Landroid/arch/lifecycle/g;->mLifecycleOwner:Ljava/lang/ref/WeakReference;

    .line 8
    sget-object p1, Landroid/arch/lifecycle/Lifecycle$State;->INITIALIZED:Landroid/arch/lifecycle/Lifecycle$State;

    iput-object p1, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    return-void
.end method

.method static a(Landroid/arch/lifecycle/Lifecycle$Event;)Landroid/arch/lifecycle/Lifecycle$State;
    .locals 3

    .line 17
    sget-object v0, Landroid/arch/lifecycle/f;->Ca:[I

    invoke-virtual {p0}, Ljava/lang/Enum;->ordinal()I

    move-result v1

    aget v0, v0, v1

    packed-switch v0, :pswitch_data_0

    .line 18
    new-instance v0, Ljava/lang/IllegalArgumentException;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "Unexpected event value "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw v0

    .line 19
    :pswitch_0
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$State;->DESTROYED:Landroid/arch/lifecycle/Lifecycle$State;

    return-object p0

    .line 20
    :pswitch_1
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$State;->RESUMED:Landroid/arch/lifecycle/Lifecycle$State;

    return-object p0

    .line 21
    :pswitch_2
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$State;->STARTED:Landroid/arch/lifecycle/Lifecycle$State;

    return-object p0

    .line 22
    :pswitch_3
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$State;->CREATED:Landroid/arch/lifecycle/Lifecycle$State;

    return-object p0

    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_3
        :pswitch_3
        :pswitch_2
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method static a(Landroid/arch/lifecycle/Lifecycle$State;Landroid/arch/lifecycle/Lifecycle$State;)Landroid/arch/lifecycle/Lifecycle$State;
    .locals 1
    .param p0    # Landroid/arch/lifecycle/Lifecycle$State;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .param p1    # Landroid/arch/lifecycle/Lifecycle$State;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param

    if-eqz p1, :cond_0

    .line 23
    invoke-virtual {p1, p0}, Ljava/lang/Enum;->compareTo(Ljava/lang/Enum;)I

    move-result v0

    if-gez v0, :cond_0

    move-object p0, p1

    :cond_0
    return-object p0
.end method

.method private static b(Landroid/arch/lifecycle/Lifecycle$State;)Landroid/arch/lifecycle/Lifecycle$Event;
    .locals 3

    .line 4
    sget-object v0, Landroid/arch/lifecycle/f;->Ia:[I

    invoke-virtual {p0}, Ljava/lang/Enum;->ordinal()I

    move-result v1

    aget v0, v0, v1

    const/4 v1, 0x1

    if-eq v0, v1, :cond_4

    const/4 v1, 0x2

    if-eq v0, v1, :cond_3

    const/4 v1, 0x3

    if-eq v0, v1, :cond_2

    const/4 v1, 0x4

    if-eq v0, v1, :cond_1

    const/4 v1, 0x5

    if-eq v0, v1, :cond_0

    .line 5
    new-instance v0, Ljava/lang/IllegalArgumentException;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "Unexpected state value "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw v0

    .line 6
    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    invoke-direct {p0}, Ljava/lang/IllegalArgumentException;-><init>()V

    throw p0

    .line 7
    :cond_1
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$Event;->ON_PAUSE:Landroid/arch/lifecycle/Lifecycle$Event;

    return-object p0

    .line 8
    :cond_2
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$Event;->ON_STOP:Landroid/arch/lifecycle/Lifecycle$Event;

    return-object p0

    .line 9
    :cond_3
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$Event;->ON_DESTROY:Landroid/arch/lifecycle/Lifecycle$Event;

    return-object p0

    .line 10
    :cond_4
    new-instance p0, Ljava/lang/IllegalArgumentException;

    invoke-direct {p0}, Ljava/lang/IllegalArgumentException;-><init>()V

    throw p0
.end method

.method private c(Landroid/arch/lifecycle/d;)Landroid/arch/lifecycle/Lifecycle$State;
    .locals 2

    .line 8
    iget-object v0, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    invoke-virtual {v0, p1}, Landroid/arch/core/b/a;->b(Ljava/lang/Object;)Ljava/util/Map$Entry;

    move-result-object p1

    const/4 v0, 0x0

    if-eqz p1, :cond_0

    .line 9
    invoke-interface {p1}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/arch/lifecycle/g$a;

    iget-object p1, p1, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    goto :goto_0

    :cond_0
    move-object p1, v0

    .line 10
    :goto_0
    iget-object v1, p0, Landroid/arch/lifecycle/g;->Ha:Ljava/util/ArrayList;

    invoke-virtual {v1}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_1

    iget-object v0, p0, Landroid/arch/lifecycle/g;->Ha:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->size()I

    move-result v1

    add-int/lit8 v1, v1, -0x1

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/arch/lifecycle/Lifecycle$State;

    .line 11
    :cond_1
    iget-object p0, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-static {p0, p1}, Landroid/arch/lifecycle/g;->a(Landroid/arch/lifecycle/Lifecycle$State;Landroid/arch/lifecycle/Lifecycle$State;)Landroid/arch/lifecycle/Lifecycle$State;

    move-result-object p0

    invoke-static {p0, v0}, Landroid/arch/lifecycle/g;->a(Landroid/arch/lifecycle/Lifecycle$State;Landroid/arch/lifecycle/Lifecycle$State;)Landroid/arch/lifecycle/Lifecycle$State;

    move-result-object p0

    return-object p0
.end method

.method private c(Landroid/arch/lifecycle/Lifecycle$State;)V
    .locals 1

    .line 1
    iget-object v0, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    if-ne v0, p1, :cond_0

    return-void

    .line 2
    :cond_0
    iput-object p1, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    .line 3
    iget-boolean p1, p0, Landroid/arch/lifecycle/g;->Fa:Z

    const/4 v0, 0x1

    if-nez p1, :cond_2

    iget p1, p0, Landroid/arch/lifecycle/g;->Ea:I

    if-eqz p1, :cond_1

    goto :goto_0

    .line 4
    :cond_1
    iput-boolean v0, p0, Landroid/arch/lifecycle/g;->Fa:Z

    .line 5
    invoke-direct {p0}, Landroid/arch/lifecycle/g;->sync()V

    const/4 p1, 0x0

    .line 6
    iput-boolean p1, p0, Landroid/arch/lifecycle/g;->Fa:Z

    return-void

    .line 7
    :cond_2
    :goto_0
    iput-boolean v0, p0, Landroid/arch/lifecycle/g;->Ga:Z

    return-void
.end method

.method private d(Landroid/arch/lifecycle/Lifecycle$State;)V
    .locals 0

    .line 1
    iget-object p0, p0, Landroid/arch/lifecycle/g;->Ha:Ljava/util/ArrayList;

    invoke-virtual {p0, p1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method private static e(Landroid/arch/lifecycle/Lifecycle$State;)Landroid/arch/lifecycle/Lifecycle$Event;
    .locals 3

    .line 1
    sget-object v0, Landroid/arch/lifecycle/f;->Ia:[I

    invoke-virtual {p0}, Ljava/lang/Enum;->ordinal()I

    move-result v1

    aget v0, v0, v1

    const/4 v1, 0x1

    if-eq v0, v1, :cond_4

    const/4 v1, 0x2

    if-eq v0, v1, :cond_3

    const/4 v1, 0x3

    if-eq v0, v1, :cond_2

    const/4 v1, 0x4

    if-eq v0, v1, :cond_1

    const/4 v1, 0x5

    if-ne v0, v1, :cond_0

    goto :goto_0

    .line 2
    :cond_0
    new-instance v0, Ljava/lang/IllegalArgumentException;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "Unexpected state value "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw v0

    .line 3
    :cond_1
    new-instance p0, Ljava/lang/IllegalArgumentException;

    invoke-direct {p0}, Ljava/lang/IllegalArgumentException;-><init>()V

    throw p0

    .line 4
    :cond_2
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$Event;->ON_RESUME:Landroid/arch/lifecycle/Lifecycle$Event;

    return-object p0

    .line 5
    :cond_3
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$Event;->ON_START:Landroid/arch/lifecycle/Lifecycle$Event;

    return-object p0

    .line 6
    :cond_4
    :goto_0
    sget-object p0, Landroid/arch/lifecycle/Lifecycle$Event;->ON_CREATE:Landroid/arch/lifecycle/Lifecycle$Event;

    return-object p0
.end method

.method private h(Landroid/arch/lifecycle/e;)V
    .locals 5

    .line 1
    iget-object v0, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    .line 2
    invoke-virtual {v0}, Landroid/arch/core/b/c;->descendingIterator()Ljava/util/Iterator;

    move-result-object v0

    .line 3
    :cond_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_1

    iget-boolean v1, p0, Landroid/arch/lifecycle/g;->Ga:Z

    if-nez v1, :cond_1

    .line 4
    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/util/Map$Entry;

    .line 5
    invoke-interface {v1}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/arch/lifecycle/g$a;

    .line 6
    :goto_0
    iget-object v3, v2, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    iget-object v4, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-virtual {v3, v4}, Ljava/lang/Enum;->compareTo(Ljava/lang/Enum;)I

    move-result v3

    if-lez v3, :cond_0

    iget-boolean v3, p0, Landroid/arch/lifecycle/g;->Ga:Z

    if-nez v3, :cond_0

    iget-object v3, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    .line 7
    invoke-interface {v1}, Ljava/util/Map$Entry;->getKey()Ljava/lang/Object;

    move-result-object v4

    invoke-virtual {v3, v4}, Landroid/arch/core/b/a;->contains(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_0

    .line 8
    iget-object v3, v2, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-static {v3}, Landroid/arch/lifecycle/g;->b(Landroid/arch/lifecycle/Lifecycle$State;)Landroid/arch/lifecycle/Lifecycle$Event;

    move-result-object v3

    .line 9
    invoke-static {v3}, Landroid/arch/lifecycle/g;->a(Landroid/arch/lifecycle/Lifecycle$Event;)Landroid/arch/lifecycle/Lifecycle$State;

    move-result-object v4

    invoke-direct {p0, v4}, Landroid/arch/lifecycle/g;->d(Landroid/arch/lifecycle/Lifecycle$State;)V

    .line 10
    invoke-virtual {v2, p1, v3}, Landroid/arch/lifecycle/g$a;->b(Landroid/arch/lifecycle/e;Landroid/arch/lifecycle/Lifecycle$Event;)V

    .line 11
    invoke-direct {p0}, Landroid/arch/lifecycle/g;->me()V

    goto :goto_0

    :cond_1
    return-void
.end method

.method private i(Landroid/arch/lifecycle/e;)V
    .locals 5

    .line 1
    iget-object v0, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    .line 2
    invoke-virtual {v0}, Landroid/arch/core/b/c;->va()Landroid/arch/core/b/c$d;

    move-result-object v0

    .line 3
    :cond_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_1

    iget-boolean v1, p0, Landroid/arch/lifecycle/g;->Ga:Z

    if-nez v1, :cond_1

    .line 4
    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/util/Map$Entry;

    .line 5
    invoke-interface {v1}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/arch/lifecycle/g$a;

    .line 6
    :goto_0
    iget-object v3, v2, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    iget-object v4, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-virtual {v3, v4}, Ljava/lang/Enum;->compareTo(Ljava/lang/Enum;)I

    move-result v3

    if-gez v3, :cond_0

    iget-boolean v3, p0, Landroid/arch/lifecycle/g;->Ga:Z

    if-nez v3, :cond_0

    iget-object v3, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    .line 7
    invoke-interface {v1}, Ljava/util/Map$Entry;->getKey()Ljava/lang/Object;

    move-result-object v4

    invoke-virtual {v3, v4}, Landroid/arch/core/b/a;->contains(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_0

    .line 8
    iget-object v3, v2, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-direct {p0, v3}, Landroid/arch/lifecycle/g;->d(Landroid/arch/lifecycle/Lifecycle$State;)V

    .line 9
    iget-object v3, v2, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-static {v3}, Landroid/arch/lifecycle/g;->e(Landroid/arch/lifecycle/Lifecycle$State;)Landroid/arch/lifecycle/Lifecycle$Event;

    move-result-object v3

    invoke-virtual {v2, p1, v3}, Landroid/arch/lifecycle/g$a;->b(Landroid/arch/lifecycle/e;Landroid/arch/lifecycle/Lifecycle$Event;)V

    .line 10
    invoke-direct {p0}, Landroid/arch/lifecycle/g;->me()V

    goto :goto_0

    :cond_1
    return-void
.end method

.method private le()Z
    .locals 3

    .line 1
    iget-object v0, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    invoke-virtual {v0}, Landroid/arch/core/b/c;->size()I

    move-result v0

    const/4 v1, 0x1

    if-nez v0, :cond_0

    return v1

    .line 2
    :cond_0
    iget-object v0, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    invoke-virtual {v0}, Landroid/arch/core/b/c;->eldest()Ljava/util/Map$Entry;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/arch/lifecycle/g$a;

    iget-object v0, v0, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    .line 3
    iget-object v2, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    invoke-virtual {v2}, Landroid/arch/core/b/c;->wa()Ljava/util/Map$Entry;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/arch/lifecycle/g$a;

    iget-object v2, v2, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    if-ne v0, v2, :cond_1

    .line 4
    iget-object p0, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    if-ne p0, v2, :cond_1

    goto :goto_0

    :cond_1
    const/4 v1, 0x0

    :goto_0
    return v1
.end method

.method private me()V
    .locals 1

    .line 1
    iget-object p0, p0, Landroid/arch/lifecycle/g;->Ha:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->size()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    invoke-virtual {p0, v0}, Ljava/util/ArrayList;->remove(I)Ljava/lang/Object;

    return-void
.end method

.method private sync()V
    .locals 3

    .line 1
    iget-object v0, p0, Landroid/arch/lifecycle/g;->mLifecycleOwner:Ljava/lang/ref/WeakReference;

    invoke-virtual {v0}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/arch/lifecycle/e;

    if-nez v0, :cond_0

    const-string p0, "LifecycleRegistry"

    const-string v0, "LifecycleOwner is garbage collected, you shouldn\'t try dispatch new events from it."

    .line 2
    invoke-static {p0, v0}, Landroid/util/Log;->w(Ljava/lang/String;Ljava/lang/String;)I

    return-void

    .line 3
    :cond_0
    :goto_0
    invoke-direct {p0}, Landroid/arch/lifecycle/g;->le()Z

    move-result v1

    const/4 v2, 0x0

    if-nez v1, :cond_2

    .line 4
    iput-boolean v2, p0, Landroid/arch/lifecycle/g;->Ga:Z

    .line 5
    iget-object v1, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    iget-object v2, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    invoke-virtual {v2}, Landroid/arch/core/b/c;->eldest()Ljava/util/Map$Entry;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/arch/lifecycle/g$a;

    iget-object v2, v2, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-virtual {v1, v2}, Ljava/lang/Enum;->compareTo(Ljava/lang/Enum;)I

    move-result v1

    if-gez v1, :cond_1

    .line 6
    invoke-direct {p0, v0}, Landroid/arch/lifecycle/g;->h(Landroid/arch/lifecycle/e;)V

    .line 7
    :cond_1
    iget-object v1, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    invoke-virtual {v1}, Landroid/arch/core/b/c;->wa()Ljava/util/Map$Entry;

    move-result-object v1

    .line 8
    iget-boolean v2, p0, Landroid/arch/lifecycle/g;->Ga:Z

    if-nez v2, :cond_0

    if-eqz v1, :cond_0

    iget-object v2, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    .line 9
    invoke-interface {v1}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/arch/lifecycle/g$a;

    iget-object v1, v1, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-virtual {v2, v1}, Ljava/lang/Enum;->compareTo(Ljava/lang/Enum;)I

    move-result v1

    if-lez v1, :cond_0

    .line 10
    invoke-direct {p0, v0}, Landroid/arch/lifecycle/g;->i(Landroid/arch/lifecycle/e;)V

    goto :goto_0

    .line 11
    :cond_2
    iput-boolean v2, p0, Landroid/arch/lifecycle/g;->Ga:Z

    return-void
.end method


# virtual methods
.method public a(Landroid/arch/lifecycle/Lifecycle$State;)V
    .locals 0
    .param p1    # Landroid/arch/lifecycle/Lifecycle$State;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .annotation build Landroid/support/annotation/MainThread;
    .end annotation

    .line 1
    invoke-direct {p0, p1}, Landroid/arch/lifecycle/g;->c(Landroid/arch/lifecycle/Lifecycle$State;)V

    return-void
.end method

.method public a(Landroid/arch/lifecycle/d;)V
    .locals 6
    .param p1    # Landroid/arch/lifecycle/d;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 2
    iget-object v0, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    sget-object v1, Landroid/arch/lifecycle/Lifecycle$State;->DESTROYED:Landroid/arch/lifecycle/Lifecycle$State;

    if-ne v0, v1, :cond_0

    goto :goto_0

    :cond_0
    sget-object v1, Landroid/arch/lifecycle/Lifecycle$State;->INITIALIZED:Landroid/arch/lifecycle/Lifecycle$State;

    .line 3
    :goto_0
    new-instance v0, Landroid/arch/lifecycle/g$a;

    invoke-direct {v0, p1, v1}, Landroid/arch/lifecycle/g$a;-><init>(Landroid/arch/lifecycle/d;Landroid/arch/lifecycle/Lifecycle$State;)V

    .line 4
    iget-object v1, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    invoke-virtual {v1, p1, v0}, Landroid/arch/core/b/a;->putIfAbsent(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/arch/lifecycle/g$a;

    if-eqz v1, :cond_1

    return-void

    .line 5
    :cond_1
    iget-object v1, p0, Landroid/arch/lifecycle/g;->mLifecycleOwner:Ljava/lang/ref/WeakReference;

    invoke-virtual {v1}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/arch/lifecycle/e;

    if-nez v1, :cond_2

    return-void

    .line 6
    :cond_2
    iget v2, p0, Landroid/arch/lifecycle/g;->Ea:I

    const/4 v3, 0x1

    if-nez v2, :cond_4

    iget-boolean v2, p0, Landroid/arch/lifecycle/g;->Fa:Z

    if-eqz v2, :cond_3

    goto :goto_1

    :cond_3
    const/4 v2, 0x0

    goto :goto_2

    :cond_4
    :goto_1
    move v2, v3

    .line 7
    :goto_2
    invoke-direct {p0, p1}, Landroid/arch/lifecycle/g;->c(Landroid/arch/lifecycle/d;)Landroid/arch/lifecycle/Lifecycle$State;

    move-result-object v4

    .line 8
    iget v5, p0, Landroid/arch/lifecycle/g;->Ea:I

    add-int/2addr v5, v3

    iput v5, p0, Landroid/arch/lifecycle/g;->Ea:I

    .line 9
    :goto_3
    iget-object v5, v0, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-virtual {v5, v4}, Ljava/lang/Enum;->compareTo(Ljava/lang/Enum;)I

    move-result v4

    if-gez v4, :cond_5

    iget-object v4, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    .line 10
    invoke-virtual {v4, p1}, Landroid/arch/core/b/a;->contains(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_5

    .line 11
    iget-object v4, v0, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-direct {p0, v4}, Landroid/arch/lifecycle/g;->d(Landroid/arch/lifecycle/Lifecycle$State;)V

    .line 12
    iget-object v4, v0, Landroid/arch/lifecycle/g$a;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    invoke-static {v4}, Landroid/arch/lifecycle/g;->e(Landroid/arch/lifecycle/Lifecycle$State;)Landroid/arch/lifecycle/Lifecycle$Event;

    move-result-object v4

    invoke-virtual {v0, v1, v4}, Landroid/arch/lifecycle/g$a;->b(Landroid/arch/lifecycle/e;Landroid/arch/lifecycle/Lifecycle$Event;)V

    .line 13
    invoke-direct {p0}, Landroid/arch/lifecycle/g;->me()V

    .line 14
    invoke-direct {p0, p1}, Landroid/arch/lifecycle/g;->c(Landroid/arch/lifecycle/d;)Landroid/arch/lifecycle/Lifecycle$State;

    move-result-object v4

    goto :goto_3

    :cond_5
    if-nez v2, :cond_6

    .line 15
    invoke-direct {p0}, Landroid/arch/lifecycle/g;->sync()V

    .line 16
    :cond_6
    iget p1, p0, Landroid/arch/lifecycle/g;->Ea:I

    sub-int/2addr p1, v3

    iput p1, p0, Landroid/arch/lifecycle/g;->Ea:I

    return-void
.end method

.method public b(Landroid/arch/lifecycle/Lifecycle$Event;)V
    .locals 0
    .param p1    # Landroid/arch/lifecycle/Lifecycle$Event;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    invoke-static {p1}, Landroid/arch/lifecycle/g;->a(Landroid/arch/lifecycle/Lifecycle$Event;)Landroid/arch/lifecycle/Lifecycle$State;

    move-result-object p1

    .line 2
    invoke-direct {p0, p1}, Landroid/arch/lifecycle/g;->c(Landroid/arch/lifecycle/Lifecycle$State;)V

    return-void
.end method

.method public b(Landroid/arch/lifecycle/d;)V
    .locals 0
    .param p1    # Landroid/arch/lifecycle/d;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 3
    iget-object p0, p0, Landroid/arch/lifecycle/g;->Da:Landroid/arch/core/b/a;

    invoke-virtual {p0, p1}, Landroid/arch/core/b/a;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public getCurrentState()Landroid/arch/lifecycle/Lifecycle$State;
    .locals 0
    .annotation build Landroid/support/annotation/NonNull;
    .end annotation

    .line 1
    iget-object p0, p0, Landroid/arch/lifecycle/g;->mState:Landroid/arch/lifecycle/Lifecycle$State;

    return-object p0
.end method
