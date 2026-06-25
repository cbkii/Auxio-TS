.class public Lantlr/TokenStreamRewriteEngine;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/TokenStream;
.implements Lantlr/ASdebug/IASDebugStream;


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lantlr/TokenStreamRewriteEngine$DeleteOp;,
        Lantlr/TokenStreamRewriteEngine$ReplaceOp;,
        Lantlr/TokenStreamRewriteEngine$InsertBeforeOp;,
        Lantlr/TokenStreamRewriteEngine$RewriteOperation;
    }
.end annotation


# static fields
.field public static final DEFAULT_PROGRAM_NAME:Ljava/lang/String; = "default"

.field public static final MIN_TOKEN_INDEX:I = 0x0

.field public static final PROGRAM_INIT_SIZE:I = 0x64


# instance fields
.field public discardMask:Lantlr/collections/impl/BitSet;

.field public index:I

.field public lastRewriteTokenIndexes:Ljava/util/Map;

.field public programs:Ljava/util/Map;

.field public stream:Lantlr/TokenStream;

.field public tokens:Ljava/util/List;


# direct methods
.method public constructor <init>(Lantlr/TokenStream;)V
    .locals 1

    const/16 v0, 0x3e8

    invoke-direct {p0, p1, v0}, Lantlr/TokenStreamRewriteEngine;-><init>(Lantlr/TokenStream;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;I)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/TokenStreamRewriteEngine;->programs:Ljava/util/Map;

    iput-object v0, p0, Lantlr/TokenStreamRewriteEngine;->lastRewriteTokenIndexes:Ljava/util/Map;

    const/4 v0, 0x0

    iput v0, p0, Lantlr/TokenStreamRewriteEngine;->index:I

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-direct {v0}, Lantlr/collections/impl/BitSet;-><init>()V

    iput-object v0, p0, Lantlr/TokenStreamRewriteEngine;->discardMask:Lantlr/collections/impl/BitSet;

    iput-object p1, p0, Lantlr/TokenStreamRewriteEngine;->stream:Lantlr/TokenStream;

    new-instance p1, Ljava/util/ArrayList;

    invoke-direct {p1, p2}, Ljava/util/ArrayList;-><init>(I)V

    iput-object p1, p0, Lantlr/TokenStreamRewriteEngine;->tokens:Ljava/util/List;

    new-instance p1, Ljava/util/HashMap;

    invoke-direct {p1}, Ljava/util/HashMap;-><init>()V

    iput-object p1, p0, Lantlr/TokenStreamRewriteEngine;->programs:Ljava/util/Map;

    iget-object p1, p0, Lantlr/TokenStreamRewriteEngine;->programs:Ljava/util/Map;

    new-instance p2, Ljava/util/ArrayList;

    const/16 v0, 0x64

    invoke-direct {p2, v0}, Ljava/util/ArrayList;-><init>(I)V

    const-string v0, "default"

    invoke-interface {p1, v0, p2}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    new-instance p1, Ljava/util/HashMap;

    invoke-direct {p1}, Ljava/util/HashMap;-><init>()V

    iput-object p1, p0, Lantlr/TokenStreamRewriteEngine;->lastRewriteTokenIndexes:Ljava/util/Map;

    return-void
.end method

.method private initializeProgram(Ljava/lang/String;)Ljava/util/List;
    .locals 2

    new-instance v0, Ljava/util/ArrayList;

    const/16 v1, 0x64

    invoke-direct {v0, v1}, Ljava/util/ArrayList;-><init>(I)V

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->programs:Ljava/util/Map;

    invoke-interface {p0, p1, v0}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-object v0
.end method


# virtual methods
.method public addToSortedRewriteList(Lantlr/TokenStreamRewriteEngine$RewriteOperation;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1}, Lantlr/TokenStreamRewriteEngine;->addToSortedRewriteList(Ljava/lang/String;Lantlr/TokenStreamRewriteEngine$RewriteOperation;)V

    return-void
.end method

