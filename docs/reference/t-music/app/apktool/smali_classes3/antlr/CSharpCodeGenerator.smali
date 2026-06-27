.class public Lantlr/CSharpCodeGenerator;
.super Lantlr/CodeGenerator;
.source ""


# static fields
.field public static final NONUNIQUE:Ljava/lang/String;

.field public static final caseSizeThreshold:I = 0x7f

.field public static nameSpace:Lantlr/CSharpNameSpace;


# instance fields
.field public astTypes:Ljava/util/Vector;

.field public astVarNumber:I

.field public blockNestingLevel:I

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

.field public lt1Value:Ljava/lang/String;

.field public saveIndexCreateLevel:I

.field public saveText:Z

.field public semPreds:Lantlr/collections/impl/Vector;

.field public syntacticPredLevel:I

.field public throwNoViable:Ljava/lang/String;

.field public treeVariableMap:Ljava/util/Hashtable;

.field public usingCustomAST:Z


# direct methods
.method public static constructor <clinit>()V
    .locals 1

    new-instance v0, Ljava/lang/String;

    invoke-direct {v0}, Ljava/lang/String;-><init>()V

    sput-object v0, Lantlr/CSharpCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    const/4 v0, 0x0

    sput-object v0, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    return-void
.end method

.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lantlr/CodeGenerator;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    iput-boolean v0, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    iput-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    iput-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/CSharpCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    const/4 v0, 0x1

    iput v0, p0, Lantlr/CSharpCodeGenerator;->astVarNumber:I

    new-instance v0, Lantlr/CSharpCharFormatter;

    invoke-direct {v0}, Lantlr/CSharpCharFormatter;-><init>()V

    iput-object v0, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    return-void
.end method

.method private GenRuleInvocation(Lantlr/RuleRefElement;)V
    .locals 7

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    const-string v1, ","

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    const-string v0, "true"

    goto :goto_0

    :cond_0
    const-string v0, "false"

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    if-nez v0, :cond_1

    iget-object v0, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v0, :cond_2

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_2
    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->commonExtraArgs:Ljava/lang/String;

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

    iget-object v3, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v4, 0x0

    invoke-virtual {p0, v2, v4, v3, v1}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

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

    iget-object v4, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

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
    const-string p1, ");"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_8

    const-string p1, "_t = retTree_;"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    return-void
.end method

.method public static OctalToUnicode(Ljava/lang/String;)Ljava/lang/String;
    .locals 5

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v0

    const/4 v1, 0x4

    if-gt v1, v0, :cond_0

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v0

    const/16 v1, 0x27

    if-ne v1, v0, :cond_0

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v2

    const/16 v3, 0x5c

    if-ne v3, v2, :cond_0

    const/4 v2, 0x2

    invoke-virtual {p0, v2}, Ljava/lang/String;->charAt(I)C

    move-result v3

    const/16 v4, 0x30

    if-gt v4, v3, :cond_0

    invoke-virtual {p0, v2}, Ljava/lang/String;->charAt(I)C

    move-result v3

    const/16 v4, 0x37

    if-lt v4, v3, :cond_0

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-virtual {p0, v3}, Ljava/lang/String;->charAt(I)C

    move-result v3

    if-ne v1, v3, :cond_0

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v1

    sub-int/2addr v1, v0

    invoke-virtual {p0, v2, v1}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p0

    const/16 v0, 0x8

    invoke-static {p0, v0}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;I)Ljava/lang/Integer;

    move-result-object p0

    const-string v0, "\'\\x"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    invoke-static {p0}, Ljava/lang/Integer;->toHexString(I)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "\'"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    :cond_0
    return-object p0
.end method

.method private declareSaveIndexVariableIfNeeded()V
    .locals 1

    iget v0, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    if-nez v0, :cond_0

    const-string v0, "int _saveIndex = 0;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iput v0, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_0
    return-void
.end method

.method private genBitSet(Lantlr/collections/impl/BitSet;I)V
    .locals 6

    const-string v0, "private static long[] mk_"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0, p2}, Lantlr/CSharpCodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "()"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "{"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->lengthInLongWords()I

    move-result v0

    const/16 v1, 0x8

    if-ge v0, v1, :cond_0

    const-string v0, "long[] data = { "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toStringOfWords()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "};"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto/16 :goto_4

    :cond_0
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "long[] data = new long["

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, "];"

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toPackedArray()[J

    move-result-object p1

    const/4 v0, 0x0

    :goto_0
    array-length v1, p1

    if-ge v0, v1, :cond_4

    add-int/lit8 v1, v0, 0x1

    array-length v2, p1

    if-eq v1, v2, :cond_3

    aget-wide v2, p1, v0

    aget-wide v4, p1, v1

    cmp-long v2, v2, v4

    if-eqz v2, :cond_1

    goto :goto_2

    :cond_1
    :goto_1
    array-length v2, p1

    if-ge v1, v2, :cond_2

    aget-wide v2, p1, v1

    aget-wide v4, p1, v0

    cmp-long v2, v2, v4

    if-nez v2, :cond_2

    add-int/lit8 v1, v1, 0x1

    goto :goto_1

    :cond_2
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "for (int i = "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, "; i<="

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    add-int/lit8 v3, v1, -0x1

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, "; i++) { data[i]="

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget-wide v3, p1, v0

    invoke-virtual {v2, v3, v4}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    const-string v0, "L; }"

    goto :goto_3

    :cond_3
    :goto_2
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "data["

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, "]="

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget-wide v3, p1, v0

    invoke-virtual {v2, v3, v4}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    const-string v0, "L;"

    :goto_3
    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    move v0, v1

    goto :goto_0

    :cond_4
    :goto_4
    const-string p1, "return data;"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "public static readonly BitSet "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Lantlr/CSharpCodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " = new BitSet(mk_"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Lantlr/CSharpCodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "());"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method private genBlockFinish(Lantlr/CSharpBlockFinishingInfo;Ljava/lang/String;)V
    .locals 2

    iget-boolean v0, p1, Lantlr/CSharpBlockFinishingInfo;->needAnErrorClause:Z

    if-eqz v0, :cond_2

    iget-boolean v0, p1, Lantlr/CSharpBlockFinishingInfo;->generatedAnIf:Z

    if-nez v0, :cond_0

    iget-boolean v0, p1, Lantlr/CSharpBlockFinishingInfo;->generatedSwitch:Z

    if-eqz v0, :cond_2

    :cond_0
    iget-boolean v0, p1, Lantlr/CSharpBlockFinishingInfo;->generatedAnIf:Z

    const-string v1, "{"

    if-eqz v0, :cond_1

    const-string v0, "else"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "}"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    iget-object v0, p1, Lantlr/CSharpBlockFinishingInfo;->postscript:Ljava/lang/String;

    if-eqz v0, :cond_5

    iget-boolean v0, p1, Lantlr/CSharpBlockFinishingInfo;->needAnErrorClause:Z

    if-eqz v0, :cond_4

    iget-boolean v0, p1, Lantlr/CSharpBlockFinishingInfo;->generatedSwitch:Z

    if-eqz v0, :cond_4

    iget-boolean v0, p1, Lantlr/CSharpBlockFinishingInfo;->generatedAnIf:Z

    if-nez v0, :cond_4

    if-eqz p2, :cond_4

    const-string v0, "throw"

    invoke-virtual {p2, v0}, Ljava/lang/String;->indexOf(Ljava/lang/String;)I

    move-result v0

    if-eqz v0, :cond_3

    const-string v0, "goto"

    invoke-virtual {p2, v0}, Ljava/lang/String;->indexOf(Ljava/lang/String;)I

    move-result p2

    if-nez p2, :cond_4

    :cond_3
    iget-object p2, p1, Lantlr/CSharpBlockFinishingInfo;->postscript:Ljava/lang/String;

    const-string v0, "break;"

    invoke-virtual {p2, v0}, Ljava/lang/String;->indexOf(Ljava/lang/String;)I

    move-result p2

    add-int/lit8 p2, p2, 0x6

    iget-object p1, p1, Lantlr/CSharpBlockFinishingInfo;->postscript:Ljava/lang/String;

    invoke-virtual {p1, p2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object p1

    goto :goto_0

    :cond_4
    iget-object p1, p1, Lantlr/CSharpBlockFinishingInfo;->postscript:Ljava/lang/String;

    :goto_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    return-void
.end method

.method private genElementAST(Lantlr/AlternativeElement;)V
    .locals 10

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    const-string v2, "_in = "

    const-string v3, " "

    const-string v4, "_AST"

    const-string v5, "tmp"

    const/4 v6, 0x1

    const-string v7, ";"

    if-eqz v1, :cond_1

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-nez v0, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v5, p0, Lantlr/CSharpCodeGenerator;->astVarNumber:I

    invoke-virtual {v1, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    iget v4, p0, Lantlr/CSharpCodeGenerator;->astVarNumber:I

    add-int/2addr v4, v6

    iput v4, p0, Lantlr/CSharpCodeGenerator;->astVarNumber:I

    invoke-direct {p0, p1, v1}, Lantlr/CSharpCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v4, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {p1, v0, v7, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_0
    return-void

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_13

    iget v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_13

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    const/4 v1, 0x3

    if-eqz v0, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-eq v0, v1, :cond_3

    :cond_2
    move v0, v6

    goto :goto_0

    :cond_3
    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v8

    if-eq v8, v1, :cond_4

    instance-of v1, p1, Lantlr/TokenRefElement;

    if-eqz v1, :cond_4

    move v0, v6

    :cond_4
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_5

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v5

    goto :goto_1

    :cond_5
    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget v8, p0, Lantlr/CSharpCodeGenerator;->astVarNumber:I

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    iget v8, p0, Lantlr/CSharpCodeGenerator;->astVarNumber:I

    add-int/2addr v8, v6

    iput v8, p0, Lantlr/CSharpCodeGenerator;->astVarNumber:I

    :goto_1
    if-eqz v0, :cond_7

    instance-of v8, p1, Lantlr/GrammarAtom;

    if-eqz v8, :cond_6

    move-object v8, p1

    check-cast v8, Lantlr/GrammarAtom;

    invoke-virtual {v8}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v9

    if-eqz v9, :cond_6

    invoke-virtual {v8}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v8

    goto :goto_2

    :cond_6
    iget-object v8, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    :goto_2
    invoke-virtual {p0, p1, v5, v8}, Lantlr/CSharpCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V

    :cond_7
    invoke-static {v5, v4}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    invoke-direct {p0, p1, v4}, Lantlr/CSharpCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/TreeWalkerGrammar;

    if-eqz v5, :cond_8

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v8, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "_in = null;"

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    const-string v5, " = "

    if-eqz v3, :cond_a

    instance-of v3, p1, Lantlr/GrammarAtom;

    if-eqz v3, :cond_9

    invoke-static {v4, v5}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    move-object v8, p1

    check-cast v8, Lantlr/GrammarAtom;

    invoke-virtual {p0, v8, v1}, Lantlr/CSharpCodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    goto :goto_3

    :cond_9
    invoke-static {v4, v5}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {p0, v1}, Lantlr/CSharpCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :goto_3
    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_c

    if-eqz v0, :cond_c

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    instance-of v1, p1, Lantlr/GrammarAtom;

    if-eqz v1, :cond_b

    invoke-static {v4, v5}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    move-object v3, p1

    check-cast v3, Lantlr/GrammarAtom;

    invoke-virtual {p0, v3, v0}, Lantlr/CSharpCodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    goto :goto_4

    :cond_b
    invoke-static {v4, v5}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0, v0}, Lantlr/CSharpCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    :goto_4
    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_c

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    if-eqz v0, :cond_13

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    const-string v1, ");"

    if-eq v0, v6, :cond_10

    const/4 v2, 0x2

    if-eq v0, v2, :cond_d

    goto :goto_8

    :cond_d
    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-nez v0, :cond_f

    instance-of v0, p1, Lantlr/GrammarAtom;

    if-eqz v0, :cond_e

    check-cast p1, Lantlr/GrammarAtom;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_e

    goto :goto_5

    :cond_e
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "astFactory.makeASTRoot(ref currentAST, "

    goto :goto_7

    :cond_f
    :goto_5
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "astFactory.makeASTRoot(ref currentAST, (AST)"

    goto :goto_7

    :cond_10
    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-nez v0, :cond_12

    instance-of v0, p1, Lantlr/GrammarAtom;

    if-eqz v0, :cond_11

    check-cast p1, Lantlr/GrammarAtom;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_11

    goto :goto_6

    :cond_11
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "astFactory.addASTChild(ref currentAST, "

    goto :goto_7

    :cond_12
    :goto_6
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "astFactory.addASTChild(ref currentAST, (AST)"

    :goto_7
    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_13
    :goto_8
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

    const-string v0, "}"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    :cond_3
    return-void
.end method

.method private genErrorHandler(Lantlr/ExceptionSpec;)V
    .locals 6

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

    const-string v2, "catch ("

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v3, v1, Lantlr/ExceptionHandler;->exceptionTypeAndName:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ")"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "{"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v3, :cond_0

    const-string v3, "if (0 == inputState.guessing)"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_0
    new-instance v3, Lantlr/ActionTransInfo;

    invoke-direct {v3}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v4, v1, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    iget-object v1, v1, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v1}, Lantlr/Token;->getLine()I

    move-result v1

    iget-object v5, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v4, v1, v5, v3}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    const-string v3, "}"

    if-eqz v1, :cond_1

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "else"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "throw;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto/16 :goto_0

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

    move-result-object p1

    invoke-virtual {v0, p1}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object p1

    if-eqz p1, :cond_3

    const-string p1, "try   // for error handling"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "{"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_3
    return-void
.end method

.method private genLiteralsTest()V
    .locals 1

    const-string v0, "_ttype = testLiteralsTable(_ttype);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method private genLiteralsTestForPartialToken()V
    .locals 1

    const-string v0, "_ttype = testLiteralsTable(text.ToString(_begin, text.Length-_begin), _ttype);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

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
    invoke-direct {p0, v1}, Lantlr/CSharpCodeGenerator;->mangleLiteral(Ljava/lang/String;)Ljava/lang/String;

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

    instance-of v0, p0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_0

    const-string p0, "_t.Type"

    return-object p0

    :cond_0
    instance-of p0, p0, Lantlr/LexerGrammar;

    if-eqz p0, :cond_2

    const/4 p0, 0x1

    if-ne p1, p0, :cond_1

    const-string p0, "cached_LA1"

    return-object p0

    :cond_1
    const/4 p0, 0x2

    if-ne p1, p0, :cond_2

    const-string p0, "cached_LA2"

    return-object p0

    :cond_2
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

    invoke-direct {p0, p1, p2}, Lantlr/CSharpCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

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

    iget-object p1, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p1, v0}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p1

    if-eqz p1, :cond_3

    iget-object p1, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p1, v0}, Ljava/util/Hashtable;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    sget-object p1, Lantlr/CSharpCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    invoke-virtual {p0, v0, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    goto :goto_1

    :cond_3
    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p0, v0, p2}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_4
    :goto_1
    return-void
