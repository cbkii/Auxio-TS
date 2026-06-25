.class public abstract Lantlr/GrammarAtom;
.super Lantlr/AlternativeElement;
.source ""


# instance fields
.field public ASTNodeType:Ljava/lang/String;

.field public atomText:Ljava/lang/String;

.field public label:Ljava/lang/String;

.field public not:Z

.field public tokenType:I


# direct methods
.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;I)V
    .locals 0

    invoke-direct {p0, p1, p2, p3}, Lantlr/AlternativeElement;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    const/4 p1, 0x0

    iput p1, p0, Lantlr/GrammarAtom;->tokenType:I

    iput-boolean p1, p0, Lantlr/GrammarAtom;->not:Z

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/GrammarAtom;->ASTNodeType:Ljava/lang/String;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public getASTNodeType()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/GrammarAtom;->ASTNodeType:Ljava/lang/String;

    return-object p0
.end method

.method public getLabel()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    return-object p0
.end method

.method public getText()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    return-object p0
.end method

.method public getType()I
    .locals 0

    iget p0, p0, Lantlr/GrammarAtom;->tokenType:I

    return p0
.end method

.method public setASTNodeType(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/GrammarAtom;->ASTNodeType:Ljava/lang/String;

    return-void
.end method

.method public setLabel(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    return-void
.end method

.method public setOption(Lantlr/Token;Lantlr/Token;)V
    .locals 2

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "AST"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/GrammarAtom;->setASTNodeType(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    iget-object p2, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v0, "Invalid element option:"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-static {p1, v0}, La/a/a/a/a;->a(Lantlr/Token;Ljava/lang/StringBuilder;)Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    invoke-virtual {p2, v0, p0, v1, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_0
    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    iget-object v0, p0, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    const-string v1, " "

    if-eqz v0, :cond_0

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    const-string v2, ":"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_0
    iget-boolean v0, p0, Lantlr/GrammarAtom;->not:Z

    if-eqz v0, :cond_1

    const-string v0, "~"

    invoke-static {v1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_1
    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
