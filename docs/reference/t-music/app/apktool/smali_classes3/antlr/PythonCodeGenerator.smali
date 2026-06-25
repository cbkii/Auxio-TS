.class public Lantlr/PythonCodeGenerator;
.super Lantlr/CodeGenerator;
.source ""


# static fields
.field public static final NONUNIQUE:Ljava/lang/String;

.field public static final caseSizeThreshold:I = 0x7f

.field public static final initHeaderAction:Ljava/lang/String; = "__init__"

.field public static final mainHeaderAction:Ljava/lang/String; = "__main__"


# instance fields
.field public astVarNumber:I

.field public commonExtraArgs:Ljava/lang/String;

.field public commonExtraParams:Ljava/lang/String;

.field public commonLocalVars:Ljava/lang/String;

.field public currentASTResult:Ljava/lang/String;

.field public currentRule:Lantlr/RuleBlock;

.field public declaredASTVariables:Ljava/util/Hashtable;

.field public exceptionThrown:Ljava/lang/String;

.field public genAST:Z

.field public labeledElementASTType:Ljava/lang/String;

.field public labeledElementInit:Ljava/lang/String;

.field public labeledElementType:Ljava/lang/String;

.field public lexerClassName:Ljava/lang/String;

.field public lt1Value:Ljava/lang/String;

.field public parserClassName:Ljava/lang/String;

.field public saveText:Z

.field public semPreds:Lantlr/collections/impl/Vector;

.field public syntacticPredLevel:I

.field public throwNoViable:Ljava/lang/String;

.field public treeVariableMap:Ljava/util/Hashtable;

.field public treeWalkerClassName:Ljava/lang/String;


# direct methods
.method public static constructor <clinit>()V
    .locals 1

    new-instance v0, Ljava/lang/String;

    invoke-direct {v0}, Ljava/lang/String;-><init>()V

    sput-object v0, Lantlr/PythonCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    return-void
.end method

.method public constructor <init>()V
    .locals 2

    invoke-direct {p0}, Lantlr/CodeGenerator;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    iput-boolean v0, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    iput-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    const/4 v0, 0x1

    iput v0, p0, Lantlr/PythonCodeGenerator;->astVarNumber:I

    new-instance v1, Lantlr/PythonCharFormatter;

    invoke-direct {v1}, Lantlr/PythonCharFormatter;-><init>()V

    iput-object v1, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    iput-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    return-void
.end method

.method private GenRuleInvocation(Lantlr/RuleRefElement;)V
    .locals 7

    const-string v0, "self."

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    const-string v1, ", "

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    const-string v0, "True"

    goto :goto_0

    :cond_0
    const-string v0, "False"

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    if-nez v0, :cond_1

    iget-object v0, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v0, :cond_2

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_2
    iget-object v0, p0, Lantlr/PythonCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    if-eqz v0, :cond_3

    iget-object v0, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v0, :cond_3

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_3
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    iget-object v1, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v1, :cond_6

    new-instance v1, Lantlr/ActionTransInfo;

    invoke-direct {v1}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v2, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    iget-object v3, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v4, 0x0

    invoke-virtual {p0, v2, v4, v3, v1}, Lantlr/PythonCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v2

    iget-boolean v3, v1, Lantlr/ActionTransInfo;->assignToRoot:Z

    if-nez v3, :cond_4

    iget-object v1, v1, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    if-eqz v1, :cond_5

    :cond_4
    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v3, "Arguments of rule reference \'"

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v4, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "\' cannot set or ref #"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v4}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v5

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v6

    invoke-virtual {v1, v3, v4, v5, v6}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_5
    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-nez v0, :cond_7

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Rule \'"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    const-string v3, "\' accepts no arguments"

    invoke-static {v1, v2, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    goto :goto_1

    :cond_6
    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz v0, :cond_7

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Missing parameters on reference to rule "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :goto_1
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result p1

    invoke-virtual {v0, v1, v2, v3, p1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_7
    const-string p1, ")"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_8

    const-string p1, "_t = self._retTree"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    return-void
.end method

.method private genBitSet(Lantlr/collections/impl/BitSet;I)V
    .locals 9

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x0

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, ""

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "### generate bit set"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "def mk"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "(): "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->lengthInLongWords()I

    move-result v2

    const/16 v3, 0x8

    if-ge v2, v3, :cond_0

    const-string v1, "### var1"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "data = [ "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toStringOfWords()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "]"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto/16 :goto_4

    :cond_0
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "data = [0L] * "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, " ### init list"

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toPackedArray()[J

    move-result-object p1

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

    const-string v4, "L"

    if-eq v2, v3, :cond_4

    aget-wide v5, p1, v1

    aget-wide v7, p1, v2

    cmp-long v3, v5, v7

    if-eqz v3, :cond_2

    goto :goto_2

    :cond_2
    :goto_1
    array-length v3, p1

    if-ge v2, v3, :cond_3

    aget-wide v5, p1, v2

    aget-wide v7, p1, v1

    cmp-long v3, v5, v7

    if-nez v3, :cond_3

    add-int/lit8 v2, v2, 0x1

    goto :goto_1

    :cond_3
    aget-wide v5, p1, v1

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "for x in xrange("

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ", "

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, "):"

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "data[x] = "

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v5, v6}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    goto :goto_3

    :cond_4
    :goto_2
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "data["

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v5, "] ="

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget-wide v5, p1, v1

    invoke-virtual {v3, v5, v6}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_3
    move v1, v2

    goto/16 :goto_0

    :cond_5
    :goto_4
    const-string p1, "return data"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = antlr.BitSet(mk"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "())"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    return-void
.end method

.method private genBlockFinish(Lantlr/PythonBlockFinishingInfo;Ljava/lang/String;)V
    .locals 1

    iget-boolean v0, p1, Lantlr/PythonBlockFinishingInfo;->needAnErrorClause:Z

    if-eqz v0, :cond_2

    iget-boolean v0, p1, Lantlr/PythonBlockFinishingInfo;->generatedAnIf:Z

    if-nez v0, :cond_0

    iget-boolean v0, p1, Lantlr/PythonBlockFinishingInfo;->generatedSwitch:Z

    if-eqz v0, :cond_2

    :cond_0
    iget-boolean v0, p1, Lantlr/PythonBlockFinishingInfo;->generatedAnIf:Z

    if-eqz v0, :cond_1

    const-string v0, "else:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    iget-object p1, p1, Lantlr/PythonBlockFinishingInfo;->postscript:Ljava/lang/String;

    if-eqz p1, :cond_3

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method private genBlockFinish1(Lantlr/PythonBlockFinishingInfo;Ljava/lang/String;)V
    .locals 1

    iget-boolean v0, p1, Lantlr/PythonBlockFinishingInfo;->needAnErrorClause:Z

    if-eqz v0, :cond_2

    iget-boolean v0, p1, Lantlr/PythonBlockFinishingInfo;->generatedAnIf:Z

    if-nez v0, :cond_0

    iget-boolean v0, p1, Lantlr/PythonBlockFinishingInfo;->generatedSwitch:Z

    if-eqz v0, :cond_2

    :cond_0
    iget-boolean v0, p1, Lantlr/PythonBlockFinishingInfo;->generatedAnIf:Z

    if-eqz v0, :cond_1

    const-string v0, "else:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-boolean p2, p1, Lantlr/PythonBlockFinishingInfo;->generatedAnIf:Z

    :cond_2
    iget-object p1, p1, Lantlr/PythonBlockFinishingInfo;->postscript:Ljava/lang/String;

    if-eqz p1, :cond_3

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method private genElementAST(Lantlr/AlternativeElement;)V
    .locals 9

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    const-string v2, "_in = "

    const-string v3, "_AST"

    const-string v4, "tmp"

    const/4 v5, 0x1

    if-eqz v1, :cond_1

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-nez v0, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v4, p0, Lantlr/PythonCodeGenerator;->astVarNumber:I

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    iget v3, p0, Lantlr/PythonCodeGenerator;->astVarNumber:I

    add-int/2addr v3, v5

    iput v3, p0, Lantlr/PythonCodeGenerator;->astVarNumber:I

    invoke-direct {p0, p1, v1}, Lantlr/PythonCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    return-void

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_f

    iget v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_f

    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    const/4 v1, 0x3

    if-eqz v0, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-eq v0, v1, :cond_3

    :cond_2
    move v0, v5

    goto :goto_0

    :cond_3
    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v6

    if-eq v6, v1, :cond_4

    instance-of v1, p1, Lantlr/TokenRefElement;

    if-eqz v1, :cond_4

    move v0, v5

    :cond_4
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_5

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    goto :goto_1

    :cond_5
    iget-object v1, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget v6, p0, Lantlr/PythonCodeGenerator;->astVarNumber:I

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    iget v6, p0, Lantlr/PythonCodeGenerator;->astVarNumber:I

    add-int/2addr v6, v5

    iput v6, p0, Lantlr/PythonCodeGenerator;->astVarNumber:I

    :goto_1
    if-eqz v0, :cond_7

    instance-of v6, p1, Lantlr/GrammarAtom;

    if-eqz v6, :cond_6

    move-object v6, p1

    check-cast v6, Lantlr/GrammarAtom;

    invoke-virtual {v6}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v7

    if-eqz v7, :cond_6

    invoke-virtual {v6}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v6

    goto :goto_2

    :cond_6
    iget-object v6, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    :goto_2
    invoke-virtual {p0, p1, v4, v6}, Lantlr/PythonCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V

    :cond_7
    invoke-static {v4, v3}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-direct {p0, p1, v3}, Lantlr/PythonCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v4, v4, Lantlr/TreeWalkerGrammar;

    if-eqz v4, :cond_8

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "_in = None"

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    const-string v6, " = "

    const-string v7, ""

    if-eqz v4, :cond_a

    instance-of v4, p1, Lantlr/GrammarAtom;

    if-eqz v4, :cond_9

    invoke-static {v3, v6}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    move-object v8, p1

    check-cast v8, Lantlr/GrammarAtom;

    invoke-virtual {p0, v8, v1}, Lantlr/PythonCodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    goto :goto_3

    :cond_9
    invoke-static {v3, v6}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {p0, v1}, Lantlr/PythonCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :goto_3
    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_c

    if-eqz v0, :cond_c

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    instance-of v1, p1, Lantlr/GrammarAtom;

    if-eqz v1, :cond_b

    invoke-static {v3, v6}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    move-object v4, p1

    check-cast v4, Lantlr/GrammarAtom;

    invoke-virtual {p0, v4, v0}, Lantlr/PythonCodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    goto :goto_4

    :cond_b
    invoke-static {v3, v6}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0, v0}, Lantlr/PythonCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    :goto_4
    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_c

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    if-eqz v0, :cond_f

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    const-string v0, ")"

    if-eq p1, v5, :cond_e

    const/4 v1, 0x2

    if-eq p1, v1, :cond_d

    goto :goto_6

    :cond_d
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "self.makeASTRoot(currentAST, "

    goto :goto_5

    :cond_e
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "self.addASTChild(currentAST, "

    :goto_5
    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    :goto_6
    return-void
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

    move-result-object p1

    invoke-virtual {v0, p1}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object p1

    if-eqz p1, :cond_3

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    :cond_3
    return-void
.end method

.method private genErrorHandler(Lantlr/ExceptionSpec;)V
    .locals 7

    const/4 v0, 0x0

    move v1, v0

    :goto_0
    iget-object v2, p1, Lantlr/ExceptionSpec;->handlers:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    if-ge v1, v2, :cond_4

    iget-object v2, p1, Lantlr/ExceptionSpec;->handlers:Lantlr/collections/impl/Vector;

    invoke-virtual {v2, v1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/ExceptionHandler;

    iget-object v3, v2, Lantlr/ExceptionHandler;->exceptionTypeAndName:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->removeAssignmentFromDeclaration(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v4

    add-int/lit8 v4, v4, -0x1

    :goto_1
    const-string v5, ""

    if-ltz v4, :cond_1

    invoke-virtual {v3, v4}, Ljava/lang/String;->charAt(I)C

    move-result v5

    invoke-static {v5}, Ljava/lang/Character;->isLetterOrDigit(C)Z

    move-result v5

    if-nez v5, :cond_0

    invoke-virtual {v3, v4}, Ljava/lang/String;->charAt(I)C

    move-result v5

    const/16 v6, 0x5f

    if-eq v5, v6, :cond_0

    invoke-virtual {v3, v0, v4}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v5

    add-int/lit8 v4, v4, 0x1

    invoke-virtual {v3, v4}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v3

    goto :goto_2

    :cond_0
    add-int/lit8 v4, v4, -0x1

    goto :goto_1

    :cond_1
    move-object v3, v5

    :goto_2
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "except "

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ", "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ":"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v4, v4, 0x1

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v4, :cond_2

    const-string v4, "if not self.inputState.guessing:"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v4, v4, 0x1

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    new-instance v4, Lantlr/ActionTransInfo;

    invoke-direct {v4}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v5, v2, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v5}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    iget-object v2, v2, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v2}, Lantlr/Token;->getLine()I

    move-result v2

    iget-object v6, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v5, v2, v6, v4}, Lantlr/PythonCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/PythonCodeGenerator;->printAction(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v2, :cond_3

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, -0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "else:"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "raise "

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, -0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_3
    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, -0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    goto/16 :goto_0

    :cond_4
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

    move-result-object p1

    invoke-virtual {v0, p1}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object p1

    if-eqz p1, :cond_3

    const-string p1, "try: # for error handling"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_3
    return-void
.end method

.method private genLiteralsTest()V
    .locals 1

    const-string v0, "### option { testLiterals=true } "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "_ttype = self.testLiteralsTable(_ttype)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method private genLiteralsTestForPartialToken()V
    .locals 1

    const-string v0, "_ttype = self.testLiteralsTable(self.text.getString(), _begin, self.text.length()-_begin, _ttype)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method private getValueString(IZ)Ljava/lang/String;
    .locals 2

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-eqz v1, :cond_1

    iget-object p0, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {p0, p1}, Lantlr/CharFormatter;->literalChar(I)Ljava/lang/String;

    move-result-object p0

    if-eqz p2, :cond_0

    const-string p1, "u\'"

    const-string p2, "\'"

    invoke-static {p1, p0, p2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    :cond_0
    return-object p0

    :cond_1
    iget-object p2, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2, p1}, Lantlr/TokenManager;->getTokenSymbolAt(I)Lantlr/TokenSymbol;

    move-result-object p2

    if-nez p2, :cond_2

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    const-string p2, ""

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_2
    invoke-virtual {p2}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v0

    instance-of v1, p2, Lantlr/StringLiteralSymbol;

    if-nez v1, :cond_3

    return-object v0

    :cond_3
    check-cast p2, Lantlr/StringLiteralSymbol;

    invoke-virtual {p2}, Lantlr/StringLiteralSymbol;->getLabel()Ljava/lang/String;

    move-result-object p2

    if-eqz p2, :cond_4

    goto :goto_0

    :cond_4
    invoke-direct {p0, v0}, Lantlr/PythonCodeGenerator;->mangleLiteral(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p2

    if-nez p2, :cond_5

    invoke-static {p1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object p2

    :cond_5
    :goto_0
    return-object p2
.end method

.method public static isEmpty(Ljava/lang/String;)Z
    .locals 5

    const/4 v0, 0x0

    const/4 v1, 0x1

    move v2, v0

    :goto_0
    if-eqz v1, :cond_1

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v3

    if-ge v2, v3, :cond_1

    invoke-virtual {p0, v2}, Ljava/lang/String;->charAt(I)C

    move-result v3

    const/16 v4, 0x9

    if-eq v3, v4, :cond_0

    const/16 v4, 0xa

    if-eq v3, v4, :cond_0

    const/16 v4, 0xc

    if-eq v3, v4, :cond_0

    const/16 v4, 0xd

    if-eq v3, v4, :cond_0

    const/16 v4, 0x20

    if-eq v3, v4, :cond_0

    move v1, v0

    :cond_0
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_1
    return v1
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

    const-string v0, "self.LA("

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

    invoke-direct {p0, p1, p2}, Lantlr/PythonCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

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

    iget-object p1, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p1, v0}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p1

    if-eqz p1, :cond_3

    iget-object p1, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p1, v0}, Ljava/util/Hashtable;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p0, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    sget-object p1, Lantlr/PythonCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    invoke-virtual {p0, v0, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    goto :goto_1

    :cond_3
    iget-object p0, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p0, v0, p2}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_4
    :goto_1
    return-void
.end method

.method private setupGrammarParameters(Lantlr/Grammar;)V
    .locals 9

    instance-of v0, p1, Lantlr/ParserGrammar;

    const-string v1, "None"

    const-string v2, "antlr.RecognitionException"

    const-string v3, "className"

    const-string v4, "ASTLabelType"

    const-string v5, ""

    const-string v6, "\""

    if-eqz v0, :cond_2

    iput-object v5, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p1, v4}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p1, v4}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0, v6, v6}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    :cond_0
    iput-object v5, p0, Lantlr/PythonCodeGenerator;->labeledElementType:Ljava/lang/String;

    iput-object v1, p0, Lantlr/PythonCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v5, p0, Lantlr/PythonCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    const-string v0, "self"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->commonExtraParams:Ljava/lang/String;

    iput-object v5, p0, Lantlr/PythonCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string v0, "self.LT(1)"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v2, p0, Lantlr/PythonCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string v0, "raise antlr.NoViableAltException(self.LT(1), self.getFilename())"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->throwNoViable:Ljava/lang/String;

    const-string v0, "Parser"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->parserClassName:Ljava/lang/String;

    invoke-virtual {p1, v3}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-virtual {p1, v3}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object p1

    if-eqz p1, :cond_1

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1, v6, v6}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_1

    iput-object p1, p0, Lantlr/PythonCodeGenerator;->parserClassName:Ljava/lang/String;

    :cond_1
    return-void

    :cond_2
    instance-of v0, p1, Lantlr/LexerGrammar;

    if-eqz v0, :cond_4

    const-string v0, "char "

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->labeledElementType:Ljava/lang/String;

    const-string v0, "\'\\0\'"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v5, p0, Lantlr/PythonCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    const-string v0, "self, _createToken"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->commonExtraParams:Ljava/lang/String;

    const-string v0, "_ttype = 0\n        _token = None\n        _begin = self.text.length()"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string v0, "self.LA(1)"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v2, p0, Lantlr/PythonCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string v0, "self.raise_NoViableAlt(self.LA(1))"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->throwNoViable:Ljava/lang/String;

    const-string v0, "Lexer"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->lexerClassName:Ljava/lang/String;

    invoke-virtual {p1, v3}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_3

    invoke-virtual {p1, v3}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object p1

    if-eqz p1, :cond_3

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1, v6, v6}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_3

    iput-object p1, p0, Lantlr/PythonCodeGenerator;->lexerClassName:Ljava/lang/String;

    :cond_3
    return-void

    :cond_4
    instance-of v0, p1, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_8

    iput-object v5, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    iput-object v5, p0, Lantlr/PythonCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {p1, v4}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_5

    invoke-virtual {p1, v4}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v0

    if-eqz v0, :cond_5

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0, v6, v6}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_5

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->labeledElementType:Ljava/lang/String;

    :cond_5
    invoke-virtual {p1, v4}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-nez v0, :cond_6

    new-instance v0, Lantlr/Token;

    const/4 v7, 0x6

    const-string v8, "<4>AST"

    invoke-direct {v0, v7, v8}, Lantlr/Token;-><init>(ILjava/lang/String;)V

    invoke-virtual {p1, v4, v0}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    :cond_6
    iput-object v1, p0, Lantlr/PythonCodeGenerator;->labeledElementInit:Ljava/lang/String;

    const-string v0, "_t"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    const-string v1, "self, _t"

    iput-object v1, p0, Lantlr/PythonCodeGenerator;->commonExtraParams:Ljava/lang/String;

    iput-object v5, p0, Lantlr/PythonCodeGenerator;->commonLocalVars:Ljava/lang/String;

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v2, p0, Lantlr/PythonCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string v0, "raise antlr.NoViableAltException(_t)"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->throwNoViable:Ljava/lang/String;

    const-string v0, "Walker"

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->treeWalkerClassName:Ljava/lang/String;

    invoke-virtual {p1, v3}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_7

    invoke-virtual {p1, v3}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object p1

    if-eqz p1, :cond_7

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1, v6, v6}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_7

    iput-object p1, p0, Lantlr/PythonCodeGenerator;->treeWalkerClassName:Ljava/lang/String;

    :cond_7
    return-void

    :cond_8
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string p1, "Unknown grammar type"

    invoke-virtual {p0, p1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

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
.method public _printAction(Ljava/lang/String;)V
    .locals 13

    if-nez p1, :cond_0

    return-void

    :cond_0
    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v0

    const/4 v1, 0x0

    const/4 v2, 0x1

    move v3, v1

    move v5, v3

    move v4, v2

    :goto_0
    const/16 v6, 0x20

    const/16 v7, 0xd

    const/16 v8, 0xa

    if-ge v3, v0, :cond_4

    if-eqz v4, :cond_4

    add-int/lit8 v9, v3, 0x1

    invoke-virtual {p1, v3}, Ljava/lang/String;->charAt(I)C

    move-result v3

    if-eq v3, v8, :cond_3

    if-eq v3, v7, :cond_2

    if-eq v3, v6, :cond_1

    move v4, v1

    :cond_1
    move v3, v9

    goto :goto_0

    :cond_2
    if-gt v9, v0, :cond_3

    invoke-virtual {p1, v9}, Ljava/lang/String;->charAt(I)C

    move-result v3

    if-ne v3, v8, :cond_3

    add-int/lit8 v9, v9, 0x1

    :cond_3
    move v5, v9

    move v3, v5

    goto :goto_0

    :cond_4
    if-nez v4, :cond_5

    add-int/lit8 v3, v3, -0x1

    :cond_5
    sub-int v4, v3, v5

    sub-int/2addr v0, v2

    :goto_1
    if-le v0, v3, :cond_6

    invoke-virtual {p1, v0}, Ljava/lang/String;->charAt(I)C

    move-result v5

    invoke-virtual {p0, v5}, Lantlr/PythonCodeGenerator;->isspace(C)Z

    move-result v5

    if-eqz v5, :cond_6

    add-int/lit8 v0, v0, -0x1

    goto :goto_1

    :cond_6
    move v5, v1

    :goto_2
    if-gt v3, v0, :cond_10

    invoke-virtual {p1, v3}, Ljava/lang/String;->charAt(I)C

    move-result v9

    const/16 v10, 0x9

    if-eq v9, v10, :cond_a

    if-eq v9, v8, :cond_9

    if-eq v9, v7, :cond_8

    if-eq v9, v6, :cond_7

    iget-object v10, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v10, v9}, Ljava/io/PrintWriter;->print(C)V

    goto :goto_4

    :cond_7
    iget-object v9, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const-string v10, " "

    goto :goto_3

    :cond_8
    add-int/lit8 v5, v3, 0x1

    if-gt v5, v0, :cond_9

    invoke-virtual {p1, v5}, Ljava/lang/String;->charAt(I)C

    move-result v9

    if-ne v9, v8, :cond_9

    move v3, v5

    :cond_9
    move v5, v2

    goto :goto_4

    :cond_a
    sget-object v9, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v10, "warning: tab characters used in Python action"

    invoke-virtual {v9, v10}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    iget-object v9, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const-string v10, "        "

    :goto_3
    invoke-virtual {v9, v10}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    :goto_4
    if-eqz v5, :cond_f

    iget-object v5, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const-string v9, "\n"

    invoke-virtual {v5, v9}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->printTabs()V

    add-int/lit8 v3, v3, 0x1

    move v5, v1

    move v10, v5

    :goto_5
    if-gt v3, v0, :cond_f

    invoke-virtual {p1, v3}, Ljava/lang/String;->charAt(I)C

    move-result v11

    invoke-virtual {p0, v11}, Lantlr/PythonCodeGenerator;->isspace(C)Z

    move-result v12

    if-nez v12, :cond_b

    add-int/lit8 v3, v3, -0x1

    goto :goto_8

    :cond_b
    if-eq v11, v8, :cond_d

    if-eq v11, v7, :cond_c

    goto :goto_6

    :cond_c
    add-int/lit8 v5, v3, 0x1

    if-gt v5, v0, :cond_d

    invoke-virtual {p1, v5}, Ljava/lang/String;->charAt(I)C

    move-result v11

    if-ne v11, v8, :cond_d

    move v3, v5

    :cond_d
    move v5, v2

    :goto_6
    if-eqz v5, :cond_e

    iget-object v5, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v5, v9}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->printTabs()V

    move v5, v1

    move v10, v5

    goto :goto_7

    :cond_e
    if-ge v10, v4, :cond_f

    add-int/lit8 v10, v10, 0x1

    :goto_7
    add-int/lit8 v3, v3, 0x1

    goto :goto_5

    :cond_f
    :goto_8
    add-int/2addr v3, v2

    goto :goto_2

    :cond_10
    iget-object p0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p0}, Ljava/io/PrintWriter;->println()V

    return-void
