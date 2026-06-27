.class public Landroid/tw/john/TWUtil;
.super Ljava/lang/Object;
.source "TWUtil.java"

.field private static final TAG:Ljava/lang/String; = "TWUtilPixelCompat"

.field private final mHandlers:Ljava/util/Map;

.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/concurrent/ConcurrentHashMap;

    invoke-direct {v0}, Ljava/util/concurrent/ConcurrentHashMap;-><init>()V

    iput-object v0, p0, Landroid/tw/john/TWUtil;->mHandlers:Ljava/util/Map;

    const-string v0, "init()"

    invoke-static {v0}, Landroid/tw/john/TWUtil;->logCompat(Ljava/lang/String;)V

    return-void
.end method

.method public constructor <init>(I)V
    .locals 0

    invoke-direct {p0}, Landroid/tw/john/TWUtil;-><init>()V

    return-void
.end method

.method private static logCompat(Ljava/lang/String;)V
    .locals 1

    const-string v0, "TWUtilPixelCompat"

    invoke-static {v0, p0}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method public addHandler(Ljava/lang/String;Landroid/os/Handler;)V
    .locals 1

    if-eqz p1, :cond_0

    if-nez p2, :cond_1

    :cond_0
    return-void

    :cond_1
    iget-object v0, p0, Landroid/tw/john/TWUtil;->mHandlers:Ljava/util/Map;

    invoke-interface {v0, p1, p2}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public close()V
    .locals 1

    const-string v0, "close()"

    invoke-static {v0}, Landroid/tw/john/TWUtil;->logCompat(Ljava/lang/String;)V

    return-void
.end method

.method public open([S)I
    .locals 1

    const-string v0, "open([S) -> 0"

    invoke-static {v0}, Landroid/tw/john/TWUtil;->logCompat(Ljava/lang/String;)V

    const/4 v0, 0x0

    return v0
.end method

.method public open([SI)I
    .locals 1

    const-string v0, "open([SI) -> 0"

    invoke-static {v0}, Landroid/tw/john/TWUtil;->logCompat(Ljava/lang/String;)V

    const/4 v0, 0x0

    return v0
.end method

.method public removeHandler(Ljava/lang/String;)V
    .locals 1

    if-eqz p1, :cond_0

    iget-object v0, p0, Landroid/tw/john/TWUtil;->mHandlers:Ljava/util/Map;

    invoke-interface {v0, p1}, Ljava/util/Map;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    :cond_0
    return-void
.end method

.method public start()V
    .locals 1

    const-string v0, "start()"

    invoke-static {v0}, Landroid/tw/john/TWUtil;->logCompat(Ljava/lang/String;)V

    return-void
.end method

.method public stop()V
    .locals 1

    const-string v0, "stop()"

    invoke-static {v0}, Landroid/tw/john/TWUtil;->logCompat(Ljava/lang/String;)V

    return-void
.end method

.method public write(I)I
    .locals 1

    const/4 v0, 0x0

    return v0
.end method

.method public write(II)I
    .locals 1

    const/4 v0, 0x0

    return v0
.end method

.method public write(III)I
    .locals 1

    const/4 v0, 0x0

    return v0
.end method

.method public write(IIILjava/lang/Object;)I
    .locals 1

    const/4 v0, 0x0

    return v0
.end method

.method public write(IIILjava/lang/Object;Ljava/lang/Object;)I
    .locals 1

    const/4 v0, 0x0

    return v0
.end method