.method public addToSortedRewriteList(Ljava/lang/String;Lantlr/TokenStreamRewriteEngine$RewriteOperation;)V
    .locals 6

    invoke-virtual {p0, p1}, Lantlr/TokenStreamRewriteEngine;->getProgram(Ljava/lang/String;)Ljava/util/List;

    move-result-object p1

    new-instance v0, Lantlr/TokenStreamRewriteEngine$1;

    invoke-direct {v0, p0}, Lantlr/TokenStreamRewriteEngine$1;-><init>(Lantlr/TokenStreamRewriteEngine;)V

    invoke-static {p1, p2, v0}, Ljava/util/Collections;->binarySearch(Ljava/util/List;Ljava/lang/Object;Ljava/util/Comparator;)I

    move-result p0

    const/4 v0, 0x1

    if-ltz p0, :cond_5

    :goto_0
    if-ltz p0, :cond_1

    invoke-interface {p1, p0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/TokenStreamRewriteEngine$RewriteOperation;

    iget v1, v1, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    iget v2, p2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    if-ge v1, v2, :cond_0

    goto :goto_1

    :cond_0
    add-int/lit8 p0, p0, -0x1

    goto :goto_0

    :cond_1
    :goto_1
    add-int/2addr p0, v0

    instance-of v1, p2, Lantlr/TokenStreamRewriteEngine$ReplaceOp;

    if-eqz v1, :cond_6

    const/4 v1, 0x0

    move v2, p0

    :goto_2
    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v3

    if-ge v2, v3, :cond_4

    invoke-interface {p1, p0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/TokenStreamRewriteEngine$RewriteOperation;

    iget v4, v3, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    iget v5, p2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    if-eq v4, v5, :cond_2

    goto :goto_3

    :cond_2
    instance-of v3, v3, Lantlr/TokenStreamRewriteEngine$ReplaceOp;

    if-eqz v3, :cond_3

    invoke-interface {p1, p0, p2}, Ljava/util/List;->set(ILjava/lang/Object;)Ljava/lang/Object;

    goto :goto_4

    :cond_3
    add-int/lit8 v2, v2, 0x1

    goto :goto_2

    :cond_4
    :goto_3
    move v0, v1

    :goto_4
    if-nez v0, :cond_7

    invoke-interface {p1, v2, p2}, Ljava/util/List;->add(ILjava/lang/Object;)V

    goto :goto_5

    :cond_5
    neg-int p0, p0

    sub-int/2addr p0, v0

    :cond_6
    invoke-interface {p1, p0, p2}, Ljava/util/List;->add(ILjava/lang/Object;)V

    :cond_7
    :goto_5
    return-void
.end method

.method public delete(I)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p1}, Lantlr/TokenStreamRewriteEngine;->delete(Ljava/lang/String;II)V

    return-void
.end method

.method public delete(II)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2}, Lantlr/TokenStreamRewriteEngine;->delete(Ljava/lang/String;II)V

    return-void
.end method

.method public delete(Lantlr/Token;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p1}, Lantlr/TokenStreamRewriteEngine;->delete(Ljava/lang/String;Lantlr/Token;Lantlr/Token;)V

    return-void
.end method

.method public delete(Lantlr/Token;Lantlr/Token;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2}, Lantlr/TokenStreamRewriteEngine;->delete(Ljava/lang/String;Lantlr/Token;Lantlr/Token;)V

    return-void
.end method

.method public delete(Ljava/lang/String;II)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, p2, p3, v0}, Lantlr/TokenStreamRewriteEngine;->replace(Ljava/lang/String;IILjava/lang/String;)V

    return-void
.end method

.method public delete(Ljava/lang/String;Lantlr/Token;Lantlr/Token;)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, p2, p3, v0}, Lantlr/TokenStreamRewriteEngine;->replace(Ljava/lang/String;Lantlr/Token;Lantlr/Token;Ljava/lang/String;)V

    return-void
.end method

.method public deleteProgram()V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0}, Lantlr/TokenStreamRewriteEngine;->deleteProgram(Ljava/lang/String;)V

    return-void
.end method

.method public deleteProgram(Ljava/lang/String;)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, v0}, Lantlr/TokenStreamRewriteEngine;->rollback(Ljava/lang/String;I)V

    return-void
.end method

.method public discard(I)V
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->discardMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->add(I)V

    return-void
.end method

.method public getEntireText()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->stream:Lantlr/TokenStream;

    invoke-static {p0}, Lantlr/ASdebug/ASDebugStream;->getEntireText(Lantlr/TokenStream;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getLastRewriteTokenIndex()I
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0}, Lantlr/TokenStreamRewriteEngine;->getLastRewriteTokenIndex(Ljava/lang/String;)I

    move-result p0

    return p0
.end method

