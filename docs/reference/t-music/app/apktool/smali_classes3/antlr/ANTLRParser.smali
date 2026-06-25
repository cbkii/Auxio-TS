.class public Lantlr/ANTLRParser;
.super Lantlr/LLkParser;
.source ""

# interfaces
.implements Lantlr/ANTLRTokenTypes;


# static fields
.field public static final DEBUG_PARSER:Z = false

.field public static final _tokenNames:[Ljava/lang/String;

.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_1:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_10:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_11:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_2:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_3:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_4:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_5:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_6:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_7:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_8:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_9:Lantlr/collections/impl/BitSet;


# instance fields
.field public antlrTool:Lantlr/Tool;

.field public behavior:Lantlr/ANTLRGrammarParseBehavior;

.field public blockNesting:I


# direct methods
.method public static constructor <clinit>()V
    .locals 64

    const-string v0, "<0>"

    const-string v1, "EOF"

    const-string v2, "<2>"

    const-string v3, "NULL_TREE_LOOKAHEAD"

    const-string v4, "\"tokens\""

    const-string v5, "\"header\""

    const-string v6, "STRING_LITERAL"

    const-string v7, "ACTION"

    const-string v8, "DOC_COMMENT"

    const-string v9, "\"lexclass\""

    const-string v10, "\"class\""

    const-string v11, "\"extends\""

    const-string v12, "\"Lexer\""

    const-string v13, "\"TreeParser\""

    const-string v14, "OPTIONS"

    const-string v15, "ASSIGN"

    const-string v16, "SEMI"

    const-string v17, "RCURLY"

    const-string v18, "\"charVocabulary\""

    const-string v19, "CHAR_LITERAL"

    const-string v20, "INT"

    const-string v21, "OR"

    const-string v22, "RANGE"

    const-string v23, "TOKENS"

    const-string v24, "TOKEN_REF"

    const-string v25, "OPEN_ELEMENT_OPTION"

    const-string v26, "CLOSE_ELEMENT_OPTION"

    const-string v27, "LPAREN"

    const-string v28, "RPAREN"

    const-string v29, "\"Parser\""

    const-string v30, "\"protected\""

    const-string v31, "\"public\""

    const-string v32, "\"private\""

    const-string v33, "BANG"

    const-string v34, "ARG_ACTION"

    const-string v35, "\"returns\""

    const-string v36, "COLON"

    const-string v37, "\"throws\""

    const-string v38, "COMMA"

    const-string v39, "\"exception\""

    const-string v40, "\"catch\""

    const-string v41, "RULE_REF"

    const-string v42, "NOT_OP"

    const-string v43, "SEMPRED"

    const-string v44, "TREE_BEGIN"

    const-string v45, "QUESTION"

    const-string v46, "STAR"

    const-string v47, "PLUS"

    const-string v48, "IMPLIES"

    const-string v49, "CARET"

    const-string v50, "WILDCARD"

    const-string v51, "\"options\""

    const-string v52, "WS"

    const-string v53, "COMMENT"

    const-string v54, "SL_COMMENT"

    const-string v55, "ML_COMMENT"

    const-string v56, "ESC"

    const-string v57, "DIGIT"

    const-string v58, "XDIGIT"

    const-string v59, "NESTED_ARG_ACTION"

    const-string v60, "NESTED_ACTION"

    const-string v61, "WS_LOOP"

    const-string v62, "INTERNAL_RULE_REF"

    const-string v63, "WS_OPT"

    filled-new-array/range {v0 .. v63}, [Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lantlr/ANTLRParser;->_tokenNames:[Ljava/lang/String;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_1()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_2()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_3()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_4()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_5()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_6()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_7()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_7:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_8()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_8:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_9()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_9:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_10()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_10:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRParser;->mk_tokenSet_11()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRParser;->_tokenSet_11:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/ParserSharedInputState;)V
    .locals 1

    const/4 v0, 0x2

    invoke-direct {p0, p1, v0}, Lantlr/LLkParser;-><init>(Lantlr/ParserSharedInputState;I)V

    const/4 p1, -0x1

    iput p1, p0, Lantlr/ANTLRParser;->blockNesting:I

    sget-object p1, Lantlr/ANTLRParser;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;)V
    .locals 1

    const/4 v0, 0x2

    invoke-direct {p0, p1, v0}, Lantlr/ANTLRParser;-><init>(Lantlr/TokenBuffer;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenBuffer;I)V

    const/4 p1, -0x1

    iput p1, p0, Lantlr/ANTLRParser;->blockNesting:I

    sget-object p1, Lantlr/ANTLRParser;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;Lantlr/ANTLRGrammarParseBehavior;Lantlr/Tool;)V
    .locals 1

    const/4 v0, 0x1

    invoke-direct {p0, p1, v0}, Lantlr/LLkParser;-><init>(Lantlr/TokenBuffer;I)V

    const/4 p1, -0x1

    iput p1, p0, Lantlr/ANTLRParser;->blockNesting:I

    sget-object p1, Lantlr/ANTLRParser;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    iput-object p2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    iput-object p3, p0, Lantlr/ANTLRParser;->antlrTool:Lantlr/Tool;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;)V
    .locals 1

    const/4 v0, 0x2

    invoke-direct {p0, p1, v0}, Lantlr/ANTLRParser;-><init>(Lantlr/TokenStream;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenStream;I)V

    const/4 p1, -0x1

    iput p1, p0, Lantlr/ANTLRParser;->blockNesting:I

    sget-object p1, Lantlr/ANTLRParser;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method private checkForMissingEndRule(Lantlr/Token;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v0

    const/4 v1, 0x1

    if-ne v0, v1, :cond_0

    iget-object v0, p0, Lantlr/ANTLRParser;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v2, "did you forget to terminate previous rule?"

    invoke-virtual {v0, v2, p0, v1, p1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    return-void
.end method

.method private lastInRule()Z
    .locals 3

    iget v0, p0, Lantlr/ANTLRParser;->blockNesting:I

    if-nez v0, :cond_1

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x10

    if-eq v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x27

    if-eq v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p0

    const/16 v1, 0x15

    if-ne p0, v1, :cond_1

    :cond_0
    return v0

    :cond_1
    const/4 p0, 0x0

    return p0
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
        0x201c1000100L
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
        0x23fc1004080L
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_10()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff961b69c0c0L
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_11()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x61e06090800c0L
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
        0x41e00090800c0L
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
        0x41e861b2900c0L
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
        0x4060009080040L
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
        0x61e961b6940c0L
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
        0x61e861b6940c0L
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
        0x4000001080040L
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
        0x61e861b2900c0L
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_9()[J
    .locals 1

    const/4 v0, 0x2

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x41e82192800c0L
        0x0
    .end array-data
.end method


# virtual methods
.method public final alternative()V
    .locals 7

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v2, 0x6

    const/16 v3, 0x1c

    const/16 v4, 0x27

    const/16 v5, 0x15

    const/16 v6, 0x10

    if-eq v1, v2, :cond_1

    const/4 v2, 0x7

    if-eq v1, v2, :cond_1

    if-eq v1, v6, :cond_1

    const/16 v2, 0x13

    if-eq v1, v2, :cond_1

    if-eq v1, v5, :cond_1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    const/16 v2, 0x21

    if-eq v1, v2, :cond_0

    if-eq v1, v4, :cond_1

    const/16 v2, 0x32

    if-eq v1, v2, :cond_1

    const/16 v2, 0x1b

    if-eq v1, v2, :cond_1

    if-eq v1, v3, :cond_1

    packed-switch v1, :pswitch_data_0

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_1

    const/4 v1, 0x0

    goto :goto_0

    :cond_1
    :pswitch_0
    move v1, v0

    :goto_0
    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_2

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v2, v1}, Lantlr/ANTLRGrammarParseBehavior;->beginAlt(Z)V

    :cond_2
    :goto_1
    sget-object v1, Lantlr/ANTLRParser;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    invoke-virtual {v1, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_3

    invoke-virtual {p0}, Lantlr/ANTLRParser;->element()V

    goto :goto_1

    :cond_3
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v6, :cond_5

    if-eq v1, v5, :cond_5

    if-eq v1, v3, :cond_5

    if-ne v1, v4, :cond_4

    invoke-virtual {p0}, Lantlr/ANTLRParser;->exceptionSpecNoLabel()V

    goto :goto_2

    :cond_4
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_5
    :goto_2
    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_6

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0}, Lantlr/ANTLRGrammarParseBehavior;->endAlt()V

    :cond_6
    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x29
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final ast_type_spec()I
    .locals 3

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v2, 0x6

    if-eq v1, v2, :cond_2

    const/4 v2, 0x7

    if-eq v1, v2, :cond_2

    const/16 v2, 0x10

    if-eq v1, v2, :cond_2

    const/16 v2, 0x13

    if-eq v1, v2, :cond_2

    const/16 v2, 0x15

    if-eq v1, v2, :cond_2

    const/16 v2, 0x27

    if-eq v1, v2, :cond_2

    const/16 v2, 0x18

    if-eq v1, v2, :cond_2

    const/16 v2, 0x19

    if-eq v1, v2, :cond_2

    const/16 v2, 0x1b

    if-eq v1, v2, :cond_2

    const/16 v2, 0x1c

    if-eq v1, v2, :cond_2

    const/16 v2, 0x21

    if-eq v1, v2, :cond_1

    const/16 v2, 0x22

    if-eq v1, v2, :cond_2

    const/16 v2, 0x31

    if-eq v1, v2, :cond_0

    const/16 v2, 0x32

    if-eq v1, v2, :cond_2

    packed-switch v1, :pswitch_data_0

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p0, :cond_2

    const/4 v0, 0x2

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p0, :cond_2

    const/4 v0, 0x3

    :cond_2
    :goto_0
    :pswitch_0
    return v0

    :pswitch_data_0
    .packed-switch 0x29
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final block()V
    .locals 3

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    const/4 v1, 0x1

    if-nez v0, :cond_0

    iget v0, p0, Lantlr/ANTLRParser;->blockNesting:I

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/ANTLRParser;->blockNesting:I

    :cond_0
    :goto_0
    invoke-virtual {p0}, Lantlr/ANTLRParser;->alternative()V

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v2, 0x15

    if-ne v0, v2, :cond_1

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    goto :goto_0

    :cond_1
    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_2

    iget v0, p0, Lantlr/ANTLRParser;->blockNesting:I

    sub-int/2addr v0, v1

    iput v0, p0, Lantlr/ANTLRParser;->blockNesting:I

    :cond_2
    return-void
.end method

.method public final charSet()Lantlr/collections/impl/BitSet;
    .locals 3

    invoke-virtual {p0}, Lantlr/ANTLRParser;->setBlockElement()Lantlr/collections/impl/BitSet;

    move-result-object v0

    :cond_0
    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x15

    if-ne v1, v2, :cond_1

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->setBlockElement()Lantlr/collections/impl/BitSet;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_0

    invoke-virtual {v0, v1}, Lantlr/collections/impl/BitSet;->orInPlace(Lantlr/collections/impl/BitSet;)V

    goto :goto_0

    :cond_1
    return-object v0
.end method

.method public final classDef()V
    .locals 14

    const/16 v0, 0x8

    const/16 v1, 0x9

    const/4 v2, 0x0

    const/16 v3, 0xa

    const/4 v4, 0x1

    :try_start_0
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    packed-switch v5, :pswitch_data_0

    new-instance v5, Lantlr/NoViableAltException;

    goto/16 :goto_9

    :pswitch_0
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v5

    const/4 v6, 0x7

    invoke-virtual {p0, v6}, Lantlr/Parser;->match(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v6, v6, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v6, :cond_0

    iget-object v6, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v6, v5}, Lantlr/ANTLRGrammarParseBehavior;->refPreambleAction(Lantlr/Token;)V

    :cond_0
    :pswitch_1
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    packed-switch v5, :pswitch_data_1

    new-instance v5, Lantlr/NoViableAltException;

    goto/16 :goto_8

    :pswitch_2
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v5

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v6, v6, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v6, :cond_1

    invoke-virtual {v5}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    goto :goto_0

    :cond_1
    :pswitch_3
    const/4 v5, 0x0

    :goto_0
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    const/16 v7, 0xb

    const/16 v8, 0x29

    const/16 v9, 0x18

    const/4 v10, 0x2

    if-eq v6, v1, :cond_2

    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-ne v6, v3, :cond_3

    :cond_2
    invoke-virtual {p0, v10}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-eq v6, v9, :cond_4

    invoke-virtual {p0, v10}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-ne v6, v8, :cond_3

    goto :goto_1

    :cond_3
    move v11, v2

    goto :goto_4

    :cond_4
    :goto_1
    invoke-virtual {p0}, Lantlr/Parser;->mark()I

    move-result v6

    iget-object v11, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v12, v11, Lantlr/ParserSharedInputState;->guessing:I

    add-int/2addr v12, v4

    iput v12, v11, Lantlr/ParserSharedInputState;->guessing:I
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_2

    :try_start_1
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LA(I)I

    move-result v11

    if-eq v11, v1, :cond_6

    if-ne v11, v3, :cond_5

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    invoke-virtual {p0, v7}, Lantlr/Parser;->match(I)V

    const/16 v11, 0xc

    invoke-virtual {p0, v11}, Lantlr/Parser;->match(I)V

    goto :goto_2

    :cond_5
    new-instance v11, Lantlr/NoViableAltException;

    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v12

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v13

    invoke-direct {v11, v12, v13}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v11

    :cond_6
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V
    :try_end_1
    .catch Lantlr/RecognitionException; {:try_start_1 .. :try_end_1} :catch_0

    :goto_2
    move v11, v4

    goto :goto_3

    :catch_0
    move v11, v2

    :goto_3
    :try_start_2
    invoke-virtual {p0, v6}, Lantlr/Parser;->rewind(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v12, v6, Lantlr/ParserSharedInputState;->guessing:I

    sub-int/2addr v12, v4

    iput v12, v6, Lantlr/ParserSharedInputState;->guessing:I

    :goto_4
    if-eqz v11, :cond_7

    invoke-virtual {p0, v5}, Lantlr/ANTLRParser;->lexerSpec(Ljava/lang/String;)V

    goto :goto_7

    :cond_7
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-ne v6, v3, :cond_9

    invoke-virtual {p0, v10}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-eq v6, v9, :cond_8

    invoke-virtual {p0, v10}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-ne v6, v8, :cond_9

    :cond_8
    invoke-virtual {p0}, Lantlr/Parser;->mark()I

    move-result v6

    iget-object v11, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v12, v11, Lantlr/ParserSharedInputState;->guessing:I

    add-int/2addr v12, v4

    iput v12, v11, Lantlr/ParserSharedInputState;->guessing:I
    :try_end_2
    .catch Lantlr/RecognitionException; {:try_start_2 .. :try_end_2} :catch_2

    :try_start_3
    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    invoke-virtual {p0, v7}, Lantlr/Parser;->match(I)V

    const/16 v7, 0xd

    invoke-virtual {p0, v7}, Lantlr/Parser;->match(I)V
    :try_end_3
    .catch Lantlr/RecognitionException; {:try_start_3 .. :try_end_3} :catch_1

    move v7, v4

    goto :goto_5

    :catch_1
    move v7, v2

    :goto_5
    :try_start_4
    invoke-virtual {p0, v6}, Lantlr/Parser;->rewind(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v11, v6, Lantlr/ParserSharedInputState;->guessing:I

    sub-int/2addr v11, v4

    iput v11, v6, Lantlr/ParserSharedInputState;->guessing:I

    goto :goto_6

    :cond_9
    move v7, v2

    :goto_6
    if-eqz v7, :cond_a

    invoke-virtual {p0, v5}, Lantlr/ANTLRParser;->treeParserSpec(Ljava/lang/String;)V

    goto :goto_7

    :cond_a
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-ne v6, v3, :cond_c

    invoke-virtual {p0, v10}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-eq v6, v9, :cond_b

    invoke-virtual {p0, v10}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    if-ne v6, v8, :cond_c

    :cond_b
    invoke-virtual {p0, v5}, Lantlr/ANTLRParser;->parserSpec(Ljava/lang/String;)V

    :goto_7
    invoke-virtual {p0}, Lantlr/ANTLRParser;->rules()V

    iget-object v5, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v5, v5, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v5, :cond_f

    iget-object v5, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v5}, Lantlr/ANTLRGrammarParseBehavior;->endGrammar()V

    goto :goto_c

    :cond_c
    new-instance v5, Lantlr/NoViableAltException;

    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v6

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v7

    invoke-direct {v5, v6, v7}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v5

    :goto_8
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v6

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v7

    invoke-direct {v5, v6, v7}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v5

    :goto_9
    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v6

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v7

    invoke-direct {v5, v6, v7}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v5
    :try_end_4
    .catch Lantlr/RecognitionException; {:try_start_4 .. :try_end_4} :catch_2

    :catch_2
    move-exception v5

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v6, v6, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v6, :cond_10

    instance-of v6, v5, Lantlr/NoViableAltException;

    if-eqz v6, :cond_d

    move-object v6, v5

    check-cast v6, Lantlr/NoViableAltException;

    iget-object v6, v6, Lantlr/NoViableAltException;->token:Lantlr/Token;

    invoke-virtual {v6}, Lantlr/Token;->getType()I

    move-result v6

    if-ne v6, v0, :cond_d

    const-string v0, "JAVADOC comments may only prefix rules and grammars"

    goto :goto_a

    :cond_d
    const-string v0, "rule classDef trapped:\n"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v5}, Lantlr/RecognitionException;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :goto_a
    invoke-virtual {p0, v5, v0}, Lantlr/ANTLRParser;->reportError(Lantlr/RecognitionException;Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v0}, Lantlr/ANTLRGrammarParseBehavior;->abortGrammar()V

    move v0, v4

    :goto_b
    if-eqz v0, :cond_f

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    invoke-virtual {p0, v4}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    if-eq v5, v4, :cond_e

    if-eq v5, v1, :cond_e

    if-eq v5, v3, :cond_e

    goto :goto_b

    :cond_e
    move v0, v2

    goto :goto_b

    :cond_f
    :goto_c
    return-void

    :cond_10
    throw v5

    nop

    :pswitch_data_0
    .packed-switch 0x7
        :pswitch_0
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x8
        :pswitch_2
        :pswitch_3
        :pswitch_3
    .end packed-switch
.end method

.method public final ebnf(Lantlr/Token;Z)V
    .locals 12

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    const/16 v2, 0x1b

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    iget-object v3, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v3, p1, v1, p2}, Lantlr/ANTLRGrammarParseBehavior;->beginSubRule(Lantlr/Token;Lantlr/Token;Z)V

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    const/16 p2, 0xe

    const/16 v1, 0x24

    const/4 v3, 0x7

    if-ne p1, p2, :cond_3

    invoke-virtual {p0}, Lantlr/ANTLRParser;->subruleOptionsSpec()V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    if-eq p1, v3, :cond_2

    if-ne p1, v1, :cond_1

    goto :goto_1

    :cond_1
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, p2, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_2
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p1

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    iget-object p2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p2, p2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p2, :cond_4

    goto :goto_0

    :cond_3
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    const/4 p2, 0x2

    if-ne p1, v3, :cond_5

    invoke-virtual {p0, p2}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    if-ne p1, v1, :cond_5

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p1

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    iget-object p2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p2, p2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p2, :cond_4

    :goto_0
    iget-object p2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p2, p1}, Lantlr/ANTLRGrammarParseBehavior;->refInitAction(Lantlr/Token;)V

    :cond_4
    :goto_1
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    goto :goto_2

    :cond_5
    sget-object p1, Lantlr/ANTLRParser;->_tokenSet_9:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    invoke-virtual {p1, v1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result p1

    if-eqz p1, :cond_b

    sget-object p1, Lantlr/ANTLRParser;->_tokenSet_10:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p2}, Lantlr/LLkParser;->LA(I)I

    move-result p2

    invoke-virtual {p1, p2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result p1

    if-eqz p1, :cond_b

    :goto_2
    invoke-virtual {p0}, Lantlr/ANTLRParser;->block()V

    const/16 p1, 0x1c

    invoke-virtual {p0, p1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p2

    const/16 v1, 0x19

    const/16 v4, 0x18

    const/16 v5, 0x32

    const/16 v6, 0x27

    const/16 v7, 0x15

    const/16 v8, 0x13

    const/16 v9, 0x10

    const/4 v10, 0x6

    const/16 v11, 0x21

    if-eq p2, v10, :cond_6

    if-eq p2, v3, :cond_6

    if-eq p2, v9, :cond_6

    if-eq p2, v8, :cond_6

    if-eq p2, v7, :cond_6

    if-eq p2, v11, :cond_6

    if-eq p2, v6, :cond_6

    if-eq p2, v5, :cond_6

    if-eq p2, v4, :cond_6

    if-eq p2, v1, :cond_6

    if-eq p2, v2, :cond_6

    if-eq p2, p1, :cond_6

    packed-switch p2, :pswitch_data_0

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, p2, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :pswitch_0
    const/16 p1, 0x30

    invoke-virtual {p0, p1}, Lantlr/Parser;->match(I)V

    iget-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p1, p1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p1, :cond_9

    iget-object p1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p1}, Lantlr/ANTLRGrammarParseBehavior;->synPred()V

    goto/16 :goto_4

    :cond_6
    :pswitch_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p2

    if-eq p2, v10, :cond_7

    if-eq p2, v3, :cond_7

    if-eq p2, v9, :cond_7

    if-eq p2, v8, :cond_7

    if-eq p2, v7, :cond_7

    if-eq p2, v11, :cond_7

    if-eq p2, v6, :cond_7

    if-eq p2, v5, :cond_7

    if-eq p2, v4, :cond_7

    if-eq p2, v1, :cond_7

    if-eq p2, v2, :cond_7

    if-eq p2, p1, :cond_7

    packed-switch p2, :pswitch_data_1

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, p2, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :pswitch_2
    const/16 p2, 0x2f

    invoke-virtual {p0, p2}, Lantlr/Parser;->match(I)V

    iget-object p2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p2, p2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p2, :cond_7

    iget-object p2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p2}, Lantlr/ANTLRGrammarParseBehavior;->oneOrMoreSubRule()V

    goto :goto_3

    :pswitch_3
    const/16 p2, 0x2e

    invoke-virtual {p0, p2}, Lantlr/Parser;->match(I)V

    iget-object p2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p2, p2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p2, :cond_7

    iget-object p2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p2}, Lantlr/ANTLRGrammarParseBehavior;->zeroOrMoreSubRule()V

    goto :goto_3

    :pswitch_4
    const/16 p2, 0x2d

    invoke-virtual {p0, p2}, Lantlr/Parser;->match(I)V

    iget-object p2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p2, p2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p2, :cond_7

    iget-object p2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p2}, Lantlr/ANTLRGrammarParseBehavior;->optionalSubRule()V

    :cond_7
    :goto_3
    :pswitch_5
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p2

    if-eq p2, v10, :cond_9

    if-eq p2, v3, :cond_9

    if-eq p2, v9, :cond_9

    if-eq p2, v8, :cond_9

    if-eq p2, v7, :cond_9

    if-eq p2, v11, :cond_8

    if-eq p2, v6, :cond_9

    if-eq p2, v5, :cond_9

    if-eq p2, v4, :cond_9

    if-eq p2, v1, :cond_9

    if-eq p2, v2, :cond_9

    if-eq p2, p1, :cond_9

    packed-switch p2, :pswitch_data_2

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, p2, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_8
    invoke-virtual {p0, v11}, Lantlr/Parser;->match(I)V

    iget-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p1, p1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p1, :cond_9

    iget-object p1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p1}, Lantlr/ANTLRGrammarParseBehavior;->noASTSubRule()V

    :cond_9
    :goto_4
    :pswitch_6
    iget-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p1, p1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p1, :cond_a

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0}, Lantlr/ANTLRGrammarParseBehavior;->endSubRule()V

    :cond_a
    return-void

    :cond_b
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, p2, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    nop

    :pswitch_data_0
    .packed-switch 0x29
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x29
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x29
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
    .end packed-switch
