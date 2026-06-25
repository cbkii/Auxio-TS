.class public Ld/a/a/a/h;
.super Ld/a/a/a/v;
.source ""

# interfaces
.implements Ld/a/a/a/r;


# instance fields
.field public mObserver:Ld/a/a/a/q;


# direct methods
.method public constructor <init>(I)V
    .locals 0

    invoke-direct {p0, p1}, Ld/a/a/a/v;-><init>(I)V

    const/4 p1, 0x0

    iput-object p1, p0, Ld/a/a/a/h;->mObserver:Ld/a/a/a/q;

    return-void
.end method


# virtual methods
.method public a(Ld/a/a/a/q;)V
    .locals 0

    iput-object p1, p0, Ld/a/a/a/h;->mObserver:Ld/a/a/a/q;

    return-void
.end method

.method public b([BIZ)Z
    .locals 0

    if-eqz p1, :cond_1

    if-eqz p3, :cond_0

    goto :goto_0

    :cond_0
    invoke-virtual {p0, p1, p2}, Ld/a/a/a/v;->i([BI)Z

    iget-boolean p0, p0, Ld/a/a/a/v;->mDone:Z

    return p0

    :cond_1
    :goto_0
    const/4 p0, 0x0

    return p0
.end method

.method public tb(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Ld/a/a/a/h;->mObserver:Ld/a/a/a/q;

    if-eqz p0, :cond_0

    invoke-interface {p0, p1}, Ld/a/a/a/q;->Notify(Ljava/lang/String;)V

    :cond_0
    return-void
.end method