.end method

.method private setupGrammarParameters(Lantlr/Grammar;)V
    .locals 8

    instance-of v0, p1, Lantlr/ParserGrammar;

    if-nez v0, :cond_0

    instance-of v1, p1, Lantlr/LexerGrammar;

    if-nez v1, :cond_0

    instance-of v1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_2

    :cond_0
    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, v1, Lantlr/Tool;->nameSpace:Lantlr/NameSpace;

    if-eqz v1, :cond_1

    new-instance v2, Lantlr/CSharpNameSpace;

    invoke-virtual {v1}, Lantlr/NameSpace;->getName()Ljava/lang/String;

    move-result-object v1

    invoke-direct {v2, v1}, Lantlr/CSharpNameSpace;-><init>(Ljava/lang/String;)V

    sput-object v2, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    :cond_1
    const-string v1, "namespace"

    invoke-virtual {p1, v1}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v2

    if-eqz v2, :cond_2

    invoke-virtual {p1, v1}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v1

    if-eqz v1, :cond_2

    new-instance v2, Lantlr/CSharpNameSpace;

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-direct {v2, v1}, Lantlr/CSharpNameSpace;-><init>(Ljava/lang/String;)V

    sput-object v2, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    :cond_2
    const/4 v1, 0x1

    const-string v2, "null"

    const-string v3, "RecognitionException"

    const-string v4, "\""

    const-string v5, "AST"

    const-string v6, ""

    const-string v7, "ASTLabelType"

    if-eqz v0, :cond_4

    iput-object v5, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p1, v7}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_3

    invoke-virtual {p1, v7}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object p1

    if-eqz p1, :cond_3

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1, v4, v4}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_3

    iput-boolean v1, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    :cond_3
    const-string p1, "IToken "

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->labeledElementType:Ljava/lang/String;

    iput-object v2, p0, Lantlr/CSharpCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CSharpCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CSharpCodeGenerator;->commonExtraParams:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CSharpCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string p1, "LT(1)"

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v3, p0, Lantlr/CSharpCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string p1, "throw new NoViableAltException(LT(1), getFilename());"

    goto :goto_0

    :cond_4
    instance-of v0, p1, Lantlr/LexerGrammar;

    if-eqz v0, :cond_5

    const-string p1, "char "

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->labeledElementType:Ljava/lang/String;

    const-string p1, "\'\\0\'"

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CSharpCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    const-string p1, "bool _createToken"

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->commonExtraParams:Ljava/lang/String;

    const-string p1, "int _ttype; IToken _token=null; int _begin=text.Length;"

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string p1, "cached_LA1"

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v3, p0, Lantlr/CSharpCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string p1, "throw new NoViableAltForCharException(cached_LA1, getFilename(), getLine(), getColumn());"

    :goto_0
    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->throwNoViable:Ljava/lang/String;

    goto :goto_1

    :cond_5
    instance-of v0, p1, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_9

    iput-object v5, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    iput-object v5, p0, Lantlr/CSharpCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {p1, v7}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_6

    invoke-virtual {p1, v7}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v0

    if-eqz v0, :cond_6

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0, v4, v4}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_6

    iput-boolean v1, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    iput-object v0, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    iput-object v0, p0, Lantlr/CSharpCodeGenerator;->labeledElementType:Ljava/lang/String;

    :cond_6
    invoke-virtual {p1, v7}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-nez v0, :cond_7

    new-instance v0, Lantlr/Token;

    const/4 v1, 0x6

    invoke-direct {v0, v1, v5}, Lantlr/Token;-><init>(ILjava/lang/String;)V

    invoke-virtual {p1, v7, v0}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    :cond_7
    iput-object v2, p0, Lantlr/CSharpCodeGenerator;->labeledElementInit:Ljava/lang/String;

    const-string p1, "_t"

    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    const-string v0, "AST _t"

    iput-object v0, p0, Lantlr/CSharpCodeGenerator;->commonExtraParams:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CSharpCodeGenerator;->commonLocalVars:Ljava/lang/String;

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_8

    const-string p1, "(_t==ASTNULL) ? null : ("

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v1, ")_t"

    invoke-static {p1, v0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    :cond_8
    iput-object p1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    iput-object v3, p0, Lantlr/CSharpCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string p1, "throw new NoViableAltException(_t);"

    goto :goto_0

    :cond_9
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
.method public addSemPred(Ljava/lang/String;)I
    .locals 1

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {v0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

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

    invoke-direct {p0, v1}, Lantlr/CSharpCodeGenerator;->setupGrammarParameters(Lantlr/Grammar;)V

    invoke-virtual {v1}, Lantlr/Grammar;->generate()V

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->exitIfError()V

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

    invoke-virtual {p0, v1}, Lantlr/CSharpCodeGenerator;->genTokenTypes(Lantlr/TokenManager;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->genTokenInterchange(Lantlr/TokenManager;)V

    :cond_1
    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->exitIfError()V
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

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "genAction("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-boolean v0, p1, Lantlr/ActionElement;->isSemPred:Z

    if-eqz v0, :cond_1

    iget-object v0, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    iget p1, p1, Lantlr/GrammarElement;->line:I

    invoke-virtual {p0, v0, p1}, Lantlr/CSharpCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    goto/16 :goto_0

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v0, :cond_2

    const-string v0, "if (0==inputState.guessing)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "{"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    new-instance v0, Lantlr/ActionTransInfo;

    invoke-direct {v0}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v1, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    iget-object v2, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v1, p1, v2, v0}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    iget-object v1, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    if-eqz v1, :cond_3

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " = ("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v3, ")currentAST.root;"

    invoke-static {v1, v2, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_3
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    iget-boolean p1, v0, Lantlr/ActionTransInfo;->assignToRoot:Z

    if-eqz p1, :cond_4

    const-string p1, "currentAST.root = "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v1, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ";"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "if ( (null != "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ") && (null != "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    const-string v3, ".getFirstChild()) )"

    invoke-static {p1, v2, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "currentAST.child = "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v3, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    const-string v4, ".getFirstChild();"

    invoke-static {v2, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, -0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "else"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-static {p1, v0, v1, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "currentAST.advanceChildToEnd();"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz p1, :cond_5

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
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
    const-string v0, "{"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v2

    iput-object v2, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_1
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v2, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    invoke-virtual {p0, p1, v1}, Lantlr/CSharpCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CSharpBlockFinishingInfo;

    move-result-object p1

    iget-object v2, p0, Lantlr/CSharpCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-direct {p0, p1, v2}, Lantlr/CSharpCodeGenerator;->genBlockFinish(Lantlr/CSharpBlockFinishingInfo;Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput-object v0, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

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

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_1
    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v1, :cond_2

    goto :goto_0

    :cond_2
    const/4 v1, 0x0

    :goto_0
    iput-boolean v1, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    iput-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    return-void
.end method

.method public gen(Lantlr/CharRangeElement;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/CharRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/CharRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_2

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

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

    const-string v1, "_saveIndex = text.Length;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    const-string v1, "matchRange("

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-static {v2}, Lantlr/CSharpCodeGenerator;->OctalToUnicode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p1, p1, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-static {p1}, Lantlr/CSharpCodeGenerator;->OctalToUnicode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ");"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    if-eqz v0, :cond_4

    const-string p1, "text.Length = _saveIndex;"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    return-void
.end method

.method public gen(Lantlr/LexerGrammar;)V
    .locals 2

    iget-boolean v0, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v0, :cond_0

    new-instance v0, Lantlr/collections/impl/Vector;

    invoke-direct {v0}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v0, p0, Lantlr/CSharpCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-nez v0, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Internal error generating lexer"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBody(Lantlr/LexerGrammar;)V

    return-void
.end method

.method public gen(Lantlr/OneOrMoreBlock;)V
    .locals 9

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "gen+("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const-string v0, "{ // ( ... )+"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_1

    const-string v0, "_cnt_"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :cond_1
    const-string v0, "_cnt"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    :goto_0
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "int "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "=0;"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v2

    goto :goto_1

    :cond_2
    const-string v2, "_loop"

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget v3, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    :goto_1
    const-string v3, "for (;;)"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "{"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iget v3, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/2addr v3, v1

    iput v3, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v3, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    iput-object v4, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_3
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v4, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/OneOrMoreBlock;)Z

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v4, v4, Lantlr/Grammar;->maxk:I

    iget-boolean v5, p1, Lantlr/AlternativeBlock;->greedy:Z

    const/4 v6, 0x0

    if-nez v5, :cond_4

    iget v5, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    if-gt v5, v4, :cond_4

    iget-object v7, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aget-object v5, v7, v5

    invoke-virtual {v5}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v5

    if-eqz v5, :cond_4

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    goto :goto_2

    :cond_4
    iget-boolean v5, p1, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v5, :cond_5

    iget v5, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    const v7, 0x7fffffff

    if-ne v5, v7, :cond_5

    :goto_2
    move v5, v4

    move v4, v1

    goto :goto_3

    :cond_5
    move v5, v4

    move v4, v6

    :goto_3
    if-eqz v4, :cond_7

    iget-boolean v4, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v4, :cond_6

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v7, "nongreedy (...)+ loop; exit depth is "

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget v8, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v4, v7}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_6
    iget-object v4, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    invoke-virtual {p0, v4, v5}, Lantlr/CSharpCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object v4

    const-string v5, "// nongreedy exit test"

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "if (("

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, " >= 1) && "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ") goto "

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "_breakloop;"

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    invoke-virtual {p0, p1, v6}, Lantlr/CSharpCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CSharpBlockFinishingInfo;

    move-result-object p1

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "if ("

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " >= 1) { goto "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "_breakloop; } else { "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/CSharpCodeGenerator;->throwNoViable:Ljava/lang/String;

    const-string v7, "; }"

    invoke-static {v4, v5, v7}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    invoke-direct {p0, p1, v4}, Lantlr/CSharpCodeGenerator;->genBlockFinish(Lantlr/CSharpBlockFinishingInfo;Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "++;"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget p1, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v0, p1, -0x1

    iput v0, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget v0, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    if-ne p1, v0, :cond_8

    iput v6, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_8
    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "_breakloop:"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string p1, ";"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget p1, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v0, p1, -0x1

    iput v0, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget v0, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    if-ne p1, v0, :cond_9

    iput v6, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_9
    const-string p1, "}    // ( ... )+"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput-object v3, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    return-void
.end method

.method public gen(Lantlr/ParserGrammar;)V
    .locals 2

    iget-boolean v0, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v0, :cond_0

    new-instance v0, Lantlr/collections/impl/Vector;

    invoke-direct {v0}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v0, p0, Lantlr/CSharpCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/ParserGrammar;

    if-nez v0, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Internal error generating parser"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBody(Lantlr/ParserGrammar;)V

    return-void
.end method

.method public gen(Lantlr/RuleRefElement;)V
    .locals 6

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "genRR("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    const-string v1, "Rule \'"

    if-eqz v0, :cond_15

    invoke-virtual {v0}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v2

    if-nez v2, :cond_1

    goto/16 :goto_4

    :cond_1
    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_2

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_2

    iget v2, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v2, :cond_2

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " = _t==ASTNULL ? null : "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v4, ";"

    invoke-static {v2, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_2
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    const/4 v3, 0x3

    if-eqz v2, :cond_4

    iget-boolean v2, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz v2, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v3, :cond_4

    :cond_3
    invoke-direct {p0}, Lantlr/CSharpCodeGenerator;->declareSaveIndexVariableIfNeeded()V

    const-string v2, "_saveIndex = text.Length;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    invoke-virtual {p0}, Lantlr/CodeGenerator;->printTabs()V

    iget-object v2, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    if-eqz v2, :cond_6

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-nez v0, :cond_5

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

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
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    if-nez v2, :cond_7

    iget v2, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v2, :cond_7

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v0, :cond_7

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

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
    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->GenRuleInvocation(Lantlr/RuleRefElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_9

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz v0, :cond_8

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-ne v0, v3, :cond_9

    :cond_8
    invoke-direct {p0}, Lantlr/CSharpCodeGenerator;->declareSaveIndexVariableIfNeeded()V

    const-string v0, "text.Length = _saveIndex;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iget v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_14

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v0, Lantlr/Grammar;->hasSyntacticPredicate:Z

    const/4 v2, 0x1

    if-eqz v1, :cond_c

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_a

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_b

    :cond_a
    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    if-eqz v0, :cond_c

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-ne v0, v2, :cond_c

    :cond_b
    move v0, v2

    goto :goto_1

    :cond_c
    const/4 v0, 0x0

    :goto_1
    if-eqz v0, :cond_d

    const-string v1, "if (0 == inputState.guessing)"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "{"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_d
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-eqz v1, :cond_e

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_e

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "_AST = ("

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v4, ")returnAST;"

    invoke-static {v1, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_e
    iget-boolean v1, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    if-eqz v1, :cond_12

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-eq v1, v2, :cond_10

    const/4 v3, 0x2

    if-eq v1, v3, :cond_f

    goto :goto_3

    :cond_f
    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v3, "Internal: encountered ^ after rule reference"

    invoke-virtual {v1, v3}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_3

    :cond_10
    iget-boolean v1, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v1, :cond_11

    const-string v1, "astFactory.addASTChild(ref currentAST, (AST)returnAST);"

    goto :goto_2

    :cond_11
    const-string v1, "astFactory.addASTChild(ref currentAST, returnAST);"

    :goto_2
    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_12
    :goto_3
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_13

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_13

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " = returnToken_;"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_13
    if-eqz v0, :cond_14

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v2

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "}"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_14
    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    return-void

    :cond_15
    :goto_4
    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

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

    iget v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_1
    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v1, :cond_2

    goto :goto_0

    :cond_2
    const/4 v1, 0x0

    :goto_0
    iput-boolean v1, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    iput-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_3

    const-string p1, "_t = _t.getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method public gen(Lantlr/TokenRangeElement;)V
    .locals 3

    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    invoke-virtual {p1}, Lantlr/TokenRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/TokenRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_0
    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    const-string v0, "matchRange("

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p1, Lantlr/TokenRangeElement;->beginText:Ljava/lang/String;

    invoke-static {v1}, Lantlr/CSharpCodeGenerator;->OctalToUnicode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p1, Lantlr/TokenRangeElement;->endText:Ljava/lang/String;

    invoke-static {v1}, Lantlr/CSharpCodeGenerator;->OctalToUnicode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ");"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

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
    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_2

    iget v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_2
    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_3

    const-string p1, "_t = _t.getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method public gen(Lantlr/TreeElement;)V
    .locals 6

    const-string v0, "AST __t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, " = _t;"

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

    const-string v1, " = (ASTNULL == _t) ? null : ("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v2, ")_t;"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

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

    invoke-direct {p0, v0}, Lantlr/CSharpCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_3

    const-string v0, "ASTPair __currentAST"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, " = currentAST.copy();"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "currentAST.root = currentAST.child;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "currentAST.child = null;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    instance-of v1, v0, Lantlr/WildcardElement;

    if-eqz v1, :cond_4

    const-string v0, "if (null == _t) throw new MismatchedTokenException();"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v0}, Lantlr/CSharpCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    :goto_0
    const-string v0, "_t = _t.getFirstChild();"

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

    const-string v1, ";"

    if-eqz v0, :cond_7

    const-string v0, "currentAST = __currentAST"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    const-string v0, "_t = __t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget p1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "_t = _t.getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public gen(Lantlr/TreeWalkerGrammar;)V
    .locals 2

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Internal error generating tree-walker"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBody(Lantlr/TreeWalkerGrammar;)V

    return-void
.end method

.method public gen(Lantlr/WildcardElement;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_0
    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_1

    const-string p1, "if (null == _t) throw new MismatchedTokenException();"

    :goto_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_5

    const/4 v1, 0x3

    if-eqz v0, :cond_3

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-ne v0, v1, :cond_3

    :cond_2
    invoke-direct {p0}, Lantlr/CSharpCodeGenerator;->declareSaveIndexVariableIfNeeded()V

    const-string v0, "_saveIndex = text.Length;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    const-string v0, "matchNot(EOF/*_CHAR*/);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_6

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz v0, :cond_4

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v1, :cond_6

    :cond_4
    invoke-direct {p0}, Lantlr/CSharpCodeGenerator;->declareSaveIndexVariableIfNeeded()V

    const-string p1, "text.Length = _saveIndex;"

    goto :goto_0

    :cond_5
    const-string p1, "matchNot("

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    const/4 v0, 0x1

    invoke-direct {p0, v0}, Lantlr/CSharpCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v0

    const-string v1, ");"

    invoke-static {p1, v0, v1, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_6
    :goto_1
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_7

    const-string p1, "_t = _t.getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    return-void
.end method

.method public gen(Lantlr/ZeroOrMoreBlock;)V
    .locals 9

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "gen*("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const-string v0, "{    // ( ... )*"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget v0, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_1
    const-string v0, "_loop"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :goto_0
    const-string v2, "for (;;)"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "{"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget v2, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/2addr v2, v1

    iput v2, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v2, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v3

    iput-object v3, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_2
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v3, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/ZeroOrMoreBlock;)Z

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v3, v3, Lantlr/Grammar;->maxk:I

    iget-boolean v4, p1, Lantlr/AlternativeBlock;->greedy:Z

    const/4 v5, 0x0

    if-nez v4, :cond_3

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    if-gt v4, v3, :cond_3

    iget-object v6, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aget-object v4, v6, v4

    invoke-virtual {v4}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v4

    if-eqz v4, :cond_3

    iget v3, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    goto :goto_1

    :cond_3
    iget-boolean v4, p1, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v4, :cond_4

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    const v6, 0x7fffffff

    if-ne v4, v6, :cond_4

    :goto_1
    move v4, v3

    move v3, v1

    goto :goto_2

    :cond_4
    move v4, v3

    move v3, v5

    :goto_2
    const-string v6, "_breakloop;"

    if-eqz v3, :cond_6

    iget-boolean v3, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v3, :cond_5

    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v7, "nongreedy (...)* loop; exit depth is "

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget v8, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v3, v7}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v3, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    invoke-virtual {p0, v3, v4}, Lantlr/CSharpCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object v3

    const-string v4, "// nongreedy exit test"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "if ("

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ") goto "

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v4, v0, v6, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_6
    invoke-virtual {p0, p1, v5}, Lantlr/CSharpCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CSharpBlockFinishingInfo;

    move-result-object p1

    const-string v3, "goto "

    invoke-static {v3, v0, v6}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-direct {p0, p1, v3}, Lantlr/CSharpCodeGenerator;->genBlockFinish(Lantlr/CSharpBlockFinishingInfo;Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget p1, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v3, p1, -0x1

    iput v3, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget v3, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    if-ne p1, v3, :cond_7

    iput v5, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_7
    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "_breakloop:"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string p1, ";"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget p1, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v0, p1, -0x1

    iput v0, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget v0, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    if-ne p1, v0, :cond_8

    iput v5, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_8
    const-string p1, "}    // ( ... )*"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput-object v2, p0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;)V
    .locals 1

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p0, p1, v0}, Lantlr/CSharpCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, p1, v0, p2}, Lantlr/CSharpCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

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

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {p0, p1, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V
    .locals 8

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

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
    iput-boolean v3, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    iget-boolean v3, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz v3, :cond_1

    invoke-virtual {p1}, Lantlr/Alternative;->getAutoGen()Z

    move-result v4

    if-eqz v4, :cond_1

    move v1, v2

    :cond_1
    iput-boolean v1, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    new-instance v4, Ljava/util/Hashtable;

    invoke-direct {v4}, Ljava/util/Hashtable;-><init>()V

    iput-object v4, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    iget-object v4, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz v4, :cond_2

    const-string v4, "try        // for error handling"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "{"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

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
    iget-boolean v4, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    if-eqz v4, :cond_6

    instance-of v4, p2, Lantlr/RuleBlock;

    if-eqz v4, :cond_5

    check-cast p2, Lantlr/RuleBlock;

    iget-boolean v4, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v4, :cond_4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "_AST = ("

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p2, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v5, ")currentAST.root;"

    invoke-static {v4, p2, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    goto :goto_2

    :cond_4
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "_AST = currentAST.root;"

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_2

    :cond_5
    invoke-virtual {p2}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_6

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

    :cond_6
    :goto_2
    iget-object p2, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz p2, :cond_7

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p2, v2

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p2, "}"

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    :cond_7
    iput-boolean v0, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    iput-boolean v3, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    iput-object v1, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

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

    invoke-direct {p0, v1, v0}, Lantlr/CSharpCodeGenerator;->genBitSet(Lantlr/collections/impl/BitSet;I)V

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

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v2, 0x0

    invoke-virtual {p0, v0, p1, v1, v2}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public genBlockPreamble(Lantlr/AlternativeBlock;)V
    .locals 8

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

    const-string v3, ";"

    const-string v4, " = "

    const-string v5, " "

    if-nez v2, :cond_1

    instance-of v6, v1, Lantlr/AlternativeBlock;

    if-eqz v6, :cond_0

    instance-of v6, v1, Lantlr/RuleBlock;

    if-nez v6, :cond_0

    instance-of v6, v1, Lantlr/SynPredBlock;

    if-nez v6, :cond_0

    goto :goto_1

    :cond_0
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, p0, Lantlr/CSharpCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CSharpCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-static {v2, v4, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

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

    invoke-virtual {p0, v1, v2}, Lantlr/CSharpCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V

    goto/16 :goto_2

    :cond_1
    :goto_1
    if-nez v2, :cond_3

    move-object v2, v1

    check-cast v2, Lantlr/AlternativeBlock;

    iget-boolean v6, v2, Lantlr/AlternativeBlock;->not:Z

    if-eqz v6, :cond_3

    iget-object v6, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v7, v7, Lantlr/LexerGrammar;

    invoke-interface {v6, v2, v7}, Lantlr/LLkGrammarAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result v2

    if-eqz v2, :cond_3

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, p0, Lantlr/CSharpCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CSharpCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-static {v2, v4, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_6

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CSharpCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;)V

    goto :goto_2

    :cond_3
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_4

    invoke-virtual {p0, v1}, Lantlr/CSharpCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;)V

    :cond_4
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    if-eqz v2, :cond_5

    const-string v2, "IToken "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, " = null;"

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_6

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, p0, Lantlr/CSharpCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-static {v2, v1, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_6
    :goto_2
    add-int/lit8 v0, v0, 0x1

    goto/16 :goto_0

    :cond_7
    return-void
.end method

.method public genBody(Lantlr/LexerGrammar;)V
    .locals 10

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CSharpCodeGenerator;->setupOutput(Ljava/lang/String;)V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    const/4 v1, 0x1

    iput-boolean v1, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genHeader()V

    iget-object v2, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    const-string v3, ""

    invoke-virtual {v2, v3}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    sget-object v2, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    if-eqz v2, :cond_0

    iget-object v4, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v2, v4}, Lantlr/CSharpNameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_0
    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "// Generate header specific to lexer CSharp file"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using System;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using Stream                          = System.IO.Stream;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using TextReader                      = System.IO.TextReader;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using Hashtable                       = System.Collections.Hashtable;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using Comparer                        = System.Collections.Comparer;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v2, p1, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    if-nez v2, :cond_1

    const-string v2, "using CaseInsensitiveHashCodeProvider = System.Collections.CaseInsensitiveHashCodeProvider;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using CaseInsensitiveComparer         = System.Collections.CaseInsensitiveComparer;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using TokenStreamException            = antlr.TokenStreamException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using TokenStreamIOException          = antlr.TokenStreamIOException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using TokenStreamRecognitionException = antlr.TokenStreamRecognitionException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using CharStreamException             = antlr.CharStreamException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using CharStreamIOException           = antlr.CharStreamIOException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using ANTLRException                  = antlr.ANTLRException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using CharScanner                     = antlr.CharScanner;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using InputBuffer                     = antlr.InputBuffer;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using ByteBuffer                      = antlr.ByteBuffer;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using CharBuffer                      = antlr.CharBuffer;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using Token                           = antlr.Token;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using IToken                          = antlr.IToken;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using CommonToken                     = antlr.CommonToken;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using SemanticException               = antlr.SemanticException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using RecognitionException            = antlr.RecognitionException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using NoViableAltForCharException     = antlr.NoViableAltForCharException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using MismatchedCharException         = antlr.MismatchedCharException;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using TokenStream                     = antlr.TokenStream;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using LexerSharedInputState           = antlr.LexerSharedInputState;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "using BitSet                          = antlr.collections.impl.BitSet;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v2, :cond_2

    goto :goto_0

    :cond_2
    const-string v2, "antlr."

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    :goto_0
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v4, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_3
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v5, "classHeaderPrefix"

    invoke-virtual {v4, v5}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/Token;

    const-string v5, "\""

    const-string v6, "public "

    if-nez v4, :cond_4

    goto :goto_1

    :cond_4
    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    invoke-static {v4, v5, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    if-nez v4, :cond_5

    :goto_1
    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_2

    :cond_5
    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " "

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :goto_2
    const-string v4, "class "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v7}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, " : "

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    const-string v2, ", TokenStream"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v4, "classHeaderSuffix"

    invoke-virtual {v2, v4}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/Token;

    const-string v4, ", "

    if-eqz v2, :cond_6

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2, v5, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_6

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_6
    const-string v2, " {"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p0, v2}, Lantlr/CSharpCodeGenerator;->genTokenDefinitions(Lantlr/TokenManager;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v5}, Lantlr/Token;->getLine()I

    move-result v5

    iget-object v7, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v8, 0x0

    invoke-virtual {p0, v2, v5, v7, v8}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v5}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "(Stream ins) : this(new ByteBuffer(ins))"

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "{"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "}"

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "(TextReader r) : this(new CharBuffer(r))"

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "(InputBuffer ib)"

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v7, v7, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v7, :cond_7

    const-string v7, " : this(new LexerSharedInputState(new antlr.debug.DebuggingInputBuffer(ib)))"

    goto :goto_3

    :cond_7
    const-string v7, " : this(new LexerSharedInputState(ib))"

    :goto_3
    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v6}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "(LexerSharedInputState state) : base(state)"

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v6, v1

    iput v6, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v6, "initialize();"

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v6, v1

    iput v6, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v6, "private void initialize()"

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_8

    const-string v2, "ruleNames  = _ruleNames;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "semPredNames = _semPredNames;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "setupDebugging();"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    const-string v2, "caseSensitiveLiterals = "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-boolean v6, p1, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string v6, ";"

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "setCaseSensitive("

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean v6, p1, Lantlr/LexerGrammar;->caseSensitive:Z

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string v6, ");"

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean p1, p1, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    if-eqz p1, :cond_9

    const-string p1, "literals = new Hashtable(100, (float) 0.4, null, Comparer.Default);"

    goto :goto_4

    :cond_9
    const-string p1, "literals = new Hashtable(100, (float) 0.4, CaseInsensitiveHashCodeProvider.Default, CaseInsensitiveComparer.Default);"

    :goto_4
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1}, Lantlr/TokenManager;->getTokenSymbolKeys()Ljava/util/Enumeration;

    move-result-object p1

    :cond_a
    :goto_5
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_c

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    invoke-virtual {v2, v0}, Ljava/lang/String;->charAt(I)C

    move-result v7

    const/16 v9, 0x22

    if-eq v7, v9, :cond_b

    goto :goto_5

    :cond_b
    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v7, v2}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v2

    instance-of v7, v2, Lantlr/StringLiteralSymbol;

    if-eqz v7, :cond_a

    check-cast v2, Lantlr/StringLiteralSymbol;

    const-string v7, "literals.Add("

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    invoke-virtual {v2}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v2

    invoke-virtual {v7, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_5

    :cond_c
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_f

    const-string p1, "private static readonly string[] _ruleNames = new string[] {"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object p1

    :cond_d
    :goto_6
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_e

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/GrammarSymbol;

    instance-of v4, v2, Lantlr/RuleSymbol;

    if-eqz v4, :cond_d

    const-string v4, "  \""

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    check-cast v2, Lantlr/RuleSymbol;

    invoke-virtual {v2}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\","

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_6

    :cond_e
    const-string p1, "};"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genNextToken()V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object p1

    move v2, v0

    :goto_7
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v4

    if-eqz v4, :cond_11

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/RuleSymbol;

    invoke-virtual {v4}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    const-string v7, "mnextToken"

    invoke-virtual {v6, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-nez v6, :cond_10

    add-int/lit8 v6, v2, 0x1

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v7, v7, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p0, v4, v0, v2, v7}, Lantlr/CSharpCodeGenerator;->genRule(Lantlr/RuleSymbol;ZILantlr/TokenManager;)V

    move v2, v6

    :cond_10
    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->exitIfError()V

    goto :goto_7

    :cond_11
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_12

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genSemPredMap()V

    :cond_12
    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-object v0, v0, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0}, Lantlr/collections/impl/BitSet;->size()I

    move-result v0

    invoke-virtual {p0, p1, v0}, Lantlr/CSharpCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    sget-object p1, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    if-eqz p1, :cond_13

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/CSharpNameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_13
    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    iput-object v8, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genBody(Lantlr/ParserGrammar;)V
    .locals 12

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CSharpCodeGenerator;->setupOutput(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    iput-boolean v0, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genHeader()V

    iget-object v1, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    const-string v2, ""

    invoke-virtual {v1, v2}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    sget-object v1, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    if-eqz v1, :cond_0

    iget-object v3, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v1, v3}, Lantlr/CSharpNameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_0
    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v1, v3

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "// Generate the header common to all output files."

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using System;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using TokenBuffer              = antlr.TokenBuffer;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using TokenStreamException     = antlr.TokenStreamException;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using TokenStreamIOException   = antlr.TokenStreamIOException;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using ANTLRException           = antlr.ANTLRException;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v1

    const-string v4, "."

    invoke-virtual {p0, v1, v4}, Lantlr/CSharpCodeGenerator;->split(Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v4

    const-string v5, "using "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    array-length v6, v4

    sub-int/2addr v6, v3

    aget-object v4, v4, v6

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " = antlr."

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ";"

    invoke-virtual {v5, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using Token                    = antlr.Token;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using IToken                   = antlr.IToken;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using TokenStream              = antlr.TokenStream;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using RecognitionException     = antlr.RecognitionException;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using NoViableAltException     = antlr.NoViableAltException;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using MismatchedTokenException = antlr.MismatchedTokenException;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using SemanticException        = antlr.SemanticException;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using ParserSharedInputState   = antlr.ParserSharedInputState;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using BitSet                   = antlr.collections.impl.BitSet;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v1, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    if-eqz v1, :cond_1

    const-string v1, "using AST                      = antlr.collections.AST;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using ASTPair                  = antlr.ASTPair;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using ASTFactory               = antlr.ASTFactory;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "using ASTArray                 = antlr.collections.impl.ASTArray;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v1, :cond_2

    goto :goto_0

    :cond_2
    const-string v1, "antlr."

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :goto_0
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v4, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_3
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v5, "classHeaderPrefix"

    invoke-virtual {v4, v5}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/Token;

    const-string v5, "public "

    const-string v6, "\""

    if-nez v4, :cond_4

    goto :goto_1

    :cond_4
    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    invoke-static {v4, v6, v6}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    if-nez v4, :cond_5

    :goto_1
    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_2

    :cond_5
    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " "

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :goto_2
    const-string v4, "class "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v7}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, " : "

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v4, "classHeaderSuffix"

    invoke-virtual {v1, v4}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/Token;

    if-eqz v1, :cond_6

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1, v6, v6}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_6

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "              , "

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_6
    const-string v1, "{"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v3

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p0, v4}, Lantlr/CSharpCodeGenerator;->genTokenDefinitions(Lantlr/TokenManager;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v4, :cond_9

    const-string v4, "private static readonly string[] _ruleNames = new string[] {"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v3

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v4}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v4

    :cond_7
    :goto_3
    invoke-interface {v4}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v6

    if-eqz v6, :cond_8

    invoke-interface {v4}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lantlr/GrammarSymbol;

    instance-of v7, v6, Lantlr/RuleSymbol;

    if-eqz v7, :cond_7

    const-string v7, "  \""

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    check-cast v6, Lantlr/RuleSymbol;

    invoke-virtual {v6}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "\","

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_3

    :cond_8
    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v3

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v4, "};"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v6, v6, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v6}, Lantlr/Token;->getLine()I

    move-result v6

    iget-object v7, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v8, 0x0

    invoke-virtual {p0, v4, v6, v7, v8}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "protected void initialize()"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v3

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v4, "tokenNames = tokenNames_;"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->buildAST:Z

    if-eqz v4, :cond_a

    const-string v4, "initializeFactory();"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v4, :cond_b

    const-string v4, "ruleNames  = _ruleNames;"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "semPredNames = _semPredNames;"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "setupDebugging(tokenBuf);"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_b
    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v3

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v4, "}"

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "protected "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "(TokenBuffer tokenBuf, int k) : base(tokenBuf, k)"

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v6, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v6, v3

    iput v6, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v6, "initialize();"

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v9, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v9, v3

    iput v9, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v9, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v11, "(TokenBuffer tokenBuf) : this(tokenBuf,"

    invoke-static {v10, v9, v11}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v10, v10, Lantlr/Grammar;->maxk:I

    invoke-virtual {v9, v10}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v10, ")"

    invoke-virtual {v9, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {p0, v9}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v7}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "(TokenStream lexer, int k) : base(lexer,k)"

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v3

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v7, v3

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v11, "(TokenStream lexer) : this(lexer,"

    invoke-static {v9, v7, v11}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v9, v9, Lantlr/Grammar;->maxk:I

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v9, "(ParserSharedInputState state) : base(state,"

    invoke-static {v5, v7, v9}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v5, v5, Lantlr/Grammar;->maxk:I

    invoke-virtual {v7, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v3

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v5, v3

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/util/Vector;

    const/16 v6, 0x64

    invoke-direct {v5, v6}, Ljava/util/Vector;-><init>(I)V

    iput-object v5, p0, Lantlr/CSharpCodeGenerator;->astTypes:Ljava/util/Vector;

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v5}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v5

    move v6, v0

    :goto_4
    invoke-interface {v5}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v7

    if-eqz v7, :cond_e

    invoke-interface {v5}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Lantlr/GrammarSymbol;

    instance-of v9, v7, Lantlr/RuleSymbol;

    if-eqz v9, :cond_d

    check-cast v7, Lantlr/RuleSymbol;

    iget-object v9, v7, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v9}, Lantlr/collections/impl/Vector;->size()I

    move-result v9

    if-nez v9, :cond_c

    move v9, v3

    goto :goto_5

    :cond_c
    move v9, v0

    :goto_5
    add-int/lit8 v10, v6, 0x1

    iget-object v11, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v11, v11, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p0, v7, v9, v6, v11}, Lantlr/CSharpCodeGenerator;->genRule(Lantlr/RuleSymbol;ZILantlr/TokenManager;)V

    move v6, v10

    :cond_d
    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->exitIfError()V

    goto :goto_4

    :cond_e
    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_f

    const-string v0, "public new "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v5, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v0, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " getAST()"

    invoke-virtual {v0, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v3

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "return ("

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v5, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v6, ") returnAST;"

    invoke-static {v0, v5, v6, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v3

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    const-string v0, "private void initializeFactory()"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v3

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_11

    const-string v0, "if (astFactory == null)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v3

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_10

    const-string v0, "astFactory = new ASTFactory(\""

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v5, "\");"

    invoke-static {v0, v1, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    goto :goto_6

    :cond_10
    const-string v0, "astFactory = new ASTFactory();"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_6
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v3

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "initializeASTFactory( astFactory );"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_11
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v3

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genInitFactory(Lantlr/Grammar;)V

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genTokenStrings()V

    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->maxTokenType()I

    move-result v0

    invoke-virtual {p0, p1, v0}, Lantlr/CSharpCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_12

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genSemPredMap()V

    :cond_12
    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v3

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v3

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    sget-object p1, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    if-eqz p1, :cond_13

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/CSharpNameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_13
    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    iput-object v8, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genBody(Lantlr/TreeWalkerGrammar;)V
    .locals 11

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->setupOutput(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->buildAST:Z

    iput-boolean p1, p0, Lantlr/CSharpCodeGenerator;->genAST:Z

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genHeader()V

    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    const-string v1, ""

    invoke-virtual {v0, v1}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    sget-object v0, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    if-eqz v0, :cond_0

    iget-object v2, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v0, v2}, Lantlr/CSharpNameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_0
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v0, v2

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "// Generate header specific to the tree-parser CSharp file"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using System;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "using "

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " = antlr."

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ";"

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using Token                    = antlr.Token;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using IToken                   = antlr.IToken;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using AST                      = antlr.collections.AST;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using RecognitionException     = antlr.RecognitionException;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using ANTLRException           = antlr.ANTLRException;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using NoViableAltException     = antlr.NoViableAltException;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using MismatchedTokenException = antlr.MismatchedTokenException;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using SemanticException        = antlr.SemanticException;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using BitSet                   = antlr.collections.impl.BitSet;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using ASTPair                  = antlr.ASTPair;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using ASTFactory               = antlr.ASTFactory;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "using ASTArray                 = antlr.collections.impl.ASTArray;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v0, :cond_1

    goto :goto_0

    :cond_1
    const-string v0, "antlr."

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v3, :cond_2

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_2
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v4, "classHeaderPrefix"

    invoke-virtual {v3, v4}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/Token;

    const-string v4, "public "

    const-string v5, "\""

    if-nez v3, :cond_3

    goto :goto_1

    :cond_3
    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3, v5, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    if-nez v3, :cond_4

    :goto_1
    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_2

    :cond_4
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " "

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :goto_2
    const-string v3, "class "

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v6}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, " : "

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v3, "classHeaderSuffix"

    invoke-virtual {v0, v3}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Token;

    if-eqz v0, :cond_5

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0, v5, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_5

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "              , "

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_5
    const-string v0, "{"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p0, v3}, Lantlr/CSharpCodeGenerator;->genTokenDefinitions(Lantlr/TokenManager;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v5}, Lantlr/Token;->getLine()I

    move-result v5

    iget-object v6, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v7, 0x0

    invoke-virtual {p0, v3, v5, v6, v7}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "()"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "tokenNames = tokenNames_;"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v2

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "}"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v4, Ljava/util/Vector;

    invoke-direct {v4}, Ljava/util/Vector;-><init>()V

    iput-object v4, p0, Lantlr/CSharpCodeGenerator;->astTypes:Ljava/util/Vector;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v4}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v4

    move v5, p1

    :goto_3
    invoke-interface {v4}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v6

    if-eqz v6, :cond_8

    invoke-interface {v4}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lantlr/GrammarSymbol;

    instance-of v8, v6, Lantlr/RuleSymbol;

    if-eqz v8, :cond_7

    check-cast v6, Lantlr/RuleSymbol;

    iget-object v8, v6, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v8}, Lantlr/collections/impl/Vector;->size()I

    move-result v8

    if-nez v8, :cond_6

    move v8, v2

    goto :goto_4

    :cond_6
    move v8, p1

    :goto_4
    add-int/lit8 v9, v5, 0x1

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v10, v10, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p0, v6, v8, v5, v10}, Lantlr/CSharpCodeGenerator;->genRule(Lantlr/RuleSymbol;ZILantlr/TokenManager;)V

    move v5, v9

    :cond_7
    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->exitIfError()V

    goto :goto_3

    :cond_8
    iget-boolean p1, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz p1, :cond_9

    const-string p1, "public new "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v4, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " getAST()"

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "return ("

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v4, ") returnAST;"

    invoke-static {p1, v0, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genInitFactory(Lantlr/Grammar;)V

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genTokenStrings()V

    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->maxTokenType()I

    move-result v0

    invoke-virtual {p0, p1, v0}, Lantlr/CSharpCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;I)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    sget-object p1, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    if-eqz p1, :cond_a

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/CSharpNameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_a
    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    iput-object v7, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genCases(Lantlr/collections/impl/BitSet;)V
    .locals 8

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

    const/4 v1, 0x1

    if-eqz v0, :cond_1

    const/4 v0, 0x4

    goto :goto_0

    :cond_1
    move v0, v1

    :goto_0
    const/4 v2, 0x0

    move v4, v1

    move v5, v4

    move v3, v2

    :goto_1
    array-length v6, p1

    const-string v7, ""

    if-ge v3, v6, :cond_4

    if-ne v5, v1, :cond_2

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_2

    :cond_2
    const-string v4, "  "

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :goto_2
    const-string v4, "case "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    aget v6, p1, v3

    invoke-direct {p0, v6}, Lantlr/CSharpCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, ":"

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    if-ne v5, v0, :cond_3

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

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

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_5
    return-void
