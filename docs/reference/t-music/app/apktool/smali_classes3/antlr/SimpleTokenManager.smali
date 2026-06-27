.class public Lantlr/SimpleTokenManager;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/TokenManager;
.implements Ljava/lang/Cloneable;


# instance fields
.field public antlrTool:Lantlr/Tool;

.field public maxToken:I

.field public name:Ljava/lang/String;

.field public readOnly:Z

.field public table:Ljava/util/Hashtable;

.field public vocabulary:Lantlr/collections/impl/Vector;


# direct methods
.method public constructor <init>(Ljava/lang/String;Lantlr/Tool;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x4

    iput v0, p0, Lantlr/SimpleTokenManager;->maxToken:I

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/SimpleTokenManager;->readOnly:Z

    iput-object p2, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    iput-object p1, p0, Lantlr/SimpleTokenManager;->name:Ljava/lang/String;

    new-instance p1, Lantlr/collections/impl/Vector;

    const/4 p2, 0x1

    invoke-direct {p1, p2}, Lantlr/collections/impl/Vector;-><init>(I)V

    iput-object p1, p0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    new-instance p1, Ljava/util/Hashtable;

    invoke-direct {p1}, Ljava/util/Hashtable;-><init>()V

    iput-object p1, p0, Lantlr/SimpleTokenManager;->table:Ljava/util/Hashtable;

    new-instance p1, Lantlr/TokenSymbol;

    const-string v0, "EOF"

    invoke-direct {p1, v0}, Lantlr/TokenSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {p1, p2}, Lantlr/TokenSymbol;->setTokenType(I)V

    invoke-virtual {p0, p1}, Lantlr/SimpleTokenManager;->define(Lantlr/TokenSymbol;)V

    iget-object p1, p0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    const/4 p2, 0x3

    invoke-virtual {p1, p2}, Lantlr/collections/impl/Vector;->ensureCapacity(I)V

    iget-object p0, p0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    const-string p1, "NULL_TREE_LOOKAHEAD"

    invoke-virtual {p0, p1, p2}, Lantlr/collections/impl/Vector;->setElementAt(Ljava/lang/Object;I)V

    return-void
.end method


# virtual methods
.method public clone()Ljava/lang/Object;
    .locals 2

    :try_start_0
    invoke-super {p0}, Ljava/lang/Object;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/SimpleTokenManager;

    iget-object v1, p0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->clone()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/collections/impl/Vector;

    iput-object v1, v0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    iget-object v1, p0, Lantlr/SimpleTokenManager;->table:Ljava/util/Hashtable;

    invoke-virtual {v1}, Ljava/util/Hashtable;->clone()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/util/Hashtable;

    iput-object v1, v0, Lantlr/SimpleTokenManager;->table:Ljava/util/Hashtable;

    iget v1, p0, Lantlr/SimpleTokenManager;->maxToken:I

    iput v1, v0, Lantlr/SimpleTokenManager;->maxToken:I

    iget-object v1, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    iput-object v1, v0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/SimpleTokenManager;->name:Ljava/lang/String;

    iput-object v1, v0, Lantlr/SimpleTokenManager;->name:Ljava/lang/String;
    :try_end_0
    .catch Ljava/lang/CloneNotSupportedException; {:try_start_0 .. :try_end_0} :catch_0

    return-object v0

    :catch_0
    iget-object p0, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    const-string v0, "cannot clone token manager"

    invoke-virtual {p0, v0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    const/4 p0, 0x0

    return-object p0
.end method

.method public define(Lantlr/TokenSymbol;)V
    .locals 3

    iget-object v0, p0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v1

    invoke-virtual {v0, v1}, Lantlr/collections/impl/Vector;->ensureCapacity(I)V

    iget-object v0, p0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v2

    invoke-virtual {v0, v1, v2}, Lantlr/collections/impl/Vector;->setElementAt(Ljava/lang/Object;I)V

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0, p1}, Lantlr/SimpleTokenManager;->mapToTokenSymbol(Ljava/lang/String;Lantlr/TokenSymbol;)V

    return-void
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/SimpleTokenManager;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getTokenStringAt(I)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/String;

    return-object p0
.end method

.method public getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;
    .locals 0

    iget-object p0, p0, Lantlr/SimpleTokenManager;->table:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/TokenSymbol;

    return-object p0
.end method

.method public getTokenSymbolAt(I)Lantlr/TokenSymbol;
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/SimpleTokenManager;->getTokenStringAt(I)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/SimpleTokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p0

    return-object p0
.end method

.method public getTokenSymbolElements()Ljava/util/Enumeration;
    .locals 0

    iget-object p0, p0, Lantlr/SimpleTokenManager;->table:Ljava/util/Hashtable;

    invoke-virtual {p0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object p0

    return-object p0
.end method

.method public getTokenSymbolKeys()Ljava/util/Enumeration;
    .locals 0

    iget-object p0, p0, Lantlr/SimpleTokenManager;->table:Ljava/util/Hashtable;

    invoke-virtual {p0}, Ljava/util/Hashtable;->keys()Ljava/util/Enumeration;

    move-result-object p0

    return-object p0
.end method

.method public getVocabulary()Lantlr/collections/impl/Vector;
    .locals 0

    iget-object p0, p0, Lantlr/SimpleTokenManager;->vocabulary:Lantlr/collections/impl/Vector;

    return-object p0
.end method

.method public isReadOnly()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public mapToTokenSymbol(Ljava/lang/String;Lantlr/TokenSymbol;)V
    .locals 0

    iget-object p0, p0, Lantlr/SimpleTokenManager;->table:Ljava/util/Hashtable;

    invoke-virtual {p0, p1, p2}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public maxTokenType()I
    .locals 0

    iget p0, p0, Lantlr/SimpleTokenManager;->maxToken:I

    add-int/lit8 p0, p0, -0x1

    return p0
.end method

.method public nextTokenType()I
    .locals 2

    iget v0, p0, Lantlr/SimpleTokenManager;->maxToken:I

    add-int/lit8 v1, v0, 0x1

    iput v1, p0, Lantlr/SimpleTokenManager;->maxToken:I

    return v0
.end method

.method public setName(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/SimpleTokenManager;->name:Ljava/lang/String;

    return-void
.end method

.method public setReadOnly(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/SimpleTokenManager;->readOnly:Z

    return-void
.end method

.method public tokenDefined(Ljava/lang/String;)Z
    .locals 0

    iget-object p0, p0, Lantlr/SimpleTokenManager;->table:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method
