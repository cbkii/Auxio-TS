.class public Lantlr/DefineGrammarSymbols;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/ANTLRGrammarParseBehavior;


# static fields
.field public static final DEFAULT_TOKENMANAGER_NAME:Ljava/lang/String; = "*default"


# instance fields
.field public analyzer:Lantlr/LLkAnalyzer;

.field public args:[Ljava/lang/String;

.field public grammar:Lantlr/Grammar;

.field public grammars:Ljava/util/Hashtable;

.field public headerActions:Ljava/util/Hashtable;

.field public language:Ljava/lang/String;

.field public numLexers:I

.field public numParsers:I

.field public numTreeParsers:I

.field public thePreambleAction:Lantlr/Token;

.field public tokenManagers:Ljava/util/Hashtable;

.field public tool:Lantlr/Tool;


# direct methods
.method public constructor <init>(Lantlr/Tool;[Ljava/lang/String;Lantlr/LLkAnalyzer;)V
    .locals 3

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->headerActions:Ljava/util/Hashtable;

    new-instance v0, Lantlr/CommonToken;

    const/4 v1, 0x0

    const-string v2, ""

    invoke-direct {v0, v1, v2}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->thePreambleAction:Lantlr/Token;

    const-string v0, "Java"

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->language:Ljava/lang/String;

    iput v1, p0, Lantlr/DefineGrammarSymbols;->numLexers:I

    iput v1, p0, Lantlr/DefineGrammarSymbols;->numParsers:I

    iput v1, p0, Lantlr/DefineGrammarSymbols;->numTreeParsers:I

    iput-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iput-object p2, p0, Lantlr/DefineGrammarSymbols;->args:[Ljava/lang/String;

    iput-object p3, p0, Lantlr/DefineGrammarSymbols;->analyzer:Lantlr/LLkAnalyzer;

    return-void
.end method


# virtual methods
.method public _refStringLiteral(Lantlr/Token;Lantlr/Token;IZ)V
    .locals 0

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/LexerGrammar;

    if-nez p2, :cond_1

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2, p1}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p2

    if-eqz p2, :cond_0

    return-void

    :cond_0
    new-instance p2, Lantlr/StringLiteralSymbol;

    invoke-direct {p2, p1}, Lantlr/StringLiteralSymbol;-><init>(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1}, Lantlr/TokenManager;->nextTokenType()I

    move-result p1

    invoke-virtual {p2, p1}, Lantlr/TokenSymbol;->setTokenType(I)V

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0, p2}, Lantlr/TokenManager;->define(Lantlr/TokenSymbol;)V

    :cond_1
    return-void
.end method

.method public _refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V
    .locals 0

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2, p1}, Lantlr/TokenManager;->tokenDefined(Ljava/lang/String;)Z

    move-result p2

    if-nez p2, :cond_0

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2}, Lantlr/TokenManager;->nextTokenType()I

    move-result p2

    new-instance p3, Lantlr/TokenSymbol;

    invoke-direct {p3, p1}, Lantlr/TokenSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {p3, p2}, Lantlr/TokenSymbol;->setTokenType(I)V

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0, p3}, Lantlr/TokenManager;->define(Lantlr/TokenSymbol;)V

    :cond_0
    return-void
.end method

.method public abortGrammar()V
    .locals 2

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/util/Hashtable;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    :cond_0
    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    return-void
.end method

.method public beginAlt(Z)V
    .locals 0

    return-void
.end method

.method public beginChildList()V
    .locals 0

    return-void
.end method

.method public beginExceptionGroup()V
    .locals 0

    return-void
.end method