.end method

.method public _printJavadoc(Ljava/lang/String;)V
    .locals 10

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v0

    iget-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const-string v2, "\n"

    invoke-virtual {v1, v2}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->printTabs()V

    iget-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const-string v3, "###"

    invoke-virtual {v1, v3}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    const/4 v1, 0x0

    move v4, v1

    move v5, v4

    :goto_0
    if-ge v4, v0, :cond_5

    invoke-virtual {p1, v4}, Ljava/lang/String;->charAt(I)C

    move-result v6

    const/16 v7, 0x9

    const/4 v8, 0x1

    if-eq v6, v7, :cond_3

    const/16 v7, 0xa

    if-eq v6, v7, :cond_2

    const/16 v9, 0xd

    if-eq v6, v9, :cond_1

    const/16 v7, 0x20

    if-eq v6, v7, :cond_0

    iget-object v7, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v7, v6}, Ljava/io/PrintWriter;->print(C)V

    goto :goto_2

    :cond_0
    iget-object v6, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const-string v7, " "

    goto :goto_1

    :cond_1
    add-int/lit8 v5, v4, 0x1

    if-gt v5, v0, :cond_2

    invoke-virtual {p1, v5}, Ljava/lang/String;->charAt(I)C

    move-result v6

    if-ne v6, v7, :cond_2

    move v4, v5

    :cond_2
    move v5, v8

    goto :goto_2

    :cond_3
    iget-object v6, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const-string v7, "\t"

    :goto_1
    invoke-virtual {v6, v7}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    :goto_2
    if-eqz v5, :cond_4

    iget-object v5, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v5, v2}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->printTabs()V

    iget-object v5, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v5, v3}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    move v5, v1

    :cond_4
    add-int/2addr v4, v8

    goto :goto_0

    :cond_5
    iget-object p0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p0}, Ljava/io/PrintWriter;->println()V

    return-void
.end method

