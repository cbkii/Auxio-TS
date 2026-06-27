.class public Lantlr/ASTFactory;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public theASTNodeType:Ljava/lang/String;

.field public theASTNodeTypeClass:Ljava/lang/Class;

.field public tokenTypeToASTClassMap:Ljava/util/Hashtable;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/ASTFactory;->theASTNodeType:Ljava/lang/String;

    iput-object v0, p0, Lantlr/ASTFactory;->theASTNodeTypeClass:Ljava/lang/Class;

    iput-object v0, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    return-void
.end method

.method public constructor <init>(Ljava/util/Hashtable;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/ASTFactory;->theASTNodeType:Ljava/lang/String;

    iput-object v0, p0, Lantlr/ASTFactory;->theASTNodeTypeClass:Ljava/lang/Class;

    iput-object v0, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->setTokenTypeToASTClassMap(Ljava/util/Hashtable;)V

    return-void
.end method


# virtual methods
.method public addASTChild(Lantlr/ASTPair;Lantlr/collections/AST;)V
    .locals 1

    if-eqz p2, :cond_2

    iget-object p0, p1, Lantlr/ASTPair;->root:Lantlr/collections/AST;

    if-nez p0, :cond_0

    iput-object p2, p1, Lantlr/ASTPair;->root:Lantlr/collections/AST;

    goto :goto_0

    :cond_0
    iget-object v0, p1, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    if-nez v0, :cond_1

    invoke-interface {p0, p2}, Lantlr/collections/AST;->setFirstChild(Lantlr/collections/AST;)V

    goto :goto_0

    :cond_1
    invoke-interface {v0, p2}, Lantlr/collections/AST;->setNextSibling(Lantlr/collections/AST;)V

    :goto_0
    iput-object p2, p1, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    invoke-virtual {p1}, Lantlr/ASTPair;->advanceChildToEnd()V

    :cond_2
    return-void
.end method

.method public create()Lantlr/collections/AST;
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lantlr/ASTFactory;->create(I)Lantlr/collections/AST;

    move-result-object p0

    return-object p0
.end method

.method public create(I)Lantlr/collections/AST;
    .locals 1

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->getASTNodeType(I)Ljava/lang/Class;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/ASTFactory;->create(Ljava/lang/Class;)Lantlr/collections/AST;

    move-result-object p0

    if-eqz p0, :cond_0

    const-string v0, ""

    invoke-interface {p0, p1, v0}, Lantlr/collections/AST;->initialize(ILjava/lang/String;)V

    :cond_0
    return-object p0
.end method

.method public create(ILjava/lang/String;)Lantlr/collections/AST;
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->create(I)Lantlr/collections/AST;

    move-result-object p0

    if-eqz p0, :cond_0

    invoke-interface {p0, p1, p2}, Lantlr/collections/AST;->initialize(ILjava/lang/String;)V

    :cond_0
    return-object p0
.end method

.method public create(ILjava/lang/String;Ljava/lang/String;)Lantlr/collections/AST;
    .locals 0

    invoke-virtual {p0, p3}, Lantlr/ASTFactory;->create(Ljava/lang/String;)Lantlr/collections/AST;

    move-result-object p0

    if-eqz p0, :cond_0

    invoke-interface {p0, p1, p2}, Lantlr/collections/AST;->initialize(ILjava/lang/String;)V

    :cond_0
    return-object p0
.end method

.method public create(Lantlr/Token;)Lantlr/collections/AST;
    .locals 1

    invoke-virtual {p1}, Lantlr/Token;->getType()I

    move-result v0

    invoke-virtual {p0, v0}, Lantlr/ASTFactory;->create(I)Lantlr/collections/AST;

    move-result-object p0

    if-eqz p0, :cond_0

    invoke-interface {p0, p1}, Lantlr/collections/AST;->initialize(Lantlr/Token;)V

    :cond_0
    return-object p0
.end method

.method public create(Lantlr/Token;Ljava/lang/String;)Lantlr/collections/AST;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lantlr/ASTFactory;->createUsingCtor(Lantlr/Token;Ljava/lang/String;)Lantlr/collections/AST;

    move-result-object p0

    return-object p0
.end method

.method public create(Lantlr/collections/AST;)Lantlr/collections/AST;
    .locals 1

    if-nez p1, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    invoke-interface {p1}, Lantlr/collections/AST;->getType()I

    move-result v0

    invoke-virtual {p0, v0}, Lantlr/ASTFactory;->create(I)Lantlr/collections/AST;

    move-result-object p0

    if-eqz p0, :cond_1

    invoke-interface {p0, p1}, Lantlr/collections/AST;->initialize(Lantlr/collections/AST;)V

    :cond_1
    return-object p0
.end method

.method public create(Ljava/lang/Class;)Lantlr/collections/AST;
    .locals 1

    :try_start_0
    invoke-virtual {p1}, Ljava/lang/Class;->newInstance()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/collections/AST;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    return-object v0

    :catch_0
    const-string v0, "Can\'t create AST Node "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->error(Ljava/lang/String;)V

    const/4 p0, 0x0

    return-object p0
.end method

.method public create(Ljava/lang/String;)Lantlr/collections/AST;
    .locals 1

    :try_start_0
    invoke-static {p1}, Lantlr/Utils;->loadClass(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object p1
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->create(Ljava/lang/Class;)Lantlr/collections/AST;

    move-result-object p0

    return-object p0

    :catch_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string v0, "Invalid class, "

    invoke-static {v0, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public createUsingCtor(Lantlr/Token;Ljava/lang/String;)Lantlr/collections/AST;
    .locals 5

    :try_start_0
    invoke-static {p2}, Lantlr/Utils;->loadClass(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v1, 0x1

    new-array v2, v1, [Ljava/lang/Class;

    const-class v3, Lantlr/Token;

    const/4 v4, 0x0

    aput-object v3, v2, v4
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_1

    :try_start_1
    invoke-virtual {v0, v2}, Ljava/lang/Class;->getConstructor([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;

    move-result-object v2

    new-array v1, v1, [Ljava/lang/Object;

    aput-object p1, v1, v4

    invoke-virtual {v2, v1}, Ljava/lang/reflect/Constructor;->newInstance([Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/collections/AST;
    :try_end_1
    .catch Ljava/lang/NoSuchMethodException; {:try_start_1 .. :try_end_1} :catch_0
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    goto :goto_0

    :catch_0
    :try_start_2
    invoke-virtual {p0, v0}, Lantlr/ASTFactory;->create(Ljava/lang/Class;)Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_0

    invoke-interface {v1, p1}, Lantlr/collections/AST;->initialize(Lantlr/Token;)V
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_1

    :cond_0
    :goto_0
    return-object v1

    :catch_1
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "Invalid class or can\'t make instance, "

    invoke-static {p1, p2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public dup(Lantlr/collections/AST;)Lantlr/collections/AST;
    .locals 1

    if-nez p1, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    invoke-virtual {p1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/ASTFactory;->create(Ljava/lang/Class;)Lantlr/collections/AST;

    move-result-object p0

    invoke-interface {p0, p1}, Lantlr/collections/AST;->initialize(Lantlr/collections/AST;)V

    return-object p0
.end method

.method public dupList(Lantlr/collections/AST;)Lantlr/collections/AST;
    .locals 3

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->dupTree(Lantlr/collections/AST;)Lantlr/collections/AST;

    move-result-object v0

    move-object v1, v0

    :goto_0
    if-eqz p1, :cond_0

    invoke-interface {p1}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->dupTree(Lantlr/collections/AST;)Lantlr/collections/AST;

    move-result-object v2

    invoke-interface {v1, v2}, Lantlr/collections/AST;->setNextSibling(Lantlr/collections/AST;)V

    invoke-interface {v1}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object v1

    goto :goto_0

    :cond_0
    return-object v0
.end method

.method public dupTree(Lantlr/collections/AST;)Lantlr/collections/AST;
    .locals 1

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->dup(Lantlr/collections/AST;)Lantlr/collections/AST;

    move-result-object v0

    if-eqz p1, :cond_0

    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->dupList(Lantlr/collections/AST;)Lantlr/collections/AST;

    move-result-object p0

    invoke-interface {v0, p0}, Lantlr/collections/AST;->setFirstChild(Lantlr/collections/AST;)V

    :cond_0
    return-object v0
.end method

.method public error(Ljava/lang/String;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public getASTNodeType(I)Ljava/lang/Class;
    .locals 2

    iget-object v0, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    if-eqz v0, :cond_0

    new-instance v1, Ljava/lang/Integer;

    invoke-direct {v1, p1}, Ljava/lang/Integer;-><init>(I)V

    invoke-virtual {v0, v1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Ljava/lang/Class;

    if-eqz p1, :cond_0

    return-object p1

    :cond_0
    iget-object p0, p0, Lantlr/ASTFactory;->theASTNodeTypeClass:Ljava/lang/Class;

    if-eqz p0, :cond_1

    return-object p0

    :cond_1
    const-class p0, Lantlr/CommonAST;

    return-object p0
.end method

.method public getTokenTypeToASTClassMap()Ljava/util/Hashtable;
    .locals 0

    iget-object p0, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    return-object p0
.end method

.method public make(Lantlr/collections/impl/ASTArray;)Lantlr/collections/AST;
    .locals 0

    iget-object p1, p1, Lantlr/collections/impl/ASTArray;->array:[Lantlr/collections/AST;

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->make([Lantlr/collections/AST;)Lantlr/collections/AST;

    move-result-object p0

    return-object p0
.end method

.method public make([Lantlr/collections/AST;)Lantlr/collections/AST;
    .locals 3

    const/4 p0, 0x0

    if-eqz p1, :cond_7

    array-length v0, p1

    if-nez v0, :cond_0

    goto :goto_4

    :cond_0
    const/4 v0, 0x0

    aget-object v0, p1, v0

    if-eqz v0, :cond_1

    invoke-interface {v0, p0}, Lantlr/collections/AST;->setFirstChild(Lantlr/collections/AST;)V

    :cond_1
    const/4 v1, 0x1

    :goto_0
    array-length v2, p1

    if-ge v1, v2, :cond_6

    aget-object v2, p1, v1

    if-nez v2, :cond_2

    goto :goto_3

    :cond_2
    if-nez v0, :cond_3

    aget-object v0, p1, v1

    move-object p0, v0

    goto :goto_2

    :cond_3
    if-nez p0, :cond_4

    aget-object p0, p1, v1

    invoke-interface {v0, p0}, Lantlr/collections/AST;->setFirstChild(Lantlr/collections/AST;)V

    invoke-interface {v0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p0

    goto :goto_2

    :cond_4
    aget-object v2, p1, v1

    invoke-interface {p0, v2}, Lantlr/collections/AST;->setNextSibling(Lantlr/collections/AST;)V

    :goto_1
    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p0

    :goto_2
    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object v2

    if-eqz v2, :cond_5

    goto :goto_1

    :cond_5
    :goto_3
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_6
    return-object v0

    :cond_7
    :goto_4
    return-object p0
.end method

.method public makeASTRoot(Lantlr/ASTPair;Lantlr/collections/AST;)V
    .locals 0

    if-eqz p2, :cond_0

    iget-object p0, p1, Lantlr/ASTPair;->root:Lantlr/collections/AST;

    invoke-interface {p2, p0}, Lantlr/collections/AST;->addChild(Lantlr/collections/AST;)V

    iget-object p0, p1, Lantlr/ASTPair;->root:Lantlr/collections/AST;

    iput-object p0, p1, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    invoke-virtual {p1}, Lantlr/ASTPair;->advanceChildToEnd()V

    iput-object p2, p1, Lantlr/ASTPair;->root:Lantlr/collections/AST;

    :cond_0
    return-void
.end method

.method public setASTNodeClass(Ljava/lang/Class;)V
    .locals 0

    if-eqz p1, :cond_0

    iput-object p1, p0, Lantlr/ASTFactory;->theASTNodeTypeClass:Ljava/lang/Class;

    invoke-virtual {p1}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/ASTFactory;->theASTNodeType:Ljava/lang/String;

    :cond_0
    return-void
.end method

.method public setASTNodeClass(Ljava/lang/String;)V
    .locals 2

    iput-object p1, p0, Lantlr/ASTFactory;->theASTNodeType:Ljava/lang/String;

    :try_start_0
    invoke-static {p1}, Lantlr/Utils;->loadClass(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    iput-object v0, p0, Lantlr/ASTFactory;->theASTNodeTypeClass:Ljava/lang/Class;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Can\'t find/access AST Node type"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->error(Ljava/lang/String;)V

    :goto_0
    return-void
.end method

.method public setASTNodeType(Ljava/lang/String;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->setASTNodeClass(Ljava/lang/String;)V

    return-void
.end method

.method public setTokenTypeASTNodeType(ILjava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    :cond_0
    if-nez p2, :cond_1

    iget-object p0, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    new-instance p2, Ljava/lang/Integer;

    invoke-direct {p2, p1}, Ljava/lang/Integer;-><init>(I)V

    invoke-virtual {p0, p2}, Ljava/util/Hashtable;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    return-void

    :cond_1
    :try_start_0
    invoke-static {p2}, Lantlr/Utils;->loadClass(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    iget-object p0, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    new-instance v1, Ljava/lang/Integer;

    invoke-direct {v1, p1}, Ljava/lang/Integer;-><init>(I)V

    invoke-virtual {p0, v1, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    return-void

    :catch_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "Invalid class, "

    invoke-static {p1, p2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public setTokenTypeToASTClassMap(Ljava/util/Hashtable;)V
    .locals 0

    iput-object p1, p0, Lantlr/ASTFactory;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    return-void
.end method
