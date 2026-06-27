.class public Lantlr/DiagnosticCodeGenerator;
.super Lantlr/CodeGenerator;
.source ""


# instance fields
.field public doingLexRules:Z

.field public syntacticPredLevel:I


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lantlr/CodeGenerator;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/DiagnosticCodeGenerator;->syntacticPredLevel:I

    iput-boolean v0, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    new-instance v0, Lantlr/JavaCharFormatter;

    invoke-direct {v0}, Lantlr/JavaCharFormatter;-><init>()V

    iput-object v0, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    return-void
.end method


# virtual methods
.method public gen()V
    .locals 3

    :try_start_0
    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    iget-object v0, v0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :cond_0
    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/Grammar;

    iget-object v2, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-virtual {v1, v2}, Lantlr/Grammar;->setGrammarAnalyzer(Lantlr/LLkGrammarAnalyzer;)V

    invoke-virtual {v1, p0}, Lantlr/Grammar;->setCodeGenerator(Lantlr/CodeGenerator;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v2, v1}, Lantlr/LLkGrammarAnalyzer;->setGrammar(Lantlr/Grammar;)V

    invoke-virtual {v1}, Lantlr/Grammar;->generate()V

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1}, Lantlr/Tool;->hasError()Z

    move-result v1

    if-eqz v1, :cond_0

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v2, "Exiting due to errors."

    invoke-virtual {v1, v2}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    iget-object v0, v0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :cond_2
    :goto_1
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_3

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/TokenManager;

    invoke-interface {v1}, Lantlr/TokenManager;->isReadOnly()Z

    move-result v2

    if-nez v2, :cond_2

    invoke-virtual {p0, v1}, Lantlr/DiagnosticCodeGenerator;->genTokenTypes(Lantlr/TokenManager;)V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception v0

    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1}, Lantlr/Tool;->reportException(Ljava/lang/Exception;Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method public gen(Lantlr/ActionElement;)V
    .locals 1

    iget-boolean v0, p1, Lantlr/ActionElement;->isSemPred:Z

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const-string v0, "ACTION: "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object p1, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_printAction(Ljava/lang/String;)V

    :goto_0
    return-void
.end method

.method public gen(Lantlr/AlternativeBlock;)V
    .locals 1

    const-string v0, "Start of alternative block."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/DiagnosticCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    move-result v0

    if-nez v0, :cond_0

    const-string v0, "Warning: This alternative block is non-deterministic"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/DiagnosticCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    return-void
.end method

.method public gen(Lantlr/BlockEndElement;)V
    .locals 0

    return-void
.end method

.method public gen(Lantlr/CharLiteralElement;)V
    .locals 1

    const-string v0, "Match character "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-boolean v0, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v0, :cond_0

    const-string v0, "NOT "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    if-eqz v0, :cond_1

    const-string v0, ", label="

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p1, p1, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_1
    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/CharRangeElement;)V
    .locals 2

    const-string v0, "Match character range: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p1, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p1, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/CharRangeElement;->label:Ljava/lang/String;

    if-eqz v0, :cond_0

    const-string v0, ", label = "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p1, p1, Lantlr/CharRangeElement;->label:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/LexerGrammar;)V
    .locals 6

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v0, "Generating "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->reportProgress(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    invoke-virtual {p0}, Lantlr/DiagnosticCodeGenerator;->genHeader()V

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "*** Lexer Preamble Action."

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "This action will appear before the declaration of your lexer class:"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "*** End of Lexer Preamble Action"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "*** Your lexer class is called \'"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v4, "\' and is a subclass of \'"

    invoke-static {v3, v2, v4}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "\'."

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "*** User-defined lexer  class members:"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "These are the member declarations that you defined for your class:"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "*** End of user-defined lexer class members"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "*** String literals used in the parser"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "The following string literals were used in the parser."

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "An actual code generator would arrange to place these literals"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "into a table in the generated lexer, so that actions in the"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "generated lexer could match token text against the literals."

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "String literals used in the lexer are not listed here, as they"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "are incorporated into the mainstream lexer processing."

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getSymbols()Ljava/util/Enumeration;

    move-result-object v2

    :cond_0
    :goto_0
    invoke-interface {v2}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_1

    invoke-interface {v2}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/GrammarSymbol;

    instance-of v4, v3, Lantlr/StringLiteralSymbol;

    if-eqz v4, :cond_0

    check-cast v3, Lantlr/StringLiteralSymbol;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " = "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v3

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "*** End of string literals used by the parser"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/DiagnosticCodeGenerator;->genNextToken()V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "*** User-defined Lexer rules:"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v2

    :cond_2
    :goto_1
    invoke-interface {v2}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_3

    invoke-interface {v2}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/RuleSymbol;

    iget-object v4, v3, Lantlr/GrammarSymbol;->id:Ljava/lang/String;

    const-string v5, "mnextToken"

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-nez v4, :cond_2

    invoke-virtual {p0, v3}, Lantlr/DiagnosticCodeGenerator;->genRule(Lantlr/RuleSymbol;)V

    goto :goto_1

    :cond_3
    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** End User-defined Lexer rules:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v0}, Ljava/io/PrintWriter;->close()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iput-boolean p1, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    return-void