.method public addSemPred(Ljava/lang/String;)I
    .locals 1

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {v0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    iget-object p0, p0, Lantlr/PythonCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {p0}, Lantlr/collections/impl/Vector;->size()I

    move-result p0

    add-int/lit8 p0, p0, -0x1

    return p0
.end method

.method public checkCurrentOutputStream()V
    .locals 0

    :try_start_0
    iget-object p0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/NullPointerException;

    invoke-direct {p0}, Ljava/lang/NullPointerException;-><init>()V

    throw p0
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    const-string p0, "current output is not set"

    invoke-static {p0}, Lantlr/Utils;->error(Ljava/lang/String;)V

    const/4 p0, 0x0

    throw p0
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

.method public extractIdOfAction(Ljava/lang/String;II)Ljava/lang/String;
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->removeAssignmentFromDeclaration(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public extractTypeOfAction(Ljava/lang/String;II)Ljava/lang/String;
    .locals 0

    const-string p0, ""

    return-object p0
.end method

.method public flushTokens()V
    .locals 6

    const-string v0, ""

    :try_start_0
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->checkCurrentOutputStream()V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "### import antlr.Token "

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "from antlr import Token"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "### >>>The Known Token Types <<<"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iget-object v2, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    iget-object v2, v2, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v2}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v2

    const/4 v3, 0x0

    :goto_0
    invoke-interface {v2}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v4

    if-eqz v4, :cond_2

    invoke-interface {v2}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/TokenManager;

    invoke-interface {v4}, Lantlr/TokenManager;->isReadOnly()Z

    move-result v5

    if-nez v5, :cond_1

    if-nez v3, :cond_0

    invoke-virtual {p0, v4}, Lantlr/PythonCodeGenerator;->genTokenTypes(Lantlr/TokenManager;)V

    const/4 v3, 0x1

    :cond_0
    iput-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->genTokenInterchange(Lantlr/TokenManager;)V

    iput-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    :cond_1
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->exitIfError()V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->exitIfError()V

    :cond_2
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->checkCurrentOutputStream()V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

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

    invoke-direct {p0, v1}, Lantlr/PythonCodeGenerator;->setupGrammarParameters(Lantlr/Grammar;)V

    invoke-virtual {v1}, Lantlr/Grammar;->generate()V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->exitIfError()V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception v0

    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1}, Lantlr/Tool;->reportException(Ljava/lang/Exception;Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public gen(Lantlr/ActionElement;)V
    .locals 3

    iget-boolean v0, p1, Lantlr/ActionElement;->isSemPred:Z

    if-eqz v0, :cond_0

    iget-object v0, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    iget p1, p1, Lantlr/GrammarElement;->line:I

    invoke-virtual {p0, v0, p1}, Lantlr/PythonCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    goto/16 :goto_0

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v0, :cond_1

    const-string v0, "if not self.inputState.guessing:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_1
    new-instance v0, Lantlr/ActionTransInfo;

    invoke-direct {v0}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v1, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    iget-object v2, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v1, p1, v2, v0}, Lantlr/PythonCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    iget-object v1, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    if-eqz v1, :cond_2

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " = currentAST.root"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->printAction(Ljava/lang/String;)V

    iget-boolean p1, v0, Lantlr/ActionTransInfo;->assignToRoot:Z

    if-eqz p1, :cond_3

    const-string p1, "currentAST.root = "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v1, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ""

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "if ("

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " != None) and ("

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".getFirstChild() != None):"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "currentAST.child = "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ".getFirstChild()"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "else:"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "currentAST.advanceChildToEnd()"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz p1, :cond_4

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_4
    :goto_0
    return-void
.end method

.method public gen(Lantlr/AlternativeBlock;)V
    .locals 3

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
    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    iput-object v1, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v1, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    invoke-virtual {p0, p1, v2}, Lantlr/PythonCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/PythonBlockFinishingInfo;

    move-result-object p1

    iget-object v2, p0, Lantlr/PythonCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-direct {p0, p1, v2}, Lantlr/PythonCodeGenerator;->genBlockFinish(Lantlr/PythonBlockFinishingInfo;Ljava/lang/String;)V

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

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

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v1, :cond_2

    goto :goto_0

    :cond_2
    const/4 v1, 0x0

    :goto_0
    iput-boolean v1, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    iput-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    return-void
.end method

.method public gen(Lantlr/CharRangeElement;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/CharRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/CharRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_2

    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v0, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    const/4 v1, 0x3

    if-ne v0, v1, :cond_2

    :cond_1
    const/4 v0, 0x1

    goto :goto_0

    :cond_2
    const/4 v0, 0x0

    :goto_0
    if-eqz v0, :cond_3

    const-string v1, "_saveIndex = self.text.length()"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    const-string v1, "self.matchRange(u"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ", u"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p1, p1, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    if-eqz v0, :cond_4

    const-string p1, "self.text.setLength(_saveIndex)"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    return-void
.end method

.method public gen(Lantlr/LexerGrammar;)V
    .locals 9

    iget-boolean v0, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v0, :cond_0

    new-instance v0, Lantlr/collections/impl/Vector;

    invoke-direct {v0}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-nez v0, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Internal error generating lexer"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/PythonCodeGenerator;->setupOutput(Ljava/lang/String;)V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    const/4 v1, 0x1

    iput-boolean v1, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genHeader()V

    const-string v2, "### import antlr and other modules .."

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "import sys"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "import antlr"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, ""

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "version = sys.version.split()[0]"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "if version < \'2.2.1\':"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "False = 0"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "if version < \'2.3\':"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "True = not False"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "### header action >>> "

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v3, v2}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3, v0}, Lantlr/PythonCodeGenerator;->printActionCode(Ljava/lang/String;I)V

    const-string v3, "### header action <<< "

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "### preamble action >>> "

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3, v0}, Lantlr/PythonCodeGenerator;->printActionCode(Ljava/lang/String;I)V

    const-string v3, "### preamble action <<< "

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v3, :cond_2

    goto :goto_0

    :cond_2
    const-string v3, "antlr."

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    :goto_0
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v5, "classHeaderPrefix"

    invoke-virtual {v4, v5}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/Token;

    const-string v5, "\""

    if-eqz v4, :cond_3

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    invoke-static {v4, v5, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    :cond_3
    const-string v4, "### >>>The Literals<<<"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "literals = {}"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v4}, Lantlr/TokenManager;->getTokenSymbolKeys()Ljava/util/Enumeration;

    move-result-object v4

    :cond_4
    :goto_1
    invoke-interface {v4}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v6

    if-eqz v6, :cond_6

    invoke-interface {v4}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/lang/String;

    invoke-virtual {v6, v0}, Ljava/lang/String;->charAt(I)C

    move-result v7

    const/16 v8, 0x22

    if-eq v7, v8, :cond_5

    goto :goto_1

    :cond_5
    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v7, v6}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v6

    instance-of v7, v6, Lantlr/StringLiteralSymbol;

    if-eqz v7, :cond_4

    check-cast v6, Lantlr/StringLiteralSymbol;

    const-string v7, "literals[u"

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    invoke-virtual {v6}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "] = "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v6

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_6
    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->flushTokens()V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v4}, Lantlr/PythonCodeGenerator;->genJavadocComment(Lantlr/Grammar;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "class "

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, p0, Lantlr/PythonCodeGenerator;->lexerClassName:Ljava/lang/String;

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "("

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, ") :"

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v1

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v4}, Lantlr/PythonCodeGenerator;->printGrammarAction(Lantlr/Grammar;)V

    const-string v4, "def __init__(self, *argv, **kwargs) :"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v1

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ".__init__(self, *argv, **kwargs)"

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "self.caseSensitiveLiterals = "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean v4, p1, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    invoke-virtual {p0, v4}, Lantlr/PythonCodeGenerator;->toString(Z)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "self.setCaseSensitive("

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean p1, p1, Lantlr/LexerGrammar;->caseSensitive:Z

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->toString(Z)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "self.literals = literals"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_9

    const-string p1, "ruleNames[] = ["

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object p1

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_7
    :goto_2
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_8

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/GrammarSymbol;

    instance-of v4, v3, Lantlr/RuleSymbol;

    if-eqz v4, :cond_7

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    check-cast v3, Lantlr/RuleSymbol;

    invoke-virtual {v3}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "\","

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_2

    :cond_8
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "]"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genHeaderInit(Lantlr/Grammar;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genNextToken()V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object p1

    move v1, v0

    :goto_3
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_b

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/RuleSymbol;

    invoke-virtual {v3}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v4

    const-string v5, "mnextToken"

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-nez v4, :cond_a

    add-int/lit8 v4, v1, 0x1

    invoke-virtual {p0, v3, v0, v1}, Lantlr/PythonCodeGenerator;->genRule(Lantlr/RuleSymbol;ZI)V

    move v1, v4

    :cond_a
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->exitIfError()V

    goto :goto_3

    :cond_b
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_c

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genSemPredMap()V

    :cond_c
    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-object v0, v0, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0}, Lantlr/collections/impl/BitSet;->size()I

    move-result v0

    invoke-virtual {p0, p1, v0}, Lantlr/PythonCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genHeaderMain(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public gen(Lantlr/OneOrMoreBlock;)V
    .locals 10

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    const-string v0, "_cnt_"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :cond_0
    const-string v0, "_cnt"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    :goto_0
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, ""

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "= 0"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "while True:"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v3, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    iput-object v4, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_1
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v4, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/OneOrMoreBlock;)Z

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v4, v4, Lantlr/Grammar;->maxk:I

    iget-boolean v5, p1, Lantlr/AlternativeBlock;->greedy:Z

    const/4 v6, 0x0

    if-nez v5, :cond_2

    iget v5, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    if-gt v5, v4, :cond_2

    iget-object v7, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aget-object v5, v7, v5

    invoke-virtual {v5}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v5

    if-eqz v5, :cond_2

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    goto :goto_1

    :cond_2
    iget-boolean v5, p1, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v5, :cond_3

    iget v5, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    const v7, 0x7fffffff

    if-ne v5, v7, :cond_3

    :goto_1
    move v5, v4

    move v4, v2

    goto :goto_2

    :cond_3
    move v5, v4

    move v4, v6

    :goto_2
    const-string v7, "if "

    const-string v8, "break"

    if-eqz v4, :cond_4

    const-string v4, "### nongreedy (...)+ loop; exit depth is "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget v9, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-virtual {v4, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    invoke-virtual {p0, v4, v5}, Lantlr/PythonCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object v4

    const-string v5, "### nongreedy exit test"

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, " >= 1 and "

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ":"

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_4
    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1, v6}, Lantlr/PythonCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/PythonBlockFinishingInfo;

    move-result-object p1

    invoke-direct {p0, p1, v8}, Lantlr/PythonCodeGenerator;->genBlockFinish(Lantlr/PythonBlockFinishingInfo;Ljava/lang/String;)V

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " += 1"

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " < 1:"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object p1, p0, Lantlr/PythonCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iput-object v3, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    return-void
.end method

