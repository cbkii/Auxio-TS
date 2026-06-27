.class public Lantlr/ANTLRTokdefParser;
.super Lantlr/LLkParser;
.source ""

# interfaces
.implements Lantlr/ANTLRTokdefParserTokenTypes;


# static fields
.field public static final _tokenNames:[Ljava/lang/String;

.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_1:Lantlr/collections/impl/BitSet;


# instance fields
.field public antlrTool:Lantlr/Tool;


# direct methods
.method public static constructor <clinit>()V
    .locals 16

    const-string v0, "<0>"

    const-string v1, "EOF"

    const-string v2, "<2>"

    const-string v3, "NULL_TREE_LOOKAHEAD"

    const-string v4, "ID"

    const-string v5, "STRING"

    const-string v6, "ASSIGN"

    const-string v7, "LPAREN"

    const-string v8, "RPAREN"

    const-string v9, "INT"

    const-string v10, "WS"

    const-string v11, "SL_COMMENT"

    const-string v12, "ML_COMMENT"

    const-string v13, "ESC"

    const-string v14, "DIGIT"

    const-string v15, "XDIGIT"

    filled-new-array/range {v0 .. v15}, [Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lantlr/ANTLRTokdefParser;->_tokenNames:[Ljava/lang/String;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRTokdefParser;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRTokdefParser;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRTokdefParser;->mk_tokenSet_1()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRTokdefParser;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/ParserSharedInputState;)V
    .locals 1

    const/4 v0, 0x3

    invoke-direct {p0, p1, v0}, Lantlr/LLkParser;-><init>(Lantlr/ParserSharedInputState;I)V

    sget-object p1, Lantlr/ANTLRTokdefParser;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;)V
    .locals 1

    const/4 v0, 0x3

    invoke-direct {p0, p1, v0}, Lantlr/ANTLRTokdefParser;-><init>(Lantlr/TokenBuffer;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenBuffer;I)V

    sget-object p1, Lantlr/ANTLRTokdefParser;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;)V
    .locals 1

    const/4 v0, 0x3

    invoke-direct {p0, p1, v0}, Lantlr/ANTLRTokdefParser;-><init>(Lantlr/TokenStream;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenStream;I)V

    sget-object p1, Lantlr/ANTLRTokdefParser;->_tokenNames:[Ljava/lang/String;

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
        0x32
        0x0
    .end array-data
.end method


# virtual methods
.method public final file(Lantlr/ImportVocabTokenManager;)V
    .locals 4

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    const/4 v1, 0x4

    invoke-virtual {p0, v1}, Lantlr/Parser;->match(I)V

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v1, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    const/4 v3, 0x5

    if-ne v2, v3, :cond_1

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/ANTLRTokdefParser;->line(Lantlr/ImportVocabTokenManager;)V
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception p1

    invoke-virtual {p0, p1}, Lantlr/ANTLRTokdefParser;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object p1, Lantlr/ANTLRTokdefParser;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :cond_1
    return-void
.end method

.method public getTool()Lantlr/Tool;
    .locals 0

    iget-object p0, p0, Lantlr/ANTLRTokdefParser;->antlrTool:Lantlr/Tool;

    return-object p0
.end method

.method public final line(Lantlr/ImportVocabTokenManager;)V
    .locals 11

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/16 v2, 0x9

    const/4 v3, 0x0

    const/4 v4, 0x6

    const/4 v5, 0x5

    if-ne v1, v5, :cond_0

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    move-object v6, v1

    move-object v1, v3

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v6, 0x3

    const/4 v7, 0x2

    const/4 v8, 0x4

    if-ne v1, v8, :cond_1

    invoke-virtual {p0, v7}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-ne v1, v4, :cond_1

    invoke-virtual {p0, v6}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-ne v1, v5, :cond_1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v8}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v6

    :goto_0
    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    move-object v10, v6

    move-object v6, v3

    move-object v3, v10

    goto :goto_1

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-ne v1, v8, :cond_2

    invoke-virtual {p0, v7}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    const/4 v9, 0x7

    if-ne v1, v9, :cond_2

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v8}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v9}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v6

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    const/16 v5, 0x8

    invoke-virtual {p0, v5}, Lantlr/Parser;->match(I)V

    goto :goto_1

    :cond_2
    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-ne v1, v8, :cond_4

    invoke-virtual {p0, v7}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-ne v1, v4, :cond_4

    invoke-virtual {p0, v6}, Lantlr/LLkParser;->LA(I)I

    move-result v1

    if-ne v1, v2, :cond_4

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0, v8}, Lantlr/Parser;->match(I)V

    move-object v6, v3

    :goto_1
    invoke-virtual {p0, v4}, Lantlr/Parser;->match(I)V

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0, v2}, Lantlr/Parser;->match(I)V

    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object v0

    if-eqz v3, :cond_3

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    invoke-virtual {p1, v2, v0}, Lantlr/ImportVocabTokenManager;->define(Ljava/lang/String;I)V

    if-eqz v1, :cond_5

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/SimpleTokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v0

    check-cast v0, Lantlr/StringLiteralSymbol;

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/StringLiteralSymbol;->setLabel(Ljava/lang/String;)V

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1, v1, v0}, Lantlr/SimpleTokenManager;->mapToTokenSymbol(Ljava/lang/String;Lantlr/TokenSymbol;)V

    goto :goto_2

    :cond_3
    if-eqz v1, :cond_5

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    invoke-virtual {p1, v2, v0}, Lantlr/ImportVocabTokenManager;->define(Ljava/lang/String;I)V

    if-eqz v6, :cond_5

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/SimpleTokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p1

    invoke-virtual {v6}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/TokenSymbol;->setParaphrase(Ljava/lang/String;)V

    goto :goto_2

    :cond_4
    new-instance p1, Lantlr/NoViableAltException;

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p1, v0, v1}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw p1
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    move-exception p1

    invoke-virtual {p0, p1}, Lantlr/ANTLRTokdefParser;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object p1, Lantlr/ANTLRTokdefParser;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :cond_5
    :goto_2
    return-void
.end method

.method public reportError(Lantlr/RecognitionException;)V
    .locals 3

    invoke-virtual {p0}, Lantlr/ANTLRTokdefParser;->getTool()Lantlr/Tool;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/ANTLRTokdefParser;->getTool()Lantlr/Tool;

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

    invoke-virtual {p0}, Lantlr/ANTLRTokdefParser;->getTool()Lantlr/Tool;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/ANTLRTokdefParser;->getTool()Lantlr/Tool;

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

    invoke-virtual {p0}, Lantlr/ANTLRTokdefParser;->getTool()Lantlr/Tool;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/ANTLRTokdefParser;->getTool()Lantlr/Tool;

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

.method public setTool(Lantlr/Tool;)V
    .locals 1

    iget-object v0, p0, Lantlr/ANTLRTokdefParser;->antlrTool:Lantlr/Tool;

    if-nez v0, :cond_0

    iput-object p1, p0, Lantlr/ANTLRTokdefParser;->antlrTool:Lantlr/Tool;

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalStateException;

    const-string p1, "antlr.Tool already registered"

    invoke-direct {p0, p1}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw p0
.end method
