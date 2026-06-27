.class public Lantlr/CppCodeGenerator;
.super Lantlr/CodeGenerator;
.source ""


# static fields
.field public static final NONUNIQUE:Ljava/lang/String;

.field public static final caseSizeThreshold:I = 0x7f

.field public static nameSpace:Lantlr/NameSpace; = null

.field public static namespaceAntlr:Ljava/lang/String; = null

.field public static namespaceStd:Ljava/lang/String; = null

.field public static final postIncludeCpp:Ljava/lang/String; = "post_include_cpp"

.field public static final postIncludeHpp:Ljava/lang/String; = "post_include_hpp"

.field public static final preIncludeCpp:Ljava/lang/String; = "pre_include_cpp"

.field public static final preIncludeHpp:Ljava/lang/String; = "pre_include_hpp"


# instance fields
.field public DEBUG_CPP_CODE_GENERATOR:Z

.field public astTypes:Lantlr/collections/impl/Vector;

.field public astVarNumber:I

.field public commonExtraArgs:Ljava/lang/String;

.field public commonExtraParams:Ljava/lang/String;

.field public commonLocalVars:Ljava/lang/String;

.field public currentASTResult:Ljava/lang/String;

.field public currentRule:Lantlr/RuleBlock;

.field public declaredASTVariables:Ljava/util/Hashtable;

.field public exceptionThrown:Ljava/lang/String;

.field public genAST:Z

.field public genHashLines:Z

.field public labeledElementASTInit:Ljava/lang/String;

.field public labeledElementASTType:Ljava/lang/String;

.field public labeledElementInit:Ljava/lang/String;

.field public labeledElementType:Ljava/lang/String;

.field public lt1Value:Ljava/lang/String;

.field public noConstructors:Z

.field public outputFile:Ljava/lang/String;

.field public outputLine:I

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

    sput-object v0, Lantlr/CppCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    const-string v0, "ANTLR_USE_NAMESPACE(std)"

    sput-object v0, Lantlr/CppCodeGenerator;->namespaceStd:Ljava/lang/String;

    const-string v0, "ANTLR_USE_NAMESPACE(antlr)"

    sput-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const/4 v0, 0x0

    sput-object v0, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    return-void
.end method

.method public constructor <init>()V
    .locals 2

    invoke-direct {p0}, Lantlr/CodeGenerator;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    iput v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    iput-boolean v1, p0, Lantlr/CppCodeGenerator;->genHashLines:Z

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/CppCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    iput v1, p0, Lantlr/CppCodeGenerator;->astVarNumber:I

    new-instance v0, Lantlr/CppCharFormatter;

    invoke-direct {v0}, Lantlr/CppCharFormatter;-><init>()V

    iput-object v0, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    return-void
.end method

