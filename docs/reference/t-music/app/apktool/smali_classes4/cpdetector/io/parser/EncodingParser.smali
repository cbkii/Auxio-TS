.class public Lcpdetector/io/parser/EncodingParser;
.super Lantlr/LLkParser;
.source ""

# interfaces
.implements Lcpdetector/io/parser/EncodingParserTokenTypes;


# static fields
.field public static final _tokenNames:[Ljava/lang/String;

.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;


# direct methods
.method public static constructor <clinit>()V
    .locals 12

    const-string v0, "<0>"

    const-string v1, "EOF"

    const-string v2, "<2>"

    const-string v3, "NULL_TREE_LOOKAHEAD"

    const-string v4, "META_CONTENT_TYPE"

    const-string v5, "XML_ENCODING_DECL"

    const-string v6, "IDENTIFIER"

    const-string v7, "SPACING"

    const-string v8, "NEWLINE"

    const-string v9, "SPACE"

    const-string v10, "DIGIT"

    const-string v11, "LETTER"

    filled-new-array/range {v0 .. v11}, [Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcpdetector/io/parser/EncodingParser;->_tokenNames:[Ljava/lang/String;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lcpdetector/io/parser/EncodingParser;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lcpdetector/io/parser/EncodingParser;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/ParserSharedInputState;)V
    .locals 1

    const/4 v0, 0x1

    invoke-direct {p0, p1, v0}, Lantlr/LLkParser;-><init>(Lantlr/ParserSharedInputState;I)V

    sget-object p1, Lcpdetector/io/parser/EncodingParser;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;)V
    .locals 1

    const/4 v0, 0x1

    invoke-direct {p0, p1, v0}, Lcpdetector/io/parser/EncodingParser;-><init>(Lantlr/TokenBuffer;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenBuffer;I)V

    sget-object p1, Lcpdetector/io/parser/EncodingParser;->_tokenNames:[Ljava/lang/String;

    iput-object p1, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;)V
    .locals 1

    const/4 v0, 0x1

    invoke-direct {p0, p1, v0}, Lcpdetector/io/parser/EncodingParser;-><init>(Lantlr/TokenStream;I)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenStream;I)V

    sget-object p1, Lcpdetector/io/parser/EncodingParser;->_tokenNames:[Ljava/lang/String;

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


# virtual methods
.method public final htmlDocument()Ljava/lang/String;
    .locals 4

    const/4 v0, 0x0

    const/4 v1, 0x1

    :try_start_0
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-eq v2, v1, :cond_2

    const/4 v3, 0x4

    if-eq v2, v3, :cond_1

    const/4 v3, 0x5

    if-ne v2, v3, :cond_0

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    :goto_0
    invoke-virtual {p0, v3}, Lantlr/Parser;->match(I)V

    goto :goto_1

    :cond_0
    new-instance v2, Lantlr/NoViableAltException;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-direct {v2, v1, v3}, Lantlr/NoViableAltException;-><init>(Lantlr/Token;Ljava/lang/String;)V

    throw v2

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    goto :goto_0

    :goto_1
    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_2

    :catch_0
    move-exception v1

    invoke-virtual {p0, v1}, Lantlr/Parser;->reportError(Lantlr/RecognitionException;)V

    invoke-virtual {p0}, Lantlr/LLkParser;->consume()V

    sget-object v1, Lcpdetector/io/parser/EncodingParser;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    :cond_2
    :goto_2
    return-object v0
.end method