.method public gen(Lantlr/ParserGrammar;)V
    .locals 7

    iget-boolean v0, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v0, :cond_0

    new-instance v0, Lantlr/collections/impl/Vector;

    invoke-direct {v0}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v0, p0, Lantlr/PythonCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/ParserGrammar;

    if-nez p1, :cond_1

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v0, "Internal error generating parser"

    invoke-virtual {p1, v0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->setupOutput(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->buildAST:Z

    iput-boolean p1, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genHeader()V

    const-string v0, "### import antlr and other modules .."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "import sys"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "import antlr"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "version = sys.version.split()[0]"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "if version < \'2.2.1\':"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "False = 0"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "if version < \'2.3\':"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "True = not False"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "### header action >>> "

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v1, v0}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1, p1}, Lantlr/PythonCodeGenerator;->printActionCode(Ljava/lang/String;I)V

    const-string v1, "### header action <<< "

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "### preamble action>>>"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1, p1}, Lantlr/PythonCodeGenerator;->printActionCode(Ljava/lang/String;I)V

    const-string v1, "### preamble action <<<"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->flushTokens()V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v1, :cond_2

    goto :goto_0

    :cond_2
    const-string v1, "antlr."

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :goto_0
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v3}, Lantlr/PythonCodeGenerator;->genJavadocComment(Lantlr/Grammar;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v4, "classHeaderPrefix"

    invoke-virtual {v3, v4}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/Token;

    const-string v4, "\""

    if-eqz v3, :cond_3

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3, v4, v4}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    :cond_3
    const-string v3, "class "

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v5, p0, Lantlr/PythonCodeGenerator;->parserClassName:Ljava/lang/String;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "("

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    const-string v3, "):"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v3, :cond_6

    const-string v3, "_ruleNames = ["

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v3

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v2

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_4
    :goto_1
    invoke-interface {v3}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v5

    if-eqz v5, :cond_5

    invoke-interface {v3}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Lantlr/GrammarSymbol;

    instance-of v6, v5, Lantlr/RuleSymbol;

    if-eqz v6, :cond_4

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    check-cast v5, Lantlr/RuleSymbol;

    invoke-virtual {v5}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "\","

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_5
    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "]"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v3}, Lantlr/PythonCodeGenerator;->printGrammarAction(Lantlr/Grammar;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "def __init__(self, *args, **kwargs):"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".__init__(self, *args, **kwargs)"

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "self.tokenNames = _tokenNames"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v1, :cond_7

    const-string v1, "self.ruleNames  = _ruleNames"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "self.semPredNames = _semPredNames"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "self.setupDebugging(self.tokenBuf)"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-eqz v1, :cond_8

    const-string v1, "self.buildTokenTypeASTClassMap()"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "self.astFactory = antlr.ASTFactory(self.getTokenTypeToASTClassMap())"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    if-eqz v1, :cond_8

    const-string v1, "self.astFactory.setASTNodeClass("

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ")"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v1}, Lantlr/PythonCodeGenerator;->genHeaderInit(Lantlr/Grammar;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v1

    move v3, p1

    :goto_2
    invoke-interface {v1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v4

    if-eqz v4, :cond_b

    invoke-interface {v1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/GrammarSymbol;

    instance-of v5, v4, Lantlr/RuleSymbol;

    if-eqz v5, :cond_a

    check-cast v4, Lantlr/RuleSymbol;

    iget-object v5, v4, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v5}, Lantlr/collections/impl/Vector;->size()I

    move-result v5

    if-nez v5, :cond_9

    move v5, v2

    goto :goto_3

    :cond_9
    move v5, p1

    :goto_3
    add-int/lit8 v6, v3, 0x1

    invoke-virtual {p0, v4, v5, v3}, Lantlr/PythonCodeGenerator;->genRule(Lantlr/RuleSymbol;ZI)V

    move v3, v6

    :cond_a
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->exitIfError()V

    goto :goto_2

    :cond_b
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-eqz v1, :cond_c

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genTokenASTNodeMap()V

    :cond_c
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genTokenStrings()V

    iget-object v1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2}, Lantlr/TokenManager;->maxTokenType()I

    move-result v2

    invoke-virtual {p0, v1, v2}, Lantlr/PythonCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v1, :cond_d

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genSemPredMap()V

    :cond_d
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genHeaderMain(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public gen(Lantlr/RuleRefElement;)V
    .locals 6

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    const-string v1, ")"

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "genRR("

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v2}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    const-string v2, "Rule \'"

    if-eqz v0, :cond_11

    invoke-virtual {v0}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v3

    if-nez v3, :cond_1

    goto/16 :goto_2

    :cond_1
    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/TreeWalkerGrammar;

    if-eqz v3, :cond_2

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_2

    iget v3, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v3, :cond_2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " = antlr.ifelse(_t == antlr.ASTNULL, None, "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    const/4 v3, 0x3

    if-eqz v1, :cond_4

    iget-boolean v1, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v1, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-ne v1, v3, :cond_4

    :cond_3
    const-string v1, "_saveIndex = self.text.length()"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->printTabs()V

    iget-object v1, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    if-eqz v1, :cond_6

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-nez v0, :cond_5

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    const-string v4, "\' has no return type"

    invoke-static {v1, v2, v4}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v5

    invoke-virtual {v0, v1, v2, v4, v5}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_5
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_0

    :cond_6
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-nez v1, :cond_7

    iget v1, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v1, :cond_7

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v0, :cond_7

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    const-string v4, "\' returns a value"

    invoke-static {v1, v2, v4}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v5

    invoke-virtual {v0, v1, v2, v4, v5}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_7
    :goto_0
    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->GenRuleInvocation(Lantlr/RuleRefElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_9

    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v0, :cond_8

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-ne v0, v3, :cond_9

    :cond_8
    const-string v0, "self.text.setLength(_saveIndex)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iget v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_10

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v0, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v1, :cond_b

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_a

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_b

    :cond_a
    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    if-eqz v0, :cond_b

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    :cond_b
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_c

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_c

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "_AST = self.returnAST"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    if-eqz v0, :cond_f

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    const/4 v1, 0x1

    if-eq v0, v1, :cond_e

    const/4 v1, 0x2

    if-eq v0, v1, :cond_d

    goto :goto_1

    :cond_d
    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Internal: encountered ^ after rule reference"

    invoke-virtual {v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_1

    :cond_e
    const-string v0, "self.addASTChild(currentAST, self.returnAST)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    :goto_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_10

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_10

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = self._returnToken"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_10
    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    return-void

    :cond_11
    :goto_2
    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    const-string v3, "\' is not defined"

    invoke-static {v1, v2, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result p1

    invoke-virtual {v0, v1, p0, v2, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void
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

    iget v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ""

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v1, :cond_2

    goto :goto_0

    :cond_2
    const/4 v1, 0x0

    :goto_0
    iput-boolean v1, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    iput-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_3

    const-string p1, "_t = _t.getNextSibling()"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method public gen(Lantlr/TokenRangeElement;)V
    .locals 2

    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    invoke-virtual {p1}, Lantlr/TokenRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/TokenRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    const-string v0, "self.matchRange(u"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p1, Lantlr/TokenRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ", u"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p1, Lantlr/TokenRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ")"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

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
    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_2

    iget v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ""

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_3

    const-string p1, "_t = _t.getNextSibling()"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method public gen(Lantlr/TreeElement;)V
    .locals 6

    const-string v0, "_t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, " = _t"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v0}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = antlr.ifelse(_t == antlr.ASTNULL, None, _t)"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v0}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    const/4 v1, 0x3

    const/4 v2, 0x1

    if-ne v0, v1, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v4

    const-string v5, "Suffixing a root node with \'!\' is not implemented"

    invoke-virtual {v0, v5, v1, v3, v4}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v0, v2}, Lantlr/AlternativeElement;->setAutoGenType(I)V

    :cond_1
    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v0}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    const/4 v1, 0x2

    if-ne v0, v1, :cond_2

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v4

    const-string v5, "Suffixing a root node with \'^\' is redundant; already a root"

    invoke-virtual {v0, v5, v1, v3, v4}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v0, v2}, Lantlr/AlternativeElement;->setAutoGenType(I)V

    :cond_2
    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-direct {p0, v0}, Lantlr/PythonCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_3

    const-string v0, "_currentAST"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, " = currentAST.copy()"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "currentAST.root = currentAST.child"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "currentAST.child = None"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    instance-of v1, v0, Lantlr/WildcardElement;

    if-eqz v1, :cond_4

    const-string v0, "if not _t: raise antlr.MismatchedTokenException()"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v0}, Lantlr/PythonCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    :goto_0
    const-string v0, "_t = _t.getFirstChild()"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const/4 v0, 0x0

    :goto_1
    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_6

    invoke-virtual {p1, v0}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v1

    iget-object v1, v1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_2
    if-eqz v1, :cond_5

    invoke-virtual {v1}, Lantlr/GrammarElement;->generate()V

    iget-object v1, v1, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_2

    :cond_5
    add-int/lit8 v0, v0, 0x1

    goto :goto_1

    :cond_6
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    const-string v1, ""

    if-eqz v0, :cond_7

    const-string v0, "currentAST = _currentAST"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    const-string v0, "_t = _t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget p1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "_t = _t.getNextSibling()"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TreeWalkerGrammar;)V
    .locals 6

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-nez p1, :cond_0

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v0, "Internal error generating tree-walker"

    invoke-virtual {p1, v0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->setupOutput(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->buildAST:Z

    iput-boolean p1, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genHeader()V

    const-string v0, "### import antlr and other modules .."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "import sys"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "import antlr"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "version = sys.version.split()[0]"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "if version < \'2.2.1\':"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "False = 0"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "if version < \'2.3\':"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "True = not False"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "### header action >>> "

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v1, v0}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1, p1}, Lantlr/PythonCodeGenerator;->printActionCode(Ljava/lang/String;I)V

    const-string v1, "### header action <<< "

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->flushTokens()V

    const-string v1, "### user code>>>"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1, p1}, Lantlr/PythonCodeGenerator;->printActionCode(Ljava/lang/String;I)V

    const-string v1, "### user code<<<"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v1, :cond_1

    goto :goto_0

    :cond_1
    const-string v1, "antlr."

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v4, "classHeaderPrefix"

    invoke-virtual {v3, v4}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/Token;

    if-eqz v3, :cond_2

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    const-string v4, "\""

    invoke-static {v3, v4, v4}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    :cond_2
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v3}, Lantlr/PythonCodeGenerator;->genJavadocComment(Lantlr/Grammar;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "class "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/PythonCodeGenerator;->treeWalkerClassName:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "("

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "):"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "# ctor .."

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "def __init__(self, *args, **kwargs):"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".__init__(self, *args, **kwargs)"

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "self.tokenNames = _tokenNames"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v1}, Lantlr/PythonCodeGenerator;->genHeaderInit(Lantlr/Grammar;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, v0}, Lantlr/PythonCodeGenerator;->printGrammarAction(Lantlr/Grammar;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    move v1, p1

    :goto_1
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_5

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/GrammarSymbol;

    instance-of v4, v3, Lantlr/RuleSymbol;

    if-eqz v4, :cond_4

    check-cast v3, Lantlr/RuleSymbol;

    iget-object v4, v3, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v4}, Lantlr/collections/impl/Vector;->size()I

    move-result v4

    if-nez v4, :cond_3

    move v4, v2

    goto :goto_2

    :cond_3
    move v4, p1

    :goto_2
    add-int/lit8 v5, v1, 0x1

    invoke-virtual {p0, v3, v4, v1}, Lantlr/PythonCodeGenerator;->genRule(Lantlr/RuleSymbol;ZI)V

    move v1, v5

    :cond_4
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->exitIfError()V

    goto :goto_1

    :cond_5
    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genTokenStrings()V

    iget-object v0, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v1}, Lantlr/TokenManager;->maxTokenType()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lantlr/PythonCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genHeaderMain(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public gen(Lantlr/WildcardElement;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ""

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    const/4 v2, 0x1

    if-eqz v1, :cond_1

    const-string p1, "if not _t:"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "raise antlr.MismatchedTokenException()"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    goto :goto_1

    :cond_1
    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_5

    const/4 v1, 0x3

    if-eqz v0, :cond_3

    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-ne v0, v1, :cond_3

    :cond_2
    const-string v0, "_saveIndex = self.text.length()"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    const-string v0, "self.matchNot(antlr.EOF_CHAR)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_6

    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v0, :cond_4

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v1, :cond_6

    :cond_4
    const-string p1, "self.text.setLength(_saveIndex)"

    goto :goto_0

    :cond_5
    const-string p1, "self.matchNot("

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    const/4 v0, 0x0

    invoke-direct {p0, v2, v0}, Lantlr/PythonCodeGenerator;->getValueString(IZ)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ")"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    :goto_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    :goto_1
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_7

    const-string p1, "_t = _t.getNextSibling()"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    return-void
.end method

.method public gen(Lantlr/ZeroOrMoreBlock;)V
    .locals 9

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    const-string v0, "while True:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v2, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_0

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v3

    iput-object v3, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_0
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v3, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/ZeroOrMoreBlock;)Z

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v3, v3, Lantlr/Grammar;->maxk:I

    iget-boolean v4, p1, Lantlr/AlternativeBlock;->greedy:Z

    const/4 v5, 0x0

    if-nez v4, :cond_1

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    if-gt v4, v3, :cond_1

    iget-object v6, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aget-object v4, v6, v4

    invoke-virtual {v4}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v4

    if-eqz v4, :cond_1

    iget v3, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    goto :goto_0

    :cond_1
    iget-boolean v4, p1, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v4, :cond_2

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    const v6, 0x7fffffff

    if-ne v4, v6, :cond_2

    :goto_0
    move v4, v3

    move v3, v1

    goto :goto_1

    :cond_2
    move v4, v3

    move v3, v5

    :goto_1
    const-string v6, "break"

    if-eqz v3, :cond_4

    iget-boolean v3, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v3, :cond_3

    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v7, "nongreedy (...)* loop; exit depth is "

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget v8, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v3, v7}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_3
    iget-object v3, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    invoke-virtual {p0, v3, v4}, Lantlr/PythonCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object v3

    const-string v4, "###  nongreedy exit test"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "if ("

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "):"

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_4
    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1, v5}, Lantlr/PythonCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/PythonBlockFinishingInfo;

    move-result-object p1

    invoke-direct {p0, p1, v6}, Lantlr/PythonCodeGenerator;->genBlockFinish(Lantlr/PythonBlockFinishingInfo;Ljava/lang/String;)V

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iput-object v2, p0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;)V
    .locals 1

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p0, p1, v0}, Lantlr/PythonCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, p1, v0, p2}, Lantlr/PythonCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V
    .locals 0

    iget-object p3, p0, Lantlr/PythonCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {p3, p1}, Ljava/util/Hashtable;->contains(Ljava/lang/Object;)Z

    move-result p3

    if-eqz p3, :cond_0

    return-void

    :cond_0
    new-instance p3, Ljava/lang/StringBuilder;

    invoke-direct {p3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p3, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "_AST = None"

    invoke-virtual {p3, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p0, p0, Lantlr/PythonCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {p0, p1, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V
    .locals 8

    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->genAST:Z

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
    iput-boolean v3, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    iget-boolean v3, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v3, :cond_1

    invoke-virtual {p1}, Lantlr/Alternative;->getAutoGen()Z

    move-result v4

    if-eqz v4, :cond_1

    move v1, v2

    :cond_1
    iput-boolean v1, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    new-instance v4, Ljava/util/Hashtable;

    invoke-direct {v4}, Ljava/util/Hashtable;-><init>()V

    iput-object v4, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    iget-object v4, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz v4, :cond_2

    const-string v4, "try:"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v2

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    const-string v4, "pass"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, p1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_1
    instance-of v5, v4, Lantlr/BlockEndElement;

    if-nez v5, :cond_3

    invoke-virtual {v4}, Lantlr/GrammarElement;->generate()V

    iget-object v4, v4, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_1

    :cond_3
    iget-boolean v4, p0, Lantlr/PythonCodeGenerator;->genAST:Z

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

    const-string p2, "_AST = currentAST.root"

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

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
    iget-object p1, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz p1, :cond_6

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p2, v2

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    :cond_6
    iput-boolean v0, p0, Lantlr/PythonCodeGenerator;->genAST:Z

    iput-boolean v3, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    iput-object v1, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    return-void
.end method

.method public genBitsets(Lantlr/collections/impl/Vector;I)V
    .locals 2

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    invoke-virtual {p1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/collections/impl/BitSet;

    invoke-virtual {v1, p2}, Lantlr/collections/impl/BitSet;->growToInclude(I)V

    invoke-direct {p0, v1, v0}, Lantlr/PythonCodeGenerator;->genBitSet(Lantlr/collections/impl/BitSet;I)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public genBlockInitAction(Lantlr/AlternativeBlock;)V
    .locals 3

    iget-object v0, p1, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v2, 0x0

    invoke-virtual {p0, v0, p1, v1, v2}, Lantlr/PythonCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->printAction(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public genBlockPreamble(Lantlr/AlternativeBlock;)V
    .locals 6

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

    instance-of v2, v1, Lantlr/RuleRefElement;

    const-string v3, " = "

    if-nez v2, :cond_1

    instance-of v4, v1, Lantlr/AlternativeBlock;

    if-eqz v4, :cond_0

    instance-of v4, v1, Lantlr/RuleBlock;

    if-nez v4, :cond_0

    instance-of v4, v1, Lantlr/SynPredBlock;

    if-nez v4, :cond_0

    goto :goto_1

    :cond_0
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/PythonCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_6

    instance-of v2, v1, Lantlr/GrammarAtom;

    if-eqz v2, :cond_2

    move-object v2, v1

    check-cast v2, Lantlr/GrammarAtom;

    invoke-virtual {v2}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_2

    invoke-virtual {v2}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v1, v2}, Lantlr/PythonCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V

    goto/16 :goto_2

    :cond_1
    :goto_1
    if-nez v2, :cond_3

    move-object v2, v1

    check-cast v2, Lantlr/AlternativeBlock;

    iget-boolean v4, v2, Lantlr/AlternativeBlock;->not:Z

    if-eqz v4, :cond_3

    iget-object v4, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    invoke-interface {v4, v2, v5}, Lantlr/LLkGrammarAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result v2

    if-eqz v2, :cond_3

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/PythonCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_6

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/PythonCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;)V

    goto :goto_2

    :cond_3
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_4

    invoke-virtual {p0, v1}, Lantlr/PythonCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;)V

    :cond_4
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    if-eqz v2, :cond_5

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " = None"

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_6

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    :goto_2
    add-int/lit8 v0, v0, 0x1

    goto/16 :goto_0

    :cond_7
    return-void
.end method

.method public genCases(Lantlr/collections/impl/BitSet;)V
    .locals 3

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "genCases("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object p1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    const-string v0, "elif la1 and la1 in "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    const/4 v1, 0x0

    if-eqz v0, :cond_2

    const-string v0, "u\'"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    move v0, v1

    :goto_0
    array-length v2, p1

    if-ge v0, v2, :cond_1

    aget v2, p1, v0

    invoke-direct {p0, v2, v1}, Lantlr/PythonCodeGenerator;->getValueString(IZ)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    const-string p1, "\':\n"

    :goto_1
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    return-void

    :cond_2
    const-string v0, "["

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    move v0, v1

    :cond_3
    :goto_2
    array-length v2, p1

    if-ge v0, v2, :cond_4

    aget v2, p1, v0

    invoke-direct {p0, v2, v1}, Lantlr/PythonCodeGenerator;->getValueString(IZ)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    array-length v2, p1

    if-ge v0, v2, :cond_3

    const-string v2, ","

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_2

    :cond_4
    const-string p1, "]:\n"

    goto :goto_1
.end method

.method public genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/PythonBlockFinishingInfo;
    .locals 21

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    new-instance v2, Lantlr/PythonBlockFinishingInfo;

    invoke-direct {v2}, Lantlr/PythonBlockFinishingInfo;-><init>()V

    iget-boolean v3, v0, Lantlr/PythonCodeGenerator;->genAST:Z

    const/4 v4, 0x0

    const/4 v5, 0x1

    if-eqz v3, :cond_0

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result v6

    if-eqz v6, :cond_0

    move v6, v5

    goto :goto_0

    :cond_0
    move v6, v4

    :goto_0
    iput-boolean v6, v0, Lantlr/PythonCodeGenerator;->genAST:Z

    iget-boolean v6, v0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v6, :cond_1

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result v7

    if-eqz v7, :cond_1

    move v7, v5

    goto :goto_1

    :cond_1
    move v7, v4

    :goto_1
    iput-boolean v7, v0, Lantlr/PythonCodeGenerator;->saveText:Z

    iget-boolean v7, v1, Lantlr/AlternativeBlock;->not:Z

    const-string v8, ""

    if-eqz v7, :cond_6

    iget-object v7, v0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v9, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v9, Lantlr/LexerGrammar;

    invoke-interface {v7, v1, v9}, Lantlr/LLkGrammarAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result v7

    if-eqz v7, :cond_6

    iget-boolean v3, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v3, :cond_2

    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v4, "special case: ~(subrule)"

    invoke-virtual {v3, v4}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_2
    iget-object v3, v0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v3, v5, v1}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object v3

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_3

    iget v4, v0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    if-nez v4, :cond_3

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " = "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, v0, Lantlr/PythonCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    invoke-direct/range {p0 .. p1}, Lantlr/PythonCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_4

    const-string v8, "_t, "

    :cond_4
    const-string v1, "self.match("

    invoke-static {v1, v8}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, v3, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->markBitsetForGen(Lantlr/collections/impl/BitSet;)I

    move-result v3

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ")"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_5

    const-string v1, "_t = _t.getNextSibling()"

    invoke-virtual {v0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    return-object v2

    :cond_6
    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v7

    invoke-virtual {v7}, Lantlr/collections/impl/Vector;->size()I

    move-result v7

    if-ne v7, v5, :cond_9

    invoke-virtual {v1, v4}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v7

    iget-object v9, v7, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v9, :cond_7

    iget-object v9, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v10, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v10}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v1, v4}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v11

    iget-object v11, v11, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v11}, Lantlr/GrammarElement;->getLine()I

    move-result v11

    invoke-virtual {v1, v4}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v12

    iget-object v12, v12, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v12}, Lantlr/GrammarElement;->getColumn()I

    move-result v12

    const-string v13, "Syntactic predicate superfluous for single alternative"

    invoke-virtual {v9, v13, v10, v11, v12}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_7
    if-eqz p2, :cond_9

    iget-object v3, v7, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v3, :cond_8

    iget v4, v1, Lantlr/GrammarElement;->line:I

    invoke-virtual {v0, v3, v4}, Lantlr/PythonCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    :cond_8
    invoke-virtual {v0, v7, v1}, Lantlr/PythonCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    return-object v2

    :cond_9
    move v7, v4

    move v9, v7

    :goto_2
    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v10

    invoke-virtual {v10}, Lantlr/collections/impl/Vector;->size()I

    move-result v10

    if-ge v7, v10, :cond_b

    invoke-virtual {v1, v7}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v10

    invoke-static {v10}, Lantlr/PythonCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v10

    if-eqz v10, :cond_a

    add-int/lit8 v9, v9, 0x1

    :cond_a
    add-int/lit8 v7, v7, 0x1

    goto :goto_2

    :cond_b
    iget v7, v0, Lantlr/CodeGenerator;->makeSwitchThreshold:I

    const-string v10, "else:"

    const-string v11, "_t = antlr.ASTNULL"

    const-string v12, "if not _t:"

    if-lt v9, v7, :cond_10

    invoke-direct {v0, v5}, Lantlr/PythonCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v7

    iget-object v9, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v9, Lantlr/TreeWalkerGrammar;

    if-eqz v9, :cond_c

    invoke-virtual {v0, v12}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v9, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v9, v5

    iput v9, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v11}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v9, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v9, v5

    iput v9, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_c
    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    const-string v13, "la1 = "

    invoke-virtual {v9, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "if False:"

    invoke-virtual {v0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v7, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v5

    iput v7, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v7, "pass"

    invoke-virtual {v0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v7, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v7, v5

    iput v7, v0, Lantlr/CodeGenerator;->tabs:I

    move v7, v4

    :goto_3
    iget-object v9, v1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v9}, Lantlr/collections/impl/Vector;->size()I

    move-result v9

    if-ge v7, v9, :cond_f

    invoke-virtual {v1, v7}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v9

    invoke-static {v9}, Lantlr/PythonCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v13

    if-nez v13, :cond_d

    goto :goto_4

    :cond_d
    iget-object v13, v9, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v13, v13, v5

    iget-object v14, v13, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v14}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v14

    if-nez v14, :cond_e

    invoke-virtual {v13}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v14

    if-nez v14, :cond_e

    iget-object v13, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v14, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v14}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v14

    iget-object v15, v9, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v15}, Lantlr/GrammarElement;->getLine()I

    move-result v15

    iget-object v9, v9, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v9}, Lantlr/GrammarElement;->getColumn()I

    move-result v9

    const-string v4, "Alternate omitted due to empty prediction set"

    invoke-virtual {v13, v4, v14, v15, v9}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_4

    :cond_e
    iget-object v4, v13, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v4}, Lantlr/PythonCodeGenerator;->genCases(Lantlr/collections/impl/BitSet;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v9, v1}, Lantlr/PythonCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    :goto_4
    add-int/lit8 v7, v7, 0x1

    const/4 v4, 0x0

    goto :goto_3

    :cond_f
    invoke-virtual {v0, v10}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    move v4, v5

    goto :goto_5

    :cond_10
    const/4 v4, 0x0

    :goto_5
    iget-object v7, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v7, Lantlr/LexerGrammar;

    if-eqz v9, :cond_11

    iget v7, v7, Lantlr/Grammar;->maxk:I

    goto :goto_6

    :cond_11
    const/4 v7, 0x0

    :goto_6
    const/4 v9, 0x0

    :goto_7
    if-ltz v7, :cond_25

    move v13, v9

    const/4 v9, 0x0

    :goto_8
    iget-object v14, v1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v14}, Lantlr/collections/impl/Vector;->size()I

    move-result v14

    if-ge v9, v14, :cond_24

    invoke-virtual {v1, v9}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v14

    iget-boolean v15, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v15, :cond_12

    sget-object v15, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v17, v8

    const-string v8, "genAlt: "

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v15, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_9

    :cond_12
    move-object/from16 v17, v8

    :goto_9
    if-eqz v4, :cond_13

    invoke-static {v14}, Lantlr/PythonCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v5

    if-eqz v5, :cond_13

    iget-boolean v5, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v5, :cond_16

    sget-object v5, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v8, "ignoring alt because it was in the switch"

    :goto_a
    invoke-virtual {v5, v8}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_c

    :cond_13
    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v8, v5, Lantlr/LexerGrammar;

    if-eqz v8, :cond_18

    iget v8, v14, Lantlr/Alternative;->lookaheadDepth:I

    const v15, 0x7fffffff

    if-ne v8, v15, :cond_14

    iget v8, v5, Lantlr/Grammar;->maxk:I

    :cond_14
    :goto_b
    const/4 v5, 0x1

    if-lt v8, v5, :cond_15

    iget-object v5, v14, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v5, v5, v8

    invoke-virtual {v5}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v5

    if-eqz v5, :cond_15

    add-int/lit8 v8, v8, -0x1

    goto :goto_b

    :cond_15
    if-eq v8, v7, :cond_17

    iget-boolean v5, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v5, :cond_16

    sget-object v5, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v14, Ljava/lang/StringBuilder;

    invoke-direct {v14}, Ljava/lang/StringBuilder;-><init>()V

    const-string v15, "ignoring alt because effectiveDepth!=altDepth"

    invoke-virtual {v14, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v8, "!="

    invoke-virtual {v14, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v14}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    goto :goto_a

    :cond_16
    :goto_c
    move-object/from16 v19, v2

    move/from16 v20, v3

    move/from16 p2, v4

    move/from16 v18, v6

    goto/16 :goto_15

    :cond_17
    invoke-virtual {v0, v14, v8}, Lantlr/PythonCodeGenerator;->lookaheadIsEmpty(Lantlr/Alternative;I)Z

    move-result v5

    goto :goto_d

    :cond_18
    iget v5, v5, Lantlr/Grammar;->maxk:I

    invoke-virtual {v0, v14, v5}, Lantlr/PythonCodeGenerator;->lookaheadIsEmpty(Lantlr/Alternative;I)Z

    move-result v5

    iget-object v8, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v8, v8, Lantlr/Grammar;->maxk:I

    :goto_d
    invoke-virtual {v0, v14, v8}, Lantlr/PythonCodeGenerator;->getLookaheadTestExpression(Lantlr/Alternative;I)Ljava/lang/String;

    move-result-object v8

    iget-object v15, v14, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    const/16 v16, 0x1

    aget-object v15, v15, v16

    iget-object v15, v15, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v15}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v15

    move/from16 p2, v4

    const/16 v4, 0x7f

    move/from16 v18, v6

    const-string v6, ":"

    if-le v15, v4, :cond_1a

    invoke-static {v14}, Lantlr/PythonCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v4

    if-eqz v4, :cond_1a

    new-instance v4, Ljava/lang/StringBuilder;

    if-nez v13, :cond_19

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "<m1> if "

    goto :goto_e

    :cond_19
    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "<m2> elif "

    :goto_e
    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_f
    move-object/from16 v19, v2

    move/from16 v20, v3

    goto/16 :goto_14

    :cond_1a
    if-eqz v5, :cond_1c

    iget-object v4, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-nez v4, :cond_1c

    iget-object v4, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-nez v4, :cond_1c

    if-nez v13, :cond_1b

    const-string v4, "##<m3> <closing"

    invoke-virtual {v0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_10

    :cond_1b
    const-string v4, "else: ## <m4>"

    invoke-virtual {v0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    add-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    :goto_10
    const/4 v4, 0x0

    iput-boolean v4, v2, Lantlr/PythonBlockFinishingInfo;->needAnErrorClause:Z

    goto :goto_f

    :cond_1c
    iget-object v4, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v4, :cond_1f

    new-instance v4, Lantlr/ActionTransInfo;

    invoke-direct {v4}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v5, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    iget v15, v1, Lantlr/GrammarElement;->line:I

    move-object/from16 v19, v2

    iget-object v2, v0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v0, v5, v15, v2, v4}, Lantlr/PythonCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v2

    iget-object v4, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v4, Lantlr/ParserGrammar;

    const-string v15, "))"

    move/from16 v20, v3

    const-string v3, "("

    if-nez v5, :cond_1d

    instance-of v4, v4, Lantlr/LexerGrammar;

    if-eqz v4, :cond_1e

    :cond_1d
    iget-object v4, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v4, :cond_1e

    const-string v4, " and fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.PREDICTING, "

    invoke-static {v3, v8, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v4, v0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v4, v2}, Lantlr/CharFormatter;->escapeString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v4}, Lantlr/PythonCodeGenerator;->addSemPred(Ljava/lang/String;)I

    move-result v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, ", "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v3, v2, v15}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    goto :goto_11

    :cond_1e
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " and ("

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    goto :goto_11

    :cond_1f
    move-object/from16 v19, v2

    move/from16 v20, v3

    :goto_11
    iget-object v2, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-lez v13, :cond_21

    if-eqz v2, :cond_20

    invoke-virtual {v0, v10}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v2, v3

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    goto :goto_13

    :cond_20
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "elif "

    :goto_12
    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_14

    :cond_21
    if-eqz v2, :cond_22

    :goto_13
    invoke-virtual {v0, v2, v8}, Lantlr/PythonCodeGenerator;->genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V

    goto :goto_14

    :cond_22
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_23

    invoke-virtual {v0, v12}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v2, v3

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v11}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v3

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_23
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "if "

    goto :goto_12

    :goto_14
    add-int/lit8 v13, v13, 0x1

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v2, v3

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v14, v1}, Lantlr/PythonCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v3

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    :goto_15
    add-int/lit8 v9, v9, 0x1

    move/from16 v4, p2

    move-object/from16 v8, v17

    move/from16 v6, v18

    move-object/from16 v2, v19

    move/from16 v3, v20

    const/4 v5, 0x1

    goto/16 :goto_8

    :cond_24
    move-object/from16 v19, v2

    move/from16 v20, v3

    move/from16 p2, v4

    move/from16 v18, v6

    move-object/from16 v17, v8

    add-int/lit8 v7, v7, -0x1

    move v9, v13

    const/4 v5, 0x1

    goto/16 :goto_7

    :cond_25
    move-object/from16 v19, v2

    move v2, v3

    move/from16 p2, v4

    move/from16 v18, v6

    move-object/from16 v17, v8

    iput-boolean v2, v0, Lantlr/PythonCodeGenerator;->genAST:Z

    move/from16 v1, v18

    iput-boolean v1, v0, Lantlr/PythonCodeGenerator;->saveText:Z

    move-object/from16 v1, v17

    move-object/from16 v0, v19

    if-eqz p2, :cond_27

    iput-object v1, v0, Lantlr/PythonBlockFinishingInfo;->postscript:Ljava/lang/String;

    const/4 v5, 0x1

    iput-boolean v5, v0, Lantlr/PythonBlockFinishingInfo;->generatedSwitch:Z

    if-lez v9, :cond_26

    goto :goto_16

    :cond_26
    const/4 v1, 0x0

    goto :goto_17

    :cond_27
    const/4 v5, 0x1

    iput-object v1, v0, Lantlr/PythonBlockFinishingInfo;->postscript:Ljava/lang/String;

    const/4 v1, 0x0

    iput-boolean v1, v0, Lantlr/PythonBlockFinishingInfo;->generatedSwitch:Z

    if-lez v9, :cond_28

    :goto_16
    move v1, v5

    :cond_28
    :goto_17
    iput-boolean v1, v0, Lantlr/PythonBlockFinishingInfo;->generatedAnIf:Z

    return-object v0