.method private GenRuleInvocation(Lantlr/RuleRefElement;)V
    .locals 5

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

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
    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CppCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    if-nez v0, :cond_1

    iget-object v0, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v0, :cond_2

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    :cond_2
    iget-object v0, p0, Lantlr/CppCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CppCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    if-eqz v0, :cond_3

    iget-object v0, p1, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    if-eqz v0, :cond_3

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

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

    iget v3, p1, Lantlr/GrammarElement;->line:I

    iget-object v4, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v2, v3, v4, v1}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

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

    iget-object v4, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v4}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " on line "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_5
    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-nez v0, :cond_6

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Rule \'"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    const-string v3, "\' accepts no arguments"

    invoke-static {v1, v2, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v3

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result p1

    invoke-virtual {v0, v1, v2, v3, p1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_6
    const-string p1, ");"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_7

    const-string p1, "_t = _retTree;"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    return-void
.end method

.method private charIsDigit(Ljava/lang/String;I)Z
    .locals 0

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result p0

    if-ge p2, p0, :cond_0

    invoke-virtual {p1, p2}, Ljava/lang/String;->charAt(I)C

    move-result p0

    invoke-static {p0}, Ljava/lang/Character;->isDigit(C)Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method private convertJavaToCppString(Ljava/lang/String;Z)Ljava/lang/String;
    .locals 17

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    new-instance v2, Ljava/lang/String;

    invoke-direct {v2}, Ljava/lang/String;-><init>()V

    const-string v3, "\""

    const-string v4, "\'"

    if-eqz p2, :cond_1

    invoke-virtual {v1, v4}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v5

    if-eqz v5, :cond_0

    invoke-virtual {v1, v4}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result v5

    if-nez v5, :cond_3

    :cond_0
    iget-object v5, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "Invalid character literal: \'"

    goto :goto_0

    :cond_1
    invoke-virtual {v1, v3}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v5

    if-eqz v5, :cond_2

    invoke-virtual {v1, v3}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result v5

    if-nez v5, :cond_3

    :cond_2
    iget-object v5, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "Invalid character string: \'"

    :goto_0
    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v6}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_3
    invoke-virtual/range {p1 .. p1}, Ljava/lang/String;->length()I

    move-result v5

    const/4 v6, 0x1

    sub-int/2addr v5, v6

    invoke-virtual {v1, v6, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v5

    iget-object v7, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v8, v7, Lantlr/LexerGrammar;

    const/16 v10, 0xff

    const-string v11, ""

    if-eqz v8, :cond_5

    check-cast v7, Lantlr/LexerGrammar;

    iget-object v7, v7, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {v7}, Lantlr/collections/impl/BitSet;->size()I

    move-result v7

    sub-int/2addr v7, v6

    if-le v7, v10, :cond_4

    const-string v11, "L"

    :cond_4
    move v12, v7

    const/4 v8, 0x0

    move-object v7, v2

    const/4 v2, 0x0

    goto :goto_1

    :cond_5
    move-object v7, v2

    move v12, v10

    const/4 v2, 0x0

    const/4 v8, 0x0

    :goto_1
    invoke-virtual {v5}, Ljava/lang/String;->length()I

    move-result v13

    if-ge v2, v13, :cond_1b

    invoke-virtual {v5, v2}, Ljava/lang/String;->charAt(I)C

    move-result v13

    const/16 v14, 0x5c

    if-ne v13, v14, :cond_13

    invoke-virtual {v5}, Ljava/lang/String;->length()I

    move-result v13

    add-int/lit8 v10, v2, 0x1

    const-string v6, "Invalid escape in char literal: \'"

    const-string v9, "\' looking at \'"

    if-ne v13, v10, :cond_6

    iget-object v13, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v6, v1, v9}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v15

    invoke-virtual {v5, v2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v15, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v15, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v15}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v13, v14}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_6
    invoke-virtual {v5, v10}, Ljava/lang/String;->charAt(I)C

    move-result v13

    const/16 v14, 0x22

    if-eq v13, v14, :cond_12

    const/16 v14, 0x27

    if-eq v13, v14, :cond_12

    const/16 v14, 0x5c

    if-eq v13, v14, :cond_12

    const/16 v14, 0x66

    if-eq v13, v14, :cond_11

    const/16 v14, 0x6e

    if-eq v13, v14, :cond_10

    const/16 v14, 0x72

    if-eq v13, v14, :cond_f

    const/16 v14, 0x61

    if-eq v13, v14, :cond_e

    const/16 v14, 0x62

    if-eq v13, v14, :cond_d

    const/16 v14, 0x74

    if-eq v13, v14, :cond_c

    const/16 v14, 0x75

    if-eq v13, v14, :cond_a

    packed-switch v13, :pswitch_data_0

    goto :goto_2

    :pswitch_0
    add-int/lit8 v6, v2, 0x2

    invoke-direct {v0, v5, v6}, Lantlr/CppCodeGenerator;->charIsDigit(Ljava/lang/String;I)Z

    move-result v8

    invoke-virtual {v5, v10}, Ljava/lang/String;->charAt(I)C

    if-eqz v8, :cond_7

    invoke-virtual {v5, v6}, Ljava/lang/String;->charAt(I)C

    add-int/lit8 v2, v2, 0x3

    goto :goto_2

    :cond_7
    move v2, v6

    goto :goto_2

    :pswitch_1
    add-int/lit8 v6, v2, 0x2

    invoke-direct {v0, v5, v6}, Lantlr/CppCodeGenerator;->charIsDigit(Ljava/lang/String;I)Z

    move-result v8

    if-eqz v8, :cond_9

    add-int/lit8 v8, v2, 0x3

    invoke-direct {v0, v5, v8}, Lantlr/CppCodeGenerator;->charIsDigit(Ljava/lang/String;I)Z

    move-result v9

    if-eqz v9, :cond_8

    invoke-virtual {v5, v10}, Ljava/lang/String;->charAt(I)C

    move-result v9

    add-int/lit8 v9, v9, -0x30

    const/16 v13, 0x8

    mul-int/2addr v9, v13

    mul-int/2addr v9, v13

    invoke-virtual {v5, v6}, Ljava/lang/String;->charAt(I)C

    move-result v6

    add-int/lit8 v6, v6, -0x30

    mul-int/2addr v6, v13

    add-int/2addr v6, v9

    invoke-virtual {v5, v8}, Ljava/lang/String;->charAt(I)C

    move-result v8

    add-int/lit8 v8, v8, -0x30

    add-int/2addr v8, v6

    add-int/lit8 v2, v2, 0x4

    goto/16 :goto_6

    :cond_8
    const/16 v13, 0x8

    invoke-virtual {v5, v10}, Ljava/lang/String;->charAt(I)C

    move-result v2

    add-int/lit8 v2, v2, -0x30

    mul-int/2addr v2, v13

    invoke-virtual {v5, v6}, Ljava/lang/String;->charAt(I)C

    move-result v6

    add-int/lit8 v6, v6, -0x30

    add-int/2addr v6, v2

    move v2, v8

    goto/16 :goto_4

    :cond_9
    invoke-virtual {v5, v10}, Ljava/lang/String;->charAt(I)C

    move-result v2

    add-int/lit8 v2, v2, -0x30

    goto/16 :goto_5

    :goto_2
    iget-object v6, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v8, "Unhandled escape in char literal: \'"

    invoke-static {v8, v1, v9}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v5, v2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v6, v8}, Lantlr/Tool;->error(Ljava/lang/String;)V

    const/4 v8, 0x0

    goto/16 :goto_6

    :cond_a
    add-int/lit8 v10, v2, 0x5

    invoke-virtual {v5}, Ljava/lang/String;->length()I

    move-result v13

    if-ge v10, v13, :cond_b

    add-int/lit8 v6, v2, 0x2

    invoke-virtual {v5, v6}, Ljava/lang/String;->charAt(I)C

    move-result v6

    const/16 v8, 0x10

    invoke-static {v6, v8}, Ljava/lang/Character;->digit(CI)I

    move-result v6

    mul-int/2addr v6, v8

    mul-int/2addr v6, v8

    mul-int/2addr v6, v8

    add-int/lit8 v9, v2, 0x3

    invoke-virtual {v5, v9}, Ljava/lang/String;->charAt(I)C

    move-result v9

    invoke-static {v9, v8}, Ljava/lang/Character;->digit(CI)I

    move-result v9

    mul-int/2addr v9, v8

    mul-int/2addr v9, v8

    add-int/2addr v9, v6

    add-int/lit8 v6, v2, 0x4

    invoke-virtual {v5, v6}, Ljava/lang/String;->charAt(I)C

    move-result v6

    invoke-static {v6, v8}, Ljava/lang/Character;->digit(CI)I

    move-result v6

    mul-int/2addr v6, v8

    add-int/2addr v6, v9

    invoke-virtual {v5, v10}, Ljava/lang/String;->charAt(I)C

    move-result v9

    invoke-static {v9, v8}, Ljava/lang/Character;->digit(CI)I

    move-result v9

    add-int/2addr v9, v6

    add-int/lit8 v2, v2, 0x6

    move v8, v9

    goto :goto_6

    :cond_b
    iget-object v10, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v6, v1, v9}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v5, v2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v10, v6}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_6

    :cond_c
    const/16 v6, 0x9

    goto :goto_3

    :cond_d
    const/16 v13, 0x8

    add-int/lit8 v2, v2, 0x2

    move v8, v13

    goto :goto_6

    :cond_e
    const/4 v6, 0x7

    goto :goto_3

    :cond_f
    const/16 v6, 0xd

    goto :goto_3

    :cond_10
    const/16 v6, 0xa

    goto :goto_3

    :cond_11
    const/16 v6, 0xc

    goto :goto_3

    :cond_12
    invoke-virtual {v5, v10}, Ljava/lang/String;->charAt(I)C

    move-result v6

    :goto_3
    add-int/lit8 v2, v2, 0x2

    :goto_4
    move v8, v6

    goto :goto_6

    :cond_13
    add-int/lit8 v6, v2, 0x1

    invoke-virtual {v5, v2}, Ljava/lang/String;->charAt(I)C

    move-result v2

    :goto_5
    move v8, v2

    move v2, v6

    :goto_6
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v6, v6, Lantlr/LexerGrammar;

    if-eqz v6, :cond_16

    if-le v8, v12, :cond_16

    const/16 v6, 0x20

    if-gt v6, v8, :cond_14

    const/16 v6, 0x7f

    if-ge v8, v6, :cond_14

    iget-object v6, v0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    const/4 v9, 0x1

    invoke-interface {v6, v8, v9}, Lantlr/CharFormatter;->escapeChar(IZ)Ljava/lang/String;

    move-result-object v6

    goto :goto_7

    :cond_14
    const-string v6, "0x"

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    const/16 v9, 0x10

    invoke-static {v8, v9}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    :goto_7
    iget-object v9, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v10, "Character out of range in "

    invoke-static {v10}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v10

    if-eqz p2, :cond_15

    const-string v13, "char literal"

    goto :goto_8

    :cond_15
    const-string v13, "string constant"

    :goto_8
    invoke-virtual {v10, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v13, ": \'"

    invoke-virtual {v10, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v9, v10}, Lantlr/Tool;->error(Ljava/lang/String;)V

    iget-object v9, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    const-string v13, "Vocabulary size: "

    invoke-virtual {v10, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v12}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v13, " Character "

    invoke-virtual {v10, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v9, v6}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_16
    if-eqz p2, :cond_1a

    invoke-virtual {v5}, Ljava/lang/String;->length()I

    move-result v6

    if-eq v2, v6, :cond_17

    iget-object v6, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "Invalid char literal: \'"

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_17
    const/16 v6, 0xff

    if-gt v12, v6, :cond_19

    if-gt v8, v6, :cond_18

    and-int/lit16 v7, v8, 0x80

    if-eqz v7, :cond_18

    const-string v7, "static_cast<unsigned char>(\'"

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-object v9, v0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    const/4 v10, 0x1

    invoke-interface {v9, v8, v10}, Lantlr/CharFormatter;->escapeChar(IZ)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\')"

    goto :goto_a

    :cond_18
    const/4 v10, 0x1

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    goto :goto_9

    :cond_19
    const/4 v10, 0x1

    const-string v7, "L\'"

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    :goto_9
    iget-object v9, v0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v9, v8, v10}, Lantlr/CharFormatter;->escapeChar(IZ)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_b

    :cond_1a
    const/16 v6, 0xff

    const/4 v10, 0x1

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-object v9, v0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v9, v8, v10}, Lantlr/CharFormatter;->escapeChar(IZ)Ljava/lang/String;

    move-result-object v9

    :goto_a
    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_b
    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    move/from16 v16, v10

    move v10, v6

    move/from16 v6, v16

    goto/16 :goto_1

    :cond_1b
    if-nez p2, :cond_1c

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    :cond_1c
    return-object v7

    nop

    :pswitch_data_0
    .packed-switch 0x30
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method private fixNameSpaceOption(Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    const-string p0, "\""

    invoke-static {p1, p0, p0}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result p1

    const/4 v0, 0x2

    if-le p1, v0, :cond_0

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result p1

    sub-int/2addr p1, v0

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v0

    invoke-virtual {p0, p1, v0}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p1

    const-string v0, "::"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_0

    invoke-static {p0, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    :cond_0
    return-object p0
.end method

.method private genBlockFinish(Lantlr/CppBlockFinishingInfo;Ljava/lang/String;)V
    .locals 1

    iget-boolean v0, p1, Lantlr/CppBlockFinishingInfo;->needAnErrorClause:Z

    if-eqz v0, :cond_2

    iget-boolean v0, p1, Lantlr/CppBlockFinishingInfo;->generatedAnIf:Z

    if-nez v0, :cond_0

    iget-boolean v0, p1, Lantlr/CppBlockFinishingInfo;->generatedSwitch:Z

    if-eqz v0, :cond_2

    :cond_0
    iget-boolean v0, p1, Lantlr/CppBlockFinishingInfo;->generatedAnIf:Z

    if-eqz v0, :cond_1

    const-string v0, "else {"

    goto :goto_0

    :cond_1
    const-string v0, "{"

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p2, "}"

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_2
    iget-object p1, p1, Lantlr/CppBlockFinishingInfo;->postscript:Ljava/lang/String;

    if-eqz p1, :cond_3

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method private genElementAST(Lantlr/AlternativeElement;)V
    .locals 11

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    const-string v2, " "

    const-string v3, "_AST"

    const-string v4, "tmp"

    const-string v5, "_in = "

    const-string v6, ";"

    const/4 v7, 0x1

    if-eqz v1, :cond_1

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-nez v0, :cond_1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v4, p0, Lantlr/CppCodeGenerator;->astVarNumber:I

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    iget v3, p0, Lantlr/CppCodeGenerator;->astVarNumber:I

    add-int/2addr v3, v7

    iput v3, p0, Lantlr/CppCodeGenerator;->astVarNumber:I

    invoke-direct {p0, p1, v1}, Lantlr/CppCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {p1, v0, v6, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_0
    return-void

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_15

    iget v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_15

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

    const/4 v1, 0x3

    const/4 v8, 0x0

    if-eqz v0, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-eq v0, v1, :cond_3

    :cond_2
    move v0, v7

    goto :goto_0

    :cond_3
    move v0, v8

    :goto_0
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v9

    if-eq v9, v1, :cond_4

    instance-of v1, p1, Lantlr/TokenRefElement;

    if-eqz v1, :cond_4

    move v0, v7

    :cond_4
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v1, :cond_5

    if-eqz v0, :cond_5

    move v8, v7

    :cond_5
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_6

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    goto :goto_1

    :cond_6
    iget-object v1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget v9, p0, Lantlr/CppCodeGenerator;->astVarNumber:I

    invoke-virtual {v4, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    iget v9, p0, Lantlr/CppCodeGenerator;->astVarNumber:I

    add-int/2addr v9, v7

    iput v9, p0, Lantlr/CppCodeGenerator;->astVarNumber:I

    :goto_1
    if-eqz v0, :cond_8

    instance-of v9, p1, Lantlr/GrammarAtom;

    if-eqz v9, :cond_7

    move-object v9, p1

    check-cast v9, Lantlr/GrammarAtom;

    invoke-virtual {v9}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v10

    if-eqz v10, :cond_7

    const-string v10, "Ref"

    invoke-static {v10}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v10

    invoke-virtual {v9}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v10, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v9

    goto :goto_2

    :cond_7
    iget-object v9, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    :goto_2
    invoke-virtual {p0, p1, v4, v9}, Lantlr/CppCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V

    :cond_8
    invoke-static {v4, v3}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-direct {p0, p1, v3}, Lantlr/CppCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v4, v4, Lantlr/TreeWalkerGrammar;

    if-eqz v4, :cond_9

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v4, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-static {v4, v2, v6, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_9
    if-eqz v8, :cond_a

    const-string v2, "if ( inputState->guessing == 0 ) {"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v7

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_a
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v2

    const-string v4, " = "

    if-eqz v2, :cond_c

    instance-of v2, p1, Lantlr/GrammarAtom;

    if-eqz v2, :cond_b

    invoke-static {v3, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    move-object v9, p1

    check-cast v9, Lantlr/GrammarAtom;

    invoke-virtual {p0, v9, v1}, Lantlr/CppCodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    goto :goto_3

    :cond_b
    invoke-static {v3, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :goto_3
    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_e

    if-eqz v0, :cond_e

    iget-object v0, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    instance-of v1, p1, Lantlr/GrammarAtom;

    if-eqz v1, :cond_d

    invoke-static {v3, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    move-object v2, p1

    check-cast v2, Lantlr/GrammarAtom;

    invoke-virtual {p0, v2, v0}, Lantlr/CppCodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    goto :goto_4

    :cond_d
    invoke-static {v3, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->getASTCreateString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    :goto_4
    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_e

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_e
    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

    if-eqz v0, :cond_14

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    const-string v1, ");"

    const-string v2, "));"

    const-string v4, "RefAST("

    if-eq v0, v7, :cond_11

    const/4 v5, 0x2

    if-eq v0, v5, :cond_f

    goto :goto_8

    :cond_f
    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    const-string v5, "astFactory->makeASTRoot(currentAST, "

    if-nez v0, :cond_13

    instance-of v0, p1, Lantlr/GrammarAtom;

    if-eqz v0, :cond_10

    check-cast p1, Lantlr/GrammarAtom;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_10

    goto :goto_6

    :cond_10
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    goto :goto_5

    :cond_11
    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    const-string v5, "astFactory->addASTChild(currentAST, "

    if-nez v0, :cond_13

    instance-of v0, p1, Lantlr/GrammarAtom;

    if-eqz v0, :cond_12

    check-cast p1, Lantlr/GrammarAtom;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_12

    goto :goto_6

    :cond_12
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    :goto_5
    invoke-virtual {p1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_7

    :cond_13
    :goto_6
    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_7
    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_14
    :goto_8
    if-eqz v8, :cond_15

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v7

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_15
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

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    :cond_3
    return-void
.end method

.method private genErrorHandler(Lantlr/ExceptionSpec;)V
    .locals 5

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

    const-string v3, ") {"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v2, :cond_0

    const-string v2, "if (inputState->guessing==0) {"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_0
    new-instance v2, Lantlr/ActionTransInfo;

    invoke-direct {v2}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v3, v1, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->genLineNo(Lantlr/Token;)V

    iget-object v3, v1, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    iget-object v1, v1, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    invoke-virtual {v1}, Lantlr/Token;->getLine()I

    move-result v1

    iget-object v4, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v3, v1, v4, v2}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    const-string v2, "}"

    if-eqz v1, :cond_1

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "} else {"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "throw;"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_1
    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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

    const-string p1, "try { // for error handling"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_3
    return-void
.end method

.method private genLiteralsTest()V
    .locals 1

    const-string v0, "_ttype = testLiteralsTable(_ttype);"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method private genLiteralsTestForPartialToken()V
    .locals 1

    const-string v0, "_ttype = testLiteralsTable(text.substr(_begin, text.length()-_begin),_ttype);"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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
    invoke-direct {p0, v1}, Lantlr/CppCodeGenerator;->mangleLiteral(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    if-nez p0, :cond_5

    invoke-static {p1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object p0

    goto :goto_0

    :cond_3
    const-string p0, "EOF"

    invoke-virtual {v1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_4

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object p1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v0, "Token::EOF_TYPE"

    invoke-static {p0, p1, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    goto :goto_0

    :cond_4
    move-object p0, v1

    :cond_5
    :goto_0
    return-object p0
.end method

.method private lookaheadString(I)Ljava/lang/String;
    .locals 1

    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p0, p0, Lantlr/TreeWalkerGrammar;

    if-eqz p0, :cond_0

    const-string p0, "_t->getType()"

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

    invoke-direct {p0, p1, p2}, Lantlr/CppCodeGenerator;->mapTreeVariable(Lantlr/AlternativeElement;Ljava/lang/String;)V

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

    iget-object p1, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p1, v0}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p1

    if-eqz p1, :cond_3

    iget-object p1, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p1, v0}, Ljava/util/Hashtable;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p0, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    sget-object p1, Lantlr/CppCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    invoke-virtual {p0, v0, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    goto :goto_1

    :cond_3
    iget-object p0, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {p0, v0, p2}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_4
    :goto_1
    return-void
.end method

.method private setupGrammarParameters(Lantlr/Grammar;)V
    .locals 10

    instance-of v0, p1, Lantlr/ParserGrammar;

    const-string v1, "\""

    if-nez v0, :cond_0

    instance-of v2, p1, Lantlr/LexerGrammar;

    if-nez v2, :cond_0

    instance-of v2, p1, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_b

    :cond_0
    iget-object v2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v2, v2, Lantlr/Tool;->nameSpace:Lantlr/NameSpace;

    if-eqz v2, :cond_1

    sput-object v2, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    :cond_1
    iget-object v2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v2, v2, Lantlr/Tool;->namespaceStd:Ljava/lang/String;

    if-eqz v2, :cond_2

    invoke-direct {p0, v2}, Lantlr/CppCodeGenerator;->fixNameSpaceOption(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    sput-object v2, Lantlr/CppCodeGenerator;->namespaceStd:Ljava/lang/String;

    :cond_2
    iget-object v2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v2, v2, Lantlr/Tool;->namespaceAntlr:Ljava/lang/String;

    if-eqz v2, :cond_3

    invoke-direct {p0, v2}, Lantlr/CppCodeGenerator;->fixNameSpaceOption(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    sput-object v2, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    :cond_3
    iget-object v2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-boolean v2, v2, Lantlr/Tool;->genHashLines:Z

    iput-boolean v2, p0, Lantlr/CppCodeGenerator;->genHashLines:Z

    const-string v2, "namespace"

    invoke-virtual {p1, v2}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v3

    if-eqz v3, :cond_4

    invoke-virtual {p1, v2}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v2

    if-eqz v2, :cond_4

    new-instance v3, Lantlr/NameSpace;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-direct {v3, v2}, Lantlr/NameSpace;-><init>(Ljava/lang/String;)V

    sput-object v3, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    :cond_4
    const-string v2, "namespaceAntlr"

    invoke-virtual {p1, v2}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v3

    const-string v4, "::"

    const/4 v5, 0x2

    if-eqz v3, :cond_6

    invoke-virtual {p1, v2}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v2

    if-eqz v2, :cond_6

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_6

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v3

    if-le v3, v5, :cond_5

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v3

    sub-int/2addr v3, v5

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v6

    invoke-virtual {v2, v3, v6}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_5

    invoke-static {v2, v4}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    :cond_5
    sput-object v2, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    :cond_6
    const-string v2, "namespaceStd"

    invoke-virtual {p1, v2}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v3

    if-eqz v3, :cond_8

    invoke-virtual {p1, v2}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v2

    if-eqz v2, :cond_8

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_8

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v3

    if-le v3, v5, :cond_7

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v3

    sub-int/2addr v3, v5

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v5

    invoke-virtual {v2, v3, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_7

    invoke-static {v2, v4}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    :cond_7
    sput-object v2, Lantlr/CppCodeGenerator;->namespaceStd:Ljava/lang/String;

    :cond_8
    const-string v2, "genHashLines"

    invoke-virtual {p1, v2}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v3

    const-string v4, "true"

    if-eqz v3, :cond_9

    invoke-virtual {p1, v2}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v2

    if-eqz v2, :cond_9

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    iput-boolean v2, p0, Lantlr/CppCodeGenerator;->genHashLines:Z

    :cond_9
    iget-object v2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-boolean v2, v2, Lantlr/Tool;->noConstructors:Z

    iput-boolean v2, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    const-string v2, "noConstructors"

    invoke-virtual {p1, v2}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v2

    if-eqz v2, :cond_b

    const-string v2, "noConstructors"

    invoke-virtual {p1, v2}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v2

    if-eqz v2, :cond_a

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_a

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    const-string v5, "false"

    invoke-virtual {v3, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_a

    iget-object v3, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-virtual {v3}, Lantlr/Tool;->getGrammarFile()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2}, Lantlr/Token;->getLine()I

    move-result v6

    invoke-virtual {v2}, Lantlr/Token;->getColumn()I

    move-result v7

    const-string v8, "noConstructors option must be true or false"

    invoke-virtual {v3, v8, v5, v6, v7}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_a
    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    iput-boolean v2, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    :cond_b
    const-string v2, "RecognitionException"

    const-string v3, "nullAST"

    const-string v4, "throw "

    const-string v5, "RefAST"

    const-string v6, ""

    const-string v7, "ASTLabelType"

    if-eqz v0, :cond_d

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v0, v8, v5}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v5, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v0, v5, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-virtual {p1, v7}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_c

    invoke-virtual {p1, v7}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object p1

    if-eqz p1, :cond_c

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    if-eqz p1, :cond_c

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    iput-object p1, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v0, "("

    invoke-static {p1, v0}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v1, "nullAST)"

    invoke-static {p1, v0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    :cond_c
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v1, "RefToken "

    invoke-static {p1, v0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v1, "nullToken"

    invoke-static {p1, v0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CppCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CppCodeGenerator;->commonExtraParams:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CppCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string p1, "LT(1)"

    iput-object p1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {p1, v0, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v1, "NoViableAltException(LT(1), getFilename());"

    goto :goto_0

    :cond_d
    instance-of v0, p1, Lantlr/LexerGrammar;

    if-eqz v0, :cond_e

    const-string p1, "char "

    iput-object p1, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    const-string p1, "\'\\0\'"

    iput-object p1, p0, Lantlr/CppCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CppCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    const-string p1, "bool _createToken"

    iput-object p1, p0, Lantlr/CppCodeGenerator;->commonExtraParams:Ljava/lang/String;

    const-string p1, "int _ttype; "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "RefToken _token; "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceStd:Ljava/lang/String;

    const-string v1, "string::size_type _begin = text.length();"

    invoke-static {p1, v0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->commonLocalVars:Ljava/lang/String;

    const-string p1, "LA(1)"

    iput-object p1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {p1, v0, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->exceptionThrown:Ljava/lang/String;

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v1, "NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());"

    :goto_0
    invoke-static {p1, v0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->throwNoViable:Ljava/lang/String;

    goto/16 :goto_1

    :cond_e
    instance-of v0, p1, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_11

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v0, v8, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementInit:Ljava/lang/String;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v0, v8, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v0, v3, v5}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v0, v3, v5}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v8, "RefAST _t"

    invoke-static {v0, v3, v8}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v8, "NoViableAltException(_t);"

    invoke-static {v0, v3, v8}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->throwNoViable:Ljava/lang/String;

    const-string v0, "_t"

    iput-object v0, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    invoke-virtual {p1, v7}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v3

    if-eqz v3, :cond_f

    invoke-virtual {p1, v7}, Lantlr/Grammar;->getOption(Ljava/lang/String;)Lantlr/Token;

    move-result-object v3

    if-eqz v3, :cond_f

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_f

    const/4 v3, 0x1

    iput-boolean v3, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    iput-object v1, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    iput-object v1, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    const-string v3, "("

    invoke-static {v1, v3}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "nullAST)"

    invoke-static {v3, v8, v9}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    iput-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementInit:Ljava/lang/String;

    iput-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    const-string v3, " _t"

    invoke-static {v1, v3}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, p0, Lantlr/CppCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "NoViableAltException("

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v4, "RefAST(_t));"

    invoke-static {v1, v3, v4}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, p0, Lantlr/CppCodeGenerator;->throwNoViable:Ljava/lang/String;

    iput-object v0, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    :cond_f
    invoke-virtual {p1, v7}, Lantlr/Grammar;->hasOption(Ljava/lang/String;)Z

    move-result v1

    if-nez v1, :cond_10

    new-instance v1, Lantlr/Token;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v4, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v3, v4, v5}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    const/4 v4, 0x6

    invoke-direct {v1, v4, v3}, Lantlr/Token;-><init>(ILjava/lang/String;)V

    invoke-virtual {p1, v7, v1}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    :cond_10
    iput-object v0, p0, Lantlr/CppCodeGenerator;->commonExtraArgs:Ljava/lang/String;

    iput-object v6, p0, Lantlr/CppCodeGenerator;->commonLocalVars:Ljava/lang/String;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {p1, v0, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->exceptionThrown:Ljava/lang/String;

    goto :goto_1

    :cond_11
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
    .locals 2

    if-eqz p1, :cond_0

    iget v0, p0, Lantlr/CppCodeGenerator;->outputLine:I

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->countLines(Ljava/lang/String;)I

    move-result v1

    add-int/2addr v1, v0

    iput v1, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object p0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p0, p1}, Ljava/io/PrintWriter;->print(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public _printAction(Ljava/lang/String;)V
    .locals 2

    if-eqz p1, :cond_0

    iget v0, p0, Lantlr/CppCodeGenerator;->outputLine:I

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->countLines(Ljava/lang/String;)I

    move-result v1

    add-int/lit8 v1, v1, 0x1

    add-int/2addr v1, v0

    iput v1, p0, Lantlr/CppCodeGenerator;->outputLine:I

    invoke-super {p0, p1}, Lantlr/CodeGenerator;->_printAction(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public _println(Ljava/lang/String;)V
    .locals 2

    if-eqz p1, :cond_0

    iget v0, p0, Lantlr/CppCodeGenerator;->outputLine:I

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->countLines(Ljava/lang/String;)I

    move-result v1

    add-int/lit8 v1, v1, 0x1

    add-int/2addr v1, v0

    iput v1, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object p0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p0, p1}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public addSemPred(Ljava/lang/String;)I
    .locals 1

    iget-object v0, p0, Lantlr/CppCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {v0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    iget-object p0, p0, Lantlr/CppCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {p0}, Lantlr/collections/impl/Vector;->size()I

    move-result p0

    add-int/lit8 p0, p0, -0x1

    return p0
.end method

.method public countLines(Ljava/lang/String;)I
    .locals 3

    const/4 p0, 0x0

    move v0, p0

    :goto_0
    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v1

    if-ge p0, v1, :cond_1

    invoke-virtual {p1, p0}, Ljava/lang/String;->charAt(I)C

    move-result v1

    const/16 v2, 0xa

    if-ne v1, v2, :cond_0

    add-int/lit8 v0, v0, 0x1

    :cond_0
    add-int/lit8 p0, p0, 0x1

    goto :goto_0

    :cond_1
    return v0
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
    .locals 5

    :try_start_0
    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    iget-object v0, v0, Lantlr/DefineGrammarSymbols;->grammars:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/Grammar;

    iget-boolean v2, v1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_0

    iget-object v2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ": C++ mode does not support -debug"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_0
    iget-object v2, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-virtual {v1, v2}, Lantlr/Grammar;->setGrammarAnalyzer(Lantlr/LLkGrammarAnalyzer;)V

    invoke-virtual {v1, p0}, Lantlr/Grammar;->setCodeGenerator(Lantlr/CodeGenerator;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v2, v1}, Lantlr/LLkGrammarAnalyzer;->setGrammar(Lantlr/Grammar;)V

    invoke-direct {p0, v1}, Lantlr/CppCodeGenerator;->setupGrammarParameters(Lantlr/Grammar;)V

    invoke-virtual {v1}, Lantlr/Grammar;->generate()V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->exitIfError()V

    goto :goto_0

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    iget-object v0, v0, Lantlr/DefineGrammarSymbols;->tokenManagers:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v0

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

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->genTokenTypes(Lantlr/TokenManager;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->genTokenInterchange(Lantlr/TokenManager;)V

    :cond_2
    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->exitIfError()V
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
    .locals 5

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    iget-boolean v0, p1, Lantlr/ActionElement;->isSemPred:Z

    if-eqz v0, :cond_2

    iget-object v0, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    iget p1, p1, Lantlr/GrammarElement;->line:I

    invoke-virtual {p0, v0, p1}, Lantlr/CppCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    goto/16 :goto_0

    :cond_2
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v0, :cond_3

    const-string v0, "if ( inputState->guessing==0 ) {"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_3
    new-instance v0, Lantlr/ActionTransInfo;

    invoke-direct {v0}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v1, p1, Lantlr/ActionElement;->actionText:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    iget-object v3, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v1, v2, v3, v0}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v1

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    if-eqz v2, :cond_4

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " = "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v4, "(currentAST.root);"

    invoke-static {v2, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_4
    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genLineNo(Lantlr/GrammarElement;)V

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

    iget-boolean p1, v0, Lantlr/ActionTransInfo;->assignToRoot:Z

    if-eqz p1, :cond_5

    const-string p1, "currentAST.root = "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v1, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ";"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "if ( "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "!="

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    const-string v3, " &&"

    invoke-static {p1, v2, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "->getFirstChild() != "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " )"

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "  currentAST.child = "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    const-string v3, "->getFirstChild();"

    invoke-static {p1, v2, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "else"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "currentAST.child = "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, v0, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    invoke-static {p1, v0, v1, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "currentAST.advanceChildToEnd();"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz p1, :cond_6

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    :goto_0
    return-void
.end method

.method public gen(Lantlr/AlternativeBlock;)V
    .locals 3

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    const-string v0, "{"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v0, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    iput-object v1, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_2
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v1, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    const/4 v1, 0x1

    invoke-virtual {p0, p1, v1}, Lantlr/CppCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CppBlockFinishingInfo;

    move-result-object p1

    iget-object v1, p0, Lantlr/CppCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-direct {p0, p1, v1}, Lantlr/CppCodeGenerator;->genBlockFinish(Lantlr/CppBlockFinishingInfo;Ljava/lang/String;)V

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput-object v0, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    return-void
.end method

.method public gen(Lantlr/BlockEndElement;)V
    .locals 2

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean p0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz p0, :cond_1

    :cond_0
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

    :cond_1
    return-void
.end method

.method public gen(Lantlr/CharLiteralElement;)V
    .locals 4

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-nez v0, :cond_2

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "cannot ref character literals in grammar: "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_2
    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_3

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_3
    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_4

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v1, :cond_4

    move v2, v1

    goto :goto_0

    :cond_4
    const/4 v2, 0x0

    :goto_0
    iput-boolean v2, p0, Lantlr/CppCodeGenerator;->saveText:Z

    iget-boolean v2, p0, Lantlr/CppCodeGenerator;->saveText:Z

    const/4 v3, 0x3

    if-eqz v2, :cond_5

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v3, :cond_6

    :cond_5
    const-string v2, "_saveIndex = text.length();"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    iget-boolean v2, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v2, :cond_7

    const-string v2, "matchNot("

    goto :goto_1

    :cond_7
    const-string v2, "match("

    :goto_1
    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v2, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    invoke-direct {p0, v2, v1}, Lantlr/CppCodeGenerator;->convertJavaToCppString(Ljava/lang/String;Z)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    const-string v1, " /* charlit */ );"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget-boolean v1, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v1, :cond_8

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v3, :cond_9

    :cond_8
    const-string p1, "text.erase(_saveIndex);"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    return-void
.end method

.method public gen(Lantlr/CharRangeElement;)V
    .locals 4

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "genCharRangeElement("

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p1, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ".."

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p1, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-nez v0, :cond_2

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "cannot ref character range in grammar: "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    :cond_2
    invoke-virtual {p1}, Lantlr/CharRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_3

    iget v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_3

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/CharRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_3
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    const/4 v1, 0x1

    if-eqz v0, :cond_5

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v0, :cond_4

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    const/4 v2, 0x3

    if-ne v0, v2, :cond_5

    :cond_4
    move v0, v1

    goto :goto_0

    :cond_5
    const/4 v0, 0x0

    :goto_0
    if-eqz v0, :cond_6

    const-string v2, "_saveIndex=text.length();"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    const-string v2, "matchRange("

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v3, p1, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-direct {p0, v3, v1}, Lantlr/CppCodeGenerator;->convertJavaToCppString(Ljava/lang/String;Z)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ","

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p1, p1, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-direct {p0, p1, v1}, Lantlr/CppCodeGenerator;->convertJavaToCppString(Ljava/lang/String;Z)Ljava/lang/String;

    move-result-object p1

    const-string v1, ");"

    invoke-static {v2, p1, v1, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    if-eqz v0, :cond_7

    const-string p1, "text.erase(_saveIndex);"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    return-void
.end method

.method public gen(Lantlr/LexerGrammar;)V
    .locals 3

    iget-boolean v0, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v0, :cond_0

    new-instance v0, Lantlr/collections/impl/Vector;

    invoke-direct {v0}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v0, p0, Lantlr/CppCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    :cond_0
    iget-object v0, p1, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0}, Lantlr/collections/impl/BitSet;->size()I

    move-result v0

    const/16 v1, 0x100

    if-le v0, v1, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ": Vocabularies of this size still experimental in C++ mode (vocabulary size now: "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p1, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {v2}, Lantlr/collections/impl/BitSet;->size()I

    move-result v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-nez v0, :cond_2

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Internal error generating lexer"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_2
    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBody(Lantlr/LexerGrammar;)V

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genInclude(Lantlr/LexerGrammar;)V

    return-void
.end method

.method public gen(Lantlr/OneOrMoreBlock;)V
    .locals 10

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    const-string v0, "{ // ( ... )+"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_2

    const-string v0, "_cnt_"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :cond_2
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

    const-string v2, "int "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "=0;"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v1

    goto :goto_1

    :cond_3
    const-string v1, "_loop"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :goto_1
    const-string v2, "for (;;) {"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v2, v3

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v2, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_4

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    iput-object v4, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_4
    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v4, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/OneOrMoreBlock;)Z

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v4, v4, Lantlr/Grammar;->maxk:I

    iget-boolean v5, p1, Lantlr/AlternativeBlock;->greedy:Z

    const/4 v6, 0x0

    if-nez v5, :cond_5

    iget v5, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    if-gt v5, v4, :cond_5

    iget-object v7, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aget-object v5, v7, v5

    invoke-virtual {v5}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v5

    if-eqz v5, :cond_5

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    goto :goto_2

    :cond_5
    iget-boolean v5, p1, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v5, :cond_6

    iget v5, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    const v7, 0x7fffffff

    if-ne v5, v7, :cond_6

    :goto_2
    move v5, v4

    move v4, v3

    goto :goto_3

    :cond_6
    move v5, v4

    move v4, v6

    :goto_3
    const-string v7, "if ( "

    if-eqz v4, :cond_9

    iget-boolean v4, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v4, :cond_7

    iget-boolean v4, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v4, :cond_8

    :cond_7
    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v8, "nongreedy (...)+ loop; exit depth is "

    invoke-static {v8}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    iget v9, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v4, v8}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_8
    iget-object v4, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    invoke-virtual {p0, v4, v5}, Lantlr/CppCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object v4

    const-string v5, "// nongreedy exit test"

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, ">=1 && "

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ") goto "

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ";"

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    invoke-virtual {p0, p1, v6}, Lantlr/CppCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CppBlockFinishingInfo;

    move-result-object p1

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ">=1 ) { goto "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "; } else {"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/CppCodeGenerator;->throwNoViable:Ljava/lang/String;

    const-string v6, "}"

    invoke-static {v4, v5, v6}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    invoke-direct {p0, p1, v4}, Lantlr/CppCodeGenerator;->genBlockFinish(Lantlr/CppBlockFinishingInfo;Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "++;"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v3

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ":;"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "}  // ( ... )+"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput-object v2, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    return-void
.end method

.method public gen(Lantlr/ParserGrammar;)V
    .locals 2

    iget-boolean v0, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v0, :cond_0

    new-instance v0, Lantlr/collections/impl/Vector;

    invoke-direct {v0}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v0, p0, Lantlr/CppCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->setGrammar(Lantlr/Grammar;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/ParserGrammar;

    if-nez v0, :cond_1

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Internal error generating parser"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBody(Lantlr/ParserGrammar;)V

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genInclude(Lantlr/ParserGrammar;)V

    return-void
.end method

.method public gen(Lantlr/RuleRefElement;)V
    .locals 6

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, p1, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    const-string v1, "Rule \'"

    if-eqz v0, :cond_16

    invoke-virtual {v0}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v2

    if-nez v2, :cond_2

    goto/16 :goto_3

    :cond_2
    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_3

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_3

    iget v2, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v2, :cond_3

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " = (_t == ASTNULL) ? "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " : "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v4, ";"

    invoke-static {v2, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_3
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    const/4 v3, 0x3

    if-eqz v2, :cond_5

    iget-boolean v2, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v2, :cond_4

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v3, :cond_5

    :cond_4
    const-string v2, "_saveIndex = text.length();"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    invoke-virtual {p0}, Lantlr/CodeGenerator;->printTabs()V

    iget-object v2, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    if-eqz v2, :cond_7

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-nez v0, :cond_6

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

    :cond_6
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p1, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_0

    :cond_7
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    if-nez v2, :cond_8

    iget v2, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v2, :cond_8

    iget-object v0, v0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v0, :cond_8

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

    :cond_8
    :goto_0
    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->GenRuleInvocation(Lantlr/RuleRefElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_a

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v0, :cond_9

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-ne v0, v3, :cond_a

    :cond_9
    const-string v0, "text.erase(_saveIndex);"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    iget v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_15

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v0, Lantlr/Grammar;->hasSyntacticPredicate:Z

    const/4 v2, 0x1

    if-eqz v1, :cond_d

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_b

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_c

    :cond_b
    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

    if-eqz v0, :cond_d

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-ne v0, v2, :cond_d

    :cond_c
    move v0, v2

    goto :goto_1

    :cond_d
    const/4 v0, 0x0

    :goto_1
    if-eqz v0, :cond_e

    const-string v1, "if (inputState->guessing==0) {"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_e
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    if-eqz v1, :cond_f

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_f

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "_AST = returnAST;"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    iget-boolean v1, p0, Lantlr/CppCodeGenerator;->genAST:Z

    if-eqz v1, :cond_13

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-eq v1, v2, :cond_11

    const/4 v3, 0x2

    if-eq v1, v3, :cond_10

    goto :goto_2

    :cond_10
    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v3, "Internal: encountered ^ after rule reference"

    invoke-virtual {v1, v3}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_2

    :cond_11
    iget-boolean v1, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz v1, :cond_12

    const-string v1, "astFactory->addASTChild(currentAST, "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v4, "RefAST(returnAST));"

    invoke-static {v1, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    goto :goto_2

    :cond_12
    const-string v1, "astFactory->addASTChild( currentAST, returnAST );"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_13
    :goto_2
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_14

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_14

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/RuleRefElement;->getLabel()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "=_returnToken;"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_14
    if-eqz v0, :cond_15

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v2

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "}"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_15
    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    return-void

    :cond_16
    :goto_3
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

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_2

    iget v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_2
    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v2

    if-ne v2, v1, :cond_3

    goto :goto_0

    :cond_3
    const/4 v1, 0x0

    :goto_0
    iput-boolean v1, p0, Lantlr/CppCodeGenerator;->saveText:Z

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_4

    const-string p1, "_t = _t->getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    return-void
.end method

.method public gen(Lantlr/TokenRangeElement;)V
    .locals 3

    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    invoke-virtual {p1}, Lantlr/TokenRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/TokenRangeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_0
    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    const-string v0, "matchRange("

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p1, Lantlr/TokenRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p1, Lantlr/TokenRangeElement;->endText:Ljava/lang/String;

    const-string v2, ");"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    return-void
.end method

.method public gen(Lantlr/TokenRefElement;)V
    .locals 3

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_2

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v1, "Token reference found in lexer"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_2
    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genErrorTryForElement(Lantlr/AlternativeElement;)V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_3

    iget v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_3

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_3
    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genErrorCatchForElement(Lantlr/AlternativeElement;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_4

    const-string p1, "_t = _t->getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    return-void
.end method

.method public gen(Lantlr/TreeElement;)V
    .locals 6

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " __t"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, " = _t;"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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

    const-string v1, " = (_t == "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "(ASTNULL)) ? "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    const-string v2, " : _t;"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

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

    invoke-direct {p0, v0}, Lantlr/CppCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    const-string v1, ";"

    if-eqz v0, :cond_3

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v2, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "ASTPair __currentAST"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, " = currentAST;"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "currentAST.root = currentAST.child;"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "currentAST.child = "

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-static {v0, v2, v1, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_3
    iget-object v0, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    instance-of v2, v0, Lantlr/WildcardElement;

    if-eqz v2, :cond_4

    const-string v0, "if ( _t == ASTNULL ) throw "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    sget-object v2, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v3, "MismatchedTokenException();"

    invoke-static {v0, v2, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->genMatch(Lantlr/GrammarAtom;)V

    :goto_0
    const-string v0, "_t = _t->getFirstChild();"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const/4 v0, 0x0

    :goto_1
    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v2

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    if-ge v0, v2, :cond_6

    invoke-virtual {p1, v0}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v2

    iget-object v2, v2, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_2
    if-eqz v2, :cond_5

    invoke-virtual {v2}, Lantlr/GrammarElement;->generate()V

    iget-object v2, v2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_2

    :cond_5
    add-int/lit8 v0, v0, 0x1

    goto :goto_1

    :cond_6
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    if-eqz v0, :cond_7

    const-string v0, "currentAST = __currentAST"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    const-string v0, "_t = __t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget p1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "_t = _t->getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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
    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBody(Lantlr/TreeWalkerGrammar;)V

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genInclude(Lantlr/TreeWalkerGrammar;)V

    return-void
.end method

.method public gen(Lantlr/WildcardElement;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    iget v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/WildcardElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " = "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v2, ";"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_0
    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_1

    const-string p1, "if ( _t == "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " ) throw "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v1, "MismatchedTokenException();"

    :goto_0
    invoke-static {p1, v0, v1, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    goto :goto_1

    :cond_1
    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_5

    const/4 v1, 0x3

    if-eqz v0, :cond_3

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v0

    if-ne v0, v1, :cond_3

    :cond_2
    const-string v0, "_saveIndex = text.length();"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    const-string v0, "matchNot(EOF/*_CHAR*/);"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_6

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v0, :cond_4

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v1, :cond_6

    :cond_4
    const-string p1, "text.erase(_saveIndex);"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_5
    const-string p1, "matchNot("

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    const/4 v0, 0x1

    invoke-direct {p0, v0}, Lantlr/CppCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v0

    const-string v1, ");"

    goto :goto_0

    :cond_6
    :goto_1
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p1, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_7

    const-string p1, "_t = _t->getNextSibling();"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    return-void
.end method

.method public gen(Lantlr/ZeroOrMoreBlock;)V
    .locals 9

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    const-string v0, "{ // ( ... )*"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_2
    const-string v0, "_loop"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :goto_0
    const-string v1, "for (;;) {"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    iget-object v1, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_3

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v3

    iput-object v3, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    :cond_3
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v3, p1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/ZeroOrMoreBlock;)Z

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v3, v3, Lantlr/Grammar;->maxk:I

    iget-boolean v4, p1, Lantlr/AlternativeBlock;->greedy:Z

    const/4 v5, 0x0

    if-nez v4, :cond_4

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    if-gt v4, v3, :cond_4

    iget-object v6, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aget-object v4, v6, v4

    invoke-virtual {v4}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v4

    if-eqz v4, :cond_4

    iget v3, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    goto :goto_1

    :cond_4
    iget-boolean v4, p1, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v4, :cond_5

    iget v4, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    const v6, 0x7fffffff

    if-ne v4, v6, :cond_5

    :goto_1
    move v4, v3

    move v3, v2

    goto :goto_2

    :cond_5
    move v4, v3

    move v3, v5

    :goto_2
    const-string v6, ";"

    if-eqz v3, :cond_8

    iget-boolean v3, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v3, :cond_6

    iget-boolean v3, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v3, :cond_7

    :cond_6
    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v7, "nongreedy (...)* loop; exit depth is "

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget v8, p1, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v3, v7}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_7
    iget-object v3, p1, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    invoke-virtual {p0, v3, v4}, Lantlr/CppCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

    move-result-object v3

    const-string v4, "// nongreedy exit test"

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "if ("

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ") goto "

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v4, v0, v6, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_8
    invoke-virtual {p0, p1, v5}, Lantlr/CppCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CppBlockFinishingInfo;

    move-result-object p1

    const-string v3, "goto "

    invoke-static {v3, v0, v6}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-direct {p0, p1, v3}, Lantlr/CppCodeGenerator;->genBlockFinish(Lantlr/CppBlockFinishingInfo;Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ":;"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "} // ( ... )*"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput-object v1, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;)V
    .locals 1

    iget-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {p0, p1, v0}, Lantlr/CppCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, p1, v0, p2}, Lantlr/CppCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;Ljava/lang/String;)V
    .locals 3

    iget-object v0, p0, Lantlr/CppCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {v0, p1}, Ljava/util/Hashtable;->contains(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    instance-of v1, p1, Lantlr/GrammarAtom;

    if-eqz v1, :cond_1

    move-object v1, p1

    check-cast v1, Lantlr/GrammarAtom;

    invoke-virtual {v1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_1

    const-string v0, "Ref"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    const-string v2, ")"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_1
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p3, " "

    invoke-virtual {v1, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "_AST = "

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ";"

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p0, p0, Lantlr/CppCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {p0, p1, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V
    .locals 8

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

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
    iput-boolean v3, p0, Lantlr/CppCodeGenerator;->genAST:Z

    iget-boolean v3, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v3, :cond_1

    invoke-virtual {p1}, Lantlr/Alternative;->getAutoGen()Z

    move-result v4

    if-eqz v4, :cond_1

    move v1, v2

    :cond_1
    iput-boolean v1, p0, Lantlr/CppCodeGenerator;->saveText:Z

    iget-object v1, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    new-instance v4, Ljava/util/Hashtable;

    invoke-direct {v4}, Ljava/util/Hashtable;-><init>()V

    iput-object v4, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    iget-object v4, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz v4, :cond_2

    const-string v4, "try {      // for error handling"

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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
    iget-boolean v4, p0, Lantlr/CppCodeGenerator;->genAST:Z

    if-eqz v4, :cond_6

    instance-of v4, p2, Lantlr/RuleBlock;

    if-eqz v4, :cond_5

    check-cast p2, Lantlr/RuleBlock;

    iget-boolean v4, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz v4, :cond_4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "_AST = "

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p2, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v5, "(currentAST.root);"

    invoke-static {v4, p2, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

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

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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

    const-string v7, "Labeled subrules are not implemented"

    invoke-virtual {v4, v7, v5, v6, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_6
    :goto_2
    iget-object p2, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz p2, :cond_7

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p2, v2

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p2, "}"

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    :cond_7
    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

    iput-boolean v3, p0, Lantlr/CppCodeGenerator;->saveText:Z

    iput-object v1, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    return-void
.end method

.method public genBitsets(Lantlr/collections/impl/Vector;ILjava/lang/String;)V
    .locals 10

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v3

    if-ge v2, v3, :cond_5

    invoke-virtual {p1, v2}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/collections/impl/BitSet;

    invoke-virtual {v3, p2}, Lantlr/collections/impl/BitSet;->growToInclude(I)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "const unsigned long "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "_data_[] = { "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Lantlr/collections/impl/BitSet;->toStringOfHalfWords()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " };"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "// "

    move v5, v1

    move-object v6, v4

    :goto_1
    invoke-interface {v0}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v7

    invoke-virtual {v7}, Lantlr/collections/impl/Vector;->size()I

    move-result v7

    const/16 v8, 0x20

    if-ge v5, v7, :cond_3

    invoke-virtual {v3, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v7

    if-eqz v7, :cond_2

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v7, v7, Lantlr/LexerGrammar;

    const-string v9, " "

    if-eqz v7, :cond_1

    if-gt v8, v5, :cond_0

    const/16 v7, 0x7f

    if-ge v5, v7, :cond_0

    const/16 v7, 0x5c

    if-eq v5, v7, :cond_0

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    iget-object v7, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    const/4 v8, 0x1

    invoke-interface {v7, v5, v8}, Lantlr/CharFormatter;->escapeChar(IZ)Ljava/lang/String;

    move-result-object v7

    goto :goto_2

    :cond_0
    const-string v7, "0x"

    invoke-static {v6, v7}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    const/16 v7, 0x10

    invoke-static {v5, v7}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object v7

    goto :goto_2

    :cond_1
    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-interface {v0, v5}, Lantlr/TokenManager;->getTokenStringAt(I)Ljava/lang/String;

    move-result-object v7

    :goto_2
    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/String;->length()I

    move-result v7

    const/16 v8, 0x46

    if-le v7, v8, :cond_2

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    move-object v6, v4

    :cond_2
    add-int/lit8 v5, v5, 0x1

    goto :goto_1

    :cond_3
    if-eq v6, v4, :cond_4

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    const-string v4, "const "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    sget-object v5, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "BitSet "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "("

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "_data_,"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Lantlr/collections/impl/BitSet;->size()I

    move-result v3

    div-int/2addr v3, v8

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, ");"

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    add-int/lit8 v2, v2, 0x1

    goto/16 :goto_0

    :cond_5
    return-void
.end method

.method public genBitsetsHeader(Lantlr/collections/impl/Vector;I)V
    .locals 3

    const-string v0, ""

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    invoke-virtual {p1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/collections/impl/BitSet;

    invoke-virtual {v1, p2}, Lantlr/collections/impl/BitSet;->growToInclude(I)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "static const unsigned long "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "_data_[];"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "static const "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "BitSet "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v0}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ";"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public genBlockInitAction(Lantlr/AlternativeBlock;)V
    .locals 3

    iget-object v0, p1, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genLineNo(Lantlr/GrammarElement;)V

    iget-object v0, p1, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    iget p1, p1, Lantlr/GrammarElement;->line:I

    iget-object v1, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    const/4 v2, 0x0

    invoke-virtual {p0, v0, p1, v1, v2}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CodeGenerator;->printAction(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

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

    const-string v3, " = "

    const-string v4, " "

    const-string v5, ";"

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

    iget-object v6, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-static {v2, v3, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

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

    const-string v3, "Ref"

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v2}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v1, v2}, Lantlr/CppCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;Ljava/lang/String;)V

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

    iget-object v6, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-static {v2, v3, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_6

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;)V

    goto :goto_2

    :cond_3
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_4

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->genASTDeclaration(Lantlr/AlternativeElement;)V

    :cond_4
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/LexerGrammar;

    if-eqz v2, :cond_5

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v6, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "RefToken "

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_6

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->labeledElementInit:Ljava/lang/String;

    invoke-static {v2, v1, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_6
    :goto_2
    add-int/lit8 v0, v0, 0x1

    goto/16 :goto_0

    :cond_7
    return-void
.end method

.method public genBody(Lantlr/LexerGrammar;)V
    .locals 17

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ".cpp"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iput-object v2, v0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    const/4 v2, 0x1

    iput v2, v0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object v3, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v4, v0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v3, v4}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v3

    iput-object v3, v0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 v3, 0x0

    iput-boolean v3, v0, Lantlr/CppCodeGenerator;->genAST:Z

    iput-boolean v2, v0, Lantlr/CppCodeGenerator;->saveText:Z

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v4, v0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->genHeader(Ljava/lang/String;)V

    const-string v4, "pre_include_cpp"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "#include \""

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v5}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ".hpp\""

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "#include <antlr/CharBuffer.hpp>"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "#include <antlr/TokenStreamException.hpp>"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "#include <antlr/TokenStreamIOException.hpp>"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "#include <antlr/TokenStreamRecognitionException.hpp>"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "#include <antlr/CharStreamException.hpp>"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "#include <antlr/CharStreamIOException.hpp>"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v4, "#include <antlr/NoViableAltForCharException.hpp>"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v4, v4, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v4, :cond_0

    const-string v4, "#include <antlr/DebuggingInputBuffer.hpp>"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_0
    const-string v4, ""

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "post_include_cpp"

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    sget-object v5, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v5, :cond_1

    iget-object v6, v0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v5, v6}, Lantlr/NameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_1
    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->printAction(Lantlr/Token;)V

    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v6, v5, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v6, :cond_2

    goto :goto_0

    :cond_2
    invoke-virtual {v5}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v5

    const/16 v6, 0x2e

    invoke-virtual {v5, v6}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v7

    const/4 v8, -0x1

    if-eq v7, v8, :cond_3

    invoke-virtual {v5, v6}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v6

    add-int/2addr v6, v2

    invoke-virtual {v5, v6}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v5

    :cond_3
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v6, v7, v5}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    :goto_0
    iget-boolean v5, v0, Lantlr/CppCodeGenerator;->noConstructors:Z

    if-eqz v5, :cond_4

    const-string v5, "#if 0"

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "// constructor creation turned of with \'noConstructor\' option"

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v8, "::"

    invoke-static {v7, v5, v8}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v7, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v9, "("

    invoke-static {v7, v5, v9}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceStd:Ljava/lang/String;

    const-string v10, "istream& in)"

    invoke-static {v5, v7, v10, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v5, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, v2

    iput v5, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v5, v5, Lantlr/Grammar;->debuggingOutput:Z

    const-string v7, "(new "

    const-string v10, ")"

    const-string v11, ": "

    if-eqz v5, :cond_5

    invoke-static {v11, v6, v7}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    sget-object v12, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v5, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v12, "DebuggingInputBuffer(new "

    invoke-virtual {v5, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v12, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v5, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v12, "CharBuffer(in)),"

    goto :goto_1

    :cond_5
    invoke-static {v11, v6, v7}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    sget-object v12, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v5, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v12, "CharBuffer(in),"

    :goto_1
    invoke-virtual {v5, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean v12, v1, Lantlr/LexerGrammar;->caseSensitive:Z

    invoke-virtual {v5, v12}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v5, v2

    iput v5, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v5, "{"

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v12, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v12, v2

    iput v12, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v12, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v12, v12, Lantlr/Grammar;->debuggingOutput:Z

    const-string v13, "setupDebugging();"

    const-string v14, "setSemPredNames(_semPredNames);"

    const-string v15, "setRuleNames(_ruleNames);"

    if-eqz v12, :cond_6

    invoke-virtual {v0, v15}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v14}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    const-string v12, "initLiterals();"

    invoke-virtual {v0, v12}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v2

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "}"

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v16, v4

    iget-object v4, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v4, v2, v8}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v4, v2, v9}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v4, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "InputBuffer& ib)"

    invoke-static {v2, v4, v9, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    add-int/2addr v2, v4

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_7

    invoke-static {v11, v6, v7}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    sget-object v4, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "DebuggingInputBuffer(ib),"

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_2

    :cond_7
    const-string v2, "(ib,"

    invoke-static {v11, v6, v2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    :goto_2
    iget-boolean v4, v1, Lantlr/LexerGrammar;->caseSensitive:Z

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    sub-int/2addr v2, v4

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v4

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_8

    invoke-virtual {v0, v15}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v14}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    invoke-virtual {v0, v12}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    sub-int/2addr v2, v4

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    move-object/from16 v2, v16

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v7, v4, v8}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v7, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v9, "(const "

    invoke-static {v7, v4, v9}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "LexerSharedInputState& state)"

    invoke-static {v4, v7, v9, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v7, 0x1

    add-int/2addr v4, v7

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v4, "(state,"

    invoke-static {v11, v6, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-boolean v1, v1, Lantlr/LexerGrammar;->caseSensitive:Z

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v7

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v1, v7

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v1, :cond_9

    invoke-virtual {v0, v15}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v14}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_9
    invoke-virtual {v0, v12}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    sub-int/2addr v1, v4

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v1, v0, Lantlr/CppCodeGenerator;->noConstructors:Z

    if-eqz v1, :cond_a

    const-string v1, "// constructor creation turned of with \'noConstructor\' option"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#endif"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    const-string v1, "void "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v4, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "::initLiterals()"

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    add-int/2addr v1, v4

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v1}, Lantlr/TokenManager;->getTokenSymbolKeys()Ljava/util/Enumeration;

    move-result-object v1

    :cond_b
    :goto_3
    invoke-interface {v1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v4

    if-eqz v4, :cond_d

    invoke-interface {v1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/String;

    const/4 v5, 0x0

    invoke-virtual {v4, v5}, Ljava/lang/String;->charAt(I)C

    move-result v6

    const/16 v5, 0x22

    if-eq v6, v5, :cond_c

    goto :goto_3

    :cond_c
    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v5, v4}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v4

    instance-of v5, v4, Lantlr/StringLiteralSymbol;

    if-eqz v5, :cond_b

    check-cast v4, Lantlr/StringLiteralSymbol;

    const-string v5, "literals["

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v4}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "] = "

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v4

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, ";"

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_3

    :cond_d
    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    sub-int/2addr v1, v4

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v1, :cond_10

    const-string v1, "const char* "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "::_ruleNames[] = {"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    add-int/2addr v1, v3

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v1

    :cond_e
    :goto_4
    invoke-interface {v1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_f

    invoke-interface {v1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/GrammarSymbol;

    instance-of v4, v3, Lantlr/RuleSymbol;

    if-eqz v4, :cond_e

    const-string v4, "\""

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    check-cast v3, Lantlr/RuleSymbol;

    invoke-virtual {v3}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "\","

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_4

    :cond_f
    const-string v1, "0"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v3, 0x1

    sub-int/2addr v1, v3

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "};"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_10
    invoke-virtual/range {p0 .. p0}, Lantlr/CppCodeGenerator;->genNextToken()V

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v1

    const/4 v3, 0x0

    :goto_5
    invoke-interface {v1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v4

    if-eqz v4, :cond_12

    invoke-interface {v1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/RuleSymbol;

    invoke-virtual {v4}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v5

    const-string v6, "mnextToken"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-nez v5, :cond_11

    add-int/lit8 v5, v3, 0x1

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v7}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    const/4 v7, 0x0

    invoke-virtual {v0, v4, v7, v3, v6}, Lantlr/CppCodeGenerator;->genRule(Lantlr/RuleSymbol;ZILjava/lang/String;)V

    move v3, v5

    goto :goto_6

    :cond_11
    const/4 v7, 0x0

    :goto_6
    invoke-virtual/range {p0 .. p0}, Lantlr/CppCodeGenerator;->exitIfError()V

    goto :goto_5

    :cond_12
    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v1, :cond_13

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->genSemPredMap(Ljava/lang/String;)V

    :cond_13
    iget-object v1, v0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v3, Lantlr/LexerGrammar;

    iget-object v3, v3, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {v3}, Lantlr/collections/impl/BitSet;->size()I

    move-result v3

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v5}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v1, v3, v4}, Lantlr/CppCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;ILjava/lang/String;)V

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    sget-object v1, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v1, :cond_14

    iget-object v2, v0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v1, v2}, Lantlr/NameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_14
    iget-object v1, v0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v1}, Ljava/io/PrintWriter;->close()V

    const/4 v1, 0x0

    iput-object v1, v0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genBody(Lantlr/ParserGrammar;)V
    .locals 13

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".cpp"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    const/4 v0, 0x1

    iput v0, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v2, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v1, v2}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v1

    iput-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v1, v1, Lantlr/Grammar;->buildAST:Z

    iput-boolean v1, p0, Lantlr/CppCodeGenerator;->genAST:Z

    const/4 v1, 0x0

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->genHeader(Ljava/lang/String;)V

    const-string v2, "pre_include_cpp"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "#include \""

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ".hpp\""

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "#include <antlr/NoViableAltException.hpp>"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "#include <antlr/SemanticException.hpp>"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "#include <antlr/ASTFactory.hpp>"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "post_include_cpp"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    sget-object v2, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v2, :cond_0

    iget-object v3, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v2, v3}, Lantlr/NameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_0
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->printAction(Lantlr/Token;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v2, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v3, :cond_1

    goto :goto_0

    :cond_1
    invoke-virtual {v2}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v2

    const/16 v3, 0x2e

    invoke-virtual {v2, v3}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v4

    const/4 v5, -0x1

    if-eq v4, v5, :cond_2

    invoke-virtual {v2, v3}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v3

    add-int/2addr v3, v0

    invoke-virtual {v2, v3}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v2

    :cond_2
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v4, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v3, v4, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    :goto_0
    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_5

    const-string v2, "const char* "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "::_ruleNames[] = {"

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v2

    :cond_3
    :goto_1
    invoke-interface {v2}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v4

    if-eqz v4, :cond_4

    invoke-interface {v2}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/GrammarSymbol;

    instance-of v5, v4, Lantlr/RuleSymbol;

    if-eqz v5, :cond_3

    const-string v5, "\""

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    check-cast v4, Lantlr/RuleSymbol;

    invoke-virtual {v4}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, "\","

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_1

    :cond_4
    const-string v2, "0"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v0

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "};"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-boolean v2, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    const-string v4, "// constructor creation turned of with \'noConstructor\' option"

    if-eqz v2, :cond_6

    const-string v2, "#if 0"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v6, "::"

    invoke-static {v5, v2, v6}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v5}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "("

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v2, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "TokenBuffer& tokenBuf, int k)"

    invoke-virtual {v2, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, ": "

    invoke-virtual {v2, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "(tokenBuf,k)"

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "{"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v8, "}"

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v9, ""

    invoke-virtual {p0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v11, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v11, v10, v6}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v11, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v11}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {p0, v10}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v11, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v11, "TokenBuffer& tokenBuf)"

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {p0, v10}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v11, "(tokenBuf,"

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v11, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v11, v11, Lantlr/Grammar;->maxk:I

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v11, ")"

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {p0, v10}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v12, v10, v6}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v12}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v10, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {p0, v10}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v12, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v10, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v12, "TokenStream& lexer, int k)"

    invoke-virtual {v10, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {p0, v10}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v12, "(lexer,k)"

    invoke-virtual {v10, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {p0, v10}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v12, v10, v6}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v12, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v12}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v10, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {p0, v10}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v5, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v10, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "TokenStream& lexer)"

    invoke-virtual {v10, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v10, "(lexer,"

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v10, v10, Lantlr/Grammar;->maxk:I

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v10, v5, v6}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v10}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v10, "(const "

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v10, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v10, "ParserSharedInputState& state)"

    invoke-virtual {v5, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "(state,"

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v3, v3, Lantlr/Grammar;->maxk:I

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v2, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    if-eqz v2, :cond_7

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "#endif"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    new-instance v2, Lantlr/collections/impl/Vector;

    invoke-direct {v2}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v2, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v2

    move v3, v1

    :goto_2
    invoke-interface {v2}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v4

    if-eqz v4, :cond_a

    invoke-interface {v2}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/GrammarSymbol;

    instance-of v5, v4, Lantlr/RuleSymbol;

    if-eqz v5, :cond_9

    check-cast v4, Lantlr/RuleSymbol;

    iget-object v5, v4, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v5}, Lantlr/collections/impl/Vector;->size()I

    move-result v5

    if-nez v5, :cond_8

    move v5, v0

    goto :goto_3

    :cond_8
    move v5, v1

    :goto_3
    add-int/lit8 v7, v3, 0x1

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v10}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v4, v5, v3, v8}, Lantlr/CppCodeGenerator;->genRule(Lantlr/RuleSymbol;ZILjava/lang/String;)V

    move v3, v7

    :cond_9
    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->exitIfError()V

    goto :goto_2

    :cond_a
    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genInitFactory(Lantlr/Grammar;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genTokenStrings(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->maxTokenType()I

    move-result v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, p1, v0, v1}, Lantlr/CppCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;ILjava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_b

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genSemPredMap(Ljava/lang/String;)V

    :cond_b
    invoke-virtual {p0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    sget-object p1, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz p1, :cond_c

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/NameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_c
    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genBody(Lantlr/TreeWalkerGrammar;)V
    .locals 10

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ".cpp"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    const/4 p1, 0x1

    iput p1, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v0

    iput-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v1, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->genHeader(Ljava/lang/String;)V

    const-string v1, "pre_include_cpp"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "#include \""

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ".hpp\""

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#include <antlr/Token.hpp>"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#include <antlr/AST.hpp>"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#include <antlr/NoViableAltException.hpp>"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#include <antlr/MismatchedTokenException.hpp>"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#include <antlr/SemanticException.hpp>"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#include <antlr/BitSet.hpp>"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "post_include_cpp"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    sget-object v1, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v1, :cond_0

    iget-object v2, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v1, v2}, Lantlr/NameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_0
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v1, v1, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->printAction(Lantlr/Token;)V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v1, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v2, :cond_1

    goto :goto_0

    :cond_1
    invoke-virtual {v1}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v1

    const/16 v2, 0x2e

    invoke-virtual {v1, v2}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v3

    const/4 v4, -0x1

    if-eq v3, v4, :cond_2

    invoke-virtual {v1, v2}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v2

    add-int/2addr v2, p1

    invoke-virtual {v1, v2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v1

    :cond_2
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    :goto_0
    iget-boolean v1, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    const-string v2, "// constructor creation turned of with \'noConstructor\' option"

    if-eqz v1, :cond_3

    const-string v1, "#if 0"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v4, "::"

    invoke-static {v3, v1, v4}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "()"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "\t: "

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v5, "TreeParser() {"

    invoke-static {v1, v3, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v1, p1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    iget v1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, p1

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "}"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v1, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    if-eqz v1, :cond_4

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#endif"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_4
    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Lantlr/collections/impl/Vector;

    invoke-direct {v2}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object v2, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v2

    move v3, v0

    :goto_1
    invoke-interface {v2}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v5

    if-eqz v5, :cond_7

    invoke-interface {v2}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Lantlr/GrammarSymbol;

    instance-of v6, v5, Lantlr/RuleSymbol;

    if-eqz v6, :cond_6

    check-cast v5, Lantlr/RuleSymbol;

    iget-object v6, v5, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v6}, Lantlr/collections/impl/Vector;->size()I

    move-result v6

    if-nez v6, :cond_5

    move v6, p1

    goto :goto_2

    :cond_5
    move v6, v0

    :goto_2
    add-int/lit8 v7, v3, 0x1

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v5, v6, v3, v8}, Lantlr/CppCodeGenerator;->genRule(Lantlr/RuleSymbol;ZILjava/lang/String;)V

    move v3, v7

    :cond_6
    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->exitIfError()V

    goto :goto_1

    :cond_7
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genInitFactory(Lantlr/Grammar;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genTokenStrings(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->maxTokenType()I

    move-result v0

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, p1, v0, v2}, Lantlr/CppCodeGenerator;->genBitsets(Lantlr/collections/impl/Vector;ILjava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    sget-object p1, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz p1, :cond_8

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/NameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_8
    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genCases(Lantlr/collections/impl/BitSet;)V
    .locals 4

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object p1

    const/4 v0, 0x0

    :goto_0
    array-length v1, p1

    if-ge v0, v1, :cond_2

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "case "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget v3, p1, v0

    invoke-direct {p0, v3}, Lantlr/CppCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ":"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    return-void
.end method

.method public genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CppBlockFinishingInfo;
    .locals 27

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    new-instance v2, Lantlr/CppBlockFinishingInfo;

    invoke-direct {v2}, Lantlr/CppBlockFinishingInfo;-><init>()V

    iget-boolean v3, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v3, :cond_0

    iget-boolean v3, v0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v3, :cond_1

    :cond_0
    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "genCommonBlk("

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v5, ")"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_1
    iget-boolean v3, v0, Lantlr/CppCodeGenerator;->genAST:Z

    const/4 v4, 0x0

    const/4 v5, 0x1

    if-eqz v3, :cond_2

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result v6

    if-eqz v6, :cond_2

    move v6, v5

    goto :goto_0

    :cond_2
    move v6, v4

    :goto_0
    iput-boolean v6, v0, Lantlr/CppCodeGenerator;->genAST:Z

    iget-boolean v6, v0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v6, :cond_3

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result v7

    if-eqz v7, :cond_3

    move v7, v5

    goto :goto_1

    :cond_3
    move v7, v4

    :goto_1
    iput-boolean v7, v0, Lantlr/CppCodeGenerator;->saveText:Z

    iget-boolean v7, v1, Lantlr/AlternativeBlock;->not:Z

    const-string v8, ""

    if-eqz v7, :cond_8

    iget-object v7, v0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v9, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v9, Lantlr/LexerGrammar;

    invoke-interface {v7, v1, v9}, Lantlr/LLkGrammarAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result v7

    if-eqz v7, :cond_8

    iget-object v3, v0, Lantlr/CodeGenerator;->analyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v3, v5, v1}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object v3

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_4

    iget v4, v0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    if-nez v4, :cond_4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " = "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, v0, Lantlr/CppCodeGenerator;->lt1Value:Ljava/lang/String;

    const-string v6, ";"

    invoke-static {v4, v5, v6, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_4
    invoke-direct/range {p0 .. p1}, Lantlr/CppCodeGenerator;->genElementAST(Lantlr/AlternativeElement;)V

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_6

    iget-boolean v1, v0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz v1, :cond_5

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v4, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v5, "RefAST(_t),"

    invoke-static {v1, v4, v5}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

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

    invoke-virtual {v0, v3}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ");"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_7

    const-string v1, "_t = _t->getNextSibling();"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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

    invoke-virtual {v0, v3, v4}, Lantlr/CppCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    :cond_a
    invoke-virtual {v0, v7, v1}, Lantlr/CppCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

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

    invoke-static {v10}, Lantlr/CppCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v10

    if-eqz v10, :cond_c

    add-int/lit8 v9, v9, 0x1

    :cond_c
    add-int/lit8 v7, v7, 0x1

    goto :goto_3

    :cond_d
    iget v7, v0, Lantlr/CodeGenerator;->makeSwitchThreshold:I

    const-string v10, "{"

    const-string v11, "_t = ASTNULL;"

    const-string v12, " )"

    const-string v13, "if (_t == "

    const-string v14, "}"

    if-lt v9, v7, :cond_12

    invoke-direct {v0, v5}, Lantlr/CppCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v7

    iget-object v9, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v9, Lantlr/TreeWalkerGrammar;

    if-eqz v9, :cond_e

    invoke-static {v13}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v9

    iget-object v15, v0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-static {v9, v15, v12, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v9, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v9, v5

    iput v9, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v11}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v9, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v9, v5

    iput v9, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_e
    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    const-string v15, "switch ( "

    invoke-virtual {v9, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, ") {"

    invoke-virtual {v9, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v9}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    move v7, v4

    :goto_4
    iget-object v9, v1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v9}, Lantlr/collections/impl/Vector;->size()I

    move-result v9

    if-ge v7, v9, :cond_11

    invoke-virtual {v1, v7}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v9

    invoke-static {v9}, Lantlr/CppCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v15

    if-nez v15, :cond_f

    move-object/from16 v17, v8

    goto :goto_5

    :cond_f
    iget-object v15, v9, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v15, v15, v5

    iget-object v4, v15, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v4}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v4

    if-nez v4, :cond_10

    invoke-virtual {v15}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v4

    if-nez v4, :cond_10

    iget-object v4, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v15, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v15}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v15

    iget-object v5, v9, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v5}, Lantlr/GrammarElement;->getLine()I

    move-result v5

    iget-object v9, v9, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    invoke-virtual {v9}, Lantlr/GrammarElement;->getColumn()I

    move-result v9

    move-object/from16 v17, v8

    const-string v8, "Alternate omitted due to empty prediction set"

    invoke-virtual {v4, v8, v15, v5, v9}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    const/4 v5, 0x1

    goto :goto_5

    :cond_10
    move-object/from16 v17, v8

    iget-object v4, v15, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->genCases(Lantlr/collections/impl/BitSet;)V

    invoke-virtual {v0, v10}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    add-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v9, v1}, Lantlr/CppCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    const-string v4, "break;"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v14}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :goto_5
    add-int/lit8 v7, v7, 0x1

    move-object/from16 v8, v17

    const/4 v4, 0x0

    goto :goto_4

    :cond_11
    move-object/from16 v17, v8

    const-string v4, "default:"

    invoke-virtual {v0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v4, 0x1

    goto :goto_6

    :cond_12
    move-object/from16 v17, v8

    const/4 v4, 0x0

    :goto_6
    iget-object v5, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v7, v5, Lantlr/LexerGrammar;

    if-eqz v7, :cond_13

    iget v5, v5, Lantlr/Grammar;->maxk:I

    goto :goto_7

    :cond_13
    const/4 v5, 0x0

    :goto_7
    const/4 v7, 0x0

    const/4 v8, 0x0

    :goto_8
    if-ltz v5, :cond_2d

    iget-boolean v9, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v9, :cond_15

    iget-boolean v9, v0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v9, :cond_14

    goto :goto_9

    :cond_14
    move/from16 p2, v8

    goto :goto_a

    :cond_15
    :goto_9
    sget-object v9, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v15, Ljava/lang/StringBuilder;

    invoke-direct {v15}, Ljava/lang/StringBuilder;-><init>()V

    move/from16 p2, v8

    const-string v8, "checking depth "

    invoke-virtual {v15, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v15, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v15}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v9, v8}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :goto_a
    move/from16 v8, p2

    move v9, v7

    const/4 v7, 0x0

    :goto_b
    iget-object v15, v1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v15}, Lantlr/collections/impl/Vector;->size()I

    move-result v15

    if-ge v7, v15, :cond_2c

    invoke-virtual {v1, v7}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v15

    move/from16 v18, v6

    iget-boolean v6, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v6, :cond_17

    iget-boolean v6, v0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v6, :cond_16

    goto :goto_c

    :cond_16
    move/from16 v19, v3

    move-object/from16 v20, v14

    goto :goto_d

    :cond_17
    :goto_c
    sget-object v6, Ljava/lang/System;->out:Ljava/io/PrintStream;

    move/from16 v19, v3

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v20, v14

    const-string v14, "genAlt: "

    invoke-virtual {v3, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v6, v3}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :goto_d
    if-eqz v4, :cond_19

    invoke-static {v15}, Lantlr/CppCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v3

    if-eqz v3, :cond_19

    iget-boolean v3, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v3, :cond_18

    iget-boolean v3, v0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v3, :cond_1d

    :cond_18
    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v6, "ignoring alt because it was in the switch"

    :goto_e
    invoke-virtual {v3, v6}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_10

    :cond_19
    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v6, v3, Lantlr/LexerGrammar;

    if-eqz v6, :cond_1f

    iget v6, v15, Lantlr/Alternative;->lookaheadDepth:I

    const v14, 0x7fffffff

    if-ne v6, v14, :cond_1a

    iget v6, v3, Lantlr/Grammar;->maxk:I

    :cond_1a
    :goto_f
    const/4 v3, 0x1

    if-lt v6, v3, :cond_1b

    iget-object v3, v15, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v3, v3, v6

    invoke-virtual {v3}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v3

    if-eqz v3, :cond_1b

    add-int/lit8 v6, v6, -0x1

    goto :goto_f

    :cond_1b
    if-eq v6, v5, :cond_1e

    iget-boolean v3, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v3, :cond_1c

    iget-boolean v3, v0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v3, :cond_1d

    :cond_1c
    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v14, Ljava/lang/StringBuilder;

    invoke-direct {v14}, Ljava/lang/StringBuilder;-><init>()V

    const-string v15, "ignoring alt because effectiveDepth!=altDepth;"

    invoke-virtual {v14, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v6, "!="

    invoke-virtual {v14, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v14}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    goto :goto_e

    :cond_1d
    :goto_10
    move-object/from16 v26, v2

    move/from16 v21, v4

    move/from16 v22, v5

    move/from16 v23, v7

    move-object/from16 v25, v10

    move-object/from16 v2, v20

    goto/16 :goto_19

    :cond_1e
    invoke-virtual {v0, v15, v6}, Lantlr/CppCodeGenerator;->lookaheadIsEmpty(Lantlr/Alternative;I)Z

    move-result v3

    goto :goto_11

    :cond_1f
    iget v3, v3, Lantlr/Grammar;->maxk:I

    invoke-virtual {v0, v15, v3}, Lantlr/CppCodeGenerator;->lookaheadIsEmpty(Lantlr/Alternative;I)Z

    move-result v3

    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget v6, v6, Lantlr/Grammar;->maxk:I

    :goto_11
    invoke-virtual {v0, v15, v6}, Lantlr/CppCodeGenerator;->getLookaheadTestExpression(Lantlr/Alternative;I)Ljava/lang/String;

    move-result-object v6

    iget-object v14, v15, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    const/16 v16, 0x1

    aget-object v14, v14, v16

    iget-object v14, v14, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v14}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v14

    move/from16 v21, v4

    const-string v4, "if "

    move/from16 v22, v5

    const-string v5, "else if "

    move/from16 v23, v7

    const-string v7, " {"

    move/from16 v24, v9

    const/16 v9, 0x7f

    if-le v14, v9, :cond_22

    invoke-static {v15}, Lantlr/CppCodeGenerator;->suitableForCaseExpression(Lantlr/Alternative;)Z

    move-result v9

    if-eqz v9, :cond_22

    if-nez v8, :cond_21

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v3, v3, Lantlr/TreeWalkerGrammar;

    if-eqz v3, :cond_20

    invoke-static {v13}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v5, v0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-static {v3, v5, v12, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    add-int/2addr v3, v5

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v11}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v5

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_20
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_12

    :cond_21
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_12
    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :goto_13
    move-object/from16 v26, v2

    move-object/from16 v25, v10

    goto/16 :goto_17

    :cond_22
    const-string v9, "else {"

    if-eqz v3, :cond_24

    iget-object v3, v15, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-nez v3, :cond_24

    iget-object v3, v15, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-nez v3, :cond_24

    if-nez v8, :cond_23

    invoke-virtual {v0, v10}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_14

    :cond_23
    invoke-virtual {v0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :goto_14
    const/4 v3, 0x0

    iput-boolean v3, v2, Lantlr/CppBlockFinishingInfo;->needAnErrorClause:Z

    goto :goto_13

    :cond_24
    iget-object v3, v15, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v3, :cond_27

    new-instance v3, Lantlr/ActionTransInfo;

    invoke-direct {v3}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v14, v15, Lantlr/Alternative;->semPred:Ljava/lang/String;

    move-object/from16 v25, v10

    iget v10, v1, Lantlr/GrammarElement;->line:I

    move-object/from16 v26, v2

    iget-object v2, v0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v0, v14, v10, v2, v3}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v2

    iget-object v3, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v10, v3, Lantlr/Grammar;->debuggingOutput:Z

    const-string v14, "))"

    const-string v1, "("

    if-eqz v10, :cond_26

    instance-of v10, v3, Lantlr/ParserGrammar;

    if-nez v10, :cond_25

    instance-of v3, v3, Lantlr/LexerGrammar;

    if-eqz v3, :cond_26

    :cond_25
    const-string v3, "&& fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.PREDICTING,"

    invoke-static {v1, v6, v3}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, v0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v3, v2}, Lantlr/CharFormatter;->escapeString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->addSemPred(Ljava/lang/String;)I

    move-result v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, ","

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v1, v2, v14}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    goto :goto_15

    :cond_26
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "&&("

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    goto :goto_15

    :cond_27
    move-object/from16 v26, v2

    move-object/from16 v25, v10

    :goto_15
    iget-object v1, v15, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-lez v8, :cond_29

    if-eqz v1, :cond_28

    invoke-virtual {v0, v9}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v1, v15, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v0, v1, v6}, Lantlr/CppCodeGenerator;->genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V

    add-int/lit8 v9, v24, 0x1

    goto :goto_18

    :cond_28
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_16
    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_17

    :cond_29
    if-eqz v1, :cond_2a

    invoke-virtual {v0, v1, v6}, Lantlr/CppCodeGenerator;->genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V

    goto :goto_17

    :cond_2a
    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_2b

    invoke-static {v13}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, v0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-static {v1, v2, v12, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v11}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_2b
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_16

    :goto_17
    move/from16 v9, v24

    :goto_18
    add-int/lit8 v8, v8, 0x1

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    move-object/from16 v1, p1

    invoke-virtual {v0, v15, v1}, Lantlr/CppCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    iget v3, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v2

    iput v3, v0, Lantlr/CodeGenerator;->tabs:I

    move-object/from16 v2, v20

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :goto_19
    add-int/lit8 v7, v23, 0x1

    move-object v14, v2

    move/from16 v6, v18

    move/from16 v3, v19

    move/from16 v4, v21

    move/from16 v5, v22

    move-object/from16 v10, v25

    move-object/from16 v2, v26

    goto/16 :goto_b

    :cond_2c
    move-object/from16 v26, v2

    move/from16 v19, v3

    move/from16 v21, v4

    move/from16 v22, v5

    move/from16 v18, v6

    move/from16 v24, v9

    move-object/from16 v25, v10

    move-object v2, v14

    add-int/lit8 v5, v22, -0x1

    move/from16 v7, v24

    move-object/from16 v2, v26

    goto/16 :goto_8

    :cond_2d
    move-object/from16 v26, v2

    move/from16 v19, v3

    move/from16 v21, v4

    move/from16 v18, v6

    move/from16 p2, v8

    move-object v2, v14

    move-object/from16 v3, v17

    const/4 v1, 0x1

    :goto_1a
    if-gt v1, v7, :cond_2e

    iget v4, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v5, 0x1

    sub-int/2addr v4, v5

    iput v4, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v3, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    add-int/lit8 v1, v1, 0x1

    goto :goto_1a

    :cond_2e
    move/from16 v1, v19

    const/4 v5, 0x1

    iput-boolean v1, v0, Lantlr/CppCodeGenerator;->genAST:Z

    move/from16 v1, v18

    iput-boolean v1, v0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v21, :cond_30

    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v1, v5

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v3, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    move-object/from16 v1, v26

    iput-object v0, v1, Lantlr/CppBlockFinishingInfo;->postscript:Ljava/lang/String;

    iput-boolean v5, v1, Lantlr/CppBlockFinishingInfo;->generatedSwitch:Z

    if-lez p2, :cond_2f

    goto :goto_1b

    :cond_2f
    const/4 v0, 0x0

    goto :goto_1c

    :cond_30
    move-object/from16 v1, v26

    iput-object v3, v1, Lantlr/CppBlockFinishingInfo;->postscript:Ljava/lang/String;

    const/4 v0, 0x0

    iput-boolean v0, v1, Lantlr/CppBlockFinishingInfo;->generatedSwitch:Z

    if-lez p2, :cond_31

    :goto_1b
    move v0, v5

    :cond_31
    :goto_1c
    iput-boolean v0, v1, Lantlr/CppBlockFinishingInfo;->generatedAnIf:Z

    return-object v1
.end method

.method public genHeader(Ljava/lang/String;)V
    .locals 3

    const-string v0, "/* $ANTLR "

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

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "\"$ */"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genInclude(Lantlr/LexerGrammar;)V
    .locals 8

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".hpp"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    const/4 v0, 0x1

    iput v0, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v2, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v1, v2}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v1

    iput-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 v1, 0x0

    iput-boolean v1, p0, Lantlr/CppCodeGenerator;->genAST:Z

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "#ifndef INC_"

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "_hpp_"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "#define INC_"

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, ""

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "pre_include_hpp"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    const-string v3, "#include <antlr/config.hpp>"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->genHeader(Ljava/lang/String;)V

    const-string v3, "#include <antlr/CommonToken.hpp>"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "#include <antlr/InputBuffer.hpp>"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "#include <antlr/BitSet.hpp>"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "#include \""

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v4}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v4, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    const-string v5, ".hpp\""

    invoke-static {v3, v4, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v3, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v4, :cond_0

    const-string v3, "\n// Include correct superclass header with a header statement for example:"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "// header \"post_include_hpp\" {"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "// #include \""

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "// }"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "// Or...."

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "// header {"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "// }\n"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    invoke-virtual {v3}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v3

    const/16 v4, 0x2e

    invoke-virtual {v3, v4}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v5

    const/4 v6, -0x1

    if-eq v5, v6, :cond_1

    invoke-virtual {v3, v4}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v4

    add-int/2addr v4, v0

    invoke-virtual {v3, v4}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v3

    :cond_1
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "#include <antlr/"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ".hpp>"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v5, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v4, v5, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    :goto_0
    const-string v3, "post_include_hpp"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    sget-object v3, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v3, :cond_2

    iget-object v5, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v3, v5}, Lantlr/NameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v3, :cond_3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_3
    const-string v3, "class CUSTOM_API "

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v5}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " : public "

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, ", public "

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

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v4, "classHeaderSuffix"

    invoke-virtual {v3, v4}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/Token;

    if-eqz v3, :cond_4

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    const-string v4, "\""

    invoke-static {v3, v4, v4}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, ", "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_4
    const-string v3, "{"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    const/4 v5, 0x0

    if-eqz v4, :cond_5

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->genLineNo(Lantlr/Token;)V

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v4, v4, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v6, v6, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v6}, Lantlr/Token;->getLine()I

    move-result v6

    iget-object v7, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v4, v6, v7, v5}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

    :cond_5
    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v4, "private:"

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v6, "void initLiterals();"

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v6, "public:"

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v7, "bool getCaseSensitiveLiterals() const"

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v0

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "return "

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-boolean p1, p1, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string p1, ";"

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v0

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-boolean p1, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    const-string v3, "// constructor creation turned of with \'noConstructor\' option"

    if-eqz p1, :cond_6

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "#if 0"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_6
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v7, "("

    invoke-static {v6, p1, v7}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v6, Lantlr/CppCodeGenerator;->namespaceStd:Ljava/lang/String;

    invoke-virtual {p1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "istream& in);"

    invoke-virtual {p1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v6, p1, v7}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v6, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {p1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "InputBuffer& ib);"

    invoke-virtual {p1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v7, "(const "

    invoke-static {v6, p1, v7}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v6, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v7, "LexerSharedInputState& state);"

    invoke-static {p1, v6, v7, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-boolean p1, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    if-eqz p1, :cond_7

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "#endif"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_7
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v6, "RefToken nextToken();"

    invoke-static {p1, v3, v6, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object p1

    :goto_1
    invoke-interface {p1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_9

    invoke-interface {p1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/RuleSymbol;

    invoke-virtual {v3}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    const-string v7, "mnextToken"

    invoke-virtual {v6, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-nez v6, :cond_8

    invoke-virtual {p0, v3, v1}, Lantlr/CppCodeGenerator;->genRuleHeader(Lantlr/RuleSymbol;Z)V

    :cond_8
    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->exitIfError()V

    goto :goto_1

    :cond_9
    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_a

    const-string p1, "static const char* _ruleNames[];"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_a
    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_b

    const-string p1, "static const char* _semPredNames[];"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_b
    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-object v0, v0, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0}, Lantlr/collections/impl/BitSet;->size()I

    move-result v0

    invoke-virtual {p0, p1, v0}, Lantlr/CppCodeGenerator;->genBitsetsHeader(Lantlr/collections/impl/Vector;I)V

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "};"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    sget-object p1, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz p1, :cond_c

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/NameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_c
    const-string p1, "#endif /*INC_"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "_hpp_*/"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    iput-object v5, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genInclude(Lantlr/ParserGrammar;)V
    .locals 11

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ".hpp"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    const/4 p1, 0x1

    iput p1, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v0

    iput-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "#ifndef INC_"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "_hpp_"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "#define INC_"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "pre_include_hpp"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    const-string v2, "#include <antlr/config.hpp>"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->genHeader(Ljava/lang/String;)V

    const-string v2, "#include <antlr/TokenStream.hpp>"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "#include <antlr/TokenBuffer.hpp>"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "#include \""

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    const-string v4, ".hpp\""

    invoke-static {v2, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v2, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v3, :cond_0

    const-string v2, "\n// Include correct superclass header with a header statement for example:"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// header \"post_include_hpp\" {"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "// #include \""

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// }"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// Or...."

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// header {"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// }\n"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    invoke-virtual {v2}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v2

    const/16 v3, 0x2e

    invoke-virtual {v2, v3}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v4

    const/4 v5, -0x1

    if-eq v4, v5, :cond_1

    invoke-virtual {v2, v3}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v3

    add-int/2addr v3, p1

    invoke-virtual {v2, v3}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v2

    :cond_1
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "#include <antlr/"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ".hpp>"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v4, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v3, v4, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "post_include_hpp"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    sget-object v2, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v2, :cond_2

    iget-object v4, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v2, v4}, Lantlr/NameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v2, :cond_3

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_3
    const-string v2, "class CUSTOM_API "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " : public "

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, ", public "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v3, "classHeaderSuffix"

    invoke-virtual {v2, v3}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/Token;

    if-eqz v2, :cond_4

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    const-string v3, "\""

    invoke-static {v2, v3, v3}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_4

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, ", "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_4
    const-string v2, "{"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v3, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v3, :cond_5

    const-string v3, "public: static const char* _ruleNames[];"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    const/4 v4, 0x0

    if-eqz v3, :cond_6

    invoke-virtual {v3}, Lantlr/Token;->getLine()I

    move-result v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->genLineNo(I)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v5}, Lantlr/Token;->getLine()I

    move-result v5

    iget-object v6, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v3, v5, v6, v4}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

    :cond_6
    const-string v3, "public:"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v5, "void initializeASTFactory( "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    sget-object v6, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v7, "ASTFactory& factory );"

    invoke-static {v5, v6, v7, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-boolean v5, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    if-eqz v5, :cond_7

    const-string v5, "#if 0"

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "// constructor creation turned of with \'noConstructor\' option"

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    const-string v5, "protected:"

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v8, "("

    invoke-static {v7, v6, v8}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "TokenBuffer& tokenBuf, int k);"

    invoke-static {v6, v7, v9, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v7, v6, v8}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "TokenBuffer& tokenBuf);"

    invoke-static {v6, v7, v9, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v7, v6, v8}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "TokenStream& lexer, int k);"

    invoke-static {v6, v7, v9, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v7, v6, v8}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "TokenStream& lexer);"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v8, "(const "

    invoke-static {v7, v6, v8}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v8, "ParserSharedInputState& state);"

    invoke-static {v6, v7, v8, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-boolean v6, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    if-eqz v6, :cond_8

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v6, "// constructor creation turned of with \'noConstructor\' option"

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v6, "#endif"

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_8
    const-string v6, "int getNumTokens() const"

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v6, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v6, p1

    iput v6, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v6, "return "

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "::NUM_TOKENS;"

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v7, p1

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v7, "}"

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v8, "const char* getTokenName( int type ) const"

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, p1

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v8, "if( type > getNumTokens() ) return 0;"

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "::tokenNames[type];"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v8, p1

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v8, "const char* const* getTokenNames() const"

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, p1

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "::tokenNames;"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v8, p1

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v8}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v8

    :goto_1
    invoke-interface {v8}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v9

    if-eqz v9, :cond_b

    invoke-interface {v8}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v9

    check-cast v9, Lantlr/GrammarSymbol;

    instance-of v10, v9, Lantlr/RuleSymbol;

    if-eqz v10, :cond_a

    check-cast v9, Lantlr/RuleSymbol;

    iget-object v10, v9, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v10}, Lantlr/collections/impl/Vector;->size()I

    move-result v10

    if-nez v10, :cond_9

    move v10, p1

    goto :goto_2

    :cond_9
    move v10, v0

    :goto_2
    invoke-virtual {p0, v9, v10}, Lantlr/CppCodeGenerator;->genRuleHeader(Lantlr/RuleSymbol;Z)V

    :cond_a
    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->exitIfError()V

    goto :goto_1

    :cond_b
    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "RefAST getAST()"

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v2, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz v2, :cond_c

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, p1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v6, "RefAST(returnAST);"

    invoke-static {v2, v3, v6, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    goto :goto_3

    :cond_c
    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, p1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "return returnAST;"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :goto_3
    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, p1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v5, " returnAST;"

    invoke-static {v2, v3, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "private:"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "static const char* tokenNames[];"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "#ifndef NO_STATIC_CONSTS"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "static const int NUM_TOKENS = "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v2

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ";"

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "#else"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    const-string p1, "enum {"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "\tNUM_TOKENS = "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v2

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "};"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "#endif"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2}, Lantlr/TokenManager;->maxTokenType()I

    move-result v2

    invoke-virtual {p0, p1, v2}, Lantlr/CppCodeGenerator;->genBitsetsHeader(Lantlr/collections/impl/Vector;I)V

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean p1, p1, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p1, :cond_d

    const-string p1, "static const char* _semPredNames[];"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_d
    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "};"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    sget-object p1, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz p1, :cond_e

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/NameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_e
    const-string p1, "#endif /*INC_"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "_hpp_*/"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    iput-object v4, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genInclude(Lantlr/TreeWalkerGrammar;)V
    .locals 11

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ".hpp"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    const/4 p1, 0x1

    iput p1, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object v0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v0

    iput-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    iput-boolean v0, p0, Lantlr/CppCodeGenerator;->genAST:Z

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "#ifndef INC_"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "_hpp_"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "#define INC_"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "pre_include_hpp"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    const-string v2, "#include <antlr/config.hpp>"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "#include \""

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    const-string v4, ".hpp\""

    invoke-static {v2, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-object v2, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->genHeader(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v2, Lantlr/Grammar;->superClass:Ljava/lang/String;

    if-eqz v3, :cond_0

    const-string v2, "\n// Include correct superclass header with a header statement for example:"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// header \"post_include_hpp\" {"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "// #include \""

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// }"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// Or...."

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// header {"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "// }\n"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    invoke-virtual {v2}, Lantlr/Grammar;->getSuperClass()Ljava/lang/String;

    move-result-object v2

    const/16 v3, 0x2e

    invoke-virtual {v2, v3}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v4

    const/4 v5, -0x1

    if-eq v4, v5, :cond_1

    invoke-virtual {v2, v3}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v3

    add-int/2addr v3, p1

    invoke-virtual {v2, v3}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v2

    :cond_1
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "#include <antlr/"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ".hpp>"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v4, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-static {v3, v4, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "post_include_hpp"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    sget-object v2, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v2, :cond_2

    iget-object v4, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v2, v4}, Lantlr/NameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->printHeaderAction(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->comment:Ljava/lang/String;

    if-eqz v2, :cond_3

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_3
    const-string v2, "class CUSTOM_API "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " : public "

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, ", public "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    const-string v3, "classHeaderSuffix"

    invoke-virtual {v2, v3}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/Token;

    if-eqz v2, :cond_4

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    const-string v3, "\""

    invoke-static {v2, v3, v3}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_4

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, ", "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    :cond_4
    const-string v2, "{"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    const/4 v4, 0x0

    if-eqz v3, :cond_5

    invoke-virtual {v3}, Lantlr/Token;->getLine()I

    move-result v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->genLineNo(I)V

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    iget-object v5, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    invoke-virtual {v5}, Lantlr/Token;->getLine()I

    move-result v5

    iget-object v6, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, v3, v5, v6, v4}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

    :cond_5
    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "public:"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v5, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    if-eqz v5, :cond_6

    const-string v5, "#if 0"

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "// constructor creation turned of with \'noConstructor\' option"

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v6}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "();"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v5, p0, Lantlr/CppCodeGenerator;->noConstructors:Z

    const-string v6, "#endif"

    if-eqz v5, :cond_7

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_7
    const-string v5, "static void initializeASTFactory( "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "ASTFactory& factory );"

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v5, "int getNumTokens() const"

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v5, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v5, p1

    iput v5, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v5, "return "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v8}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "::NUM_TOKENS;"

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v7, p1

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v7, "}"

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v8, "const char* getTokenName( int type ) const"

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, p1

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v8, "if( type > getNumTokens() ) return 0;"

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "::tokenNames[type];"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v8, p1

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v8, "const char* const* getTokenNames() const"

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v8, p1

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    iget-object v9, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "::tokenNames;"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v8, p1

    iput v8, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v8, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v8}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v8

    :goto_1
    invoke-interface {v8}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v9

    if-eqz v9, :cond_a

    invoke-interface {v8}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v9

    check-cast v9, Lantlr/GrammarSymbol;

    instance-of v10, v9, Lantlr/RuleSymbol;

    if-eqz v10, :cond_9

    check-cast v9, Lantlr/RuleSymbol;

    iget-object v10, v9, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v10}, Lantlr/collections/impl/Vector;->size()I

    move-result v10

    if-nez v10, :cond_8

    move v10, p1

    goto :goto_2

    :cond_8
    move v10, v0

    :goto_2
    invoke-virtual {p0, v9, v10}, Lantlr/CppCodeGenerator;->genRuleHeader(Lantlr/RuleSymbol;Z)V

    :cond_9
    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->exitIfError()V

    goto :goto_1

    :cond_a
    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "RefAST getAST()"

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-boolean v2, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz v2, :cond_b

    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, p1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v5, "RefAST(returnAST);"

    invoke-static {v2, v3, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    goto :goto_3

    :cond_b
    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, p1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "return returnAST;"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :goto_3
    iget v2, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, p1

    iput v2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "protected:"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " returnAST;"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    const-string v5, " _retTree;"

    invoke-static {v2, v3, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "private:"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "static const char* tokenNames[];"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "#ifndef NO_STATIC_CONSTS"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "static const int NUM_TOKENS = "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v2

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ";"

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "#else"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    const-string p1, "enum {"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "\tNUM_TOKENS = "

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v2

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p1, "};"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v6}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CodeGenerator;->bitsetsUsed:Lantlr/collections/impl/Vector;

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v3}, Lantlr/TokenManager;->maxTokenType()I

    move-result v3

    invoke-virtual {p0, v2, v3}, Lantlr/CppCodeGenerator;->genBitsetsHeader(Lantlr/collections/impl/Vector;I)V

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    sget-object p1, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz p1, :cond_c

    iget-object v0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1, v0}, Lantlr/NameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_c
    const-string p1, "#endif /*INC_"

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "_hpp_*/"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    iput-object v4, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public genInitFactory(Lantlr/Grammar;)V
    .locals 8

    iget-boolean v0, p1, Lantlr/Grammar;->buildAST:Z

    if-nez v0, :cond_0

    const-string v0, ""

    goto :goto_0

    :cond_0
    const-string v0, "factory "

    :goto_0
    const-string v1, "void "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v2, "::initializeASTFactory( "

    invoke-static {p1, v1, v2}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "ASTFactory& "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ")"

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "{"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-boolean p1, p1, Lantlr/Grammar;->buildAST:Z

    if-eqz p1, :cond_6

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1}, Lantlr/TokenManager;->getTokenSymbolKeys()Ljava/util/Enumeration;

    move-result-object v0

    :cond_1
    :goto_1
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_3

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    invoke-interface {p1, v2}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v3

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_1

    iget-object v4, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v5

    invoke-virtual {v4, v5}, Lantlr/collections/impl/Vector;->ensureCapacity(I)V

    iget-object v4, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v5

    invoke-virtual {v4, v5}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/String;

    if-nez v4, :cond_2

    iget-object v2, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getTokenType()I

    move-result v3

    invoke-virtual {v2, v4, v3}, Lantlr/collections/impl/Vector;->setElementAt(Ljava/lang/Object;I)V

    goto :goto_1

    :cond_2
    invoke-virtual {v3}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-nez v5, :cond_1

    iget-object v5, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v6, "Token "

    const-string v7, " taking most specific AST type"

    invoke-static {v6, v2, v7}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v6}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v2, v6, v1, v1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    iget-object v2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v5, "  using "

    const-string v6, " ignoring "

    invoke-static {v5, v4, v6}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {v3}, Lantlr/TokenSymbol;->getASTNodeType()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    iget-object v4, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v3, v4, v1, v1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_1

    :cond_3
    const/4 p1, 0x0

    :goto_2
    iget-object v0, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->size()I

    move-result v0

    if-ge p1, v0, :cond_5

    iget-object v0, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    invoke-virtual {v0, p1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    if-eqz v0, :cond_4

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "factory.registerFactory("

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, ", \""

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "\", "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "::factory);"

    invoke-static {v2, v0, v3, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_4
    add-int/lit8 p1, p1, 0x1

    goto :goto_2

    :cond_5
    const-string p1, "factory.setMaxNodeType("

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->maxTokenType()I

    move-result v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, ");"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_6
    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "}"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genLineNo(I)V
    .locals 2

    if-nez p1, :cond_0

    add-int/lit8 p1, p1, 0x1

    :cond_0
    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->genHashLines:Z

    if-eqz v0, :cond_1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "#line "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, " \""

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v1, p1, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    invoke-virtual {p1, v1}, Lantlr/Tool;->fileMinusPath(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "\""

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_1
    return-void
.end method

.method public genLineNo(Lantlr/GrammarElement;)V
    .locals 0

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genLineNo(I)V

    :cond_0
    return-void
.end method

.method public genLineNo(Lantlr/Token;)V
    .locals 0

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genLineNo(I)V

    :cond_0
    return-void
.end method

.method public genLineNo2()V
    .locals 2

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->genHashLines:Z

    if-eqz v0, :cond_0

    const-string v0, "#line "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v1, p0, Lantlr/CppCodeGenerator;->outputLine:I

    add-int/lit8 v1, v1, 0x1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, " \""

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\""

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public genMatch(Lantlr/GrammarAtom;)V
    .locals 2

    instance-of v0, p1, Lantlr/StringLiteralElement;

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_2

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genMatchUsingAtomText(Lantlr/GrammarAtom;)V

    goto :goto_0

    :cond_0
    instance-of v0, p1, Lantlr/CharLiteralElement;

    if-eqz v0, :cond_1

    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "cannot ref character literals in grammar: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    instance-of v0, p1, Lantlr/TokenRefElement;

    if-eqz v0, :cond_3

    :cond_2
    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->genMatchUsingAtomTokenType(Lantlr/GrammarAtom;)V

    goto :goto_0

    :cond_3
    instance-of v0, p1, Lantlr/WildcardElement;

    if-eqz v0, :cond_4

    check-cast p1, Lantlr/WildcardElement;

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->gen(Lantlr/WildcardElement;)V

    :cond_4
    :goto_0
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

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v2, "RefAST(_t),"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

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

    iget-boolean v1, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v1, :cond_2

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-ne v1, v2, :cond_3

    :cond_2
    const-string v1, "_saveIndex = text.length();"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_3
    iget-boolean v1, p1, Lantlr/GrammarAtom;->not:Z

    if-eqz v1, :cond_4

    const-string v1, "matchNot("

    goto :goto_1

    :cond_4
    const-string v1, "match("

    :goto_1
    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    const-string v1, "EOF"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "Token::EOF_TYPE"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :goto_2
    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    goto :goto_3

    :cond_5
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_6

    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    const/4 v1, 0x0

    invoke-direct {p0, v0, v1}, Lantlr/CppCodeGenerator;->convertJavaToCppString(Ljava/lang/String;Z)Ljava/lang/String;

    move-result-object v0

    goto :goto_2

    :cond_6
    iget-object v0, p1, Lantlr/GrammarAtom;->atomText:Ljava/lang/String;

    goto :goto_2

    :goto_3
    const-string v0, ");"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_8

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->saveText:Z

    if-eqz v0, :cond_7

    invoke-virtual {p1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p1

    if-ne p1, v2, :cond_8

    :cond_7
    const-string p1, "text.erase(_saveIndex);"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    return-void
.end method

.method public genMatchUsingAtomTokenType(Lantlr/GrammarAtom;)V
    .locals 3

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_1

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v2, "RefAST(_t),"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

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

    invoke-direct {p0, v1}, Lantlr/CppCodeGenerator;->getValueString(I)Ljava/lang/String;

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

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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
    const-string v2, "RefToken "

    const-string v5, ""

    if-nez v1, :cond_2

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    const-string v2, "::nextToken() { return "

    invoke-static {v1, v0, v2}, La/a/a/a/a;->a(Lantlr/Grammar;Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "RefToken(new "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "CommonToken("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "Token::EOF_TYPE, \"\")); }"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :goto_2
    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    return-void

    :cond_2
    iget-object v1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v6, v1, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    const-string v7, "nextToken"

    invoke-static {v1, v6, v7}, Lantlr/MakeGrammar;->createNextTokenRule(Lantlr/Grammar;Lantlr/collections/impl/Vector;Ljava/lang/String;)Lantlr/RuleBlock;

    move-result-object v1

    new-instance v6, Lantlr/RuleSymbol;

    const-string v7, "mnextToken"

    invoke-direct {v6, v7}, Lantlr/RuleSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {v6}, Lantlr/RuleSymbol;->setDefined()V

    invoke-virtual {v6, v1}, Lantlr/RuleSymbol;->setBlock(Lantlr/RuleBlock;)V

    const-string v7, "private"

    iput-object v7, v6, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v7, v6}, Lantlr/Grammar;->define(Lantlr/RuleSymbol;)V

    iget-object v6, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v6, v6, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v6, v1}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    const/4 v6, 0x0

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v7, Lantlr/LexerGrammar;

    iget-boolean v8, v7, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v8, :cond_3

    iget-object v6, v7, Lantlr/LexerGrammar;->filterRule:Ljava/lang/String;

    :cond_3
    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v7, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "::nextToken()"

    invoke-virtual {v7, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "{"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v4

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "RefToken theRetToken;"

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "for (;;) {"

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v4

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v9, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "int _ttype = "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "Token::INVALID_TYPE;"

    invoke-static {v7, v8, v9, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v7, Lantlr/LexerGrammar;

    iget-boolean v7, v7, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v7, :cond_7

    const-string v7, "setCommitToPath(false);"

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    if-eqz v6, :cond_7

    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v6}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Lantlr/Grammar;->isDefined(Ljava/lang/String;)Z

    move-result v7

    const-string v8, " does not exist in this lexer"

    const-string v9, "Filter rule "

    if-nez v7, :cond_4

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    :goto_3
    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_4
    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v3, v7}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_5

    :cond_4
    iget-object v7, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-static {v6}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v7, v10}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v7

    check-cast v7, Lantlr/RuleSymbol;

    invoke-virtual {v7}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v10

    if-nez v10, :cond_5

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    goto :goto_3

    :cond_5
    iget-object v7, v7, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v7, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_6

    iget-object v3, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, " must be protected"

    goto :goto_4

    :cond_6
    :goto_5
    const-string v3, "int _m;"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v3, "_m = mark();"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_7
    const-string v3, "resetText();"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v7, "try {   // for lexical and char stream error handling"

    invoke-virtual {p0, v7}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v7, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v4

    iput v7, p0, Lantlr/CodeGenerator;->tabs:I

    move v7, v0

    :goto_6
    invoke-virtual {v1}, Lantlr/RuleBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v8

    invoke-virtual {v8}, Lantlr/collections/impl/Vector;->size()I

    move-result v8

    if-ge v7, v8, :cond_9

    invoke-virtual {v1, v7}, Lantlr/RuleBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v8

    iget-object v8, v8, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v8, v8, v4

    invoke-virtual {v8}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v8

    if-eqz v8, :cond_8

    iget-object v8, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v9, "found optional path in nextToken()"

    invoke-virtual {v8, v9}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    :cond_8
    add-int/lit8 v7, v7, 0x1

    goto :goto_6

    :cond_9
    const-string v7, "line.separator"

    invoke-static {v7}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {p0, v1, v0}, Lantlr/CppCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CppBlockFinishingInfo;

    move-result-object v0

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "if (LA(1)==EOF_CHAR)"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t{"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t\tuponEOF();"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t\t_returnToken = makeToken("

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v9, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "Token::EOF_TYPE);"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "\t\t\t\t}"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    const-string v10, "\t\t\t\t"

    invoke-static {v8, v7, v10}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    iget-object v10, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v10, Lantlr/LexerGrammar;

    iget-boolean v10, v10, Lantlr/LexerGrammar;->filterMode:Z

    const-string v11, "(false);}"

    const-string v12, "else {"

    const-string v13, "}"

    if-eqz v10, :cond_b

    if-nez v6, :cond_a

    const-string v9, "else {consume(); goto tryAgain;}"

    invoke-static {v8, v9}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    goto :goto_7

    :cond_a
    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\t\tcommit();"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\t\ttry {m"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\t\tcatch("

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "RecognitionException& e) {"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\t\t\t// catastrophic failure"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\t\t\treportError(e);"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\t\t\tconsume();"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\t\t}"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\t\tgoto tryAgain;"

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v10, v7, v9}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    goto :goto_7

    :cond_b
    invoke-static {v8, v12}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    iget-object v9, p0, Lantlr/CppCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-static {v8, v9, v13}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v8

    :goto_7
    invoke-direct {p0, v0, v8}, Lantlr/CppCodeGenerator;->genBlockFinish(Lantlr/CppBlockFinishingInfo;Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-boolean v0, v0, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v0, :cond_c

    if-eqz v6, :cond_c

    const-string v0, "commit();"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_c
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "if ( !_returnToken )"

    invoke-virtual {v0, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "\t\t\t\tgoto tryAgain; // found SKIP token"

    invoke-virtual {v0, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "_ttype = _returnToken->getType();"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    invoke-virtual {v0}, Lantlr/LexerGrammar;->getTestLiterals()Z

    move-result v0

    if-eqz v0, :cond_d

    invoke-direct {p0}, Lantlr/CppCodeGenerator;->genLiteralsTest()V

    :cond_d
    const-string v0, "_returnToken->setType(_ttype);"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "return _returnToken;"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "catch ("

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v8, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "RecognitionException& e) {"

    invoke-static {v0, v8, v9, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    check-cast v0, Lantlr/LexerGrammar;

    iget-boolean v0, v0, Lantlr/LexerGrammar;->filterMode:Z

    if-eqz v0, :cond_f

    const-string v0, "if ( !getCommitToPath() ) {"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    if-nez v6, :cond_e

    const-string v0, "consume();"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "goto tryAgain;"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_8

    :cond_e
    const-string v0, "rewind(_m);"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "try {m"

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "catch("

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "RecognitionException& ee) {"

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\t// horrendous failure: error in filter rule"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\treportError(ee);"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "\tconsume();"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "else"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    :goto_8
    invoke-virtual {v1}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v0

    if-eqz v0, :cond_10

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "reportError(e);"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "consume();"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_9

    :cond_10
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "throw "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v2, "TokenStreamRecognitionException(e);"

    invoke-static {v0, v1, v2, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :goto_9
    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "CharStreamIOException& csie) {"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "\tthrow "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "TokenStreamIOException(csie.io);"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "CharStreamException& cse) {"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "\tthrow "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "TokenStreamException(cse.getMessage());"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "tryAgain:;"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v0, v4

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v13}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto/16 :goto_2
.end method

.method public genRule(Lantlr/RuleSymbol;ZILjava/lang/String;)V
    .locals 16

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move/from16 v2, p3

    iget-boolean v3, v0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    if-nez v3, :cond_0

    iget-boolean v3, v0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v3, :cond_1

    :cond_0
    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v4, "genRule("

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ")"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_1
    invoke-virtual/range {p1 .. p1}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v3

    if-nez v3, :cond_2

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

    :cond_2
    invoke-virtual/range {p1 .. p1}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v3

    iput-object v3, v0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    iget-object v4, v0, Lantlr/CppCodeGenerator;->declaredASTVariables:Ljava/util/Hashtable;

    invoke-virtual {v4}, Ljava/util/Hashtable;->clear()V

    iget-boolean v4, v0, Lantlr/CppCodeGenerator;->genAST:Z

    const/4 v5, 0x0

    const/4 v6, 0x1

    if-eqz v4, :cond_3

    invoke-virtual {v3}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v7

    if-eqz v7, :cond_3

    move v7, v6

    goto :goto_0

    :cond_3
    move v7, v5

    :goto_0
    iput-boolean v7, v0, Lantlr/CppCodeGenerator;->genAST:Z

    invoke-virtual {v3}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v7

    iput-boolean v7, v0, Lantlr/CppCodeGenerator;->saveText:Z

    iget-object v7, v1, Lantlr/RuleSymbol;->comment:Ljava/lang/String;

    if-eqz v7, :cond_4

    invoke-virtual {v0, v7}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    :cond_4
    iget-object v7, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    const-string v8, " "

    if-eqz v7, :cond_5

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v3}, Lantlr/RuleBlock;->getLine()I

    move-result v10

    invoke-virtual {v3}, Lantlr/RuleBlock;->getColumn()I

    move-result v11

    invoke-virtual {v0, v9, v10, v11}, Lantlr/CodeGenerator;->extractTypeOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    goto :goto_1

    :cond_5
    const-string v7, "void "

    :goto_1
    invoke-virtual {v0, v7}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    invoke-static/range {p4 .. p4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "("

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v0, v7}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v7, v0, Lantlr/CppCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {v0, v7}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object v7, v0, Lantlr/CppCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {v7}, Ljava/lang/String;->length()I

    move-result v7

    if-eqz v7, :cond_6

    iget-object v7, v3, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz v7, :cond_6

    const-string v7, ","

    invoke-virtual {v0, v7}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    :cond_6
    iget-object v7, v3, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    const-string v9, ") "

    const-string v10, ""

    if-eqz v7, :cond_a

    invoke-virtual {v0, v10}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget v7, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v7, v6

    iput v7, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v7, v3, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    const/16 v11, 0x3d

    invoke-virtual {v7, v11}, Ljava/lang/String;->indexOf(I)I

    move-result v12

    const/4 v13, -0x1

    if-eq v12, v13, :cond_8

    move-object v15, v7

    move-object v6, v10

    move-object v14, v6

    move v7, v5

    :cond_7
    :goto_2
    if-eq v7, v13, :cond_9

    if-eq v12, v13, :cond_9

    invoke-static {v14, v6}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v15, v5, v12}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v7}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v14

    const-string v6, ", "

    const/16 v7, 0x2c

    invoke-virtual {v15, v7, v12}, Ljava/lang/String;->indexOf(II)I

    move-result v7

    if-eq v7, v13, :cond_7

    add-int/lit8 v12, v7, 0x1

    invoke-virtual {v15, v12}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v12}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v15

    invoke-virtual {v15, v11}, Ljava/lang/String;->indexOf(I)I

    move-result v12

    if-ne v12, v13, :cond_7

    invoke-static {v14, v6, v15}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v14

    goto :goto_2

    :cond_8
    move-object v14, v7

    :cond_9
    invoke-virtual {v0, v14}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v6, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v7, 0x1

    sub-int/2addr v6, v7

    iput v6, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v9}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_3

    :cond_a
    move v7, v6

    invoke-virtual {v0, v9}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    :goto_3
    const-string v6, "{"

    invoke-virtual {v0, v6}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget v6, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v6, v7

    iput v6, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v7, v6, Lantlr/Grammar;->traceRules:Z

    if-eqz v7, :cond_d

    instance-of v6, v6, Lantlr/TreeWalkerGrammar;

    if-eqz v6, :cond_c

    iget-boolean v6, v0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    const-string v7, "Tracer traceInOut(this,\""

    if-eqz v6, :cond_b

    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\","

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v7, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v9, "RefAST(_t));"

    invoke-static {v6, v7, v9, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    goto :goto_5

    :cond_b
    invoke-static {v7}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\",_t);"

    goto :goto_4

    :cond_c
    const-string v6, "Tracer traceInOut(this, \""

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, "\");"

    :goto_4
    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_d
    :goto_5
    iget-object v6, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    const-string v7, ";"

    if-eqz v6, :cond_e

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->genLineNo(Lantlr/GrammarElement;)V

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual/range {p0 .. p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

    :cond_e
    iget-object v6, v0, Lantlr/CppCodeGenerator;->commonLocalVars:Ljava/lang/String;

    invoke-virtual {v6, v10}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-nez v6, :cond_f

    iget-object v6, v0, Lantlr/CppCodeGenerator;->commonLocalVars:Ljava/lang/String;

    invoke-virtual {v0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_f
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v6, v6, Lantlr/LexerGrammar;

    if-eqz v6, :cond_11

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    const-string v9, "mEOF"

    invoke-virtual {v6, v9}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    const-string v9, "_ttype = "

    if-eqz v6, :cond_10

    invoke-static {v9}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    sget-object v9, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    const-string v11, "Token::EOF_TYPE;"

    invoke-static {v6, v9, v11, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    goto :goto_6

    :cond_10
    invoke-static {v9}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v9

    const/4 v11, 0x1

    invoke-virtual {v9, v11}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :goto_6
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v9, Lantlr/CppCodeGenerator;->namespaceStd:Ljava/lang/String;

    const-string v11, "string::size_type _saveIndex;"

    invoke-static {v6, v9, v11, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_11
    iget-object v6, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v9, v6, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v9, :cond_13

    instance-of v9, v6, Lantlr/ParserGrammar;

    const-string v11, "fireEnterRule("

    if-eqz v9, :cond_12

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ",0);"

    :goto_7
    invoke-virtual {v6, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_8

    :cond_12
    instance-of v6, v6, Lantlr/LexerGrammar;

    if-eqz v6, :cond_13

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ",_ttype);"

    goto :goto_7

    :cond_13
    :goto_8
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_14

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, v0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "_AST_in = (_t == "

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, v0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "(ASTNULL)) ? "

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, v0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    const-string v9, " : _t;"

    invoke-static {v2, v6, v9, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_14
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    const-string v6, "returnAST = "

    if-eqz v2, :cond_15

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v9, v0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-virtual {v2, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v9, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v2, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v9, "ASTPair currentAST;"

    invoke-virtual {v2, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v9, v0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v2, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v8, "_AST = "

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v8, v0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    invoke-static {v2, v8, v7, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    :cond_15
    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->genBlockPreamble(Lantlr/AlternativeBlock;)V

    invoke-virtual {v0, v3}, Lantlr/CppCodeGenerator;->genBlockInitAction(Lantlr/AlternativeBlock;)V

    invoke-virtual {v0, v10}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v3, v10}, Lantlr/RuleBlock;->findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;

    move-result-object v2

    if-nez v2, :cond_17

    invoke-virtual {v3}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v8

    if-eqz v8, :cond_16

    goto :goto_9

    :cond_16
    const/4 v9, 0x1

    goto :goto_a

    :cond_17
    :goto_9
    const-string v8, "try {      // for error handling"

    invoke-virtual {v0, v8}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v8, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v9, 0x1

    add-int/2addr v8, v9

    iput v8, v0, Lantlr/CodeGenerator;->tabs:I

    :goto_a
    iget-object v8, v3, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v8}, Lantlr/collections/impl/Vector;->size()I

    move-result v8

    if-ne v8, v9, :cond_1a

    invoke-virtual {v3, v5}, Lantlr/RuleBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v5

    iget-object v8, v5, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v8, :cond_18

    iget-object v9, v0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget v9, v9, Lantlr/GrammarElement;->line:I

    invoke-virtual {v0, v8, v9}, Lantlr/CppCodeGenerator;->genSemPred(Ljava/lang/String;I)V

    :cond_18
    iget-object v8, v5, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-eqz v8, :cond_19

    iget-object v8, v0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v9, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {v9}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v9

    iget-object v11, v5, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v11}, Lantlr/GrammarElement;->getLine()I

    move-result v11

    iget-object v12, v5, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    invoke-virtual {v12}, Lantlr/GrammarElement;->getColumn()I

    move-result v12

    const-string v13, "Syntactic predicate ignored for single alternative"

    invoke-virtual {v8, v13, v9, v11, v12}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_19
    invoke-virtual {v0, v5, v3}, Lantlr/CppCodeGenerator;->genAlt(Lantlr/Alternative;Lantlr/AlternativeBlock;)V

    goto :goto_b

    :cond_1a
    iget-object v8, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v8, v3}, Lantlr/LLkGrammarAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    invoke-virtual {v0, v3, v5}, Lantlr/CppCodeGenerator;->genCommonBlock(Lantlr/AlternativeBlock;Z)Lantlr/CppBlockFinishingInfo;

    move-result-object v5

    iget-object v8, v0, Lantlr/CppCodeGenerator;->throwNoViable:Ljava/lang/String;

    invoke-direct {v0, v5, v8}, Lantlr/CppCodeGenerator;->genBlockFinish(Lantlr/CppBlockFinishingInfo;Ljava/lang/String;)V

    :goto_b
    const-string v5, "}"

    if-nez v2, :cond_1b

    invoke-virtual {v3}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v8

    if-eqz v8, :cond_1c

    :cond_1b
    iget v8, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v9, 0x1

    sub-int/2addr v8, v9

    iput v8, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_1c
    if-eqz v2, :cond_1d

    invoke-direct {v0, v2}, Lantlr/CppCodeGenerator;->genErrorHandler(Lantlr/ExceptionSpec;)V

    goto/16 :goto_d

    :cond_1d
    invoke-virtual {v3}, Lantlr/RuleBlock;->getDefaultErrorHandler()Z

    move-result v2

    if-eqz v2, :cond_21

    const-string v2, "catch ("

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v8, v0, Lantlr/CppCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string v9, "& ex) {"

    invoke-static {v2, v8, v9, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v8, 0x1

    add-int/2addr v2, v8

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v2, :cond_1e

    const-string v2, "if( inputState->guessing == 0 ) {"

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v8

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    :cond_1e
    const-string v2, "reportError(ex);"

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v9, v2, Lantlr/TreeWalkerGrammar;

    if-nez v9, :cond_1f

    iget-object v2, v2, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    iget-object v9, v3, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    invoke-interface {v2, v8, v9}, Lantlr/LLkGrammarAnalyzer;->FOLLOW(ILantlr/RuleEndElement;)Lantlr/Lookahead;

    move-result-object v2

    iget-object v2, v2, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->markBitsetForGen(Lantlr/collections/impl/BitSet;)I

    move-result v2

    invoke-virtual {v0, v2}, Lantlr/CodeGenerator;->getBitsetName(I)Ljava/lang/String;

    move-result-object v2

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "recover(ex,"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ");"

    invoke-virtual {v8, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const/4 v8, 0x1

    goto :goto_c

    :cond_1f
    const-string v2, "if ( _t != "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v8, v0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    const-string v9, " )"

    invoke-static {v2, v8, v9, v0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v8, 0x1

    add-int/2addr v2, v8

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "_t = _t->getNextSibling();"

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v8

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    :goto_c
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->hasSyntacticPredicate:Z

    if-eqz v2, :cond_20

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v8

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "} else {"

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v2, v8

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    const-string v2, "throw;"

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v8

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_20
    iget v2, v0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v2, v8

    iput v2, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_21
    :goto_d
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, v2, Lantlr/Grammar;->buildAST:Z

    if-eqz v2, :cond_22

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "_AST;"

    invoke-virtual {v2, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_22
    iget-object v2, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v2, v2, Lantlr/TreeWalkerGrammar;

    if-eqz v2, :cond_23

    const-string v2, "_retTree = _t;"

    invoke-virtual {v0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_23
    invoke-virtual {v3}, Lantlr/RuleBlock;->getTestLiterals()Z

    move-result v2

    if-eqz v2, :cond_25

    iget-object v1, v1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    const-string v2, "protected"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_24

    invoke-direct/range {p0 .. p0}, Lantlr/CppCodeGenerator;->genLiteralsTestForPartialToken()V

    goto :goto_e

    :cond_24
    invoke-direct/range {p0 .. p0}, Lantlr/CppCodeGenerator;->genLiteralsTest()V

    :cond_25
    :goto_e
    iget-object v1, v0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v1, Lantlr/LexerGrammar;

    if-eqz v1, :cond_26

    const-string v1, "if ( _createToken && _token=="

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    sget-object v2, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "nullToken && _ttype!="

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "Token::SKIP ) {"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "   _token = makeToken(_ttype);"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "   _token->setText(text.substr(_begin, text.length()-_begin));"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "_returnToken = _token;"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "_saveIndex=0;"

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_26
    iget-object v1, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v1, :cond_27

    const-string v1, "return "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, v3, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v3}, Lantlr/RuleBlock;->getLine()I

    move-result v6

    invoke-virtual {v3}, Lantlr/RuleBlock;->getColumn()I

    move-result v3

    invoke-virtual {v0, v2, v6, v3}, Lantlr/CodeGenerator;->extractIdOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_27
    iget v1, v0, Lantlr/CodeGenerator;->tabs:I

    const/4 v2, 0x1

    sub-int/2addr v1, v2

    iput v1, v0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {v0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {v0, v10}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput-boolean v4, v0, Lantlr/CppCodeGenerator;->genAST:Z

    return-void
.end method

.method public genRuleHeader(Lantlr/RuleSymbol;Z)V
    .locals 7

    const/4 p2, 0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    const-string v1, ")"

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, "genRuleHeader("

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p1}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v0

    if-nez v0, :cond_2

    iget-object p0, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string p2, "undefined rule: "

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-void

    :cond_2
    invoke-virtual {p1}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v2

    iput-object v2, p0, Lantlr/CppCodeGenerator;->currentASTResult:Ljava/lang/String;

    iget-boolean v2, p0, Lantlr/CppCodeGenerator;->genAST:Z

    if-eqz v2, :cond_3

    invoke-virtual {v0}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v3

    if-eqz v3, :cond_3

    move v3, p2

    goto :goto_0

    :cond_3
    const/4 v3, 0x0

    :goto_0
    iput-boolean v3, p0, Lantlr/CppCodeGenerator;->genAST:Z

    invoke-virtual {v0}, Lantlr/RuleBlock;->getAutoGen()Z

    move-result v3

    iput-boolean v3, p0, Lantlr/CppCodeGenerator;->saveText:Z

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v4, p1, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, ": "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    iget-object v3, v0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    if-eqz v3, :cond_4

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v4, v0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    invoke-virtual {v0}, Lantlr/RuleBlock;->getLine()I

    move-result v5

    invoke-virtual {v0}, Lantlr/RuleBlock;->getColumn()I

    move-result v6

    invoke-virtual {p0, v4, v5, v6}, Lantlr/CodeGenerator;->extractTypeOfAction(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    goto :goto_1

    :cond_4
    const-string v3, "void "

    :goto_1
    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "("

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CppCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/CppCodeGenerator;->commonExtraParams:Ljava/lang/String;

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result p1

    if-eqz p1, :cond_5

    iget-object p1, v0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz p1, :cond_5

    const-string p1, ","

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    :cond_5
    iget-object p1, v0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    if-eqz p1, :cond_6

    const-string p1, ""

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr p1, p2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object p1, v0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, p2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    goto :goto_2

    :cond_6
    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->_print(Ljava/lang/String;)V

    :goto_2
    const-string p1, ";"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr p1, p2

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iput-boolean v2, p0, Lantlr/CppCodeGenerator;->genAST:Z

    return-void
.end method

.method public genSemPred(Ljava/lang/String;I)V
    .locals 2

    new-instance v0, Lantlr/ActionTransInfo;

    invoke-direct {v0}, Lantlr/ActionTransInfo;-><init>()V

    iget-object v1, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0, p1, p2, v1, v0}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

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

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->addSemPred(Ljava/lang/String;)I

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

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "throw "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "SemanticException(\""

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\");"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    return-void
.end method

.method public genSemPredMap(Ljava/lang/String;)V
    .locals 3

    iget-object v0, p0, Lantlr/CppCodeGenerator;->semPreds:Lantlr/collections/impl/Vector;

    invoke-virtual {v0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "const char* "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "_semPredNames[] = {"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result p1

    if-eqz p1, :cond_0

    const-string p1, "\""

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v1, "\","

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    const-string p1, "0"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "};"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genSynPred(Lantlr/SynPredBlock;Ljava/lang/String;)V
    .locals 6

    iget-boolean v0, p0, Lantlr/CodeGenerator;->DEBUG_CODE_GENERATOR:Z

    const-string v1, ")"

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->DEBUG_CPP_CODE_GENERATOR:Z

    if-eqz v0, :cond_1

    :cond_0
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

    :cond_1
    const-string v0, "bool synPredMatched"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, " = false;"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v0, :cond_2

    const-string v0, "if (_t == "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v3, p0, Lantlr/CppCodeGenerator;->labeledElementASTInit:Ljava/lang/String;

    const-string v4, " )"

    invoke-static {v0, v3, v4, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "_t = ASTNULL;"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    :cond_2
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "if ("

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ") {"

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, 0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/TreeWalkerGrammar;

    if-eqz p2, :cond_3

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v0, p0, Lantlr/CppCodeGenerator;->labeledElementType:Ljava/lang/String;

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " __t"

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v0, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, " = _t;"

    goto :goto_0

    :cond_3
    const-string p2, "int _m"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    iget v0, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, " = mark();"

    :goto_0
    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "synPredMatched"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v3, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, " = true;"

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v0, "inputState->guessing++;"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v3, v0, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v3, :cond_5

    instance-of v3, v0, Lantlr/ParserGrammar;

    if-nez v3, :cond_4

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_5

    :cond_4
    const-string v0, "fireSyntacticPredicateStarted();"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    iget v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    const-string v0, "try {"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->gen(Lantlr/AlternativeBlock;)V

    iget v0, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v0, "}"

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "catch ("

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lantlr/CppCodeGenerator;->exceptionThrown:Ljava/lang/String;

    const-string v5, "& pe) {"

    invoke-static {v3, v4, v5, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 v3, v3, 0x1

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    iget v3, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p2, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p2, p2, Lantlr/TreeWalkerGrammar;

    if-eqz p2, :cond_6

    const-string p2, "_t = __t"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ";"

    goto :goto_1

    :cond_6
    const-string p2, "rewind(_m"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ");"

    :goto_1
    invoke-virtual {p2, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "inputState->guessing--;"

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-boolean v2, p2, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz v2, :cond_8

    instance-of v2, p2, Lantlr/ParserGrammar;

    if-nez v2, :cond_7

    instance-of p2, p2, Lantlr/LexerGrammar;

    if-eqz p2, :cond_8

    :cond_7
    const-string p2, "if (synPredMatched"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    iget v2, p1, Lantlr/AlternativeBlock;->ID:I

    invoke-virtual {p2, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "  fireSyntacticPredicateSucceeded();"

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "else"

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string p2, "  fireSyntacticPredicateFailed();"

    invoke-virtual {p0, p2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_8
    iget p2, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CppCodeGenerator;->syntacticPredLevel:I

    iget p2, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p2, p2, -0x1

    iput p2, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

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

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genTokenStrings(Ljava/lang/String;)V
    .locals 5

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "const char* "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "tokenNames[] = {"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    iget-object p1, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p1}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object p1

    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_2

    invoke-virtual {p1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    const-string v2, "<"

    if-nez v1, :cond_0

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-static {v0}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ">"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :cond_0
    const-string v3, "\""

    invoke-virtual {v1, v3}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v4

    if-nez v4, :cond_1

    invoke-virtual {v1, v2}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v2

    if-nez v2, :cond_1

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v2, v1}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v2

    if-eqz v2, :cond_1

    invoke-virtual {v2}, Lantlr/TokenSymbol;->getParaphrase()Ljava/lang/String;

    move-result-object v4

    if-eqz v4, :cond_1

    invoke-virtual {v2}, Lantlr/TokenSymbol;->getParaphrase()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1, v3, v3}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_1
    iget-object v2, p0, Lantlr/CodeGenerator;->charFormatter:Lantlr/CharFormatter;

    invoke-interface {v2, v1}, Lantlr/CharFormatter;->literalString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CodeGenerator;->print(Ljava/lang/String;)V

    const-string v1, ","

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->_println(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    const-string p1, "0"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string p1, "};"

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    return-void
.end method

.method public genTokenTypes(Lantlr/TokenManager;)V
    .locals 10

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v1, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    const-string v2, ".hpp"

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    const/4 v0, 0x1

    iput v0, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object v1, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    iget-object v2, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {v1, v2}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v1

    iput-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    const/4 v1, 0x0

    iput v1, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v1, "#ifndef INC_"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v2, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "_hpp_"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "#define INC_"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, ""

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    sget-object v2, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v2, :cond_0

    iget-object v3, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v2, v3}, Lantlr/NameSpace;->emitDeclarations(Ljava/io/PrintWriter;)V

    :cond_0
    iget-object v2, p0, Lantlr/CppCodeGenerator;->outputFile:Ljava/lang/String;

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->genHeader(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "#ifndef CUSTOM_API"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "# define CUSTOM_API"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v2, "#endif"

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const-string v1, "#ifdef __cplusplus"

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "struct CUSTOM_API "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v4, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v4, " {"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v0

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "enum {"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    add-int/2addr v3, v0

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-interface {p1}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v3

    const-string v4, "EOF_ = 1,"

    invoke-virtual {p0, v4}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    const/4 v4, 0x4

    :goto_0
    invoke-virtual {v3}, Lantlr/collections/impl/Vector;->size()I

    move-result v5

    if-ge v4, v5, :cond_6

    invoke-virtual {v3, v4}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Ljava/lang/String;

    if-eqz v5, :cond_5

    const-string v6, "\""

    invoke-virtual {v5, v6}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v6

    const-string v7, ","

    const-string v8, " = "

    if-eqz v6, :cond_4

    invoke-interface {p1, v5}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v6

    check-cast v6, Lantlr/StringLiteralSymbol;

    if-nez v6, :cond_1

    iget-object v6, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "String literal "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, " not in symbol table"

    invoke-virtual {v7, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v6, v5}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto :goto_3

    :cond_1
    iget-object v9, v6, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    if-eqz v9, :cond_2

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v6, v6, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    goto :goto_2

    :cond_2
    invoke-direct {p0, v5}, Lantlr/CppCodeGenerator;->mangleLiteral(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v9

    if-eqz v9, :cond_3

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iput-object v9, v6, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    goto :goto_3

    :cond_3
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "// "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    goto :goto_1

    :cond_4
    const-string v6, "<"

    invoke-virtual {v5, v6}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v6

    if-nez v6, :cond_5

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_1
    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    :goto_2
    invoke-virtual {p0, v5}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    :cond_5
    :goto_3
    add-int/lit8 v4, v4, 0x1

    goto/16 :goto_0

    :cond_6
    const-string v3, "NULL_TREE_LOOKAHEAD = 3"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v3, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v3, v0

    iput v3, p0, Lantlr/CodeGenerator;->tabs:I

    const-string v3, "};"

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    iget v4, p0, Lantlr/CodeGenerator;->tabs:I

    sub-int/2addr v4, v0

    iput v4, p0, Lantlr/CodeGenerator;->tabs:I

    invoke-virtual {p0, v1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v3}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    sget-object v0, Lantlr/CppCodeGenerator;->nameSpace:Lantlr/NameSpace;

    if-eqz v0, :cond_7

    iget-object v1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v0, v1}, Lantlr/NameSpace;->emitClosures(Ljava/io/PrintWriter;)V

    :cond_7
    const-string v0, "#endif /*INC_"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-interface {p1}, Lantlr/TokenManager;->getName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object p1, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    const-string v1, "_hpp_*/"

    invoke-static {v0, p1, v1, p0}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Lantlr/CppCodeGenerator;)V

    iget-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->exitIfError()V

    return-void
.end method

.method public getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;
    .locals 8

    const-string v0, ")"

    const-string v1, "astFactory->create("

    if-eqz p1, :cond_2

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_2

    iget-object v2, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getType()I

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/Vector;->ensureCapacity(I)V

    iget-object v2, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getType()I

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    if-nez v2, :cond_0

    goto :goto_0

    :cond_0
    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_1

    iget-object v3, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v4, "Attempt to redefine AST type for "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getText()Ljava/lang/String;

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

    invoke-virtual {v3, v4, v5, v6, v7}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    iget-object v3, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    const-string v4, " from \""

    const-string v5, "\" to \""

    invoke-static {v4, v2, v5}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, "\" sticking to \""

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\""

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    invoke-virtual {p1}, Lantlr/GrammarElement;->getColumn()I

    move-result p1

    invoke-virtual {v3, v2, p0, v4, p1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_1

    :cond_1
    :goto_0
    iget-object p0, p0, Lantlr/CppCodeGenerator;->astTypes:Lantlr/collections/impl/Vector;

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getASTNodeType()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/GrammarAtom;->getType()I

    move-result p1

    invoke-virtual {p0, v2, p1}, Lantlr/collections/impl/Vector;->setElementAt(Ljava/lang/Object;I)V

    :goto_1
    invoke-static {v1, p2, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_2
    const/16 p1, 0x2c

    invoke-virtual {p2, p1}, Ljava/lang/String;->indexOf(I)I

    move-result v2

    const/4 v3, -0x1

    const/4 v4, 0x0

    if-eq v2, v3, :cond_3

    iget-object v2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    iget-object v2, v2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p2, p1}, Ljava/lang/String;->indexOf(I)I

    move-result p1

    invoke-virtual {p2, v4, p1}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p1

    invoke-interface {v2, p1}, Lantlr/TokenManager;->tokenDefined(Ljava/lang/String;)Z

    move-result v4

    :cond_3
    iget-boolean p1, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz p1, :cond_4

    iget-object p0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of p1, p0, Lantlr/TreeWalkerGrammar;

    if-eqz p1, :cond_4

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0, p2}, Lantlr/TokenManager;->tokenDefined(Ljava/lang/String;)Z

    move-result p0

    if-nez p0, :cond_4

    if-nez v4, :cond_4

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p0

    sget-object p1, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "RefAST("

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "))"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_4
    invoke-static {v1, p2, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

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

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object p0, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "(astFactory->make((new "

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object p0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "ASTArray("

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "))"

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const/4 v1, 0x0

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    if-ge v1, v2, :cond_1

    const-string v2, "->add("

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {p1, v1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v3, ")"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getASTCreateString(Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

    if-eqz v0, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object p0, p0, Lantlr/CppCodeGenerator;->labeledElementASTType:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "(astFactory->create("

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object p0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "RefAST("

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, ")))"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    const-string p0, "astFactory->create("

    const-string v0, ")"

    invoke-static {p0, p1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

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

    const-string p0, "true"

    return-object p0

    :cond_1
    const-string p2, "("

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    iget-object p1, p1, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    invoke-virtual {p0, p1, v0}, Lantlr/CppCodeGenerator;->getLookaheadTestExpression([Lantlr/Lookahead;I)Ljava/lang/String;

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
    invoke-virtual {p0, v1, v3}, Lantlr/CppCodeGenerator;->getLookaheadTestTerm(ILantlr/collections/impl/BitSet;)Ljava/lang/String;

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

    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object v1

    invoke-static {v1}, Lantlr/CodeGenerator;->elementsAreRange([I)Z

    move-result v2

    if-eqz v2, :cond_0

    invoke-virtual {p0, p1, v1}, Lantlr/CppCodeGenerator;->getRangeExpression(I[I)Ljava/lang/String;

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

    invoke-direct {p0, v2}, Lantlr/CppCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v2

    if-lez p2, :cond_3

    const-string v3, " || "

    invoke-virtual {p1, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_3
    invoke-virtual {p1, v0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string v3, " == "

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

    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " >= "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, v0}, Lantlr/CppCodeGenerator;->getValueString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, " && "

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, p1}, Lantlr/CppCodeGenerator;->lookaheadString(I)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " <= "

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-direct {p0, p2}, Lantlr/CppCodeGenerator;->getValueString(I)Ljava/lang/String;

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
    .locals 7

    iget-object v0, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    if-nez v0, :cond_0

    return-object p1

    :cond_0
    iget-object v0, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    const-string v2, "_in"

    const/4 v3, 0x1

    const/4 v4, 0x0

    if-eqz v1, :cond_1

    iget-boolean v0, v0, Lantlr/Grammar;->buildAST:Z

    xor-int/2addr v0, v3

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v1

    const/4 v5, 0x3

    if-le v1, v5, :cond_2

    invoke-virtual {p1, v2}, Ljava/lang/String;->lastIndexOf(Ljava/lang/String;)I

    move-result v1

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v6

    sub-int/2addr v6, v5

    if-ne v1, v6, :cond_2

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v0

    sub-int/2addr v0, v5

    invoke-virtual {p1, v4, v0}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p1

    move v0, v3

    goto :goto_0

    :cond_1
    move v0, v4

    :cond_2
    :goto_0
    iget-object v1, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    const-string v3, "_AST"

    if-ge v4, v1, :cond_5

    iget-object v1, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v1, v4}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/AlternativeElement;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_4

    if-eqz v0, :cond_3

    goto :goto_1

    :cond_3
    invoke-static {p1, v3}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    :goto_1
    return-object p1

    :cond_4
    add-int/lit8 v4, v4, 0x1

    goto :goto_0

    :cond_5
    iget-object v1, p0, Lantlr/CppCodeGenerator;->treeVariableMap:Ljava/util/Hashtable;

    invoke-virtual {v1, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    if-eqz v1, :cond_9

    sget-object p2, Lantlr/CppCodeGenerator;->NONUNIQUE:Ljava/lang/String;

    const/4 v3, 0x0

    const-string v4, " in rule "

    const-string v5, "Ambiguous reference to AST element "

    if-ne v1, p2, :cond_6

    :goto_2
    iget-object p2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-static {v5, p1, v4}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object p0, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-object v3

    :cond_6
    iget-object p2, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p2}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v1, p2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_7

    goto :goto_2

    :cond_7
    if-eqz v0, :cond_8

    invoke-static {v1, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_8
    return-object v1

    :cond_9
    iget-object p0, p0, Lantlr/CppCodeGenerator;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {p0}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_c

    if-eqz v0, :cond_a

    const-string p0, "_AST_in"

    invoke-static {p1, p0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    goto :goto_3

    :cond_a
    invoke-static {p1, v3}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    :goto_3
    if-eqz p2, :cond_b

    if-nez v0, :cond_b

    iput-object p0, p2, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    :cond_b
    return-object p0

    :cond_c
    return-object p1
.end method

.method public printAction(Lantlr/Token;)V
    .locals 2

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->genLineNo(I)V

    invoke-virtual {p0}, Lantlr/CodeGenerator;->printTabs()V

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p1

    const/4 v1, 0x0

    invoke-virtual {p0, v0, p1, v1, v1}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->_printAction(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

    :cond_0
    return-void
.end method

.method public printHeaderAction(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/CodeGenerator;->behavior:Lantlr/DefineGrammarSymbols;

    iget-object v0, v0, Lantlr/DefineGrammarSymbols;->headerActions:Ljava/util/Hashtable;

    invoke-virtual {v0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lantlr/Token;

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p0, v0}, Lantlr/CppCodeGenerator;->genLineNo(I)V

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p1

    const/4 v1, 0x0

    invoke-virtual {p0, v0, p1, v1, v1}, Lantlr/CppCodeGenerator;->processActionForSpecialSymbols(Ljava/lang/String;ILantlr/RuleBlock;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/CppCodeGenerator;->genLineNo2()V

    :cond_0
    return-void
.end method

.method public println(Ljava/lang/String;)V
    .locals 2

    if-eqz p1, :cond_0

    invoke-virtual {p0}, Lantlr/CodeGenerator;->printTabs()V

    iget v0, p0, Lantlr/CppCodeGenerator;->outputLine:I

    invoke-virtual {p0, p1}, Lantlr/CppCodeGenerator;->countLines(Ljava/lang/String;)I

    move-result v1

    add-int/lit8 v1, v1, 0x1

    add-int/2addr v1, v0

    iput v1, p0, Lantlr/CppCodeGenerator;->outputLine:I

    iget-object p0, p0, Lantlr/CodeGenerator;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {p0, p1}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    :cond_0
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
    new-instance v1, Lantlr/actions/cpp/ActionLexer;

    invoke-direct {v1, p1, p3, p0, p4}, Lantlr/actions/cpp/ActionLexer;-><init>(Ljava/lang/String;Lantlr/RuleBlock;Lantlr/CodeGenerator;Lantlr/ActionTransInfo;)V

    invoke-virtual {v1, p2}, Lantlr/actions/cpp/ActionLexer;->setLineOffset(I)V

    iget-object p2, p0, Lantlr/CodeGenerator;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v1, p2}, Lantlr/CharScanner;->setFilename(Ljava/lang/String;)V

    iget-object p2, p0, Lantlr/CodeGenerator;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1, p2}, Lantlr/actions/cpp/ActionLexer;->setTool(Lantlr/Tool;)V

    const/4 p2, 0x1

    :try_start_0
    invoke-virtual {v1, p2}, Lantlr/actions/cpp/ActionLexer;->mACTION(Z)V

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

    invoke-virtual {v1, p0}, Lantlr/actions/cpp/ActionLexer;->reportError(Lantlr/RecognitionException;)V

    return-object p1

    :cond_6
    :goto_1
    const/4 p0, 0x0

    return-object p0
.end method

.method public processStringForASTConstructor(Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    iget-boolean v0, p0, Lantlr/CppCodeGenerator;->usingCustomAST:Z

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

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v0, Lantlr/CppCodeGenerator;->namespaceAntlr:Ljava/lang/String;

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "RefAST("

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ")"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_1
    return-object p1
.end method