.end method

.method public genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CSharpBlockFinishingInfo;
    .locals 24

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    new-instance v2, Lantlr/CSharpBlockFinishingInfo;

    invoke-direct {v2}, Lantlr/CSharpBlockFinishingInfo;-><init>()V

    iget-boolean v3, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v3, :cond_0

    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "genCommonBlock("

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v5, ")"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-boolean v3, v0, Lantlr/CSharpCodeGenerator;->genAST:Z

    const/4 v4, 0x0

    const/4 v5, 0x1

    if-eqz v3, :cond_1

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result v6

    if-eqz v6, :cond_1

    move v6, v5

    goto :goto_0

    :cond_1
    move v6, v4

    :goto_0
    iput-boolean v6, v0, Lantlr/CSharpCodeGenerator;->genAST:Z

    iget-boolean v6, v0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz v6, :cond_2

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result v7

    if-eqz v7, :cond_2

    move v7, v5

    goto :goto_1

    :cond_2
    move v7, v4

    :goto_1
    iput-boolean v7, v0, Lantlr/CSharpCodeGenerator;->saveText:Z

    iget-boolean v7, v1, Lantlr/AlternativeBlock;->not:Z

    const-string v8, ""

    if-eqz v7, :cond_8

    iget-object v7, v0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v9, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v9, Lantlr/LexerGrammar;

    invoke-interface {v7, v1, v9}, Lantlr/LLkGrammarAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result v7

    if-eqz v7, :cond_8

    iget-boolean v3, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v3, :cond_3

    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v4, "special case: ~(subrule)"

    invoke-virtual {v3, v4}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_3
    iget-object v3, v0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v3, v5, v1}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object v3

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_4

    iget v4, v0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    if-nez v4, :cond_4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " = "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, v0, Lantlr/CSharpCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v6, ";"

    invoke-static {v4, v5, v6, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_4
    invoke-direct/range {p0 .. p1}, Lantlr/CSharpCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_6

    iget-boolean v1, v0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v1, :cond_5

    const-string v8, "(AST)_t,"

    goto :goto_2

    :cond_5
    const-string v8, "_t,"

    :cond_6
    :goto_2
    const-string v1, "match("

    invoke-static {v1, v8}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, v3, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->markBitsetForGen(Lantlr/collections/impl/BitSet;)I

    move-result v3

    invoke-virtual {v0, v3}, Lantlr/CSharpCodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ");"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_7

    const-string v1, "_t = _t.getNextSibling();"

    invoke-virtual {v0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    return-object v2

    :cond_8
    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v7

    invoke-virtual {v7}, Lantlr/collections/impl/Vector;->size()I

    move-result v7

    if-ne v7, v5, :cond_b

    invoke-virtual {v1, v4}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v7

    iget-object v9, v7, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v9, :cond_9

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

    :cond_9
    if-eqz p2, :cond_b

    iget-object v3, v7, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v3, :cond_a

    iget v4, v1, Lantlr/GrammarElement;->line:I

    invoke-virtual {v0, v3, v4}, Lantlr/CSharpCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    :cond_a
    invoke-virtual {v0, v7, v1}, Lantlr/CSharpCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    return-object v2

    :cond_b
    move v7, v4

    move v9, v7

    :goto_3
    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v10

    invoke-virtual {v10}, Lantlr/collections/impl/Vector;->size()I

    move-result v10

    if-ge v7, v10, :cond_d

    invoke-virtual {v1, v7}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v10

    invoke-static {v10}, Lantlr/CSharpCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v10

    if-eqz v10, :cond_c

    add-int/lit8 v9, v9, 0x1

    :cond_c
    add-int/lit8 v7, v7, 0x1

    goto :goto_3

    :cond_d
    iget v7, v0, Lantlr/CodeGenerator;->makeSwitchThreshold:I

    const-string v10, "_t = ASTNULL;"

    const-string v11, "}"

    const-string v12, "{"

    if-lt v9, v7, :cond_13

    invoke-direct {v0, v5}, Lantlr/CSharpCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v7

    iget-object v9, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v9, Lantlr/TreeWalkerGrammar;

    if-eqz v9, :cond_e

    const-string v9, "if (null == _t)"

    invoke-virtual {v0, v9}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v9, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v9, v5

    iput v9, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v10}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v9, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v9, v5

    iput v9, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_e
    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    const-string v13, "switch ( "

    invoke-virtual {v9, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, " )"

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v12}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v7, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/2addr v7, v5

    iput v7, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    move v7, v4

    :goto_4
    iget-object v9, v1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v9}, Lantlr/collections/impl/Vector;->size()I

    move-result v9

    if-ge v7, v9, :cond_12

    invoke-virtual {v1, v7}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v9

    invoke-static {v9}, Lantlr/CSharpCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v13

    if-nez v13, :cond_f

    goto :goto_5

    :cond_f
    iget-object v13, v9, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v13, v13, v5

    iget-object v14, v13, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v14}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v14

    if-nez v14, :cond_10

    invoke-virtual {v13}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v14

    if-nez v14, :cond_10

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

    goto :goto_5

    :cond_10
    iget-object v4, v13, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v4}, Lantlr/CSharpCodeGenerator;->genCases(Lantlr/collections/impl/BitSet;)V

    invoke-virtual {v0, v12}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    iget v4, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/2addr v4, v5

    iput v4, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    invoke-virtual {v0, v9, v1}, Lantlr/CSharpCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    const-string v4, "break;"

    invoke-virtual {v0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v9, v4, -0x1

    iput v9, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget v9, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    if-ne v4, v9, :cond_11

    const/4 v4, 0x0

    iput v4, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_11
    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v11}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_5
    add-int/lit8 v7, v7, 0x1

    const/4 v4, 0x0

    goto :goto_4

    :cond_12
    const-string v4, "default:"

    invoke-virtual {v0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    move v4, v5

    goto :goto_6

    :cond_13
    const/4 v4, 0x0

    :goto_6
    iget-object v7, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v7, Lantlr/LexerGrammar;

    if-eqz v9, :cond_14

    iget v7, v7, Lantlr/Grammar;->maxk:I

    goto :goto_7

    :cond_14
    const/4 v7, 0x0

    :goto_7
    const/4 v9, 0x0

    const/4 v13, 0x0

    :goto_8
    if-ltz v7, :cond_2a

    iget-boolean v14, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v14, :cond_15

    sget-object v14, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v15, Ljava/lang/StringBuilder;

    invoke-direct {v15}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "checking depth "

    invoke-virtual {v15, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v15, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v15}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v14, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_15
    const/4 v5, 0x0

    :goto_9
    iget-object v14, v1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v14}, Lantlr/collections/impl/Vector;->size()I

    move-result v14

    if-ge v5, v14, :cond_29

    invoke-virtual {v1, v5}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v14

    iget-boolean v15, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v15, :cond_16

    sget-object v15, Ljava/lang/System;->out:Ljava/io/PrintStream;

    move-object/from16 v17, v8

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    move/from16 v18, v6

    const-string v6, "genAlt: "

    invoke-virtual {v8, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v15, v6}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_a

    :cond_16
    move/from16 v18, v6

    move-object/from16 v17, v8

    :goto_a
    if-eqz v4, :cond_17

    invoke-static {v14}, Lantlr/CSharpCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v6

    if-eqz v6, :cond_17

    iget-boolean v6, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v6, :cond_1a

    sget-object v6, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v8, "ignoring alt because it was in the switch"

    :goto_b
    invoke-virtual {v6, v8}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_d

    :cond_17
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v8, v6, Lantlr/LexerGrammar;

    if-eqz v8, :cond_1c

    iget v8, v14, Lantlr/Alternative;->lookaheadDepth:I

    const v15, 0x7fffffff

    if-ne v8, v15, :cond_18

    iget v8, v6, Lantlr/Grammar;->maxk:I

    :cond_18
    :goto_c
    const/4 v6, 0x1

    if-lt v8, v6, :cond_19

    iget-object v6, v14, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v6, v6, v8

    invoke-virtual {v6}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v6

    if-eqz v6, :cond_19

    add-int/lit8 v8, v8, -0x1

    goto :goto_c

    :cond_19
    if-eq v8, v7, :cond_1b

    iget-boolean v6, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-eqz v6, :cond_1a

    sget-object v6, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v14, Ljava/lang/StringBuilder;

    invoke-direct {v14}, Ljava/lang/StringBuilder;-><init>()V

    const-string v15, "ignoring alt because effectiveDepth!=altDepth;"

    invoke-virtual {v14, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v8, "!="

    invoke-virtual {v14, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v14}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    goto :goto_b

    :cond_1a
    :goto_d
    move-object/from16 v21, v2

    move/from16 v19, v3

    move/from16 p2, v4

    move/from16 v22, v5

    move/from16 v20, v7

    move-object v2, v11

    goto/16 :goto_14

    :cond_1b
    invoke-virtual {v0, v14, v8}, Lantlr/CSharpCodeGenerator;->lookaheadIsEmpty(Lantlr/Alternative;I)Z

    move-result v6

    goto :goto_e

    :cond_1c
    iget v6, v6, Lantlr/Grammar;->maxk:I

    invoke-virtual {v0, v14, v6}, Lantlr/CSharpCodeGenerator;->lookaheadIsEmpty(Lantlr/Alternative;I)Z

    move-result v6

    iget-object v8, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v8, v8, Lantlr/Grammar;->maxk:I

    :goto_e
    invoke-virtual {v0, v14, v8}, Lantlr/CSharpCodeGenerator;->getLookaheadTestExpression(Lantlr/Alternative;I)Ljava/lang/String;

    move-result-object v8

    iget-object v15, v14, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    const/16 v16, 0x1

    aget-object v15, v15, v16

    iget-object v15, v15, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v15}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v15

    move/from16 p2, v4

    const/16 v4, 0x7f

    move/from16 v19, v3

    const-string v3, "if "

    move/from16 v20, v7

    const-string v7, "else if "

    if-le v15, v4, :cond_1e

    invoke-static {v14}, Lantlr/CSharpCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v4

    if-eqz v4, :cond_1e

    if-nez v9, :cond_1d

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    goto :goto_f

    :cond_1d
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    :goto_f
    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v12}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_10
    move-object/from16 v21, v2

    move/from16 v22, v5

    move-object/from16 v23, v11

    goto/16 :goto_13

    :cond_1e
    const-string v4, "else {"

    if-eqz v6, :cond_20

    iget-object v6, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-nez v6, :cond_20

    iget-object v6, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-nez v6, :cond_20

    if-nez v9, :cond_1f

    invoke-virtual {v0, v12}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_11

    :cond_1f
    invoke-virtual {v0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_11
    const/4 v3, 0x0

    iput-boolean v3, v2, Lantlr/CSharpBlockFinishingInfo;->needAnErrorClause:Z

    goto :goto_10

    :cond_20
    iget-object v6, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v6, :cond_23

    new-instance v6, Lantlr/ActionTransInfo;

    invoke-direct {v6}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v15, v14, Lantlr/Alternative;->semPred:Ljava/lang/String;

    move-object/from16 v21, v2

    iget v2, v1, Lantlr/GrammarElement;->line:I

    move/from16 v22, v5

    iget-object v5, v0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v0, v15, v2, v5, v6}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v2

    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v6, v5, Lantlr/ParserGrammar;

    const-string v15, "))"

    move-object/from16 v23, v11

    const-string v11, "("

    if-nez v6, :cond_21

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_22

    :cond_21
    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v5, :cond_22

    const-string v5, "&& fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEventArgs.PREDICTING,"

    invoke-static {v11, v8, v5}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget-object v6, v0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v6, v2}, Lantlr/CharFormatter;->escapeString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CSharpCodeGenerator;->addSemPred(Ljava/lang/String;)I

    move-result v6

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v6, ","

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v5, v2, v15}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    goto :goto_12

    :cond_22
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "&&("

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    goto :goto_12

    :cond_23
    move-object/from16 v21, v2

    move/from16 v22, v5

    move-object/from16 v23, v11

    :goto_12
    iget-object v2, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-lez v9, :cond_25

    if-eqz v2, :cond_24

    invoke-virtual {v0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v2, v3

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    iget v2, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/2addr v2, v3

    iput v2, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget-object v2, v14, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v0, v2, v8}, Lantlr/CSharpCodeGenerator;->genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V

    add-int/lit8 v13, v13, 0x1

    goto :goto_13

    :cond_24
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " {"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_13

    :cond_25
    if-eqz v2, :cond_26

    invoke-virtual {v0, v2, v8}, Lantlr/CSharpCodeGenerator;->genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V

    goto :goto_13

    :cond_26
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_27

    const-string v2, "if (_t == null)"

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    add-int/2addr v2, v4

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v10}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v4

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_27
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v12}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_13
    iget v2, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    const/4 v3, 0x1

    add-int/2addr v2, v3

    iput v2, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v9, v9, 0x1

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v3

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v14, v1}, Lantlr/CSharpCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v3

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    iget v2, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v3, v2, -0x1

    iput v3, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget v3, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    if-ne v2, v3, :cond_28

    const/4 v2, 0x0

    iput v2, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_28
    move-object/from16 v2, v23

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_14
    add-int/lit8 v5, v22, 0x1

    move/from16 v4, p2

    move-object v11, v2

    move-object/from16 v8, v17

    move/from16 v6, v18

    move/from16 v3, v19

    move/from16 v7, v20

    move-object/from16 v2, v21

    goto/16 :goto_9

    :cond_29
    move-object/from16 v21, v2

    move/from16 v19, v3

    move/from16 p2, v4

    move/from16 v18, v6

    move/from16 v20, v7

    move-object/from16 v17, v8

    move-object v2, v11

    add-int/lit8 v7, v20, -0x1

    move-object/from16 v2, v21

    const/4 v5, 0x1

    goto/16 :goto_8

    :cond_2a
    move-object/from16 v21, v2

    move/from16 v19, v3

    move/from16 p2, v4

    move/from16 v18, v6

    move-object/from16 v17, v8

    move-object v2, v11

    move-object/from16 v3, v17

    const/4 v1, 0x1

    :goto_15
    if-gt v1, v13, :cond_2c

    invoke-static {v3, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    iget v4, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v5, v4, -0x1

    iput v5, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget v5, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    if-ne v4, v5, :cond_2b

    const/4 v4, 0x0

    iput v4, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_2b
    add-int/lit8 v1, v1, 0x1

    goto :goto_15

    :cond_2c
    move/from16 v1, v19

    iput-boolean v1, v0, Lantlr/CSharpCodeGenerator;->genAST:Z

    move/from16 v1, v18

    iput-boolean v1, v0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz p2, :cond_2e

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    sub-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "break; }"

    invoke-static {v3, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    move-object/from16 v2, v21

    iput-object v1, v2, Lantlr/CSharpBlockFinishingInfo;->postscript:Ljava/lang/String;

    iget v1, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    add-int/lit8 v3, v1, -0x1

    iput v3, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iget v3, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    const/4 v4, 0x0

    if-ne v1, v3, :cond_2d

    iput v4, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_2d
    const/4 v6, 0x1

    iput-boolean v6, v2, Lantlr/CSharpBlockFinishingInfo;->generatedSwitch:Z

    if-lez v9, :cond_2f

    goto :goto_16

    :cond_2e
    move-object/from16 v2, v21

    const/4 v4, 0x0

    const/4 v6, 0x1

    iput-object v3, v2, Lantlr/CSharpBlockFinishingInfo;->postscript:Ljava/lang/String;

    iput-boolean v4, v2, Lantlr/CSharpBlockFinishingInfo;->generatedSwitch:Z

    if-lez v9, :cond_2f

    :goto_16
    move v4, v6

    :cond_2f
    iput-boolean v4, v2, Lantlr/CSharpBlockFinishingInfo;->generatedAnIf:Z

    return-object v2
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

    const-string v1, ".cs\"$"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genInitFactory(Lantlr/Grammar;)V
    .locals 6

    iget-boolean v0, p1, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_2

    const-string v0, "static public void initializeASTFactory( ASTFactory factory )"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "{"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "factory.setMaxNodeType("

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v1}, Lantlr/TokenManager;->maxTokenType()I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ");"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v0

    const/4 v1, 0x0

    :goto_0
    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    if-ge v1, v2, :cond_1

    invoke-virtual {v0, v1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    if-eqz v2, :cond_0

    iget-object v3, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3, v2}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v3

    if-eqz v3, :cond_0

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_0

    const-string v4, "factory.setTokenTypeASTNodeType("

    const-string v5, ", \""

    invoke-static {v4, v2, v5}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "\");"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
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
    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genMatchUsingAtomTokenType(Lantlr/GrammarAtom;)V

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
    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genMatchUsingAtomText(Lantlr/GrammarAtom;)V

    goto :goto_1

    :cond_4
    instance-of v0, p1, Lantlr/WildcardElement;

    if-eqz v0, :cond_5

    check-cast p1, Lantlr/WildcardElement;

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->gen(Lantlr/WildcardElement;)V

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

    if-eqz v0, :cond_1

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_0

    const-string v0, "(AST)_t,"

    goto :goto_0

    :cond_0
    const-string v0, "_t,"

    goto :goto_0

    :cond_1
    const-string v0, ""

    :goto_0
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    const/4 v2, 0x3

    if-eqz v1, :cond_3

    iget-boolean v1, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz v1, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-ne v1, v2, :cond_3

    :cond_2
    invoke-direct {p0}, Lantlr/CSharpCodeGenerator;->declareSaveIndexVariableIfNeeded()V

    const-string v1, "_saveIndex = text.Length;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget-boolean v1, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v1, :cond_4

    const-string v1, "matchNot("

    goto :goto_1

    :cond_4
    const-string v1, "match("

    :goto_1
    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    const-string v1, "EOF"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    const-string v0, "Token.EOF_TYPE"

    goto :goto_2

    :cond_5
    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    :goto_2
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    const-string v0, ");"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_7

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->saveText:Z

    if-eqz v0, :cond_6

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v2, :cond_7

    :cond_6
    invoke-direct {p0}, Lantlr/CSharpCodeGenerator;->declareSaveIndexVariableIfNeeded()V

    const-string p1, "text.Length = _saveIndex;"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    return-void
.end method

.method public genMatchUsingAtomTokenType(Lantlr/GrammarAtom;)V
    .locals 2

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_1

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_0

    const-string v0, "(AST)_t,"

    goto :goto_0

    :cond_0
    const-string v0, "_t,"

    goto :goto_0

    :cond_1
    const-string v0, ""

    :goto_0
    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getType()I

    move-result v1

    invoke-direct {p0, v1}, Lantlr/CSharpCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-boolean p1, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz p1, :cond_2

    const-string p1, "matchNot("

    goto :goto_1

    :cond_2
    const-string p1, "match("

    :goto_1
    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ");"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genNextToken()V
    .locals 15

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
    const-string v2, "override public IToken nextToken()\t\t\t//throws TokenStreamException"

    const-string v5, ""

    const-string v6, "{"

    const-string v7, "}"

    if-nez v1, :cond_2

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "try"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "uponEOF();"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "catch(CharStreamIOException csioe)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "throw new TokenStreamIOException(csioe.io);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "catch(CharStreamException cse)"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "throw new TokenStreamException(cse.Message);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "return new CommonToken(Token.EOF_TYPE, \"\");"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_2
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void

    :cond_2
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    const-string v9, "nextToken"

    invoke-static {v1, v8, v9}, Lantlr/MakeGrammar;->createNextTokenRule(Lantlr/Grammar;Lantlr/collections/impl/Vector;Ljava/lang/String;)Lantlr/RuleBlock;

    move-result-object v1

    new-instance v8, Lantlr/RuleSymbol;

    const-string v9, "mnextToken"

    invoke-direct {v8, v9}, Lantlr/RuleSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {v8}, Lantlr/RuleSymbol;->setDefined()V

    invoke-virtual {v8, v1}, Lantlr/RuleSymbol;->setBlock(Lantlr/RuleBlock;)V

    const-string v9, "private"

    iput-object v9, v8, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9, v8}, Lantlr/Grammar;->define(Lantlr/RuleSymbol;)V

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v8, v1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    const/4 v8, 0x0

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v9, Lantlr/LexerGrammar;

    iget-boolean v10, v9, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v10, :cond_3

    iget-object v8, v9, Lantlr/LexerGrammar;->filterRule:Ljava/lang/String;

    :cond_3
    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v4

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iput v4, p0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iput v0, p0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    const-string v2, "IToken theRetToken = null;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "tryAgain:"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    const-string v2, "for (;;)"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v4

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "IToken _token = null;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "int _ttype = Token.INVALID_TYPE;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v2, Lantlr/LexerGrammar;

    iget-boolean v2, v2, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v2, :cond_7

    const-string v2, "setCommitToPath(false);"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    if-eqz v8, :cond_7

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v8}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v2, v9}, Lantlr/Grammar;->isDefined(Ljava/lang/String;)Z

    move-result v2

    const-string v9, " does not exist in this lexer"

    const-string v10, "Filter rule "

    if-nez v2, :cond_4

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    :goto_3
    invoke-virtual {v3, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_4
    invoke-virtual {v3, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_5

    :cond_4
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v8}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v2, v11}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v2

    check-cast v2, Lantlr/RuleSymbol;

    invoke-virtual {v2}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v11

    if-nez v11, :cond_5

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    goto :goto_3

    :cond_5
    iget-object v2, v2, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_6

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, " must be protected"

    goto :goto_4

    :cond_6
    :goto_5
    const-string v2, "int _m;"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "_m = mark();"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    const-string v2, "resetText();"

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "try     // for char stream error handling"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "try     // for lexical error handling"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v4

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    move v3, v0

    :goto_6
    invoke-virtual {v1}, Lantlr/RuleBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v9

    invoke-virtual {v9}, Lantlr/collections/impl/Vector;->size()I

    move-result v9

    if-ge v3, v9, :cond_9

    invoke-virtual {v1, v3}, Lantlr/RuleBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v9

    iget-object v10, v9, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v10, v10, v4

    invoke-virtual {v10}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v10

    if-eqz v10, :cond_8

    iget-object v9, v9, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    check-cast v9, Lantlr/RuleRefElement;

    iget-object v9, v9, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-static {v9}, Lantlr/CodeGenerator;->decodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v9

    iget-object v10, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string v12, "public lexical rule "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, " is optional (can match \"nothing\")"

    invoke-virtual {v11, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v10, v9}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    :cond_8
    add-int/lit8 v3, v3, 0x1

    goto :goto_6

    :cond_9
    const-string v3, "line.separator"

    invoke-static {v3}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v1, v0}, Lantlr/CSharpCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CSharpBlockFinishingInfo;

    move-result-object v0

    const-string v9, "if (cached_LA1==EOF_CHAR) { uponEOF(); returnToken_ = makeToken(Token.EOF_TYPE); }"

    const-string v10, "\t\t\t\t"

    invoke-static {v9, v3, v10}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v9

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v10, Lantlr/LexerGrammar;

    iget-boolean v10, v10, Lantlr/LexerGrammar;->filterMode:Z

    const-string v11, "(false);}"

    if-eqz v10, :cond_b

    const-string v10, "\t\t\t\t}"

    const-string v12, "\t\t\t\t\tgoto tryAgain;"

    if-nez v8, :cond_a

    const-string v3, "\t\t\t\telse"

    invoke-static {v9, v3}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    const-string v9, "\t\t\t\t{"

    invoke-static {v3, v9}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    const-string v9, "\t\t\t\t\tconsume();"

    invoke-static {v3, v9}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-static {v3, v12}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-static {v3, v10}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    goto :goto_7

    :cond_a
    new-instance v13, Ljava/lang/StringBuilder;

    invoke-direct {v13}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v13, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t\telse"

    invoke-virtual {v13, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t\t{"

    invoke-virtual {v13, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v14, "\t\t\t\t\tcommit();"

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v14, "\t\t\t\t\ttry {m"

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v14, "\t\t\t\t\tcatch(RecognitionException e)"

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t\t\t// catastrophic failure"

    invoke-virtual {v13, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t\t\treportError(e);"

    invoke-virtual {v13, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t\t\tconsume();"

    invoke-virtual {v13, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t\t}"

    invoke-virtual {v13, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    goto :goto_7

    :cond_b
    const-string v3, "else {"

    invoke-static {v9, v3}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v9, p0, Lantlr/CSharpCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-static {v3, v9, v7}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    :goto_7
    invoke-direct {p0, v0, v3}, Lantlr/CSharpCodeGenerator;->genBlockFinish(Lantlr/CSharpBlockFinishingInfo;Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-boolean v0, v0, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v0, :cond_c

    if-eqz v8, :cond_c

    const-string v0, "commit();"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    const-string v0, "if ( null==returnToken_ ) goto tryAgain; // found SKIP token"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "_ttype = returnToken_.Type;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    invoke-virtual {v0}, Lantlr/LexerGrammar;->getTestLiterals()Z

    move-result v0

    if-eqz v0, :cond_d

    invoke-direct {p0}, Lantlr/CSharpCodeGenerator;->genLiteralsTest()V

    :cond_d
    const-string v0, "returnToken_.Type = _ttype;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "return returnToken_;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "catch (RecognitionException e) {"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-boolean v0, v0, Lantlr/LexerGrammar;->filterMode:Z

    const-string v3, "consume();"

    if-eqz v0, :cond_f

    const-string v0, "if (!getCommitToPath())"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    if-nez v8, :cond_e

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "goto tryAgain;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_8

    :cond_e
    const-string v0, "rewind(_m);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "try {m"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "catch(RecognitionException ee) {"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t// horrendous failure: error in filter rule"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\treportError(ee);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\tconsume();"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "else"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    :goto_8
    invoke-virtual {v1}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v0

    if-eqz v0, :cond_10

    invoke-virtual {p0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "reportError(e);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_9

    :cond_10
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "throw new TokenStreamRecognitionException(e);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :goto_9
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "catch (CharStreamException cse) {"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\tif ( cse is CharStreamIOException ) {"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t\tthrow new TokenStreamIOException(((CharStreamIOException)cse).io);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t}"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\telse {"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t\tthrow new TokenStreamException(cse.Message);"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t}"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto/16 :goto_2
.end method

.method public genRule(Lantlr/RuleSymbol;ZILantlr/TokenManager;)V
    .locals 19

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move/from16 v2, p3

    const/4 v3, 0x1

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    iget-boolean v4, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

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

    iget-object v0, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v2, "undefined rule: "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-void

    :cond_1
    invoke-virtual/range {p1 .. p1}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v4

    iput-object v4, v0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    iput-object v6, v0, Lantlr/CSharpCodeGenerator;->currentASTResult:Ljava/lang/String;

    iget-object v6, v0, Lantlr/CSharpCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {v6}, Ljava/util/Hashtable;->clear()V

    iget-boolean v6, v0, Lantlr/CSharpCodeGenerator;->genAST:Z

    const/4 v7, 0x0

    if-eqz v6, :cond_2

    invoke-virtual {v4}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v8

    if-eqz v8, :cond_2

    move v8, v3

    goto :goto_0

    :cond_2
    move v8, v7

    :goto_0
    iput-boolean v8, v0, Lantlr/CSharpCodeGenerator;->genAST:Z

    invoke-virtual {v4}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v8

    iput-boolean v8, v0, Lantlr/CSharpCodeGenerator;->saveText:Z

    iget-object v8, v1, Lantlr/RuleSymbol;->comment:Ljava/lang/String;

    if-eqz v8, :cond_3

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    :cond_3
    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, v1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, " "

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v8, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v8, :cond_4

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v10, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v4}, Lantlr/RuleBlock;->getLine()I

    move-result v11

    invoke-virtual {v4}, Lantlr/RuleBlock;->getColumn()I

    move-result v12

    invoke-virtual {v0, v10, v11, v12}, Lantlr/CodeGenerator;->extractTypeOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    goto :goto_1

    :cond_4
    const-string v8, "void "

    :goto_1
    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v10, "("

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {v8}, Ljava/lang/String;->length()I

    move-result v8

    if-eqz v8, :cond_5

    iget-object v8, v4, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz v8, :cond_5

    const-string v8, ","

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_5
    iget-object v8, v4, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    const-string v10, ""

    if-eqz v8, :cond_6

    invoke-virtual {v0, v10}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget v8, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, v3

    iput v8, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v8, v4, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v8, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v8, v3

    iput v8, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_2

    :cond_6
    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :goto_2
    const-string v5, " //throws "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v8, v5, Lantlr/ParserGrammar;

    if-eqz v8, :cond_7

    const-string v5, ", TokenStreamException"

    :goto_3
    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_4

    :cond_7
    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_8

    const-string v5, ", CharStreamException, TokenStreamException"

    goto :goto_3

    :cond_8
    :goto_4
    iget-object v5, v4, Lantlr/RuleBlock;->throwsSpec:Ljava/lang/String;

    if-eqz v5, :cond_a

    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v5, v5, Lantlr/LexerGrammar;

    if-eqz v5, :cond_9

    iget-object v5, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v8, "user-defined throws spec not allowed (yet) for lexer rule "

    invoke-static {v8}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    iget-object v11, v4, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    invoke-virtual {v8, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v5, v8}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_5

    :cond_9
    const-string v5, ", "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget-object v8, v4, Lantlr/RuleBlock;->throwsSpec:Ljava/lang/String;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_a
    :goto_5
    invoke-virtual {v0, v10}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    const-string v5, "{"

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    iget v8, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, v3

    iput v8, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v8, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    const-string v11, ";"

    if-eqz v8, :cond_b

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v12, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-static {v8, v12, v11, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_b
    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->commonLocalVars:Ljava/lang/String;

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v8, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v12, v8, Lantlr/Grammar;->traceRules:Z

    const-string v13, "\",_t);"

    const-string v14, "\");"

    if-eqz v12, :cond_e

    instance-of v8, v8, Lantlr/TreeWalkerGrammar;

    const-string v12, "traceIn(\""

    if-eqz v8, :cond_d

    iget-boolean v8, v0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v8, :cond_c

    invoke-static {v12}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v8, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v12, "\",(AST)_t);"

    invoke-virtual {v8, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_6

    :cond_c
    invoke-static {v12}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v8, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_6

    :cond_d
    invoke-static {v12}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v8, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_6
    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_e
    iget-object v8, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v8, v8, Lantlr/LexerGrammar;

    if-eqz v8, :cond_10

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v8

    const-string v12, "mEOF"

    invoke-virtual {v8, v12}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v8

    if-eqz v8, :cond_f

    const-string v8, "_ttype = Token.EOF_TYPE;"

    goto :goto_7

    :cond_f
    const-string v8, "_ttype = "

    invoke-static {v8}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v12, v3}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v8, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    :goto_7
    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput v3, v0, Lantlr/CSharpCodeGenerator;->blockNestingLevel:I

    iput v7, v0, Lantlr/CSharpCodeGenerator;->saveIndexCreateLevel:I

    :cond_10
    iget-object v8, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v12, v8, Lantlr/Grammar;->debuggingOutput:Z

    const-string v15, ",_ttype);"

    const-string v7, ",0);"

    if-eqz v12, :cond_12

    instance-of v12, v8, Lantlr/ParserGrammar;

    const-string v3, "fireEnterRule("

    if-eqz v12, :cond_11

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_8
    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_9

    :cond_11
    instance-of v8, v8, Lantlr/LexerGrammar;

    if-eqz v8, :cond_12

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_8

    :cond_12
    :goto_9
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v8, v3, Lantlr/Grammar;->debuggingOutput:Z

    if-nez v8, :cond_13

    iget-boolean v3, v3, Lantlr/Grammar;->traceRules:Z

    if-eqz v3, :cond_14

    :cond_13
    const-string v3, "try { // debugging"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v8, 0x1

    add-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_14
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/TreeWalkerGrammar;

    if-eqz v3, :cond_15

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "_AST_in = ("

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v12, ")_t;"

    invoke-static {v3, v8, v12, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    :cond_15
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->buildAST:Z

    if-eqz v3, :cond_16

    const-string v3, "returnAST = null;"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "ASTPair currentAST = new ASTPair();"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "_AST = null;"

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_16
    invoke-virtual {v0, v4}, Lantlr/CSharpCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {v0, v4}, Lantlr/CSharpCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    invoke-virtual {v0, v10}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v4, v10}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object v3

    if-nez v3, :cond_18

    invoke-virtual {v4}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v8

    if-eqz v8, :cond_17

    goto :goto_a

    :cond_17
    const/4 v9, 0x1

    goto :goto_b

    :cond_18
    :goto_a
    const-string v8, "try {      // for error handling"

    invoke-virtual {v0, v8}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v8, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v9, 0x1

    add-int/2addr v8, v9

    iput v8, v0, Lantlr/CodeGenerator;->tabs:I

    :goto_b
    iget-object v8, v4, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v8}, Lantlr/collections/impl/Vector;->size()I

    move-result v8

    if-ne v8, v9, :cond_1b

    const/4 v8, 0x0

    invoke-virtual {v4, v8}, Lantlr/RuleBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v8

    iget-object v9, v8, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v9, :cond_19

    iget-object v12, v0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget v12, v12, Lantlr/GrammarElement;->line:I

    invoke-virtual {v0, v9, v12}, Lantlr/CSharpCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    :cond_19
    iget-object v9, v8, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v9, :cond_1a

    iget-object v9, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v12, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v12}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v12

    move/from16 v16, v6

    iget-object v6, v8, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v6}, Lantlr/GrammarElement;->getLine()I

    move-result v6

    move-object/from16 v17, v10

    iget-object v10, v8, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v10}, Lantlr/GrammarElement;->getColumn()I

    move-result v10

    move-object/from16 v18, v14

    const-string v14, "Syntactic predicate ignored for single alternative"

    invoke-virtual {v9, v14, v12, v6, v10}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_c

    :cond_1a
    move/from16 v16, v6

    move-object/from16 v17, v10

    move-object/from16 v18, v14

    :goto_c
    invoke-virtual {v0, v8, v4}, Lantlr/CSharpCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    goto :goto_d

    :cond_1b
    move/from16 v16, v6

    move-object/from16 v17, v10

    move-object/from16 v18, v14

    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v6, v6, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v6, v4}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    const/4 v6, 0x0

    invoke-virtual {v0, v4, v6}, Lantlr/CSharpCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CSharpBlockFinishingInfo;

    move-result-object v6

    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-direct {v0, v6, v8}, Lantlr/CSharpCodeGenerator;->genBlockFinish(Lantlr/CSharpBlockFinishingInfo;Ljava/lang/String;)V

    :goto_d
    const-string v6, "}"

    if-nez v3, :cond_1c

    invoke-virtual {v4}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v8

    if-eqz v8, :cond_1d

    :cond_1c
    iget v8, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v9, 0x1

    sub-int/2addr v8, v9

    iput v8, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1d
    if-eqz v3, :cond_1e

    invoke-direct {v0, v3}, Lantlr/CSharpCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    goto/16 :goto_f

    :cond_1e
    invoke-virtual {v4}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v3

    if-eqz v3, :cond_22

    const-string v3, "catch ("

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v8, v0, Lantlr/CSharpCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, " ex)"

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v8, 0x1

    add-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v3, :cond_1f

    const-string v3, "if (0 == inputState.guessing)"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_1f
    const-string v3, "reportError(ex);"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v3, Lantlr/TreeWalkerGrammar;

    if-nez v9, :cond_20

    iget-object v3, v3, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v9, v4, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    invoke-interface {v3, v8, v9}, Lantlr/LLkGrammarAnalyzer;->FOLLOW(ILantlr/RuleEndElement;)Lantlr/Lookahead;

    move-result-object v3

    iget-object v3, v3, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->markBitsetForGen(Lantlr/collections/impl/BitSet;)I

    move-result v3

    invoke-virtual {v0, v3}, Lantlr/CSharpCodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v3

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "recover(ex,"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ");"

    invoke-virtual {v8, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const/4 v8, 0x1

    goto :goto_e

    :cond_20
    const-string v3, "if (null != _t)"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v8, 0x1

    add-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "_t = _t.getNextSibling();"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_e
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v3, :cond_21

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "else"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "throw ex;"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_21
    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v8

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_22
    :goto_f
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->buildAST:Z

    if-eqz v3, :cond_23

    const-string v3, "returnAST = "

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "_AST;"

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_23
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/TreeWalkerGrammar;

    if-eqz v3, :cond_24

    const-string v3, "retTree_ = _t;"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_24
    invoke-virtual {v4}, Lantlr/RuleBlock;->getTestLiterals()Z

    move-result v3

    if-eqz v3, :cond_26

    iget-object v3, v1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    const-string v8, "protected"

    invoke-virtual {v3, v8}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_25

    invoke-direct/range {p0 .. p0}, Lantlr/CSharpCodeGenerator;->genLiteralsTestForPartialToken()V

    goto :goto_10

    :cond_25
    invoke-direct/range {p0 .. p0}, Lantlr/CSharpCodeGenerator;->genLiteralsTest()V

    :cond_26
    :goto_10
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/LexerGrammar;

    if-eqz v3, :cond_27

    const-string v3, "if (_createToken && (null == _token) && (_ttype != Token.SKIP))"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v5}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    add-int/2addr v3, v5

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "_token = makeToken(_ttype);"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "_token.setText(text.ToString(_begin, text.Length-_begin));"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v5

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "returnToken_ = _token;"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_27
    iget-object v3, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v3, :cond_28

    const-string v3, "return "

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v5, v4, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v4}, Lantlr/RuleBlock;->getLine()I

    move-result v8

    invoke-virtual {v4}, Lantlr/RuleBlock;->getColumn()I

    move-result v4

    invoke-virtual {v0, v5, v8, v4}, Lantlr/CodeGenerator;->extractIdOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_28
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v3, Lantlr/Grammar;->debuggingOutput:Z

    if-nez v4, :cond_2a

    iget-boolean v3, v3, Lantlr/Grammar;->traceRules:Z

    if-eqz v3, :cond_29

    goto :goto_11

    :cond_29
    const/4 v2, 0x1

    goto/16 :goto_15

    :cond_2a
    :goto_11
    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    sub-int/2addr v3, v4

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "finally"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "{ // debugging"

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v4

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v3, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v4, :cond_2c

    instance-of v4, v3, Lantlr/ParserGrammar;

    const-string v5, "fireExitRule("

    if-eqz v4, :cond_2b

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_12
    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_13

    :cond_2b
    instance-of v3, v3, Lantlr/LexerGrammar;

    if-eqz v3, :cond_2c

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_12

    :cond_2c
    :goto_13
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v2, Lantlr/Grammar;->traceRules:Z

    if-eqz v3, :cond_2e

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_2d

    const-string v2, "traceOut(\""

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_14

    :cond_2d
    const-string v2, "traceOut(\""

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-object/from16 v1, v18

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_14
    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_2e
    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    sub-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :goto_15
    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v6}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    move-object/from16 v1, v17

    invoke-virtual {v0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    move/from16 v1, v16

    iput-boolean v1, v0, Lantlr/CSharpCodeGenerator;->genAST:Z

    return-void
.end method

.method public genSemPred(Ljava/lang/String;I)V
    .locals 2

    new-instance v0, Lantlr/ActionTransInfo;

    invoke-direct {v0}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v1, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, p1, p2, v1, v0}, Lantlr/CSharpCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

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

    invoke-virtual {p0, p2}, Lantlr/CSharpCodeGenerator;->addSemPred(Ljava/lang/String;)I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    :cond_1
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "if (!("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "))"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "  throw new SemanticException(\""

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "\");"

    invoke-static {p1, p2, v0, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CSharpCodeGenerator;)V

    return-void
.end method

.method public genSemPredMap()V
    .locals 3

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    const-string v1, "private string[] _semPredNames = {"

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

    const-string v0, "};"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V
    .locals 6

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
    const-string v0, "bool synPredMatched"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, " = false;"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_1

    const-string v0, "if (_t==null) _t=ASTNULL;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "if ("

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "{"

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_2

    const-string v0, "AST __t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v3, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, " = _t;"

    goto :goto_0

    :cond_2
    const-string v0, "int _m"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v3, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, " = mark();"

    :goto_0
    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "synPredMatched"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " = true;"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "inputState.guessing++;"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v3, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v4, :cond_4

    instance-of v4, v3, Lantlr/ParserGrammar;

    if-nez v4, :cond_3

    instance-of v3, v3, Lantlr/LexerGrammar;

    if-eqz v3, :cond_4

    :cond_3
    const-string v3, "fireSyntacticPredicateStarted();"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    iget v3, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    const-string v3, "try {"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->gen(Lantlr/AlternativeBlock;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, -0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "}"

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "catch ("

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/CSharpCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v4, v4, 0x1

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v4, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_5

    const-string v0, "_t = __t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ";"

    goto :goto_1

    :cond_5
    const-string v0, "rewind(_m"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ");"

    :goto_1
    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "inputState.guessing--;"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v0, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_7

    instance-of v2, v0, Lantlr/ParserGrammar;

    if-nez v2, :cond_6

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_7

    :cond_6
    const-string v0, "if (synPredMatched"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "  fireSyntacticPredicateSucceeded();"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "else"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "  fireSyntacticPredicateFailed();"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    iget v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CSharpCodeGenerator;->syntacticPredLevel:I

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "if ( synPredMatched"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget p1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, " )"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genTokenDefinitions(Lantlr/TokenManager;)V
    .locals 8

    invoke-interface {p1}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v0

    const-string v1, "public const int EOF = 1;"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "public const int NULL_TREE_LOOKAHEAD = 3;"

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

    const-string v4, ";"

    const-string v5, "public const int "

    const-string v6, " = "

    if-eqz v3, :cond_3

    invoke-interface {p1, v2}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v3

    check-cast v3, Lantlr/StringLiteralSymbol;

    if-nez v3, :cond_0

    iget-object v3, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "String literal "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " not in symbol table"

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v3, v2}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto/16 :goto_3

    :cond_0
    iget-object v7, v3, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    if-eqz v7, :cond_1

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v3, v3, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    goto :goto_2

    :cond_1
    invoke-direct {p0, v2}, Lantlr/CSharpCodeGenerator;->mangleLiteral(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    if-eqz v7, :cond_2

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iput-object v7, v3, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    goto :goto_3

    :cond_2
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "// "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    goto :goto_1

    :cond_3
    const-string v3, "<"

    invoke-virtual {v2, v3}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v3

    if-nez v3, :cond_4

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_1
    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    :goto_2
    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    :goto_3
    add-int/lit8 v1, v1, 0x1

    goto/16 :goto_0

    :cond_5
    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genTokenStrings()V
    .locals 7

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "public static readonly string[] tokenNames_ = new string[] {"

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v1}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v1

    const/4 v2, 0x0

    :goto_0
    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v3

    if-ge v2, v3, :cond_4

    invoke-virtual {v1, v2}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    const-string v4, "<"

    if-nez v3, :cond_0

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ">"

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    :cond_0
    const-string v5, "\""

    invoke-virtual {v3, v5}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v6

    if-nez v6, :cond_1

    invoke-virtual {v3, v4}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v4

    if-nez v4, :cond_1

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v4, v3}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v4

    if-eqz v4, :cond_2

    invoke-virtual {v4}, Lantlr/TokenSymbol;->getParaphrase()Ljava/lang/String;

    move-result-object v6

    if-eqz v6, :cond_2

    invoke-virtual {v4}, Lantlr/TokenSymbol;->getParaphrase()Ljava/lang/String;

    move-result-object v3

    goto :goto_1

    :cond_1
    invoke-virtual {v3, v5}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v4

    if-eqz v4, :cond_2

    :goto_1
    invoke-static {v3, v5, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    :cond_2
    iget-object v4, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v4, v3}, Lantlr/CharFormatter;->literalString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v3

    add-int/lit8 v3, v3, -0x1

    if-eq v2, v3, :cond_3

    const-string v3, ","

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->_print(Ljava/lang/String;)V

    :cond_3
    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->_println(Ljava/lang/String;)V

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_4
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "};"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genTokenTypes(Lantlr/TokenManager;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CSharpCodeGenerator;->setupOutput(Ljava/lang/String;)V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->genHeader()V

    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    const-string v1, ""

    invoke-virtual {v0, v1}, Lantlr/DefineGrammarSymbols;->getHeaderAction(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    sget-object v0, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    if-eqz v0, :cond_0

    iget-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v0, v1}, Lantlr/CSharpNameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_0
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "public class "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "{"

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->genTokenDefinitions(Lantlr/TokenManager;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    sget-object p1, Lantlr/CSharpCodeGenerator;->nameSpace:Lantlr/CSharpNameSpace;

    if-eqz p1, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/CSharpNameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_1
    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p0}, Lantlr/CSharpCodeGenerator;->exitIfError()V

    return-void
.end method

.method public getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;
    .locals 5

    const-string v0, "astFactory.create("

    const-string v1, ")"

    invoke-static {v0, p2, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    if-nez p1, :cond_0

    invoke-virtual {p0, p2}, Lantlr/CSharpCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v1

    const-string v2, ") "

    const-string v3, "("

    if-eqz v1, :cond_3

    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-interface {p0, v1}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p0

    if-eqz p0, :cond_2

    invoke-virtual {p0}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v4

    if-eq v1, v4, :cond_1

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_4

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p0}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object p0

    goto :goto_1

    :cond_2
    :goto_0
    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ") astFactory.create("

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ", \""

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "\")"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    goto :goto_2

    :cond_3
    iget-boolean p1, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz p1, :cond_4

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    :goto_1
    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_4
    :goto_2
    return-object v0
.end method

.method public getASTCreateString(Lantlr/collections/impl/Vector;)Ljava/lang/String;
    .locals 3

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

    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, ") astFactory.make("

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const/4 p0, 0x0

    invoke-virtual {p1, p0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/Object;)Ljava/lang/StringBuffer;

    const/4 p0, 0x1

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge p0, v1, :cond_1

    const-string v1, ", "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p1, p0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 p0, p0, 0x1

    goto :goto_0

    :cond_1
    const-string p0, ")"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getASTCreateString(Ljava/lang/String;)Ljava/lang/String;
    .locals 5

    if-nez p1, :cond_0

    const-string p1, ""

    :cond_0
    const-string v0, "astFactory.create("

    const-string v1, ")"

    invoke-static {v0, p1, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    const/16 v1, 0x2c

    invoke-virtual {p1, v1}, Ljava/lang/String;->indexOf(I)I

    move-result v2

    const/4 v3, -0x1

    if-eq v2, v3, :cond_1

    const/4 v3, 0x0

    invoke-virtual {p1, v3, v2}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    add-int/lit8 v2, v2, 0x1

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v4

    invoke-virtual {p1, v2, v4}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1, v1}, Ljava/lang/String;->indexOf(I)I

    move-object p1, v3

    :cond_1
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v1, p1}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p1

    const-string v1, ") "

    const-string v2, "("

    if-eqz p1, :cond_2

    invoke-virtual {p1}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_2

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_2
    iget-boolean p1, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz p1, :cond_3

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_3
    :goto_0
    return-object v0
.end method

.method public getBitsetName(I)Ljava/lang/String;
    .locals 1

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "tokenSet_"

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, "_"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

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

    invoke-virtual {p0, p1, v0}, Lantlr/CSharpCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

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
    invoke-virtual {p0, v1, v3}, Lantlr/CSharpCodeGenerator;->getLookaheadTestTerm(ILantlr/collections/impl/BitSet;)Ljava/lang/String;

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

    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object v1

    invoke-static {v1}, Lantlr/CodeGenerator;->elementsAreRange([I)Z

    move-result v2

    if-eqz v2, :cond_0

    invoke-virtual {p0, p1, v1}, Lantlr/CSharpCodeGenerator;->getRangeExpression(I[I)Ljava/lang/String;

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

    invoke-virtual {p0, p1}, Lantlr/CSharpCodeGenerator;->getBitsetName(I)Ljava/lang/String;

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

    invoke-direct {p0, v2}, Lantlr/CSharpCodeGenerator;->getValueString(I)Ljava/lang/String;

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

    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " >= "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, v0}, Lantlr/CSharpCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " && "

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, p1}, Lantlr/CSharpCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " <= "

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, p2}, Lantlr/CSharpCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object p0

    const-string p1, ")"

    invoke-static {v1, p0, p1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getTokenTypesClassName()Ljava/lang/String;
    .locals 2

    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    new-instance v0, Ljava/lang/String;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-interface {p0}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object p0, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Ljava/lang/String;-><init>(Ljava/lang/String;)V

    return-object v0
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

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

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
    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    const-string v1, "_AST"

    if-ge v4, v0, :cond_5

    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

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
    iget-object v0, p0, Lantlr/CSharpCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {v0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    if-eqz v0, :cond_9

    sget-object p2, Lantlr/CSharpCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    const/4 v1, 0x0

    const-string v4, " in rule "

    const-string v5, "Ambiguous reference to AST element "

    if-ne v0, p2, :cond_6

    :goto_2
    iget-object p2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v5, p1, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-object v1

    :cond_6
    iget-object p2, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

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
    iget-object p0, p0, Lantlr/CSharpCodeGenerator;->currentRule:Lantlr/RuleBlock;

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
    new-instance v1, Lantlr/actions/csharp/ActionLexer;

    invoke-direct {v1, p1, p3, p0, p4}, Lantlr/actions/csharp/ActionLexer;-><init>(Ljava/lang/String;Lantlr/RuleBlock;Lantlr/CodeGenerator;Lantlr/ActionTransInfo;)V

    invoke-virtual {v1, p2}, Lantlr/actions/csharp/ActionLexer;->setLineOffset(I)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v1, p2}, Lantlr/CharScanner;->setFilename(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1, p2}, Lantlr/actions/csharp/ActionLexer;->setTool(Lantlr/Tool;)V

    const/4 p2, 0x1

    :try_start_0
    invoke-virtual {v1, p2}, Lantlr/actions/csharp/ActionLexer;->mACTION(Z)V

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

    invoke-virtual {v1, p0}, Lantlr/actions/csharp/ActionLexer;->reportError(Lantlr/RecognitionException;)V

    return-object p1

    :cond_6
    :goto_1
    const/4 p0, 0x0

    return-object p0
.end method

.method public processStringForASTConstructor(Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    iget-boolean v0, p0, Lantlr/CSharpCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    if-nez v1, :cond_0

    instance-of v0, v0, Lantlr/ParserGrammar;

    if-eqz v0, :cond_1

    :cond_0
    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0, p1}, Lantlr/TokenManager;->tokenDefined(Ljava/lang/String;)Z

    move-result p0

    if-nez p0, :cond_1

    const-string p0, "(AST)"

    invoke-static {p0, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_1
    return-object p1
.end method

.method public setupOutput(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ".cs"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public split(Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;
    .locals 1

    new-instance p0, Ljava/util/StringTokenizer;

    invoke-direct {p0, p1, p2}, Ljava/util/StringTokenizer;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-virtual {p0}, Ljava/util/StringTokenizer;->countTokens()I

    move-result p1

    new-array p1, p1, [Ljava/lang/String;

    const/4 p2, 0x0

    :goto_0
    invoke-virtual {p0}, Ljava/util/StringTokenizer;->hasMoreTokens()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Ljava/util/StringTokenizer;->nextToken()Ljava/lang/String;

    move-result-object v0

    aput-object v0, p1, p2

    add-int/lit8 p2, p2, 0x1

    goto :goto_0

    :cond_0
    return-object p1
.end method
