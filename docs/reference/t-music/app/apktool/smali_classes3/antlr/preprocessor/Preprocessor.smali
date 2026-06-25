.class public Lantlr/preprocessor/Preprocessor;
.super Lantlr/LLkParser;
.source ""

# interfaces
.implements Lantlr/preprocessor/PreprocessorTokenTypes;


# static fields
.field public static final _tokenNames:[Ljava/lang/String;

.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_1:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_2:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_3:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_4:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_5:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_6:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_7:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_8:Lantlr/collections/impl/BitSet;


# instance fields
.field public antlrTool:Lantlr/Tool;


# direct methods
.method public static constructor <clinit>()V
    .locals 43

    const-string v0, "<0>"

    const-string v1, "EOF"

    const-string v2, "<2>"

    const-string v3, "NULL_TREE_LOOKAHEAD"

    const-string v4, "\"tokens\""

    const-string v5, "HEADER_ACTION"

    const-string v6, "SUBRULE_BLOCK"

    const-string v7, "ACTION"

    const-string v8, "\"class\""

    const-string v9, "ID"

    const-string v10, "\"extends\""

    const-string v11, "SEMI"

    const-string v12, "TOKENS_SPEC"

    const-string v13, "OPTIONS_START"

    const-string v14, "ASSIGN_RHS"

    const-string v15, "RCURLY"

    const-string v16, "\"protected\""

    const-string v17, "\"private\""

    const-string v18, "\"public\""

    const-string v19, "BANG"

    const-string v20, "ARG_ACTION"

    const-string v21, "\"returns\""

    const-string v22, "RULE_BLOCK"

    const-string v23, "\"throws\""

    const-string v24, "COMMA"

    const-string v25, "\"exception\""

    const-string v26, "\"catch\""

    const-string v27, "ALT"

    const-string v28, "ELEMENT"

    const-string v29, "LPAREN"

    const-string v30, "RPAREN"

    const-string v31, "ID_OR_KEYWORD"

    const-string v32, "CURLY_BLOCK_SCARF"

    const-string v33, "WS"

    const-string v34, "NEWLINE"

    const-string v35, "COMMENT"

    const-string v36, "SL_COMMENT"

    const-string v37, "ML_COMMENT"

    const-string v38, "CHAR_LITERAL"

    const-string v39, "STRING_LITERAL"

    const-string v40, "ESC"

    const-string v41, "DIGIT"

    const-string v42, "XDIGIT"

    filled-new-array/range {v0 .. v42}, [Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenNames:[Ljava/lang/String;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_1()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_2()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_3()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_4()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_5()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_6()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_7()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_7:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/Preprocessor;->mk_tokenSet_8()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_8:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/ParserSharedInputState;)V
    .locals 1

    const/4 v0, 0x1

    invoke-direct {p0, p1, v0}, Lantlr/LLkParser;-><init>(Lantlr/ParserSharedInputState;I)V

    sget-object p1, Lantlr/preprocessor/Preprocessor;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;)V
    .locals 1

    const/4 v0, 0x1

    invoke-direct {p0, p1, v0}, Lantlr/preprocessor/Preprocessor;-><init>(Lantlr/TokenBuffer;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenBuffer;I)V

    sget-object p1, Lantlr/preprocessor/Preprocessor;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;)V
    .locals 1

    const/4 v0, 0x1

    invoke-direct {p0, p1, v0}, Lantlr/preprocessor/Preprocessor;-><init>(Lantlr/TokenStream;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenStream;I)V

    sget-object p1, Lantlr/preprocessor/Preprocessor;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public static final mk_tokenSet_0()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x2
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_1()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x471382
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_2()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x70200
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_3()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x182
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_4()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x800
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_5()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x70382
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_6()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x402080
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_7()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x2070382
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_8()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x6070382
        0x0
    .end array-data
.end method


