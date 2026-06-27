.class public Lantlr/TokenRefElement;
.super Lantlr/GrammarAtom;
.source ""


# direct methods
.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;ZI)V
    .locals 2

    invoke-direct {p0, p1, p2, p4}, Lantlr/GrammarAtom;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    iput-boolean p3, p0, Lantlr/GrammarAtom;->not:Z

    iget-object p3, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p3, p3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    iget-object p4, p0, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-interface {p3, p4}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p3

    if-nez p3, :cond_0

    iget-object p1, p1, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string p3, "Undefined token symbol: "

    invoke-static {p3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p3

    iget-object p4, p0, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-virtual {p3, p4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p3

    iget-object p4, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    invoke-virtual {p4}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p4

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result v1

    invoke-virtual {p1, p3, p4, v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_0

    :cond_0
    invoke-virtual {p3}, Lantlr/TokenSymbol;->getTokenType()I

    move-result p1

    iput p1, p0, Lantlr/GrammarAtom;->tokenType:I

    invoke-virtual {p3}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/GrammarAtom;->setASTNodeType(Ljava/lang/String;)V

    :goto_0
    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result p1

    iput p1, p0, Lantlr/GrammarElement;->line:I

    return-void
.end method


# virtual methods
.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/TokenRefElement;)V

    return-void
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/GrammarAtom;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method
