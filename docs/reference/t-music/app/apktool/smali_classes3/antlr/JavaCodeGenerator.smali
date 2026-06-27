.class public Lantlr/JavaCodeGenerator;
.super Lantlr/CodeGenerator;
.source ""


# static fields
.field public static final CONTINUE_LAST_MAPPING:I = -0x378

.field public static final NONUNIQUE:Ljava/lang/String;

.field public static final NO_MAPPING:I = -0x3e7

.field public static final caseSizeThreshold:I = 0x7f


# instance fields
.field public astVarNumber:I

.field public commonExtraArgs:Ljava/lang/String;

.field public commonExtraParams:Ljava/lang/String;

.field public commonLocalVars:Ljava/lang/String;

.field public currentASTResult:Ljava/lang/String;

.field public currentRule:Lantlr/RuleBlock;

.field public declaredASTVariables:Ljava/util/Hashtable;

.field public defaultLine:I

.field public exceptionThrown:Ljava/lang/String;

.field public genAST:Z

.field public labeledElementASTType:Ljava/lang/String;

.field public labeledElementInit:Ljava/lang/String;

.field public labeledElementType:Ljava/lang/String;

.field public lt1Value:Ljava/lang/String;

.field public printWriterManager:Lantlr/JavaCodeGeneratorPrintWriterManager;

.field public saveText:Z

.field public semPreds:Lantlr/collections/impl/Vector;

.field public syntacticPredLevel:I

.field public throwNoViable:Ljava/lang/String;

.field public treeVariableMap:Ljava/util/Hashtable;


# direct methods
.method public static constructor <clinit>()V
    .locals 1

    new-instance v0, Ljava/lang/String;

    invoke-direct {v0}, Ljava/lang/String;-><init>()V

    sput-object v0, Lantlr/JavaCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    return-void
.end method

.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lantlr/CodeGenerator;-><init>()V

    const/16 v0, -0x3e7

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/4 v0, 0x0

    iput v0, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    iput-boolean v0, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    iput-boolean v0, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/JavaCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    const/4 v0, 0x1

    iput v0, p0, Lantlr/JavaCodeGenerator;->astVarNumber:I

    new-instance v0, Lantlr/JavaCharFormatter;

    invoke-direct {v0}, Lantlr/JavaCharFormatter;-><init>()V

    iput-object v0, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    return-void
.end method