# virtual methods
.method public final class_def(Ljava/lang/String;Lantlr/preprocessor/Hierarchy;)Lantlr/preprocessor/Grammar;
    .locals 11

    new-instance v0, Lantlr/collections/impl/IndexedVector;

    const/16 v1, 0x64

    invoke-direct {v0, v1}, Lantlr/collections/impl/IndexedVector;-><init>(I)V

    const/4 v1, 0x0

    const/4 v2, 0x1

    :try_start_0
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    const/16 v4, 0x8

    const/4 v5, 0x7

    if-eq v3, v5, :cond_1

    if-ne v3, v4, :cond_0

    move-object v3, v1

    goto :goto_0

    :cond_0
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p1, p2, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_1
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v3

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    :goto_0
    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v4

    const/16 v6, 0x9

    invoke-virtual {p0, v6}, Lantlr/Parser;->match(I)V

    const/16 v7, 0xa

    invoke-virtual {p0, v7}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v7

    invoke-virtual {p0, v6}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v8

    const/4 v9, 0x6

    const/16 v10, 0xb

    if-eq v8, v9, :cond_3

    if-ne v8, v10, :cond_2

    move-object v8, v1

    goto :goto_1

    :cond_2
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p1, p2, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_3
    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->superClass()Ljava/lang/String;

    move-result-object v8

    :goto_1
    invoke-virtual {p0, v10}, Lantlr/Parser;->match(I)V

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {p2, v9}, Lantlr/preprocessor/Hierarchy;->getGrammar(Ljava/lang/String;)Lantlr/preprocessor/Grammar;

    move-result-object v9
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_2

    if-nez v9, :cond_d

    :try_start_1
    new-instance p1, Lantlr/preprocessor/Grammar;

    invoke-virtual {p2}, Lantlr/preprocessor/Hierarchy;->getTool()Lantlr/Tool;

    move-result-object p2

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v7}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v7

    invoke-direct {p1, p2, v4, v7, v0}, Lantlr/preprocessor/Grammar;-><init>(Lantlr/Tool;Ljava/lang/String;Ljava/lang/String;Lantlr/collections/impl/IndexedVector;)V
    :try_end_1
    .catch Lantlr/RecognitionException; {:try_start_1 .. :try_end_1} :catch_1

    :try_start_2
    iput-object v8, p1, Lantlr/preprocessor/Grammar;->superClass:Ljava/lang/String;

    if-eqz v3, :cond_4

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Lantlr/preprocessor/Grammar;->setPreambleAction(Ljava/lang/String;)V

    :cond_4
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result p2

    const/16 v0, 0xc

    if-eq p2, v5, :cond_6

    if-eq p2, v6, :cond_6

    if-eq p2, v0, :cond_6

    const/16 v3, 0xd

    if-eq p2, v3, :cond_5

    packed-switch p2, :pswitch_data_0

    new-instance p2, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p2, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p2

    :cond_5
    invoke-virtual {p0, p1}, Lantlr/preprocessor/Preprocessor;->optionSpec(Lantlr/preprocessor/Grammar;)Lantlr/collections/impl/IndexedVector;

    move-result-object v1

    :cond_6
    :pswitch_0
    invoke-virtual {p1, v1}, Lantlr/preprocessor/Grammar;->setOptions(Lantlr/collections/impl/IndexedVector;)V

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result p2

    if-eq p2, v5, :cond_8

    if-eq p2, v6, :cond_8

    if-eq p2, v0, :cond_7

    packed-switch p2, :pswitch_data_1

    new-instance p2, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p2, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p2

    :cond_7
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Lantlr/preprocessor/Grammar;->setTokenSection(Ljava/lang/String;)V

    :cond_8
    :pswitch_1
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result p2

    if-eq p2, v5, :cond_9

    if-eq p2, v6, :cond_a

    packed-switch p2, :pswitch_data_2

    new-instance p2, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p2, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p2

    :cond_9
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Lantlr/preprocessor/Grammar;->setMemberAction(Ljava/lang/String;)V

    :cond_a
    :pswitch_2
    const/4 p2, 0x0

    :goto_2
    sget-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    invoke-virtual {v0, v1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v0

    if-eqz v0, :cond_b

    invoke-virtual {p0, p1}, Lantlr/preprocessor/Preprocessor;->rule(Lantlr/preprocessor/Grammar;)V

    add-int/lit8 p2, p2, 0x1

    goto :goto_2

    :cond_b
    if-lt p2, v2, :cond_c

    goto :goto_4

    :cond_c
    new-instance p2, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p2, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p2
    :try_end_2
    .catch Lantlr/RecognitionException; {:try_start_2 .. :try_end_2} :catch_0

    :catch_0
    move-exception p2

    goto :goto_3

    :catch_1
    move-exception p1

    move-object p2, p1

    move-object p1, v9

    goto :goto_3

    :cond_d
    :try_start_3
    new-instance p2, Lantlr/SemanticException;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "redefinition of grammar "

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v4}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {v4}, Lantlr/Token;->getColumn()I

    move-result v3

    invoke-direct {p2, v0, p1, v2, v3}, Lantlr/SemanticException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    throw p2
    :try_end_3
    .catch Lantlr/RecognitionException; {:try_start_3 .. :try_end_3} :catch_2

    :catch_2
    move-exception p1

    move-object p2, p1

    move-object p1, v1

    :goto_3
    invoke-virtual {p0, p2}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object p2, Lantlr/preprocessor/Preprocessor;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p2}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :goto_4
    return-object p1

    nop

    :pswitch_data_0
    .packed-switch 0x10
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x10
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x10
        :pswitch_2
        :pswitch_2
        :pswitch_2
    .end packed-switch