.end method

.method public genHeader()V
    .locals 3

    const-string v0, "### $ANTLR "

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

    const-string v1, ".py\"$"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genHeaderInit(Lantlr/Grammar;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "."

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "__init__"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v1, v0}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lantlr/PythonCodeGenerator;->isEmpty(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_0

    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v0, p1}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_0
    invoke-static {v0}, Lantlr/PythonCodeGenerator;->isEmpty(Ljava/lang/String;)Z

    move-result p1

    if-eqz p1, :cond_1

    goto :goto_0

    :cond_1
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "### __init__ header action >>> "

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1}, Lantlr/PythonCodeGenerator;->printActionCode(Ljava/lang/String;I)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "### __init__ header action <<< "

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_0
    return-void
.end method

.method public genHeaderMain(Lantlr/Grammar;)V
    .locals 5

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "__main__"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object v2, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v2, v0}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lantlr/PythonCodeGenerator;->isEmpty(Ljava/lang/String;)Z

    move-result v2

    if-eqz v2, :cond_0

    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    invoke-virtual {v0, v1}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_0
    invoke-static {v0}, Lantlr/PythonCodeGenerator;->isEmpty(Ljava/lang/String;)Z

    move-result v1

    const-string v2, "### __main__ header action <<< "

    const-string v3, "### __main__ header action >>> "

    const/4 v4, 0x0

    if-eqz v1, :cond_1

    instance-of p1, p1, Lantlr/LexerGrammar;

    if-eqz p1, :cond_2

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->genLexerTest()V

    goto :goto_0

    :cond_1
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/PythonCodeGenerator;->printMainFunc(Ljava/lang/String;)V

    :goto_0
    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    return-void