.method private GenRuleInvocation(Lantlr/RuleRefElement;)V
    .locals 8

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    invoke-interface {v1, v2}, Lantlr/JavaCodeGeneratorPrintWriterManager;->startSingleSourceLineMapping(I)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v1

    invoke-interface {v1}, Lantlr/JavaCodeGeneratorPrintWriterManager;->endMapping()V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v2, ","

    if-eqz v1, :cond_2

    :try_start_1
    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_0

    const-string v1, "true"

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_1

    :cond_0
    const-string v1, "false"

    goto :goto_0

    :goto_1
    iget-object v1, p0, Lantlr/JavaCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->length()I

    move-result v1

    if-nez v1, :cond_1

    iget-object v1, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v1, :cond_2

    :cond_1
    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    :cond_2
    iget-object v1, p0, Lantlr/JavaCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->length()I

    move-result v1

    if-eqz v1, :cond_3

    iget-object v1, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v1, :cond_3

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    :cond_3
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v1, v2}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v1

    check-cast v1, Lantlr/RuleSymbol;

    iget-object v2, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v2, :cond_6

    new-instance v2, Lantlr/ActionTransInfo;

    invoke-direct {v2}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v3, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    const/4 v4, 0x0

    iget-object v5, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v3, v4, v5, v2}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v3

    iget-boolean v4, v2, Lantlr/ActionTransInfo;->assignToRoot:Z

    if-nez v4, :cond_4

    iget-object v2, v2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    if-eqz v2, :cond_5

    :cond_4
    iget-object v2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "Arguments of rule reference \'"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "\' cannot set or ref #"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v5}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v5}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v6

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v7

    invoke-virtual {v2, v4, v5, v6, v7}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_5
    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v1, v1, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-nez v1, :cond_7

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Rule \'"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "\' accepts no arguments"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    :goto_2
    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result p1

    goto :goto_3

    :cond_6
    iget-object v1, v1, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz v1, :cond_7

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Missing parameters on reference to rule "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    goto :goto_2

    :goto_3
    invoke-virtual {v1, v2, v3, v4, p1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_7
    const-string p1, ");"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_8

    const-string p1, "_t = _retTree;"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :cond_8
    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method private genBitSet(Lantlr/collections/impl/BitSet;I)V
    .locals 7

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/16 v1, -0x3e7

    :try_start_0
    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "private static final long[] mk"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "() {"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->lengthInLongWords()I

    move-result v1

    const/16 v2, 0x8

    if-ge v1, v2, :cond_0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "\tlong[] data = { "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toStringOfWords()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "};"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto/16 :goto_5

    :cond_0
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "\tlong[] data = new long["

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, "];"

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toPackedArray()[J

    move-result-object p1

    const/4 v1, 0x0

    :goto_0
    array-length v2, p1

    if-ge v1, v2, :cond_5

    aget-wide v2, p1, v1

    const-wide/16 v4, 0x0

    cmp-long v2, v2, v4

    if-nez v2, :cond_1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    add-int/lit8 v2, v1, 0x1

    array-length v3, p1

    if-eq v2, v3, :cond_4

    aget-wide v3, p1, v1

    aget-wide v5, p1, v2

    cmp-long v3, v3, v5

    if-eqz v3, :cond_2

    goto :goto_3

    :cond_2
    :goto_1
    array-length v3, p1

    if-ge v2, v3, :cond_3

    aget-wide v3, p1, v2

    aget-wide v5, p1, v1

    cmp-long v3, v3, v5

    if-nez v3, :cond_3

    add-int/lit8 v2, v2, 0x1

    goto :goto_1

    :cond_3
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "\tfor (int i = "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, "; i<="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    add-int/lit8 v4, v2, -0x1

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, "; i++) { data[i]="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget-wide v4, p1, v1

    invoke-virtual {v3, v4, v5}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    const-string v1, "L; }"

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_2
    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    goto :goto_4

    :cond_4
    :goto_3
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "\tdata["

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, "]="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget-wide v4, p1, v1

    invoke-virtual {v3, v4, v5}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    const-string v1, "L;"

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_2

    :goto_4
    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    move v1, v2

    goto :goto_0

    :cond_5
    :goto_5
    const-string p1, "\treturn data;"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "public static final BitSet "

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = new BitSet(mk"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "());"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method private genBlockFinish(Lantlr/JavaBlockFinishingInfo;Ljava/lang/String;I)V
    .locals 1

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    iput p3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean p3, p1, Lantlr/JavaBlockFinishingInfo;->needAnErrorClause:Z

    if-eqz p3, :cond_2

    iget-boolean p3, p1, Lantlr/JavaBlockFinishingInfo;->generatedAnIf:Z

    if-nez p3, :cond_0

    iget-boolean p3, p1, Lantlr/JavaBlockFinishingInfo;->generatedSwitch:Z

    if-eqz p3, :cond_2

    :cond_0
    iget-boolean p3, p1, Lantlr/JavaBlockFinishingInfo;->generatedAnIf:Z

    if-eqz p3, :cond_1

    const-string p3, "else {"

    :goto_0
    invoke-virtual {p0, p3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    const-string p3, "{"

    goto :goto_0

    :goto_1
    iget p3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p3, p3, 0x1

    iput p3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p2, "}"

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    iget-object p1, p1, Lantlr/JavaBlockFinishingInfo;->postscript:Ljava/lang/String;

    if-eqz p1, :cond_3

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    :cond_3
    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method private genElementAST(Lantlr/AlternativeElement;)V
    .locals 12

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v2, "_in = "

    const-string v3, " "

    const-string v4, "_AST"

    const-string v5, "tmp"

    const/4 v6, 0x1

    const-string v7, ";"

    if-eqz v1, :cond_1

    :try_start_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-nez v1, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_0

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v5, p0, Lantlr/JavaCodeGenerator;->astVarNumber:I

    invoke-virtual {v8, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    iget v5, p0, Lantlr/JavaCodeGenerator;->astVarNumber:I

    add-int/2addr v5, v6

    iput v5, p0, Lantlr/JavaCodeGenerator;->astVarNumber:I

    invoke-direct {p0, p1, v4}, Lantlr/JavaCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v5, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :cond_0
    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :cond_1
    :try_start_2
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-eqz v1, :cond_f

    iget v1, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v1, :cond_f

    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    const/4 v8, 0x3

    if-eqz v1, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-eq v1, v8, :cond_3

    :cond_2
    move v1, v6

    goto :goto_0

    :cond_3
    const/4 v1, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v9

    if-eq v9, v8, :cond_4

    instance-of v8, p1, Lantlr/TokenRefElement;

    if-eqz v8, :cond_4

    move v1, v6

    :cond_4
    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v8, v8, Lantlr/Grammar;->hasSyntacticPredicate:Z

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v8

    if-eqz v8, :cond_5

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v8

    move-object v11, v8

    move-object v8, v5

    move-object v5, v11

    goto :goto_1

    :cond_5
    iget-object v8, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v9, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v5, p0, Lantlr/JavaCodeGenerator;->astVarNumber:I

    invoke-virtual {v9, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    iget v9, p0, Lantlr/JavaCodeGenerator;->astVarNumber:I

    add-int/2addr v9, v6

    iput v9, p0, Lantlr/JavaCodeGenerator;->astVarNumber:I

    :goto_1
    if-eqz v1, :cond_7

    instance-of v9, p1, Lantlr/GrammarAtom;

    if-eqz v9, :cond_6

    move-object v9, p1

    check-cast v9, Lantlr/GrammarAtom;

    invoke-virtual {v9}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v10

    if-eqz v10, :cond_6

    invoke-virtual {v9}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v9

    goto :goto_2

    :cond_6
    iget-object v9, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    :goto_2
    invoke-virtual {p0, p1, v5, v9}, Lantlr/JavaCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V

    :cond_7
    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v9, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-direct {p0, p1, v4}, Lantlr/JavaCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/TreeWalkerGrammar;

    if-eqz v5, :cond_8

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "_in = null;"

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v3
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    const-string v5, " = "

    if-eqz v3, :cond_a

    :try_start_3
    instance-of v3, p1, Lantlr/GrammarAtom;

    if-eqz v3, :cond_9

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-object v9, p1

    check-cast v9, Lantlr/GrammarAtom;

    invoke-virtual {p0, v9, v8}, Lantlr/JavaCodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_3
    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    goto :goto_4

    :cond_9
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_3

    :goto_4
    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    if-nez v3, :cond_c

    if-eqz v1, :cond_c

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    instance-of v3, p1, Lantlr/GrammarAtom;

    if-eqz v3, :cond_b

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-object v5, p1

    check-cast v5, Lantlr/GrammarAtom;

    invoke-virtual {p0, v5, v1}, Lantlr/JavaCodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_5
    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    goto :goto_6

    :cond_b
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_5

    :goto_6
    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/TreeWalkerGrammar;

    if-eqz v3, :cond_c

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    if-eqz v1, :cond_f

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_0

    const-string v1, ");"

    if-eq p1, v6, :cond_e

    const/4 v2, 0x2

    if-eq p1, v2, :cond_d

    goto :goto_9

    :cond_d
    :try_start_4
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "astFactory.makeASTRoot(currentAST, "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_7
    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    goto :goto_8

    :cond_e
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "astFactory.addASTChild(currentAST, "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_7

    :goto_8
    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_0

    :cond_f
    :goto_9
    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method private genErrorCatchForElement(Lantlr/AlternativeElement;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p1, Lantlr/AlternativeElement;->enclosingRuleName:Ljava/lang/String;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_1

    invoke-static {v0}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    if-nez v0, :cond_2

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v2, "Enclosing rule not found!"

    invoke-virtual {v1, v2}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_2
    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object v0

    if-eqz v0, :cond_3

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    const-string v1, "}"

    invoke-virtual {p0, v1, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    invoke-direct {p0, v0}, Lantlr/JavaCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    :cond_3
    return-void
.end method

.method private genErrorHandler(Lantlr/ExceptionSpec;)V
    .locals 7

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p1, Lantlr/ExceptionSpec;->handlers:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_2

    iget-object v1, p1, Lantlr/ExceptionSpec;->handlers:Lantlr/collections/impl/Vector;

    invoke-virtual {v1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/ExceptionHandler;

    iget v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    iget-object v3, v1, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getLine()I

    move-result v3

    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "catch ("

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, v1, Lantlr/ExceptionHandler;->exceptionTypeAndName:Lantlr/Token;

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ") {"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    iget-object v4, v1, Lantlr/ExceptionHandler;->exceptionTypeAndName:Lantlr/Token;

    invoke-virtual {v4}, Lantlr/Token;->getLine()I

    move-result v4

    invoke-virtual {p0, v3, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v3, :cond_0

    const-string v3, "if (inputState.guessing==0) {"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_0
    new-instance v3, Lantlr/ActionTransInfo;

    invoke-direct {v3}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v4, v1, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    iget-object v5, v1, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v5}, Lantlr/Token;->getLine()I

    move-result v5

    iget-object v6, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v4, v5, v6, v3}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->printAction(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->hasSyntacticPredicate:Z
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v4, "}"

    if-eqz v3, :cond_1

    :try_start_1
    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, -0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "} else {"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "throw "

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, v1, Lantlr/ExceptionHandler;->exceptionTypeAndName:Lantlr/Token;

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->extractIdOfAction(Lantlr/Token;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ";"

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    add-int/lit8 v0, v0, 0x1

    goto/16 :goto_0

    :catchall_0
    move-exception p1

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1

    :cond_2
    return-void
.end method

.method private genErrorTryForElement(Lantlr/AlternativeElement;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p1, Lantlr/AlternativeElement;->enclosingRuleName:Ljava/lang/String;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_1

    invoke-static {v0}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    if-nez v0, :cond_2

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v2, "Enclosing rule not found!"

    invoke-virtual {v1, v2}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_2
    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object v0

    if-eqz v0, :cond_3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    const-string v0, "try { // for error handling"

    invoke-virtual {p0, v0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_3
    return-void
.end method

.method private genLiteralsTest()V
    .locals 1

    const-string v0, "_ttype = testLiteralsTable(_ttype);"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method private genLiteralsTestForPartialToken()V
    .locals 1

    const-string v0, "_ttype = testLiteralsTable(new String(text.getBuffer(),_begin,text.length()-_begin),_ttype);"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method private getValueString(I)Ljava/lang/String;
    .locals 3

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-eqz v1, :cond_0

    iget-object p0, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {p0, p1}, Lantlr/CharFormatter;->literalChar(I)Ljava/lang/String;

    move-result-object p0

    goto :goto_0

    :cond_0
    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0, p1}, Lantlr/TokenManager;->getTokenSymbolAt(I)Lantlr/TokenSymbol;

    move-result-object v0

    if-nez v0, :cond_1

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, ""

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_1
    invoke-virtual {v0}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v1

    instance-of v2, v0, Lantlr/StringLiteralSymbol;

    if-eqz v2, :cond_3

    check-cast v0, Lantlr/StringLiteralSymbol;

    invoke-virtual {v0}, Lantlr/StringLiteralSymbol;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_2

    move-object p0, v0

    goto :goto_0

    :cond_2
    invoke-direct {p0, v1}, Lantlr/JavaCodeGenerator;->mangleLiteral(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    if-nez p0, :cond_4

    invoke-static {p1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object p0

    goto :goto_0

    :cond_3
    move-object p0, v1

    :cond_4
    :goto_0
    return-object p0
.end method

.method private lookaheadString(I)Ljava/lang/String;
    .locals 1

    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p0, p0, Lantlr/TreeWalkerGrammar;

    if-eqz p0, :cond_0

    const-string p0, "_t.getType()"

    return-object p0

    :cond_0
    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "LA("

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method private mangleLiteral(Ljava/lang/String;)Ljava/lang/String;
    .locals 5

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v0, v0, Lantlr/Tool;->literalsPrefix:Ljava/lang/String;

    const/4 v1, 0x1

    move-object v2, v0

    move v0, v1

    :goto_0
    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v3

    sub-int/2addr v3, v1

    if-ge v0, v3, :cond_1

    invoke-virtual {p1, v0}, Ljava/lang/String;->charAt(I)C

    move-result v3

    invoke-static {v3}, Ljava/lang/Character;->isLetter(C)Z

    move-result v3

    if-nez v3, :cond_0

    invoke-virtual {p1, v0}, Ljava/lang/String;->charAt(I)C

    move-result v3

    const/16 v4, 0x5f

    if-eq v3, v4, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {p1, v0}, Ljava/lang/String;->charAt(I)C

    move-result v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-boolean p0, p0, Lantlr/Tool;->upperCaseMangledLiterals:Z

    if-eqz p0, :cond_2

    invoke-virtual {v2}, Ljava/lang/String;->toUpperCase()Ljava/lang/String;

    move-result-object v2

    :cond_2
    return-object v2
.end method

.method private mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V
    .locals 2

    instance-of v0, p1, Lantlr/TreeElement;

    if-eqz v0, :cond_0

    check-cast p1, Lantlr/TreeElement;

    iget-object p1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-direct {p0, p1, p2}, Lantlr/JavaCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    return-void

    :cond_0
    const/4 v0, 0x0

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_2

    instance-of v1, p1, Lantlr/TokenRefElement;

    if-eqz v1, :cond_1

    check-cast p1, Lantlr/TokenRefElement;

    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    goto :goto_0

    :cond_1
    instance-of v1, p1, Lantlr/RuleRefElement;

    if-eqz v1, :cond_2

    check-cast p1, Lantlr/RuleRefElement;

    iget-object v0, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    :cond_2
    :goto_0
    if-eqz v0, :cond_4

    iget-object p1, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p1, v0}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p1

    if-eqz p1, :cond_3

    iget-object p1, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p1, v0}, Ljava/util/Hashtable;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p0, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    sget-object p1, Lantlr/JavaCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    invoke-virtual {p0, v0, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    goto :goto_1

    :cond_3
    iget-object p0, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p0, v0, p2}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_4
    :goto_1
    return-void
.end method

.method private setupGrammarParameters(Lantlr/Grammar;)V
    .locals 7

    instance-of v0, p1, Lantlr/ParserGrammar;

    const-string v1, "null"

    const-string v2, "RecognitionException"

    const-string v3, "\""

    const-string v4, "AST"

    const-string v5, ""

    const-string v6, "ASTLabelType"

    if-eqz v0, :cond_1

    iput-object v4, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p1, v6}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p1, v6}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object p1

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1, v3, v3}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_0

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    :cond_0
    const-string p1, "Token "

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->labeledElementType:Ljava/lang/String;

    iput-object v1, p0, Lantlr/JavaCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v5, p0, Lantlr/JavaCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    iput-object v5, p0, Lantlr/JavaCodeGenerator;->commonExtraParams:Ljava/lang/String;

    iput-object v5, p0, Lantlr/JavaCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string p1, "LT(1)"

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v2, p0, Lantlr/JavaCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string p1, "throw new NoViableAltException(LT(1), getFilename());"

    goto :goto_0

    :cond_1
    instance-of v0, p1, Lantlr/LexerGrammar;

    if-eqz v0, :cond_2

    const-string p1, "char "

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->labeledElementType:Ljava/lang/String;

    const-string p1, "\'\\0\'"

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v5, p0, Lantlr/JavaCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    const-string p1, "boolean _createToken"

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->commonExtraParams:Ljava/lang/String;

    const-string p1, "int _ttype; Token _token=null; int _begin=text.length();"

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string p1, "LA(1)"

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v2, p0, Lantlr/JavaCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string p1, "throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());"

    :goto_0
    iput-object p1, p0, Lantlr/JavaCodeGenerator;->throwNoViable:Ljava/lang/String;

    goto :goto_1

    :cond_2
    instance-of v0, p1, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_5

    iput-object v4, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    iput-object v4, p0, Lantlr/JavaCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {p1, v6}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_3

    invoke-virtual {p1, v6}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v0

    if-eqz v0, :cond_3

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0, v3, v3}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_3

    iput-object v0, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    iput-object v0, p0, Lantlr/JavaCodeGenerator;->labeledElementType:Ljava/lang/String;

    :cond_3
    invoke-virtual {p1, v6}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-nez v0, :cond_4

    new-instance v0, Lantlr/Token;

    const/4 v3, 0x6

    invoke-direct {v0, v3, v4}, Lantlr/Token;-><init>(ILjava/lang/String;)V

    invoke-virtual {p1, v6, v0}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    :cond_4
    iput-object v1, p0, Lantlr/JavaCodeGenerator;->labeledElementInit:Ljava/lang/String;

    const-string p1, "_t"

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    const-string p1, "AST _t"

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->commonExtraParams:Ljava/lang/String;

    iput-object v5, p0, Lantlr/JavaCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string p1, "("

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v1, ")_t"

    invoke-static {p1, v0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v2, p0, Lantlr/JavaCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string p1, "throw new NoViableAltException(_t);"

    goto :goto_0

    :cond_5
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string p1, "Unknown grammar type"

    invoke-virtual {p0, p1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :goto_1
    return-void
.end method

.method public static suitableForCaseExpression(Lantlr/Alternative;)Z
    .locals 2

    iget v0, p0, Lantlr/Alternative;->lookaheadDepth:I

    const/4 v1, 0x1

    if-ne v0, v1, :cond_0

    iget-object v0, p0, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v0, v0, v1

    invoke-virtual {v0}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v0

    if-nez v0, :cond_0

    iget-object p0, p0, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object p0, p0, v1

    iget-object p0, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->degree()I

    move-result p0

    const/16 v0, 0x7f

    if-gt p0, v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v1, 0x0

    :goto_0
    return v1
.end method


# virtual methods
.method public _print(Ljava/lang/String;)V
    .locals 1

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;I)V

    return-void
.end method

.method public _print(Ljava/lang/String;I)V
    .locals 2

    const/16 v0, -0x378

    if-gtz p2, :cond_0

    if-ne p2, v0, :cond_1

    :cond_0
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v1

    invoke-interface {v1, p2}, Lantlr/JavaCodeGeneratorPrintWriterManager;->startMapping(I)V

    :cond_1
    invoke-super {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    if-gtz p2, :cond_2

    if-ne p2, v0, :cond_3

    :cond_2
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p0

    invoke-interface {p0}, Lantlr/JavaCodeGeneratorPrintWriterManager;->endMapping()V

    :cond_3
    return-void
.end method

.method public _println(Ljava/lang/String;)V
    .locals 1

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;I)V

    return-void
.end method

.method public _println(Ljava/lang/String;I)V
    .locals 2

    const/16 v0, -0x378

    if-gtz p2, :cond_0

    if-ne p2, v0, :cond_1

    :cond_0
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v1

    invoke-interface {v1, p2}, Lantlr/JavaCodeGeneratorPrintWriterManager;->startMapping(I)V

    :cond_1
    invoke-super {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    if-gtz p2, :cond_2

    if-ne p2, v0, :cond_3

    :cond_2
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p0

    invoke-interface {p0}, Lantlr/JavaCodeGeneratorPrintWriterManager;->endMapping()V

    :cond_3
    return-void
.end method

.method public addSemPred(Ljava/lang/String;)I
    .locals 1

    iget-object v0, p0, Lantlr/JavaCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {v0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    iget-object p0, p0, Lantlr/JavaCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {p0}, Lantlr/collections/impl/Vector;->size()I

    move-result p0

    add-int/lit8 p0, p0, -0x1

    return p0
.end method

.method public exitIfError()V
    .locals 1

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Tool;->hasError()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v0, "Exiting due to errors."

    invoke-virtual {p0, v0}, Lantlr/Tool;->fatalError(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public gen()V
    .locals 3

    :try_start_0
    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    iget-object v0, v0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/Grammar;

    iget-object v2, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-virtual {v1, v2}, Lantlr/Grammar;->setGrammarAnalyzer(Lantlr/LLkGrammarAnalyzer;)V

    invoke-virtual {v1, p0}, Lantlr/Grammar;->setCodeGenerator(Lantlr/CodeGenerator;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v2, v1}, Lantlr/LLkGrammarAnalyzer;->setGrammar(Lantlr/Grammar;)V

    invoke-direct {p0, v1}, Lantlr/JavaCodeGenerator;->setupGrammarParameters(Lantlr/Grammar;)V

    invoke-virtual {v1}, Lantlr/Grammar;->generate()V

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->exitIfError()V

    goto :goto_0

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    iget-object v0, v0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :goto_1
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_2

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/TokenManager;

    invoke-interface {v1}, Lantlr/TokenManager;->isReadOnly()Z

    move-result v2

    if-nez v2, :cond_1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->genTokenTypes(Lantlr/TokenManager;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->genTokenInterchange(Lantlr/TokenManager;)V

    :cond_1
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->exitIfError()V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception v0

    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1}, Lantlr/Tool;->reportException(Ljava/lang/Exception;Ljava/lang/String;)V

    :cond_2
    return-void
.end method

.method public gen(Lantlr/ActionElement;)V
    .locals 5

    const-string v0, ";"

    iget v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean v2, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v2, :cond_0

    sget-object v2, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "genAction("

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v4, ")"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-boolean v2, p1, Lantlr/ActionElement;->isSemPred:Z

    if-eqz v2, :cond_1

    iget-object v0, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    iget p1, p1, Lantlr/GrammarElement;->line:I

    invoke-virtual {p0, v0, p1}, Lantlr/JavaCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    goto/16 :goto_0

    :cond_1
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v2, :cond_2

    const-string v2, "if ( inputState.guessing==0 ) {"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    new-instance v2, Lantlr/ActionTransInfo;

    invoke-direct {v2}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v3, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    iget-object v4, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v3, p1, v4, v2}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    iget-object v3, v2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    if-eqz v3, :cond_3

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v4, v2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " = ("

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ")currentAST.root;"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->printAction(Ljava/lang/String;)V

    iget-boolean p1, v2, Lantlr/ActionTransInfo;->assignToRoot:Z

    const/16 v3, -0x3e7

    if-eqz p1, :cond_4

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "currentAST.root = "

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, v2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "currentAST.child = "

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, v2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "!=null &&"

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, v2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ".getFirstChild()!=null ?"

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v4, v2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ".getFirstChild() : "

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, v2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "currentAST.advanceChildToEnd();"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz p1, :cond_5

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    :cond_5
    :goto_0
    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/AlternativeBlock;)V
    .locals 4

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "gen("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const/16 v0, -0x3e7

    const-string v1, "{"

    invoke-virtual {p0, v1, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v2

    iput-object v2, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_1
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v2, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    const/4 v2, 0x1

    invoke-virtual {p0, p1, v2}, Lantlr/JavaCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/JavaBlockFinishingInfo;

    move-result-object v2

    iget-object v3, p0, Lantlr/JavaCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    invoke-direct {p0, v2, v3, p1}, Lantlr/JavaCodeGenerator;->genBlockFinish(Lantlr/JavaBlockFinishingInfo;Ljava/lang/String;I)V

    const-string p1, "}"

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iput-object v1, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;

    return-void
.end method

.method public gen(Lantlr/BlockEndElement;)V
    .locals 2

    iget-boolean p0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz p0, :cond_0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "genRuleEnd("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public gen(Lantlr/CharLiteralElement;)V
    .locals 3

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "genChar("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_1
    iget-boolean v0, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v1, :cond_2

    goto :goto_0

    :cond_2
    const/4 v1, 0x0

    :goto_0
    iput-boolean v1, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    iput-boolean v0, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    return-void
.end method

.method public gen(Lantlr/CharRangeElement;)V
    .locals 4

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p1}, Lantlr/CharRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_0

    iget v1, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v1, :cond_0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/CharRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " = "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ";"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_2

    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v1, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    const/4 v2, 0x3

    if-ne v1, v2, :cond_2

    :cond_1
    const/4 v1, 0x1

    goto :goto_0

    :cond_2
    const/4 v1, 0x0

    :goto_0
    if-eqz v1, :cond_3

    const-string v2, "_saveIndex=text.length();"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "matchRange("

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p1, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ","

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p1, p1, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ");"

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    if-eqz v1, :cond_4

    const-string p1, "text.setLength(_saveIndex);"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    :cond_4
    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/LexerGrammar;)V
    .locals 11

    const-string v0, ";"

    const-string v1, ""

    const-string v2, "public "

    const-string v3, "}"

    iget v4, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/16 v5, -0x3e7

    :try_start_0
    iput v5, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean v6, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v6, :cond_0

    new-instance v6, Lantlr/collections/impl/Vector;

    invoke-direct {v6}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v6, p0, Lantlr/JavaCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v6, v6, Lantlr/LexerGrammar;

    if-nez v6, :cond_1

    iget-object v6, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v7, "Internal error generating lexer"

    invoke-virtual {v6, v7}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v6

    iget-object v7, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-interface {v6, v7, v8}, Lantlr/JavaCodeGeneratorPrintWriterManager;->setupOutput(Lantlr/Tool;Lantlr/Grammar;)Ljava/io/PrintWriter;

    move-result-object v6

    iput-object v6, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 v6, 0x0

    iput-boolean v6, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    const/4 v7, 0x1

    iput-boolean v7, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    iput v6, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genHeader()V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_1

    :try_start_1
    iget-object v8, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v8, v1}, Lantlr/DefineGrammarSymbols;->getHeaderActionLine(Ljava/lang/String;)I

    move-result v8

    iput v8, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-object v8, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v8, v1}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :try_start_2
    iput v5, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const-string v5, "import java.io.InputStream;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.TokenStreamException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.TokenStreamIOException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.TokenStreamRecognitionException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.CharStreamException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.CharStreamIOException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.ANTLRException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import java.io.Reader;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import java.util.Hashtable;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "import antlr."

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.InputBuffer;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.ByteBuffer;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.CharBuffer;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.Token;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.CommonToken;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.RecognitionException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.NoViableAltForCharException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.MismatchedCharException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.TokenStream;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.ANTLRHashString;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.LexerSharedInputState;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.collections.impl.BitSet;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "import antlr.SemanticException;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v5}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v5, :cond_2

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->superClass:Ljava/lang/String;

    goto :goto_0

    :cond_2
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "antlr."

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    :goto_0
    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v8, :cond_3

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->comment:Ljava/lang/String;

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_3
    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v9, "classHeaderPrefix"

    invoke-virtual {v8, v9}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Lantlr/Token;
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_1

    const-string v9, "\""

    if-eqz v8, :cond_4

    :try_start_3
    invoke-virtual {v8}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v8

    invoke-static {v8, v9, v9}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_1

    if-eqz v8, :cond_4

    goto :goto_1

    :cond_4
    const-string v8, "public"

    :goto_1
    :try_start_4
    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, " "

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v10, "class "

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v10}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v10, " extends "

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, " implements "

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v8}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v8, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, ", TokenStream"

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v8, "classHeaderSuffix"

    invoke-virtual {v5, v8}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Lantlr/Token;

    if-eqz v5, :cond_5

    invoke-virtual {v5}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    invoke-static {v5, v9, v9}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    if-eqz v5, :cond_5

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, ", "

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    :cond_5
    const-string v5, " {"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v5}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v8}, Lantlr/Token;->getLine()I

    move-result v8

    iget-object v9, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v10, 0x0

    invoke-virtual {p0, v5, v8, v9, v10}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v5

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v8}, Lantlr/Token;->getLine()I

    move-result v8

    invoke-virtual {p0, v5, v8}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;I)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "(InputStream in) {"

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v7

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v5, "this(new ByteBuffer(in));"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v5, v7

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "(Reader in) {"

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v7

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v5, "this(new CharBuffer(in));"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v5, v7

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "(InputBuffer ib) {"

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v7

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v5, :cond_6

    const-string v5, "this(new LexerSharedInputState(new antlr.debug.DebuggingInputBuffer(ib)));"

    :goto_2
    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_3

    :cond_6
    const-string v5, "this(new LexerSharedInputState(ib));"

    goto :goto_2

    :goto_3
    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v5, v7

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "(LexerSharedInputState state) {"

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v7

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "super(state);"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_7

    const-string v2, "  ruleNames  = _ruleNames;"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "  semPredNames = _semPredNames;"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "  setupDebugging();"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "caseSensitiveLiterals = "

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean v5, p1, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "setCaseSensitive("

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean p1, p1, Lantlr/LexerGrammar;->caseSensitive:Z

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string p1, ");"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "literals = new Hashtable();"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1}, Lantlr/TokenManager;->getTokenSymbolKeys()Ljava/util/Enumeration;

    move-result-object p1

    :cond_8
    :goto_4
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v0

    if-eqz v0, :cond_a

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    invoke-virtual {v0, v6}, Ljava/lang/String;->charAt(I)C

    move-result v2

    const/16 v5, 0x22

    if-eq v2, v5, :cond_9

    goto :goto_4

    :cond_9
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2, v0}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v0

    instance-of v2, v0, Lantlr/StringLiteralSymbol;

    if-eqz v2, :cond_8

    check-cast v0, Lantlr/StringLiteralSymbol;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "literals.put(new ANTLRHashString("

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ", this), new Integer("

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v0

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, "));"

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_4

    :cond_a
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v7

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_d

    const-string p1, "private static final String _ruleNames[] = {"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object p1

    :cond_b
    :goto_5
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v0

    if-eqz v0, :cond_c

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/GrammarSymbol;

    instance-of v2, v0, Lantlr/RuleSymbol;

    if-eqz v2, :cond_b

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "  \""

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    check-cast v0, Lantlr/RuleSymbol;

    invoke-virtual {v0}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "\","

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_5

    :cond_c
    const-string p1, "};"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_d
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genNextToken()V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object p1

    move v0, v6

    :goto_6
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_f

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/RuleSymbol;

    invoke-virtual {v2}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v5

    const-string v7, "mnextToken"

    invoke-virtual {v5, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-nez v5, :cond_e

    add-int/lit8 v5, v0, 0x1

    invoke-virtual {p0, v2, v6, v0}, Lantlr/JavaCodeGenerator;->genRule(Lantlr/RuleSymbol;ZI)V

    move v0, v5

    :cond_e
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->exitIfError()V

    goto :goto_6

    :cond_f
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_10

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genSemPredMap()V

    :cond_10
    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-object v0, v0, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0}, Lantlr/collections/impl/BitSet;->size()I

    move-result v0

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p1

    invoke-interface {p1}, Lantlr/JavaCodeGeneratorPrintWriterManager;->finishOutput()V
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_1

    iput v4, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    :try_start_5
    iput v5, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_1

    :catchall_1
    move-exception p1

    iput v4, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/OneOrMoreBlock;)V
    .locals 13

    const-string v0, "}"

    iget v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean v2, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v2, :cond_0

    sget-object v2, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "gen+("

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v4, ")"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const-string v2, "{"

    const/16 v3, -0x3e7

    invoke-virtual {p0, v2, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_1

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "_cnt_"

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :cond_1
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "_cnt"

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    :goto_0
    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "int "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "=0;"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    goto :goto_1

    :cond_2
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "_loop"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v5, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    :goto_1
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, ":"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "do {"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v6, 0x1

    add-int/2addr v5, v6

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v5, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v7

    if-eqz v7, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v7

    iput-object v7, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_3
    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v7, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/OneOrMoreBlock;)Z

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v7, v7, Lantlr/Grammar;->maxk:I

    iget-boolean v8, p1, Lantlr/AlternativeBlock;->greedy:Z

    const/4 v9, 0x0

    if-nez v8, :cond_4

    iget v8, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v10, v10, Lantlr/Grammar;->maxk:I

    if-gt v8, v10, :cond_4

    iget-object v10, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aget-object v8, v10, v8

    invoke-virtual {v8}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v8

    if-eqz v8, :cond_4

    iget v7, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    goto :goto_2

    :cond_4
    iget-boolean v8, p1, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v8, :cond_5

    iget v8, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const v10, 0x7fffffff

    if-ne v8, v10, :cond_5

    :goto_2
    move v8, v7

    move v7, v6

    goto :goto_3

    :cond_5
    move v8, v7

    move v7, v9

    :goto_3
    const-string v10, "if ( "

    if-eqz v7, :cond_7

    :try_start_1
    iget-boolean v7, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v7, :cond_6

    sget-object v7, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string v12, "nongreedy (...)+ loop; exit depth is "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v12, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v7, v11}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_6
    iget-object v7, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    invoke-virtual {p0, v7, v8}, Lantlr/JavaCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object v7

    const-string v8, "// nongreedy exit test"

    invoke-virtual {p0, v8, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, ">=1 && "

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, ") break "

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, ";"

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    const/16 v7, -0x378

    invoke-virtual {p0, v3, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_7
    invoke-virtual {p0, p1, v9}, Lantlr/JavaCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/JavaBlockFinishingInfo;

    move-result-object v3

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, ">=1 ) { break "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "; } else {"

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/JavaCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    invoke-direct {p0, v3, v4, p1}, Lantlr/JavaCodeGenerator;->genBlockFinish(Lantlr/JavaBlockFinishingInfo;Ljava/lang/String;I)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "++;"

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v6

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "} while (true);"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iput-object v5, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/ParserGrammar;)V
    .locals 13

    const-string v0, "protected "

    const-string v1, ");"

    const-string v2, "public "

    const-string v3, "  tokenNames = _tokenNames;"

    const-string v4, "}"

    const-string v5, ""

    iget v6, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/16 v7, -0x3e7

    :try_start_0
    iput v7, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean v8, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v8, :cond_0

    new-instance v8, Lantlr/collections/impl/Vector;

    invoke-direct {v8}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v8, p0, Lantlr/JavaCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/ParserGrammar;

    if-nez p1, :cond_1

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v8, "Internal error generating parser"

    invoke-virtual {p1, v8}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p1

    iget-object v8, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-interface {p1, v8, v9}, Lantlr/JavaCodeGeneratorPrintWriterManager;->setupOutput(Lantlr/Tool;Lantlr/Grammar;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->buildAST:Z

    iput-boolean p1, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genHeader()V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_1

    :try_start_1
    iget-object v8, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v8, v5}, Lantlr/DefineGrammarSymbols;->getHeaderActionLine(Ljava/lang/String;)I

    move-result v8

    iput v8, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-object v8, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v8, v5}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :try_start_2
    iput v7, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const-string v7, "import antlr.TokenBuffer;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.TokenStreamException;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.TokenStreamIOException;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.ANTLRException;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "import antlr."

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, ";"

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.Token;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.TokenStream;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.RecognitionException;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.NoViableAltException;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.MismatchedTokenException;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.SemanticException;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.ParserSharedInputState;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.collections.impl.BitSet;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v7, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    if-eqz v7, :cond_2

    const-string v7, "import antlr.collections.AST;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import java.util.Hashtable;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.ASTFactory;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.ASTPair;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "import antlr.collections.impl.ASTArray;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v7}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v7, :cond_3

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->superClass:Ljava/lang/String;

    goto :goto_0

    :cond_3
    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "antlr."

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    :goto_0
    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v8, :cond_4

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->comment:Ljava/lang/String;

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_4
    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v9, "classHeaderPrefix"

    invoke-virtual {v8, v9}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Lantlr/Token;
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_1

    const-string v9, "\""

    if-eqz v8, :cond_5

    :try_start_3
    invoke-virtual {v8}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v8

    invoke-static {v8, v9, v9}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_1

    if-eqz v8, :cond_5

    goto :goto_1

    :cond_5
    const-string v8, "public"

    :goto_1
    :try_start_4
    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, " "

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v10, "class "

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v10}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v10, " extends "

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "       implements "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v8}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v8, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v8, "classHeaderSuffix"

    invoke-virtual {v7, v8}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Lantlr/Token;

    if-eqz v7, :cond_6

    invoke-virtual {v7}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v7

    invoke-static {v7, v9, v9}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    if-eqz v7, :cond_6

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, ", "

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    :cond_6
    const-string v7, " {"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v7, v7, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v7, :cond_9

    const-string v7, "private static final String _ruleNames[] = {"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v7}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v7

    :cond_7
    :goto_2
    invoke-interface {v7}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v8

    if-eqz v8, :cond_8

    invoke-interface {v7}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Lantlr/GrammarSymbol;

    instance-of v9, v8, Lantlr/RuleSymbol;

    if-eqz v9, :cond_7

    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    const-string v10, "  \""

    invoke-virtual {v9, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    check-cast v8, Lantlr/RuleSymbol;

    invoke-virtual {v8}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v9, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\","

    invoke-virtual {v9, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_2

    :cond_8
    const-string v7, "};"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v7}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v7

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v8}, Lantlr/Token;->getLine()I

    move-result v8

    iget-object v9, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v10, 0x0

    invoke-virtual {p0, v7, v8, v9, v10}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v7

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v8}, Lantlr/Token;->getLine()I

    move-result v8

    invoke-virtual {p0, v7, v8}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;I)V

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "(TokenBuffer tokenBuf, int k) {"

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "  super(tokenBuf,k);"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v7, v7, Lantlr/Grammar;->debuggingOutput:Z
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_1

    const-string v8, "  semPredNames = _semPredNames;"

    const-string v9, "  ruleNames  = _ruleNames;"

    if-eqz v7, :cond_a

    :try_start_5
    invoke-virtual {p0, v9}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "  setupDebugging(tokenBuf);"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v7, v7, Lantlr/Grammar;->buildAST:Z
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_1

    const-string v10, "  astFactory = new ASTFactory(getTokenTypeToASTClassMap());"

    const-string v11, "  buildTokenTypeASTClassMap();"

    if-eqz v7, :cond_b

    :try_start_6
    invoke-virtual {p0, v11}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_b
    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v12}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v7, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v12, "(TokenBuffer tokenBuf) {"

    invoke-virtual {v7, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v12, "  this(tokenBuf,"

    invoke-virtual {v7, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v12, v12, Lantlr/Grammar;->maxk:I

    invoke-virtual {v7, v12}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "(TokenStream lexer, int k) {"

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "  super(lexer,k);"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v0, :cond_c

    invoke-virtual {p0, v9}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "  setupDebugging(lexer);"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_d

    invoke-virtual {p0, v11}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_d
    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v7}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "(TokenStream lexer) {"

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "  this(lexer,"

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v7, v7, Lantlr/Grammar;->maxk:I

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "(ParserSharedInputState state) {"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "  super(state,"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v2, v2, Lantlr/Grammar;->maxk:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_e

    invoke-virtual {p0, v11}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_e
    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    move v1, p1

    :goto_3
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_11

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/GrammarSymbol;

    instance-of v3, v2, Lantlr/RuleSymbol;

    if-eqz v3, :cond_10

    check-cast v2, Lantlr/RuleSymbol;

    iget-object v3, v2, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->size()I

    move-result v3

    if-nez v3, :cond_f

    const/4 v3, 0x1

    goto :goto_4

    :cond_f
    move v3, p1

    :goto_4
    add-int/lit8 v7, v1, 0x1

    invoke-virtual {p0, v2, v3, v1}, Lantlr/JavaCodeGenerator;->genRule(Lantlr/RuleSymbol;ZI)V

    move v1, v7

    :cond_10
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->exitIfError()V

    goto :goto_3

    :cond_11
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genTokenStrings()V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->buildAST:Z

    if-eqz p1, :cond_12

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genTokenASTNodeMap()V

    :cond_12
    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->maxTokenType()I

    move-result v0

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_13

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genSemPredMap()V

    :cond_13
    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p1

    invoke-interface {p1}, Lantlr/JavaCodeGeneratorPrintWriterManager;->finishOutput()V
    :try_end_6
    .catchall {:try_start_6 .. :try_end_6} :catchall_1

    iput v6, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    :try_start_7
    iput v7, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
    :try_end_7
    .catchall {:try_start_7 .. :try_end_7} :catchall_1

    :catchall_1
    move-exception p1

    iput v6, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/RuleRefElement;)V
    .locals 7

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean v1, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v1, :cond_0

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "genRR("

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v3, ")"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v1, v2}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v1

    check-cast v1, Lantlr/RuleSymbol;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v2, "Rule \'"

    if-eqz v1, :cond_11

    :try_start_1
    invoke-virtual {v1}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v3

    if-nez v3, :cond_1

    goto/16 :goto_2

    :cond_1
    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/TreeWalkerGrammar;

    if-eqz v3, :cond_2

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_2

    iget v3, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v3, :cond_2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " = _t==ASTNULL ? null : "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ";"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/LexerGrammar;

    const/4 v4, 0x3

    if-eqz v3, :cond_4

    iget-boolean v3, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v3, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v3

    if-ne v3, v4, :cond_4

    :cond_3
    const-string v3, "_saveIndex=text.length();"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    invoke-virtual {p0}, Lantlr/CodeGenerator;->printTabs()V

    iget-object v3, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    if-eqz v3, :cond_6

    iget-object v1, v1, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-nez v1, :cond_5

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\' has no return type"

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v5

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v6

    invoke-virtual {v1, v2, v3, v5, v6}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_5
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "="

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_0

    :cond_6
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/LexerGrammar;

    if-nez v3, :cond_7

    iget v3, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v3, :cond_7

    iget-object v1, v1, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v1, :cond_7

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\' returns a value"

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v5

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v6

    invoke-virtual {v1, v2, v3, v5, v6}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_7
    :goto_0
    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->GenRuleInvocation(Lantlr/RuleRefElement;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_9

    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v1, :cond_8

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-ne v1, v4, :cond_9

    :cond_8
    const-string v1, "text.setLength(_saveIndex);"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iget v1, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v1, :cond_10

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v1, :cond_b

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-eqz v1, :cond_a

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_b

    :cond_a
    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    if-eqz v1, :cond_b

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    :cond_b
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-eqz v1, :cond_c

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_c

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "_AST = ("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ")returnAST;"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    if-eqz v1, :cond_f

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    const/4 v2, 0x1

    if-eq v1, v2, :cond_e

    const/4 v2, 0x2

    if-eq v1, v2, :cond_d

    goto :goto_1

    :cond_d
    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v2, "Internal: encountered ^ after rule reference"

    invoke-virtual {v1, v2}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_1

    :cond_e
    const-string v1, "astFactory.addASTChild(currentAST, returnAST);"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    :goto_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_10

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_10

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "=_returnToken;"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_10
    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :cond_11
    :goto_2
    :try_start_2
    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\' is not defined"

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result p1

    invoke-virtual {v1, v2, v3, v4, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/StringLiteralElement;)V
    .locals 3

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "genString("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_1

    iget v0, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_1
    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-boolean v0, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v1, :cond_2

    goto :goto_0

    :cond_2
    const/4 v1, 0x0

    :goto_0
    iput-boolean v1, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    iput-boolean v0, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    const-string v0, "_t = _t.getNextSibling();"

    invoke-virtual {p0, v0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_3
    return-void
.end method

.method public gen(Lantlr/TokenRangeElement;)V
    .locals 3

    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    invoke-virtual {p1}, Lantlr/TokenRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/TokenRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_0
    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    const-string v0, "matchRange("

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p1, Lantlr/TokenRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p1, Lantlr/TokenRangeElement;->endText:Ljava/lang/String;

    const-string v2, ");"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    return-void
.end method

.method public gen(Lantlr/TokenRefElement;)V
    .locals 3

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "genTokenRef("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Token reference found in lexer"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_2

    iget v0, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_2
    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    const-string v0, "_t = _t.getNextSibling();"

    invoke-virtual {p0, v0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_3
    return-void
.end method

.method public gen(Lantlr/TreeElement;)V
    .locals 7

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "AST __t"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, " = _t;"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v2}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " = _t==ASTNULL ? null :("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ")_t;"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    iget-object v2, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v2}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    invoke-virtual {p0, v1, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_0
    iget-object v1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    const/4 v2, 0x3

    const/4 v3, 0x1

    if-ne v1, v2, :cond_1

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v2, "Suffixing a root node with \'!\' is not implemented"

    :try_start_1
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v5

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v6

    invoke-virtual {v1, v2, v4, v5, v6}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    iget-object v1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v1, v3}, Lantlr/AlternativeElement;->setAutoGenType(I)V

    :cond_1
    iget-object v1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    const/4 v2, 0x2

    if-ne v1, v2, :cond_2

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    const-string v2, "Suffixing a root node with \'^\' is redundant; already a root"

    :try_start_2
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v5

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v6

    invoke-virtual {v1, v2, v4, v5, v6}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    iget-object v1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v1, v3}, Lantlr/AlternativeElement;->setAutoGenType(I)V

    :cond_2
    iget-object v1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-direct {p0, v1}, Lantlr/JavaCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-eqz v1, :cond_3

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "ASTPair __currentAST"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, " = currentAST.copy();"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "currentAST.root = currentAST.child;"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "currentAST.child = null;"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget-object v1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    instance-of v2, v1, Lantlr/WildcardElement;
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    if-eqz v2, :cond_4

    const-string v2, "if ( _t==null ) throw new MismatchedTokenException();"

    :try_start_3
    invoke-virtual {v1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    invoke-virtual {p0, v2, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    :goto_0
    const-string v1, "_t = _t.getFirstChild();"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const/4 v1, 0x0

    :goto_1
    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v2

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    if-ge v1, v2, :cond_6

    invoke-virtual {p1, v1}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v2

    iget-object v2, v2, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_2
    if-eqz v2, :cond_5

    invoke-virtual {v2}, Lantlr/GrammarElement;->generate()V

    iget-object v2, v2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_2

    :cond_5
    add-int/lit8 v1, v1, 0x1

    goto :goto_1

    :cond_6
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_0

    const-string v2, ";"

    if-eqz v1, :cond_7

    :try_start_4
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "currentAST = __currentAST"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "_t = __t"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget p1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "_t = _t.getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_0

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/TreeWalkerGrammar;)V
    .locals 9

    const-string v0, "}"

    const-string v1, ""

    iget v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/16 v3, -0x3e7

    :try_start_0
    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-nez p1, :cond_0

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v4, "Internal error generating tree-walker"

    invoke-virtual {p1, v4}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p1

    iget-object v4, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-interface {p1, v4, v5}, Lantlr/JavaCodeGeneratorPrintWriterManager;->setupOutput(Lantlr/Tool;Lantlr/Grammar;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->buildAST:Z

    iput-boolean p1, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genHeader()V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_1

    :try_start_1
    iget-object v4, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v4, v1}, Lantlr/DefineGrammarSymbols;->getHeaderActionLine(Ljava/lang/String;)I

    move-result v4

    iput v4, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-object v4, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v4, v1}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :try_start_2
    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "import antlr."

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ";"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.Token;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.collections.AST;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.RecognitionException;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.ANTLRException;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.NoViableAltException;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.MismatchedTokenException;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.SemanticException;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.collections.impl.BitSet;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.ASTPair;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "import antlr.collections.impl.ASTArray;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v3, :cond_1

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->superClass:Ljava/lang/String;

    goto :goto_0

    :cond_1
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "antlr."

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v4, :cond_2

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->comment:Ljava/lang/String;

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_2
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v5, "classHeaderPrefix"

    invoke-virtual {v4, v5}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/Token;
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_1

    const-string v5, "\""

    if-eqz v4, :cond_3

    :try_start_3
    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    invoke-static {v4, v5, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_1

    if-eqz v4, :cond_3

    goto :goto_1

    :cond_3
    const-string v4, "public"

    :goto_1
    :try_start_4
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " "

    invoke-virtual {v6, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "class "

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v6}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, " extends "

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "       implements "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v4}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v4, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v4, "classHeaderSuffix"

    invoke-virtual {v3, v4}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/Token;

    if-eqz v3, :cond_4

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3, v5, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, ", "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    :cond_4
    const-string v3, " {"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v4}, Lantlr/Token;->getLine()I

    move-result v4

    iget-object v5, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v6, 0x0

    invoke-virtual {p0, v3, v4, v5, v6}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v3

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v4}, Lantlr/Token;->getLine()I

    move-result v4

    invoke-virtual {p0, v3, v4}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;I)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "public "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "() {"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    add-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "tokenNames = _tokenNames;"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v3

    move v5, p1

    :goto_2
    invoke-interface {v3}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v6

    if-eqz v6, :cond_7

    invoke-interface {v3}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lantlr/GrammarSymbol;

    instance-of v7, v6, Lantlr/RuleSymbol;

    if-eqz v7, :cond_6

    check-cast v6, Lantlr/RuleSymbol;

    iget-object v7, v6, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v7}, Lantlr/collections/impl/Vector;->size()I

    move-result v7

    if-nez v7, :cond_5

    move v7, v4

    goto :goto_3

    :cond_5
    move v7, p1

    :goto_3
    add-int/lit8 v8, v5, 0x1

    invoke-virtual {p0, v6, v7, v5}, Lantlr/JavaCodeGenerator;->genRule(Lantlr/RuleSymbol;ZI)V

    move v5, v8

    :cond_6
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->exitIfError()V

    goto :goto_2

    :cond_7
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genTokenStrings()V

    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3}, Lantlr/TokenManager;->maxTokenType()I

    move-result v3

    invoke-virtual {p0, p1, v3}, Lantlr/JavaCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p1

    invoke-interface {p1}, Lantlr/JavaCodeGeneratorPrintWriterManager;->finishOutput()V
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_1

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    :try_start_5
    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_1

    :catchall_1
    move-exception p1

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/WildcardElement;)V
    .locals 3

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_0

    iget v1, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v1, :cond_0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " = "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ";"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_1

    const-string p1, "if ( _t==null ) throw new MismatchedTokenException();"

    :goto_0
    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_5

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    const/4 v2, 0x3

    if-eqz v1, :cond_3

    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v1, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-ne v1, v2, :cond_3

    :cond_2
    const-string v1, "_saveIndex=text.length();"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    const-string v1, "matchNot(EOF_CHAR);"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_6

    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v1, :cond_4

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v2, :cond_6

    :cond_4
    const-string p1, "text.setLength(_saveIndex);"

    goto :goto_0

    :cond_5
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "matchNot("

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/4 v1, 0x1

    invoke-direct {p0, v1}, Lantlr/JavaCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ");"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    goto :goto_0

    :cond_6
    :goto_1
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_7

    const-string p1, "_t = _t.getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    :cond_7
    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public gen(Lantlr/ZeroOrMoreBlock;)V
    .locals 10

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean v1, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v1, :cond_0

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "gen*("

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v3, ")"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const-string v1, "{"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    goto :goto_0

    :cond_1
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "_loop"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :goto_0
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ":"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "do {"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v2, v3

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v2, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    iput-object v4, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_2
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v4, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/ZeroOrMoreBlock;)Z

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v4, v4, Lantlr/Grammar;->maxk:I

    iget-boolean v5, p1, Lantlr/AlternativeBlock;->greedy:Z

    const/4 v6, 0x0

    if-nez v5, :cond_3

    iget v5, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v7, v7, Lantlr/Grammar;->maxk:I

    if-gt v5, v7, :cond_3

    iget-object v7, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aget-object v5, v7, v5

    invoke-virtual {v5}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v5

    if-eqz v5, :cond_3

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    goto :goto_1

    :cond_3
    iget-boolean v5, p1, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v5, :cond_4

    iget v5, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const v7, 0x7fffffff

    if-ne v5, v7, :cond_4

    :goto_1
    move v5, v4

    move v4, v3

    goto :goto_2

    :cond_4
    move v5, v4

    move v4, v6

    :goto_2
    const-string v7, ";"

    if-eqz v4, :cond_6

    :try_start_1
    iget-boolean v4, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v4, :cond_5

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "nongreedy (...)* loop; exit depth is "

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v9, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v4, v8}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v4, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    invoke-virtual {p0, v4, v5}, Lantlr/JavaCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object v4

    const-string v5, "// nongreedy exit test"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "if ("

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ") break "

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    invoke-virtual {p0, p1, v6}, Lantlr/JavaCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/JavaBlockFinishingInfo;

    move-result-object v4

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "break "

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    invoke-direct {p0, v4, v1, p1}, Lantlr/JavaCodeGenerator;->genBlockFinish(Lantlr/JavaBlockFinishingInfo;Ljava/lang/String;I)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v3

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "} while (true);"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iput-object v2, p0, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;)V
    .locals 1

    iget-object v0, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, p1, v0, p2}, Lantlr/JavaCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lantlr/JavaCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {v0, p1}, Ljava/util/Hashtable;->contains(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    return-void

    :cond_0
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p3, " "

    invoke-virtual {v0, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "_AST = null;"

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p0, p0, Lantlr/JavaCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {p0, p1, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V
    .locals 8

    iget-boolean v0, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    const/4 v1, 0x0

    const/4 v2, 0x1

    if-eqz v0, :cond_0

    invoke-virtual {p1}, Lantlr/Alternative;->getAutoGen()Z

    move-result v3

    if-eqz v3, :cond_0

    move v3, v2

    goto :goto_0

    :cond_0
    move v3, v1

    :goto_0
    iput-boolean v3, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    iget-boolean v3, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v3, :cond_1

    invoke-virtual {p1}, Lantlr/Alternative;->getAutoGen()Z

    move-result v4

    if-eqz v4, :cond_1

    move v1, v2

    :cond_1
    iput-boolean v1, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    new-instance v4, Ljava/util/Hashtable;

    invoke-direct {v4}, Ljava/util/Hashtable;-><init>()V

    iput-object v4, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    iget-object v4, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz v4, :cond_2

    iget-object v4, p1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v4}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    const-string v5, "try {      // for error handling"

    invoke-virtual {p0, v5, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    iget-object v4, p1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_1
    instance-of v5, v4, Lantlr/BlockEndElement;

    if-nez v5, :cond_3

    invoke-virtual {v4}, Lantlr/GrammarElement;->generate()V

    iget-object v4, v4, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_1

    :cond_3
    iget-boolean v4, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    if-eqz v4, :cond_5

    instance-of v4, p2, Lantlr/RuleBlock;

    if-eqz v4, :cond_4

    check-cast p2, Lantlr/RuleBlock;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->hasSyntacticPredicate:Z

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "_AST = ("

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p2, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ")currentAST.root;"

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    const/16 v4, -0x378

    invoke-virtual {p0, p2, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p2, p2, Lantlr/Grammar;->hasSyntacticPredicate:Z

    goto :goto_2

    :cond_4
    invoke-virtual {p2}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_5

    iget-object v4, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v5}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p2}, Lantlr/GrammarElement;->getLine()I

    move-result v6

    invoke-virtual {p2}, Lantlr/GrammarElement;->getColumn()I

    move-result p2

    const-string v7, "Labeled subrules not yet supported"

    invoke-virtual {v4, v7, v5, v6, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_5
    :goto_2
    iget-object p2, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz p2, :cond_6

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p2, v2

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    const/16 p2, -0x3e7

    const-string v2, "}"

    invoke-virtual {p0, v2, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget-object p1, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    :cond_6
    iput-boolean v0, p0, Lantlr/JavaCodeGenerator;->genAST:Z

    iput-boolean v3, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    iput-object v1, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    return-void
.end method

.method public genBitsets(Lantlr/collections/impl/Vector;I)V
    .locals 2

    const-string v0, ""

    const/16 v1, -0x3e7

    invoke-virtual {p0, v0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    invoke-virtual {p1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/collections/impl/BitSet;

    invoke-virtual {v1, p2}, Lantlr/collections/impl/BitSet;->growToInclude(I)V

    invoke-direct {p0, v1, v0}, Lantlr/JavaCodeGenerator;->genBitSet(Lantlr/collections/impl/BitSet;I)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public genBlockInitAction(Lantlr/AlternativeBlock;)V
    .locals 4

    iget-object v0, p1, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iget-object v2, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v3, 0x0

    invoke-virtual {p0, v0, v1, v2, v3}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    invoke-virtual {p0, v0, p1}, Lantlr/JavaCodeGenerator;->printAction(Ljava/lang/String;I)V

    :cond_0
    return-void
.end method

.method public genBlockPreamble(Lantlr/AlternativeBlock;)V
    .locals 9

    instance-of v0, p1, Lantlr/RuleBlock;

    if-eqz v0, :cond_7

    check-cast p1, Lantlr/RuleBlock;

    iget-object v0, p1, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    if-eqz v0, :cond_7

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p1, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_7

    iget-object v1, p1, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/AlternativeElement;

    iget v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {v1}, Lantlr/GrammarElement;->getLine()I

    move-result v3

    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    instance-of v3, v1, Lantlr/RuleRefElement;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v4, ";"

    const-string v5, " = "

    const-string v6, " "

    if-nez v3, :cond_1

    :try_start_1
    instance-of v3, v1, Lantlr/AlternativeBlock;

    if-eqz v3, :cond_0

    instance-of v3, v1, Lantlr/RuleBlock;

    if-nez v3, :cond_0

    instance-of v3, v1, Lantlr/SynPredBlock;

    if-nez v3, :cond_0

    goto :goto_1

    :cond_0
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lantlr/JavaCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/JavaCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->buildAST:Z

    if-eqz v3, :cond_6

    instance-of v3, v1, Lantlr/GrammarAtom;

    if-eqz v3, :cond_2

    move-object v3, v1

    check-cast v3, Lantlr/GrammarAtom;

    invoke-virtual {v3}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_2

    move-object v3, v1

    check-cast v3, Lantlr/GrammarAtom;

    invoke-virtual {v3}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v1, v3}, Lantlr/JavaCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V

    goto/16 :goto_2

    :cond_1
    :goto_1
    instance-of v3, v1, Lantlr/RuleRefElement;

    if-nez v3, :cond_3

    move-object v3, v1

    check-cast v3, Lantlr/AlternativeBlock;

    iget-boolean v3, v3, Lantlr/AlternativeBlock;->not:Z

    if-eqz v3, :cond_3

    iget-object v3, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    move-object v7, v1

    check-cast v7, Lantlr/AlternativeBlock;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v8, v8, Lantlr/LexerGrammar;

    invoke-interface {v3, v7, v8}, Lantlr/LLkGrammarAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result v3

    if-eqz v3, :cond_3

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lantlr/JavaCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/JavaCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->buildAST:Z

    if-eqz v3, :cond_6

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;)V

    goto :goto_2

    :cond_3
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->buildAST:Z

    if-eqz v3, :cond_4

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;)V

    :cond_4
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/LexerGrammar;

    if-eqz v3, :cond_5

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "Token "

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "=null;"

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/TreeWalkerGrammar;

    if-eqz v3, :cond_6

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lantlr/JavaCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :cond_6
    :goto_2
    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    add-int/lit8 v0, v0, 0x1

    goto/16 :goto_0

    :catchall_0
    move-exception p1

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1

    :cond_7
    return-void
.end method

.method public genCases(Lantlr/collections/impl/BitSet;I)V
    .locals 8

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    iput p2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean p2, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz p2, :cond_0

    sget-object p2, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "genCases("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p2, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object p1

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/LexerGrammar;

    const/4 v1, 0x1

    if-eqz p2, :cond_1

    const/4 p2, 0x4

    goto :goto_0

    :cond_1
    move p2, v1

    :goto_0
    const/4 v2, 0x0

    move v4, v1

    move v5, v4

    move v3, v2

    :goto_1
    array-length v6, p1
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v7, ""

    if-ge v3, v6, :cond_4

    if-ne v5, v1, :cond_2

    :try_start_1
    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_2

    :cond_2
    const-string v4, "  "

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    :goto_2
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "case "

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget v6, p1, v3

    invoke-direct {p0, v6}, Lantlr/JavaCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, ":"

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    if-ne v5, p2, :cond_3

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    move v4, v1

    move v5, v4

    goto :goto_3

    :cond_3
    add-int/lit8 v5, v5, 0x1

    move v4, v2

    :goto_3
    add-int/lit8 v3, v3, 0x1

    goto :goto_1

    :cond_4
    if-nez v4, :cond_5

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :cond_5
    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/JavaBlockFinishingInfo;
    .locals 26

    move-object/from16 v1, p0

    move-object/from16 v0, p1

    iget v2, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarElement;->getLine()I

    move-result v3

    iput v3, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    new-instance v3, Lantlr/JavaBlockFinishingInfo;

    invoke-direct {v3}, Lantlr/JavaBlockFinishingInfo;-><init>()V

    iget-boolean v4, v1, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v4, :cond_0

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "genCommonBlock("

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v6, ")"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-boolean v4, v1, Lantlr/JavaCodeGenerator;->genAST:Z

    iget-boolean v5, v1, Lantlr/JavaCodeGenerator;->genAST:Z

    const/4 v6, 0x0

    const/4 v7, 0x1

    if-eqz v5, :cond_1

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result v5

    if-eqz v5, :cond_1

    move v5, v7

    goto :goto_0

    :cond_1
    move v5, v6

    :goto_0
    iput-boolean v5, v1, Lantlr/JavaCodeGenerator;->genAST:Z

    iget-boolean v5, v1, Lantlr/JavaCodeGenerator;->saveText:Z

    iget-boolean v8, v1, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v8, :cond_2

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result v8

    if-eqz v8, :cond_2

    move v8, v7

    goto :goto_1

    :cond_2
    move v8, v6

    :goto_1
    iput-boolean v8, v1, Lantlr/JavaCodeGenerator;->saveText:Z

    iget-boolean v8, v0, Lantlr/AlternativeBlock;->not:Z
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_2

    const-string v9, ""

    if-eqz v8, :cond_7

    :try_start_1
    iget-object v8, v1, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v10, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v10, v10, Lantlr/LexerGrammar;

    invoke-interface {v8, v0, v10}, Lantlr/LLkGrammarAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result v8

    if-eqz v8, :cond_7

    iget-boolean v4, v1, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v4, :cond_3

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v5, "special case: ~(subrule)"

    invoke-virtual {v4, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_3
    iget-object v4, v1, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v4, v7, v0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object v4

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v5

    if-eqz v5, :cond_4

    iget v5, v1, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    if-nez v5, :cond_4

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, " = "

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, v1, Lantlr/JavaCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, ";"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    invoke-direct/range {p0 .. p1}, Lantlr/JavaCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v0, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_2

    if-eqz v0, :cond_5

    const-string v9, "_t,"

    :cond_5
    :try_start_2
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "match("

    invoke-virtual {v0, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, v4, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v1, v4}, Lantlr/CodeGenerator;->markBitsetForGen(Lantlr/collections/impl/BitSet;)I

    move-result v4

    invoke-virtual {v1, v4}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ");"

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_6

    const-string v0, "_t = _t.getNextSibling();"

    invoke-virtual {v1, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_2

    :cond_6
    iput v2, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-object v3

    :cond_7
    :try_start_3
    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v8

    invoke-virtual {v8}, Lantlr/collections/impl/Vector;->size()I

    move-result v8

    if-ne v8, v7, :cond_a

    invoke-virtual {v0, v6}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v8

    iget-object v10, v8, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v10, :cond_8

    iget-object v10, v1, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_2

    const-string v11, "Syntactic predicate superfluous for single alternative"

    :try_start_4
    iget-object v12, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v12}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v0, v6}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v13

    iget-object v13, v13, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v13}, Lantlr/GrammarElement;->getLine()I

    move-result v13

    invoke-virtual {v0, v6}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v14

    iget-object v14, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v14}, Lantlr/GrammarElement;->getColumn()I

    move-result v14

    invoke-virtual {v10, v11, v12, v13, v14}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_8
    if-eqz p2, :cond_a

    iget-object v4, v8, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v4, :cond_9

    iget v5, v0, Lantlr/GrammarElement;->line:I

    invoke-virtual {v1, v4, v5}, Lantlr/JavaCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    :cond_9
    invoke-virtual {v1, v8, v0}, Lantlr/JavaCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_2

    iput v2, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-object v3

    :cond_a
    move v8, v6

    move v10, v8

    :goto_2
    :try_start_5
    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v11

    invoke-virtual {v11}, Lantlr/collections/impl/Vector;->size()I

    move-result v11

    if-ge v8, v11, :cond_c

    invoke-virtual {v0, v8}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v11

    invoke-static {v11}, Lantlr/JavaCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v11

    if-eqz v11, :cond_b

    add-int/lit8 v10, v10, 0x1

    :cond_b
    add-int/lit8 v8, v8, 0x1

    goto :goto_2

    :cond_c
    iget v8, v1, Lantlr/CodeGenerator;->makeSwitchThreshold:I
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_2

    const-string v11, "{"

    const-string v12, "if (_t==null) _t=ASTNULL;"

    const-string v13, "}"

    if-lt v10, v8, :cond_11

    :try_start_6
    invoke-direct {v1, v7}, Lantlr/JavaCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v8

    iget-object v10, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v10, v10, Lantlr/TreeWalkerGrammar;

    if-eqz v10, :cond_d

    invoke-virtual {v1, v12}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_d
    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    const-string v14, "switch ( "

    invoke-virtual {v10, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, ") {"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v1, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    move v8, v6

    :goto_3
    iget-object v10, v0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v10}, Lantlr/collections/impl/Vector;->size()I

    move-result v10

    if-ge v8, v10, :cond_10

    invoke-virtual {v0, v8}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v10

    invoke-static {v10}, Lantlr/JavaCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v14

    if-nez v14, :cond_e

    goto :goto_4

    :cond_e
    iget-object v14, v10, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v14, v14, v7

    iget-object v15, v14, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v15}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v15

    if-nez v15, :cond_f

    invoke-virtual {v14}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v15

    if-nez v15, :cond_f

    iget-object v14, v1, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;
    :try_end_6
    .catchall {:try_start_6 .. :try_end_6} :catchall_2

    const-string v15, "Alternate omitted due to empty prediction set"

    :try_start_7
    iget-object v6, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v6}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v6

    iget-object v7, v10, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v7}, Lantlr/GrammarElement;->getLine()I

    move-result v7

    iget-object v10, v10, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v10}, Lantlr/GrammarElement;->getColumn()I

    move-result v10

    invoke-virtual {v14, v15, v6, v7, v10}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_4

    :cond_f
    iget-object v6, v14, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    iget-object v7, v10, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v7}, Lantlr/GrammarElement;->getLine()I

    move-result v7

    invoke-virtual {v1, v6, v7}, Lantlr/JavaCodeGenerator;->genCases(Lantlr/collections/impl/BitSet;I)V

    iget-object v6, v10, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v6}, Lantlr/GrammarElement;->getLine()I

    move-result v6

    invoke-virtual {v1, v11, v6}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget v6, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v7, 0x1

    add-int/2addr v6, v7

    iput v6, v1, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v1, v10, v0}, Lantlr/JavaCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    const-string v6, "break;"

    const/16 v7, -0x3e7

    invoke-virtual {v1, v6, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget v6, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v10, 0x1

    sub-int/2addr v6, v10

    iput v6, v1, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v1, v13, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :goto_4
    add-int/lit8 v8, v8, 0x1

    const/4 v6, 0x0

    const/4 v7, 0x1

    goto :goto_3

    :cond_10
    const-string v6, "default:"

    invoke-virtual {v1, v6}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v6, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v7, 0x1

    add-int/2addr v6, v7

    iput v6, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v6, 0x1

    goto :goto_5

    :cond_11
    const/4 v6, 0x0

    :goto_5
    iget-object v7, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v7, v7, Lantlr/LexerGrammar;

    if-eqz v7, :cond_12

    iget-object v7, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v7, v7, Lantlr/Grammar;->maxk:I

    goto :goto_6

    :cond_12
    const/4 v7, 0x0

    :goto_6
    const/4 v8, 0x0

    const/4 v10, 0x0

    :goto_7
    if-ltz v7, :cond_27

    iget-boolean v14, v1, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v14, :cond_13

    sget-object v14, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v15, Ljava/lang/StringBuilder;

    invoke-direct {v15}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v17, v9

    const-string v9, "checking depth "

    invoke-virtual {v15, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v15, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v15}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v14, v9}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_8

    :cond_13
    move-object/from16 v17, v9

    :goto_8
    move v9, v8

    const/4 v8, 0x0

    :goto_9
    iget-object v14, v0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v14}, Lantlr/collections/impl/Vector;->size()I

    move-result v14

    if-ge v8, v14, :cond_26

    invoke-virtual {v0, v8}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v14

    iget-boolean v15, v1, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v15, :cond_14

    sget-object v15, Ljava/lang/System;->out:Ljava/io/PrintStream;
    :try_end_7
    .catchall {:try_start_7 .. :try_end_7} :catchall_2

    move/from16 v18, v2

    :try_start_8
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    move/from16 v19, v5

    const-string v5, "genAlt: "

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v15, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_a

    :cond_14
    move/from16 v18, v2

    move/from16 v19, v5

    :goto_a
    if-eqz v6, :cond_15

    invoke-static {v14}, Lantlr/JavaCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v2

    if-eqz v2, :cond_15

    iget-boolean v2, v1, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v2, :cond_18

    sget-object v2, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v5, "ignoring alt because it was in the switch"

    invoke-virtual {v2, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_c

    :cond_15
    iget-object v2, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    if-eqz v2, :cond_1a

    iget v2, v14, Lantlr/Alternative;->lookaheadDepth:I

    const v5, 0x7fffffff

    if-ne v2, v5, :cond_16

    iget-object v2, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v2, v2, Lantlr/Grammar;->maxk:I

    :cond_16
    :goto_b
    const/4 v5, 0x1

    if-lt v2, v5, :cond_17

    iget-object v5, v14, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v5, v5, v2

    invoke-virtual {v5}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v5

    if-eqz v5, :cond_17

    add-int/lit8 v2, v2, -0x1

    goto :goto_b

    :cond_17
    if-eq v2, v7, :cond_19

    iget-boolean v5, v1, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v5, :cond_18

    sget-object v5, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v14, Ljava/lang/StringBuilder;

    invoke-direct {v14}, Ljava/lang/StringBuilder;-><init>()V

    const-string v15, "ignoring alt because effectiveDepth!=altDepth;"

    invoke-virtual {v14, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, "!="

    invoke-virtual {v14, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v14}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v5, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_18
    :goto_c
    move-object/from16 v25, v3

    move/from16 v20, v4

    move/from16 p2, v6

    move/from16 v22, v7

    move/from16 v23, v8

    move-object/from16 v21, v11

    move-object v2, v13

    goto/16 :goto_17

    :cond_19
    invoke-virtual {v1, v14, v2}, Lantlr/JavaCodeGenerator;->lookaheadIsEmpty(Lantlr/Alternative;I)Z

    move-result v5

    :goto_d
    invoke-virtual {v1, v14, v2}, Lantlr/JavaCodeGenerator;->getLookaheadTestExpression(Lantlr/Alternative;I)Ljava/lang/String;

    move-result-object v2

    goto :goto_e

    :cond_1a
    iget-object v2, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v2, v2, Lantlr/Grammar;->maxk:I

    invoke-virtual {v1, v14, v2}, Lantlr/JavaCodeGenerator;->lookaheadIsEmpty(Lantlr/Alternative;I)Z

    move-result v5

    iget-object v2, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v2, v2, Lantlr/Grammar;->maxk:I

    goto :goto_d

    :goto_e
    iget v15, v1, Lantlr/JavaCodeGenerator;->defaultLine:I
    :try_end_8
    .catchall {:try_start_8 .. :try_end_8} :catchall_1

    move/from16 p2, v6

    :try_start_9
    iget-object v6, v14, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v6}, Lantlr/GrammarElement;->getLine()I

    move-result v6

    iput v6, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-object v6, v14, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    const/16 v16, 0x1

    aget-object v6, v6, v16

    iget-object v6, v6, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v6}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v6
    :try_end_9
    .catchall {:try_start_9 .. :try_end_9} :catchall_0

    move/from16 v20, v4

    const-string v4, "if "

    move/from16 v22, v7

    const-string v7, "else if "

    move/from16 v23, v8

    const-string v8, " {"

    move-object/from16 v24, v13

    const/16 v13, 0x7f

    if-le v6, v13, :cond_1c

    :try_start_a
    invoke-static {v14}, Lantlr/JavaCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v6

    if-eqz v6, :cond_1c

    if-nez v9, :cond_1b

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    :goto_f
    move-object/from16 v25, v3

    move-object/from16 v21, v11

    goto/16 :goto_15

    :cond_1b
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2
    :try_end_a
    .catchall {:try_start_a .. :try_end_a} :catchall_0

    goto :goto_f

    :cond_1c
    const-string v6, "else {"

    if-eqz v5, :cond_1e

    :try_start_b
    iget-object v5, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-nez v5, :cond_1e

    iget-object v5, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-nez v5, :cond_1e

    if-nez v9, :cond_1d

    invoke-virtual {v1, v11}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_10

    :cond_1d
    invoke-virtual {v1, v6}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :goto_10
    const/4 v2, 0x0

    iput-boolean v2, v3, Lantlr/JavaBlockFinishingInfo;->needAnErrorClause:Z

    move-object/from16 v25, v3

    move-object/from16 v21, v11

    goto/16 :goto_16

    :cond_1e
    iget-object v5, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v5, :cond_21

    new-instance v5, Lantlr/ActionTransInfo;

    invoke-direct {v5}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v13, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    move-object/from16 v21, v11

    iget v11, v0, Lantlr/GrammarElement;->line:I

    move-object/from16 v25, v3

    iget-object v3, v1, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v1, v13, v11, v3, v5}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v3

    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/ParserGrammar;
    :try_end_b
    .catchall {:try_start_b .. :try_end_b} :catchall_0

    const-string v11, "))"

    const-string v13, "("

    if-nez v5, :cond_1f

    :try_start_c
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_20

    :cond_1f
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v5, :cond_20

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "&& fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.PREDICTING,"

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, v1, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v2, v3}, Lantlr/CharFormatter;->escapeString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Lantlr/JavaCodeGenerator;->addSemPred(Ljava/lang/String;)I

    move-result v2

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_11
    invoke-virtual {v5, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_12

    :cond_20
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "&&("

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_11

    :goto_12
    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    goto :goto_13

    :cond_21
    move-object/from16 v25, v3

    move-object/from16 v21, v11

    :goto_13
    if-lez v9, :cond_23

    iget-object v3, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v3, :cond_22

    invoke-virtual {v3}, Lantlr/GrammarElement;->getLine()I

    move-result v3

    invoke-virtual {v1, v6, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    iget v3, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    add-int/2addr v3, v4

    iput v3, v1, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v1, v3, v2}, Lantlr/JavaCodeGenerator;->genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V

    add-int/lit8 v10, v10, 0x1

    goto :goto_16

    :cond_22
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_14
    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    goto :goto_15

    :cond_23
    iget-object v3, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v3, :cond_24

    invoke-virtual {v1, v3, v2}, Lantlr/JavaCodeGenerator;->genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V

    goto :goto_16

    :cond_24
    iget-object v3, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/TreeWalkerGrammar;

    if-eqz v3, :cond_25

    invoke-virtual {v1, v12}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_25
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_14

    :goto_15
    invoke-virtual {v1, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_c
    .catchall {:try_start_c .. :try_end_c} :catchall_0

    :goto_16
    :try_start_d
    iput v15, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    add-int/lit8 v9, v9, 0x1

    iget v2, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v2, v3

    iput v2, v1, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v1, v14, v0}, Lantlr/JavaCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    iget v2, v1, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v3

    iput v2, v1, Lantlr/CodeGenerator;->tabs:I

    move-object/from16 v2, v24

    invoke-virtual {v1, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :goto_17
    add-int/lit8 v8, v23, 0x1

    move/from16 v6, p2

    move-object v13, v2

    move/from16 v2, v18

    move/from16 v5, v19

    move/from16 v4, v20

    move-object/from16 v11, v21

    move/from16 v7, v22

    move-object/from16 v3, v25

    goto/16 :goto_9

    :catchall_0
    move-exception v0

    iput v15, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw v0

    :cond_26
    move/from16 v18, v2

    move-object/from16 v25, v3

    move/from16 v20, v4

    move/from16 v19, v5

    move/from16 p2, v6

    move/from16 v22, v7

    move-object/from16 v21, v11

    move-object v2, v13

    add-int/lit8 v7, v22, -0x1

    move v8, v9

    move-object/from16 v9, v17

    move/from16 v2, v18

    goto/16 :goto_7

    :cond_27
    move/from16 v18, v2

    move-object/from16 v25, v3

    move/from16 v20, v4

    move/from16 v19, v5

    move/from16 p2, v6

    move-object/from16 v17, v9

    move-object v2, v13

    move-object/from16 v3, v17

    const/4 v0, 0x1

    :goto_18
    if-gt v0, v10, :cond_28

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    add-int/lit8 v0, v0, 0x1

    goto :goto_18

    :cond_28
    move/from16 v0, v20

    iput-boolean v0, v1, Lantlr/JavaCodeGenerator;->genAST:Z

    move/from16 v0, v19

    iput-boolean v0, v1, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz p2, :cond_2a

    iget v0, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    sub-int/2addr v0, v4

    iput v0, v1, Lantlr/CodeGenerator;->tabs:I

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    move-object/from16 v2, v25

    iput-object v0, v2, Lantlr/JavaBlockFinishingInfo;->postscript:Ljava/lang/String;

    const/4 v7, 0x1

    iput-boolean v7, v2, Lantlr/JavaBlockFinishingInfo;->generatedSwitch:Z

    if-lez v8, :cond_29

    goto :goto_19

    :cond_29
    const/4 v7, 0x0

    goto :goto_19

    :cond_2a
    move-object/from16 v2, v25

    const/4 v7, 0x1

    iput-object v3, v2, Lantlr/JavaBlockFinishingInfo;->postscript:Ljava/lang/String;

    const/4 v0, 0x0

    iput-boolean v0, v2, Lantlr/JavaBlockFinishingInfo;->generatedSwitch:Z

    if-lez v8, :cond_2b

    goto :goto_19

    :cond_2b
    move v7, v0

    :goto_19
    iput-boolean v7, v2, Lantlr/JavaBlockFinishingInfo;->generatedAnIf:Z
    :try_end_d
    .catchall {:try_start_d .. :try_end_d} :catchall_1

    move/from16 v3, v18

    iput v3, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-object v2

    :catchall_1
    move-exception v0

    move/from16 v3, v18

    goto :goto_1a

    :catchall_2
    move-exception v0

    move v3, v2

    :goto_1a
    iput v3, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw v0
.end method

.method public genHeader()V
    .locals 3

    const-string v0, "// $ANTLR "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    sget-object v1, Lantlr/Tool;->version:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ": \""

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v2, v1, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    invoke-virtual {v1, v2}, Lantlr/Tool;->fileMinusPath(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\" -> \""

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".java\"$"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const/16 v1, -0x3e7

    invoke-virtual {p0, v0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    return-void
.end method

.method public genMatch(Lantlr/GrammarAtom;)V
    .locals 2

    instance-of v0, p1, Lantlr/StringLiteralElement;

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genMatchUsingAtomTokenType(Lantlr/GrammarAtom;)V

    goto :goto_1

    :cond_1
    instance-of v0, p1, Lantlr/CharLiteralElement;

    if-eqz v0, :cond_3

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_2

    goto :goto_0

    :cond_2
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "cannot ref character literals in grammar: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_1

    :cond_3
    instance-of v0, p1, Lantlr/TokenRefElement;

    if-eqz v0, :cond_4

    :goto_0
    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->genMatchUsingAtomText(Lantlr/GrammarAtom;)V

    goto :goto_1

    :cond_4
    instance-of v0, p1, Lantlr/WildcardElement;

    if-eqz v0, :cond_5

    check-cast p1, Lantlr/WildcardElement;

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->gen(Lantlr/WildcardElement;)V

    :cond_5
    :goto_1
    return-void
.end method

.method public genMatch(Lantlr/collections/impl/BitSet;)V
    .locals 0

    return-void
.end method

.method public genMatchUsingAtomText(Lantlr/GrammarAtom;)V
    .locals 4

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    if-eqz v1, :cond_0

    const-string v1, "_t,"

    goto :goto_0

    :cond_0
    const-string v1, ""

    :goto_0
    :try_start_1
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    const/4 v3, 0x3

    if-eqz v2, :cond_2

    iget-boolean v2, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v2, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v3, :cond_2

    :cond_1
    const-string v2, "_saveIndex=text.length();"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    iget-boolean v2, p1, Lantlr/GrammarAtom;->not:Z
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    if-eqz v2, :cond_3

    const-string v2, "matchNot("

    goto :goto_1

    :cond_3
    const-string v2, "match("

    :goto_1
    :try_start_2
    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    const/16 v2, -0x3e7

    invoke-virtual {p0, v1, v2}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;I)V

    iget-object v1, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    const-string v2, "EOF"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_4

    const-string v1, "Token.EOF_TYPE"

    :goto_2
    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_3

    :cond_4
    iget-object v1, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    goto :goto_2

    :goto_3
    const-string v1, ");"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_6

    iget-boolean v1, p0, Lantlr/JavaCodeGenerator;->saveText:Z

    if-eqz v1, :cond_5

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v3, :cond_6

    :cond_5
    const-string p1, "text.setLength(_saveIndex);"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    :cond_6
    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public genMatchUsingAtomTokenType(Lantlr/GrammarAtom;)V
    .locals 3

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_0

    const-string v0, "_t,"

    goto :goto_0

    :cond_0
    const-string v0, ""

    :goto_0
    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getType()I

    move-result v1

    invoke-direct {p0, v1}, Lantlr/JavaCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-boolean v2, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v2, :cond_1

    const-string v2, "matchNot("

    goto :goto_1

    :cond_1
    const-string v2, "match("

    :goto_1
    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ");"

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    invoke-virtual {p0, v0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    return-void
.end method

.method public genNextToken()V
    .locals 15

    const-string v0, "resetText();"

    iget v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/16 v2, -0x3e7

    :try_start_0
    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/4 v2, 0x0

    move v3, v2

    :goto_0
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v4}, Lantlr/collections/impl/Vector;->size()I

    move-result v4
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v5, "public"

    const/4 v6, 0x1

    if-ge v3, v4, :cond_1

    :try_start_1
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v4, v3}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/RuleSymbol;

    invoke-virtual {v4}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v7

    if-eqz v7, :cond_0

    iget-object v4, v4, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    if-eqz v4, :cond_0

    move v3, v6

    goto :goto_1

    :cond_0
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_1
    move v3, v2

    :goto_1
    const-string v4, "\t\tthrow new TokenStreamException(cse.getMessage());"

    const-string v7, "public Token nextToken() throws TokenStreamException {"

    const-string v8, "\t}"

    const-string v9, ""

    const-string v10, "}"

    if-nez v3, :cond_2

    :try_start_2
    invoke-virtual {p0, v9}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\ttry {uponEOF();}"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\tcatch(CharStreamIOException csioe) {"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t\tthrow new TokenStreamIOException(csioe.io);"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\tcatch(CharStreamException cse) {"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\treturn new CommonToken(Token.EOF_TYPE, \"\");"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v9}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :cond_2
    :try_start_3
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v11, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v11, v11, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    const-string v12, "nextToken"

    invoke-static {v3, v11, v12}, Lantlr/MakeGrammar;->createNextTokenRule(Lantlr/Grammar;Lantlr/collections/impl/Vector;Ljava/lang/String;)Lantlr/RuleBlock;

    move-result-object v3

    new-instance v11, Lantlr/RuleSymbol;

    const-string v12, "mnextToken"

    invoke-direct {v11, v12}, Lantlr/RuleSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {v11}, Lantlr/RuleSymbol;->setDefined()V

    invoke-virtual {v11, v3}, Lantlr/RuleSymbol;->setBlock(Lantlr/RuleBlock;)V

    const-string v12, "private"

    iput-object v12, v11, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v12, v11}, Lantlr/Grammar;->define(Lantlr/RuleSymbol;)V

    iget-object v11, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v11, v11, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v11, v3}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    const/4 v11, 0x0

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v12, Lantlr/LexerGrammar;

    iget-boolean v12, v12, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v12, :cond_3

    iget-object v11, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v11, Lantlr/LexerGrammar;

    iget-object v11, v11, Lantlr/LexerGrammar;->filterRule:Ljava/lang/String;

    :cond_3
    invoke-virtual {p0, v9}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v6

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v7, "Token theRetToken=null;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "tryAgain:"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    const-string v7, "for (;;) {"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v6

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v7, "Token _token = null;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "int _ttype = Token.INVALID_TYPE;"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v7, Lantlr/LexerGrammar;

    iget-boolean v7, v7, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v7, :cond_7

    const-string v7, "setCommitToPath(false);"

    invoke-virtual {p0, v7}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    if-eqz v11, :cond_7

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v11}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v7, v12}, Lantlr/Grammar;->isDefined(Ljava/lang/String;)Z

    move-result v7
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_0

    const-string v12, " does not exist in this lexer"

    const-string v13, "Filter rule "

    if-nez v7, :cond_4

    :try_start_4
    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_2
    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    goto :goto_3

    :cond_4
    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v11}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v7, v14}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v7

    check-cast v7, Lantlr/RuleSymbol;

    invoke-virtual {v7}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v14

    if-nez v14, :cond_5

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_2

    :cond_5
    iget-object v7, v7, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v7, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_6

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v12, " must be protected"

    invoke-virtual {v7, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_2

    :goto_3
    invoke-virtual {v5, v7}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_6
    const-string v5, "int _m;"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "_m = mark();"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "try {   // for char stream error handling"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v6

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v5, "try {   // for lexical error handling"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v6

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    move v5, v2

    :goto_4
    invoke-virtual {v3}, Lantlr/RuleBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v7

    invoke-virtual {v7}, Lantlr/collections/impl/Vector;->size()I

    move-result v7

    if-ge v5, v7, :cond_9

    invoke-virtual {v3, v5}, Lantlr/RuleBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v7

    iget-object v12, v7, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v12, v12, v6

    invoke-virtual {v12}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v12

    if-eqz v12, :cond_8

    iget-object v7, v7, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    check-cast v7, Lantlr/RuleRefElement;

    iget-object v7, v7, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-static {v7}, Lantlr/CodeGenerator;->decodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    iget-object v12, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v13, Ljava/lang/StringBuilder;

    invoke-direct {v13}, Ljava/lang/StringBuilder;-><init>()V

    const-string v14, "public lexical rule "

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, " is optional (can match \"nothing\")"

    invoke-virtual {v13, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v12, v7}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    :cond_8
    add-int/lit8 v5, v5, 0x1

    goto :goto_4

    :cond_9
    const-string v5, "line.separator"

    invoke-static {v5}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v3, v2}, Lantlr/JavaCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/JavaBlockFinishingInfo;

    move-result-object v2
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_0

    const-string v7, "if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}"

    :try_start_5
    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v12, Lantlr/LexerGrammar;

    iget-boolean v12, v12, Lantlr/LexerGrammar;->filterMode:Z
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_0

    const-string v13, "(false);}"

    const-string v14, "else {"

    if-eqz v12, :cond_b

    if-nez v11, :cond_a

    :try_start_6
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "else {consume(); continue tryAgain;}"

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_5

    :cond_a
    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t\tcommit();"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t\ttry {m"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t\tcatch(RecognitionException e) {"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t\t\t// catastrophic failure"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t\t\treportError(e);"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t\t\tconsume();"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t\t}"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\t\t\t\t\tcontinue tryAgain;"

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "\t\t\t\t}"

    invoke-virtual {v12, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-object v5, v12

    goto :goto_5

    :cond_b
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v7, p0, Lantlr/JavaCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_5
    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3}, Lantlr/RuleBlock;->getLine()I

    move-result v7

    invoke-direct {p0, v2, v5, v7}, Lantlr/JavaCodeGenerator;->genBlockFinish(Lantlr/JavaBlockFinishingInfo;Ljava/lang/String;I)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v2, Lantlr/LexerGrammar;

    iget-boolean v2, v2, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v2, :cond_c

    if-eqz v11, :cond_c

    const-string v2, "commit();"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    const-string v2, "if ( _returnToken==null ) continue tryAgain; // found SKIP token"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "_ttype = _returnToken.getType();"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v2, Lantlr/LexerGrammar;

    invoke-virtual {v2}, Lantlr/LexerGrammar;->getTestLiterals()Z

    move-result v2

    if-eqz v2, :cond_d

    invoke-direct {p0}, Lantlr/JavaCodeGenerator;->genLiteralsTest()V

    :cond_d
    const-string v2, "_returnToken.setType(_ttype);"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "return _returnToken;"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v6

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "catch (RecognitionException e) {"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v6

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v2, Lantlr/LexerGrammar;

    iget-boolean v2, v2, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v2, :cond_f

    if-nez v11, :cond_e

    const-string v0, "if ( !getCommitToPath() ) {consume(); continue tryAgain;}"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_6

    :cond_e
    const-string v2, "if ( !getCommitToPath() ) {"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v6

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "rewind(_m);"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "try {m"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "catch(RecognitionException ee) {"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t// horrendous failure: error in filter rule"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\treportError(ee);"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\tconsume();"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "continue tryAgain;"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v6

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    :goto_6
    invoke-virtual {v3}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v0

    if-eqz v0, :cond_10

    const-string v0, "reportError(e);"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "consume();"

    :goto_7
    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_8

    :cond_10
    const-string v0, "throw new TokenStreamRecognitionException(e);"

    goto :goto_7

    :goto_8
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v6

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v6

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "catch (CharStreamException cse) {"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\tif ( cse instanceof CharStreamIOException ) {"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t\tthrow new TokenStreamIOException(((CharStreamIOException)cse).io);"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\telse {"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v6

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v6

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v10}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v9}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_6
    .catchall {:try_start_6 .. :try_end_6} :catchall_0

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception v0

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw v0
.end method

