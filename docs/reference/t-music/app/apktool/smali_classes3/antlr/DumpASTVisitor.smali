.class public Lantlr/DumpASTVisitor;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/ASTVisitor;


# instance fields
.field public level:I


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/DumpASTVisitor;->level:I

    return-void
.end method

.method private tabs()V
    .locals 3

    const/4 v0, 0x0

    :goto_0
    iget v1, p0, Lantlr/DumpASTVisitor;->level:I

    if-ge v0, v1, :cond_0

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, "   "

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method


# virtual methods
.method public visit(Lantlr/collections/AST;)V
    .locals 3

    move-object v0, p1

    :goto_0
    if-eqz v0, :cond_1

    invoke-interface {v0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_0

    goto :goto_1

    :cond_0
    invoke-interface {v0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object v0

    goto :goto_0

    :cond_1
    :goto_1
    if-eqz p1, :cond_4

    invoke-direct {p0}, Lantlr/DumpASTVisitor;->tabs()V

    invoke-interface {p1}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_2

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "nil"

    goto :goto_2

    :cond_2
    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-interface {p1}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v1

    :goto_2
    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, " ["

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-interface {p1}, Lantlr/collections/AST;->getType()I

    move-result v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, "] "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, ""

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v0

    if-eqz v0, :cond_3

    iget v0, p0, Lantlr/DumpASTVisitor;->level:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/DumpASTVisitor;->level:I

    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/DumpASTVisitor;->visit(Lantlr/collections/AST;)V

    iget v0, p0, Lantlr/DumpASTVisitor;->level:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/DumpASTVisitor;->level:I

    :cond_3
    invoke-interface {p1}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p1

    goto :goto_1

    :cond_4
    return-void
.end method