.end method

.method public genJavadocComment(Lantlr/Grammar;)V
    .locals 0

    iget-object p1, p1, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz p1, :cond_0

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->_printJavadoc(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public genJavadocComment(Lantlr/RuleSymbol;)V
    .locals 0

    iget-object p1, p1, Lantlr/RuleSymbol;->comment:Ljava/lang/String;

    if-eqz p1, :cond_0

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->_printJavadoc(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public genLexerTest()V
    .locals 4

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    const-string v1, "if __name__ == \'__main__\' :"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "import sys"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "import antlr"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "import "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "### create lexer - shall read from stdin"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "try:"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "for token in "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ".Lexer():"

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "print token"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "except antlr.TokenStreamException, e:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "print \"error: exception caught while lexing: \", e"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

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
    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genMatchUsingAtomTokenType(Lantlr/GrammarAtom;)V

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
    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->genMatchUsingAtomText(Lantlr/GrammarAtom;)V

    goto :goto_1

    :cond_4
    instance-of v0, p1, Lantlr/WildcardElement;

    if-eqz v0, :cond_5

    check-cast p1, Lantlr/WildcardElement;

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->gen(Lantlr/WildcardElement;)V

    :cond_5
    :goto_1
    return-void
.end method

.method public genMatch(Lantlr/collections/impl/BitSet;)V
    .locals 0

    return-void
.end method

.method public genMatchUsingAtomText(Lantlr/GrammarAtom;)V
    .locals 3

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_0

    const-string v0, "_t,"

    goto :goto_0

    :cond_0
    const-string v0, ""

    :goto_0
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    const/4 v2, 0x3

    if-eqz v1, :cond_2

    iget-boolean v1, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v1, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-ne v1, v2, :cond_2

    :cond_1
    const-string v1, "_saveIndex = self.text.length()"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    iget-boolean v1, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v1, :cond_3

    const-string v1, "self.matchNot("

    goto :goto_1

    :cond_3
    const-string v1, "self.match("

    :goto_1
    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    const-string v1, "EOF"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_4

    const-string v0, "EOF_TYPE"

    goto :goto_2

    :cond_4
    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    :goto_2
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string v0, ")"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_6

    iget-boolean v0, p0, Lantlr/PythonCodeGenerator;->saveText:Z

    if-eqz v0, :cond_5

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v2, :cond_6

    :cond_5
    const-string p1, "self.text.setLength(_saveIndex)"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    return-void
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

    const/4 v2, 0x1

    invoke-direct {p0, v1, v2}, Lantlr/PythonCodeGenerator;->getValueString(IZ)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-boolean p1, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz p1, :cond_1

    const-string p1, "self.matchNot("

    goto :goto_1

    :cond_1
    const-string p1, "self.match("

    :goto_1
    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genNextToken()V
    .locals 14

    const/4 v0, 0x0

    move v1, v0

    :goto_0
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    const-string v3, "public"

    const/4 v4, 0x1

    if-ge v1, v2, :cond_1

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v2, v1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/RuleSymbol;

    invoke-virtual {v2}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v5

    if-eqz v5, :cond_0

    iget-object v2, v2, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_0

    move v1, v4

    goto :goto_1

    :cond_0
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    move v1, v0

    :goto_1
    const-string v2, "raise antlr.TokenStreamException(str(cse))"

    const-string v5, "try:"

    const-string v6, ""

    if-nez v1, :cond_2

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "def nextToken(self): "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "self.uponEOF()"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "except antlr.CharStreamIOException, csioe:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "raise antlr.TokenStreamIOException(csioe.io)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "except antlr.CharStreamException, cse:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "return antlr.CommonToken(type=EOF_TYPE, text=\"\")"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_2
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    return-void

    :cond_2
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    const-string v8, "nextToken"

    invoke-static {v1, v7, v8}, Lantlr/MakeGrammar;->createNextTokenRule(Lantlr/Grammar;Lantlr/collections/impl/Vector;Ljava/lang/String;)Lantlr/RuleBlock;

    move-result-object v1

    new-instance v7, Lantlr/RuleSymbol;

    const-string v8, "mnextToken"

    invoke-direct {v7, v8}, Lantlr/RuleSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {v7}, Lantlr/RuleSymbol;->setDefined()V

    invoke-virtual {v7, v1}, Lantlr/RuleSymbol;->setBlock(Lantlr/RuleBlock;)V

    const-string v8, "private"

    iput-object v8, v7, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8, v7}, Lantlr/Grammar;->define(Lantlr/RuleSymbol;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v7, v1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    const/4 v7, 0x0

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v8, Lantlr/LexerGrammar;

    iget-boolean v9, v8, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v9, :cond_3

    iget-object v7, v8, Lantlr/LexerGrammar;->filterRule:Ljava/lang/String;

    :cond_3
    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v8, "def nextToken(self):"

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, v4

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v8, "while True:"

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v9, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v9, v4

    iput v9, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v9, "try: ### try again .."

    invoke-virtual {p0, v9}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v9, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v9, v4

    iput v9, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, v4

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v8, "_token = None"

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v8, "_ttype = INVALID_TYPE"

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v8, Lantlr/LexerGrammar;

    iget-boolean v8, v8, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v8, :cond_7

    const-string v8, "self.setCommitToPath(False)"

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    if-eqz v7, :cond_7

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v7}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Lantlr/Grammar;->isDefined(Ljava/lang/String;)Z

    move-result v8

    const-string v9, " does not exist in this lexer"

    const-string v10, "Filter rule "

    if-nez v8, :cond_4

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    :goto_3
    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_4
    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v3, v8}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_5

    :cond_4
    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v7}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v8, v11}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v8

    check-cast v8, Lantlr/RuleSymbol;

    invoke-virtual {v8}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v11

    if-nez v11, :cond_5

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    goto :goto_3

    :cond_5
    iget-object v8, v8, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v8, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_6

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, " must be protected"

    goto :goto_4

    :cond_6
    :goto_5
    const-string v3, "_m = self.mark()"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    const-string v3, "self.resetText()"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v8, "try: ## for char stream error handling"

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, v4

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v8, "try: ##for lexical error handling"

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, v4

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    move v9, v0

    :goto_6
    invoke-virtual {v1}, Lantlr/RuleBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v10

    invoke-virtual {v10}, Lantlr/collections/impl/Vector;->size()I

    move-result v10

    if-ge v9, v10, :cond_9

    invoke-virtual {v1, v9}, Lantlr/RuleBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v10

    iget-object v11, v10, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v11, v11, v4

    invoke-virtual {v11}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v11

    if-eqz v11, :cond_8

    iget-object v10, v10, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    check-cast v10, Lantlr/RuleRefElement;

    iget-object v10, v10, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-static {v10}, Lantlr/CodeGenerator;->decodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v10

    iget-object v11, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    const-string v13, "public lexical rule "

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v10, " is optional (can match \"nothing\")"

    invoke-virtual {v12, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v11, v10}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    :cond_8
    add-int/lit8 v9, v9, 0x1

    goto :goto_6

    :cond_9
    const-string v9, "line.separator"

    invoke-static {v9}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    invoke-virtual {p0, v1, v0}, Lantlr/PythonCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/PythonBlockFinishingInfo;

    move-result-object v0

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v9, Lantlr/LexerGrammar;

    iget-boolean v9, v9, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v9, :cond_b

    if-nez v7, :cond_a

    const-string v9, "self.filterdefault(self.LA(1))"

    invoke-static {v6, v9}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    goto :goto_7

    :cond_a
    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v9, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "self.filterdefault(self.LA(1), self.m"

    invoke-virtual {v9, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, ", False)"

    invoke-virtual {v9, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    goto :goto_7

    :cond_b
    const-string v6, "self.default(self.LA(1))"

    :goto_7
    invoke-direct {p0, v0, v6}, Lantlr/PythonCodeGenerator;->genBlockFinish1(Lantlr/PythonBlockFinishingInfo;Ljava/lang/String;)V

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-boolean v0, v0, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v0, :cond_c

    if-eqz v7, :cond_c

    const-string v0, "self.commit()"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    const-string v0, "if not self._returnToken:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "raise antlr.TryAgain ### found SKIP token"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    invoke-virtual {v0}, Lantlr/LexerGrammar;->getTestLiterals()Z

    move-result v0

    if-eqz v0, :cond_d

    const-string v0, "### option { testLiterals=true } "

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "self.testForLiteral(self._returnToken)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_d
    const-string v0, "### return token to caller"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "return self._returnToken"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "### handle lexical errors ...."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "except antlr.RecognitionException, e:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-boolean v0, v0, Lantlr/LexerGrammar;->filterMode:Z

    const-string v6, "self.consume()"

    if-eqz v0, :cond_f

    const-string v0, "raise antlr.TryAgain()"

    if-nez v7, :cond_e

    const-string v3, "if not self.getCommitToPath():"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_8

    :cond_e
    const-string v8, "if not self.getCommitToPath(): "

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, v4

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v8, "self.rewind(_m)"

    invoke-virtual {p0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "self.m"

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "(False)"

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "except antlr.RecognitionException, ee:"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "### horrendous failure: error in filter rule"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "self.reportError(ee)"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    :goto_8
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_f
    invoke-virtual {v1}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v0

    if-eqz v0, :cond_10

    const-string v0, "self.reportError(e)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_9

    :cond_10
    const-string v0, "raise antlr.TokenStreamRecognitionException(e)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_9
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "### handle char stream errors ..."

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "except antlr.CharStreamException,cse:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "if isinstance(cse, antlr.CharStreamIOException):"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "raise antlr.TokenStreamIOException(cse.io)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "else:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "except antlr.TryAgain:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "pass"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    goto/16 :goto_2
.end method

.method public genRule(Lantlr/RuleSymbol;ZI)V
    .locals 16

    move-object/from16 v0, p0

    move/from16 v1, p3

    const/4 v2, 0x1

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual/range {p1 .. p1}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v3

    if-nez v3, :cond_0

    iget-object v0, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "undefined rule: "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-void

    :cond_0
    invoke-virtual/range {p1 .. p1}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v3

    iput-object v3, v0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lantlr/PythonCodeGenerator;->currentASTResult:Ljava/lang/String;

    iget-object v4, v0, Lantlr/PythonCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {v4}, Ljava/util/Hashtable;->clear()V

    iget-boolean v4, v0, Lantlr/PythonCodeGenerator;->genAST:Z

    const/4 v5, 0x0

    if-eqz v4, :cond_1

    invoke-virtual {v3}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v6

    if-eqz v6, :cond_1

    move v6, v2

    goto :goto_0

    :cond_1
    move v6, v5

    :goto_0
    iput-boolean v6, v0, Lantlr/PythonCodeGenerator;->genAST:Z

    invoke-virtual {v3}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v6

    iput-boolean v6, v0, Lantlr/PythonCodeGenerator;->saveText:Z

    invoke-virtual/range {p0 .. p1}, Lantlr/PythonCodeGenerator;->genJavadocComment(Lantlr/RuleSymbol;)V

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "def "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "("

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v6, v0, Lantlr/PythonCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v6, v0, Lantlr/PythonCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {v6}, Ljava/lang/String;->length()I

    move-result v6

    if-eqz v6, :cond_2

    iget-object v6, v3, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz v6, :cond_2

    const-string v6, ","

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_2
    iget-object v6, v3, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    const-string v7, "):"

    const-string v8, ""

    if-eqz v6, :cond_3

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget v6, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v6, v2

    iput v6, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v6, v3, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v6, v2

    iput v6, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v7}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_1

    :cond_3
    invoke-virtual {v0, v7}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :goto_1
    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v6, v2

    iput v6, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v6, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v6, :cond_5

    const/16 v7, 0x3d

    invoke-virtual {v6, v7}, Ljava/lang/String;->indexOf(I)I

    move-result v6

    if-ltz v6, :cond_4

    iget-object v6, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    goto :goto_2

    :cond_4
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v3}, Lantlr/RuleBlock;->getLine()I

    move-result v9

    invoke-virtual {v3}, Lantlr/RuleBlock;->getColumn()I

    move-result v10

    invoke-virtual {v0, v7, v9, v10}, Lantlr/PythonCodeGenerator;->extractIdOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, " = None"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    :goto_2
    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v6, v0, Lantlr/PythonCodeGenerator;->commonLocalVars:Ljava/lang/String;

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v7, v6, Lantlr/Grammar;->traceRules:Z

    const-string v9, "\")"

    if-eqz v7, :cond_7

    instance-of v6, v6, Lantlr/TreeWalkerGrammar;

    const-string v7, "self.traceIn(\""

    if-eqz v6, :cond_6

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\",_t)"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_3

    :cond_6
    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_3
    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v6, v6, Lantlr/LexerGrammar;

    if-eqz v6, :cond_9

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    const-string v7, "mEOF"

    invoke-virtual {v6, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_8

    const-string v6, "_ttype = EOF_TYPE"

    goto :goto_4

    :cond_8
    const-string v6, "_ttype = "

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v7, v2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    :goto_4
    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v6, "_saveIndex = 0"

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v7, v6, Lantlr/Grammar;->debuggingOutput:Z

    const-string v10, ", _ttype)"

    const-string v11, ", 0)"

    if-eqz v7, :cond_b

    instance-of v7, v6, Lantlr/ParserGrammar;

    const-string v12, "self.fireEnterRule("

    if-eqz v7, :cond_a

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_5
    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_6

    :cond_a
    instance-of v6, v6, Lantlr/LexerGrammar;

    if-eqz v6, :cond_b

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_5

    :cond_b
    :goto_6
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v7, v6, Lantlr/Grammar;->debuggingOutput:Z

    if-nez v7, :cond_c

    iget-boolean v6, v6, Lantlr/Grammar;->traceRules:Z

    if-eqz v6, :cond_d

    :cond_c
    const-string v6, "try: ### debugging"

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v6, v2

    iput v6, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_d
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v6, v6, Lantlr/TreeWalkerGrammar;

    if-eqz v6, :cond_e

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "_AST_in = None"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v6, "if _t != antlr.ASTNULL:"

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v6, v2

    iput v6, v0, Lantlr/CodeGenerator;->tabs:I

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "_AST_in = _t"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v6, v2

    iput v6, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_e
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v6, v6, Lantlr/Grammar;->buildAST:Z

    if-eqz v6, :cond_f

    const-string v6, "self.returnAST = None"

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v6, "currentAST = antlr.ASTPair()"

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "_AST = None"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    invoke-virtual {v0, v3}, Lantlr/PythonCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {v0, v3}, Lantlr/PythonCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    invoke-virtual {v3, v8}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object v6

    if-nez v6, :cond_10

    invoke-virtual {v3}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v7

    if-eqz v7, :cond_11

    :cond_10
    const-string v7, "try:      ## for error handling"

    invoke-virtual {v0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v7, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v2

    iput v7, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_11
    iget v7, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v12, v3, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v12}, Lantlr/collections/impl/Vector;->size()I

    move-result v12

    if-ne v12, v2, :cond_14

    invoke-virtual {v3, v5}, Lantlr/RuleBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v5

    iget-object v12, v5, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v12, :cond_12

    iget-object v13, v0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget v13, v13, Lantlr/GrammarElement;->line:I

    invoke-virtual {v0, v12, v13}, Lantlr/PythonCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    :cond_12
    iget-object v12, v5, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v12, :cond_13

    iget-object v12, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v13, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v13}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v13

    iget-object v14, v5, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v14}, Lantlr/GrammarElement;->getLine()I

    move-result v14

    iget-object v15, v5, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v15}, Lantlr/GrammarElement;->getColumn()I

    move-result v15

    const-string v2, "Syntactic predicate ignored for single alternative"

    invoke-virtual {v12, v2, v13, v14, v15}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_13
    invoke-virtual {v0, v5, v3}, Lantlr/PythonCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    goto :goto_7

    :cond_14
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v2, v3}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    invoke-virtual {v0, v3, v5}, Lantlr/PythonCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/PythonBlockFinishingInfo;

    move-result-object v2

    iget-object v5, v0, Lantlr/PythonCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-direct {v0, v2, v5}, Lantlr/PythonCodeGenerator;->genBlockFinish(Lantlr/PythonBlockFinishingInfo;Ljava/lang/String;)V

    :goto_7
    iput v7, v0, Lantlr/CodeGenerator;->tabs:I

    if-nez v6, :cond_15

    invoke-virtual {v3}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v2

    if-eqz v2, :cond_16

    :cond_15
    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    sub-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_16
    if-eqz v6, :cond_17

    invoke-direct {v0, v6}, Lantlr/PythonCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    goto/16 :goto_9

    :cond_17
    invoke-virtual {v3}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v2

    if-eqz v2, :cond_1b

    const-string v2, "except "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v5, v0, Lantlr/PythonCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ", ex:"

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    add-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v2, :cond_18

    const-string v2, "if not self.inputState.guessing:"

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_18
    const-string v2, "self.reportError(ex)"

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v6, v2, Lantlr/TreeWalkerGrammar;

    if-nez v6, :cond_19

    iget-object v2, v2, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v6, v3, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    invoke-interface {v2, v5, v6}, Lantlr/LLkGrammarAnalyzer;->FOLLOW(ILantlr/RuleEndElement;)Lantlr/Lookahead;

    move-result-object v2

    iget-object v2, v2, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->markBitsetForGen(Lantlr/collections/impl/BitSet;)I

    move-result v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v2

    const-string v5, "self.consume()"

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "self.consumeUntil("

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const/4 v5, 0x1

    goto :goto_8

    :cond_19
    const-string v2, "if _t:"

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    add-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "_t = _t.getNextSibling()"

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    :goto_8
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v2, :cond_1a

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "else:"

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "raise ex"

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_1a
    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v5

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1b
    :goto_9
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_1c

    const-string v2, "self.returnAST = "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "_AST"

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1c
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_1d

    const-string v2, "self._retTree = _t"

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1d
    invoke-virtual {v3}, Lantlr/RuleBlock;->getTestLiterals()Z

    move-result v2

    if-eqz v2, :cond_1f

    move-object/from16 v2, p1

    iget-object v5, v2, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    const-string v6, "protected"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_1e

    invoke-direct/range {p0 .. p0}, Lantlr/PythonCodeGenerator;->genLiteralsTestForPartialToken()V

    goto :goto_a

    :cond_1e
    invoke-direct/range {p0 .. p0}, Lantlr/PythonCodeGenerator;->genLiteralsTest()V

    goto :goto_a

    :cond_1f
    move-object/from16 v2, p1

    :goto_a
    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_20

    const-string v5, "self.set_return_token(_createToken, _token, _ttype, _begin)"

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_20
    iget-object v5, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v5, :cond_21

    const-string v5, "return "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget-object v6, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v3}, Lantlr/RuleBlock;->getLine()I

    move-result v7

    invoke-virtual {v3}, Lantlr/RuleBlock;->getColumn()I

    move-result v3

    invoke-virtual {v0, v6, v7, v3}, Lantlr/PythonCodeGenerator;->extractIdOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_21
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v3, Lantlr/Grammar;->debuggingOutput:Z

    if-nez v5, :cond_23

    iget-boolean v3, v3, Lantlr/Grammar;->traceRules:Z

    if-eqz v3, :cond_22

    goto :goto_b

    :cond_22
    const/4 v2, 0x1

    goto/16 :goto_f

    :cond_23
    :goto_b
    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    sub-int/2addr v3, v5

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "finally:  ### debugging"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v5

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v3, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v5, :cond_25

    instance-of v5, v3, Lantlr/ParserGrammar;

    const-string v6, "self.fireExitRule("

    if-eqz v5, :cond_24

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_c
    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_d

    :cond_24
    instance-of v3, v3, Lantlr/LexerGrammar;

    if-eqz v3, :cond_25

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_c

    :cond_25
    :goto_d
    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v1, Lantlr/Grammar;->traceRules:Z

    if-eqz v3, :cond_27

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    const-string v3, "self.traceOut(\""

    if-eqz v1, :cond_26

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\", _t)"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_e

    :cond_26
    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_e
    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_27
    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    sub-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    :goto_f
    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput-boolean v4, v0, Lantlr/PythonCodeGenerator;->genAST:Z

    return-void