.method public genRule(Lantlr/RuleSymbol;ZI)V
    .locals 19

    move-object/from16 v1, p0

    move-object/from16 v0, p1

    move/from16 v2, p3

    const/4 v3, 0x1

    iput v3, v1, Lantlr/CodeGenerator;->tabs:I

    iget-boolean v4, v1, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    const-string v5, ")"

    if-eqz v4, :cond_0

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v6, "genRule("

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual/range {p1 .. p1}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v4

    if-nez v4, :cond_1

    iget-object v1, v1, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v2, "undefined rule: "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-void

    :cond_1
    invoke-virtual/range {p1 .. p1}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v4

    iget v6, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {v4}, Lantlr/RuleBlock;->getLine()I

    move-result v7

    iput v7, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    iput-object v4, v1, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    iput-object v7, v1, Lantlr/JavaCodeGenerator;->currentASTResult:Ljava/lang/String;

    iget-object v7, v1, Lantlr/JavaCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {v7}, Ljava/util/Hashtable;->clear()V

    iget-boolean v7, v1, Lantlr/JavaCodeGenerator;->genAST:Z

    iget-boolean v8, v1, Lantlr/JavaCodeGenerator;->genAST:Z

    if-eqz v8, :cond_2

    invoke-virtual {v4}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v8

    if-eqz v8, :cond_2

    move v8, v3

    goto :goto_0

    :cond_2
    const/4 v8, 0x0

    :goto_0
    iput-boolean v8, v1, Lantlr/JavaCodeGenerator;->genAST:Z

    invoke-virtual {v4}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v8

    iput-boolean v8, v1, Lantlr/JavaCodeGenerator;->saveText:Z

    iget-object v8, v0, Lantlr/RuleSymbol;->comment:Ljava/lang/String;

    if-eqz v8, :cond_3

    invoke-virtual {v1, v8}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_3
    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v10, v0, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v10, " final "

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v1, v8}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    iget-object v8, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_1

    const-string v10, " "

    if-eqz v8, :cond_4

    :try_start_1
    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v11, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v4}, Lantlr/RuleBlock;->getLine()I

    move-result v12

    invoke-virtual {v4}, Lantlr/RuleBlock;->getColumn()I

    move-result v13

    invoke-virtual {v1, v11, v12, v13}, Lantlr/CodeGenerator;->extractTypeOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v8, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    :goto_1
    invoke-virtual {v1, v8}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_2

    :cond_4
    const-string v8, "void "

    goto :goto_1

    :goto_2
    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v8, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v11, "("

    invoke-virtual {v8, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v1, v8}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v8, v1, Lantlr/JavaCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {v1, v8}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v8, v1, Lantlr/JavaCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {v8}, Ljava/lang/String;->length()I

    move-result v8

    if-eqz v8, :cond_5

    iget-object v8, v4, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz v8, :cond_5

    const-string v8, ","

    invoke-virtual {v1, v8}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    :cond_5
    iget-object v8, v4, Lantlr/RuleBlock;->argAction:Ljava/lang/String;
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_1

    const-string v11, ""

    if-eqz v8, :cond_6

    :try_start_2
    invoke-virtual {v1, v11}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    iget v8, v1, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, v3

    iput v8, v1, Lantlr/CodeGenerator;->tabs:I

    iget-object v8, v4, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    invoke-virtual {v1, v8}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, v1, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v8, v3

    iput v8, v1, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_3

    :cond_6
    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    :goto_3
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, " throws "

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, v1, Lantlr/JavaCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/ParserGrammar;

    if-eqz v5, :cond_7

    const-string v5, ", TokenStreamException"

    :goto_4
    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_5

    :cond_7
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_8

    const-string v5, ", CharStreamException, TokenStreamException"

    goto :goto_4

    :cond_8
    :goto_5
    iget-object v5, v4, Lantlr/RuleBlock;->throwsSpec:Ljava/lang/String;

    if-eqz v5, :cond_a

    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_9

    iget-object v5, v1, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v12, "user-defined throws spec not allowed (yet) for lexer rule "

    invoke-virtual {v8, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v12, v4, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    invoke-virtual {v8, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v5, v8}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_6

    :cond_9
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, ", "

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, v4, Lantlr/RuleBlock;->throwsSpec:Ljava/lang/String;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    :cond_a
    :goto_6
    const-string v5, " {"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    iget v5, v1, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v3

    iput v5, v1, Lantlr/CodeGenerator;->tabs:I

    iget-object v5, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_1

    const-string v8, ";"

    if-eqz v5, :cond_b

    :try_start_3
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v12, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v5, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_b
    iget-object v5, v1, Lantlr/JavaCodeGenerator;->commonLocalVars:Ljava/lang/String;

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->traceRules:Z
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_1

    const-string v12, "\",_t);"

    const-string v13, "\");"

    if-eqz v5, :cond_d

    :try_start_4
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/TreeWalkerGrammar;
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_1

    const-string v14, "traceIn(\""

    if-eqz v5, :cond_c

    :try_start_5
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v5, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_7

    :cond_c
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v5, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_7
    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_d
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_f

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v5

    const-string v14, "mEOF"

    invoke-virtual {v5, v14}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_e

    const-string v5, "_ttype = Token.EOF_TYPE;"

    :goto_8
    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_9

    :cond_e
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v14, "_ttype = "

    invoke-virtual {v5, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v14, v3}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v5, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    goto :goto_8

    :goto_9
    const-string v5, "int _saveIndex;"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->debuggingOutput:Z
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_1

    const-string v14, ",_ttype);"

    const-string v15, ",0);"

    if-eqz v5, :cond_11

    :try_start_6
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/ParserGrammar;
    :try_end_6
    .catchall {:try_start_6 .. :try_end_6} :catchall_1

    const-string v9, "fireEnterRule("

    if-eqz v5, :cond_10

    :try_start_7
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_a

    :cond_10
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_11

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_a
    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_11
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->debuggingOutput:Z

    if-nez v5, :cond_12

    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->traceRules:Z

    if-eqz v5, :cond_13

    :cond_12
    const-string v5, "try { // debugging"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, v1, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v3

    iput v5, v1, Lantlr/CodeGenerator;->tabs:I

    :cond_13
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/TreeWalkerGrammar;

    if-eqz v5, :cond_14

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, v1, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "_AST_in = (_t == ASTNULL) ? null : ("

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, v1, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, ")_t;"

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    const/16 v9, -0x3e7

    invoke-virtual {v1, v5, v9}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    :cond_14
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->buildAST:Z

    if-eqz v5, :cond_15

    const-string v5, "returnAST = null;"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "ASTPair currentAST = new ASTPair();"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, v1, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "_AST = null;"

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_15
    invoke-virtual {v1, v4}, Lantlr/JavaCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {v1, v4}, Lantlr/JavaCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    invoke-virtual {v1, v11}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v4, v11}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object v5

    if-nez v5, :cond_16

    invoke-virtual {v4}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v9

    if-eqz v9, :cond_17

    :cond_16
    const-string v9, "try {      // for error handling"

    invoke-virtual {v1, v9}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v9, v1, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v9, v3

    iput v9, v1, Lantlr/CodeGenerator;->tabs:I

    :cond_17
    iget-object v9, v4, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v9}, Lantlr/collections/impl/Vector;->size()I

    move-result v9

    if-ne v9, v3, :cond_1a

    const/4 v9, 0x0

    invoke-virtual {v4, v9}, Lantlr/RuleBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v9

    iget-object v10, v9, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v10, :cond_18

    iget-object v3, v1, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget v3, v3, Lantlr/GrammarElement;->line:I

    invoke-virtual {v1, v10, v3}, Lantlr/JavaCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    :cond_18
    iget-object v3, v9, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v3, :cond_19

    iget-object v3, v1, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;
    :try_end_7
    .catchall {:try_start_7 .. :try_end_7} :catchall_1

    const-string v10, "Syntactic predicate ignored for single alternative"

    move/from16 v16, v6

    :try_start_8
    iget-object v6, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v6}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v6

    move/from16 v17, v7

    iget-object v7, v9, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v7}, Lantlr/GrammarElement;->getLine()I

    move-result v7

    move-object/from16 v18, v11

    iget-object v11, v9, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v11}, Lantlr/GrammarElement;->getColumn()I

    move-result v11

    invoke-virtual {v3, v10, v6, v7, v11}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_b

    :cond_19
    move/from16 v16, v6

    move/from16 v17, v7

    move-object/from16 v18, v11

    :goto_b
    invoke-virtual {v1, v9, v4}, Lantlr/JavaCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    goto :goto_c

    :cond_1a
    move/from16 v16, v6

    move/from16 v17, v7

    move-object/from16 v18, v11

    iget-object v3, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v3, v4}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    const/4 v3, 0x0

    invoke-virtual {v1, v4, v3}, Lantlr/JavaCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/JavaBlockFinishingInfo;

    move-result-object v3

    iget-object v6, v1, Lantlr/JavaCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-virtual {v4}, Lantlr/RuleBlock;->getLine()I

    move-result v7

    invoke-direct {v1, v3, v6, v7}, Lantlr/JavaCodeGenerator;->genBlockFinish(Lantlr/JavaBlockFinishingInfo;Ljava/lang/String;I)V
    :try_end_8
    .catchall {:try_start_8 .. :try_end_8} :catchall_0

    :goto_c
    const-string v3, "}"

    if-nez v5, :cond_1b

    :try_start_9
    invoke-virtual {v4}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v6

    if-eqz v6, :cond_1c

    :cond_1b
    iget v6, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v7, 0x1

    sub-int/2addr v6, v7

    iput v6, v1, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v1, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_1c
    if-eqz v5, :cond_1d

    invoke-direct {v1, v5}, Lantlr/JavaCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    goto/16 :goto_f

    :cond_1d
    invoke-virtual {v4}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v5

    if-eqz v5, :cond_21

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "catch ("

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, v1, Lantlr/JavaCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, " ex) {"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v6, 0x1

    add-int/2addr v5, v6

    iput v5, v1, Lantlr/CodeGenerator;->tabs:I

    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v5, :cond_1e

    const-string v5, "if (inputState.guessing==0) {"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v6, 0x1

    add-int/2addr v5, v6

    iput v5, v1, Lantlr/CodeGenerator;->tabs:I

    :cond_1e
    const-string v5, "reportError(ex);"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/TreeWalkerGrammar;

    if-nez v5, :cond_1f

    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v6, v4, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    const/4 v7, 0x1

    invoke-interface {v5, v7, v6}, Lantlr/LLkGrammarAnalyzer;->FOLLOW(ILantlr/RuleEndElement;)Lantlr/Lookahead;

    move-result-object v5

    iget-object v5, v5, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v1, v5}, Lantlr/CodeGenerator;->markBitsetForGen(Lantlr/collections/impl/BitSet;)I

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v5

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "recover(ex,"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ");"

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    :goto_d
    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_e

    :cond_1f
    const-string v5, "if (_t!=null) {_t = _t.getNextSibling();}"

    goto :goto_d

    :goto_e
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v5, :cond_20

    iget v5, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v6, 0x1

    sub-int/2addr v5, v6

    iput v5, v1, Lantlr/CodeGenerator;->tabs:I

    const-string v5, "} else {"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "  throw ex;"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v1, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_20
    iget v5, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v6, 0x1

    sub-int/2addr v5, v6

    iput v5, v1, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v1, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_21
    :goto_f
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->buildAST:Z

    if-eqz v5, :cond_22

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "returnAST = "

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "_AST;"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_22
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/TreeWalkerGrammar;

    if-eqz v5, :cond_23

    const-string v5, "_retTree = _t;"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_23
    invoke-virtual {v4}, Lantlr/RuleBlock;->getTestLiterals()Z

    move-result v5

    if-eqz v5, :cond_25

    iget-object v5, v0, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    const-string v6, "protected"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_24

    invoke-direct/range {p0 .. p0}, Lantlr/JavaCodeGenerator;->genLiteralsTestForPartialToken()V

    goto :goto_10

    :cond_24
    invoke-direct/range {p0 .. p0}, Lantlr/JavaCodeGenerator;->genLiteralsTest()V

    :cond_25
    :goto_10
    iget-object v5, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_26

    const-string v5, "if ( _createToken && _token==null && _ttype!=Token.SKIP ) {"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "\t_token = makeToken(_ttype);"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "\t_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v1, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "_returnToken = _token;"

    invoke-virtual {v1, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_26
    iget-object v5, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v5, :cond_27

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "return "

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v4}, Lantlr/RuleBlock;->getLine()I

    move-result v7

    invoke-virtual {v4}, Lantlr/RuleBlock;->getColumn()I

    move-result v4

    invoke-virtual {v1, v6, v7, v4}, Lantlr/CodeGenerator;->extractIdOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v1, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_27
    iget-object v4, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->debuggingOutput:Z

    if-nez v4, :cond_29

    iget-object v4, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->traceRules:Z

    if-eqz v4, :cond_28

    goto :goto_11

    :cond_28
    const/4 v2, 0x1

    goto/16 :goto_14

    :cond_29
    :goto_11
    iget v4, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    sub-int/2addr v4, v5

    iput v4, v1, Lantlr/CodeGenerator;->tabs:I

    const-string v4, "} finally { // debugging"

    invoke-virtual {v1, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v1, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v5

    iput v4, v1, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v4, :cond_2b

    iget-object v4, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v4, v4, Lantlr/ParserGrammar;
    :try_end_9
    .catchall {:try_start_9 .. :try_end_9} :catchall_0

    const-string v5, "fireExitRule("

    if-eqz v4, :cond_2a

    :try_start_a
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_12

    :cond_2a
    iget-object v4, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v4, v4, Lantlr/LexerGrammar;

    if-eqz v4, :cond_2b

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_12
    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_2b
    iget-object v2, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->traceRules:Z

    if-eqz v2, :cond_2d

    iget-object v2, v1, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;
    :try_end_a
    .catchall {:try_start_a .. :try_end_a} :catchall_0

    const-string v4, "traceOut(\""

    if-eqz v2, :cond_2c

    :try_start_b
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_13

    :cond_2c
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_13
    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_2d
    iget v0, v1, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    sub-int/2addr v0, v2

    iput v0, v1, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v1, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :goto_14
    iget v0, v1, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v2

    iput v0, v1, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v1, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    move-object/from16 v0, v18

    invoke-virtual {v1, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    move/from16 v0, v17

    iput-boolean v0, v1, Lantlr/JavaCodeGenerator;->genAST:Z
    :try_end_b
    .catchall {:try_start_b .. :try_end_b} :catchall_0

    move/from16 v2, v16

    iput v2, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception v0

    move/from16 v2, v16

    goto :goto_15

    :catchall_1
    move-exception v0

    move v2, v6

    :goto_15
    iput v2, v1, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw v0
.end method

.method public genSemPred(Ljava/lang/String;I)V
    .locals 3

    new-instance v0, Lantlr/ActionTransInfo;

    invoke-direct {v0}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v1, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, p1, p2, v1, v0}, Lantlr/JavaCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    iget-object v0, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v0, p1}, Lantlr/CharFormatter;->escapeString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_1

    instance-of v2, v1, Lantlr/ParserGrammar;

    if-nez v2, :cond_0

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_1

    :cond_0
    const-string v1, "fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.VALIDATING,"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->addSemPred(Ljava/lang/String;)I

    move-result v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    :cond_1
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "if (!("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "))"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "  throw new SemanticException(\""

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "\");"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    return-void
.end method

