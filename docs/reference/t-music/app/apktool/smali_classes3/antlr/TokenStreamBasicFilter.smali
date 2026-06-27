.class public Lantlr/TokenStreamBasicFilter;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/TokenStream;
.implements Lantlr/ASdebug/IASDebugStream;


# instance fields
.field public discardMask:Lantlr/collections/impl/BitSet;

.field public input:Lantlr/TokenStream;


# direct methods
.method public constructor <init>(Lantlr/TokenStream;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/TokenStreamBasicFilter;->input:Lantlr/TokenStream;

    new-instance p1, Lantlr/collections/impl/BitSet;

    invoke-direct {p1}, Lantlr/collections/impl/BitSet;-><init>()V

    iput-object p1, p0, Lantlr/TokenStreamBasicFilter;->discardMask:Lantlr/collections/impl/BitSet;

    return-void
.end method


# virtual methods
.method public discard(I)V
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamBasicFilter;->discardMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->add(I)V

    return-void
.end method

.method public discard(Lantlr/collections/impl/BitSet;)V
    .locals 0

    iput-object p1, p0, Lantlr/TokenStreamBasicFilter;->discardMask:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public getEntireText()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamBasicFilter;->input:Lantlr/TokenStream;

    invoke-static {p0}, Lantlr/ASdebug/ASDebugStream;->getEntireText(Lantlr/TokenStream;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getOffsetInfo(Lantlr/Token;)Lantlr/ASdebug/TokenOffsetInfo;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamBasicFilter;->input:Lantlr/TokenStream;

    invoke-static {p0, p1}, Lantlr/ASdebug/ASDebugStream;->getOffsetInfo(Lantlr/TokenStream;Lantlr/Token;)Lantlr/ASdebug/TokenOffsetInfo;

    move-result-object p0

    return-object p0
.end method

.method public nextToken()Lantlr/Token;
    .locals 3

    :goto_0
    iget-object v0, p0, Lantlr/TokenStreamBasicFilter;->input:Lantlr/TokenStream;

    invoke-interface {v0}, Lantlr/TokenStream;->nextToken()Lantlr/Token;

    move-result-object v0

    if-eqz v0, :cond_0

    iget-object v1, p0, Lantlr/TokenStreamBasicFilter;->discardMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0}, Lantlr/Token;->getType()I

    move-result v2

    invoke-virtual {v1, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_0

    :cond_0
    return-object v0
.end method