.end method

.method public final element()V
    .locals 3

    invoke-virtual {p0}, Lantlr/ANTLRParser;->elementNoOptionSpec()V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v2, 0x6

    if-eq v1, v2, :cond_1

    const/4 v2, 0x7

    if-eq v1, v2, :cond_1

    const/16 v2, 0x10

    if-eq v1, v2, :cond_1

    const/16 v2, 0x13

    if-eq v1, v2, :cond_1

    const/16 v2, 0x15

    if-eq v1, v2, :cond_1

    const/16 v2, 0x27

    if-eq v1, v2, :cond_1

    const/16 v2, 0x32

    if-eq v1, v2, :cond_1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    const/16 v2, 0x19

    if-eq v1, v2, :cond_0

    const/16 v2, 0x1b

    if-eq v1, v2, :cond_1

    const/16 v2, 0x1c

    if-eq v1, v2, :cond_1

    packed-switch v1, :pswitch_data_0

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_0
    invoke-virtual {p0}, Lantlr/ANTLRParser;->elementOptionSpec()V

    :cond_1
    :pswitch_0
    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x29
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final elementNoOptionSpec()V
    .locals 26

    move-object/from16 v0, p0

    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/4 v3, 0x7

    if-eq v2, v3, :cond_22

    const/16 v4, 0x2b

    if-eq v2, v4, :cond_21

    const/16 v4, 0x2c

    if-eq v2, v4, :cond_20

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v4, 0x19

    const/16 v5, 0x32

    const/16 v6, 0x27

    const/16 v7, 0x15

    const/16 v8, 0x10

    const/4 v11, 0x6

    const/16 v12, 0x1b

    const/16 v13, 0x13

    const/16 v14, 0x29

    const/4 v15, 0x2

    const/16 v10, 0x18

    const/16 v16, 0x0

    const/16 v17, 0x0

    const/16 v24, 0x1

    if-eq v2, v10, :cond_0

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-ne v2, v14, :cond_e

    :cond_0
    invoke-virtual {v0, v15}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v9, 0xf

    if-ne v2, v9, :cond_e

    invoke-virtual/range {p0 .. p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v19

    const/16 v2, 0xf

    invoke-virtual {v0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v10, :cond_1

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-ne v2, v14, :cond_3

    :cond_1
    invoke-virtual {v0, v15}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v9, 0x24

    if-ne v2, v9, :cond_3

    invoke-virtual/range {p0 .. p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v2

    const/16 v9, 0x24

    invoke-virtual {v0, v9}, Lantlr/Parser;->match(I)V

    iget-object v9, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v9, v9, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v9, :cond_2

    invoke-direct {v0, v2}, Lantlr/ANTLRParser;->checkForMissingEndRule(Lantlr/Token;)V

    :cond_2
    move-object/from16 v21, v2

    goto :goto_0

    :cond_3
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v10, :cond_4

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-ne v2, v14, :cond_d

    :cond_4
    sget-object v2, Lantlr/ANTLRParser;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v15}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_d

    move-object/from16 v21, v16

    :goto_0
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v10, :cond_a

    if-ne v2, v14, :cond_9

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    invoke-virtual {v0, v14}, Lantlr/Parser;->match(I)V

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    if-eq v9, v11, :cond_6

    if-eq v9, v3, :cond_6

    if-eq v9, v8, :cond_6

    if-eq v9, v13, :cond_6

    if-eq v9, v7, :cond_6

    if-eq v9, v6, :cond_6

    if-eq v9, v5, :cond_6

    if-eq v9, v10, :cond_6

    if-eq v9, v4, :cond_6

    if-eq v9, v12, :cond_6

    const/16 v14, 0x1c

    if-eq v9, v14, :cond_6

    const/16 v14, 0x21

    if-eq v9, v14, :cond_6

    const/16 v14, 0x22

    if-eq v9, v14, :cond_5

    packed-switch v9, :pswitch_data_0

    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_5
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v9

    invoke-virtual {v0, v14}, Lantlr/Parser;->match(I)V

    iget-object v14, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v14, v14, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v14, :cond_6

    move-object/from16 v17, v9

    :cond_6
    :pswitch_0
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    if-eq v9, v11, :cond_8

    if-eq v9, v3, :cond_8

    if-eq v9, v8, :cond_8

    if-eq v9, v13, :cond_8

    if-eq v9, v7, :cond_8

    const/16 v3, 0x21

    if-eq v9, v3, :cond_7

    if-eq v9, v6, :cond_8

    if-eq v9, v5, :cond_8

    if-eq v9, v10, :cond_8

    if-eq v9, v4, :cond_8

    if-eq v9, v12, :cond_8

    const/16 v3, 0x1c

    if-eq v9, v3, :cond_8

    packed-switch v9, :pswitch_data_1

    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_7
    move v1, v3

    invoke-virtual {v0, v1}, Lantlr/Parser;->match(I)V

    iget-object v1, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_8

    const/16 v24, 0x3

    :cond_8
    :pswitch_1
    iget-object v1, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_23

    move-object v5, v2

    move-object/from16 v7, v17

    move-object/from16 v4, v19

    move-object/from16 v6, v21

    :goto_1
    move/from16 v8, v24

    goto/16 :goto_4

    :cond_9
    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_a
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v20

    invoke-virtual {v0, v10}, Lantlr/Parser;->match(I)V

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v11, :cond_c

    if-eq v2, v3, :cond_c

    if-eq v2, v8, :cond_c

    if-eq v2, v13, :cond_c

    if-eq v2, v7, :cond_c

    const/16 v3, 0x22

    if-eq v2, v3, :cond_b

    if-eq v2, v6, :cond_c

    if-eq v2, v5, :cond_c

    if-eq v2, v10, :cond_c

    if-eq v2, v4, :cond_c

    if-eq v2, v12, :cond_c

    const/16 v3, 0x1c

    if-eq v2, v3, :cond_c

    packed-switch v2, :pswitch_data_2

    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_b
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    const/16 v2, 0x22

    invoke-virtual {v0, v2}, Lantlr/Parser;->match(I)V

    iget-object v2, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_c

    move-object/from16 v22, v1

    goto :goto_2

    :cond_c
    :pswitch_2
    move-object/from16 v22, v17

    :goto_2
    iget-object v1, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_23

    iget-object v1, v0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-direct/range {p0 .. p0}, Lantlr/ANTLRParser;->lastInRule()Z

    move-result v25

    const/16 v23, 0x0

    move-object/from16 v18, v1

    invoke-interface/range {v18 .. v25}, Lantlr/ANTLRGrammarParseBehavior;->refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V

    goto/16 :goto_6

    :cond_d
    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_e
    sget-object v2, Lantlr/ANTLRParser;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1f

    sget-object v2, Lantlr/ANTLRParser;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v15}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1f

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v10, :cond_f

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-ne v2, v14, :cond_10

    :cond_f
    invoke-virtual {v0, v15}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v9, 0x24

    if-ne v2, v9, :cond_10

    invoke-virtual/range {p0 .. p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v2

    const/16 v9, 0x24

    invoke-virtual {v0, v9}, Lantlr/Parser;->match(I)V

    iget-object v9, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v9, v9, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v9, :cond_11

    invoke-direct {v0, v2}, Lantlr/ANTLRParser;->checkForMissingEndRule(Lantlr/Token;)V

    goto :goto_3

    :cond_10
    sget-object v2, Lantlr/ANTLRParser;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1e

    sget-object v2, Lantlr/ANTLRParser;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v15}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1e

    move-object/from16 v2, v16

    :cond_11
    :goto_3
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v9

    if-eq v9, v12, :cond_1d

    if-eq v9, v14, :cond_18

    const/16 v3, 0x2a

    if-eq v9, v3, :cond_15

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    if-eq v3, v11, :cond_12

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    if-eq v3, v13, :cond_12

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    if-ne v3, v10, :cond_13

    :cond_12
    invoke-virtual {v0, v15}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    const/16 v4, 0x16

    if-ne v3, v4, :cond_13

    invoke-virtual {v0, v2}, Lantlr/ANTLRParser;->range(Lantlr/Token;)V

    goto/16 :goto_6

    :cond_13
    sget-object v3, Lantlr/ANTLRParser;->_tokenSet_7:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_14

    sget-object v3, Lantlr/ANTLRParser;->_tokenSet_8:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v15}, Lantlr/LLkParser;->LA(I)I

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_14

    invoke-virtual {v0, v2}, Lantlr/ANTLRParser;->terminal(Lantlr/Token;)V

    goto/16 :goto_6

    :cond_14
    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_15
    const/16 v3, 0x2a

    invoke-virtual {v0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    if-eq v3, v13, :cond_17

    if-eq v3, v10, :cond_17

    if-ne v3, v12, :cond_16

    goto/16 :goto_5

    :cond_16
    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_17
    invoke-virtual {v0, v2}, Lantlr/ANTLRParser;->notTerminal(Lantlr/Token;)V

    goto/16 :goto_6

    :cond_18
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v9

    invoke-virtual {v0, v14}, Lantlr/Parser;->match(I)V

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v14

    if-eq v14, v11, :cond_1a

    if-eq v14, v3, :cond_1a

    if-eq v14, v8, :cond_1a

    if-eq v14, v13, :cond_1a

    if-eq v14, v7, :cond_1a

    if-eq v14, v6, :cond_1a

    if-eq v14, v5, :cond_1a

    if-eq v14, v10, :cond_1a

    if-eq v14, v4, :cond_1a

    if-eq v14, v12, :cond_1a

    const/16 v15, 0x1c

    if-eq v14, v15, :cond_1a

    const/16 v15, 0x21

    if-eq v14, v15, :cond_1a

    const/16 v15, 0x22

    if-eq v14, v15, :cond_19

    packed-switch v14, :pswitch_data_3

    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_19
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v14

    invoke-virtual {v0, v15}, Lantlr/Parser;->match(I)V

    iget-object v15, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v15, v15, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v15, :cond_1a

    move-object/from16 v17, v14

    :cond_1a
    :pswitch_3
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v14

    if-eq v14, v11, :cond_1c

    if-eq v14, v3, :cond_1c

    if-eq v14, v8, :cond_1c

    if-eq v14, v13, :cond_1c

    if-eq v14, v7, :cond_1c

    const/16 v3, 0x21

    if-eq v14, v3, :cond_1b

    if-eq v14, v6, :cond_1c

    if-eq v14, v5, :cond_1c

    if-eq v14, v10, :cond_1c

    if-eq v14, v4, :cond_1c

    if-eq v14, v12, :cond_1c

    const/16 v3, 0x1c

    if-eq v14, v3, :cond_1c

    packed-switch v14, :pswitch_data_4

    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_1b
    move v1, v3

    invoke-virtual {v0, v1}, Lantlr/Parser;->match(I)V

    iget-object v1, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_1c

    const/16 v24, 0x3

    :cond_1c
    :pswitch_4
    iget-object v1, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_23

    const/16 v19, 0x0

    move-object v6, v2

    move-object v5, v9

    move-object/from16 v7, v17

    move-object/from16 v4, v19

    goto/16 :goto_1

    :goto_4
    iget-object v3, v0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface/range {v3 .. v8}, Lantlr/ANTLRGrammarParseBehavior;->refRule(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;I)V

    goto :goto_6

    :cond_1d
    const/4 v1, 0x0

    :goto_5
    invoke-virtual {v0, v2, v1}, Lantlr/ANTLRParser;->ebnf(Lantlr/Token;Z)V

    goto :goto_6

    :cond_1e
    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_1f
    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_20
    invoke-virtual/range {p0 .. p0}, Lantlr/ANTLRParser;->tree()V

    goto :goto_6

    :cond_21
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    const/16 v2, 0x2b

    invoke-virtual {v0, v2}, Lantlr/Parser;->match(I)V

    iget-object v2, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_23

    iget-object v0, v0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v0, v1}, Lantlr/ANTLRGrammarParseBehavior;->refSemPred(Lantlr/Token;)V

    goto :goto_6

    :cond_22
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v0, v3}, Lantlr/Parser;->match(I)V

    iget-object v2, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_23

    iget-object v0, v0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v0, v1}, Lantlr/ANTLRGrammarParseBehavior;->refAction(Lantlr/Token;)V

    :cond_23
    :goto_6
    return-void

    :pswitch_data_0
    .packed-switch 0x29
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x29
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x29
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x29
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
    .end packed-switch

    :pswitch_data_4
    .packed-switch 0x29
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
    .end packed-switch
