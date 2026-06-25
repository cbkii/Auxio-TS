.class public Lantlr/TokenStreamHiddenTokenFilter;
.super Lantlr/TokenStreamBasicFilter;
.source ""

# interfaces
.implements Lantlr/TokenStream;


# instance fields
.field public firstHidden:Lantlr/CommonHiddenStreamToken;

.field public hideMask:Lantlr/collections/impl/BitSet;

.field public lastHiddenToken:Lantlr/CommonHiddenStreamToken;

.field public nextMonitoredToken:Lantlr/CommonHiddenStreamToken;


# direct methods
.method public constructor <init>(Lantlr/TokenStream;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/TokenStreamBasicFilter;-><init>(Lantlr/TokenStream;)V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/TokenStreamHiddenTokenFilter;->firstHidden:Lantlr/CommonHiddenStreamToken;

    new-instance p1, Lantlr/collections/impl/BitSet;

    invoke-direct {p1}, Lantlr/collections/impl/BitSet;-><init>()V

    iput-object p1, p0, Lantlr/TokenStreamHiddenTokenFilter;->hideMask:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method private consumeFirst()V
    .locals 4

    invoke-virtual {p0}, Lantlr/TokenStreamHiddenTokenFilter;->consume()V

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lantlr/TokenStreamHiddenTokenFilter;->hideMask:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v3

    invoke-virtual {v3}, Lantlr/Token;->getType()I

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-nez v1, :cond_1

    iget-object v1, p0, Lantlr/TokenStreamBasicFilter;->discardMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v3

    invoke-virtual {v3}, Lantlr/Token;->getType()I

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_1

    :cond_0
    return-void

    :cond_1
    :goto_1
    iget-object v1, p0, Lantlr/TokenStreamHiddenTokenFilter;->hideMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v3

    invoke-virtual {v3}, Lantlr/Token;->getType()I

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_3

    if-nez v0, :cond_2

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CommonHiddenStreamToken;->setHiddenAfter(Lantlr/CommonHiddenStreamToken;)V

    invoke-virtual {p0, v2}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v1

    invoke-virtual {v1, v0}, Lantlr/CommonHiddenStreamToken;->setHiddenBefore(Lantlr/CommonHiddenStreamToken;)V

    :goto_2
    invoke-virtual {p0, v2}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v0

    iput-object v0, p0, Lantlr/TokenStreamHiddenTokenFilter;->lastHiddenToken:Lantlr/CommonHiddenStreamToken;

    iget-object v1, p0, Lantlr/TokenStreamHiddenTokenFilter;->firstHidden:Lantlr/CommonHiddenStreamToken;

    if-nez v1, :cond_3

    iput-object v0, p0, Lantlr/TokenStreamHiddenTokenFilter;->firstHidden:Lantlr/CommonHiddenStreamToken;

    :cond_3
    invoke-virtual {p0}, Lantlr/TokenStreamHiddenTokenFilter;->consume()V

    goto :goto_0
.end method


# virtual methods
.method public LA(I)Lantlr/CommonHiddenStreamToken;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamHiddenTokenFilter;->nextMonitoredToken:Lantlr/CommonHiddenStreamToken;

    return-object p0
.end method

.method public consume()V
    .locals 1

    iget-object v0, p0, Lantlr/TokenStreamBasicFilter;->input:Lantlr/TokenStream;

    invoke-interface {v0}, Lantlr/TokenStream;->nextToken()Lantlr/Token;

    move-result-object v0

    check-cast v0, Lantlr/CommonHiddenStreamToken;

    iput-object v0, p0, Lantlr/TokenStreamHiddenTokenFilter;->nextMonitoredToken:Lantlr/CommonHiddenStreamToken;

    return-void
.end method

.method public getDiscardMask()Lantlr/collections/impl/BitSet;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamBasicFilter;->discardMask:Lantlr/collections/impl/BitSet;

    return-object p0
.end method

.method public getHiddenAfter(Lantlr/CommonHiddenStreamToken;)Lantlr/CommonHiddenStreamToken;
    .locals 0

    invoke-virtual {p1}, Lantlr/CommonHiddenStreamToken;->getHiddenAfter()Lantlr/CommonHiddenStreamToken;

    move-result-object p0

    return-object p0
.end method

.method public getHiddenBefore(Lantlr/CommonHiddenStreamToken;)Lantlr/CommonHiddenStreamToken;
    .locals 0

    invoke-virtual {p1}, Lantlr/CommonHiddenStreamToken;->getHiddenBefore()Lantlr/CommonHiddenStreamToken;

    move-result-object p0

    return-object p0
.end method

.method public getHideMask()Lantlr/collections/impl/BitSet;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamHiddenTokenFilter;->hideMask:Lantlr/collections/impl/BitSet;

    return-object p0
.end method

.method public getInitialHiddenToken()Lantlr/CommonHiddenStreamToken;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamHiddenTokenFilter;->firstHidden:Lantlr/CommonHiddenStreamToken;

    return-object p0
.end method

.method public hide(I)V
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamHiddenTokenFilter;->hideMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->add(I)V

    return-void
.end method

.method public hide(Lantlr/collections/impl/BitSet;)V
    .locals 0

    iput-object p1, p0, Lantlr/TokenStreamHiddenTokenFilter;->hideMask:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public nextToken()Lantlr/Token;
    .locals 5

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v1

    if-nez v1, :cond_0

    invoke-direct {p0}, Lantlr/TokenStreamHiddenTokenFilter;->consumeFirst()V

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v1

    iget-object v2, p0, Lantlr/TokenStreamHiddenTokenFilter;->lastHiddenToken:Lantlr/CommonHiddenStreamToken;

    invoke-virtual {v1, v2}, Lantlr/CommonHiddenStreamToken;->setHiddenBefore(Lantlr/CommonHiddenStreamToken;)V

    const/4 v2, 0x0

    iput-object v2, p0, Lantlr/TokenStreamHiddenTokenFilter;->lastHiddenToken:Lantlr/CommonHiddenStreamToken;

    invoke-virtual {p0}, Lantlr/TokenStreamHiddenTokenFilter;->consume()V

    move-object v2, v1

    :goto_0
    iget-object v3, p0, Lantlr/TokenStreamHiddenTokenFilter;->hideMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v0}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v4

    invoke-virtual {v4}, Lantlr/Token;->getType()I

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-nez v3, :cond_2

    iget-object v3, p0, Lantlr/TokenStreamBasicFilter;->discardMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v0}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v4

    invoke-virtual {v4}, Lantlr/Token;->getType()I

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_1

    goto :goto_1

    :cond_1
    return-object v1

    :cond_2
    :goto_1
    iget-object v3, p0, Lantlr/TokenStreamHiddenTokenFilter;->hideMask:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v0}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v4

    invoke-virtual {v4}, Lantlr/Token;->getType()I

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_4

    invoke-virtual {p0, v0}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v3

    invoke-virtual {v2, v3}, Lantlr/CommonHiddenStreamToken;->setHiddenAfter(Lantlr/CommonHiddenStreamToken;)V

    if-eq v2, v1, :cond_3

    invoke-virtual {p0, v0}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v3

    invoke-virtual {v3, v2}, Lantlr/CommonHiddenStreamToken;->setHiddenBefore(Lantlr/CommonHiddenStreamToken;)V

    :cond_3
    invoke-virtual {p0, v0}, Lantlr/TokenStreamHiddenTokenFilter;->LA(I)Lantlr/CommonHiddenStreamToken;

    move-result-object v2

    iput-object v2, p0, Lantlr/TokenStreamHiddenTokenFilter;->lastHiddenToken:Lantlr/CommonHiddenStreamToken;

    :cond_4
    invoke-virtual {p0}, Lantlr/TokenStreamHiddenTokenFilter;->consume()V

    goto :goto_0
.end method
