.class public Lantlr/TreeParser;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static ASTNULL:Lantlr/ASTNULLType;


# instance fields
.field public _retTree:Lantlr/collections/AST;

.field public astFactory:Lantlr/ASTFactory;

.field public inputState:Lantlr/TreeParserSharedInputState;

.field public returnAST:Lantlr/collections/AST;

.field public tokenNames:[Ljava/lang/String;

.field public traceDepth:I


# direct methods
.method public static constructor <clinit>()V
    .locals 1

    new-instance v0, Lantlr/ASTNULLType;

    invoke-direct {v0}, Lantlr/ASTNULLType;-><init>()V

    sput-object v0, Lantlr/TreeParser;->ASTNULL:Lantlr/ASTNULLType;

    return-void
.end method

.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lantlr/ASTFactory;

    invoke-direct {v0}, Lantlr/ASTFactory;-><init>()V

    iput-object v0, p0, Lantlr/TreeParser;->astFactory:Lantlr/ASTFactory;

    const/4 v0, 0x0

    iput v0, p0, Lantlr/TreeParser;->traceDepth:I

    new-instance v0, Lantlr/TreeParserSharedInputState;

    invoke-direct {v0}, Lantlr/TreeParserSharedInputState;-><init>()V

    iput-object v0, p0, Lantlr/TreeParser;->inputState:Lantlr/TreeParserSharedInputState;

    return-void
.end method

.method public static panic()V
    .locals 2

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "TreeWalker: panic"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    const-string v0, ""

    invoke-static {v0}, Lantlr/Utils;->error(Ljava/lang/String;)V

    const/4 v0, 0x0

    throw v0
.end method


# virtual methods
.method public getAST()Lantlr/collections/AST;
    .locals 0

    iget-object p0, p0, Lantlr/TreeParser;->returnAST:Lantlr/collections/AST;

    return-object p0
.end method

.method public getASTFactory()Lantlr/ASTFactory;
    .locals 0

    iget-object p0, p0, Lantlr/TreeParser;->astFactory:Lantlr/ASTFactory;

    return-object p0
.end method

.method public getTokenName(I)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TreeParser;->tokenNames:[Ljava/lang/String;

    aget-object p0, p0, p1

    return-object p0
.end method

.method public getTokenNames()[Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TreeParser;->tokenNames:[Ljava/lang/String;

    return-object p0
.end method

.method public match(Lantlr/collections/AST;I)V
    .locals 2

    if-eqz p1, :cond_0

    sget-object v0, Lantlr/TreeParser;->ASTNULL:Lantlr/ASTNULLType;

    if-eq p1, v0, :cond_0

    invoke-interface {p1}, Lantlr/collections/AST;->getType()I

    move-result v0

    if-ne v0, p2, :cond_0

    return-void

    :cond_0
    new-instance v0, Lantlr/MismatchedTokenException;

    invoke-virtual {p0}, Lantlr/TreeParser;->getTokenNames()[Ljava/lang/String;

    move-result-object p0

    const/4 v1, 0x0

    invoke-direct {v0, p0, p1, p2, v1}, Lantlr/MismatchedTokenException;-><init>([Ljava/lang/String;Lantlr/collections/AST;IZ)V

    throw v0
.end method

.method public match(Lantlr/collections/AST;Lantlr/collections/impl/BitSet;)V
    .locals 2

    if-eqz p1, :cond_0

    sget-object v0, Lantlr/TreeParser;->ASTNULL:Lantlr/ASTNULLType;

    if-eq p1, v0, :cond_0

    invoke-interface {p1}, Lantlr/collections/AST;->getType()I

    move-result v0

    invoke-virtual {p2, v0}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v0

    if-eqz v0, :cond_0

    return-void

    :cond_0
    new-instance v0, Lantlr/MismatchedTokenException;

    invoke-virtual {p0}, Lantlr/TreeParser;->getTokenNames()[Ljava/lang/String;

    move-result-object p0

    const/4 v1, 0x0

    invoke-direct {v0, p0, p1, p2, v1}, Lantlr/MismatchedTokenException;-><init>([Ljava/lang/String;Lantlr/collections/AST;Lantlr/collections/impl/BitSet;Z)V

    throw v0
.end method

.method public matchNot(Lantlr/collections/AST;I)V
    .locals 2

    if-eqz p1, :cond_0

    sget-object v0, Lantlr/TreeParser;->ASTNULL:Lantlr/ASTNULLType;

    if-eq p1, v0, :cond_0

    invoke-interface {p1}, Lantlr/collections/AST;->getType()I

    move-result v0

    if-eq v0, p2, :cond_0

    return-void

    :cond_0
    new-instance v0, Lantlr/MismatchedTokenException;

    invoke-virtual {p0}, Lantlr/TreeParser;->getTokenNames()[Ljava/lang/String;

    move-result-object p0

    const/4 v1, 0x1

    invoke-direct {v0, p0, p1, p2, v1}, Lantlr/MismatchedTokenException;-><init>([Ljava/lang/String;Lantlr/collections/AST;IZ)V

    throw v0
.end method

.method public reportError(Lantlr/RecognitionException;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    invoke-virtual {p1}, Lantlr/RecognitionException;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public reportError(Ljava/lang/String;)V
    .locals 2

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "error: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public reportWarning(Ljava/lang/String;)V
    .locals 2

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "warning: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public setASTFactory(Lantlr/ASTFactory;)V
    .locals 0

    iput-object p1, p0, Lantlr/TreeParser;->astFactory:Lantlr/ASTFactory;

    return-void
.end method

.method public setASTNodeClass(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lantlr/TreeParser;->astFactory:Lantlr/ASTFactory;

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->setASTNodeType(Ljava/lang/String;)V

    return-void
.end method

.method public setASTNodeType(Ljava/lang/String;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/TreeParser;->setASTNodeClass(Ljava/lang/String;)V

    return-void
.end method

.method public traceIn(Ljava/lang/String;Lantlr/collections/AST;)V
    .locals 3

    iget v0, p0, Lantlr/TreeParser;->traceDepth:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/TreeParser;->traceDepth:I

    invoke-virtual {p0}, Lantlr/TreeParser;->traceIndent()V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "> "

    const-string v2, "("

    invoke-static {v1, p1, v2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    if-eqz p2, :cond_0

    invoke-interface {p2}, Lantlr/collections/AST;->toString()Ljava/lang/String;

    move-result-object p2

    goto :goto_0

    :cond_0
    const-string p2, "null"

    :goto_0
    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ")"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lantlr/TreeParser;->inputState:Lantlr/TreeParserSharedInputState;

    iget p0, p0, Lantlr/TreeParserSharedInputState;->guessing:I

    if-lez p0, :cond_1

    const-string p0, " [guessing]"

    goto :goto_1

    :cond_1
    const-string p0, ""

    :goto_1
    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public traceIndent()V
    .locals 3

    const/4 v0, 0x0

    :goto_0
    iget v1, p0, Lantlr/TreeParser;->traceDepth:I

    if-ge v0, v1, :cond_0

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, " "

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public traceOut(Ljava/lang/String;Lantlr/collections/AST;)V
    .locals 3

    invoke-virtual {p0}, Lantlr/TreeParser;->traceIndent()V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "< "

    const-string v2, "("

    invoke-static {v1, p1, v2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    if-eqz p2, :cond_0

    invoke-interface {p2}, Lantlr/collections/AST;->toString()Ljava/lang/String;

    move-result-object p2

    goto :goto_0

    :cond_0
    const-string p2, "null"

    :goto_0
    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ")"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p2, p0, Lantlr/TreeParser;->inputState:Lantlr/TreeParserSharedInputState;

    iget p2, p2, Lantlr/TreeParserSharedInputState;->guessing:I

    if-lez p2, :cond_1

    const-string p2, " [guessing]"

    goto :goto_1

    :cond_1
    const-string p2, ""

    :goto_1
    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/TreeParser;->traceDepth:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/TreeParser;->traceDepth:I

    return-void
.end method