.end method

.method public final elementOptionSpec()V
    .locals 4

    const/16 v0, 0x19

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/16 v1, 0xf

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v2

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    goto :goto_1

    :cond_0
    :goto_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v2, 0x10

    if-ne v0, v2, :cond_1

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v2

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    :goto_1
    iget-object v3, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v3, v0, v2}, Lantlr/ANTLRGrammarParseBehavior;->refElementOption(Lantlr/Token;Lantlr/Token;)V

    goto :goto_0

    :cond_1
    const/16 v0, 0x1a

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    return-void
.end method

.method public final exceptionGroup()V
    .locals 4

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v0}, Lantlr/ANTLRGrammarParseBehavior;->beginExceptionGroup()V

    :cond_0
    const/4 v0, 0x0

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v3, 0x27

    if-ne v2, v3, :cond_1

    invoke-virtual {p0}, Lantlr/ANTLRParser;->exceptionSpec()V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    if-lt v0, v1, :cond_3

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_2

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0}, Lantlr/ANTLRGrammarParseBehavior;->endExceptionGroup()V

    :cond_2
    return-void

    :cond_3
    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, v1, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0
.end method

.method public final exceptionHandler()V
    .locals 3

    const/16 v0, 0x28

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    const/16 v2, 0x22

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    const/4 v2, 0x7

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_0

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0, v1, v0}, Lantlr/ANTLRGrammarParseBehavior;->refExceptionHandler(Lantlr/Token;Lantlr/Token;)V

    :cond_0
    return-void
