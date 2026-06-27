.class public Lantlr/HTMLCodeGenerator;
.super Lantlr/CodeGenerator;
.source ""


# instance fields
.field public doingLexRules:Z

.field public firstElementInAlt:Z

.field public prevAltElem:Lantlr/AlternativeElement;

.field public syntacticPredLevel:I


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lantlr/CodeGenerator;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/HTMLCodeGenerator;->syntacticPredLevel:I

    iput-boolean v0, p0, Lantlr/HTMLCodeGenerator;->doingLexRules:Z

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/HTMLCodeGenerator;->prevAltElem:Lantlr/AlternativeElement;

    new-instance v0, Lantlr/JavaCharFormatter;

    invoke-direct {v0}, Lantlr/JavaCharFormatter;-><init>()V

    iput-object v0, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    return-void
.end method

.method public static HTMLEncode(Ljava/lang/String;)Ljava/lang/String;
    .locals 5

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v1

    const/4 v2, 0x0

    :goto_0
    if-ge v2, v1, :cond_5

    invoke-virtual {p0, v2}, Ljava/lang/String;->charAt(I)C

    move-result v3

    const/16 v4, 0x26

    if-ne v3, v4, :cond_0

    const-string v3, "&amp;"

    :goto_1
    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_2

    :cond_0
    const/16 v4, 0x22

    if-ne v3, v4, :cond_1

    const-string v3, "&quot;"

    goto :goto_1

    :cond_1
    const/16 v4, 0x27

    if-ne v3, v4, :cond_2

    const-string v3, "&#039;"

    goto :goto_1

    :cond_2
    const/16 v4, 0x3c

    if-ne v3, v4, :cond_3

    const-string v3, "&lt;"

    goto :goto_1

    :cond_3
    const/16 v4, 0x3e

    if-ne v3, v4, :cond_4

    const-string v3, "&gt;"

    goto :goto_1

    :cond_4
    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    :goto_2
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_5
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
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

    invoke-virtual {v1, p0}, Lantlr/Grammar;->setCodeGenerator(Lantlr/CodeGenerator;)V

    invoke-virtual {v1}, Lantlr/Grammar;->generate()V

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1}, Lantlr/Tool;->hasError()Z

    move-result v1

    if-eqz v1, :cond_0

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v2, "Exiting due to errors."

    invoke-virtual {v1, v2}, Lantlr/Tool;->fatalError(Ljava/lang/String;)V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception v0

    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1}, Lantlr/Tool;->reportException(Ljava/lang/Exception;Ljava/lang/String;)V

    :cond_1
    return-void
.end method

.method public gen(Lantlr/ActionElement;)V
    .locals 0

    return-void
.end method

.method public gen(Lantlr/AlternativeBlock;)V
    .locals 1

    const-string v0, ""

    invoke-virtual {p0, p1, v0}, Lantlr/HTMLCodeGenerator;->genGenericBlock(Lantlr/AlternativeBlock;Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/BlockEndElement;)V
    .locals 0

    return-void
.end method

.method public gen(Lantlr/CharLiteralElement;)V
    .locals 1

    iget-boolean v0, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v0, :cond_0

    const-string v0, "~"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object p1, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-static {p1}, Lantlr/HTMLCodeGenerator;->HTMLEncode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " "

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/CharRangeElement;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p1, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p1, p1, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " "

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/LexerGrammar;)V
    .locals 4

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v0, "Generating "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".html"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->reportProgress(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/HTMLCodeGenerator;->doingLexRules:Z

    invoke-virtual {p0}, Lantlr/HTMLCodeGenerator;->genHeader()V

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-static {v0}, Lantlr/HTMLCodeGenerator;->HTMLEncode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_0
    const-string v0, "Definition of lexer "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v2, ", which is a subclass of "

    invoke-static {v1, v0, v2}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/HTMLCodeGenerator;->genNextToken()V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :cond_1
    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_2

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/RuleSymbol;

    iget-object v2, v1, Lantlr/GrammarSymbol;->id:Ljava/lang/String;

    const-string v3, "mnextToken"

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_1

    invoke-virtual {p0, v1}, Lantlr/HTMLCodeGenerator;->genRule(Lantlr/RuleSymbol;)V

    goto :goto_0

    :cond_2
    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v0}, Ljava/io/PrintWriter;->close()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iput-boolean p1, p0, Lantlr/HTMLCodeGenerator;->doingLexRules:Z

    return-void
.end method