.end method

.method public gen(Lantlr/OneOrMoreBlock;)V
    .locals 1

    const-string v0, "Start ONE-OR-MORE (...)+ block:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/DiagnosticCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/OneOrMoreBlock;)Z

    move-result v0

    if-nez v0, :cond_0

    const-string v0, "Warning: This one-or-more block is non-deterministic"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/DiagnosticCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "End ONE-OR-MORE block."

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/ParserGrammar;)V
    .locals 3

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v0, "Generating "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->reportProgress(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/DiagnosticCodeGenerator;->genHeader()V

    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** Parser Preamble Action."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "This action will appear before the declaration of your parser class:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "*** End of Parser Preamble Action"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "*** Your parser class is called \'"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v2, "\' and is a subclass of \'"

    invoke-static {v1, v0, v2}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\'."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** User-defined parser class members:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "These are the member declarations that you defined for your class:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "*** End of user-defined parser class members"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** Parser rules:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :cond_0
    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/GrammarSymbol;

    instance-of v2, v1, Lantlr/RuleSymbol;

    if-eqz v2, :cond_0

    check-cast v1, Lantlr/RuleSymbol;

    invoke-virtual {p0, v1}, Lantlr/DiagnosticCodeGenerator;->genRule(Lantlr/RuleSymbol;)V

    goto :goto_0

    :cond_1
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** End of parser rules"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "*** End of parser"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public gen(Lantlr/RuleRefElement;)V
    .locals 3

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    const-string v1, "Rule Reference: "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v1, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    if-eqz v1, :cond_0

    const-string v1, ", assigned to \'"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\'"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    iget-object v1, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v1, :cond_1

    const-string v1, ", arguments = "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_1
    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    if-eqz v0, :cond_6

    invoke-virtual {v0}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v1

    if-nez v1, :cond_2

    goto :goto_2

    :cond_2
    iget-object v1, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    if-eqz v1, :cond_3

    iget-object v1, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-nez v1, :cond_4

    const-string v1, "Error: You assigned from Rule \'"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\', but that rule has no return type."

    :goto_0
    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_3
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-nez v1, :cond_4

    iget v1, p0, Lantlr/DiagnosticCodeGenerator;->syntacticPredLevel:I

    if-nez v1, :cond_4

    iget-object v1, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v1, :cond_4

    const-string v1, "Warning: Rule \'"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\' returns a value"

    goto :goto_0

    :cond_4
    :goto_1
    iget-object v1, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v1, :cond_5

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-nez v0, :cond_5

    const-string v0, "Error: Rule \'"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "\' accepts no arguments."

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    return-void

    :cond_6
    :goto_2
    const-string v0, "Rule \'"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "\' is referenced, but that rule is not defined."

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "\tPerhaps the rule is misspelled, or you forgot to define it."

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/StringLiteralElement;)V
    .locals 1

    const-string v0, "Match string literal "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    if-eqz v0, :cond_0

    const-string v0, ", label="

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p1, p1, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TokenRangeElement;)V
    .locals 2

    const-string v0, "Match token range: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p1, Lantlr/TokenRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p1, Lantlr/TokenRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/TokenRangeElement;->label:Ljava/lang/String;

    if-eqz v0, :cond_0

    const-string v0, ", label = "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p1, p1, Lantlr/TokenRangeElement;->label:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TokenRefElement;)V
    .locals 1

    const-string v0, "Match token "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-boolean v0, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v0, :cond_0

    const-string v0, "NOT "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    if-eqz v0, :cond_1

    const-string v0, ", label="

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p1, p1, Lantlr/GrammarAtom;->label:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_1
    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TreeElement;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Tree reference: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TreeWalkerGrammar;)V
    .locals 3

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v0, "Generating "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->reportProgress(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/DiagnosticCodeGenerator;->genHeader()V

    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** Tree-walker Preamble Action."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "This action will appear before the declaration of your tree-walker class:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "*** End of tree-walker Preamble Action"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "*** Your tree-walker class is called \'"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v2, "\' and is a subclass of \'"

    invoke-static {v1, v0, v2}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\'."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** User-defined tree-walker class members:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "These are the member declarations that you defined for your class:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "*** End of user-defined tree-walker class members"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** tree-walker rules:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :cond_0
    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/GrammarSymbol;

    instance-of v2, v1, Lantlr/RuleSymbol;

    if-eqz v2, :cond_0

    check-cast v1, Lantlr/RuleSymbol;

    invoke-virtual {p0, v1}, Lantlr/DiagnosticCodeGenerator;->genRule(Lantlr/RuleSymbol;)V

    goto :goto_0

    :cond_1
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** End of tree-walker rules"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "*** End of tree-walker"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public gen(Lantlr/WildcardElement;)V
    .locals 1

    const-string v0, "Match wildcard"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    const-string v0, ", label = "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/ZeroOrMoreBlock;)V
    .locals 1

    const-string v0, "Start ZERO-OR-MORE (...)+ block:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/DiagnosticCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/ZeroOrMoreBlock;)Z

    move-result v0

    if-nez v0, :cond_0

    const-string v0, "Warning: This zero-or-more block is non-deterministic"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/DiagnosticCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "End ZERO-OR-MORE block."

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genAlt(Lantlr/Alternative;)V
    .locals 2

    iget-object v0, p1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_0
    instance-of v1, v0, Lantlr/BlockEndElement;

    if-nez v1, :cond_0

    invoke-virtual {v0}, Lantlr/GrammarElement;->generate()V

    iget-object v0, v0, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_0

    :cond_0
    invoke-virtual {p1}, Lantlr/Alternative;->getTreeSpecifier()Lantlr/Token;

    move-result-object v0

    if-eqz v0, :cond_1

    const-string v0, "AST will be built as: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/Alternative;->getTreeSpecifier()Lantlr/Token;

    move-result-object p1

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    return-void
.end method