.end method

.method public final exceptionSpec()V
    .locals 3

    const/16 v0, 0x27

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v0, :cond_1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    const/16 v2, 0x22

    if-eq v1, v2, :cond_0

    packed-switch v1, :pswitch_data_0

    packed-switch v1, :pswitch_data_1

    packed-switch v1, :pswitch_data_2

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_1

    goto :goto_0

    :cond_1
    :pswitch_0
    const/4 v1, 0x0

    :goto_0
    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_2

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v2, v1}, Lantlr/ANTLRGrammarParseBehavior;->beginExceptionSpec(Lantlr/Token;)V

    :cond_2
    :goto_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x28

    if-ne v1, v2, :cond_3

    invoke-virtual {p0}, Lantlr/ANTLRParser;->exceptionHandler()V

    goto :goto_1

    :cond_3
    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_4

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0}, Lantlr/ANTLRGrammarParseBehavior;->endExceptionSpec()V

    :cond_4
    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x7
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x1e
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x27
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final exceptionSpecNoLabel()V
    .locals 2

    const/16 v0, 0x27

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    const/4 v1, 0x0

    invoke-interface {v0, v1}, Lantlr/ANTLRGrammarParseBehavior;->beginExceptionSpec(Lantlr/Token;)V

    :cond_0
    :goto_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v1, 0x28

    if-ne v0, v1, :cond_1

    invoke-virtual {p0}, Lantlr/ANTLRParser;->exceptionHandler()V

    goto :goto_0

    :cond_1
    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_2

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0}, Lantlr/ANTLRGrammarParseBehavior;->endExceptionSpec()V

    :cond_2
    return-void
.end method

.method public final fileOptionsSpec()V
    .locals 4

    const/16 v0, 0xe

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v1, 0x29

    if-ne v0, v1, :cond_0

    goto :goto_1

    :cond_0
    const/16 v0, 0x11

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    return-void

    :cond_1
    :goto_1
    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/16 v1, 0xf

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_2

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-virtual {p0}, Lantlr/Parser;->getInputState()Lantlr/ParserSharedInputState;

    move-result-object v3

    iget-object v3, v3, Lantlr/ParserSharedInputState;->filename:Ljava/lang/String;

    invoke-interface {v2, v0, v1, v3}, Lantlr/ANTLRGrammarParseBehavior;->setFileOption(Lantlr/Token;Lantlr/Token;Ljava/lang/String;)V

    :cond_2
    const/16 v0, 0x10

    goto :goto_0
.end method