.method public gen(Lantlr/OneOrMoreBlock;)V
    .locals 1

    const-string v0, "+"

    invoke-virtual {p0, p1, v0}, Lantlr/HTMLCodeGenerator;->genGenericBlock(Lantlr/AlternativeBlock;Ljava/lang/String;)V

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

    const-string v1, ".html"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->reportProgress(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/HTMLCodeGenerator;->genHeader()V

    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-static {v0}, Lantlr/HTMLCodeGenerator;->HTMLEncode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_0
    const-string v0, "Definition of parser "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v2, ", which is a subclass of "

    invoke-static {v1, v0, v2}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :cond_1
    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_2

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/GrammarSymbol;

    instance-of v2, v1, Lantlr/RuleSymbol;

    if-eqz v2, :cond_1

    check-cast v1, Lantlr/RuleSymbol;

    invoke-virtual {p0, v1}, Lantlr/HTMLCodeGenerator;->genRule(Lantlr/RuleSymbol;)V

    goto :goto_0

    :cond_2
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/HTMLCodeGenerator;->genTail()V

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

    const-string v0, "<a href=\""

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v2, ".html#"

    invoke-static {v1, v0, v2}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\">"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object p1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string p1, "</a>"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string p1, " "

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/StringLiteralElement;)V
    .locals 1

    iget-boolean v0, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v0, :cond_0

    const-string v0, "~"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    iget-object p1, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-static {p1}, Lantlr/HTMLCodeGenerator;->HTMLEncode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string p1, " "

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TokenRangeElement;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p1, Lantlr/TokenRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p1, p1, Lantlr/TokenRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " "

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TokenRefElement;)V
    .locals 1

    iget-boolean v0, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v0, :cond_0

    const-string v0, "~"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    iget-object p1, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string p1, " "

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TreeElement;)V
    .locals 1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string p1, " "

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

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

    const-string v1, ".html"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->reportProgress(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/HTMLCodeGenerator;->genHeader()V

    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-static {v0}, Lantlr/HTMLCodeGenerator;->HTMLEncode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_0
    const-string v0, "Definition of tree parser "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v2, ", which is a subclass of "

    invoke-static {v1, v0, v2}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :cond_1
    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_2

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/GrammarSymbol;

    instance-of v2, v1, Lantlr/RuleSymbol;

    if-eqz v2, :cond_1

    check-cast v1, Lantlr/RuleSymbol;

    invoke-virtual {p0, v1}, Lantlr/HTMLCodeGenerator;->genRule(Lantlr/RuleSymbol;)V

    goto :goto_0

    :cond_2
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public gen(Lantlr/WildcardElement;)V
    .locals 0

    const-string p1, ". "

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/ZeroOrMoreBlock;)V
    .locals 1

    const-string v0, "*"

    invoke-virtual {p0, p1, v0}, Lantlr/HTMLCodeGenerator;->genGenericBlock(Lantlr/AlternativeBlock;Ljava/lang/String;)V

    return-void
.end method

.method public genAlt(Lantlr/Alternative;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/Alternative;->getTreeSpecifier()Lantlr/Token;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {p1}, Lantlr/Alternative;->getTreeSpecifier()Lantlr/Token;

    move-result-object v0

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_0
    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/HTMLCodeGenerator;->prevAltElem:Lantlr/AlternativeElement;

    iget-object p1, p1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_0
    instance-of v0, p1, Lantlr/BlockEndElement;

    if-nez v0, :cond_1

    invoke-virtual {p1}, Lantlr/GrammarElement;->generate()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/HTMLCodeGenerator;->firstElementInAlt:Z

    iput-object p1, p0, Lantlr/HTMLCodeGenerator;->prevAltElem:Lantlr/AlternativeElement;

    iget-object p1, p1, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_0

    :cond_1
    return-void
.end method

.method public genCommonBlock(Lantlr/AlternativeBlock;)V
    .locals 5

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_1

    invoke-virtual {p1, v0}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v1

    iget-object v2, v1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    const/4 v2, 0x1

    if-lez v0, :cond_0

    iget-object v3, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->size()I

    move-result v3

    if-le v3, v2, :cond_0

    const-string v3, ""

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    const-string v3, "|\t"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_0
    iget-boolean v3, p0, Lantlr/HTMLCodeGenerator;->firstElementInAlt:Z

    iput-boolean v2, p0, Lantlr/HTMLCodeGenerator;->firstElementInAlt:Z

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v1}, Lantlr/HTMLCodeGenerator;->genAlt(Lantlr/Alternative;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iput-boolean v3, p0, Lantlr/HTMLCodeGenerator;->firstElementInAlt:Z

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
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

    invoke-virtual {p0, v0, v1, p1}, Lantlr/HTMLCodeGenerator;->printSet(IILantlr/Lookahead;)V

    return-void
.end method

.method public genGenericBlock(Lantlr/AlternativeBlock;Ljava/lang/String;)V
    .locals 5

    iget-object v0, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    const/4 v1, 0x1

    const-string v2, ""

    if-le v0, v1, :cond_2

    iget-boolean v0, p0, Lantlr/HTMLCodeGenerator;->firstElementInAlt:Z

    const-string v3, "(\t"

    if-nez v0, :cond_1

    iget-object v0, p0, Lantlr/HTMLCodeGenerator;->prevAltElem:Lantlr/AlternativeElement;

    if-eqz v0, :cond_0

    instance-of v4, v0, Lantlr/AlternativeBlock;

    if-eqz v4, :cond_0

    check-cast v0, Lantlr/AlternativeBlock;

    iget-object v0, v0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    if-ne v0, v1, :cond_1

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_0

    :cond_2
    const-string v0, "( "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :goto_0
    invoke-virtual {p0, p1}, Lantlr/HTMLCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;)V

    iget-object v0, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    const-string v3, " "

    const-string v4, ")"

    if-le v0, v1, :cond_3

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object p1, p1, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    instance-of p1, p1, Lantlr/BlockEndElement;

    if-nez p1, :cond_4

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_1

    :cond_3
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_4
    :goto_1
    return-void