.end method

.method public genSemPred(Ljava/lang/String;I)V
    .locals 2

    new-instance v0, Lantlr/ActionTransInfo;

    invoke-direct {v0}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, p1, p2, v1, v0}, Lantlr/PythonCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    iget-object p2, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {p2, p1}, Lantlr/CharFormatter;->escapeString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p2

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v0, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v1, :cond_1

    instance-of v1, v0, Lantlr/ParserGrammar;

    if-nez v1, :cond_0

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_1

    :cond_0
    const-string v0, "fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.VALIDATING,"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0, p2}, Lantlr/PythonCodeGenerator;->addSemPred(Ljava/lang/String;)I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ", "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    :cond_1
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "if not "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ":"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "raise antlr.SemanticException(\""

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\")"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    return-void
.end method

.method public genSemPredMap()V
    .locals 3

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    const-string v1, "_semPredNames = ["

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_0

    const-string v1, "\""

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, "\","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "]"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V
    .locals 5

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    const-string v1, ")"

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "gen=>("

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const-string v0, "synPredMatched"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget v3, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, " = False"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "if "

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ":"

    invoke-virtual {v2, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_1

    const-string v2, "_t"

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " = _t"

    goto :goto_0

    :cond_1
    const-string v2, "_m"

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " = self.mark()"

    :goto_0
    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " = True"

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "self.inputState.guessing += 1"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v4, :cond_3

    instance-of v4, v2, Lantlr/ParserGrammar;

    if-nez v4, :cond_2

    instance-of v2, v2, Lantlr/LexerGrammar;

    if-eqz v2, :cond_3

    :cond_2
    const-string v2, "self.fireSyntacticPredicateStarted()"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget v2, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    const-string v2, "try:"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->gen(Lantlr/AlternativeBlock;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, -0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "except "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v4, p0, Lantlr/PythonCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ", pe:"

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_4

    const-string v0, "_t = _t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ""

    goto :goto_1

    :cond_4
    const-string v0, "self.rewind(_m"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    :goto_1
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "self.inputState.guessing -= 1"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v0, Lantlr/Grammar;->debuggingOutput:Z

    const-string v2, "if synPredMatched"

    if-eqz v1, :cond_6

    instance-of v1, v0, Lantlr/ParserGrammar;

    if-nez v1, :cond_5

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_6

    :cond_5
    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "self.fireSyntacticPredicateSucceeded()"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "else:"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "self.fireSyntacticPredicateFailed()"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_6
    iget v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/PythonCodeGenerator;->syntacticPredLevel:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget p1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genTokenASTNodeMap()V
    .locals 8

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "def buildTokenTypeASTClassMap(self):"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v0

    const/4 v2, 0x0

    move v3, v2

    move v4, v3

    :goto_0
    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v5

    if-ge v2, v5, :cond_2

    invoke-virtual {v0, v2}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Ljava/lang/String;

    if-eqz v5, :cond_1

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v6, v6, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v6, v5}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v5

    if-eqz v5, :cond_1

    invoke-virtual {v5}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v6

    if-eqz v6, :cond_1

    add-int/lit8 v3, v3, 0x1

    if-nez v4, :cond_0

    const-string v4, "self.tokenTypeToASTClassMap = {}"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    move v4, v1

    :cond_0
    const-string v6, "self.tokenTypeToASTClassMap["

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v5}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v7, "] = "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_2
    if-nez v3, :cond_3

    const-string v0, "self.tokenTypeToASTClassMap = None"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    return-void