.method public final grammar()V
    .locals 6

    const/4 v0, 0x0

    move-object v1, v0

    :cond_0
    :goto_0
    const/4 v2, 0x1

    :try_start_0
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    const/4 v4, 0x5

    const/4 v5, 0x7

    if-ne v3, v4, :cond_4

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_1

    move-object v1, v0

    :cond_1
    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    const/4 v4, 0x6

    if-eq v3, v4, :cond_3

    if-ne v3, v5, :cond_2

    goto :goto_1

    :cond_2
    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-direct {v0, v1, v3}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0

    :cond_3
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    :goto_1
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v3

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    iget-object v4, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v4, v4, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v4, :cond_0

    iget-object v4, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v4, v1, v3}, Lantlr/ANTLRGrammarParseBehavior;->refHeaderAction(Lantlr/Token;Lantlr/Token;)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    if-eq v0, v2, :cond_6

    const/16 v1, 0xe

    if-eq v0, v1, :cond_5

    packed-switch v0, :pswitch_data_0

    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-direct {v0, v1, v3}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0

    :cond_5
    invoke-virtual {p0}, Lantlr/ANTLRParser;->fileOptionsSpec()V

    :cond_6
    :goto_2
    :pswitch_0
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    if-lt v0, v5, :cond_7

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v1, 0xa

    if-gt v0, v1, :cond_7

    invoke-virtual {p0}, Lantlr/ANTLRParser;->classDef()V

    goto :goto_2

    :cond_7
    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_3

    :catch_0
    move-exception v0

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_8

    const-string v1, "rule grammar trapped:\n"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v0}, Lantlr/RecognitionException;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v0, v1}, Lantlr/ANTLRParser;->reportError(Lantlr/RecognitionException;Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/Parser;->consumeUntil(I)V

    :goto_3
    return-void

    :cond_8
    throw v0

    nop

    :pswitch_data_0
    .packed-switch 0x7
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final id()Lantlr/Token;
    .locals 3

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    const/16 v2, 0x29

    if-ne v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p0, :cond_2

    goto :goto_0

    :cond_0
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p0, :cond_2

    goto :goto_0

    :cond_2
    const/4 v0, 0x0

    :goto_0
    return-object v0
.end method

.method public final lexerOptionsSpec()V
    .locals 4

    const/16 v0, 0xe

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    :cond_0
    :goto_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v1, 0x10

    const/16 v2, 0xf

    const/16 v3, 0x12

    if-eq v0, v3, :cond_3

    const/16 v3, 0x18

    if-eq v0, v3, :cond_1

    const/16 v3, 0x29

    if-eq v0, v3, :cond_1

    const/16 v0, 0x11

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    return-void

    :cond_1
    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v2

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_2

    iget-object v3, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v3, v0, v2}, Lantlr/ANTLRGrammarParseBehavior;->setGrammarOption(Lantlr/Token;Lantlr/Token;)V

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    goto :goto_0

    :cond_3
    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->charSet()Lantlr/collections/impl/BitSet;

    move-result-object v0

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_0

    iget-object v1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v1, v0}, Lantlr/ANTLRGrammarParseBehavior;->setCharVocabulary(Lantlr/collections/impl/BitSet;)V

    goto :goto_0
.end method

.method public final lexerSpec(Ljava/lang/String;)V
    .locals 9

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x10

    const/16 v3, 0x9

    const/4 v4, 0x0

    if-eq v1, v3, :cond_2

    const/16 v3, 0xa

    if-ne v1, v3, :cond_1

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v1

    const/16 v3, 0xb

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    const/16 v3, 0xc

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    if-eq v3, v2, :cond_4

    const/16 v4, 0x1b

    if-ne v3, v4, :cond_0

    invoke-virtual {p0}, Lantlr/ANTLRParser;->superClass()Ljava/lang/String;

    move-result-object v4

    goto :goto_0

    :cond_0
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_1
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_2
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v3

    iget-object v5, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v5, v5, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v5, :cond_3

    iget-object v5, p0, Lantlr/ANTLRParser;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v1}, Lantlr/Token;->getLine()I

    move-result v7

    invoke-virtual {v1}, Lantlr/Token;->getColumn()I

    move-result v1

    const-string v8, "lexclass\' is deprecated; use \'class X extends Lexer\'"

    invoke-virtual {v5, v8, v6, v7, v1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_3
    move-object v1, v3

    :cond_4
    :goto_0
    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_5

    iget-object v3, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v5

    invoke-interface {v3, v5, v1, v4, p1}, Lantlr/ANTLRGrammarParseBehavior;->startLexer(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;Ljava/lang/String;)V

    :cond_5
    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    const/16 v1, 0x17

    const/16 v2, 0x18

    const/16 v3, 0x29

    const/16 v4, 0x8

    const/4 v5, 0x7

    if-eq p1, v5, :cond_7

    if-eq p1, v4, :cond_7

    const/16 v6, 0xe

    if-eq p1, v6, :cond_6

    if-eq p1, v3, :cond_7

    if-eq p1, v1, :cond_7

    if-eq p1, v2, :cond_7

    packed-switch p1, :pswitch_data_0

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_6
    invoke-virtual {p0}, Lantlr/ANTLRParser;->lexerOptionsSpec()V

    :cond_7
    :pswitch_0
    iget-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p1, p1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p1, :cond_8

    iget-object p1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p1}, Lantlr/ANTLRGrammarParseBehavior;->endOptions()V

    :cond_8
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    if-eq p1, v5, :cond_a

    if-eq p1, v4, :cond_a

    if-eq p1, v1, :cond_9

    if-eq p1, v2, :cond_a

    if-eq p1, v3, :cond_a

    packed-switch p1, :pswitch_data_1

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_9
    invoke-virtual {p0}, Lantlr/ANTLRParser;->tokensSpec()V

    :cond_a
    :pswitch_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    if-eq p1, v5, :cond_b

    if-eq p1, v4, :cond_c

    if-eq p1, v2, :cond_c

    if-eq p1, v3, :cond_c

    packed-switch p1, :pswitch_data_2

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_b
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p1

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_c

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0, p1}, Lantlr/ANTLRGrammarParseBehavior;->refMemberAction(Lantlr/Token;)V

    :cond_c
    :pswitch_2
    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x1e
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x1e
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x1e
        :pswitch_2
        :pswitch_2
        :pswitch_2
    .end packed-switch
.end method

.method public final notTerminal(Lantlr/Token;)V
    .locals 12

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x18

    const/16 v3, 0x13

    if-eq v1, v3, :cond_1

    if-ne v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v6

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->ast_type_spec()I

    move-result v10

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_4

    iget-object v4, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-direct {p0}, Lantlr/ANTLRParser;->lastInRule()Z

    move-result v11

    const/4 v5, 0x0

    const/4 v8, 0x0

    const/4 v9, 0x1

    move-object v7, p1

    invoke-interface/range {v4 .. v11}, Lantlr/ANTLRGrammarParseBehavior;->refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V

    goto/16 :goto_0

    :cond_0
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v4

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v5, 0x6

    if-eq v1, v5, :cond_3

    const/4 v5, 0x7

    if-eq v1, v5, :cond_3

    const/16 v5, 0x10

    if-eq v1, v5, :cond_3

    if-eq v1, v3, :cond_3

    const/16 v3, 0x15

    if-eq v1, v3, :cond_3

    const/16 v3, 0x21

    if-eq v1, v3, :cond_2

    const/16 v3, 0x27

    if-eq v1, v3, :cond_3

    const/16 v3, 0x32

    if-eq v1, v3, :cond_3

    if-eq v1, v2, :cond_3

    const/16 v2, 0x19

    if-eq v1, v2, :cond_3

    const/16 v2, 0x1b

    if-eq v1, v2, :cond_3

    const/16 v2, 0x1c

    if-eq v1, v2, :cond_3

    packed-switch v1, :pswitch_data_0

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_2
    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_3

    const/4 v0, 0x3

    :cond_3
    :pswitch_0
    move v5, v0

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_4

    iget-object v1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-direct {p0}, Lantlr/ANTLRParser;->lastInRule()Z

    move-result v6

    const/4 p0, 0x1

    move-object v2, v4

    move-object v3, p1

    move v4, p0

    invoke-interface/range {v1 .. v6}, Lantlr/ANTLRGrammarParseBehavior;->refCharLiteral(Lantlr/Token;Lantlr/Token;ZIZ)V

    :cond_4
    :goto_0
    return-void

    :pswitch_data_0
    .packed-switch 0x29
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final optionValue()Lantlr/Token;
    .locals 3

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v2, 0x6

    if-eq v1, v2, :cond_3

    const/16 v2, 0x18

    if-eq v1, v2, :cond_2

    const/16 v2, 0x29

    if-eq v1, v2, :cond_2

    const/16 v2, 0x13

    if-eq v1, v2, :cond_1

    const/16 v2, 0x14

    if-ne v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p0, :cond_4

    goto :goto_0

    :cond_0
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p0, :cond_4

    goto :goto_0

    :cond_2
    invoke-virtual {p0}, Lantlr/ANTLRParser;->qualifiedID()Lantlr/Token;

    move-result-object v0

    goto :goto_0

    :cond_3
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p0, :cond_4

    goto :goto_0

    :cond_4
    const/4 v0, 0x0

    :goto_0
    return-object v0
.end method

.method public final parserOptionsSpec()V
    .locals 3

    const/16 v0, 0xe

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v1, 0x29

    if-ne v0, v1, :cond_0

    goto :goto_1

    :cond_0
    const/16 v0, 0x11

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    return-void

    :cond_1
    :goto_1
    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/16 v1, 0xf

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_2

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v2, v0, v1}, Lantlr/ANTLRGrammarParseBehavior;->setGrammarOption(Lantlr/Token;Lantlr/Token;)V

    :cond_2
    const/16 v0, 0x10

    goto :goto_0
.end method

