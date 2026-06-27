.class public Lantlr/TokenStreamSelector;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/TokenStream;
.implements Lantlr/ASdebug/IASDebugStream;


# instance fields
.field public input:Lantlr/TokenStream;

.field public inputStreamNames:Ljava/util/Hashtable;

.field public streamStack:Lantlr/collections/Stack;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lantlr/collections/impl/LList;

    invoke-direct {v0}, Lantlr/collections/impl/LList;-><init>()V

    iput-object v0, p0, Lantlr/TokenStreamSelector;->streamStack:Lantlr/collections/Stack;

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/TokenStreamSelector;->inputStreamNames:Ljava/util/Hashtable;

    return-void
.end method


# virtual methods
.method public addInputStream(Lantlr/TokenStream;Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamSelector;->inputStreamNames:Ljava/util/Hashtable;

    invoke-virtual {p0, p2, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public getCurrentStream()Lantlr/TokenStream;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamSelector;->input:Lantlr/TokenStream;

    return-object p0
.end method

.method public getEntireText()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamSelector;->input:Lantlr/TokenStream;

    invoke-static {p0}, Lantlr/ASdebug/ASDebugStream;->getEntireText(Lantlr/TokenStream;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getOffsetInfo(Lantlr/Token;)Lantlr/ASdebug/TokenOffsetInfo;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamSelector;->input:Lantlr/TokenStream;

    invoke-static {p0, p1}, Lantlr/ASdebug/ASDebugStream;->getOffsetInfo(Lantlr/TokenStream;Lantlr/Token;)Lantlr/ASdebug/TokenOffsetInfo;

    move-result-object p0

    return-object p0
.end method

.method public getStream(Ljava/lang/String;)Lantlr/TokenStream;
    .locals 2

    iget-object p0, p0, Lantlr/TokenStreamSelector;->inputStreamNames:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/TokenStream;

    if-eqz p0, :cond_0

    return-object p0

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string v0, "TokenStream "

    const-string v1, " not found"

    invoke-static {v0, p1, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public nextToken()Lantlr/Token;
    .locals 1

    :catch_0
    :try_start_0
    iget-object v0, p0, Lantlr/TokenStreamSelector;->input:Lantlr/TokenStream;

    invoke-interface {v0}, Lantlr/TokenStream;->nextToken()Lantlr/Token;

    move-result-object p0
    :try_end_0
    .catch Lantlr/TokenStreamRetryException; {:try_start_0 .. :try_end_0} :catch_0

    return-object p0
.end method

.method public pop()Lantlr/TokenStream;
    .locals 1

    iget-object v0, p0, Lantlr/TokenStreamSelector;->streamStack:Lantlr/collections/Stack;

    invoke-interface {v0}, Lantlr/collections/Stack;->pop()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/TokenStream;

    invoke-virtual {p0, v0}, Lantlr/TokenStreamSelector;->select(Lantlr/TokenStream;)V

    return-object v0
.end method

.method public push(Lantlr/TokenStream;)V
    .locals 2

    iget-object v0, p0, Lantlr/TokenStreamSelector;->streamStack:Lantlr/collections/Stack;

    iget-object v1, p0, Lantlr/TokenStreamSelector;->input:Lantlr/TokenStream;

    invoke-interface {v0, v1}, Lantlr/collections/Stack;->push(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/TokenStreamSelector;->select(Lantlr/TokenStream;)V

    return-void
.end method

.method public push(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/TokenStreamSelector;->streamStack:Lantlr/collections/Stack;

    iget-object v1, p0, Lantlr/TokenStreamSelector;->input:Lantlr/TokenStream;

    invoke-interface {v0, v1}, Lantlr/collections/Stack;->push(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/TokenStreamSelector;->select(Ljava/lang/String;)V

    return-void
.end method

.method public retry()V
    .locals 0

    new-instance p0, Lantlr/TokenStreamRetryException;

    invoke-direct {p0}, Lantlr/TokenStreamRetryException;-><init>()V

    throw p0
.end method

.method public select(Lantlr/TokenStream;)V
    .locals 0

    iput-object p1, p0, Lantlr/TokenStreamSelector;->input:Lantlr/TokenStream;

    return-void
.end method

.method public select(Ljava/lang/String;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/TokenStreamSelector;->getStream(Ljava/lang/String;)Lantlr/TokenStream;

    move-result-object p1

    iput-object p1, p0, Lantlr/TokenStreamSelector;->input:Lantlr/TokenStream;

    return-void
.end method