.method public genBlockPreamble(Lantlr/AlternativeBlock;)V
    .locals 1

    iget-object v0, p1, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    if-eqz v0, :cond_0

    const-string v0, "Init action: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p1, p1, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public genCommonBlock(Lantlr/AlternativeBlock;)V
    .locals 6

    iget-object v0, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    const/4 v1, 0x0

    const/4 v2, 0x1

    if-ne v0, v2, :cond_0

    move v0, v2

    goto :goto_0

    :cond_0
    move v0, v1

    :goto_0
    const-string v3, "Start of an alternative block."

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "The lookahead set for this block is:"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/DiagnosticCodeGenerator;->genLookaheadSetForBlock(Lantlr/AlternativeBlock;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    if-eqz v0, :cond_1

    const-string v3, "This block has a single alternative"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1, v1}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v3

    iget-object v3, v3, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v3, :cond_2

    const-string v3, "Warning: you specified a syntactic predicate for this alternative,"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "and it is the only alternative of a block and will be ignored."

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    const-string v3, "This block has multiple alternatives:"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    :goto_1
    iget-object v3, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->size()I

    move-result v3

    const-string v4, ""

    if-ge v1, v3, :cond_9

    invoke-virtual {p1, v1}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v3

    iget-object v5, v3, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    if-eqz v1, :cond_3

    const-string v4, "Otherwise, "

    :cond_3
    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    const-string v4, "Alternate("

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    add-int/lit8 v1, v1, 0x1

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v5, ") will be taken IF:"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    const-string v4, "The lookahead set: "

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/DiagnosticCodeGenerator;->genLookaheadSetForAlt(Lantlr/Alternative;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, v3, Lantlr/Alternative;->semPred:Ljava/lang/String;

    const-string v5, "is matched."

    if-nez v4, :cond_5

    iget-object v4, v3, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v4, :cond_4

    goto :goto_2

    :cond_4
    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_3

    :cond_5
    :goto_2
    const-string v4, "is matched, AND "

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :goto_3
    iget-object v4, v3, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v4, :cond_7

    const-string v4, "the semantic predicate:"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, v3, Lantlr/Alternative;->semPred:Ljava/lang/String;

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, v3, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v4, :cond_6

    const-string v4, "is true, AND "

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_4

    :cond_6
    const-string v4, "is true."

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    :goto_4
    iget-object v4, v3, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v4, :cond_8

    const-string v4, "the syntactic predicate:"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, v3, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {p0, v4}, Lantlr/DiagnosticCodeGenerator;->genSynPred(Lantlr/SynPredBlock;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    invoke-virtual {p0, v3}, Lantlr/DiagnosticCodeGenerator;->genAlt(Lantlr/Alternative;)V

    goto/16 :goto_1

    :cond_9
    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "OTHERWISE, a NoViableAlt exception will be thrown"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    if-nez v0, :cond_a

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "End of alternatives"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "End of alternative block."

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genFollowSetForRuleBlock(Lantlr/RuleBlock;)V
    .locals 2

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object p1, p1, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    const/4 v1, 0x1

    invoke-interface {v0, v1, p1}, Lantlr/LLkGrammarAnalyzer;->FOLLOW(ILantlr/RuleEndElement;)Lantlr/Lookahead;

    move-result-object p1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v0, v0, Lantlr/Grammar;->maxk:I

    invoke-virtual {p0, v0, v1, p1}, Lantlr/DiagnosticCodeGenerator;->printSet(IILantlr/Lookahead;)V

    return-void
.end method

.method public genHeader()V
    .locals 3

    const-string v0, "ANTLR-generated file resulting from grammar "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, v1, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "Diagnostic output"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "Terence Parr, MageLang Institute"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "with John Lilley, Empathy Software"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "ANTLR Version "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/Tool;->version:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "; 1989-2005"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "*** Header Action."

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "This action will appear at the top of all generated files."

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v1, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v1, v0}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "*** End of Header Action"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genLookaheadSetForAlt(Lantlr/Alternative;)V
    .locals 3

    iget-boolean v0, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_0

    iget-object v0, p1, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v0, v0, v1

    invoke-virtual {v0}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v0

    if-eqz v0, :cond_0

    const-string p1, "MATCHES ALL"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void

    :cond_0
    iget v0, p1, Lantlr/Alternative;->lookaheadDepth:I

    const v2, 0x7fffffff

    if-ne v0, v2, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v0, v0, Lantlr/Grammar;->maxk:I

    :cond_1
    :goto_0
    if-gt v1, v0, :cond_2

    iget-object v2, p1, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v2, v2, v1

    invoke-virtual {p0, v0, v1, v2}, Lantlr/DiagnosticCodeGenerator;->printSet(IILantlr/Lookahead;)V

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    return-void
.end method

.method public genLookaheadSetForBlock(Lantlr/AlternativeBlock;)V
    .locals 4

    const/4 v0, 0x0

    move v1, v0

    :goto_0
    iget-object v2, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    if-ge v0, v2, :cond_2

    invoke-virtual {p1, v0}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v2

    iget v2, v2, Lantlr/Alternative;->lookaheadDepth:I

    const v3, 0x7fffffff

    if-ne v2, v3, :cond_0

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v1, v0, Lantlr/Grammar;->maxk:I

    goto :goto_1

    :cond_0
    if-ge v1, v2, :cond_1

    move v1, v2

    :cond_1
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    :goto_1
    const/4 v0, 0x1

    :goto_2
    if-gt v0, v1, :cond_3

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v2, v0, p1}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object v2

    invoke-virtual {p0, v1, v0, v2}, Lantlr/DiagnosticCodeGenerator;->printSet(IILantlr/Lookahead;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_2

    :cond_3
    return-void
.end method

.method public genNextToken()V
    .locals 3

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "*** Lexer nextToken rule:"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "The lexer nextToken rule is synthesized from all of the user-defined"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "lexer rules.  It logically consists of one big alternative block with"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "each user-defined rule being an alternative."

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    const-string v2, "nextToken"

    invoke-static {v0, v1, v2}, Lantlr/MakeGrammar;->createNextTokenRule(Lantlr/Grammar;Lantlr/collections/impl/Vector;Ljava/lang/String;)Lantlr/RuleBlock;

    move-result-object v0

    new-instance v1, Lantlr/RuleSymbol;

    const-string v2, "mnextToken"

    invoke-direct {v1, v2}, Lantlr/RuleSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {v1}, Lantlr/RuleSymbol;->setDefined()V

    invoke-virtual {v1, v0}, Lantlr/RuleSymbol;->setBlock(Lantlr/RuleBlock;)V

    const-string v2, "private"

    iput-object v2, v1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2, v1}, Lantlr/Grammar;->define(Lantlr/RuleSymbol;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v1, v0}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    move-result v1

    if-nez v1, :cond_0

    const-string v1, "The grammar analyzer has determined that the synthesized"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "nextToken rule is non-deterministic (i.e., it has ambiguities)"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "This means that there is some overlap of the character"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "lookahead for two or more of your lexer rules."

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/DiagnosticCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;)V

    const-string v0, "*** End of nextToken lexer rule."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genRule(Lantlr/RuleSymbol;)V
    .locals 10

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v1, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    if-eqz v1, :cond_0

    const-string v1, "Lexer"

    goto :goto_0

    :cond_0
    const-string v1, "Parser"

    :goto_0
    const-string v2, " Rule: "

    const-string v3, "*** "

    invoke-static {v3, v1, v2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v3

    const-string v4, "*** End "

    if-nez v3, :cond_1

    const-string v0, "This rule is undefined."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "This means that the rule was referenced somewhere in the grammar,"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "but a definition for the rule was not encountered."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "It is also possible that syntax errors during the parse of"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "your grammar file prevented correct processing of the rule."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_1
    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void

    :cond_1
    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, p1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v3

    if-eqz v3, :cond_2

    const-string v3, "Access: "

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v5, p1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    invoke-virtual {p1}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v3

    iget-object v5, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v5, :cond_3

    const-string v5, "Return value(s): "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget-object v6, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v5, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    if-eqz v5, :cond_5

    const-string v5, "Error: you specified return value(s) for a lexical rule."

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "\tLexical rules have an implicit return type of \'int\'."

    goto :goto_2

    :cond_3
    iget-boolean v5, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    if-eqz v5, :cond_4

    const-string v5, "Return value: lexical rule returns an implicit token type"

    goto :goto_2

    :cond_4
    const-string v5, "Return value: none"

    :goto_2
    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v5, v3, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz v5, :cond_6

    const-string v5, "Arguments: "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget-object v6, v3, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    invoke-virtual {p0, v3}, Lantlr/DiagnosticCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v5, v3}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    move-result v5

    if-nez v5, :cond_7

    const-string v5, "Error: This rule is non-deterministic"

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    invoke-virtual {p0, v3}, Lantlr/DiagnosticCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;)V

    invoke-virtual {v3, v0}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object v5

    if-eqz v5, :cond_a

    const-string v6, "You specified error-handler(s) for this rule:"

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v6, v6, 0x1

    iput v6, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v6, 0x0

    :goto_3
    iget-object v7, v5, Lantlr/ExceptionSpec;->handlers:Lantlr/collections/impl/Vector;

    invoke-virtual {v7}, Lantlr/collections/impl/Vector;->size()I

    move-result v7

    if-ge v6, v7, :cond_9

    if-eqz v6, :cond_8

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    iget-object v7, v5, Lantlr/ExceptionSpec;->handlers:Lantlr/collections/impl/Vector;

    invoke-virtual {v7, v6}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Lantlr/ExceptionHandler;

    const-string v8, "Error-handler("

    invoke-static {v8}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    add-int/lit8 v6, v6, 0x1

    invoke-virtual {v8, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v9, ") catches ["

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, v7, Lantlr/ExceptionHandler;->exceptionTypeAndName:Lantlr/Token;

    invoke-virtual {v9}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "] and executes:"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v7, v7, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v7}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    goto :goto_3

    :cond_9
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "End error-handlers."

    goto :goto_4

    :cond_a
    iget-boolean v0, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    if-nez v0, :cond_b

    const-string v0, "Default error-handling will be generated, which catches all"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "parser exceptions and consumes tokens until the follow-set is seen."

    :goto_4
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_b
    iget-boolean v0, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    if-nez v0, :cond_c

    const-string v0, "The follow set for this rule is:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/DiagnosticCodeGenerator;->genFollowSetForRuleBlock(Lantlr/RuleBlock;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_c
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v4, v1, v2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    goto/16 :goto_1
.end method