.method public genSemPredMap()V
    .locals 4

    iget-object v0, p0, Lantlr/JavaCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    const/16 v1, -0x3e7

    const-string v2, "private String _semPredNames[] = {"

    :goto_0
    invoke-virtual {p0, v2, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_0

    const-string v2, "\""

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v3, "\","

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    goto :goto_0

    :cond_0
    const-string v0, "};"

    invoke-virtual {p0, v0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    return-void
.end method

.method public genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V
    .locals 8

    const-string v0, "synPredMatched"

    const-string v1, " = false;"

    const-string v2, "}"

    iget v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    :try_start_0
    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    iput v4, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-boolean v4, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v5, ")"

    if-eqz v4, :cond_0

    :try_start_1
    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "gen=>("

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "boolean synPredMatched"

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v6, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v4, v4, Lantlr/TreeWalkerGrammar;

    if-eqz v4, :cond_1

    const-string v4, "if (_t==null) _t=ASTNULL;"

    invoke-virtual {p0, v4}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "if ("

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ") {"

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, 0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/TreeWalkerGrammar;

    if-eqz p2, :cond_2

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "AST __t"

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " = _t;"

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_0
    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    goto :goto_1

    :cond_2
    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "int _m"

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " = mark();"

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :goto_1
    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " = true;"

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "inputState.guessing++;"

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p2, p2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p2, :cond_4

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/ParserGrammar;

    if-nez p2, :cond_3

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/LexerGrammar;

    if-eqz p2, :cond_4

    :cond_3
    const-string p2, "fireSyntacticPredicateStarted();"

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    iget p2, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 p2, p2, 0x1

    iput p2, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    const-string p2, "try {"

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, 0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->gen(Lantlr/AlternativeBlock;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "catch ("

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/JavaCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " pe) {"

    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, 0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v0, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/TreeWalkerGrammar;

    if-eqz p2, :cond_5

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "_t = __t"

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v0, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, ";"

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_2
    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    goto :goto_3

    :cond_5
    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "rewind(_m"

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v0, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, ");"

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_2

    :goto_3
    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "inputState.guessing--;"

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p2, p2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p2, :cond_7

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/ParserGrammar;

    if-nez p2, :cond_6

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/LexerGrammar;

    if-eqz p2, :cond_7

    :cond_6
    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "if (synPredMatched"

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v0, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "  fireSyntacticPredicateSucceeded();"

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "else"

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "  fireSyntacticPredicateFailed();"

    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    iget p2, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/JavaCodeGenerator;->syntacticPredLevel:I

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "if ( synPredMatched"

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget p1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, " ) {"

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public genTokenASTNodeMap()V
    .locals 9

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/16 v1, -0x3e7

    :try_start_0
    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "protected void buildTokenTypeASTClassMap() {"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v1}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v1

    const/4 v3, 0x0

    move v4, v3

    move v5, v4

    :goto_0
    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v6

    if-ge v3, v6, :cond_2

    invoke-virtual {v1, v3}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/lang/String;

    if-eqz v6, :cond_1

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v7, v6}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v6

    if-eqz v6, :cond_1

    invoke-virtual {v6}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v7

    if-eqz v7, :cond_1

    add-int/lit8 v4, v4, 0x1

    if-nez v5, :cond_0

    const-string v5, "tokenTypeToASTClassMap = new Hashtable();"

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    move v5, v2

    :cond_0
    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "tokenTypeToASTClassMap.put(new Integer("

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v8

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v8, "), "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, ".class);"

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {p0, v6}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_2
    if-nez v4, :cond_3

    const-string v1, "tokenTypeToASTClassMap=null;"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "};"

    invoke-virtual {p0, v1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception v1

    iput v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw v1
.end method

.method public genTokenStrings()V
    .locals 8

    const-string v0, ""

    const-string v1, "\""

    iget v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/16 v3, -0x3e7

    :try_start_0
    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "public static final String[] _tokenNames = {"

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v3

    const/4 v4, 0x0

    :goto_0
    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->size()I

    move-result v5

    if-ge v4, v5, :cond_3

    invoke-virtual {v3, v4}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Ljava/lang/String;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    const-string v6, "<"

    if-nez v5, :cond_0

    :try_start_1
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v4}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, ">"

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    :cond_0
    invoke-virtual {v5, v1}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v7

    if-nez v7, :cond_1

    invoke-virtual {v5, v6}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v6

    if-nez v6, :cond_1

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v6, v6, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v6, v5}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v6

    if-eqz v6, :cond_1

    invoke-virtual {v6}, Lantlr/TokenSymbol;->getParaphrase()Ljava/lang/String;

    move-result-object v7

    if-eqz v7, :cond_1

    invoke-virtual {v6}, Lantlr/TokenSymbol;->getParaphrase()Ljava/lang/String;

    move-result-object v5

    invoke-static {v5, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    :cond_1
    iget-object v6, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v6, v5}, Lantlr/CharFormatter;->literalString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->size()I

    move-result v5

    add-int/lit8 v5, v5, -0x1

    if-eq v4, v5, :cond_2

    const-string v5, ","

    invoke-virtual {p0, v5}, Lantlr/JavaCodeGenerator;->_print(Ljava/lang/String;)V

    :cond_2
    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->_println(Ljava/lang/String;)V

    add-int/lit8 v4, v4, 0x1

    goto :goto_0

    :cond_3
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "};"

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception v0

    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw v0