.end method

.method public final exceptionGroup()Ljava/lang/String;
    .locals 3

    const-string v0, ""

    :goto_0
    const/4 v1, 0x1

    :try_start_0
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x19

    if-ne v1, v2, :cond_0

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->exceptionSpec()Ljava/lang/String;

    move-result-object v1

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception v1

    invoke-virtual {p0, v1}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object v1, Lantlr/preprocessor/Preprocessor;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :cond_0
    return-object v0
.end method

.method public final exceptionHandler()Ljava/lang/String;
    .locals 4

    const/16 v0, 0x1a

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    const/16 v2, 0x14

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    const/4 v2, 0x7

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "line.separator"

    invoke-static {v3}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "catch "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " "

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception v0

    invoke-virtual {p0, v0}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object v0, Lantlr/preprocessor/Preprocessor;->_tokenSet_8:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v0}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    const/4 p0, 0x0

    :goto_0
    return-object p0
.end method

.method public final exceptionSpec()Ljava/lang/String;
    .locals 6

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "line.separator"

    invoke-static {v1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "exception "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const/16 v1, 0x19

    :try_start_0
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    const/16 v4, 0x1a

    if-eq v3, v2, :cond_1

    const/16 v5, 0x14

    if-eq v3, v5, :cond_0

    const/4 v5, 0x7

    if-eq v3, v5, :cond_1

    const/16 v5, 0x8

    if-eq v3, v5, :cond_1

    const/16 v5, 0x9

    if-eq v3, v5, :cond_1

    if-eq v3, v1, :cond_1

    if-eq v3, v4, :cond_1

    packed-switch v3, :pswitch_data_0

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-direct {v1, v2, v3}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    :goto_0
    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_2

    :cond_1
    :goto_1
    :pswitch_0
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-ne v1, v4, :cond_2

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->exceptionHandler()Ljava/lang/String;

    move-result-object v1

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :goto_2
    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception v1

    invoke-virtual {p0, v1}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object v1, Lantlr/preprocessor/Preprocessor;->_tokenSet_7:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :cond_2
    return-object v0

    nop

    :pswitch_data_0
    .packed-switch 0x10
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public getTool()Lantlr/Tool;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Preprocessor;->antlrTool:Lantlr/Tool;

    return-object p0
.end method

.method public final grammarFile(Lantlr/preprocessor/Hierarchy;Ljava/lang/String;)V
    .locals 6

    :goto_0
    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v2, 0x5

    if-ne v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p1, p2}, Lantlr/preprocessor/Hierarchy;->getFile(Ljava/lang/String;)Lantlr/preprocessor/GrammarFile;

    move-result-object v1

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Lantlr/preprocessor/GrammarFile;->addHeaderAction(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x8

    const/4 v3, 0x7

    const/4 v4, 0x0

    if-eq v1, v0, :cond_3

    const/16 v5, 0xd

    if-eq v1, v5, :cond_2

    if-eq v1, v3, :cond_3

    if-ne v1, v2, :cond_1

    goto :goto_1

    :cond_1
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p1, p2, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_2
    invoke-virtual {p0, v4}, Lantlr/preprocessor/Preprocessor;->optionSpec(Lantlr/preprocessor/Grammar;)Lantlr/collections/impl/IndexedVector;

    move-result-object v4

    :cond_3
    :goto_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v3, :cond_5

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-ne v1, v2, :cond_4

    goto :goto_2

    :cond_4
    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    goto :goto_3

    :cond_5
    :goto_2
    invoke-virtual {p0, p2, p1}, Lantlr/preprocessor/Preprocessor;->class_def(Ljava/lang/String;Lantlr/preprocessor/Hierarchy;)Lantlr/preprocessor/Grammar;

    move-result-object v1

    if-eqz v1, :cond_6

    if-eqz v4, :cond_6

    invoke-virtual {p1, p2}, Lantlr/preprocessor/Hierarchy;->getFile(Ljava/lang/String;)Lantlr/preprocessor/GrammarFile;

    move-result-object v5

    invoke-virtual {v5, v4}, Lantlr/preprocessor/GrammarFile;->setOptions(Lantlr/collections/impl/IndexedVector;)V

    :cond_6
    if-eqz v1, :cond_3

    invoke-virtual {v1, p2}, Lantlr/preprocessor/Grammar;->setFileName(Ljava/lang/String;)V

    invoke-virtual {p1, v1}, Lantlr/preprocessor/Hierarchy;->addGrammar(Lantlr/preprocessor/Grammar;)V
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception p1

    invoke-virtual {p0, p1}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object p1, Lantlr/preprocessor/Preprocessor;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :goto_3
    return-void
.end method

.method public final optionSpec(Lantlr/preprocessor/Grammar;)Lantlr/collections/impl/IndexedVector;
    .locals 7

    new-instance v0, Lantlr/collections/impl/IndexedVector;

    invoke-direct {v0}, Lantlr/collections/impl/IndexedVector;-><init>()V

    const/16 v1, 0xd

    :try_start_0
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    :cond_0
    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v3, 0x9

    if-ne v2, v3, :cond_2

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v3

    const/16 v4, 0xe

    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    new-instance v4, Lantlr/preprocessor/Option;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v6

    invoke-direct {v4, v5, v6, p1}, Lantlr/preprocessor/Option;-><init>(Ljava/lang/String;Ljava/lang/String;Lantlr/preprocessor/Grammar;)V

    invoke-virtual {v4}, Lantlr/preprocessor/Option;->getName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v0, v5, v4}, Lantlr/collections/impl/IndexedVector;->appendElement(Ljava/lang/Object;Ljava/lang/Object;)V

    if-eqz p1, :cond_1

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    const-string v5, "importVocab"

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_1

    iput-boolean v1, p1, Lantlr/preprocessor/Grammar;->specifiedVocabulary:Z

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    iput-object v1, p1, Lantlr/preprocessor/Grammar;->importVocab:Ljava/lang/String;

    goto :goto_0

    :cond_1
    if-eqz p1, :cond_0

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    const-string v4, "exportVocab"

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_0

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    const/4 v4, 0x0

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v3

    sub-int/2addr v3, v1

    invoke-virtual {v2, v4, v3}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v1

    iput-object v1, p1, Lantlr/preprocessor/Grammar;->exportVocab:Ljava/lang/String;

    iget-object v1, p1, Lantlr/preprocessor/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v1

    iput-object v1, p1, Lantlr/preprocessor/Grammar;->exportVocab:Ljava/lang/String;

    goto :goto_0

    :cond_2
    const/16 p1, 0xf

    invoke-virtual {p0, p1}, Lantlr/Parser;->match(I)V
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception p1

    invoke-virtual {p0, p1}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object p1, Lantlr/preprocessor/Preprocessor;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :goto_1
    return-object v0