.method public genSynPred(Lantlr/SynPredBlock;)V
    .locals 1

    iget v0, p0, Lantlr/DiagnosticCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/DiagnosticCodeGenerator;->syntacticPredLevel:I

    invoke-virtual {p0, p1}, Lantlr/DiagnosticCodeGenerator;->gen(Lantlr/AlternativeBlock;)V

    iget p1, p0, Lantlr/DiagnosticCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/DiagnosticCodeGenerator;->syntacticPredLevel:I

    return-void
.end method

.method public genTokenTypes(Lantlr/TokenManager;)V
    .locals 3

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Generating "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->reportProgress(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v0

    iput-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/DiagnosticCodeGenerator;->genHeader()V

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "*** Tokens used by the parser"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "This is a list of the token numeric values and the corresponding"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "token identifiers.  Some tokens are literals, and because of that"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "they have no identifiers.  Literals are double-quoted."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-interface {p1}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object p1

    const/4 v0, 0x4

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_1

    invoke-virtual {p1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    if-eqz v1, :cond_0

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "*** End of tokens used by the parser"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;
    .locals 0

    const-string p0, "["

    const-string p1, "]"

    invoke-static {p0, p2, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getASTCreateString(Lantlr/collections/impl/Vector;)Ljava/lang/String;
    .locals 0

    const-string p0, "***Create an AST from a vector here***"

    invoke-static {p0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p0

    const-string p1, "line.separator"

    invoke-static {p1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public mapTreeId(Ljava/lang/String;Lantlr/ActionTransInfo;)Ljava/lang/String;
    .locals 0

    return-object p1
.end method

.method public printSet(IILantlr/Lookahead;)V
    .locals 6

    iget-object p3, p3, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {p3}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object p3

    const/4 v0, 0x1

    if-eq p1, v0, :cond_0

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "k=="

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p2, ": {"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    goto :goto_0

    :cond_0
    const-string p1, "{ "

    :goto_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    array-length p1, p3

    const-string p2, ""

    const/4 v1, 0x5

    if-le p1, v1, :cond_1

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr p1, v0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_1
    const/4 p1, 0x0

    move v2, p1

    move v3, v2

    :goto_1
    array-length v4, p3

    if-ge v2, v4, :cond_5

    add-int/2addr v3, v0

    if-le v3, v1, :cond_2

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    move v3, p1

    :cond_2
    iget-boolean v4, p0, Lantlr/DiagnosticCodeGenerator;->doingLexRules:Z

    if-eqz v4, :cond_3

    iget-object v4, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    aget v5, p3, v2

    invoke-interface {v4, v5}, Lantlr/CharFormatter;->literalChar(I)Ljava/lang/String;

    move-result-object v4

    goto :goto_2

    :cond_3
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v4}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v4

    aget v5, p3, v2

    invoke-virtual {v4, v5}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/String;

    :goto_2
    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    array-length v4, p3

    sub-int/2addr v4, v0

    if-eq v2, v4, :cond_4

    const-string v4, ", "

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_4
    add-int/lit8 v2, v2, 0x1

    goto :goto_1

    :cond_5
    array-length p1, p3

    if-le p1, v1, :cond_6

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_6
    const-string p1, " }"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    return-void
.end method

.method public processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;
    .locals 0

    return-object p1
.end method