.end method

.method public genTokenStrings()V
    .locals 8

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x0

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, ""

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "_tokenNames = ["

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v3

    :goto_0
    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->size()I

    move-result v4

    if-ge v1, v4, :cond_3

    invoke-virtual {v3, v1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/String;

    const-string v5, "<"

    if-nez v4, :cond_0

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-static {v1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, ">"

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    :cond_0
    const-string v6, "\""

    invoke-virtual {v4, v6}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v7

    if-nez v7, :cond_1

    invoke-virtual {v4, v5}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v5

    if-nez v5, :cond_1

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v5, v4}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v5

    if-eqz v5, :cond_1

    invoke-virtual {v5}, Lantlr/TokenSymbol;->getParaphrase()Ljava/lang/String;

    move-result-object v7

    if-eqz v7, :cond_1

    invoke-virtual {v5}, Lantlr/TokenSymbol;->getParaphrase()Ljava/lang/String;

    move-result-object v4

    invoke-static {v4, v6, v6}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    :cond_1
    iget-object v5, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v5, v4}, Lantlr/CharFormatter;->literalString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->size()I

    move-result v4

    add-int/lit8 v4, v4, -0x1

    if-eq v1, v4, :cond_2

    const-string v4, ", "

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_3
    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "]"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    return-void
.end method

.method public genTokenTypes(Lantlr/TokenManager;)V
    .locals 8

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-interface {p1}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v0

    const-string v1, "SKIP                = antlr.SKIP"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "INVALID_TYPE        = antlr.INVALID_TYPE"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "EOF_TYPE            = antlr.EOF_TYPE"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "EOF                 = antlr.EOF"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "NULL_TREE_LOOKAHEAD = antlr.NULL_TREE_LOOKAHEAD"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "MIN_USER_TYPE       = antlr.MIN_USER_TYPE"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const/4 v1, 0x4

    :goto_0
    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    if-ge v1, v2, :cond_5

    invoke-virtual {v0, v1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    if-eqz v2, :cond_4

    const-string v3, "\""

    invoke-virtual {v2, v3}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v3

    const-string v4, " = "

    if-eqz v3, :cond_3

    invoke-interface {p1, v2}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v3

    check-cast v3, Lantlr/StringLiteralSymbol;

    if-nez v3, :cond_0

    iget-object v5, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "String literal "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, " not in symbol table"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v6}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    iget-object v5, v3, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    if-eqz v5, :cond_1

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, v3, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    goto :goto_2

    :cond_1
    invoke-direct {p0, v2}, Lantlr/PythonCodeGenerator;->mangleLiteral(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    if-eqz v5, :cond_2

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput-object v5, v3, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    goto :goto_3

    :cond_2
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "### "

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_1

    :cond_3
    const-string v3, "<"

    invoke-virtual {v2, v3}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v3

    if-nez v3, :cond_4

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    :goto_1
    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    :goto_2
    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    :goto_3
    add-int/lit8 v1, v1, 0x1

    goto/16 :goto_0

    :cond_5
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->exitIfError()V

    return-void
.end method

.method public getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    const-string p0, "self.astFactory.create("

    const-string v0, ", "

    invoke-static {p0, p2, v0}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-virtual {p0, p2}, Lantlr/PythonCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getASTCreateString(Lantlr/collections/impl/Vector;)Ljava/lang/String;
    .locals 2

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result p0

    if-nez p0, :cond_0

    const-string p0, ""

    return-object p0

    :cond_0
    new-instance p0, Ljava/lang/StringBuffer;

    invoke-direct {p0}, Ljava/lang/StringBuffer;-><init>()V

    const-string v0, "antlr.make("

    invoke-virtual {p0, v0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const/4 v0, 0x0

    :cond_1
    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_2

    invoke-virtual {p1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    invoke-virtual {p0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/Object;)Ljava/lang/StringBuffer;

    add-int/lit8 v0, v0, 0x1

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_1

    const-string v1, ", "

    invoke-virtual {p0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_0

    :cond_2
    const-string p1, ")"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {p0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getASTCreateString(Ljava/lang/String;)Ljava/lang/String;
    .locals 7

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

    const-string v6, "self.astFactory.create("

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

    const-string v0, ", \"\""

    :cond_4
    if-eqz v1, :cond_5

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ", "

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_5
    iget-object p0, p0, Lantlr/PythonCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v0, "AST"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_6

    invoke-static {v6, p1, v4}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_6
    invoke-static {v6, p1, v4}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_7
    invoke-static {v6, p1, v4}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

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

    const-string p0, "True"

    return-object p0

    :cond_1
    iget-object p1, p1, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    invoke-virtual {p0, p1, v0}, Lantlr/PythonCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

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

    const-string v2, ") and ("

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_0
    const/4 v2, 0x0

    aget-object v4, p1, v1

    invoke-virtual {v4}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v4

    if-eqz v4, :cond_1

    const-string v3, "True"

    goto :goto_1

    :cond_1
    invoke-virtual {p0, v1, v3}, Lantlr/PythonCodeGenerator;->getLookaheadTestTerm(ILantlr/collections/impl/BitSet;)Ljava/lang/String;

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

    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object v1

    invoke-static {v1}, Lantlr/CodeGenerator;->elementsAreRange([I)Z

    move-result v2

    if-eqz v2, :cond_0

    invoke-virtual {p0, p1, v1}, Lantlr/PythonCodeGenerator;->getRangeExpression(I[I)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-virtual {p2}, Lantlr/collections/impl/BitSet;->degree()I

    move-result p1

    if-nez p1, :cond_1

    const-string p0, "True"

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

    const/4 v3, 0x1

    invoke-direct {p0, v2, v3}, Lantlr/PythonCodeGenerator;->getValueString(IZ)Ljava/lang/String;

    move-result-object v2

    if-lez p2, :cond_3

    const-string v3, " or "

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

    invoke-virtual {p1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getRangeExpression(I[I)Ljava/lang/String;
    .locals 4

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

    const/4 v2, 0x1

    sub-int/2addr v1, v2

    aget p2, p2, v1

    const-string v1, "("

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " >= "

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, v0, v2}, Lantlr/PythonCodeGenerator;->getValueString(IZ)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " and "

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, p1}, Lantlr/PythonCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " <= "

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, p2, v2}, Lantlr/PythonCodeGenerator;->getValueString(IZ)Ljava/lang/String;

    move-result-object p0

    const-string p1, ")"

    invoke-static {v1, p0, p1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public isspace(C)Z
    .locals 0

    const/16 p0, 0x9

    if-eq p1, p0, :cond_0

    const/16 p0, 0xa

    if-eq p1, p0, :cond_0

    const/16 p0, 0xd

    if-eq p1, p0, :cond_0

    const/16 p0, 0x20

    if-eq p1, p0, :cond_0

    const/4 p0, 0x0

    goto :goto_0

    :cond_0
    const/4 p0, 0x1

    :goto_0
    return p0
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

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

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
    iget-object v0, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    const-string v1, "_AST"

    if-ge v4, v0, :cond_5

    iget-object v0, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

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
    iget-object v0, p0, Lantlr/PythonCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {v0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    if-eqz v0, :cond_9

    sget-object p2, Lantlr/PythonCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    const/4 v1, 0x0

    const-string v4, " in rule "

    const-string v5, "Ambiguous reference to AST element "

    if-ne v0, p2, :cond_6

    :goto_2
    iget-object p2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v5, p1, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object p0, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-object v1

    :cond_6
    iget-object p2, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

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
    iget-object p0, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

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

.method public od(Ljava/lang/String;IILjava/lang/String;)V
    .locals 2

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-virtual {p0, p4}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :goto_0
    if-gt p2, p3, :cond_3

    invoke-virtual {p1, p2}, Ljava/lang/String;->charAt(I)C

    move-result p0

    const/16 p4, 0x9

    if-eq p0, p4, :cond_2

    const/16 p4, 0xa

    if-eq p0, p4, :cond_1

    const/16 p4, 0x20

    if-eq p0, p4, :cond_0

    sget-object p4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, " "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p4, p0}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    goto :goto_2

    :cond_0
    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string p4, " sp "

    goto :goto_1

    :cond_1
    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string p4, " nl "

    goto :goto_1

    :cond_2
    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string p4, " ht "

    :goto_1
    invoke-virtual {p0, p4}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    :goto_2
    add-int/lit8 p2, p2, 0x1

    goto :goto_0

    :cond_3
    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string p1, ""

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public printAction(Ljava/lang/String;)V
    .locals 0

    if-eqz p1, :cond_0

    invoke-virtual {p0}, Lantlr/PythonCodeGenerator;->printTabs()V

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->_printAction(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public printActionCode(Ljava/lang/String;I)V
    .locals 0

    invoke-virtual {p0, p1, p2}, Lantlr/PythonCodeGenerator;->processActionCode(Ljava/lang/String;I)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->printAction(Ljava/lang/String;)V

    return-void
.end method

.method public printGrammarAction(Lantlr/Grammar;)V
    .locals 3

    const-string v0, "### user action >>>"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    iget-object p1, p1, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p1

    iget-object v1, p0, Lantlr/PythonCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v2, 0x0

    invoke-virtual {p0, v0, p1, v1, v2}, Lantlr/PythonCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/PythonCodeGenerator;->printAction(Ljava/lang/String;)V

    const-string p1, "### user action <<<"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public printMainFunc(Ljava/lang/String;)V
    .locals 3

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x0

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "if __name__ == \'__main__\':"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1, v1}, Lantlr/PythonCodeGenerator;->printActionCode(Ljava/lang/String;I)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    return-void
.end method

.method public printTabs()V
    .locals 3

    const/4 v0, 0x0

    :goto_0
    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    if-ge v0, v1, :cond_0

    iget-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const-string v2, "    "

    invoke-virtual {v1, v2}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public processActionCode(Ljava/lang/String;I)Ljava/lang/String;
    .locals 4

    const-string v0, "Error reading action:"

    if-eqz p1, :cond_1

    invoke-static {p1}, Lantlr/PythonCodeGenerator;->isEmpty(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_2

    :cond_0
    new-instance v1, Lantlr/actions/python/CodeLexer;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-direct {v1, p1, v2, p2, v3}, Lantlr/actions/python/CodeLexer;-><init>(Ljava/lang/String;Ljava/lang/String;ILantlr/Tool;)V

    const/4 p2, 0x1

    :try_start_0
    invoke-virtual {v1, p2}, Lantlr/actions/python/CodeLexer;->mACTION(Z)V

    invoke-virtual {v1}, Lantlr/CharScanner;->getTokenObject()Lantlr/Token;

    move-result-object p2

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_2
    .catch Lantlr/TokenStreamException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Lantlr/CharStreamException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    goto :goto_0

    :catch_1
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    :goto_0
    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto :goto_1

    :catch_2
    move-exception p0

    invoke-virtual {v1, p0}, Lantlr/actions/python/CodeLexer;->reportError(Lantlr/RecognitionException;)V

    :goto_1
    return-object p1

    :cond_1
    :goto_2
    const-string p0, ""

    return-object p0
.end method

.method public processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;
    .locals 2

    const-string v0, "Error reading action:"

    if-eqz p1, :cond_3

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v1

    if-nez v1, :cond_0

    goto :goto_2

    :cond_0
    invoke-static {p1}, Lantlr/PythonCodeGenerator;->isEmpty(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_1

    const-string p0, ""

    return-object p0

    :cond_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    if-nez v1, :cond_2

    return-object p1

    :cond_2
    new-instance v1, Lantlr/actions/python/ActionLexer;

    invoke-direct {v1, p1, p3, p0, p4}, Lantlr/actions/python/ActionLexer;-><init>(Ljava/lang/String;Lantlr/RuleBlock;Lantlr/CodeGenerator;Lantlr/ActionTransInfo;)V

    invoke-virtual {v1, p2}, Lantlr/actions/python/ActionLexer;->setLineOffset(I)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v1, p2}, Lantlr/CharScanner;->setFilename(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1, p2}, Lantlr/actions/python/ActionLexer;->setTool(Lantlr/Tool;)V

    const/4 p2, 0x1

    :try_start_0
    invoke-virtual {v1, p2}, Lantlr/actions/python/ActionLexer;->mACTION(Z)V

    invoke-virtual {v1}, Lantlr/CharScanner;->getTokenObject()Lantlr/Token;

    move-result-object p2

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_2
    .catch Lantlr/TokenStreamException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Lantlr/CharStreamException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    goto :goto_0

    :catch_1
    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    :goto_0
    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto :goto_1

    :catch_2
    move-exception p0

    invoke-virtual {v1, p0}, Lantlr/actions/python/ActionLexer;->reportError(Lantlr/RecognitionException;)V

    :goto_1
    return-object p1

    :cond_3
    :goto_2
    const/4 p0, 0x0

    return-object p0
.end method

.method public setupOutput(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ".py"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public toString(Z)Ljava/lang/String;
    .locals 0

    if-eqz p1, :cond_0

    const-string p0, "True"

    goto :goto_0

    :cond_0
    const-string p0, "False"

    :goto_0
    return-object p0
.end method