.end method

.method public reportError(Lantlr/RecognitionException;)V
    .locals 3

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->getTool()Lantlr/Tool;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->getTool()Lantlr/Tool;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/RecognitionException;->getErrorMessage()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/RecognitionException;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/RecognitionException;->getLine()I

    move-result v2

    invoke-virtual {p1}, Lantlr/RecognitionException;->getColumn()I

    move-result p1

    invoke-virtual {p0, v0, v1, v2, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_0

    :cond_0
    invoke-super {p0, p1}, Lantlr/Parser;->reportError(Lantlr/RecognitionException;)V

    :goto_0
    return-void
.end method

.method public reportError(Ljava/lang/String;)V
    .locals 2

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->getTool()Lantlr/Tool;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->getTool()Lantlr/Tool;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    const/4 v1, -0x1

    invoke-virtual {v0, p1, p0, v1, v1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_0

    :cond_0
    invoke-super {p0, p1}, Lantlr/Parser;->reportError(Ljava/lang/String;)V

    :goto_0
    return-void
.end method

.method public reportWarning(Ljava/lang/String;)V
    .locals 2

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->getTool()Lantlr/Tool;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->getTool()Lantlr/Tool;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    const/4 v1, -0x1

    invoke-virtual {v0, p1, p0, v1, v1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_0

    :cond_0
    invoke-super {p0, p1}, Lantlr/Parser;->reportWarning(Ljava/lang/String;)V

    :goto_0
    return-void
.end method

.method public final rule(Lantlr/preprocessor/Grammar;)V
    .locals 12

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x9

    const/4 v3, 0x0

    if-eq v1, v2, :cond_0

    packed-switch v1, :pswitch_data_0

    new-instance p1, Lantlr/NoViableAltException;

    goto :goto_0

    :pswitch_0
    const/16 v1, 0x12

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    const-string v1, "public"

    goto :goto_1

    :pswitch_1
    const/16 v1, 0x11

    :try_start_1
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V
    :try_end_1
    .catch Lantlr/RecognitionException; {:try_start_1 .. :try_end_1} :catch_0

    const-string v1, "private"

    goto :goto_1

    :pswitch_2
    const/16 v1, 0x10

    :try_start_2
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V
    :try_end_2
    .catch Lantlr/RecognitionException; {:try_start_2 .. :try_end_2} :catch_0

    const-string v1, "protected"

    goto :goto_1

    :goto_0
    :try_start_3
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p1, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_0
    move-object v1, v3

    :goto_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v4

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v5, 0xd

    const/4 v6, 0x7

    if-eq v2, v6, :cond_1

    if-eq v2, v5, :cond_1

    packed-switch v2, :pswitch_data_1

    new-instance p1, Lantlr/NoViableAltException;

    goto :goto_2

    :pswitch_3
    const/16 v2, 0x13

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    move v2, v0

    goto :goto_3

    :goto_2
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p1, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_1
    :pswitch_4
    const/4 v2, 0x0

    :goto_3
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v7

    const/16 v8, 0x14

    if-eq v7, v6, :cond_2

    if-eq v7, v5, :cond_2

    packed-switch v7, :pswitch_data_2

    new-instance p1, Lantlr/NoViableAltException;

    goto :goto_4

    :pswitch_5
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v7

    invoke-virtual {p0, v8}, Lantlr/Parser;->match(I)V

    goto :goto_5

    :goto_4
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p1, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_2
    :pswitch_6
    move-object v7, v3

    :goto_5
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    if-eq v9, v6, :cond_3

    if-eq v9, v5, :cond_3

    packed-switch v9, :pswitch_data_3

    new-instance p1, Lantlr/NoViableAltException;

    goto :goto_6

    :pswitch_7
    const/16 v9, 0x15

    invoke-virtual {p0, v9}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v9

    invoke-virtual {p0, v8}, Lantlr/Parser;->match(I)V

    goto :goto_7

    :goto_6
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p1, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_3
    :pswitch_8
    move-object v9, v3

    :goto_7
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v8

    const/16 v10, 0x16

    if-eq v8, v6, :cond_5

    if-eq v8, v5, :cond_5

    if-eq v8, v10, :cond_5

    const/16 v11, 0x17

    if-ne v8, v11, :cond_4

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->throwsSpec()Ljava/lang/String;

    move-result-object v8

    goto :goto_8

    :cond_4
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p1, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1
    :try_end_3
    .catch Lantlr/RecognitionException; {:try_start_3 .. :try_end_3} :catch_0

    :cond_5
    const-string v8, ""

    :goto_8
    :try_start_4
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v11

    if-eq v11, v6, :cond_8

    if-eq v11, v5, :cond_7

    if-ne v11, v10, :cond_6

    goto :goto_9

    :cond_6
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p1, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_7
    invoke-virtual {p0, v3}, Lantlr/preprocessor/Preprocessor;->optionSpec(Lantlr/preprocessor/Grammar;)Lantlr/collections/impl/IndexedVector;

    move-result-object v5

    goto :goto_a

    :cond_8
    :goto_9
    move-object v5, v3

    :goto_a
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v11

    if-eq v11, v6, :cond_a

    if-ne v11, v10, :cond_9

    goto :goto_b

    :cond_9
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p1, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_a
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v3

    invoke-virtual {p0, v6}, Lantlr/Parser;->match(I)V

    :goto_b
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v10}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/preprocessor/Preprocessor;->exceptionGroup()Ljava/lang/String;

    move-result-object v6

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v10, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    new-instance v6, Lantlr/preprocessor/Rule;

    invoke-virtual {v4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v4

    invoke-direct {v6, v4, v0, v5, p1}, Lantlr/preprocessor/Rule;-><init>(Ljava/lang/String;Ljava/lang/String;Lantlr/collections/impl/IndexedVector;Lantlr/preprocessor/Grammar;)V

    invoke-virtual {v6, v8}, Lantlr/preprocessor/Rule;->setThrowsSpec(Ljava/lang/String;)V

    if-eqz v7, :cond_b

    invoke-virtual {v7}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v6, v0}, Lantlr/preprocessor/Rule;->setArgs(Ljava/lang/String;)V

    :cond_b
    if-eqz v9, :cond_c

    invoke-virtual {v9}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v6, v0}, Lantlr/preprocessor/Rule;->setReturnValue(Ljava/lang/String;)V

    :cond_c
    if-eqz v3, :cond_d

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v6, v0}, Lantlr/preprocessor/Rule;->setInitAction(Ljava/lang/String;)V

    :cond_d
    if-eqz v2, :cond_e

    invoke-virtual {v6}, Lantlr/preprocessor/Rule;->setBang()V

    :cond_e
    invoke-virtual {v6, v1}, Lantlr/preprocessor/Rule;->setVisibility(Ljava/lang/String;)V

    if-eqz p1, :cond_f

    invoke-virtual {p1, v6}, Lantlr/preprocessor/Grammar;->addRule(Lantlr/preprocessor/Rule;)V
    :try_end_4
    .catch Lantlr/RecognitionException; {:try_start_4 .. :try_end_4} :catch_0

    goto :goto_c

    :catch_0
    move-exception p1

    invoke-virtual {p0, p1}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object p1, Lantlr/preprocessor/Preprocessor;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :cond_f
    :goto_c
    return-void

    :pswitch_data_0
    .packed-switch 0x10
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x13
        :pswitch_3
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x14
        :pswitch_5
        :pswitch_6
        :pswitch_6
        :pswitch_6
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x15
        :pswitch_7
        :pswitch_8
        :pswitch_8
    .end packed-switch
.end method

.method public setTool(Lantlr/Tool;)V
    .locals 1

    iget-object v0, p0, Lantlr/preprocessor/Preprocessor;->antlrTool:Lantlr/Tool;

    if-nez v0, :cond_0

    iput-object p1, p0, Lantlr/preprocessor/Preprocessor;->antlrTool:Lantlr/Tool;

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalStateException;

    const-string p1, "antlr.Tool already registered"

    invoke-direct {p0, p1}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public final superClass()Ljava/lang/String;
    .locals 2

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const/4 v1, 0x6

    :try_start_0
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception v1

    invoke-virtual {p0, v1}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object v1, Lantlr/preprocessor/Preprocessor;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :goto_0
    return-object v0
.end method

.method public final throwsSpec()Ljava/lang/String;
    .locals 6

    const-string v0, "throws "

    const/16 v1, 0x17

    :try_start_0
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    const/16 v3, 0x9

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    :goto_0
    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v4, 0x18

    if-ne v2, v4, :cond_0

    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ","

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception v1

    invoke-virtual {p0, v1}, Lantlr/preprocessor/Preprocessor;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object v1, Lantlr/preprocessor/Preprocessor;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :cond_0
    return-object v0
.end method