.end method

.method public genHeader()V
    .locals 2

    const-string v0, "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "<HTML>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "<HEAD>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "<TITLE>Grammar "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, v1, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "</TITLE>"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "</HEAD>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "<BODY>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "<table summary=\"\" border=\"1\" cellpadding=\"5\">"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "<tr>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "<td>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "<font size=\"+2\">Grammar "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "</font><br>"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "<a href=\"http://www.ANTLR.org\">ANTLR</a>-generated HTML file from "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, v1, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "<p>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "Terence Parr, <a href=\"http://www.magelang.com\">MageLang Institute</a>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "<br>ANTLR Version "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/Tool;->version:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "; 1989-2005"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "</td>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "</tr>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "</table>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "<PRE>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genLookaheadSetForAlt(Lantlr/Alternative;)V
    .locals 3

    iget-boolean v0, p0, Lantlr/HTMLCodeGenerator;->doingLexRules:Z

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

    invoke-virtual {p0, v0, v1, v2}, Lantlr/HTMLCodeGenerator;->printSet(IILantlr/Lookahead;)V

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

    invoke-virtual {p0, v1, v0, v2}, Lantlr/HTMLCodeGenerator;->printSet(IILantlr/Lookahead;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_2

    :cond_3
    return-void
.end method

.method public genNextToken()V
    .locals 3

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "/** Lexer nextToken rule:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, " *  The lexer nextToken rule is synthesized from all of the user-defined"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, " *  lexer rules.  It logically consists of one big alternative block with"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, " *  each user-defined rule being an alternative."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, " */"

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

    invoke-virtual {p0, v0}, Lantlr/HTMLCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;)V

    return-void
.end method

.method public genRule(Lantlr/RuleSymbol;)V
    .locals 3

    if-eqz p1, :cond_3

    invoke-virtual {p1}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v0

    if-nez v0, :cond_0

    goto/16 :goto_0

    :cond_0
    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p1, Lantlr/RuleSymbol;->comment:Ljava/lang/String;

    if-eqz v1, :cond_1

    invoke-static {v1}, Lantlr/HTMLCodeGenerator;->HTMLEncode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_1
    iget-object v1, p1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->length()I

    move-result v1

    if-eqz v1, :cond_2

    iget-object v1, p1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    const-string v2, "public"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_2

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_2
    const-string v1, "<a name=\""

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\">"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string v1, "</a>"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object p1

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, ":\t"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/HTMLCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    const-string p1, ";"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_3
    :goto_0
    return-void
.end method

.method public genSynPred(Lantlr/SynPredBlock;)V
    .locals 1

    iget v0, p0, Lantlr/HTMLCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/HTMLCodeGenerator;->syntacticPredLevel:I

    const-string v0, " =>"

    invoke-virtual {p0, p1, v0}, Lantlr/HTMLCodeGenerator;->genGenericBlock(Lantlr/AlternativeBlock;Ljava/lang/String;)V

    iget p1, p0, Lantlr/HTMLCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/HTMLCodeGenerator;->syntacticPredLevel:I

    return-void
.end method

.method public genTail()V
    .locals 1

    const-string v0, "</PRE>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "</BODY>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "</HTML>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

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

    invoke-virtual {p0}, Lantlr/HTMLCodeGenerator;->genHeader()V

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

    const/4 p0, 0x0

    return-object p0
.end method

.method public getASTCreateString(Lantlr/collections/impl/Vector;)Ljava/lang/String;
    .locals 0

    const/4 p0, 0x0

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
    iget-boolean v4, p0, Lantlr/HTMLCodeGenerator;->doingLexRules:Z

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