.method public final parserSpec(Ljava/lang/String;)V
    .locals 8

    const/16 v0, 0xa

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v3, 0xb

    const/16 v4, 0x10

    if-eq v2, v3, :cond_1

    if-ne v2, v4, :cond_0

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_3

    iget-object v2, p0, Lantlr/ANTLRParser;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0}, Lantlr/Token;->getLine()I

    move-result v5

    invoke-virtual {v0}, Lantlr/Token;->getColumn()I

    move-result v6

    const-string v7, "use \'class X extends Parser\'"

    invoke-virtual {v2, v7, v3, v5, v6}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_0

    :cond_0
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_1
    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    const/16 v2, 0x1d

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v4, :cond_3

    const/16 v3, 0x1b

    if-ne v2, v3, :cond_2

    invoke-virtual {p0}, Lantlr/ANTLRParser;->superClass()Ljava/lang/String;

    move-result-object v2

    goto :goto_1

    :cond_2
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_3
    :goto_0
    const/4 v2, 0x0

    :goto_1
    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_4

    iget-object v3, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v5

    invoke-interface {v3, v5, v0, v2, p1}, Lantlr/ANTLRGrammarParseBehavior;->startParser(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;Ljava/lang/String;)V

    :cond_4
    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    const/16 v0, 0x17

    const/16 v2, 0x18

    const/16 v3, 0x29

    const/16 v4, 0x8

    const/4 v5, 0x7

    if-eq p1, v5, :cond_6

    if-eq p1, v4, :cond_6

    const/16 v6, 0xe

    if-eq p1, v6, :cond_5

    if-eq p1, v3, :cond_6

    if-eq p1, v0, :cond_6

    if-eq p1, v2, :cond_6

    packed-switch p1, :pswitch_data_0

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_5
    invoke-virtual {p0}, Lantlr/ANTLRParser;->parserOptionsSpec()V

    :cond_6
    :pswitch_0
    iget-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p1, p1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p1, :cond_7

    iget-object p1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p1}, Lantlr/ANTLRGrammarParseBehavior;->endOptions()V

    :cond_7
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    if-eq p1, v5, :cond_9

    if-eq p1, v4, :cond_9

    if-eq p1, v0, :cond_8

    if-eq p1, v2, :cond_9

    if-eq p1, v3, :cond_9

    packed-switch p1, :pswitch_data_1

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_8
    invoke-virtual {p0}, Lantlr/ANTLRParser;->tokensSpec()V

    :cond_9
    :pswitch_1
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    if-eq p1, v5, :cond_a

    if-eq p1, v4, :cond_b

    if-eq p1, v2, :cond_b

    if-eq p1, v3, :cond_b

    packed-switch p1, :pswitch_data_2

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_a
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p1

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_b

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0, p1}, Lantlr/ANTLRGrammarParseBehavior;->refMemberAction(Lantlr/Token;)V

    :cond_b
    :pswitch_2
    return-void

    :pswitch_data_0
    .packed-switch 0x1e
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x1e
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x1e
        :pswitch_2
        :pswitch_2
        :pswitch_2
    .end packed-switch
.end method

.method public final qualifiedID()Lantlr/Token;
    .locals 4

    new-instance v0, Ljava/lang/StringBuffer;

    const/16 v1, 0x1e

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_0

    :goto_0
    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_0
    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v3, 0x32

    if-ne v2, v3, :cond_1

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_0

    const/16 v2, 0x2e

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    goto :goto_0

    :cond_1
    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p0, :cond_2

    new-instance p0, Lantlr/CommonToken;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v0

    const/16 v2, 0x18

    invoke-direct {p0, v2, v0}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    invoke-virtual {v1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p0, v0}, Lantlr/CommonToken;->setLine(I)V

    goto :goto_1

    :cond_2
    const/4 p0, 0x0

    :goto_1
    return-object p0
.end method

.method public final range(Lantlr/Token;)V
    .locals 13

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x16

    const/16 v3, 0x18

    const/4 v4, 0x6

    if-eq v1, v4, :cond_4

    const/16 v5, 0x13

    if-eq v1, v5, :cond_1

    if-ne v1, v3, :cond_0

    goto/16 :goto_0

    :cond_0
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v6

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v7

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v4, :cond_3

    const/4 v2, 0x7

    if-eq v1, v2, :cond_3

    const/16 v2, 0x10

    if-eq v1, v2, :cond_3

    if-eq v1, v5, :cond_3

    const/16 v2, 0x15

    if-eq v1, v2, :cond_3

    const/16 v2, 0x21

    if-eq v1, v2, :cond_2

    const/16 v2, 0x27

    if-eq v1, v2, :cond_3

    const/16 v2, 0x32

    if-eq v1, v2, :cond_3

    if-eq v1, v3, :cond_3

    const/16 v2, 0x19

    if-eq v1, v2, :cond_3

    const/16 v2, 0x1b

    if-eq v1, v2, :cond_3

    const/16 v2, 0x1c

    if-eq v1, v2, :cond_3

    packed-switch v1, :pswitch_data_0

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_3

    const/4 v0, 0x3

    :cond_3
    :pswitch_0
    move v5, v0

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_b

    iget-object v1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-direct {p0}, Lantlr/ANTLRParser;->lastInRule()Z

    move-result p0

    move-object v2, v6

    move-object v3, v7

    move-object v4, p1

    move v6, p0

    invoke-interface/range {v1 .. v6}, Lantlr/ANTLRGrammarParseBehavior;->refCharRange(Lantlr/Token;Lantlr/Token;Lantlr/Token;IZ)V

    goto/16 :goto_5

    :cond_4
    :goto_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v5, 0x0

    if-eq v1, v4, :cond_6

    if-ne v1, v3, :cond_5

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v6, v6, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v6, :cond_7

    goto :goto_1

    :cond_5
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_6
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v6, v6, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v6, :cond_7

    :goto_1
    move-object v8, v1

    goto :goto_2

    :cond_7
    move-object v8, v5

    :goto_2
    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v4, :cond_9

    if-ne v1, v3, :cond_8

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_a

    goto :goto_3

    :cond_8
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_9
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_a

    :goto_3
    move-object v9, v0

    goto :goto_4

    :cond_a
    move-object v9, v5

    :goto_4
    invoke-virtual {p0}, Lantlr/ANTLRParser;->ast_type_spec()I

    move-result v11

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_b

    iget-object v7, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-direct {p0}, Lantlr/ANTLRParser;->lastInRule()Z

    move-result v12

    move-object v10, p1

    invoke-interface/range {v7 .. v12}, Lantlr/ANTLRGrammarParseBehavior;->refTokenRange(Lantlr/Token;Lantlr/Token;Lantlr/Token;IZ)V

    :cond_b
    :goto_5
    return-void

    :pswitch_data_0
    .packed-switch 0x29
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public reportError(Lantlr/RecognitionException;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/RecognitionException;->getErrorMessage()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, p1, v0}, Lantlr/ANTLRParser;->reportError(Lantlr/RecognitionException;Ljava/lang/String;)V

    return-void
.end method