.end method

.method public genTokenTypes(Lantlr/TokenManager;)V
    .locals 9

    const-string v0, ""

    iget v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    const/16 v2, -0x3e7

    :try_start_0
    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v3

    iget-object v4, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v6, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-interface {v3, v4, v5}, Lantlr/JavaCodeGeneratorPrintWriterManager;->setupOutput(Lantlr/Tool;Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v3

    iput-object v3, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 v3, 0x0

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->genHeader()V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_1

    :try_start_1
    iget-object v3, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v3, v0}, Lantlr/DefineGrammarSymbols;->getHeaderActionLine(Ljava/lang/String;)I

    move-result v3

    iput v3, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    iget-object v3, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v3, v0}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :try_start_2
    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "public interface "

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " {"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-interface {p1}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v0

    const-string v2, "int EOF = 1;"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "int NULL_TREE_LOOKAHEAD = 3;"

    invoke-virtual {p0, v2}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    const/4 v2, 0x4

    :goto_0
    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v3

    if-ge v2, v3, :cond_5

    invoke-virtual {v0, v2}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    if-eqz v3, :cond_4

    const-string v4, "\""

    invoke-virtual {v3, v4}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v4
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_1

    const-string v5, ";"

    const-string v6, "int "

    const-string v7, " = "

    if-eqz v4, :cond_3

    :try_start_3
    invoke-interface {p1, v3}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v4

    check-cast v4, Lantlr/StringLiteralSymbol;

    if-nez v4, :cond_0

    iget-object v4, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "String literal "

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " not in symbol table"

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v4, v3}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto/16 :goto_3

    :cond_0
    iget-object v8, v4, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    if-eqz v8, :cond_1

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, v4, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    goto :goto_2

    :cond_1
    invoke-direct {p0, v3}, Lantlr/JavaCodeGenerator;->mangleLiteral(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    if-eqz v8, :cond_2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    iput-object v8, v4, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    goto :goto_3

    :cond_2
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "// "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    :goto_1
    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    goto :goto_2

    :cond_3
    const-string v4, "<"

    invoke-virtual {v3, v4}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v4

    if-nez v4, :cond_4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_1

    :goto_2
    invoke-virtual {p0, v3}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    :goto_3
    add-int/lit8 v2, v2, 0x1

    goto/16 :goto_0

    :cond_5
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p1

    invoke-interface {p1}, Lantlr/JavaCodeGeneratorPrintWriterManager;->finishOutput()V

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->exitIfError()V
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    return-void

    :catchall_0
    move-exception p1

    :try_start_4
    iput v2, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_1

    :catchall_1
    move-exception p1

    iput v1, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    throw p1
.end method

.method public getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    const-string p0, "("

    invoke-static {p0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ")astFactory.create("

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ",\""

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "\")"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-virtual {p0, p2}, Lantlr/JavaCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getASTCreateString(Lantlr/collections/impl/Vector;)Ljava/lang/String;
    .locals 4

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    if-nez v0, :cond_0

    const-string p0, ""

    return-object p0

    :cond_0
    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    const-string v1, "("

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object p0, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, ")astFactory.make( (new ASTArray("

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "))"

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const/4 p0, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    const-string v2, ")"

    if-ge p0, v1, :cond_1

    const-string v1, ".add("

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p1, p0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 p0, p0, 0x1

    goto :goto_0

    :cond_1
    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getASTCreateString(Ljava/lang/String;)Ljava/lang/String;
    .locals 8

    const-string v0, ""

    if-nez p1, :cond_0

    move-object p1, v0

    :cond_0
    const/4 v1, 0x0

    move v2, v1

    move v3, v2

    :goto_0
    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v4

    const/16 v5, 0x2c

    if-ge v2, v4, :cond_2

    invoke-virtual {p1, v2}, Ljava/lang/String;->charAt(I)C

    move-result v4

    if-ne v4, v5, :cond_1

    add-int/lit8 v3, v3, 0x1

    :cond_1
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_2
    const/4 v2, 0x2

    const-string v4, ")"

    const-string v6, ")astFactory.create("

    const-string v7, "("

    if-ge v3, v2, :cond_7

    invoke-virtual {p1, v5}, Ljava/lang/String;->indexOf(I)I

    move-result v2

    invoke-virtual {p1, v5}, Ljava/lang/String;->lastIndexOf(I)I

    if-lez v3, :cond_3

    invoke-virtual {p1, v1, v2}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v1

    goto :goto_1

    :cond_3
    move-object v1, p1

    :goto_1
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2, v1}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v1

    if-eqz v1, :cond_5

    invoke-virtual {v1}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v1

    if-nez v3, :cond_4

    const-string v0, ",\"\""

    :cond_4
    if-eqz v1, :cond_5

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ",\""

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "\")"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_5
    iget-object v0, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v1, "AST"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_6

    const-string p0, "astFactory.create("

    invoke-static {p0, p1, v4}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_6
    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_7
    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/JavaCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getLookaheadTestExpression(Lantlr/Alternative;I)Ljava/lang/String;
    .locals 2

    iget v0, p1, Lantlr/Alternative;->lookaheadDepth:I

    const v1, 0x7fffffff

    if-ne v0, v1, :cond_0

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v0, v0, Lantlr/Grammar;->maxk:I

    :cond_0
    if-nez p2, :cond_1

    const-string p0, "( true )"

    return-object p0

    :cond_1
    const-string p2, "("

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    iget-object p1, p1, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object p0

    const-string p1, ")"

    invoke-static {p2, p0, p1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;
    .locals 5

    new-instance v0, Ljava/lang/StringBuffer;

    const/16 v1, 0x64

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(I)V

    const-string v1, "("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const/4 v1, 0x1

    move v2, v1

    :goto_0
    if-gt v1, p2, :cond_2

    aget-object v3, p1, v1

    iget-object v3, v3, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    if-nez v2, :cond_0

    const-string v2, ") && ("

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_0
    const/4 v2, 0x0

    aget-object v4, p1, v1

    invoke-virtual {v4}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v4

    if-eqz v4, :cond_1

    const-string v3, "true"

    goto :goto_1

    :cond_1
    invoke-virtual {p0, v1, v3}, Lantlr/JavaCodeGenerator;->getLookaheadTestTerm(ILantlr/collections/impl/BitSet;)Ljava/lang/String;

    move-result-object v3

    :goto_1
    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    const-string p0, ")"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getLookaheadTestTerm(ILantlr/collections/impl/BitSet;)Ljava/lang/String;
    .locals 4

    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object v1

    invoke-static {v1}, Lantlr/CodeGenerator;->elementsAreRange([I)Z

    move-result v2

    if-eqz v2, :cond_0

    invoke-virtual {p0, p1, v1}, Lantlr/JavaCodeGenerator;->getRangeExpression(I[I)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-virtual {p2}, Lantlr/collections/impl/BitSet;->degree()I

    move-result p1

    if-nez p1, :cond_1

    const-string p0, "true"

    return-object p0

    :cond_1
    iget v2, p0, Lantlr/CodeGenerator;->bitsetTestThreshold:I

    if-lt p1, v2, :cond_2

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->markBitsetForGen(Lantlr/collections/impl/BitSet;)I

    move-result p1

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, ".member("

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, ")"

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_2
    new-instance p1, Ljava/lang/StringBuffer;

    invoke-direct {p1}, Ljava/lang/StringBuffer;-><init>()V

    const/4 p2, 0x0

    :goto_0
    array-length v2, v1

    if-ge p2, v2, :cond_4

    aget v2, v1, p2

    invoke-direct {p0, v2}, Lantlr/JavaCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v2

    if-lez p2, :cond_3

    const-string v3, "||"

    invoke-virtual {p1, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_3
    invoke-virtual {p1, v0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string v3, "=="

    invoke-virtual {p1, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 p2, p2, 0x1

    goto :goto_0

    :cond_4
    invoke-virtual {p1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;
    .locals 1

    iget-object v0, p0, Lantlr/JavaCodeGenerator;->printWriterManager:Lantlr/JavaCodeGeneratorPrintWriterManager;

    if-nez v0, :cond_0

    new-instance v0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;

    invoke-direct {v0}, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;-><init>()V

    iput-object v0, p0, Lantlr/JavaCodeGenerator;->printWriterManager:Lantlr/JavaCodeGeneratorPrintWriterManager;

    :cond_0
    iget-object p0, p0, Lantlr/JavaCodeGenerator;->printWriterManager:Lantlr/JavaCodeGeneratorPrintWriterManager;

    return-object p0
.end method

.method public getRangeExpression(I[I)Ljava/lang/String;
    .locals 3

    invoke-static {p2}, Lantlr/CodeGenerator;->elementsAreRange([I)Z

    move-result v0

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "getRangeExpression called with non-range"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    const/4 v0, 0x0

    aget v0, p2, v0

    array-length v1, p2

    add-int/lit8 v1, v1, -0x1

    aget p2, p2, v1

    const-string v1, "("

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " >= "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, v0}, Lantlr/JavaCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " && "

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, p1}, Lantlr/JavaCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " <= "

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, p2}, Lantlr/JavaCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object p0

    const-string p1, ")"

    invoke-static {v1, p0, p1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public lookaheadIsEmpty(Lantlr/Alternative;I)Z
    .locals 3

    iget v0, p1, Lantlr/Alternative;->lookaheadDepth:I

    const v1, 0x7fffffff

    if-ne v0, v1, :cond_0

    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v0, p0, Lantlr/Grammar;->maxk:I

    :cond_0
    const/4 p0, 0x1

    move v1, p0

    :goto_0
    if-gt v1, v0, :cond_2

    if-gt v1, p2, :cond_2

    iget-object v2, p1, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v2, v2, v1

    iget-object v2, v2, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v2}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v2

    if-eqz v2, :cond_1

    const/4 p0, 0x0

    return p0

    :cond_1
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    return p0
.end method

.method public mapTreeId(Ljava/lang/String;Lantlr/ActionTransInfo;)Ljava/lang/String;
    .locals 6

    iget-object v0, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    if-nez v0, :cond_0

    return-object p1

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    const-string v2, "_in"

    const/4 v3, 0x1

    const/4 v4, 0x0

    if-eqz v1, :cond_2

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-nez v0, :cond_1

    goto :goto_0

    :cond_1
    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v0

    const/4 v1, 0x3

    if-le v0, v1, :cond_2

    invoke-virtual {p1, v2}, Ljava/lang/String;->lastIndexOf(Ljava/lang/String;)I

    move-result v0

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v5

    sub-int/2addr v5, v1

    if-ne v0, v5, :cond_2

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v0

    sub-int/2addr v0, v1

    invoke-virtual {p1, v4, v0}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p1

    goto :goto_0

    :cond_2
    move v3, v4

    :goto_0
    iget-object v0, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    const-string v1, "_AST"

    if-ge v4, v0, :cond_5

    iget-object v0, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v0, v4}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/AlternativeElement;

    invoke-virtual {v0}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_4

    if-eqz v3, :cond_3

    goto :goto_1

    :cond_3
    invoke-static {p1, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    :goto_1
    return-object p1

    :cond_4
    add-int/lit8 v4, v4, 0x1

    goto :goto_0

    :cond_5
    iget-object v0, p0, Lantlr/JavaCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {v0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    if-eqz v0, :cond_9

    sget-object p2, Lantlr/JavaCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    const/4 v1, 0x0

    const-string v4, " in rule "

    const-string v5, "Ambiguous reference to AST element "

    if-ne v0, p2, :cond_6

    :goto_2
    iget-object p2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v5, p1, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object p0, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-object v1

    :cond_6
    iget-object p2, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p2}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v0, p2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_7

    goto :goto_2

    :cond_7
    if-eqz v3, :cond_8

    invoke-static {v0, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_8
    return-object v0

    :cond_9
    iget-object p0, p0, Lantlr/JavaCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_c

    if-eqz v3, :cond_a

    const-string p0, "_AST_in"

    invoke-static {p1, p0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    goto :goto_3

    :cond_a
    invoke-static {p1, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    :goto_3
    if-eqz p2, :cond_b

    if-nez v3, :cond_b

    iput-object p0, p2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    :cond_b
    return-object p0

    :cond_c
    return-object p1
.end method

.method public print(Ljava/lang/String;)V
    .locals 1

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->print(Ljava/lang/String;I)V

    return-void
.end method

.method public print(Ljava/lang/String;I)V
    .locals 2

    const/16 v0, -0x378

    if-gtz p2, :cond_0

    if-ne p2, v0, :cond_1

    :cond_0
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v1

    invoke-interface {v1, p2}, Lantlr/JavaCodeGeneratorPrintWriterManager;->startMapping(I)V

    :cond_1
    invoke-super {p0, p1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    if-gtz p2, :cond_2

    if-ne p2, v0, :cond_3

    :cond_2
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p0

    invoke-interface {p0}, Lantlr/JavaCodeGeneratorPrintWriterManager;->endMapping()V

    :cond_3
    return-void
.end method

.method public printAction(Ljava/lang/String;)V
    .locals 1

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->printAction(Ljava/lang/String;I)V

    return-void
.end method

.method public printAction(Ljava/lang/String;I)V
    .locals 1

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v0

    invoke-interface {v0, p2}, Lantlr/JavaCodeGeneratorPrintWriterManager;->startMapping(I)V

    invoke-super {p0, p1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p0

    invoke-interface {p0}, Lantlr/JavaCodeGeneratorPrintWriterManager;->endMapping()V

    return-void
.end method

.method public println(Ljava/lang/String;)V
    .locals 1

    iget v0, p0, Lantlr/JavaCodeGenerator;->defaultLine:I

    invoke-virtual {p0, p1, v0}, Lantlr/JavaCodeGenerator;->println(Ljava/lang/String;I)V

    return-void
.end method

.method public println(Ljava/lang/String;I)V
    .locals 2

    const/16 v0, -0x378

    if-gtz p2, :cond_0

    if-ne p2, v0, :cond_1

    :cond_0
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object v1

    invoke-interface {v1, p2}, Lantlr/JavaCodeGeneratorPrintWriterManager;->startSingleSourceLineMapping(I)V

    :cond_1
    invoke-super {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    if-gtz p2, :cond_2

    if-ne p2, v0, :cond_3

    :cond_2
    invoke-virtual {p0}, Lantlr/JavaCodeGenerator;->getPrintWriterManager()Lantlr/JavaCodeGeneratorPrintWriterManager;

    move-result-object p0

    invoke-interface {p0}, Lantlr/JavaCodeGeneratorPrintWriterManager;->endMapping()V

    :cond_3
    return-void
.end method

.method public processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;
    .locals 4

    const-string v0, "Error reading action:"

    if-eqz p1, :cond_6

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v1

    if-nez v1, :cond_0

    goto :goto_1

    :cond_0
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    if-nez v1, :cond_1

    return-object p1

    :cond_1
    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    const/4 v2, -0x1

    if-eqz v1, :cond_2

    const/16 v1, 0x23

    invoke-virtual {p1, v1}, Ljava/lang/String;->indexOf(I)I

    move-result v1

    if-ne v1, v2, :cond_4

    :cond_2
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v1, Lantlr/TreeWalkerGrammar;

    if-nez v3, :cond_4

    instance-of v3, v1, Lantlr/LexerGrammar;

    if-nez v3, :cond_3

    instance-of v1, v1, Lantlr/ParserGrammar;

    if-eqz v1, :cond_5

    :cond_3
    const/16 v1, 0x24

    invoke-virtual {p1, v1}, Ljava/lang/String;->indexOf(I)I

    move-result v1

    if-eq v1, v2, :cond_5

    :cond_4
    new-instance v1, Lantlr/actions/java/ActionLexer;

    invoke-direct {v1, p1, p3, p0, p4}, Lantlr/actions/java/ActionLexer;-><init>(Ljava/lang/String;Lantlr/RuleBlock;Lantlr/CodeGenerator;Lantlr/ActionTransInfo;)V

    invoke-virtual {v1, p2}, Lantlr/actions/java/ActionLexer;->setLineOffset(I)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v1, p2}, Lantlr/CharScanner;->setFilename(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1, p2}, Lantlr/actions/java/ActionLexer;->setTool(Lantlr/Tool;)V

    const/4 p2, 0x1

    :try_start_0
    invoke-virtual {v1, p2}, Lantlr/actions/java/ActionLexer;->mACTION(Z)V

    invoke-virtual {v1}, Lantlr/CharScanner;->getTokenObject()Lantlr/Token;

    move-result-object p2

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_2
    .catch Lantlr/TokenStreamException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Lantlr/CharStreamException; {:try_start_0 .. :try_end_0} :catch_0

    :cond_5
    return-object p1

    :catch_0
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    :goto_0
    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    return-object p1

    :catch_1
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    goto :goto_0

    :catch_2
    move-exception p0

    invoke-virtual {v1, p0}, Lantlr/actions/java/ActionLexer;->reportError(Lantlr/RecognitionException;)V

    return-object p1

    :cond_6
    :goto_1
    const/4 p0, 0x0

    return-object p0
.end method

.method public setPrintWriterManager(Lantlr/JavaCodeGeneratorPrintWriterManager;)V
    .locals 0

    iput-object p1, p0, Lantlr/JavaCodeGenerator;->printWriterManager:Lantlr/JavaCodeGeneratorPrintWriterManager;

    return-void
.end method

.method public setTool(Lantlr/Tool;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/CodeGenerator;->setTool(Lantlr/Tool;)V

    return-void
.end method