.method public beginExceptionSpec(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public beginSubRule(Lantlr/Token;Lantlr/Token;Z)V
    .locals 0

    return-void
.end method

.method public beginTree(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public defineRuleName(Lantlr/Token;Ljava/lang/String;ZLjava/lang/String;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p3

    iget v0, p1, Lantlr/Token;->type:I

    const/16 v1, 0x18

    if-ne v0, v1, :cond_0

    invoke-static {p3}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p3

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-interface {v0, v1}, Lantlr/TokenManager;->tokenDefined(Ljava/lang/String;)Z

    move-result v0

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->nextTokenType()I

    move-result v0

    new-instance v1, Lantlr/TokenSymbol;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-direct {v1, v2}, Lantlr/TokenSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {v1, v0}, Lantlr/TokenSymbol;->setTokenType(I)V

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0, v1}, Lantlr/TokenManager;->define(Lantlr/TokenSymbol;)V

    :cond_0
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v0, p3}, Lantlr/Grammar;->isDefined(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v0, p3}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    invoke-virtual {v0}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v1

    if-eqz v1, :cond_2

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v2, "redefinition of rule "

    invoke-static {v2, p3}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p3

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    invoke-virtual {v1, p3, p0, v2, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_0

    :cond_1
    new-instance v0, Lantlr/RuleSymbol;

    invoke-direct {v0, p3}, Lantlr/RuleSymbol;-><init>(Ljava/lang/String;)V

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v0}, Lantlr/Grammar;->define(Lantlr/RuleSymbol;)V

    :cond_2
    :goto_0
    invoke-virtual {v0}, Lantlr/RuleSymbol;->setDefined()V

    iput-object p2, v0, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    iput-object p4, v0, Lantlr/RuleSymbol;->comment:Ljava/lang/String;

    return-void
.end method

.method public defineToken(Lantlr/Token;Lantlr/Token;)V
    .locals 4

    const/4 v0, 0x0

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    goto :goto_0

    :cond_0
    move-object v1, v0

    :goto_0
    if-eqz p2, :cond_1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    :cond_1
    const-string v2, "Redefinition of token in tokens {...}: "

    if-eqz v0, :cond_7

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1, v0}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p1

    check-cast p1, Lantlr/StringLiteralSymbol;

    if-eqz p1, :cond_4

    if-eqz v1, :cond_3

    invoke-virtual {p1}, Lantlr/StringLiteralSymbol;->getLabel()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_2

    goto :goto_1

    :cond_2
    invoke-virtual {p1, v1}, Lantlr/StringLiteralSymbol;->setLabel(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3, v1, p1}, Lantlr/TokenManager;->mapToTokenSymbol(Ljava/lang/String;Lantlr/TokenSymbol;)V

    goto :goto_2

    :cond_3
    :goto_1
    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v1, "Redefinition of literal in tokens {...}: "

    invoke-static {v1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    invoke-virtual {p1, v0, p0, v1, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_4
    :goto_2
    if-eqz v1, :cond_6

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1, v1}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p1

    if-eqz p1, :cond_6

    instance-of v3, p1, Lantlr/StringLiteralSymbol;

    if-eqz v3, :cond_5

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-static {v2, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    invoke-virtual {p1, v0, p0, v1, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_5
    invoke-virtual {p1}, Lantlr/TokenSymbol;->getTokenType()I

    move-result p1

    new-instance p2, Lantlr/StringLiteralSymbol;

    invoke-direct {p2, v0}, Lantlr/StringLiteralSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {p2, p1}, Lantlr/TokenSymbol;->setTokenType(I)V

    invoke-virtual {p2, v1}, Lantlr/StringLiteralSymbol;->setLabel(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1, p2}, Lantlr/TokenManager;->define(Lantlr/TokenSymbol;)V

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0, v1, p2}, Lantlr/TokenManager;->mapToTokenSymbol(Ljava/lang/String;Lantlr/TokenSymbol;)V

    return-void

    :cond_6
    new-instance p1, Lantlr/StringLiteralSymbol;

    invoke-direct {p1, v0}, Lantlr/StringLiteralSymbol;-><init>(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2}, Lantlr/TokenManager;->nextTokenType()I

    move-result p2

    invoke-virtual {p1, p2}, Lantlr/TokenSymbol;->setTokenType(I)V

    invoke-virtual {p1, v1}, Lantlr/StringLiteralSymbol;->setLabel(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2, p1}, Lantlr/TokenManager;->define(Lantlr/TokenSymbol;)V

    if-eqz v1, :cond_9

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0, v1, p1}, Lantlr/TokenManager;->mapToTokenSymbol(Ljava/lang/String;Lantlr/TokenSymbol;)V

    goto :goto_3

    :cond_7
    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2, v1}, Lantlr/TokenManager;->tokenDefined(Ljava/lang/String;)Z

    move-result p2

    if-eqz p2, :cond_8

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-static {v2, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    invoke-virtual {p2, v0, p0, v1, p1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_8
    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1}, Lantlr/TokenManager;->nextTokenType()I

    move-result p1

    new-instance p2, Lantlr/TokenSymbol;

    invoke-direct {p2, v1}, Lantlr/TokenSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {p2, p1}, Lantlr/TokenSymbol;->setTokenType(I)V

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0, p2}, Lantlr/TokenManager;->define(Lantlr/TokenSymbol;)V

    :cond_9
    :goto_3
    return-void