.method public getLastRewriteTokenIndex(Ljava/lang/String;)I
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->lastRewriteTokenIndexes:Ljava/util/Map;

    invoke-interface {p0, p1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Integer;

    if-nez p0, :cond_0

    const/4 p0, -0x1

    return p0

    :cond_0
    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    return p0
.end method

.method public getOffsetInfo(Lantlr/Token;)Lantlr/ASdebug/TokenOffsetInfo;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->stream:Lantlr/TokenStream;

    invoke-static {p0, p1}, Lantlr/ASdebug/ASDebugStream;->getOffsetInfo(Lantlr/TokenStream;Lantlr/Token;)Lantlr/ASdebug/TokenOffsetInfo;

    move-result-object p0

    return-object p0
.end method

.method public getProgram(Ljava/lang/String;)Ljava/util/List;
    .locals 1

    iget-object v0, p0, Lantlr/TokenStreamRewriteEngine;->programs:Ljava/util/Map;

    invoke-interface {v0, p1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/List;

    if-nez v0, :cond_0

    invoke-direct {p0, p1}, Lantlr/TokenStreamRewriteEngine;->initializeProgram(Ljava/lang/String;)Ljava/util/List;

    move-result-object v0

    :cond_0
    return-object v0
.end method

.method public getToken(I)Lantlr/TokenWithIndex;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->tokens:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/TokenWithIndex;

    return-object p0
.end method

.method public getTokenStreamSize()I
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->tokens:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    return p0
.end method

.method public index()I
    .locals 0

    iget p0, p0, Lantlr/TokenStreamRewriteEngine;->index:I

    return p0
.end method

.method public insertAfter(ILjava/lang/String;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2}, Lantlr/TokenStreamRewriteEngine;->insertAfter(Ljava/lang/String;ILjava/lang/String;)V

    return-void
.end method

.method public insertAfter(Lantlr/Token;Ljava/lang/String;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2}, Lantlr/TokenStreamRewriteEngine;->insertAfter(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;)V

    return-void
.end method

.method public insertAfter(Ljava/lang/String;ILjava/lang/String;)V
    .locals 0

    add-int/lit8 p2, p2, 0x1

    invoke-virtual {p0, p1, p2, p3}, Lantlr/TokenStreamRewriteEngine;->insertBefore(Ljava/lang/String;ILjava/lang/String;)V

    return-void
.end method

.method public insertAfter(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;)V
    .locals 0

    check-cast p2, Lantlr/TokenWithIndex;

    invoke-virtual {p2}, Lantlr/TokenWithIndex;->getIndex()I

    move-result p2

    invoke-virtual {p0, p1, p2, p3}, Lantlr/TokenStreamRewriteEngine;->insertAfter(Ljava/lang/String;ILjava/lang/String;)V

    return-void
.end method

.method public insertBefore(ILjava/lang/String;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2}, Lantlr/TokenStreamRewriteEngine;->insertBefore(Ljava/lang/String;ILjava/lang/String;)V

    return-void
.end method

.method public insertBefore(Lantlr/Token;Ljava/lang/String;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2}, Lantlr/TokenStreamRewriteEngine;->insertBefore(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;)V

    return-void
.end method

.method public insertBefore(Ljava/lang/String;ILjava/lang/String;)V
    .locals 1

    new-instance v0, Lantlr/TokenStreamRewriteEngine$InsertBeforeOp;

    invoke-direct {v0, p2, p3}, Lantlr/TokenStreamRewriteEngine$InsertBeforeOp;-><init>(ILjava/lang/String;)V

    invoke-virtual {p0, p1, v0}, Lantlr/TokenStreamRewriteEngine;->addToSortedRewriteList(Ljava/lang/String;Lantlr/TokenStreamRewriteEngine$RewriteOperation;)V

    return-void
.end method

.method public insertBefore(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;)V
    .locals 0

    check-cast p2, Lantlr/TokenWithIndex;

    invoke-virtual {p2}, Lantlr/TokenWithIndex;->getIndex()I

    move-result p2

    invoke-virtual {p0, p1, p2, p3}, Lantlr/TokenStreamRewriteEngine;->insertBefore(Ljava/lang/String;ILjava/lang/String;)V

    return-void
.end method

.method public nextToken()Lantlr/Token;
    .locals 3

    :cond_0
    iget-object v0, p0, Lantlr/TokenStreamRewriteEngine;->stream:Lantlr/TokenStream;

    invoke-interface {v0}, Lantlr/TokenStream;->nextToken()Lantlr/Token;

    move-result-object v0

    check-cast v0, Lantlr/TokenWithIndex;

    if-eqz v0, :cond_2

    iget v1, p0, Lantlr/TokenStreamRewriteEngine;->index:I

    invoke-virtual {v0, v1}, Lantlr/TokenWithIndex;->setIndex(I)V

    invoke-virtual {v0}, Lantlr/Token;->getType()I

    move-result v1

    const/4 v2, 0x1

    if-eq v1, v2, :cond_1

    iget-object v1, p0, Lantlr/TokenStreamRewriteEngine;->tokens:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_1
    iget v1, p0, Lantlr/TokenStreamRewriteEngine;->index:I

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/TokenStreamRewriteEngine;->index:I

    :cond_2
    if-eqz v0, :cond_3

    iget-object v1, p0, Lantlr/TokenStreamRewriteEngine;->discardMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0}, Lantlr/Token;->getType()I

    move-result v2

    invoke-virtual {v1, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-nez v1, :cond_0

    :cond_3
    return-object v0
.end method

.method public replace(IILjava/lang/String;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2, p3}, Lantlr/TokenStreamRewriteEngine;->replace(Ljava/lang/String;IILjava/lang/String;)V

    return-void
.end method

.method public replace(ILjava/lang/String;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p1, p2}, Lantlr/TokenStreamRewriteEngine;->replace(Ljava/lang/String;IILjava/lang/String;)V

    return-void
.end method

.method public replace(Lantlr/Token;Lantlr/Token;Ljava/lang/String;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2, p3}, Lantlr/TokenStreamRewriteEngine;->replace(Ljava/lang/String;Lantlr/Token;Lantlr/Token;Ljava/lang/String;)V

    return-void
.end method

.method public replace(Lantlr/Token;Ljava/lang/String;)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p1, p2}, Lantlr/TokenStreamRewriteEngine;->replace(Ljava/lang/String;Lantlr/Token;Lantlr/Token;Ljava/lang/String;)V

    return-void
.end method

.method public replace(Ljava/lang/String;IILjava/lang/String;)V
    .locals 0

    new-instance p1, Lantlr/TokenStreamRewriteEngine$ReplaceOp;

    invoke-direct {p1, p2, p3, p4}, Lantlr/TokenStreamRewriteEngine$ReplaceOp;-><init>(IILjava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/TokenStreamRewriteEngine;->addToSortedRewriteList(Lantlr/TokenStreamRewriteEngine$RewriteOperation;)V

    return-void
.end method

.method public replace(Ljava/lang/String;Lantlr/Token;Lantlr/Token;Ljava/lang/String;)V
    .locals 0

    check-cast p2, Lantlr/TokenWithIndex;

    invoke-virtual {p2}, Lantlr/TokenWithIndex;->getIndex()I

    move-result p2

    check-cast p3, Lantlr/TokenWithIndex;

    invoke-virtual {p3}, Lantlr/TokenWithIndex;->getIndex()I

    move-result p3

    invoke-virtual {p0, p1, p2, p3, p4}, Lantlr/TokenStreamRewriteEngine;->replace(Ljava/lang/String;IILjava/lang/String;)V

    return-void
.end method

.method public rollback(I)V
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1}, Lantlr/TokenStreamRewriteEngine;->rollback(Ljava/lang/String;I)V

    return-void
.end method

.method public rollback(Ljava/lang/String;I)V
    .locals 2

    iget-object v0, p0, Lantlr/TokenStreamRewriteEngine;->programs:Ljava/util/Map;

    invoke-interface {v0, p1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/List;

    if-eqz v0, :cond_0

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->programs:Ljava/util/Map;

    const/4 v1, 0x0

    invoke-interface {v0, v1, p2}, Ljava/util/List;->subList(II)Ljava/util/List;

    move-result-object p2

    invoke-interface {p0, p1, p2}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_0
    return-void
.end method

.method public setLastRewriteTokenIndex(Ljava/lang/String;I)V
    .locals 1

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->lastRewriteTokenIndexes:Ljava/util/Map;

    new-instance v0, Ljava/lang/Integer;

    invoke-direct {v0, p2}, Ljava/lang/Integer;-><init>(I)V

    invoke-interface {p0, p1, v0}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public size()I
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamRewriteEngine;->tokens:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    return p0
.end method

.method public toDebugString()Ljava/lang/String;
    .locals 2

    invoke-virtual {p0}, Lantlr/TokenStreamRewriteEngine;->getTokenStreamSize()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    const/4 v1, 0x0

    invoke-virtual {p0, v1, v0}, Lantlr/TokenStreamRewriteEngine;->toDebugString(II)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toDebugString(II)Ljava/lang/String;
    .locals 2

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    :goto_0
    if-ltz p1, :cond_0

    if-gt p1, p2, :cond_0

    iget-object v1, p0, Lantlr/TokenStreamRewriteEngine;->tokens:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge p1, v1, :cond_0

    invoke-virtual {p0, p1}, Lantlr/TokenStreamRewriteEngine;->getToken(I)Lantlr/TokenWithIndex;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/Object;)Ljava/lang/StringBuffer;

    add-int/lit8 p1, p1, 0x1

    goto :goto_0

    :cond_0
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toOriginalString()Ljava/lang/String;
    .locals 2

    invoke-virtual {p0}, Lantlr/TokenStreamRewriteEngine;->getTokenStreamSize()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    const/4 v1, 0x0

    invoke-virtual {p0, v1, v0}, Lantlr/TokenStreamRewriteEngine;->toOriginalString(II)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toOriginalString(II)Ljava/lang/String;
    .locals 2

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    :goto_0
    if-ltz p1, :cond_0

    if-gt p1, p2, :cond_0

    iget-object v1, p0, Lantlr/TokenStreamRewriteEngine;->tokens:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge p1, v1, :cond_0

    invoke-virtual {p0, p1}, Lantlr/TokenStreamRewriteEngine;->getToken(I)Lantlr/TokenWithIndex;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/CommonToken;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 p1, p1, 0x1

    goto :goto_0

    :cond_0
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    invoke-virtual {p0}, Lantlr/TokenStreamRewriteEngine;->getTokenStreamSize()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    const/4 v1, 0x0

    invoke-virtual {p0, v1, v0}, Lantlr/TokenStreamRewriteEngine;->toString(II)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toString(II)Ljava/lang/String;
    .locals 1

    const-string v0, "default"

    invoke-virtual {p0, v0, p1, p2}, Lantlr/TokenStreamRewriteEngine;->toString(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toString(Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    invoke-virtual {p0}, Lantlr/TokenStreamRewriteEngine;->getTokenStreamSize()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    const/4 v1, 0x0

    invoke-virtual {p0, p1, v1, v0}, Lantlr/TokenStreamRewriteEngine;->toString(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toString(Ljava/lang/String;II)Ljava/lang/String;
    .locals 4

    iget-object v0, p0, Lantlr/TokenStreamRewriteEngine;->programs:Ljava/util/Map;

    invoke-interface {v0, p1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Ljava/util/List;

    if-eqz p1, :cond_8

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    if-nez v0, :cond_0

    goto/16 :goto_4

    :cond_0
    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    const/4 v1, 0x0

    :cond_1
    :goto_0
    if-ltz p2, :cond_5

    if-gt p2, p3, :cond_5

    iget-object v2, p0, Lantlr/TokenStreamRewriteEngine;->tokens:Ljava/util/List;

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v2

    if-ge p2, v2, :cond_5

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v2

    if-ge v1, v2, :cond_4

    :goto_1
    invoke-interface {p1, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;

    :cond_2
    iget v3, v2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    if-ge v3, p2, :cond_3

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v3

    if-ge v1, v3, :cond_3

    add-int/lit8 v1, v1, 0x1

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v3

    if-ge v1, v3, :cond_2

    goto :goto_1

    :cond_3
    :goto_2
    iget v3, v2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    if-ne p2, v3, :cond_4

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v3

    if-ge v1, v3, :cond_4

    invoke-virtual {v2, v0}, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->execute(Ljava/lang/StringBuffer;)I

    move-result p2

    add-int/lit8 v1, v1, 0x1

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v3

    if-ge v1, v3, :cond_3

    invoke-interface {p1, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;

    goto :goto_2

    :cond_4
    if-gt p2, p3, :cond_1

    invoke-virtual {p0, p2}, Lantlr/TokenStreamRewriteEngine;->getToken(I)Lantlr/TokenWithIndex;

    move-result-object v2

    invoke-virtual {v2}, Lantlr/CommonToken;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 p2, p2, 0x1

    goto :goto_0

    :cond_5
    :goto_3
    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result p2

    if-ge v1, p2, :cond_7

    invoke-interface {p1, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;

    iget p3, p2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    invoke-virtual {p0}, Lantlr/TokenStreamRewriteEngine;->size()I

    move-result v2

    if-lt p3, v2, :cond_6

    invoke-virtual {p2, v0}, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->execute(Ljava/lang/StringBuffer;)I

    :cond_6
    add-int/lit8 v1, v1, 0x1

    goto :goto_3

    :cond_7
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_8
    :goto_4
    invoke-virtual {p0, p2, p3}, Lantlr/TokenStreamRewriteEngine;->toOriginalString(II)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