.method public reportError(Lantlr/RecognitionException;Ljava/lang/String;)V
    .locals 2

    iget-object p0, p0, Lantlr/ANTLRParser;->antlrTool:Lantlr/Tool;

    invoke-virtual {p1}, Lantlr/RecognitionException;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/RecognitionException;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/RecognitionException;->getColumn()I

    move-result p1

    invoke-virtual {p0, p2, v0, v1, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void
.end method

.method public reportError(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/ANTLRParser;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    const/4 v1, -0x1

    invoke-virtual {v0, p1, p0, v1, v1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void
.end method

.method public reportWarning(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/ANTLRParser;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    const/4 v1, -0x1

    invoke-virtual {v0, p1, p0, v1, v1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    return-void
.end method

.method public final rootNode()V
    .locals 4

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v2, 0x2

    const/16 v3, 0x18

    if-eq v1, v3, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v3, 0x29

    if-ne v1, v3, :cond_1

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v3, 0x24

    if-ne v1, v3, :cond_1

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_2

    invoke-direct {p0, v0}, Lantlr/ANTLRParser;->checkForMissingEndRule(Lantlr/Token;)V

    goto :goto_0

    :cond_1
    sget-object v1, Lantlr/ANTLRParser;->_tokenSet_7:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_3

    sget-object v1, Lantlr/ANTLRParser;->_tokenSet_11:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    invoke-virtual {v1, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_3

    const/4 v0, 0x0

    :cond_2
    :goto_0
    invoke-virtual {p0, v0}, Lantlr/ANTLRParser;->terminal(Lantlr/Token;)V

    return-void

    :cond_3
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1
.end method

.method public final rule()V
    .locals 10

    const/4 v0, -0x1

    iput v0, p0, Lantlr/ANTLRParser;->blockNesting:I

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x8

    const/16 v3, 0x29

    const/16 v4, 0x18

    if-eq v1, v2, :cond_0

    if-eq v1, v4, :cond_1

    if-eq v1, v3, :cond_1

    packed-switch v1, :pswitch_data_0

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_1

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    goto :goto_0

    :cond_1
    :pswitch_0
    const/4 v1, 0x0

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v4, :cond_2

    if-eq v2, v3, :cond_2

    packed-switch v2, :pswitch_data_1

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :pswitch_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    const/16 v5, 0x20

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    iget-object v5, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v5, v5, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v5, :cond_2

    goto :goto_1

    :pswitch_2
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    const/16 v5, 0x1f

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    iget-object v5, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v5, v5, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v5, :cond_2

    goto :goto_1

    :pswitch_3
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    const/16 v5, 0x1e

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    iget-object v5, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v5, v5, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v5, :cond_2

    :goto_1
    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    goto :goto_2

    :cond_2
    const-string v2, "public"

    :goto_2
    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v5

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v6

    const/16 v7, 0xe

    const/4 v8, 0x7

    if-eq v6, v8, :cond_3

    if-eq v6, v7, :cond_3

    packed-switch v6, :pswitch_data_2

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :pswitch_4
    const/16 v6, 0x21

    invoke-virtual {p0, v6}, Lantlr/Parser;->match(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v6, v6, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v6, :cond_3

    const/4 v6, 0x0

    goto :goto_3

    :cond_3
    :pswitch_5
    move v6, v0

    :goto_3
    iget-object v9, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v9, v9, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v9, :cond_4

    iget-object v9, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v9, v5, v2, v6, v1}, Lantlr/ANTLRGrammarParseBehavior;->defineRuleName(Lantlr/Token;Ljava/lang/String;ZLjava/lang/String;)V

    :cond_4
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x22

    if-eq v1, v8, :cond_5

    if-eq v1, v7, :cond_5

    packed-switch v1, :pswitch_data_3

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :pswitch_6
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v6, v6, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v6, :cond_5

    iget-object v6, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v6, v1}, Lantlr/ANTLRGrammarParseBehavior;->refArgAction(Lantlr/Token;)V

    :cond_5
    :pswitch_7
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v8, :cond_6

    if-eq v1, v7, :cond_6

    packed-switch v1, :pswitch_data_4

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :pswitch_8
    const/16 v1, 0x23

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_6

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v2, v1}, Lantlr/ANTLRGrammarParseBehavior;->refReturnAction(Lantlr/Token;)V

    :cond_6
    :pswitch_9
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x24

    if-eq v1, v8, :cond_8

    if-eq v1, v7, :cond_8

    if-eq v1, v2, :cond_8

    const/16 v6, 0x25

    if-ne v1, v6, :cond_7

    invoke-virtual {p0}, Lantlr/ANTLRParser;->throwsSpec()V

    goto :goto_4

    :cond_7
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_8
    :goto_4
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v8, :cond_b

    if-eq v1, v7, :cond_a

    if-ne v1, v2, :cond_9

    goto :goto_5

    :cond_9
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_a
    invoke-virtual {p0}, Lantlr/ANTLRParser;->ruleOptionsSpec()V

    :cond_b
    :goto_5
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v8, :cond_d

    if-ne v1, v2, :cond_c

    goto :goto_6

    :cond_c
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_d
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v8}, Lantlr/Parser;->match(I)V

    iget-object v6, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v6, v6, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v6, :cond_e

    iget-object v6, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v6, v1}, Lantlr/ANTLRGrammarParseBehavior;->refInitAction(Lantlr/Token;)V

    :cond_e
    :goto_6
    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->block()V

    const/16 v1, 0x10

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-eq v1, v0, :cond_10

    if-eq v1, v4, :cond_10

    const/16 v2, 0x27

    if-eq v1, v2, :cond_f

    if-eq v1, v3, :cond_10

    packed-switch v1, :pswitch_data_5

    packed-switch v1, :pswitch_data_6

    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_f
    invoke-virtual {p0}, Lantlr/ANTLRParser;->exceptionGroup()V

    :cond_10
    :pswitch_a
    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_11

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-virtual {v5}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-interface {p0, v0}, Lantlr/ANTLRGrammarParseBehavior;->endRule(Ljava/lang/String;)V

    :cond_11
    return-void

    :pswitch_data_0
    .packed-switch 0x1e
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x1e
        :pswitch_3
        :pswitch_2
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x21
        :pswitch_4
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x22
        :pswitch_6
        :pswitch_7
        :pswitch_7
        :pswitch_7
    .end packed-switch

    :pswitch_data_4
    .packed-switch 0x23
        :pswitch_8
        :pswitch_9
        :pswitch_9
    .end packed-switch

    :pswitch_data_5
    .packed-switch 0x7
        :pswitch_a
        :pswitch_a
        :pswitch_a
        :pswitch_a
    .end packed-switch

    :pswitch_data_6
    .packed-switch 0x1e
        :pswitch_a
        :pswitch_a
        :pswitch_a
    .end packed-switch
.end method

.method public final ruleOptionsSpec()V
    .locals 3

    const/16 v0, 0xe

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v1, 0x29

    if-ne v0, v1, :cond_0

    goto :goto_1

    :cond_0
    const/16 v0, 0x11

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    return-void

    :cond_1
    :goto_1
    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/16 v1, 0xf

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_2

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v2, v0, v1}, Lantlr/ANTLRGrammarParseBehavior;->setRuleOption(Lantlr/Token;Lantlr/Token;)V

    :cond_2
    const/16 v0, 0x10

    goto :goto_0
.end method

.method public final rules()V
    .locals 4

    const/4 v0, 0x0

    :goto_0
    sget-object v1, Lantlr/ANTLRParser;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    sget-object v1, Lantlr/ANTLRParser;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    const/4 v3, 0x2

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Lantlr/ANTLRParser;->rule()V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    if-lt v0, v2, :cond_1

    return-void

    :cond_1
    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, v1, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0
.end method

.method public final setBlockElement()Lantlr/collections/impl/BitSet;
    .locals 8

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    const/16 v2, 0x13

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3}, Lantlr/ANTLRLexer;->tokenTypeForCharLiteral(Ljava/lang/String;)I

    move-result v3

    invoke-static {v3}, Lantlr/collections/impl/BitSet;->of(I)Lantlr/collections/impl/BitSet;

    move-result-object v4

    goto :goto_0

    :cond_0
    const/4 v4, 0x0

    const/4 v3, 0x0

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    const/16 v6, 0x10

    if-eq v5, v6, :cond_3

    const/16 v6, 0x15

    if-eq v5, v6, :cond_3

    const/16 v6, 0x16

    if-ne v5, v6, :cond_2

    invoke-virtual {p0, v6}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v5

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_3

    invoke-virtual {v5}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2}, Lantlr/ANTLRLexer;->tokenTypeForCharLiteral(Ljava/lang/String;)I

    move-result v2

    if-ge v2, v3, :cond_1

    iget-object v5, p0, Lantlr/ANTLRParser;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1}, Lantlr/Token;->getLine()I

    move-result v6

    invoke-virtual {v1}, Lantlr/Token;->getColumn()I

    move-result v1

    const-string v7, "Malformed range line "

    invoke-virtual {v5, v7, p0, v6, v1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_1
    add-int/2addr v3, v0

    :goto_1
    if-gt v3, v2, :cond_3

    invoke-virtual {v4, v3}, Lantlr/collections/impl/BitSet;->add(I)V

    add-int/lit8 v3, v3, 0x1

    goto :goto_1

    :cond_2
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1

    :cond_3
    return-object v4
.end method

.method public final subruleOptionsSpec()V
    .locals 3

    const/16 v0, 0xe

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v1, 0x29

    if-ne v0, v1, :cond_0

    goto :goto_1

    :cond_0
    const/16 v0, 0x11

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    return-void

    :cond_1
    :goto_1
    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/16 v1, 0xf

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_2

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v2, v0, v1}, Lantlr/ANTLRGrammarParseBehavior;->setSubruleOption(Lantlr/Token;Lantlr/Token;)V

    :cond_2
    const/16 v0, 0x10

    goto :goto_0
.end method

