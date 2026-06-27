.class public Lantlr/debug/InputBufferEventSupport;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final CONSUME:I = 0x0

.field public static final LA:I = 0x1

.field public static final MARK:I = 0x2

.field public static final REWIND:I = 0x3


# instance fields
.field public inputBufferEvent:Lantlr/debug/InputBufferEvent;

.field public inputBufferListeners:Ljava/util/Vector;

.field public source:Ljava/lang/Object;


# direct methods
.method public constructor <init>(Ljava/lang/Object;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lantlr/debug/InputBufferEvent;

    invoke-direct {v0, p1}, Lantlr/debug/InputBufferEvent;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    iput-object p1, p0, Lantlr/debug/InputBufferEventSupport;->source:Ljava/lang/Object;

    return-void
.end method


# virtual methods
.method public addInputBufferListener(Lantlr/debug/InputBufferListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Vector;

    invoke-direct {v0}, Ljava/util/Vector;-><init>()V

    iput-object v0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    :cond_0
    iget-object p0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    invoke-virtual {p0, p1}, Ljava/util/Vector;->addElement(Ljava/lang/Object;)V

    return-void
.end method

.method public fireConsume(C)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, p1, v1}, Lantlr/debug/InputBufferEvent;->setValues(ICI)V

    iget-object p1, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    invoke-virtual {p0, v1, p1}, Lantlr/debug/InputBufferEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireEvent(ILantlr/debug/ListenerBase;)V
    .locals 1

    if-eqz p1, :cond_3

    const/4 v0, 0x1

    if-eq p1, v0, :cond_2

    const/4 v0, 0x2

    if-eq p1, v0, :cond_1

    const/4 v0, 0x3

    if-ne p1, v0, :cond_0

    check-cast p2, Lantlr/debug/InputBufferListener;

    iget-object p0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    invoke-interface {p2, p0}, Lantlr/debug/InputBufferListener;->inputBufferRewind(Lantlr/debug/InputBufferEvent;)V

    goto :goto_0

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "bad type "

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, " for fireEvent()"

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_1
    check-cast p2, Lantlr/debug/InputBufferListener;

    iget-object p0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    invoke-interface {p2, p0}, Lantlr/debug/InputBufferListener;->inputBufferMark(Lantlr/debug/InputBufferEvent;)V

    goto :goto_0

    :cond_2
    check-cast p2, Lantlr/debug/InputBufferListener;

    iget-object p0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    invoke-interface {p2, p0}, Lantlr/debug/InputBufferListener;->inputBufferLA(Lantlr/debug/InputBufferEvent;)V

    goto :goto_0

    :cond_3
    check-cast p2, Lantlr/debug/InputBufferListener;

    iget-object p0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    invoke-interface {p2, p0}, Lantlr/debug/InputBufferListener;->inputBufferConsume(Lantlr/debug/InputBufferEvent;)V

    :goto_0
    return-void
.end method

.method public fireEvents(ILjava/util/Vector;)V
    .locals 2

    monitor-enter p0

    if-nez p2, :cond_0

    :try_start_0
    monitor-exit p0

    return-void

    :cond_0
    invoke-virtual {p2}, Ljava/util/Vector;->clone()Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Ljava/util/Vector;

    monitor-exit p0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    if-eqz p2, :cond_1

    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p2}, Ljava/util/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_1

    invoke-virtual {p2, v0}, Ljava/util/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/debug/ListenerBase;

    invoke-virtual {p0, p1, v1}, Lantlr/debug/InputBufferEventSupport;->fireEvent(ILantlr/debug/ListenerBase;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    return-void

    :catchall_0
    move-exception p1

    :try_start_1
    monitor-exit p0
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    throw p1
.end method

.method public fireLA(CI)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    const/4 v1, 0x1

    invoke-virtual {v0, v1, p1, p2}, Lantlr/debug/InputBufferEvent;->setValues(ICI)V

    iget-object p1, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    invoke-virtual {p0, v1, p1}, Lantlr/debug/InputBufferEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMark(I)V
    .locals 3

    iget-object v0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    const/4 v1, 0x2

    const/16 v2, 0x20

    invoke-virtual {v0, v1, v2, p1}, Lantlr/debug/InputBufferEvent;->setValues(ICI)V

    iget-object p1, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    invoke-virtual {p0, v1, p1}, Lantlr/debug/InputBufferEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireRewind(I)V
    .locals 3

    iget-object v0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferEvent:Lantlr/debug/InputBufferEvent;

    const/4 v1, 0x3

    const/16 v2, 0x20

    invoke-virtual {v0, v1, v2, p1}, Lantlr/debug/InputBufferEvent;->setValues(ICI)V

    iget-object p1, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    invoke-virtual {p0, v1, p1}, Lantlr/debug/InputBufferEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public getInputBufferListeners()Ljava/util/Vector;
    .locals 0

    iget-object p0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    return-object p0
.end method

.method public refresh(Ljava/util/Vector;)V
    .locals 1

    monitor-enter p1

    :try_start_0
    invoke-virtual {p1}, Ljava/util/Vector;->clone()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/util/Vector;

    monitor-exit p1
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    if-eqz p0, :cond_0

    const/4 p1, 0x0

    :goto_0
    invoke-virtual {p0}, Ljava/util/Vector;->size()I

    move-result v0

    if-ge p1, v0, :cond_0

    invoke-virtual {p0, p1}, Ljava/util/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/debug/ListenerBase;

    invoke-interface {v0}, Lantlr/debug/ListenerBase;->refresh()V

    add-int/lit8 p1, p1, 0x1

    goto :goto_0

    :cond_0
    return-void

    :catchall_0
    move-exception p0

    :try_start_1
    monitor-exit p1
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    throw p0
.end method

.method public refreshListeners()V
    .locals 1

    iget-object v0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    invoke-virtual {p0, v0}, Lantlr/debug/InputBufferEventSupport;->refresh(Ljava/util/Vector;)V

    return-void
.end method

.method public removeInputBufferListener(Lantlr/debug/InputBufferListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/InputBufferEventSupport;->inputBufferListeners:Ljava/util/Vector;

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1}, Ljava/util/Vector;->removeElement(Ljava/lang/Object;)Z

    :cond_0
    return-void
.end method