.end method

.method public endAlt()V
    .locals 0

    return-void
.end method

.method public endChildList()V
    .locals 0

    return-void
.end method

.method public endExceptionGroup()V
    .locals 0

    return-void
.end method

.method public endExceptionSpec()V
    .locals 0

    return-void
.end method

.method public endGrammar()V
    .locals 0

    return-void
.end method

.method public endOptions()V
    .locals 7

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    const-string v2, "*default"

    if-nez v1, :cond_1

    iget-object v1, v0, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    if-nez v1, :cond_1

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v0, v2}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iput-object v2, v0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v0, v2}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/TokenManager;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v0}, Lantlr/Grammar;->setTokenManager(Lantlr/TokenManager;)V

    return-void

    :cond_0
    new-instance v0, Lantlr/SimpleTokenManager;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-direct {v0, v1, v3}, Lantlr/SimpleTokenManager;-><init>(Ljava/lang/String;Lantlr/Tool;)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->setTokenManager(Lantlr/TokenManager;)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1, v3, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {p0, v2, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void

    :cond_1
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    const/4 v3, 0x0

    if-nez v1, :cond_5

    iget-object v1, v0, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    if-eqz v1, :cond_5

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v0, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    iget-object v0, v0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_2

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v1, "Grammar "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " cannot have importVocab same as default output vocab (grammar name); ignored."

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    const/4 v1, 0x0

    iput-object v1, v0, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    invoke-virtual {p0}, Lantlr/DefineGrammarSymbols;->endOptions()V

    return-void

    :cond_2
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_3

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/TokenManager;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-interface {v0, v1}, Lantlr/TokenManager;->setName(Ljava/lang/String;)V

    invoke-interface {v0, v3}, Lantlr/TokenManager;->setReadOnly(Z)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->setTokenManager(Lantlr/TokenManager;)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1, p0, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void

    :cond_3
    new-instance v0, Lantlr/ImportVocabTokenManager;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v5, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v5, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v5, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    iget-object v5, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    iget-object v6, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-direct {v0, v1, v4, v5, v6}, Lantlr/ImportVocabTokenManager;-><init>(Lantlr/Grammar;Ljava/lang/String;Ljava/lang/String;Lantlr/Tool;)V

    invoke-virtual {v0, v3}, Lantlr/SimpleTokenManager;->setReadOnly(Z)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1, v3, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->setTokenManager(Lantlr/TokenManager;)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v1, v2}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_4

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {p0, v2, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_4
    return-void

    :cond_5
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    if-eqz v1, :cond_8

    iget-object v0, v0, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    if-nez v0, :cond_8

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v0, v1}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_6

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/TokenManager;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v0}, Lantlr/Grammar;->setTokenManager(Lantlr/TokenManager;)V

    return-void

    :cond_6
    new-instance v0, Lantlr/SimpleTokenManager;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-direct {v0, v1, v3}, Lantlr/SimpleTokenManager;-><init>(Ljava/lang/String;Lantlr/Tool;)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->setTokenManager(Lantlr/TokenManager;)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1, v3, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v1, v2}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_7

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {p0, v2, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_7
    return-void

    :cond_8
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    if-eqz v1, :cond_b

    iget-object v0, v0, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    if-eqz v0, :cond_b

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_9

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v1, "exportVocab of "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v4, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " same as importVocab; probably not what you want"

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_9
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_a

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/TokenManager;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-interface {v0, v1}, Lantlr/TokenManager;->setName(Ljava/lang/String;)V

    invoke-interface {v0, v3}, Lantlr/TokenManager;->setReadOnly(Z)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->setTokenManager(Lantlr/TokenManager;)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1, p0, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void

    :cond_a
    new-instance v0, Lantlr/ImportVocabTokenManager;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v5, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v5, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v5, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    iget-object v5, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    iget-object v6, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-direct {v0, v1, v4, v5, v6}, Lantlr/ImportVocabTokenManager;-><init>(Lantlr/Grammar;Ljava/lang/String;Ljava/lang/String;Lantlr/Tool;)V

    invoke-virtual {v0, v3}, Lantlr/SimpleTokenManager;->setReadOnly(Z)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1, v3, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->setTokenManager(Lantlr/TokenManager;)V

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v1, v2}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_b

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {p0, v2, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_b
    return-void
.end method

.method public endRule(Ljava/lang/String;)V
    .locals 0

    return-void
.end method

.method public endSubRule()V
    .locals 0

    return-void
.end method

.method public endTree()V
    .locals 0

    return-void
.end method

.method public getHeaderAction(Ljava/lang/String;)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->headerActions:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/Token;

    if-nez p0, :cond_0

    const-string p0, ""

    return-object p0

    :cond_0
    invoke-virtual {p0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getHeaderActionLine(Ljava/lang/String;)I
    .locals 0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->headerActions:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/Token;

    if-nez p0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    invoke-virtual {p0}, Lantlr/Token;->getLine()I

    move-result p0

    return p0
.end method

.method public hasError()V
    .locals 0

    return-void
.end method

.method public noASTSubRule()V
    .locals 0

    return-void
.end method

.method public oneOrMoreSubRule()V
    .locals 0

    return-void
.end method

.method public optionalSubRule()V
    .locals 0

    return-void
.end method

.method public refAction(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refArgAction(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refCharLiteral(Lantlr/Token;Lantlr/Token;ZIZ)V
    .locals 0

    return-void
.end method

.method public refCharRange(Lantlr/Token;Lantlr/Token;Lantlr/Token;IZ)V
    .locals 0

    return-void
.end method

.method public refElementOption(Lantlr/Token;Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refExceptionHandler(Lantlr/Token;Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refHeaderAction(Lantlr/Token;Lantlr/Token;)V
    .locals 3

    const-string v0, ""

    if-nez p1, :cond_0

    move-object p1, v0

    goto :goto_0

    :cond_0
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    const-string v1, "\""

    invoke-static {p1, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    :goto_0
    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->headerActions:Ljava/util/Hashtable;

    invoke-virtual {v1, p1}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_2

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ": header action already defined"

    goto :goto_1

    :cond_1
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ": header action \'"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\' already defined"

    :goto_1
    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_2
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->headerActions:Ljava/util/Hashtable;

    invoke-virtual {p0, p1, p2}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public refInitAction(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refMemberAction(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refPreambleAction(Lantlr/Token;)V
    .locals 0

    iput-object p1, p0, Lantlr/DefineGrammarSymbols;->thePreambleAction:Lantlr/Token;

    return-void
.end method

.method public refReturnAction(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refRule(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;I)V
    .locals 0

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iget p2, p2, Lantlr/Token;->type:I

    const/16 p3, 0x18

    if-ne p2, p3, :cond_0

    invoke-static {p1}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    :cond_0
    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p2, p1}, Lantlr/Grammar;->isDefined(Ljava/lang/String;)Z

    move-result p2

    if-nez p2, :cond_1

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    new-instance p2, Lantlr/RuleSymbol;

    invoke-direct {p2, p1}, Lantlr/RuleSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {p0, p2}, Lantlr/Grammar;->define(Lantlr/RuleSymbol;)V

    :cond_1
    return-void
.end method

.method public refSemPred(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refStringLiteral(Lantlr/Token;Lantlr/Token;IZ)V
    .locals 0

    invoke-virtual {p0, p1, p2, p3, p4}, Lantlr/DefineGrammarSymbols;->_refStringLiteral(Lantlr/Token;Lantlr/Token;IZ)V

    return-void
.end method

.method public refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V
    .locals 0

    invoke-virtual/range {p0 .. p7}, Lantlr/DefineGrammarSymbols;->_refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V

    return-void
.end method

.method public refTokenRange(Lantlr/Token;Lantlr/Token;Lantlr/Token;IZ)V
    .locals 14

    move-object v8, p0

    move/from16 v9, p5

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const/4 v10, 0x0

    invoke-virtual {v0, v10}, Ljava/lang/String;->charAt(I)C

    move-result v0

    const/4 v11, 0x1

    const/4 v12, 0x0

    const/16 v13, 0x22

    move-object v2, p1

    if-ne v0, v13, :cond_0

    invoke-virtual {p0, p1, v12, v11, v9}, Lantlr/DefineGrammarSymbols;->refStringLiteral(Lantlr/Token;Lantlr/Token;IZ)V

    goto :goto_0

    :cond_0
    const/4 v1, 0x0

    const/4 v3, 0x0

    const/4 v4, 0x0

    const/4 v5, 0x0

    const/4 v6, 0x1

    move-object v0, p0

    move/from16 v7, p5

    invoke-virtual/range {v0 .. v7}, Lantlr/DefineGrammarSymbols;->_refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V

    :goto_0
    invoke-virtual/range {p2 .. p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v10}, Ljava/lang/String;->charAt(I)C

    move-result v0

    move-object/from16 v2, p2

    if-ne v0, v13, :cond_1

    invoke-virtual {p0, v2, v12, v11, v9}, Lantlr/DefineGrammarSymbols;->_refStringLiteral(Lantlr/Token;Lantlr/Token;IZ)V

    goto :goto_1

    :cond_1
    const/4 v1, 0x0

    const/4 v3, 0x0

    const/4 v4, 0x0

    const/4 v5, 0x0

    const/4 v6, 0x1

    move-object v0, p0

    move/from16 v7, p5

    invoke-virtual/range {v0 .. v7}, Lantlr/DefineGrammarSymbols;->_refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V

    :goto_1
    return-void
.end method

.method public refTokensSpecElementOption(Lantlr/Token;Lantlr/Token;Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refTreeSpecifier(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public refWildcard(Lantlr/Token;Lantlr/Token;I)V
    .locals 0

    return-void
.end method

.method public reset()V
    .locals 1

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    return-void
.end method

.method public setArgOfRuleRef(Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public setCharVocabulary(Lantlr/collections/impl/BitSet;)V
    .locals 0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    check-cast p0, Lantlr/LexerGrammar;

    invoke-virtual {p0, p1}, Lantlr/LexerGrammar;->setCharVocabulary(Lantlr/collections/impl/BitSet;)V

    return-void
.end method

.method public setFileOption(Lantlr/Token;Lantlr/Token;Ljava/lang/String;)V
    .locals 9

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "language"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/4 v1, 0x6

    if-eqz v0, :cond_3

    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    if-ne p1, v1, :cond_0

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    const/16 p2, 0x22

    invoke-static {p1, p2}, Lantlr/StringUtils;->stripFront(Ljava/lang/String;C)Ljava/lang/String;

    move-result-object p1

    invoke-static {p1, p2}, Lantlr/StringUtils;->stripBack(Ljava/lang/String;C)Ljava/lang/String;

    move-result-object p1

    :goto_0
    iput-object p1, p0, Lantlr/DefineGrammarSymbols;->language:Ljava/lang/String;

    goto/16 :goto_7

    :cond_0
    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    const/16 v0, 0x18

    if-eq p1, v0, :cond_2

    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    const/16 v0, 0x29

    if-ne p1, v0, :cond_1

    goto :goto_1

    :cond_1
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result p1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v0, "language option must be string or identifier"

    goto :goto_2

    :cond_2
    :goto_1
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    goto :goto_0

    :cond_3
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v2, "mangleLiteralPrefix"

    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    if-ne p1, v1, :cond_4

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    const-string p2, "\""

    invoke-static {p1, p2, p2}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/Tool;->literalsPrefix:Ljava/lang/String;

    goto/16 :goto_7

    :cond_4
    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result p1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v0, "mangleLiteralPrefix option must be string"

    :goto_2
    invoke-virtual {p0, v0, p3, p1, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto/16 :goto_7

    :cond_5
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v2, "upperCaseMangledLiterals"

    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const-string v2, "false"

    const-string v3, "true"

    if-eqz v0, :cond_8

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_6

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const/4 p1, 0x1

    :goto_3
    iput-boolean p1, p0, Lantlr/Tool;->upperCaseMangledLiterals:Z

    goto/16 :goto_7

    :cond_6
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p2, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_7

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const/4 p1, 0x0

    goto :goto_3

    :cond_7
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p2

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v0, "Value for upperCaseMangledLiterals must be true or false"

    invoke-virtual {p0, v0, p3, p2, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto/16 :goto_7

    :cond_8
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v4, "namespaceStd"

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const-string v5, " option must be a string"

    const-string v6, "genHashLines"

    const-string v7, "namespaceAntlr"

    const-string v8, "Cpp"

    if-nez v0, :cond_d

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_d

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_9

    goto :goto_4

    :cond_9
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v2, "namespace"

    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_c

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->language:Ljava/lang/String;

    invoke-virtual {v0, v8}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_a

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->language:Ljava/lang/String;

    const-string v3, "CSharp"

    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_a

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " option only valid for C++ and C# (a.k.a CSharp)"

    goto :goto_5

    :cond_a
    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result v0

    if-eq v0, v1, :cond_b

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    goto/16 :goto_6

    :cond_b
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_15

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->setNameSpace(Ljava/lang/String;)V

    goto/16 :goto_7

    :cond_c
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v0, "Invalid file-level option: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-static {p1, v0}, La/a/a/a/a;->a(Lantlr/Token;Ljava/lang/StringBuilder;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    goto/16 :goto_2

    :cond_d
    :goto_4
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->language:Ljava/lang/String;

    invoke-virtual {v0, v8}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_e

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " option only valid for C++"

    :goto_5
    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    invoke-virtual {p0, p2, p3, v0, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto/16 :goto_7

    :cond_e
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v8, "noConstructors"

    invoke-virtual {v0, v8}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_10

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_f

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_f

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result v1

    const-string v2, "noConstructors option must be true or false"

    invoke-virtual {p1, v2, p3, v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_f
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    iput-boolean p1, p0, Lantlr/Tool;->noConstructors:Z

    goto/16 :goto_7

    :cond_10
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_12

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_11

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_11

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result v1

    const-string v2, "genHashLines option must be true or false"

    invoke-virtual {p1, v2, p3, v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_11
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    iput-boolean p1, p0, Lantlr/Tool;->genHashLines:Z

    goto :goto_7

    :cond_12
    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result v0

    if-eq v0, v1, :cond_13

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    :goto_6
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    invoke-virtual {p0, p1, p3, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_7

    :cond_13
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p3

    invoke-virtual {p3, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p3

    if-eqz p3, :cond_14

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/Tool;->namespaceStd:Ljava/lang/String;

    goto :goto_7

    :cond_14
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_15

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/Tool;->namespaceAntlr:Ljava/lang/String;

    :cond_15
    :goto_7
    return-void
.end method

.method public setGrammarOption(Lantlr/Token;Lantlr/Token;)V
    .locals 4

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "tokdef"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_9

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "tokenVocabulary"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    goto/16 :goto_2

    :cond_0
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "literal"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-eqz v1, :cond_1

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "the literal option is invalid >= ANTLR 2.6.0.\n  Use the \"tokens {...}\" mechanism instead."

    invoke-virtual {p0, v1, p1, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto/16 :goto_4

    :cond_1
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "exportVocab"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/16 v1, 0x18

    const/16 v2, 0x29

    if-eqz v0, :cond_4

    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    if-eq p1, v2, :cond_3

    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    if-ne p1, v1, :cond_2

    goto :goto_0

    :cond_2
    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "exportVocab must be an identifier"

    goto/16 :goto_3

    :cond_3
    :goto_0
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    goto/16 :goto_4

    :cond_4
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v3, "importVocab"

    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_7

    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    if-eq p1, v2, :cond_6

    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    if-ne p1, v1, :cond_5

    goto :goto_1

    :cond_5
    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "importVocab must be an identifier"

    goto :goto_3

    :cond_6
    :goto_1
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    goto :goto_4

    :cond_7
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "k"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_8

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_8

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "1"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_8

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "Treewalkers only support k=1"

    goto :goto_3

    :cond_8
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1, p2}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    goto :goto_4

    :cond_9
    :goto_2
    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "tokdef/tokenVocabulary options are invalid >= ANTLR 2.6.0.\n  Use importVocab/exportVocab instead.  Please see the documentation.\n  The previous options were so heinous that Terence changed the whole\n  vocabulary mechanism; it was better to change the names rather than\n  subtly change the functionality of the known options.  Sorry!"

    :goto_3
    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_4
    return-void
.end method

.method public setRuleOption(Lantlr/Token;Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public setSubruleOption(Lantlr/Token;Lantlr/Token;)V
    .locals 0

    return-void
.end method

.method public setUserExceptions(Ljava/lang/String;)V
    .locals 0

    return-void
.end method

.method public startLexer(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;Ljava/lang/String;)V
    .locals 3

    iget v0, p0, Lantlr/DefineGrammarSymbols;->numLexers:I

    if-lez v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v1, "You may only have one lexer per grammar file: class "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    iget v0, p0, Lantlr/DefineGrammarSymbols;->numLexers:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/DefineGrammarSymbols;->numLexers:I

    invoke-virtual {p0}, Lantlr/DefineGrammarSymbols;->reset()V

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    invoke-virtual {v0, p2}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Grammar;

    if-eqz v0, :cond_2

    instance-of p1, v0, Lantlr/LexerGrammar;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    if-nez p1, :cond_1

    const-string p1, "\'"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\' is already defined as a non-lexer"

    goto :goto_0

    :cond_1
    const-string p1, "Lexer \'"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\' is already defined"

    :goto_0
    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto :goto_1

    :cond_2
    new-instance v0, Lantlr/LexerGrammar;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-direct {v0, p2, v1, p3}, Lantlr/LexerGrammar;-><init>(Ljava/lang/String;Lantlr/Tool;Ljava/lang/String;)V

    iput-object p4, v0, Lantlr/Grammar;->comment:Ljava/lang/String;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->args:[Ljava/lang/String;

    invoke-virtual {v0, p2}, Lantlr/LexerGrammar;->processArguments([Ljava/lang/String;)V

    invoke-virtual {v0, p1}, Lantlr/Grammar;->setFilename(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->thePreambleAction:Lantlr/Token;

    iput-object p1, v0, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    new-instance p1, Lantlr/CommonToken;

    const/4 p2, 0x0

    const-string p3, ""

    invoke-direct {p1, p2, p3}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    iput-object p1, p0, Lantlr/DefineGrammarSymbols;->thePreambleAction:Lantlr/Token;

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    :goto_1
    return-void
.end method

.method public startParser(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;Ljava/lang/String;)V
    .locals 3

    iget v0, p0, Lantlr/DefineGrammarSymbols;->numParsers:I

    if-lez v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v1, "You may only have one parser per grammar file: class "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    iget v0, p0, Lantlr/DefineGrammarSymbols;->numParsers:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/DefineGrammarSymbols;->numParsers:I

    invoke-virtual {p0}, Lantlr/DefineGrammarSymbols;->reset()V

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    invoke-virtual {v0, p2}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Grammar;

    if-eqz v0, :cond_2

    instance-of p1, v0, Lantlr/ParserGrammar;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    if-nez p1, :cond_1

    const-string p1, "\'"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\' is already defined as a non-parser"

    goto :goto_0

    :cond_1
    const-string p1, "Parser \'"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\' is already defined"

    :goto_0
    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto :goto_1

    :cond_2
    new-instance v0, Lantlr/ParserGrammar;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-direct {v0, p2, v1, p3}, Lantlr/ParserGrammar;-><init>(Ljava/lang/String;Lantlr/Tool;Ljava/lang/String;)V

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iput-object p4, p2, Lantlr/Grammar;->comment:Ljava/lang/String;

    iget-object p3, p0, Lantlr/DefineGrammarSymbols;->args:[Ljava/lang/String;

    invoke-virtual {p2, p3}, Lantlr/Grammar;->processArguments([Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p2, p1}, Lantlr/Grammar;->setFilename(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object p2

    iget-object p3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p1, p2, p3}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->thePreambleAction:Lantlr/Token;

    iput-object p2, p1, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    new-instance p1, Lantlr/CommonToken;

    const/4 p2, 0x0

    const-string p3, ""

    invoke-direct {p1, p2, p3}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    iput-object p1, p0, Lantlr/DefineGrammarSymbols;->thePreambleAction:Lantlr/Token;

    :goto_1
    return-void
.end method

.method public startTreeWalker(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;Ljava/lang/String;)V
    .locals 3

    iget v0, p0, Lantlr/DefineGrammarSymbols;->numTreeParsers:I

    if-lez v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v1, "You may only have one tree parser per grammar file: class "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    iget v0, p0, Lantlr/DefineGrammarSymbols;->numTreeParsers:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/DefineGrammarSymbols;->numTreeParsers:I

    invoke-virtual {p0}, Lantlr/DefineGrammarSymbols;->reset()V

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    invoke-virtual {v0, p2}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Grammar;

    if-eqz v0, :cond_2

    instance-of p1, v0, Lantlr/TreeWalkerGrammar;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    if-nez p1, :cond_1

    const-string p1, "\'"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\' is already defined as a non-tree-walker"

    goto :goto_0

    :cond_1
    const-string p1, "Tree-walker \'"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\' is already defined"

    :goto_0
    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto :goto_1

    :cond_2
    new-instance v0, Lantlr/TreeWalkerGrammar;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-direct {v0, p2, v1, p3}, Lantlr/TreeWalkerGrammar;-><init>(Ljava/lang/String;Lantlr/Tool;Ljava/lang/String;)V

    iput-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iput-object p4, p2, Lantlr/Grammar;->comment:Ljava/lang/String;

    iget-object p3, p0, Lantlr/DefineGrammarSymbols;->args:[Ljava/lang/String;

    invoke-virtual {p2, p3}, Lantlr/Grammar;->processArguments([Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p2, p1}, Lantlr/Grammar;->setFilename(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object p2

    iget-object p3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p1, p2, p3}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->thePreambleAction:Lantlr/Token;

    iput-object p2, p1, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    new-instance p1, Lantlr/CommonToken;

    const/4 p2, 0x0

    const-string p3, ""

    invoke-direct {p1, p2, p3}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    iput-object p1, p0, Lantlr/DefineGrammarSymbols;->thePreambleAction:Lantlr/Token;

    :goto_1
    return-void
.end method

.method public synPred()V
    .locals 0

    return-void
.end method

.method public zeroOrMoreSubRule()V
    .locals 0

    return-void
.end method