.method public final superClass()Ljava/lang/String;
    .locals 2

    const/16 v0, 0x1b

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_0

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "\""

    invoke-static {v0, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    const/4 v1, 0x6

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    const/16 v1, 0x1c

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    return-object v0
.end method

.method public final terminal(Lantlr/Token;)V
    .locals 16

    move-object/from16 v0, p0

    move-object/from16 v3, p1

    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/4 v4, 0x6

    if-eq v2, v4, :cond_7

    const/16 v6, 0x1b

    const/16 v7, 0x19

    const/16 v8, 0x27

    const/16 v9, 0x15

    const/16 v10, 0x10

    const/4 v11, 0x7

    const/16 v12, 0x18

    const/16 v13, 0x32

    const/16 v14, 0x13

    if-eq v2, v14, :cond_4

    if-eq v2, v12, :cond_1

    if-ne v2, v13, :cond_0

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v0, v13}, Lantlr/Parser;->match(I)V

    invoke-virtual/range {p0 .. p0}, Lantlr/ANTLRParser;->ast_type_spec()I

    move-result v2

    iget-object v4, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v4, v4, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v4, :cond_8

    iget-object v0, v0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v0, v1, v3, v2}, Lantlr/ANTLRGrammarParseBehavior;->refWildcard(Lantlr/Token;Lantlr/Token;I)V

    goto/16 :goto_1

    :cond_0
    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_1
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    invoke-virtual {v0, v12}, Lantlr/Parser;->match(I)V

    invoke-virtual/range {p0 .. p0}, Lantlr/ANTLRParser;->ast_type_spec()I

    move-result v15

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    if-eq v5, v4, :cond_3

    if-eq v5, v11, :cond_3

    if-eq v5, v10, :cond_3

    if-eq v5, v14, :cond_3

    if-eq v5, v9, :cond_3

    const/16 v4, 0x22

    if-eq v5, v4, :cond_2

    if-eq v5, v8, :cond_3

    if-eq v5, v13, :cond_3

    if-eq v5, v12, :cond_3

    if-eq v5, v7, :cond_3

    if-eq v5, v6, :cond_3

    const/16 v4, 0x1c

    if-eq v5, v4, :cond_3

    packed-switch v5, :pswitch_data_0

    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_2
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v0, v4}, Lantlr/Parser;->match(I)V

    iget-object v4, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v4, v4, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v4, :cond_3

    goto :goto_0

    :cond_3
    :pswitch_0
    const/4 v1, 0x0

    :goto_0
    move-object v4, v1

    iget-object v1, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_8

    iget-object v1, v0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-direct/range {p0 .. p0}, Lantlr/ANTLRParser;->lastInRule()Z

    move-result v7

    const/4 v5, 0x0

    const/4 v6, 0x0

    move-object v0, v1

    move-object v1, v5

    move-object/from16 v3, p1

    move v5, v6

    move v6, v15

    invoke-interface/range {v0 .. v7}, Lantlr/ANTLRGrammarParseBehavior;->refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V

    goto/16 :goto_1

    :cond_4
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    invoke-virtual {v0, v14}, Lantlr/Parser;->match(I)V

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    if-eq v5, v4, :cond_6

    if-eq v5, v11, :cond_6

    if-eq v5, v10, :cond_6

    if-eq v5, v14, :cond_6

    if-eq v5, v9, :cond_6

    const/16 v4, 0x21

    if-eq v5, v4, :cond_5

    if-eq v5, v8, :cond_6

    if-eq v5, v13, :cond_6

    if-eq v5, v12, :cond_6

    if-eq v5, v7, :cond_6

    if-eq v5, v6, :cond_6

    const/16 v4, 0x1c

    if-eq v5, v4, :cond_6

    packed-switch v5, :pswitch_data_1

    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-direct {v2, v1, v0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_5
    invoke-virtual {v0, v4}, Lantlr/Parser;->match(I)V

    iget-object v4, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v4, v4, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v4, :cond_6

    const/4 v1, 0x3

    :cond_6
    :pswitch_1
    move v4, v1

    iget-object v1, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_8

    iget-object v1, v0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-direct/range {p0 .. p0}, Lantlr/ANTLRParser;->lastInRule()Z

    move-result v5

    const/4 v6, 0x0

    move-object v0, v1

    move-object v1, v2

    move-object/from16 v2, p1

    move v3, v6

    invoke-interface/range {v0 .. v5}, Lantlr/ANTLRGrammarParseBehavior;->refCharLiteral(Lantlr/Token;Lantlr/Token;ZIZ)V

    goto :goto_1

    :cond_7
    invoke-virtual {v0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v0, v4}, Lantlr/Parser;->match(I)V

    invoke-virtual/range {p0 .. p0}, Lantlr/ANTLRParser;->ast_type_spec()I

    move-result v2

    iget-object v4, v0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v4, v4, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v4, :cond_8

    iget-object v4, v0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-direct/range {p0 .. p0}, Lantlr/ANTLRParser;->lastInRule()Z

    move-result v0

    invoke-interface {v4, v1, v3, v2, v0}, Lantlr/ANTLRGrammarParseBehavior;->refStringLiteral(Lantlr/Token;Lantlr/Token;IZ)V

    :cond_8
    :goto_1
    return-void

    :pswitch_data_0
    .packed-switch 0x29
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x29
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch
.end method

.method public final throwsSpec()V
    .locals 3

    const/16 v0, 0x25

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_0

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :cond_1
    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x26

    if-ne v1, v2, :cond_2

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_1

    const-string v2, ","

    invoke-static {v0, v2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-static {v1, v0}, La/a/a/a/a;->a(Lantlr/Token;Ljava/lang/StringBuilder;)Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_2
    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_3

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0, v0}, Lantlr/ANTLRGrammarParseBehavior;->setUserExceptions(Ljava/lang/String;)V

    :cond_3
    return-void
.end method

.method public final tokensSpec()V
    .locals 10

    const/16 v0, 0x17

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x0

    const/4 v1, 0x0

    move-object v2, v0

    :goto_0
    const/4 v3, 0x1

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LA(I)I

    move-result v4

    const/16 v5, 0x18

    const/4 v6, 0x6

    if-eq v4, v6, :cond_2

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LA(I)I

    move-result v4

    if-ne v4, v5, :cond_0

    goto :goto_1

    :cond_0
    if-lt v1, v3, :cond_1

    const/16 v0, 0x11

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    return-void

    :cond_1
    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, v1, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0

    :cond_2
    :goto_1
    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LA(I)I

    move-result v4

    const/16 v7, 0x19

    const/16 v8, 0x10

    if-eq v4, v6, :cond_a

    if-ne v4, v5, :cond_9

    iget-object v4, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v4, v4, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v4, :cond_3

    move-object v2, v0

    :cond_3
    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v4

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    const/16 v9, 0xf

    if-eq v5, v9, :cond_5

    if-eq v5, v8, :cond_6

    if-ne v5, v7, :cond_4

    goto :goto_2

    :cond_4
    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, v1, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0

    :cond_5
    invoke-virtual {p0, v9}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v2

    invoke-virtual {p0, v6}, Lantlr/Parser;->match(I)V

    :cond_6
    :goto_2
    iget-object v5, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v5, v5, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v5, :cond_7

    iget-object v5, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v5, v4, v2}, Lantlr/ANTLRGrammarParseBehavior;->defineToken(Lantlr/Token;Lantlr/Token;)V

    :cond_7
    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    if-eq v5, v8, :cond_d

    if-ne v5, v7, :cond_8

    goto :goto_3

    :cond_8
    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, v1, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0

    :cond_9
    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, v1, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0

    :cond_a
    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v4

    invoke-virtual {p0, v6}, Lantlr/Parser;->match(I)V

    iget-object v5, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v5, v5, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v5, :cond_b

    iget-object v5, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v5, v0, v4}, Lantlr/ANTLRGrammarParseBehavior;->defineToken(Lantlr/Token;Lantlr/Token;)V

    :cond_b
    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LA(I)I

    move-result v5

    if-eq v5, v8, :cond_d

    if-ne v5, v7, :cond_c

    :goto_3
    invoke-virtual {p0, v4}, Lantlr/ANTLRParser;->tokensSpecOptions(Lantlr/Token;)V

    goto :goto_4

    :cond_c
    new-instance v0, Lantlr/NoViableAltException;

    invoke-virtual {p0, v3}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, v1, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v0

    :cond_d
    :goto_4
    invoke-virtual {p0, v8}, Lantlr/Parser;->match(I)V

    add-int/lit8 v1, v1, 0x1

    goto/16 :goto_0
.end method

.method public final tokensSpecOptions(Lantlr/Token;)V
    .locals 4

    const/16 v0, 0x19

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/16 v1, 0xf

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v2

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    goto :goto_1

    :cond_0
    :goto_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v2, 0x10

    if-ne v0, v2, :cond_1

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v2

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    :goto_1
    iget-object v3, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v3, p1, v0, v2}, Lantlr/ANTLRGrammarParseBehavior;->refTokensSpecElementOption(Lantlr/Token;Lantlr/Token;Lantlr/Token;)V

    goto :goto_0

    :cond_1
    const/16 p1, 0x1a

    invoke-virtual {p0, p1}, Lantlr/Parser;->match(I)V

    return-void
.end method

.method public final tree()V
    .locals 4

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    const/16 v2, 0x2c

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_0

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v2, v1}, Lantlr/ANTLRGrammarParseBehavior;->beginTree(Lantlr/Token;)V

    :cond_0
    invoke-virtual {p0}, Lantlr/ANTLRParser;->rootNode()V

    iget-object v1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v1, v1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v1, :cond_1

    iget-object v1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v1}, Lantlr/ANTLRGrammarParseBehavior;->beginChildList()V

    :cond_1
    const/4 v1, 0x0

    :goto_0
    sget-object v2, Lantlr/ANTLRParser;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_2

    invoke-virtual {p0}, Lantlr/ANTLRParser;->element()V

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    if-lt v1, v0, :cond_5

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_3

    iget-object v0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v0}, Lantlr/ANTLRGrammarParseBehavior;->endChildList()V

    :cond_3
    const/16 v0, 0x1c

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_4

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0}, Lantlr/ANTLRGrammarParseBehavior;->endTree()V

    :cond_4
    return-void

    :cond_5
    new-instance v1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v1
.end method

.method public final treeParserOptionsSpec()V
    .locals 3

    const/16 v0, 0xe

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x18

    if-eq v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    const/16 v1, 0x29

    if-ne v0, v1, :cond_0

    goto :goto_1

    :cond_0
    const/16 v0, 0x11

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    return-void

    :cond_1
    :goto_1
    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/16 v1, 0xf

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->optionValue()Lantlr/Token;

    move-result-object v1

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v2, :cond_2

    iget-object v2, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {v2, v0, v1}, Lantlr/ANTLRGrammarParseBehavior;->setGrammarOption(Lantlr/Token;Lantlr/Token;)V

    :cond_2
    const/16 v0, 0x10

    goto :goto_0
.end method

.method public final treeParserSpec(Ljava/lang/String;)V
    .locals 7

    const/16 v0, 0xa

    invoke-virtual {p0, v0}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0}, Lantlr/ANTLRParser;->id()Lantlr/Token;

    move-result-object v0

    const/16 v1, 0xb

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    const/16 v1, 0xd

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/16 v3, 0x10

    if-eq v2, v3, :cond_1

    const/16 v4, 0x1b

    if-ne v2, v4, :cond_0

    invoke-virtual {p0}, Lantlr/ANTLRParser;->superClass()Ljava/lang/String;

    move-result-object v2

    goto :goto_0

    :cond_0
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_1
    const/4 v2, 0x0

    :goto_0
    iget-object v4, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v4, v4, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v4, :cond_2

    iget-object v4, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v5

    invoke-interface {v4, v5, v0, v2, p1}, Lantlr/ANTLRGrammarParseBehavior;->startTreeWalker(Ljava/lang/String;Lantlr/Token;Ljava/lang/String;Ljava/lang/String;)V

    :cond_2
    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    const/16 v0, 0x17

    const/16 v2, 0x18

    const/16 v3, 0x29

    const/16 v4, 0x8

    const/4 v5, 0x7

    if-eq p1, v5, :cond_4

    if-eq p1, v4, :cond_4

    const/16 v6, 0xe

    if-eq p1, v6, :cond_3

    if-eq p1, v3, :cond_4

    if-eq p1, v0, :cond_4

    if-eq p1, v2, :cond_4

    packed-switch p1, :pswitch_data_0

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_3
    invoke-virtual {p0}, Lantlr/ANTLRParser;->treeParserOptionsSpec()V

    :cond_4
    :pswitch_0
    iget-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p1, p1, Lantlr/ParserSharedInputState;->guessing:I

    if-nez p1, :cond_5

    iget-object p1, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p1}, Lantlr/ANTLRGrammarParseBehavior;->endOptions()V

    :cond_5
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    if-eq p1, v5, :cond_7

    if-eq p1, v4, :cond_7

    if-eq p1, v0, :cond_6

    if-eq p1, v2, :cond_7

    if-eq p1, v3, :cond_7

    packed-switch p1, :pswitch_data_1

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_6
    invoke-virtual {p0}, Lantlr/ANTLRParser;->tokensSpec()V

    :cond_7
    :pswitch_1
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result p1

    if-eq p1, v5, :cond_8

    if-eq p1, v4, :cond_9

    if-eq p1, v2, :cond_9

    if-eq p1, v3, :cond_9

    packed-switch p1, :pswitch_data_2

    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v0, p0}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1

    :cond_8
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p1

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v0, :cond_9

    iget-object p0, p0, Lantlr/ANTLRParser;->behavior:Lantlr/ANTLRGrammarParseBehavior;

    invoke-interface {p0, p1}, Lantlr/ANTLRGrammarParseBehavior;->refMemberAction(Lantlr/Token;)V

    :cond_9
    :pswitch_2
    return-void

    :pswitch_data_0
    .packed-switch 0x1e
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x1e
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x1e
        :pswitch_2
        :pswitch_2
        :pswitch_2
    .end packed-switch
.end method
